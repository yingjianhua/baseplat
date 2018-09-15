package irille.pub.ext;

import irille.core.sys.SysOrg;
import irille.pub.Log;
import irille.pub.html.ExtExp;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Tb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;

/**
 * 环境类，大部分的基础方法
 */
public class Ext {
	private static final Log LOG = new Log(Ext.class);
	private static String _pathApp;
	public static final String LN = "\r\n";
	public static final String LNT1 = "\r\n\t";
	public static final String LNT2 = "\r\n\t\t";
	public static final String LNT3 = "\r\n\t\t\t";
	public static final String LNT4 = "\r\n\t\t\t\t";
	public static final String LNT5 = "\r\n\t\t\t\t\t";
	public static final String LNT6 = "\r\n\t\t\t\t\t\t";

	// 取本地项目的/mvc/app路径
	public static String getPathApp() {
		if (_pathApp != null)
			return _pathApp;
		_pathApp = Ext.class.getResource("/").getFile()
				.replace("/WEB-INF/classes", "/mvc/app");
		return _pathApp;
	}

	public static void main(String[] args) {
		System.out.println(getPag(SysOrg.TB));
		System.out.println(getClazz(SysOrg.TB));
	}
	
	// 根据TB取包名
	public static String getPag(Tb tb) {
		return tb.getClazz().getName().split("\\.")[2];
	}

	// 根据OPT取包名 class irille.bean.sys.Sys$OYn
	public static String getPag(Class opt) {
		return opt.getName().split("\\.")[2];
	}

	// 根据OPT取类+变量名
	public static String getClassVarName(Class opt) {
		String[] tmp = opt.getName().split("\\.");
		return tmp[tmp.length - 1].replace("$", "");
	}

	// 根据OPT取变量名--更改为内部类名
	public static String getVarName(Class opt) {
		String[] tmp = opt.getName().split("\\.");
		return tmp[tmp.length - 1].split("\\$")[1];
	}

	// 根据OPT取类名
	public static String getOptClazz(Class opt) {
		String[] tmp = opt.getName().split("\\.");
		return tmp[tmp.length - 1].split("\\$")[0];
	}

	// 根据OPT取键值对
	public static String getOpeLines(Class opt) {
		Field[] fields = opt.getDeclaredFields();
		String str = "";
		ArrayList<String> ar = new ArrayList<String>();
		for (Field line : fields) {
			if (line.getType().equals(opt)
					&& line.getName().equals("DEFAULT") == false) {
				Method m;
				try {
					m = opt.getMethod("getLine");
					EnumLine en = (EnumLine) m.invoke(line.get(opt));
					ar.add(en.getKey() + "##" + en.getName());
				} catch (Exception e) {
					System.err.println(line.getType());
					throw LOG.err("err", "反射取枚举选项出错");
				}
			}
		}
		Collections.sort(ar);
		for (String line : ar)
			str += line + ",";
		return str;
	}

	// 根据OPT--取其默认值
	public static String getOpeDefault(Class opt) {
		Field[] fields = opt.getDeclaredFields();
		for (Field line : fields) {
			if (line.getType().equals(opt) && line.getName().equals("DEFAULT")) {
				Method m;
				try {
					m = opt.getMethod("getLine");
					EnumLine en = (EnumLine) m.invoke(line.get(opt));
					return en.getKey() + "";
				} catch (Exception e) {
					System.err.println(line.getType());
					throw LOG.err("err", "反射取枚举选项默认值出错");
				}
			}
		}
		return null;
	}

	// 根据TB取类名
	public static String getClazz(Tb tb) {
		return getClazz(tb.getClazz());
	}

	public static String getClazz(Class clazz ){
		return clazz.getName().split("\\.")[3];	
	}
	
	// 创建EXT前台JS文件
	public static void crtJs(String fileName, String content, boolean delFlag) {
		File f = new File(fileName);
		if (f.exists() && !delFlag) {
			System.out.println(fileName + " 跳过");
		} else {
			f.getParentFile().mkdirs();
			try {
				OutputStreamWriter out = new OutputStreamWriter(new FileOutputStream(
						fileName), "UTF-8");
				out.write(content);
				out.close();
				System.err.println(fileName + " 【创建】");
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(fileName + " 失败");
			}
		}
	}

	// 截取中间的一段代码
	public static String cutOrDel(String content, String s1, String s2,
			boolean has) {
		if (has)
			return content.replace(s1, "").replace(s2, "");
		int start = content.indexOf(s1);
		int end = content.indexOf(s2);
		return content.substring(0, start) + content.substring(end + s2.length());
	}

	// 加载模板文件
	public static String loadTemplate(String name) {
		File file = new File(Ext.class.getResource(name).getPath());
		BufferedReader reader = null;
		StringBuffer buf = new StringBuffer();
		try {
			reader = new BufferedReader(new FileReader(file));
			String tempString = null;
			while ((tempString = reader.readLine()) != null) {
				buf.append(tempString + "\r\n");
			}
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("EXT表格模板加载失败!");
		}
		return buf.toString();
	}

	public static final String[] T = new String[20]; // 各级缩进的定符串
	public static final String TAB_STEP = "\t";// Env.TAB; // 如果为"",则不缩进
	public static final String MVC = "mvc.";
	public static final int PAGE_SIZE = 20; // 列表显示的行数
	public static final int WIN_WIDTH = 400; // 窗口宽度
	static {
		String s = "";
		for (int i = 1; i < T.length; i++) {
			T[i] = s;
			s += TAB_STEP;
		}
		T[0] = T[1]; // 数组0与1的值一样，以防溢出出错。
	}

	//
	// public static String dirStore(String pack, String clazz) {
	// return "'" + MVC + "store." + pack + "." + clazz + "'";
	// }
	//
	// public static String dirModel(String pack, String clazz) {
	// return MVC + "model." + pack + "." + clazz ;
	// }
	//
	// public static String dirView(String pack, String clazz, String add) {
	// return MVC + "view." + pack + "." + clazz + "." + add ;
	// }

	public static String url(String pack, String clazz, String add) {
		return "base_path+'/" + pack + "_" + clazz + "_" + add + "'";
	}

	/**
	 * 输出对象的JS代码
	 * 
	 * @param obj
	 *          对象
	 * @param tabs
	 *          缩进层级
	 * @param buf
	 *          输出的目的缓冲区
	 */
	public static void outObject(Object obj, int tabs, StringBuilder buf) {
		if (obj == null) {
			buf.append("null");
			return;
		}
		if (obj instanceof IExtOut) {
			((IExtOut) obj).out(tabs, buf);
			return;
		}
		if (obj instanceof String) {
			buf.append("'" + obj.toString() + "'");
			return;
		}

		buf.append(obj.toString());
	}

	/**
	 * 常量定义接口
	 * 
	 * @author whx
	 * 
	 */
	public interface IExtVars {
		public static final String[] T = Ext.T;
		public static final String LN = Env.LN;
		public static final String SS = "'";
		//@formatter:off
		public static final String
			AFTER_LABEL_TEXT_TPL="afterLabelTextTpl",
			ALLOW_BLANK="allowBlank",
			ALLOW_DECIMALS="allowDecimals",
			ALIGN="align",
			ANCHOR="anchor",
			AUTO_LOAD="autoLoad",
			BODY_PADDING="bodyPadding",
			BORDER="border",
			BUTTON_ALIGN="buttonAlign",
			BUTTONS="buttons",
			COLUMNS="columns",
			CONVERT="convert",
			DATA_INDEX="dataIndex",
			DATE_FORMAT="dateFormat",
			DECIMAL_PRECISION="decimalPrecision",
			EDITOR="editor",
			EDITABLE="editable",
			EMPTY_TEXT="emptyText",
			EXTEND="extend",
			FAILURE="failure",
			FIELD_DEFAULTS="fieldDefaults",
			FIELD_LABEL="fieldLabel",
			FORCE_SELECTION="forceSelection",
			FORM="form",
			FORMAT="format",
			FRAME="frame",
			HANDLER="handler",
			HEIGHT="height",
			ICON_CLS="iconCls",
			INIT_COMPONENT="initComponent",
			INS_FLAG="insFlag",
			IS_CELL="isCell",
			IS_PCT="isPct",
			ITEM_CLS="itemCls",
			ITEMS="items",
			LABEL_WIDTH="labelWidth",
			LABEL_STYLE="labelStyle",
			LAYOUT="layout",
			LISTENERS="listeners",
			MODE="mode",
			MODEL="model",
			MAPPING="mapping",
			NAME="name",
			PAGE_SIZE="pageSize",
			PARAMS="params",
			PCT_VALUE="pctValue",
			PLAIN="plain",
			PLUGINS="plugins",
			PROXY="proxy",
			PTYPE="ptype",
			READER="reader",
			READ_ONLY="readOnly",
			HIDDEN="hidden",
			REMOTE_SORT="remoteSort",
			RENDERER="renderer",
			REQUIRES="requires",
			RESIZABLE="resizable",
			ROOT="root",
			SIMPLE_SORT_MODE="simpleSortMode",
			SCOPE="scope",
			SORTABLE="sortable",
			EXPANDCOL="expandCol",
			STORE="store",
			SUBMIT_EMPTY_TEXT="submitEmptyText",
			SUCCESS="success",
			TEXT="text",
			THIS="this.",
			TRIGGER_ACTION="triggerAction",
			TOTAL_PROPERTY="totalProperty",
			TYPE="type",
			TYPE_AHEAD="typeAhead",
			URL="url",
			USE_NULL="useNull",
			VALUE="value",
			DIY_SQL="diySql",
			VALUE_FIELD="valueField",
			WAIT_TITLE="waitTitle",
			WAIT_MSG="waitMsg",
			WIDTH="width",
			XTYPE="xtype";
			ExtExp	EXP_THIS=new ExtExp("this");
		//@formatter:on
	}

	/**
	 * 输出接口
	 * 
	 * @author whx
	 * 
	 */
	public interface IExtOut extends IExtVars {
		public void out(int tabs, StringBuilder buf);
		public String toString(int tabs);
	}

	
}
