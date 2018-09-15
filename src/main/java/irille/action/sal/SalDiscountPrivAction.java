package irille.action.sal;

import irille.action.ActionBase;
import irille.gl.gs.GsWarehouse;
import irille.pss.sal.SalDiscountPriv;
import irille.pub.bean.Bean;

import org.json.JSONObject;

public class SalDiscountPrivAction extends ActionBase<SalDiscountPriv> {

	@Override
	public Class beanClazz() {
		return SalDiscountPriv.class;
	}

	public SalDiscountPriv getBean() {
		return _bean;
	}

	public void setBean(SalDiscountPriv bean) {
		this._bean = bean;
	}

	@Override
	public void setPkey(String pkey) {
		super.setPkey(pkey.split("##")[0]);
	}

	@Override
	public void setPkeys(String pkeys) {
		String pks[] = pkeys.split(",");
		String pk = "";
		for (String p : pks) {
			pk += p.split("##")[0] + ",";
		}
		super.setPkeys(pk);
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SalDiscountPriv mode = (SalDiscountPriv) bean;
		json.remove(pref + SalDiscountPriv.T.PKEY.getFld().getCode());
		json.put(pref + SalDiscountPriv.T.PKEY.getFld().getCode(), mode.getPkey() + BEAN_SPLIT + mode.gtUser().getName());
		return json;
	}

}
