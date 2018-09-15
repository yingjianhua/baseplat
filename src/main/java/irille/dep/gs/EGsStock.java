package irille.dep.gs;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockLine;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMForm;
import irille.pub.html.EMModel;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsStock extends GsStock {

	public static void main(String[] args) {
		new EGsStock().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GsStockLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { VFlds.newVFlds(new VFlds[] { new VFlds(TB).add(GsGoods.T.UOM),
		    EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) }) };
		vflds[0].moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		VFlds[] mflds = new VFlds[] { new VFlds(T.WAREHOUSE, T.GOODS, T.LOCATION, T.QTY, T.ENROUTE_QTY, T.LOCKED_QTY,
		    GsGoods.T.NAME, GsGoods.T.SPEC, GsGoods.T.UOM) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.WAREHOUSE, T.GOODS, T.LOCATION) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		EMCrtCompSimple ext = new MyCrt(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(GsStockLine.T.STOCK) });
		VFlds mainTable = ext.getMainFlds();
		mainTable.get(GsGoods.T.NAME).setCode("goodsName").setBeanCode("link.");
		mainTable.get(GsGoods.T.SPEC).setCode("goodsSpec").setBeanCode("link.");
		ext.getVfldsForm().del(GsGoods.T.UOM);
		VFlds vl = ext.getVfldsList();
		vl.moveBefore(GsGoods.T.UOM, T.QTY);
		VFlds vs = ext.getVfldsForm();
		vs.del(T.PKEY, T.QTY, T.ENROUTE_QTY, T.LOCKED_QTY, T.ENABLED, T.CELL);// FROM页面删除建档员等字段
		vs.get(T.WAREHOUSE).setReadOnly("!this.insFlag");
		vs.get(T.GOODS).setReadOnly("!this.insFlag");
		vs.setGoodsLink(T.GOODS); //FORM里带出货物名称与规格
//		vs.get(T.WAREHOUSE).attrs().add("aaa", "bbbb");
		((EMForm) ext.newForm()).setGoodsLink(true); // 货物的空间重写
		ext.newExts().init();
		return ext;
	}

	class MyForm extends EMForm {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(GsStock.T.WAREHOUSE.getFld().getCode())) {
				fldList.add(new MyWarehouse()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

	}

	public static class MyWarehouse implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(GsStock.TB).loadFunCode(EGsStock.class, "myWarehouse"));
		}

		//@formatter:off	
  		/** Begin myWarehouse ********
		mvc.Tools.crtComboTrigger(false,'gs_GsWarehouse','',{
			name : 'bean.warehouse',
			fieldLabel : '仓库',
			readOnly : !this.insFlag,
			listeners : {
				scope : this,
				change : function(field, newValue, oldValue, eOpts) {
					var loc = this.down('[name=bean.location]');
					if (newValue)
						loc.diySql = 'warehouse=' + newValue;
					else
						loc.diySql = '';
				}
			}
		})
  		*** End myWarehouse *********/
		
		/** Begin initFormListMain ********
		 {
			scope : this,
             selectionchange: function(model, records) {
                 if (records.length === 1){
                     this.mdMain.getForm().loadRecord(records[0]);
							this.mdLineTable.store.filter([{'id':'filter', 'property':'【0】','value':records[0].get('bean.pkey')}]);
						if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(false);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(false);
					if (this.roles.indexOf('print') != -1)
						this.down('#'+this.oldId+'print').setDisabled(false);
                 }else{
                 	this.mdMain.getForm().reset();
                 	this.mdLineTable.store.removeAll();
                 	if (this.roles.indexOf('upd') != -1)
						this.down('#'+this.oldId+'upd').setDisabled(true);
					if (this.roles.indexOf('del') != -1)
						this.down('#'+this.oldId+'del').setDisabled(true);
					if (this.roles.indexOf('print') != -1)
						this.down('#'+this.oldId+'print').setDisabled(true);
                 }
             }
           }
		 *** End initFormListMain *********/
  		//@formatter:on	
	}

	class MyCrt extends EMCrtCompSimple {

		public MyCrt(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		@Override
		public ExtFile newForm() {
			if (_form == null)
				_form = new MyForm(getTb(), getVfldsForm());
			return _form;
		}

	}
	//@formatter:off	
		/** Begin onPrint ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var key = selection.get('bean.pkey');
		var win = Ext.create('mvc.view.gs.GsStock.Search',{
			title: '存货出入库明细时间段选择',
		    titleAlign:'center',
		    pkey : key
		});
		win.show();		 
		*** End onPrint *********/
	//@formatter:on	
}
