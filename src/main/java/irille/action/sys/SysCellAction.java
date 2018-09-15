package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.pub.bean.Bean;
import irille.pub.bean.CmbBill.T;

import org.json.JSONObject;

public class SysCellAction extends ActionBase<SysCell> {

	@Override
	public Class beanClazz() {
		return SysCell.class;
	}

	public SysCell getBean() {
		return _bean;
	}

	public void setBean(SysCell bean) {
		this._bean = bean;
	}

	@Override
	public String orderBy() {
		return " ORDER BY " + T.CODE.getFld().getCodeSqlField();
	}

}
