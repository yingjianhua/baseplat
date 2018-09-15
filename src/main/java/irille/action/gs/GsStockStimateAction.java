package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsStockStimate;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsStockStimateAction extends ActionBase<GsStockStimate> {

	public GsStockStimate getBean() {
		return _bean;
	}

	public void setBean(GsStockStimate bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsStockStimate.class;
	}
	
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsStockStimate) bean).gtGoods();
		String gcode = GsStockStimate.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		return json;
	}
	
	@Override
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		//搜索或高级搜索关联到外表字段查询的情况
		sql += crtOutWhereSearch(ja, tb, GsStockStimate.T.GOODS.getFld(), GsGoods.TB, "goods", GsGoods.T.NAME.getFld(), GsGoods.T.SPEC.getFld());
		return crtFilterAll() + sql + orderBy();
	}
}
