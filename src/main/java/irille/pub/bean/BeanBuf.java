//Created on 2006-3-14
package irille.pub.bean;

import irille.pub.Log;
import irille.pub.Objs;

import java.io.Serializable;
import java.util.Hashtable;

/**
 * Title: Bean缓冲区<br>
 * Description: <br>
 * Copyright: Copyright (c) 2006<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class BeanBuf {
	private static final Log LOG = new Log(BeanBuf.class);
	public static final Hashtable<Class, Hashtable<Serializable, Bean>> _buffers = new Hashtable();
	public static final String ERR_NOT_FOUND = "缓冲中没有以{0}为键的内容!";
	public static final String IGNORE_CLASS = "SysOrg,SysDept,SysEm,SysUser,SysTemplat,SysCell,SysTable,SysMenuAct,GsUom,GsUomType,";

	//本类待完善，当前缓冲未启用，或者可以找一些现成的代码来使用，

	public static final Bean get(Class clazz, Serializable... pkeys) {
		if (IGNORE_CLASS.contains(clazz.getSimpleName()+",") == false)
			return BeanMain.load(clazz, pkeys);
		Hashtable<Serializable, Bean> hash = _buffers.get(clazz);
		Bean bean;
		if (hash == null) {
			hash = new Hashtable<Serializable, Bean>();
			_buffers.put(clazz, hash);
		} else {
			bean = hash.get(Objs.toString(pkeys, null, ",", null));
			if (bean != null)
				return bean;
		}
		bean = BeanMain.load(clazz, pkeys);
		hash.put(Objs.toString(pkeys, null, ",", null), bean);
		return bean;
	}

	public static final void clear(Class clazz, Serializable... pkeys) {
		Hashtable<Serializable, Bean> hash = _buffers.get(clazz);
		if (hash != null)
			hash.remove(Objs.toString(pkeys, null, ",", null));
	}
	
	public static final void clear(Class beanClass) {
		_buffers.remove(beanClass.getName());
	}

	public static final void clearAll() {
		_buffers.clear();
	}
}
