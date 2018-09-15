package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysPerson;

public class SysPersonAction extends ActionBase<SysPerson> {

	@Override
	public Class beanClazz() {
		return SysPerson.class;
	}
	
	public SysPerson getBean() {
		return _bean;
	}

	public void setBean(SysPerson bean) {
		this._bean = bean;
	}
}
