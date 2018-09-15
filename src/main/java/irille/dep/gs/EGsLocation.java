package irille.dep.gs;

import irille.gl.gs.GsLocation;
import irille.gl.gs.GsGoods.T;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMListTrigger;
import irille.pub.view.VFlds;

public class EGsLocation extends GsLocation {

	public static void main(String[] args) {
		new EGsLocation().crtExt().crtFiles(); // 产生Ext文件
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.WAREHOUSE, T.NAME, T.ENABLED) };
		VFlds listFlds = new VFlds().addAll(TB);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.setVfldsList(listFlds);
		VFlds vsl = ext.getVfldsList();
		vsl.moveAfter(T.ENABLED, T.REM);
		ext.newExts().init();

		//选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vsl, T.NAME, new VFlds(T.NAME, T.REM));
		trigger.newExts().init().crtFiles();

		return ext;
	}
}
