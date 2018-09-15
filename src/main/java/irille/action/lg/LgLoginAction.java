package irille.action.lg;

import irille.action.ActionBase;
import irille.core.lg.LgLogin;

public class LgLoginAction extends ActionBase<LgLogin> {

	@Override
	public Class beanClazz() {
		return LgLogin.class;
	}

	public LgLogin getBean() {
		return _bean;
	}

	public void setBean(LgLogin bean) {
		this._bean = bean;
	}

	public String orderBy() {
		return " ORDER BY " + LgLogin.FLD_PKEY.getCodeSqlField() + " DESC";
	}
}
