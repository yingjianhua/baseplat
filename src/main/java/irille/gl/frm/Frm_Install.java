package irille.gl.frm;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Frm_Install extends InstallBase {
	private static final Log LOG = new Log(Frm_Install.class);
	public static final Frm_Install INST = new Frm_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
//		m.proc(LgLogin.TB, 10, null);
//		m.proc(LgTran.TB, 20, null);
		SysMenu frm = m.procParent("单据管理", "sys", 500, null);
		m.proc(FrmHandover.TB, 510, frm, "sys");
		m.proc(FrmLink.TB, 520, frm, "sys");
		m.proc(FrmPending.TB, 530, frm, "sys");
	}
}
