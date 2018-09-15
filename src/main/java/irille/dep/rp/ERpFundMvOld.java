package irille.dep.rp;

import irille.gl.rp.RpFundMvOld;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMList;
import irille.pub.html.EMModel;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ERpFundMvOld extends RpFundMvOld {

	public static void main(String[] args) {
		new ERpFundMvOld().crtExt();
	}

	public void crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds = new VFlds(T.SOURCE_JOURNAL, T.DESC_JOURNAL, T.STATUS);
		EMCrt ext = new MyCrt(TB, vflds, searchVflds);
		VFlds lvs = ext.getVfldsList();
		lvs.moveLast(T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.REM);
		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.CODE, T.STATUS, T.ORG, T.CELL, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
		    T.TALLY_TIME, T.TAKE_OVER_TIME, T.DESC_BY, T.TYPE, T.DESC_CELL, T.DESC_ORG);
		fvs.moveLast(T.REM);
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
				v = new ExtAct(this, act, ERpFundMvOld.class, getPack(), getClazz(), getTb().getName());
			} else if (oact == OAct.DO_NOTE) {
				v = new ExtAct(this, act, ERpFundMvOld.class, getTb().getClazz().getName());
			} else if (act.getCode().equals("rec")) {
				v = new ExtAct(this, act, ERpFundMvOld.class, getPack(), getClazz(), getTb().getName());
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

		//@formatter:off	
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
	
		/** Begin onChangeStatus ********
				if (this.roles.indexOf('upd') != -1)
					this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
				if (this.roles.indexOf('del') != -1)
					this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
				if (this.roles.indexOf('doAppr') != -1)
					this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
				if (this.roles.indexOf('unAppr') != -1)
					this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_APPROVE);
				if (this.roles.indexOf('rec') != -1)
					this.down('#'+this.oldId+'rec').setDisabled(status != STATUS_APPROVE);
				if (this.roles.indexOf('doNote') != -1)
					this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
				if (this.roles.indexOf('doTally') != -1)
					this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
				if (this.roles.indexOf('unTally') != -1)
					this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
		 *** End onChangeStatus *********/
		/** Begin initChange ********
    if (selected.length === 1){
				var status = selected[0].get('bean.status'); //根据单据状态判断
				this.onChangeStatus(status);
    }else{
    	this.onChangeStatus(-1);
    }
		 *** End initChange *********/
		//@formatter:on	

		//@formatter:off	
		/** Begin onRec ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if(selection) {
			Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.code') +'] -确认接收吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/【0】_【1】_rec?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
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
  	*** End onRec *********/
  //@formatter:on

		//@formatter:off	
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
			//@formatter:on
	}
}
