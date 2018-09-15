package irille.action.sys;

import irille.action.ActionSync;
import irille.core.prv.PrvRole;
import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.core.sys.SysSupplierOrg;
import irille.pub.Log;
import irille.pub.Obj;
import irille.pub.bean.BeanBase;
import irille.pub.svr.OptCustCtrl;

import java.util.List;
import java.util.Vector;

public class SysSupplierOrgAction extends ActionSync<SysSupplierOrg> {
	private static final Log LOG = new Log(SysSupplierOrgAction.class);

	@Override
	public Class beanClazz() {
		return SysSupplierOrg.class;
	}

	public SysSupplierOrg getBean() {
		return _bean;
	}

	public void setBean(SysSupplierOrg bean) {
		this._bean = bean;
	}

	public List<SysSupplierOrg> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysSupplierOrg> insLines) {
		_insLines = insLines;
	}

	public List<SysSupplierOrg> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysSupplierOrg> updLines) {
		_updLines = updLines;
	}

	public List<SysSupplierOrg> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysSupplierOrg> delLines) {
		_delLines = delLines;
	}

	@Override
	public void syncBefore() {
		super.syncBefore();
		Integer supplier = Integer.parseInt(getMainPkey());

		if (getInsLines() != null)
			for (SysSupplierOrg line : getInsLines()) {
				line.setSupplier(supplier);
			}
	}

	@Override
	public void syncAfter() {
		super.syncAfter();
		Integer supplier = Integer.parseInt(getMainPkey());
		List<SysSupplierOrg> list = BeanBase.list(SysSupplierOrg.class,
				SysSupplierOrg.T.SUPPLIER.getFld().getCodeSqlField() + "="
						+ supplier, false);
		Vector<String> v = new Vector<String>();
		for (SysSupplierOrg Obj : list) {
			if (v.contains(Obj.getOrg().toString())) {
				throw LOG.err("err","机构已存在");
			}
			v.add(Obj.getOrg().toString());
		}
	}
}
