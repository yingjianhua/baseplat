package irille.action.sys;

import irille.action.ActionSync;
import irille.core.sys.SysCustomOrg;
import irille.pub.bean.BeanBase;

import java.util.List;
import java.util.Vector;

public class SysCustomOrgAction extends ActionSync<SysCustomOrg> {

	@Override
	public Class beanClazz() {
		return SysCustomOrg.class;
	}
	
	public SysCustomOrg getBean() {
		return _bean;
	}

	public void setBean(SysCustomOrg bean) {
		this._bean = bean;
	}

	public List<SysCustomOrg> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysCustomOrg> insLines) {
		_insLines = insLines;
	}

	public List<SysCustomOrg> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysCustomOrg> updLines) {
		_updLines = updLines;
	}

	public List<SysCustomOrg> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysCustomOrg> delLines) {
		_delLines = delLines;
	}
	
	public void syncBefore() {
		super.syncBefore();
		if (getInsLines() != null)
			for (SysCustomOrg line : getInsLines()) {
				line.setCustom(Integer.parseInt(getMainPkey()));
			}
	}
	@Override
	public void syncAfter() {
		super.syncAfter();
		Integer supplier = Integer.parseInt(getMainPkey());
		List<SysCustomOrg> list = BeanBase.list(SysCustomOrg.class,
				SysCustomOrg.T.CUSTOM.getFld().getCodeSqlField()+"="+supplier, false);
		Vector<String> v = new Vector<String>();
		for (SysCustomOrg Obj : list) {
			if (v.contains(Obj.getOrg().toString())) {
				throw LOG.err("err","机构已存在");
			}
			v.add(Obj.getOrg().toString());
		}
	}

	@Override
	public String orderBy() {
		return " ORDER BY " + SysCustomOrg.T.ORG.getFld().getCodeSqlField();
	}

}
