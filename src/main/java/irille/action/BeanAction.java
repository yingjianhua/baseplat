package irille.action;

import java.io.Serializable;

import irille.pub.bean.BeanMain;
import irille.pub.util.GenericsUtils;

public abstract class BeanAction<T extends BeanMain<?, ?>, R extends Serializable> extends BaseAction {
	
	private static final long serialVersionUID = -3099350026313996838L;

	@SuppressWarnings("unchecked")
	public BeanAction() {
		beanClass = (Class<T>)GenericsUtils.getSuperClassGenricType(getClass());
	}
	private Class<T> beanClass;
	
	private R pkey;
	private T bean = null;
	
	public final Class<T> beanClazz(){
		return beanClass;
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
