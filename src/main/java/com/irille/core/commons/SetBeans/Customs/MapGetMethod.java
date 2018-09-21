package com.irille.core.commons.SetBeans.Customs;


import com.irille.core.commons.SetBeans.Core.AbsGetMethodString;

/**
 * Created by IntelliJ IDEA.
 * User: Hellobox
 * Date: 2018/8/20
 * Time: 13:51
 */
public class MapGetMethod extends AbsGetMethodString {
    @Override
    public String getName() {
        return "java.util.*Map";
    }

    @Override
    public String getMethodString(String o, Class ocla) {
        if (o.charAt(0) == '_') {
            return o.substring(1);
        } else {
            return o;
        }
    }

    @Override
    public String setMethodString(String o, Class ocla) {
        return null;
    }
}
