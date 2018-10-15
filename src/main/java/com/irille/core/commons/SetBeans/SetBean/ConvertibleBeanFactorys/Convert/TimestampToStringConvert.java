package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: lijie@shoestp.cn
 * Date: 2018/8/11
 * Time: 10:25
 */
public class TimestampToStringConvert implements IConvertBeanFactory<Timestamp, String> {
    @Override
    public String getName() {
        return Tools.getName(Timestamp.class, String.class);
    }

    @Override
    public Class<String> getType() {
        return String.class;
    }

    @Override
    public String convert(Timestamp date) {
        if (date != null && date.getTime() > 0) {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            return simpleDateFormat.format(date);
        }
        return null;
    }
}
