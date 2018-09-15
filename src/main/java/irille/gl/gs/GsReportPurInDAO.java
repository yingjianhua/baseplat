package irille.gl.gs;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.inf.IIn;
import irille.pub.svr.ProvDataCtrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GsReportPurInDAO {
	public static final Log LOG = new Log(GsReportPurInDAO.class);

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

	public List<GsReportPurIn> list() throws Exception{
		List<GsReportPurIn> reports = new ArrayList<GsReportPurIn>();
		String role = ProvDataCtrl.INST.getWhere(Idu.getUser(), GsIn.class);
		String where = Idu.sqlString("{0}=? and {1} mod 100000 = ? and {2} between ? and ? and "+ role , GsIn.T.WAREHOUSE, GsIn.T.ORIG_FORM, GsIn.T.CREATED_TIME);
		List<GsIn> list = BeanBase.list(GsIn.class, where, false, warehouse,6802, startDate, endDate);//6802 是收货单的表ID
		for (GsIn gs : list) {
			System.out.println("length:"+list.size());
			Class dc = Class.forName(gs.gtOrigForm().getClass().getName() + "DAO");
			IIn InLine = (IIn) dc.newInstance();
			List<IGoods> lines = InLine.getInLines(gs.gtOrigForm(), 0, 100);
			for (IGoods line : lines) {
				GsReportPurIn report = new GsReportPurIn();
				report.setCode(gs.getCode());
				report.setInTime(gs.getCreatedTime());
				report.setGoodsCode(line.gtGoods().getCode());
				report.setName(line.gtGoods().getName());
				report.setSpec(line.gtGoods().getSpec());
				report.setUom(line.getUom());
				report.setQty(line.getQty());
				reports.add(report);
			}
		}
		return reports;
	}
}
