package irille.dep.gs;

import irille.gl.gs.GsDemand;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EGsDemand extends GsDemand {
	public static void main(String[] args) {
		new EGsDemand().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) });
		vflds.moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		VFlds searchVflds = new VFlds(T.WAREHOUSE, T.GOODS, T.STATUS);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		return ext;
	}

}
