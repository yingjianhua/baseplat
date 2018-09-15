/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlSubjectMap;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.html.EMList;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class EGlSubjectMap extends GlSubjectMap {
	public static void main(String[] args) {
		new EGlSubjectMap().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.TEMPLAT, T.NAME, T.ALIAS_SOURCE) };
		((VFldOutKey) searchVflds[0].get(T.TEMPLAT)).setDiySql("type=1");
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		VFlds vs = ext.getVfldsList();
		vs.setWidths(150, 100, 120, 100, 160, 150, 0);//由于一些字段可能显示不下，所以自定义了list中的字段宽度
		ext.getVfldsForm().setReadOnly("true", T.PKEY, T.TEMPLAT, T.ALIAS_SOURCE, T.ALIAS_TARGET);
		((EMList) ext.newList()).setLineActs(false);
		((EMForm) ext.newForm()).setWidthLabel(120);
		ext.newExts().init();
		return ext;
	}

//@formatter:off	
/** Begin onRefresh ********
		var me = this;
		Ext.MessageBox.confirm(msg_confirm_title, '您确认要刷新科目别名吗？',
			function(btn) {
				if (btn != 'yes')
					return;
				Ext.Ajax.request({
					url : base_path+'/gl_GlSubjectMap_refresh',
					success : function (response, options) {
						var result = Ext.decode(response.responseText);
						if (result.success){
							me.getStore().load();
							Ext.example.msg(msg_title, '科目别名刷新成功!');
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
	*** End onRefresh *********/
//@formatter:on		

	
	//@formatter:off
	/** Begin onExport ********
		var me = this;
		Ext.MessageBox.confirm(msg_confirm_title, "确认要导出数据吗?", 
			function(btn) {
				if (btn != 'yes')
					return;
				var array = mvc.Tools.searchValues(me.down('toolbar'));
				for (var i=0; i<array.length; i++)
					delete array[i].id;
				var fields = mvc.Tools.columnValues(me.columns);
				var furl = '';
				if (array.length != 0)
					furl = '&filter=' + Ext.encode(array);
				window.open(base_path+'/gl_GlSubjectMap_exportGrid?expMethod=list&expFields='
					+Ext.encode(fields) + furl);
			}
		);
	*** End onExport *********/		
	//@formatter:on
}
