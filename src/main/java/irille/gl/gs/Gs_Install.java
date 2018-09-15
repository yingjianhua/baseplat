package irille.gl.gs;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.bean.InstallBase;

public class Gs_Install extends InstallBase {
	private static final Log LOG = new Log(Gs_Install.class);
	public static final Gs_Install INST = new Gs_Install();
	/* 
	 * 菜单的初始化操作
	 * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
	 */
	@Override
	public void initMenu(SysMenuDAO m) {
		m.proc(GsGoods.TB, 10, null, "goods");
		m.proc(GsGoodsKind.TB, 20, null, "goods");
		//m.proc(GsGoodsCmb.TB, 30, null, "goods");
		m.proc(GsUomType.TB, 50, null, "goods");
		m.proc(GsPrice.TB, 60, null, "goods");
		m.proc(GsPriceCtl.TB, 70, null, "goods");
		m.proc(GsPriceGoodsKind.TB, 75, null, "goods");
		m.proc(GsPriceGoods.TB, 80, null, "goods");
		m.proc(GsPriceGoodsCell.TB, 90, null, "goods");
		// 库存管理(gs)=================
		m.proc(GsIn.TB, 10, null);
		m.proc(GsOut.TB, 20, null);
		m.proc(GsStock.TB, 30, null);
		m.proc(GsStockBatch.TB, 35, null);
		m.proc(GsStockStimate.TB, 40, null);
		m.proc(GsWarehouse.TB, 45, null);
		m.proc(GsLocation.TB, 50, null);
		m.proc(GsRequest.TB, 55, null);
		m.proc(GsDemand.TB, 60, null);
		m.proc(GsDemandDirect.TB, 65, null);
		m.proc(GsMovement.TB, 70, null);
		m.proc(GsGain.TB, 75, null);
		m.proc(GsLoss.TB, 80, null);
		m.proc(GsUnite.TB, 85, null);
		m.proc(GsSplit.TB, 90, null);
		m.proc(GsPhyinv.TB, 95, null);
		SysMenu up = m.procParent("库存分析", "gs", 100, null);
		m.proc(GsReportSalOut.TB, 110, up);
		m.proc(GsReportMvOut.TB, 115, up);
		m.proc(GsReportPurIn.TB, 120, up);
	}
}
