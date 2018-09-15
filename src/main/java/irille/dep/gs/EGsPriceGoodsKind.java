package irille.dep.gs;

import irille.gl.gs.GsPriceGoodsKind;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMForm;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMList;
import irille.pub.html.EMModel;
import irille.pub.html.EMWinTwoRow;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtFunDefine;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsPriceGoodsKind extends GsPriceGoodsKind {

	public static void main(String[] args) {
		new EGsPriceGoodsKind().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds vflds = new VFlds(TB) ;
		VFlds searchVflds = new VFlds(T.PRICE, T.CODE, T.NAME);
		VFlds listFlds = new VFlds().addAll(TB);
		MyComp ext = new MyComp(TB, vflds, searchVflds);
		ext.setVfldsList(listFlds);
		ext.getVfldsList().moveLast(T.PRICE_ORIG, T.ENABLED);
		ext.getVfldsList().get(T.CODE).setWidthList(100);
		ext.getVfldsForm().del(T.ENABLED);
		ext.getVfldsForm().setHidden("true", GsPriceGoodsKind.T.RATE1, GsPriceGoodsKind.T.RATE2, GsPriceGoodsKind.T.RATE3,
		    GsPriceGoodsKind.T.RATE4, GsPriceGoodsKind.T.RATE5, GsPriceGoodsKind.T.RATE6, GsPriceGoodsKind.T.RATE7,
		    GsPriceGoodsKind.T.RATE8, GsPriceGoodsKind.T.RATE9, GsPriceGoodsKind.T.RATE10, GsPriceGoodsKind.T.RATE11,
		    GsPriceGoodsKind.T.RATE12);
		((MyForm)ext.newForm()).setWidthLabel(120);
		ext.newExts().init();
		return ext;
	}

	/**
	 * 重构FORM， 自定义LABEL长度，字段长度，布局改为TABLE-3列 setFldAttr中对客户字段完全重写--更改控件类型及相关事件处理
	 * 
	 */
	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsPriceGoodsKind.T.PRICE.getFld().getCode())) {
				fldList.add(new MyPrice()).setCloseStr(null);
			} else if (fld.getCode().equals(GsPriceGoodsKind.T.RANGE_TYPE.getFld().getCode())) {
				fldList.add(new MyRangeType()).setCloseStr(null);
			} else if (fld.getCode().equals(GsPriceGoodsKind.T.RANGE_PKEY.getFld().getCode())) {
				fldList.add(new MyRangePkey()).setCloseStr(null);
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
			fun.add(loadFunCode(EMForm.class, "myBot"));
			//@formatter:off
			/** Begin myBot ********
	var rt = this.down('[name=bean.rangeType]');
	var rp = this.down('[name=bean.rangePkey]');
	if (rt.getValue() == 1) {
		rp.setDisabled(true);
		rp.hide();
	} else if (rt.getValue() > 10 && rt.getValue() <= 20) {
		rp.setFieldLabel('可视机构');
		rp.store.proxy.url = base_path + '/sys_SysOrg_getComboTrigger';
		rp.store.load();
	} else if (rt.getValue() >20 && rt.getValue() <= 30) {
		rp.setFieldLabel('可视单元');
		rp.store.proxy.url = base_path + '/sys_SysCell_getComboTrigger';
		rp.store.load();
	}
		*** End myBot *********/		
		//@formatter:on
		}
	}

	public static class MyPrice implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoodsKind.TB).loadFunCode(EGsPriceGoodsKind.class, "myPrice"));
		}

		//@formatter:off	
  		/** Begin myPrice ********
		mvc.Tools.crtComboTrigger(false,'gs_GsPrice','',{
					name : 'bean.price',
					fieldLabel : '定价名称',
					listeners : {
						scope : this,
						change : function(field,newv,oldv,opts) {
							var me = this;
							Ext.Ajax.request({
								async : false,
								url : base_path+'/gs_GsPrice_getPriceNames?pkey=' + newv,
								method : 'GET',
								success : function (response, options) {
									if(response.responseText == '') {
										for (var i = 0; i < 12; i++) {
											var fld = me.down('[name=bean.rate' + (i + 1) + ']');
											fld.setValue(null);
											fld.setValue(0);
											fld.hide();
										}
										return;
									}
									var nameslength = response.responseText.lastIndexOf("-");
									var names = response.responseText.substring(0,nameslength).split('-');
									for (var i = 0; i < 12; i++) {
										var fld = me.down('[name=bean.rate' + (i + 1) + ']');
										if (i < names.length) {
											fld.setFieldLabel(names[i] + '利润率(%)');
											fld.setValue(0);
											fld.show();
										} else {
											fld.setValue(null);
											fld.hide();
										}
									}
									var rangelength = response.responseText.indexOf("*")+1;
									var range = response.responseText.substring(rangelength,response.responseText.length).split('*');
									me.down('[name=bean.cell]').setValue(range[0]);
									if(range[1] > 10){
										me.down('[name=bean.rangePkey]').setValue(range[2]);	
									}
									me.down('[name=bean.rangeType]').setValue(Number(range[1]));
									me.doLayout();
								}
							});
						}
					}
				})
  		*** End myPrice *********/
  		//@formatter:on	
	}

	public static class MyRangeType implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoodsKind.TB).loadFunCode(EGsPriceGoodsKind.class, "myRangeType"));
		}

		//@formatter:off	
  		/** Begin myRangeType ********
		mvc.Tools.crtComboForm(false,{
					name : 'bean.rangeType',
					fieldLabel : '可视范围',
					store : Ext.create('mvc.combo.sys.SysORangeType'),
					value : 1,
					listeners : {
						scope : this,
						change : function(field,newv,oldv,opts) {
							var range = this.down('[name=bean.rangePkey]');
							if (newv <= 10) {
								range.setDisabled(true);
								range.hide();
							} else {
								if (newv > 10 && newv <= 20) {
									range.setFieldLabel('可视机构');
									range.store.proxy.url = base_path + '/sys_SysOrg_getComboTrigger';
								} else if (newv >20 && newv <= 30) {
									range.setFieldLabel('可视单元');
									range.store.proxy.url = base_path + '/sys_SysCell_getComboTrigger';
								}
								range.store.load();
								range.setDisabled(false);
								range.show();
							}
							this.doLayout();
						}
					}
				})
  		*** End myRangeType *********/
  		//@formatter:on	
	}

	public static class MyRangePkey implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsPriceGoodsKind.TB).loadFunCode(EGsPriceGoodsKind.class, "myRangePkey"));
		}

		//@formatter:off	
  		/** Begin myRangePkey ********
		mvc.Tools.crtComboTrigger(false,'sys_SysOrg','',{
			name : 'bean.rangePkey',
			fieldLabel : '可视机构',
			hidden : this.insFlag ? true : false,
			disabled : this.insFlag ? true : false
		})
  		*** End myRangePkey *********/
  		//@formatter:on	
	}

	class MyList extends EMList<MyList> {

		public MyList(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}
		
		public void initFuns() {
			super.initFuns();
			add(AddFun("onGetPriceNames", EGsPriceGoodsKind.class).addFunParasExp("newv"));
			add(AddFun("onSearchDo", EGsPriceGoods.class).addFunParasExp("array"));
		}
		// @formatter:off
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
						var fld = me.down('[dataIndex=bean.rate' + (i + 1) + ']');
						if (i < names.length) {
							fld.setText(names[i] + '利润率(%)');
							fld.show();
						} else {
							fld.hide();
						}
					}
				}
			})
		 *** End onGetPriceNames *********/
			// @formatter:on
		public void initComponent(ExtFunDefine fun) {
			// 明细行功能定义
			initLineActs(fun);
			initMainActs(fun);
			fun.add("this.columns = ");
			fun.add(getColumns());
			fun.add(loadFunCode(EMList.class, "MyComponent", getPack(), getClazz()));
			fun.add("		this.callParent(arguments);");
			fun.add("		mvc.Tools.onENTER2SearchBar(this.down('[dock=top]'),this);");
		// @formatter:off
					/** Begin MyComponent ********
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
								text : '代码：'
							},{
								xtype : 'textfield',
								name : 'code'
							},'',{
								xtype : 'label',
								text : '名称：'
							},{
								xtype : 'textfield',
								name : 'name'
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
					 *** End MyComponent *********/
		// @formatter:on
		}
	}

	class MyComp extends EMCrtSimpleTwo<MyComp> {

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
