package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysSeqDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gs.Gs;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsDemandDAO;
import irille.gl.gs.GsDemandDirectDAO;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsPub;
import irille.gl.gs.GsRequest;
import irille.gl.gs.GsRequestLine;
import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.cst.CstIn;
import irille.pss.cst.CstMvInvoiceIn;
import irille.pss.pur.PurMvIn.OFromType;
import irille.pss.pur.PurMvIn.OGoodsTo;
import irille.pss.pur.PurMvIn.OMvType;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalMvOutDAO;
import irille.pss.sal.SalMvOutLine;
import irille.pss.sal.SalMvOut.OGoodsFrom;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.bean.BeanForm;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

public class PurMvInDAO implements IIn<PurMvIn> {
	public static final Log LOG = new Log(PurMvInDAO.class);

	public static class Ins extends IduInsLines<Ins, PurMvIn, PurMvInLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			if (getB().getWarehouse() == null && getB().getOrigForm() == null)
				throw LOG.err("upd", "普通调入单仓库不可为空！");
			getB().stStatus(Sys.OBillStatus.VERIFIED);
			if (getB().getWarehouse() != null) {
				getB().stCell(SysCellDAO.getCellByDept(getB().getWarehouse()));
				getB().setDept(getB().getWarehouse());
				getB().setOrg(getB().gtDept().getOrg());
			} else { //直销的情况
				Bean orig = getB().gtOrigForm();
				if (BeanBill.class.isAssignableFrom(orig.clazz())) {
					getB().setOrg(((BeanBill) orig).getOrg());
					getB().setDept(((BeanBill) orig).getDept());
					getB().setCell(((BeanBill) orig).getCell());
				} else
					throw LOG.err("err", "新建调入单错误");
				//				getB().setOrg(getUser().getOrg());
				//				getB().setDept(getUser().getDept());
				//				getB().stCell(SysCellDAO.getCellByOrg(getB().getOrg()));
			}
			getB().setCode(SysSeqDAO.getSeqnum(PurMvIn.TB, getB().gtOrg(), true));
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
			getB().setOrgOther(getB().gtCellOther().getOrg());
			if (getB().getCellOther().equals(getB().getCell()))
				throw LOG.err("sameCell", "相同核算单元之间的调拨，请使用[库存管理-内部调拨单]来处理!");
			if (getB().getOrg() == getB().getOrgOther())
				getB().stMvType(OMvType.CELL);
			else
				getB().stMvType(OMvType.ORG);

			if (getB().getFromType() == null)
				getB().stFromType(OFromType.DEFAULT);
			if (getB().getWarehouse() == null)
				getB().stGoodsTo(PurMvIn.OGoodsTo.DIRECT_DEMAND);
			else
				getB().stGoodsTo(PurMvIn.OGoodsTo.WAREHOURSE);

			BigDecimal amt = BigDecimal.ZERO;
			for (PurMvInLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty())
				    .setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
		}

		@Override
		public void after() {
			super.after();
			if (getB().getFromType() == OFromType.DEMAND.getLine().getKey())
				for (PurMvInLine line : getLines()) {
					GsDemandDAO.createdByPo(line.getPkey(), getB(), getB().getCode());
				}
			if (getB().getFromType() == OFromType.DIRECT.getLine().getKey()) {
				GsDemandDirectDAO.doByForm((BeanForm) getB().gtOrigForm(), getB());
			}
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, PurMvIn.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}
	}

	public static class Upd extends IduUpdLines<Upd, PurMvIn, PurMvInLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			PurMvIn order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			PropertyUtils.copyProperties(order, getB(), PurMvIn.T.WAREHOUSE, PurMvIn.T.CELL_OTHER, PurMvIn.T.BILL_FLAG,
			    PurMvIn.T.REM, PurMvIn.T.SHIPING_MODE);
			setB(order);
			if (getB().getWarehouse() == null && getB().getOrigForm() == null)
				throw LOG.err("upd", "普通调入单仓库不可为空！");
			if (getB().getWarehouse() != null) {
				SysCell updCell = SysCellDAO.getCellByDept(getB().getWarehouse());
				if (updCell.equals(getB().gtCell()) == false)
					throw LOG.err("updCell", "调出单[{0}]的机构为[{1}]，不可选择仓库[{2}]", getB().getCode(), getB().gtOrg().getExtName(),
					    getB().gtWarehouse().getExtName());
				getB().setDept(getB().getWarehouse());
				getB().setOrg(getB().gtDept().getOrg());
			}
			getB().setOrgOther(getB().gtCellOther().getOrg());
			if (getB().getCellOther().equals(getB().getCell()))
				throw LOG.err("sameCell", "相同核算单元之间的调拨，请使用[库存管理-内部调拨单]来处理!");
			if (getB().getOrg() == getB().getOrgOther())
				getB().stMvType(OMvType.CELL);
			else
				getB().stMvType(OMvType.ORG);
			BigDecimal amt = BigDecimal.ZERO;
			for (PurMvInLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty())
				    .setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			updLineTid(getB(), getLines(), PurMvInLine.class);
			SysShipingDAO.upd(getB(), _ship, PurMvIn.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class UpdByOut extends IduUpdLines<Upd, PurMvIn, PurMvInLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			PurMvIn order = loadThisBeanAndLock();
			PropertyUtils.copyProperties(order, getB(), PurMvIn.T.BILL_FLAG, PurMvIn.T.WAREHOUSE_OTHER, PurMvIn.T.AMT,
			    PurMvIn.T.AMT_COST, PurMvIn.T.SHIPING_MODE);
			setB(order);
			updLineTid(getB(), getLines(), PurMvInLine.class);
			SysShipingDAO.upd(getB(), _ship, PurMvIn.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Chk extends IduUpdLines<Upd, PurMvIn, PurMvInLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			PurMvIn order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			PropertyUtils.copyProperties(order, getB(), PurMvIn.T.WAREHOUSE, PurMvIn.T.CELL_OTHER, PurMvIn.T.BILL_FLAG,
			    PurMvIn.T.REM, PurMvIn.T.REM, PurMvIn.T.SHIPING_MODE);
			setB(order);
			SalMvOut out = (SalMvOut) getB().gtOutForm();
			if (getB().getWarehouse() == null)
				throw LOG.err("chk", "调入仓库不可为空！");
			if (out.getWarehouseOther() != null && getB().getWarehouse() != out.getWarehouseOther()
			    && ((SalMvOut) getB().gtOutForm()).getFromType() == SalMvOut.OFromType.DEMAND.getLine().getKey())
				throw LOG.err("chk", "调出单需求发起，仓库不可修改！");
			getB().setDept(getB().getWarehouse());
			BigDecimal amt = BigDecimal.ZERO;
			for (PurMvInLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty())
				    .setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setCostPur(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().stStatus(Sys.OBillStatus.VERIFIED);
			updLineTid(getB(), getLines(), PurMvInLine.class);
			SysShipingDAO.upd(getB(), _ship, PurMvIn.T.SHIPING.getFld(), getB().gtShipingMode());
		}

		@Override
		public void after() {
			// TODO Auto-generated method stub
			super.after();
			SalMvOutDAO.UpdByIn dao = new SalMvOutDAO.UpdByIn();
			SalMvOut out = (SalMvOut) getB().gtOutForm();
			out.setAmt(getB().getAmt());
			out.setAmtCost(getB().getAmtCost());
			out.setBillFlag(getB().getBillFlag());
			out.setShipingMode(getB().getShipingMode());
			dao.setB(out);
			dao._ship = getB().gtShiping();
			List<SalMvOutLine> lines = getLinesTid(out, SalMvOutLine.class);
			for (int i = 0; i < lines.size(); i++) {
				SalMvOutLine outLine = lines.get(i);
				PurMvInLine inLine = getLines().get(i);
				outLine.setGoods(inLine.getGoods());
				outLine.setUom(inLine.getUom());
				outLine.setPrice(inLine.getPrice());
				outLine.setAmt(inLine.getAmt());
				outLine.setExpensesSale(inLine.getCostPur());
			}
			dao.setLines(lines);
			dao.commit();
		}
	}

	public static class Del extends IduDel<Del, PurMvIn> {

		@Override
		public void before() {
			super.before();
			assertStatus(getB(), Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			if (getB().getFromType() == OFromType.MVOUT.getLine().getKey())
				throw LOG.err("del", "该调入单自动生成，不可直接删除！");
			if (getB().getFromType() == OFromType.DEMAND.getLine().getKey())
				GsDemandDAO.fireByPo(getB(), getB().getCode());
			if (getB().getFromType() == OFromType.DIRECT.getLine().getKey())
				GsDemandDirectDAO.clearByForm((BeanForm) getB().gtOrigForm());
			delLineTid(getB(), PurMvInLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}
	}

	public static class DelByOut extends IduDel<Del, PurMvIn> {

		@Override
		public void before() {
			super.before();
			assertStatus(getB(), Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			delLineTid(getB(), PurMvInLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}
	}

	public static class Approve extends IduApprove<Approve, PurMvIn> {

		@Override
		public void run() {
			super.run();
			PurMvIn mvIn = loadThisBeanAndLock();
			assertStatus(mvIn, Sys.OBillStatus.VERIFIED);
			if (mvIn.getFromType() != OFromType.MVOUT.getLine().getKey()) //由调入单自动建调出单
				mvIn.setOutForm(SalMvOutDAO.createBy(mvIn).gtLongPkey());
			else {//由调出单生的调入单，审核时-产生出入库单
				if (mvIn.getWarehouse() != null)
					GsPub.insertIn(mvIn, mvIn.gtWarehouse(), mvIn.gtCellOther().getName(), mvIn.getShipingMode(),
					    mvIn.getShiping());
				SalMvOut mvOut = (SalMvOut) mvIn.gtOutForm();
				if (mvOut.getGoodsFrom() == OGoodsFrom.DIRECT_DEMAND.getLine().getKey()) {// 生产直销需求
					GsDemandDirectDAO.insertByForm(mvOut);
				}
				if (mvOut.getWarehouse() != null)
					GsPub.insertOut(mvOut, mvOut.gtWarehouse(), mvOut.gtCellOther().getName(), mvIn.getShipingMode(),
					    mvIn.getShiping());
				mvOut.setWarehouseOther(mvIn.getWarehouse());
				mvOut.upd();
				mvIn.stDept(Idu.getDept()); //调整合适的部门
			}
			mvIn.stStatus(STATUS.TALLY_ABLE);
			mvIn.setApprBy(getUser().getPkey());
			mvIn.setApprTime(Env.INST.getSystemTime());
			mvIn.upd();
			setB(mvIn);
			// 在途处理
			if (mvIn.getWarehouse() != null)
				GsPub
				    .insertStimate(mvIn, mvIn.gtWarehouse(), Idu.getLinesTid(mvIn, PurMvInLine.class), OEnrouteType.DBZT, null);
			List<GsDemand> list;
			if (mvIn.getFromType() != OFromType.MVOUT.getLine().getKey()) {
				list = BeanBase
				    .list(GsDemand.class, "po_form = ? and po_form_num = ?", true, mvIn.gtLongPkey(), mvIn.getCode());
			} else {
				SalMvOut mvOut = (SalMvOut) mvIn.gtOutForm();
				list = BeanBase.list(GsDemand.class, "po_form = ? and po_form_num = ?", true, mvOut.gtLongPkey(),
				    mvOut.getCode());
			}
			for (GsDemand demand : list) {
				Bean bean = demand.gtOrigForm();
				if (bean instanceof GsRequest) {
					List<IGoods> glines = new ArrayList<IGoods>();
					GsRequestLine line = new GsRequestLine();
					line.setGoods(demand.getGoods());
					line.setQty(demand.getQty());
					line.setUom(demand.getUom());
					glines.add(line);
					GsPub.deleteStimate((GsRequest) bean, mvIn.gtWarehouse(), glines, Gs.OEnrouteType.YQG);
				}
			}
			// 待处理
			FrmPendingDAO.ins(getB(), CstIn.TB, GlDaybook.TB);
			FrmPendingDAO.ins(getB(), CstMvInvoiceIn.TB, getB().gtMvType().getLine().getName());
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurMvIn> {

		@Override
		public void run() {
			super.run();
			PurMvIn mvIn = loadThisBeanAndLock();
			assertStatus(mvIn, Sys.OBillStatus.TALLY_ABLE, Sys.OBillStatus.INOUT);
			if (mvIn.getFromType() != OFromType.MVOUT.getLine().getKey()) {
				//调入单不是由调出单产生时，需要把自动产生的调出单删除
				SalMvOutDAO.DelByIn dao = new SalMvOutDAO.DelByIn();
				dao.setB((SalMvOut) mvIn.gtOutForm());
				dao.commit();
				mvIn.setOutForm(null);
			} else {
				SalMvOut mvOut = (SalMvOut) mvIn.gtOutForm();
				if (mvOut.getGoodsFrom() == OGoodsFrom.DIRECT_DEMAND.getLine().getKey()) {// 删除直销需求
					GsDemandDirectDAO.deleteByForm(mvOut);
				}
				if (mvOut.getWarehouse() != null) {
					GsPub.deleteOut(mvOut);
				}
				if (mvIn.getWarehouse() != null) {// 删除出入库单
					GsPub.deleteIn(mvIn);
				}
			}
			mvIn.stStatus(Sys.OBillStatus.VERIFIED);
			mvIn.setApprBy(null);
			mvIn.setApprTime(null);
			mvIn.upd();
			setB(mvIn);
			// 在途处理
			if (mvIn.getWarehouse() != null)
				GsPub.deleteStimate(mvIn, mvIn.gtWarehouse(), Idu.getLinesTid(mvIn, PurMvInLine.class), Gs.OEnrouteType.DBZT);
			List<GsDemand> list;
			if (mvIn.getFromType() != OFromType.MVOUT.getLine().getKey()) {
				list = BeanBase
				    .list(GsDemand.class, "po_form = ? and po_form_num = ?", true, mvIn.gtLongPkey(), mvIn.getCode());
			} else {
				SalMvOut mvOut = (SalMvOut) mvIn.gtOutForm();
				list = BeanBase.list(GsDemand.class, "po_form = ? and po_form_num = ?", true, mvOut.gtLongPkey(),
				    mvOut.getCode());
			}
			for (GsDemand demand : list) {
				Bean bean = demand.gtOrigForm();
				if (bean instanceof GsRequest) {
					List<IGoods> glines = new ArrayList<IGoods>();
					GsRequestLine line = new GsRequestLine();
					line.setGoods(demand.getGoods());
					line.setQty(demand.getQty());
					line.setUom(demand.getUom());
					glines.add(line);
					GsPub.insertStimate((GsRequest) bean, mvIn.gtWarehouse(), glines, Gs.OEnrouteType.YQG, null);
				}
			}
			// 取消待处理
			FrmPendingDAO.del(getB(), CstMvInvoiceIn.TB, CstIn.TB, GlDaybook.TB);
		}
	}
	
	public static class CIn extends Idu<CIn, PurMvIn> {

		@Override
		public void commit() {
			PurMvIn mvIn = loadThisBeanAndLock();
			assertStatusIsCheck(mvIn);
			if (mvIn.getWarehouse() == null)
				throw LOG.err("cin", "该调入单不能产生出库单！");
			if (mvIn.getOutForm() == null)
				throw LOG.err("cin", "调出方还未确认，不能产生入库单！");
			SalMvOut mvOut = (SalMvOut) mvIn.gtOutForm();
			if (mvOut.getStatus() < Sys.OBillStatus.CHECKED.getLine().getKey())
				throw LOG.err("cin", "调出方还未确认，不能产生入库单！");
			GsPub.insertIn(mvIn, mvIn.gtWarehouse(), mvIn.gtCellOther().getName(), mvIn.getShipingMode(), mvIn.getShiping());
			mvIn.stStatus(Sys.OBillStatus.INOUT); // 出入库状态修改
			mvIn.upd();
			setB(mvIn);
		}
	}

	public static class Close extends IduOther<Close, PurMvIn> {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			super.run();
			assertStatusIsCheck(getB());
			PurMvIn order = loadThisBeanAndLock();
			order.stStatus(STATUS.DONE);
			order.upd();
			setB(order);
		}
	}

	public static class InsFromOut extends IduInsLines<Ins, PurMvIn, PurMvInLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "货物信息不可为空");
			getB().setCode(SysSeqDAO.getSeqnum(PurMvIn.TB, getB().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
			getB().stGoodsTo(OGoodsTo.WAREHOURSE);
			getB().stFromType(OFromType.MVOUT);

		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, PurMvIn.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}
	}

	public static PurMvIn createByOut(SalMvOut out) {
		PurMvIn mvIn = new PurMvIn();
		mvIn.setWarehouseOther(out.getWarehouse());
		mvIn.setOrgOther(out.getOrg());
		mvIn.setCellOther(out.getCell());
		mvIn.setOutForm(out.gtLongPkey());
		mvIn.setAmt(out.getAmt());
		mvIn.setAmtCost(out.getAmtCost());
		mvIn.setOrg(out.getOrgOther());
		mvIn.setCell(out.getCellOther());
		mvIn.setShipingMode(out.getShipingMode());
		mvIn.setMvType(out.getMvType());
		mvIn.setBillFlag(out.getBillFlag());
		mvIn.setWarehouse(out.getWarehouseOther());
		mvIn.setDept(out.getDept()); //由单据审核时更改为对应操作人员的部门
		InsFromOut dao = new InsFromOut();
		dao.setB(mvIn);
		dao._ship = out.gtShiping();
		List<SalMvOutLine> outLines = Idu.getLinesTid(out, SalMvOutLine.class);
		List<PurMvInLine> inLines = new ArrayList<PurMvInLine>();
		for (SalMvOutLine outLine : outLines) {
			PurMvInLine inLine = new PurMvInLine();
			inLine.setGoods(outLine.getGoods());
			inLine.setUom(outLine.getUom());
			inLine.setQty(outLine.getQty());
			inLine.setPrice(outLine.getPrice());
			inLine.setAmt(outLine.getAmt());
			inLine.setCostPur(outLine.getExpensesSale());
			inLines.add(inLine);
		}
		dao.setLines(inLines);
		dao.commit();
		return dao.getB();
	}

	@Override
	public void inOk(GsIn in, PurMvIn model) {
		// TODO Auto-generated method stub
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurMvInLine.class), OEnrouteType.DBZT);
		model.setShiping(in.getShiping());
		model.setShipingMode(in.getShipingMode());
		model.upd();
	}

	@Override
	public void inCancel(GsIn in, PurMvIn model) {
		// TODO Auto-generated method stub
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurMvInLine.class), OEnrouteType.DBZT, null);
	}

	@Override
	public List<IGoods> getInLines(PurMvIn model, int idx, int count) {
		// TODO Auto-generated method stub
		return Idu.getLinesTid(model, PurMvInLine.class, idx, count);
	}

	@Override
	public int getInLinesCount(PurMvIn model) {
		// TODO Auto-generated method stub
		return Idu.getLinesTidCount(model, PurMvInLine.class);
	}
}
