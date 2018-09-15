package irille.dep.gs;

import irille.core.sys.SysShiping;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsInLineView;
import irille.pss.sal.SalSaleLine;
import irille.pub.bean.CmbGoods;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipListForm;
import irille.pub.html.EMZipListLine;
import irille.pub.html.EMZipListMain;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.html.Exts.ExtAct;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGsIn extends GsIn {
	public static void main(String[] args) {
		new EGsIn().crtExt().crtFiles();
	}

	public MyComp crtExt() {
		GsInLineView.TB.getCode();
		CmbGoods.TB.getCode();
		VFlds ship = new VFlds("ship").addWithout(SysShiping.TB, SysShiping.T.PKEY, SysShiping.T.REM,
			    SysShiping.T.SHIPING_FORM, SysShiping.T.TIME_SHIP, SysShiping.T.TIME_ARR, SysShiping.T.ROW_VERSION);
		ship.get(SysShiping.T.NAME).setName("收货人名称");
		ship.get(SysShiping.T.ADDR).setName("收货地址");
		ship.get(SysShiping.T.MOBILE).setName("收货人手机");
		ship.get(SysShiping.T.TEL).setName("收货人电话");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.SHIPING), ship };
		VFlds[] mflds = { new VFlds(T.CODE, T.ORIG_FORM_NUM, T.GS_NAME, T.WAREHOUSE, T.STATUS, T.IN_TIME, T.CREATED_BY, T.CREATED_TIME, T.ORG) }; // 主表信息字段
		VFlds[] searchVflds = { new VFlds(T.ORIG_FORM_NUM, T.WAREHOUSE, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(GsInLineView.T.PKEY) });

		//list字段调整
		VFlds vl = ext.getVfldsList();
		vl.moveLast(T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.REM);
		vl.setExpandAndHidden("true", SysShiping.T.ADDR, SysShiping.T.MOBILE, SysShiping.T.TEL, SysShiping.T.TIME_ARR_PLAN,
			    SysShiping.T.TIME_SHIP_PLAN, T.REM, T.APPR_BY, T.APPR_TIME, T.CREATED_BY, T.CREATED_TIME, T.DEPT,
			    T.OPERATOR, T.CHECKER);
		((EMZipListMain) ext.newZipListMain()).setExtendRow();
		
//		//form字段调整
//		VFlds vs = ext.getVfldsForm();
//		vs.setHidden("true", SysShiping.T.TIME_ARR_PLAN,SysShiping.T.TIME_SHIP_PLAN,
//				SysShiping.T.NAME,SysShiping.T.ADDR,SysShiping.T.MOBILE,SysShiping.T.TEL);

		ext.newExts().init();
		return ext;
	}
	
	class MyZipList extends EMZipList<EMZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		//在list.js改写doAppr方法体，增加onApprWin方法
		@Override
		public void initFunsAddAct(ExtAct act) {
			if (!act.getCode().equals("doAppr"))
				super.initFunsAddAct(act);
			else {
				add(AddFun("onDoAppr", EGsIn.class));
				add(AddFun("onApproveRecord", EGsIn.class).addFunParasExp("form, data"));
				add(AddFun("getGoodsKeys", EGsIn.class).addFunParasExp("pkey"));
				add(AddFun("onApprWin", EGsIn.class).addFunParasExp("selection"));
			}
		}
		
		@Override
		public void initFuns() {
			AddFun("onChangeStatus", EGsIn.class).addFunParasExp(
					"status");
			super.initFuns();
		}
		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EGsIn.class, "initFormListMainStatus", getSearchVFlds().get(0).getCode()));
		}
		//@formatter:off	
		/** Begin onChangeStatus ********
		if (this.roles.indexOf('doAppr') != -1)
		this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
		if (this.roles.indexOf('print') != -1)
		this.down('#'+this.oldId+'print').setDisabled(status != STATUS_INIT && status != STATUS_CHECKED);
		if (this.roles.indexOf('unAppr') != -1)
		this.down('#'+this.oldId+'unAppr').setDisabled(status != STATUS_CHECKED );
				*** End onChangeStatus *********/
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
	
	class MyModel extends EMModel<EMModel> {

		public MyModel(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}
		
	}
	
	/**
	 * 重构复合界面产生器 将各种对象更改为上面的自定义对象类
	 * 
	 * @author whx
	 */
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds,
				VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds())
					.setSearchVFlds(getSearchVflds());
		}
		
		@Override
		public void crtOutFld(VFld fld) {
			EMZipListLine el= new EMZipListLine(getTb(), fld, getNowOutKeyLineVflds());
			if (fld.getTb().getClazz().equals(GsInLineView.class))
				el.getVFlds().del(GsInLineView.T.BATCH_CODE);
			addExt(el);
			addExt(new MyModel((Tb) fld.getFld().getTb(), getNowOutKeyModelVflds()));
			addExt(new EMStore((Tb) fld.getFld().getTb()));
			if(isCrtWinAndForm()){
				addExt(newZipWin(fld));
				addExt(new EMZipListForm(getTb(),fld,getNowOutKeyZipListFormVflds()));
			}
		}
		
	}
	//@formatter:off	
	/** Begin onDoAppr ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
		this.onApprWin(selection);
*** End onDoAppr *********/
	/** Begin getGoodsKeys ********
	var keys = '';
	Ext.Ajax.request({
		async : false,
		url : base_path+'/gs_GsInLineView_getGoodsKeys?pkey=' + pkey,
		method : 'GET',
		success : function (response, options) {
			keys = response.responseText;
		}
	});
	return keys;
*** End getGoodsKeys *********/
	
	/** Begin onApprWin ********
	if (selection){
			mvc.model.gs.GsIn.load(selection.get('bean.pkey'), {
				scope : this,
				failure : function(response, operation) {
					Ext.example.msg(msg_title,msg_ajax);
				},
				success : function(response, operation) {
					Ext.apply(selection.data,response.data);
					var win = Ext.create('mvc.view.gs.GsIn.ApprWin',{
						title : this.title+'>审核',
						insFlag : false,
						goodsKeys : this.getGoodsKeys(selection.get('bean.pkey'))
					});
					win.on('create',this.onApproveRecord,this);
					win.show();
					win.setActiveRecord(selection);
				}
			});
		}
*** End onApprWin *********/
	/** Begin onApproveRecord ********
	var selection = this.mdMainTable.getView().getSelectionModel().getSelection()[0];
	var bean = Ext.create('mvc.model.gs.GsIn', data);
	Ext.apply(selection.data,bean.data);
	selection.commit();
	this.mdMainTable.getSelectionModel().deselectAll();
	this.mdMainTable.getView().select(selection);
	 *** End onApproveRecord *********/
	//@formatter:on	
}
