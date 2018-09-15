package irille.pub.ext;

import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

/**
 * EXT复合窗口类
 */
public class ExtZipWin {

	public static String TEMPLATE = null;

	static {
		TEMPLATE = Ext.loadTemplate("ZipWin.txt");
	}

	public static String getContent(Tb tb, Fld out) {
		String content = TEMPLATE.replace("【包名】", Ext.getPag(tb));
		content = content.replace("【类名】", Ext.getClazz(tb));
		content = content.replace("【外键代码】", out.getCode());
		return content;
	}

	private static String getJsPath(Tb tb) {
		return Ext.getPathApp() + "view/" + Ext.getPag(tb) + "/" + Ext.getClazz(tb) + "/Win.js";
	}

	//注意复合表相关的，JS都在主表下面
	public static void crtJs(Tb tb, Fld outfld, boolean del) {
		Ext.crtJs(getJsPath(tb), getContent(tb, outfld), del);
	}

	public static void main(String[] args) {
	}

}
