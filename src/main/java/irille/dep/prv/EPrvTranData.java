package irille.dep.prv;

import irille.core.prv.PrvTranData;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.view.VFlds;

public class EPrvTranData extends PrvTranData {
	public static void main(String[] args) {
		new EPrvTranData().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		VFlds[] vflds = new VFlds[] { new VFlds().addWithout(TB, T.TRAN,T.USER_FLD,T.DEPT_FLD,T.CELL_FLD,T.ORG_FLD,T.TRAN_CODE) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.TRAN_NAME, T.GRP, T.IS_FORM) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.setVfldsForm(new VFlds(T.NAME, T.TRAN_NAME, T.GRP, T.ROW_VERSION));
		VFlds vf = ext.getVfldsForm(); 
		vf.get(T.ROW_VERSION).setHidden("true");
		vf.get(T.NAME).setReadOnly("true");
		vf.get(T.TRAN_NAME).setReadOnly("true");
		ext.newExts().init();
		return ext;
	}

}
