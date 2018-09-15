package irille.pub.ext;

import irille.core.sys.SysOrg;
import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT基础表格类--请不要格式化代码！！
 * TODO 还少个表格扩展字段待处理
 */
public class ExtList {

	public static String TEMPLATE = null;
	
	static {
		TEMPLATE = Ext.loadTemplate("List.txt");
	}

	public static String getContent(Tb tb, Fld... searchFlds) {
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
		//==========主界面功能代码
		String R_MAIN_ACTS = "var mainActs = [];" + Ext.LNT2;
		if (isIns)
			R_MAIN_ACTS += "" +
					"if (this.roles.indexOf('ins') != -1)" + Ext.LNT3 +
						"mainActs.push({" + Ext.LNT4 +
							"text : '新增'," + Ext.LNT4 +
							"iconCls : 'ins-icon'," + Ext.LNT4 +
							"itemId : this.oldId+'ins'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onIns" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (isEdit)
			R_MAIN_ACTS += "" +
					"if (this.roles.indexOf('edit') != -1)" + Ext.LNT3 +
						"mainActs.push({" + Ext.LNT4 +
							"text : '编辑'," + Ext.LNT4 +
							"iconCls : 'edit-icon'," + Ext.LNT4 +
							"itemId : this.oldId+'edit'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onEdit," + Ext.LNT4 +
							"disabled : this.lock" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (isUpd)
			R_MAIN_ACTS += "" +
					"if (this.roles.indexOf('upd') != -1)" + Ext.LNT3 +
						"mainActs.push({" + Ext.LNT4 +
							"text : '修改'," + Ext.LNT4 +
							"iconCls : 'upd-icon'," + Ext.LNT4 +
							"itemId : this.oldId+'upd'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onUpd," + Ext.LNT4 +
							"disabled : this.lock" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (isDel)
			R_MAIN_ACTS += "" +
					"if (this.roles.indexOf('del') != -1)" + Ext.LNT3 +
						"mainActs.push({" + Ext.LNT4 +
							"text : '删除'," + Ext.LNT4 +
							"disabled : this.lock," + Ext.LNT4 +
							"itemId : this.oldId+'del'," + Ext.LNT4 +
							"iconCls : 'del-icon'," + Ext.LNT4 +
							"scope : this," + Ext.LNT4 +
							"handler : this.onDel" + Ext.LNT3 +
						"});" + Ext.LNT2;
		if (Str.isEmpty(R_MAIN_ACTS) == false)
			R_MAIN_ACTS = R_MAIN_ACTS.substring(0,R_MAIN_ACTS.length()-4);
		//==========单元格定义
		String R_COLUMNS = "";
		for (Fld fld : tb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey")) {
				if (tb.isAutoIncrement())
					continue;
				if (tb.isAutoLocal() && fld.getJavaType().equals(String.class) == false)
					continue;
			}
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
		//==========搜索栏定义
		String R_SEARCH = "";
		for (Fld fld : searchFlds) 
			R_SEARCH += fld.crtVFld().extSearch(Ext.LNT3)+",' ',";
		//==========行选择事件
		String R_ROWCHANGE = "";
		if (isEdit)
			R_ROWCHANGE += "" +
					/**/"if (this.roles.indexOf('edit') != -1)"+ Ext.LNT3 +
					/**/"	this.down('#'+this.oldId+'edit').setDisabled(selected.length === 0);"+ Ext.LNT3;
		if (isUpd)
			R_ROWCHANGE += "" +
					/**/"if (this.roles.indexOf('upd') != -1)"+ Ext.LNT3 +
					/**/"	this.down('#'+this.oldId+'upd').setDisabled(selected.length === 0);"+ Ext.LNT3;
		if (isDel)
			R_ROWCHANGE += "" +
					/**/"if (this.roles.indexOf('del') != -1)"+ Ext.LNT3 +
					/**/"	this.down('#'+this.oldId+'del').setDisabled(selected.length === 0);"+ Ext.LNT3;
		if (Str.isEmpty(R_ROWCHANGE) == false)
			R_ROWCHANGE = R_ROWCHANGE.substring(0,R_ROWCHANGE.length()-5);
		
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【明细行功能定义】", R_LINE_ACTS);
		content = content.replace("【主界面功能定义】", R_MAIN_ACTS);
		content = content.replace("【单元格定义】", R_COLUMNS);
		content = content.replace("【搜索栏定义】", R_SEARCH);
		content = content.replace("【行选择事件】", R_ROWCHANGE);
		content = Ext.cutOrDel(content, "【编辑-头】", "【编辑-尾】", isEdit);
		content = Ext.cutOrDel(content, "【新增-头】", "【新增-尾】", isIns);
		content = Ext.cutOrDel(content, "【修改-头】", "【修改-尾】", isUpd);
		content = Ext.cutOrDel(content, "【删除-头】", "【删除-尾】", isDel);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/List.js";
	}

	public static void crtJs(Tb tb, boolean del, Fld... flds) {
		Ext.crtJs(getJsPath(tb), getContent(tb, flds), del);
	}
	
	public static void main(String[] args) {
		System.out.println(getContent(SysOrg.TB));
	}

}
