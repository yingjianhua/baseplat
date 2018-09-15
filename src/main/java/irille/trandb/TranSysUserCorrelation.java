package irille.trandb;

import irille.core.sys.Sys;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysEmDAO;
import irille.core.sys.SysOrg;
import irille.core.sys.SysPerson;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserLogin;
import irille.core.sys.SysUserRole;
import irille.core.sys.SysUserRoleDAO;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-15 09:31:07
 * 迁移数据: `base_user`
 * 迁移条件: `org` NOT IN ('11001','11009','11010','11011','11012') AND `enabled` <> 2 AND `name` NOT LIKE '%已删除%'
 */
public class TranSysUserCorrelation {
	/**
	 * 初始化职员、用户、个人信息为admin
	 * @throws Exception
	 */
	public static void run() throws Exception {
		DelAndAdd();
		runUserClass();
		runUser();
	}

	public static void runUserClass() {
		SysEm em = new SysEm().init();
		em.setName("管理员");
		em.setNickname("管理员");
		em.setDept(SysDept.chkUniqueCode(false, "11004").getPkey()); // 默认管理员更改为11004的用户
		em.setOrg(em.gtDept().getOrg());
		em.setCode("admin");
		SysEmDAO.Ins emins = new SysEmDAO.Ins();
		emins.setB(em);
		emins._person = new SysPerson().init();
		emins._loginName = "admin";
		emins.commit();
		SysUser user = SysUser.chkUniqueCode(false, "admin");
		user.stLoginState(Sys.OLoginState.LOGOUT);
		user.upd();
		SysUserRole userRole = new SysUserRole().init();
		userRole.setUserSys(user.getPkey());
		userRole.setRole(1);
		SysUserRoleDAO.Ins userRoleIns = new SysUserRoleDAO.Ins();
		userRoleIns.setB(userRole);
		userRoleIns.commit();
		System.out.println("管理员创建成功");
	}

	/**
	 * 使用SysEm中的ins()方法同时创建用户表数据和个人信息数据
	 * @throws Exception
	 */
	public static void runUser() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `base_user` WHERE `org` NOT IN ('11001','11009','11010','11011','11012') AND `enabled` <> 2 AND `name` NOT LIKE '%已删除%';";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("=============数据迁移开始==========");
		System.out.println("======迁移【用户、职员、个人信息】======");
		while (rs.next()) {
			byte gender = rs.getByte("gender");
			byte certificate_type = rs.getByte("certificate_type");
			int salutation = rs.getInt("salutation");
			String oldId = rs.getString("id");
			String name = rs.getString("name");
			String org = rs.getString("org");
			// 获取dept时检查是否符合新数据的格式
			String dept = rs.getString("dept").replaceAll("--", "-").replaceAll("-", "");
			// 字符串中转站:把原数据的格式转换成行数据格式
			String certificate_numb = rs.getString("certificate_numb");
			String mobile = rs.getString("mobile");
			String tel1 = rs.getString("tel1");
			String fax = rs.getString("fax");
			String address = rs.getString("address");
			String zip_code = rs.getString("zip_code");
			String remark = rs.getString("zip_code");
			// 根据格式好的code获取pkey
			SysOrg orgObj = SysOrg.chkUniqueCode(false, org);
			SysDept deptObj = SysDept.chkUniqueCode(false, dept);
			if (deptObj == null) {
				System.err.println("因错误数据导致没有本机部门,所以查找上级部门");
				System.err.println("该部门代码: ["+dept+"]");
				deptObj = SysDept.chkUniqueCode(false, dept.substring(0, dept.length() - 2));
			}
			SysEm em = SysEm.chkUniqueCode(false, oldId);
			if (em == null) {
				em = new SysEm().init();
				em.setName(name);
				em.setDept(deptObj.getPkey());
				em.setOrg(orgObj.getPkey());
				em.setCode(oldId);
				SysEmDAO.Ins emins = new SysEmDAO.Ins();
				emins.setB(em);
				emins._person = new SysPerson().init();
				emins._person.setPeSex(gender);
				emins._person.setPeCardType(certificate_type);
				emins._person.setPeCardNumb(certificate_numb);
				emins._person.setPeMobile(mobile);
				emins._person.setOfTel(tel1);
				emins._person.setOfFax(fax);
				emins._person.setOfAddr(address);
				emins._person.setOfZipCode(zip_code);
				emins._person.setRem(remark);
				emins._loginName = oldId;
				emins.commit();
				SysUser user = SysUser.chkUniqueCode(false, oldId);
				user.stLoginState(Sys.OLoginState.LOGOUT);
				user.upd();
			}else{
				System.err.println("职员: ["+oldId+"]已存在");
			}
		}
		System.out.println("===========数据迁移完成==========");
		TranDb.INST.close(stat, rs);
	}

	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SysEm.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysEm.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysUser.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysUser.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysPerson.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysPerson.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysUserLogin.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysUserLogin.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(SysUserRole.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SysUserRole.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SysEm.class);
		new DbMysql().db(SysUser.class);
		new DbMysql().db(SysPerson.class);
		new DbMysql().db(SysUserLogin.class);
		new DbMysql().db(SysUserRole.class);
	}
}
