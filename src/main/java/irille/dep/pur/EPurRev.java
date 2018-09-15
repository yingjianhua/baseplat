/**
 * 
 */
package irille.dep.pur;

import irille.core.sys.SysShiping;
import irille.dep.pur.EPurOrder.MyShip;
import irille.dep.pur.EPurOrder.MySupplier;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRevLine;
import irille.pss.pur.PurOrder.T;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMFormTwoRow;
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
public class EPurRev extends PurRev {
	public static void main(String[] args) {
		new EPurRev().crtExt().crtFiles();
	}
	public EMCrt crtExt() {
		CmbGoods.TB.getCode();
		PurRevLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB,
				SysShiping.T.PKEY, SysShiping.T.REM, SysShiping.T.SHIPING_FORM,
				SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR,
				SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("发货人名称");
		ship.get(SysShiping.T.ADDR).setName("发货地址");
		ship.get(SysShiping.T.MOBILE).setName("发货人手机");
		ship.get(SysShiping.T.TEL).setName("发货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING),
				ship };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.SUPPLIER, T.SUPNAME,
				T.ORD,T.WAREHOUSE,T.AMT) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.SUPNAME,
				T.STATUS) }; // 搜索栏字段
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(PurRevLine.T.PKEY) });
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.ORG, T.REM);
		vl.get(T.SUPNAME).setWidthList(150);
		vl.setExpandAndHidden("true", T.AMT_COST, T.DEPT, T.CELL,
				T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.CREATED_BY,
				T.CREATED_TIME, T.REM);
		vl.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE,
				SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN);
		((EMZipListMain)ext.newZipListMain()).setExtendRow();
		
		VFlds vs = ext.getVfldsForm();
		vs.moveAfter(T.REM, T.BILL_FLAG);
		vs.setReadOnly("true",T.ORD, T.CELL,T.SUPPLIER, T.SUPNAME, T.WAREHOUSE, T.AMT,
				T.AMT_COST);
		vs.setNull(true, T.SUPNAME, T.AMT, T.AMT_COST);
		vs.del(T.CODE, T.STATUS, T.ORG, T.CELL, T.DEPT, T.CREATED_BY,
				T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY,
				T.TALLY_TIME,T.AMT_COST);
		ext.getVfldsForm().setHidden("true", SysShiping.T.TIME_ARR_PLAN,
				SysShiping.T.TIME_SHIP_PLAN, SysShiping.T.NAME,
				SysShiping.T.ADDR, SysShiping.T.MOBILE, SysShiping.T.TEL);
		ext.newExts().init();

		return ext;
	}
	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		public void initFuns() {
			AddFun("onInsRev", EPurRev.class).addFunParasExp(
					"data,params");
			AddFun("onChangeStatus", EPurOrder.class).addFunParasExp(
					"status,ordStatus");
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
					loadFunCode(EPurRev.class, "initFormListMainStatus",
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
    								var ordStatus = records[0].get('bean.ordStatus'); //根据单据状态判断
    								this.onChangeStatus(status, ordStatus);
                }else{
                	this.mdMain.getForm().reset();
                	this.mdLineTable.store.removeAll();
                	this.onChangeStatus(-1, -1);
                }
            }
                }
			 *** End initFormListMainStatus *********/
			/** Begin onIns ********
				var orderList = Ext.create('mvc.view.pur.PurOrder.RevTrigger',{
					title : '收货单>新增【选择器-采购订单】'
				});
				orderList.on('trigger', this.onInsRev, this);
				orderList.show();
			 *** End onIns *********/
			/** Begin onInsRev ********
				var win = Ext.create('mvc.view.pur.PurRev.Win',{
					title : '收货单>新增'
				});
				win.on('create',this.onSaveRecord,this);
				win.show();
				win.setActiveRecordIns(data.split('##')[0]);
			 *** End onInsRev *********/
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
				this.down('#'+this.oldId+'print').setDisabled(false);
					*** End onChangeStatus *********/
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
			add("maxHeight", 520);
			add(RESIZABLE, false);
			add("modal", true);
			add(ICON_CLS, "app-icon");
			add("pkeyFlag", true);
			add(INS_FLAG, true);
		}
		@Override
		public void initFuns() {
			AddFun("setActiveRecordIns", EPurOrder.class, TB.getCode())
					.addFunParasExp("order");
			super.initFuns();
		}
		//@formatter:off	
		/** Begin setActiveRecordIns ********
			Ext.Ajax.request({
						async : false,
						scope : this,
						url : base_path + '/pur_PurRev_init',
						params:{orderId:order},
						method : 'GET',
						success : function(response) {
							var rtn = Ext.JSON.decode(response.responseText, true);
							var rev  = Ext.create('mvc.model.pur.PurRev');
							Ext.apply(rev.data,rtn.rev);
							this.form.getForm().loadRecord(rev);
							this.lineTable.store.loadData(rtn.revLine);
						},
						failure : function(response) {
							Ext.example.msg(msg_title, msg_ajax);
						}
				});
		*** End setActiveRecordIns *********/
		/** Begin onSave ********
		var form = this.form.getForm();
		console.log('a',1);
		if (form.isValid()) {
			console.log('a',2);
			form.submit({
				url : this.form.url,
				submitEmptyText: false,
				type : 'ajax',
				params : mvc.Tools.storeValues(this.lineTable.store,{insFlag : this.insFlag}),
				success : function(form, action) {
					this.fireEvent('create', this, action.result);
					this.onClose();
				},
				failure : mvc.Tools.formFailure(),
				waitTitle : wait_title,
				waitMsg : wait_msg,
				scope : this
			});
		}
		*** End onSave *********/
		//@formatter:on		
	}
	class MyForm extends EMFormTwoRow<MyForm> {

		public MyForm(Tb tb, VFlds... vFlds) {
			super(tb, vFlds);
		}
		public void setLayoutProperies(ExtList v) {
			v.add(TYPE, "table").add(COLUMNS, 3)
					.add(ITEM_CLS, "x-layout-table-items-form");
		}

		public void setFieldDefaultsProperies(ExtList v) {
			v.add(LABEL_WIDTH, 110).add(WIDTH, 275)
					.add(LABEL_STYLE, "font-weight : bold");
		}

		public void setFldAttr(VFld fld, ExtList fldList) {
			if (fld.getCode().equals(PurRev.T.SUPPLIER.getFld().getCode())) {
				fldList.add(new MySupplier());
			} else if (fld.getCode().equals(
					PurRev.T.SHIPING_MODE.getFld().getCode())) {
				fldList.add(new MyShip()).setCloseStr(null);
			} else {
				super.setFldAttr(fld, fldList);
			}
		}
	}
	
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds,
				VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(
					getOutVflds()).setSearchVFlds(getSearchVflds());
		}
		public ExtFile newZipWin(VFld fld) {
			return new MyWin(getTb(), fld);
		}
		public ExtFile newForm() {
			return new MyForm(getTb(), getVfldsForm());
		}
		@Override
		public ExtFile newZipListForm(Tb tb, VFld outfld, VFlds... vflds) {
			return null;
		}
		/*@Override
		public ExtFile newZipListMain() {
			return null;
		}*/

		
	}
}
