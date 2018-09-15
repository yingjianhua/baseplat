package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsStockAction extends ActionBase<GsStock> {
	public List<GsStockLine> _listLine;
	
	public GsStock getBean() {
		return _bean;
	}

	public void setBean(GsStock bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsStock.class;
	}
	
	public List<GsStockLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsStockLine> listLine) {
		_listLine = listLine;
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
		sql += crtOutWhereSearch(ja, tb, GsStock.T.GOODS.getFld(), GsGoods.TB, "goods", GsGoods.T.NAME.getFld(), GsGoods.T.SPEC.getFld());
		return crtFilterAll() + sql + orderBy();
	}
	
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsStock) bean).gtGoods();
		String gcode = GsStock.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		json.put(pref + "uom", goods.getUom()+BEAN_SPLIT+goods.gtUom().getName());
		return json;
	}
	public void loadQty() throws Exception{
		GsStock bean = GsStock.chkUniqueWg(false, _bean.getWarehouse(), _bean.getGoods());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject lineJson = new JSONObject();
		double qty = 0.0;
		if(bean!=null) qty=bean.getQty().doubleValue();
		lineJson.put("qty", "["+_bean.gtWarehouse().gtDept().getName()+"]中还有["+_bean.gtGoods().getName()+"]"+qty+" "+_bean.gtGoods().gtUom().getName());
		lineJson.put("success", true);
		response.getWriter().print(lineJson.toString());
	}
}
