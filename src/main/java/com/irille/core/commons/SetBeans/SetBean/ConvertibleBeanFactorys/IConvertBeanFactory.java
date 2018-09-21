package com.irille.core.commons.SetBeans.SetBean.ConvertibleBeanFactorys;

/**
 * Created by IntelliJ IDEA.
 * User: HelloBox passxml@gmail.com
 * Date: 2018/8/4
 * Time: 11:43
 */
public interface IConvertBeanFactory<S, T> {

    String getName();

    Class<T> getType();

    T convert(S source);

}

