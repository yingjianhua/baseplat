package com.irille.core.controller;

import java.io.Serializable;

import com.irille.core.repository.orm.Entity;

import irille.pub.util.GenericsUtils;

public abstract class EntityAction<T extends Entity, R extends Serializable> {
	
	@SuppressWarnings("unchecked")
	public EntityAction() {
		entityClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	private Class<T> entityClass;
	
	private R id;
	private R pkey;
	private T bean = null;
	
	public final Class<T> entityClazz(){
		return entityClass;
	}
	public R getId() {
		return id;
	}
	public void setId(R id) {
		this.id = id;
	}
	public final R getPkey() {
		return pkey;
	}
	public final void setPkey(R pkey) {
		this.pkey = pkey;
	}
	public final T getBean() {
		return bean;
	}
	public final void setBean(T bean) {
		this.bean = bean;
	}
	
}
