package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlReportProfitLine;
import irille.pub.bean.Bean;

import org.json.JSONException;
import org.json.JSONObject;

public class GlReportProfitLineAction extends ActionBase<GlReportProfitLine>{
	
	@Override
	public Class beanClazz() {
	  return GlReportProfitLine.class;
	}
	
	public GlReportProfitLine getBean() {
		return _bean;
	}

	public void setBean(GlReportProfitLine bean) {
		this._bean = bean;
	}
	
	@Override
	public String crtAll() {
		String where = "profit_report="+_bean.getProfitReport();
		where += " AND table_type = 4";
		
		return where;
	}
	
	@Override
	public String crtFilter() throws JSONException {
	  return super.crtFilter().replace("ORDER BY PKEY DESC", "ORDER BY PKEY ASC");
	}
	
	public JSONObject crtJsonByBean(Bean bean) throws Exception {
		JSONObject lineJson = crtJsonByBean(bean, "");
		try {
	    lineJson.get("keyValue");
    } catch (JSONException e) {
    	lineJson.put("amtBegin", "");
			lineJson.put("amtEnd", "");
    }
		return lineJson;
	}
}
