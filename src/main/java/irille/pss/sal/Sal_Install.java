package irille.pss.sal;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Sal_Install extends InstallBase {
	private static final Log LOG = new Log(Sal_Install.class);
	public static final Sal_Install INST = new Sal_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(SalOrder.TB, 10, null);
		m.proc(SalSale.TB, 20, null); 
		m.proc(SalSaleDirect.TB, 50, null);
		m.proc(SalRtn.TB, 60, null); 
		m.proc(SalPresent.TB, 70, null); 
		m.proc(SalReserve.TB, 80, null); 
		m.proc(SalMvOut.TB, 190, null);
		m.proc(SalDiscountPriv.TB, 181, null);	
		m.proc(SalPriceProt.TB, 200, null);
		m.proc(SalPriceProtMv.TB, 201, null);
		m.proc(SalCustGoods.TB, 210, null);
		m.proc(SalCollect.TB, 220, null);
	}
}
