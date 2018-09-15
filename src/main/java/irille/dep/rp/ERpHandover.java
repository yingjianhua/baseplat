/**
 * 
 */
package irille.dep.rp;

import irille.gl.rp.RpHandover;
import irille.gl.rp.RpHandoverLine;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMZipListMain;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ERpHandover extends RpHandover {
	public static void main(String[] args) {
		new ERpHandover().crtExt();
	}

	public void crtExt() {
		RpHandoverLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.STATUS, T.TAKE_OVER_TIME, T.SOURCE,
		    T.DESC_BY, T.WORK_BOX) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SOURCE, T.DESC_BY) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(
		    RpHandoverLine.T.PKEY) });
		VFlds lvs = ext.getVfldsList();
		lvs.moveLast(T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
		    T.TALLY_TIME, T.REM);
		lvs.setExpandAndHidden("true", T.CREATED_BY,T.CREATED_TIME,T.APPR_BY,T.APPR_TIME,T.TALLY_BY,T.TALLY_TIME,T.REM);
		((EMZipListMain) ext.newZipListMain()).setExtendRow();
		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.CODE, T.STATUS, T.ORG, T.CELL, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY,
		    T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.TAKE_OVER_TIME, T.DESC_BY, T.WORK_BOX_NAME);
		fvs.get(T.GIVE_UP_TIME).attrs().addExp("value", "new Date()");
		fvs.moveLast(T.REM);
		ext.newExts().init();
		ext.crtFiles();
	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newForm() {
			// TODO Auto-generated method stub
			return new MyForm(getTb(), getVfldsForm());
		}

		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	class MyForm extends EMFormTwoRow<MyForm> {
		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			addExp("frist", "true");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(RpHandover.T.SOURCE.getFld().getCode())) {
				fldList.setCloseStr(null);
				fldList.add(new MyUser());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

		class MyUser implements IExtOut {

			public String toString(int tabs) {
				return null;
			}

			public void out(int tabs, StringBuilder buf) {
				buf.append(new EMModel(RpHandover.TB).loadFunCode(ERpHandover.class, "MyForm"));
			}
		}

		//@formatter:off	
		/** Begin MyForm ********
		mvc.Tools.crtComboTrigger(false,'sys_SysUser','',{
					name : 'bean.source',
					fieldLabel : '交出人',
						listeners :{
							scope : this,
							change : function(field,newv,oldv,opts) {
								if(!newv && this.frist)
									return;
								if(!this.frist  || this.insFlag){
									this.up('panel').down('grid').getStore().removeAll();
								}
								this.frist = false;
							}
						}
				})
  		*** End MyForm *********/
  		//@formatter:on	
	}
	
}
