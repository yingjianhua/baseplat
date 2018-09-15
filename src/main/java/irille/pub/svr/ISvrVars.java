/**
 * 
 */
package irille.pub.svr;

import irille.core.lg.Lg;
import irille.core.sys.Sys;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.gl.frm.Frm;
import irille.gl.gl.Gl;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.Gs;
import irille.gl.rp.Rp;
import irille.pss.cst.Cst;
import irille.pss.pur.Pur;
import irille.pss.sal.Sal;
import irille.pub.IPubVars;
import irille.pub.svr.Env.SysConf;


/**
 * @author surface1
 *
 */
public interface ISvrVars  extends IPubVars{
	public static final Sys.T SYS = Sys.T.USER_SYS;
	public static final Env ENV=Env.INST;
	public static final Lg.T LG = Lg.T.LOGIN;
	public static final Gl.T GL = Gl.T.DIRECT;
//	public static final Gs.T GS = Gs.T.UOM;
//	public static final Mv.T MV = Mv.T.COST_TYPE;
//	public static final Pur.T PUR = Pur.T.SETTLE_TYPE;
//	public static final Cst.T CST = Cst.T.SAL_INVOICE_RED;
//	public static final Sal.T SAL = Sal.T.SALE;
//	public static final Rp.T RP = Rp.T.JOURNAL_TYPE;
	public static final byte JF = ODirect.DR.getLine().getKey(); // 借方
	public static final byte DF = ODirect.CR.getLine().getKey(); // 贷方
	public static final byte DR = JF; // 借方
	public static final byte CR = DF; // 贷方

	public static final byte YES = OYn.YES.getLine().getKey();
	public static final byte NO = OYn.NO.getLine().getKey();
	public static final byte TRUE = OEnabled.TRUE.getLine().getKey();
	public static final byte FALSE = OEnabled.FALSE.getLine().getKey();
}
