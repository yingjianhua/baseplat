package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.gl.pya.PyaNoteAccountPayable;
import irille.gl.pya.Pya.OPaType;
import irille.gl.rp.RpStimateRecDAO;
import irille.pss.cst.CstInRed;
import irille.pss.cst.CstPurInvoiceRed;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class PurRtnDAO implements IOut<PurRtn> {
	public static final Log LOG = new Log(PurRtnDAO.class);

	public static class Ins extends IduInsLines<Ins, PurRtn, PurRtnLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			getB().setAmt(BigDecimal.ZERO);
			getB().setAmtCost(BigDecimal.ZERO);
			BigDecimal amt = BigDecimal.ZERO;
			for (PurRtnLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			if (getB().getAmtXf().add(getB().getAmtGz()).compareTo(amt) != 0)
				throw LOG.err("sumErr", "[现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
		}

		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}
	}

	public static class Upd extends IduUpdLines<Upd, PurRtn, PurRtnLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			PurRtn rtn = loadThisBeanAndLock();
			PropertyUtils
			    .copyProperties(rtn, getB(), PurRtn.T.SUPPLIER, PurRtn.T.WAREHOUSE, PurRtn.T.BILL_FLAG, PurRtn.T.REM ,PurRtn.T.AMT_XF,PurRtn.T.AMT_GZ);
			setB(rtn);
			BigDecimal amt = BigDecimal.ZERO;
			for (PurRtnLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				amt = amt.add(line.getAmt());
				line.setCostPur(BigDecimal.ZERO);
			}
			getB().setAmt(amt);
			if (getB().getAmtXf().add(getB().getAmtGz()).compareTo(amt) != 0)
				throw LOG.err("sumErr", "[现付金额 + 挂账金额] 不等于 [总金额 : {0}]", getB().getAmt());
			updLineTid(getB(), getLines(), PurRtnLine.class);
		}
	}

	public static class Del extends IduDel<Del, PurRtn> {

		@Override
		public void valid() {
			super.valid();
			PurRtn order = loadThisBeanAndLock();
			assertStatusIsInit(order);
		}

		@Override
		public void before() {
			super.before();
			delLineTid(getB(), PurRtnLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, PurRtn> {

		@Override
		public void run() {
			super.run();
			PurRtn rtn = loadThisBeanAndLock();
			assertStatusIsInit(rtn);
			rtn.stStatus(Sys.OBillStatus.TALLY_ABLE);
			rtn.setApprBy(getUser().getPkey());
			rtn.setApprTime(Env.getTranBeginTime());
			rtn.upd();
			setB(rtn);
			GsPub.insertStimate(rtn, rtn.gtWarehouse(), Idu.getLinesTid(rtn, PurRtnLine.class), OEnrouteType.DFH, null);
			GsPub.insertOut(rtn, rtn.gtWarehouse(), rtn.getSupname(), Sys.OShipingMode.NO.getLine().getKey(), null);
			// 待处理
			FrmPendingDAO.ins(getB(), CstPurInvoiceRed.TB, CstInRed.TB, GlDaybook.TB);
			if (getB().getAmtXf().signum() != 0)
				RpStimateRecDAO.insByBill(getB(), getB().gtSupplier(), getB().getAmtXf());
			//记账NOTE
			//借 – 采购待处理账户（负数）-- BILL、代收款处理登记-现金(正数)
			//贷 – 供应商应付账款(负数)、
			if (rtn.getAmtGz().signum() != 0) {
				GlJournal jnl = GlJournalDAO.getAutoCreate(rtn.gtCell(), Pur.SubjectAlias.PUR_INCOME,
				    new AccObjs().setSupplier(rtn.gtSupplier()));
				
				GlNote note = GlNoteDAO.insAuto(rtn, rtn.getAmtGz().negate(), jnl, ODirect.CR, PyaNoteAccountPayable.TB.getID());
				PyaNoteAccountPayable notew = new PyaNoteAccountPayable().init();
				notew.stNote(note);
				notew.stType(OPaType.SUPPLIER);
				notew.setObj(rtn.gtSupplier().gtLongPkey());
				notew.setBalance(note.getAmt());
				notew.setDateStart(rtn.getApprTime());
				notew.ins();
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurRtn> {
		@Override
		public void run() {
			super.run();
			PurRtn rtn = loadThisBeanAndLock();
			assertStatusIsTally(rtn);
			rtn.stStatus(Sys.OBillStatus.INIT);
			rtn.setApprBy(null);
			rtn.setApprTime(null);
			rtn.upd();
			setB(rtn);
			GsPub.deleteStimate(rtn, rtn.gtWarehouse(), Idu.getLinesTid(rtn, PurRtnLine.class), OEnrouteType.DFH);
			GsPub.deleteOut(rtn);
			// 取消待处理
			FrmPendingDAO.del(getB(), CstPurInvoiceRed.TB, CstInRed.TB, GlDaybook.TB);
			//取消NOTE
			GlNoteDAO.delByBill(rtn.gtLongPkey());
			if (getB().getAmtXf().signum() != 0) //取消待付款
				RpStimateRecDAO.delByBill(getB());
		}
	}

	@Override
	public void outOk(GsOut out, PurRtn model) {
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurRtnLine.class), OEnrouteType.DFH);
	}

	@Override
	public void outCancel(GsOut out, PurRtn model) {
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurRtnLine.class), OEnrouteType.DFH, null);
	}

	@Override
	public List<IGoods> getOutLines(PurRtn model, int idx, int count) {
		return Idu.getLinesTid(model, PurRtnLine.class, idx, count);
	}

	@Override
	public int getOutLinesCount(PurRtn model) {
		return Idu.getLinesTidCount(model, PurRtnLine.class);
	}
}
