package irille.pss.sal;

import irille.core.sys.Sys;
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
import irille.gl.rp.RpStimateRecDAO;
import irille.gl.rva.Rva;
import irille.gl.rva.RvaNoteDeposit;
import irille.gl.rva.RvaNoteDepositLine;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstSalInvoice;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.bean.IGoodsPrice;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class SalOrderDAO {
	public static final Log LOG = new Log(SalOrderDAO.class);

	/**
	 * 由销售单或直销单审核时调用
	 * 更改订单的开单数量、当完全开单时状态更改为关闭
	 * @param orderPkey
	 */
	public static void addQtyOpen(Long orderPkey, List<IGoodsPrice> lines) {
		SalOrder order = BeanBase.loadAndLock(SalOrder.class, orderPkey);
		Idu.assertStatusOther(order, Sys.OBillStatus.TALLY_ABLE, Sys.OBillStatus.DONE);
		List<SalOrderLine> orderLines = Idu.getLinesTid(order, SalOrderLine.class);
		for (IGoodsPrice line : lines) {
			BigDecimal qty = line.getQty();
			for (SalOrderLine ordline : orderLines) {
				if (ordline.getGoods().equals(line.getGoods()) == false)
					continue;
				if (ordline.getPrice().equals(line.getPrice()) == false)
					continue;
				//订单行数量多的情况
				if (ordline.getQty().subtract(ordline.getQtyOpen()).subtract(qty).signum() >= 0) {
					ordline.setQtyOpen(ordline.getQtyOpen().add(qty));
					qty = BigDecimal.ZERO;
					break;
				} else {
					//本条订单行数量不够
					qty = qty.add(ordline.getQtyOpen()).subtract(ordline.getQty());
					ordline.setQtyOpen(ordline.getQty());
				}
			}
			if (qty.signum() > 0)
				throw LOG.err("qtySignum", "货物[{0}-{1}]的开单数量超过了预定的数量", line.gtGoods().getCode(), line.gtGoods().getName());
		}
		boolean openAll = true;
		for (SalOrderLine ordline : orderLines) {
			if (ordline.getQty().compareTo(ordline.getQtyOpen()) != 0)
				openAll = false;
			ordline.upd();
		}
		if (openAll) {
			Close act = new Close();
			act.setB(order);
			act.commit();
		}
	}

	/**
	 * 由销售单或直销单弃审时调用
	 * 不检查订单状态，执行后，订单状态如可关闭的更改为审核完成
	 * @param orderPkey
	 * @param lines
	 */
	public static void subQtyOpen(Long orderPkey, List<IGoods> lines) {
		SalOrder order = BeanBase.loadAndLock(SalOrder.class, orderPkey);
		List<SalOrderLine> orderLines = Idu.getLinesTid(order, SalOrderLine.class);
		for (IGoods line : lines) {
			BigDecimal qty = line.getQty();
			for (SalOrderLine ordline : orderLines) {
				if (ordline.getGoods().equals(line.getGoods()) == false)
					continue;
				//订单行数量多的情况
				if (ordline.getQtyOpen().compareTo(qty) >= 0) {
					ordline.setQtyOpen(ordline.getQtyOpen().subtract(qty));
					qty = BigDecimal.ZERO;
					break;
				} else {
					//本条订单行数量不够
					qty = qty.subtract(ordline.getQtyOpen());
					ordline.setQtyOpen(BigDecimal.ZERO);
				}
			}
			if (qty.signum() > 0)
				throw LOG.err("qtySignum", "货物[{0}-{1}]的开单数量异常", line.gtGoods().getCode(), line.gtGoods().getName());
		}
		for (SalOrderLine ordline : orderLines)
			ordline.upd();
		if (order.gtOrdStatus() != Sal.OOrderStatus.INIT) {
			order.stOrdStatus(Sal.OOrderStatus.INIT);
			order.upd();
		}
	}

	public static String WHERE_SALE_LINES = Idu.sqlString("{0}=?", SalSale.T.ORD);
	public static String WHERE_DIRECT_LINES = Idu.sqlString("{0}=?", SalSaleDirect.T.ORD);

	/**
	 * 校验开单时，冲减定金金额是否合理
	 * 并返回订单的预收账款凭条
	 * @param order
	 * @param amt
	 */
	public static RvaNoteDeposit getOrdNote(SalOrder order, BigDecimal amt) {
		if (order == null)
			throw LOG.err("noOrder", "不存在对应的销售订单，不可输入冲减定金金额!");
		String where = Idu.sqlString("{0}=? and {1}=?", GlNote.T.BILL, GlNote.T.EXT_TABLE);
		List<GlNote> list = BeanBase.list(GlNote.class, where, false, order.gtLongPkey(), RvaNoteDeposit.TB.getID());
		if (list.size() == 0)
			throw LOG.err("noNote", "销售订单的预收账款不存在!");
		GlNote note = list.get(0);
		RvaNoteDeposit ext = BeanBase.load(RvaNoteDeposit.class, note.getPkey());
		if (ext.getBalance().compareTo(amt) < 0)
			throw LOG.err("noBalance", "销售订单的预收账款余额[{0}]不足!", ext.getBalance());
		return ext;
	}

	public static void initOpenQty(List<SalOrderLine> list) {
		for (SalOrderLine line : list)
			line.setQtyOpen(BigDecimal.ZERO);
	}

	public static class Ins extends IduInsLines<Ins, SalOrder, SalOrderLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getB().getEarnest().signum() == -1) {
				throw LOG.err("noLines", "订金不能为负数");
			}
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			getB().stOrdStatus(Sal.OOrderStatus.INIT);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().setAmt(sumAmt(getLines()));
			initOpenQty(getLines());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, SalOrder.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}

	}

	public static class Upd extends IduUpdLines<Upd, SalOrder, SalOrderLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getB().getEarnest().signum() == -1) {
				throw LOG.err("noLines", "订金不能为负数");
			}
			Idu.checkFormGoodsPrice(getLines());
			SalOrder order = loadThisBeanAndLock();
			assertStatusIsInit(order);
			PropertyUtils.copyPropertiesWithout(order, getB(), SalOrder.T.PKEY, SalOrder.T.CODE, SalOrder.T.STATUS,
			    SalOrder.T.ORG, SalOrder.T.DEPT, SalOrder.T.CREATED_BY, SalOrder.T.CREATED_TIME, SalOrder.T.AMT,
			    SalOrder.T.AMT_COST, SalOrder.T.ORD_STATUS, SalOrder.T.TALLY_BY, SalOrder.T.TALLY_TIME, SalOrder.T.CELL,
			    SalOrder.T.SHIPING);
			order.setAmt(sumAmt(getLines()));
			initOpenQty(getLines());
			setB(order);
			updLineTid(getB(), getLines(), SalOrderLine.class);
			SysShipingDAO.upd(getB(), _ship, SalOrder.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Del extends IduDel<Del, SalOrder> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			delLineTid(getB(), SalOrderLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, SalOrder> {

		@Override
		public void run() {
			super.run();
			SalOrder order = loadThisBeanAndLock();
			assertStatusIsInit(order);
			order.stStatus(STATUS.TALLY_ABLE);
			order.setApprBy(getUser().getPkey());
			order.setApprTime(Env.INST.getWorkDate());
			order.upd();
			setB(order);
			SalPriceProtDAO.checkPrice(getB().gtCell(),getB().gtCust(), Idu.getLinesTid(getB(), SalOrderLine.class));
			//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
			//订金产生对应的NOTE记录
			//		贷 - 预收账款--定金
			//		借 – 现金
			if (order.getEarnest().signum() > 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(order.gtCell(), Sal.SubjectAlias.SAL_PAY_CUST,
				    new AccObjs().setCustom(order.gtCust()));
				GlNote note = GlNoteDAO.insAuto(order, order.getEarnest(), jnl, ODirect.CR, RvaNoteDeposit.TB.getID());
				RvaNoteDeposit notew = new RvaNoteDeposit().init();
				notew.stNote(note);
				notew.setBalance(note.getAmt());
				notew.setObj(order.gtCust().gtLongPkey());
				notew.setDateStart(order.getApprTime());
				notew.stType(Rva.ORdType.CUST);
				notew.ins();
				RpStimateRecDAO.insByBill(order, order.gtCust(), order.getEarnest());
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, SalOrder> {

		@Override
		public void run() {
			super.run();
			SalOrder order = loadThisBeanAndLock();
			assertStatusIsTally(order);
			List<SalOrderLine> lines = Idu.getLinesTid(order, SalOrderLine.class);
			for (SalOrderLine line : lines)
				if (line.getQtyOpen().compareTo(BigDecimal.ZERO) == 1)
					throw LOG.err("QtyOpen", "销售订单[" + order.getCode() + "]已产生销售单，不可弃审！");
			order.stStatus(STATUS_INIT);
			order.stOrdStatus(Sal.OOrderStatus.INIT);
			order.setApprBy(null);
			order.setApprTime(null);
			order.upd();
			setB(order);
		//产生流水账的待处理登记
			FrmPendingDAO.del(getB(), GlDaybook.TB);
			GlNoteDAO.delByBill(order.gtLongPkey());
			if (order.getEarnest().signum() > 0) 
				RpStimateRecDAO.delByBill(getB());
		}
	}

	public static class Close extends IduOther<Close, SalOrder> {

		@Override
		public void run() {
			super.run();
			SalOrder order = loadThisBeanAndLock();
			assertStatus(order, STATUS.TALLY_ABLE, STATUS.DONE);
			order.stOrdStatus(Sal.OOrderStatus.CLOSE);
			order.upd();
			setB(order);
		}

	}
}
