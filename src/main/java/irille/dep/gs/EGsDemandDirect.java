package irille.dep.gs;

import irille.gl.gs.GsDemandDirect;
import irille.gl.gs.GsLocation.T;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGsDemandDirect extends GsDemandDirect {
	
	public static void main(String[] args) {
		new EGsDemandDirect().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		
		VFlds[] searchVflds = new VFlds[] {
				new VFlds(T.ORIG_FORM_NUM, T.ORG, T.STATUS)
		};
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}

}
