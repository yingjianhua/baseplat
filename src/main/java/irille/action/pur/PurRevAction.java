package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.pss.pur.Pur;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRevDAO;
import irille.pss.pur.PurRevLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PurRevAction extends ActionForm<PurRev, PurRevLine> {

	private Integer orderId;
	public SysShiping _ship;

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return PurRev.class;
	}

	public PurRev getBean() {
		return _bean;
	}

	public void setBean(PurRev bean) {
		this._bean = bean;
	}

	public List<PurRevLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurRevLine> listLine) {
		_listLine = listLine;
	}

	public void init() throws Exception {
		JSONObject json = new JSONObject();
		PurOrder order = BeanBase.load(PurOrder.class, getOrderId());
		PurRev rev = new PurRev();
		rev.setOrd(order.getPkey());
		rev.setSupplier(order.getSupplier());
		rev.setSupname(order.getSupname());
		rev.setWarehouse(order.getWarehouse());
		rev.setBuyer(order.getBuyer());
		rev.setBillFlag(order.getBillFlag());
		rev.setAmtCost(BigDecimal.ZERO);// 费用未处理
		rev.stSettleFlag(Pur.OSettleFlag.REV);
		rev.setShipingMode(order.getShipingMode());
		rev.setRowVersion((short)0);
		JSONArray revLinesJson = new JSONArray();
		List<PurOrderLine> lines = Idu.getLinesTid(order, PurOrderLine.class);
		BigDecimal amt = BigDecimal.ZERO;
		for (PurOrderLine line : lines) {
			if (line.getQty().subtract(line.getQtyOpen()).compareTo(BigDecimal.ZERO) == 0) {
				continue;
			}
			PurRevLine revLine = new PurRevLine();
			revLine.setGoods(line.getGoods());
			revLine.setUom(line.getUom());
			revLine.setQty(line.getQty().subtract(line.getQtyOpen()));
			revLine.setPrice(line.getPrice());
			revLine.setAmt(revLine.getPrice().multiply(revLine.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			revLine.setCostPur(BigDecimal.ZERO);// 费用未处理
			revLine.setReceivedQty(revLine.getQty());
			amt = amt.add(revLine.getAmt());
			JSONObject lineJson = crtJsonByBean(revLine, "bean.");
			lineJson.put("link.goodsName", revLine.gtGoods().getName());
			lineJson.put("link.goodsSpec", revLine.gtGoods().getSpec());
			revLinesJson.put(lineJson);
		}
		rev.setAmt(amt);
		rev.setAmtDj(BigDecimal.ZERO);
		rev.setAmtGz(BigDecimal.ZERO);
		rev.setAmtXf(BigDecimal.ZERO);
		JSONObject revJson = new JSONObject();
		revJson = crtJsonByBean(rev, "bean.");
		SysShiping ship = order.gtShiping();
		if (ship != null) {
			revJson.put("ship." + SysShiping.T.TIME_ARR_PLAN.getFld().getCode(), nv(ship.getTimeArrPlan()));
			revJson.put("ship." + SysShiping.T.TIME_SHIP_PLAN.getFld().getCode(), nv(ship.getTimeShipPlan()));
			revJson.put("ship." + SysShiping.T.NAME.getFld().getCode(), nv(ship.getName()));
			revJson.put("ship." + SysShiping.T.ADDR.getFld().getCode(), nv(ship.getAddr()));
			revJson.put("ship." + SysShiping.T.MOBILE.getFld().getCode(), nv(ship.getMobile()));
			revJson.put("ship." + SysShiping.T.TEL.getFld().getCode(), nv(ship.getTel()));
		}
		json.put("rev", revJson);
		json.put("revLine", revLinesJson);
		writerOrExport(json);
	}

	@Override
	public PurRev insRun() throws Exception {
		PurRevDAO.Ins ins = new PurRevDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	@Override
	public PurRev updRun() throws Exception {
		PurRevDAO.Upd ins = new PurRevDAO.Upd();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (!(bean instanceof PurRev))
			return json;
		SysShiping smode = ((PurRev) bean).gtShiping();
		if (smode == null)
			return json;
		String ship = "";
		if (Str.isEmpty(pref) == false)
			ship = "ship.";
		json.put(ship + SysShiping.T.TIME_ARR_PLAN.getFld().getCode(), nv(smode.getTimeArrPlan()));
		json.put(ship + SysShiping.T.TIME_SHIP_PLAN.getFld().getCode(), nv(smode.getTimeShipPlan()));
		json.put(ship + SysShiping.T.NAME.getFld().getCode(), nv(smode.getName()));
		json.put(ship + SysShiping.T.ADDR.getFld().getCode(), nv(smode.getAddr()));
		json.put(ship + SysShiping.T.MOBILE.getFld().getCode(), nv(smode.getMobile()));
		json.put(ship + SysShiping.T.TEL.getFld().getCode(), nv(smode.getTel()));
		return json;
	}

	private Object nv(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return toTimeJson((Date) obj); // 注意类型
		if (obj instanceof BeanMain) {
			BeanMain b = (BeanMain) obj;
			return b.getPkey() + BEAN_SPLIT + ((IExtName) b).getExtName();
		}
		return obj;
	}
}
