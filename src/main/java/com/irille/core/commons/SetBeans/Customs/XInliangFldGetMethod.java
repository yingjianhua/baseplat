package com.irille.core.commons.SetBeans.Customs;


import com.irille.core.commons.SetBeans.Core.AbsGetMethodString;

/**
 * Created by IntelliJ IDEA.
 * User: Hellobox
 * Date: 2018/8/20
 * Time: 13:51
 */
public class XInliangFldGetMethod extends AbsGetMethodString {
    @Override
    public String getName() {
        return "irille.shop.*";
    }

    @Override
    public String getMethodString(String o, Class ocla) {
        StringBuffer stringBuffer = new StringBuffer();
        if (o.charAt(0) == '_') {
            stringBuffer.append("get").
                    append(Character.toUpperCase(o.charAt(1)))
                    .append(o.length() > 1 ? o.substring(2) : "");
        } else {
            stringBuffer.append("get").
                    append(Character.toUpperCase(o.charAt(0)))
                    .append(o.length() > 1 ? o.substring(1) : "");
        }
        return stringBuffer.toString();
    }

    @Override
    public String setMethodString(String o, Class ocla) {
        StringBuffer stringBuffer = new StringBuffer();
        if (o.charAt(0) == '_') {
            stringBuffer.append("set").
                    append(Character.toUpperCase(o.charAt(1)))
                    .append(o.length() > 1 ? o.substring(2) : "");
        } else {
            stringBuffer.append("set").
                    append(Character.toUpperCase(o.charAt(0)))
                    .append(o.length() > 1 ? o.substring(1) : "");
        }
        return stringBuffer.toString();
    }
}
