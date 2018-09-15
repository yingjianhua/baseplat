package irille.core.sys;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

public class SysAccessoryDAO {
	public static final Log LOG = new Log(SysAccessoryDAO.class);

	public static class Ins extends IduIns<IduIns, SysAccessory> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			// getB().setType(Tp.OAccType.FILE.getLine().getKey());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.getSystemTime());
			super.before();
		}
	}

	public static class Upd extends IduUpd<IduUpd, SysAccessory> {

		@Override
		public void before() {
			super.before();
			// TODO Auto-generated method stub
			SysAccessory acc = loadThisBeanAndLock();
			PropertyUtils.copyProperties(acc, getB(), SysAccessory.T.TYPE,
					SysAccessory.T.NAME);
			// acc.setName(getB().getName());
			setB(acc);
		}

	}

//	public static List<TpAccessory> listByComm(Long pkey) {
//		List<TpAccessory> list1 = BeanBase.list(TpAccessory.class,
//				"tbandkey_table=? and bandkey_pkey=?", false,
//				TpComm.TB.getID(), pkey);
//
//		return list1;
//	}

}
