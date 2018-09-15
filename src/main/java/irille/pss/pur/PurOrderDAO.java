package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
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
import irille.gl.gs.Gs;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsDemandDAO;
import irille.gl.gs.GsPub;
import irille.gl.gs.GsRequest;
import irille.gl.gs.GsRequestLine;
import irille.gl.gs.GsStockStimateDAO;
import irille.gl.pya.Pya;
import irille.gl.pya.PyaNoteDepositPayable;
import irille.gl.rp.RpStimatePayDAO;
import irille.pss.pur.Pur.OOrderStatus;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanGoods;
import irille.pub.bean.IGoods;
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
import java.util.ArrayList;
import java.util.List;

public class PurOrderDAO {
	public static final Log LOG = new Log(PurOrderDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		qtyOpen("采购订单[{0}]已产生收货单，不可弃审！"),
		delStatus("{0}[{1}]的状态为[{2}]，不可删除！"),
		checkPrice("{0}[{1}]的状态为[{2}]，不可再核价！"),
		updStatus("{0}[{1}]的状态为[{2}]，不可再修改！"),
		noLines("货物信息不可为空!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on
	
	
	/**
	 * 校验开单时，冲减定金金额是否合理
	 * 并返回订单的预付账款凭条
	 * @param order
	 * @param amt
	 */
	public static PyaNoteDepositPayable getOrdNote(PurOrder order, BigDecimal amt) {
		if (order == null) 
			throw LOG.err("noOrder", "不存在对应的采购订单，不可输入冲减定金金额!");
		String where = Idu.sqlString("{0}=? and {1}=?", GlNote.T.BILL, GlNote.T.EXT_TABLE);
		List<GlNote> list = BeanBase.list(GlNote.class, where, false, order.gtLongPkey(), PyaNoteDepositPayable.TB.getID());
		if (list.size() == 0)
			throw LOG.err("noNote", "采购订单的预付账款不存在!");
		GlNote note = list.get(0);
		PyaNoteDepositPayable ext = BeanBase.load(PyaNoteDepositPayable.class, note.getPkey());
		if (ext.getBalance().compareTo(amt) < 0)
			throw LOG.err("noBalance", "采购订单的预付账款余额[{0}]不足!", ext.getBalance());
		return ext;
	}
	
	public static class Ins extends IduInsLines<Ins, PurOrder, PurOrderLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err(Msgs.noLines);
			getB().setCode(SysSeqDAO.getSeqnumForm(PurOrder.TB));
			getB().stStatus(STATUS_INIT);
			getB().setAmt(BigDecimal.ZERO);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().stCell(getCell());
			getB().setOrg(getUser().getOrg());
			getB().setDept(getUser().getDept());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
			getB().stOrdStatus(OOrderStatus.INIT);
			// 检查供应商协议
			PurProtDAO.checkSupplier(SysTemplatCellDAO.getPurTmpl().getPkey(), getB().getSupplier());
		}

		@Override
		public void after() {
			super.after();
			List<PurOrderLine> mergeLines = new ArrayList<PurOrderLine>();
			for (PurOrderLine line : getLines()) {
				GsDemandDAO.createdByPo(line.getPkey(), getB(), getB().getCode());
				line.setPrice(BigDecimal.ZERO);
				line.setAmt(BigDecimal.ZERO);
				line.setCostPur(BigDecimal.ZERO);
				line.setQtyOpen(BigDecimal.ZERO);
				mergeLines.add(line);
			}
			//合并明细
			for (PurOrderLine line : getLines()) {
				int i = 0;
				List<PurOrderLine> temps = new ArrayList<PurOrderLine>();
				for (PurOrderLine mline : mergeLines) {
					if (mline.getGoods().equals(line.getGoods()) && mline.getUom().equals(line.getUom())) {
						if (i > 0)
							temps.add(mline);
						i++;
					}
				}
				for (PurOrderLine temp : temps)
					mergeLines.remove(temp);
			}
			for (PurOrderLine mline : mergeLines) {
				BigDecimal tmpQty = BigDecimal.ZERO;
				for (PurOrderLine line : getLines()) {
					if (mline.getGoods().equals(line.getGoods()) && mline.getUom().equals(line.getUom()))
						tmpQty = tmpQty.add(line.getQty());
				}
				mline.setQty(tmpQty);
			}
			insLineTid(getB(), mergeLines);

			SysShipingDAO.ins(getB(), _ship, PurOrder.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();

		}
	}

	public static class Upd extends IduUpdLines<Upd, PurOrder, PurOrderLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err(Msgs.noLines);
			PurOrder order = loadThisBeanAndLock();
			if (order.getStatus() > Sys.OBillStatus.VERIFIED.getLine().getKey()) {
				throw LOG.err(Msgs.updStatus, order.gtTB().getName(), order.getCode(), order.gtStatus()
				    .getLine().getName());
			}

			PropertyUtils.copyPropertiesWithout(order, getB(), PurOrder.T.PKEY, PurOrder.T.CODE,
			    PurOrder.T.STATUS, PurOrder.T.ORG, PurOrder.T.DEPT, PurOrder.T.CREATED_BY,
			    PurOrder.T.CREATED_TIME, PurOrder.T.ORD_STATUS, PurOrder.T.SHIPING,
			    PurOrder.T.CELL, PurOrder.T.TALLY_BY, PurOrder.T.TALLY_TIME, PurOrder.T.AMT_COST);
			order.setRem(getB().getRem());
			setB(order);
			updLineTid(getB(), getLines(), PurOrderLine.class);
			SysShipingDAO.upd(getB(), _ship, PurOrder.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class CheckPrice extends IduUpdLines<Upd, PurOrder, PurOrderLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			PurOrder order = loadThisBeanAndLock();

			if (order.getStatus() > Sys.OBillStatus.VERIFIED.getLine().getKey()) {
				throw LOG.err(Msgs.checkPrice, order.gtTB().getName(), order.getCode(), order.gtStatus()
				    .getLine().getName());
			}

			PropertyUtils.copyPropertiesWithout(order, getB(), PurOrder.T.PKEY, PurOrder.T.CODE,
			    PurOrder.T.STATUS, PurOrder.T.ORG, PurOrder.T.DEPT, PurOrder.T.CREATED_BY,
			    PurOrder.T.CREATED_TIME, PurOrder.T.ORD_STATUS, PurOrder.T.SHIPING,
			    PurOrder.T.CELL, PurOrder.T.TALLY_BY, PurOrder.T.TALLY_TIME,PurOrder.T.AMT_COST);
			order.setRem(getB().getRem());
			order.stStatus(Sys.OBillStatus.VERIFIED);
			order.setAmt(sumAmt(getLines()));
			setB(order);
			updLineTid(getB(), getLines(), PurOrderLine.class);
			SysShipingDAO.upd(getB(), _ship, PurOrder.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Del extends IduDel<Del, PurOrder> {

		@Override
		public void valid() {
			super.valid();
			PurOrder order = loadThisBeanAndLock();
			if (order.getStatus() > Sys.OBillStatus.VERIFIED.getLine().getKey()) {
				throw LOG.err(Msgs.delStatus, order.gtTB().getName(), order.getCode(), order.gtStatus()
				    .getLine().getName());
			}
		}

		@Override
		public void before() {
			super.before();
			GsDemandDAO.fireByPo(getB(), getB().getCode());
			SysShipingDAO.del(getB(), getB().gtShipingMode());
			delLineTid(getB(), PurOrderLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, PurOrder> {

		@Override
		public void run() {
			super.run();
			PurOrder order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.VERIFIED);

			// 在途处理

			List<PurOrderLine> lines = getLinesTid(order, PurOrderLine.class);
			GsPub.insertStimate(order, order.gtWarehouse(), lines, Gs.OEnrouteType.CGZT, null);

			List<GsDemand> list = BeanBase.list(GsDemand.class, "po_form = ? and po_form_num = ?", true,
			    order.gtLongPkey(), order.getCode());
			for (GsDemand demand : list) {
				Bean bean = demand.gtOrigForm();
				if (bean instanceof GsRequest) {
					List<IGoods> glines = new ArrayList<IGoods>();
					GsRequestLine line = new GsRequestLine();
					line.setGoods(demand.getGoods());
					line.setQty(demand.getQty());
					line.setUom(demand.getUom());
					glines.add(line);
					GsPub.deleteStimate((GsRequest) bean, order.gtWarehouse(), glines, Gs.OEnrouteType.YQG);
				}
			}

			order.stStatus(STATUS.TALLY_ABLE);
			order.setApprBy(getUser().getPkey());
			order.setApprTime(Env.INST.getWorkDate());
			order.upd();
			setB(order);
			// 记录最新成交价
			for (PurOrderLine line : lines) {
				PurProtGoods protGoods = PurProtGoods.loadUniqueTempCustObj(true,
				    SysTemplatCellDAO.getPurTmpl(order.gtCell()).getPkey(), order.getSupplier(), line.getGoods());
				protGoods.setPriceLast(line.getPrice());
				protGoods.setDateLast(Env.getTranBeginTime());
				protGoods.upd();
			}
			//相关字段：订金金额
			//贷 - 预收账款
			//借 - 待收款
			//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
			if (order.getEarnest().signum() > 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(order.gtCell(), Pur.SubjectAlias.PUR_PAY_SUP,
				    new AccObjs().setSupplier(order.gtSupplier()));
				GlNote note = GlNoteDAO.insAuto(order, order.getEarnest(), jnl, ODirect.DR, PyaNoteDepositPayable.TB.getID());
				PyaNoteDepositPayable notew = new PyaNoteDepositPayable().init();
				notew.stNote(note);
				notew.setBalance(note.getAmt());
				notew.setObj(order.gtSupplier().gtLongPkey());
				notew.setDateStart(order.getApprTime());
				notew.stType(Pya.OPdType.SUPPLIER);
				notew.ins();
				RpStimatePayDAO.insByBill(order, order.gtSupplier(), order.getEarnest());
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurOrder> {

		@Override
		public void run() {
			super.run();
			PurOrder order = loadThisBeanAndLock();
			assertStatusIsTally(order);
			List<PurOrderLine> lines = Idu.getLinesTid(order, PurOrderLine.class);
			for (PurOrderLine line : lines)
				if (line.getQtyOpen().compareTo(BigDecimal.ZERO) == 1)
					throw LOG.err(Msgs.qtyOpen, order.getCode());
			order.stStatus(STATUS_INIT);
			order.setApprBy(null);
			order.setApprTime(null);

			// 在途处理

			List<GsDemand> list = BeanBase.list(GsDemand.class, "po_form = ? and po_form_num = ?", true,
			    order.gtLongPkey(), order.getCode());
			for (GsDemand demand : list) {
				Bean bean = demand.gtOrigForm();
				if (bean instanceof GsRequest) {
					List<IGoods> glines = new ArrayList<IGoods>();
					GsRequestLine line = new GsRequestLine();
					line.setGoods(demand.getGoods());
					line.setQty(demand.getQty());
					line.setUom(demand.getUom());
					glines.add(line);
					GsPub.insertStimate((GsRequest) bean, order.gtWarehouse(), glines, Gs.OEnrouteType.YQG,
					    null);
				}
			}
			GsPub.deleteStimate(order, order.gtWarehouse(), lines, Gs.OEnrouteType.CGZT);
			order.upd();
			setB(order);
			GlNoteDAO.delByBill(order.gtLongPkey());
			if (order.getEarnest().signum() > 0) 
			RpStimatePayDAO.delByBill(order);
			//取消流水账的待处理登记
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

	public static class Close extends IduOther<Close, PurOrder> {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			PurOrder order = loadThisBeanAndLock();
			assertStatus(order, STATUS.TALLY_ABLE, STATUS.DONE);
			// order.stStatus(STATUS.DONE);
			order.stOrdStatus(OOrderStatus.CLOSE);
			order.upd();
			setB(order);
			GsStockStimateDAO.deleteByPurOrder(order);
		}
	}
	
	public static class Open extends IduOther<Open, PurOrder> {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			PurOrder order = loadThisBeanAndLock();
			assertStatus(order,STATUS.TALLY_ABLE, STATUS.DONE);
			List<PurOrderLine> ordLines = Idu.getLinesTid(order, PurOrderLine.class);
			for (PurOrderLine purOrderLine : ordLines) 
				if(purOrderLine.getQtyOpen().compareTo(purOrderLine.getQty())>=0)
					throw LOG.err("purOrder","开单数量与数量一样无法再次打开");
			order.stOrdStatus(OOrderStatus.INIT);
			order.upd();
			setB(order);
			GsStockStimateDAO.insertByForm(order, order.gtWarehouse(), ordLines, Gs.OEnrouteType.CGZT, null);
		}
	}

	public static void addOpenQty(PurOrder order, List lines) {
		List<PurOrderLine> ordLines = Idu.getLinesTid(order, PurOrderLine.class);
		for (Object line : lines) {
			BeanGoods guq = (BeanGoods) line;
			BigDecimal qty = guq.getQty();
			for (PurOrderLine ordLine : ordLines) {
				if (guq.getGoods().intValue() == ordLine.getGoods().intValue()) {
					BigDecimal openQty = ordLine.getQty().subtract(ordLine.getQtyOpen());
					if (qty.compareTo(openQty) <= 0) {
						ordLine.setQtyOpen(ordLine.getQtyOpen().add(qty));
//						qty = BigDecimal.ZERO;
					} else {
						throw LOG.err("overflow", "货物开单数量超过订单数量，不可新增！");
//						ordLine.setQtyOpen(ordLine.getQty());
//						qty = qty.subtract(openQty);
					}
					ordLine.upd();
//					if (qty.compareTo(BigDecimal.ZERO) == 0)
//						break;
				}
			}
		}
		closeOpenOrd(order, ordLines);
//		GsStockStimateDAO.updateByPurOrder(order, ordLines);
	}
	
	public static void subOpenQty(PurOrder order, List lines) {
		List<PurOrderLine> ordLines = Idu.getLinesTid(order, PurOrderLine.class);
		for (Object line : lines) {
			BeanGoods guq = (BeanGoods) line;
			BigDecimal qty = guq.getQty();
			for (PurOrderLine ordLine : ordLines) {
				if (guq.getGoods().intValue() == ordLine.getGoods().intValue()) {
					if (qty.compareTo(ordLine.getQtyOpen()) <= 0) {
						ordLine.setQtyOpen(ordLine.getQtyOpen().subtract(qty));
//						qty = BigDecimal.ZERO;
					} else {
						throw LOG.err("overflow", "货物开单数量超过订单数量，不可更改！");
//						ordLine.setQtyOpen(BigDecimal.ZERO);
//						qty = qty.subtract(ordLine.getQtyOpen());
					}
					ordLine.upd();
//					if (qty.compareTo(BigDecimal.ZERO) == 0)
//						break;
				}
			}
		}
		closeOpenOrd(order, ordLines);
//		GsStockStimateDAO.updateByPurOrder(order, ordLines);
	}
	
	public static void closeOpenOrd(PurOrder order, List<PurOrderLine> lines) {
		int count = 0;
		for (PurOrderLine line : lines) {
			if (line.getQty().compareTo(line.getQtyOpen()) == -1)
				throw LOG.err("overflow", "货物[{0}]开单数量超过订单数量，不可新增！", line.gtGoods().getName());
			if (line.getQty().compareTo(line.getQtyOpen()) == 0)
				count++;
		}
		if (lines.size() == count) {
			order.stOrdStatus(OOrderStatus.CLOSE);
			order.upd();
		}
		if (lines.size() > count) {
			order.stOrdStatus(OOrderStatus.INIT);
			order.upd();
		}
	}
}
