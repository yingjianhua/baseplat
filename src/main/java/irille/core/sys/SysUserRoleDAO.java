//Created on 2005-9-27
package irille.core.sys;

import irille.core.prv.PrvRole;
import irille.core.prv.PrvRoleDAO;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.util.List;
import java.util.Vector;

public class SysUserRoleDAO {
	public static final Log LOG = new Log(SysUserRoleDAO.class);

	public static String WHERE_GET_ROLES = Idu.sqlString("{0}=?", SysUserRole.T.USER_SYS);

	//取用户的所有子角色
	public static Vector<Integer> listAllRoleByUser(SysUser user) {
		Vector<Integer> vec = new Vector(); //所有角色集合
		List<SysUserRole> list = BeanBase.list(SysUserRole.class, false, WHERE_GET_ROLES, 0, 0, user.getPkey());
		for (SysUserRole line : list) {
			PrvRoleDAO.getAllRoles(line.getRole(), vec);
		}
		return vec;
	}

	public static Vector<PrvRole> listRoleByUser(SysUser user) {
		Vector<PrvRole> vec = new Vector();
		List<SysUserRole> list = BeanBase.list(SysUserRole.class, false, WHERE_GET_ROLES, 0, 0, user.getPkey());
		for (SysUserRole line : list)
			vec.add(line.gtRole());
		return vec;
	}

	public static class Upd extends IduUpd<Upd, SysUserRole> {
		@Override
		public void log() {
		}

		@Override
		public void valid() {
			super.valid();
			SysUserRole dbBean = BeanBase.load(SysUserRole.class, getB().getPkey());
			if (dbBean.getRole().equals(getB().getRole()) == false)
				if (SysUserRole.chkUniqueUserRole(false, getB().getUserSys(), getB().getRole()) != null)
					throw LOG.err("isExist", "用户[{0}]的角色[{1}]已存在!", getB().gtUserSys().getName(), getB().gtRole().getName());
		}

	}

	public static class Ins extends IduIns<Ins, SysUserRole> {
		@Override
		public void log() {
		}

		@Override
		public void valid() {
			super.valid();
			if (SysUserRole.chkUniqueUserRole(false, getB().getUserSys(), getB().getRole()) != null)
				throw LOG.err("isExist", "用户[{0}]的角色[{1}]已存在!", getB().gtUserSys().getName(), getB().gtRole().getName());
		}
	}

	public static class Del extends IduDel<Del, SysUserRole> {
		@Override
		public void log() {
		}
	}
}