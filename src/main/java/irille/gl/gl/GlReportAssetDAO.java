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

public class GlReportAssetDAO {
	public static final Log LOG = new Log(GlReportAssetDAO.class);

	public static class Ins extends IduInsLines<Ins, GlReportAsset, GlReportAssetLine> {
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
			for (GlReportAssetLine bean : GlReportAssetLineDAO.getListLine(b.gtOrg(), b.getBeginDate(), b.getEndDate())) {
				bean.setAssetReport(main);
				bean.setPkey(pkey + num);
				validFromOut(bean);
				bean.ins();
				num++;
			}
		}
		
	}
	public static void Insert(SysOrg org, Date beginDate, Date endDate) {
		GlReportAssetDAO.Ins ins = new GlReportAssetDAO.Ins();
		GlReportAsset bean = new GlReportAsset();
		bean.stOrg(org);
		bean.setBeginDate(beginDate);
		bean.setEndDate(endDate);
		ins.setB(bean);
		ins.commit();
	}
	public static class Del extends IduDel<Del, GlReportAsset> {
		
		@Override
		public void after() {
		  super.after();
		  String where = Idu.sqlString("{0}=?", GlReportAssetLine.T.ASSET_REPORT);
			List<GlReportAssetLine> list = BeanBase.list(GlReportAssetLine.class, where, false, b.getPkey());
			Idu.delLine(list);
		}
	}
}
