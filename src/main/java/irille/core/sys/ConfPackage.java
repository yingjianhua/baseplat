/**
 * 
 */
package irille.core.sys;

import irille.core.lg.Lg;
import irille.core.ms.Ms;
import irille.core.prv.Prv;
import irille.gl.frm.Frm;
import irille.gl.gl.Gl;
import irille.gl.gs.Gs;
import irille.gl.pya.Pya;
import irille.gl.rp.Rp;
import irille.gl.rva.Rva;
import irille.pss.cst.Cst;
import irille.pss.lgt.Lgt;
import irille.pss.pur.Pur;
import irille.pss.sal.Sal;
import irille.pub.bean.PackageBase.ConfPackageBase;

/**
 * 包配置文件
 * @author whx
 * 
 */
public class ConfPackage extends ConfPackageBase {
	public static ConfPackage INST = new ConfPackage();

	public void installSys() {
		//core系统核心 2000以内
		add(Sys.class, 0);
		add(Lg.class, 200);
		//add(Ms.class, 400);
		add(Prv.class, 600);
	}

	public void installGl() {
		add(Frm.class, 2000);
		add(Gl.class, 2200);
		add(Gs.class, 2400);
		add(Rp.class, 2600);
		add(Pya.class, 4400);
		add(Rva.class, 4600);
	}

	public void installErp() {
		add(Cst.class, 6200);
		//add(Lgt.class, 6400);
		add(Pur.class, 6800);
		add(Sal.class, 7000);
	}

	@Override
	public void initPacks() {
		super.initPacks();
		_packsFlag = true;
		installSys();
		installGl();
		installErp();
	}
}
