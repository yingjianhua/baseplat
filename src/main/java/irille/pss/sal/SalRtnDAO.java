package irille.pss.sal;

import irille.core.sys.Sys;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.Gl;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.gl.rp.RpStimatePayDAO;
import irille.gl.rva.RvaNoteAccount;
import irille.gl.rva.RvaNoteDepositLine;
import irille.gl.rva.Rva.ORaType;
import irille.pss.cst.CstOutRed;
import irille.pss.cst.CstSalInvoiceRed;
import irille.pss.sal.Sal.OInoutStatus;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.inf.IRecBack;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO 最低售价未检查、财务相关无（现收、应收、核销订金）
 * 审核时检查 -- 锁库存
 * 产生出入库 -- 实际数量是否足够
 * @author whx
 * @version 创建时间：2014年12月8日 下午2:16:11
 */
public class SalRtnDAO implements IIn<SalRtn>, IRecBack<SalRtn> {
	public static final Log LOG = new Log(SalRtnDAO.class);

	public static void validateAmt(SalRtn mode) {
		BigDecimal sum = mode.getAmtPay().add(mode.getAmtRec());
		if (sum.compareTo(mode.getAmt()) != 0)
			throw LOG.err("sumErr", "[现付金额 + 挂账金额] 不等于 [总金额 : {0}]", mode.getAmt());
		if (mode.getAmtRec().signum() < 0 || mode.getAmtPay().signum() < 0)
			throw LOG.err("sumErr", "现付金额、挂账金额不可为负数!");
	}

	public static class Ins extends IduInsLines<Ins, SalRtn, SalRtnLine> {
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			System.out.println("所选仓库: "+getB().gtWarehouse().getOrg());
			System.out.println("当前机构: "+getB().getOrg());
			if (getB().getOrg().equals(getB().gtWarehouse().getOrg()) == false)
				throw LOG.err("errWs", "所选仓库与当前机构冲突!");
			getB().stInoutStatus(Sal.OInoutStatus.INIT);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().setAmtRecBack(BigDecimal.ZERO);
			getB().setAmt(Idu.sumAmt(getLines()));
			for (SalRtnLine line : getLines())
				//TODO 原价
				line.setPriceOld(BigDecimal.ZERO);
			validateAmt(getB());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Upd extends IduUpdLines<Upd, SalRtn, SalRtnLine> {

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			SalRtn sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			PropertyUtils.copyPropertiesWithout(sale, getB(), SalRtn.T.PKEY, SalRtn.T.CODE, SalRtn.T.STATUS, SalRtn.T.ORG,
			    SalRtn.T.DEPT, SalRtn.T.CREATED_BY, SalRtn.T.CREATED_TIME, SalRtn.T.AMT_COST, SalRtn.T.INOUT_STATUS,
			    SalRtn.T.TALLY_BY, SalRtn.T.TALLY_TIME, SalRtn.T.CELL, SalRtn.T.AMT_REC_BACK);
			sale.setAmt(Idu.sumAmt(getLines()));
			setB(sale);
			for (SalRtnLine line : getLines())
				line.setPriceOld(BigDecimal.ZERO);
			validateAmt(getB());
			updLineTid(getB(), getLines(), SalRtnLine.class);
		}
	}

	public static class Del extends IduDel<Del, SalRtn> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			delLineTid(getB(), SalRtnLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, SalRtn> {

		@Override
		public void run() {
			super.run();
			SalRtn sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			List rtnLines = Idu.getLinesTid(sale, SalRtnLine.class);
			GsPub.insertStimate(sale, sale.gtWarehouse(), rtnLines, OEnrouteType.QTZT, null);
			//记录报表流水
			SalRptLineDAO.insBySalForm(sale, rtnLines);
			//待处理
			//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), CstSalInvoiceRed.TB, CstOutRed.TB, GlDaybook.TB);
			//待付款登记
			if (getB().getAmtPay().signum() != 0)
				RpStimatePayDAO.insByBill(getB(), getB().gtCust(), getB().getAmtPay());
			//记账NOTE
			//借 – 客户应收账款（负数）、
			//贷 – 销售待处理账户（负数）-- BILL  待付款
			if (sale.getAmtRec().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(sale.gtCell(), Sal.SubjectAlias.SAL_INCOME,
				    new AccObjs().setCustom(sale.gtCust()));
				
				GlNote note = GlNoteDAO.insAuto(sale, sale.getAmtRec().negate(), jnl, ODirect.DR, RvaNoteAccount.TB.getID());
				RvaNoteAccount notew = new RvaNoteAccount().init();
				notew.stNote(note);
				notew.stType(ORaType.CUST);
				notew.setObj(sale.gtCust().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(sale.getApprTime());
				notew.ins();
			}
		}
	}

	/**
	 * 从状态“已出入库、已审核”，都可操作弃审
	 * @author whx
	 * @version 创建时间：2014年12月9日 上午11:29:23
	 */
	public static class Unapprove extends IduUnapprove<Unapprove, SalRtn> {

		@Override
		public void run() {
			super.run();
			SalRtn sale = loadThisBeanAndLock();
			assertStatusIsTally(sale);
			//取消出库单
			if (sale.gtInoutStatus() != OInoutStatus.INIT)
				GsPub.deleteIn(sale);
			//取消预出入库登记
			GsPub.deleteStimate(sale, sale.gtWarehouse(), Idu.getLinesTid(sale, SalRtnLine.class), OEnrouteType.QTZT);
			//取消NOTE
			GlNoteDAO.delByBill(sale.gtLongPkey());
			// 取消报表流水
			SalRptLineDAO.delBySalForm(sale);
			//取消待处理
			FrmPendingDAO.del(getB(), CstSalInvoiceRed.TB, CstOutRed.TB, GlDaybook.TB);
			if (getB().getAmtPay().signum() != 0) //取消待付款
				RpStimatePayDAO.delByBill(getB());
			sale.stStatus(STATUS_INIT);
			sale.stInoutStatus(OInoutStatus.INIT);
			sale.setApprBy(null);
			sale.setApprTime(null);
			sale.upd();
			setB(sale);
		}
	}

	public static class CrtGs extends IduOther<CrtGs, SalRtn> {
		public static Cn CN = new Cn("crtGs", "生产入库单");

		@Override
		public void run() {
			super.run();
			SalRtn sale = loadThisBeanAndLock();
			assertStatus(sale, STATUS.TALLY_ABLE, STATUS.DONE);
			sale.stInoutStatus(Sal.OInoutStatus.CRT);
			sale.upd();
			setB(sale);
			GsPub.insertIn(sale, sale.gtWarehouse(), sale.getCustName(), Sys.OShipingMode.NO.getLine().getKey(), null);
		}
	}

	public void inOk(GsIn in, SalRtn model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.CRT)
			throw LOG.err("outOkErr", "退货单[{0}]的入库状态错误!", model.getCode());
		model.stInoutStatus(OInoutStatus.DONE);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalRtnLine.class), OEnrouteType.QTZT);
	}

	@Override
	public void inCancel(GsIn in, SalRtn model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.DONE)
			throw LOG.err("outOkErr", "退货单[{0}]的入库状态错误!", model.getCode());
		model.stInoutStatus(OInoutStatus.CRT);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalRtnLine.class), OEnrouteType.QTZT, null);
	}

	@Override
	public List<IGoods> getInLines(SalRtn model, int idx, int count) {
		return Idu.getLinesTid(model, SalRtnLine.class, idx, count);
	}

	@Override
	public int getInLinesCount(SalRtn model) {
		return Idu.getLinesTidCount(model, SalRtnLine.class);
	}

	@Override
	public void updRecBack(SalRtn model, BigDecimal amt) {
		model.setAmtRecBack(amt.negate());
		model.upd();
	}
}
