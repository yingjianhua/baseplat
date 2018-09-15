package irille.gl.rva;

import irille.core.sys.SysCell;
import irille.core.sys.SysSupplier;
import irille.gl.gl.Gl;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;

import java.util.Date;

public class RvaNoteAccountDAO {
	public static final Log LOG = new Log(RvaNoteAccountDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noNote("没有找到制定的记账便签!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void insByBill(RvaRecBill mode) {
		GlNote note = GlNoteDAO.insAuto(mode, mode.getAmt(), mode.gtJournal(), mode.gtJournal().gtDirect(), 
				RvaNoteAccount.TB.getID(), mode.getDocNum(), mode.getSummary());
		RvaNoteAccount ext = new RvaNoteAccount();
		ext.stNote(note);
		ext.stWriteoffState(Gl.OWriteoffState.NO);
		ext.setBalance(mode.getAmt());
		ext.setDateStart(mode.getDateStart());
		ext.setDateDue(mode.getDateDue());
		ext.setObj((long) mode.gtJournal().getObjPkey());
		ext.setType(getTypeByObj(mode.gtJournal()));
		ext.setUpdatedDate(Env.getWorkDate());
		ext.ins();
	}
	public static void insByNote(GlNote note, Date dateStart, Date dateDue) {
		note.ins();
		RvaNoteAccount ext = new RvaNoteAccount();
		ext.stNote(note);
		ext.stWriteoffState(Gl.OWriteoffState.NO);
		ext.setBalance(note.getAmt());
		ext.setDateStart(dateStart);
		ext.setDateDue(dateDue);
		ext.setObj((long) note.gtJournal().getObjPkey());
		ext.setType(getTypeByObj(note.gtJournal()));
		ext.setUpdatedDate(Env.getWorkDate());
		ext.ins();
	}
	//根据OBJ取账户类型
	private static byte getTypeByObj(GlJournal journal) {
		if(journal.gtAccType()==Gl.OAccType.MUCH) {
			return Rva.ORoType.OTHER.getLine().getKey();
		}
		long longpkey = journal.getObjPkey();
		Class clazz = Bean.gtLongClass(longpkey);
		if (clazz.equals(SysSupplier.class))
			return Rva.ORaType.CUST.getLine().getKey();
		if (clazz.equals(SysCell.class)) {
			SysCell cell = (SysCell) Bean.gtLongTbObj(longpkey);
			if (cell.getOrg().equals(Idu.getOrg().getPkey()))
				return Rva.ORaType.INNER_CELL.getLine().getKey();
			return Rva.ORaType.OUTER_CELL.getLine().getKey();
		}
		return Rva.ORaType.OTHER.getLine().getKey();
	}

}
