/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMList;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class ESysOrg extends SysOrg {
	public static void main(String[] args) {
		new ESysOrg().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds one = new VFlds("one").addWithout(SysCom.TB, SysCom.T.PKEY, SysCom.T.NAME, SysCom.T.SHORT_NAME);
		VFlds[] vflds = new VFlds[] { new VFlds(TB), one };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME) };
		EMCrt ext = new EMCrtSimpleTwo(TB, vflds, searchVflds);
		((VFldOutKey) ext.getVfldsForm().get(T.TEMPLAT)).setDiySql("type=1");
		ext.getVfldsForm().get(T.CODE).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.ORG_UP).setReadOnly("!this.insFlag");
		ext.getVfldsForm().del(T.ENABLED, T.WORK_DATE, T.STATE);
		ext.getVfldsForm().del(SysCom.T.CREATED_BY, SysCom.T.CREATED_DATE_TIME, SysCom.T.UPDATED_BY,
		    SysCom.T.UPDATED_DATE_TIME, SysCom.T.ROW_VERSION);
		// 在此可以重新设置ext中的VFlds对象的属性
		ext.setVfldsList(vflds[0], one);
		VFlds vl = ext.getVfldsList();
		vl.get(T.CODE).setWidthList(70);
		vl.get(T.STATE).setWidthList(70);
		vl.get(T.NAME).setWidthList(150);
		vl.get(SysCom.T.TEL1).setWidthList(120);
		vl.get(SysCom.T.FAX).setWidthList(120);
		vl.get(SysCom.T.ADDR).setWidthList(200);
		vl.setExpandAndHidden("true", T.ORG_UP, T.TEMPLAT, T.INTERNATION_TRADE, T.CURRENCY, T.VALUATION_METHODS);
		vl.setExpandAndHidden("true", SysCom.T.TEL2, SysCom.T.WEBSITE, SysCom.T.ZIP_CODE, SysCom.T.REM,
		    SysCom.T.UPDATED_BY, SysCom.T.UPDATED_DATE_TIME, SysCom.T.CREATED_BY, SysCom.T.CREATED_DATE_TIME);
		((EMList) ext.newList()).setExtendRow();

		ext.newExts().init();
		return ext;

	}
//@formatter:off	
	/** Begin onDed ********
	
 *** End onDed *********/

	//@formatter:on	
}
