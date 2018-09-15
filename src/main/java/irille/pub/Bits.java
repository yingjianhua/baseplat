//Created on 2005-5-14
package irille.pub;

import java.io.Serializable;
import java.util.BitSet;

/**
 * <p>Title: 位处理类</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: IRILLE</p>
 * @version 1.0
 */
public class Bits extends LockBase implements Cloneable, Serializable {
	private static final Log LOG = new Log(Bits.class);
	private static final long serialVersionUID = 1L;
	public static final Bits ALL_TRUE = new Bits(true);
	public static final Bits ALL_FALSE = new Bits(false);
	static {
		ALL_TRUE.lock();
		ALL_FALSE.lock();
	}
	private BitSet _bit = new BitSet();
	private boolean _default;
	/**
	 * 构造方法
	 * @param def 缺省值
	 */
	private Bits(boolean def) {
		_default = def;
	}

	/**
	 * 取缺省值
	 * @return 结果
	 */
	public boolean getDefault() {
		return _default;
	}

	/**
	 * 取大小
	 * @return 结果
	 */
	public int size() {
		return _bit.size();
	}
	
	/**
	 * 是否全部为假
	 * @return 结果
	 */
	public boolean isAllFalse() {
		for (int i = 0; i < _bit.size(); i++) {
			if(get(i))
				return false;
		}
		return true;
	}
	
	/**
	 * 是否全部为真
	 * @return 结果
	 */
	public boolean isAllTrue() {
		for (int i = 0; i < _bit.size(); i++) {
			if(!get(i))
				return false;
		}
		return true;
	}

	/**
	 * 取缺省值为true的Bits
	 * @return 结果
	 */
	public static Bits newTrue() {
		return new Bits(true);
	}

	/**
	 * 新建全部取反值的Bits
	 * @return 结果
	 */
	public Bits newNot() {
		Bits bits = new Bits(!_default);
		for (int i = 0; i < _bit.size(); i++)
			bits.set(i, !get(i));
		return bits;
	}

	/**
	 * 取缺省值为true的Bits, 并将数组中指定ID的元素置为false
	 * @param ids 下标数组
	 * @throws 出错
	 */
	public static Bits newTrue(int[] ids) {
		Bits bits = new Bits(true);
		try {
			bits.clean(ids);
		} catch (Exception e) {
		}
		return bits;
	}

	/**
	 * 取缺省值为true的Bits, 将数组中指定ID的元素置为false, 并锁定对象
	 * @param ids 下标数组
	 * @return 结果
	 */
	public static Bits newTrueAndLock(int[] ids) {
		Bits bits= newTrue(ids);
		bits.lock();
		return bits;
	}

	/**
	 * 取缺省值为false的Bits
	 * @return 结果
	 */
	public static Bits newFalse() {
		return new Bits(false);
	}

	/**
	 * 取缺省值为false的Bits, 并将数组中指定ID的元素置为true
	 * @param ids 下标数组
	 * @return 结果
	 */
	public static Bits newFalse(int[] ids) {
		Bits bits = new Bits(false);
		try {
			bits.set(ids);
		} catch (Exception e) {
		}
		return bits;
	}

	/**
	 * 取缺省值为false的Bits, 将数组中指定ID的元素置为true, 并锁定对象
	 * @param ids 下标数组
	 * @return 结果
	 */
	public static Bits newFalseAndLock(int[] ids) {
		Bits bits= newFalse(ids);
		bits.lock();
		return bits;
	}

	/**
	 * 位或产生新对象
	 * @param bits Bits对象
	 * @return 新对象
	 */
	public Bits newOr(Bits bits) {
		Bits bitsNew = new Bits(_default || bits._default);
		int size = bits._bit.size();
		if (size < _bit.size())
			size = _bit.size();
		for (int i = size - 1; i >= 0; i--)
			bitsNew.set(i, get(i) || bits.get(i));
		return bitsNew;
	}

	/**
	 * 位减，即true-true=false, true-false=true, false-*=false
	 * @param bits Bits对象
	 * @return 新对象
	 */
	public Bits newSub(Bits bits) {
		Bits bitsNew = new Bits(bits._default ? false : _default);
		int size = bits._bit.size();
		if (size < _bit.size())
			size = _bit.size();
		for (int i = size - 1; i >= 0; i--)
			bitsNew.set(i, bits.get(i) ? false : get(i));
		return bitsNew;
	}

	/**
	 * 位与产生新对象
	 * @param bits Bits对象
	 * @return 新对象
	 */
	public Bits newAnd(Bits bits) {
		Bits bitsNew = new Bits(_default && bits._default);
		int size = bits._bit.size();
		if (size < _bit.size())
			size = _bit.size();
		for (int i = size - 1; i >= 0; i--)
			bitsNew.set(i, get(i) && bits.get(i));
		return bitsNew;
	}

	/**
	 * 清除所有已设置的位信息
	 * @return 本对象
	 * @throws 出错
	 */
	public Bits clear() {
		assertUnlocked();
		_bit = new BitSet();
		return this;
	}

	/**
	 * 取指定位的状态
	 * @param id 下标
	 * @return 结果
	 */
	public boolean get(int id) {
		return _default ? !_bit.get(id) : _bit.get(id);
	}

	/**
	 * 置指定的Id值为true
	 * @param id 下标
	 * @return 本对象
	 * @throws 出错
	 */
	public Bits set(int id) {
//		assertUnlocked();
		if (_default)
			_bit.clear(id);
		else
			_bit.set(id);
		return this;
	}

	/**
	 * 置指定的Id值为newValue
	 * @param id 下标
	 * @param newValue 新值
	 * @return 本对象
	 * @throws 出错
	 */
	public Bits set(int id, boolean newValue) {
		if (newValue)
			return set(id);
		return clean(id);
	}

	/**
	 * 置指定的Id值为false
	 * @param id 下标
	 * @return 本对象
	 * @throws 出错
	 */
	public Bits clean(int id) {
//		assertUnlocked();
		if (!_default)
			_bit.clear(id);
		else
			_bit.set(id);
		return this;
	}

	/**
	 * 置ids中所有指定下标对应位为false
	 * @param ids 下标数组
	 * @return 本对象
	 * @throws 出错
	 */
	public Bits clean(int[] ids) {
		for (int i = 0; i < ids.length; i++) {
			clean(ids[i]);
		}
		return this;
	}

	/**
	 * 置ids中所有指定下标对应位为true
	 * @param ids 下标数组
	 * @return 本对象

	 */
	public Bits set(int[] ids) {
		for (int i = 0; i < ids.length; i++) {
			set(ids[i]);
		}
		return this;
	}

	/**
	 * 克隆对象, 返回结果的lock标志为false
	 * @throws 出错
	 */
	public Bits cloneBits() {
		Bits bits;
		try {
			bits = (Bits) clone();
		} catch (Exception e) {
			throw LOG.errClone(e, Bits.class);
		}
		bits._bit = (BitSet) bits._bit.clone();
		bits._locked = false;
		return bits;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for (; i < _bit.size(); i++)
			if (get(i) != _default)
				buf.append("," + i + !_default);
		String str = buf.toString();
		if (str.length() > 0)
			return "Default value" + _default + ":" + buf.toString().substring(1);
		return "Default value" + _default + ":";
	}

	/**
	 * 相等比较
	 */
	public boolean equals(Object obj) {
		if (!(obj instanceof Bits))
			return false;
		Bits bits = (Bits) obj;
		if (_default != bits._default)
			return false;
		return _bit.equals(bits._bit);
	}
}
