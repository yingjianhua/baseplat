package irille.dep.gs;

import irille.gl.gs.GsPhyinv;
import irille.gl.gs.GsPhyinvGoodsLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class EGsPhyinv extends GsPhyinv {

	public static void main(String[] args) {
		new EGsPhyinv().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		GsPhyinvGoodsLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.WAREHOUSE, T.STATUS,
				T.ORG, T.DEPT) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.WAREHOUSE, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(GsPhyinvGoodsLine.T.PKEY) });

		VFlds vs = ext.getVfldsForm();
		vs.del(T.PKEY, T.CODE, T.ORG, T.DEPT, T.STATUS, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.FINI_TIME, T.UNMATCH_NUM, T.UNMATCH_AMT);// FROM页面删除建档员等字段
		vs.moveAfter(T.REM, T.COUNTED_BY);// FORM页面的各字段位置调整
		//list字段排序调整
		VFlds vsl = ext.getVfldsList();
		vsl.moveLast(T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.REM);

		ext.newExts().init();
		return ext;
	}
	
	/**
	 * 重构LIST，加入【盘点】和【产生】按钮的支持 loadTbAct是初始化控件时加入按钮及其功能代码；
	 * initFormListMain是重写主表的行选择事件，加上对【盘点】和【产生】按钮的状态设置；
	 * 
	 * @author WHX
	 * @version 创建时间：2014年12月16日 下午3:11:55
	 */
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("produce")) {
				ExtAct v = new ExtAct(this, act, EGsPhyinv.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				.add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
			} else if (act.getCode().equals("inv")) {
				ExtAct v = new ExtAct(this, act, EGsPhyinv.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				.add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
			} else
				return;
		}
		
		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EGsIn.class).addFunParasExp(
					"status");
			AddFun("onInvRecord", EGsIn.class).addFunParasExp(
					"form, data");
			AddFun("onInvWin", EGsIn.class).addFunParasExp(
					"selection");
			super.initFuns();
		}

		//@formatter:off	
		/** Begin onProduce ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '盘点单['+selection.get('bean.code') + '] - 确认产生盘盈盘亏单吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/gs_GsPhyinv_produce?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.gs.GsPhyinv',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '产生--成功');
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
		*** End onProduce *********/
		/** Begin onInv ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		this.onInvWin(selection);
		*** End onInv *********/
		/** Begin onInvRecord ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var bean = Ext.create('mvc.model.gs.GsPhyinv', data);
		Ext.apply(selection.data,bean.data);
		selection.commit();
		this.mdMainTable.getView().select(selection);
		*** End onInvRecord *********/
		/** Begin onInvWin ********
		if (selection){
			mvc.model.gs.GsPhyinv.load(selection.get('bean.pkey'), {
				scope : this,
				failure : function(response, operation) {
					Ext.example.msg(msg_title,msg_ajax);
				},
				success : function(response, operation) {
					Ext.apply(selection.data,response.data);
					var win = Ext.create('mvc.view.gs.GsPhyinv.InvWin',{
						title : this.title+'>盘点',
						insFlag : false,
						pkey : selection.get('bean.pkey')
					});
					win.on('create',this.onInvRecord,this);
					win.show();
					win.setActiveRecord(selection);
				}
			});
		}
		*** End onInvWin *********/
	//@formatter:on	

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EGsPhyinv.class, "initFormListMainStatus", getOutVFlds().get(0).getCode()));
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
	if (this.roles.indexOf('inv') != -1)
		this.down('#'+this.oldId+'inv').setDisabled(status != STATUS_CHECKED);
	if (this.roles.indexOf('produce') != -1)
		this.down('#'+this.oldId+'produce').setDisabled(status != STATUS_CHECKED);
					*** End onChangeStatus *********/
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
	
	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * 
	 * @author WHX
	 * @version 创建时间：2014年12月16日 下午3:11:55
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

//		@Override
//		public VFlds[] getNowOutKeyZipListFormVflds() {
//			VFlds[] vs = super.getNowOutKeyZipListFormVflds();
//			vs[0].del(SalOrderLine.T.QTY_OPEN, SalOrderLine.T.AMT_COST);
//			return vs;
//		}

	}
}
