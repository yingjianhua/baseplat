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

public class GsReportSalOutDAO {
	public static final Log LOG = new Log(GsReportSalOutDAO.class);

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

	public List<GsReportSalOut> list() throws Exception{
		List<GsReportSalOut> reports = new ArrayList<GsReportSalOut>();
		String role = ProvDataCtrl.INST.getWhere(Idu.getUser(), GsOut.class);
		String where = Idu.sqlString("{0}=? and {1} mod 100000 = ? and {2} between ? and ? and "+role , GsOut.T.WAREHOUSE, GsOut.T.ORIG_FORM, GsOut.T.CREATED_TIME);
		List<GsOut> list = BeanBase.list(GsOut.class, where, false, warehouse, 7002, startDate, endDate);//7002 是销售单的表ID
		for (GsOut gs : list) {
			Class dc = Class.forName(gs.gtOrigForm().getClass().getName() + "DAO");
			IOut outLine = (IOut) dc.newInstance();
			List<IGoods> lines = outLine.getOutLines(gs.gtOrigForm(), 0, 100);
			//System.out.println("orginForm:"+gs.gtOrigForm().gtTbId());
			//System.out.println("salSaleID:"+);
			for (IGoods line : lines) {
				GsReportSalOut report = new GsReportSalOut();
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
