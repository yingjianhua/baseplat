package irille.dep.gs;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsKind;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMForm;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMList;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsPriceGoods extends GsPriceGoods {

	public static void main(String[] args) {
		new EGsPriceGoods().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB).add(GsGoods.T.UOM),
		    EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) });
		vflds.moveBefore(GsGoods.T.UOM, T.PRICE_KIND);
		vflds.get(GsGoods.T.UOM).setReadOnly("true");
		vflds.moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		VFlds searchVflds = new VFlds(GsPriceGoodsKind.T.PRICE, T.GOODS);
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.del(T.PRICE_NAME);
		vl.setHidden("true", T.PRICE1, T.PRICE2, T.PRICE3, T.PRICE4, T.PRICE5, T.PRICE6, T.PRICE7, T.PRICE8, T.PRICE9,
		    T.PRICE10, T.PRICE11, T.PRICE12);
		VFlds vf = ext.getVfldsForm();
		vf.setGoodsLink(T.GOODS);
		vf.get(GsGoods.T.UOM).attrs().addExp("disabled", "true").add("disabledCls", "");
		vf.del(T.ENABLED, T.PRICE_NAME);
		vf.setHidden("true", T.PRICE1, T.PRICE2, T.PRICE3, T.PRICE4, T.PRICE5, T.PRICE6, T.PRICE7, T.PRICE8, T.PRICE9,
		    T.PRICE10, T.PRICE11, T.PRICE12);
		((EMForm) ext.newForm()).setGoodsLink(true);
		((EMWin) ext.newWin()).setWidth(670);
		ext.newExts().init();
		//选择器 注意回调的方法，在下面重写过了
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vflds, T.PRICE_KIND, new VFlds(T.GOODS));
		trigger.getVfldsList().del(T.PRICE_NAME);
		trigger.newExts().init().crtFiles();

		return ext;
	}

	class MyList extends EMList<MyList> {

		public MyList(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}

		public void initFuns() {
			super.initFuns();
			add(AddFun("onGetPriceNames", EGsPriceGoods.class).addFunParasExp("newv"));
			add(AddFun("onSearchDo", EGsPriceGoods.class).addFunParasExp("array"));
		}

		public void initComponent(ExtFunDefine fun) {
			// 明细行功能定义
			initLineActs(fun);
			initMainActs(fun);
			fun.add("this.columns = ");
			fun.add(getColumns());
			fun.add(loadFunCode(EMList.class, "myComponent", getPack(), getClazz()));
			/*fun.add("this.dockedItems=");
			fun.AddDime().add(getFormDocked()).add(getFormTable());*/
			fun.add("		this.callParent(arguments);");
			fun.add("		mvc.Tools.onENTER2SearchBar(this.down('[dock=top]'),this);");
			// @formatter:off
			/** Begin myComponent ********
			mvc.Tools.doGoodsLine(this.columns, 1);	
			if (mainActs.length > 0)
				this.tbar=mainActs;
			this.store=Ext.create('mvc.store.【0】.【1】'); 
			this.store.remoteFilter = true;
			this.store.proxy.filterParam = 'filter';
			this.dockedItems = [{
			dock : 'top',
			xtype : 'toolbar',
			items : [{
						xtype : 'label',
						text : '定价名称：'
					}, mvc.Tools.crtComboTrigger(true, 'gs_GsPrice', '', {
						name : 'price',
						listeners : {
							afterrender : function() {
								var keys = '';
								Ext.Ajax.request({
									async : false,
									url : base_path + 'gs_GsPrice_getComboTrigger',
									method : 'POST',
									success : function(response) {
										keys = Ext.JSON.decode(response.responseText).items[0].value;
										keys += '##'+ Ext.JSON.decode(response.responseText).items[0].text;
									}
								});
								this.setValue(keys);
							}
						}
					}), '', {
						xtype : 'label',
						text : '货物：'
					}, {
						xtype : 'beantrigger',
						name : 'goods',
						bean : 'GsGoods',
						beanType : 'gs',
						emptyText : form_empty_text
					}, '', {
						xtype : 'button',
						text : '撤销',
						scope : this,
						iconCls : 'win-close-icon',
						handler : this.onSearchCancel
					}, {
						xtype : 'splitbutton',
						text : '搜索',
						scope : this,
						iconCls : 'win-ok-icon',
						handler : this.onSearch,
						menu : [{
									text : '高级搜索',
									iconCls : 'win-ok-icon',
									scope : this,
									handler : this.onSearchAdv
								}]
					}]
		}, {
			xtype : 'pagingtoolbar',
			store : this.store,
			dock : 'bottom',
			displayInfo : true,
			displayMsg : '显示 {0} - {1} 条，共计 {2} 条',
			emptyMsg : '没有数据',
			items : [{
						xtype : Ext.create('mvc.tools.ComboxPaging', {
									myList : this
								})
					}]
		}];
			 *** End myComponent *********/
			
			/** Begin onGetPriceNames ********
			var me = this;
			Ext.Ajax.request({
			async : false,
			url : base_path + '/gs_GsPrice_getPriceNames?pkey=' + newv,
			method : 'GET',
			success : function(response, options) {
			var nameslength = response.responseText.lastIndexOf("-");
				var names = response.responseText.substring(0,nameslength).split('-');
			for (var i = 0; i < 12; i++) {
				var fld = me.down('[dataIndex=bean.price' + (i + 1) + ']');
				if (i < names.length) {
					fld.setText(names[i]);
					fld.show();
				} else {
					fld.hide();
				}
			}
			}
			});
			 *** End onGetPriceNames *********/
			/** Begin onSearchDo ********
			var price = this.down('[name=price]').getValue();
					this.getSelectionModel().deselectAll();
					if (array.length == 0||price==null){
						Ext.MessageBox.show({
							title : "错误",
							msg : "搜索时必须填写【定价名称】",
							buttons : Ext.MessageBox.OK,
							icon : Ext.MessageBox.ERROR
						})
					} else {
						this.store.clearFilter(true);
						this.store.filter(array);
						this.onGetPriceNames(array[0].value);
					}
			*** End onSearchDo *********/
			/** Begin onSearchCancel ********
			this.getSelectionModel().deselectAll();
			mvc.Tools.searchClear(this.down('toolbar'));
			 *** End onSearchCancel *********/
			// @formatter:on
		}

	}

	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void initFuns() {
			AddFun("onLoadGoodsForm", EGsPriceGoods.class).addFunParasExp("goodsCode,form");
			super.initFuns();
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 100).add(WIDTH, 320).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsPriceGoods.T.GOODS.getFld().getCode())) {
				fldList.add(new myGoods());
			} else if (fld.getCode().equals(GsPriceGoods.T.PRICE_KIND.getFld().getCode())) {
				fldList.add(new MyPriceKind()).setCloseStr(null);
			} else if (fld.getCode().equals(GsPriceGoods.T.PRICE_COST.getFld().getCode())) {
				fldList.add(new MyPriceCost());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

		@Override
		public void initComponent(ExtFunDefine fun) {
			fun.add(loadFunCode(EMForm.class, "initComponent"));
			//@formatter:off
			/** Begin initComponent ********
				if (this.insFlag)
					this.url = this.url + 'ins';
				else
					this.url = this.url + 'upd';
				var formFlds = [];
				formFlds.push
			*** End initComponent *********/		
			//@formatter:on
			fun.add(getColumns());
			fun.add("	this.items = ");
			fun.AddDime(getForm()); // 是否需要"[]"有待验证 whx 20141015
			fun.add("	this.callParent(arguments);" + LN);
		}
		//@formatter:off
		/** Begin onLoadGoodsForm ********
		if (!goodsCode)
			return null;
		if (goodsCode.indexOf(bean_split) != -1) //两种情况：手动输入代码 或 选择器带回
			goodsCode = goodsCode.split(bean_split)[1]
		var urlGoods = base_path+ '/loadInfo?sarg1=' + goodsCode;
		var rtn = null;
		Ext.Ajax.request({
			async : true,
			url : urlGoods,
			method : 'GET',
			success : function(response) {
    			rtn = Ext.JSON.decode(response.responseText, true);
				if (!rtn) {
						form.down('[name=bean.goods]')
								.setValue(null);
						form.down('[name=link.goodsName]')
						.setValue(null);
						form.down('[name=link.goodsSpec]')
								.setValue(null);
						form.down('[name=bean.uom]')
								.setValue(null);
					} else {
						form.down('[name=bean.goods]')
						.setValue(rtn.goods);
						form.down('[name=link.goodsName]')
								.setValue(rtn.goodsName);
						form.down('[name=link.goodsSpec]')
								.setValue(rtn.goodsSpec);
						form.down('[name=bean.uom]')
								.setValue(rtn.uom);
					}    				
			},
			failure : function(response) {
				Ext.example.msg(msg_title, msg_ajax);
			}
		});
		*** End onLoadGoodsForm *********/		
		//@formatter:on
	}

	public static class MyPriceKind implements IExtOut {

		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoods.TB).loadFunCode(EGsPriceGoods.class, "myPriceKind"));
		}

		@Override
		public String toString(int tabs) {
			return null;
		}
		// @formatter:off
		/** Begin myPriceKind ********
mvc.Tools.crtComboTrigger(false,'gs_GsPriceGoodsKind','',{
		name : 'bean.priceKind',
		fieldLabel : '基础价格分类',
		listeners : {
			scope : this,
			change : function(field,newv,oldv,opts) {
				var me = this;
				Ext.Ajax.request({
					async : false,
					url : base_path+'/gs_GsPriceGoodsKind_getPriceNamesAndRates?pkey=' + newv,
					method : 'GET',
					success : function (response, options) {
						if(response.responseText == '') {
							for (var i = 0; i < 12; i++) {
								var fld = me.down('[name=bean.price' + (i + 1) + ']');
								fld.setValue(null);
								fld.hide();
							}
							return;
						}
						var cost = me.down('[name=bean.priceCost]');
						var names = response.responseText.split('||')[0].split('##');
						rates = response.responseText.split('||')[1].split('##');
						for (var i = 0; i < 12; i++) {
							var fld = me.down('[name=bean.price' + (i + 1) + ']');
							if (i < names.length) {
								fld.setFieldLabel(names[i]);
								fld.setValue(rates[i]*cost.getValue());
								fld.show();
							} else {
								fld.setValue(null);
								fld.hide();
							}
						}
						me.doLayout();
					}
				});
			}
		}
		})
		 *** End myPriceKind *********/
		// @formatter:on
	}

	public static class myGoods implements IExtOut {

		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoods.TB).loadFunCode(EGsPriceGoods.class, "myGoods"));
		}

		@Override
		public String toString(int tabs) {
			return null;
		}
		// @formatter:off
		/** Begin myGoods ********
		xtype : 'comboauto',
		listConfig : {minWidth:250},
		fieldLabel : '货物',
		fields : ['pkey','code','name','spec'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		name : 'bean.goods', //提交使用
		url : base_path + '/gs_GsGoods_autoComplete',
		urlExt : 'gs.GsGoods',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		readOnly : !this.insFlag,
		listeners : {
			scope : this,
			blur : function(field){
				this.onLoadGoodsForm(field.getRawValue(),this);
			}
		}
		 *** End myGoods *********/
		// @formatter:on
	}

	public static class MyPriceCost implements IExtOut {

		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoods.TB).loadFunCode(EGsPriceGoods.class, "myPriceCost"));
		}

		@Override
		public String toString(int tabs) {
			return null;
		}
		// @formatter:off
		/** Begin myPriceCost ********
xtype : 'numberfield',name : 'bean.priceCost',afterLabelTextTpl : required,allowBlank : false,fieldLabel : '定价基数',decimalPrecision : 4, value : 0,
		listeners : {
			scope : this,
			change : function(field,newv,oldv,opts) {
				for (var i = 0; i < 12; i++) {
					var fld = this.down('[name=bean.price' + (i + 1) + ']');
					fld.setValue(rates[i]*newv);
				}
			}
		}
		 *** End myPriceCost *********/
		// @formatter:on
	}

	/*
	 * public static VFlds getGoodsVflds(Fld fld) {
	 * Fld fldName = new FldStr(fld.getCode() + "Name", fld.getName() + "名称", 100,
	 * true);
	 * Fld fldSpec = new FldStr(fld.getCode() + "Spec", fld.getName() + "规格", 100,
	 * true);
	 * //Fld fldUom = new FldStr(fld.getCode() + "Uom", "计量单位", 100, true);
	 * VFlds vflds = new
	 * VFlds("link").add(fldName.getVFld()).add(fldSpec.getVFld()
	 * ).add(fldUom.getVFld());
	 * return vflds;
	 * }
	 */
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

		public ExtFile newList() {
			if (_list == null)
				_list = new MyList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
			return _list;
		}
	}

//@formatter:off	
/** Begin onTriggerList ********
			var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			this.fireEvent('trigger', selection.get('bean.pkey') + bean_split + selection.get('bean.priceKind').split(bean_split)[1], null);
		}
*** End onTriggerList *********/
//@formatter:on
}
