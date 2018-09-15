package irille.gl.gs;

import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.inf.IOut;
import irille.pub.svr.ProvDataCtrl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GsReportMvOutDAO {
	public static final Log LOG = new Log(GsReportMvOutDAO.class);

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

	public List<GsReportMvOut> list() throws Exception{
		List<GsReportMvOut> reports = new ArrayList<GsReportMvOut>();
		String role = ProvDataCtrl.INST.getWhere(Idu.getUser(), GsOut.class);;
		String where = Idu.sqlString("{0}=? and {1} mod 100000 = ? and {2} between ? and ? and "+role , GsOut.T.WAREHOUSE, GsOut.T.ORIG_FORM, GsOut.T.CREATED_TIME);
		System.out.println(where);
		List<GsOut> list = BeanBase.list(GsOut.class, where, false, warehouse,7014, startDate, endDate);//7014 是调出单的表ID
		System.out.println(list.size());
		System.out.println(""+warehouse+" "+7014+" "+startDate+" "+endDate);
		for (GsOut gs : list) {
			Class dc = Class.forName(gs.gtOrigForm().getClass().getName() + "DAO");
			IOut outLine = (IOut) dc.newInstance();
			List<IGoods> lines = outLine.getOutLines(gs.gtOrigForm(), 0, 100);
			for (IGoods line : lines) {
				GsReportMvOut report = new GsReportMvOut();
				report.setCode(gs.getCode());
				report.setOutTime(gs.getCreatedTime());
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
