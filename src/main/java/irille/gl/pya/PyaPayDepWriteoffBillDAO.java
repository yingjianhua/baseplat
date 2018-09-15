package irille.gl.pya;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.gl.pya.PyaNoteDepositPayableLineDAO.Msgs;
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

public class PyaPayDepWriteoffBillDAO {
	public static final Log LOG = new Log(PyaPayDepWriteoffBillDAO.class);

	public static class Ins extends IduInsLines<Ins, PyaPayDepWriteoffBill, PyaPayDepWriteoffBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			PyaPayDepBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Upd extends IduUpdLines<Upd, PyaPayDepWriteoffBill, PyaPayDepWriteoffBill> {

		@Override
		public void before() {
			super.before();
			PyaPayDepWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), PyaPayDepWriteoffBill.T.PKEY, PyaPayDepWriteoffBill.T.CODE,
			    PyaPayDepWriteoffBill.T.STATUS, PyaPayDepWriteoffBill.T.ORG, PyaPayDepWriteoffBill.T.DEPT,
			    PyaPayDepWriteoffBill.T.CELL, PyaPayDepWriteoffBill.T.CREATED_BY, PyaPayDepWriteoffBill.T.CREATED_TIME);
			setB(mode);
			PyaPayDepBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Del extends IduDel<Del, PyaPayDepWriteoffBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, PyaPayDepWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayDepWriteoffBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			PyaNoteDepositPayableLineDAO.insByBill(getB());
			if (dbBean.getAmt().signum() < 0)
				RpStimatePayDAO.insByBill(dbBean, (long) dbBean.gtJournal().getObjPkey(), dbBean.gtJournal().getName(), dbBean
				    .getAmt().negate());
			else
				RpStimateRecDAO.insByBill(dbBean, (long) dbBean.gtJournal().getObjPkey(), dbBean.gtJournal().getName(),
				    dbBean.getAmt());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PyaPayDepWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayDepWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsTally(mode);
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			GlNoteDAO.delByBill(getB().gtLongPkey());
			if (mode.getAmt().signum() < 0)
				RpStimatePayDAO.delByBill(getB());
			else
				RpStimateRecDAO.delByBill(getB());
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

}
