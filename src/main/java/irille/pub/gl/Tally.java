package irille.pub.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.Gl;
import irille.gl.gl.GlDailyLedgerDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlDaybookLine;
import irille.gl.gl.GlGoodsDAO;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.GlTransform;
import irille.gl.gl.Gl.OFrostFlag;
import irille.gl.gl.Gl.OJlState;
import irille.gl.gl.Gl.OTallyState;
import irille.gl.rp.RpStimatePayDAO;
import irille.gl.rp.RpStimateRecDAO;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstSalInvoice;
import irille.pub.ClassTools;
import irille.pub.DateTools;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.bean.IBill;
import irille.pub.gl.TallyLineClasses.TallyLine;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.svr.ISvrVars;
import irille.pub.svr.Svr.IRunSvr;

import java.util.List;
import java.util.Vector;

public class Tally implements ISvrVars, IRunSvr {
	private static final Log LOG = new Log(Tally.class);
	private Vector<TallyLines> _lines = new Vector<TallyLines>();
	private Vector<Bean> _accObjs = null;
	private IBill _bill;
	private List<GlNote> _notes;

	public static void main(String[] args) {
		runTally(new GlTransform().load(120003));
		System.err.println("OK!");
	}

	public Tally(IBill bill) {
		_bill = bill;
		_notes = GlNoteDAO.getByBill(_bill.gtLongPkey(), false);
	}

	public static void runTally(IBill bill) {
		Tally tally = new Tally(bill);
		tally.tally();
	}

	public static void runTallyCancel(IBill bill) {
		if (DateTools.equalsYMD(bill.getTallyTime(), bill.gtOrg().getWorkDate()) == false)
			throw LOG.err("errDate", "只能取消当日的记账单据!");
		Tally tally = new Tally(bill);
		tally.tallyCancel();
	}

	@Override
	public void runSvr() {
		tally();
	}

	public static void checkOrgStatus(SysOrg org) {
		if (org.gtState() != Sys.OOrgState.OPENING)
			throw LOG.err("daying", "当前机构[{0}]日终处理中，不可作记账相关操作!", org.getName());
		if (org.getPkey().equals(Idu.getOrg().getPkey()) == false)
			throw LOG.err("errOrg", "单据不属于机构[{0}]，不可作记账相关操作!", Idu.getOrg().getName());
	}

	//记账方法入口
	public void tally() {
		// 记账时，将待处理单据删除
		FrmPendingDAO.del(_bill, GlDaybook.TB);
		// 检查单据及凭条状态及收付款状态、并更改单据与凭条信息
		if (_bill.gtStatus() != OBillStatus.TALLY_ABLE)
			throw LOG.err("errStatus", "{0}[{1}]的状态为'{2}'，不可记账!", ((BeanBill) _bill).gtTB().getName(), _bill.getCode(), _bill
			    .gtStatus().getLine().getName());
		RpStimateRecDAO.isTally(_bill);
		RpStimatePayDAO.isTally(_bill);
		for (GlNote line : _notes) {
			if (line.gtStatus() != OBillStatus.TALLY_ABLE)
				throw LOG.err("errStatus", "{0}[{1}]对应的凭条[{2}]的状态为[{3}]，不可记账!", ((BeanBill) _bill).gtTB().getName(),
				    _bill.getCode(), line.gtJournal().getName(), line.gtStatus().getLine().getName());
			line.stStatus(OBillStatus.DONE);
			line.upd();
			//下面是对核销类型NOTE作处理，用于更新原核销计划中的余额及状态
			if (line.getExtTable() == null)
				continue;
			Class extClazz = SysTable.gtTable(line.getExtTable()).gtClazz();
			if (extClazz.isAssignableFrom(IWriteoff.class) )
				((IWriteoff) BeanBase.load(extClazz, line.getPkey())).tallyWriteoff(line);
		}
		_bill.stStatus(OBillStatus.DONE);
		_bill.setTallyTime(_bill.gtOrg().getWorkDate());
		_bill.stTallyBy(Env.INST.getTran().getUser());
		((BeanBill) _bill).upd();

		// 插入流水账
		GlDaybook daybook = new GlDaybook().init();
		daybook.setBill(((BeanBill) _bill).gtLongPkey());
		daybook.setCode(((BeanBill) _bill).getCode());
		daybook.stTallyBy(Env.getTran().getUser());
		daybook.setDept(((BeanBill) _bill).getDept());
		daybook.setCell(((BeanBill) _bill).getCell());
		daybook.setOrg(((BeanBill) _bill).getOrg());
		daybook.setWorkDate(_bill.gtOrg().getWorkDate());
		daybook.ins();

		TallyLines allLines = merge(); //合并所有单据的凭条
		GlJournal journal;
		GlDaybookLine daybookLine;
		int i = 1;
		long firstPkey = Idu.lineFirstPkey(daybook.getPkey());
		for (TallyLine tallyLine : (Vector<TallyLine>) allLines.getLines()) {
			journal = tallyLine.getJournal().loadAndLock(tallyLine.getJournal().getPkey());
			daybookLine = new GlDaybookLine().init();
			tallyDaybookLine(tallyLine, journal, daybookLine, firstPkey + (i++)); //记流水明细
			//存货账处理
			GlGoodsDAO.tally(daybookLine, tallyLine);
			//汇总入账时不再重复更新分户账的余额、日总账
			if (tallyLine.gtTallyState() != OTallyState.DONE_TOTAL) {
				GlDailyLedgerDAO.updByTally(journal, daybook, daybookLine, true); //更新日总账
				tallyJournal(journal, daybook, daybookLine); //更新分户账
			}
			//逐笔记账或汇总的合并分录，则记分户账明细行
			switch (tallyLine.gtTallyState()) {
			case DONE_ONE:
			case DONE_TOTAL:
				journal.gtJournalExt().tallyLine(daybook, daybookLine);
			}
		}

	}

	//记账取消
	public void tallyCancel() {
		// 记账取消时，将待处理单据添加回去
		FrmPendingDAO.ins(_bill, GlDaybook.TB);
		// 检查单据及凭条状态、并更改状态及记账信息
		if (_bill.gtStatus() != OBillStatus.DONE)
			throw LOG.err("errStatus", "{0}[{1}]的状态为'{2}'，不可取消记账!", ((BeanBill) _bill).gtTB().getName(), _bill.getCode(),
			    _bill.gtStatus().getLine().getName());
		for (GlNote line : _notes) {
			if (line.gtStatus() != OBillStatus.DONE)
				throw LOG.err("errStatus", "{0}[{1}]对应的凭条[{3}]的状态为'{2}'，不可取消记账!", ((BeanBill) _bill).gtTB().getName(),
				    _bill.getCode(), _bill.gtStatus().getLine().getName(), line.gtJournal().getName());
			line.stStatus(OBillStatus.TALLY_ABLE);
			line.upd();
			//撤销对核销类型NOTE作的处理
			if (line.getExtTable() == null)
				continue;
			Class extClazz = SysTable.gtTable(line.getExtTable()).gtClazz();
			if (IWrite.class.isAssignableFrom(extClazz))
				((IWrite) BeanBase.load(extClazz, line.getPkey())).tallyCancel(line);
			if (IWriteoff.class.isAssignableFrom(extClazz))
				((IWriteoff) BeanBase.load(extClazz, line.getPkey())).tallyWriteoffCancel(line);
		}
		_bill.stStatus(OBillStatus.TALLY_ABLE);
		_bill.setTallyTime(null);
		_bill.stTallyBy(null);
		((BeanBill) _bill).upd();

		// 取流水账及明细
		GlDaybook daybook = GlDaybook.chkUniqueBill(true, ((BeanBill) _bill).gtLongPkey());
		List<GlDaybookLine> bookLines = Idu.getLinesTid(daybook, GlDaybookLine.class);
		GlJournal journal;
		for (GlDaybookLine line : bookLines) {
			journal = line.gtJournal();
			//取消存货账处理
			GlGoodsDAO.tallyCancel(line);
			if (line.gtTallyState() != OTallyState.DONE_TOTAL) {
				tallyJournalCancel(journal, daybook, line); //更新分户账
				GlDailyLedgerDAO.updByTally(journal, daybook, line, false); //更新日总账
			}
			//删除分户账金额行，并更新之后所有行的余额
			switch (line.gtTallyState()) {
			case DONE_ONE:
			case DONE_TOTAL:
				journal.gtJournalExt().tallyLineCancel(daybook, line);
			}
			line.del();//删除流水账明细行
		}
		daybook.del();
	}

	/**
	 * 记分户账
	 */
	public void tallyJournal(GlJournal journal, GlDaybook daybook, GlDaybookLine line) {
		if (journal.getState() == OJlState.CLOSE.getLine().getKey())
			throw LOG.err("isClose", "账户【{0}:{1}】已销户，不可记账！", journal.getCode(), journal.getName());
		//XXX 检查余额控制的代码待增加
		OFrostFlag frost = journal.gtFrostFlag();
		if ((frost == OFrostFlag.NO_DR || frost == OFrostFlag.NO) && line.getDirect() == Bean.DR
		    || (frost == OFrostFlag.NO_CR || frost == OFrostFlag.NO) && line.getDirect() == Bean.CR)
			throw LOG.err("no", "账户【{0}:{1}】冻结标志非正常，不可记账{2}方的账！", journal.getCode(), journal.getName(), line.gtDirect()
			    .getLine().getName());

		if (journal.getDirect() == line.getDirect()) {
			journal.setBalance(journal.getBalance().add(line.getAmt()));
			journal.setBalanceUse(journal.getBalanceUse().add(line.getAmt()));
		} else {
			journal.setBalance(journal.getBalance().subtract(line.getAmt()));
			journal.setBalanceUse(journal.getBalanceUse().subtract(line.getAmt()));
		}
		journal.gtJournalExt().tallyExt(daybook, line); //调用扩展表的更新分户账处理
		journal.upd();
	}

	public void tallyJournalCancel(GlJournal journal, GlDaybook daybook, GlDaybookLine line) {
		if (journal.getDirect() == line.getDirect()) {
			journal.setBalance(journal.getBalance().subtract(line.getAmt()));
			journal.setBalanceUse(journal.getBalanceUse().subtract(line.getAmt()));
		} else {
			journal.setBalance(journal.getBalance().add(line.getAmt()));
			journal.setBalanceUse(journal.getBalanceUse().add(line.getAmt()));
		}
		journal.gtJournalExt().tallyExtCancel(daybook, line); //调用扩展表的更新分户账处理
		journal.upd();
	}

	/**
	 * 记流水明细
	 */
	private void tallyDaybookLine(TallyLine tallyLine, GlJournal journal, GlDaybookLine daybookLine, long pkey) {
		daybookLine.setPkey(pkey); //主键按算法产生
		daybookLine.stJournal(journal);
		daybookLine.setTallyState(tallyLine.getTallyState());
		daybookLine.setDirect(tallyLine.getDirect());
		daybookLine.setAmt(tallyLine.getAmt());
		daybookLine.setCurrency(tallyLine.getCurrency()); //币种
		daybookLine.setSummary(tallyLine.getSummary());
		daybookLine.setInFlag(journal.getInFlag());
		daybookLine.setDocNum(tallyLine.getDocNum());
		daybookLine.setCell(journal.getCell());
		daybookLine.stAgentCell(Env.getTran().getCell());
		daybookLine.ins();
	}

	public TallyLines merge() {
		// 对单据内并的分录进行合并到allLines中
		TallyLines allLines = new TallyLines();
		mergeLines(allLines, _bill);
		for (GlNote note : _notes) {
			mergeLines(allLines, note);
		}
		//内转单汇总入账的特殊处理
		if (_bill instanceof GlTransform) {
			GlTransform tf = (GlTransform) _bill;
			if (tf.gtType() == Gl.OTransformType.SUM)
				for (TallyLine tallyLine : (Vector<TallyLine>) allLines.getLines()) {
					tallyLine.stTallyState(OTallyState.DONE_TOTAL);
				}
		}
		allLines.balanceCheck();
		return allLines;
	}

	/**
	 * 合并Bill或Note的分录，如单据合并记账的合并为一条记录
	 * 
	 * @param ls
	 * @param tallyBean
	 */
	private void mergeLines(TallyLines ls, ITallyBean tallyBean) {
		TallyLines beanLs = new TallyLines();
		tallyBean.initTallyLines(beanLs);
		ls.mergeAndVerify(beanLs);
	}

}
