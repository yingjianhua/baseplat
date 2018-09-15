package irille.pub.ext;

import irille.pub.Str;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT外键选择器表格类
 */
public class ExtListTrigger {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("TriggerList.txt");
	}

	public static String getContent(Tb tb, Fld vfld, Fld... combFlds) {
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
		R_COLUMNS = R_COLUMNS.substring(0, R_COLUMNS.length() - 3);
		//==========选择集定义等
		String R_COMB = "";
		String R_COMB_CODE = "";
		for (Fld fld : combFlds) {
			if (Str.isEmpty(R_COMB_CODE))
				R_COMB_CODE = fld.getCode();
			R_COMB += Ext.LNT6 + "{value : '" + fld.getCode() + "', text : '" + fld.getName() + "'},";
		}
		R_COMB = R_COMB.substring(0, R_COMB.length() - 1);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【字段集】", R_COLUMNS);
		content = content.replace("【选择集】", R_COMB);
		content = content.replace("【选择默认代码】", R_COMB_CODE);
		content = content.replace("【显示字段代码】", vfld.getCode());
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/TriggerList.js";
	}

	public static void crtJs(Tb tb, boolean del, Fld vfld, Fld... combFlds) {
		Ext.crtJs(getJsPath(tb), getContent(tb, vfld, combFlds), del);
	}

}
