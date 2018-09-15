package irille.action.sal;

import irille.action.ActionForm;
import irille.core.sys.SysShiping;
import irille.pss.sal.SalSaleDirect;
import irille.pss.sal.SalSaleDirectDAO;
import irille.pss.sal.SalSaleDirectLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class SalSaleDirectAction extends ActionForm<SalSaleDirect, SalSaleDirectLine> {
	public SysShiping _ship;

	@Override
	public Class beanClazz() {
		return SalSaleDirect.class;
	}

	public SalSaleDirect getBean() {
		return _bean;
	}

	public void setBean(SalSaleDirect bean) {
		this._bean = bean;
	}

	public List<SalSaleDirectLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalSaleDirectLine> listLine) {
		_listLine = listLine;
	}

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}
	
	@Override
	public SalSaleDirect updRun() throws Exception {
		SalSaleDirectDAO.Upd upd = new SalSaleDirectDAO.Upd();
		upd.setB(_bean);
		upd._ship = getShip();
		upd.setLines(_listLine);
		upd.commit();
		return upd.getB();
	}

	@Override
	public SalSaleDirect insRun() throws Exception {
		SalSaleDirectDAO.Ins ins = new SalSaleDirectDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysShiping smode = ((SalSaleDirect) bean).gtShiping();
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
