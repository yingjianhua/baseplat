package irille.gl.rp;

import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;

public class RpNoteCashPayDAO {
	public static final Log LOG = new Log(RpNoteCashPayDAO.class);
	
	public static void insByBill(RpNotePayBill mode) {
		GlNote note = GlNoteDAO.insAuto(mode, mode.getAmt(), mode.gtJournal().gtJournal(), ODirect.CR, RpNoteCashPay.TB.getID(), mode.getDocNum(), mode.getSummary());
		RpNoteCashPay ext = new RpNoteCashPay();
		ext.stNote(note);
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.setTranTime(mode.getTranTime());
		ext.ins();
		RpJournalLineDAO.addByBill(note);
	}
	
	//由便签新建时关联调用，出纳账流水由便签方法里同意新增
	public static void insByNote(GlNote note,RpNotePayBill mode) {
		note.stJournal(mode.gtJournal().gtJournal());
		note.stDirect(Gl.ODirect.CR); //付款凭条为贷方
		note.ins();
		RpNoteCashPay ext = new RpNoteCashPay();
		ext.stNote(note);
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.setTranTime(mode.getTranTime());
		ext.ins();
	}
}
