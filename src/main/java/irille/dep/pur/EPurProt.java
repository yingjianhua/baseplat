package irille.dep.pur;

import irille.pss.pur.PurProt;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EPurProt extends PurProt {

	public static void main(String[] args) {
		new EPurProt().crtExt();
	}

	public void crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.SUPPLIER, T.NAME);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsList().get(T.NAME).setWidthList(180);
		ext.getVfldsList().del(T.SHIP_MODE);
		ext.newExts().init();
		ext.crtFiles();
	}
}
