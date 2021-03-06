package irille.gl.rp;

import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;

public class RpNoteCashRptDAO {
	public static final Log LOG = new Log(RpNoteCashRptDAO.class);
	
	public static void insByBill(RpNoteRptBill mode) {
		GlNote note = GlNoteDAO.insAuto(mode, mode.getAmt(), mode.gtJournal().gtJournal(), ODirect.DR, RpNoteCashRpt.TB.getID(), mode.getDocNum(), mode.getSummary());
		RpNoteCashRpt ext = new RpNoteCashRpt();
		ext.stNote(note);
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.setTranTime(mode.getTranTime());
		ext.ins();
		RpJournalLineDAO.addByBill(note);//在对应的出纳日记账中，添加出纳日记账明细；
	}
	public static void insByNote(GlNote note,RpNoteRptBill mode) {
		note.stJournal(mode.gtJournal().gtJournal());
		note.stDirect(Gl.ODirect.DR);
		note.ins();
		RpNoteCashRpt ext = new RpNoteCashRpt();
		ext.stNote(note);
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.setTranTime(mode.getTranTime());
		ext.ins();
	}
}
