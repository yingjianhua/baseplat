//Created on 2006-2-25
package irille.pub.bean;

import irille.pub.Log;

import java.io.Serializable;

/**
 * Title: 主表的模型基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2006<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class BeanMain<BEAN extends BeanMain, TYPE extends Serializable> extends
    Bean<BEAN, TYPE> {
	private static final Log LOG = new Log(BeanMain.class);

	/**
	 * 取主键的字符串
	 */
	@Override
	public Serializable[] pkeyValues() {
		return new Serializable[] { getPkey() };
	}

	@Override
	public String pkeysToString(Serializable... pkeys) {
		String msg = "";
		for (Serializable line : pkeys)
			msg += "、" + line.toString();
		return gtTB().get("pkey").getName() + "=" + msg.substring(1);
	}

	/**
	 * 设定主键值的抽象方法
	 * @param value 主键值
	 */
	public abstract void setPkey(TYPE value);

	@Override
	public String msg() {
		return "主键为[" + getPkey() + "]";
	}

	/**
	 * 比较相等
	 * 
	 * @param o
	 *          对象
	 * @return 结果
	 */
	public boolean equals(Object o) {
		BeanMain model = (BeanMain) o;
		if (model.getPkey() == null || getPkey() == null)
			return false;
		return getPkey().equals(model.getPkey());
	}
	
}
