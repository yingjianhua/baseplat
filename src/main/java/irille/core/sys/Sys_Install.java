package irille.core.sys;

import irille.core.prv.PrvRole;
import irille.core.prv.PrvRoleDAO;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.InstallBase;
import irille.pub.svr.Env;
import irille.pub.svr.LoginUserMsg;

public class Sys_Install extends InstallBase {
	private static final Log LOG = new Log(Sys_Install.class);
	public static final Sys_Install INST = new Sys_Install();

	/*
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(SysOrg.TB,"机构信息","sys","view.sys.SysOrg.List",10,null,"list,ins,upd,del,doEnabled,unEnabled");
		m.proc(SysDept.TB, 20, null);
		m.proc(SysEm.TB, 30, null);
		m.proc(SysUser.TB, 40, null);
		m.proc(SysTemplat.TB, "财务模板", "sys","view.sys.SysTemplat.List", 60, null, "list,ins,upd,del,doEnabled,unEnabled");
		m.proc(SysCell.TB, 70, null);
		//m.proc(SysSeq.TB, 80, null);
		//m.proc(SysCtype.TB, 90, null);
		m.proc(SysSupplier.TB, 100, null);
		m.proc(SysCustom.TB, 110, null);
	}

	public void installAfter() {
		super.installAfter();
		if(BeanBase.list(SysUser.class, null, false).size()>0) return;
		SysUser lu = new SysUser();
		lu.setPkey(1);
		LoginUserMsg msg = new LoginUserMsg(lu,(byte)0,null,null,null);
		Env.INST.initTran(msg, null);
		//基础表数据初始化
		//模板
		SysTemplat temp = SysTemplat.chkUniqueCodeYear(false, "T01", (short) 2015, Sys.OTemplateType.GL.getLine().getKey());
		if (temp == null) {
			temp = new SysTemplat().init();
			temp.setCode("T01");
			temp.setName("标准模板");
			temp.setYear((short) 2015);
			SysTemplatDAO.Ins act = new SysTemplatDAO.Ins();
			act.setB(temp);
			act.commit();
		}
		//机构及单位信息
		SysOrg org = SysOrg.chkUniqueCode(false, "101");
		if (org == null) {
			org = new SysOrg().init();
			org.setCode("101");
			org.setName("律雅视觉文化传播（上海）有限公司");
			org.setShortName("律雅视觉");
			org.stTemplat(temp);
			SysOrgDAO.Ins orgins = new SysOrgDAO.Ins();
			orgins.setB(org);
			orgins._com = new SysCom().init();
			orgins.commit();
		}
		//部门
		SysDept dept = SysDept.chkUniqueCode(false, "10101");
		if (dept == null) {
			dept = new SysDept().init();
			dept.setCode("10101");
			dept.setName("财务部");
			dept.stOrg(org);
			SysDeptDAO.Ins act = new SysDeptDAO.Ins();
			act.setB(dept);
			act.commit();
		}
		//职员、用户、个人信息
		SysEm em = SysEm.chkUniqueCode(false, "001");
		if (em == null) {
			em = new SysEm().init();
			em.setCode("001");
			em.setName("管理员");
			em.stOrg(org);
			em.stDept(dept);
			SysPerson person = new SysPerson().init();
			SysEmDAO.Ins act = new SysEmDAO.Ins();
			act.setB(em);
			act._loginName = "admin";
			act._person = person;
			act.commit();
			SysUser admin = em.gtUserSys();
			admin.stLoginState(Sys.OLoginState.LOGOUT);
			admin.upd();
		}
		//核算单元
		SysCell cell = BeanBase.chk(SysCell.class, 1);
		if (cell == null) {
			cell = new SysCell();
			cell.setPkey(1);
			cell.setName("上海单元");
			cell.setYear((short)2015);
			cell.stOrg(org);
			cell.setTemplat(temp.getPkey());
			SysCellDAO.Ins act = new SysCellDAO.Ins();
			act.setB(cell);
			act.commit();
		}
		
		//角色
		SysUser user = em.gtUserSys();
		PrvRole role = PrvRole.chkUniqueCode(false, "admin");
		if (role == null) {
			role = new PrvRole().init();
			role.setPkey(1);
			role.setCode("admin");
			role.setName("系统管理员");
			role.stCell(cell);
			PrvRoleDAO.Ins act = new PrvRoleDAO.Ins();
			act.setB(role);
			act.commit();
		}
		//用户角色
		SysUserRole rel = SysUserRole.chkUniqueUserRole(false, user.getPkey(), role.getPkey());
		if (rel == null) {
			rel = new SysUserRole();
			rel.setUserSys(user.getPkey());
			rel.setRole(role.getPkey());
			rel.ins();
		}
	}
}
