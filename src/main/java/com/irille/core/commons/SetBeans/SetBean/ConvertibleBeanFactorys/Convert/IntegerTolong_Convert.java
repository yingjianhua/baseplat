package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 16:08
 */
public class IntegerTolong_Convert implements IConvertBeanFactory<Integer, Long> {


    @Override
    public Long convert(Integer s) {
        return s.longValue();
    }


    @Override
    public String getName() {
        return Tools.getName(Integer.class, long.class);
    }

    @Override
    public Class<Long> getType() {
        return Long.TYPE;
    }


}
