package irille.pub.ext;

import irille.core.lg.Lg;
import irille.core.sys.Sys;
import irille.gl.gl.Gl;
import irille.gl.gs.Gs;
import irille.pss.pur.Pur;
import irille.pss.sal.Sal;
import irille.pub.Str;
import irille.pub.tb.OptCust;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * EXT系统选项
 */
public class ExtOpt {
	public static Class[] _optClasses = new Class[] { Sys.class, Lg.class, Gl.class, Gs.class, Sal.class, Pur.class};

	public static String TEMPLATE_SYS = null;
	public static String TEMPLATE_CUST = null;

	static {
		TEMPLATE_SYS = Ext.loadTemplate("Opt.txt");
		TEMPLATE_CUST = Ext.loadTemplate("OptCust.txt");
	}

	public static String getContent(Class opt) {
		String lines = "";
		String isStr = "";
		//		if (opt.isStr())
		//			isStr = "'";
		String values = Ext.getOpeLines(opt);
		if (Str.isEmpty(values) == false) {
			String[] optLines = values.split("\\,");
			for (String line : optLines) {
				String[] keyname = line.split("\\##");
				lines += "{value : " + isStr + keyname[0] + isStr + ", text : '" + keyname[1] + "'}," + Ext.LNT2;
			}
		}
		lines = lines.substring(0, lines.length() - 5);
		String content = TEMPLATE_SYS.replace("【包名】", Ext.getPag(opt));
		content = content.replace("【类变量名】", Ext.getClassVarName(opt));
		content = content.replace("【选项明细】", lines);
		return content;
	}

	public static String getContentCust(OptCust opt) {
		return TEMPLATE_CUST.replace("【变量名】", opt.getCode());
	}

	public static void crtOptAll(boolean del) {
		for (Class line : _optClasses)
			crtOpt(line, del);
	}

	private static void crtOpt(Class clazz, boolean delFlag) {
		Field[] fields = clazz.getDeclaredFields();
		Class[] classes = clazz.getClasses();
		try {
			//系统选项
			for (Class line : classes) {
				if (line.isEnum() && line.getName().endsWith("$T") == false)
					crtJsOpt(line, delFlag);
			}
			//用户选项
			for (Field f : fields) {
				if (f.getType().equals(OptCust.class)) {
					OptCust opt = (OptCust) f.get(clazz);
					if (opt == null) {
						System.out.println(clazz.getName() + " 该类无法通过编译");
						continue;
					}
					crtJsCust(opt, delFlag);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			System.err.println(clazz.getName() + " 选项失败");
		}
	}

	private static String getJsPathOpt(Class opt) {
		return Ext.getPathApp() + "combo/" + Ext.getPag(opt) + "/" + Ext.getClassVarName(opt) + ".js";
	}

	public static void crtJsOpt(Class opt, boolean del) {
		Ext.crtJs(getJsPathOpt(opt), getContent(opt), del);
	}

	private static String getJsPathCust(OptCust opt) {
		return Ext.getPathApp() + "comboCust/" + opt.getCode() + ".js";
	}

	public static void crtJsCust(OptCust opt, boolean del) {
		Ext.crtJs(getJsPathCust(opt), getContentCust(opt), del);
	}

	public static void main(String[] args) throws IllegalArgumentException, SecurityException, IllegalAccessException,
	    InvocationTargetException, NoSuchMethodException, InstantiationException {
		crtOptAll(false);
	}
}
