package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlReportAssetLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.idu.IduPage;

import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlReportAssetLineAction extends ActionBase<GlReportAssetLine>{
	
	private int tableType ;
	
	@Override
	public Class beanClazz() {
	  return GlReportAssetLine.class;
	}
	
	public GlReportAssetLine getBean() {
		return _bean;
	}

	public void setBean(GlReportAssetLine bean) {
		this._bean = bean;
	}
	
	@Override
	public String crtAll() {
		String where = "asset_report="+_bean.getAssetReport();
		if(tableType==1) {
			where += " AND table_type = 1";
		} else if(tableType==2) {
			where += " AND table_type in (2,3)";
		} else if(tableType==3) {
			where += " AND table_type = 4";
		} 
		return where;
	}
	
	public void listZC() throws Exception {
		tableType = 1;
		list();
	}
	
	public void listLS() throws Exception {
		tableType = 2;
		list();
	}
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = Str.isEmpty(getQuery()) ? crtFilter() : crtQuery();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(getLimit());
		page.setWhere(where);
		page.commit();
		List<GlReportAssetLine> list = page.getList();
		JSONObject lineJson = null;
		for (GlReportAssetLine line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
			if(line.getKeyValue()!=null&&line.getKeyValue()==122) {
				ja.put(new JSONObject());
				ja.put(new JSONObject());
			}
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, page.getCount());
		writerOrExport(json);
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
