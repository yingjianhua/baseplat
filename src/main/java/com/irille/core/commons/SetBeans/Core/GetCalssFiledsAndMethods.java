package com.irille.core.commons.SetBeans.Core;


import com.irille.core.commons.SetBeans.Customs.XInliangFldGetMethod;
import com.irille.core.commons.SetBeans.SetBean.Utils.StringUtils;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox Passxml@gmail.com
 * Date: 2018/7/7
 * Time: 10:52
 */
public class GetCalssFiledsAndMethods {
    private static volatile ConcurrentHashMap<String, Method> methodMap;
    private static volatile ConcurrentHashMap<Class, Field[]> fieldMap;
    private static volatile ConcurrentLinkedQueue<AbsGetMethodString> getMethods;
    private static AbsGetMethodString defaultGetMethod;

    static {
        if (methodMap == null) {
            methodMap = new ConcurrentHashMap();
        }
        if (fieldMap == null) {
            fieldMap = new ConcurrentHashMap();
        }
        if (getMethods == null) {
            getMethods = new ConcurrentLinkedQueue();
            defaultGetMethod = new GetMethodString();
            getMethods.add(new XInliangFldGetMethod());
        }
    }

    public static void addGetMethod(AbsGetMethodString getMethodString) {
        getMethods.add(getMethodString);
    }

    public static ConcurrentLinkedQueue getAbsGetMethods() {
        return getMethods;
    }


    public static String getMethodName(Object get, String s, Class<?> type) {
        for (AbsGetMethodString getMethod : getMethods) {
            if (StringUtils.isMatch(getMethod.getName(), get.getClass().getName())) {
                return getMethod.getMethodString(s, type);
            }
        }
        return defaultGetMethod.getMethodString(s, type);
    }

    public static String setMethodName(Object get, String s, Class<?> type) {
        for (AbsGetMethodString getMethod : getMethods) {
            if (StringUtils.isMatch(getMethod.getName(), get.getClass().getName())) {
                return getMethod.setMethodString(s, type);
            }
        }
        return defaultGetMethod.setMethodString(s, type);
    }

    public static AbsGetMethodString getDefaultGetMethod() {
        return defaultGetMethod;
    }

    public static void setDefaultGetMethod(AbsGetMethodString defaultGetMethod) {
        GetCalssFiledsAndMethods.defaultGetMethod = defaultGetMethod;
    }

    public static Method getMethod(Object object, String methodName, Class<?>... type) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(object.getClass().getName()).append("_")
                .append(methodName);
        StringBuffer typeBuffer = new StringBuffer();
        if (type != null) {
            for (Class<?> aClass : type) {
                typeBuffer.append("_");
                typeBuffer.append(aClass.getName());
            }
        }
        String key = buffer.toString() + typeBuffer.toString();
        Method result = methodMap.get(key);
        if (result != null) {
            return result;
        }
        Class aClass = object.getClass();
        do {
            try {
                result = aClass.getDeclaredMethod(methodName, type);
                methodMap.put(key, result);
                return result;
            } catch (NoSuchMethodException e) {
                aClass = aClass.getSuperclass();
//                e.printStackTrace();
            }
        } while (aClass != Object.class);
        return result;
    }


    public static Field[] getFidlds(Class c) {
        Field[] result = fieldMap.get(c);
        if (result == null) {
            Class aClass = c;
            do {
                result = CoppArray(result, aClass.getDeclaredFields());
                if (aClass == Object.class) {
                    break;
                } else {
                    aClass = aClass.getSuperclass();
                }
            } while (true);
        }
        return result;
    }

    public static Field[] getFidlds(Object c) {
        return getFidlds(c.getClass());
    }

    /***
     *
     * @param a1
     * @param a2
     * @param <T>
     * @return
     */
    public static <T> T[] CoppArray(T[] a1, T[] a2) {
        if (a1 == null && a2 != null) {
            return a2;
        }
        if (a1 != null && a2 == null) {
            return a1;
        }
        if (a1 == null && a2 == null) {
            return null;
        }
        T[] result = (T[]) Array.newInstance(a1.getClass().getComponentType(), a1.length + a2.length);
        System.arraycopy(a1, 0, result, 0, a1.length);
        System.arraycopy(a2, 0, result, a1.length, a2.length);
        return result;
    }

//    public static <T> T getValue(Object o, String methodType, Field field, Class<T> type) throws InvocationTargetException, IllegalAccessException {
//        return (T) getMethod(o, getAbsGetMethodString().getMethodString(methodType, field.getName()), type).invoke(o, null);
//    }
}
