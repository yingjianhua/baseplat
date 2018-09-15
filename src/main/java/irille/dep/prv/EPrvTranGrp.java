package irille.dep.prv;

import irille.core.prv.PrvTranGrp;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EPrvTranGrp extends PrvTranGrp {
	public static void main(String[] args) {
		new EPrvTranGrp().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME, T.REM) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsList().setWidths(100,200,200,100);
		ext.newExts().init();
		return ext;
	}

}
