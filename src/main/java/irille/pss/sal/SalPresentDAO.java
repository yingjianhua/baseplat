package irille.pss.sal;

import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.cst.CstOut;
import irille.pss.cst.CstSalInvoice;
import irille.pss.sal.Sal.OInoutStatus;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.gl.Tally;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduTally;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

/**
 * 不产生金额相关单据，出入库审核不用检查财务上的单据审核状态
 * 审核时检查 -- 锁库存
 * 产生出入库 -- 实际数量是否足够
 * @author whx
 * @version 创建时间：2014年12月9日 下午4:05:50
 */
public class SalPresentDAO implements IOut<SalPresent> {
	public static final Log LOG = new Log(SalPresentDAO.class);

	public static class Ins extends IduInsLines<Ins, SalPresent, SalPresentLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			initBill(getB());
			if (getB().getOrg().equals(getB().gtWarehouse().getOrg()) == false)
				throw LOG.err("errWs", "所选仓库与当前机构冲突!");
			getB().stInoutStatus(Sal.OInoutStatus.INIT);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().setAmt(Idu.sumAmt(getLines()));
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, SalPresent.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}

	}

	public static class Upd extends IduUpdLines<Upd, SalPresent, SalPresentLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			SalPresent sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			PropertyUtils.copyPropertiesWithout(sale, getB(), SalPresent.T.PKEY, SalPresent.T.CODE, SalPresent.T.STATUS,
			    SalPresent.T.ORG, SalPresent.T.DEPT, SalPresent.T.CREATED_BY, SalPresent.T.CREATED_TIME,
			    SalPresent.T.AMT_COST, SalPresent.T.INOUT_STATUS, SalPresent.T.TALLY_BY, SalPresent.T.TALLY_TIME,
			    SalPresent.T.CELL, SalPresent.T.SHIPING);
			sale.setAmt(Idu.sumAmt(getLines()));
			setB(sale);
			updLineTid(getB(), getLines(), SalPresentLine.class);
			SysShipingDAO.upd(getB(), _ship, SalPresent.T.SHIPING.getFld(), getB().gtShipingMode());
		}

	}

	public static class Del extends IduDel<Del, SalPresent> {

		@Override
		public void before() {
			super.before();
			assertStatusIsInit(getB());
			delLineTid(getB(), SalPresentLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, SalPresent> {

		@Override
		public void run() {
			super.run();
			SalPresent sale = loadThisBeanAndLock();
			assertStatusIsInit(sale);
			sale.stStatus(STATUS.TALLY_ABLE);
			sale.setApprBy(getUser().getPkey());
			sale.setApprTime(Env.INST.getWorkDate());
			sale.upd();
			setB(sale);
			GsPub
			    .insertStimate(sale, sale.gtWarehouse(), Idu.getLinesTid(sale, SalPresentLine.class), OEnrouteType.DFH, null);
			//待处理
			//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), CstOut.TB, GlDaybook.TB);
			//记账NOTE TODO
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, SalPresent> {

		@Override
		public void run() {
			super.run();
			SalPresent sale = loadThisBeanAndLock();
			assertStatusIsTally(sale);
			//取消出库单
			if (sale.gtInoutStatus() != OInoutStatus.INIT)
				GsPub.deleteOut(sale);
			//取消预出入库登记
			GsPub.deleteStimate(sale, sale.gtWarehouse(), Idu.getLinesTid(sale, SalPresentLine.class), OEnrouteType.DFH);
			//取消NOTE
			GlNoteDAO.delByBill(sale.gtLongPkey());
			sale.stStatus(STATUS_INIT);
			sale.stInoutStatus(OInoutStatus.INIT);
			sale.setApprBy(null);
			sale.setApprTime(null);
			sale.upd();
			setB(sale);
			FrmPendingDAO.del(getB(), CstOut.TB, GlDaybook.TB);
		}
	}
	
	public static class CrtGs extends IduOther<CrtGs, SalPresent> {
		public static Cn CN = new Cn("crtGs", "生产出库单");

		@Override
		public void run() {
			super.run();
			SalPresent sale = loadThisBeanAndLock();
			assertStatus(sale, STATUS.TALLY_ABLE, STATUS.DONE);
			if (sale.gtInoutStatus() != OInoutStatus.INIT)
				throw LOG.err("inout", "赠送单[{0}]已产生出库单!", sale.getCode());
			sale.stInoutStatus(OInoutStatus.CRT);
			sale.upd();
			setB(sale);
			List lines = Idu.getLinesTid(sale, SalPresentLine.class);
			GsPub.checkQtyFact(sale.gtWarehouse(), lines);
			GsPub.insertOut(sale, sale.gtWarehouse(), sale.getCustName(), sale.getShipingMode(), sale.getShiping());
		}
	}

	@Override
	public void outOk(GsOut out, SalPresent model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.CRT)
			throw LOG.err("outOkErr", "赠送单[{0}]的出库状态错误!", model.getCode());
		model.stInoutStatus(OInoutStatus.DONE);
		model.setShiping(out.getShiping());
		model.setShipingMode(out.getShipingMode());
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalPresentLine.class), OEnrouteType.DFH);
	}

	@Override
	public void outCancel(GsOut out, SalPresent model) {
		Idu.assertStatusOther(model, OBillStatus.TALLY_ABLE, OBillStatus.DONE);
		if (model.gtInoutStatus() != OInoutStatus.DONE)
			throw LOG.err("outOkErr", "赠送单[{0}]的出库状态错误!", model.getCode());
		model.stInoutStatus(OInoutStatus.CRT);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalPresentLine.class), OEnrouteType.DFH, null);
	}
	
	@Override
	public List<IGoods> getOutLines(SalPresent model, int idx, int count) {
		return Idu.getLinesTid(model, SalPresentLine.class, idx, count);
	}

	@Override
	public int getOutLinesCount(SalPresent model) {
		return Idu.getLinesTidCount(model, SalPresentLine.class);
	}

}
