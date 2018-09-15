package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsReportMvOut;
import irille.gl.gs.GsReportMvOutDAO;
import irille.gl.gs.GsWarehouse;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GsReportMvOutAction extends ActionBase<GsReportMvOut>{
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
	  return GsReportMvOut.class;
	}
	public GsReportMvOut getBean() {
		return _bean;
	}

	public void setBean(GsReportMvOut bean) {
		this._bean = bean;
	}
	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		GsReportMvOutDAO dao = new GsReportMvOutDAO();
		dao.setWarehouse(warehouse);
		dao.setStartDate(startDate);
		dao.setEndDate(endDate);
		List<GsReportMvOut> list = dao.list();
		JSONObject lineJson = null;
		for (GsReportMvOut line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		json.put("success",true);
		writerOrExport(json);
	}
}
