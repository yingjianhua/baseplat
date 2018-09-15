/**
 * 
 */
package irille.dep.pur;

import irille.pss.pur.PurRtn;
import irille.pss.pur.PurRtnLine;
import irille.pub.bean.CmbGoods;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListMain;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;



/**
 * 过滤了listForm文件
 * @author administrator
 *
 */
public class EPurRtn extends PurRtn {
	public static void main(String[] args) {
		new EPurRtn().crtExt().crtFiles();
	}
	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		PurRtnLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB)};
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME,T.WAREHOUSE,T.AMT) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME, T.WAREHOUSE,
				T.STATUS) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(PurRtnLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG, T.REM);
		vl.setExpandAndHidden("true", T.AMT_COST, T.DEPT, T.CELL,
				T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
				T.CREATED_TIME, T.REM);
		((EMZipListMain)ext.newZipListMain()).setExtendRow();
		VFlds vs = ext.getVfldsForm();
		vs.setReadOnly("true", T.SUPNAME,T.AMT);
		vs.moveAfter(T.REM,T.BILL_FLAG);
		vs.del(T.CODE,T.CELL,T.TALLY_BY,T.TALLY_TIME,T.STATUS,T.ORG,T.DEPT,T.CREATED_BY,T.CREATED_TIME,T.APPR_BY,T.APPR_TIME,T.AMT_COST);
		ext.newExts().init();
		return ext;
	}
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		public void initFuns() {
			AddFun("onChangeStatus", EPurOrder.class).addFunParasExp(
					"status");
			super.initFuns();
		}
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
					loadFunCode(EPurRtn.class, "initFormListMainStatus",
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
				        								this.onChangeStatus(status);
				                    }else{
				                    	this.mdMain.getForm().reset();
				                    	this.mdLineTable.store.removeAll();
				                    	this.onChangeStatus(-1);
				                    }
				                }
			                }
			 *** End initFormListMainStatus *********/
			/** Begin onChangeStatus ********
			if (this.roles.indexOf('upd') != -1)
				this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('del') != -1)
				this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('doAppr') != -1)
				this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
			if (this.roles.indexOf('unAppr') != -1)
				this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_TALLY );
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

	}
	
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
			if (fld.getCode().equals(PurRtn.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

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

	}
	public static class MySupplier implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurRtn.TB).loadFunCode(EPurRtn.class,
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
	    				if (rtn.supplier !=null) {
			  				var supkey = rtn.supplier.split(bean_split)[0];
			  				me.up('panel').lineTable.goodsEditor.diySql = 'pkey in(select goods from pur_prot_goods where supplier = '+supkey+')' ;
		  				}
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
	
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds,
				VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}
		@Override
		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(
					getOutVflds()).setSearchVFlds(getSearchVflds());
		}
		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}
		
		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
		}
		
		
	}
}
