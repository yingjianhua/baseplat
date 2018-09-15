package irille.gl.pya;

import irille.core.sys.Sys.OBillStatus;
import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.math.BigDecimal;
import java.util.List;

public class PyaNoteDepositPayableLineDAO {
	public static final Log LOG = new Log(PyaNoteDepositPayableLineDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noNote("没有找到制定的记账便签!"),
		writeOff("核销金额超过预付账款!"),
		noAmt("核销金额不能为零");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void insByBill(PyaPayDepWriteoffBill mode) {
		int symbol = mode.getAmt().signum();
		if(symbol==0) throw LOG.err(Msgs.noAmt);
		String where = Idu.sqlString("{0}=? and {1}!={2}", PyaNoteDepositPayable.T.OBJ,
		    PyaNoteDepositPayable.T.WRITEOFF_STATE, Gl.OWriteoffState.CLEAR);
		where = where+" and balance"+(symbol>0?">":"<")+"0";//如果余额为负，则只核销红字的账款凭条；
		List<PyaNoteDepositPayable> list = BeanBase.list(PyaNoteDepositPayable.class, where, false, mode.gtJournal().getObjPkey());
		BigDecimal amt = mode.getAmt();
		for (PyaNoteDepositPayable rec : list) {
			if(rec.gtNote().gtStatus()!=OBillStatus.DONE) continue;//只对状态为完成的单据进行核销
			GlNote note = GlNoteDAO.insAuto(mode, amt.compareTo(rec.getBalance())!=symbol?amt:rec.getBalance(), mode.gtJournal(),
					mode.gtJournal().gtDirect() == Gl.ODirect.DR ? Gl.ODirect.CR : Gl.ODirect.DR, PyaNoteDepositPayableLine.TB.getID(), mode.getDocNum(), mode.getSummary());
			PyaNoteDepositPayableLine ext = new PyaNoteDepositPayableLine();
			ext.stNote(note);
			ext.stMainNote(rec);
			ext.ins();
			amt = amt.subtract(rec.getBalance());
			if (amt.signum()!=symbol)
				break;
		}
		if (amt.signum() == symbol) {
			throw LOG.err("writeoff","核销金额[{0}]超过预付账款金额[{1}]",mode.getAmt(),mode.getAmt().subtract(amt));
		}
	}
	public static void insByNote(GlNote note, int pkey) {
		note.ins();
		PyaNoteDepositPayable pay = BeanBase.load(PyaNoteDepositPayable.class, pkey);
		BigDecimal amt = note.getAmt();
		if(amt.compareTo(pay.getBalance())==1) {
			throw LOG.err(Msgs.writeOff);
		}
		PyaNoteDepositPayableLine ext = new PyaNoteDepositPayableLine();
		ext.stNote(note);
		ext.stMainNote(pay);
		ext.ins();
	}
}
