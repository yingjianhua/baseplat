package irille.gl.pya;

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

public class PyaPayBillDAO {
	public static final Log LOG = new Log(PyaPayBillDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noSubject("分户账{0}[{1}]与别名[应付账款]设置的科目不一致!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void validateAlia(GlJournal journal) {
		System.out.println("alias:" + Pya.SubjectAlias.PA_SUPPLIER.getCode());
		Integer sub = journal.getSubject();
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_SUPPLIER.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_OTHER.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_INNER_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_CELL.getCode()).getSubject()))
			return;
		throw LOG.err(Msgs.noSubject, journal.gtSubject().getCode(), journal.gtSubject().getName());
	}

	public static class Ins extends IduInsLines<Ins, PyaPayBill, PyaPayBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, PyaPayBill, PyaPayBill> {

		@Override
		public void before() {
			super.before();
			PyaPayBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), PyaPayBill.T.PKEY, PyaPayBill.T.CODE, PyaPayBill.T.STATUS,
			    PyaPayBill.T.ORG, PyaPayBill.T.DEPT, PyaPayBill.T.CELL, PyaPayBill.T.CREATED_BY, PyaPayBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, PyaPayBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, PyaPayBill> {

		@Override
		public void run() {
			super.run();
			PyaPayBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			PyaNoteAccountPayableDAO.insByBill(getB());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PyaPayBill> {

		@Override
		public void run() {
			super.run();
			PyaPayBill mode = loadThisBeanAndLock();
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
