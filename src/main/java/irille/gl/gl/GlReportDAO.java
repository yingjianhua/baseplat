package irille.gl.gl;

import irille.pub.Log;
import irille.pub.idu.IduInsLines;

public class GlReportDAO {
	public static final Log LOG = new Log(GlReportDAO.class);
	
	public static class Ins extends IduInsLines<Ins, GlReport, GlReportLine> {
		
		@Override
		public void run() {
//			super.run();
			//GlReportLineDAO.update(getLines());
		}
	}
}
