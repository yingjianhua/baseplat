package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSeqDAO;
import irille.core.sys.SysTable;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.gl.AccObjs;
import irille.pub.gl.Tally;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;

public class GlTransformDAO {
	public static final Log LOG = new Log(GlTransformDAO.class);

	/**
	 * 搜索当前工作日【待被汇总入账】的流水明细行记录，并更改为【已被汇总入账】
	 * 产生多张内转单(按核算单元分)，待汇总的分录合并后产生凭条，自动审核并记账
	 */
	public static void doTotal(SysOrg org) {
		String where = Idu.sqlString("{0}={1} and {2} div " + SysTable.NUM_BASE
		    + " in (select s.pkey from {3} s where s.{4}=? and s.{5}=?)", GlDaybookLine.T.TALLY_STATE,
		    Gl.OTallyState.WAIT_TOTAL, GlDaybookLine.T.PKEY, GlDaybook.class, GlDaybook.T.WORK_DATE, GlDaybook.T.ORG);
		List<GlDaybookLine> dayLines = BeanBase.list(GlDaybookLine.class, where, false, org.getWorkDate(), org.getPkey());
		if (dayLines == null || dayLines.size() == 0)
			return;
		HashMap<Long, TotalStruct> map = new HashMap<Long, TotalStruct>(); //<明细账主键值，结构>
		for (GlDaybookLine line : dayLines) {
			TotalStruct struct = map.get(line.getJournal());
			if (struct == null) {
				struct = new TotalStruct(line.gtJournal(), line.gtDirect(), line.getAmt());
				map.put(line.getJournal(), struct);
			} else {
				struct.add(line.gtDirect(), line.getAmt());
			}
			line.stTallyState(Gl.OTallyState.DONE_TOTAL_BY);
			line.upd();
		}
		//根据明细账的核算单元分组
		HashMap<Integer, Vector<TotalStruct>> cellMap = new HashMap<Integer, Vector<TotalStruct>>();
		for (TotalStruct line : map.values()) {
			Vector vec = cellMap.get(line._journal.getCell());
			if (vec == null) {
				vec = new Vector();
				cellMap.put(line._journal.getCell(), vec);
			}
			vec.add(line);
		}
		//每个核算单元建一张内转单，单据与凭条为可记账状态
		for (Integer cellPkey : cellMap.keySet()) {
			Vector<TotalStruct> vec = cellMap.get(cellPkey);
			GlTransform form = new GlTransform().init();
			form.stStatus(Sys.OBillStatus.TALLY_ABLE);
			form.stType(Gl.OTransformType.SUM);
			form.setCode(SysSeqDAO.getSeqnumForm(GlTransform.TB));
			form.stOrg(org);
			form.setDept(Idu.getUser().getDept());
			form.setCell(cellPkey);
			form.setRem("日终自动产生-待汇总处理");
			form.ins();
			for (TotalStruct line : vec) {
				GlNote note = newNote(line._journal, line._direct, line._amt, "日终汇总");
				note.setBill(form.gtLongPkey());
				note.setOrg(form.getOrg());
				note.setDept(form.getDept());
				note.setCell(form.getCell());
				note.ins();
			}
			FrmPendingDAO.ins(form, GlDaybook.TB);
			Tally.runTally(form); //直接记账，记账功能类中有检查机构状态
		}
	}

	/**
	 * 产生多张内转单(按核算单元分)
	 * 根据损益结转定义，自动产生内转单，自动审核并记账
	 */
	public static void doCarryOver(SysOrg org) {
		String whereGl = Idu.sqlString("{0}=?", GlCarryOver.T.TEMPLAT);
		List<GlCarryOver> list = BeanBase.list(GlCarryOver.class, whereGl, false, org.getTemplat());
		if (list == null && list.size() == 0)
			return;
		String whereCell = Idu.sqlString("{0}=?", SysCell.T.ORG);
		List<SysCell> cellList = BeanBase.list(SysCell.class, whereCell, false, org.getPkey());
		for (SysCell cell : cellList) { //机构内可能存在多个核算单元，自动产生多张内转单
			Vector<GlNote> noteVec = new Vector<GlNote>();
			for (GlCarryOver line : list) {
				Set<GlJournal> set = GlJournalDAO.getJournals(cell, line.gtSubjectSource()); //取所有该科目下的明细账
				GlJournal jnlDest = GlJournalDAO.getAutoCreate(cell, line.gtSubjectTarget(), new AccObjs());
				BigDecimal amtDr = BigDecimal.ZERO, amtCr = BigDecimal.ZERO;
				for (GlJournal jnlSrc : set) {
					if (jnlSrc.getBalance().signum() == 0)
						continue;
					Gl.ODirect dir = jnlSrc.gtSubject().gtDirect() == Gl.ODirect.DR ? Gl.ODirect.CR : Gl.ODirect.DR; //借贷方向反转
					noteVec.add(newNote(jnlSrc, dir, jnlSrc.getBalance(), "月末结转"));
					if (dir == Gl.ODirect.DR)
						amtCr = amtCr.add(jnlSrc.getBalance());
					else
						amtDr = amtDr.add(jnlSrc.getBalance());
				}
				BigDecimal amt = amtDr.subtract(amtCr);
				if (amt.signum() == 0) //源科目的分户账借贷金额0，则目标科目分户账不用新建
					continue;
				if (amt.signum() > 0)
					noteVec.add(newNote(jnlDest, ODirect.DR, amt, "月末结转"));
				if (amt.signum() < 0)
					noteVec.add(newNote(jnlDest, ODirect.CR, amt.negate(), "月末结转"));
			}
			if (noteVec.size() == 0)
				continue;
			//单据与凭条为可记账状态
			GlTransform form = new GlTransform().init();
			form.stStatus(Sys.OBillStatus.TALLY_ABLE);
			form.stType(Gl.OTransformType.CARRY);
			form.setCode(SysSeqDAO.getSeqnumForm(GlTransform.TB));
			form.stOrg(org);
			form.setDept(Idu.getUser().getDept());
			form.stCell(cell);
			form.setRem("月末自动产生-损益结转处理");
			form.ins();
			for (GlNote note : noteVec) {
				note.setOrg(form.getOrg());
				note.setDept(form.getDept());
				note.setCell(form.getCell());
				note.setBill(form.gtLongPkey());
				note.ins();
			}
			FrmPendingDAO.ins(form, GlDaybook.TB);
			Tally.runTally(form); //直接记账，记账功能类中有检查机构状态
		}
	}

	private static GlNote newNote(GlJournal journal, ODirect dir, BigDecimal amt, String name) {
		GlNote note = new GlNote();
		note.stJournal(journal);
		note.stDirect(dir);
		note.stStatus(OBillStatus.TALLY_ABLE);
		note.setAmt(amt);
		note.setSummary(name);
		note.stCreatedBy(Idu.getUser());
		note.setCreatedTime(Env.INST.getSystemTime());
		note.stIsAuto(false);
		return note;
	}

	public static class Ins extends IduInsLines<Ins, GlTransform, GlTransform> {

		@Override
		public void before() {
			super.before();
			Integer cellpkey = getB().getCell();
			initBill(getB());
			getB().setCell(cellpkey);
			getB().stType(Gl.OTransformType.NORMAL);
			if (getB().gtCell().getOrg().equals(getB().getOrg()) == false)
				throw LOG.err("sameOrg", "核算单元[{0}]所属的机构与当前单据机构[{1}]不一致!", getB().gtCell().getName(), getB().gtOrg().getName());
		}
	}

	public static class Upd extends IduUpdLines<Upd, GlTransform, GlTransform> {

		@Override
		public void before() {
			super.before();
			GlTransform tf = loadThisBeanAndLock();
			assertStatusIsInit(tf);
			tf.setRem(getB().getRem());
			tf.setCell(getB().getCell());
			setB(tf);
			if (getB().gtCell().getOrg().equals(getB().getOrg()) == false)
				throw LOG.err("sameOrg", "核算单元[{0}]所属的机构与当前单据机构[{1}]不一致!", getB().gtCell().getName(), getB().gtOrg().getName());

		}
	}

	public static class Del extends IduDel<Del, GlTransform> {

		@Override
		public void before() {
			super.before();
			GlTransform gain = loadThisBeanAndLock();
			assertStatusIsInit(gain);
		}

	}

	public static class Approve extends IduApprove<Approve, GlTransform> {

		@Override
		public void run() {
			super.run();
			GlTransform gain = loadThisBeanAndLock();
			assertStatusIsInit(gain);
			gain.stStatus(STATUS.TALLY_ABLE);
			gain.setApprBy(getUser().getPkey());
			gain.setApprTime(Env.INST.getSystemTime());
			gain.upd();
			setB(gain);
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GlTransform> {

		@Override
		public void run() {
			super.run();
			GlTransform gain = loadThisBeanAndLock();
			assertStatusIsTally(gain);
			gain.stStatus(STATUS_INIT);
			gain.setApprBy(null);
			gain.setApprTime(null);
			gain.upd();
			setB(gain);
			GlNoteDAO.delByBill(gain.gtLongPkey());
			// 取消待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

	static class TotalStruct {
		GlJournal _journal;
		ODirect _direct;
		BigDecimal _amt;

		TotalStruct(GlJournal gj, ODirect dt, BigDecimal amt) {
			_journal = gj;
			_direct = dt;
			_amt = amt;
		}

		void add(ODirect dt, BigDecimal amt) {
			if (_direct == dt)
				_amt = _amt.add(amt);
			else
				_amt = _amt.add(amt.negate());
		}

	}

}
