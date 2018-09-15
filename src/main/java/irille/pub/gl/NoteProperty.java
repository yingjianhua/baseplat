/**
 * 
 */
package irille.pub.gl;

import irille.pub.Str;

/**
 * @author surface1
 *
 */
public enum NoteProperty {
	J("j","借方"),
	J_XK("j.xk","现款"),
	J_XK_XJ("j.xk.xj","现金"),
	J_XK_ZZ("j.xk.zz","转账"),
	J_XK_WKPYFK("j.xk.wkpyfk","预收款(可开票)"),
	J_XK_YKPYFK("j.xk.ykpyfk","预收款(不可开票)"),
//	J_XK_BP("j.xk.bp","本票"),
	J_XK_HDFK("j.xk.hdfk","货到付款"),
//	J_PJ("j.pj.","票据"),
//	J_PJ_BP("j.pj.bp","本票"),
//	J_PJ_XYS("j.pj.xyz","信用证"),
	J_YSK("j.ysk","应收款"),
	J_YF("j.yf","运费"),
	J_QTFY("j.qtfy","其它费用"),
	D("d","贷方"),
	D_DDK("d.ddk","代垫款类"),
	D_YIFK("d.yifk","应付款"),
	D_YUFK("d.yufk","预付款"),
	;
	private String _code, _name;
	private NoteProperty(String code,String name) {
		_code=code; _name=name;
	}
	public String getCode() { 
		return _code;
	}
	public String getName() {
		return _name;
	}
	
	public boolean isIn(NoteProperty[] noteProperties) {
		int l;
		for(NoteProperty p : noteProperties) {
			if(Str.isUpDown(p._code, _code))
				return true;
		}
		return false;
	}
}	
