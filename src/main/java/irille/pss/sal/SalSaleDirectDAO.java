package irille.pss.sal;

import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.Gl;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsPub;
import irille.gl.rp.RpStimateRecDAO;
import irille.gl.rva.RvaNoteAccount;
import irille.gl.rva.RvaNoteDeposit;
import irille.gl.rva.RvaNoteDepositLine;
import irille.gl.rva.Rva.ORaType;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstSalInvoice;
import irille.pss.sal.Sal.OInoutStatus;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IDirect;
import irille.pub.inf.IRecBack;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class SalSaleDirectDAO implements IDirect<SalSaleDirect>, IRecBack<SalSaleDirect> {
	public static final Log LOG = new Log(SalSaleDirectDAO.class);

	public static void validateAmt(SalSaleDirect mode) {
		BigDecimal sum = mode.getAmtOrd().add(mode.getAmtPay()).add(mode.getAmtRec());
		if (sum.compareTo(mode.getAmt()) != 0)
			throw LOG.err("sumErr", "[冲减订金金额 + 现付金额 + 挂账金额] 不等于 [总金额 : {0}]", mode.getAmt());
		if (mode.getAmtOrd().signum()<0 || mode.getAmtRec().signum()<0 || 
				mode.getAmtPay().signum()<0)
			throw LOG.err("sumErr", "冲减订金金额、现付金额、挂账金额不可为负数!");
	}
	
	public static class Ins extends IduInsLines<Ins, SalSaleDirect, SalSaleDirectLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			getB().stInoutStatus(Sal.OInoutStatus.INIT);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().setAmtRecBack(BigDecimal.ZERO);
			getB().setAmt(Idu.sumAmt(getLines()));
			validateAmt(getB());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, SalSaleDirect.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}

	}

	public static class Upd extends IduUpdLines<Upd, SalSaleDirect, SalSaleDirectLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			SalSaleDirect sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			PropertyUtils.copyPropertiesWithout(sale, getB(), SalSaleDirect.T.PKEY, SalSaleDirect.T.CODE,
			    SalSaleDirect.T.STATUS, SalSaleDirect.T.ORG, SalSaleDirect.T.DEPT, SalSaleDirect.T.CREATED_BY,
			    SalSaleDirect.T.CREATED_TIME, SalSaleDirect.T.AMT_COST, SalSaleDirect.T.ORD, SalSaleDirect.T.INOUT_STATUS,
			    SalSaleDirect.T.TALLY_BY, SalSaleDirect.T.TALLY_TIME, SalSaleDirect.T.CELL, SalSaleDirect.T.SHIPING,
			    SalSaleDirect.T.AMT_REC_BACK);
			sale.setAmt(Idu.sumAmt(getLines()));
			setB(sale);
			validateAmt(getB());
			updLineTid(getB(), getLines(), SalSaleDirectLine.class);
			SysShipingDAO.upd(getB(), _ship, SalSaleDirect.T.SHIPING.getFld(), getB().gtShipingMode());
		}

	}

	public static class Del extends IduDel<Del, SalSaleDirect> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			delLineTid(getB(), SalSaleDirectLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, SalSaleDirect> {

		@Override
		public void run() {
			super.run();
			SalSaleDirect sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			GsPub.insertDemandDirect(getB());
			List directLine = Idu.getLinesTid(sale, SalSaleDirectLine.class);
			if (sale.getOrd() != null)
				SalOrderDAO.addQtyOpen(sale.getOrd(), directLine);
			SalPriceProtDAO.checkPrice(getB().gtCell(),getB().gtCust(), directLine);
			//记录报表流水
			SalRptLineDAO.insBySalForm(sale, directLine);
			//待处理//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), CstSalInvoice.TB, CstOut.TB, GlDaybook.TB);
			//记账NOTE
			//借 – 客户应收账款、待收款、核销预收账款
			//贷 - 销售待处理账户
			if (sale.getAmtOrd().signum() > 0) {
				RvaNoteDeposit orderNote = SalOrderDAO.getOrdNote(sale.gtOrd(), sale.getAmtOrd());
				GlJournal jnl = orderNote.gtNote().gtJournal();
				GlNote note = GlNoteDAO.insAuto(sale, sale.getAmtOrd(), jnl, ODirect.DR, RvaNoteDepositLine.TB.getID());
				RvaNoteDepositLine notew = new RvaNoteDepositLine().init();
				notew.stNote(note);
				notew.setMainNote(orderNote.getPkey());
				notew.ins();
			}
			if (sale.getAmtPay().signum() != 0)
				RpStimateRecDAO.insByBill(sale, sale.gtCust(), sale.getAmtPay());
			if (sale.getAmtRec().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(sale.gtCell(), Sal.SubjectAlias.SAL_INCOME,
				    new AccObjs().setCustom(sale.gtCust()));
				GlNote note = GlNoteDAO.insAuto(sale, sale.getAmtRec(), jnl, ODirect.DR, RvaNoteAccount.TB.getID());
				RvaNoteAccount notew = new RvaNoteAccount().init();
				notew.stNote(note);
				notew.stType(ORaType.CUST);
				notew.setObj(sale.gtCust().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(sale.getApprTime());
				notew.ins();
			}
			SalCustGoodsDAO.updCustGoods(getB().gtOrg(), getB().gtCust(),
					directLine);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, SalSaleDirect> {

		@Override
		public void run() {
			super.run();
			SalSaleDirect sale = loadThisBeanAndLock();
			assertStatusIsTally(sale);
			sale.stStatus(STATUS_INIT);
			sale.setApprBy(null);
			sale.setApprTime(null);
			sale.upd();
			setB(sale);
			GsPub.deleteDemandDirect(getB());
			GlNoteDAO.delByBill(sale.gtLongPkey());
			// 取消报表流水
			SalRptLineDAO.delBySalForm(sale);
			FrmPendingDAO.del(getB(), CstSalInvoice.TB, CstOut.TB, GlDaybook.TB);
			if (getB().getAmtPay().signum() != 0) //取消待收款
				RpStimateRecDAO.delByBill(getB());
			if (sale.getOrd() != null)
				SalOrderDAO.subQtyOpen(sale.getOrd(), Idu.getLinesTid(sale, SalSaleDirectLine.class));
		}
	}

	@Override
	public void directCancel(SalSaleDirect model) {
		model.stInoutStatus(OInoutStatus.INIT);
		model.upd();
	}

	@Override
	public void directOk(SalSaleDirect model) {
		model.stInoutStatus(OInoutStatus.DONE);
		model.upd();
	}

	@Override
	public void updRecBack(SalSaleDirect model, BigDecimal amt) {
		model.setAmtRecBack(amt);
		model.upd();
	}

}
