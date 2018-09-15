package irille.trandb;

import irille.core.prv.PrvRole;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserRole;
import irille.core.sys.SysUserRoleDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 *         2015年6月12日16:50:56 用户角色迁移备注
 *         在迁移中遇到原数据base_role中的几个角色
 *         （1-0607：机构级库管员）改成（1-0602：库管员）
 *         （1-work：工作流管理员）╮
 *         （1-9004：运输商管理员） → 统一跳过不迁移
 *         （1-0608：仓库内部调拨）╯
 */
public class TranPrvRole {

	public static void run() throws Exception {
		runUserRole();
	}

	public static void runUserRole() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM base_user_role ";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==============迁移数据开始=============");
		System.out.println("==========迁移【用户角色信息】=========");
		while (rs.next()) {
			int usercode = rs.getInt("user");
			String role = rs.getString("role");
			role = role.substring(2, role.length());
			SysUser list = SysUser.chkUniqueCode(false, usercode + "");
			if (list != null && !"work".equals(role) && !"9004".equals(role) && !"0608".equals(role)) {
				if ("0607".equals(role)) {
					role = "0602";
				}
				PrvRole role_pkey = PrvRole.chkUniqueCode(false, role);
				SysUserRole userRole = SysUserRole.chkUniqueUserRole(false, list.getPkey(), role_pkey.getPkey());
				if (userRole == null) {
					userRole = new SysUserRole().init();
					userRole.setUserSys(list.getPkey());
					userRole.setRole(role_pkey.getPkey());
					SysUserRoleDAO.Ins userRoleins = new SysUserRoleDAO.Ins();
					userRoleins.setB(userRole);
					userRoleins.commit();
				}
			} else {
				System.err.println("因机构条件没迁移的用户[" + usercode + "]");
			}
		}
		System.out.println("==============迁移数据完成==============");
		TranDb.INST.close(stat, rs);
	}
}
