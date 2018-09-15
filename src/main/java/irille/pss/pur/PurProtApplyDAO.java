package irille.pss.pur;

import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

public class PurProtApplyDAO {
	public static final Log LOG = new Log(PurProtApplyDAO.class);

	public static class Ins extends IduIns<Ins, PurProtApply> {
		@Override
		public void before() {
			super.before();
			initForm(getB());
		}
	}

	public static class Upd extends IduUpd<Upd, PurProtApply> {

		public void before() {
			super.before();
			PurProtApply dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			PropertyUtils.copyPropertiesWithout(dbBean, getB(),
					PurProtApply.T.PKEY, PurProtApply.T.CODE,
					PurProtApply.T.STATUS, PurProtApply.T.CREATED_BY,
					PurProtApply.T.CREATED_TIME, PurProtApply.T.DEPT,
					PurProtApply.T.ORG, PurProtApply.T.APPR_BY,
					PurProtApply.T.APPR_TIME);
			setB(dbBean);
		}
	}

	public static class Approve extends IduApprove<Approve, PurProtApply> {

		@Override
		public void run() {
			super.run();
			PurProtApply dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.CHECKED);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			// 审核后续操作
			PurProtDAO.actionByApply(dbBean);
		}
	}
}
