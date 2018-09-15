package irille.gl.rva;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Rva_Install extends InstallBase {
	private static final Log LOG = new Log(Rva_Install.class);
	public static final Rva_Install INST = new Rva_Install();

	@Override
	public void initMenu(SysMenuDAO m) {
		//pya 应收的菜单与应付模块放在同一模块下
		m.proc(RvaRecBill.TB, 110, null, "pya");
		m.proc(RvaRecWriteoffBill.TB, 120, null, "pya");
		m.proc(RvaRecOtherBill.TB, 130, null, "pya");
		m.proc(RvaRecOtherWriteoffBill.TB, 140, null, "pya");
		m.proc(RvaRecDepBill.TB, 150, null, "pya");
		m.proc(RvaRecDepWriteoffBill.TB, 160, null, "pya");
	}
}
