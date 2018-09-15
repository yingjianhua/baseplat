package irille.dep.cst;

import irille.pss.cst.CstSalInvoiceRed;
import irille.pss.cst.CstSalInvoiceRedLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtDime;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ECstSalInvoiceRed extends CstSalInvoiceRed{
	public static void main(String[] args) {
		new ECstSalInvoiceRed().crtExt();
	}

	public void crtExt() {
		CmbGoods.TB.getCode();
		CstSalInvoiceRedLine.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.STATUS, T.AMT, T.ORG, T.DEPT, T.APPR_BY, T.APPR_TIME,
		    T.CREATED_BY, T.CREATED_TIME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.AMT, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(CstSalInvoiceRedLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG, T.DEPT, T.CELL, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY, T.CREATED_TIME,
		    T.REM);
		VFlds vs = ext.getVfldsForm();
		vs.del(T.ORG, T.DEPT, T.CELL, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME);
		ext.newExts().init();
		ext.crtFiles();
	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(ECstSalInvoiceRed.class, "initChange", getOutVFlds().get(0).getCode()));
		}

		public void initFuns() {
			AddFun("onChangeStatus", ECstSalInvoiceRed.class).addFunParasExp("status");
			super.initFuns();
		}

		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("ins")) {
				ExtAct v = new ExtAct(this, act, ECstSalInvoiceRed.class);
				v.add(XTYPE, "button").add(TEXT, act.getName()).add(ICON_CLS, act.getIcon())
				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
				getActs().add(v);
			} else {
				super.loadTbAct(funCodeFile, act);
			}
		}
	// 从表信息定义
			public void initColumnsOut() {
				super.initColumnsOut();
				ExtList form = getColumnsOut();
				ExtList l = form.AddFunCall("},{xtype : Ext.create","mvc.view.cst.CstPurInvoice.ListLineOrigin").AddFunParaList();
				l.add("title", "来源单据").addExp("itemId", "this.oldId+'origintable'").add(ICON_CLS, "tab-user-icon");
					
			}
			public void initComponent(ExtFunDefine fun) {
				// 主界面功能定义
				initMainActs(fun);

				ExtDime dime = fun.add("		this.items =").AddDime();
				dime.add(getFormDocked());

				dime.add(getFormTabpanel());

				fun.add(loadFunCode(EMZipList.class, "initComponent2"));
		//@formatter:off	
		/** Begin initComponent2 ********
				this.callParent(arguments);
				this.mdSearch = this.down('#'+this.oldId+'search');
				this.mdAct = this.down('#'+this.oldId+'act');
				this.mdMain = this.down('#'+this.oldId+'main');
				this.mdMainTable = this.down('#'+this.oldId+'maintable');
				this.mdLineTable = this.down('#'+this.oldId+'linetable');
				this.mdOriginTable = this.down('#' + this.oldId + 'origintable');
				mvc.Tools.onENTER2SearchBar(this.mdSearch,this);
				if (mainActs.length == 0)
					this.down('[region=north]').remove(this.mdAct);
		*** End initComponent2 *********/
		//@formatter:on
			}
		//@formatter:off	
		/** Begin onChangeStatus ********
	if (this.roles.indexOf('del') != -1)
		this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('doAppr') != -1)
		this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('unAppr') != -1)
		this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('doNote') != -1)
		this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('doTally') != -1)
		this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('unTally') != -1)
		this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
		*** End onChangeStatus *********/

			/** Begin initChange ********
			{
					scope : this,
	        selectionchange: function(model, records) {
	            if (records.length === 1){
	                this.mdMain.getForm().loadRecord(records[0]);
									this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
									this.mdOriginTable.store.filter([{
												'id' : 'filter',
												'property' : 'mainForm',
												'value' : records[0].get('bean.pkey')+'06202'//销售红字发票汇总单
											}]);
									var status = records[0].get('bean.status'); //根据单据状态判断
									this.onChangeStatus(status);
	            }else{
	            	this.mdMain.getForm().reset();
	            	this.mdLineTable.store.removeAll();
	            	this.mdOriginTable.store.removeAll();
	            	this.onChangeStatus(-1);
	            }
	        }
	    }
	*** End initChange *********/
		
		/** Begin onIns ********
		var win = Ext.create('mvc.view.frm.FrmPending.WinPending',{
			title : this.title+'>新增',
			descUrl : 'cst_CstSalInvoiceRed_ins',
			descType : 6202, //目标单据类型
		});
		win.on('create',this.onSaveRecord,this);
		win.show();
	*** End onIns *********/
		//@formatter:on	

	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		@Override
		public ExtFile newForm() {
			return null;
		}

		@Override
		public ExtFile newZipWin(VFld fld) {
			return null;
		}

		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
		}

	}
}
