//Created on 2005-11-15
package irille.pub;

import irille.pub.PubInfs.IMsg;

import java.util.Vector;

/**
 * Title: 文本文件操作, 对文本文件中由指定文件括起来的行进行操作<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class FileText<T extends FileText> implements IPubVars {
	private static final Log LOG = new Log(FileText.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFoundStr("文件[{0}]没有到包括字符[{1}]的行！"),
		javaLastLine("文件[{0}]的最后一行不是“}”, 请格式化一下代码！"),
		fileChanged("文件[{0}]已修改, 请按【F5】键同步显示内容!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on


	private Vector<String> _lines;
	private String _fileName;
	private String _beginMsg = ">>>以下是自动产生的源代码行--{0}--请保留此行用于识别>>>";
	private String _endMsg = "<<<以上是自动产生的源代码行--{0}--请保留此行用于识别<<<";

	/**
	 *
	 * @param fileName 要操作的文件名
	 */
	public FileText(String fileName) {
		_fileName = fileName;
		_lines = FileTools.readToBuf(_fileName);
	}

	/**
	 * 
	 * @param clazz 类，通过类找到源代码文件
	 */
	public FileText(Class clazz) {
		this(FileTools.tranSrcFileDir(clazz));
	}
	/**
	 * 取文本段，不包含头尾的标识文本行
	 * @param name 文本段的名称
	 * @return
	 */
	public String getMiddleText(String name) {
		StringBuilder buf=new StringBuilder();
		int end=findEnd(name);
		for(int i=findBegin(name)+1;i<end;i++) {
			buf.append(_lines.get(i));
		}
		return buf.toString();
	}
	/**
	 * 取文本段，包含头尾的标识文本行
	 * @param name 文本段的名称
	 * @return
	 */
	public String getText(String name) {
		StringBuilder buf=new StringBuilder();
		int end=findEnd(name);
		for(int i=findBegin(name);i<=end;i++) {
			buf.append(_lines.get(i));
		}
		return buf.toString();
	}
	/**
	 * 替换指定的文本段为新内容
	 * @param name 文本段的名称
	 * @param text 内容
	 */
	public void replace(String name, String text) {
		replace(findBegin(name)+1,findEnd(name)-1,text);
	}

	public void replace(int beginLine, int endLine, String text) {
		for(int i=endLine;i>=beginLine;i--) {
			_lines.remove(i);
		}
		_lines.add(beginLine,text);		
	}

	public void del(String name) {
		del(findBegin(name),findEnd(name));
	}
	public void del(int beginLine, int endLine) {
		for(int i=endLine;i>=beginLine;i--) {
			_lines.remove(i);
		}
	}
	
	/**
	 * 保存到磁盘
	 */
	public void save() {
		StringBuilder buf=new StringBuilder();
		for(int i=0;i<_lines.size();i++) {
			buf.append(_lines.get(i));
		}
		FileTools.writeStr(_fileName, buf.toString());
		LOG.info(Msgs.fileChanged,_fileName);

	}
	
	public T setBeginEndMsg(String beginMsg, String endMsg) {
		_beginMsg = beginMsg;
		_endMsg = endMsg;
		return (T) this;
	}

	public int findBegin(String name) {
		return find(getBeginMsg(name));
	}
	public int findEnd(String name) {
		return find(getEndMsg(name));
	}
	
	public int findJavaLastLine() {
		String ln;
		for(int i=_lines.size()-1;i>=0;i--) {
			ln=Str.trim(_lines.get(i));
			if(ln.length()==0)
				continue;
			if(ln.equals("}"))
				return i;
			throw LOG.err(Msgs.javaLastLine,_fileName);
		}
		throw LOG.err(Msgs.javaLastLine,_fileName);
	}
	
	/**
	 * 
	 * @param name
	 * @return 没找到返回-1
	 */
	public int findCheckBegin(String name) {
		return findCheck(getBeginMsg(name));
	}
	/**
	 * 
	 * @param name
	 * @return 没找到返回-1
	 */
	public int findCheckEnd(String name) {
		return findCheck(getEndMsg(name));
	}
	
	public String getBeginMsg(String name) {
		return Str.format(_beginMsg, name);
	}
	
	public String getEndMsg(String name) {
		return Str.format(_endMsg, name);
	}
	
	public int find(String str) {
		int i=findCheck(str);
		if(i<0)
			throw LOG.err(Msgs.notFoundStr, _fileName,str);
		return i;
	}
	/**
	 * 查找包含指定行串的行, 没找到则返回-1
	 * @param str
	 * @return
	 */
	public int findCheck(String str) {
		int size = _lines.size();
		String ln;
		for (int i = 0; i < size; i++) {
			ln = _lines.get(i);
			if (ln.indexOf(str) >= 0) { // 找自动产生代码的起始位置
				return i;
			}
		}
		return -1;
	}

	/**
	 * @return the lines
	 */
	public Vector<String> getLines() {
		return _lines;
	}
}
