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
 * 
 * @version 1.0
 */
public class SysComDAO {
	public static final Log LOG = new Log(SysComDAO.class);

	public static class Ins extends IduIns<Ins, SysCom> {

		public Ins() {
			setChkVersion(false);
		}

		@Override
		public void before() {
			super.before();
			if (SysCom.chkUniqueName(false, getB().getName()) != null)
				throw LOG.err("notFound", "名称[{0}]已存在,不可重复新增!", getB().getName());
			getB().setCreatedDateTime(Env.getSystemTime());
			getB().stCreatedBy(getUser());
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
		}

		@Override
		public void log() {
		}

	}

	public static class Upd extends IduUpd<Upd, SysCom> {

		public Upd() {
			setChkVersion(false);
		}

		public void before() {
			super.before();
			SysCom dbBean = load(getB().getPkey());
			// 判断名称是否更改,判断唯一
			if (!dbBean.getName().equals(getB().getName())) {
				if (SysCom.chkUniqueName(false, getB().getName()) != null) {
					throw LOG.err("notFound", "名称[{0}]已存在,不可重名!", getB().getName());
				}
			}
			getB().setUpdatedDateTime(Env.getSystemTime());
			getB().stUpdatedBy(getUser());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysCom.T.PKEY, SysCom.T.CREATED_BY,
			    SysCom.T.CREATED_DATE_TIME);
			setB(dbBean);
		}

		@Override
		public void log() {
		}

	}

	public static class Del extends IduDel<Del, SysCom> {

		public Del() {
			setChkVersion(false);
		}

		@Override
		public void before() {
			super.before();
		}

		@Override
		public void log() {
		}
	}

}