package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsCmb;
import irille.pub.Str;
import irille.pub.bean.Bean;

import org.json.JSONObject;

public class GsGoodsCmbAction extends ActionBase<GsGoodsCmb> {
	public GsGoodsCmb getBean() {
		return _bean;
	}

	public void setBean(GsGoodsCmb bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsGoodsCmb.class;
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsGoodsCmb) bean).gtGoods();
		GsGoods ingoods = ((GsGoodsCmb) bean).gtInnerGoods();
		String gcode = GsGoodsCmb.T.GOODS.getFld().getCode();
		String incode = GsGoodsCmb.T.INNER_GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		json.put(pref + incode + "Name", ingoods.getName());
		json.put(pref + incode + "Spec", ingoods.getSpec());
		return json;
	}

}
