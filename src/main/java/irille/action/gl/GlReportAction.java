package irille.action.gl;

import irille.action.ActionForm;
import irille.gl.gl.GlReport;
import irille.gl.gl.GlReportLine;

import java.util.List;

import org.json.JSONException;

public class GlReportAction extends ActionForm<GlReport, GlReportLine>{
	
	private int tableType;
	@Override
	public Class beanClazz() {
	  return GlReport.class;
	}
	
	public GlReport getBean() {
		return _bean;
	}

	public void setBean(GlReport bean) {
		this._bean = bean;
	}
	
	public List<GlReportLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GlReportLine> listLine) {
		_listLine = listLine;
	}
	@Override
	public String crtFilter() throws JSONException {
	  return super.crtFilter().replace("ORDER BY PKEY DESC", "ORDER BY PKEY ASC");
	}
	
	@Override
	public String crtAll() {
		if(tableType==1) {
			return "table_type = 1";
		} else if(tableType==2) {
			return "table_type in (2,3)";
		} else if(tableType==3) {
			return "table_type = 4";
		} 
		return "";
	}
	
	public void listZC() throws Exception {
		tableType = 1;
		list();
	}
	public void listFS() throws Exception {
		tableType = 2;
		list();
	}
	public void listLR() throws Exception {
		tableType = 3;
		list();
	}
}
