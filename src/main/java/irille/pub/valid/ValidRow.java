//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.tb.Fld;

/**
 * Title: Tb校验器<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class ValidRow extends Valid {
	private static final Log LOG = new Log(ValidRow.class);
	public static final ValidRow DEFAULT = new ValidRow();

	/**
	 * 数据转换
	 * @param clazz 目标类
	 * @param value 值
	 * @return 转换后的值
	 */
	// public Object tran(Class clazz, Object value) {
	// if (!clazz.equals(TbRow.class))
	// throw LOG.err("tranClass", "数据类型转换错误:[{0}]", clazz);
	// return value;
	// }

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
		return obj1 == obj2;
	}

	/**
	 * 比较对象大小, null值最小
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 出错
	 */
	public int compare(Object obj1, Object obj2) {
		throw LOG.err("tbCanNotComp", "行对象不可比较");
	}

	public Object tran(Class clazz, Object value) {
		throw LOG.err("tbCanNotTran", "行对象不可转换");
	}
}
