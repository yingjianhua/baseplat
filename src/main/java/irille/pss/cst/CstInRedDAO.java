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

public class CstInRedDAO {
	public static final Log LOG = new Log(CstInRedDAO.class);

	public static class Ins extends IduInsLines<Ins, CstInRed, CstInRedLine> {
		public String _formIds;

		@Override
		public void before() {
			super.before();
			initBill(getB());
			getB().setAmtCost(BigDecimal.ZERO);//TODO 预留字段 ，暂时保留
			setLines(CstPub.doInsert(getB(), CstInRedLine.class, _formIds, ICstInout.TYPE_IN_RED));

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
			 * setLines(CstPub.doInsert(getB(), CstInRedLine.class, aryIdsn.get(ids),
			 * ICstInout.TYPE_IN_RED));
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

	public static class Del extends IduDel<Del, CstInRed> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			CstPub.delInvoice(getB());
			delLineTid(getB(), CstInRedLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, CstInRed> {

		@Override
		public void run() {
			super.run();
			CstInRed sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			//记账NOTE TODO
			// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, CstInRed> {

		@Override
		public void run() {
			super.run();
			CstInRed sale = loadThisBeanAndLock();
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
