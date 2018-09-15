/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysDept;
import irille.core.sys.SysDeptDAO;
import irille.core.sys.SysPersonLink;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMList;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * @author whx
 * @version 创建时间：2014年11月13日 下午3:25:14
 */
public class ESysDept extends SysDept {
	public static void main(String[] args) {
		new ESysDept().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.ENABLED) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsForm().get(T.CODE).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.ORG).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.DEPT_UP).setReadOnly("!this.insFlag");
		ext.getVfldsForm().del(T.CELL, T.ENABLED);
		ext.newExts().init();
		//下面是编辑相关JS产生
		VFlds editVflds = new VFlds().addWithout(SysDept.TB, SysDept.T.PKEY);
		VFlds[] editOuts = new VFlds[] { new VFlds().add(SysPersonLink.TB, SysPersonLink.T.TYPE, SysPersonLink.T.NAME,
		    SysPersonLink.T.PE_MOBILE, SysPersonLink.T.PE_EMAIL, SysPersonLink.T.OF_FAX, SysPersonLink.T.OF_ADDR) };
		VFld[] outflds = new VFld[] { SysPersonLink.T.TB_OBJ_LONG.getFld().getVFld() };
		editOuts[0].get(SysPersonLink.T.PE_MOBILE).setName("手机");
		editOuts[0].get(SysPersonLink.T.PE_EMAIL).setName("邮箱");
		editOuts[0].get(SysPersonLink.T.OF_FAX).setName("传真");
		editOuts[0].get(SysPersonLink.T.OF_ADDR).setName("地址").setWidthList(250);
		EMCrt extEdit = new ESysCustom.MyEdit(TB, editVflds, editOuts, outflds);
		extEdit.newExts().init();
		extEdit.crtFiles();

		EMCrt ext2 = new EMCrtTrigger(TB, vflds[0], T.NAME, new VFlds(T.CODE, T.NAME));
		ext2.newExts().init().crtFiles();

		return ext;
	}

}
