package irille.gl.pya;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.gl.pya.PyaNoteAccountPayableLineDAO.Msgs;
import irille.gl.rp.RpStimatePayDAO;
import irille.gl.rp.RpStimateRecDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class PyaPayWriteoffBillDAO {
	public static final Log LOG = new Log(PyaPayWriteoffBillDAO.class);

	public static class Ins extends IduInsLines<Ins, PyaPayWriteoffBill, PyaPayWriteoffBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			PyaPayBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}

	}

	public static class Upd extends IduUpdLines<Upd, PyaPayWriteoffBill, PyaPayWriteoffBill> {

		@Override
		public void before() {
			super.before();
			PyaPayWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), PyaPayWriteoffBill.T.PKEY, PyaPayWriteoffBill.T.CODE,
			    PyaPayWriteoffBill.T.STATUS, PyaPayWriteoffBill.T.ORG, PyaPayWriteoffBill.T.DEPT, PyaPayWriteoffBill.T.CELL,
			    PyaPayWriteoffBill.T.CREATED_BY, PyaPayWriteoffBill.T.CREATED_TIME);
			setB(mode);
			PyaPayBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Del extends IduDel<Del, PyaPayWriteoffBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, PyaPayWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayWriteoffBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			PyaNoteAccountPayableLineDAO.insByBill(getB());
			if (dbBean.getAmt().signum() < 0)
				RpStimateRecDAO.insByBill(dbBean, (long) dbBean.gtJournal().getObjPkey(), dbBean.gtJournal().getName(), dbBean
				    .getAmt().negate());
			else
				RpStimatePayDAO.insByBill(dbBean, (long) dbBean.gtJournal().getObjPkey(), dbBean.gtJournal().getName(),
				    dbBean.getAmt());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PyaPayWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsTally(mode);
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			GlNoteDAO.delByBill(getB().gtLongPkey());
			if (mode.getAmt().signum() < 0)
				RpStimateRecDAO.delByBill(getB());
			else
				RpStimatePayDAO.delByBill(getB());
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

}
