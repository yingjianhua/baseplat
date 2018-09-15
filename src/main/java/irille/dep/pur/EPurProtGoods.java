package irille.dep.pur;

import irille.pss.pur.PurProtGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EPurProtGoods extends PurProtGoods {

	public static void main(String[] args) {
		new EPurProtGoods().crtExt();
	}

	public void crtExt() {
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld())});
		VFlds searchVflds = new VFlds(T.SUPPLIER, T.NAME, T.GOODS);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		ext.crtFiles();
	}
}
