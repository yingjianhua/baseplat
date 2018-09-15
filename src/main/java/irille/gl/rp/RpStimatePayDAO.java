package irille.gl.rp;

import irille.core.sys.SysCustom;
import irille.core.sys.SysSupplier;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.GlNoteViewRp;
import irille.gl.gl.Gl.ODirect;
import irille.gl.rp.RpStimateRecDAO.Msgs;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.bean.IBill;
import irille.pub.svr.Env;

import java.math.BigDecimal;

public class RpStimatePayDAO {
	public static final Log LOG = new Log(RpStimatePayDAO.class);

	public enum Msgs implements IMsg {//@formatter:off
		errOrg("所选的出纳帐[{0}]，与单据的核算单元不符合!"),
		noData("单据对应的待付款登记信息不存!"),
		isDoing("单据对应的待付款登记，已存在付款信息，不可删除!"),
		noAmt("付款金额必须大于零!"),
		bigAmt("付款金额不可大于待付款登记中的[余额]!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void insByBill(IBill bill, SysCustom cust, BigDecimal amt) {
		insByBill(bill, cust.gtLongPkey(), cust.getName(), amt);
	}

	public static void insByBill(IBill bill, SysSupplier sup, BigDecimal amt) {
		insByBill(bill, sup.gtLongPkey(), sup.getName(), amt);
	}

	public static void insByBill(IBill bill, Long objkey, String objname, BigDecimal amt) {
		if (amt.signum() == 0)
			return;
		RpStimatePay mode = new RpStimatePay();
		mode.setOrigForm(bill.gtLongPkey());
		mode.setOrigFormNum(bill.getCode());
		mode.setObj(objkey);
		mode.setName(objname);
		mode.setAmt(amt);
		mode.setBalance(amt);
		mode.setDept(bill.getDept());
		mode.setCell(bill.getCell());
		mode.setOrg(bill.getOrg());
		mode.setCreatedBy(bill.getCreatedBy());
		mode.setCreatedTime(bill.getCreatedTime());
		mode.ins();
	}

	public static void delByBill(IBill bill) {
		RpStimatePay mode = RpStimatePay.chkUniqueForm(true, bill.gtLongPkey());
		if (mode == null)
			throw LOG.err(Msgs.noData);
		if (mode.getAmt().compareTo(mode.getBalance()) != 0)
			throw LOG.err(Msgs.isDoing);
		mode.del();
	}

	/**
	 * 单据记账时，判断待付款是否已经处理完成
	 * @param bill
	 */
	public static void isTally(IBill bill) {
		RpStimatePay mode = RpStimatePay.chkUniqueForm(true, bill.gtLongPkey());
		if (mode == null)
			return;
		if (mode.getBalance().signum() != 0)
			throw LOG.err("notClear", "{0}[{1}]的待付款未完成", ((Bean) bill).gtTB().getName(), bill.getCode());
	}

	/**
	 * 付款处理
	 * rp.pkey 是待处理登记的主键值
	 * @param noteRp
	 */
	public static RpStimatePay doProc(GlNoteViewRp noteRp, RpNotePay rp) {
		if (noteRp.getAmt().signum() <= 0)
			throw LOG.err(Msgs.noAmt);
		RpStimatePay pay = BeanBase.loadAndLock(RpStimatePay.class, noteRp.getPkey());
		pay.setBalance(pay.getBalance().subtract(noteRp.getAmt()));
		if (pay.getBalance().signum() < 0)
			throw LOG.err(Msgs.bigAmt);
		else if (pay.getBalance().signum() == 0)
			pay.setClearTime(Env.INST.getSystemTime());
		pay.upd();
		if (noteRp.gtJournal().getCell().equals(pay.getCell()) == false)
			throw LOG.err(Msgs.errOrg, noteRp.gtJournal().getErrMsg());
		//登记对应的付款凭条，分现金与非现金两种
		GlNote note = GlNoteDAO.insAuto(((BeanBill)pay.gtOrigForm()), noteRp.getAmt(), noteRp.gtJournal().gtJournal(), ODirect.CR,
				noteRp.getType()==0?RpNoteCashPay.TB.getID():RpNotePay.TB.getID(), noteRp.getDocNum(), noteRp.getSummary());
		if (noteRp.getType() == 0) { //现金方式
			RpNoteCashPay ext = new RpNoteCashPay();
			ext.stNote(note);
			ext.setCashier(noteRp.gtJournal().getCashier());
			ext.setTranTime(rp != null ? rp.getPayDate() : null);
			ext.ins();
		} else { //非现金方式
			RpNotePay ext = new RpNotePay();
			ext.stNote(note);
			ext.setCashier(noteRp.gtJournal().getCashier());
			ext.setType(noteRp.getType());
			if (rp != null) {
				ext.setDate(rp.getDate());
				ext.setPayDate(rp.getPayDate());
				ext.setName(rp.getName());
				ext.setAccount(rp.getAccount());
				ext.setBank(rp.getBank());
			}
			ext.ins();
		}
		RpJournalLineDAO.addByBill(note);//在对应的出纳日记账中，添加出纳日记账明细；
		return pay;
	}

	public static RpStimatePay unProc(Long pkey) {
		RpStimatePay pay = BeanBase.load(RpStimatePay.class, pkey);
		RpJournalLineDAO.delByMv(pay.getOrigForm());//根据原单据，删除出纳日记账中相应的出纳日记账明细；
		String ids = RpNoteCashPay.TB.getID() + "," + RpNotePay.TB.getID();
		GlNoteDAO.delByBillAuto(pay.getOrigForm(), ids);
		pay.setBalance(pay.getAmt());
		pay.setClearTime(null);
		pay.upd();
		return pay;
	}

}
