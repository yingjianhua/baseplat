//Created on 2005-9-17
package irille.pub;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * <p>Title: 容器（数组、容器、集合）的相关处理</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2004</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class Objs {
	
	/**
	 * 将容器相减，即all-sub
		 * @param all 被减容器
		 * @param sub 要减容器
	 * @param keyProperty 主键字段
	 * @return
	 */
	public static final Collection sub(Collection all,
			Collection sub, String keyProperty) {
		return sub(all,sub,keyProperty,keyProperty);
	}
		/**
		 * 将容器相减，即all-sub
		 * @param all 被减容器
		 * @param sub 要减容器
		 * @param allKeyProperty 被减容器中对象主键字段
		 * @param subKeyProperty 要减容器中对象主键字段
		 * @return
		 */
		public static final Collection sub(Collection all,
				Collection sub, String allKeyProperty,String subKeyProperty ) {
		Hashtable map = new Hashtable();
		for (Object obj : all)
			map.put(ClassTools.getProperty(obj, allKeyProperty), obj);
		for (Object obj : sub)
			map.remove(ClassTools.getProperty(obj, subKeyProperty));
		return map.values();
	}

	/**
	 * 取Hashtable的键数组
	 * @param hash  Hashtable
	 * @return 结果
	 */
	public static Object[] hashtableKeys(Hashtable hash) {
		return hashtableKeys(hash, Object.class);
	}

	/**
	 * 取Hashtable的键数组
	 * @param hash  Hashtable
	 * @param clazz 结果类型
	 * @return 结果
	 */
	public static Object[] hashtableKeys(Hashtable hash, Class clazz) {
		Object[] objs = (Object[]) Array.newInstance(clazz, hash.size());
		Enumeration elements = hash.keys();
		for (int i = 0; elements.hasMoreElements(); i++)
			objs[i] = elements.nextElement();
		return objs;
	}

	/**
	 * 取Hashtable的值数组
	 * @param hash  Hashtable
	 * @return 结果
	 */
	public static Object[] hashtableValues(Hashtable hash) {
		return hashtableValues(hash, Object.class);
	}

	/**
	 * 取Hashtable的值数组
	 * @param hash  Hashtable
	 * @param clazz 结果类型
	 * @return 结果
	 */
	public static Object[] hashtableValues(Hashtable hash, Class clazz) {
		Object[] objs = (Object[]) Array.newInstance(clazz, hash.size());
		Enumeration elements = hash.elements();
		for (int i = 0; elements.hasMoreElements(); i++)
			objs[i] = elements.nextElement();
		return objs;
	}

	/**
	 * 将Enumeration转换为Object[]
	 * @param elements Enumeration
	 * @return 结果
	 */
	public static Object[] enumerationToArray(Enumeration elements) {
		return enumerationToArray(elements, Object.class);
	}

	/**
	 * 将Enumeration转换为Object[]
	 * @param elements Enumeration
	 * @param clazz 结果类型
	 * @return 结果
	 */
	public static Object[] enumerationToArray(Enumeration elements, Class clazz) {
		Vector vector = new Vector();
		while (elements.hasMoreElements())
			vector.addElement(elements.nextElement());
		return vectorToArray(vector, clazz);
	}

	/**
	 * 将Vector转换为Object[]
	 * @param vector Vector
	 * @return 结果
	 */
	public static Object[] vectorToArray(Vector vector) {
		return vectorToArray(vector, Object.class);
	}

	/**
	 * 将Vector转换为Object[]
	 * @param vector Vector
	 * @param clazz 结果类型
	 * @return 结果
	 */
	public static Object[] vectorToArray(Vector vector, Class clazz) {
		Object[] objs = (Object[]) Array.newInstance(clazz, vector.size());
		for (int i = vector.size() - 1; i >= 0; i--) {
			objs[i] = vector.elementAt(i);
		}
		return objs;
	}

	/**
	 * 将数组加到Vector中
	 * @param vector Vector
	 * @param objs 对象数组
	 */
	public static void addToVector(Vector vector, Object[] objs) {
		for (int i = 0; i < objs.length; i++) {
			vector.addElement(objs[i]);
		}
	}
	
	public static void addToColletion(Collection col,Object[] objs){
		for (int i=0; i<objs.length; i++)
			col.add(objs[i]);
	}

	/**
	 * Vector.addAll()功能
	 * @param vec1
	 * @param vec2
	 */
	public static void vectorAddAll(Vector vec1, Vector vec2) {
		Object[] objs = vectorToArray(vec2);
		for (int i = 0; i < objs.length; i++)
			vec1.addElement(objs[i]);
	}

	/**
	 * 将Vector转换为String[]
	 * @param vector Vector
	 * @return 结果
	 */
	public static String[] vectorToStrings(Vector vector) {
		String[] objs = new String[vector.size()];
		for (int i = vector.size() - 1; i >= 0; i--) {
			objs[i] = vector.elementAt(i).toString();
		}
		return objs;
	}

	/**
	 * 取最后一个元素
	 * @param strs 字符串
	 * @return 结果, 如果strs==null或strs.length==0, 返回null
	 */
	public static String getLast(String[] strs) {
		if (strs == null || strs.length == 0)
			return null;
		return strs[strs.length - 1];
	}

	/**
	 * 取对象在数组中的位置，
	 * @param objs 数组
	 * @param obj 对象
	 * @return 结果，不在数组中返回-1，否则返回下标
	 */
	public static int getId(Object[] objs, Object obj) {
		for (int i = objs.length - 1; i >= 0; i--)
			if (Obj.equals(obj, objs[i]))
				return i;
		return -1;
	}

	/**
	 * 取数值在数组中的位置，
	 * @param values 数组
	 * @param value 数值
	 * @return 结果，不在数组中返回-1，否则返回下标
	 */
	public static int getId(int[] values, int value) {
		for (int i = values.length - 1; i >= 0; i--)
			if (value == values[i])
				return i;
		return -1;
	}

	/**
	 * 判断数值是否在数组中，
	 * @param values 数组
	 * @param value 数值
	 * @return 结果
	 */
	public static boolean in(int[] values, int value) {
		return getId(values, value) >= 0;
	}

	/**
	 * 取数值在数组中的位置，
	 * @param values 数组
	 * @param value 数值
	 * @return 结果，不在数组中返回-1，否则返回下标
	 */
	public static int getId(byte[] values, byte value) {
		for (int i = values.length - 1; i >= 0; i--)
			if (value == values[i])
				return i;
		return -1;
	}

	/**
	 * 判断数值是否在数组中，
	 * @param values 数组
	 * @param value 数值
	 * @return 结果
	 */
	public static boolean in(byte[] values, byte value) {
		return getId(values, value) >= 0;
	}

	/**
	 * 判断对象是否在数组中
	 * @param objs 数组
	 * @param obj 对象
	 * @return 结果
	 */
	public static boolean in(Object[] objs, Object obj) {
		return getId(objs, obj) >= 0;
	}

	/**
	 * 比较数组对象是否一致
	 * @param obj1 对象数组1
	 * @param obj2 对象数组1
	 * @return 结果
	 */
	public static boolean equals(Object[] obj1, Object[] obj2) {
		if (obj1 == obj2)
			return true;
		if (obj1 == null || obj2 == null || obj1.length != obj2.length)
			return false;
		for (int i = 0; i < obj2.length; i++) {
			if (!Obj.equals(obj1[i], obj2[i]))
				return false;
		}
		return true;
	}
	
	/**
	 * 将数组的内容转为字符串
	 * @param objs Object[] 对象数组
	 * @param left String 每个元素左边先输出的串（如左边空白），“null”表示无
	 * @param right String 每个元素右边输出的串（如“Env.LN”,“,”)
	 * @param endRight String 最后一行右边输出的串
	 *（如“right”为“Env.LN”则此处一般也为“Env.LN”，如“right”为“,”，则此处一般为null）
	 * @return String
	 */
	public static String toString(Object[] objs, String left, String right,
			String endRight) {
		if (left == null) {
			left = "";
		}
		if (endRight == null) {
			endRight ="";
		}
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < objs.length; i++) {
			if (i == objs.length - 1) {
				buf.append(left + objs[i] + endRight);
			} else {
				buf.append(left + objs[i] + right);
			}
		}
		return buf.toString();
	}

	//	/**
	//	 * 将数组转换成以“，”分隔的字符串
	//	 * @param objs Object[] 对象数组
	//	 * @return String 结果
	//	 */
	//	public static String toString(Object[] objs) {
	//		return toString(objs, null, ",", null);
	//	}
	//	/**
	//	 * 将数组转换成以指定分隔的字符串
	//	 * @param objs Object[] 对象数组
	//	 * @return String 结果
	//	 */
	//	public static String toString(Object[] objs,String separator ) {
	//		return toString(objs, null, separator, null);
	//	}
	//
	//	/**
	//	 * 将对象数组转代为字符串数组
	//	 * @param objs Object[] 对象数组
	//	 * @return String[] 字符串数组
	//	 */
	//	public static String[] toStrings(Object[] objs) {
	//		String[] strings = new String[objs.length];
	//		for (int i = objs.length - 1; i >= 0; i--) {
	//			strings[i] = objs[i].toString();
	//		}
	//		return strings;
	//	}
	//
	//	/**
	//	 * 将Map转换成以“，”分隔的字符串
	//	 * @param objs Map map
	//	 * @return String 结果
	//	 */
	//	public static String toString(Map objs) {
	//		return toString(objs.values().toArray());
	//	}
	//	/**
	//	 * 将容器中每个元素的"methodCode"对应方法的结果转换成以“，”分隔的字符串
	//	 * @param objs Collection 容器
	//	 * @param separator 指定分隔符
	//	 * @return String 结果
	//	 */
	//	public static String toString(Collection objs, String separator) {
	//		return toString(objs.toArray(), separator);
	//	}
	//
	//
	//	/**
	//	 * 将容器转换成以“，”分隔的字符串
	//	 * @param objs Collection 容器
	//	 * @return String 结果
	//	 */
	//	public static String toString(Collection objs) {
	//		return toString(objs.toArray());
	//	}
	//
	//
	//	/**
	//	 * 将ids中指定序号数组中每个元素组合成数组
	//	 * @param objs Object[] 对象数组
	//	 * @param ids short[] ID号, 负数取绝对值，Short.MIN_VALUE取0
	//	 * @throws Exp 出错
	//	 * @return Object[] 结果
	//	 */
	//	public static String[] toArray(Object[] objs, short[] ids) {
	//		String[] values = new String[ids.length];
	//		int id;
	//		for (int i = 0; i < ids.length; i++) {
	//			id = ids[i];
	//			if (id < 0) {
	//				if (id == Short.MIN_VALUE) {
	//					id = 0;
	//				} else {
	//					id = -id;
	//				}
	//			}
	//			values[i] = objs[id].toString();
	//		}
	//		return values;
	//	}
	//
	//	/**
	//	 * 将ArrayList对象转化为数组
	//	 * @param list ArrayList对象
	//	 * @param cls 要转化为数组的类型
	//	 * @return 结果
	//	 */
	//	public static Object[] tranArray(List list, Class cls) {
	//		Object[] objs=(Object[])Array.newInstance(cls,list.size());
	//		for(int i=list.size()-1;i>=0;i--)
	//			Array.set(objs,i,list.get(i));
	//		return objs;
	//	}
	//	
	//	/**
	//	 * 将objs中每个元素组合成数组
	//	 * @param objs Object[] 对象数组
	//	 * @throws Exp 出错
	//	 * @return String[] 结果
	//	 */
	//	public static String[] toStringArray(Object[] objs) {
	//		String[] values = new String[objs.length];
	//		for (int i = objs.length - 1; i >= 0; i--) {
	//			values[i] = objs[i].toString();
	//		}
	//		return values;
	//	}
	//
	//
	//
	//	/**
	//	 * 将数据对放入HashMap
	//	 * @param hash HashMap HashMap表，为null则新建
	//	 * @param objs Object[][] 数据对
	//	 * @return HashMap HashMap
	//	 */
	//	public static HashMap toHashMap(HashMap hash, Object[][] objs) {
	//		if (hash == null) {
	//			hash = new HashMap();
	//		}
	//		for (int i = objs.length - 1; i >= 0; i--) {
	//			hash.put(objs[i][0], objs[i][1]);
	//		}
	//		return hash;
	//	}
	//	/**
	//	 * 将数组转换成ArrayList
	//	 * @param objs Object[] 数组
	//	 * @return  结果
	//	 */
	//	public static ArrayList toArrayList(Object[] objs) {
	//		ArrayList list=new ArrayList();
	//		for (int i = 0; i < objs.length; i++) {
	//			list.add(objs[i]);
	//		}
	//		return list;
	//	}
	//
	//
	//	
}
