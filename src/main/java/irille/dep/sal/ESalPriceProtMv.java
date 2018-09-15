package irille.dep.sal;

import irille.pss.sal.SalPriceProtMv;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class ESalPriceProtMv extends SalPriceProtMv {

	public static void main(String[] args) {
		new ESalPriceProtMv().crtExt().crtFiles();
	}
	
	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds(TB) };

		VFlds[] searchVflds = new VFlds[] { new VFlds(T.CELL_SAL, T.CELL_PUR) };
		VFlds listFlds = new VFlds().addAll(TB);
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.setVfldsList(listFlds);
		ext.newExts().init();
		return ext;
	}
}
