package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMForm;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMListTrigger;
import irille.pub.html.EMModel;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListMain;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EPurOrder extends PurOrder {
	public static void main(String[] args) {
		new EPurOrder().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		PurOrderLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB,
				SysShiping.T.PKEY, SysShiping.T.REM, SysShiping.T.SHIPING_FORM,
				SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR,
				SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING),
				ship };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME,
				T.AMT, T.AMT_COST) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME,
				T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(PurOrderLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG);
		vl.setExpandAndHidden("true", T.AMT_COST, T.ORD_STATUS, T.DEPT, T.CELL,
				T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
				T.CREATED_TIME, T.REM);
		vl.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE,
				SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN);
		
		// Form相关设置
		VFlds vs = ext.getVfldsForm();
		vs.del(T.CODE, T.STATUS, T.ORG, T.CELL, T.DEPT, T.CREATED_BY,
				T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
				T.TALLY_TIME, T.ORD_STATUS,T.AMT_COST);
		vs.moveAfter(T.REM, T.BILL_FLAG);
		vs.setReadOnly("true", T.SUPPLIER, T.SUPNAME, T.WAREHOUSE, T.AMT);
		vs.setNull(true, T.SUPNAME, T.AMT);
		ext.getVfldsForm().setHidden("true", SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN, SysShiping.T.NAME,
				SysShiping.T.ADDR, SysShiping.T.MOBILE, SysShiping.T.TEL);
		ext.newExts().init();

		// 选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vl, T.CODE, new VFlds(
				T.CODE));
		((EMListTrigger) trigger.newList()).setExtendRow();
		((EMListTrigger) trigger.newList()).setTdCount(4);
		trigger.newExts().init();

		return ext;
	}

	/**
	 * 重构LIST，加入【关闭】按钮的支持 loadTbAct是初始化控件时加入按钮及其功能代码；
	 * initFormListMain是重写主表的行选择事件，加上对【关闭】按钮的状态设置；
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:02:28
	 */
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EPurOrder.class).addFunParasExp(
					"status,ordStatus");
			super.initFuns();
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("doClose") == false
					&& act.getCode().equals("doOpen") == false
					&& act.getCode().equals("checkPrice") == false)
				return;
			ExtAct v = new ExtAct(this, act, EPurOrder.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon")
					.addExp("itemId", "this.oldId+'" + act.getCode() + "'")
					.add(SCOPE, EXP_THIS)
					.addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
					.addExp("disabled", "this.lock");
			getActs().add(v);
		}

		//@formatter:off	
		/** Begin onIns ********
		var win = Ext.create('mvc.view.pur.PurOrder.WinDemand',{
			title : this.title+'>新增',
			parent : this
		});
		win.show();
		*** End onIns *********/
		/** Begin onDoClose ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '采购订单['+selection.get('bean.code') + '] - 确认关闭吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/pur_PurOrder_close?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.pur.PurOrder',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '关闭--成功');
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
		*** End onDoClose *********/
		
		/** Begin onDoOpen ********
		 	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
			var me = this;
			if (selection){
				Ext.MessageBox.confirm(msg_confirm_title, '采购订单['+selection.get('bean.code') + '] - 确认打开吗？',
						function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/pur_PurOrder_open?pkey='+selection.get('bean.pkey'),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								selection.set("bean.ordStatus", 1);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '打开--成功');
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
		 *** End onDoOpen *********/
		
		/** Begin onCheckPrice ********
		 var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		this.mdMainTable.onCheckPriceWin(selection);
		 *** End onCheckPrice *********/
		
		/** Begin onChangeStatus ********
	if (this.roles.indexOf('upd') != -1)
		this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('del') != -1)
		this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('checkPrice') != -1)
		this.down('#'+this.oldId+'checkPrice').setDisabled(status != STATUS_INIT && status != STATUS_VERIFIED);
	if (this.roles.indexOf('doAppr') != -1)
		this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_VERIFIED);
	if (this.roles.indexOf('unAppr') != -1)
		this.down('#'+this.oldId+'unAppr').setDisabled(ordStatus != 1 || status != STATUS_TALLY );
	if (this.roles.indexOf('doNote') != -1)
		this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('doTally') != -1)
		this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('unTally') != -1)
		this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
	if (this.roles.indexOf('doClose') != -1)
		this.down('#'+this.oldId+'doClose').setDisabled(status < STATUS_TALLY || ordStatus != 1);
	if (this.roles.indexOf('doOpen') != -1)
		this.down('#'+this.oldId+'doOpen').setDisabled(ordStatus != 2 || status < STATUS_TALLY);
	if (this.roles.indexOf('print') != -1)
				this.down('#'+this.oldId+'print').setDisabled(false);
			*** End onChangeStatus *********/
	//@formatter:on	

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create",
					"mvc.view." + getPack() + "." + getClazz() + ".ListMain")
					.AddFunParaList();
			l.add("title", getTb().getName())
					.addExp("itemId", "this.oldId+'maintable'")
					.add(ICON_CLS, "tab-user-icon")
					.addExp("roles", "this.roles");
			l.addExp(
					"listeners",
					loadFunCode(EPurOrder.class, "initFormListMainStatus",
							getOutVFlds().get(0).getCode()));
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
        								this.onChangeStatus(status, ordStatus);
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

	}

	class MyWin extends EMZipWin<MyWin> {

		public MyWin(Tb tb, VFld outFld) {
			super(tb, outFld);
		}

		public void initAttrs() {
			add(EXTEND, "Ext.window.Window");
			add(WIDTH, 880);
			add("maxHeight", 520);
			add(RESIZABLE, false);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add("pkeyFlag", true);
			add(INS_FLAG, true);
		}

		public void initForm() {
			// EMWin.initForm(getForm(), getPack(), getClazz());
			getForm()
					.add(ANCHOR, "100%")
					.add("plain", true)
					.addExp(XTYPE,
							"Ext.create('mvc.view."
									+ getPack()
									+ "."
									+ getClazz()
									+ ".Form',{	insFlag : this.insFlag,checkPrice : this.checkPrice})");
			getFormList()
					.addExp(XTYPE,
							"Ext.create('mvc.view."
									+ getPack()
									+ "."
									+ getClazz()
									+ ".ListForm',{minHeight : 200,border : false,checkPrice : this.checkPrice })");
		}

		@Override
		public void initFuns() {
			// AddFun("setActiveRecord",EPurOrder.class,TB.getCode()).addFunParasExp("record");
			AddFun("setActiveRecordIns", EPurOrder.class, TB.getCode())
					.addFunParasExp("warehouse", "tmp", "supplier", "lines");
			super.initFuns();
		}
		//@formatter:off	
				/** Begin setActiveRecord ********
				this.form.activeRecord = record;
		if (record || this.form.activeRecord) {
			this.form.getForm().loadRecord(record);
			if(!this.checkPrice){
				this.lineTable.store.filter([{'id':'filter','property':'pkey','value':record.get('bean.pkey')}]);
			}else{
				this.lineTable.store.setProxy({
					type : 'ajax',
					url : base_path+'/pur_PurOrderLine_listForPrice',
					reader : {
						type : 'json',
						root : 'items',
						totalProperty : 'total'
					},
					simpleSortMode : true
				});
				this.lineTable.store.load({
					params : {pkey:record.get('bean.pkey')},
					callback:function(){
						var amt=0;
						this.lineTable.store.each(function(line){
							//line.set('bean.price',10);
							amt = amt + Number(line.get('bean.amt'));
						});
						//this.down('#amt').setValue(amt);
					},scope:this});
			}
		} else {
			this.form.getForm().reset();
			this.lineTable.store.removeAll();
		}
				*** End setActiveRecord *********/
		/** Begin setActiveRecordIns ********
		Ext.Ajax.request({
			async : false,
			scope : this,
			url : base_path + '/pur_PurOrder_init',
			params:{temId:tmp,supId:supplier,whsId:warehouse},
			method : 'GET',
			success : function(response) {
				var rtn = Ext.JSON.decode(response.responseText, true);
				var order  = Ext.create('mvc.model.pur.PurOrder');
				Ext.apply(order.data,rtn);
				this.form.getForm().loadRecord(order);
			},
			failure : function(response) {
				Ext.example.msg(msg_title, msg_ajax);
			}
	});
	Ext.each(lines,function(dm){
		var orderLine  = Ext.create('mvc.model.pur.PurOrderLine');
		orderLine.set('bean.pkey',dm.get('bean.pkey'));
		orderLine.set('bean.goods',dm.get('bean.goods'));
		orderLine.set('link.goodsName',dm.get('link.goodsName'));
		orderLine.set('link.goodsSpec',dm.get('link.goodsSpec'));
		orderLine.set('bean.uom',dm.get('bean.uom'));
		orderLine.set('bean.qty',dm.get('bean.qty'));
		this.lineTable.store.insert(0,orderLine);
		
	},this);
	//this.form.getForm().loadRecord(mainRecord);
		*** End setActiveRecordIns *********/
		
				//@formatter:on		

	}

	class MyListMain extends EMZipListMain<MyListMain> {
		private String _extend = "Ext.grid.Panel";

		public MyListMain(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
			setExtendRow();
		}

		@Override
		public void initFuns() {
			super.initFuns();
			add(AddFun("onCheckPriceWin", EPurOrder.class, getPack(),
					getClazz()).addFunParasExp("selection"));
		}
		//@formatter:off	
		/** Begin onCheckPriceWin ********
		if (selection){
		mvc.model.pur.PurOrder.load(selection.get('bean.pkey'), {
			scope : this,
			failure : function(response, operation) {
				Ext.example.msg(msg_title,msg_ajax);
			},
			success : function(response, operation) {
				Ext.apply(selection.data,response.data);
				var win = Ext.create('mvc.view.pur.PurOrder.Win',{
					title : this.title+'>核价',
					insFlag : false,
					checkPrice : true
				});
				win.on('create',this.onUpdateRecord,this);
				win.show();
				win.setActiveRecord(selection);
			}
		});
	}
		*** End onCheckPriceWin *********/

		//@formatter:on		
	}

	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3)
					.add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275)
					.add(LABEL_STYLE, "font-weight : bold");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurOrder.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			} else if (fld.getCode().equals(
					PurOrder.T.SHIPING_MODE.getFld().getCode())) {
				fldList.add(new MyShip()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EMForm.class, "myComponent"));
			//@formatter:off
			/** Begin myComponent ********
				if(this.checkPrice)
					this.url = this.url + 'checkPrice';
				else if (this.insFlag)
					this.url = this.url + 'ins';
				else
					this.url = this.url + 'upd';
				var formFlds = [];
				formFlds.push
			*** End myComponent *********/		
			//@formatter:on
			fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
		}
	}

	public static class MySupplier implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurOrder.TB).loadFunCode(EPurOrder.class,
					"mySupplier"));
		}

		//@formatter:off	
  		/** Begin mySupplier ********
 		 		xtype : 'comboauto',
		fieldLabel : '供应商',
		name : 'bean.supplier',
		listConfig : {minWidth:250},
		fields : ['pkey','code','name'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		url : base_path + '/sys_SysSuppliser_autoComplete',
		urlExt : 'sys.SysCustom',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		readOnly : true
  		
  		*** End mySupplier *********/
  		//@formatter:on	
	}

	public static class MyShip implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurOrder.TB).loadFunCode(PurOrder.class,
					"myShip"));
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

	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:11:55
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds,
				VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(
					getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}

		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
		}

		@Override
		public ExtFile newZipListMain() {
			return new MyListMain(getTb(), getVfldsList());
		}

		@Override
		public VFlds getNowOutKeyZipListFormVflds() {
			VFlds vs = super.getNowOutKeyZipListFormVflds();
			vs.del(PurOrderLine.T.QTY_OPEN);
			return vs;
		}
	}
}
