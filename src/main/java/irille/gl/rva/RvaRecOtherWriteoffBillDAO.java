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

public class RvaRecOtherWriteoffBillDAO {
	public static final Log LOG = new Log(RvaRecOtherWriteoffBillDAO.class);

	public static class Ins extends IduInsLines<Ins, RvaRecOtherWriteoffBill, RvaRecOtherWriteoffBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			RvaRecOtherBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Upd extends IduUpdLines<Upd, RvaRecOtherWriteoffBill, RvaRecOtherWriteoffBill> {

		@Override
		public void before() {
			super.before();
			RvaRecOtherWriteoffBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RvaRecOtherWriteoffBill.T.PKEY, RvaRecOtherWriteoffBill.T.CODE,
			    RvaRecOtherWriteoffBill.T.STATUS, RvaRecOtherWriteoffBill.T.ORG, RvaRecOtherWriteoffBill.T.DEPT,
			    RvaRecOtherWriteoffBill.T.CELL, RvaRecOtherWriteoffBill.T.CREATED_BY, RvaRecOtherWriteoffBill.T.CREATED_TIME);
			setB(mode);
			RvaRecOtherBillDAO.validateAlia(getB().gtJournal());
		}

		@Override
		public void valid() {
			super.valid();
			if (getB().getAmt().signum() == 0)
				throw LOG.err(Msgs.noAmt);
		}
	}

	public static class Del extends IduDel<Del, RvaRecOtherWriteoffBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RvaRecOtherWriteoffBill> {

		@Override
		public void run() {
			super.run();
			RvaRecOtherWriteoffBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			RvaNoteOtherLineDAO.insByBill(getB());
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

	public static class Unapprove extends IduUnapprove<Unapprove, RvaRecOtherWriteoffBill> {

		@Override
		public void run() {
			super.run();
			RvaRecOtherWriteoffBill mode = loadThisBeanAndLock();
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
