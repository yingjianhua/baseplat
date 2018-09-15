package irille.dep.pur;
import irille.pss.pur.PurProtApply;
import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMForm;
import irille.pub.html.EMList;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.ExtAttr;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunCall;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFldEnumByte;
import irille.pub.view.VFlds;

import java.util.Iterator;
import java.util.Vector;

public class EPurProtApply extends PurProtApply {

	public static void main(String[] args) {
		new EPurProtApply().crtExt();
	}

	public void crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIP_MODE, T.AFT_SHIP_MODE) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.SUPPLIER, T.NAME,
				T.STATUS) };
		searchVflds[0].get(T.NAME).setName("名称");
		EMCrt ext = new MyCrt(TB, vflds, searchVflds);
		VFlds listFlds = new VFlds()
				.addWithout(TB, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME,
						T.APPR_BY, T.APPR_TIME, T.TEMPLAT, T.SHIP_MODE, T.AFT_SHIP_MODE);
		listFlds.moveAfter(T.REM, T.AFT_DATE_END);
		ext.setVfldsList(listFlds);
		VFlds formFlds = new VFlds().add(T.TEMPLAT, T.SUPPLIER, T.NAME, T.REM,
				T.SETTLE, T.AFT_SETTLE, T.SETTLE_MONTH, T.AFT_SETTLE_MONTH,
				T.SETTLE_NEXT_DAY, T.AFT_SETTLE_NEXT_DAY, T.CREDIT_LEVEL,
				T.AFT_CREDIT_LEVEL, T.CREDIT_LIMIT, T.AFT_CREDIT_LIMIT,
				T.CREDIT_OTHER, T.AFT_CREDIT_OTHER, T.TAX_RATE, T.AFT_TAX_RATE,
				T.DESC_KIND, T.AFT_DESC_KIND, T.DESC_SAL, T.AFT_DESC_SAL,
				T.PACK_DEMAND, T.AFT_PACK_DEMAND, 
				T.SHIP_TYPE, T.AFT_SHIP_TYPE, T.DATE_PROT, T.AFT_DATE_PROT,
				T.DATE_START, T.AFT_DATE_START, T.DATE_END, T.AFT_DATE_END).add(T.ROW_VERSION);
		formFlds.setReadOnly("true", T.NAME, T.SETTLE, T.AFT_SETTLE,
				T.SETTLE_MONTH, T.AFT_SETTLE_MONTH, T.SETTLE_NEXT_DAY,
				T.AFT_SETTLE_NEXT_DAY, T.CREDIT_LEVEL, T.AFT_CREDIT_LEVEL,
				T.CREDIT_LIMIT, T.AFT_CREDIT_LIMIT, T.CREDIT_OTHER,
				T.AFT_CREDIT_OTHER, T.TAX_RATE, T.AFT_TAX_RATE, T.DESC_KIND,
				T.AFT_DESC_KIND, T.DESC_SAL, T.AFT_DESC_SAL, T.PACK_DEMAND,
				T.AFT_PACK_DEMAND, T.SHIP_TYPE,
				T.AFT_SHIP_TYPE, T.DATE_PROT, T.AFT_DATE_PROT, T.DATE_START,
				T.AFT_DATE_START, T.DATE_END, T.AFT_DATE_END);
		formFlds.get(T.SETTLE_MONTH).setDefaultValue("null");
		formFlds.get(T.SETTLE_NEXT_DAY).setDefaultValue("null");
		formFlds.get(T.CREDIT_LEVEL).setDefaultValue("null");
		formFlds.get(T.CREDIT_LIMIT).setDefaultValue("null");
		formFlds.get(T.CREDIT_OTHER).setDefaultValue("null");
		formFlds.get(T.TAX_RATE).setDefaultValue("null");
		formFlds.get(T.SHIP_TYPE).setDefaultValue("null");
		
		ext.setVfldsForm(formFlds);
		VFlds vl = ext.getVfldsList();
		vl.setExpandAndHidden("true", T.SETTLE,T.SETTLE_MONTH,T.SETTLE_NEXT_DAY,T.CREDIT_LEVEL,T.CREDIT_LIMIT
				,T.CREDIT_OTHER,T.TAX_RATE,T.DESC_KIND,T.DESC_SAL,T.PACK_DEMAND,T.SHIP_TYPE,T.DATE_PROT
				,T.DATE_START,T.DATE_END);
		//((EMList) ext.newList()).setExtendRow();
		ext.newExts().init();
		ext.crtFiles();
	}

	class MyCrt extends EMCrtSimpleTwo {

		public MyCrt(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
			super(tb, vflds, searchVflds);
			// TODO Auto-generated constructor stub
			
		}

		@Override
		public ExtFile newList() {
			EMList myList = new MyList(getTb(), getVfldsList())
					.setSearchVFlds(getSearchVflds());
			myList.setLineActs(false);
			return myList;
		}

		@Override
		public ExtFile newWin() {
			// TODO Auto-generated method stub
			return new MyWin(getTb());
		}

		@Override
		public ExtFile newForm() {
			// TODO Auto-generated method stub
			return new MyForm(getTb(), getVfldsForm());
		}

	}

	class MyForm extends EMForm {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 120).add(WIDTH, 300)
					.add(LABEL_STYLE, "font-weight : bold");
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.html.EMForm#setLayoutProperies(irille.pub.html.ExtList)
		 */
		@Override
		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 2)
					.add(ITEM_CLS, "x-layout-table-items-form");
		}

		@Override
		public void initAttrs() {
			add(EXTEND, "Ext.form.Panel");
			AddDime(REQUIRES, "Ext.ux.DataTip");
			add(LAYOUT, "form");
			add(BORDER, false);
			add(FRAME, false);
			add("first", true);
			add(INS_FLAG, true);
			add(BODY_PADDING, "5 5 5 5");
			addExp(URL, Ext.url(Ext.getPag(getTb()), Ext.getClazz(getTb()), ""));
			setFieldDefaultsProperies(AddList(FIELD_DEFAULTS));
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurProtApply.T.TEMPLAT.getFld().getCode())) {
				fldList.setCloseStr(null);
				fldList.add(new MyTemplat());
			} else if (fld.getCode().equals(
					PurProtApply.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

		@Override
		public void initFuns() {
			// TODO Auto-generated method stub
			super.initFuns();
			AddFunDefine("loadSup").add(
					loadFunCode(EPurProtApply.class, "loadSup"));
			AddFunDefine("onNewApp").add(
					loadFunCode(EPurProtApply.class, "onNewApp"));
			AddFunDefine("onUpdApp").add(
					loadFunCode(EPurProtApply.class, "onUpdApp"));
		}
	}

	//@formatter:off	
		/** Begin loadSup ********
		var supAll = this.down('[name=bean.supplier]').getValue();
		var tmp = this.down('[name=bean.templat]').getValue();
		var rv = this.down('[name=bean.rowVersion]').getValue();
		var pkey = this.down('[name=bean.pkey]').getValue();
		if(!supAll || !tmp)
			return;
		var sup = supAll.split('##')[0];
		Ext.Ajax.request({
			async : false,
			url : base_path + '/pur_PurProtApply_init?supId='+sup +'&temId='+tmp,
			method : 'GET',
			scope : this,
			success : function(response) {
				var rtn = Ext.decode(response.responseText, true);
				this.down('[name=bean.name]').setValue(rtn.name);
				var formRecord = Ext.create("mvc.model.pur.PurProtApply");
				formRecord.data = Ext.decode(response.responseText);
				this.getForm().loadRecord(formRecord);
				if(rtn.success == '0'){
					this.down('[name=bean.aftSettleMonth]').setValue(1);
					this.down('[name=bean.aftSettleNextDay]').setValue(0);
					this.down('[name=bean.aftCreditLevel]').setValue(1);
					this.down('[name=bean.aftCreditLimit]').setValue(0);
					this.down('[name=bean.aftCreditOther]').setValue(0);
					this.down('[name=bean.aftTaxRate]').setValue(0);
					this.down('[name=bean.aftShipType]').setValue(3);
				}
				this.onUpdApp();
				this.down('[name=bean.rowVersion]').setValue(rv);
				this.down('[name=bean.pkey]').setValue(pkey);
			},
			failure : function(response) {
				Ext.example.msg(msg_title, msg_ajax);
			}
		});
	
		*** End loadSup *********/
		//@formatter:on	
	
	//@formatter:off	
			/** Begin onNewApp ********
			
		this.down('[name=bean.settle]').setReadOnly(false);
		this.down('[name=bean.settleMonth]').setReadOnly(false);
		this.down('[name=bean.settleNextDay]').setReadOnly(false);
		this.down('[name=bean.creditLevel]').setReadOnly(false);
		this.down('[name=bean.creditLimit]').setReadOnly(false);
		this.down('[name=bean.creditOther]').setReadOnly(false);
		this.down('[name=bean.taxRate]').setReadOnly(false);
		this.down('[name=bean.descKind]').setReadOnly(false);
		this.down('[name=bean.descSal]').setReadOnly(false);
		this.down('[name=bean.packDemand]').setReadOnly(false);
		this.down('[name=bean.shipType]').setReadOnly(false);
		this.down('[name=bean.dateProt]').setReadOnly(false);
		this.down('[name=bean.dateStart]').setReadOnly(false);
		this.down('[name=bean.dateEnd]').setReadOnly(false);
		
		
		this.down('[name=bean.aftSettle]').setReadOnly(true);
		this.down('[name=bean.aftSettleMonth]').setReadOnly(true);
		this.down('[name=bean.aftSettleNextDay]').setReadOnly(true);
		this.down('[name=bean.aftCreditLevel]').setReadOnly(true);
		this.down('[name=bean.aftCreditLimit]').setReadOnly(true);
		this.down('[name=bean.aftCreditOther]').setReadOnly(true);
		this.down('[name=bean.aftTaxRate]').setReadOnly(true);
		this.down('[name=bean.aftDescKind]').setReadOnly(true);
		this.down('[name=bean.aftDescSal]').setReadOnly(true);
		this.down('[name=bean.aftPackDemand]').setReadOnly(true);
		this.down('[name=bean.aftShipType]').setReadOnly(true);
		this.down('[name=bean.aftDateProt]').setReadOnly(true);
		this.down('[name=bean.aftDateStart]').setReadOnly(true);
		this.down('[name=bean.aftDateEnd]').setReadOnly(true);
	
			*** End onNewApp *********/
	//@formatter:on	

	//@formatter:off	
	/** Begin onUpdApp ********

		this.down('[name=bean.settle]').setReadOnly(true);
		this.down('[name=bean.settleMonth]').setReadOnly(true);
		this.down('[name=bean.settleNextDay]').setReadOnly(true);
		this.down('[name=bean.creditLevel]').setReadOnly(true);
		this.down('[name=bean.creditLimit]').setReadOnly(true);
		this.down('[name=bean.creditOther]').setReadOnly(true);
		this.down('[name=bean.taxRate]').setReadOnly(true);
		this.down('[name=bean.descKind]').setReadOnly(true);
		this.down('[name=bean.descSal]').setReadOnly(true);
		this.down('[name=bean.packDemand]').setReadOnly(true);
		this.down('[name=bean.shipType]').setReadOnly(true);
		this.down('[name=bean.dateProt]').setReadOnly(true);
		this.down('[name=bean.dateStart]').setReadOnly(true);
		this.down('[name=bean.dateEnd]').setReadOnly(true);
		
		this.down('[name=bean.aftSettle]').setReadOnly(false);
		this.down('[name=bean.aftSettleMonth]').setReadOnly(false);
		this.down('[name=bean.aftSettleNextDay]').setReadOnly(false);
		this.down('[name=bean.aftCreditLevel]').setReadOnly(false);
		this.down('[name=bean.aftCreditLimit]').setReadOnly(false);
		this.down('[name=bean.aftCreditOther]').setReadOnly(false);
		this.down('[name=bean.aftTaxRate]').setReadOnly(false);
		this.down('[name=bean.aftDescKind]').setReadOnly(false);
		this.down('[name=bean.aftDescSal]').setReadOnly(false);
		this.down('[name=bean.aftPackDemand]').setReadOnly(false);
		this.down('[name=bean.aftShipType]').setReadOnly(false);
		this.down('[name=bean.aftDateProt]').setReadOnly(false);
		this.down('[name=bean.aftDateStart]').setReadOnly(false);
		this.down('[name=bean.aftDateEnd]').setReadOnly(false);
	
	 *** End onUpdApp *********/
	//@formatter:on	

	/**
	 * 重构IEXTOUT，用于FORM中客户的控件实现
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:09:31
	 */
	class MyTemplat implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurProtApply.TB).loadFunCode(
					EPurProtApply.class, "MyTemplat"));
		}

		//@formatter:off	
  		/** Begin MyTemplat ********
		mvc.Tools.crtComboTrigger(false,'sys_SysTemplat','type=3',{
				name : 'bean.templat',
				fieldLabel : '采购模板',
				listeners :{
					scope : this,
					change : function(field,newv,oldv,opts) {
						this.loadSup();
					}
				}
			}) 		
  		*** End MyTemplat *********/
  		//@formatter:on	
	}

	class MySupplier implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurProtApply.TB).loadFunCode(
					EPurProtApply.class, "MySupplier"));
		}

		//@formatter:off	
  		/** Begin MySupplier ********
				xtype : 'beantrigger',
				name : 'bean.supplier',
				fieldLabel : '供应商',
				bean : 'SysSupplier',
				beanType : 'sys',
				emptyText : form_empty_text,
				listeners :{
					scope : this,
					change : function(field,newv,oldv,opts) {
						this.loadSup();
					}
				}
				
  		*** End MySupplier *********/
  		//@formatter:on	
	}

	class MyWin extends EMWin {

		public MyWin(Tb tb) {
			super(tb);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void initAttrs() {
			add(EXTEND, "Ext.window.Window");
			add(WIDTH, 650);
			add(LAYOUT, "fit");
			addExp(FORM, "null");
			add(RESIZABLE, true);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add(INS_FLAG, true);
		}

	}

	class MyList extends EMList {

		public MyList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
			setExtendRow();
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			if (act.getCode().equals("unAppr"))
				return;
			if (act.getCode().equals("doAppr")) {
				ExtAct v = new ExtAct(this, act, EPurProtApply.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "doAppr-icon")
						.addExp("itemId", "this.oldId+'" + act.getCode() + "'")
						.add(SCOPE, EXP_THIS)
						.addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
						.addExp("disabled", "this.lock");
				getActs().add(v);
			} else {
				super.loadTbAct(funCodeFile, act);
			}
		}

		@Override
		public void initFuns() {
			// 行选择事件处理
			// ExtFunDefine fun = AddList("listeners") // 表格行选择事件，主要设置按钮是否可用
			// .AddFunDefine(
			// "selectionchange",
			// loadFunCode(EPurOrder.class,
			// "initFormListMainStatus"));
			// selectionChanges(fun);

			AddList("listeners").addExp("selectionchange",
					loadFunCode(EPurProtApply.class, "initFormListMainStatus"));

			initFunsAddOtherFuns();
			initFunsAddActs();

			add(AddFun("onSearchCancel", EMList.class));
			add(AddFun("onSearch", EMList.class));
			add(AddFun("onSearchAdv", EMList.class, getPack(), getClazz()));
			add(AddFunD("onSearchDo", EMList.class, new ExtExp("array")));
		}

	}

	//@formatter:off	
		/** Begin onDoAppr ********		
			var selection = this.getView().getSelectionModel().getSelection()[0];
			var me = this;
			if (selection){
				Ext.MessageBox.confirm(msg_confirm_title, '申请单['+selection.get('bean.code') + '] - 审核确认吗？',
					function(btn) {
						if (btn != 'yes')
							return;
						Ext.Ajax.request({
							url : base_path+'/pur_PurProtApply_approve?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
							success : function (response, options) {
								var result = Ext.decode(response.responseText);
								if (result.success){
									var bean  = Ext.create('mvc.model.pur.PurMvIn',result);
									Ext.apply(selection.data, bean.data);
									selection.commit();
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
		** End onDoAppr ********/
	
	/** Begin initFormListMainStatus ********
		   	function(selModel, records) {
            if (records.length === 1){
				var status = records[0].get('bean.status'); //根据单据状态判断
				//初始状态
				if (status==STATUS_INIT){
					if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(false);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(false);
					if (this.roles.indexOf('doAppr') != -1)
						this.down('#'+this.oldId+'doAppr').setDisabled(false);
				}else if (status==STATUS_APPROVE){
					if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(true);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(true);
					if (this.roles.indexOf('doAppr') != -1)
						this.down('#'+this.oldId+'doAppr').setDisabled(true);
				}
            }else{
            	if (this.roles.indexOf('upd') != -1)
					this.down('#'+this.oldId+'upd').setDisabled(true);
				if (this.roles.indexOf('del') != -1)
					this.down('#'+this.oldId+'del').setDisabled(true);
				if (this.roles.indexOf('doAppr') != -1)
					this.down('#'+this.oldId+'doAppr').setDisabled(true);
            }
        }
	 *** End initFormListMainStatus *********/
	//@formatter:on
}
