//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.Obj;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Hashtable;


/**
 * Title: 数据校验器基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class Valid implements Serializable {
	private static final Log LOG = new Log(Valid.class);

	private static Hashtable _defaultValidation;

	/**
	 * 取类的缺省校验器
	 * @param clazz 类
	 * @return 结果
	 */
	public static final Valid getDefaultValidation(Class clazz) {
		if (_defaultValidation == null) {
			_defaultValidation = new Hashtable();
			_defaultValidation.put(String.class, ValidString.DEFAULT);
			_defaultValidation.put(Byte.class, ValidNumber.DEFAULT);
			_defaultValidation.put(Short.class, ValidNumber.DEFAULT);
			_defaultValidation.put(Integer.class, ValidNumber.DEFAULT);
			_defaultValidation.put(Long.class, ValidNumber.DEFAULT);
			_defaultValidation.put(BigDecimal.class, ValidNumber.DEFAULT);
			_defaultValidation.put(Date.class, ValidDate.DEFAULT);
			_defaultValidation.put(java.sql.Date.class, ValidDate.DEFAULT);
			_defaultValidation.put(Timestamp.class, ValidDate.DEFAULT);
			_defaultValidation.put(Tb.class, ValidTb.DEFAULT);
			_defaultValidation.put(Blob.class, ValidBytes.DEFAULT);
			_defaultValidation.put(Clob.class, ValidBytes.DEFAULT);
		}
		Valid v = (Valid) _defaultValidation.get(clazz);
		if (v != null)
			return v;
		throw LOG.err("clazzDefaultValidataionNotFound", "类[{0}]的缺省较验器没找到!", clazz.getName());
	}

	/**
	 * 数据转换
	 * @param field 字段
	 * @param value 值
	 * @return 转换后的值
	 */
	public Object tran(Fld field, Object value) {
		return tran(field.getJavaType(), value);
	}

	/**
	 * 数据转换
	 * @param clazz 目标类
	 * @param value 值
	 * @return 转换后的值
	 */
	public abstract Object tran(Class clazz, Object value);

	/**
	 * 数据有效性检查
	 * @param field 字段
	 * @param value 值
	 */
	public abstract void check(Fld field, Object value);

	/**
	 * 检查空值状态
	 * @param field 字段
	 * @param value 值
	 */
	public static final void checkNullFlag(Fld field, Object value) {
		if ((Obj.equals(value, "") || value == null) && !field.isNull())
			throw LOG.err("isNull", "[{0}]的值不允许为空!", field.getName());
	}

	/**
	 * 对象是否相等
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return 结果
	 */
	public abstract boolean equals(Object obj1, Object obj2);

	/**
	 * 比较对象大小, null值最小
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @return date1>date2时返回1，date1==date2时返回0，date1<date2时返回-1
	 */
	public abstract int compare(Object obj1, Object obj2);
}
