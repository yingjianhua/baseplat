package irille.pss.cst;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Cst_Install extends InstallBase {
	private static final Log LOG = new Log(Cst_Install.class);
	public static final Cst_Install INST = new Cst_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(CstSalInvoice.TB, 10, null);
		m.proc(CstSalInvoiceRed.TB, 20, null);
		m.proc(CstPurInvoice.TB,	30, null);
		m.proc(CstPurInvoiceRed.TB, 40, null);
		m.proc(CstMvInvoiceIn.TB, 50, null);
		m.proc(CstMvInvoiceOut.TB, 60, null);
		m.proc(CstOut.TB, 70, null);
		m.proc(CstOutRed.TB, 80, null);
		m.proc(CstIn.TB, 90, null);
		m.proc(CstInRed.TB, 100, null);
	}
}
