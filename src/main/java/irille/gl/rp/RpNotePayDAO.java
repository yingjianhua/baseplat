package irille.gl.rp;

import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;

public class RpNotePayDAO {
	public static final Log LOG = new Log(RpNotePayDAO.class);
	
	public static void insByBill(RpNotePayBill mode) {
		GlNote note = GlNoteDAO.insAuto(mode, mode.getAmt(), mode.gtJournal().gtJournal(), ODirect.CR, RpNotePay.TB.getID(), mode.getDocNum(), mode.getSummary());
		RpNotePay ext = new RpNotePay();
		ext.stNote(note);
		ext.stType(mode.gtType());
		ext.setDate(mode.getDate());
		ext.setPayDate(mode.getPayDate());
		ext.setName(mode.getName());
		ext.setAccount(mode.getAccount());
		ext.setBank(mode.getBank());
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.ins();
		RpJournalLineDAO.addByBill(note);
	}
	public static void insByNote(GlNote note,RpNotePayBill mode) {
		note.stJournal(mode.gtJournal().gtJournal());
		note.stDirect(Gl.ODirect.CR);
		note.ins();
		RpNotePay ext = new RpNotePay();
		ext.stNote(note);
		ext.stType(mode.gtType());
		ext.setDate(mode.getDate());
		ext.setPayDate(mode.getPayDate());
		ext.setName(mode.getName());
		ext.setAccount(mode.getAccount());
		ext.setBank(mode.getBank());
		ext.stCashier(mode.gtJournal().gtCashier());
		ext.ins();
	}
}
