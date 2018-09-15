package irille.gl.gl;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuDAO;
import irille.core.sys.SysOrg;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Gl_Install extends InstallBase {
	private static final Log LOG = new Log(Gl_Install.class);
	public static final Gl_Install INST = new Gl_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(GlJournal.TB, 10, null);
		m.proc(GlNote.TB, "凭条管理", "gl", 23, null);
		m.proc(GlDaybook.TB, 30, null);
		m.proc(GlDailyLedger.TB, 40, null);
		m.proc(GlTransform.TB, 50, null);
		m.proc(GlNote.TB, "销账管理", "gl", "view.gl.GlWriteoff.List", 60, null, "list");
		m.proc(GlSubject.TB, 70, null);
		m.proc(GlSubjectMap.TB, 80, null);
		m.proc(GlCarryOver.TB, "损益结转定义", "gl", 90, null);
		m.proc(GlGoods.TB, 100, null);
//	m.proc(GlRateType.TB, 80, null);
//	m.proc(GlRate.TB, 90, null);
//	m.proc(GlNoteAmtCancel.TB, 110, null);
//	m.proc(GlNoteWriteoff.TB, 120, null);
		SysMenu up = m.procParent("财务报表", "gl", 150, null);
		m.proc(GlAgeRvaView.TB, 160, up);
		m.proc(GlAgePyaView.TB, 165, up);
		m.proc(GlReportAsset.TB, 170, up);
		m.proc(GlReportProfit.TB, 175, up);
		SysMenu up2 = m.procParent("报表设置", "gl", 200, up);
		m.proc(GlReport.TB, "资产负债表设置", "gl", "view.gl.GlReport.ListZ", 210, up2, "list");
		m.proc(GlReport.TB, "利润表设置", "gl", "view.gl.GlReport.ListL", 215, up2, "list");
		m.proc(SysOrg.TB,"机构信息", "gl","view.gl.SysOrg.List",250,null,"ded");
	}
}
