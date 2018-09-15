package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsReportPurIn;
import irille.gl.gs.GsReportPurInDAO;
import irille.gl.gs.GsWarehouse;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GsReportPurInAction extends ActionBase<GsReportPurIn>{
	private int warehouse;
	private Date startDate;
	private Date endDate;
	
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public int getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(int warehouse) {
		this.warehouse = warehouse;
	}
	@Override
	public Class beanClazz() {
	  return GsReportPurIn.class;
	}
	public GsReportPurIn getBean() {
		return _bean;
	}

	public void setBean(GsReportPurIn bean) {
		this._bean = bean;
	}
	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		GsReportPurInDAO dao = new GsReportPurInDAO();
		dao.setEndDate(getEndDate());
		dao.setStartDate(getStartDate());
		dao.setWarehouse(getWarehouse());
  	
		List<GsReportPurIn> list = dao.list();
		JSONObject lineJson = null;
		for (GsReportPurIn line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		json.put("success",true);
		writerOrExport(json);
	}
}
