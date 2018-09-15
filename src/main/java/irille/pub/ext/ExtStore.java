package irille.pub.ext;

import irille.pub.tb.Tb;

/**
 * EXT仓库类
 */
public class ExtStore {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("Store.txt");
	}

	public static String getContent(Tb tb) {
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "store/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + ".js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}

}
