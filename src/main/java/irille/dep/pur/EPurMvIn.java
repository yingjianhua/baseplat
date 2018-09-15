/**
 * 
 */
package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.dep.sal.ESalMvOut;
import irille.pss.pur.PurMvIn;
import irille.pss.pur.PurMvInLine;
import irille.pss.pur.PurOrderDirect.T;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListForm;
import irille.pub.html.EMZipListMain;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EPurMvIn extends PurMvIn {
	public static void main(String[] args) {
		new EPurMvIn().crtExt();
	}

	public void crtExt() {
		PurMvInLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		CmbGoods.TB.getCode();
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
		    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds(TB) ,ship};
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.STATUS, T.AMT,T.WAREHOUSE, T.ORG_OTHER,
		    T.CELL_OTHER, T.FROM_TYPE, T.GOODS_TO, T.ORG) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.STATUS, T.ORG_OTHER) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(
		    PurMvInLine.T.PKEY) });
		VFlds lvs = ext.getVfldsList();
		lvs.del(T.PKEY, T.AMT_COST, T.SHIPING);
		lvs.moveLast(T.REM, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME);
		lvs.setExpandAndHidden("true", T.DEPT, T.CELL,
				T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
				T.CREATED_TIME, T.REM);
		lvs.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE,
				SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN);
		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.CODE, T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME,
		    T.AMT_COST, T.OUT_FORM, T.ORG_OTHER, T.MV_TYPE, T.CELL, T.GOODS_TO, T.SHIPING, T.TALLY_BY, T.TALLY_TIME);
		fvs.setHidden("true", T.WAREHOUSE_OTHER, T.FROM_TYPE, T.ORIG_FORM, T.ORIG_FORM_NUM);
		fvs.get(T.CELL_OTHER).setReadOnly("this.chkFlag");
		fvs.get(T.WAREHOUSE).setReadOnly("this.insFd");
		ext.getVfldsForm().setHidden("true", SysShiping.T.TIME_ARR_PLAN,
		    SysShiping.T.TIME_SHIP_PLAN, SysShiping.T.NAME, SysShiping.T.ADDR, SysShiping.T.MOBILE,
		    SysShiping.T.TEL);
		fvs.setReadOnly("true", T.AMT);
		fvs.moveLast(T.REM);
		fvs.moveLast(T.SHIPING_MODE);
		ext.newExts().init();
		ext.crtFiles();
		// ext.backupFiles();
		// ext.crtFilesAndCompBackup();
	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newList() {
			// TODO Auto-generated method stub
			return new MyList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(
			    getSearchVflds());
		}

		@Override
		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}

		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			// TODO Auto-generated method stub
			// return null;
			// VFlds lfv = vflds[0];
			// lfv.del(PurMvInLine.T.PRICE, PurMvInLine.T.AMT,
			// PurMvInLine.T.AMT_COST);
			return new MyListForm(tb, outfld, vflds);
		}

		@Override
		public ExtFile newZipListMain() {
			// TODO Auto-generated method stub
			return new MyListMain(getTb(), getVfldsList());
		}

	}

	class MyListForm extends EMZipListForm<MyListForm> {

		public MyListForm(Tb tb, VFld outfld, VFlds[] vflds) {
			super(tb, outfld, vflds);
			// TODO Auto-generated constructor stub
			addExp("insFd", "false");
			addExp("chkFlag", "false");
		}

		@Override
		public void initComponent(ExtFunDefine fun) {
			// 主界面功能定义
			fun.add(loadFunCode(EPurMvIn.class, "initComponentGoods"));
			//@formatter:off	
				/** Begin initComponentGoods ********
	if(!this.insFd && !this.chkFlag){		
		var mainActs = [{
			text : '新增',
			iconCls : 'ins-icon',
			scope : this,
			handler : this.onIns
		},{
			text : '删除',
			iconCls : 'del-icon',
			scope : this,
			handler : this.onDel
		}];
		this.tbar = mainActs;
		this.columns =[		
		{text : '货物', width : 100, dataIndex : 'bean.goods', sortable : true, renderer : mvc.Tools.beanRenderer(), 
				editor: {
					xtype : 'comboautocell',
					listConfig : {minWidth:250},
					fields : ['pkey','code','name'],//查询返回信息model
					valueField : ['pkey'],//提交值
					textField : 'code', //显示信息
					queryParam : 'code',//搜索使用
					name : 'goods', //提交使用
					url : base_path + '/gs_GsGoods_autoComplete',
					urlExt : 'gs.GsGoods',
					hasBlur : false,
					grid : this
		}}
		,{text : '数量',width : 100,dataIndex : 'bean.qty',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right',editor : {xtype : 'numberfield',decimalPrecision : 4}}
		,{text : '单位',width : 50,dataIndex : 'bean.uom',sortable : true,renderer : mvc.Tools.beanRenderer(),editor : {xtype : 'beantriggercell',bean : 'GsUom',beanType : 'gs',beanName : 'bean.uom',grid : this,emptyText : form_empty_text}}
		,{text : '单价',width : 100,dataIndex : 'bean.price',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right',editor : {xtype : 'numberfield',decimalPrecision : 4}}
		,{text : '金额',width : 100,dataIndex : 'bean.amt',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right',editor : {xtype : 'numberfield',decimalPrecision : 2}}
		];
	}else{
		this.columns =[		
		{text : '货物', width : 100, dataIndex : 'bean.goods', sortable : true, renderer : mvc.Tools.beanRenderer()}
		,{text : '数量',width : 100,dataIndex : 'bean.qty',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right'}
		,{text : '单位',width : 50,dataIndex : 'bean.uom',sortable : true,renderer : mvc.Tools.beanRenderer()}
		,{text : '单价',width : 100,dataIndex : 'bean.price',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right',editor : {xtype : 'numberfield',decimalPrecision : 4}}
		,{text : '金额',width : 100,dataIndex : 'bean.amt',sortable : true,renderer : mvc.Tools.numberRenderer(4),align : 'right'	}
		];
	}
	
	mvc.Tools.doGoodsLine(this.columns, 1);				
	this.store=Ext.create('mvc.store.pur.PurMvInLine');
				this.store.pageSize = 0;
				this.store.remoteFilter = true;
				this.store.proxy.filterParam = 'filter';
				this.plugins = [this.cellEditing];
				this.on('edit', function(editor, e) {
					if (e.field == 'bean.goods'){
						if (this.oldGoods != e.value){ //值变更后触发
							mvc.Tools.onLoadInfo(e.value, e.record, this);
						}
					}
					if (e.field == 'bean.price'){
						var price = Number(e.value);
						var num = Number(e.record.get('bean.qty'));
						var amt = price * num;
						e.record.set('bean.amt',amt);
						var amtAll=0;
						this.store.each(function(recode){
							amtAll = amtAll + Number(recode.get('bean.amt'));
						});
						this.up('panel').down('[name=bean.amt]').setValue(amtAll);
					}
					if (e.field == 'bean.qty'){
						var num = Number(e.value);
						var price = Number(e.record.get('bean.price'));
						var amt = price * num;
						e.record.set('bean.amt',amt);
						var amtAll=0;
						this.store.each(function(recode){
							amtAll = amtAll + Number(recode.get('bean.amt'));
						});
						this.up('panel').down('[name=bean.amt]').setValue(amtAll);
					}
				});
				this.on('beforeedit', function(editor, e) {
					this.diySql = null;
					if (e.field == 'bean.goods')
						this.oldGoods = e.value;
					else if (e.field == 'bean.uom' && e.value){//CELL-EDITOR对象找不到，暂只能把参数存储到GRID中
						var s = e.record.get('bean.uom').split(bean_split);
						this.diySql = 'uom_type = (select uom_type from gs_uom where pkey='+s[0]+')';
					}
				});
				this.callParent(arguments);	
				 *** End initComponentGoods *********/
			//@formatter:on
		}

	}

	class MyListMain extends EMZipListMain<MyListMain> {

		public MyListMain(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			setExtendRow();
		}

		@Override
		public void initFuns() {
			// 为兼容原来的代码产生器，按原顺序产生各Function, 否则可以直接用AddFun方法增加新的Function，并调用超类的本方法即可
			if (super.isUpd()) {
				ExtFunDefine onUpdateRecord = AddFun("onUpdateRecord", EMZipListMain.class, getPack(),
				    getClazz()).addFunParasExp("form, data");
				ExtFunDefine onUpdWin = AddFun("onUpdWin", EPurMvIn.class, getPack(), getClazz())
				    .addFunParasExp("selection");

				add(onUpdateRecord);
				add(getAct(OAct.UPD_ROW).getFun());
				add(onUpdWin);
			}
			if (super.isDel()) {
				add(getAct(OAct.DEL_ROW).getFun());
			}
			ExtFunDefine onChkWin = AddFun("onChkWin", EPurMvIn.class, getPack(), getClazz())
			    .addFunParasExp("selection");
			add(onChkWin);
		}

		//@formatter:off	
		/** Begin onUpdWin ********
		if (selection){
			var fd = selection.get('bean.fromType');
			mvc.model.【0】.【1】.load(selection.get('bean.pkey'), {
				scope : this,
				failure : function(response, operation) {
					Ext.example.msg(msg_title,msg_ajax);
				},
				success : function(response, operation) {
					Ext.apply(selection.data,response.data);
					var win = Ext.create('mvc.view.【0】.【1】.Win',{
						title : this.title+'>修改',
						insFlag : false,
						insFd : fd != 3
					});
					win.on('create',this.onUpdateRecord,this);
					win.show();
					win.setActiveRecord(selection);
				}
			});
		}
		*** End onUpdWin *********/
		
		/** Begin onChkWin ********
		if (selection){
			mvc.model.【0】.【1】.load(selection.get('bean.pkey'), {
				scope : this,
				failure : function(response, operation) {
					Ext.example.msg(msg_title,msg_ajax);
				},
				success : function(response, operation) {
					Ext.apply(selection.data,response.data);
					var cell = selection.get('bean.cell').split(bean_split)[0];
					var win = Ext.create('mvc.view.【0】.【1】.Win',{
						title : this.title+'>确认',
						insFlag : false,
						chkFlag : true,
						wsql : 'cell = '+ cell
					});
					win.on('create',this.onUpdateRecord,this);
					win.show();
					win.setActiveRecord(selection);
				}
			});
		}
		*** End onChkWin *********/
		//@formatter:on	

	}

	class MyList extends EMZipList<MyList> {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		
		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EPurMvIn.class).addFunParasExp("status,ordStatus,fromType");
			AddFun("onInsFd", EPurMvIn.class);
			AddFun("onInsDr", EPurMvIn.class);
			super.initFuns();
			//@formatter:off	
			/** Begin onChangeStatus ********
			if (this.roles.indexOf('upd') != -1)
				this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_VERIFIED || fromType == 2);
			if (this.roles.indexOf('del') != -1)
				this.down('#'+this.oldId+'del').setDisabled(status != STATUS_VERIFIED || fromType == 2);
			if (this.roles.indexOf('chk') != -1)
				this.down('#'+this.oldId+'chk').setDisabled(status > STATUS_VERIFIED || fromType != 2); 
			if (this.roles.indexOf('doAppr') != -1)
				this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_VERIFIED);
			if (this.roles.indexOf('unAppr') != -1)
				this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('doNote') != -1)
				this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('doTally') != -1)
				this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('unTally') != -1)
				this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
			if (this.roles.indexOf('cIn') != -1)
				this.down('#'+this.oldId+'cIn').setDisabled(status != STATUS_APPROVE);
				*** End onChangeStatus *********/
			//@formatter:on
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			if (act.getCode().equals("unAppr")) {
				ExtAct v = new ExtAct(this, act, EPurMvIn.class, getPack(), getClazz(), getTb().getName());
				v.add(TEXT, act.getName()).add(ICON_CLS, act.getIcon())
				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
				return;
			}
			if (act.getCode().equals("ins")) {
				ExtAct v = new ExtAct(this, act, EMZipList.class);
				v.add(XTYPE, "splitbutton").add(TEXT, act.getName()).add(ICON_CLS, act.getIcon())
				.addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				.addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
				.addExp("menu", "[{text:'从需求新增',iconCls : 'ins-icon', scope : this,handler: this.onInsFd},{text:'调入直销',iconCls : 'ins-icon', scope : this,handler: this.onInsDr}]");
				getActs().add(v);
				return;
			}
			super.loadTbAct(funCodeFile, act);
//			if (act.getCode().equals("insFd")) {
//				ExtAct v = new ExtAct(this, act, EPurMvIn.class);
//				v.add(TEXT, act.getName()).add(ICON_CLS, "ins-icon")
//				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
//				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
//				getActs().add(v);
//			}
//			if (act.getCode().equals("insDr")) {
//				ExtAct v = new ExtAct(this, act, EPurMvIn.class);
//				v.add(TEXT, act.getName()).add(ICON_CLS, "ins-icon")
//				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
//				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper());
//				getActs().add(v);
//			}
			if (act.getCode().equals("cIn")) {
				ExtAct v = new ExtAct(this, act, EPurMvIn.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "ins-icon")
				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
			}
			if (act.getCode().equals("chk")) {
				ExtAct v = new ExtAct(this, act, EPurMvIn.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "ins-icon")
				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
			}
		}

		//@formatter:off	
		/** Begin onInsFd ********
		var win = Ext.create('mvc.view.pur.PurMvIn.WinDemand',{
			title : this.title+'>新增',
			parent : this
		});
		win.show();
		*** End onInsFd *********/
		
		/** Begin onInsDr ********
		var win = Ext.create('mvc.view.pur.PurMvIn.WinDirect',{
			title : '调入直销>新增',
			parent : this
		});
		win.show();
		*** End onInsDr *********/
		
		
		/** Begin onCIn ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.Ajax.request({
				url : base_path+'/pur_PurMvIn_cIn?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
				success : function (response, options) {
					var result = Ext.decode(response.responseText);
					if (result.success){
						var bean  = Ext.create('mvc.model.pur.PurMvIn',result);
						Ext.apply(selection.data, bean.data);
						selection.commit();
						me.mdMainTable.getSelectionModel().deselectAll();
						me.mdMainTable.getView().select(selection);
						Ext.example.msg(msg_title, '产生入库单--成功');
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
		*** End onCIn *********/
		
		/** Begin onUnAppr ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
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
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
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
		
		/** Begin onChk ********
			var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
			this.mdMainTable.onChkWin(selection);
		*** End onChk *********/
	//@formatter:on	

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create",
			    "mvc.view." + getPack() + "." + getClazz() + ".ListMain").AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'")
			    .add(ICON_CLS, "tab-user-icon").addExp("roles", "this.roles");
			l.addExp("listeners",
			    loadFunCode(EPurMvIn.class, "initFormListMainStatus", getOutVFlds().get(0).getCode()));
		}
		//@formatter:off	
		/** Begin initFormListMainStatus ********
													{
												scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
        								this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
        								var status = records[0].get('bean.status'); //根据单据状态判断
        								var ordStatus = records[0].get('bean.ordStatus'); //根据单据状态判断
        								var fromType = records[0].get('bean.fromType'); 
        								this.onChangeStatus(status, ordStatus, fromType);
        								}else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	this.onChangeStatus(-1, -1);
				                    }
				                }
					        }
		 *** End initFormListMainStatus *********/
		//@formatter:on
	}

	class MyWin extends EMZipWin<MyWin> {

		public MyWin(Tb tb, VFld outFld) {
			super(tb, outFld);
			addExp("insFd", "false");
			addExp("chkFlag", "false");
			add("wsql", "");
		}

		public void initAttrs() {
			add(EXTEND, "Ext.window.Window");
			add(WIDTH, 780);
			add(RESIZABLE, false);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add("pkeyFlag", true);
			add(INS_FLAG, true);
		}

		@Override
		public void initFuns() {
			// TODO Auto-generated method stub
			AddFun("setActiveRecordIns", EPurMvIn.class).addFunParasExp("warehouse", "lines");
			AddFun("setActiveRecordInsDr", EPurMvIn.class).addFunParasExp("warehouse", "dirId");
			super.initFuns();
		}

		@Override
		public void initForm() {
			getForm()
			    .add(ANCHOR, "100%")
			    .add("plain", true)
			    .addExp(
			        XTYPE,
			        "Ext.create('mvc.view."
			            + getPack()
			            + "."
			            + getClazz()
			            + ".Form',{	insFlag : this.insFlag,insFd:this.insFd,chkFlag:this.chkFlag,wsql:this.wsql})");
			getFormList()
			    .addExp(
			        XTYPE,
			        "Ext.create('mvc.view."
			            + getPack()
			            + "."
			            + getClazz()
			            + ".ListForm',{minHeight : 300,border : false,insFd:this.insFd,chkFlag:this.chkFlag })");
		}
	}

	//@formatter:off	
	/** Begin setActiveRecordIns ********
	var dpkeys=[];
	Ext.each(lines,function(dm,index){
		dpkeys.push(dm.get('bean.pkey'));
	});
	Ext.Ajax.request({
			async : false,
			scope : this,
			url : base_path + '/pur_PurMvIn_init',
			params:{whsId:warehouse,dlines:dpkeys.join(',')},
			method : 'GET',
			success : function(response) {
				var rtn = Ext.JSON.decode(response.responseText, true);
				var mvIn  = Ext.create('mvc.model.pur.PurMvIn');
				Ext.apply(mvIn.data,rtn.mvIn);
				this.form.getForm().loadRecord(mvIn);
				this.lineTable.store.loadData(rtn.lines);
			},
			failure : function(response) {
				Ext.example.msg(msg_title, msg_ajax);
			}
	});
	*** End setActiveRecordIns *********/
	
	/** Begin setActiveRecordInsDr ********
		Ext.Ajax.request({
				async : false,
				scope : this,
				url : base_path + '/pur_PurMvIn_initDr',
				params:{whsId:warehouse,dirId:dirId},
				method : 'GET',
				success : function(response) {
					var rtn = Ext.JSON.decode(response.responseText, true);
					if(rtn.message){
						alert(rtn.message);
						return;
					}
					var mvIn  = Ext.create('mvc.model.pur.PurMvIn');
					Ext.apply(mvIn.data,rtn.mvIn);
					this.form.getForm().loadRecord(mvIn);
					this.lineTable.store.loadData(rtn.lines);
				},
				failure : function(response) {
					Ext.example.msg(msg_title, msg_ajax);
				}
		});
	*** End setActiveRecordInsDr *********/
	//@formatter:on	

	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			addExp("insFd", "false");
			addExp("chkFlag", "false");
			addExp("frist", "true");
			add("wsql", "");
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 100).add(WIDTH, 245).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3).add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurMvIn.T.WAREHOUSE.getFld().getCode())) {
				fldList.setCloseStr(null);
				fldList.add(new MyWarehouse());
			}else if (fld.getCode().equals(PurMvIn.T.SHIPING_MODE.getFld().getCode())) {
				fldList.add(new MyShip()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EPurMvIn.class, "initComponentMy"));
			//@formatter:off
			/** Begin initComponentMy ********
			  if(this.chkFlag)
					this.url = this.url + 'chk';
				else{
					if (this.insFlag)
						this.url = this.url + 'ins';
					else
						this.url = this.url + 'upd';
				}
				var formFlds = [];
				formFlds.push
			*** End initComponentMy *********/		
			//@formatter:on
			fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
		}

		/**
		 * 重构IEXTOUT，用于FORM中客户的控件实现
		 * 
		 * @author whx
		 * @version 创建时间：2014年11月7日 下午5:09:31
		 */
		class MyWarehouse implements IExtOut {

			public String toString(int tabs) {
				return null;
			}

			public void out(int tabs, StringBuilder buf) {
				buf.append(new EMModel(PurMvIn.TB).loadFunCode(EPurMvIn.class, "MyWarehouse"));
			}

//@formatter:off	
  		/** Begin MyWarehouse ********
				mvc.Tools.crtComboTrigger(true,'gs_GsWarehouse',this.wsql,{
					name : 'bean.warehouse',
					fieldLabel : '仓库',
					readOnly : this.insFd,
					listeners :{
						scope : this,
						change : function(field,newv,oldv,opts) {
						if(!newv)
							return;
						if(!this.frist  || this.insFlag){
								Ext.Ajax.request({
										async : false,
										scope : this,
										url : base_path + '/gs_GsWarehouse_loadParm',
										params:{pkey:newv},
										method : 'GET',
										success : function(response) {
											var rtn = Ext.JSON.decode(response.responseText, true);
											this.down('[name=ship.name]').setValue(rtn.shipName);
						  				this.down('[name=ship.addr]').setValue(rtn.shipAddr);
						  				this.down('[name=ship.mobile]').setValue(rtn.shipMobile);
						  				this.down('[name=ship.tel]').setValue(rtn.shipTel);
										},
										failure : function(response) {
											Ext.example.msg(msg_title, msg_ajax);
										}
								});
						}
						this.frist = false;
						}
					}
				})
	  		
	  		*** End MyWarehouse *********/
	  		//@formatter:on	
		}
		
		
		class MyShip implements IExtOut {

			public String toString(int tabs) {
				return null;
			}

			public void out(int tabs, StringBuilder buf) {
				buf.append(new EMModel(PurMvIn.TB).loadFunCode(EPurMvIn.class, "myShip"));
			}

			//@formatter:off	
	  		/** Begin myShip ********
			mvc.Tools.crtComboForm(false,{
						name : 'bean.shipingMode',
						fieldLabel : '运输方式',
						store : Ext.create('mvc.combo.sys.SysOShipingMode'),
						value : 1,
						listeners : {
							scope : this,
							change : function(field,newv,oldv,opts) {
								if (newv >= 10){
									this.down('[name=ship.timeShipPlan]').show();
									this.down('[name=ship.timeArrPlan]').show();
									this.down('[name=ship.name]').show();
									this.down('[name=ship.addr]').show();
									this.down('[name=ship.mobile]').show();
									this.down('[name=ship.tel]').show();
								}else{
									this.down('[name=ship.timeShipPlan]').hide();
									this.down('[name=ship.timeArrPlan]').hide();
									this.down('[name=ship.name]').hide();
									this.down('[name=ship.addr]').hide();
									this.down('[name=ship.mobile]').hide();
									this.down('[name=ship.tel]').hide();
								}
								this.doLayout();
							}
						}
					})
	  		*** End myShip *********/
	  		//@formatter:on	
		}
		
	}

}
