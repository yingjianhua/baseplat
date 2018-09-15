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

public class PyaNoteAccountPayableLineDAO {
	public static final Log LOG = new Log(PyaNoteAccountPayableLineDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noNote("没有找到制定的记账便签!"),
		writeOff("核销金额超过应付账款!"),
		noAmt("核销金额不能为零");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void insByBill(PyaPayWriteoffBill mode) {
		int symbol = mode.getAmt().signum();
		if(symbol==0)  throw LOG.err(Msgs.noAmt);
		String where = Idu.sqlString("{0}=? and {1}!={2}", PyaNoteAccountPayable.T.OBJ, 
				PyaNoteAccountPayable.T.WRITEOFF_STATE, Gl.OWriteoffState.CLEAR);
		where = where+" and balance"+(symbol>0?">":"<")+"0";//如果余额为负，则只核销红字的账款凭条；
		List<PyaNoteAccountPayable> list = BeanBase.list(PyaNoteAccountPayable.class, where, false, mode.gtJournal().getObjPkey());
		BigDecimal amt = mode.getAmt();
		for(PyaNoteAccountPayable pay:list) {
			if(pay.gtNote().gtStatus()!=OBillStatus.DONE) continue;//只对状态为完成的单据进行核销
			GlNote note = GlNoteDAO.insAuto(mode, amt.compareTo(pay.getBalance())!=symbol?amt:pay.getBalance(), mode.gtJournal(),
					mode.gtJournal().gtDirect() == Gl.ODirect.DR ? Gl.ODirect.CR : Gl.ODirect.DR, PyaNoteAccountPayableLine.TB.getID(), mode.getDocNum(), mode.getSummary());
			PyaNoteAccountPayableLine ext = new PyaNoteAccountPayableLine();
			ext.stNote(note);
			ext.stMainNote(pay);
			ext.ins();
			amt = amt.subtract(pay.getBalance());
			if(amt.signum()!=symbol) {
				break;
			}
		}
		if (amt.signum() == symbol) {
			throw LOG.err("writeoff","核销金额[{0}]超过应付账款金额[{1}]",mode.getAmt(),mode.getAmt().subtract(amt));
		}
	}
	public static void insByNote(GlNote note, int pkey) {
		note.ins();
		PyaNoteAccountPayable pay = BeanBase.load(PyaNoteAccountPayable.class, pkey);
		BigDecimal amt = note.getAmt();
		if(amt.compareTo(pay.getBalance())==1) {
			throw LOG.err(Msgs.writeOff);
		}
		PyaNoteAccountPayableLine ext = new PyaNoteAccountPayableLine();
		ext.stNote(note);
		ext.stMainNote(pay);
		ext.ins();
	}

}
