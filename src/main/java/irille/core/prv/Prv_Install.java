package irille.core.prv;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Prv_Install extends InstallBase {
	private static final Log LOG = new Log(Prv_Install.class);
	public static final Prv_Install INST = new Prv_Install();

	/*
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		SysMenu up = m.procParent("权限管理", "sys", 400, null);
		m.proc(PrvRole.TB,  "功能权限管理", "sys", 401, up);
		m.proc(PrvRoleTran.TB,  "资源权限管理", "sys", 402, up);
		m.proc(PrvTranGrp.TB, 403, up, "sys");
		m.proc(PrvTranData.TB, 404, up, "sys");
	}
}
