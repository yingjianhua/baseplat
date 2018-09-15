//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.tb.Fld;

import java.math.BigDecimal;

/**
 * Title: Number校验器<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class ValidNumber extends Valid {
	private static final Log LOG = new Log(ValidNumber.class);
	public static final ValidNumber DEFAULT = new ValidNumber();

	/**
	 * 数据转换
	 * 
	 * @param field
	 *          字段
	 * @param value
	 *          值
	 * @return 转换后的值
	 */
	public Object tran(Fld field, Object value) {
		value = tran(field.getJavaType(), value);
		if (value != null && value instanceof BigDecimal)
			// 精度处理
			return ((BigDecimal) value).setScale(field.getScale(),
					BigDecimal.ROUND_HALF_UP);
		return value;
	}

	/**
	 * 数据转换
	 * 
	 * @param clazz
	 *          目标类
	 * @param value
	 *          值
	 * @return 转换后的值
	 */
	public Object tran(Class clazz, Object value) {
		if (value == null)
			return null;
		String val = value.toString().trim();
		if (val.equals(""))
			return null;
		if (value.getClass().equals(clazz))
			return value;
		try {
			if (clazz.equals(BigDecimal.class)) {
				return new BigDecimal(val.toString());
			}
			if (clazz.equals(Byte.class)) {
				return new Byte(new Double(val.toString()).byteValue());
			}
			if (clazz.equals(Integer.class)) {
				// if (val instanceof Integer)
				// return val;
				return new Integer(new Double(val.toString()).intValue());
			}
			if (clazz.equals(Long.class)) {
				// if (val instanceof Long)
				// return val;
				return new Long(val.toString());
			}
			if (clazz.equals(Short.class)) {
				// if (val instanceof Short)
				// return val;
				return new Short(new Double(val.toString()).shortValue());
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw LOG.err("Err.NotNumber", "非法数据");
		}
		throw LOG.err("tranNumber", "数字转换出错");
	}

	/**
	 * 确认BigDecimal值的范围有效
	 * 
	 * @param negativeZeroPlus
	 *          有效范围, 有三位数组成，分别代表负数、零、正数，每位值可为0或1，1表示有效<br>
	 *          如：110表示非正数，10表示必须为零
	 * @param val
	 *          金额
	 */
	public static boolean check(int negativeZeroPlus, BigDecimal val) {
		if (negativeZeroPlus != 0 && negativeZeroPlus != 1
				&& negativeZeroPlus != 10 && negativeZeroPlus != 11
				&& negativeZeroPlus != 100 && negativeZeroPlus != 101
				&& negativeZeroPlus != 110 && negativeZeroPlus != 111)
			throw LOG.err("scope", "有效范围的值[{0}]无效", negativeZeroPlus);
		byte[] bytes = ("" + (1000 + negativeZeroPlus)).substring(1).getBytes();
		int result;
		result = val.signum();
		return !(result < 0 && bytes[0] != '1' || result == 0 && bytes[1] != '1' || result > 0
				&& bytes[2] != '1');
	}

	/**
	 * 数据有效性检查
	 * 
	 * @param field
	 *          字段
	 * @param value
	 *          值
	 */
	public void check(Fld field, Object value) {
		checkNullFlag(field, value);
	}

	/**
	 * 对象是否相等
	 * 
	 * @param obj1
	 *          对象1
	 * @param obj2
	 *          对象2
	 * @return 结果
	 */
	public boolean equals(Number obj1, Number obj2) {
		return DEFAULT.equals((Object) obj1, (Object) obj2);
	}

	/**
	 * 对象是否相等
	 * 
	 * @param obj1
	 *          对象1
	 * @param obj2
	 *          对象2
	 * @return 结果
	 */
	public boolean equals(Object obj1, Object obj2) {
		if (obj1 == obj2)
			return true;
		if (obj1 == null || obj2 == null)
			return false;
		return compare(obj1, obj2) == 0;
	}

	/**
	 * 比较对象大小, null值最小
	 * 
	 * @param obj1
	 *          对象1
	 * @param obj2
	 *          对象2
	 * @return date1>date2时返回1，date1==date2时返回0，date1<date2时返回-1
	 */
	public int compare(Object obj1, Object obj2) {
		if (obj1 == null) {
			if (obj2 == null)
				return 0;
			return 1;
		}
		if (obj2 == null)
			return -1;
		if (obj1 instanceof BigDecimal)
			return ((BigDecimal) obj1).compareTo(new BigDecimal(obj2.toString()));
		else if (obj1 instanceof Byte || obj1 instanceof Short
				|| obj1 instanceof Integer || obj1 instanceof Long) {
			long l1 = new Long(obj1.toString()).longValue();
			long l2 = new Long(obj2.toString()).longValue();
			if (l1 == l2)
				return 0;
			if (l1 < l2)
				return -1;
			return 1;
		}
		throw LOG.err("numberCom", "数字比较出错");
	}

	public static boolean isNumber(String num) {
		char[] cs = num.toCharArray();
		for (char c : cs)
			if (!Character.isDigit(c))
				return false;
		return true;
	}
}
