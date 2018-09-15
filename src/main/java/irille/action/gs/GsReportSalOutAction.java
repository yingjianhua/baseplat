package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsReportSalOut;
import irille.gl.gs.GsReportSalOutDAO;
import irille.gl.gs.GsWarehouse;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GsReportSalOutAction extends ActionBase<GsReportSalOut>{
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
	  return GsReportSalOut.class;
	}
	public GsReportSalOut getBean() {
		return _bean;
	}

	public void setBean(GsReportSalOut bean) {
		this._bean = bean;
	}
	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		GsReportSalOutDAO dao = new GsReportSalOutDAO();
		dao.setWarehouse(warehouse);
		dao.setStartDate(startDate);
		dao.setEndDate(endDate);
		System.out.println("warehouse:"+warehouse);
		System.out.println("startDate:"+startDate);
		System.out.println("endDate:"+endDate);
		List<GsReportSalOut> list = dao.list();
		JSONObject lineJson = null;
		for (GsReportSalOut line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		json.put("success",true);
		writerOrExport(json);
	}
}
