package irille.gl.rp;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.rp.RpFundMvOld.OMvType;
import irille.gl.rp.RpJournalLine.ODC;
import irille.pss.sal.Sal;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.gl.AccObjs;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class RpFundMvOldDAO {
	public static final Log LOG = new Log(RpFundMvOldDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		samejour("相同的帐户之间不可调拨!"),
		norole("你没有权限对该帐户做调出处理!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static class Ins extends IduInsLines<Ins, RpFundMvOld, RpFundMvOld> {
		@Override
		public void before() {
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!");
			// TODO Auto-generated method stub
			getB().setCode(SysSeqDAO.getSeqnumForm(RpFundMvOld.TB));
			if (getB().getSourceJournal().longValue() == getB().getDescJournal().longValue())
				throw LOG.err(Msgs.samejour);
			getB().stStatus(STATUS_INIT);

			getB().stCreatedBy(getB().gtSourceJournal().gtCashier());
			getB().setOrg(getB().gtSourceJournal().getOrg());
			getB().setDept(getB().gtCreatedBy().getDept());
			getB().stCell(getB().gtSourceJournal().gtCell());
			getB().setCreatedTime(Env.getWorkDate());

			getB().setDescCell(getB().gtDescJournal().getCell());
			getB().setDescBy(getB().gtDescJournal().getCashier());
			getB().setDescOrg(getB().gtDescJournal().getOrg());

			if (getB().getOrg().intValue() != getB().getDescOrg().intValue()) {
				getB().stType(OMvType.GRP);//跨机构
			} else if (getB().getCell().intValue() != getB().getDescCell().intValue()) {
				getB().stType(OMvType.ORG);//相同机构不同核算单元
			} else
				getB().stType(OMvType.CELL);//核算单元内部
		}
	}

	public static class Upd extends IduUpdLines<Upd, RpFundMvOld, RpFundMvOld> {

		@Override
		public void before() {
			RpFundMvOld rev = loadThisBeanAndLock();
			if (getB().getAmt().intValueExact() == -1)
				throw LOG.err("err", "金额不能为负数!");
			if (getB().getSourceJournal().longValue() == getB().getDescJournal().longValue())
				throw LOG.err(Msgs.samejour);
			//if (getB().gtSourceJournal().getCell() != SysCellDAO.getCellByUser(getUser()).getPkey())
			//throw LOG.err(Msgs.norole);
			PropertyUtils.copyProperties(rev, getB(), RpFundMvOld.T.SOURCE_JOURNAL, RpFundMvOld.T.DESC_JOURNAL, RpFundMvOld.T.AMT,
			    RpFundMvOld.T.REM);
			rev.stCreatedBy(getB().gtSourceJournal().gtCashier());
			rev.setOrg(getB().gtSourceJournal().getOrg());
			rev.stCell(getB().gtSourceJournal().gtCell());
			rev.setDept(getB().gtSourceJournal().gtCashier().getDept());

			rev.setDescBy(getB().gtDescJournal().getCashier());
			rev.setDescOrg(getB().gtDescJournal().getOrg());
			rev.setDescCell(getB().gtDescJournal().getCell());

			if (rev.getOrg().intValue() != rev.getDescOrg().intValue()) {
				rev.stType(OMvType.GRP);//跨机构
			} else if (rev.getCell().intValue() != rev.getDescCell().intValue()) {
				rev.stType(OMvType.ORG);//相同机构不同核算单元
			} else
				rev.stType(OMvType.CELL);//核算单元内部

			setB(rev);
		}
	}

	public static class Del extends IduDel<Del, RpFundMvOld> {

		@Override
		public void valid() {
			super.valid();
			RpFundMvOld hand = loadThisBeanAndLock();
			assertStatusIsInit(hand);
		}

	}

	public static class Approve extends IduApprove<Approve, RpFundMvOld> {

		@Override
		public void run() {
			super.run();
			RpFundMvOld handover = loadThisBeanAndLock();
			assertStatusIsInit(handover);
			handover.stStatus(Sys.OBillStatus.CHECKED);
			if (handover.gtCreatedBy().getPkey() != getUser().getPkey())
				throw LOG.err(Msgs.norole);
			handover.setApprBy(getUser().getPkey());
			handover.setApprTime(Env.getTranBeginTime());
			handover.upd();
			setB(handover);
			GlNote note = GlNoteDAO.insAuto(handover, handover.getAmt(), handover.gtSourceJournal().gtJournal(), ODirect.CR,
			    RpNoteCashPay.TB.getID());
			RpNoteCashPay ext = new RpNoteCashPay();
			ext.stNote(note);
			ext.stCashier(handover.gtCreatedBy());
			ext.setTranTime(handover.getApprTime());
			ext.ins();
			RpJournalLineDAO.addByBill(note, ODC.CR, "资金调拨", handover.gtCreatedBy());
			if (handover.gtType() != OMvType.CELL)
				GlNoteDAO.insAuto(handover, handover.getAmt(),
				    GlJournalDAO.getAutoCreate(handover.gtCell(), Sal.SubjectAlias.SAL_MV, new AccObjs()), ODirect.DR, null);
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RpFundMvOld> {
		@Override
		public void run() {
			super.run();
			RpFundMvOld rev = loadThisBeanAndLock();
			assertStatusIsCheck(rev);
			rev.stStatus(Sys.OBillStatus.INIT);
			rev.setApprBy(null);
			rev.setApprTime(null);
			rev.upd();
			setB(rev);
			RpJournalLineDAO.delByMv(rev.gtLongPkey());
			GlNoteDAO.delByBill(rev.gtLongPkey());
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

	public static class Rec extends IduOther<Rec, RpFundMvOld> {
		public void run() {
			super.run();
			RpFundMvOld handover = loadThisBeanAndLock();
			assertStatusIsCheck(handover);
			handover.stStatus(Sys.OBillStatus.TALLY_ABLE);
			if (handover.gtDescBy().getPkey() != getUser().getPkey())
				throw LOG.err(Msgs.norole);
			handover.setTakeOverTime(Env.getTranBeginTime());
			handover.setApprBy(getUser().getPkey());
			handover.upd();
			setB(handover);
			GlNote note = GlNoteDAO.insAuto(handover, handover.getAmt(), handover.gtDescJournal().gtJournal(), ODirect.DR,
			    RpNoteCashRpt.TB.getID());
			RpNoteCashRpt ext = new RpNoteCashRpt();
			ext.stNote(note);
			ext.stCashier(handover.gtDescBy());
			ext.setTranTime(handover.getTakeOverTime());
			ext.ins();
			RpJournalLineDAO.addByBill(note, ODC.DR, "资金调拨", handover.gtDescBy());
			if (handover.gtType() != OMvType.CELL)
				GlNoteDAO
				    .insAuto(handover, handover.getAmt(),
				        GlJournalDAO.getAutoCreate(handover.gtDescCell(), Sal.SubjectAlias.SAL_MV, new AccObjs()), ODirect.CR,
				        null);
		}
	}
}
