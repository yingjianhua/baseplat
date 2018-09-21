package com.irille.core.commons.SetBeans.Core;

/**
 * Created by IntelliJ IDEA.
 * User: PassXml@gmail.com
 * Date: 2018-06-23
 * Time: 16:24
 */
public class GetMethodString extends AbsGetMethodString {


    @Override
    public String getMethodString(String o, Class ocla) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ocla == Boolean.class ? "is" : "get").
                append(Character.toUpperCase(o.charAt(0)))
                .append(o.length() > 1 ? o.substring(1) : "");
        return stringBuffer.toString();
    }

    @Override
    public String setMethodString(String o, Class ocla) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("set").
                append(Character.toUpperCase(o.charAt(0)))
                .append(o.length() > 1 ? o.substring(1) : "");
        return stringBuffer.toString();
    }
}
