package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import java.sql.Timestamp;
import java.time.ZoneId;
import java.util.Date;

import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/11
 * Time: 9:34
 */
public class TimestampToDateConvert implements IConvertBeanFactory<Timestamp, Date> {
    @Override
    public String getName() {
        return Tools.getName(Timestamp.class, Date.class);
    }

    @Override
    public Class<Date> getType() {
        return Date.class;
    }

    @Override
    public Date convert(Timestamp source) {
        return Date.from(source.toLocalDateTime().atZone(ZoneId.systemDefault()).toInstant());
    }
}
