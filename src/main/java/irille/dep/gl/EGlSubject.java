/**
 * 
 */
package irille.dep.gl;

import irille.gl.gl.GlSubject;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimpleTwo;
import irille.pub.html.EMCrtTrigger;
import irille.pub.html.EMFormTwoRow;
import irille.pub.html.EMList;
import irille.pub.html.ExtFile;
import irille.pub.tb.Tb;
import irille.pub.view.VFldOutKey;
import irille.pub.view.VFlds;

public class EGlSubject extends GlSubject {
	public static void main(String[] args) {
		new EGlSubject().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CODE, T.NAME) };
		MySimple ext = new MySimple(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();//把list中的本级编号设置为hidden
		vl.moveBefore(T.CODE, T.TEMPLAT);
		vl.get(T.NAME).setWidthList(170);
		vl.get(T.USE_SCOPE).setWidthList(150);
		vl.setExpandAndHidden("true", T.SUBJECT_KIND, T.CURRENCY, T.ACC_JOURNAL_TYPE, T.TALLY_FLAG, T.AUTO_CRT, T.REM);
		((EMList) ext.newList()).setLineActs(false);
		VFlds vs = ext.getVfldsForm();
		ext.getVfldsForm().get(T.CODE).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.SUBJECT_UP).setReadOnly("!this.insFlag");
		ext.getVfldsForm().get(T.TEMPLAT).setReadOnly("!this.insFlag");
		((VFldOutKey) vs.get(T.TEMPLAT)).setDiySql("type=1");
		vs.get(T.TOTAL_FLAG).setDefaultValue(0);
		vs.get(T.WRITEOFF_FLAG).setDefaultValue(0);
		vs.get(T.IN_FLAG).setDefaultValue(1);
		((EMList) ext.newList()).setExtendRow();
		
		ext.newExts().init();
		//		ext.getVfldsForm()[0].get(T.WRITEOFF_FLAG).setDefaultValue(0);
		
		EMCrt extTrig = new EMCrtTrigger(TB, vflds, T.NAME, new VFlds(T.CODE, T.NAME));
		extTrig.newExts().init().crtFiles();
		return ext;
	}
	
	public class MySimple extends EMCrtSimpleTwo {
		
		public MySimple(Tb tb, VFlds[] vflds, VFlds[] searchVflds) {
			super(tb, vflds, searchVflds);
		}
		
		@Override
		public ExtFile newForm() {
			if (_form == null)
				_form = new EMFormTwoRow(getTb(), getVfldsForm());
			_form.setWidthLabel(120);
			return _form;
		}
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
				window.open(base_path+'/gl_GlSubject_exportGrid?expMethod=list&expFields='
					+Ext.encode(fields) + furl);
			}
		);
	*** End onExport *********/		
	//@formatter:on
}
