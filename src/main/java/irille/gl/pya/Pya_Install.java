package irille.gl.pya;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Pya_Install extends InstallBase {
	private static final Log LOG = new Log(Pya_Install.class);
	public static final Pya_Install INST = new Pya_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(PyaPayBill.TB, 10, null);
		m.proc(PyaPayWriteoffBill.TB, 20, null);
		m.proc(PyaPayOtherBill.TB, 30, null);
		m.proc(PyaPayOtherWriteoffBill.TB, 40, null);
		m.proc(PyaPayDepBill.TB, 50, null);
		m.proc(PyaPayDepWriteoffBill.TB, 60, null);
	}
}
