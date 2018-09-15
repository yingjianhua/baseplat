/**
 * 
 */
package irille.dep.sal;

import irille.pss.sal.SalRtn;
import irille.pss.sal.SalRtnLine;
import irille.pss.sal.SalSale;
import irille.pss.sal.SalSale.T;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMWin;
import irille.pub.html.EMWinSearch;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListMain;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.svr.Act;
import irille.pub.svr.Act.OAct;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class ESalRtn extends SalRtn {
	public static void main(String[] args) {
		new ESalRtn().crtExt();
	}

	public void crtExt() {
		CmbGoods.TB.getCode();
		SalRtnLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.CUST, T.CUST_NAME, T.AMT, T.AMT_PAY, T.AMT_REC, T.STATUS,
		    T.CREATED_BY, T.CREATED_TIME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.CUST_NAME, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(SalRtnLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG);
		vl.setExpandAndHidden("true", T.AMT_COST, T.INOUT_STATUS, T.DEPT, T.CELL, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
		    T.TALLY_TIME, T.CREATED_BY, T.CREATED_TIME, T.REM);
		((EMZipListMain) ext.newZipListMain()).setExtendRow();

		VFlds vs = ext.getVfldsForm();
		vs.del(T.AMT_COST, T.AMT_REC_BACK, T.INOUT_STATUS, T.ORG, T.DEPT, T.CELL, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY,
		    T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME);// FROM页面删除建档员等字段
		vs.moveAfter(T.AMT, T.STATUS);
		vs.moveLast(T.REM);
		vs.setReadOnly("true", T.CODE, T.STATUS, T.AMT, T.CUST_NAME);// 只读设置
		vs.setNull(true, T.CODE, T.STATUS, T.AMT, T.CUST_NAME);// 为空设置
		ext.newExts().init();
		ext.crtFiles();
	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void loadTbAct(Class funCodeFile, Act act) {
			IEnumOpt oact = act.getAct();
			if (act.getCode().equals("crtGs")) {
				ExtAct v = new ExtAct(this, act, ESalRtn.class);
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
			l.addExp("listeners", loadFunCode(ESalRtn.class, "initChange", getOutVFlds().get(0).getCode()));
		}

		public void initFuns() {
			AddFun("onChangeStatus", ESalSale.class).addFunParasExp("status,ordStatus");
			super.initFuns();
		}

		//@formatter:off	
		/** Begin onCrtGs ********
		var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		var me = this;
		if (selection){
			Ext.MessageBox.confirm(msg_confirm_title, '退货单['+selection.get('bean.code') + '] - 产生入库单确认吗？',
				function(btn) {
					if (btn != 'yes')
						return;
					Ext.Ajax.request({
						url : base_path+'/sal_SalRtn_crtGs?pkey='+selection.get('bean.pkey')+'&rowVersion='+selection.get(BEAN_VERSION),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								var bean  = Ext.create('mvc.model.sal.SalRtn',result);
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
			);
		}
		 *** End onCrtGs *********/
		
		/** Begin onChangeStatus ********
	if (this.roles.indexOf('upd') != -1)
		this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('del') != -1)
		this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
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
	if (this.roles.indexOf('print') != -1)
		this.down('#'+this.oldId+'print').setDisabled(status == -1);
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
			if (fld.getCode().equals(SalRtn.T.CUST.getFld().getCode())) {
				fldList.add(new MyCust());
			} else if (fld.getCode().equals(SalRtn.T.WAREHOUSE.getFld().getCode())) {
				fldList.add(new ESalRtn.MyWarehouse()).setCloseStr(null);
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
			buf.append(new EMModel(SalRtn.TB).loadFunCode(ESalRtn.class, "myWarehouse"));
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
	 * 重构IEXTOUT，用于FORM中客户的控件实现
	 * 
	 * @author whx
	 * @version 创建时间：2014年11月7日 下午5:09:31
	 */
	class MyCust implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(SalRtn.TB).loadFunCode(ESalRtn.class, "myCust"));
		}

		//@formatter:off	
  		/** Begin myCust ********
 		xtype : 'comboauto',
		fieldLabel : '客户',
		name : 'bean.cust',
		listConfig : {minWidth:250},
		fields : ['pkey','code','name','spec'],//查询返回信息model
		valueField : ['pkey'],//提交值
		textField : 'code', //显示信息
		queryParam : 'code',//搜索使用
		url : base_path + '/sys_SysCustom_autoComplete',
		urlExt : 'sys.SysCustom',
		hasBlur : false,
		afterLabelTextTpl : required,
		allowBlank : false,
		listeners : {
			scope : this,
			blur : function(field){
				var me = this;
				if (!field.getRawValue()){
					me.down('[name=bean.custName]').setValue(null);
					me.down('[name=bean.operator]').setValue(null);
	    		return;
	    	}
				var urlCust = base_path+ '/sys_SysCustom_loadInfoDetail?sarg1=' + field.getRawValue();
	    		Ext.Ajax.request({
	    			//async : false, //加上同步限制后，单元格之间切换会中断
	    			url : urlCust,
	    			method : 'GET',
	    			success : function(response) {
	    				rtn = Ext.JSON.decode(response.responseText, true);
	    				me.down('[name=bean.cust]').setValue(rtn.cust);
	    				me.down('[name=bean.custName]').setValue(rtn.custName);
	    				me.down('[name=bean.operator]').setValue(rtn.business);
	    			},
	    			failure : function(response) {
	    				Ext.example.msg(msg_title, msg_ajax);
	    			}
	    		});
			}
		} 		
  		
  		*** End myCust *********/
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

		public void initForm() {
			EMWin.initForm(getForm(), getPack(), getClazz());
			getFormList().addExp(XTYPE,
			    "Ext.create('mvc.view." + getPack() + "." + getClazz() + ".ListForm',{height : 250,border : false })");
		}

		@Override
		public void initActs() {
			getActs().addExp("{xtype : 'label',itemId : 'cqty'},'->'");
			AddAct(new Act(getTb(), OAct.RESET), EMZipWin.class);
			AddAct(new Act(getTb(), OAct.CLOSE), EMZipWin.class);
			AddAct(new Act(getTb(), OAct.SAVE), EMZipWin.class);
		}
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

		@Override
		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;//后来又加了取默认价、计划金额功能，暂不用产生器产生 TODO
		}

		@Override
		public VFlds getNowOutKeyZipListFormVflds() {
			VFlds vs = super.getNowOutKeyZipListFormVflds();
			vs.del(SalRtnLine.T.EXPENSES_SALE, SalRtnLine.T.PRICE_OLD);
			return vs;
		}

	}

}
