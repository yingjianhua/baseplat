//Created on 2005-9-27
package irille.core.sys;

import irille.core.sys.Sys.OShipingMode;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IForm;
import irille.pub.idu.Idu;
import irille.pub.tb.Fld;

public class SysShipingDAO {
	public static final Log LOG = new Log(SysShipingDAO.class);

	/**
	 * 根据运输方式，新增物流信息，并为FORM设置物流主键值
	 * @param form 来源单据
	 * @param ship 物流信息
	 * @param fld 单据中的物流外键
	 * @param type 运输方式
	 */
	public static void ins(IForm form, SysShiping ship, Fld fld, OShipingMode type) {
		if (type.getLine().getKey() <= Sys.OShipingMode.SELF.getLine().getKey())
			return;
		ship.setPkey(form.gtLongPkey());
		ship.setRowVersion(((Bean) form).getRowVersion());
		ship.ins();
		((Bean) form).propertySet(fld, ship.getPkey());
	}

	public static void del(IForm form, OShipingMode type) {
		if (type.getLine().getKey() <= Sys.OShipingMode.SELF.getLine().getKey())
			return;
		String sql = Idu.sqlString("delete from {0} where {1}=?", SysShiping.class, SysShiping.T.PKEY);
		Bean.executeUpdate(sql, form.gtLongPkey());
	}

	/**
	 * 新增或更新物流信息，并为FORM设置物流主键值
	 * @param form 来源单据
	 * @param ship 物流信息
	 * @param fld 单据中的物流外键
	 * @param type 运输方式
	 */
	public static void upd(IForm form, SysShiping ship, Fld fld, OShipingMode type) {
		SysShiping mode = BeanBase.chk(SysShiping.class, form.gtLongPkey());
		if (mode == null) {
			ins(form, ship, fld, type);
			return;
		}
		Bean mainForm = (Bean) form;
		if (type.getLine().getKey() <= Sys.OShipingMode.SELF.getLine().getKey()) {
			mode.del();
			mainForm.propertySet(fld, null);
		} else {
			PropertyUtils.copyPropertiesWithout(mode, ship, SysShiping.T.PKEY);
			mode.setRowVersion(((Bean) form).getRowVersion());
			mode.upd();
		}
	}

	/**
	 * 出入库审核时运输方式特殊处理，如运输主键可能是来源单据的LONGPKEY
	 */
	public static void updByGs(IForm form, Bean origForm, SysShiping ship, Fld fld, OShipingMode type, Long shipPkey) {
		SysShiping mode = null;
		if (shipPkey != null)
			mode = BeanBase.chk(SysShiping.class, shipPkey);
		boolean has = ((Bean)origForm).gtTB().chk(fld.getCode());
		if (mode == null) {
			if (type.getLine().getKey() <= Sys.OShipingMode.SELF.getLine().getKey())
				return;
			ship.setPkey(has ? origForm.gtLongPkey() : form.gtLongPkey());
			ship.setRowVersion(((Bean) form).getRowVersion());
			ship.ins();
			((Bean) form).propertySet(fld, ship.getPkey());
			return;
		}
		Bean mainForm = (Bean) form;
		if (type.getLine().getKey() <= Sys.OShipingMode.SELF.getLine().getKey()) {
			mode.del();
			mainForm.propertySet(fld, null);
		} else {
			PropertyUtils.copyPropertiesWithout(mode, ship, SysShiping.T.PKEY);
			mode.setRowVersion(((Bean) form).getRowVersion());
			mode.upd();
		}
	}

}