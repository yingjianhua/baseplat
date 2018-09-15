package irille.gl.rp;

import irille.core.sys.SysCellDAO;
import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class RpWorkBoxDAO {
	public static final Log LOG = new Log(RpWorkBoxDAO.class);

	public static class Ins extends IduIns<IduIns, RpWorkBox> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			getB().setEnabled(OEnabled.TRUE.getLine().getKey());
			getB().stMngCell(SysCellDAO.getCellByUser(getB().gtUserSys()));

			super.before();
		}
	}

	public static class Upd extends IduUpd<IduUpd, RpWorkBox> {

		@Override
		public void before() {
			RpWorkBox dbBean = loadThisBeanAndLock();
			PropertyUtils.copyProperties(dbBean, getB(), RpWorkBox.T.NAME, RpWorkBox.T.TYPE,
			    RpWorkBox.T.REM);
			setB(dbBean);
		}
	}
	public static class Del extends IduDel<Del, RpWorkBox> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(RpHandover.class, RpHandover.T.WORK_BOX, b.getPkey());
			haveBeUsed(RpSeal.class, RpSeal.T.WORK_BOX, b.getPkey());
			haveBeUsed(RpJournal.class, RpJournal.T.WORK_BOX, b.getPkey());
		}
	}

}
