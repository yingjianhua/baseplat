//Created on 2005-9-27
package irille.core.sys;

import irille.core.sys.Sys.OLinkType;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

import java.util.List;

public class SysPersonLinkDAO {
	public static final Log LOG = new Log(SysPersonLinkDAO.class);

	public static String SQL_OBJ_TYPE = Idu
	    .sqlString("{0}=? and {1}=?", SysPersonLink.T.TB_OBJ_LONG, SysPersonLink.T.TYPE);
	public static String SQL_OBJ = Idu.sqlString("{0}=?", SysPersonLink.T.TB_OBJ_LONG);

	public static SysPersonLink getDefault(Bean bean, OLinkType type) {
		List<SysPersonLink> list = BeanBase.list(SysPersonLink.class, SQL_OBJ_TYPE, false, bean.gtLongPkey(), type.getLine().getKey());
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	public static SysPersonLink getDefault(Bean bean) {
		List<SysPersonLink> list = BeanBase.list(SysPersonLink.class, SQL_OBJ, false, bean.gtLongPkey());
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	public static class Ins extends IduIns<Ins, SysPersonLink> {

		@Override
		public void before() {
			super.before();
			getB().setCreatedDateTime(Env.getSystemTime());
			getB().stCreatedBy(getUser());
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
		}

	}

	public static class Upd extends IduUpd<Upd, SysPersonLink> {

		public void before() {
			super.before();
			SysPersonLink dbBean = loadThisBeanAndLock();
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysPersonLink.T.PKEY, SysPersonLink.T.CREATED_BY,
			    SysPersonLink.T.CREATED_DATE_TIME);
			setB(dbBean);
		}

	}

}