package irille.dep.rp;

import irille.gl.rp.RpHandoverLine;
import irille.gl.rp.RpWorkBox;
import irille.gl.rp.RpWorkBoxGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMWin;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ERpWorkBox extends RpWorkBox {

	public static void main(String[] args) {
		new ERpWorkBox().crtExt();
	}

	public void crtExt() {
		RpWorkBoxGoods.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.NAME, T.ENABLED, T.TYPE, T.USER_SYS, T.MNG_CELL) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.USER_SYS, T.NAME) }; // 搜索栏字段
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(
		    RpWorkBoxGoods.T.PKEY) });

		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.ENABLED, T.MNG_CELL);
		fvs.setReadOnly("!this.insFlag", T.USER_SYS);
		ext.newExts().init();
		ext.crtFiles();
		EMCrtTrigger extT = new EMCrtTrigger(TB, vflds[0], T.NAME, new VFlds(T.NAME, T.USER_SYS));
		extT.newExts().init().crtFiles();
		
		EMCrtTrigger extGoods = new EMCrtTrigger(RpWorkBoxGoods.TB, new VFlds(RpWorkBoxGoods.TB),
				RpWorkBoxGoods.T.BOX_GOODS, new VFlds(RpWorkBoxGoods.T.NAME));
		extGoods.newExts().init().crtFiles();
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

		public ExtFile newZipWin(VFld fld) {
			return new EMWin(getTb());
		}

		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
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
			l.addExp("listeners", loadFunCode(ERpWorkBox.class, "initFormListMain"));
			//@formatter:off	
						/** Begin initFormListMain ********
								 {
									scope : this,
					                selectionchange: function(model, records) {
					                    if (records.length === 1){
					                        this.mdMain.getForm().loadRecord(records[0]);
	        								this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
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
