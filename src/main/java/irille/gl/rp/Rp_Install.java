package irille.gl.rp;

import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Rp_Install extends InstallBase {
	private static final Log LOG = new Log(Rp_Install.class);
	public static final Rp_Install INST = new Rp_Install();

	/*
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(RpJournal.TB, 10, null);
		m.proc(RpStimatePay.TB, 20, null);
		m.proc(RpStimateRec.TB, 30, null);
		m.proc(RpNotePayBill.TB, 40, null);
		m.proc(RpNoteRptBill.TB, 50, null);
		m.proc(RpFundMv.TB, 60, null);
		m.proc(RpFundMvIn.TB, 62, null);
		m.proc(RpFundMvOut.TB, 64, null);
		m.proc(RpHandover.TB, 70, null);
		m.proc(RpSeal.TB, 80, null);
		m.proc(RpWorkBox.TB, 90, null);
	}
}
