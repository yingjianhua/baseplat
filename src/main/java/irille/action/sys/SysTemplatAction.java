package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.Sys;
import irille.core.sys.SysTemplat;

public class SysTemplatAction extends ActionBase<SysTemplat> {
	private boolean _isPur = false;
	private boolean _isGl = false;

	@Override
	public Class beanClazz() {
		return SysTemplat.class;
	}

	public SysTemplat getBean() {
		return _bean;
	}

	public void setBean(SysTemplat bean) {
		this._bean = bean;
	}

	@Override
	public String orderBy() {
		return " ORDER BY PKEY";
	}

	//用于销售参数的查询
	public void listPur() throws Exception {
		_isPur = true;
		list();
	}
	
	public void listGl() throws Exception {
		_isGl = true;
		list();
	}

	public String crtAll() {
		String all = "1=1";
		if (_isPur)
			all += " AND " + SysTemplat.T.TYPE.getFld().getCodeSqlField() + " = " + Sys.OTemplateType.PUR.getLine().getKey();
		if (_isGl)
			all += " AND " + SysTemplat.T.TYPE.getFld().getCodeSqlField() + " = " + Sys.OTemplateType.GL.getLine().getKey();
		return all;
	}

}
