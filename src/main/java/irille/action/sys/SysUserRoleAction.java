package irille.action.sys;

import irille.action.ActionSync;
import irille.core.sys.SysUserRole;
import irille.pub.Log;
import irille.pub.svr.ProvCtrl;
import irille.pub.svr.ProvDataCtrl;

import java.util.List;

public class SysUserRoleAction extends ActionSync<SysUserRole> {
	private static final Log LOG = new Log(SysUserRoleAction.class);

	@Override
	public Class beanClazz() {
		return SysUserRole.class;
	}

	public SysUserRole getBean() {
		return _bean;
	}

	public void setBean(SysUserRole bean) {
		this._bean = bean;
	}

	public List<SysUserRole> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysUserRole> insLines) {
		_insLines = insLines;
	}

	public List<SysUserRole> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysUserRole> updLines) {
		_updLines = updLines;
	}

	public List<SysUserRole> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysUserRole> delLines) {
		_delLines = delLines;
	}

	public void syncBefore() {
		super.syncBefore();
		Integer pkey = Integer.parseInt(getMainPkey());
		if (pkey == 1)
			throw LOG.err("err", "系统管理员帐号不可设置");
		if (getInsLines() != null)
			for (SysUserRole line : getInsLines()) {
				line.setUserSys(pkey);
			}
	}

	@Override
	public void syncAfter() {
		super.syncAfter();
		Integer pkey = Integer.parseInt(getMainPkey());
		ProvCtrl.getInstance().clear(pkey);
		ProvDataCtrl.INST.clear(pkey);
	}

}
