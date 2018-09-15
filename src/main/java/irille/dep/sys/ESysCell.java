/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysUserRole;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.svr.DbPool;
import irille.pub.svr.StartInitServlet;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class ESysCell extends SysCell {

	public static void main(String[] args) {
		StartInitServlet.initBeanLoad();
		new ESysCell().crtExt().crtFiles();
		DbPool.getInstance().releaseAll();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME, T.YEAR) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		((VFldOutKey) ext.getVfldsForm().get(T.TEMPLAT)).setDiySql("type="+Sys.OTemplateType.GL.getLine().getKey());
		ext.getVfldsForm().del(T.CODE, T.TEMPLAT);
		ext.newExts().init();
		EMCrtModelAndStore.crtModelAndStore(SysUserRole.TB, new VFlds(SysUserRole.TB));
		return ext;
	}
}
