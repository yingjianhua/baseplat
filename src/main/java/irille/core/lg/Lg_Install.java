package irille.core.lg;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuDAO;
import irille.gl.frm.FrmHandover;
import irille.gl.frm.FrmLink;
import irille.gl.frm.FrmPending;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Lg_Install extends InstallBase {
	private static final Log LOG = new Log(Lg_Install.class);
	public static final Lg_Install INST = new Lg_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		SysMenu up = m.procParent("系统日志", "sys", 900, null);
		m.proc(LgLogin.TB, 910, up, "sys");
		m.proc(LgTran.TB, 920, up, "sys");
	}
}
