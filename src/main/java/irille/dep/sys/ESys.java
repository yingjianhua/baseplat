/**
 * 
 */
package irille.dep.sys;

import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.pub.html.EMCrtModelAndStore;
import irille.pub.svr.ISvrVars;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * @author surface1
 * 
 */
public class ESys extends EMCrtModelAndStore<ESys> implements ISvrVars{
	public static final Tb TB = new Tb(ESys.class, "变量");

	public enum VARS implements IEnumFld {//@formatter:off
		ROLES(SYS.STR__200,"roles");
		private Fld _fld;
		private VARS(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private VARS(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private VARS(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private VARS(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}		

	public static void main(String[] args) {
		new ESys().crtExt();
	}

	public void crtExt() {
		crtModelAndStore_CompBackup(SysCtype.TB);
		crtModelAndStore_CompBackup(SysCtypeCode.TB);
	}
}
