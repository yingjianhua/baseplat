package irille.pss.pur;

import irille.core.sys.SysMenuDAO;
import irille.core.sys.SysTemplat;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Pur_Install extends InstallBase {
	private static final Log LOG = new Log(Pur_Install.class);
	public static final Pur_Install INST = new Pur_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(PurOrder.TB, 10, null); //XXX
		m.proc(PurRev.TB, 20, null);
		m.proc(PurRtn.TB, 30, null);
		m.proc(PurPresent.TB, 40, null);
		m.proc(PurOrderDirect.TB, 100, null);
		m.proc(PurMvIn.TB, 90, null);
		m.proc(PurProt.TB, 50, null);
		m.proc(PurProtApply.TB, 60, null);
		m.proc(PurProtGoods.TB, 70, null);
		m.proc(PurProtGoodsApply.TB, 80, null);
		m.proc(SysTemplat.TB, "采购模板", "pur","view.sys.SysTemplat.PurList", 100, null, "list,ins,upd,del,edit,doEnabled,unEnabled");

	}
}
