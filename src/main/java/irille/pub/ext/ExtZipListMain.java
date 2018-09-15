package irille.pub.ext;

import irille.gl.gl.GlJournal;
import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT复合表格-主表类
 */
public class ExtZipListMain {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ZipListMain.txt");
	}

	public static String getContent(Tb tb) {
		boolean isUpd = tb.checkAct("upd") != null;
		boolean isIns = tb.checkAct("ins") != null;
		boolean isDel = tb.checkAct("del") != null;
		boolean isEdit = tb.checkAct("edit") != null;
		//==========明细行功能代码
		String R_LINE_ACTS = "";
		if (isUpd || isDel)
			R_LINE_ACTS += "var lineActs = [];" + Ext.LNT2;
		if (isUpd)
			R_LINE_ACTS += "" +
					"if (this.roles.indexOf('upd') != -1)" + Ext.LNT3 +
						"lineActs.push({" + Ext.LNT4 +
							"iconCls : 'upd-icon-table'," + Ext.LNT4 +
							"tooltip : '修改'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onUpdRow" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (isDel)
			R_LINE_ACTS += "" +
					"if (this.roles.indexOf('del') != -1)" + Ext.LNT3 +
						"lineActs.push({" + Ext.LNT4 +
							"iconCls : 'del-icon-table'," + Ext.LNT4 +
							"tooltip : '删除'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onDelRow" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (Str.isEmpty(R_LINE_ACTS) == false)
			R_LINE_ACTS = R_LINE_ACTS.substring(0,R_LINE_ACTS.length()-4);
		//==========单元格定义
		String R_COLUMNS = "";
		for (Fld fld : tb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey") && tb.isAutoIncrement())
				continue;
			if (Str.isEmpty(R_COLUMNS) == false)
				R_COLUMNS += "\t\t\t";
			R_COLUMNS += fld.crtVFld().extList() + "," + Ext.LN;
		}
		if (isUpd || isDel)
			R_COLUMNS += "\t\t\t" +
					"{" + Ext.LNT4 +
						"xtype : 'actioncolumn'," + Ext.LNT4 +
						"width : 80," + Ext.LNT4 +
						"sortable : false," + Ext.LNT4 +
						"menuDisabled : true," + Ext.LNT4 +
						"header : '操作'," + Ext.LNT4 +
						"items : lineActs" + Ext.LNT3 +
					"}";
		else
			R_COLUMNS = R_COLUMNS.substring(0,R_COLUMNS.length()-3);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【明细行功能定义】", R_LINE_ACTS);
		content = content.replace("【单元格定义】", R_COLUMNS);
		content = Ext.cutOrDel(content, "【修改-头】", "【修改-尾】", isUpd);
		content = Ext.cutOrDel(content, "【删除-头】", "【删除-尾】", isDel);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/ListMain.js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}
	
	public static void main(String[] args) {
		System.out.println(getContent(GlJournal.TB));
	}

}
