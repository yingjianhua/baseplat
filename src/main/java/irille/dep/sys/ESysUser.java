/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysUser;
import irille.gl.gs.GsLocation.T;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMCrtTrigger;
import irille.pub.tb.FldStr;
import irille.pub.tb.IEnumFld;
import irille.pub.view.VFlds;

public class ESysUser extends SysUser {
	IEnumFld _roles = SYS.STR__100;

	public static void main(String[] args) {
		new ESysUser().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		_roles.getFld().setName("角色").setCode("roles");
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.PHOTO) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.LOGIN_NAME, T.NAME, T.ORG) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsForm().del(T.NAME, T.NICKNAME, T.TB_OBJ);
		VFlds myv = ext.getVfldsForm();
		myv.add(new FldStr("New", "密码", 40, false).crtVFld(), "mm");
		myv.add(new FldStr("Check", "确认密码", 40, false).crtVFld(), "mm");
		ext.setVfldsForm(myv);
		ext.getVfldsForm().get("New").setHidden("!this.insFlag").attrs().addExp("disabled", "!this.insFlag")
		    .addExp("disabledCls", "''");
		ext.getVfldsForm().get("Check").setHidden("!this.insFlag").attrs().addExp("disabled", "!this.insFlag")
		    .addExp("disabledCls", "''");
		ext.getVfldsForm().get(T.ORG).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.DEPT).setReadOnly("!this.insFlag");
		ext.getVfldsList().add(_roles);
		ext.getVfldsList().get(_roles).setWidthList(250);
		ext.getVfldsModel().add(_roles);
		ext.newExts().init();

		//选择器
		EMCrtTrigger trigger = new EMCrtTrigger(TB, vflds, T.NAME, new VFlds(T.NAME, T.LOGIN_NAME));
		trigger.getVfldsList().del(T.LOGIN_STATE);
		trigger.newExts().init().crtFiles();

		return ext;
	}
	//@formatter:off	
		/** Begin onPwd ********
	var selection = this.getView().getSelectionModel().getSelection()[0];
	var win = Ext.create('mvc.view.sys.SysUser.WinPwd',{
		title : this.title+'>密码修改'
	});
	win.setActiveRecord(selection);
	win.show();
	 *** End onPwd *********/
	/** Begin onRole ********
		var selection = this.getView().getSelectionModel().getSelection()[0];
		if (selection){
			var win = Ext.create('mvc.view.sys.SysUser.WinEdit',{
				title : '用户 ['+selection.get('bean.name')+']>角色配置',
				record : selection
			});
			win.show();
			win.loadData();
		}	
	*** End onRole *********/
		//@formatter:on	
//@formatter:off	
		/** Begin onDel ********
		var selection = this.getView().getSelectionModel().getSelection();
		if (selection){
			var me = this;
			Ext.MessageBox.confirm(msg_confirm_title, '您确认要停用所选的记录吗?', 
				function(btn) {
					if (btn != 'yes')
						return;
					var arr=new Array();
					var arrv = new Array();
					for(var i = 0; i < selection.length; i++){
						arr.push(selection[i].get('bean.pkey'));
						arrv.push(selection[i].get(BEAN_VERSION));
					}
					Ext.Ajax.request({
						url : base_path+'/sys_SysUser_delMulti?pkeys='+arr.toString()+'&rowVersions='+arrv.toString(),
						success : function (response, options) {
							var result = Ext.decode(response.responseText);
							if (result.success){
								me.getStore().reload();
								me.getView().deselect(me.getView().getSelectionModel().getSelection());
								Ext.example.msg(msg_title, msg_del);
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
	*** End onDel *********/
		//@formatter:on	
}
