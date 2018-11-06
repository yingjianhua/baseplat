package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Convert;


import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.IConvertBeanFactory;
import com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys.Tools;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 16:08
 */
public class LongToIntegerConvert implements IConvertBeanFactory<Long, Integer> {


    @Override
    public String getName() {
        return Tools.getName(Long.class, Integer.class);
    }

    @Override
    public Class<Integer> getType() {
        return Integer.class;
    }

    @Override
    public Integer convert(Long source) {
        return source.intValue();
    }
}
