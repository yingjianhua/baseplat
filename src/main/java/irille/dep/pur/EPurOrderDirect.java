/**
 * 
 */
package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.pss.pur.PurOrderDirect;
import irille.pss.pur.PurOrderDirectLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMForm;
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
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

/**
 * @author administrator
 *
 */
public class EPurOrderDirect extends PurOrderDirect {
	public static void main(String[] args) {
		new EPurOrderDirect().crtExt();
	}

	public void crtExt() {
		PurOrderDirectLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		CmbGoods.TB.getCode();
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
		    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds(TB) ,ship};
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME, T.AMT) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME, T.STATUS) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(
		    PurOrderDirectLine.T.PKEY) });
		VFlds lvs = ext.getVfldsList();
		lvs.del(T.PKEY,T.SHIPING);
		lvs.moveLast(T.REM, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME);
		lvs.setExpandAndHidden("true", T.AMT_COST, T.DEPT, T.CELL,
				T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
				T.CREATED_TIME, T.REM);
		lvs.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE,
				SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN);
		
		VFlds fvs = ext.getVfldsForm();
		fvs.del(T.CODE, T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.AMT_COST, T.APPR_BY,
		    T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME,T.SHIPING,T.CELL);
		fvs.setHidden("true", T.ORIG_FORM, T.ORIG_FORM_NUM);
		fvs.setReadOnly("true", T.AMT, T.SUPNAME);
		fvs.moveLast(T.REM);
		ext.getVfldsForm().setHidden("true", SysShiping.T.TIME_ARR_PLAN,
		    SysShiping.T.TIME_SHIP_PLAN, SysShiping.T.NAME, SysShiping.T.ADDR, SysShiping.T.MOBILE,
		    SysShiping.T.TEL);
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
			return new MyListForm(tb, outfld, vflds);
		}

		 @Override
		 public ExtFile newZipListMain() {
			 return new MyZipListMain(getTb(), getVfldsList());
		 }

	}
	
	class MyZipListMain extends EMZipListMain<MyZipListMain> {
		
		public MyZipListMain(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			setExtendRow();
		}
		
		@Override
		public void initFuns() {
			super.initFuns();
			add(AddFun("onCheckPriceWin", EPurOrderDirect.class, getPack(),
					getClazz()).addFunParasExp("selection"));
			//@formatter:off	
			/** Begin onCheckPriceWin ********
			if (selection){
				mvc.model.pur.PurOrderDirect.load(selection.get('bean.pkey'), {
					scope : this,
					failure : function(response, operation) {
						Ext.example.msg(msg_title,msg_ajax);
					},
					success : function(response, operation) {
						Ext.apply(selection.data,response.data);
						var win = Ext.create('mvc.view.pur.PurOrderDirect.Win',{
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
		
	}

	class MyList extends EMZipList<MyList> {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			super.loadTbAct(funCodeFile, act);
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("checkPrice") == false)
				return;
			ExtAct v = new ExtAct(this, act, EPurOrder.class);
			v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon")
					.addExp("itemId", "this.oldId+'" + act.getCode() + "'")
					.add(SCOPE, EXP_THIS)
					.addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
					.addExp("disabled", "this.lock");
			getActs().add(v);
		}
		
		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EPurRtn.class).addFunParasExp("status,ordStatus");
			super.initFuns();
			//@formatter:off	
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
				this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('doNote') != -1)
				this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('doTally') != -1)
				this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
			if (this.roles.indexOf('unTally') != -1)
				this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
			if (this.roles.indexOf('print') != -1)
				this.down('#'+this.oldId+'print').setDisabled(false);
				*** End onChangeStatus *********/
			//@formatter:on
		}
		
		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EPurRtn.class, "initFormListMainStatus", getOutVFlds().get(0).getCode()));
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

		//@formatter:off	
		
		/** Begin onIns ********
		var win = Ext.create('mvc.view.pur.PurOrderDirect.WinDirect',{
			title : '采购直销>新增',
			parent : this
		});
		win.show();
		*** End onIns *********/
	//@formatter:on	
	}

	class MyWin extends EMZipWin<MyWin> {

		public MyWin(Tb tb, VFld outFld) {
			super(tb, outFld);
		}

		public void initAttrs() {
			add(EXTEND, "Ext.window.Window");
			add(WIDTH, 880);
			add(RESIZABLE, false);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add("pkeyFlag", true);
			add(INS_FLAG, true);
		}

		@Override
		public void initFuns() {
			// TODO Auto-generated method stub
			super.initFuns();
			add(AddFun("setActiveRecordDr", EPurOrder.class, getPack(),
					getClazz()).addFunParasExp("dirId"));
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
			            + ".Form',{	insFlag : this.insFlag,checkPrice : this.checkPrice})");
//			            + ".Form',{	insFlag : this.insFlag,insFd:this.insFd,chkFlag:this.chkFlag,wsql:this.wsql})");
			getFormList()
			    .addExp(
			        XTYPE,
			        "Ext.create('mvc.view."
			            + getPack()
			            + "."
			            + getClazz()
			            + ".ListForm',{minHeight : 200,border : false,checkPrice : this.checkPrice })");
//			            + ".ListForm',{minHeight : 300,border : false,insFd:this.insFd,chkFlag:this.chkFlag })");
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
					url : base_path+'/pur_PurOrderDirectLine_listForPrice',
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
						this.down('#amt').setValue(amt);
					},scope:this});
			}
		} else {
			this.form.getForm().reset();
			this.lineTable.store.removeAll();
		}
		*** End setActiveRecord *********/
		/** Begin setActiveRecordDr ********
		Ext.Ajax.request({
				async : false,
				scope : this,
				url : base_path + '/pur_PurOrderDirect_initDr',
				params:{dirId:dirId},
				method : 'GET',
				success : function(response) {
					var rtn = Ext.JSON.decode(response.responseText, true);
					if(rtn.message){
						alert(rtn.message);
						return;
					}
					var order  = Ext.create('mvc.model.pur.PurOrderDirect');
					Ext.apply(order.data,rtn.order);
					this.form.getForm().loadRecord(order);
					this.lineTable.store.loadData(rtn.lines);
				},
				failure : function(response) {
					Ext.example.msg(msg_title, msg_ajax);
				}
		});
		 *** End setActiveRecordDr *********/
		//@formatter:on	
	}

	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			addExp("frist", "true");
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3).add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurOrderDirect.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			}else if (fld.getCode().equals(PurOrderDirect.T.SHIPING_MODE.getFld().getCode())) {
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

		class MySupplier implements IExtOut {

			public String toString(int tabs) {
				return null;
			}

			public void out(int tabs, StringBuilder buf) {
				buf.append(new EMModel(PurOrderDirect.TB).loadFunCode(EPurOrderDirect.class, "mySupplier"));
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
			url : base_path + '/sys_SysSupplier_autoComplete',
			urlExt : 'sys.SysSupplier',
			hasBlur : false,
			afterLabelTextTpl : required,
			allowBlank : false,
			listeners : {
				scope : this,
				blur : function(field){
					var me = this;
					if (!field.getRawValue()){
						me.down('[name=bean.supname]').setValue(null);
						me.down('[name=ship.name]').setValue(null);
	  				me.down('[name=ship.addr]').setValue(null);
	  				me.down('[name=ship.mobile]').setValue(null);
	  				me.down('[name=ship.tel]').setValue(null);
		    		return;
		    	}
					var urlCust = base_path+ '/sys_SysSupplier_loadInfoDetail?sarg1=' + field.getRawValue();
		    		Ext.Ajax.request({
		    			//async : false, //加上同步限制后，单元格之间切换会中断
		    			url : urlCust,
		    			method : 'GET',
		    			success : function(response) {
		    				rtn = Ext.JSON.decode(response.responseText, true);
		    				me.down('[name=bean.supplier]').setValue(rtn.supplier);
		    				me.down('[name=bean.supname]').setValue(rtn.supname);
		    				me.down('[name=ship.name]').setValue(rtn.goodsbyName);
			  				me.down('[name=ship.addr]').setValue(rtn.goodsbyAddr);
			  				me.down('[name=ship.mobile]').setValue(rtn.goodsbyMobile);
			  				me.down('[name=ship.tel]').setValue(rtn.goodsbyTel);
		    			},
		    			failure : function(response) {
		    				Ext.example.msg(msg_title, msg_ajax);
		    			}
		    		});
				}
			} 		
	  		*** End mySupplier *********/
	  		//@formatter:on	
		}
		class MyShip implements IExtOut {

			public String toString(int tabs) {
				return null;
			}

			public void out(int tabs, StringBuilder buf) {
				buf.append(new EMModel(PurOrderDirect.TB).loadFunCode(EPurOrderDirect.class, "myShip"));
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

	class MyListForm extends EMZipListForm<MyListForm> {

		public MyListForm(Tb tb, VFld outfld, VFlds[] vflds) {
			super(tb, outfld, vflds);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public void initAttrs() {
			super.initAttrs();
			addExp("checkPrice", "false");
		}

		@Override
		public void initComponent(ExtFunDefine fun) {
			// 主界面功能定义
			fun.add(loadFunCode(EPurOrderDirect.class, "initComponentGoods"));
			//@formatter:off	
				/** Begin initComponentGoods ********
		this.columns =[		
		{text : '货物', width : 120, dataIndex : 'bean.goods', sortable : true, renderer : mvc.Tools.beanRenderer()}
		,{text : '数量',width : 100,dataIndex : 'bean.qty',sortable : true,align : 'right',renderer : mvc.Tools.numberRenderer(4)}
		,{text : '计量单位',width : 80,dataIndex : 'bean.uom',sortable : true,renderer : mvc.Tools.beanRenderer()}
		,{text : '单价',width : 100,dataIndex : 'bean.price',sortable : true,align : 'right',renderer : mvc.Tools.numberRenderer(4),editor : {xtype : 'numberfield',decimalPrecision : 4}}
		,{text : '金额',width : 120,dataIndex : 'bean.amt',sortable : true,renderer : mvc.Tools.numberRenderer(),align : 'right'}
		];
	mvc.Tools.doGoodsLine(this.columns, 1);				
	this.store=Ext.create('mvc.store.pur.PurOrderDirectLine');
				this.store.pageSize = 0;
				this.store.remoteFilter = true;
				this.store.proxy.filterParam = 'filter';
				if(this.checkPrice){
					this.plugins = [this.cellEditing];
					this.on('edit', function(editor, e) {
						if (e.field == 'bean.price'){
							var pr = Number(e.value);
							var num = Number(e.record.get('bean.qty'));
							var amt = pr * num;
							e.record.set('bean.amt',amt);
							var amtAll=0;
							this.store.each(function(recode){
								amtAll = amtAll + Number(recode.get('bean.amt'));
							});
							this.up('panel').down('[name=bean.amt]').setValue(amtAll);
						}
					});
				}
				this.callParent(arguments);	
				 *** End initComponentGoods *********/
			//@formatter:on
		}

	}
}
