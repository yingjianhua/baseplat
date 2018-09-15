package irille.core.sys;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.ProvDataCtrl;

public class SysDeptDAO {
	public static final Log LOG = new Log(SysDeptDAO.class);

	public static class Ins extends IduIns<Ins, SysDept> {

		@Override
		public void before() {
			super.before();
			getB().stEnabled(true);
			if (getB().getDeptUp() != null) {
				if (!getB().getCode().startsWith(getB().gtDeptUp().getCode()))
					throw LOG.err("codeError", "部门代码[{0}]需要以上级部门代码[{1}]开头", getB().getCode(), getB().gtDeptUp().getCode());
				//getB().setCode(getB().gtDeptUp().getCode() + "." + getB().getCode());
				if (getB().gtDeptUp().getOrg() != getB().getOrg())
					throw LOG.err("orgError", "部门所属机构[{0}]与上级部门所属机构[{1}]不同", getB().gtOrg().getName(), getB().gtDeptUp().gtOrg()
					    .getName());
			}
			if (SysDept.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("notFound", "部门代码[{0}]已存在,不可重复新增!", getB().getCode());
		}

		public void after() {
			super.after();
			ProvDataCtrl.initDeptMap();//将缓存中存储的部门上下级关系更新
		}

	}

	public static class Upd extends IduUpd<Upd, SysDept> {

		public void before() {
			super.before();
			if (getB().getDeptUp() != null)
				if (getB().gtDeptUp().getOrg() != getB().getOrg())
					throw LOG.err("orgError", "部门所属机构[{0}]与上级部门所属机构[{1}]不同", getB().gtOrg().getName(), getB().gtDeptUp().gtOrg()
					    .getName());
			SysDept dbBean = load(getB().getPkey());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysDept.T.PKEY, SysDept.T.CODE, SysDept.T.DEPT_UP,
			    SysDept.T.ORG,SysDept.T.CELL, SysDept.T.ENABLED);
			setB(dbBean);
			BeanBuf.clear(SysDept.class, getB().getPkey());
		}

	}

	public static class Del extends IduDel<Del, SysDept> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(SysDept.class, SysDept.T.DEPT_UP, b.getPkey());
			haveBeUsed(SysEm.class, SysEm.T.DEPT, b.getPkey());
		}

		public void after() {
			super.after();
			ProvDataCtrl.initDeptMap();//将缓存中存储的部门上下级关系更新
			BeanBuf.clear(SysDept.class, getB().getPkey());
		}
	}

}
