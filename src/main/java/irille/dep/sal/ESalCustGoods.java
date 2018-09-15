package irille.dep.sal;

import irille.pss.sal.SalCustGoods;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ESalCustGoods extends SalCustGoods{
	public static void main(String[] args) {
		new ESalCustGoods().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { VFlds.newVFlds(new VFlds[] { new VFlds(TB),
		    EMCrtModelAndStore.getGoodsVflds(T.GOODS.getFld()) }) };
		vflds[0].moveAfter("goodsSpec", "goods").moveAfter("goodsName", "goods");
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.ORG,T.GOODS) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.newExts().init();
		
		return ext;
	}
}
