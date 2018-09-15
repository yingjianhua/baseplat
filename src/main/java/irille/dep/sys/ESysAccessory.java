/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysAccessory;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ESysAccessory extends SysAccessory {

	public static void main(String[] args) {
		//new ESysAccessory().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] {};
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}
}
