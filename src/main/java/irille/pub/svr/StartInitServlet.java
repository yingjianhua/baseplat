/*
 *  DLOG_RandomImageServlet.java
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 *  Author: Winter Lau
 *  http://dlog4j.sourceforge.net
 *  
 */
package irille.pub.svr;

import irille.action.sys.SysMenuAction;
import irille.core.sys.SysMenuAct;
import irille.core.sys.SysOrg;
import irille.pub.FileTools;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.struts2.ServletActionContext;

/**
 * 系统ENV初始化
 * @author IBM
 */
public class StartInitServlet extends HttpServlet {

	public final static String RANDOM_LOGIN_KEY = "RANDOM_LOGIN_KEY";

	public void init() throws ServletException {
		System.out.println(new Date().toLocaleString() + " : 系统ENV初始化");
		Env.INST.getDB();
		DbPool.getInstance();
		//太多的循环引用 TODO 
		SysMenuAct.TB.getCode();
		initBeanLoad();
		SysMenuAction.initMenus();
	}

	public static void initBeanLoad() {
		File rootFile = new File(SysOrg.class.getResource("/").getFile());
		initBeanLoad(rootFile, rootFile.getPath() + File.separator, BeanBase.class);
		initBeanLoadInJar(FileTools.BASE_JAR);
		initBeanLoadInJar(FileTools.WX_JAR);
	}

	private static <T> void initBeanLoad(File rootFile, String parentDirectory, Class<T> parentClass) {
		if (rootFile.isDirectory()) {
			File[] files = rootFile.listFiles();
			for (File file : files) {
				initBeanLoad(file, parentDirectory, parentClass);
			}
		} else {
			String className = null;
			try {
				if (rootFile.getPath().indexOf(".class") != -1) {
					className = rootFile.getPath().replace(parentDirectory, "").replace(".class", "")
					    .replace(File.separator, ".");
					if (className.contains("$") || className.contains("irille.action") || className.contains("irille.pub")
					    || className.contains("DAO") || className.contains("irille.dep") || className.contains("irille.doc")
					    || className.contains("irille.print") || className.contains("irille.trandb")) //内部类、公共包跳过
						return;
					Class clazz = Class.forName(className);
					if (BeanMain.class.isAssignableFrom(clazz))
						System.out.println("类初始加载：" + ((BeanMain) clazz.newInstance()).tb(clazz).getCode());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 自动搜索项目的lib，初始化其中的指定jar包中的class，以解决静态块初始化未初始化的问题
	 * @param jarName jar文件的名字（需要填写.jar后缀名）
	 */
	private static void initBeanLoadInJar(String jarName) {
		try {
			if (new File(jarName).exists() == false)
				return;
			JarFile jars = new JarFile(jarName);
			Enumeration<JarEntry> enu = jars.entries();
			while (enu.hasMoreElements()) {
				String className = "";
				String clsname = enu.nextElement().getName();
				if (clsname.indexOf(".class") != -1) {
					className = clsname.replace(".class", "").replace("/", ".");
					if (className.contains("$") || className.contains("irille.action") || className.contains("irille.pub")
					    || className.contains("DAO") || className.contains("irille.dep") || className.contains("irille.doc")
					    || className.contains("irille.print") || className.contains("irille.trandb")) //内部类、公共包跳过
						continue;
					Class clazz = Class.forName(className);
					if (BeanMain.class.isAssignableFrom(clazz))
						System.out.println("类初始加载：" + ((BeanMain) clazz.newInstance()).tb(clazz).getCode());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
