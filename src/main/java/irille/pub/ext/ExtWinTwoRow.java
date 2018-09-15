package irille.pub.ext;

import irille.pub.tb.Tb;

/**
 * EXT普通窗口类
 */
public class ExtWinTwoRow {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("WinTwoRow.txt");
	}

	public static String getContent(Tb tb) {
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		return content;
	}
	
	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/Win.js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}

	public static void main(String[] args) {
//		System.err.println(getContent(SysA.TB));
	}

}
