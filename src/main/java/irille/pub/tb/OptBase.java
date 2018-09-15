//Created on 2005-9-27
package irille.pub.tb;

import irille.pub.LockBase;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.Infs.IOpt;
import irille.pub.tb.Infs.IOptLine;

import java.util.Hashtable;
import java.util.Vector;


/**
 * Title: 选项类型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
@SuppressWarnings({ "serial", "rawtypes" })
public abstract class OptBase<T extends OptBase> extends LockBase implements IOpt {
	private static final Log LOG = new Log(OptBase.class);
	protected transient Vector<IOptLine> _optLines = new Vector<IOptLine>();
	protected transient Hashtable<String, IOptLine> _optLinesMap = new Hashtable<String, IOptLine>();
	private String _code;
	private String _name;
	private boolean isStr = false;

	public OptBase(String code, String name) {
		_code = code;
		_name = name;
	}

	public String getCode() {
		return _code;
	}

	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}

	/**
	 * 取类型，可以为：TYPE_OPT，TYPE_OPT_CUST，TYPE_TABLE
	 * 
	 * @return
	 */
	public abstract TYPE getType();

	/**
	 * 取optLine
	 * 
	 * @return optLine
	 */
	public IOptLine get(int value) {
		return get("" + value);
	}

	/**
	 * 取optLine
	 * @return optLine
	 */
	public IOptLine get(String value) {
		IOptLine line = _optLinesMap.get(value);
		if (line == null)
			throw LOG.err("optLineNotFound", "选项[" + getName() + "]值为[{0}]的选项行没找到", value);
		return line;
	}
	
	public IOptLine chk(String value) {
		IOptLine line = _optLinesMap.get(value);
		return line;
	}

	/**
	 * 取明细数量
	 * 
	 * @return 结果
	 */
	public int size() {
		return _optLines.size();
	}

	/**
	 * 取明细行
	 * 
	 * @return 结果
	 */
	public IOptLine[] getLines() {
		IOptLine[] opts = new IOptLine[_optLines.size()];
		_optLines.toArray(opts);
		return opts;
	}

	/**
	 * 将值数据转换成选项行
	 * 
	 * @param values
	 * @return
	 */
	public IOptLine[] getLines(Object... values) {
		IOptLine[] opts = new IOptLine[values.length];
		int i = 0;
		for (Object val : values) {
			opts[i++] = get(val.toString());
		}
		return opts;
	}

	public T add(Object key, String name, String rem, String shortcutKey, boolean enable) {
		if (key.getClass().equals(String.class))
			setStr(true);
		add(new OptLine(this, key.toString(), name, rem, shortcutKey, enable));
		return (T) this;
	}

	public T add(Object key, String name, String rem) {
		if (key.getClass().equals(String.class))
			setStr(true);
		add(new OptLine(this, key.toString(), name, rem, null, true));
		return (T) this;
	}

	public T add(Object key, String name) {
		if (key.getClass().equals(String.class))
			setStr(true);
		add(new OptLine(this, key.toString(), name, null, null, true));
		return (T) this;
	}

	/**
	 * 增加行
	 * 
	 * @param line
	 *          行
	 * @return 增加的行
	 */
	public synchronized IOptLine add(IOptLine line) {
		assertUnlocked();
		try {
			assertNotIn(_name, line.getKey());
		} catch (Exception e) {
			throw LOG.err("valueDup", "选项[{0}]的值[{1}]重复", getCode(), line.getKey());
		}
		_optLines.addElement(line);
		_optLinesMap.put(line.getKey(), line);
		return line;
	}

	/**
	 * 取明细行
	 * 
	 * @return 结果
	 */
	public Vector<IOptLine> getLineVector() {
		Vector v = new Vector(_optLines.size());
		for (int i = 0; i < _optLines.size(); i++)
			v.addElement(_optLines.elementAt(i));
		return v;
	}

	/**
	 * 确认值在选项行的所有列表中
	 * 
	 * @param fld
	 *          字段，用于出错时显示名称
	 * @param lineOrValues
	 *          值
	 */
	public void assertValid(Fld fld, Fld... lineOrValues) {
		assertIn(fld.getName(), lineOrValues);
	}

	/**
	 * 确认值在选项行的所有列表中
	 * 
	 * @param fieldName
	 *          字段，用于出错时显示名称
	 * @param value
	 *          值
	 */
	public void assertValid(String fieldName, Object value) {
		assertIn(fieldName, value);
	}

	/**
	 * 确认值在选项行列表中
	 * 
	 * @param fieldName
	 *          字段，用于出错时显示名称
	 * @param value
	 *          值
	 * @param lineOrValues
	 *          选项行列表
	 */
	public void assertIn(String fieldName, Object value, Object... lineOrValues) {
		if (isIn(value, lineOrValues))
			return;
		throw LOG.err("optValueNotValid", "选项[{0}]的值[{1}]无效!", fieldName, value);
	}

	/**
	 * 确认值不在选项行列表中，并且有效
	 * 
	 * @param fieldName
	 *          字段，用于出错时显示名称
	 * @param value
	 *          值
	 * @param lines
	 *          选项行列表,
	 */
	public void assertNotIn(String fieldName, Object value, Object... lineOrValues) {
		if (!isIn(value, lineOrValues))
			return;
		throw LOG.err("optValueNotValid", "选项[{0}]的值[{1}]无效!", fieldName, value);
	}

	/**
	 * 判断值在选项行列表中
	 * 
	 * @param value
	 *          值
	 * @param lineOrValues
	 *          选项行列表, 为空表示从所有包括的行中检查
	 */
	public boolean isIn(Object value, Object... lineOrValues) {
		if (lineOrValues == null || lineOrValues.length == 0) {
			IOptLine[] ls = getLines();
			for (int i = 0; i < ls.length; i++)
				if (ls[i].getKey().equals(value.toString()))
					return true;
		} else {
			for (int i = 0; i < lineOrValues.length; i++) {
				if (lineOrValues[i].toString().equals(value.toString()))
					return true;
			}
		}
		return false;
	}

	/**
	 * 是否检查值在列表中
	 * 
	 * @return
	 */
	public boolean isInCheck() {
		return true;
	}

	/**
	 * 将选项列表的名称合并成以“，”分隔的字符串
	 * 
	 * @param lines
	 *          选项行列表
	 * @return 结果
	 */
	public static String namesToString(IOptLine... lines) {
		StringBuffer buf = new StringBuffer();
		if (lines.length == 0)
			return "";
		for (int i = 0; i < lines.length; i++) {
			buf.append("," + lines[i].getName());
		}
		return buf.toString().substring(1);
	}

	public void removeLines() {
		assertUnlocked();
		_optLines.removeAllElements();
	}

	/**
	 * 克隆新对象
	 * 
	 * @return 结果
	 */
	@SuppressWarnings({ "unchecked" })
  public T cloneNew() {
		try {
			T opt = (T) clone();
			if (_optLines != null)
				opt._optLines = (Vector) _optLines.clone();
			if (_optLinesMap != null)
				opt._optLinesMap = (Hashtable) _optLinesMap.clone();
			return (T) opt;
		} catch (Exception e) {
			throw LOG.err(e, "clone", "克隆[{0}]类型的对象出错!", getClass());
		}
	}

	/**
	 * 列出明细，以“、”分隔
	 * 
	 * @return 结果
	 */
	public String listToString() {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (; i < _optLines.size(); i++) {
			buf.append("、" + ((IOptLine) _optLines.elementAt(i)).getName());
		}
		if (i == 0)
			return "";
		return buf.toString().substring(1);
	}
	/**
	 * 返回显示的宽度，取最长的明细行的长度
	 * @return
	 */
	public short getWidth() {
		int width=0;
		int w;
		for (IOptLine line: _optLines) {
			w=Str.len(line.getName());
			if(width<w)
				width=w;
		}
		return (short)width;
	}

	public boolean isStr() {
  	return isStr;
  }

	public void setStr(boolean isStr) {
  	this.isStr = isStr;
  }
	
}
