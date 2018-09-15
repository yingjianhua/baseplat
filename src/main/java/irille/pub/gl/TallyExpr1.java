//Created on 2005-11-19
package irille.pub.gl;

import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Obj;
import irille.pub.tb.Fld;
import irille.pub.valid.Crit;
import irille.pub.valid.ValidDate;
import irille.pub.valid.ValidNumber;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Hashtable;


/**
 * Title: 表达式对象Expression<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class TallyExpr1 implements Serializable {
	private static final Log LOG = new Log(TallyExpr1.class);
	private boolean _isCallGet = true;

	/**
	 * 构造方法, 当变量集合为Hashtable时，isCallGet值将不用，而直接取Hashtable的get(key)值
	 * 
	 * @param isCallGet
	 *          要取的属性是否用getProperty(String propertyCode)方法，否则调用对象的 “get属性名()”方法
	 */
	public TallyExpr1(boolean isCallGet) {
		_isCallGet = isCallGet;
	}

	/**
	 * 构造方法
	 */
	public TallyExpr1() {
	}

	/**
	 * 相等
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit eq(String property, Object obj) {
		return new Equal(new Property(property), new Value(obj), false);
	}

	public Crit eq(Fld field, Object obj) {
		return eq(field.getCode(), obj);
	}

	/**
	 * 相等
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit eq(String property, Crit crit) {
		return new Equal(new Property(property), crit, false);
	}

	public Crit eq(Fld field, Crit crit) {
		return eq(field.getCode(), crit);
	}

	/**
	 * 相等
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit eq(Crit crit, Object obj) {
		return new Equal(crit, new Value(obj), false);
	}

	/**
	 * 相等
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit eqCrit(Crit crit1, Crit crit2) {
		return new Equal(crit1, crit2, false);
	}

	/**
	 * 相等
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit eqProperty(String property1, String property2) {
		return new Equal(new Property(property1), new Property(property2), false);
	}

	public Crit eqProperty(Fld property1, Fld property2) {
		return eqProperty(property1.getCode(), property2.getCode());
	}

	/**
	 * 相等
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit eqValue(Object obj1, Object obj2) {
		return new Equal(new Value(obj1), new Value(obj2), false);
	}

	/**
	 * 不相等
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit ne(String property, Object obj) {
		return new Equal(new Property(property), new Value(obj), true);
	}

	public Crit ne(Fld field, Object obj) {
		return ne(field.getCode(), obj);
	}

	/**
	 * 不相等
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit ne(String property, Crit crit) {
		return new Equal(new Property(property), crit, true);
	}

	public Crit ne(Fld field, Crit cirt) {
		return ne(field.getCode(), cirt);
	}

	/**
	 * 不相等
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit ne(Crit crit, Object obj) {
		return new Equal(crit, new Value(obj), true);
	}

	/**
	 * 不相等
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit neCrit(Crit crit1, Crit crit2) {
		return new Equal(crit1, crit2, true);
	}

	/**
	 * 不相等
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit neProperty(String property1, String property2) {
		return new Equal(new Property(property1), new Property(property2), true);
	}

	public Crit neProperty(Fld property1, Fld property2) {
		return eqProperty(property1.getCode(), property2.getCode());
	}

	/**
	 * 不相等
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit neValue(Object obj1, Object obj2) {
		return new Equal(new Value(obj1), new Value(obj2), true);
	}

	/**
	 * 为null
	 * 
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit isNull(String property) {
		return new IsNull(new Property(property), false);
	}

	public Crit isNull(Fld field) {
		return isNull(field.getCode());
	}

	/**
	 * 为null
	 * 
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit isNull(Object obj) {
		return new IsNull(new Value(obj), false);
	}

	/**
	 * 为null
	 * 
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit isNull(Crit crit) {
		return new IsNull(crit, false);
	}

	/**
	 * 不为null
	 * 
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit notIsNull(String property) {
		return new IsNull(new Property(property), true);
	}

	public Crit notIsNull(Fld field) {
		return notIsNull(field.getCode());
	}

	/**
	 * 不为null
	 * 
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit notIsNull(Object obj) {
		return new IsNull(new Value(obj), true);
	}

	/**
	 * 不为null
	 * 
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit notIsNull(Crit crit) {
		return new IsNull(crit, true);
	}

	/**
	 * 小于
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit lt(String property, Object obj) {
		return new LessThan(new Property(property), new Value(obj), false);
	}

	public Crit lt(Object obj, String property) {
		return new LessThan(new Value(obj), new Property(property), false);
	}

	/**
	 * 小于
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit lt(String property, Crit crit) {
		return new LessThan(new Property(property), crit, false);
	}

	public Crit lt(Crit crit, String property) {
		return new LessThan(crit, new Property(property), false);
	}

	/**
	 * 小于
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit lt(Crit crit, Object obj) {
		return new LessThan(crit, new Value(obj), false);
	}

	public Crit lt(Object obj, Crit crit) {
		return new LessThan(new Value(obj), crit, false);
	}

	/**
	 * 小于
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit ltCrit(Crit crit1, Crit crit2) {
		return new LessThan(crit1, crit2, false);
	}

	/**
	 * 小于
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit ltProperty(String property1, String property2) {
		return new LessThan(new Property(property1), new Property(property2), false);
	}

	/**
	 * 小于
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit ltValue(Object obj1, Object obj2) {
		return new LessThan(new Value(obj1), new Value(obj2), false);
	}

	/**
	 * 大于
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit gt(String property, Object obj) {
		return new LessThan(new Property(property), new Value(obj), true);
	}

	public Crit gt(Object obj, String property) {
		return new LessThan(new Value(obj), new Property(property), true);
	}

	/**
	 * 大于
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit gt(String property, Crit crit) {
		return new LessThan(new Property(property), crit, true);
	}

	public Crit gt(Crit crit, String property) {
		return new LessThan(crit, new Property(property), true);
	}

	/**
	 * 大于
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit gt(Crit crit, Object obj) {
		return new LessThan(crit, new Value(obj), true);
	}

	public Crit gt(Object obj, Crit crit) {
		return new LessThan(new Value(obj), crit, true);
	}

	/**
	 * 大于
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit gtCrit(Crit crit1, Crit crit2) {
		return new LessThan(crit1, crit2, true);
	}

	/**
	 * 大于
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit gtProperty(String property1, String property2) {
		return new LessThan(new Property(property1), new Property(property2), true);
	}

	/**
	 * 大于
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit gtValue(Object obj1, Object obj2) {
		return new LessThan(new Value(obj1), new Value(obj2), true);
	}

	/**
	 * 小于等于
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit le(String property, Object obj) {
		return new LessEqual(new Property(property), new Value(obj), false);
	}

	public Crit le(Object obj, String property) {
		return new LessEqual(new Value(obj), new Property(property), false);
	}

	/**
	 * 小于等于
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit le(String property, Crit crit) {
		return new LessEqual(new Property(property), crit, false);
	}

	public Crit le(Crit crit, String property) {
		return new LessEqual(crit, new Property(property), false);
	}

	/**
	 * 小于等于
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit le(Crit crit, Object obj) {
		return new LessEqual(crit, new Value(obj), false);
	}

	public Crit le(Object obj, Crit crit) {
		return new LessEqual(new Value(obj), crit, false);
	}

	/**
	 * 小于等于
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit leCrit(Crit crit1, Crit crit2) {
		return new LessEqual(crit1, crit2, false);
	}

	/**
	 * 小于等于
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit leProperty(String property1, String property2) {
		return new LessEqual(new Property(property1), new Property(property2),
		    false);
	}

	/**
	 * 小于等于
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit leValue(Object obj1, Object obj2) {
		return new LessEqual(new Value(obj1), new Value(obj2), false);
	}

	/**
	 * 大于等于
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit ge(String property, Object obj) {
		return new LessEqual(new Property(property), new Value(obj), true);
	}

	public Crit ge(Object obj, String property) {
		return new LessEqual(new Value(obj), new Property(property), true);
	}

	/**
	 * 大于等于
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit ge(String property, Crit crit) {
		return new LessEqual(new Property(property), crit, true);
	}

	public Crit ge(Crit crit, String property) {
		return new LessEqual(crit, new Property(property), true);
	}

	/**
	 * 大于等于
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit ge(Crit crit, Object obj) {
		return new LessEqual(crit, new Value(obj), true);
	}

	public Crit ge(Object obj, Crit crit) {
		return new LessEqual(new Value(obj), crit, true);
	}

	/**
	 * 大于等于
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit geCrit(Crit crit1, Crit crit2) {
		return new LessEqual(crit1, crit2, true);
	}

	/**
	 * 大于等于
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit geProperty(String property1, String property2) {
		return new LessEqual(new Property(property1), new Property(property2), true);
	}

	/**
	 * 大于等于
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit geValue(Object obj1, Object obj2) {
		return new LessEqual(new Value(obj1), new Value(obj2), true);
	}

	/**
	 * 类型转换
	 * 
	 * @param clazz
	 *          类型类
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit cast(Class clazz, Object obj) {
		return new Cast(clazz, new Value(obj));
	}

	/**
	 * 类型转换
	 * 
	 * @param clazz
	 *          类型类
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit cast(Class clazz, String property) {
		return new Cast(clazz, new Property(property));
	}

	/**
	 * 类型转换
	 * 
	 * @param clazz
	 *          类型类
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit cast(Class clazz, Crit crit) {
		return new Cast(clazz, crit);
	}

	/**
	 * 字符串连接
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit concat(String property, Object obj) {
		return new Concat(new Property(property), new Value(obj));
	}

	public Crit concat(Object obj, String property) {
		return new Concat(new Value(obj), new Property(property));
	}

	/**
	 * 字符串连接
	 * 
	 * @param property
	 *          属性
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit concat(String property, Crit crit) {
		return new Concat(new Property(property), crit);
	}

	public Crit concat(Crit crit, String property) {
		return new Concat(crit, new Property(property));
	}

	/**
	 * 字符串连接
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit concat(Crit crit, Object obj) {
		return new Concat(crit, new Value(obj));
	}

	public Crit concat(Object obj, Crit crit) {
		return new Concat(new Value(obj), crit);
	}

	/**
	 * 字符串连接
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit concatProperty(String property1, String property2) {
		return new Concat(new Property(property1), new Property(property2));
	}

	/**
	 * 字符串连接
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit concatCrit(Crit crit1, Crit crit2) {
		return new Concat(crit1, crit2);
	}

	/**
	 * 字符串连接
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit concatValue(Object obj1, Object obj2) {
		return new Concat(new Value(obj1), new Value(obj2));
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit between(Object obj, Object obj1, Object obj2) {
		return new Between(new Value(obj), new Value(obj1), new Value(obj2), false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit between(Crit crit, Object obj1, Object obj2) {
		return new Between(crit, new Value(obj1), new Value(obj2), false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit between(String property, Object obj1, Object obj2) {
		return new Between(new Property(property), new Value(obj1),
		    new Value(obj2), false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit between(String property, String property1, String property2) {
		return new Between(new Property(property), new Property(property1),
		    new Property(property2), false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit between(Object obj, String property1, String property2) {
		return new Between(new Value(obj), new Property(property1), new Property(
		    property2), false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit between(Crit crit, String property1, String property2) {
		return new Between(crit, new Property(property1), new Property(property2),
		    false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit between(Crit crit, Crit crit1, Crit crit2) {
		return new Between(crit, crit1, crit2, false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit between(Object obj, Crit crit1, Crit crit2) {
		return new Between(new Value(obj), crit1, crit2, false);
	}

	/**
	 * 在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit between(String property, Crit crit1, Crit crit2) {
		return new Between(new Property(property), crit1, crit2, false);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit notBetween(Object obj, Object obj1, Object obj2) {
		return new Between(new Value(obj), new Value(obj1), new Value(obj2), true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit notBetween(Crit crit, Object obj1, Object obj2) {
		return new Between(crit, new Value(obj1), new Value(obj2), true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param obj1
	 *          前一个对象
	 * @param obj2
	 *          后一个对象
	 * @return obj是否在obj1和obj2之间
	 */
	public Crit notBetween(String property, Object obj1, Object obj2) {
		return new Between(new Property(property), new Value(obj1),
		    new Value(obj2), true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit notBetween(String property, String property1, String property2) {
		return new Between(new Property(property), new Property(property1),
		    new Property(property2), true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit notBetween(Object obj, String property1, String property2) {
		return new Between(new Value(obj), new Property(property1), new Property(
		    property2), true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param property1
	 *          前一个属性
	 * @param property2
	 *          后一个属性
	 * @return property是否在property1和property2之间
	 */
	public Crit notBetween(Crit crit, String property1, String property2) {
		return new Between(crit, new Property(property1), new Property(property2),
		    true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param crit
	 *          表达式项
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit notBetween(Crit crit, Crit crit1, Crit crit2) {
		return new Between(crit, crit1, crit2, true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param obj
	 *          对象
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit notBetween(Object obj, Crit crit1, Crit crit2) {
		return new Between(new Value(obj), crit1, crit2, true);
	}

	/**
	 * 不在指定范围之间
	 * 
	 * @param property
	 *          属性
	 * @param crit1
	 *          前一个表达式项
	 * @param crit2
	 *          后一个表达式项
	 * @return 结果
	 */
	public Crit notBetween(String property, Crit crit1, Crit crit2) {
		return new Between(new Property(property), crit1, crit2, true);
	}

	/**
	 * 在列表之中
	 * 
	 * @param crit
	 *          表达式项
	 * @param crits
	 *          表达式项列表
	 * @return 结果
	 */
	public Crit in(Crit crit, Crit... crits) {
		return new In(crit, crits, false);
	}

	/**
	 * 在列表之中
	 * 
	 * @param crit
	 *          表达式项
	 * @param objs
	 *          对象列表
	 * @return 结果
	 */
	public Crit in(Crit crit, Object... objs) {
		Crit[] crits = new Crit[objs.length];
		for (int i = 0; i < objs.length; i++) {
			crits[i] = new Value(objs[i]);
		}
		return new In(crit, crits, false);
	}

	/**
	 * 在列表之中
	 * 
	 * @param property
	 *          属性
	 * @param crits
	 *          表达式项列表
	 * @return 结果
	 */
	public Crit in(String property, Crit... crits) {
		return new In(new Property(property), crits, false);
	}

	public Crit in(Fld field, Crit... crits) {
		return in(field.getCode(), crits);
	}

	/**
	 * 在列表之中
	 * 
	 * @param property
	 *          属性
	 * @param objs
	 *          对象列表
	 * @return 结果
	 */
	public Crit in(String property, Object... objs) {
		Crit[] crits = new Crit[objs.length];
		for (int i = 0; i < objs.length; i++) {
			crits[i] = new Value(objs[i]);
		}
		return new In(new Property(property), crits, false);
	}

	public Crit in(Fld field, Object... crits) {
		return in(field.getCode(), crits);
	}

	/**
	 * 不在列表之中
	 * 
	 * @param crit
	 *          表达式项
	 * @param crits
	 *          表达式项列表
	 * @return 结果
	 */
	public Crit notIn(Crit crit, Crit... crits) {
		return new In(crit, crits, true);
	}

	/**
	 * 不在列表之中
	 * 
	 * @param crit
	 *          表达式项
	 * @param objs
	 *          对象列表
	 * @return 结果
	 */
	public Crit notIn(Crit crit, Object... objs) {
		Crit[] crits = new Crit[objs.length];
		for (int i = 0; i < objs.length; i++) {
			crits[i] = new Value(objs[i]);
		}
		return new In(crit, crits, true);
	}

	/**
	 * 不在列表之中
	 * 
	 * @param property
	 *          属性
	 * @param crits
	 *          表达式项列表
	 * @return 结果
	 */
	public Crit notIn(String property, Crit... crits) {
		return new In(new Property(property), crits, true);
	}

	public Crit notIn(Fld field, Crit... crits) {
		return notIn(field.getCode(), crits);
	}

	/**
	 * 不在列表之中
	 * 
	 * @param property
	 *          属性
	 * @param objs
	 *          对象列表
	 * @return 结果
	 */
	public Crit notIn(String property, Object... objs) {
		Crit[] crits = new Crit[objs.length];
		for (int i = 0; i < objs.length; i++) {
			crits[i] = new Value(objs[i]);
		}
		return new In(new Property(property), crits, true);
	}

	public Crit notIn(Fld field, Object... crits) {
		return notIn(field.getCode(), crits);
	}

	/**
	 * 非
	 * 
	 * @param crit
	 *          表达式项
	 * @return 结果
	 */
	public Crit not(Crit crit) {
		return new Not(crit);
	}

	/**
	 * 非
	 * 
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit not(String property) {
		return new Not(new Property(property));
	}

	public Crit not(Fld field) {
		return not(field.getCode());
	}

	/**
	 * 非
	 * 
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit not(Object obj) {
		return new Not(new Value(obj));
	}

	/**
	 * 或
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit or(Crit crit, String property) {
		return new Or(crit, new Property(property));
	}

	/**
	 * 或
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit or(Crit crit, Object obj) {
		return new Or(crit, new Value(obj));
	}

	/**
	 * 或
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit or(String property, Object obj) {
		return new Or(new Property(property), new Value(obj));
	}

	public Crit or(Fld field, Object obj) {
		return or(field.getCode(), obj);
	}

	/**
	 * 或
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit orCrit(Crit crit1, Crit crit2) {
		return new Or(crit1, crit2);
	}

	/**
	 * 或
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit orProperty(String property1, String property2) {
		return new Or(new Property(property1), new Property(property2));
	}

	public Crit orProperty(Fld field1, Fld field2) {
		return orProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 或
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit orValue(Object obj1, Object obj2) {
		return new Or(new Value(obj1), new Value(obj2));
	}

	/**
	 * 与
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit and(Crit crit, Object obj) {
		return new And(crit, new Value(obj));
	}

	/**
	 * 与
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit and(Crit crit, String property) {
		return new And(crit, new Property(property));
	}

	/**
	 * 与
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit and(String property, Object obj) {
		return new And(new Property(property), new Value(obj));
	}

	public Crit and(Fld field, Object obj) {
		return and(field.getCode(), obj);
	}

	/**
	 * 与
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit andCrit(Crit crit1, Crit crit2) {
		return new And(crit1, crit2);
	}

	/**
	 * 与
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit andProperty(String property1, String property2) {
		return new And(new Property(property1), new Property(property2));
	}

	public Crit andProperty(Fld field1, Fld field2) {
		return andProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 与
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit andValue(Object obj1, Object obj2) {
		return new And(new Value(obj1), new Value(obj2));
	}

	/**
	 * 加
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit add(Crit crit, Object obj) {
		return new Add(crit, new Value(obj));
	}

	/**
	 * 加
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit add(Crit crit, String property) {
		return new Add(crit, new Property(property));
	}

	/**
	 * 加
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit add(String property, Object obj) {
		return new Add(new Property(property), new Value(obj));
	}

	public Crit add(Fld field, Object obj) {
		return add(field.getCode(), obj);
	}

	/**
	 * 加
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit addCrit(Crit crit1, Crit crit2) {
		return new Add(crit1, crit2);
	}

	/**
	 * 加
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit addProperty(String property1, String property2) {
		return new Add(new Property(property1), new Property(property2));
	}

	public Crit addProperty(Fld field1, Fld field2) {
		return addProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 加
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit addValue(Object obj1, Object obj2) {
		return new Add(new Value(obj1), new Value(obj2));
	}

	/**
	 * 减
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit sub(Crit crit, Object obj) {
		return new Sub(crit, new Value(obj));
	}

	public Crit sub(Object obj, Crit crit) {
		return new Sub(new Value(obj), crit);
	}

	/**
	 * 减
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit sub(Crit crit, String property) {
		return new Sub(crit, new Property(property));
	}

	public Crit sub(String property, Crit crit) {
		return new Sub(new Property(property), crit);
	}

	public Crit sub(Fld field, Crit crit) {
		return sub(field.getCode(), crit);
	}

	/**
	 * 减
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit sub(String property, Object obj) {
		return new Sub(new Property(property), new Value(obj));
	}

	public Crit sub(Fld field, Object obj) {
		return sub(field.getCode(), obj);
	}

	public Crit sub(Object obj, String property) {
		return new Sub(new Value(obj), new Property(property));
	}

	public Crit sub(Object obj, Fld field) {
		return sub(obj, field.getCode());
	}

	/**
	 * 减
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit subCrit(Crit crit1, Crit crit2) {
		return new Sub(crit1, crit2);
	}

	/**
	 * 减
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit subProperty(String property1, String property2) {
		return new Sub(new Property(property1), new Property(property2));
	}

	public Crit subProperty(Fld field1, Fld field2) {
		return subProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 减
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit subValue(Object obj1, Object obj2) {
		return new Sub(new Value(obj1), new Value(obj2));
	}

	/**
	 * 乘
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit multiply(Crit crit, Object obj) {
		return new Multiply(crit, new Value(obj));
	}

	/**
	 * 乘
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit multiply(Crit crit, String property) {
		return new Multiply(crit, new Property(property));
	}

	/**
	 * 乘
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit multiply(String property, Object obj) {
		return new Multiply(new Property(property), new Value(obj));
	}

	public Crit multiply(Fld field, Object obj) {
		return multiply(field.getCode(), obj);
	}

	/**
	 * 乘
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit multiplyCrit(Crit crit1, Crit crit2) {
		return new Multiply(crit1, crit2);
	}

	/**
	 * 乘
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit multiplyProperty(String property1, String property2) {
		return new Multiply(new Property(property1), new Property(property2));
	}

	public Crit multiplyProperty(Fld field1, Fld field2) {
		return multiplyProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 乘
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit multiplyValue(Object obj1, Object obj2) {
		return new Multiply(new Value(obj1), new Value(obj2));
	}

	/**
	 * 除
	 * 
	 * @param crit
	 *          表达式项
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit divide(Crit crit, Object obj) {
		return new Divide(crit, new Value(obj));
	}

	public Crit divide(Object obj, Crit crit) {
		return new Divide(new Value(obj), crit);
	}

	/**
	 * 除
	 * 
	 * @param crit
	 *          表达式项
	 * @param property
	 *          属性
	 * @return 结果
	 */
	public Crit divide(Crit crit, String property) {
		return new Divide(crit, new Property(property));
	}

	public Crit divide(Crit crit, Fld field) {
		return divide(crit, field.getCode());
	}

	public Crit divide(String property, Crit crit) {
		return new Divide(new Property(property), crit);
	}

	public Crit divide(Fld field, Crit crit) {
		return divide(field.getCode(), crit);
	}

	/**
	 * 除
	 * 
	 * @param property
	 *          属性
	 * @param obj
	 *          对象
	 * @return 结果
	 */
	public Crit divide(String property, Object obj) {
		return new Divide(new Property(property), new Value(obj));
	}

	public Crit divide(Object obj, String property) {
		return new Divide(new Value(obj), new Property(property));
	}

	public Crit divide(Object obj, Fld field) {
		return divide(obj, field.getCode());
	}

	/**
	 * 除
	 * 
	 * @param crit1
	 *          表达式项
	 * @param crit2
	 *          表达式项
	 * @return 结果
	 */
	public Crit divideCrit(Crit crit1, Crit crit2) {
		return new Divide(crit1, crit2);
	}

	/**
	 * 除
	 * 
	 * @param property1
	 *          属性
	 * @param property2
	 *          属性
	 * @return 结果
	 */
	public Crit divideProperty(String property1, String property2) {
		return new Divide(new Property(property1), new Property(property2));
	}

	public Crit divideProperty(Fld field1, Fld field2) {
		return divideProperty(field1.getCode(), field2.getCode());
	}

	/**
	 * 除
	 * 
	 * @param obj1
	 *          对象
	 * @param obj2
	 *          对象
	 * @return 结果
	 */
	public Crit divideValue(Object obj1, Object obj2) {
		return new Divide(new Value(obj1), new Value(obj2));
	}

	/**
	 * XXX lt 小于 //完成 gt 大于 //完成 le 小于等于 //完成 ge 大于等于 //完成 cast 类型转换 //完成 concat
	 * 字符串连接 //完成 between 在指定范围之间 //完成 notBetween 不在指定范围之间 //完成 in 在列表之中 //完成
	 * notIn 不在列表之中 //完成 like 匹配 notLike 不匹配 not 非 //完成 or 或 //完成 and 与 //完成 add 加
	 * //完成 sub 减 //完成 multiply 乘 //完成 divide 除 //完成
	 */
	/**
	 * Title: 取属性的接口<br>
	 * Description: irille.pub.View实现本接口<br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public interface CallGet {
		/**
		 * 取属性值
		 * 
		 * @param property
		 *          属性
		 * @return 结果
		 */
		public Object getProperty(String property);
	}

	/**
	 * Title: 或表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Value extends Crit {
		private final Object _value;

		/**
		 * 构造方法
		 * 
		 * @param value
		 *          值
		 */
		public Value(Object value) {
			_value = value;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			return _value;
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return Obj.toString(_value);
		}
	}

	/**
	 * Title: 属性表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public class Property extends Crit {
		private final String _property;

		/**
		 * 构造方法
		 * 
		 * @param property
		 *          属性
		 */
		public Property(String property) {
			_property = property;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			if (bean instanceof Hashtable) {
				Object obj = ((Hashtable) bean).get(_property);
				if (obj == null)
					throw LOG.err("objNotFound", "代码为[{0}]的对象在变量没找到!", _property);
				return obj;
			}
			if (_isCallGet) {
				if (!(bean instanceof CallGet))
					throw LOG.err("notIsCallGetObj", "类[{0}]没有实现[{1}]接口!", bean
					    .getClass().getName(), CallGet.class.getName());
				return ((CallGet) bean).getProperty(_property);
			}
			return ClassTools.getProperty(bean, _property);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return Obj.toString(_property);
		}
	}

	/**
	 * Title: 是否为NULL表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public class IsNull extends Crit {
		private final Crit _crit;
		private final boolean _isNot;

		/**
		 * 构造方法
		 * 
		 * @param crit
		 *          表达式项
		 */
		public IsNull(Crit crit, boolean isNot) {
			_crit = crit;
			_isNot = isNot;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			boolean result = _crit.compute(bean) == null;
			return _isNot ? new Boolean(!result) : new Boolean(result);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return (_isNot ? "not " : "") + "is null " + Obj.toString(_crit);
		}
	}

	/**
	 * Title: 类型转换式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Cast extends Crit {
		private final Crit _crit;
		private final Class _clazz;

		/**
		 * 构造方法
		 * 
		 * @param clazz
		 *          类
		 * @param crit
		 *          表达式项
		 */
		public Cast(Class clazz, Crit crit) {
			_clazz = clazz;
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
			Object obj = _crit.compute(bean);
			if (obj == null)
				return obj;
			if (_clazz.equals(BigDecimal.class))
				return ValidNumber.DEFAULT.tran(_clazz, obj);
			if (Date.class.isAssignableFrom(_clazz))
				return ValidDate.DEFAULT.tran(_clazz, obj);
			if (_clazz.equals(Boolean.class))
				return toBoolean(bean, _crit);
			if (_clazz.equals(String.class))
				return _crit.compute(bean).toString();
			throw LOG.err("cast", "类型为[{0}]的对象不能转换成[{1}]类型!",
			    obj.getClass().getName(), _clazz.getName());
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return "(" + _clazz.getName() + ")" + _crit;
		}
	}

	/**
	 * Title: 或表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Like extends Crit {
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
		public Like(Crit crit1, Crit crit2) {
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
			throw LOG.err(); // XXX代码未写
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "like", _crit2 });
		}
	}

	/**
	 * Title: 或表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Or extends Crit {
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
		public Or(Crit crit1, Crit crit2) {
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
			return toBoolean(bean, _crit1).booleanValue() ? Boolean.TRUE
			    : new Boolean(toBoolean(bean, _crit2).booleanValue());
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "or", _crit2 });
		}
	}

	/**
	 * Title: 与表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class And extends Crit {
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
		public And(Crit crit1, Crit crit2) {
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
			return toBoolean(bean, _crit1).booleanValue() ? new Boolean(toBoolean(
			    bean, _crit2).booleanValue()) : Boolean.FALSE;
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "and", _crit2 });
		}
	}

	/**
	 * Title: 或表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Not extends Crit {
		private Crit _crit;

		/**
		 * 构造方法
		 * 
		 * @param crit
		 *          表达式项
		 */
		public Not(Crit crit) {
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
			return new Boolean(!toBoolean(bean, _crit).booleanValue());
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT2, new Object[] { "!" + _crit });
		}
	}

	/**
	 * Title: 相等表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Equal extends Crit {
		private Crit _crit1;
		private Crit _crit2;
		private boolean _not;

		/**
		 * 构造方法
		 * 
		 * @param crit1
		 *          表达式项1
		 * @param crit2
		 *          表达式项2
		 * @param not
		 *          是否为非
		 */
		public Equal(Crit crit1, Crit crit2, boolean not) {
			_crit1 = crit1;
			_crit2 = crit2;
			_not = not;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			boolean result = Obj.equals(_crit1.compute(bean), _crit2.compute(bean));
			return _not ? new Boolean(!result) : new Boolean(result);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3,
			    new Object[] { _crit1, _not ? "!=" : "==", _crit2 });
		}
	}

	/**
	 * Title: 包含表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class In extends Crit {
		private Crit _crit;
		private Crit[] _crits;
		private boolean _not;

		/**
		 * 构造方法
		 * 
		 * @param crit
		 *          表达式项
		 * @param crits
		 *          要包含的表达式项数组
		 * @param not
		 *          是否为非
		 */
		public In(Crit crit, Crit[] crits, boolean not) {
			_crit = crit;
			_crits = crits;
			_not = not;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			Object v = _crit.compute(bean);
			for (int i = 0; i < _crits.length; i++) {
				if (Obj.equals(v, _crits[i].compute(bean)))
					return _not ? Boolean.FALSE : Boolean.TRUE;
			}
			return _not ? Boolean.TRUE : Boolean.FALSE;
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			StringBuffer buf = new StringBuffer();
			buf.append(_crit + (_not ? " not " : " ") + "in (");
			for (int i = 0; i < _crits.length; i++) {
				if (i != 0)
					buf.append(",");
				buf.append(_crits[i]);
			}
			return buf.toString() + ")";
		}
	}

	/**
	 * Title: 在区间之间达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Between extends Crit {
		private Crit _crit;
		private Crit _critBegin;
		private Crit _critEnd;
		private boolean _not;

		/**
		 * 
		 * @param crit
		 * @param critBegin
		 * @param critEnd
		 * @param not
		 */
		public Between(Crit crit, Crit critBegin, Crit critEnd, boolean not) {
			_crit = crit;
			_critBegin = critBegin;
			_critEnd = critEnd;
			_not = not;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			Object crit = _crit.compute(bean);
			boolean result = Obj.compare(crit, _critBegin.compute(bean)) >= 0
			    && Obj.compare(crit, _critEnd.compute(bean)) <= 0;
			return _not ? new Boolean(!result) : new Boolean(result);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return _crit.toString() + (_not ? " not " : " ") + "between ("
			    + _critBegin + "," + _critEnd + ")";
		}
	}

	/**
	 * Title: 小于表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class LessThan extends Crit {
		private Crit _crit1;
		private Crit _crit2;
		private boolean _not;

		/**
		 * 构造方法
		 * 
		 * @param crit1
		 *          表达式项1
		 * @param crit2
		 *          表达式项2
		 * @param not
		 *          是否为非
		 */
		public LessThan(Crit crit1, Crit crit2, boolean not) {
			_crit1 = crit1;
			_crit2 = crit2;
			_not = not;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			// 排除_crit1跟_crit2相等的情况
			if (Obj.compare(_crit1.compute(bean), _crit2.compute(bean)) == 0)
				return new Boolean(false);
			boolean result = Obj.compare(_crit1.compute(bean), _crit2.compute(bean)) < 0;
			return _not ? new Boolean(!result) : new Boolean(result);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, _not ? ">" : "<", _crit2 });
		}
	}

	/**
	 * Title: 小于或等于表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class LessEqual extends Crit {
		private Crit _crit1;
		private Crit _crit2;
		private boolean _not;

		/**
		 * 构造方法
		 * 
		 * @param crit1
		 *          表达式项1
		 * @param crit2
		 *          表达式项2
		 * @param not
		 *          是否为非
		 */
		public LessEqual(Crit crit1, Crit crit2, boolean not) {
			_crit1 = crit1;
			_crit2 = crit2;
			_not = not;
		}

		/**
		 * 计算结果
		 * 
		 * @param bean
		 *          对象的Object
		 * @return 结果
		 */
		public Object compute(Object bean) {
			// _crit1等于_crit2里的特殊处理
			if (Obj.compare(_crit1.compute(bean), _crit2.compute(bean)) == 0)
				return new Boolean(true);
			boolean result = Obj.compare(_crit1.compute(bean), _crit2.compute(bean)) < 0;
			return _not ? new Boolean(!result) : new Boolean(result);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3,
			    new Object[] { _crit1, _not ? ">=" : "<=", _crit2 });
		}
	}

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
	 * Title: 字符串连接表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Concat extends Crit {
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
		public Concat(Crit crit1, Crit crit2) {
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
			return _crit1.compute(bean).toString() + _crit2.compute(bean).toString();
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
	 * Title: 乘表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Multiply extends Crit {
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
		public Multiply(Crit crit1, Crit crit2) {
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
			return toBigDecimal(bean, _crit1).multiply(toBigDecimal(bean, _crit2));
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "*", _crit2 });
		}
	}

	/**
	 * Title: 除表达式项<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * 
	 * @version 1.0
	 */
	public static class Divide extends Crit {
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
		public Divide(Crit crit1, Crit crit2) {
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
			return toBigDecimal(bean, _crit1).divide(toBigDecimal(bean, _crit2),
			    BigDecimal.ROUND_HALF_UP);
		}

		/**
		 * 转化为字符串
		 * 
		 * @return 结果
		 * @see java.lang.Object#toString()
		 */
		public String toString() {
			return format(FORMAT3, new Object[] { _crit1, "/", _crit2 });
		}
	}
}
