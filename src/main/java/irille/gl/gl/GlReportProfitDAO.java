package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.core.sys.SysShiping;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.svr.Env;

import java.util.Date;
import java.util.List;

public class GlReportProfitDAO {
	public static final Log LOG = new Log(GlReportProfitDAO.class);

	public static class Ins extends IduInsLines<Ins, GlReportProfit, GlReportProfitLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			b.setCreateBy(getUser().getPkey());
			b.setCreateTime(Env.INST.getWorkDate());
		}
		@Override
		public void run() {
		  b.ins();
		}
		@Override
		public void after() {
			Long main = b.getPkey();
			long pkey = lineFirstPkey(main);
			int num = 1;
			for (GlReportProfitLine bean : GlReportProfitLineDAO.getListLine(b.gtOrg(), b.getBeginDate(), b.getEndDate())) {
				bean.setProfitReport(main);
				bean.setPkey(pkey + num);
				validFromOut(bean);
				bean.ins();
				num++;
			}
		}
		
	}
	public static void Insert(SysOrg org, Date beginDate, Date endDate) {
		GlReportProfitDAO.Ins ins = new GlReportProfitDAO.Ins();
		GlReportProfit bean = new GlReportProfit();
		bean.stOrg(org);
		bean.setBeginDate(beginDate);
		bean.setEndDate(endDate);
		ins.setB(bean);
		ins.commit();
	}
	public static class Del extends IduDel<Del, GlReportProfit> {
		
		@Override
		public void after() {
		  super.after();
		  String where = Idu.sqlString("{0}=?", GlReportProfitLine.T.PROFIT_REPORT);
			List<GlReportProfitLine> list = BeanBase.list(GlReportProfitLine.class, where, false, b.getPkey());
			Idu.delLine(list);
		}
	}
}
