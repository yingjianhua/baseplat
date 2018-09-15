package irille.pss.cst;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.pss.pur.PurMvIn;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.svr.Env;

public class CstMvInvoiceInDAO {
	public static final Log LOG = new Log(CstMvInvoiceInDAO.class);

	private static PurMvIn.OMvType checkMvType(String formIds) {
		String[] aryIds = formIds.split("\\,");
		PurMvIn.OMvType vtype = null;
		for (String lineId : aryIds) {
			Bean bean = Bean.gtLongTbObj(Long.parseLong(lineId));
			if (bean instanceof PurMvIn == false)
				throw LOG.err("notMvIn", "调入发票需由调入单产生");
			PurMvIn in = (PurMvIn) bean;
			if (vtype == null) {
				vtype = in.gtMvType();
				continue;
			}
			if (vtype != in.gtMvType())
				throw LOG.err("notSame", "调入发票必须由相同'调入类型'的调入单汇总产生!");
		}
		return vtype;
	}

	public static class Ins extends IduInsLines<Ins, CstMvInvoiceIn, CstMvInvoiceInLine> {
		public String _formIds;

		@Override
		public void before() {
			super.before();
			initBill(getB());
			getB().stMvType(checkMvType(_formIds));
			setLines(CstPub.insInvoice(getB(), CstMvInvoiceInLine.class, _formIds));
		}

		//取消默认的新增处理
		public void run() {
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Del extends IduDel<Del, CstMvInvoiceIn> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			CstPub.delInvoice(getB());
			delLineTid(getB(), CstMvInvoiceInLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, CstMvInvoiceIn> {

		@Override
		public void run() {
			super.run();
			CstMvInvoiceIn sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, CstMvInvoiceIn> {

		@Override
		public void run() {
			super.run();
			CstMvInvoiceIn sale = loadThisBeanAndLock();
			assertStatusIsTally(sale);
			//取消NOTE
			GlNoteDAO.delByBill(sale.gtLongPkey());
			sale.stStatus(STATUS_INIT);
			sale.setApprBy(null);
			sale.setApprTime(null);
			sale.upd();
			setB(sale);
			// 待处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}

}
