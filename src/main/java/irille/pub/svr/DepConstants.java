//Created on 2005-11-16
package irille.pub.svr;

import irille.pub.Log;

import java.util.Hashtable;


/**
 * Title: 开发用常量<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class DepConstants {
	private static final Log LOG = new Log(DepConstants.class);

	
	private static final Hashtable<String, String[]> paths = new Hashtable<String, String[]>();
	public static final String DOC_ROOT_DIR="D:/irilleDoc/";  //开发文档存放的根目录
	public final static String LOCAL_PATH_PREFIX = "file://";
	public final static String FILE_SEPA = "/";

	static {
		paths.put("D:/web/model", new String[] { "irille.dep", "irille.structs" });
		paths.put("D:/web/core/JavaSource", new String[] { "irille.base",
				"irille.cli", "irille.crm", "irille.erp", "irille.gl", "irille.inv",
				"irille.pub", "irille.sal", "irille.svr", "irille.pur" });
	}
	public static final String getSourcePath(String packageCodeHead) {
		for (String path : paths.keySet()) {
			String[] packs = paths.get(path);
			for (String pack : packs) {
				if (!packageCodeHead.startsWith(pack))
					continue;
				return path + "/" + packageCodeHead.replace('.', '/');
			}
		}
		throw LOG.err("pathNotFound",
				"包[{0}]的源代码路径没定义", packageCodeHead);
	}
	
	
}
