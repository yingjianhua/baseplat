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

public class RvaRecOtherBillDAO {
	public static final Log LOG = new Log(RvaRecOtherBillDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noSubject("分户账{0}[{1}]与别名[其它应付款]设置的科目不一致!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void validateAlia(GlJournal journal) {
		Integer sub = journal.getSubject();
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_USER.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_INNER_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_DEPT.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_ACCOUNT.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_OTHER.getCode()).getSubject()))
			return;
		throw LOG.err(Msgs.noSubject, journal.gtSubject().getCode(), journal.gtSubject().getName());
	}

	public static class Ins extends IduInsLines<Ins, RvaRecOtherBill, RvaRecOtherBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, RvaRecOtherBill, RvaRecOtherBill> {

		@Override
		public void before() {
			super.before();
			RvaRecOtherBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RvaRecOtherBill.T.PKEY, RvaRecOtherBill.T.CODE,
			    RvaRecOtherBill.T.STATUS, RvaRecOtherBill.T.ORG, RvaRecOtherBill.T.DEPT, RvaRecOtherBill.T.CELL,
			    RvaRecOtherBill.T.CREATED_BY, RvaRecOtherBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, RvaRecOtherBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RvaRecOtherBill> {

		@Override
		public void run() {
			super.run();
			RvaRecOtherBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			RvaNoteOtherDAO.insByBill(getB());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RvaRecOtherBill> {

		@Override
		public void run() {
			super.run();
			RvaRecOtherBill mode = loadThisBeanAndLock();
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
