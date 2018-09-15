package irille.dep.rp;

import irille.gl.gl.GlNoteViewRp;
import irille.gl.rp.RpStimateRec;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class ERpStimateRec extends RpStimateRec {

	public static void main(String[] args) {
		new ERpStimateRec().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GlNoteViewRp.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.NAME, T.DEPT, T.ORG) };
		VFlds[] mainVflds = new VFlds[] { new VFlds(T.ORIG_FORM, T.ORIG_FORM_NUM, T.AMT, T.BALANCE, T.CLEAR_TIME, 
		    T.CELL, T.ORG, T.CREATED_BY, T.CREATED_TIME) };
		VFlds[] outVflds = new VFlds[] { new VFlds(GlNoteViewRp.T.BILL) };
		EMCrt ext = new MyComp(TB, vflds, mainVflds, searchVflds, outVflds);
		ext.newExts().init();
		return ext;
	}

	class MyList extends EMZipList {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void initFuns() {
			AddFun("onDoProcRecord", ERpStimateRec.class).addFunParasExp("form, data");
			super.initFuns();
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			if (act.getCode().equals("doProc") == false && act.getCode().equals("unProc") == false)
				return;
			ExtAct v = new ExtAct(this, act, ERpStimateRec.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
			getActs().add(v);
		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(ERpStimateRec.class, "selectionchange", getOutVFlds().get(0).getCode()));
		}

		//@formatter:off	
			/** Begin onDoProcRecord ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var bean = Ext.create('mvc.model.rp.RpStimateRec', data);
		Ext.apply(selection.data,bean.data);
		selection.commit();
		this.mdMainTable.getSelectionModel().deselectAll();
		this.mdMainTable.getView().select(selection);
		Ext.example.msg(msg_title, msg_text);
			*** End onDoProcRecord *********/
			
			/** Begin onDoProc ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		if (selection){
			var win = Ext.create('mvc.view.rp.RpStimateRec.WinProc',{
				title : this.title+'>收款',
			});
			win.on('create',this.onDoProcRecord,this);
			win.show();
			win.setBalance(selection.get('bean.pkey'),selection.get('bean.balance'));
		}
			*** End onDoProc *********/
		
		/** Begin selectionchange ********
{
								scope : this,
					           selectionchange: function(model, records) {
					               if (records.length === 1){
					                  this.mdMain.getForm().loadRecord(records[0]);
														this.mdLineTable.store.filter([{'id':'filter', 'property':'bill','value':records[0].get('bean.origForm')}]);
														var amt = records[0].get('bean.amt');
														var balance = records[0].get('bean.balance');
														if (this.roles.indexOf('doProc') != -1 && balance!=0)
															this.down('#'+this.oldId+'doProc').setDisabled(false);
														if (this.roles.indexOf('unProc') != -1 && balance!=amt)
															this.down('#'+this.oldId+'unProc').setDisabled(false);
					               }else{
						               	this.mdMain.getForm().reset();
						               	this.mdLineTable.store.removeAll();
						               	if (this.roles.indexOf('doProc') != -1)
															this.down('#'+this.oldId+'doProc').setDisabled(true);
						               	if (this.roles.indexOf('unProc') != -1)
															this.down('#'+this.oldId+'unProc').setDisabled(true);
					               }
					           }
					       }
		 *** End selectionchange *********/
		
		
		/** Begin onUnProc ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '收款待处理单据['+selection.get('bean.origFormNum') + '] - 确认取消收款吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/rp_RpStimateRec_unProc?pkey='+selection.get('bean.pkey'),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								selection.set("bean.balance", selection.get("bean.amt"));
								selection.set("bean.clearTime", null);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '取消收款--成功');
							}else{
								Ext.MessageBox.show({
									title : msg_title, 
									msg : result.msg,
									buttons : Ext.MessageBox.OK,
									icon : Ext.MessageBox.ERROR
								});
							}
						}
					});
				}
			);
		}
		*** End onUnProc *********/
		//@formatter:on	

	}

	class MyComp extends EMCrtCompSimple {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newList() {
			return new MyList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}
		
		public VFlds getNowOutKeyLineVflds() {
			VFlds vflds = super.getNowOutKeyLineVflds();
			vflds.get(GlNoteViewRp.T.DIRECT).setWidthList(70);
			vflds.get(GlNoteViewRp.T.STATUS).setWidthList(70);
			vflds.get(GlNoteViewRp.T.TYPE_DES).setWidthList(150);
			return vflds;
		}

	}

}
