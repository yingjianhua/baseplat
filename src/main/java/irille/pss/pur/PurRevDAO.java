package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsPub;
import irille.gl.gs.GsStockDAO;
import irille.gl.gs.GsStockStimateDAO;
import irille.gl.gs.Gs.OEnrouteType;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.gl.pya.PyaNoteDepositPayable;
import irille.gl.pya.PyaNoteDepositPayableLine;
import irille.gl.pya.Pya.OPaType;
import irille.gl.rp.RpStimatePayDAO;
import irille.pss.cst.CstIn;
import irille.pss.cst.CstPurInvoice;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.CmbGoodsPrice;
import irille.pub.bean.IGoods;
import irille.pub.bean.IGoodsPrice;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PurRevDAO implements IIn<PurRev> {
	public static final Log LOG = new Log(PurRevDAO.class);

	public static class Ins extends IduInsLines<Ins, PurRev, PurRevLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getB().getAmtXf().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[现付订金金额]不能为负数！");
			if (getB().getAmtGz().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[挂账订金金额]不能为负数！");
			if (getB().getAmtDj().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[冲减订金金额]不能为负数！");
			for (PurRevLine line : getLines()) {
				if (line.getReceivedQty().compareTo(BigDecimal.ZERO) == -1)
					throw LOG.err("notPlus", "实收数量不能为负数！");
			}
			checkFormGoods(getLines());
			initBill(getB());
			getB().setAmtCost(BigDecimal.ZERO);
			//修改采购订单状态
			PurOrderDAO.addOpenQty(getB().gtOrd(), getLines());

			BigDecimal amt = BigDecimal.ZERO;
			for (PurRevLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getReceivedQty())
				    .setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			if (getB().getAmtXf().add(getB().getAmtDj()).add(getB().getAmtGz()).compareTo(amt) != 0)
				throw LOG.err("sumErr", "[冲减订金金额 + 现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, PurRev.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();

		}
	}

	public static class Upd extends IduUpdLines<Upd, PurRev, PurRevLine> {

		public SysShiping _ship;

		public void before() {
			super.before();
			if (getB().getAmtXf().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[现付订金金额]不能为负数！");
			if (getB().getAmtGz().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[挂账订金金额]不能为负数！");
			if (getB().getAmtDj().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("negativeErr", "[冲减订金金额]不能为负数！");
			for (PurRevLine line : getLines()) {
				if (line.getReceivedQty().compareTo(BigDecimal.ZERO) == -1)
					throw LOG.err("notPlus", "实收数量不能为负数！");
			}
			checkFormGoods(getLines());
			PurRev rev = loadThisBeanAndLock();
			PropertyUtils.copyProperties(rev, getB(), PurRev.T.SETTLE_FLAG, PurRev.T.BILL_FLAG, PurRev.T.REM,
			    PurRev.T.AMT_DJ, PurRev.T.AMT_GZ, PurRev.T.AMT_XF, PurRev.T.AMT);
			setB(rev);
			if (getB().getAmtXf().add(getB().getAmtDj()).add(getB().getAmtGz()).compareTo(getB().getAmt()) != 0)
				throw LOG.err("sumErr", "[冲减订金金额 + 现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
			PurOrderDAO.subOpenQty(getB().gtOrd(), getLinesTid(getB(), PurRevLine.class));
			BigDecimal amt = BigDecimal.ZERO;
			for (PurRevLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getReceivedQty())
				    .setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);

			updLineTid(getB(), getLines(), PurRevLine.class);
			PurOrderDAO.addOpenQty(getB().gtOrd(), getLines());
			SysShipingDAO.upd(getB(), _ship, PurRev.T.SHIPING.getFld(), getB().gtShipingMode());
		}

	}

	public static class Del extends IduDel<Del, PurRev> {

		@Override
		public void valid() {
			super.valid();
			PurRev order = loadThisBeanAndLock();
			assertStatusIsInit(order);
		}

		@Override
		public void before() {
			super.before();
			PurOrderDAO.subOpenQty(getB().gtOrd(), getLinesTid(getB(), PurRevLine.class));
			delLineTid(getB(), PurRevLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, PurRev> {

		@Override
		public void run() {
			super.run();
			PurRev rev = loadThisBeanAndLock();
			assertStatusIsInit(rev);
			rev.stStatus(Sys.OBillStatus.TALLY_ABLE);
			rev.setApprBy(getUser().getPkey());
			rev.setApprTime(Env.getTranBeginTime());
			rev.upd();
			setB(rev);
			GsPub.insertIn(rev, rev.gtWarehouse(), rev.getSupname(), rev.getShipingMode(), rev.getShiping());
			// 待处理
			FrmPendingDAO.ins(getB(), CstPurInvoice.TB, CstIn.TB, GlDaybook.TB);
			// 记账NOTE TODO
			// 记账NOTE
			//贷 – 供应商应付账款、待付款处理登记、核销预付账款--定金
			//借 - 采购待处理账户 - BILL
			if (getB().getAmtXf().signum() != 0)
				RpStimatePayDAO.insByBill(getB(), getB().gtSupplier(), getB().getAmtXf());

			if (rev.getAmtDj().signum() > 0) {
				PyaNoteDepositPayable orderNote = PurOrderDAO.getOrdNote(rev.gtOrd(), rev.getAmtDj());
				GlJournal jnl = orderNote.gtNote().gtJournal();
				GlNote note = GlNoteDAO.insAuto(rev, rev.getAmtDj(), jnl, ODirect.CR, PyaNoteDepositPayableLine.TB.getID());
				PyaNoteDepositPayableLine notew = new PyaNoteDepositPayableLine().init();
				notew.stNote(note);
				notew.setMainNote(orderNote.getPkey());
				notew.ins();
			}
			if (rev.getAmtGz().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(rev.gtCell(), Pur.SubjectAlias.PUR_INCOME,
				    new AccObjs().setSupplier(rev.gtSupplier()));
				GlNote note = GlNoteDAO.insAuto(rev, rev.getAmtGz(), jnl, ODirect.CR, PyaNoteAccountPayable.TB.getID());
				PyaNoteAccountPayable notew = new PyaNoteAccountPayable().init();
				notew.stNote(note);
				notew.stType(OPaType.SUPPLIER);
				notew.setObj(rev.gtSupplier().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(rev.getApprTime());
				notew.ins();
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurRev> {

		@Override
		public void run() {
			super.run();
			PurRev rev = loadThisBeanAndLock();
			assertStatusIsTally(rev);
			//assertStatusIsCheck(rev);
			rev.stStatus(Sys.OBillStatus.INIT);
			rev.setApprBy(null);
			rev.setApprTime(null);
			rev.upd();
			setB(rev);
			GsPub.deleteIn(rev);
			// 取消待处理
			FrmPendingDAO.del(getB(), CstPurInvoice.TB, CstIn.TB, GlDaybook.TB);
			//取消NOTE
			GlNoteDAO.delByBill(rev.gtLongPkey());
			if (rev.getAmtXf().signum() != 0) //取消待收款
				RpStimatePayDAO.delByBill(getB());
		}
	}

	@Override
	public void inOk(GsIn in, PurRev model) {
		// TODO Auto-generated method stub
		PurOrder order = model.gtOrd();
		GsStockStimateDAO.updateByPurOrder(order, Idu.getLinesTid(order, PurOrderLine.class));
		GsStockDAO.updateStQtyByPurRev(model.gtWarehouse(), Idu.getLinesTid(model, PurRevLine.class), OEnrouteType.CGZT,
		    false);
		//		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurRevLine.class),
		//		    OEnrouteType.DFH);
		model.setShiping(in.getShiping());
		model.setShipingMode(in.getShipingMode());
		model.upd();
	}

	@Override
	public void inCancel(GsIn in, PurRev model) {
		// TODO Auto-generated method stub
		PurOrder order = model.gtOrd();
		//		GsStockStimateDAO.updateByPurOrder(order, Idu.getLinesTid(order, PurOrderLine.class));
		GsStockStimateDAO.cancelByPurOrderAndPurRevLine(order, Idu.getLinesTid(model, PurRevLine.class));
		GsStockDAO.updateStQtyByPurRev(model.gtWarehouse(), Idu.getLinesTid(model, PurRevLine.class), OEnrouteType.CGZT,
		    true);
		//		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurRevLine.class),
		//		    OEnrouteType.DFH, null);
	}

	@Override
	public List<IGoods> getInLines(PurRev model, int idx, int count) {
		List<PurRevLine> list = Idu.getLinesTid(model, PurRevLine.class, idx, count);
		List<IGoods> newList = new ArrayList<IGoods>();
		CmbGoodsPrice goods;
		for (PurRevLine line : list) {
			goods = new CmbGoodsPrice();
			goods.stGoods(line.gtGoods());
			goods.stUom(line.gtUom());
			goods.setQty(line.getReceivedQty());
			goods.setAmt(line.getAmt());
			goods.setPrice(line.getPrice());
			goods.setPkey(line.getPkey());
			newList.add(goods);
		}
		return newList;
	}

	@Override
	public int getInLinesCount(PurRev model) {
		return Idu.getLinesTidCount(model, PurRevLine.class);
	}

}
