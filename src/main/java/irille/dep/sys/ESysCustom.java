/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysCom;
import irille.core.sys.SysCustom;
import irille.core.sys.SysCustomOrg;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysTemplatCell;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtEdit;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMList;
import irille.pub.html.EMListEdit;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.ExtFile;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 
 * @author whx
 * @version 创建时间：2014年11月13日 下午3:25:14
 */
public class ESysCustom extends SysCustom {
	IEnumFld rva = SYS.AMT;
	public static void main(String[] args) {
		new ESysCustom().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		
		rva.getFld().setName("应收款").setCode("rvaAmt");
		VFlds one = new VFlds("one").addWithout(SysCom.TB, SysCom.T.PKEY, SysCom.T.NAME, SysCom.T.SHORT_NAME,
		    SysCom.T.ROW_VERSION);
		VFlds[] vflds = new VFlds[] { new VFlds(TB).del(T.REM), one};
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.ENABLED) };
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		ext.getVfldsForm().del(SysCom.T.UPDATED_BY, SysCom.T.UPDATED_DATE_TIME, SysCom.T.CREATED_BY,
		    SysCom.T.CREATED_DATE_TIME, T.ENABLED);
		VFlds vl = ext.getVfldsList();
		vl.add(rva);
		vl.moveAfter(rva, T.SHORT_NAME);
		vl.get(T.NAME).setWidthList(180);
		vl.get(T.COM_PERSON_FLAG).setWidthList(65);
		vl.setExpandAndHidden("true", SysCom.T.TEL2, SysCom.T.FAX, SysCom.T.WEBSITE, SysCom.T.ADDR, SysCom.T.ZIP_CODE,
		    SysCom.T.REM, SysCom.T.UPDATED_BY, SysCom.T.UPDATED_DATE_TIME, SysCom.T.CREATED_BY, SysCom.T.CREATED_DATE_TIME);
		((EMList) ext.newList()).setExtendRow();
		ext.newExts().init().crtFiles();

		// 下面是编辑相关JS产生
		VFlds editVflds = new VFlds().addWithout(SysCustom.TB, SysCustom.T.PKEY, SysCustom.T.REM);
		VFlds[] editOuts = new VFlds[] {
		    new VFlds().add(SysPersonLink.TB, SysPersonLink.T.TYPE, SysPersonLink.T.NAME, SysPersonLink.T.PE_MOBILE,
		        SysPersonLink.T.PE_EMAIL, SysPersonLink.T.OF_FAX, SysPersonLink.T.OF_ADDR),
		    new VFlds(SysCustomOrg.TB)};
		VFld[] outflds = new VFld[] { SysPersonLink.T.TB_OBJ_LONG.getFld().getVFld(),
		    SysCustomOrg.T.CUSTOM.getFld().getVFld() };
		editOuts[0].get(SysPersonLink.T.PE_MOBILE).setName("手机");
		editOuts[0].get(SysPersonLink.T.PE_EMAIL).setName("邮箱");
		editOuts[0].get(SysPersonLink.T.OF_FAX).setName("传真");
		editOuts[0].get(SysPersonLink.T.OF_ADDR).setName("地址");
		EMCrt extEdit = new MyEdit(TB, editVflds, editOuts, outflds);
		extEdit.newExts().init().crtFiles();

		return ext;
	}
	public class MyComp extends EMCrtSimpleTwo {
		public MyComp(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
			super(tb, vflds, searchVflds);
		}
		
		public ExtFile newModel() {
			return new EMModel(getTb(), getVfldsModel(), new VFlds(rva));
		}
	}

	public static class MyEdit extends EMCrtEdit {

		public MyEdit(Tb tb, VFlds vflds, VFlds[] outVflds, VFld[] outs) {
			super(tb, vflds, outVflds, outs);
		}

		public void crtOutFld(VFld out, VFlds flds) {
			//个人信息的编辑GRID作了特殊处理，不自动产生 --> +me.mainPkey+'&itemId='+me.itemId,
			if (out.getTb().getCode().equals(SysPersonLink.TB.getCode()) == false) {
				EMListEdit el = new EMListEdit((Tb) out.getTb(), out, flds);
				el.getVfld(SysCustomOrg.T.ORG).setWidthList(150);
				el.getVfld(SysCustomOrg.T.DEPT).setWidthList(150);
				addExt(el);
			}
			addExt(new EMModel((Tb) out.getFld().getTb(), new VFlds().addAll(out.getFld().getTb())));
			addExt(new EMStore((Tb) out.getFld().getTb()));
			crtOpt((Tb) out.getFld().getTb());
		}

	}
	
}
