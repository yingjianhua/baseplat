package irille.pss.cst;

import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.pub.Log;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.inf.ICstInout;
import irille.pub.svr.Env;

import java.math.BigDecimal;

public class CstOutDAO {
	public static final Log LOG = new Log(CstOutDAO.class);

	public static class Ins extends IduInsLines<Ins, CstOut, CstOutLine> {
		public String _formIds;

		@Override
		public void before() {
			super.before();
			initBill(getB());
			getB().setAmtCost(BigDecimal.ZERO);//TODO 预留字段 ，暂时保留
			setLines(CstPub.doInsert(getB(), CstOutLine.class, _formIds, ICstInout.TYPE_OUT));

			/*
			 * String[] aryIds = _formIds.split("\\,");
			 * Map<Integer,String> aryIdsn = new HashMap<Integer,String>();
			 * for (String lineId : aryIds) {
			 * int ids = (int)(Long.parseLong(lineId) % SysTable.NUM_BASE);
			 * if(aryIdsn.containsKey(ids)) {
			 * aryIdsn.put(ids, aryIdsn.get(ids)+","+lineId);
			 * } else {
			 * aryIdsn.put(ids, lineId);
			 * }
			 * }
			 * for (int ids : aryIdsn.keySet()) { //遍历所有待处理表单
			 * initBill(getB());
			 * getB().setAmtCost(BigDecimal.ZERO);//TODO 预留字段 ，暂时保留
			 * getB().setTableId(ids);
			 * setLines(CstPub.doInsert(getB(), CstOutLine.class, aryIdsn.get(ids),
			 * ICstInout.TYPE_OUT));
			 * insLineTid(getB(), getLines());
			 * }
			 */
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

	public static class Del extends IduDel<Del, CstOut> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			CstPub.delInvoice(getB());
			delLineTid(getB(), CstOutLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, CstOut> {

		@Override
		public void run() {
			super.run();
			CstOut sale = loadThisBeanAndLock();
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

	public static class Unapprove extends IduUnapprove<Unapprove, CstOut> {

		@Override
		public void run() {
			super.run();
			CstOut sale = loadThisBeanAndLock();
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
