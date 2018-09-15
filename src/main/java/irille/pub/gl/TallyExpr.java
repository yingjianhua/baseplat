/**
 * 
 */
package irille.pub.gl;

import irille.pub.valid.Crit;

import java.math.BigDecimal;

/**
 * @author surface1
 *
 */
public class TallyExpr {
	/**
	 * Title: 加表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Add extends Crit {
		private Crit _crit1;
		private Crit _crit2;

		/**
		 * 构造方法
		 * 
		 * @param crit1
		 *          表达式项1
		 * @param crit2
		 *          表达式项2
		 */
		public Add(Crit crit1, Crit crit2) {
			_crit1 = crit1;
			_crit2 = crit2;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			return toBigDecimal(bean, _crit1).add(toBigDecimal(bean, _crit2));
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "+", _crit2 });
		}
	}
	/**
	 * Title: 减表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Sub extends Crit {
		private Crit _crit1;
		private Crit _crit2;

		/**
		 * 构造方法
		 * 
		 * @param crit1
		 *          表达式项1
		 * @param crit2
		 *          表达式项2
		 */
		public Sub(Crit crit1, Crit crit2) {
			_crit1 = crit1;
			_crit2 = crit2;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			return toBigDecimal(bean, _crit1).subtract(toBigDecimal(bean, _crit2));
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "-", _crit2 });
		}
	}

	/**
	 * Title: 减表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Reg extends Crit {
		private Crit _crit;

		/**
		 * 构造方法
		 * 
		 * @param crit
		 *          表达式项2
		 */
		public Reg(Crit crit) {
			_crit = crit;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			return BigDecimal.ZERO.subtract(toBigDecimal(bean, _crit));
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { "-", _crit });
		}
	}
}
