package irille.action.sal;

import irille.action.ActionForm;
import irille.core.sys.SysShiping;
import irille.pss.sal.SalPresent;
import irille.pss.sal.SalPresentDAO;
import irille.pss.sal.SalPresentLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class SalPresentAction extends ActionForm<SalPresent, SalPresentLine> {
	public SysShiping _ship;

	public Class beanClazz() {
		return SalPresent.class;
	}

	public SalPresent getBean() {
		return _bean;
	}

	public void setBean(SalPresent bean) {
		this._bean = bean;
	}

	public List<SalPresentLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalPresentLine> listLine) {
		_listLine = listLine;
	}
	
	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	public void crtGs() throws Exception {
		SalPresentDAO.CrtGs act = new SalPresentDAO.CrtGs();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}

	@Override
	public SalPresent updRun() throws Exception {
		SalPresentDAO.Upd upd = new SalPresentDAO.Upd();
		upd.setB(_bean);
		upd._ship = getShip();
		upd.setLines(_listLine);
		upd.commit();
		return upd.getB();
	}

	@Override
	public SalPresent insRun() throws Exception {
		SalPresentDAO.Ins ins = new SalPresentDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysShiping smode = ((SalPresent) bean).gtShiping();
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
