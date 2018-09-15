package irille.dep.gs;

import irille.gl.gs.GsLoss;
import irille.gl.gs.GsLossLine;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtComp;
import irille.pub.html.EMZipList;
import irille.pub.html.EMZipWin;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class EGsLoss extends GsLoss {
	
	public static void main(String[] args) {
		new EGsLoss().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GsLossLine.TB.getCode(); // 解决静态块初始化异常的问题。。。
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.ORIG_FORM_NUM, T.WAREHOUSE, T.STATUS,
				T.CREATED_BY, T.CREATED_TIME) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.WAREHOUSE, T.STATUS) }; // 搜索栏字段
		// 在上面改对MODEL、LIST、FROM是同时影响，下面则是各改各的-互不影响
		MyComp ext = new MyComp(TB, vflds, mflds, searchVflds,
				new VFlds[] { new VFlds(GsLossLine.T.PKEY) });
		VFlds vs = ext.getVfldsForm();
		vs.del(T.CODE, T.ORG, T.DEPT, T.STATUS, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.ORIG_FORM, T.ORIG_FORM_NUM);// FROM页面删除建档员等字段
		vs.moveLast(T.REM);
		VFlds vsl = ext.getVfldsList();
		vsl.moveLast(T.STATUS, T.ORG, T.DEPT, T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME,T.ORIG_FORM,T.ORIG_FORM_NUM, T.REM);
		ext.newExts();
		((EMZipWin)ext.newZipWin(null)).setWidth(650); // 需要在newExt与init之间调用
		ext.init();
		return ext;
	}
	
	class MyZipList extends EMZipList<EMZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
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
	if (this.roles.indexOf('upd') != -1)
		this.down('#'+this.oldId+'upd').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('print') != -1)
		this.down('#'+this.oldId+'print').setDisabled(status == -1);
	if (this.roles.indexOf('del') != -1)
		this.down('#'+this.oldId+'del').setDisabled(status != STATUS_INIT);
	if (this.roles.indexOf('doAppr') != -1)
		this.down('#'+this.oldId+'doAppr').setDisabled(status != STATUS_INIT);
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
	
	class MyComp extends EMCrtComp<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds,
				VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds())
					.setSearchVFlds(getSearchVflds());
		}
		
	}
}
