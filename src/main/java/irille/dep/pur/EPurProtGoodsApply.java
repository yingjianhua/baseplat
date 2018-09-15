/**
 * 
 */
package irille.dep.pur;

import irille.pss.pur.PurProtGoodsApply;
import irille.pss.pur.PurProtGoodsApplyLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMForm;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMZipListLine;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * 过滤了form文件
 * @author administrator
 *
 */
public class EPurProtGoodsApply extends PurProtGoodsApply {
	public static void main(String[] args) {
		new EPurProtGoodsApply().crtExt();
	}

	public void crtExt() {
		PurProtGoodsApplyLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		CmbGoods.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.NAME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.STATUS) }; // 搜索栏字段
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(PurProtGoodsApplyLine.T.PKEY) });
		VFlds lvs = ext.getVfldsList();
		lvs.moveLast( T.CREATED_BY, T.CREATED_TIME,
				T.APPR_BY, T.APPR_TIME, T.ORG, T.DEPT, T.REM);
		ext.newExts().init();
		ext.crtFiles();
	}
	class MyWin extends EMZipWin<MyWin> {

		public MyWin(Tb tb, VFld outFld) {
			super(tb, outFld);
		}
		

	/** Begin onClose ********
	this.close();
	*** End onClose *********/
		public void initForm() {
			//EMWin.initForm(getForm(), getPack(), getClazz());
			getForm()
			.add(ANCHOR, "100%")
			.add("plain", true)
			.addExp(XTYPE,
					"Ext.create('mvc.view."
							+ getPack()
							+ "."
							+ getClazz()
							+ ".Form',{	insFlag : this.insFlag})");
			getFormList()
					.addExp(XTYPE,
							"Ext.create('mvc.view."
									+ getPack()
									+ ".PurProtGoodsApplyLine.List',{height : 300,border : false,itemId : 'PurProtGoodsApplyLine',parent : this})");
		}
	}
	
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds,
				VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newForm() {
			return null;
		}
		
		@Override
		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		@Override
		public void crtOutFld(VFld fld) {
			addExt(new EMZipListLine(getTb(), fld, getNowOutKeyLineVflds()));
			addExt(new EMModel((Tb) fld.getFld().getTb(),
					getNowOutKeyModelVflds()));
			addExt(new EMStore((Tb) fld.getFld().getTb()));
			if (isCrtWinAndForm()) {
				addExt(newZipWin(fld));
				// 过滤listForm文件
				// addExt(new
				// EMZipListForm(getTb(),fld,getNowOutKeyZipListFormVflds()));
			}
		}
	}

}
