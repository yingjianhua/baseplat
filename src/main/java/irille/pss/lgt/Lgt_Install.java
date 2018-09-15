package irille.pss.lgt;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Lgt_Install extends InstallBase {
	private static final Log LOG = new Log(Lgt_Install.class);
	public static final Lgt_Install INST = new Lgt_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
//		m.proc(LgLogin.TB, 10, null);
//		m.proc(LgTran.TB, 20, null);
	}
}
