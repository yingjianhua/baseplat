package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysTable;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.gl.Gl.ODirect;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.gl.pya.PyaNoteAccountPayableDAO;
import irille.gl.pya.PyaNoteAccountPayableLine;
import irille.gl.pya.PyaNoteAccountPayableLineDAO;
import irille.gl.pya.PyaNoteDepositPayable;
import irille.gl.pya.PyaNoteDepositPayableDAO;
import irille.gl.pya.PyaNoteDepositPayableLine;
import irille.gl.pya.PyaNoteDepositPayableLineDAO;
import irille.gl.pya.PyaNotePayable;
import irille.gl.pya.PyaNotePayableDAO;
import irille.gl.pya.PyaNotePayableLine;
import irille.gl.pya.PyaNotePayableLineDAO;
import irille.gl.rp.Rp;
import irille.gl.rp.RpJournalLineDAO;
import irille.gl.rp.RpNoteCashPay;
import irille.gl.rp.RpNoteCashPayDAO;
import irille.gl.rp.RpNoteCashRpt;
import irille.gl.rp.RpNoteCashRptDAO;
import irille.gl.rp.RpNotePay;
import irille.gl.rp.RpNotePayBill;
import irille.gl.rp.RpNotePayDAO;
import irille.gl.rp.RpNoteRpt;
import irille.gl.rp.RpNoteRptBill;
import irille.gl.rp.RpNoteRptDAO;
import irille.gl.rva.RvaNoteAccount;
import irille.gl.rva.RvaNoteAccountDAO;
import irille.gl.rva.RvaNoteAccountLine;
import irille.gl.rva.RvaNoteAccountLineDAO;
import irille.gl.rva.RvaNoteDeposit;
import irille.gl.rva.RvaNoteDepositDAO;
import irille.gl.rva.RvaNoteDepositLine;
import irille.gl.rva.RvaNoteDepositLineDAO;
import irille.gl.rva.RvaNoteOther;
import irille.gl.rva.RvaNoteOtherDAO;
import irille.gl.rva.RvaNoteOtherLine;
import irille.gl.rva.RvaNoteOtherLineDAO;
import irille.pub.ClassTools;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduUnapprove;
import irille.pub.inf.IRecBack;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class GlNoteDAO {
	public static final Log LOG = new Log(GlNoteDAO.class);

	/**
	 * 对NOTE来源单据作挂账回款登记，一般由核销单记账时由核销的接口中自动调用
	 * @param mainNote 核销记账时，指定的主核销计划NOTE
	 * @param recAmt 核销计划的已回款金额
	 */
	public static void recBackByWriteoff(GlNote mainNote, BigDecimal recAmt) {
		Class clazz = Bean.gtLongClass(mainNote.getBill());
		Class clazzDao;
		try {
			clazzDao = Class.forName(clazz.getName() + "DAO");
		} catch (ClassNotFoundException e) {
			return;
		}
		try {
			if (IRecBack.class.isAssignableFrom(clazzDao)) {
				IRecBack iback = (IRecBack) clazzDao.newInstance();
				iback.updRecBack(mainNote.gtBill(), recAmt);
			}
		} catch (Exception e) {
			throw LOG.err("recBack", "{0} 回款登记失败!", mainNote.gtBill());
		}
	}

	public static List<GlNote> getByBill(long billpkey, boolean lock) {
		String wheresql = GlNote.T.BILL.getFld().getCodeSqlField() + "=?";
		return BeanBase.list(GlNote.class, wheresql, lock, billpkey);
	}

	public static GlNote getByBillOne(long billpkey, boolean lock) {
		List<GlNote> list = getByBill(billpkey, lock);
		if (list == null || list.size() == 0)
			return null;
		return list.get(0);
	}

	public static void delByBill(long billpkey) {
		String wheresql = GlNote.T.BILL.getFld().getCodeSqlField() + "=?";
		List<GlNote> listOld = BeanBase.list(GlNote.class, wheresql, true, billpkey);
		for (GlNote line : listOld)
			del(line);
	}

	/**
	 * 根据BILL编号删除所属的便签，制定便签类型集合，为自动产生的便签
	 * @param billpkey
	 * @param ids
	 */
	public static void delByBillAuto(long billpkey, String ids) {
		String wheresql = GlNote.T.IS_AUTO.getFld().getCodeSqlField() + "=1 and "
		    + GlNote.T.BILL.getFld().getCodeSqlField() + "=? and " + GlNote.T.EXT_TABLE.getFld().getCodeSqlField()
		    + " in (" + ids + ")";
		List<GlNote> listOld = BeanBase.list(GlNote.class, wheresql, true, billpkey);
		for (GlNote line : listOld)
			del(line);
	}

	public static void del(GlNote note) {
		if (note.gtStatus() != Sys.OBillStatus.INIT)
			throw LOG.err("assertStatus", "记账条[{0}：{1}]的状态为[" + note.gtStatus().getLine().getName() + "]，不可操作！", note
			    .gtJournal().getCode(), note.gtJournal().getName());
		if (note.getExtTable() != null)
			BeanBase.load(SysTable.gtTable(note.getExtTable()).gtClazz(), note.getPkey()).del();
		note.del();
	}

	public static class Del extends IduDel {
		@Override
		public void run() {
			del((GlNote) getB());
		}
	}

	private static int getTbId(Class clazz) {
		return SysTable.gtTable(clazz).getPkey();
	}

	public static JSONObject loadExt(GlNote note) throws Exception {
		JSONObject lineJson = new JSONObject();
		if (note.getExtTable() == null) {
			lineJson.put("success", true);
			return lineJson;
		}
		BeanBill bill = (BeanBill) note.gtBill();
		Bean bean = BeanBase.load(SysTable.gtTable(note.getExtTable()).gtClazz(), note.getPkey());
		if (note.getExtTable() == getTbId(PyaNoteAccountPayable.class)) {
			PyaNoteAccountPayable b = (PyaNoteAccountPayable) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("应付账款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(PyaNoteAccountPayableLine.class)) {
			PyaNoteAccountPayableLine b = (PyaNoteAccountPayableLine) bean;
			lineJson.put("应付账款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(PyaNotePayable.class)) {
			PyaNotePayable b = (PyaNotePayable) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("应付款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(PyaNotePayableLine.class)) {
			PyaNotePayableLine b = (PyaNotePayableLine) bean;
			lineJson.put("应付款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(PyaNoteDepositPayable.class)) {
			PyaNoteDepositPayable b = (PyaNoteDepositPayable) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("预付账款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(PyaNoteDepositPayableLine.class)) {
			PyaNoteDepositPayableLine b = (PyaNoteDepositPayableLine) bean;
			lineJson.put("预付账款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(RvaNoteAccount.class)) {
			RvaNoteAccount b = (RvaNoteAccount) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("应收账款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(RvaNoteAccountLine.class)) {
			RvaNoteAccountLine b = (RvaNoteAccountLine) bean;
			lineJson.put("应收账款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(RvaNoteOther.class)) {
			RvaNoteOther b = (RvaNoteOther) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("其它应收款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(RvaNoteOtherLine.class)) {
			RvaNoteOtherLine b = (RvaNoteOtherLine) bean;
			lineJson.put("其它应收款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(RvaNoteDeposit.class)) {
			RvaNoteDeposit b = (RvaNoteDeposit) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
			lineJson.put("预收账款类型", b.gtType().getLine().getName());
		} else if (note.getExtTable() == getTbId(RvaNoteDepositLine.class)) {
			RvaNoteDepositLine b = (RvaNoteDepositLine) bean;
			lineJson.put("预收账款单据", bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		} else if (note.getExtTable() == getTbId(RpNoteCashPay.class)) {
			RpNoteCashPay b = (RpNoteCashPay) bean;
			lineJson.put("出纳", b.gtCashier().getName());
			lineJson.put("付款时间", b.getTranTime());
		} else if (note.getExtTable() == getTbId(RpNotePay.class)) {
			RpNotePay b = (RpNotePay) bean;
			lineJson.put("付款方式", b.gtType().getLine().getName());
			lineJson.put("票据日期", b.getDate());
			lineJson.put("实际付款日期", b.getPayDate());
			lineJson.put("收款方名称", b.getName());
			lineJson.put("收款方账号", b.getAccount());
			lineJson.put("收款方银行", b.getBank());
			lineJson.put("出纳", b.gtCashier().getName());
		} else if (note.getExtTable() == getTbId(RpNoteCashRpt.class)) {
			RpNoteCashRpt b = (RpNoteCashRpt) bean;
			lineJson.put("出纳", b.gtCashier().getName());
			lineJson.put("收款时间 ", b.getTranTime());
		} else if (note.getExtTable() == getTbId(RpNoteRpt.class)) {
			RpNoteRpt b = (RpNoteRpt) bean;
			lineJson.put("收款方式", b.gtType().getLine().getName());
			lineJson.put("到账日期", b.getReceiveDate());
			lineJson.put("付款方名称", b.getName());
			lineJson.put("出纳", b.gtCashier().getName());
			lineJson.put("收款时间", b.getTranTime());
		} else if (note.getExtTable() == getTbId(GlNoteWriteoff.class)) {
			GlNoteWriteoff b = (GlNoteWriteoff) bean;
			lineJson.put("状态", b.gtWriteoffState().getLine().getName());
			lineJson.put("余额", b.getBalance());
			lineJson.put("起始日期", b.getDateStart());
			lineJson.put("到期日期", b.getDateDue());
		} else if (note.getExtTable() == getTbId(GlNoteWriteoffLine.class)) {
			GlNoteWriteoffLine b = (GlNoteWriteoffLine) bean;
			lineJson.put("销账计划编号", b.getMainNote());//TODO//销账计划编号 *字段名可能不合适
		} else {
			lineJson.put("success", false);
			lineJson.put("msg", "该扩展属性表未处理！！");
			return lineJson;
		}
		if (!lineJson.isNull("状态")) {
			if (lineJson.isNull("起始日期"))
				lineJson.put("起始日期", "");
			if (lineJson.isNull("到期日期"))
				lineJson.put("到期日期", "");
		}
		lineJson.put("success", true);
		return lineJson;
	}

	public static GlNote insAuto(BeanBill bill, BigDecimal amt, GlJournal journal, ODirect direct, Integer extTable) {
		return insAuto(bill, amt, journal, direct, extTable, null, "");
	}

	public static GlNote insAuto(BeanBill bill, BigDecimal amt, GlJournal journal, ODirect direct, Integer extTable,
	    String docNum, String summary) {
		GlNote note = new GlNote().init();
		note.setBill(bill.gtLongPkey());
		note.stJournal(journal);
		note.stDirect(direct);
		note.setAmt(amt);
		if (summary == null || summary.trim().equals(""))
			note.setSummary(bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		else
			note.setSummary(summary);
		note.setDocNum(docNum);
		note.stIsAuto(true);
		note.setExtTable(extTable);
		note.setOrg(bill.getOrg());
		note.setDept(bill.getDept());
		note.setCell(bill.getCell());
		note.setRem(bill.getRem());
		note.ins();
		return note;
	}

	public static void insByTb(GlNote note, String tableCode, String slip, int slipPkey, Date dateStart, Date dateDue,
	    Object obj) throws Exception {
		note.stBill(((Bean) Class.forName(tableCode).newInstance()).load(note.getPkey()));
		BeanBill bill = (BeanBill) note.gtBill();
		if (slip != null && !"".equals(slip)) {
			note.setExtTable(SysTable.gtTable(Class.forName(slip)).getPkey());
		}
		if (note.getSummary() == null || note.getSummary().trim().equals(""))
			note.setSummary(bill.gtTB().getShortName() + "(" + bill.getCode() + ")");
		note.stOrg(bill.gtOrg());
		note.stDept(bill.gtDept());
		note.stCell(bill.gtCell());
		note.stStatus(OBillStatus.INIT);
		note.setCreatedBy(Env.INST.getTran().getUser().getPkey());
		note.setCreatedTime(Env.INST.getWorkDate());
		note.stIsAuto(false);
		if (Str.isEmpty(slip)) {
			note.ins();
			return;
		} else if (slip.equals(PyaNoteAccountPayable.class.getName())) {
			PyaNoteAccountPayableDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(PyaNoteAccountPayableLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			PyaNoteAccountPayableLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(PyaNotePayable.class.getName())) {
			PyaNotePayableDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(PyaNotePayableLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			PyaNotePayableLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(PyaNoteDepositPayable.class.getName())) {
			PyaNoteDepositPayableDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(PyaNoteDepositPayableLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			PyaNoteDepositPayableLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(RvaNoteAccount.class.getName())) {
			RvaNoteAccountDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(RvaNoteAccountLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			RvaNoteAccountLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(RvaNoteOther.class.getName())) {
			RvaNoteOtherDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(RvaNoteOtherLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			RvaNoteOtherLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(RvaNoteDeposit.class.getName())) {
			RvaNoteDepositDAO.insByNote(note, dateStart, dateDue);
		} else if (slip.equals(RvaNoteDepositLine.class.getName())) {
			note.setJournal(GlNote.load(GlNote.class, slipPkey).getJournal());
			RvaNoteDepositLineDAO.insByNote(note, slipPkey);
		} else if (slip.equals(RpNotePayBill.class.getName())) {
			if (((RpNotePayBill) obj).gtType() == Rp.OPayType.CASH) {
				note.setExtTable(SysTable.gtTable(RpNoteCashPay.class).getPkey());
				RpNoteCashPayDAO.insByNote(note, (RpNotePayBill) obj);
			} else {
				note.setExtTable(SysTable.gtTable(RpNotePay.class).getPkey());
				RpNotePayDAO.insByNote(note, (RpNotePayBill) obj);
			}
			RpJournalLineDAO.addByBill(note);//在对应的出纳日记账中，添加出纳日记账明细；
		} else if (slip.equals(RpNoteRptBill.class.getName())) {
			if (((RpNoteRptBill) obj).gtType() == Rp.ORptType.CASH) {
				note.setExtTable(SysTable.gtTable(RpNoteCashRpt.class).getPkey());
				RpNoteCashRptDAO.insByNote(note, (RpNoteRptBill) obj);
			} else {
				note.setExtTable(SysTable.gtTable(RpNoteRpt.class).getPkey());
				RpNoteRptDAO.insByNote(note, (RpNoteRptBill) obj);
			}
			RpJournalLineDAO.addByBill(note);//在对应的出纳日记账中，添加出纳日记账明细；
		}
	}

	public static void loadByTb() {

	}
	
	public static class Approve extends IduApprove<Approve, GlNote> {

		private String _pkeys = null;
		private List<GlNote> _beans = null;
		public String getPkeys() {
			return _pkeys;
		}

		public void setPkeys(String pkeys) {
			_pkeys = pkeys;
		}

		public List<GlNote> getBeans() {
			return _beans;
		}

		public void setBeans(List<GlNote> beans) {
			_beans = beans;
		}

		@Override
		public void run() {
			super.run();
			_beans = new ArrayList<GlNote>();
			String[] arys = _pkeys.split(",");
			for(String spkey : arys) {
				GlNote dbBean = loadAndLock(Integer.parseInt(spkey));
				if (arys.length == 1)
					setB(dbBean);
				if (dbBean.gtStatus() != Sys.OBillStatus.INIT) {
					continue;
				}
				dbBean.stStatus(STATUS.TALLY_ABLE);
				dbBean.setApprBy(getUser().getPkey());
				dbBean.setApprTime(Env.INST.getWorkDate());
				dbBean.upd();
				_beans.add(dbBean);
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GlNote> {
		private String _pkeys = null;
		private List<GlNote> _beans = null;
		public String getPkeys() {
			return _pkeys;
		}

		public void setPkeys(String pkeys) {
			_pkeys = pkeys;
		}

		public List<GlNote> getBeans() {
			return _beans;
		}

		public void setBeans(List<GlNote> beans) {
			_beans = beans;
		}
		@Override
		public void run() {
			super.run();
			_beans = new ArrayList<GlNote>();
			String[] arys = _pkeys.split(",");
			for(String spkey : arys) {
				GlNote dbBean = loadAndLock(Integer.parseInt(spkey));
				if (arys.length == 1)
					setB(dbBean);
				if (dbBean.gtStatus() != Sys.OBillStatus.TALLY_ABLE) {
					continue;
				}
				dbBean.stStatus(STATUS_INIT);
				dbBean.setApprBy(null);
				dbBean.setApprTime(null);
				dbBean.upd();
				_beans.add(dbBean);
			}
		}
	}
}
