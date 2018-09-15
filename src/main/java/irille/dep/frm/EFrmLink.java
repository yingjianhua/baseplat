package irille.dep.frm;

import irille.gl.frm.FrmLink;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EFrmLink extends FrmLink {
	public static void main(String[] args) {
		new EFrmLink().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		
		VFlds[] searchVflds = new VFlds[] {
				new VFlds(T.MAIN_FORM_NUM, T.LINK_FORM_NUM)
		};
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}

}
