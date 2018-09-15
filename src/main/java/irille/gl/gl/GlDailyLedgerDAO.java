package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.pub.DateTools;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class GlDailyLedgerDAO {

	public final static String HQL_NEXT_DATE = Idu.sqlString("{0}=? and {1}=? and ({2}!=0 or {3}!=0)",
	    GlDailyLedger.T.ORG, GlDailyLedger.T.WORK_DATE, GlDailyLedger.T.DR_BALANCE, GlDailyLedger.T.CR_BALANCE);

	/**
	 * 更新到下一日
	 */
	public static Date nextDate(Date today, SysOrg org) {
		List<GlDailyLedger> list = BeanBase.list(GlDailyLedger.class, HQL_NEXT_DATE, false, org.getPkey(), today);
		GlDailyLedger m;
		Date nextDate = DateTools.addDate(today, 1);
		for (GlDailyLedger line : list) {
			m = new GlDailyLedger().init();
			m.setCell(line.getCell());
			m.setSubject(line.getSubject());
			m.setWorkDate(nextDate);
			m.setOrg(org.getPkey());
			m.setDrBalance(line.getDrBalance());
			m.setCrBalance(line.getCrBalance());
			m.ins();
		}
		return nextDate;
	}

	/**
	 * 更新日总账
	 * @param journal
	 * @param daybook
	 * @param line
	 * @param isTally TRUE为记账、FALSE为取消记账
	 */
	public static void updByTally(GlJournal journal, GlDaybook daybook, GlDaybookLine line, boolean isTally) {
		// 取日总账记录
		GlDailyLedger ledger = GlDailyLedger.chkUniqueCellSubjectWorkDate(true, daybook.getCell(), journal.getSubject(),
		    daybook.getWorkDate());
		if (ledger == null) {
			ledger = new GlDailyLedger().init();
			ledger.setOrg(daybook.getOrg());
			ledger.setCell(daybook.getCell());
			ledger.setSubject(journal.getSubject());
			ledger.setWorkDate(daybook.getWorkDate());
			ledger.ins();
		}
		int fsbs = 1;
		BigDecimal amt = line.getAmt();
		if (isTally == false) { //记账取消时减去发生比数
			fsbs = -1;
			amt = amt.negate();
		}
		Gl.ODirect direct = line.gtDirect();
		// 根据借贷标志置发生额
		if (direct == Gl.ODirect.DR) {
			ledger.setDrAmt(ledger.getDrAmt().add(amt));
			ledger.setDrQty(ledger.getDrQty()+fsbs);
		} else {
			ledger.setCrAmt(ledger.getCrAmt().add(amt));
			ledger.setCrQty(ledger.getCrQty()+fsbs);
		}
		if (ledger.gtSubject().gtDirect() == Gl.ODirect.DR) {
			if (direct == Gl.ODirect.DR)
				ledger.setDrBalance(ledger.getDrBalance().add(amt));
			else
				ledger.setDrBalance(ledger.getDrBalance().subtract(amt));
		} else {
			if (direct == Gl.ODirect.CR)
				ledger.setCrBalance(ledger.getCrBalance().add(amt));
			else
				ledger.setCrBalance(ledger.getCrBalance().subtract(amt));
		}
		// 如果余额、发生额都为0，则删除当前记录，否则更新记录。
		if (ledger.getCrAmt().signum() == 0 && ledger.getDrAmt().signum() == 0 && ledger.getCrBalance().signum() == 0
		    && ledger.getDrBalance().signum() == 0)
			ledger.del();
		else
			ledger.upd();
	}

}
