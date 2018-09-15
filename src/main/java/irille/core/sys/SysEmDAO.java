package irille.core.sys;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class SysEmDAO {
	public static final Log LOG = new Log(SysEmDAO.class);

	// 先增加用户、职员、个人信息
	public static class Ins extends IduIns<Ins, SysEm> {
		public SysPerson _person = null;
		public String _loginName = null;

		@Override
		public void before() {
			super.before();
			if (SysEm.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("notFound", "工号[{0}]已存在,不可重复新增!", getB().getCode());
		}

		@Override
		public void after() {
			super.after();
			_person.setPkey(getB().gtLongPkey());
			_person.setName(getB().getName());
			SysPersonDAO.Ins ins = new SysPersonDAO.Ins();
			ins.setB(_person);
			ins.commit();

			SysUser user = SysUserDAO.insByEM(getB(), _loginName);
			getB().setUserSys(user.getPkey());
			getB().upd();
		}

	}

	public static class Upd extends IduUpd<Upd, SysEm> {
		public SysPerson _person = null;

		public void before() {
			super.before();
			SysEm dbBean = load(getB().getPkey());
			_person.setPkey(getB().gtLongPkey());
			_person.setName(getB().getName());
			_person.setRowVersion(dbBean.getRowVersion());
			// 先修改单位信息
			SysPersonDAO.Upd upd = new SysPersonDAO.Upd();
			upd.setB(_person);
			upd.commit();
			// 同步用户中的名称信息
			SysUser user = dbBean.gtUserSys();
			user.setName(getB().getName());
			user.setNickname(getB().getNickname());
			user.setDept(getB().getDept());
			user.setOrg(getB().getOrg());
			user.upd();
			//修改职员
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysEm.T.PKEY, SysEm.T.USER_SYS);
			setB(dbBean);
			BeanBuf.clear(SysEm.class, getB().getPkey());
		}
		
		@Override
		public void valid() {
			if (getB().gtDept().getOrg().equals(getB().getOrg()) == false)
				throw LOG.err("deptNotInOrg","部门[{0}]不属于机构[{1}]", getB().gtDept().getName(), getB().gtOrg().getName());
		}

	}

	public static class Del extends IduDel<Del, SysEm> {

		@Override
		public void run() {
		}

	}

}