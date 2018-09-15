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

public class PyaPayOtherWriteoffBillDAO {
	public static final Log LOG = new Log(PyaPayOtherWriteoffBillDAO.class);

	public static class Ins extends IduInsLines<Ins, PyaPayOtherWriteoffBill, PyaPayOtherWriteoffBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			PyaPayOtherBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Upd extends IduUpdLines<Upd, PyaPayOtherWriteoffBill, PyaPayOtherWriteoffBill> {

		@Override
		public void before() {
			super.before();
			PyaPayOtherWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), PyaPayOtherWriteoffBill.T.PKEY, PyaPayOtherWriteoffBill.T.CODE,
			    PyaPayOtherWriteoffBill.T.STATUS, PyaPayOtherWriteoffBill.T.ORG, PyaPayOtherWriteoffBill.T.DEPT,
			    PyaPayOtherWriteoffBill.T.CELL, PyaPayOtherWriteoffBill.T.CREATED_BY, PyaPayOtherWriteoffBill.T.CREATED_TIME);
			setB(mode);
			PyaPayOtherBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Del extends IduDel<Del, PyaPayOtherWriteoffBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, PyaPayOtherWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayOtherWriteoffBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			PyaNotePayableLineDAO.insByBill(getB());
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

	public static class Unapprove extends IduUnapprove<Unapprove, PyaPayOtherWriteoffBill> {

		@Override
		public void run() {
			super.run();
			PyaPayOtherWriteoffBill mode = loadThisBeanAndLock();
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
