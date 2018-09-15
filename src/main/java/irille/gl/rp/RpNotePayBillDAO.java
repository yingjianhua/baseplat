package irille.gl.rp;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.gl.rp.Rp.OPayType;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class RpNotePayBillDAO {
	public static final Log LOG = new Log(RpNotePayBillDAO.class);

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

	public static class Ins extends IduInsLines<Ins, RpNotePayBill, RpNotePayBill> {
		@Override
		public void before() {
			super.before();
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!!!");
			initBill(getB());
			//validateAlia(getB().gtJournal());
		}

	}

	public static class Upd extends IduUpdLines<Upd, RpNotePayBill, RpNotePayBill> {

		@Override
		public void before() {
			super.before();
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!!!");
			RpNotePayBill mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), RpNotePayBill.T.PKEY, RpNotePayBill.T.CODE,
			    RpNotePayBill.T.STATUS, RpNotePayBill.T.ORG, RpNotePayBill.T.DEPT, RpNotePayBill.T.CREATED_BY,
			    RpNotePayBill.T.CREATED_TIME);
			setB(mode);
			validateAlia(getB().gtJournal());
		}
	}

	public static class Del extends IduDel<Del, RpNotePayBill> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

	}

	public static class Approve extends IduApprove<Approve, RpNotePayBill> {

		@Override
		public void run() {
			super.run();
			RpNotePayBill dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
			//RpJournalLineDAO.addByBill(dbBean);//在对应的出纳日记账中，添加出纳日记账明细；已经在下面的付款凭条中做了
			dbBean.stStatus(STATUS.TALLY_ABLE);
			dbBean.setApprBy(getUser().getPkey());
			dbBean.setApprTime(Env.INST.getWorkDate());
			dbBean.upd();
			setB(dbBean);
			if (dbBean.gtType() == OPayType.CASH) {
				RpNoteCashPayDAO.insByBill(getB());
			} else {
				RpNotePayDAO.insByBill(getB());
			}
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RpNotePayBill> {

		@Override
		public void run() {
			super.run();
			RpNotePayBill mode = loadThisBeanAndLock();
			RpJournalLineDAO.delByMv(mode.gtLongPkey());//根据付款单，删除出纳日记账中相应的出纳日记账明细；
			assertStatusIsTally(mode);
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			System.out.println("longPkey:" + getB().gtLongPkey());
			GlNoteDAO.delByBill(getB().gtLongPkey());
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

}
