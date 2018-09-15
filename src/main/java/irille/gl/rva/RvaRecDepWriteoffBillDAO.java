package irille.gl.rva;

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

public class RvaRecDepWriteoffBillDAO {
	public static final Log LOG = new Log(RvaRecDepWriteoffBillDAO.class);

	public static class Ins extends IduInsLines<Ins, RvaRecDepWriteoffBill, RvaRecDepWriteoffBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			RvaRecDepBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Upd extends IduUpdLines<Upd, RvaRecDepWriteoffBill, RvaRecDepWriteoffBill> {

		@Override
		public void before() {
			super.before();
			RvaRecDepWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RvaRecDepWriteoffBill.T.PKEY, RvaRecDepWriteoffBill.T.CODE,
			    RvaRecDepWriteoffBill.T.STATUS, RvaRecDepWriteoffBill.T.ORG, RvaRecDepWriteoffBill.T.DEPT,
			    RvaRecDepWriteoffBill.T.CELL, RvaRecDepWriteoffBill.T.CREATED_BY, RvaRecDepWriteoffBill.T.CREATED_TIME);
			setB(mode);
			RvaRecDepBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Del extends IduDel<Del, RvaRecDepWriteoffBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RvaRecDepWriteoffBill> {

		@Override
		public void run() {
			super.run();
			RvaRecDepWriteoffBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			RvaNoteDepositLineDAO.insByBill(getB());
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

	public static class Unapprove extends IduUnapprove<Unapprove, RvaRecDepWriteoffBill> {

		@Override
		public void run() {
			super.run();
			RvaRecDepWriteoffBill mode = loadThisBeanAndLock();
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
