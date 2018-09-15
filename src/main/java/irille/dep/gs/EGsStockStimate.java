package irille.dep.gs;

import irille.gl.gs.GsStockStimate;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGsStockStimate extends GsStockStimate {
	public static void main(String[] args) {
		new EGsStockStimate().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld())};
		VFlds[] searchVflds = new VFlds[] {new VFlds(T.WAREHOUSE, T.GOODS, T.QTY)};
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsList().moveLast(T.PLAN_DATE);
		ext.getVfldsList().get(T.ENROUTE_TYPE).setName("类别").setWidthList(80);
		ext.newExts().init();
		return ext;
	}

}
