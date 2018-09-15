/**
 * 
 */
package irille.dep.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysCell.T;
import irille.gl.gl.GlCarryOver;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class EGlCarryOver extends GlCarryOver {
	public static void main(String[] args) {
		new EGlCarryOver().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.TEMPLAT) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		((VFldOutKey) ext.getVfldsForm().get(T.TEMPLAT)).setDiySql("type="+Sys.OTemplateType.GL.getLine().getKey());
		ext.getVfldsList().get(T.TEMPLAT).setWidthList(120);
		ext.getVfldsList().get(T.SUBJECT_SOURCE).setWidthList(200);
		ext.getVfldsList().get(T.SUBJECT_TARGET).setWidthList(200);
		ext.newExts().init();
		return ext;
	}

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
				window.open(base_path+'/gl_GlCarryOver_exportGrid?expMethod=list&expFields='
					+Ext.encode(fields) + furl);
			}
		);
	*** End onExport *********/		
	//@formatter:on
}
