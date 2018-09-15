package irille.dep.frm;

import irille.gl.frm.FrmPending;
import irille.pss.sal.SalSale;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EFrmPending extends FrmPending {
	public static void main(String[] args) {
		new EFrmPending().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		SalSale.TB.getName();
		VFlds one = new VFlds("one").add(SalSale.T.AMT); //显示重要的金额字段
		VFlds[] vflds = new VFlds[] { new VFlds(TB), one };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.ORIG_FORM_NUM, T.ORG, T.USER_SYS) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsList().del("amt");
		ext.newExts().init();
		return ext;
	}

}
