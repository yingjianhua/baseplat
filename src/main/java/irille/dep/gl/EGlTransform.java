package irille.dep.gl;

import irille.gl.gl.GlNoteView;
import irille.gl.gl.GlTransform;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtCompSimple;
import irille.pub.html.EMModel;
import irille.pub.html.EMStore;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtFile;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;

public class EGlTransform extends GlTransform {
	public static void main(String[] args) {
		new EGlTransform().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		GlNoteView.TB.getCode();
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] mflds = new VFlds[] { new VFlds(T.CODE, T.STATUS, T.TYPE, T.ORG, T.DEPT,T.CELL, T.CREATED_BY, T.CREATED_TIME,
		    T.REM) }; // 主表信息字段
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.STATUS) }; // 搜索栏字段
		//GlNoteView这里只是产生子表的TAB， 子表的GRID前台完成重新写过
		EMCrt ext = new MyComp(TB, vflds, mflds, searchVflds, new VFlds[] { new VFlds(GlNoteView.T.BILL) });

		VFlds vs = ext.getVfldsForm();
		vs.del(T.STATUS,T.DEPT,T.CREATED_BY, T.CREATED_TIME, T.APPR_BY, T.APPR_TIME, T.TALLY_BY, T.TALLY_TIME, T.TYPE);// FROM页面删除建档员等字段
		vs.setReadOnly("true", T.CODE, T.ORG);
		vs.get(T.CELL).setDefaultValue(EMCrt.LOGIN_CELL);
		vs.setNull(true, T.CODE, T.ORG);
		ext.newExts().init();
		return ext;
	}

	class MyZipList extends EMZipList<MyZipList> {

		public MyZipList(Tb tb, VFlds... vflds) {
			super(tb, vflds);
		}

		public void initFormListMain() {
			ExtList form = getFormListMain();
			ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
			    .AddFunParaList();
			l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
			    .addExp("roles", "this.roles");
			l.addExp("listeners", loadFunCode(EGlTransform.class, "initChange", getOutVFlds().get(0).getCode()));
		}

		public void initFuns() {
			AddFun("onChangeStatus", EGlTransform.class).addFunParasExp("status");
			super.initFuns();
		}
		//@formatter:off	
		
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
		*** End onChangeStatus *********/

		/** Begin initChange ********
		{
				scope : this,
        selectionchange: function(model, records) {
            if (records.length === 1){
                this.mdMain.getForm().loadRecord(records[0]);
								
								this.mdLineTable.store.proxy.url = base_path + '/gl_GlNote_listByTb?tableCode=irille.gl.gl.GlTransform'
									+ '&bean.pkey=' + records[0].get('bean.pkey');
								this.mdLineTable.store.load();
								
								var status = records[0].get('bean.status'); //根据单据状态判断
								this.onChangeStatus(status);
            }else{
            	this.mdMain.getForm().reset();
            	this.mdLineTable.store.removeAll();
            	this.onChangeStatus(-1);
            }
        }
    }
*** End initChange *********/
		
		//@formatter:on	

	}

	class MyComp extends EMCrtCompSimple<MyComp> {

		public MyComp(Tb tb, VFlds[] vflds, VFlds[] mainvflds, VFlds[] searchVflds, VFlds[] outVflds) {
			super(tb, vflds, mainvflds, searchVflds, outVflds);
		}

		public ExtFile newList() {
			return new MyZipList(getTb(), getMainFlds()).setOutVFlds(getOutVflds()).setSearchVFlds(getSearchVflds());
		}

		public void crtOutFld(VFld fld) {
			addExt(new EMModel((Tb) fld.getFld().getTb(), getNowOutKeyModelVflds()));
			addExt(new EMStore((Tb) fld.getFld().getTb()));
		}

	}

}
