package irille.gl.rp;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.gl.rp.Rp.ORptType;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class RpNoteRptBillDAO {
	public static final Log LOG = new Log(RpNoteRptBillDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noSubject("分户账{0}[{1}]与别名[应付账款]设置的科目不一致!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void validateAlia(RpJournal journal) {
		/*
		 * System.out.println("alias:"+Pya.SubjectAlias.PA_SUPPLIER.getCode());
		 * Integer sub = journal.getSubject();
		 * if
		 * (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_SUPPLIER.getCode
		 * ()).getSubject()))
		 * return;
		 * if
		 * (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_OTHER.getCode
		 * ()).getSubject()))
		 * return;
		 * if
		 * (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_INNER_CELL.getCode
		 * ()).getSubject()))
		 * return;
		 * if
		 * (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_CELL.getCode
		 * ()).getSubject()))
		 * return;
		 * throw LOG.err(Msgs.noSubject, journal.gtSubject().getCode(),
		 * journal.gtSubject().getName());
		 */
	}

	public static class Ins extends IduInsLines<Ins, RpNoteRptBill, RpNoteRptBill> {
		@Override
		public void before() {
			super.before();
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!");
			initBill(getB());
			//validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, RpNoteRptBill, RpNoteRptBill> {

		@Override
		public void before() {
			super.before();
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!");
			RpNoteRptBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RpNoteRptBill.T.PKEY, RpNoteRptBill.T.CODE,
			    RpNoteRptBill.T.STATUS, RpNoteRptBill.T.ORG, RpNoteRptBill.T.DEPT, RpNoteRptBill.T.CREATED_BY,
			    RpNoteRptBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, RpNoteRptBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RpNoteRptBill> {

		@Override
		public void run() {
			super.run();
			RpNoteRptBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			if (dbBean.gtType() == ORptType.CASH) {
				RpNoteCashRptDAO.insByBill(getB());
			} else {
				RpNoteRptDAO.insByBill(getB());
			}
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RpNoteRptBill> {

		@Override
		public void run() {
			super.run();
			RpNoteRptBill mode = loadThisBeanAndLock();
			assertStatusIsTally(mode);
			RpJournalLineDAO.delByMv(mode.gtLongPkey());//根据收款单，删除出纳日记账中相应的出纳日记账明细；
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			GlNoteDAO.delByBill(getB().gtLongPkey());
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

}
