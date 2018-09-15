package irille.pss.sal;

import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.gl.rp.RpStimateRecDAO;
import irille.gl.rva.RvaNoteAccount;
import irille.gl.rva.RvaNoteDeposit;
import irille.gl.rva.RvaNoteDepositLine;
import irille.gl.rva.Rva.ORaType;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstSalInvoice;
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
import irille.pub.inf.IOut;
import irille.pub.inf.IRecBack;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

/**
 * TODO 最低售价未检查、财务相关无（现收、应收、核销订金） 审核时检查 -- 锁库存 产生出入库 -- 实际数量是否足够
 * 
 * @author whx
 * @version 创建时间：2014年12月8日 下午2:16:11
 */
public class SalSaleDAO implements IOut<SalSale>, IRecBack<SalSale> {
	public static final Log LOG = new Log(SalSaleDAO.class);

	public static void validateAmt(SalSale mode) {
		BigDecimal sum = mode.getAmtOrd().add(mode.getAmtPay())
				.add(mode.getAmtRec());
		if (sum.compareTo(mode.getAmt()) != 0)
			throw LOG.err("sumErr", "[冲减订金金额 + 现付金额 + 挂账金额] 不等于 [总金额 : {0}]",
					mode.getAmt());
		if (mode.getAmtOrd().signum() < 0 || mode.getAmtRec().signum() < 0
				|| mode.getAmtPay().signum() < 0)
			throw LOG.err("sumErr", "冲减订金金额、现付金额、挂账金额不可为负数!");
	}

	public static class Ins extends IduInsLines<Ins, SalSale, SalSaleLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			if (getB().getOrg().equals(getB().gtWarehouse().getOrg()) == false)
				throw LOG.err("errWs", "所选仓库与当前机构冲突!");
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
			SysShipingDAO.ins(getB(), _ship, SalSale.T.SHIPING.getFld(), getB()
					.gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}

	}

	public static class Upd extends IduUpdLines<Upd, SalSale, SalSaleLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			SalSale sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			if (sale.getOrg().equals(getB().gtWarehouse().getOrg()) == false)
				throw LOG.err("errWs", "所选仓库与单据原始机构冲突!");
			PropertyUtils.copyPropertiesWithout(sale, getB(), SalSale.T.PKEY,
					SalSale.T.CODE, SalSale.T.STATUS, SalSale.T.ORG,
					SalSale.T.DEPT, SalSale.T.CREATED_BY,
					SalSale.T.CREATED_TIME, SalSale.T.AMT_COST, SalSale.T.ORD,
					SalSale.T.INOUT_STATUS, SalSale.T.TALLY_BY,
					SalSale.T.TALLY_TIME, SalSale.T.CELL,
					SalSale.T.AMT_REC_BACK, SalSale.T.SHIPING);
			sale.setAmt(Idu.sumAmt(getLines()));
			setB(sale);
			validateAmt(getB());
			updLineTid(getB(), getLines(), SalSaleLine.class);
			SysShipingDAO.upd(getB(), _ship, SalSale.T.SHIPING.getFld(), getB()
					.gtShipingMode());
		}

	}

	public static class Del extends IduDel<Del, SalSale> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			delLineTid(getB(), SalSaleLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, SalSale> {

		@Override
		public void run() {
			super.run();
			SalSale sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			List salLines = Idu.getLinesTid(getB(), SalSaleLine.class);
			SalPriceProtDAO.checkPrice(getB().gtCell(), getB().gtCust(),
					salLines);
			GsPub.insertStimate(sale, sale.gtWarehouse(), salLines,
					OEnrouteType.DFH, null);
			if (sale.getOrd() != null)
				SalOrderDAO.addQtyOpen(sale.getOrd(), salLines);
			// 记录报表流水
			SalRptLineDAO.insBySalForm(sale, salLines);
			// 待处理
			FrmPendingDAO.ins(getB(), CstSalInvoice.TB, CstOut.TB, GlDaybook.TB);
			
			// 待收款登记
			if (getB().getAmtPay().signum() != 0)
				RpStimateRecDAO.insByBill(getB(), getB().gtCust(), getB()
						.getAmtPay());
			// 记账NOTE
			// 借 – 客户应收账款、待收款、核销预收账款--定金
			// 贷 - 销售待处理账户 - BILL
			if (sale.getAmtOrd().signum() > 0) {
				RvaNoteDeposit orderNote = SalOrderDAO.getOrdNote(sale.gtOrd(),
						sale.getAmtOrd());
				GlJournal jnl = orderNote.gtNote().gtJournal();
				GlNote note = GlNoteDAO.insAuto(sale, sale.getAmtOrd(), jnl,
						ODirect.DR, RvaNoteDepositLine.TB.getID());
				RvaNoteDepositLine notew = new RvaNoteDepositLine().init();
				notew.stNote(note);
				notew.setMainNote(orderNote.getPkey());
				notew.ins();
			}
			if (sale.getAmtRec().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(sale.gtCell(),
						Sal.SubjectAlias.SAL_INCOME,
						new AccObjs().setCustom(sale.gtCust()));
				GlNote note = GlNoteDAO.insAuto(sale, sale.getAmtRec(), jnl,
						ODirect.DR, RvaNoteAccount.TB.getID());
				RvaNoteAccount notew = new RvaNoteAccount().init();
				notew.stNote(note);
				notew.stType(ORaType.CUST);
				notew.setObj(sale.gtCust().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(sale.getApprTime());
				notew.ins();
			}
			SalCustGoodsDAO.updCustGoods(getB().gtOrg(), getB().gtCust(),
					salLines);
		}
	}

	/**
	 * @author whx
	 * @version 创建时间：2014年12月9日 上午11:29:23
	 */
	public static class Unapprove extends IduUnapprove<Unapprove, SalSale> {

		@Override
		public void run() {
			super.run();
			SalSale sale = loadThisBeanAndLock();
			assertStatusIsTally(sale);
			// 取消出库单
			if (sale.gtInoutStatus() != OInoutStatus.INIT)
				GsPub.deleteOut(sale);
			// 取消预出入库登记
			GsPub.deleteStimate(sale, sale.gtWarehouse(),
					Idu.getLinesTid(sale, SalSaleLine.class), OEnrouteType.DFH);
			// 取消订单的开单数量
			if (sale.getOrd() != null)
				SalOrderDAO.subQtyOpen(sale.getOrd(),
						Idu.getLinesTid(sale, SalSaleLine.class));
			// 取消NOTE
			GlNoteDAO.delByBill(sale.gtLongPkey());
			// 取消报表流水
			SalRptLineDAO.delBySalForm(sale);
			// 取消待处理
			FrmPendingDAO.del(getB(), CstSalInvoice.TB, CstOut.TB, GlDaybook.TB);
			if (getB().getAmtPay().signum() != 0) // 取消待收款
				RpStimateRecDAO.delByBill(getB());
			sale.stStatus(STATUS_INIT);
			sale.stInoutStatus(OInoutStatus.INIT);
			sale.setApprBy(null);
			sale.setApprTime(null);
			sale.upd();
			setB(sale);
		}
	}

	public static class CrtGs extends IduOther<CrtGs, SalSale> {
		public static Cn CN = new Cn("crtGs", "生产出库单");

		@Override
		public void run() {
			super.run();
			SalSale sale = loadThisBeanAndLock();
			assertStatus(sale, STATUS.TALLY_ABLE, STATUS.DONE);
			if (sale.gtInoutStatus() != OInoutStatus.INIT)
				throw LOG.err("inout", "销售单[{0}]已产生出库单!", sale.getCode());
			sale.stInoutStatus(OInoutStatus.CRT);
			sale.upd();
			setB(sale);
			List lines = Idu.getLinesTid(sale, SalSaleLine.class);
			GsPub.checkQtyFact(sale.gtWarehouse(), lines);
			GsPub.insertOut(sale, sale.gtWarehouse(), sale.getCustName(),
					sale.getShipingMode(), sale.getShiping());
		}
	}

	// TODO 出库需要确认收款单、应收单、核销单已审核 退货同理
	@Override
	public void outOk(GsOut out, SalSale model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.CRT)
			throw LOG.err("outOkErr", "销售单[{0}]的出库状态错误!", model.getCode());
		// model.setTimeShip(out.getOutTime()); TODO 仓库那里设置
		model.stInoutStatus(OInoutStatus.DONE);
		model.setShiping(out.getShiping());
		model.setShipingMode(out.getShipingMode());
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(),
				Idu.getLinesTid(model, SalSaleLine.class), OEnrouteType.DFH);
	}

	@Override
	public void outCancel(GsOut out, SalSale model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.DONE)
			throw LOG.err("outOkErr", "销售单[{0}]的出库状态错误!", model.getCode());
		model.stInoutStatus(OInoutStatus.CRT);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(),
				Idu.getLinesTid(model, SalSaleLine.class), OEnrouteType.DFH,
				null);
	}

	@Override
	public List<IGoods> getOutLines(SalSale model, int idx, int count) {
		return Idu.getLinesTid(model, SalSaleLine.class, idx, count);
	}

	@Override
	public int getOutLinesCount(SalSale model) {
		return Idu.getLinesTidCount(model, SalSaleLine.class);
	}

	public void updRecBack(SalSale model, BigDecimal amt) {
		model.setAmtRecBack(amt);
		model.upd();
	}

}
