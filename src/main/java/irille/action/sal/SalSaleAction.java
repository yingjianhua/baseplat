package irille.action.sal;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.pss.sal.Sal;
import irille.pss.sal.SalOrder;
import irille.pss.sal.SalOrderLine;
import irille.pss.sal.SalSale;
import irille.pss.sal.SalSaleDAO;
import irille.pss.sal.SalSaleLine;
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

public class SalSaleAction extends ActionForm<SalSale, SalSaleLine> {
	private Integer _orderPkey;
	public SysShiping _ship;

	public Class beanClazz() {
		return SalSale.class;
	}

	public SalSale getBean() {
		return _bean;
	}

	public void setBean(SalSale bean) {
		this._bean = bean;
	}

	public List<SalSaleLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalSaleLine> listLine) {
		_listLine = listLine;
	}

	public Integer getOrderPkey() {
		return _orderPkey;
	}

	public void setOrderPkey(Integer orderPkey) {
		_orderPkey = orderPkey;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	public void crtGs() throws Exception {
		SalSaleDAO.CrtGs act = new SalSaleDAO.CrtGs();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	public void initByOrder() throws Exception {
		JSONObject json = new JSONObject();
		SalOrder order = BeanBase.load(SalOrder.class, getOrderPkey());
		SalSale mode = new SalSale();
		mode.setOrd(order.getPkey());
		mode.setCust(order.getCust());
		mode.setCustName(order.getCustName());
		mode.stStatus(Sys.OBillStatus.INIT);
		mode.stInoutStatus(Sal.OInoutStatus.INIT);
		mode.setRem(order.getRem());
		mode.setOperator(order.getOperator());
		mode.setShipingMode(order.getShipingMode());
		mode.setBillFlag(order.getBillFlag());
		mode.setAmtOrd(BigDecimal.ZERO);
		mode.setAmtPay(BigDecimal.ZERO);
		mode.setAmtRec(BigDecimal.ZERO);
		mode.setRowVersion((short)0);
		//明细处理
		JSONArray jsonLines = new JSONArray();
		List<SalOrderLine> lines = Idu.getLinesTid(order, SalOrderLine.class);
		BigDecimal amt = BigDecimal.ZERO;
		for (SalOrderLine line : lines) {
			if (line.getQty().subtract(line.getQtyOpen()).signum() <= 0)
				continue;
			SalSaleLine modeLine = new SalSaleLine();
			modeLine.setGoods(line.getGoods());
			modeLine.setUom(line.getUom());
			modeLine.setQty(line.getQty().subtract(line.getQtyOpen()));
			modeLine.setPrice(line.getPrice());
			modeLine.setAmt(modeLine.getPrice().multiply(modeLine.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
			modeLine.setExpensesSale(BigDecimal.ZERO);// 费用未处理
			amt = amt.add(modeLine.getAmt());
			JSONObject lineJson = new JSONObject();
			lineJson.put("bean.goods", modeLine.getGoods()+BEAN_SPLIT+modeLine.gtGoods().getCode());
			lineJson.put("bean.uom", nv(modeLine.gtUom()));
			lineJson.put("bean.qty", nv(modeLine.getQty()));
			lineJson.put("bean.price", nv(modeLine.getPrice()));
			lineJson.put("bean.amt", nv(modeLine.getAmt()));
			lineJson.put("link.goodsName", modeLine.gtGoods().getName());
			lineJson.put("link.goodsSpec", modeLine.gtGoods().getSpec());
			jsonLines.put(lineJson);
		}
		mode.setAmt(amt);
		JSONObject jsonMode = crtJsonByBean(mode, "bean.");
		SysShiping ship = order.gtShiping();
		if (ship != null) {
			jsonMode.put("ship." + SysShiping.T.TIME_ARR_PLAN.getFld().getCode(), nv(ship.getTimeArrPlan()));
			jsonMode.put("ship." + SysShiping.T.TIME_SHIP_PLAN.getFld().getCode(), nv(ship.getTimeShipPlan()));
			jsonMode.put("ship." + SysShiping.T.NAME.getFld().getCode(), nv(ship.getName()));
			jsonMode.put("ship." + SysShiping.T.ADDR.getFld().getCode(), nv(ship.getAddr()));
			jsonMode.put("ship." + SysShiping.T.MOBILE.getFld().getCode(), nv(ship.getMobile()));
			jsonMode.put("ship." + SysShiping.T.TEL.getFld().getCode(), nv(ship.getTel()));
		}
		json.put("row", jsonMode);
		json.put("lines", jsonLines);
		writerOrExport(json);
	}

	@Override
	public SalSale updRun() throws Exception {
		SalSaleDAO.Upd upd = new SalSaleDAO.Upd();
		upd.setB(_bean);
		upd._ship = getShip();
		upd.setLines(_listLine);
		upd.commit();
		return upd.getB();
	}

	@Override
	public SalSale insRun() throws Exception {
		SalSaleDAO.Ins ins = new SalSaleDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysShiping smode = ((SalSale) bean).gtShiping();
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
			return toTimeJson((Date) obj); //注意类型
		if (obj instanceof BeanMain) {
			BeanMain b = (BeanMain) obj;
			return b.getPkey() + BEAN_SPLIT + ((IExtName) b).getExtName();
		}
		return obj;
	}
}
