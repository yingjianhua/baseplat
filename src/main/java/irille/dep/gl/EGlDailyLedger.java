/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlDailyLedger;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMList;
import irille.pub.view.VFlds;

public class EGlDailyLedger extends GlDailyLedger {
	public static void main(String[] args) {
		new EGlDailyLedger().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CELL, T.SUBJECT, T.WORK_DATE) };
		EMCrt ext = new EMCrtSimpleTwo(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.CURRENCY);
		vl.get(T.SUBJECT).setWidthList(170);
		vl.get(T.CURRENCY).setWidthList(60);
		vl.get(T.WORK_DATE).setWidthList(90);
		((EMList) ext.newList()).setLineActs(false);
		ext.newExts().init();
		return ext;
	}

}
