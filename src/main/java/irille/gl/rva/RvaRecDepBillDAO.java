package irille.gl.rva;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.GlSubjectMapDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class RvaRecDepBillDAO {
	public static final Log LOG = new Log(RvaRecDepBillDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noSubject("分户账{0}[{1}]与别名[预收账款]设置的科目不一致!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void validateAlia(GlJournal journal) {
		Integer sub = journal.getSubject();
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_CUST.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_OTHER.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_INNER_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_CELL.getCode()).getSubject()))
			return;
		throw LOG.err(Msgs.noSubject, journal.gtSubject().getCode(), journal.gtSubject().getName());
	}

	public static class Ins extends IduInsLines<Ins, RvaRecDepBill, RvaRecDepBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, RvaRecDepBill, RvaRecDepBill> {

		@Override
		public void before() {
			super.before();
			RvaRecDepBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RvaRecDepBill.T.PKEY, RvaRecDepBill.T.CODE,
			    RvaRecDepBill.T.STATUS, RvaRecDepBill.T.ORG, RvaRecDepBill.T.DEPT, RvaRecDepBill.T.CELL,
			    RvaRecDepBill.T.CREATED_BY, RvaRecDepBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, RvaRecDepBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RvaRecDepBill> {

		@Override
		public void run() {
			super.run();
			RvaRecDepBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			RvaNoteDepositDAO.insByBill(getB());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RvaRecDepBill> {

		@Override
		public void run() {
			super.run();
			RvaRecDepBill mode = loadThisBeanAndLock();
			assertStatusIsTally(mode);
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
