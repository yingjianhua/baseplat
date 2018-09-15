package irille.action.dataimport.util;

import irille.action.dataimport.util.variable.BooleanVariable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

//import com.telesound.dataimport.util.variable.BooleanVariable;

public class BeanUtil {
	public static Object setProperties(Object bean,Map properties){
		Map map=doWithMap(properties);
		Class c=bean.getClass();
		Field[] fields=c.getDeclaredFields();
		for(int i=0;i<fields.length;i++){
			Field field=fields[i];
			Class fieldType=field.getType();
			String fieldName=field.getName();
			String setMethodName="set"+(fieldName.charAt(0)+"").toUpperCase()+fieldName.substring(1,fieldName.length());
			try {
				Method setMethod=c.getMethod(setMethodName, fieldType);
				Object mapValue= map.get(fieldName.toLowerCase());
				if(fieldType==char.class||fieldType==Character.class){
					try {
						setMethod.invoke(bean,(Character)mapValue);
					} catch (ClassCastException e) {
						setMethod.invoke(bean, StringUtil.getStringValue(mapValue).charAt(0));
					}
				}else if(fieldType==boolean.class||fieldType==Boolean.class){
					try {
						setMethod.invoke(bean,BooleanVariable.getName(mapValue));
					} catch (ClassCastException e) {
						setMethod.invoke(bean,BooleanVariable.getName(mapValue));
					}
				}else if(fieldType==int.class||fieldType==Integer.class){
					try {
						BigDecimal de = (BigDecimal)mapValue;
						setMethod.invoke(bean,de.intValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Integer.parseInt(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==long.class||fieldType==Long.class){
					try {
						BigDecimal de=(BigDecimal)mapValue;
						setMethod.invoke(bean,de.longValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Long.parseLong(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==short.class||fieldType==Short.class){
					try {
						BigDecimal de=(BigDecimal)mapValue;
						setMethod.invoke(bean,de.shortValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Short.parseShort(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==byte.class||fieldType==Byte.class){
					try {
						BigDecimal de=(BigDecimal)mapValue;
						setMethod.invoke(bean,de.byteValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Byte.parseByte(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==float.class||fieldType==Float.class){
					try {
						BigDecimal de=(BigDecimal)mapValue;
						setMethod.invoke(bean,de.floatValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Float.parseFloat(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==double.class||fieldType==Double.class){
					try {
						BigDecimal de=(BigDecimal)mapValue;
						setMethod.invoke(bean,de.doubleValue());
					} catch (ClassCastException e) {
						setMethod.invoke(bean,Double.parseDouble(StringUtil.getStringValue(mapValue)));
					}
				}else if(fieldType==Date.class&&mapValue.getClass()==String.class){
					Date d=DateUtil.parse(StringUtil.getStringValue(mapValue));
					setMethod.invoke(bean,d);
				}else if(mapValue!=null){
					setMethod.invoke(bean,mapValue);
				}
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				continue;
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return bean;
	}
	
	private static Map doWithMap(Map map){
		Map resultMap=new HashMap();
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			Object key=it.next();
			Object value=map.get(key);
			String key1=StringUtil.getStringValue(key);
			resultMap.put(key1.replaceAll("_", "").toLowerCase(), value);
		}
		return resultMap;
	}
	
	public static Map doWithParameterMap(Map map){
		Map resultMap=new HashMap();
		Iterator it=map.keySet().iterator();
		while(it.hasNext()){
			Object key=it.next();
			Object value=map.get(key);
			String key1=StringUtil.getStringValue(key);
			String value1=((String[])value)[0];
			resultMap.put(key1.replaceAll("_", "").toLowerCase(), value1);
		}
		return resultMap;
	}
}
