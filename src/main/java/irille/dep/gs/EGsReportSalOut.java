package irille.dep.gs;

import irille.gl.gs.GsIn;
import irille.gl.gs.GsReportSalOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGsReportSalOut extends GsReportSalOut {
	public static void main(String[] args) {
		new EGsReportSalOut().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GsIn.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(GsIn.T.WAREHOUSE, SYS.DATE) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}
}
