package com.irille.core.commons.SetBeans.Core;

/**
 * Created by IntelliJ IDEA.
 * User: PassXml@gmail.com
 * Date: 2018-06-23
 * Time: 16:20
 */
public abstract class AbsGetMethodString {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public abstract String getMethodString(String o, Class ocla);

    public abstract String setMethodString(String o, Class ocla);
}
