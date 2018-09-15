//Created on 2005-10-24
package irille.pub;

import java.io.Serializable;

/**
 * Title: 要锁定的类的基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class LockBase<THIS extends LockBase>  implements Cloneable,Serializable {
	private static final Log LOG= new Log(LockBase.class);
	protected boolean _locked = false;

	public THIS lock() {
		_locked=true;
		return (THIS)this;
	}

	/**
	 * 取锁状态
	 * @return 结果
	 */
	public final boolean isLocked() {
		return _locked;
	}

	/**
	 * 确认未锁
	 *
	 */
public final void assertUnlocked() {
		if(_locked)
		throw LOG.err("assertUnlocked","类[{0}]的对象已加锁，不能变更其内容!", getClass().getName());
	}}
