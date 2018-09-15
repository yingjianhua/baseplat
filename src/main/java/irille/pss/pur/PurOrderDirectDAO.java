package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.core.sys.SysTemplatCellDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsDemandDirectDAO;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.gl.pya.Pya.OPaType;
import irille.gl.rp.RpStimatePayDAO;
import irille.pss.cst.CstIn;
import irille.pss.cst.CstPurInvoice;
import irille.pss.pur.PurOrderDAO.Msgs;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanForm;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class PurOrderDirectDAO {
	public static final Log LOG = new Log(PurOrderDirectDAO.class);

	public static class Ins extends IduInsLines<Ins, PurOrderDirect, PurOrderDirectLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			BigDecimal amt = BigDecimal.ZERO;
			for (PurOrderDirectLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			if (getB().getAmtXf().add(getB().getAmtGz())
			    .compareTo(getB().getAmt()) != 0)
				throw LOG.err("sumErr", "[现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
			Integer tmpl = SysTemplatCellDAO.getPurTmpl().getPkey();
			for (PurOrderDirectLine line : getLines())
				PurProtGoodsDAO.checkProtGoods(tmpl, getB().getSupplier(), line.getGoods());
		}

		@Override
		public void after() {
			super.after();
			GsDemandDirectDAO.doByForm((BeanForm) getB().gtOrigForm(), getB());
			insLineTid(getB(), getLines());

			SysShipingDAO.ins(getB(), _ship, PurOrderDirect.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();

		}
	}

	public static class Upd extends IduUpdLines<Upd, PurOrderDirect, PurOrderDirectLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			PurOrderDirect order = loadThisBeanAndLock();
			assertStatusIsInit(order);
			PropertyUtils.copyProperties(order, getB(), PurOrderDirect.T.SUPPLIER,
			    PurOrderDirect.T.SUPNAME, PurOrderDirect.T.AMT, PurOrderDirect.T.BUYER,
			    PurOrderDirect.T.SHIPING_MODE, PurOrderDirect.T.REM, 
			    PurOrderDirect.T.AMT_GZ, PurOrderDirect.T.AMT_XF,PurOrderDirect.T.CELL);
			setB(order);
			BigDecimal amt = BigDecimal.ZERO;
			for (PurOrderDirectLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			if (getB().getAmtXf().add(getB().getAmtGz())
			    .compareTo(getB().getAmt()) != 0)
				throw LOG.err("sumErr", "[现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
			updLineTid(getB(), getLines(), PurOrderDirectLine.class);
			SysShipingDAO.upd(getB(), _ship, PurOrderDirect.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}
	
	public static class CheckPrice extends IduUpdLines<Upd, PurOrderDirect, PurOrderDirectLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			PurOrderDirect order = loadThisBeanAndLock();

			if (order.getStatus() > Sys.OBillStatus.VERIFIED.getLine().getKey()) {
				throw LOG.err(Msgs.checkPrice, order.gtTB().getName(), order.getCode(), order.gtStatus()
				    .getLine().getName());
			}

			PropertyUtils.copyPropertiesWithout(order, getB(), PurOrderDirect.T.PKEY, PurOrderDirect.T.CODE,
			    PurOrderDirect.T.STATUS, PurOrderDirect.T.ORG, PurOrderDirect.T.DEPT, PurOrderDirect.T.CREATED_BY,
			    PurOrderDirect.T.CREATED_TIME, PurOrderDirect.T.AMT_COST, PurOrderDirect.T.SHIPING,
			    PurOrderDirect.T.CELL, PurOrderDirect.T.TALLY_BY, PurOrderDirect.T.TALLY_TIME);
			order.setRem(getB().getRem());
			order.stStatus(Sys.OBillStatus.VERIFIED);
			order.setAmt(sumAmt(getLines()));
			setB(order);
			updLineTid(getB(), getLines(), PurOrderDirectLine.class);
			SysShipingDAO.upd(getB(), _ship, PurOrderDirect.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Del extends IduDel<Del, PurOrderDirect> {

		@Override
		public void valid() {
			super.valid();
			PurOrderDirect order = loadThisBeanAndLock();
			assertStatusIsInit(order);
		}

		@Override
		public void before() {
			super.before();
			GsDemandDirectDAO.clearByForm((BeanForm) getB().gtOrigForm());
			delLineTid(getB(), PurOrderDirectLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, PurOrderDirect> {

		@Override
		public void run() {
			super.run();
			PurOrderDirect order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.VERIFIED);
			List<PurOrderDirectLine> lines = getLinesTid(order, PurOrderDirectLine.class);
			// 记录最新成交价
			for (PurOrderDirectLine line : lines) {
				PurProtGoods protGoods = PurProtGoods.chkUniqueTempCustObj(true,
				    SysTemplatCellDAO.getPurTmpl(order.gtCell()).getPkey(), order.getSupplier(), line.getGoods());
				if (protGoods == null)
					continue;
				protGoods.setPriceLast(line.getPrice());
				protGoods.setDateLast(Env.getTranBeginTime());
				protGoods.upd();
			}

			order.stStatus(STATUS.TALLY_ABLE);
			order.setApprBy(getUser().getPkey());
			order.setApprTime(Env.getTranBeginTime());
			order.upd();
			setB(order);
			// 待处理
			FrmPendingDAO.ins(getB(), CstPurInvoice.TB, CstIn.TB, GlDaybook.TB);
			// 记账NOTE TODO
			//贷 – 供应商应付账款、待付款、核销预付账款
			//借 - 采购待处理账户
			if (order.getAmtXf().signum() != 0)
				RpStimatePayDAO.insByBill(order, order.gtSupplier(), order.getAmtXf());
			if (order.getAmtGz().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(order.gtCell(), Pur.SubjectAlias.PUR_INCOME,
				    new AccObjs().setSupplier(order.gtSupplier()));
				GlNote note = GlNoteDAO.insAuto(order, order.getAmtGz(), jnl, ODirect.CR, PyaNoteAccountPayable.TB.getID());
				PyaNoteAccountPayable notew = new PyaNoteAccountPayable().init();
				notew.stNote(note);
				notew.stType(OPaType.SUPPLIER);
				notew.setObj(order.gtSupplier().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(order.getApprTime());
				notew.ins();
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurOrderDirect> {
		@Override
		public void run() {
			super.run();
			PurOrderDirect order = loadThisBeanAndLock();
			assertStatusIsTally(order);
			order.stStatus(STATUS_INIT);
			order.setApprBy(null);
			order.setApprTime(null);
			order.upd();
			setB(order);
			// 取消待处理
			FrmPendingDAO.del(getB(), CstPurInvoice.TB, CstIn.TB, GlDaybook.TB);
			GlNoteDAO.delByBill(order.gtLongPkey());
			if (getB().getAmtXf().signum() != 0) //取消待收款
				RpStimatePayDAO.delByBill(getB());
		}
	}
}
