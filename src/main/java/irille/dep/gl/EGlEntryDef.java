/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlEntryDef;
import irille.gl.gl.GlEntryDefLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtEdit;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMList;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 
 * @author whx
 * @version 创建时间：2014年11月13日 下午3:25:14
 */
public class EGlEntryDef extends GlEntryDef {
	public static void main(String[] args) {
		new EGlEntryDef().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.TABLE_ID, T.CODE) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		((EMList) ext.newList()).setLineActs(false);
		ext.newExts().init();
		//下面是编辑相关JS产生 -- 因了表的主键为FORM明细的方式，所以前台代码需要重写
		//		VFlds editVflds = new VFlds().addWithout(GlEntryDef.TB, GlEntryDef.T.PKEY);
		//		VFlds[] editOuts = new VFlds[] { new VFlds(GlEntryDefLine.TB)};
		//		VFld[] outflds = new VFld[] { GlEntryDefLine.T.PKEY.getFld().getVFld() };
		//		EMCrt extEdit = new EMCrtEdit(TB, editVflds, editOuts, outflds);
		//		extEdit.newExts().init();
		//		extEdit.crtFiles();
		return ext;
	}

}
