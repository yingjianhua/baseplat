package irille.dep.gs;

import irille.dep.gs.EGsPriceGoods.MyPriceCost;
import irille.dep.gs.EGsPriceGoods.myGoods;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsCell;
import irille.gl.gs.GsPriceGoodsKind;
import irille.gl.gs.GsPriceGoods.T;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
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

public class EGsPriceGoodsCell extends GsPriceGoodsCell {

	public static void main(String[] args) {
		new EGsPriceGoodsCell().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB).add(GsGoods.T.UOM), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) });
		vflds.moveBefore(GsGoods.T.UOM, T.PRICE_COST);
		vflds.get(GsGoods.T.UOM).setReadOnly("true").setNullTrue();
		vflds.get(T.GOODS).setReadOnly("true");
		vflds.moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		VFlds searchVflds = new VFlds(GsPriceGoodsKind.T.PRICE, T.GOODS, T.CELL);
		EMCrt ext = new MyComp(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.del(T.PRICE_NAME);
		vl.setHidden("true", T.PRICE1, T.PRICE2, T.PRICE3, T.PRICE4, T.PRICE5, T.PRICE6, T.PRICE7, T.PRICE8, T.PRICE9, T.PRICE10, T.PRICE11, T.PRICE12);
		ext.getVfldsForm().setGoodsLink(T.GOODS);
		VFlds vf = ext.getVfldsForm();
		vf.get(GsGoods.T.UOM).attrs().addExp("disabled", "true").add("disabledCls", "");
		vf.get(T.CELL).setReadOnly("!this.insFlag");
		vf.del(T.ENABLED, T.PRICE_NAME);
		vf.setReadOnly("true", T.GOODS);
		vf.setHidden("true", T.PRICE1, T.PRICE2, T.PRICE3, T.PRICE4, T.PRICE5, T.PRICE6, T.PRICE7, T.PRICE8, T.PRICE9, T.PRICE10, T.PRICE11, T.PRICE12);
		((EMWin) ext.newWin()).setWidth(670);
		ext.newExts().init();
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
			// fun.add("		mvc.Tools.doGoodsLine(this.columns, 1);" + LN);
			fun.add(loadFunCode(EMList.class, "myComponent", getPack(), getClazz()));
		/*	fun.add("this.dockedItems=");
			fun.AddDime().add(getFormDocked()).add(getFormTable());*/
			fun.add("		this.callParent(arguments);");
			fun.add("		mvc.Tools.onENTER2SearchBar(this.down('[dock=top]'),this);");
			// @formatter:off
			/** Begin myComponent ********
			mvc.Tools.doGoodsLine(this.columns, 3);
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
									url : base_path
											+ 'gs_GsPrice_getComboTrigger',
									method : 'POST',
									success : function(response) {
										keys = Ext.JSON.decode(response.responseText).items[0].value;
										keys += '##' + Ext.JSON.decode(response.responseText).items[0].text;
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
			AddFun("onLoadGoodsForm", EGsPriceGoodsCell.class).addFunParasExp("goodsCode,form");
			super.initFuns();
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 100).add(WIDTH, 320).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsPriceGoodsCell.T.PRICE_GOODS.getFld().getCode())) {
				fldList.add(new MyPriceGoods());
			} else if (fld.getCode().equals(GsPriceGoods.T.PRICE_COST.getFld().getCode())) {
				fldList.add(new MyPriceCost());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}
		// @formatter:off
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
		// @formatter:on
	}

	public static class MyPriceGoods implements IExtOut {

		@Override
		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoodsCell.TB).loadFunCode(EGsPriceGoodsCell.class, "myPriceGoods"));
		}

		@Override
		public String toString(int tabs) {
			return null;
		}
		// @formatter:off
		/** Begin myPriceGoods ********
		xtype : 'beantrigger',
		name : 'bean.priceGoods',
		fieldLabel : '基础价格信息',
		bean : 'GsPriceGoods',
		beanType : 'gs',
		emptyText : form_empty_text,
		afterLabelTextTpl : required,
		readOnly : !this.insFlag,
		allowBlank : false,
		diySql : '',
		listeners : {
			scope : this,
			change : function(field,newv,oldv,opts) {
				var me = this;
				Ext.Ajax.request({
					async : false,
					url : base_path+'/gs_GsPriceGoods_getGCPNR?pkey=' + newv,
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
						var goods = me.down('[name=bean.goods]');
						var cost = me.down('[name=bean.priceCost]');
						var goodsInfo = response.responseText.split('||')[0];
						var costInfo = response.responseText.split('||')[1];
						var prices = response.responseText.split('||')[2].split('##');
						var names = response.responseText.split('||')[3].split('##');
						rates = response.responseText.split('||')[4].split('##');
						goods.setValue(goodsInfo);
						me.onLoadGoodsForm(goods.getRawValue(),me);
						cost.setValue(costInfo);
						for (var i = 0; i < 12; i++) {
							var fld = me.down('[name=bean.price' + (i + 1) + ']');
							if (i < names.length) {
								fld.setFieldLabel(names[i]);
								fld.setValue(prices[i]);
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
		 *** End myPriceGoods *********/
		// @formatter:on
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

		public ExtFile newList() {
			if (_list == null)
				_list = new MyList(getTb(), getVfldsList()).setSearchVFlds(getSearchVflds());
			return _list;
		}
	}
}
