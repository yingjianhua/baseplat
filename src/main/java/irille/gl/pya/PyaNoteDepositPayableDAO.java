package irille.gl.pya;

import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
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

public class PyaNoteDepositPayableDAO {
	public static final Log LOG = new Log(PyaNoteDepositPayableDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noNote("没有找到制定的记账便签!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static void insByBill(PyaPayDepBill mode) {
		GlNote note = GlNoteDAO.insAuto(mode, mode.getAmt(), mode.gtJournal(), mode.gtJournal().gtDirect(), 
				PyaNoteDepositPayable.TB.getID(), mode.getDocNum(), mode.getSummary());
		PyaNoteDepositPayable ext = new PyaNoteDepositPayable();
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
		PyaNoteDepositPayable ext = new PyaNoteDepositPayable();
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
			return Pya.OPaType.OTHER.getLine().getKey();
		}
		long longpkey = journal.getObjPkey();
		Class clazz = Bean.gtLongClass(longpkey);
		if (clazz.equals(SysCustom.class))
			return Pya.OPdType.SUPPLIER.getLine().getKey();
		if (clazz.equals(SysCell.class)) {
			SysCell cell = (SysCell) Bean.gtLongTbObj(longpkey);
			if (cell.getOrg().equals(Idu.getOrg().getPkey()))
				return Pya.OPdType.INNER_CELL.getLine().getKey();
			return Pya.OPdType.OUTER_CELL.getLine().getKey();
		}
		
		//TODO 
		return Pya.OPdType.OTHER.getLine().getKey();
	}

}
