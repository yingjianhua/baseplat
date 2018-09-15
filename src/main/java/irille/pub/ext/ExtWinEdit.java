package irille.pub.ext;

import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.Tb;

/**
 * EXT编辑-窗口类
 */
public class ExtWinEdit {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("WinEdit.txt");
	}

	public static String getContent(Tb tb, Fld[] mflds, FldOutKey[] outflds) {
		String fields = "";
		for (Fld fld : mflds)
			fields += fld.crtVFld().extEdit(Ext.LNT3) + ",";
		fields = fields.substring(0, fields.length() - 1);
		String tabs = "";
		int i = 1;
		for (FldOutKey out : outflds) {
			Tb lineTb = (Tb)out.getTb();
//			Tb lineTb = Tb.getTBByBean(out.getOutTbClass());
			String linepag = Ext.getPag(lineTb);
			String linecname = Ext.getClazz(lineTb);
			tabs += "" +
			/**/"{" + Ext.LNT4 +
			/**/"	xtype : Ext.create('mvc.view." + linepag + "." + linecname + ".ListEdit',{" + Ext.LNT4 +
			/**/"	border : false," + Ext.LNT4 +
			/**/"	title : '" + lineTb.getName() + "'," + Ext.LNT4 +
			/**/"	itemId : this.oldId+'" + i + "'," + Ext.LNT4 +
			/**/"	mainPkey : mainPkey," + Ext.LNT4 +
			/**/"	iconCls : 'tab-user-icon'," + Ext.LNT4 +
			/**/"	isEdit : this.isEdit" + Ext.LNT4 +
			/**/"	})" + Ext.LNT4 +
			/**/"},";
			i++;
		}
		tabs = tabs.substring(0, tabs.length() - 1);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【字段集】", fields);
		content = content.replace("【从表集】", tabs);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/WinEdit.js";
	}

	public static void crtJs(Tb tb, boolean del, Fld[] mflds, FldOutKey[] outflds) {
		Ext.crtJs(getJsPath(tb), getContent(tb, mflds, outflds), del);
	}

	public static void main(String[] args) {
//		System.err.println(getContent(SysB3.TB, new Fld[] { SysA.FLD_CODE, SysA.FLD_DATE1 }, new Class[] { SysB2.class,
//		    SysB.class }));
	}

}
