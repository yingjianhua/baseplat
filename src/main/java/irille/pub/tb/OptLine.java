package irille.pub.tb;

import irille.pub.tb.Infs.IOpt;
import irille.pub.tb.Infs.IOptLine;

/**
 * Title: 明细行<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class OptLine implements IOptLine {
	private OptBase _opt;
	private String _key;
	private String _name;
	private boolean _enable; // 启用标志
	private String _shortcutKey; // 快捷键(null)
	private String _rem;

	public OptLine(OptBase opt, String key, String name, String rem, String shortcutKey,
	    boolean enable) {
		_opt = opt;
		_key = key;
		_name = name;
		_shortcutKey = shortcutKey;
		_enable = enable;
		_rem = rem;
	}

	/**
	 * 取选项
	 * 
	 * @return 结果
	 */
	public IOpt getOpt() {
		return _opt;
	}

	/**
	 * 取key
	 * 
	 * @return key
	 */
	public int getKeyInt() {
		return Integer.parseInt(_key);
	}

	/**
	 * 取key
	 * 
	 * @return key
	 */
	public byte getKeyByte() {
		return Byte.parseByte(_key);
	}

	/**
	 * 取key
	 * 
	 * @return key
	 */
	public String getKey() {
		return _key;
	}

	public char getKeyChar() {
		return (char) getKeyByte();
	}

	/**
	 * 取name
	 * 
	 * @return name
	 */
	public String getName() {
		return _name;
	}

	/**
	 * 取enable
	 * 
	 * @return enable
	 */
	public boolean isEnable() {
		return _enable;
	}

	/**
	 * 取shortcutKey
	 * 
	 * @return shortcutKey
	 */
	public String getShortcutKey() {
		return _shortcutKey == null ? "" : _shortcutKey;
	}

	/**
	 * 比较对象
	 * 
	 * @param key
	 *          值
	 * @return 结果
	 */
	public boolean equals(int key) {
		return equals("" + key);
	}

	/**
	 * 比较对象
	 * 
	 * @param key
	 *          值
	 * @return 结果
	 */
	public boolean equals(String key) {
		return _key.equals(key);
	}

	/**
	 * key的值
	 */
	public String toString() {
		return _key;
	}

	/**
	 * 取hashCode
	 * 
	 * @return 结果
	 */
	public int hashCode() {
		return _key.hashCode();
	}

	public String getRem() {
		return _rem == null ? "" : _shortcutKey;
	}
}
