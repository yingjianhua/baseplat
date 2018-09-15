//Created on 2005-9-17
package irille.pub;


/**
 * 
 * Title: 值确认<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class Assert {
	private static final Log LOG= new Log(Assert.class);
	/**
	 * 确认不为空
	 * @param objMessage 对象描述
	 * @param obj 对象 
	 * @throws Exp ERR_IS_NULL
	 */
	public static void notNull(String objMessage, Object obj) throws Exp {
		if (obj != null)
			return;
		throw LOG.err("isNull","[{0}]不可以为空!", objMessage);
	}

	/**
	 * 确认值不等，可以对null进行比较
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @throws ERR_NOT_EQUALS
	 */
	public static void notEquals(Object obj1, Object obj2)  throws Exp {
		if (Obj.equals(obj1, obj2))
			throw LOG.err("equals","两个对象的值相等出错[{0}][{1}]!", obj1, obj2);
	}

	/**
	 * 确认值相等，可以对null进行比较
	 * @param obj1 对象1
	 * @param obj2 对象2
	 * @throws ERR_EQUALS
	 */
	public static void equals(Object obj1, Object obj2)  throws Exp {
		if (!Obj.equals(obj1, obj2)) {
			throw LOG.err("notEquals","对象值不相等出错[{0}][{1}]!", obj1, obj2);
		}
	}
}
