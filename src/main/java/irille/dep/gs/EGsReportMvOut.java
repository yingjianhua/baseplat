package irille.dep.gs;

import irille.gl.gs.GsOut;
import irille.gl.gs.GsReportMvOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGsReportMvOut extends GsReportMvOut {
	public static void main(String[] args) {
		new EGsReportMvOut().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GsOut.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(GsOut.T.WAREHOUSE, SYS.DATE) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}
}
