package irille.gl.rp;

import irille.core.sys.SysUser;
import irille.gl.gl.GlNote;
import irille.gl.rp.Rp.ORptType;
import irille.gl.rp.RpJournalLine.ODC;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.svr.Env;

import java.util.List;

public class RpJournalLineDAO {
	public static final Log LOG = new Log(RpJournalLineDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		nojournal("出纳帐户不能为空！"),
		nodc("出付标志不可不为！");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static class Ins extends IduIns<IduIns, RpJournalLine> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			if (getB().getJournal() == null)
				throw LOG.err(Msgs.nojournal);
			if (getB().getDC() == null)
				throw LOG.err(Msgs.nodc);
			RpJournal journal = getB().gtJournal();
			getB().setOrg(journal.getOrg());
			getB().setCell(journal.getCell());
			if (getB().getDC() == ODC.DR.getLine().getKey())
				getB().setBalance(journal.getBalance().add(getB().getAmt()));
			else
				getB().setBalance(journal.getBalance().subtract(getB().getAmt()));
			;
			getB().setCashier(journal.getCashier());
			getB().setCreatedTime(Env.getWorkDate());
			super.before();
		}

		@Override
		public void after() {
			// TODO Auto-generated method stub
			super.after();
			RpJournal journal = getB().gtJournal();
			journal.setBalance(getB().getBalance());
			if (getB().getDC() == ODC.DR.getLine().getKey()) {
				journal.setDrAmt(journal.getDrAmt().add(getB().getAmt()));
				journal.setDrQty(journal.getDrQty() + 1);
			} else {
				journal.setCrAmt(journal.getCrAmt().add(getB().getAmt()));
				journal.setCrQty(journal.getCrQty() + 1);
			}
			journal.upd();
		}
	}

	public static class Del extends IduDel<Del, RpJournalLine> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			//将在此明细之后添加的明细的余额字段更新；
			String afterSql = Idu.sqlString("{0}=? AND {1}>?", RpJournalLine.T.JOURNAL, RpJournalLine.T.PKEY);
			List<RpJournalLine> olist = BeanBase.list(RpJournalLine.class, afterSql, true, getB().getJournal(), getB()
			    .getPkey());
			for (RpJournalLine oline : olist) {
				if (getB().gtDC() == ODC.CR)//付款
					oline.setBalance(oline.getBalance().add(getB().getAmt()));
				else
					oline.setBalance(oline.getBalance().subtract(getB().getAmt()));
				oline.upd();
			}

			RpJournal journal = getB().gtJournal();
			if (getB().getDC() == ODC.DR.getLine().getKey()) {
				journal.setDrAmt(journal.getDrAmt().subtract(getB().getAmt()));
				journal.setDrQty(journal.getDrQty() - 1);
				journal.setBalance(journal.getBalance().subtract(getB().getAmt()));
			} else {
				journal.setCrAmt(journal.getCrAmt().subtract(getB().getAmt()));
				journal.setCrQty(journal.getCrQty() - 1);
				journal.setBalance(journal.getBalance().add(getB().getAmt()));
			}
			journal.upd();
		}
	}

	public static void delByMv(Long mvId) {
		List<RpJournalLine> list = BeanBase.list(RpJournalLine.class, "bill=?", false, mvId);
		RpJournalLineDAO.Del delDao = new RpJournalLineDAO.Del();
		for (RpJournalLine line : list) {
			delDao.setB(line);
			delDao.commit();
		}
	}

	public static void addByBill(GlNote note) {
		ODC dc = null;
		String type = null;
		SysUser cashier = null;
		if (note.getExtTable() == RpNotePay.TB.getID()) {//非现金付款
			RpNotePay pay = BeanBase.load(RpNotePay.class, note.getPkey());
			type = pay.gtType().getLine().getName();
			cashier = pay.gtCashier();
			dc = ODC.CR;
		} else if (note.getExtTable() == RpNoteRpt.TB.getID()) {//非现金收款
			RpNoteRpt rpt = BeanBase.load(RpNoteRpt.class, note.getPkey());
			type = rpt.gtType().getLine().getName();
			cashier = rpt.gtCashier();
			dc = ODC.DR;
		} else if (note.getExtTable() == RpNoteCashPay.TB.getID()) {//现金付款
			RpNoteCashPay pay = BeanBase.load(RpNoteCashPay.class, note.getPkey());
			type = ORptType.CASH.getLine().getName();
			cashier = pay.gtCashier();
			dc = ODC.CR;
		} else if (note.getExtTable() == RpNoteCashRpt.TB.getID()) {//现金收款
			RpNoteCashRpt rpt = BeanBase.load(RpNoteCashRpt.class, note.getPkey());
			type = ORptType.CASH.getLine().getName();
			cashier = rpt.gtCashier();
			dc = ODC.DR;
		}
		addByBill(note, dc, type, cashier);
	}

	public static void addByBill(GlNote note, ODC dc, String type, SysUser cashier) {
		RpJournalLine line = new RpJournalLine();
		line.setCreatedTime(Env.getWorkDate());
		line.stNote(note);
		line.setOrg(note.getOrg());
		line.setCell(note.getCell());
		line.setJournal(note.getJournal());
		line.setBill(note.getBill());
		line.setType(type);
		line.setCashier(cashier.getPkey());
		line.stDC(dc);
		line.setAmt(note.getAmt());
		updateBalance(line);
		line.setDoc(note.getDocNum());
		line.setSummary(note.getSummary());
		line.ins();
	}

	public static void updateBalance(RpJournalLine line) {
		RpJournal journal = line.gtJournal();
		if (line.gtDC() == ODC.CR) {//付款
			journal.setCrQty(journal.getCrQty() + 1);
			journal.setCrAmt(journal.getCrAmt().add(line.getAmt()));
			journal.setBalance(journal.getBalance().subtract(line.getAmt()));
			line.setBalance(journal.getBalance());
		} else {
			journal.setDrQty(journal.getDrQty() + 1);
			journal.setDrAmt(journal.getDrAmt().add(line.getAmt()));
			journal.setBalance(journal.getBalance().add(line.getAmt()));
			line.setBalance(journal.getBalance());
		}
		journal.upd();
	}
}
