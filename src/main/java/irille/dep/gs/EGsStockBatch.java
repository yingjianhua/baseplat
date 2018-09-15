package irille.dep.gs;

import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockBatch;
import irille.gl.gs.GsUom;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMList;
import irille.pub.html.EMWinSearch;
import irille.pub.tb.Fld;
import irille.pub.tb.FldStr;
import irille.pub.tb.IEnumFld;
import irille.pub.view.VFlds;

public class EGsStockBatch extends GsStockBatch {
	Fld _uom = GsUom.fldOutKey();
	private static final GsStock.T STOCK = GsStock.T.PKEY;
	
	public static void main(String[] args) {
		new EGsStockBatch().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		_uom.setCode("goodsUom");
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB).add(STOCK.GOODS), EMCrtModelAndStore.getGoodsVflds(STOCK.GOODS.getFld())});
		VFlds searchVflds = new VFlds(STOCK.WAREHOUSE, STOCK.GOODS, T.NAME);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.setVfldsModel(new VFlds[] { new VFlds(TB).add(STOCK.GOODS), EMCrt.getGoodsVflds() }); //模型中加入货物名称与规格字段
		ext.getVfldsModel().add(_uom);
		ext.getVfldsForm().del(T.PKEY, T.STOCK, T.ENTRY_TIME);// FROM页面删除字段
		ext.getVfldsList().add(_uom);
		ext.getVfldsList().get(T.STOCK).setName("仓库");
		ext.getVfldsList().moveLast(T.NAME, T.QTY, T.CLEARED, T.EXP_DATE, T.ENTRY_TIME);
		((EMWinSearch)ext.newWinSearch()).getVFlds().del(T.STOCK);
		ext.newExts().init();
		return ext;
	}

}
