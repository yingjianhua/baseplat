package irille.pub.ext;

import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

/**
 * EXT复合表格类
 */
public class ExtZipList {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ZipList.txt");
	}

	/**
	 * @param tb 主表TB
	 * @param outFlds 从表外键字段
	 * @param searchFlds 查询字段
	 * @param mainFlds 主表显示字段
	 * @return
	 */
	public static String getContent(Tb tb, Fld[] outFlds, Fld[] searchFlds, Fld[] mainFlds) {
		boolean isUpd = tb.checkAct("upd") != null;
		boolean isIns = tb.checkAct("ins") != null;
		boolean isDel = tb.checkAct("del") != null;
		//==========主界面功能代码
		String R_MAIN_ACTS = "var mainActs = [];" + Ext.LNT2;
		if (isIns)
			R_MAIN_ACTS += "" + "if (this.roles.indexOf('ins') != -1)" + Ext.LNT3 + "mainActs.push({" + Ext.LNT4
			    + "text : '新增'," + Ext.LNT4 + "iconCls : 'ins-icon'," + Ext.LNT4 + "itemId : this.oldId+'ins'," + Ext.LNT4
			    + "scope : this," + Ext.LNT4 + "handler : this.onIns" + Ext.LNT3 + "});" + Ext.LNT2;
		if (isUpd)
			R_MAIN_ACTS += "" + "if (this.roles.indexOf('upd') != -1)" + Ext.LNT3 + "mainActs.push({" + Ext.LNT4
			    + "text : '修改'," + Ext.LNT4 + "iconCls : 'upd-icon'," + Ext.LNT4 + "itemId : this.oldId+'upd'," + Ext.LNT4
			    + "scope : this," + Ext.LNT4 + "handler : this.onUpd," + Ext.LNT4 + "disabled : this.lock" + Ext.LNT3 + "});"
			    + Ext.LNT2;
		if (isDel)
			R_MAIN_ACTS += "" + "if (this.roles.indexOf('del') != -1)" + Ext.LNT3 + "mainActs.push({" + Ext.LNT4
			    + "text : '删除'," + Ext.LNT4 + "disabled : this.lock," + Ext.LNT4 + "itemId : this.oldId+'del'," + Ext.LNT4
			    + "iconCls : 'del-icon'," + Ext.LNT4 + "scope : this," + Ext.LNT4 + "handler : this.onDel" + Ext.LNT3 + "});"
			    + Ext.LNT2;
		if (Str.isEmpty(R_MAIN_ACTS) == false)
			R_MAIN_ACTS = R_MAIN_ACTS.substring(0, R_MAIN_ACTS.length() - 4);
		//==========搜索栏定义
		String R_SEARCH = "";
		for (Fld fld : searchFlds)
			R_SEARCH += fld.crtVFld().extSearch(Ext.LNT3) + ",' ',";
		//===========主表信息定义
		String R_MAIN_INFO = "";
		for (Fld fld : mainFlds)
			R_MAIN_INFO += fld.crtVFld().extForm(Ext.LNT5, true) + ",";
		R_MAIN_INFO = R_MAIN_INFO.substring(0, R_MAIN_INFO.length() - 1);
		//===========从表标签页定义
		String R_LIST_LINES = "";
		for (Fld fld : outFlds)
			R_LIST_LINES += "{" + Ext.LNT4 +
			/**/"xtype : Ext.create('mvc.view." + Ext.getPag(tb) + "." + Ext.getClazz(tb) + ".ListLine"
			    + fld.getTb().getClazz().getSimpleName() + "',{" + Ext.LNT5 +
			    /**/"title : '" + fld.getTb().getName() + "'," + Ext.LNT5 +
			    /**/"itemId : this.oldId+'linetable'," + Ext.LNT5 +
			    /**/"iconCls : 'tab-user-icon'" + Ext.LNT4 +
			    /**/"})" + Ext.LNT3 +
			    /**/"},";
		R_LIST_LINES = R_LIST_LINES.substring(0, R_LIST_LINES.length() - 1);

		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【主界面功能定义】", R_MAIN_ACTS);
		content = content.replace("【搜索栏定义】", R_SEARCH);
		content = content.replace("【外键代码】", outFlds[0].getCode()); //TODO 待优化
		content = content.replace("【主表名称】", tb.getName());
		content = content.replace("【主表信息定义】", R_MAIN_INFO);
		content = content.replace("【从表标签页】", R_LIST_LINES);
		content = Ext.cutOrDel(content, "【新增-头】", "【新增-尾】", isIns);
		content = Ext.cutOrDel(content, "【修改-头】", "【修改-尾】", isUpd);
		content = Ext.cutOrDel(content, "【删除-头】", "【删除-尾】", isDel);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/List.js";
	}

	public static void crtJs(Tb tb, boolean del, Fld[] outFlds, Fld[] searchFlds, Fld[] mainFlds) {
		Ext.crtJs(getJsPath(tb), getContent(tb, outFlds, searchFlds, mainFlds), del);
	}

	public static void main(String[] args) {
//		System.out.println(getContent(GlJlAmt.TB, new FldOutKey[] { (FldOutKey) GlJlAmtLine.FLD_JOURNAL }, new Fld[] {
//		    GlJlAmt.FLD_CODE, GlJlAmt.FLD_NAME, GlJlAmt.FLD_ACCOUNT }, new Fld[] { GlJlAmt.FLD_CODE,
//		    GlJlAmt.FLD_NAME, GlJlAmt.FLD_BOOK, GlJlAmt.FLD_ACCOUNT, GlJlAmt.FLD_DIRECT, GlJlAmt.FLD_BALANCE,
//		    GlJlAmt.FLD_LINE_TYPE, GlJlAmt.FLD_STATUS }));
	}

}
