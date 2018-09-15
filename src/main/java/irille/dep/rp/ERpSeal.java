package irille.dep.rp;

import irille.gl.rp.RpSeal;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ERpSeal extends RpSeal {

	public static void main(String[] args) {
		new ERpSeal().crtExt();
	}

	public void crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.USER_SYS, T.NAME);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.ENABLED,T.TYPE,T.USER_SYS, T.MNG_CELL);
		fvs.setReadOnly("!this.insFlag", T.WORK_BOX);
		ext.newExts().init();
		ext.crtFiles();
	}
}
