package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.pss.cst.CstOut;
import irille.pub.Log;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.util.List;

public class GsLossDAO implements IOut<GsLoss> {
	public static final Log LOG = new Log(GsLossDAO.class);

	public static class Ins extends IduInsLines<Ins, GsLoss, GsLossLine> {
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			getB().setCode(SysSeqDAO.getSeqnum(GsRequest.TB, getB().gtWarehouse().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setOrg(getB().gtWarehouse().getOrg());
			getB().setDept(getB().getWarehouse());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getSystemTime());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Upd extends IduUpdLines<Upd, GsLoss, GsLossLine> {

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			GsLoss mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			if (mode.getOrigForm() != null)
				throw LOG.err("hasOrig", "盘亏单[{0}]由盘单产生，不可手工修改", mode.getCode());
			GsWarehouse warehouse = mode.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			mode.setWarehouse(getB().getWarehouse());
			mode.setRem(getB().getRem());
			setB(mode);
			updLineTid(getB(), getLines(), GsLossLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsLoss> {

		@Override
		public void before() {
			super.before();
			GsLoss mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			if (mode.getOrigForm() != null)
				throw LOG.err("hasOrig", "盘亏单[{0}]由盘点单产生，不可手工删除", mode.getCode());
			delLineTid(getB(), GsLossLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsLoss> {

		@Override
		public void run() {
			super.run();
			GsLoss mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			mode.stStatus(STATUS.CHECKED);
			mode.setApprBy(getUser().getPkey());
			mode.setApprTime(Env.INST.getWorkDate());
			mode.upd();
			setB(mode);
			GsWarehouse gw = mode.gtWarehouse();
			List lines = getLinesTid(mode, GsLossLine.class);
			GsPub.insertOut(mode, gw, mode.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertStimate(mode, gw, lines, Gs.OEnrouteType.QTSD, null);
			//待处理
			FrmPendingDAO.ins(getB(), CstOut.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsLoss> {

		@Override
		public void run() {
			super.run();
			GsLoss mode = loadThisBeanAndLock();
			assertStatusIsCheck(mode);
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			GsWarehouse gw = mode.gtWarehouse();
			List lines = getLinesTid(mode, GsLossLine.class);
			GsPub.deleteOut(mode);
			GsPub.deleteStimate(mode, gw, lines, Gs.OEnrouteType.QTSD);
			//取消待处理
			FrmPendingDAO.del(getB(), CstOut.TB);
		}
	}
	
	@Override
	public void outOk(GsOut out, GsLoss model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		model.stStatus(Sys.OBillStatus.DONE);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsLossLine.class), Gs.OEnrouteType.QTSD);
	}
	
	@Override
	public void outCancel(GsOut out, GsLoss model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		model.stStatus(Sys.OBillStatus.CHECKED);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsLossLine.class), Gs.OEnrouteType.QTSD, null);
	}
	
	@Override
	public List<IGoods> getOutLines(GsLoss model, int idx, int count) {
		return Idu.getLinesTid(model, GsLossLine.class, idx, count);
	}
	
	@Override
	public int getOutLinesCount(GsLoss model) {
		return Idu.getLinesTidCount(model, GsLossLine.class);
	}

}
