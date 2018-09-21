package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import java.util.Date;

import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/11
 * Time: 9:34
 */
public class DateoDateConvert implements IConvertBeanFactory<java.sql.Date, Date> {
    @Override
    public String getName() {
        return Tools.getName(java.sql.Date.class, Date.class);
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    public Date convert(java.sql.Date source) {
        return new Date(source.getTime());
    }
}
