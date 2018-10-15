package com.irille.core.controller;

import java.io.Serializable;

import irille.pub.bean.BeanMain;
import irille.pub.util.GenericsUtils;

public abstract class BeanAction<T extends BeanMain<?, ?>, R extends Serializable> {
	
	@SuppressWarnings("unchecked")
	public BeanAction() {
		beanClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	private Class<T> beanClass;
	
	private R id;
	private R pkey;
	private T bean = null;
	
	public final Class<T> beanClazz(){
		return beanClass;
	}
	public final R getId() {
		return id;
	}
	public final void setId(R id) {
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
