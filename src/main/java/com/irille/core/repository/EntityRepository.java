package com.irille.core.repository;

import com.irille.core.repository.orm.Entity;

import irille.pub.util.GenericsUtils;

public class EntityRepository<T extends Entity> extends Query2 {
	
	private Class<T> beanClass;

	@SuppressWarnings("unchecked")
	public EntityRepository() {
			beanClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	public Class<T> beanClass() {
		return beanClass;
	}
}
