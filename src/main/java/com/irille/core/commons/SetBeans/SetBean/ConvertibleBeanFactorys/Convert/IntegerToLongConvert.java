package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 16:08
 */
public class IntegerToLongConvert implements IConvertBeanFactory<Integer, Long> {


    @Override
    public String getName() {
        return Tools.getName(Integer.class, Long.class);
    }

    @Override
    public Class<Long> getType() {
        return Long.class;
    }

    @Override
    public Long convert(Integer source) {
        return source.longValue();
    }
}
