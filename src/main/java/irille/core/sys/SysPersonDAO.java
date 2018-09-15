//Created on 2005-9-27
package irille.core.sys;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

/**
 * Title: <br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class SysPersonDAO {
	public static final Log LOG = new Log(SysPersonDAO.class);

	public static class Ins extends IduIns<Ins, SysPerson> {

		@Override
		public void before() {
			super.before();
			getB().setCreatedDateTime(Env.getSystemTime());
			getB().stCreatedBy(getUser());
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
			setChkVersion(false);
		}

		public void log() {
		}

	}

	public static class Upd extends IduUpd<Upd, SysPerson> {
		
		public Upd() {
			setChkVersion(false);
		}

		public void before() {
			super.before();
			SysPerson dbBean = load(getB().getPkey());
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysPerson.T.PKEY, SysPerson.T.CREATED_BY,
			    SysPerson.T.CREATED_DATE_TIME);
			setB(dbBean);
		}

		public void log() {
		}

	}

	public static class Del extends IduDel<Del, SysPerson> {
		
		public Del() {
			setChkVersion(false);
		}

		@Override
		public void before() {
		  super.before();
		}
		
		public void log() {
		}

	}

}