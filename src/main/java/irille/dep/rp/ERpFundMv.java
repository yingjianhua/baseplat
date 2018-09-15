package irille.dep.rp;

import irille.dep.rp.ERpFundMvOld.MyCrt;
import irille.dep.rp.ERpFundMvOld.MyList;
import irille.gl.rp.RpFundMv;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMList;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class ERpFundMv extends RpFundMv {

	public static void main(String[] args) {
		new ERpFundMv().crtExt();
	}

	public void crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.SOURCE_JOURNAL, T.DESC_JOURNAL, T.STATUS);
		EMCrt ext = new MyCrt(TB, vflds, searchVflds);
		VFlds vf =ext.getVfldsForm().del(T.CODE, T.STATUS, T.ORG, T.CELL, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
		    T.TALLY_TIME,T.SOURCE_BY,T.SOURCE_TIME,T.DESC_BY,T.DESC_TIME);
		vf.moveLast(T.REM);
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.REM);
		ext.newExts().init();
		ext.crtFiles();
	}
	class MyCrt extends EMCrtSimple<MyCrt> {

		public MyCrt(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		@Override
		public ExtFile newList() {
			EMList myList = new MyList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
			myList.setLineActs(false);
			return myList;
		}
	}
	class MyList extends EMList {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			ExtAct v = null;
			System.err.println(act.getCode());
			if (oact == OAct.DO_APPR || oact == OAct.UN_APPR || oact == OAct.DO_TALLY || oact == OAct.UN_TALLY) {
				v = new ExtAct(this, act, ERpFundMv.class, getPack(), getClazz(), getTb().getName());
			} else if (oact == OAct.DO_NOTE) {
				v = new ExtAct(this, act, ERpFundMv.class, getTb().getClazz().getName());
			} else if (act.getCode().equals("doRec")||act.getCode().equals("unRec")) {
				v = new ExtAct(this, act, ERpFundMv.class, getPack(), getClazz(), getTb().getName());
				v.add(TEXT, act.getName()).add(ICON_CLS, "doAppr-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
				return;
			} else {
				super.loadTbAct(funCodeFile, act);
				return;
			}
			v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon()).addExp("itemId", "this.oldId+'" + act.getCode() + "'")
			    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
			getActs().add(v);
		}

		@Override
		public void initFuns() {
			AddFun("onChangeStatus", ERpNotePayBill.class).addFunParasExp("status");
			ExtFunDefine fun = AddList("listeners") // 表格行选择事件，主要设置按钮是否可用
			    .AddFunDefine("selectionchange", new ExtExp("selModel, selected"));
			fun.add(loadFunCode(ERpNotePayBill.class, "initChange"));
			initFunsAddOtherFuns();
			initFunsAddActs();

			add(AddFun("onSearchCancel", EMList.class));
			add(AddFun("onSearch", EMList.class));
			add(AddFun("onSearchAdv", EMList.class, getPack(), getClazz()));
			add(AddFunD("onSearchDo", EMList.class, new ExtExp("array")));
		}
	}
}



//@formatter:off	
/** Begin onChangeStatus ********
if (this.roles.indexOf('upd') != -1)
	this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
if (this.roles.indexOf('del') != -1)
	this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
if (this.roles.indexOf('doAppr') != -1)
	this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
if (this.roles.indexOf('unAppr') != -1)
	this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_APPROVE);
if (this.roles.indexOf('doRec') != -1)
	this.down('#'+this.oldId+'doRec').setDisabled(status != STATUS_APPROVE);
if (this.roles.indexOf('unRec') != -1)
	this.down('#'+this.oldId+'unRec').setDisabled(status != STATUS_TALLY);
if (this.roles.indexOf('doNote') != -1)
	this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
if (this.roles.indexOf('doTally') != -1)
	this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
if (this.roles.indexOf('unTally') != -1)
	this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
*** End onChangeStatus *********/

/** Begin onDoAppr ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 审核确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_approve?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.getSelectionModel().deselectAll();
								me.getView().select(selection);
								Ext.example.msg(msg_title, '审核--成功');
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
*** End onDoAppr *********/
/** Begin onUnAppr ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 弃审确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_unapprove?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.getSelectionModel().deselectAll();
								me.getView().select(selection);
								Ext.example.msg(msg_title, '弃审--成功');
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
*** End onUnAppr *********/
/** Begin onDoRec ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if(selection) {
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') +'] -确认接收吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_doRec?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
							success : function (response, options) {
								var result = Ext.decode(response.responseText);
								if (result.success){
									var bean  = Ext.create('mvc.model.【0】.【1】',result);
									Ext.apply(selection.data, bean.data);
									selection.commit();
									me.getSelectionModel().deselectAll();
									me.getView().select(selection);
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
			})
		}	
 *** End onDoRec *********/
/** Begin onUnRec ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if(selection) {
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') +'] -确认取消接收吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_unRec?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
							success : function (response, options) {
								var result = Ext.decode(response.responseText);
								if (result.success){
									var bean  = Ext.create('mvc.model.【0】.【1】',result);
									Ext.apply(selection.data, bean.data);
									selection.commit();
									me.getSelectionModel().deselectAll();
									me.getView().select(selection);
									Ext.example.msg(msg_title, '取消接收--成功');
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
			})
		}	

 *** End onUnRec *********/
/** Begin onDoNote ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			var win = Ext.create('mvc.view.gl.GlNote.WinNote',{
				title : this.title+'>记账便签',
				insFlag : false,
				tableCode : '【0】'
			});
			win.show();
			win.setActiveRecord(selection);
		}
 *** End onDoNote *********/
/** Begin onDoTally ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 记账确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_tally?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
									Ext.apply(selection.data, bean.data);
									selection.commit();
								me.getSelectionModel().deselectAll();
								me.getView().select(selection);
								Ext.example.msg(msg_title, '记账--成功');
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
 *** End onDoTally *********/
/** Begin onUnTally ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') + '] - 记账取消确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_untally?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.【0】.【1】',result);
									Ext.apply(selection.data, bean.data);
									selection.commit();
								me.getSelectionModel().deselectAll();
								me.getView().select(selection);
								Ext.example.msg(msg_title, '记账取消--成功');
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
 *** End onUnTally *********/
//@formatter:on