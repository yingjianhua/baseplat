/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysEm;
import irille.core.sys.SysPerson;
import irille.core.sys.SysUser;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMList;
import irille.pub.html.EMListTrigger;
import irille.pub.view.VFlds;

public class ESysEm extends SysEm {
	public static void main(String[] args) {
		new ESysEm().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds one = new VFlds("one").addWithout(SysPerson.TB, SysPerson.T.PKEY, SysPerson.T.NAME,
						SysPerson.T.PE_WB, SysPerson.T.PE_MOBILE_OTHER,
						SysPerson.T.PE_MSN, SysPerson.T.PE_EDU,
						SysPerson.T.PE_DEGREE, SysPerson.T.PE_POSITIONAL_TITLE,
						SysPerson.T.PE_DRIVING_LICENSE, SysPerson.T.PE_PARTY,
						SysPerson.T.PE_BELIEF, SysPerson.T.OF_COMPANY_NAME,
						SysPerson.T.OF_DEPT_NAME, SysPerson.T.OF_POST,
						SysPerson.T.OF_TEL, SysPerson.T.OF_FAX,
						SysPerson.T.OF_WEBSITE, SysPerson.T.OF_ADDR,
						SysPerson.T.OF_ZIP_CODE, SysPerson.T.PHOTO,SysPerson.T.ROW_VERSION);
		VFlds user = new VFlds("user").add(SysUser.TB, SysUser.T.LOGIN_NAME);
		VFlds[] vflds = new VFlds[] { new VFlds(TB).del(T.CELL, T.ENAME,T.USER_SYS), one , user};
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.ORG) };
		EMCrt ext = new EMCrtSimpleTwo(TB, vflds, searchVflds);
		ext.getVfldsForm().get(SysUser.T.LOGIN_NAME).setReadOnly("!this.insFlag");
		//((VFldOutKey) ext.getVfldsForm().get(T.TEMPLAT)).setDiySql("type=1");
		ext.getVfldsForm().del(SysPerson.T.UPDATED_DATE_TIME,
				SysPerson.T.UPDATED_BY, SysPerson.T.CREATED_BY,
				SysPerson.T.CREATED_DATE_TIME);

		// 在此可以重新设置ext中的VFlds对象的属性
		VFlds vl = ext.getVfldsList();
		vl.moveBefore(SysUser.T.LOGIN_NAME, T.NAME);
		vl.get(T.STATE).setWidthList(80);
		vl.get(SysPerson.T.PE_CARD_NUMB).setWidthList(150);
		vl.setExpandAndHidden("true",T.NICKNAME);
		vl.setExpandAndHidden("true",SysPerson.T.PKEY, SysPerson.T.PE_EMAIL, SysPerson.T.PE_WX,
				SysPerson.T.PE_QQ, SysPerson.T.PE_MERRY, SysPerson.T.HO_TEL,
				SysPerson.T.HO_ADDR, SysPerson.T.HO_ZIP_CODE,
				SysPerson.T.PE_SEX, SysPerson.T.PE_BIRTHDAY,
				SysPerson.T.UPDATED_BY, SysPerson.T.UPDATED_DATE_TIME,
				SysPerson.T.CREATED_BY, SysPerson.T.CREATED_DATE_TIME,SysPerson.T.REM);
		((EMList) ext.newList()).setExtendRow();
		VFlds vs = ext.getVfldsForm();
		vs.moveBefore(SysUser.T.LOGIN_NAME, T.NAME);
		ext.newExts().init();
		//选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vl, T.NAME, new VFlds(T.CODE, T.NAME));
		((EMListTrigger) trigger.newList()).setExtendRow();
		((EMListTrigger) trigger.newList()).setTdCount(4);
		trigger.newExts().init().crtFiles();
		return ext;
	}
}
