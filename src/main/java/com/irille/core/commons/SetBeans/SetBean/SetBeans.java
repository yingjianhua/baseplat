package com.irille.core.commons.SetBeans.SetBean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.irille.core.commons.SetBeans.Core.AbsGetMethodString;
import com.irille.core.commons.SetBeans.Core.GetCalssFiledsAndMethods;
import com.irille.core.commons.SetBeans.SetBean.Annotations.SetBean;
import com.irille.core.commons.SetBeans.SetBean.Beans.TypeSafeResult;
import com.irille.core.commons.SetBeans.SetBean.TypeSafe.DefaultTypeSafe;
import com.irille.core.commons.SetBeans.SetBean.TypeSafe.ITypeSafe;

public abstract class SetBeans {
    private static ITypeSafe typeSafe;
//    private static BloomFilter bloomFilter;

    static {
        if (typeSafe == null) {
            typeSafe = new DefaultTypeSafe();
        }
    }

    public static ITypeSafe getTypeSafe() {
        return typeSafe;
    }

    public static void setTypeSafe(ITypeSafe typeSafe) {
        SetBeans.typeSafe = typeSafe;
    }

    public static void set(Object get, Object set, Class label) {
        if (get == null) {
            return;
        }
        Field[] fields = label != null ? GetCalssFiledsAndMethods.getFidlds(label) : GetCalssFiledsAndMethods.getFidlds(set);
        for (Field field : fields) {
            try {
                SetBean setBean = field.getAnnotation(SetBean.class);
                String getMethodString = null;
                String setMethodString = null;
                Object getValue;
                do {
                    if (setBean == null) {
                        break;
                    }
                    if (setBean.NotSet()) {
                        break;
                    }
                    if (setBean.OriginalField().length() > 0) {
                        if (get instanceof Map) {
                            getMethodString = setBean.OriginalField();
                        } else {
                            if (label != null) {
                                setMethodString = GetCalssFiledsAndMethods.setMethodName(set, setBean.OriginalField(), field.getType());
                                getMethodString = GetCalssFiledsAndMethods.getMethodName(get, field.getName(), field.getType());
                            } else {
                                getMethodString = GetCalssFiledsAndMethods.getMethodName(get, setBean.OriginalField(), field.getType());
                            }
                        }
                    }
                    if (setBean.GetMethod().length() > 0) {
                        getMethodString = setBean.GetMethod();
                    }
                    if (setBean.SetMethod().length() > 0) {
                        setMethodString = setBean.SetMethod();
                    }
                } while (false);
                if (getMethodString == null) {
                    if (get instanceof Map) {
                        getMethodString = field.getName();
                    } else {
                        getMethodString = GetCalssFiledsAndMethods.getMethodName(get, field.getName(), field.getType());
                    }
                }
                if (setMethodString == null) {
                    setMethodString = GetCalssFiledsAndMethods.setMethodName(set, field.getName(), field.getType());
                }
                do {
                    if ((getValue = getValue(getMethodString, get)) == null) {
                        break;
                    }
                    TypeSafeResult result = typeSafe.run(getValue, field.getType());
                    Method setMethod = GetCalssFiledsAndMethods.getMethod(set, setMethodString, result.getSetType());
                    if (setMethod != null) {
                        setMethod.invoke(set, result.getSetValue());
                    }
                } while (false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //    private static
    public static void addGetMethod(AbsGetMethodString getMethodString) {
        GetCalssFiledsAndMethods.addGetMethod(getMethodString);
    }

    public static void removeGetMethod(AbsGetMethodString getMethodString) {
        GetCalssFiledsAndMethods.getAbsGetMethods().remove(getMethodString);
    }


    public static Object getValue(String methodName, Object obj) {
        if (obj instanceof Map && methodName != null) {
            return ((Map) obj).get(methodName);
        }

        Method getMethod = GetCalssFiledsAndMethods.getMethod(obj, methodName, null);
        if (getMethod == null) {
            return null;
        }
        try {
            return getMethod.invoke(obj, null);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T set(Object get, Class<T> set) {
        return set(get, set, null);
    }


    public static <T> T set(Object get, Class<T> set, Class label) {
        T setObj = null;
        try {
            setObj = set.newInstance();
        } catch (InstantiationException e) {
            try {
                Constructor constructor = set.getDeclaredConstructor();
                constructor.setAccessible(true);
                setObj = (T) constructor.newInstance();
            } catch (NoSuchMethodException e1) {
                e1.printStackTrace();
            } catch (IllegalAccessException e1) {
                e1.printStackTrace();
            } catch (InstantiationException e1) {
                e1.printStackTrace();
            } catch (InvocationTargetException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        set(get, setObj, label);
        return setObj;
    }

    public static <T> List<T> setList(List get, Class<T> set) {
        List result = new ArrayList();
        for (Object o : get) {
            result.add(set(o, set));
        }
        return result;
    }


}


