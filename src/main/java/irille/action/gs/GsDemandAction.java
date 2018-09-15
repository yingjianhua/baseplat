package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsCmb;
import irille.pub.Str;
import irille.pub.bean.Bean;

import org.json.JSONObject;

public class GsDemandAction extends ActionBase<GsDemand> {

	public GsDemand getBean() {
		return _bean;
	}

	public void setBean(GsDemand bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
		return GsDemand.class;
	}
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsDemand) bean).gtGoods();
		String gcode = GsDemand.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		return json;
	}
}
