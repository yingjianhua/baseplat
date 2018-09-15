package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysTable;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IForm;
import irille.pub.idu.Idu;
import irille.pub.idu.IduInsLines;

import java.util.List;

public class GlNoteViewDAO {
	public static final Log LOG = new Log(GlNoteViewDAO.class);

	/**
	 * 注意此方法用于内转单及各BILL单据的便签来调用
	 * 自动产生的NOTE不在此处理
	 * @param form
	 * @param list
	 */
	public static void iuByView(IForm form, List<GlNoteView> list) {
		String wheresql = Idu.sqlString("{0}=? and {1}="+Sys.OYn.NO.getLine().getKey(), GlNote.T.BILL, GlNote.T.IS_AUTO);
		Long lpkey = ((Bean) form).gtLongPkey();
		List<GlNote> listOld = BeanBase.list(GlNote.class, wheresql, true, lpkey); // 数据库旧数据
		for (GlNote line : listOld)
			del(line);
		for (GlNoteView viewline : list) {
			viewline.setPkey(null); //全部作新增处理
			viewline.setBill(lpkey);
			ins(viewline);
		}
	}

	public static void delByView(IForm form) {
		String wheresql = GlNote.T.BILL.getFld().getCodeSqlField() + "=?";
		List<GlNote> listOld = BeanBase.list(GlNote.class, wheresql, true, form.gtLongPkey()); // 数据库旧数据
		for (GlNote line : listOld)
			del(line);
	}

	private static void ins(GlNoteView view) {
		checkWriteoffFlag(view);
		GlNote note = new GlNote().init();
		note.setBill(view.getBill());
		note.setJournal(view.getJournal());
		note.setDirect(view.getDirect());
		note.setAmt(view.getAmt());
		note.setDocNum(view.getDocNum());
		note.setSummary(view.getSummary());
		note.setRem(view.getRem());
		note.stIsAuto(false);
		note.ins();
		if (view.gtWriteoffFlag() == Gl.OWriteoffFlag.WRITEOFF_NO)
			return;
		if (view.gtWriteoffFlag() == Gl.OWriteoffFlag.WRITEOFF_NEW) {
			GlNoteWriteoff nw = new GlNoteWriteoff().init();
			nw.stNote(note);
			nw.setBalance(view.getAmt());
			nw.setDateStart(view.getDateStart());
			nw.setDateDue(view.getDateDue());
			nw.ins();
			note.setExtTable(nw.gtTbId());
		} else {
			GlNoteWriteoffLine nl = new GlNoteWriteoffLine().init();
			nl.stNote(note);
			nl.setMainNote(view.getWriteoff());
			nl.ins();
			note.setExtTable(nl.gtTbId());
		}
		note.upd();
	}

	private static void del(GlNote note) {
		if (note.gtStatus() != Sys.OBillStatus.INIT)
			throw LOG.err("assertStatus", "记账条[{0}-{1}]的状态为[" + note.gtStatus().getLine().getName() + "]，不可操作！", note
			    .gtJournal().getCode(), note.gtJournal().getName());
		if (note.getExtTable() != null)
			BeanBase.load(SysTable.gtTable(note.getExtTable()).gtClazz(), note.getPkey()).del();
		note.del();
	}

	public static boolean checkWriteoffFlag(GlNoteView line) {
		if (line.getJournal() == null)
			throw LOG.err("noData", "请选择分户账!");
		GlJournal jnl = line.gtJournal();
		if (line.gtWriteoffFlag() == Gl.OWriteoffFlag.WRITEOFF_NO) {
			if (line.gtJournal().gtSubject().gtWriteoffFlag())
				throw LOG.err("notSet", "[{0}-{1}]对应的账户设销账，请确认[核销标志与核销计划]是否正确! ", jnl.getCode(), jnl.getName());
			return false;
		}
		if (line.gtWriteoffFlag() == Gl.OWriteoffFlag.WRITEOFF_NEW) {
			if (line.gtJournal().gtSubject().gtWriteoffFlag() == false)
				throw LOG.err("notSet", "[{0}-{1}]对应的账户不设销账! ", jnl.getCode(), jnl.getName());
			return true;
		}
		if (line.gtWriteoffFlag() == Gl.OWriteoffFlag.WRITEOFF_WRITE) { // 指定进行核销
			if (line.gtJournal().gtSubject().gtWriteoffFlag() == false)
				throw LOG.err("notSet", "[{0}-{1}]对应的账户不设销账! ", jnl.getCode(), jnl.getName());
			if (line.getWriteoff() == null) // 必须指定writeoffKey
				throw LOG.err("keyNotSet", "核销标志为[核销]，必须指定销账计划");
			return true;
		}
		throw LOG.err("errWriteoffType", "销账类型错误");
	}

	public static class IuByOther extends IduInsLines<IuByOther, GlNoteView, GlNoteView> {
		private String _classCode;
		private Long _billPkey;

		public String getClassCode() {
			return _classCode;
		}

		public void setClassCode(String classCode) {
			_classCode = classCode;
		}

		public Long getBillPkey() {
			return _billPkey;
		}

		public void setBillPkey(Long billPkey) {
			_billPkey = billPkey;
		}

		@Override
		public void run() {
			Bean bean = BeanBase.load(ClassTools.getClass(getClassCode()), getBillPkey());
			GlNoteViewDAO.iuByView((IForm) bean, getLines());
		}

		public void valid() {}

		public void log() {}

	}
}
