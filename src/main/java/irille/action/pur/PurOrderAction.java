package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.Sys;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysPersonLinkDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.Sys.OShipingMode;
import irille.pss.pur.Pur;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderDAO;
import irille.pss.pur.PurOrderLine;
import irille.pss.pur.PurProt;
import irille.pss.pur.PurOrderDAO.Close;
import irille.pss.pur.PurOrderDAO.Open;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class PurOrderAction extends ActionForm<PurOrder, PurOrderLine> {

	private Integer temId;
	private Integer supId;
	private Integer whsId;
	public SysShiping _ship;
	private boolean _isPur = false;
	public Integer getTemId() {
		return temId;
	}

	public void setTemId(Integer temId) {
		this.temId = temId;
	}

	public Integer getSupId() {
		return supId;
	}

	public void setSupId(Integer supId) {
		this.supId = supId;
	}
	//用于销售参数的查询
	public void listPur() throws Exception {
		_isPur = true;
		list();
	}
	public String crtAll() {
		String all = "1=1";
		if (_isPur)
			all += " AND " + PurOrder.T.STATUS.getFld().getCodeSqlField() + " between " + Sys.OBillStatus.TALLY_ABLE.getLine().getKey()
			+ " and "+Sys.OBillStatus.DONE.getLine().getKey()
			+ " and " + PurOrder.T.ORD_STATUS.getFld().getCodeSqlField() + " = " + Pur.OOrderStatus.INIT.getLine().getKey();
		return all;
	}
	public Integer getWhsId() {
		return whsId;
	}

	public void setWhsId(Integer whsId) {
		this.whsId = whsId;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return PurOrder.class;
	}

	public PurOrder getBean() {
		return _bean;
	}

	public void setBean(PurOrder bean) {
		this._bean = bean;
	}

	public List<PurOrderLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurOrderLine> listLine) {
		_listLine = listLine;
	}
	
	public void close() throws Exception {
		Close act = new Close();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}
	
	public void open() throws Exception {
		Open act = new Open();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	public void init() throws Exception {
		JSONObject json = new JSONObject();
		PurOrder order = new PurOrder();

		PurProt prot = PurProt.loadUniqueTempCust(false, getTemId(), getSupId());

		order.setSupplier(getSupId());
		order.setSupname(prot.getName());

//		List<SysPersonLink> links = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, order.gtSupplier()
//		    .gtLongPkey());
//		if (links.size() > 0) {
//			order.setLinkman(links.get(0).getPkey());
//		}
		order.gtShiping();
		order.setBuyer(getLoginSys().getPkey());
		order.setWarehouse(getWhsId());
		order.setBillFlag(Sys.OBillFlag.DEFAULT.getLine().getKey());
		order.stShipingMode(OShipingMode.NO);
		order.setEarnest(BigDecimal.ZERO);
		order.setRowVersion((short)0);
		order.setDept(getWhsId());
		SysPersonLink person = SysPersonLinkDAO.getDefault(order.gtDept(), Sys.OLinkType.SAL);
		order.setRevAddr(person == null ? "" : person.getOfAddr());
		json = crtJsonByBean(order, "bean.");

		List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, order.gtWarehouse()
		    .gtDept().gtLongPkey());
		if (warelinks.size() > 0) {
			SysPersonLink link = warelinks.get(0);
			json.put("ship.name", link.getName());
			json.put("ship.addr", link.getOfAddr());
			json.put("ship.mobile", link.getPeMobile());
			json.put("ship.tel", link.getOfTel());
		}
		json.put("bean.rowVersion", 0);
		writerOrExport(json);
	}

	@Override
	public PurOrder updRun() throws Exception {
		PurOrderDAO.Upd upd = new PurOrderDAO.Upd();
		upd.setB(_bean);
		upd._ship = getShip();
		upd.setLines(_listLine);
		upd.commit();
		return upd.getB();
	}

	@Override
	public PurOrder insRun() throws Exception {
		PurOrderDAO.Ins ins = new PurOrderDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public void checkPrice() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		PurOrderDAO.CheckPrice upd = new PurOrderDAO.CheckPrice();
		upd._ship = getShip();
		upd.setB(getBean());
		upd.setLines(getListLine());
		upd.commit();
//		JSONObject json = crtJsonByBean(upd.getB(), "bean.");
//		json.put("success", true);
//		response.getWriter().print(json.toString());
		writeSuccess(upd.getB());
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysShiping smode = ((PurOrder) bean).gtShiping();
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

	@Override
	public void approve() throws Exception {
		// TODO Auto-generated method stub
		super.approve();
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
