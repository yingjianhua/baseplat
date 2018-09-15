package irille.action.sal;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsStock;
import irille.pss.sal.SalCustGoods;
import irille.pub.Str;
import irille.pub.bean.Bean;

import org.json.JSONObject;

public class SalCustGoodsAction extends ActionBase<SalCustGoods>{
	@Override
	public Class beanClazz() {
		return SalCustGoods.class;
	}
	
	public SalCustGoods getBean() {
		return _bean;
	}

	public void setBean(SalCustGoods bean) {
		this._bean = bean;
	}
	
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((SalCustGoods) bean).gtGoods();
		String gcode = GsStock.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		return json;
	}
}
