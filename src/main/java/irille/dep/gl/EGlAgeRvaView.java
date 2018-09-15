/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlAgeRvaView;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGlAgeRvaView extends GlAgeRvaView {
	public static void main(String[] args) {
		new EGlAgeRvaView().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(SYS.ORG, T.DEPT, T.BUSINESS_MEMBER, SYS.DATE) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();

		return ext;
	}
}
