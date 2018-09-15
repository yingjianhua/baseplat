//Created on 2005-11-21
package irille.pub.valid;

import irille.pub.Exp;
import irille.pub.Log;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Date;


/**
 * 
 * Title: Criterion 表达式项<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class Crit implements Serializable {
	private static final Log LOG = new Log(Crit.class);
	protected static final String FORMAT2 = "( {0} {1} )";
	protected static final String FORMAT3 = "( {0} {1} {2} )";

	/**
	 * 计算结果
	 * 
	 * @param bean
	 *          对象的Object
	 * @return 结果
	 */
	public abstract Object compute(Object bean);

	/**
	 * 格式化串
	 * 
	 * @param format
	 *          格式化字符串
	 * @param objs
	 *          参数
	 * @return 结果
	 */
	protected String format(String format, Object[] objs) {
		return new MessageFormat(format).format(objs);
	}

	/**
	 * 转化为布尔型
	 * 
	 * @param bean
	 *          对象的Object
	 * @param obj
	 *          表达式项
	 * @return 结果
	 */
	protected boolean toBoolean(Object bean, Object obj) {
		try {
			return Boolean.getBoolean(obj.toString());
		} catch (Exception e) {
			throw LOG.err(e, "valudNotIsBoolean", "[{0}]不是布尔型值!", obj.toString());
		}

	}

	/**
	 * 转化为布尔型
	 * 
	 * @param bean
	 *          对象的Object
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	protected Boolean toBoolean(Object bean, Crit crit) {
		String obj = crit.compute(bean).toString();
		try {
			return Boolean.valueOf(obj);
		} catch (Exception e) {
			throw LOG.err(e, "notIsBoolean", "[{0}]的结果[{1}]不是布尔型值!", crit.toString(), obj);
		}
	}

	/**
	 * 转化为日期型
	 * 
	 * @param bean
	 *          对象的Object
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	protected Date toDate(Object bean, Crit crit) {
		Object obj = crit.compute(bean);
		try {
			return (Date) ValidDate.DEFAULT.tran(Date.class, obj);
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "notIsDate", "[{0}]的结果[{1}]不是日期型值!", crit.toString(), obj);
		}
	}

	/**
	 * 转化为数值型
	 * 
	 * @param bean
	 *          对象的Object
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	protected BigDecimal toBigDecimal(Object bean, Crit crit) {
		Object obj = crit.compute(bean);
		try {
			return (BigDecimal) ValidNumber.DEFAULT.tran(BigDecimal.class, obj);
		} catch (Exp e) {
			throw e;
		} catch (Exception e) {
			throw LOG.err(e, "notIsBigDecimal", "[{0}]的结果[{1}]不是数值型值!", crit.toString(), obj);
		}
	}
}
