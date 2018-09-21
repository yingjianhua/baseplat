package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import java.util.regex.Pattern;

import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 16:08
 */
public class StringToLongConvert implements IConvertBeanFactory<String, Long> {

    private final Pattern pattern = Pattern.compile("[0-9]{1,}");

    @Override
    public Long convert(String var) {
        if (var == null) {
            return null;
        }
        if (var.length() < 1) {
            return null;
        }
        if (pattern.matcher(var).matches()) {
            Long result =
                    Long.parseLong(var);
            return result;
        }
        return null;
    }


    @Override
    public String getName() {
        return Tools.getName(String.class, Long.class);
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }


}
