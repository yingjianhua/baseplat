package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/11
 * Time: 9:27
 */
public class Tools {
    public static String getName(Class s, Class t) {
        if (s != null && t != null) {
            return s.getName() + "_" + t.getName();
        }
        return null;
    }
}
