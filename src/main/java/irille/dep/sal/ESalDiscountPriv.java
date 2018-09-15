/**
 * 
 */
package irille.dep.sal;

import irille.core.sys.SysUser;
import irille.pss.sal.SalDiscountPriv;
import irille.pub.html.EMCrt;
import irille.pub.html.EMCrtSimple;
import irille.pub.html.EMList;
import irille.pub.tb.Fld;
import irille.pub.tb.FldOutKey;
import irille.pub.view.VFlds;

/**
 * 一对一的模型
 * 主键PKEY，MODEL - > type:'string', outkey:true
 * FORM 、LIST 都作对应更改
 * @author whx
 * @version 创建时间：2015年4月2日 上午8:51:19
 */
public class ESalDiscountPriv extends SalDiscountPriv {

	public static void main(String[] args) {
		new ESalDiscountPriv().crtExt().crtFiles();
	}

	public EMCrt crtExt() {
		Fld userFld = new FldOutKey(SysUser.class, "pkey", "用户");
		VFlds[] vflds = new VFlds[] { new VFlds().add(userFld).addWithout(TB, T.USER, T.PKEY) };
		VFlds[] searchVflds = new VFlds[] { new VFlds(T.DISCOUNT_LEVEL) };
		EMCrt ext = new EMCrtSimple(TB, vflds, searchVflds);
		ext.getVfldsForm().del(T.UPDATED_BY, T.UPDATED_TIME);
		ext.getVfldsForm().get("pkey").setReadOnly("!this.insFlag");
		((EMList) ext.newList()).setLineActs(false);
		ext.newExts().init();
		return ext;
	}

}
