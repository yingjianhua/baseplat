package irille.action.gs;

import irille.action.ActionBase;
import irille.core.sys.SysShiping;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsOutDAO;
import irille.gl.gs.GsOutLineView;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class GsOutAction extends ActionBase<GsOut> {
	
	public List<GsOutLineView> _listLine;
	public SysShiping _ship;
	
	public GsOut getBean() {
		return _bean;
	}

	public void setBean(GsOut bean) {
		this._bean = bean;
	}
	
	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return GsOut.class;
	}
	
	public List<GsOutLineView> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsOutLineView> listLine) {
		_listLine = listLine;
	}
	
	public void approve() throws Exception {
		GsOutDAO.Approve appr = new GsOutDAO.Approve();
		appr.setB(_bean);
		appr._ship = getShip();
		appr.setLines(getListLine());
		appr.commit();
		writeSuccess(appr.getB());
	}
	
	public void unapprove() throws Exception {
		GsOutDAO.Unapprove unappr = new GsOutDAO.Unapprove();
		unappr.setBKey(getPkey());
		unappr.commit();
		writeSuccess(unappr.getB());
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (!(bean instanceof GsOut))
			return json;
		SysShiping smode = ((GsOut) bean).gtShiping();
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
