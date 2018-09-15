package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlReportLine;
import irille.gl.gl.GlReportLineDAO;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class GlReportLineAction extends ActionBase<GlReportLine> {
	
	public List<GlReportLine> _listLine;
	
	public int _reportPkey;
	
	public int getReportPkey() {
		return _reportPkey;
	}
	
	public void setReportPkey(int reportPkey) {
		this._reportPkey = reportPkey;
	}
	
	public GlReportLine getBean() {
		return _bean;
	}

	public void setBean(GlReportLine bean) {
		this._bean = bean;
	}
	
	public List<GlReportLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GlReportLine> listLine) {
		_listLine = listLine;
	}
	@Override
	public Class beanClazz() {
	  return GlReportLine.class;
	}

	@Override
	public void upd() throws Exception {
		GlReportLineDAO.update(getListLine(),getReportPkey());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		json.put("success", true);
		response.getWriter().print(json.toString());
	}
	
	@Override
	public String crtFilter() throws JSONException {
	  return super.crtFilter().replace("ORDER BY PKEY DESC", "ORDER BY PKEY ASC");
	}
}
