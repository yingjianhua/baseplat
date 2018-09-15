/**
 * 
 */
package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.dep.sal.ESalOrder;
import irille.pss.pur.PurPresent;
import irille.pss.pur.PurPresentLine;
import irille.pub.bean.CmbGoods;
import irille.pub.bean.CmbPurLine;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListLine;
import irille.pub.html.EMZipListMain;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EPurPresent extends PurPresent {
	public static void main(String[] args) {
		new EPurPresent().crtExt();
	}

	public void crtExt() {
		PurPresentLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		CmbGoods.TB.getCode();
		CmbPurLine.TB.getCode();
		PurPresentLine.T L = PurPresentLine.T.PKEY;
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
		    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING), ship };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME, T.STATUS, T.WAREHOUSE, T.AMT, T.CREATED_BY,
		    T.CREATED_TIME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME, T.STATUS) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(L) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.ORG, T.DEPT, T.REM);
		vl.setExpandAndHidden("true", T.AMT_COST, T.DEPT, T.CELL, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME,
		    T.CREATED_BY, T.CREATED_TIME, T.REM);
		vl.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE, SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
		    SysShiping.T.TIME_SHIP_PLAN);
		((EMZipListMain) ext.newZipListMain()).setExtendRow();

		
		VFlds vs = ext.getVfldsForm();
		vs.del(T.AMT_COST, T.ORG, T.DEPT, T.CELL, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
		    T.TALLY_TIME);// FROM页面删除建档员等字段
		vs.moveAfter(T.AMT, T.STATUS);
		vs.moveLast(T.REM);
		vs.setReadOnly("true", T.CODE, T.STATUS, T.AMT, T.SUPNAME);// 只读设置
		vs.setNull(true, T.CODE, T.STATUS, T.AMT, T.SUPNAME);// 为空设置
		vs.setHidden("true", SysShiping.T.TIME_ARR_PLAN, SysShiping.T.TIME_SHIP_PLAN, SysShiping.T.NAME, SysShiping.T.ADDR,
		    SysShiping.T.MOBILE, SysShiping.T.TEL);
		ext.newExts().init();
		ext.crtFiles();
	}

	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}
		
		public void crtOutFld(VFld fld) {
			VFlds vflds = getNowOutKeyLineVflds();
			vflds.setHidden("true", PurPresentLine.T.PRICE, PurPresentLine.T.AMT, PurPresentLine.T.COST_PUR);
			addExt(new EMZipListLine(getTb(), fld, getNowOutKeyLineVflds()));
			addExt(new EMModel((Tb) fld.getFld().getTb(), getNowOutKeyModelVflds()));
			addExt(new EMStore((Tb) fld.getFld().getTb()));
			if(isCrtWinAndForm()){
				addExt(newZipWin(fld));
				addExt(newZipListForm(getTb(),fld,getNowOutKeyZipListFormVflds()));
			}
		}
		

		@Override
		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		@Override
		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}

		@Override
		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}

		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
		}

		@Override
		public VFlds getNowOutKeyZipListFormVflds() {
			VFlds vs = super.getNowOutKeyZipListFormVflds();
			vs.del(PurPresentLine.T.AMT, PurPresentLine.T.PRICE, PurPresentLine.T.COST_PUR);
			return vs;
		}

	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EPurPresent.class).addFunParasExp("status");
			super.initFuns();
			//@formatter:off	
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

		//		@Override
		//		public void initFunsAddActs() {
		//			for (ExtAct act : getActs().getActs()) {
		//				IEnumOpt oact = act.getAct().getAct();
		//				if (oact == OAct.INS) {
		//					add(AddFun("onSaveRecord", EMZipList.class).addFunParasExp("form, data"));
		//					continue;
		//				}
		//				initFunsAddAct(act);
		//			}
		//		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EPurPresent.class, "initFormListMainStatus", getOutVFlds().get(0).getCode()));
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
			//@formatter:on
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
			if (fld.getCode().equals(PurPresent.T.SHIPING_MODE.getFld().getCode())) {
				fldList.add(new ESalOrder.MyShip()).setCloseStr(null);
			}else if (fld.getCode().equals(PurPresent.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new EPurPresent.MySupplier());
			} else {
				super.setFldAttr(fld, fldList);
			}
		}

	}
	
	public static class MySupplier implements IExtOut {

		public String toString(int tabs) {
			return null;
		}

		public void out(int tabs, StringBuilder buf) {
			buf.append(new EMModel(PurPresent.TB).loadFunCode(EPurPresent.class, "mySupplier"));
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

}
