package irille.dep.gs;

import irille.core.sys.SysDept;
import irille.gl.gs.GsWarehouse;
import irille.gl.gs.GsStock.T;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.svr.DbPool;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.view.VFlds;

public class EGsWarehouse extends GsWarehouse {

	public static void main(String[] args) {
		new EGsWarehouse().crtExt().crtFiles(); // 产生Ext文件
		DbPool.getInstance().releaseAll();
	}

	public EMCrt crtExt() {
		Fld deptFld = new FldOutKey(SysDept.class, "pkey", "仓库");
		VFlds[] vflds = new VFlds[] { new VFlds().add(deptFld).addWithout(TB, T.DEPT, T.PKEY) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.ORG, T.ENABLED) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		VFlds vl = ext.getVfldsList();
		vl.moveAfter(T.ENABLED, T.REM);
		vl.get(T.PKEY).setWidthList(150);
		VFlds vs =ext.getVfldsForm();
		vs.get(T.PKEY).setReadOnly("!this.insFlag");
		vs.del(T.ORG, T.CELL);
		ext.newExts().init();
		return ext;
	}
}
