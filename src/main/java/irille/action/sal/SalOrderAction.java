package irille.action.sal;

import irille.action.ActionForm;
import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.core.sys.SysShiping;
import irille.pss.sal.SalOrder;
import irille.pss.sal.SalOrderDAO;
import irille.pss.sal.SalOrderLine;
import irille.pss.sal.SalOrderDAO.Close;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IExtName;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class SalOrderAction extends ActionForm<SalOrder, SalOrderLine> {
	public SysShiping _ship;

	@Override
	public Class beanClazz() {
		return SalOrder.class;
	}

	public SalOrder getBean() {
		return _bean;
	}

	public void setBean(SalOrder bean) {
		this._bean = bean;
	}

	public List<SalOrderLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalOrderLine> listLine) {
		_listLine = listLine;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	public void close() throws Exception {
		Close act = new Close();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	@Override
	public SalOrder updRun() throws Exception {
		SalOrderDAO.Upd upd = new SalOrderDAO.Upd();
		upd.setB(_bean);
		upd._ship = getShip();
		upd.setLines(_listLine);
		upd.commit();
		return upd.getB();
	}

	@Override
	public SalOrder insRun() throws Exception {
		SalOrderDAO.Ins ins = new SalOrderDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}
	
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception{
		SysShiping smode = ((SalOrder)bean).gtShiping();
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
