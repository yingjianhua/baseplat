package com.irille.core.commons.SetBeans.SetBean.Beans;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 10:38
 */
public class TypeSafeResult {
    private Class setType;
    private Object setValue;

    public Class getSetType() {
        return setType;
    }

    public void setSetType(Class setType) {
        this.setType = setType;
    }

    public Object getSetValue() {
        return setValue;
    }

    public void setSetValue(Object setValue) {
        this.setValue = setValue;
    }
}
