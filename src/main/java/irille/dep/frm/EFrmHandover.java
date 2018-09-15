package irille.dep.frm;

import irille.dep.pur.EPurPresent;
import irille.gl.frm.FrmHandover;
import irille.gl.frm.FrmHandoverLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMForm;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EFrmHandover extends FrmHandover {
	public static void main(String[] args) {
		new EFrmHandover().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		FrmHandoverLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.CREATED_BY, T.APPR_BY, T.ORG, T.DEPT) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(FrmHandoverLine.T.PKEY) });
		VFlds vs = ext.getVfldsForm();
		vs.del(T.CODE, T.ORG, T.DEPT, T.STATUS, T.CREATED_BY, T.CREATED_TIME, T.APPR_TIME);// FROM页面删除建档员等字段
		// list字段排序调整
		ext.newExts().init();
		return ext;
	}

	class MyForm extends EMForm<MyForm> {

		public MyForm(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}

		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EMForm.class, "myComponent"));
		}
		// @formatter:off
		/** Begin myComponent ********
		if (this.insFlag)
				this.url = this.url + 'ins';
			else
				this.url = this.url + 'upd';
			var formFlds = [];
			formFlds.push
		(
		mvc.Tools.crtComboTrigger(false,'sys_SysUser','',{
					name : 'bean.apprBy',
					fieldLabel : '接收人'
				})
		,{xtype : 'textfield',name : 'bean.rem',fieldLabel : '备注'}
		,{xtype : 'numberfield',name : 'bean.rowVersion',value : '0',afterLabelTextTpl : required,allowBlank : false,fieldLabel : '版本',hidden : true,allowDecimals : false}
		,{
		xtype : 'hiddenfield',
		name : 'bean.pkey'
		});
		this.items = [{
		layout : {
			type : 'vbox',
			align : 'stretch'
		},
		border : false,
		items : formFlds
		}];
		this.callParent(arguments);
		 *** End myComponent *********/
		// @formatter:on
	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void initFuns() {
			AddFun("onChangeStatus", EFrmHandover.class).addFunParasExp("status");
			super.initFuns();
			//@formatter:off	
			/** Begin onChangeStatus ********
			if (this.roles.indexOf('upd') != -1)
				this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('del') != -1)
				this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('doAppr') != -1)
				this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('unAppr') != -1)
				this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_CHECKED);
			if (this.roles.indexOf('take') != -1)
				this.down('#'+this.oldId+'take').setDisabled(status != STATUS_CHECKED);
				*** End onChangeStatus *********/
			//@formatter:on
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("take") == false)
				return;
			ExtAct v = new ExtAct(this, act, EFrmHandover.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
			getActs().add(v);
		}

		// @formatter:off
		/** Begin onTake ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '交接单['+selection.get('bean.code') + '] - 确认接收吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/frm_FrmHandover_take?pkey='+selection.get('bean.pkey'),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.frm.FrmHandover',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '接收--成功');
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
		*** End onTake *********/
		// @formatter:on

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EFrmHandover.class, "initFormListMainStatus"));
			//@formatter:off	
			/** Begin initFormListMainStatus ********
{
			 								scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
				        								this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
				        								var status = records[0].get('bean.status'); //根据单据状态判断
				        								this.onChangeStatus(status);
				                    }else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	this.onChangeStatus(-1);
				                    }
				                }
			                }
			 *** End initFormListMainStatus *********/
			//@formatter:on
		}
	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;// 后来又加了取默认价、计划金额功能，暂不用产生器产生 TODO
		}

		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}
	}

}
