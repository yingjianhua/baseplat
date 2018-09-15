/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteView;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMZipList;
import irille.pub.html.ExtList;
import irille.pub.tb.Tb;
import irille.pub.view.VFlds;

public class EGlNote extends GlNote {
	public static void main(String[] args) {
		//new EGlNote().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		EMCrtModelAndStore.crtModelAndStore(GlNoteView.TB); //产生记账条视图-前台MODEL与STORE
		
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.BILL, T.JOURNAL, T.STATUS) };
		EMCrtSimple ext = new MyComp(TB,vflds,searchVflds);
		//EMCrt ext = new EMCrtSimpleTwo(TB, vflds, searchVflds);
		//((EMZipList) ext.newList()).setLineActs(false);
		ext.newExts().init();
		return ext;
	}

}
class MyComp extends EMCrtSimple {
	
	public MyComp(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
		super(tb,vflds,searchVflds);
	}
/*	@Override
	public ExtFile newList() {
		return new MyList(getTb());
	}*/
}
class MyList extends EMZipList {
	public MyList(Tb tb, VFlds... vflds) {
	 super(tb, vflds);
  }
	@Override
	public void initFormListMain() {
		ExtList form = getFormListMain();
		ExtList l = form.AddFunCall("xtype : Ext.create", "mvc.view." + getPack() + "." + getClazz() + ".ListMain")
		    .AddFunParaList();
		l.add("title", getTb().getName()).addExp("itemId", "this.oldId+'maintable'").add(ICON_CLS, "tab-user-icon")
		    .addExp("roles", "this.roles");
		l.addExp("listeners", loadFunCode(EGlNote.class, "initFormListMainStatus"));
		//@formatter:off	
		
		/** Begin initFormListMainStatus ********
												{
												scope : this,
				                selectionchange: function(model, records) {
				                    if (records.length === 1){
				                        this.mdMain.getForm().loadRecord(records[0]);
        								var status = records[0].get('bean.status'); //根据单据状态判断
        								//初始状态
        								if (status==STATUS_INIT){
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(false);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(true);
        								}else if (status==STATUS_APPROVE){
											if (this.roles.indexOf('doAppr') != -1)
												this.down('#'+this.oldId+'doAppr').setDisabled(true);
											if (this.roles.indexOf('unAppr') != -1)
												this.down('#'+this.oldId+'unAppr').setDisabled(false);
        								}
				                    }else{
				                    	this.mdMain.getForm().reset();
										if (this.roles.indexOf('doAppr') != -1)
											this.down('#'+this.oldId+'doAppr').setDisabled(true);
										if (this.roles.indexOf('unAppr') != -1)
											this.down('#'+this.oldId+'unAppr').setDisabled(true);
				                    }
				                }
				            }
		 *** End initFormListMainStatus *********/
		
					//@formatter:on
	}
}
//@formatter:off
/** Begin onDoAppr ********
var selection = this.getView().getSelectionModel().getSelection()[0];
var me = this;
if (selection){
	Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.pkey') + '] - 审核确认吗？',
		function(btn) {
			if (btn != 'yes')
				return;
			Ext.Ajax.request({
				url : base_path+'/【0】_【1】_approve?pkey='+selection.get('bean.pkey'),
				success : function (response, options) {
					var result = Ext.decode(response.responseText);
					if (result.success){
						selection.set(BEAN_STATUS, result.status);
						selection.commit();
						me.getSelectionModel().deselectAll();
						me.getView().select(selection);
						Ext.example.msg(msg_title, '审核--成功');
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
*** End onDoAppr *********/

/** Begin onUnAppr ********
var selection = this.getView().getSelectionModel().getSelection()[0];
var me = this;
if (selection){
	Ext.MessageBox.confirm(msg_confirm_title, '【2】['+selection.get('bean.pkey') + '] - 弃审确认吗？',
		function(btn) {
			if (btn != 'yes')
				return;
			Ext.Ajax.request({
				url : base_path+'/【0】_【1】_unapprove?pkey='+selection.get('bean.pkey'),
				success : function (response, options) {
					var result = Ext.decode(response.responseText);
					if (result.success){
						selection.set(BEAN_STATUS, result.status);
						selection.commit();
						me.getSelectionModel().deselectAll();
						me.getView().select(selection);
						Ext.example.msg(msg_title, '弃审--成功');
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
*** End onUnAppr *********/

//@formatter:on