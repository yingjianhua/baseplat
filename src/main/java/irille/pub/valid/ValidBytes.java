//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.tb.Fld;

/**
 * Title:Blob校验器<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class ValidBytes extends Valid {
	private static final Log LOG = new Log(ValidBytes.class);
	public static final ValidBytes DEFAULT = new ValidBytes();

	/**
	 * 数据转换
	 * @param clazz 目标类
	 * @param value 值
	 * @return 转换后的值
	 */
	public Object tran(Class clazz, Object value) {
		return value;
	}

	/**
	 * 数据有效性检查
	 * @param field 字段
	 * @param value 值
	 */
	public void check(Fld field, Object value) {
		checkNullFlag(field, value);
	}

	/**
	 * 对象是否相等
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 结果
	 */
	public boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		return obj1.toString().equals(obj2.toString());
	}

	/**
	 * 比较对象大小, null值最小
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 出错
	 */
	public int compare(Object obj1, Object obj2) {
		throw LOG.err("blobCanNotComp", "流对象不可比较");
	}
}
