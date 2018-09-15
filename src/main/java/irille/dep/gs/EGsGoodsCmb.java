package irille.dep.gs;

import irille.gl.gs.GsGoodsCmb;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMForm;
import irille.pub.view.VFlds;

public class EGsGoodsCmb extends GsGoodsCmb {

	public static void main(String[] args) {
		new EGsGoodsCmb().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		//这里加入货物字段关联的名称与规格字段，goodsName  goodsSpec
		VFlds vflds = VFlds.newVFlds(new VFlds[] { new VFlds(TB), EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()),
		    EMCrtModelAndStore.getGoodsVflds(T.INNER_GOODS.getFld()) });
		vflds.moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		vflds.moveAfter("innerGoodsSpec", "innerGoods").moveAfter("innerGoodsName", "innerGoods");
		vflds.moveLast(T.SORT);
		VFlds searchVflds = new VFlds(T.GOODS, T.INNER_GOODS);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsForm().setGoodsLink(T.GOODS, T.INNER_GOODS); //FORM里带出货物名称与规格
		((EMForm) ext.newForm()).setGoodsLink(true); // 货物的空间重写
		ext.newExts().init();
		return ext;
	}

}
