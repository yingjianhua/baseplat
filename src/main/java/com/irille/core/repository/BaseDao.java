package com.irille.core.repository;

import irille.pub.bean.BeanMain;
import irille.pub.util.GenericsUtils;

public class BaseDao<T extends BeanMain<?, ?>> extends Query {
	
	private Class<T> beanClass;

	@SuppressWarnings("unchecked")
	public BaseDao() {
			beanClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	public Class<T> beanClass() {
		return beanClass;
	}
}
