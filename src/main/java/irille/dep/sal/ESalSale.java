/**
 * 
 */
package irille.dep.sal;

import irille.core.sys.SysShiping;
import irille.gl.gs.GsStockBatch.T;
import irille.pss.sal.SalOrder;
import irille.pss.sal.SalSale;
import irille.pss.sal.SalSaleLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMWinSearch;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListMain;
import irille.pub.html.ExtExp;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ESalSale extends SalSale {
	public static void main(String[] args) {
		new ESalSale().crtExt();
	}
//VFlds.newVFlds(vflds)
	public void crtExt() {
		CmbGoods.TB.getCode();
		SalSaleLine.TB.getCode();
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
		    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("收货人名称");
		ship.get(SysShiping.T.ADDR).setName("收货地址");
		ship.get(SysShiping.T.MOBILE).setName("收货人手机");
		ship.get(SysShiping.T.TEL).setName("收货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING), ship };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.CUST, T.CUST_NAME, T.AMT, T.AMT_PAY, T.AMT_REC,
				T.STATUS, T.CREATED_BY, T.CREATED_TIME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.CUST_NAME, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(SalSaleLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG);
		vl.setExpandAndHidden("true", T.AMT_COST, T.AMT_ORD, T.INOUT_STATUS, T.DEPT, T.CELL, T.APPR_BY, T.APPR_TIME,
		    T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY, T.CREATED_TIME, T.REM, T.ORD, T.SHIP_BY);
		vl.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE, SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
		    SysShiping.T.TIME_SHIP_PLAN);
		((EMZipListMain) ext.newZipListMain()).setExtendRow();
		
		VFlds vs = ext.getVfldsForm();
		vs.del(T.AMT_COST,T.AMT_REC_BACK, T.INOUT_STATUS, T.ORG, T.DEPT, T.CELL, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME,
		    T.TALLY_BY, T.TALLY_TIME);// FROM页面删除建档员等字段
		vs.moveAfter(T.AMT, T.STATUS);
		vs.moveLast(T.BILL_FLAG, T.REM, T.ORD);
		vs.get(T.ORD).setHidden("true");
		vs.setReadOnly("true", T.CODE, T.STATUS, T.AMT, T.CUST_NAME);// 只读设置
		vs.setNull(true, T.CODE, T.STATUS, T.AMT, T.CUST_NAME);// 为空设置
		vs.setHidden("true", SysShiping.T.TIME_ARR_PLAN,SysShiping.T.TIME_SHIP_PLAN,
				SysShiping.T.NAME,SysShiping.T.ADDR,SysShiping.T.MOBILE,SysShiping.T.TEL);
		
		ext.newExts().init().crtFiles();
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
		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("ins")) {
				ExtAct v = new ExtAct(this, act, EMZipList.class);
				v.add(XTYPE, "splitbutton").add(TEXT, act.getName()).add(ICON_CLS, act.getIcon())
				    .addExp("itemId", "this.oldId+'" + act.getCode() + "'").add(SCOPE, EXP_THIS)
				    .addExp(HANDLER, "this.on" + act.getCodeFirstUpper())
				    .addExp("menu", "[{text:'从订单新增',iconCls : 'ins-icon', scope : this,handler: this.onInsOrderLoad}]");
				getActs().add(v);
			} else if (act.getCode().equals("crtGs")) {
				ExtAct v = new ExtAct(this, act, ESalSale.class);
				v.add(TEXT, act.getName()).add(ICON_CLS, "upd-icon").addExp("itemId", "this.oldId+'" + act.getCode() + "'")
				    .add(SCOPE, EXP_THIS).addExp(HANDLER, "this.on" + act.getCodeFirstUpper()).addExp("disabled", "this.lock");
				getActs().add(v);
			} else {
				super.loadTbAct(funCodeFile, act);
			}
		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(ESalSale.class, "initChange", getOutVFlds().get(0).getCode()));
		}

		public void initFuns() {
			AddFun("onChangeStatus", ESalSale.class).addFunParasExp("status,ordStatus");
			super.initFuns();
			add(AddFun("onInsOrderLoad", ESalSale.class));
			add(AddFun("onInsOrder", ESalSale.class).addFunParas(new ExtExp("data, params")));
		}
		//@formatter:off	
		/** Begin onInsOrderLoad ********
			var wint = Ext.create('mvc.view.sal.SalOrder.Trigger');
			wint.on('trigger', this.onInsOrder, this);
			wint.show();
			var array = [{
            	id:'diy',
                property: 'diy',
                value: 'status>='+STATUS_TALLY + ' and ord_status=1' 
            }];
			wint.down('grid').store.filter(array);
		 *** End onInsOrderLoad *********/
		
		/** Begin onInsOrder ********
			var win = Ext.create('mvc.view.sal.SalSale.Win',{
				title : '销售单>从销售订单['+data.split('##')[1]+']新增'
			});
			win.on('create',this.onSaveRecord,this);
			win.show();
			win.setActiveOrder(data.split('##')[0]);
	 *** End onInsOrder *********/
		
		/** Begin onCrtGs ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '销售单['+selection.get('bean.code') + '] - 产生出库单确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/sal_SalSale_crtGs?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.sal.SalSale',result);
								Ext.apply(selection.data, bean.data);
								selection.commit();
								me.mdMainTable.getSelectionModel().deselectAll();
								me.mdMainTable.getView().select(selection);
								Ext.example.msg(msg_title, '产生出库单--成功');
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
		 *** End onCrtGs *********/
		
		/** Begin onChangeStatus ********
	if (this.roles.indexOf('upd') != -1)
		this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('del') != -1)
		this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('print') != -1)
		this.down('#'+this.oldId+'print').setDisabled(status == -1);
	if (this.roles.indexOf('doAppr') != -1)
		this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('unAppr') != -1)
		this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('doNote') != -1)
		this.down('#'+this.oldId+'doNote').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('doTally') != -1)
		this.down('#'+this.oldId+'doTally').setDisabled(status != STATUS_TALLY);
	if (this.roles.indexOf('unTally') != -1)
		this.down('#'+this.oldId+'unTally').setDisabled(status != STATUS_DONE);
	if (this.roles.indexOf('crtGs') != -1)
		this.down('#'+this.oldId+'crtGs').setDisabled(ordStatus != 1 || status < STATUS_TALLY);
		*** End onChangeStatus *********/

		/** Begin initChange ********
		{
				scope : this,
        selectionchange: function(model, records) {
            if (records.length === 1){
                this.mdMain.getForm().loadRecord(records[0]);
								this.mdLineTable.store.filter([{'id':'filter', 'property':'pkey','value':records[0].get('bean.pkey')}]);
								var status = records[0].get('bean.status'); //根据单据状态判断
								var ordStatus = records[0].get('bean.inoutStatus'); //根据单据状态判断
								this.onChangeStatus(status, ordStatus);
            }else{
            	this.mdMain.getForm().reset();
            	this.mdLineTable.store.removeAll();
            	this.onChangeStatus(-1, -1);
            }
        }
    }
*** End initChange *********/
		//@formatter:on	

	}

	/**
	 * 重构FORM， 自定义LABEL长度，字段长度，布局改为TABLE-3列 setFldAttr中对客户字段完全重写--更改控件类型及相关事件处理
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:04:57
	 */
	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275).add(LABEL_STYLE, "font-weight : bold");
		}

		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3).add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(SalSale.T.CUST.getFld().getCode())) {
				fldList.add(new ESalOrder.MyCust());
			} else if (fld.getCode().equals(SalSale.T.SHIPING_MODE.getFld().getCode())) {
				fldList.add(new ESalOrder.MyShip()).setCloseStr(null);
			} else if (fld.getCode().equals(SalSale.T.WAREHOUSE.getFld().getCode())) {
				fldList.add(new ESalSale.MyWarehouse()).setCloseStr(null);
			}	else {
				super.setFldAttr(fld, fldList);
			}
		}

	}
	public static class MyWarehouse implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(SalSale.TB).loadFunCode(ESalSale.class, "myWarehouse"));
		}

		//@formatter:off	
  		/** Begin myWarehouse ********
		mvc.Tools.crtComboTrigger(false,'gs_GsWarehouse','',{
					name : 'bean.warehouse',
					fieldLabel : '仓库',
					listeners: {
						scope : this,
						change : function(field, newValue, oldValue, eOpts) {
							var grid = this.up('window').lineTable;
							mvc.Tools.doLoadStock(grid);
						}
					}
				})
  		*** End myWarehouse *********/
  		//@formatter:on	
	}

	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:11:55
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}

		public ExtFile newZipWin(VFld fld) {
			return null; //增加从订单新增的处理
		}

		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;//后来又加了取默认价、计划金额功能，暂不用产生器产生 TODO
		}

		@Override
		public VFlds getNowOutKeyZipListFormVflds() {
			VFlds vs = super.getNowOutKeyZipListFormVflds();
			vs.del(SalSaleLine.T.EXPENSES_SALE);
			return vs;
		}

	}

}
