/**
 * 
 */
package irille.dep.rp;

import irille.gl.rp.RpJournal;
import irille.gl.rp.RpJournalLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMWinTwoRow;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListLine;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ERpJournal extends RpJournal {
	public static void main(String[] args) {
		new ERpJournal().crtExt();
	}

	public void crtExt() {
		RpJournalLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.ENABLED, T.BALANCE, T.TYPE, T.CASHIER) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME, T.CASHIER) }; // 搜索栏字段
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(
		    RpJournalLine.T.PKEY) });
		VFlds lvs = ext.getVfldsList();
		lvs.get(T.PKEY).setHidden("true");
		lvs.moveLast(T.TYPE, T.ENABLED);
		lvs.moveAfter(T.BALANCE, T.NAME);
		lvs.get(T.CODE).setWidthList(120);
		lvs.get(T.NAME).setWidthList(150);
		VFlds fvs = ext.getVfldsForm();
		fvs.get(T.WORK_BOX).setReadOnly("!this.insFlag");
		fvs.get(T.REM).attrs().addExp("width", "560");
		fvs.get(T.REM).attrs().addExp("colspan", "2");
		fvs.moveAfter(T.BANK_ACC_CODE, T.BANK_ACC_NAME);
		fvs.del(T.YESTODAY_BALANCE, T.BALANCE, T.DR_AMT, T.DR_QTY, T.CR_AMT, T.CR_QTY, T.CASHIER, T.ORG, T.CELL, T.PKEY, T.ENABLED, T.NAME, T.CODE);
		ext.newExts().init();
		ext.crtFiles();
		EMCrt extTrig = new EMCrtTrigger(TB, lvs, T.NAME, new VFlds(T.CODE, T.NAME));
		extTrig.newExts().init().crtFiles();
	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(
			    getSearchVflds());
		}

		@Override
		public ExtFile newWin() {
			return new EMWinTwoRow(getTb());
		}

		@Override
		public ExtFile newForm() {
			return null;
		}

		public void crtOutFld(VFld fld) {
			super.crtOutFld(fld);
			VFlds vline = getNowOutKeyLineVflds();
			vline.get(RpJournalLine.T.ORG).setHidden("true");
			vline.get(RpJournalLine.T.CELL).setHidden("true");
			vline.get(RpJournalLine.T.JOURNAL).setHidden("true");
			vline.get(RpJournalLine.T.SUMMARY).setWidthList(190);
			addExt(new EMZipListLine(getTb(), fld, getNowOutKeyLineVflds()));
			addExt(new EMModel((Tb) fld.getFld().getTb(), getNowOutKeyModelVflds()));
			addExt(new EMStore((Tb) fld.getFld().getTb()));
			addExt(newWin());
		}
		
	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create",
			    "mvc.view." + getPack() + "." + getClazz() + ".ListMain").AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'")
			    .add(ICON_CLS, "tab-user-icon").addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(ERpJournal.class, "initFormListMain"));
			//@formatter:off	
						/** Begin initFormListMain ********
								 {
									scope : this,
					                selectionchange: function(model, records) {
					                    if (records.length === 1){
					                        this.mdMain.getForm().loadRecord(records[0]);
	        								this.mdLineTable.store.filter([{'id':'filter', 'property':'journal','value':records[0].get('bean.pkey')}]);
	    									if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(false);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(false);
											if (this.roles.indexOf('doEnabled') != -1)
												this.down('#'+this.oldId+'doEnabled').setDisabled(false);	
											if (this.roles.indexOf('unEnabled') != -1)
												this.down('#'+this.oldId+'unEnabled').setDisabled(false);	
					                    }else{
					                    	this.mdMain.getForm().reset();
					                    	this.mdLineTable.store.removeAll();
					                    	if (this.roles.indexOf('upd') != -1)
												this.down('#'+this.oldId+'upd').setDisabled(true);
											if (this.roles.indexOf('del') != -1)
												this.down('#'+this.oldId+'del').setDisabled(true);
											if (this.roles.indexOf('doEnabled') != -1)
												this.down('#'+this.oldId+'doEnabled').setDisabled(true);	
											if (this.roles.indexOf('unEnabled') != -1)
												this.down('#'+this.oldId+'unEnabled').setDisabled(true);	
					                    }
					                }
				                }
						*** End initFormListMain *********/
			
						//@formatter:on
		}
	}
}
