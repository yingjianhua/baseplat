/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysCom;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ESysCom extends SysCom {

	public static void main(String[] args) {
		new ESysCom().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		// 在此可以重新设置ext中的VFlds对象的属性
		ext.newExts().init();
		return ext;
	}
}
