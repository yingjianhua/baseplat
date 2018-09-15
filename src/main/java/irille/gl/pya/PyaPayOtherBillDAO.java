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

public class PyaPayOtherBillDAO {
	public static final Log LOG = new Log(PyaPayOtherBillDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noSubject("分户账{0}[{1}]与别名[其它应付款]设置的科目不一致!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void validateAlia(GlJournal journal) {
		Integer sub = journal.getSubject();
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_USER.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_INNER_CELL.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_DEPT.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_ACCOUNT.getCode()).getSubject()))
			return;
		if (sub.equals(GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_OTHER.getCode()).getSubject()))
			return;
		throw LOG.err(Msgs.noSubject, journal.gtSubject().getCode(), journal.gtSubject().getName());
	}

	public static class Ins extends IduInsLines<Ins, PyaPayOtherBill, PyaPayOtherBill> {
		@Override
		public void before() {
			super.before();
			initBill(getB());
			validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, PyaPayOtherBill, PyaPayOtherBill> {

		@Override
		public void before() {
			super.before();
			PyaPayOtherBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), PyaPayOtherBill.T.PKEY, PyaPayOtherBill.T.CODE,
			    PyaPayOtherBill.T.STATUS, PyaPayOtherBill.T.ORG, PyaPayOtherBill.T.DEPT, PyaPayOtherBill.T.CELL,
			    PyaPayOtherBill.T.CREATED_BY, PyaPayOtherBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, PyaPayOtherBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, PyaPayOtherBill> {

		@Override
		public void run() {
			super.run();
			PyaPayOtherBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			PyaNotePayableDAO.insByBill(getB());
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PyaPayOtherBill> {

		@Override
		public void run() {
			super.run();
			PyaPayOtherBill mode = loadThisBeanAndLock();
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
