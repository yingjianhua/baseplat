//Created on 2005-11-9
package irille.pub;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Vector;

/**
 * Title: 简单属性工具<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class ClassTools {
	private static final Log LOG = new Log(ClassTools.class);
	/**
	 * 0个元素的数组，即值为CLASSES0 = {};
	 */
	public static final Class[] CLASSES0 = {};
	/**
	 * 包含一个“String.class”的数组，即 CLASSES_STRING = {String.class};
	 */
	public static final Class[] CLASSES_STRING = { String.class };
	/**
	 * 包含一个“Object.class”的数组，即CLASSER_OBJECT = {Object.class};
	 */
	public static final Class[] CLASSES_OBJECT = { Object.class };
	static final Hashtable<Class, Class> CLASSES = new Hashtable<Class, Class>();
	static {
		CLASSES.put(Boolean.class, boolean.class);
		CLASSES.put(Byte.class, byte.class);
		CLASSES.put(Short.class, short.class);
		CLASSES.put(Integer.class, int.class);
		CLASSES.put(Long.class, long.class);
		CLASSES.put(Float.class, float.class);
		CLASSES.put(Double.class, double.class);
	}
//	private static DyLoader _loader = null;

	public static final HashMap<String, String> MAP = new HashMap<String, String>();
	static {
		MAP.put("base", "erp/src/irille/bean");
		MAP.put("sch", "sch/src/irille/bean");
	}

	/**
	 * 置属性
	 * 
	 * @param bean
	 *          Bean
	 * @param propertyCode
	 *          属性代码
	 * @param value
	 *          值
	 * @throws ERR_SET_PROPERTY
	 */
	public static final void setProperty(Object bean, String propertyCode,
	    Object value) throws Exp {
		Method method;
		Class<? extends Object> clazz = bean.getClass();
		if (Character.isLowerCase(propertyCode.charAt(0))) // 首字母转为大写
			propertyCode = Character.toUpperCase(propertyCode.charAt(0))
			    + propertyCode.substring(1);
		try {
			try {
				method = clazz.getMethod("set" + propertyCode,
				    new Class[] { value.getClass() });
			} catch (Exception e) {
				Class paraClazz = CLASSES.get(value.getClass());
				if (paraClazz == null)
					throw e;
				method = clazz.getMethod("set" + propertyCode,
				    new Class[] { paraClazz });
			}
			method.invoke(bean, new Object[] { value });
		} catch (Exception e) {
			throw LOG.err("setProperty","置类[{0}]的属性[{1}]出错!", bean.getClass(), propertyCode);
		}
	}

	public static final Object getPropertyIfNullReturnZeroStr(Object bean, String propertyCode) {
		Object obj=getProperty(bean,propertyCode);
		return obj==null?"":obj;
	}
	/**
	 * 取属性
	 * 
	 * @param bean
	 *          Bean
	 * @param propertyCode
	 *          属性代码
	 * @return 结果
	 * @throws Exp
	 *           ERR_GET_PROPERTY
	 */
	public static final Object getProperty(Object bean, String propertyCode)
	    throws Exp {
		String[] fields = Str.split(propertyCode, ".");
		String get;
		Object value = null;
		Object inBean = bean;
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			get = "get" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
			try {
				value = inBean.getClass().getMethod(get, CLASSES0)
				    .invoke(inBean, new Object[0]);
			} catch (Exception e) {
				get = "is" + Character.toUpperCase(field.charAt(0))
				    + field.substring(1);
				try {
					value = inBean.getClass().getMethod(get, CLASSES0)
					    .invoke(inBean, new Object[0]);
				} catch (Exception e1) {
					throw LOG.err("getProperty","取类[{0}]的属性[{1}]出错, 必须有对应的get或is方法!",
					    bean.getClass(), propertyCode);
				}
			}
			if (value == null)
				return null;
			inBean = value;
		}
		return value;
	}

	/**
	 * 取属性
	 * 
	 * @param bean
	 *          Bean
	 * @param propertyCode
	 *          属性代码
	 * @return 结果
	 * @throws Exp
	 *           ERR_GET_PROPERTY
	 */
	public static final Object gtProperty(Object bean, String propertyCode)
	    throws Exp {
		String[] fields = Str.split(propertyCode, ".");
		String get;
		Object value = null;
		Object inBean = bean;
		for (int i = 0; i < fields.length; i++) {
			String field = fields[i];
			get = "gt" + Character.toUpperCase(field.charAt(0)) + field.substring(1);
			try {
				value = inBean.getClass().getMethod(get, CLASSES0)
				    .invoke(inBean, new Object[0]);
			} catch (Exception e) {
				get = "is" + Character.toUpperCase(field.charAt(0))
				    + field.substring(1);
				try {
					value = inBean.getClass().getMethod(get, CLASSES0)
					    .invoke(inBean, new Object[0]);
				} catch (Exception e1) {
					throw LOG.err("gtProperty","取类[{0}]的属性[{1}]出错, 必须有对应的get或is方法!",
					    bean.getClass(), propertyCode);
				}
			}
			if (value == null)
				return null;
			inBean = value;
		}
		return value;
	}
	/**
	 * 取静态属性
	 * 
	 * @param clazz
	 *          类
	 * @param propertyCode
	 *          属性代码
	 * @return 结果
	 * @throws ERR_GET_STATIC_PROPERTY
	 */
	public static final Object getStaticProerty(Class clazz, String propertyCode)
	    throws Exp {
		try {
			return clazz.getField(propertyCode).get(null);
		} catch (Exception e) {
			throw LOG.err("getStaticProerty","取类[{0}]的静态常量[{1}]出错!", clazz, propertyCode);
		}
	}

	/**
	 * 取静态属性
	 * 
	 * @param clazz
	 *          类
	 * @param propertyCode
	 *          属性代码
	 * @return 结果 | 出错返回Null
	 */
	public static final Object getStaticProertyNotThrow(Class clazz,
	    String propertyCode) {
		try {
			return clazz.getField(propertyCode).get(null);
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 取静态属性
	 * 
	 * @param clazzCode
	 *          类代码
	 * @param propertyCode
	 *          属性代码
	 * @return 结果
	 * @throws ERR_GET_STATIC_PROPERTY
	 */
	public static final Object getStaticProerty(String clazzCode,
	    String propertyCode) throws Exp {
		return getStaticProerty(getClass(clazzCode), propertyCode);
	}

	/**
	 * 取指定类型(包括其子类)的静态常量数组
	 * 
	 * @param clazz
	 *          类
	 * @param propertyClazz
	 *          属性类
	 * @param isSpecifyClassOnly
	 *          是否仅取当前类定义的，如为false则包括所有基类定义的
	 * @return 结果
	 */
	public static final Field[] getStaticProperties(Class clazz,
	    Class propertyClazz, boolean isSpecifyClassOnly) {
		Field[] flds = clazz.getFields();
		Vector<Field> v = new Vector<Field>();
		for (int i = 0; i < flds.length; i++) {
			if (!flds[i].getDeclaringClass().equals(clazz) && isSpecifyClassOnly
			    || (flds[i].getModifiers() & Modifier.STATIC) == 0
			    || !propertyClazz.isAssignableFrom(flds[i].getType()))
				continue;
			v.addElement(flds[i]);
		}
		flds = new Field[v.size()];
		for (int i = 0; i < v.size(); i++) {
			flds[i] = v.elementAt(i);
		}
		return flds;
	}

	/**
	 * 取类的构造方法
	 * 
	 * @param cls
	 *          Class 类
	 * @param classes
	 *          Class[] 方法参数类型数组
	 * @return Method 方法对象
	 */
	public static Constructor getConstructor(Class cls, Class[] classes) {
		try {
			return cls.getDeclaredConstructor(classes);
		} catch (Exception e) {
			throw LOG.err("getConstructor","取类[{0}]的构造方法[({1})]出错!", cls.getName(),
			    classesToString(classes));
		}
	}

	/**
	 * 将类数组转换成"String,Long"类似的字符串
	 * 
	 * @param clazzes
	 *          类
	 * @return 结果
	 */
	public static String classesToString(Class[] clazzes) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < clazzes.length; i++) {
			if (i != 0)
				buf.append(",");
			buf.append(clazzes[i].getName());
		}
		return buf.toString();
	}

	/**
	 * 
	 * @param clazzCode
	 * @return 结果
	 */
	public static final Object newInstance(String clazzCode) {
		return newInstance(getClass(clazzCode));
	}

	/**
	 * 构造实例
	 * 
	 * @param cls
	 *          类
	 * @return 结果
	 */
	public static final <T extends Object> T newInstance(Class<T> cls) {
		try {
			return (T)cls.newInstance();
		} catch (Exception e) {
			throw runError(e, cls, Str.getClazzLastCode(cls), CLASSES0);
		}
	}

	/**
	 * 构造实例
	 * 
	 * @param clazz
	 *          类
	 * @param paraClazzes
	 *          参数类型
	 * @param paras
	 *          参数
	 * @return 结果
	 */
	public static final <T extends Object> T newInstance(Class<T> clazz, Class[] paraClazzes,
	    Object... paras) {
		Constructor c = getConstructor(clazz, paraClazzes);
		try {
			return (T)c.newInstance(paras);
		} catch (Exception e) {
			throw runError(e, clazz, Str.getClazzLastCode(clazz), paraClazzes);
		}
	}

	/**
	 * 根据类，方法代码，方法参数类型数组取Method对象
	 * 
	 * @param cls
	 *          Class 类
	 * @param methodCode
	 *          String 方法代码
	 * @param classes
	 *          Class[] 方法参数类型数组
	 * @return Method 方法对象
	 */
	public static Method getMethod(Class cls, String methodCode, Class... classes) {
		RuntimeException exp;
		try {
			return cls.getMethod(methodCode, classes);
		} catch (Exception e) {
			exp = LOG.err("getMethod","执行类[{0}]的方法[{1}({2})]出错!", cls.getName(), methodCode,
			    ClassTools.classesToString(classes));
			if (classes.length != 1)
				throw exp;
		}
		Class c = classes[0];
		while (true) {
			c = c.getSuperclass();
			try {
				return cls.getMethod(methodCode, new Class[] { c });
			} catch (Exception e) {
			}
			if (c == null || c.equals(Object.class))
				throw exp;
		}
	}

	/**
	 * 根据类，方法代码，取无参数的Method对象
	 * 
	 * @param cls
	 *          Class 类
	 * @param methodCode
	 *          String 方法代码
	 * @return Method 方法对象
	 */
	public static Method getMethod(Class cls, String methodCode) {
		return getMethod(cls, methodCode, CLASSES0);
	}

	/**
	 * 根据类型取类
	 * 
	 * @param classCode
	 *          String 类名
	 * @return Class 类
	 */
	public static Class getClass(String classCode) {
		try {
			return Class.forName(classCode);
		} catch (Exception e) {
			throw LOG.err("classNotFound","[{0}]类没找到！", classCode);
		}
	}

	public static final Class getClassDaoByBean(String beanClassName) {
		return ClassTools.getClass(beanClassName + "DAO");
	}

	public static final Class getClassBeanByDao(String daoClassName) {
		return ClassTools.getClass(daoClassName.substring(0,
		    daoClassName.length() - 3));
	}
	
	/**
	 * 调用方法出错的分析
	 * 
	 * @param e
	 * @param clazz
	 * @param methodCode
	 * @param paras
	 * @return 结果
	 */
	public static RuntimeException runError(Exception e, Class clazz,
	    String methodCode, Class[] paras) {
		if (e instanceof InvocationTargetException) {
			// || e instanceof IllegalAccessException) {
			Throwable cause = ((InvocationTargetException) e).getTargetException();
			if (cause != null && cause instanceof Exp)
				return (Exp) cause;
		}
		return LOG.err("runError","执行类[{0}]的方法[{1}({2})]出错!", clazz.getName(), methodCode,
		    classesToString(paras));
	}

	/**
	 * 运行方法
	 * 
	 * @param method
	 *          Method 方法
	 * @param obj
	 *          Object 对象
	 * @param paras
	 *          Object[] 参数
	 * @return Object 结果
	 */
	public static Object run(Method method, Object obj, Object... paras) {
		try {
			return method.invoke(obj, paras);
		} catch (Exception e) {
			throw runError(e, method.getDeclaringClass(), method.getName(),
			    method.getParameterTypes());
		}
	}

	/**
	 * 运行指定的方法
	 * 
	 * @param objOrClass
	 *          对象或类，如为类则必须为静态方法
	 * @param methodCode
	 *          方法
	 * @param paraClasses
	 *          参数的数据类型
	 * @param paras
	 *          参数
	 * @return 结果
	 */
	public static Object run(Object objOrClass, String methodCode,
	    Class[] paraClasses, Object... paras) {
		Method method = getMethod(objOrClass instanceof Class ? (Class) objOrClass
		    : objOrClass.getClass(), methodCode, paraClasses);
		return run(method, objOrClass instanceof Class ? null : objOrClass, paras);
	}

	/**
	 * 取包中的所有类
	 * 
	 * @param oneClassOfPackage
	 *          包中的一个类
	 * @return 结果
	 */
	public static String[] getPackageClazzCodes(Class oneClassOfPackage) {
		Class[] clazzes = getPackageClazzes(oneClassOfPackage);
		String[] clazzCodes = new String[clazzes.length];
		for (int i = 0; i < clazzes.length; i++) {
			clazzCodes[i] = clazzes[i].getName();
		}
		return clazzCodes;
	}
	/**
	 * 取包中的所有类
	 * 
	 * @param oneClassOfPackage
	 *          包中的一个类
	 * @return 结果
	 */
	public static Class[] getPackageClazzes(Class oneClassOfPackage) {
		String packageName = oneClassOfPackage.getName().substring(0,
		    oneClassOfPackage.getName().lastIndexOf("."));
		return getPackageClazzes(oneClassOfPackage, packageName);
	}

	public static Class[] getPackageClazzes(Class resourceClass,
	    String packageName) {
		// 把包名字转换成绝对路径
		String name = "/" + packageName.replace('.', '/');
		URL url = resourceClass.getResource(name);
		File directory = new File(url.getFile());
		Vector cls = new Vector();
		// 获得包里面的文件清单
		String[] files = directory.list();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].endsWith(".class"))
				continue;
			String classname = files[i].substring(0, files[i].length() - 6);
			cls.addElement(packageName + "." + classname);
		}
		Class[] clazzes = new Class[cls.size()];
		for (int i = 0; i < cls.size(); i++) {
			clazzes[i] = getClass(cls.elementAt(i).toString());
		}
		return clazzes;
	}

//	public static Class[] getPackageClazzModels(Class resourceClass) {
//		return getPackageClazzModels(resourceClass, resourceClass.getName()
//		    .substring(0, resourceClass.getName().lastIndexOf(".")));
//	}

//	public static Class[] getPackageClazzModels(Class resourceClass,
//	    String packageName) {
//		return getPackageClazzModels(resourceClass.getName().split("\\.")[2],
//		    packageName);
//	}
//	public static Class[] getPackageClazzModels(String moduleCode,
//	    String packageName) {
//		String path = ErpConfig.getInstance().getProperty("irille.workSpace") + "/"
//		    + MAP.get(moduleCode) + "/" + moduleCode; // 模型path
//		Vector vec = new Vector();
//		File file = new File(path);
//		String[] files = file.list();
//		System.out.printf("path1");
//		System.out.printf(path);
//		for (String subf : files) {
//			if (!subf.endsWith(".java"))
//				continue;
//			vec.addElement(packageName + "." + subf.substring(0, subf.length() - 5));
//			System.out.printf(packageName + "."
//			    + subf.substring(0, subf.length() - 5));
//		}
//		Class[] cls = new Class[vec.size()];
//		for (int i = 0; i < vec.size(); i++)
//			cls[i] = ClassTools.getClass(vec.get(i).toString());
//		return cls;
//	}
//
//	public static Class[] getPackageClazzModules(String moduleCode,
//	    String packageName) {
//		String path = ErpConfig.getInstance().getProperty("irille") + "/"
//		    + MAP.get(moduleCode).toString().replaceFirst("bean", "module") + "/"
//		    + moduleCode; // 模型path
//		Vector vec = new Vector();
//		File file = new File(path);
//		String[] files = file.list();
//		System.out.printf("path1");
//		System.out.printf(path);
//		for (String subf : files) {
//			if (!subf.endsWith(".java"))
//				continue;
//			vec.addElement(packageName + "." + subf.substring(0, subf.length() - 5));
//			System.out.printf(packageName + "."
//			    + subf.substring(0, subf.length() - 5));
//		}
//		Class[] cls = new Class[vec.size()];
//		for (int i = 0; i < vec.size(); i++)
//			cls[i] = ClassTools.getClass(vec.get(i).toString());
//		return cls;
//	}
//
//	/**
//	 * 取loader
//	 * 
//	 * @return loader
//	 */
//	public static DyLoader getLoader() {
//		if (_loader != null)
//			return _loader;
//		_loader = new DyLoader(Thread.currentThread().getContextClassLoader());
//		return _loader;
//	}
//
//	/**
//	 * 置loader的值
//	 * 
//	 * @param loader
//	 *          新值
//	 */
//	public static void setLoader(DyLoader loader) {
//		_loader = loader;
//	}
//
//	public static void resetLoader() {
//		LOG.info("BeanTools:resetLoader!");
//		if (_loader != null)
//			_loader = _loader.copyNew();
//	}
}
