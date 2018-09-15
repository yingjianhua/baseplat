package irille.pub.ext;

import irille.pub.tb.Tb;

/**
 * EXT外键选择器窗口类
 */
public class ExtWinTrigger {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("Trigger.txt");
	}

	public static String getContent(Tb tb) {
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【名称】", tb.getName());
		return content;
	}
	
	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/Trigger.js";
	}

	public static void crtJs(Tb tb, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb), del);
	}

	public static void main(String[] args) {
//		System.err.println(getContent(SysA.TB));
	}

}
