package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.util.List;

public class GsMovementDAO implements IIn<GsMovement>, IOut<GsMovement> {

	public static final Log LOG = new Log(GsMovementDAO.class);

	public static class Ins extends IduInsLines<Ins, GsMovement, GsMovementLine> {
		@Override
		public void before() {
			super.before();
			if (getB().getWarehouseIn() == getB().getWarehouseOut())
				throw LOG.err("sameWarehouse", "调入仓库与调出仓库不可相同，请重新选择！");
			if (getB().gtWarehouseIn().getOrg() != getB().gtWarehouseOut().getOrg())
				throw LOG.err("errOrg", "调入仓库与调出仓库所属机构不相同，请重新选择！");
			Idu.checkFormGoods(getLines());
			getB().setCode(SysSeqDAO.getSeqnum(GsMovement.TB, getB().gtWarehouseIn().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().stOrg(getB().gtWarehouseIn().gtOrg());
			getB().setDept(getB().getWarehouseIn());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getSystemTime());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Upd extends IduUpdLines<Upd, GsMovement, GsMovementLine> {

		@Override
		public void before() {
			super.before();
			if (getB().getWarehouseIn() == getB().getWarehouseOut())
				throw LOG.err("sameWarehouse", "调入仓库与调出仓库不可相同，请重新选择！");
			if (getB().gtWarehouseIn().getOrg() != getB().gtWarehouseOut().getOrg())
				throw LOG.err("errOrg", "调入仓库与调出仓库所属机构不相同，请重新选择！");
			Idu.checkFormGoods(getLines());
			GsMovement mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			GsWarehouse warehouse = mode.gtWarehouseIn();
			if (warehouse.getOrg() != getB().gtWarehouseIn().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			mode.stOrg(getB().gtWarehouseIn().gtOrg());
			mode.setDept(getB().getWarehouseIn());
			mode.setWarehouseIn(getB().getWarehouseIn());
			mode.setWarehouseOut(getB().getWarehouseOut());
			mode.setRem(getB().getRem());
			setB(mode);
			updLineTid(getB(), getLines(), GsMovementLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsMovement> {

		@Override
		public void before() {
			super.before();
			GsMovement movement = loadThisBeanAndLock();
			assertStatusIsInit(movement);
			delLineTid(getB(), GsMovementLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsMovement> {

		@Override
		public void run() {
			super.run();
			GsMovement movement = loadThisBeanAndLock();
			assertStatusIsInit(movement);
			movement.stStatus(STATUS.CHECKED);
			movement.setApprBy(getUser().getPkey());
			movement.setApprTime(Env.INST.getWorkDate());
			movement.upd();
			setB(movement);
			GsWarehouse gwin = movement.gtWarehouseIn();
			GsWarehouse gwout = movement.gtWarehouseOut();
			List<GsMovementLine> lines = getLinesTid(movement, GsMovementLine.class);
			GsPub.insertIn(movement, gwin, movement.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertOut(movement, gwout, movement.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertStimate(movement, gwin, lines, Gs.OEnrouteType.DBZT, null);
			GsPub.insertStimate(movement, gwout, lines, Gs.OEnrouteType.DBDF, null);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsMovement> {

		@Override
		public void run() {
			super.run();
			GsMovement movement = loadThisBeanAndLock();
			assertStatusIsCheck(movement);
			movement.stStatus(STATUS_INIT);
			movement.setApprBy(null);
			movement.setApprTime(null);
			movement.upd();
			setB(movement);
			GsWarehouse gwin = movement.gtWarehouseIn();
			GsWarehouse gwout = movement.gtWarehouseOut();
			List<GsMovementLine> lines = getLinesTid(movement, GsMovementLine.class);
			GsPub.deleteIn(movement);
			GsPub.deleteOut(movement);
			GsPub.deleteStimate(movement, gwin, lines, Gs.OEnrouteType.DBZT);
			GsPub.deleteStimate(movement, gwout, lines, Gs.OEnrouteType.DBDF);
		}
	}

	@Override
	public void inOk(GsIn in, GsMovement model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的出库单尚未审核，不可操作！");
		model.stStatus(Sys.OBillStatus.DONE);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouseIn(), Idu.getLinesTid(model, GsMovementLine.class), Gs.OEnrouteType.DBZT);
	}

	@Override
	public void inCancel(GsIn in, GsMovement model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouseIn(), Idu.getLinesTid(model, GsMovementLine.class), Gs.OEnrouteType.DBZT, null);
	}

	@Override
	public void outOk(GsOut out, GsMovement model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouseOut(), Idu.getLinesTid(model, GsMovementLine.class), Gs.OEnrouteType.DBDF);
	}

	@Override
	public void outCancel(GsOut out, GsMovement model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的入库单已审核，不可操作！");
		model.stStatus(Sys.OBillStatus.CHECKED);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouseOut(), Idu.getLinesTid(model, GsMovementLine.class), Gs.OEnrouteType.DBDF, null);
	}
	
	@Override
	public List<IGoods> getInLines(GsMovement model, int idx, int count) {
		return Idu.getLinesTid(model, GsMovementLine.class, idx, count);
	}
	
	@Override
	public int getInLinesCount(GsMovement model) {
		return Idu.getLinesTidCount(model, GsMovementLine.class);
	}
	
	@Override
	public List<IGoods> getOutLines(GsMovement model, int idx, int count) {
		return Idu.getLinesTid(model, GsMovementLine.class, idx, count);
	}
	
	@Override
	public int getOutLinesCount(GsMovement model) {
		return Idu.getLinesTidCount(model, GsMovementLine.class);
	}
}
