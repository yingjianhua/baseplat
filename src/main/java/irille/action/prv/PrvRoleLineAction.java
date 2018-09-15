package irille.action.prv;

import irille.action.ActionSync;
import irille.core.prv.PrvRoleLine;
import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.pub.bean.BeanBase;
import irille.pub.svr.OptCustCtrl;
import irille.pub.svr.ProvCtrl;

import java.util.List;

public class PrvRoleLineAction extends ActionSync<PrvRoleLine> {

	@Override
	public Class beanClazz() {
		return PrvRoleLine.class;
	}

	public PrvRoleLine getBean() {
		return _bean;
	}

	public void setBean(PrvRoleLine bean) {
		this._bean = bean;
	}

	public List<PrvRoleLine> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<PrvRoleLine> insLines) {
		_insLines = insLines;
	}

	public List<PrvRoleLine> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<PrvRoleLine> updLines) {
		_updLines = updLines;
	}

	public List<PrvRoleLine> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<PrvRoleLine> delLines) {
		_delLines = delLines;
	}

	public void syncBefore() {
		super.syncBefore();
		Integer pkey = Integer.parseInt(getMainPkey());
		if (getInsLines() != null)
			for (PrvRoleLine line : getInsLines()) {
				line.setRole(pkey);
			}
	}
	
	@Override
	public void syncAfter() {
	  super.syncAfter();
	  ProvCtrl.getInstance().clearAll();
	}

}
