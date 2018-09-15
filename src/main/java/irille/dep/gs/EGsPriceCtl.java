package irille.dep.gs;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.gl.gs.GsPriceCtl;
import irille.pub.ext.Ext;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldInt;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldStr;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsPriceCtl extends GsPriceCtl {
	
	public static void main(String[] args) {
		new EGsPriceCtl().crtExt().crtFiles();
	}
	
//	,{name : 'tbObjType',mapping : 'tbObjType',type : 'int',useNull : true}
//	,{name : 'tbObjOrg',mapping : 'tbObjOrg',type : 'string',outkey : true}
//	,{name : 'tbObjCell',mapping : 'tbObjCell',type : 'string',outkey : true}
	
	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB);
		VFlds searchVflds =  new VFlds( T.PRICE) ;
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		
		Fld fld1 = new FldInt("tbObjType", "对象类型");
		Fld fld2 = new FldOutKey(SysOrg.class,"tbObjOrg", "机构");
		Fld fld3 = new FldOutKey(SysCell.class,"tbObjCell", "核算单元");
		VFlds objvflds = new VFlds("NULL").add(fld1.getVFld()).add(fld2.getVFld()).add(fld3.getVFld());
		ext.getVfldsModel().addAll(objvflds);
		
		VFlds vm = ext.getVfldsModel();
		vm.get(T.RETAIL_LEVEL).setModeType("string");
		vm.get(T.LOWEST_LEVEL).setModeType("string");
		vm.get(T.TRADE_LEVEL).setModeType("string");
		vm.get(T.MV_LEVEL).setModeType("string");
		VFlds vl = ext.getVfldsList();
		vl.get(T.RETAIL_LEVEL).attrs().addExp("renderer", "mvc.Tools.beanRenderer()");
		vl.get(T.LOWEST_LEVEL).attrs().addExp("renderer", "mvc.Tools.beanRenderer()");
		vl.get(T.TRADE_LEVEL).attrs().addExp("renderer", "mvc.Tools.beanRenderer()");
		vl.get(T.MV_LEVEL).attrs().addExp("renderer", "mvc.Tools.beanRenderer()");
		VFlds vf = ext.getVfldsForm();
		vf.get(T.RETAIL_LEVEL).setXtype("combo");
		vf.get(T.LOWEST_LEVEL).setXtype("combo");
		vf.get(T.TRADE_LEVEL).setXtype("combo");
		vf.get(T.MV_LEVEL).setXtype("combo");
		vf.get(T.RETAIL_LEVEL).attrs().addExp("store", "this.comboStore").add("valueField", "value");
		vf.get(T.LOWEST_LEVEL).attrs().addExp("store", "this.comboStore").add("valueField", "value");
		vf.get(T.TRADE_LEVEL).attrs().addExp("store", "this.comboStore").add("valueField", "value");
		vf.get(T.MV_LEVEL).attrs().addExp("store", "this.comboStore").add("valueField", "value");
		vf.get(T.RETAIL_LEVEL).setDefaultValue(null);
		vf.get(T.LOWEST_LEVEL).setDefaultValue(null);
		vf.get(T.TRADE_LEVEL).setDefaultValue(null);
		vf.get(T.MV_LEVEL).setDefaultValue(null);
		ext.newExts().init();
		return ext;
	}
	class MyForm extends EMForm<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		public void initAttrs() {
			add(EXTEND, "Ext.form.Panel");
			AddDime(REQUIRES, "Ext.ux.DataTip");
			add(LAYOUT, "form");
			add(BORDER, false);
			add(FRAME, false);
			add(INS_FLAG, true);
			add("comboStore", null);
			add(BODY_PADDING, "5 5 5 5");
			addExp(URL, Ext.url(Ext.getPag(getTb()), Ext.getClazz(getTb()), ""));
			setFieldDefaultsProperies(AddList(FIELD_DEFAULTS));
		}
		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 150).add(LABEL_STYLE, "font-weight : bold");
		}
		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsPriceCtl.T.PRICE.getFld().getCode())) {
				fldList.add(new MyPrice()).setCloseStr(null);
			}else if (fld.getCode().equals(GsPriceCtl.T.TB_OBJ.getFld().getCode())) {
				fldList.add(new MyObj()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}
		
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EMForm.class, "myComponent"));
			//@formatter:off
			/** Begin myComponent ********
			 this.comboStore = this.getPriceName();
				if (this.insFlag)
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
		public void initFuns() {
			super.initFuns();
			add(AddFun("getPriceName", EGsPriceCtl.class));
		}
		//@formatter:off
		/** Begin getPriceName ********
			var store = Ext.create('Ext.data.Store', {
				fields : ['value', 'text'],
				proxy : {
					type : 'ajax',
					url : base_path+'/gs_GsPrice_getPriceNameCombo',
					reader : {
						type : 'json'
					}
				}
			})
			return store;
		*** End getPriceName *********/		
		//@formatter:on
	}
	public static class MyPrice implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceCtl.TB).loadFunCode(EGsPriceCtl.class, "myPrice"));
		}

		//@formatter:off	
  		/** Begin myPrice ********
mvc.Tools.crtComboTrigger(false,'gs_GsPrice','',{
					name : 'bean.price',
					fieldLabel : '定价名称',
					listeners : {
						scope : this,
						change : function(field,newv,oldv,opts) {
							if (newv){
								this.comboStore.proxy.extraParams={"pkey":newv};
								this.comboStore.load();
							}else{
								this.comboStore.removeAll();
							}
							this.down('[name=bean.retailLevel]').setValue('');
							this.down('[name=bean.lowestLevel]').setValue('');
							this.down('[name=bean.tradeLevel]').setValue('');
							this.down('[name=bean.mvLevel]').setValue('');
						}
					}
				})
  		*** End myPrice *********/
  		//@formatter:on	
	}
	
	public static class MyObj implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceCtl.TB).loadFunCode(EGsPriceCtl.class, "myObj"));
		}

		//@formatter:off	
  		/** Begin myObj ********
 		mvc.Tools.crtComboForm(false,{
			fieldLabel : '对象类型',
			name : 'tbObjType',
			store : Ext.create('Ext.data.Store', {
			    fields: ['value', 'text'],
			    data : [
				        {value:1, text:'机构'},
				        {value:2, text:'核算单元'}
				    ]
				}),
			value : 1,
			readOnly : !this.insFlag,
			listeners : {
				scope : this,
				change : function(field,newv,oldv,opts) {
					if (newv===1){
						this.down('[name=tbObjOrg]').show();
						this.down('[name=tbObjCell]').setDisabled()
						this.down('[name=tbObjCell]').hide();
					}else if (newv === 2){
						this.down('[name=tbObjOrg]').setDisabled();
						this.down('[name=tbObjOrg]').hide();
						this.down('[name=tbObjCell]').show();
					}
				}
			}
		}),
		mvc.Tools.crtComboTrigger(true,'sys_SysOrg','',{
			name : 'tbObjOrg',
			fieldLabel : '机构',
			readOnly : !this.insFlag,
		}),
		mvc.Tools.crtComboTrigger(true,'sys_SysCell','',{
			name : 'tbObjCell',
			fieldLabel : '核算单元',
			readOnly : !this.insFlag,
			hidden : true
		})
  		*** End myObj *********/
  		//@formatter:on	
	}
	
	class MyWin extends EMWin<MyWin> {

		public MyWin(Tb tb) {
			super(tb);
		}
		//@formatter:off	
		/** Begin setActiveRecord ********
		this.form.activeRecord = record;
		if (record || this.form.activeRecord) {
			this.form.getForm().loadRecord(record);
			var retailLevel = record.get('bean.retailLevel');
			var lowestLevel = record.get('bean.lowestLevel');
			var tradeLevel = record.get('bean.tradeLevel');
			var mvLevel = record.get('bean.mvLevel');
			this.form.down('[name=bean.retailLevel]').setValue(retailLevel.split(bean_split)[0]);
			this.form.down('[name=bean.lowestLevel]').setValue(lowestLevel.split(bean_split)[0]);
			this.form.down('[name=bean.tradeLevel]').setValue(tradeLevel.split(bean_split)[0]);
			this.form.down('[name=bean.mvLevel]').setValue(mvLevel.split(bean_split)[0]);
		} else {
			this.form.getForm().reset();
		}
		*** End setActiveRecord *********/
		//@formatter:on	
	}
	class MyComp extends EMCrtSimple<MyComp> {

		public MyComp(Tb tb, VFlds vflds, VFlds searchVflds) {
			super(tb, vflds, searchVflds);
		}

		@Override
		public ExtFile newForm() {
			if (_form == null)
				_form = new MyForm(getTb(), getVfldsForm());
			return _form;
		}
		@Override
		public ExtFile newWin() {
			return new MyWin(getTb());
		}
	}
}
