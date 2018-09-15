package irille.pub.ext;

import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldV;
import irille.pub.tb.Tb;

/**
 * EXT普通表单类
 */
public class ExtForm<T extends ExtForm>  {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("Form.txt");
	}

	public static String getContent(Tb tb) {
		String fields = "";
		boolean showKey = false; //列表是否显示主键 | 系统自增与逻辑自增不显示
		for (Fld fld : tb.getFlds()) {
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			if (fld.getCode().equals("pkey")) {
				if (tb.isAutoIncrement())
					continue;
				if (tb.isAutoLocal() && fld.getJavaType().equals(String.class) == false)
					continue;
				showKey = true;
			}
			fields += fld.crtVFld().extForm(Ext.LNT3, false) + ",";
		}
		//自增长主键放在最后
		if (showKey == false)
			fields += tb.get("pkey").crtVFld().extForm(Ext.LNT3, false) + ",";
		fields = fields.substring(0, fields.length() - 1);
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【字段集】", fields);
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/Form.js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}

}
