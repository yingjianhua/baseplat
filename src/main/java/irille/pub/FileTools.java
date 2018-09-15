//Created on 2005-11-15
package irille.pub;

import irille.pub.PubInfs.IMsg;
import irille.pub.svr.Env;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.sql.Blob;
import java.util.Properties;
import java.util.Vector;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;

/**
 * Title: 文件工具<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FileTools implements IPubVars {
	private static final Log LOG = new Log(FileTools.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		copyReadErr("读文件[{0}]出错！"),
		copyWriteErr("文件[{0}]已存在，不可以覆盖！"),
		copy("复制文件：{0}" + Env.LN + "  -->{1}"),
		delOk("删除文件[{0}]成功！"),
		delErr("删除的文件[{0}]不存在！"),
		findCode("在类【{0}】中找代码【{1}】-【{2}】出错！"),
		findCodeErr("在文件【{0}】中找代码【{1}】-【{2}】出错！"),
		wirteFileErr("写文件[{0}]出错!"),
		readFileErr("读文件[{0}]出错!"),
		openFileErr("打开文件[{0}]出错!"),
		urlIsNull("Url不能为null!"),
		readUrl("读Url[{0}]出错!"),
		writeBlog("写BLOG到文件[{0}]出错!"),
		mkdirOk("目录：[{0}]建立成功!"),
		mkdirExist("目录：[{0}]已存在，不能建立!"),
		mkdirErr("建立新目录：[{0}]失败!"),
		compFile("比较文件：" + Env.LN + "文件:{0}"+LN+ "文件:{1}" + LN+ "这两个文件完全一样！"),
		notFoundFldText("在代码中没代到\"{0}\"的识别文本!"),
		;
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on


	public static final String FILE_SEPARATOR = Env.FILE_SEPARATOR;
	public static final String BASE_JAR = FileTools.class.getResource("/").getPath().replaceAll("classes", "lib/"+"irille.jar");
	public static final String WX_JAR = FileTools.class.getResource("/").getPath().replaceAll("classes", "lib/"+"wx.jar");

	/**
	 * 复制文件
	 * 
	 * @param fromFile
	 * @param toFile
	 * @param over
	 *          是否覆盖已有的文件
	 */
	public static void copy(String fromFile, String toFile, boolean over) {
		File from = new File(fromFile);
		if (!from.isFile())
			throw LOG.err(Msgs.copyReadErr, fromFile);
		File to = new File(toFile);
		if (to.isFile() && !over) {
			throw LOG.err(Msgs.copyWriteErr, toFile);
		}
		new File(to.getParent()).mkdirs(); // 建立上级目录
		FileInputStream fi = null;
		FileOutputStream fo = null;
		FileChannel in = null;
		FileChannel out = null;
		try {
			fi = new FileInputStream(from);
			fo = new FileOutputStream(to);
			in = fi.getChannel();// 得到对应的文件通道
			out = fo.getChannel();// 得到对应的文件通道
			in.transferTo(0, in.size(), out);// 连接两个通道，并且从in通道读取，然后写入out通道
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				fi.close();
				in.close();
				fo.close();
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		LOG.info(Msgs.copy, fromFile, toFile);
	}

	/**
	 * 删除文件，如不存在不提示
	 * 
	 * @param fileName
	 */
	public static void del(String fileName) {
		File file = new File(fileName);
		if (file.isFile() && file.exists()) {
			file.delete();
			LOG.info(Msgs.delOk, fileName);
		} else
			LOG.info(Msgs.delErr, fileName);
	}
	
	/**
	 * 读取java文件中特定的内容
	 * @param clazz
	 * @param beginStr 开始标识
	 * @param endStr 结束标识
	 * @return
	 */
	public static String loadJavaFileLines(Class clazz, String beginStr,
			String endStr) {
		String file = FileTools.tranSrcFileDir(clazz);
		Vector<String> lns = null;
		//假如file不存在 到基础jar中找
		if(!new File(file).exists()) {
			String fileName = clazz.getName().replace(".", "/")+".java";
			lns = readToBufInJar(fileName);
		} else {
			lns = FileTools.readToBuf(file);
		}
		return loadLines(beginStr, endStr, lns, Msgs.findCode, clazz.toString());
	}
	
	/**
	 * 读取文件中特定内容
	 * @param fileName 文件名（路径）
	 * @param beginStr 开始标识
	 * @param endStr 结束标识
	 * @return
	 */
	public static String loadFileLines(String fileName, String beginStr, String endStr) {
		Vector<String> lns = null;
		lns = FileTools.readToBuf(fileName);
		return loadLines(beginStr, endStr, lns, Msgs.findCodeErr, fileName);
	}
	
	private static String loadLines(String beginStr, String endStr, Vector<String> lns, Msgs findcodeerr, String errName) {
		int begin = -1;
		int end = -1;
		int i;
		String ln;
		for (i = 0; i < lns.size(); i++) {
			ln = lns.get(i);
			if (ln.indexOf(beginStr) >= 0) { // 起始位置
				begin = i;
				continue;
			}
			if (ln.indexOf(endStr) >= 0) { // 结束位置
				end = i;
			}
		}
		if (begin == -1 || end == -1 || end < begin)
			throw LOG.err(findcodeerr, errName,
					beginStr, endStr);

		StringBuilder out = new StringBuilder();
		for (i = begin + 1; i < end; i++)
			out.append(lns.get(i));
		return out.toString();
	}
	
	/**
	 * 将字符串写到文件
	 * 
	 * @param fileName
	 *          文件名
	 * @param str
	 *          字符串
	 */
	public static void writeStr(String fileName, String str) {
		File file = new File(fileName);
		new File(file.getParent()).mkdirs(); // 建立上级目录
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(str);
			writer.close();
		} catch (IOException e) {
			throw LOG.err(Msgs.wirteFileErr, fileName);
		}
	}

	/**
	 * 取代码文件的绝对路径名
	 * 例如：irille.core.sys.sysOrg类，转换为：用户目录/src/irille/core/sys/sysOrg.java
	 * @param clazz
	 * @return
	 */
	public static String tranSrcFileDir(Class clazz) {
		return Env.getUserDir() + "src/main/java" + FILE_SEPARATOR
				+ tranClassToFileDir(clazz) + ".java";
	}
	
	/**
	 * 将类名转换为目录。
	 * 例如：irille.core.sys.sysOrg  转换为 irille/core/sys/sysOrg
	 * 
	 * @param clazz
	 * @return
	 */
	public static String tranClassToFileDir(Class clazz) {
		return clazz.getName().replace('.', FILE_SEPARATOR.charAt(0));
	}
	
	/**
	 * 将包名转换为目录。
	 * 例如：irille.core.sys.sysOrg  转换为 irille/core/sys
	 * 
	 * @param clazz
	 * @return
	 */
	public static String tranPackageToFileDir(Class clazz) {
		return clazz.getPackage().getName().replace('.', FILE_SEPARATOR.charAt(0));
	}
	

	/**
	 * 读文本文件到Vector<String>对象中
	 * 
	 * @param fileName
	 * @return
	 */
	public static Vector<String> readToBuf(String fileName) {
		Vector<String> buf = new Vector();
		try {
//			FileReader fr = new FileReader(fileName);
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"utf-8"));
			String s = br.readLine();
			while (s != null) {
				buf.add(s + LN);
				s = br.readLine();
			}
			br.close();
		} catch (IOException e) {
			throw LOG.err(Msgs.readFileErr, fileName);
		}
		return buf;
	}
	
	/**
	 * 读Jar文件中的文本文件到Vector<String>对象中
	 * 
	 * @param fileName
	 * @return
	 */
	public static Vector<String> readToBufInJar(String fileName) {
		Vector<String> buf = new Vector<String>();
		try {
	    JarFile jar = new JarFile(BASE_JAR);
	    ZipEntry entry = jar.getEntry(fileName);
	    BufferedReader br = new BufferedReader(new InputStreamReader(jar.getInputStream(entry)));
			String s = br.readLine();
			while (s != null) {
				buf.add(s + LN);
				//buf.add(s);
				s = br.readLine();
			}
			br.close();
			jar.close();
		} catch (IOException e) {
			throw LOG.err(Msgs.readFileErr, fileName);
    }
		return buf;
	}
	/**
	 * 从文件中读取属性
	 * 
	 * @param name
	 * @return 结果
	 */
	public static final Properties loadProperties(String name) {
		Properties pro = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(new File(name));
		} catch (FileNotFoundException e) {
			throw LOG.err(Msgs.openFileErr, name);
		}
		try {
			pro.load(in);
		} catch (IOException e) {
			throw LOG.err(Msgs.readFileErr, name);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return pro;
	}
	public static final Properties loadProperties(File file) {
		Properties pro = new Properties();
		FileInputStream in;
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw LOG.err(Msgs.openFileErr, file);
		}
		try {
			pro.load(in);
		} catch (IOException e) {
			throw LOG.err(Msgs.readFileErr, file);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
			}
		}
		return pro;
	}

	public final static int MAX_BUFF_LEN = 2097152;
	private static final byte[] URL_BUF = new byte[MAX_BUFF_LEN];
	public static int count = 0;
	/**
	 * 取URL的内容到byte[]
	 * 
	 * @param url
	 *          url
	 * @return 结果
	 */
	public static byte[] loadUrlToBytes(URL url) {
		if (url == null)
			throw LOG.err(Msgs.urlIsNull);
		synchronized (URL_BUF) {
			int size = 0;
			InputStream stream = null;
			try {
				stream = url.openStream();
				int count = 0;
				while (true) {
					count = stream.read(URL_BUF, size, MAX_BUFF_LEN - size);
					if (count < 0)
						break;
					size += count;
				}
			} catch (Exception e) {
				throw LOG.err(Msgs.readUrl, url);
			} finally {
				if (stream != null)
					try {
						stream.close();
					} catch (Exception e) {
						e.printStackTrace();
					}
			}
			byte[] bytes = new byte[size];
			System.arraycopy(URL_BUF, 0, bytes, 0, size);
			return bytes;
		}
	}

	/**
	 * 将BLOG写到文件
	 * 
	 * @param blob
	 * @param file
	 */
	public static void writeBlob2File(Blob blob, File file) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(file);
			byte[] buff = new byte[2048];
			InputStream in = blob.getBinaryStream();
			int count = 0;
			while ((count = in.read(buff)) > 0)
				out.write(buff, 0, count);
			out.close();
			in.close();
		} catch (Exception e) {
			if (out != null)
				try {
					out.close();
				} catch (Exception ee) {
				}
			throw LOG.err(Msgs.writeBlog, file);
		}
	}
	/**
	 * 建立目录
	 * 
	 * @param dir
	 *          目录名
	 */
	public static final void mkDir(String... dirNames) {
		for (String dirName : dirNames) {
			File dir = new File(dirName);
			if (dir.mkdirs())
				LOG.info(Msgs.mkdirOk, dirName);
			else if (dir.isDirectory())
				LOG.info(Msgs.mkdirExist, dirName);
			else
				throw LOG.err(Msgs.mkdirErr, dirName);

		}
	}

	/**
	 * 比较文件，忽略空白字符
	 * 
	 * @param fileName1
	 * @param fileName2
	 * @return 是否正常，出错则返回false
	 */
	public static final boolean cmpFileIgnoreBlank(String fileName1,
			String fileName2) {
		CmpFile f1 = new CmpFile(fileName1);
		CmpFile f2 = new CmpFile(fileName2);
		String s1, s2;
		while (true) {
			s1 = f1.nextIgnoreBlank();
			s2 = f2.nextIgnoreBlank();
			if (s1 == null && s2 == null)
				break;
			if (s1 == null || s2 == null || !s1.equals(s2)) {
				//
				// System.err.println("!!!"+f2.getLineStr()+"!!"+f2.getLineStr().length());
				// //此段代码用于比较文件的特殊处理，有Win.js代码中少了一处“;”，用于跳过这非错误的代码！ whx 20141003
				// if(s2!=null &&
				// f2.getLineStr().substring(0,"		}]".length()).equals("		}]")) {
				// f1.back();
				// continue;
				// }

				Env.errPrintln("比较文件：" + LN);
				f2.outErrString();
				f1.outErrString();
				return false;
			}
			// System.err.println("s1="+s1+";s2="+s2);
		}
		LOG.info(Msgs.compFile, fileName1,fileName2);
		return true;
	}
	/**
	 * 要比较的文件对象
	 * 
	 * @author surface1
	 * 
	 */
	public static class CmpFile {
		final static String BLANK_CHARS = " \t\n\r";
		Vector<String> _lns;
		int _line = 0, _ch = 0;
		String _fileName;

		CmpFile(String fileName) {
			_lns = FileTools.readToBuf(fileName);
			_fileName = fileName;
		}

		String nextIgnoreBlank() {
			String s = next();
			while (s != null && BLANK_CHARS.indexOf(s) >= 0)
				s = next();
			return s;
		}

		public void back() {
			_line--;
		}

		String next() {
			if (_lns.size() == 0 || _lns.size() <= _line)
				return null;
			String ln = _lns.get(_line);
			if (_ch >= ln.length()) {
				_line++;
				_ch = 0;
				return next();
			}
			return ln.substring(_ch, ++_ch);
		}

		public String getLineStr() {
			return _lns.get(_line);
		}

		public int getLine() {
			return _line;
		}

		int getCh() {
			return _ch;
		}

		public void outErrString() {
			Env.outPrintln("!!!!!!!!"+_fileName + "[" + (_line + 1) + "," + (_ch + 1) + "]:");
			int l = _line > 2 ? _line - 3 : 0;
			String s;
			for (int i = 0; i < 5; i++) {
				if (_lns.size() <= l) {
					Env.errPrintln("已结束！");
					break;
				}
				s="  [" + (l + 1) + "]: " + _lns.get(l);
				if (l < _line)
					Env.outPrint(s);
				if(l>_line)
					Env.outPrint(s);
				if(l==_line) {
					String ln=_lns.get(l);
					Env.outPrint("  [" + (l + 1) + "]: "+ln.substring(0,_ch-1));
					Env.outPrint(">>>"+ln.substring(_ch-1));					
				}
				l++;
			}
		}
	}
	/** 
	 * 替换自动产生的代码
	 * 替换的代码为这样的代码之间：
	 *  //>>>>>>以下是自动产生的源代码行--name--请保留此行用于识别>>>>>>
	 *  //<<<<<<以上是自动产生的源代码行--name--请保留此行用于识别<<<<<<
	 * @param clazz JAVA类
	 * @param name 代码的识别名称
	 * @param buf 代码
	 */
	public static void replaceJavaFile(Class clazz,String name, StringBuilder buf) {
		String bStr=">>>以下是自动产生的源代码行--"+name+"--";
		String eStr="<<<以上是自动产生的源代码行--"+name+"--";
		// 更新文件
		String file = FileTools.tranSrcFileDir(clazz);
		Vector<String> lns = FileTools.readToBuf(file);
//		for (String l : lns)
//			// 先输出原来的代码, 以防止出错
//			System.out.println(l);
		int lastClose = -1;
		int begin = -1;
		int end = -1;
		int i;
		String ln;
		for ( i = lns.size() - 1; i >= 0 ; i--) {
			ln = lns.get(i);
			if (begin == -1)
				if (ln.indexOf(bStr) >= 0) { // 找自动产生代码的起始位置
					begin = i;
					continue;
				}
			if (end == -1)
				if (ln.indexOf(eStr) >= 0) // 找自动产生代码的结束位置
					end = i;
		}
		//去掉以前有问题在自动代码后的只有";"的行
		ln=lns.get(end+1);
		if (begin == -1 || end == -1)
			throw LOG.err(Msgs.notFoundFldText,bStr);
		StringBuilder out = new StringBuilder();
		for ( i = 0; i <= begin; i++)
			out.append(lns.get(i));
		out.append(buf.toString());
		for (i = end; i < lns.size(); i++)
			out.append(lns.get(i));

		FileTools.writeStr(file, out.toString());
		System.out.println("以上为更新前的源代码, 现源文件[" + file + "]已修改, 请按【F5】键同步显示内容!");
	}

}
