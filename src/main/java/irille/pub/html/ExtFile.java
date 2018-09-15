package irille.pub.html;

import irille.pub.Exp;
import irille.pub.FileTools;
import irille.pub.ext.Ext;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

import java.util.Hashtable;

/**
 * 产生Ext2 JS文件的基类
 * 
 * @author whx
 * 
 */
public abstract class ExtFile<T extends ExtFile> extends ExtList<T> {

	/**
	 * 构造方法
	 */
	public ExtFile() {
		super(null);
	}

	/**
	 * 初始化
	 * 调用initAttrs()
	 * @return
	 */
	public T init() {
		initAttrs();
		return (T) this;
	}

	/**
	 * 初始化属性
	 */
	public void initAttrs() {
	}

	/**
	 * 取Tb对象包名的最后一段
	 * @param tb 
	 * @return
	 */
	public static String getTbPackage(TbBase tb) {
		return Ext.getPag((Tb) tb);
	}

	/**
	 * 取Tb对角的类名的最后一段
	 * @param tb
	 * @return
	 */
	public static String getTbClazz(TbBase tb) {
		return Ext.getClazz((Tb) tb);
	}


	/**
	 * @return the fileName
	 */
	public abstract String getFileName();

	/**
	 * 取JS定义行中的文件名
	 * @return
	 */
	public abstract String getDefineName();

	/**
	 * 输出JS代码
	 * @return
	 */
	public String out() {
		StringBuilder buf = new StringBuilder();
		out(0, buf);
		return buf.toString();
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.ExtList#out(int, java.lang.StringBuilder)
	 */
	@Override
	public void out(int tabs, StringBuilder buf) {
		buf.append("Ext.define('" + getDefineName() + "',");
		super.out(tabs, buf);
		buf.append(");");
	}

	/**
	 * 建立文件
	 * @return
	 */
	public T crtFile() {
		System.out.print("正在建立文件：" + getFileName() + "......");
		FileTools.writeStr(getFileName(), out());
		System.out.println("OK!");
		return (T) this;
	}

	/**
	 * 取指定文件的备份文件名
	 * @param file
	 * @return
	 */
	public static final String getBackupFile(String file) {
		return file.substring(0, file.length() - 3) + "BAK.js";
	}

	/**
	 * 备份所有文件，不覆盖已存在的文件
	 * @return
	 */
	public final T backupFiles() {
		return backupFiles(false);
	}
	/**
	 * 备份文件
	 * @param over 覆盖标志，为true则覆盖已存在的文件
	 * @return
	 */
	public final T backupFiles(boolean over) {
		try {
			FileTools.copy(getFileName(), getBackupFile(getFileName()), over);
		} catch (Exp e) {
			e.printLastMessage();
		}
		return (T) this;
	}

	/**
	 * 比较文件并忽略空白字符
	 * @return
	 */
	public final boolean cmpFileIgnoreBlank() {
		return FileTools.cmpFileIgnoreBlank(getFileName(), getBackupFile(getFileName()));
	}
}
