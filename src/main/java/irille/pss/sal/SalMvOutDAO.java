package irille.pss.sal;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysSeqDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gs.GsDemandDAO;
import irille.gl.gs.GsDemandDirectDAO;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.cst.CstMvInvoiceOut;
import irille.pss.cst.CstOut;
import irille.pss.pur.PurMvIn;
import irille.pss.pur.PurMvInDAO;
import irille.pss.pur.PurMvInLine;
import irille.pss.sal.SalMvOut.OFromType;
import irille.pss.sal.SalMvOut.OGoodsFrom;
import irille.pss.sal.SalMvOut.OMvType;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IDirect;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SalMvOutDAO implements IDirect<SalMvOut>, IOut<SalMvOut> {
	public static final Log LOG = new Log(SalMvOutDAO.class);

	public static class Ins extends IduInsLines<Ins, SalMvOut, SalMvOutLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			getB().stStatus(Sys.OBillStatus.VERIFIED);
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getSystemTime());
			if (getB().getWarehouse() != null) {
				getB().stCell(SysCellDAO.getCellByDept(getB().getWarehouse()));
				getB().setDept(getB().getWarehouse());
				getB().setOrg(getB().gtDept().getOrg());
			} else {
				getB().setOrg(getUser().getOrg());
				getB().setDept(getUser().getDept());
				getB().stCell(SysCellDAO.getCellByDept(getB().getDept()));
			}
			getB().setCode(SysSeqDAO.getSeqnum(SalMvOut.TB, getB().gtOrg(), true));
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
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.DIRECT_DEMAND);
			else
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.WAREHOURSE);
			if (getB().getWarehouseOther() != null) {
				List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, getB().getWarehouseOther());
				if (warelinks.size() > 0) {
					SysPersonLink link = warelinks.get(0);
					_ship = new SysShiping();
					_ship.setName(link.getName());
					_ship.setAddr(link.getOfAddr());
					_ship.setMobile(link.getPeMobile());
					_ship.setTel(link.getOfTel());
				}
			}
			BigDecimal amt = BigDecimal.ZERO;
			for (SalMvOutLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setExpensesSale(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
		}

		@Override
		public void after() {
			super.after();
			if (getB().getFromType() == OFromType.DEMAND.getLine().getKey())
				for (SalMvOutLine line : getLines()) {
					GsDemandDAO.createdByPo(line.getPkey(), getB(), getB().getCode());
				}
			insLineTid(getB(), getLines());
			if (_ship != null) {
				SysShipingDAO.ins(getB(), _ship, SalMvOut.T.SHIPING.getFld(), getB().gtShipingMode());
				if (getB().getShiping() != null)
					getB().upd();
			}
		}
	}

	public static class Upd extends IduUpdLines<Upd, SalMvOut, SalMvOutLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoodsPrice(getLines());
			SalMvOut order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			PropertyUtils.copyProperties(order, getB(), SalMvOut.T.WAREHOUSE, SalMvOut.T.CELL_OTHER, SalMvOut.T.BILL_FLAG,
			    SalMvOut.T.REM, SalMvOut.T.SHIPING_MODE);
			setB(order);
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
			if (getB().getWarehouse() == null)
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.DIRECT_DEMAND);
			else
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.WAREHOURSE);
			BigDecimal amt = BigDecimal.ZERO;
			for (SalMvOutLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setExpensesSale(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().stStatus(Sys.OBillStatus.VERIFIED);
			updLineTid(getB(), getLines(), SalMvOutLine.class);
			SysShipingDAO.upd(getB(), _ship, SalMvOut.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class UpdByIn extends IduUpdLines<Upd, SalMvOut, SalMvOutLine> {

		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			SalMvOut order = loadThisBeanAndLock();
			PropertyUtils.copyProperties(order, getB(), SalMvOut.T.BILL_FLAG, SalMvOut.T.WAREHOUSE_OTHER, SalMvOut.T.AMT,
			    SalMvOut.T.AMT_COST, SalMvOut.T.SHIPING_MODE);
			setB(order);
			updLineTid(getB(), getLines(), SalMvOutLine.class);

			SysShipingDAO.upd(getB(), _ship, SalMvOut.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Chk extends IduUpdLines<Upd, SalMvOut, SalMvOutLine> {
		public SysShiping _ship;

		@Override
		public void before() {
			super.before();
			SalMvOut order = loadThisBeanAndLock();
			assertStatus(order, Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			PropertyUtils.copyProperties(order, getB(), SalMvOut.T.WAREHOUSE, SalMvOut.T.BILL_FLAG, SalMvOut.T.REM,
			    SalMvOut.T.SHIPING_MODE);
			setB(order);
			if (getB().getWarehouse() == null)
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.DIRECT_DEMAND);
			else
				getB().stGoodsFrom(SalMvOut.OGoodsFrom.WAREHOURSE);
			BigDecimal amt = BigDecimal.ZERO;
			for (SalMvOutLine line : getLines()) {
				line.setAmt(line.getPrice().multiply(line.getQty()).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP));
				line.setExpensesSale(BigDecimal.ZERO);
				amt = amt.add(line.getAmt());
			}
			getB().setAmt(amt);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().stStatus(Sys.OBillStatus.VERIFIED);
			updLineTid(getB(), getLines(), SalMvOutLine.class);
			SysShipingDAO.upd(getB(), _ship, SalMvOut.T.SHIPING.getFld(), getB().gtShipingMode());
		}

		@Override
		public void after() {
			// TODO Auto-generated method stub
			super.after();
			PurMvInDAO.UpdByOut dao = new PurMvInDAO.UpdByOut();
			PurMvIn in = (PurMvIn) getB().gtInForm();
			in.setAmt(getB().getAmt());
			in.setAmtCost(getB().getAmtCost());
			in.setBillFlag(getB().getBillFlag());
			dao.setB(in);
			dao._ship = getB().gtShiping();
			List<PurMvInLine> lines = getLinesTid(in, PurMvInLine.class);
			for (int i = 0; i < lines.size(); i++) {
				PurMvInLine inLine = lines.get(i);
				SalMvOutLine outLine = getLines().get(i);
				inLine.setGoods(outLine.getGoods());
				inLine.setUom(outLine.getUom());
				inLine.setPrice(outLine.getPrice());
				inLine.setAmt(outLine.getAmt());
				inLine.setCostPur(outLine.getExpensesSale());
			}
			dao.setLines(lines);
			dao.commit();
		}
	}

	public static class Del extends IduDel<Del, SalMvOut> {

		@Override
		public void before() {
			super.before();
			assertStatus(getB(), Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			if (getB().getFromType() == OFromType.MVIN.getLine().getKey())
				throw LOG.err("del", "该调出单自动生成，不可直接删除！");
			if (getB().getFromType() == OFromType.DEMAND.getLine().getKey())
				GsDemandDAO.fireByPo(getB(), getB().getCode());
			delLineTid(getB(), SalMvOutLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}
	}

	public static class DelByIn extends IduDel<Del, SalMvOut> {

		@Override
		public void before() {
			super.before();
			assertStatus(getB(), Sys.OBillStatus.INIT, Sys.OBillStatus.VERIFIED);
			delLineTid(getB(), SalMvOutLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}
	}

	public static class Approve extends IduApprove<Approve, SalMvOut> {

		@Override
		public void run() {
			super.run();
			SalMvOut mvOut = loadThisBeanAndLock();
			assertStatus(mvOut, Sys.OBillStatus.VERIFIED);
			if (mvOut.getFromType() != OFromType.MVIN.getLine().getKey())
				mvOut.setInForm(PurMvInDAO.createByOut(mvOut).gtLongPkey());
			else {// 产生出入库单
				if (mvOut.getWarehouse() != null)
					GsPub.insertOut(mvOut, mvOut.gtWarehouse(), mvOut.gtCellOther().getName(), mvOut.getShipingMode(),
					    mvOut.getShiping());
				PurMvIn mvIn = (PurMvIn) mvOut.gtInForm();
				if (mvIn.getWarehouse() != null)
					GsPub.insertIn(mvIn, mvIn.gtWarehouse(), mvIn.gtCellOther().getName(), mvOut.getShipingMode(),
					    mvOut.getShiping());
				if (mvOut.getGoodsFrom() == OGoodsFrom.DIRECT_DEMAND.getLine().getKey()) {// 生产直销需求
					GsDemandDirectDAO.insertByForm(mvOut);
				}
				mvIn.setWarehouseOther(mvOut.getWarehouse());
				mvIn.upd();
				mvOut.stDept(Idu.getDept()); //调整合适的部门
			}
			mvOut.stStatus(STATUS.TALLY_ABLE);
			mvOut.setApprBy(getUser().getPkey());
			mvOut.setApprTime(Env.INST.getWorkDate());
			mvOut.upd();
			setB(mvOut);
			// 在途处理
			if (mvOut.getWarehouse() != null)
				GsPub.insertStimate(mvOut, mvOut.gtWarehouse(), Idu.getLinesTid(mvOut, SalMvOutLine.class), OEnrouteType.DBDF,
				    null);
			// 待处理
			//产生流水账的待处理登记
			FrmPendingDAO.ins(getB(), CstOut.TB, GlDaybook.TB);
			FrmPendingDAO.ins(getB(), CstMvInvoiceOut.TB, getB().gtMvType().getLine().getName());
			// 记账 -- Bean-tallyInit
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, SalMvOut> {

		@Override
		public void run() {
			super.run();
			SalMvOut mvOut = loadThisBeanAndLock();
			assertStatus(mvOut, Sys.OBillStatus.TALLY_ABLE, Sys.OBillStatus.INOUT);
			if (mvOut.getFromType() != OFromType.MVIN.getLine().getKey()) {
				PurMvInDAO.DelByOut dao = new PurMvInDAO.DelByOut();
				dao.setB((PurMvIn) mvOut.gtInForm());
				dao.commit();
				mvOut.setInForm(null);

			} else {
				PurMvIn mvIn = (PurMvIn) mvOut.gtInForm();
				if (mvIn.getWarehouse() != null) {
					GsPub.deleteIn(mvIn);
				}
				if (mvOut.getWarehouse() != null) {// 删除出入库单
					GsPub.deleteOut(mvOut);
				}
				if (mvOut.getGoodsFrom() == OGoodsFrom.DIRECT_DEMAND.getLine().getKey()) {// 删除直销需求
					GsDemandDirectDAO.deleteByForm(mvOut);
				}
			}
			mvOut.stStatus(Sys.OBillStatus.VERIFIED);
			mvOut.setApprBy(null);
			mvOut.setApprTime(null);
			mvOut.upd();
			setB(mvOut);
			// 在途处理
			if (mvOut.getWarehouse() != null)
				GsPub.deleteStimate(mvOut, mvOut.gtWarehouse(), Idu.getLinesTid(mvOut, SalMvOutLine.class), OEnrouteType.DBDF);
			// 取消待处理
			FrmPendingDAO.del(getB(), CstMvInvoiceOut.TB, CstOut.TB, GlDaybook.TB);
		}
	}

	//TODO 出入库成功后，目前并未调用此方法，状态的判断也是存在问题
	public static class COut extends Idu<COut, SalMvOut> {  

		@Override
		public void commit() {
			SalMvOut mvOut = loadThisBeanAndLock();
			assertStatusIsCheck(mvOut);
			if (mvOut.getWarehouse() == null)
				throw LOG.err("cout", "该调出单不能产生出库单！");
			if (mvOut.getInForm() == null)
				throw LOG.err("cout", "调入方还未确认，不能产生出库单！");
			PurMvIn mvIn = (PurMvIn) mvOut.gtInForm();
			if (mvIn.getStatus() < Sys.OBillStatus.CHECKED.getLine().getKey())
				throw LOG.err("cout", "调入方还未确认，不能产生出库单！");
			GsPub.insertOut(mvOut, mvOut.gtWarehouse(), mvOut.gtCellOther().getName(), mvOut.getShipingMode(),
			    mvOut.getShiping());
			mvOut.stStatus(Sys.OBillStatus.INOUT); // 出入库状态修改
			mvOut.upd();
		}
	}

	public static class InsFromIn extends IduInsLines<Ins, SalMvOut, SalMvOutLine> {

		SysShiping _ship;

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "货物信息不可为空");
			getB().setCode(SysSeqDAO.getSeqnum(SalMvOut.TB, getB().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
			getB().stGoodsFrom(OGoodsFrom.WAREHOURSE);
			getB().stFromType(OFromType.MVIN);
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, SalMvOut.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}
	}

	public static SalMvOut createBy(PurMvIn in) {
		SalMvOut mvOut = new SalMvOut();
		mvOut.setWarehouseOther(in.getWarehouse());
		mvOut.setOrgOther(in.getOrg());
		mvOut.setCellOther(in.getCell());
		mvOut.setInForm(in.gtLongPkey());
		mvOut.setAmt(in.getAmt());
		mvOut.setAmtCost(in.getAmtCost());
		mvOut.setOrg(in.getOrgOther());
		mvOut.setCell(in.getCellOther());
		mvOut.setShipingMode(in.getShipingMode());
		mvOut.setMvType(in.getMvType());
		mvOut.setBillFlag(in.getBillFlag());
		mvOut.setWarehouse(in.getWarehouseOther());
		mvOut.setDept(in.getDept()); //由单据审核时更改为对应操作人员的部门
		InsFromIn dao = new InsFromIn();
		dao.setB(mvOut);
		dao._ship = in.gtShiping();
		List<PurMvInLine> inLines = Idu.getLinesTid(in, PurMvInLine.class);
		List<SalMvOutLine> outLines = new ArrayList<SalMvOutLine>();
		for (PurMvInLine inLine : inLines) {
			SalMvOutLine outLine = new SalMvOutLine();
			outLine.setGoods(inLine.getGoods());
			outLine.setUom(inLine.getUom());
			outLine.setQty(inLine.getQty());
			outLine.setPrice(inLine.getPrice());
			outLine.setAmt(inLine.getAmt());
			outLine.setExpensesSale(inLine.getCostPur());
			outLines.add(outLine);
		}
		dao.setLines(outLines);
		dao.commit();
		return dao.getB();
	}

	@Override
	public void directOk(SalMvOut model) {
		// Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		// model.stStatus(Sys.OBillStatus.DONE);
		// model.upd();
	}

	@Override
	public void directCancel(SalMvOut model) {
		// Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		// model.stStatus(Sys.OBillStatus.CHECKED);
		// model.upd();
	}

	@Override
	public void outOk(GsOut out, SalMvOut model) {
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalMvOutLine.class), OEnrouteType.DBDF);
		model.setShiping(out.getShiping());
		model.setShipingMode(out.getShipingMode());
		model.upd();
	}

	@Override
	public void outCancel(GsOut out, SalMvOut model) {
		GsPub
		    .insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, SalMvOutLine.class), OEnrouteType.DBDF, null);
	}

	@Override
	public List<IGoods> getOutLines(SalMvOut model, int idx, int count) {
		return Idu.getLinesTid(model, SalMvOutLine.class, idx, count);
	}

	@Override
	public int getOutLinesCount(SalMvOut model) {
		return Idu.getLinesTidCount(model, SalMvOutLine.class);
	}
}
