//Created on 2005-10-10
package irille.pub;

import irille.pub.bean.Bean;
import irille.pub.tb.Fld;
import irille.pub.tb.FldV;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.ArrayList;
import java.util.Collection;


/**
 * Title: 属性工具<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class PropertyUtils {
	private static final Log LOG= new Log(PropertyUtils.class);

	/**
	 * 取属性值
	 * @param bean 对象
	 * @param property 属性
	 * @return 结果
	 */
	public static Object getSimpleProperty(Object bean, String property) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getSimpleProperty(bean,
					property);
		} catch (Exception e) {
			throw LOG.err("getSimpleProperty","取类[{0}]的属性[{1}]出错!", bean.getClass(), property);
		}
	}

	/**
	 * 置属性值
	 * @param bean 对象
	 * @param property 属性
	 * @param value 值
	 */
	public static void setSimpleProperty(Object bean, String property,
			Object value) {
		try {
			org.apache.commons.beanutils.PropertyUtils.setSimpleProperty(bean,
					property, value);
		} catch (Exception e) {
			throw LOG.err("setSimpleProperty","置类[{0}]的属性[{1}]出错!",bean.getClass(), property);
		}
	}

	/**
	 * 取属性值
	 * @param bean 对象
	 * @param property 属性
	 * @return 结果
	 */
	public static Object getProperty(Object bean, String property) {
		//	return	BeanTools.getProperty(bean,property);
		try {
			return org.apache.commons.beanutils.PropertyUtils.getProperty(bean,
					property);
		} catch (Exception e) {
			throw LOG.err("getProperty","取类[{0}]的属性[{1}]出错!",bean.getClass(), property);
		}
	}
	
	/**
	 * 置属性值
	 * @param bean 对象
	 * @param property 属性
	 * @param value 值
	 */
	public static void setProperty(Object bean, String property, Object value) {
		try {
			org.apache.commons.beanutils.PropertyUtils.setProperty(bean, property,
					value);
		} catch (Exception e) {
			throw LOG.err("setProperty","置类[{0}]的属性[{1}]出错!", bean.getClass(), property);
		}
	}

	/**
	 * 取属性值
	 * @param bean 对象
	 * @param property 属性
	 * @return 结果
	 */
	public static Object getNestedProperty(Object bean, String property) {
		try {
			return org.apache.commons.beanutils.PropertyUtils.getNestedProperty(bean,
					property);
		} catch (Exception e) {
			throw LOG.err("getNestedProperty","取类[{0}]的属性[{1}]出错!", bean.getClass(), property);
		}
	}

	/**
	 * 置属性值
	 * @param bean 对象
	 * @param property 属性
	 * @param value 值
	 */
	public static void setNestedProperty(Object bean, String property,
			Object value) {
		try {
			org.apache.commons.beanutils.PropertyUtils.setNestedProperty(bean,
					property, value);
		} catch (Exception e) {
			throw LOG.err("setNestedProperty","置类[{0}]的属性[{1}]出错!",bean.getClass(), property);
		}
	}

	/**
	 * 置属性值
	 * @param model 模型 
	 * @param fieldAndValues 字段与值
	 * @return model
	 */
	public static Bean set(Bean model, Object... fieldAndValues) {
		Fld fld;
		Object value;
		for (int i = 0; i < fieldAndValues.length; i += 2) {
			fld = (Fld) fieldAndValues[i];
			value = fieldAndValues[i + 1];
			PropertyUtils.setProperty(model, fld.getCode(), value);
		}
		return model;
	}

	/**
	 * 复制属性
	 * @param to 目的Model
	 * @param from 来源Model
	 * @param flds 字段
	 */
	public static void copyProperties(Bean to, Bean from,
			Fld... flds) {
		for (int i = 0; i < flds.length; i++) {
			if (flds[i] instanceof FldV)
				continue;
			setProperty(to, flds[i].getCode(), getProperty(from, flds[i].getCode()));
		}
	}
	
	public static void copyProperties(Bean to, Bean from,
			IEnumFld... eflds) {
		Fld[] flds = new Fld[eflds.length];
		for (int i=0; i<eflds.length; i++)
			flds[i] = eflds[i].getFld();
		copyProperties(to, from, flds);
	}
	
	public static void copyPropertiesWithout(Bean to, Bean from,
			IEnumFld... eflds) {
		Fld[] flds = new Fld[eflds.length];
		for (int i=0; i<eflds.length; i++)
			flds[i] = eflds[i].getFld();
		copyPropertiesWithout(to,from,flds);
	}

	/**
	 * 复制除指定字段外的其它属性
	 * @param to 目的Model
	 * @param from 来源Model
	 * @param flds 字段
	 */
	public static void copyPropertiesWithout(Bean to, Bean from,
			Fld... flds) {
		ArrayList<String> fs = new Properties().addWithout(flds).getPropertyCodes();
		for (int i = 0; i < fs.size(); i++) {
			if (from.tb(from.clazz()).get(fs.get(i)) instanceof FldV)
				continue;
			setProperty(to, fs.get(i), getProperty(from, fs.get(i)));
		}
	}
//  REM by whx 20120913
//	public static void copy(Bean to, Bean from) {
//		Tb msg = from.tb();
//		Object obj;
//		Bean model;
//		for (int i = 0; i < msg.size(); i++) {
//			obj = getProperty(from, msg.get(i).getCode());
//			if (obj instanceof Collection) {
//				Collection col = new LinkedHashSet();
//				Collection res = (Set) obj;
//				for (Object m : res.toArray()) {
//					model = Bean.newInstance(ClassTools.getClass(((TypeRow) msg
//							.get(i).getType()).getClazzCode())); //明细Model
//					copy(model, (Bean) m);
//					model.setModelStatus(((Bean)m).getModelStatus());
//					col.add(model);
//				}
//				obj = col;
//				}
//			setProperty(to, msg.get(i).getCode(), obj);
//		}
//	}
	
	public static void copyWithoutCollection(Bean to, Bean from) {
		Tb msg = (Tb)from.gtTB();
		Object obj;
		for (int i = 0; i < msg.size(); i++) {
			if (msg.get(i) instanceof FldV)
				continue;
			obj = getProperty(from, msg.get(i).getCode());
			if (obj instanceof Collection)
				continue;
			setProperty(to, msg.get(i).getCode(), obj);
		}
	}

	/**
	 * 确认model的指定对象都相等
	 * @param model1 
	 * @param model2
	 * @param flds 字段
	 */
	public static void assertEquals(Bean model1, Bean model2,
			Fld... flds) {
		for (int i = 0; i < flds.length; i++) {
			if (Obj.equals(getProperty(model1, flds[i].getCode()), getProperty(
					model2, flds[i].getCode())))
				continue;
			throw LOG.err("assertEquals","类[{0}]的属性[{1}]的值[{2}][{3}]不一致!", model1.getClass(),
					flds[i].getName(), getProperty(model1, flds[i].getCode()), getProperty(
							model2, flds[i].getCode()));
		}
	}

	/**
	 * 确认model除指定字段外字段都相等
	 * @param model1 
	 * @param model2
	 * @param flds 字段
	 */
	public static void assertEqualsWithout(Bean model1, Bean model2,
			Fld... flds) {
		ArrayList<String> fs = new Properties().addWithout(flds).getPropertyCodes();
		for (int i = 0; i < fs.size(); i++) {
			if (Obj.equals(getProperty(model1, fs.get(i)), getProperty(model2, fs
					.get(i))))
				continue;
			throw LOG.err("assertEqualsWithout","类[{0}]的属性[{1}]的值[{2}][{3}]不一致!", model1.getClass(), fs.get(i),
					getProperty(model1, fs.get(i)), getProperty(model2, fs.get(i)));
		}
	}

	/**
	 * 确认model的指定对象都不相等
	 * @param model1 
	 * @param model2
	 * @param flds 字段
	 */
	public static void assertNotEquals(Bean model1, Bean model2,
			Fld... flds) {
		for (int i = 0; i < flds.length; i++) {
			if (!Obj.equals(getProperty(model1, flds[i].getCode()), getProperty(
					model2, flds[i].getCode())))
				continue;
			throw LOG.err("assertNotEquals","类[{0}]的属性[{1}]的值[{2}][{3}]相等", model1.getClass(), flds[i].getName(),
					getProperty(model1, flds[i].getCode()), getProperty(model2, flds[i]
							.getCode()));
		}
	}

	/**
	 * 确认model除指定字段外字段都不相等
	 * @param model1 
	 * @param model2
	 * @param flds 字段
	 */
	public static void assertNotEqualsWithout(Bean model1, Bean model2,
			Fld... flds) {
		ArrayList<String> fs = new Properties().addWithout(flds).getPropertyCodes();
		for (int i = 0; i < fs.size(); i++) {
			if (!Obj.equals(getProperty(model1, fs.get(i)), getProperty(model2, fs
					.get(i))))
				continue;
			throw LOG.err("assertNotEqualsWithout","类[{0}]的属性[{1}]的值[{2}][{3}]相等", model1.getClass(), fs.get(i),
					getProperty(model1, fs.get(i)), getProperty(model2, fs.get(i)));
		}
	}
}
