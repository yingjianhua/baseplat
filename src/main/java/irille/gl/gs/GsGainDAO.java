package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.pss.cst.CstIn;
import irille.pub.Log;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.svr.Env;

import java.util.List;

public class GsGainDAO implements IIn<GsGain> {
	public static final Log LOG = new Log(GsGainDAO.class);

	public static class Ins extends IduInsLines<Ins, GsGain, GsGainLine> {
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

	public static class Upd extends IduUpdLines<Upd, GsGain, GsGainLine> {

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			GsGain mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			if (mode.getOrigForm() != null)
				throw LOG.err("hasOrig", "盘盈单[{0}]由盘点单产生，不可手工修改", mode.getCode());
			GsWarehouse warehouse = mode.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			mode.setWarehouse(getB().getWarehouse());
			mode.setRem(getB().getRem());
			setB(mode);
			updLineTid(getB(), getLines(), GsGainLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsGain> {

		@Override
		public void before() {
			super.before();
			GsGain gain = loadThisBeanAndLock();
			assertStatusIsInit(gain);
			if (gain.getOrigForm() != null)
				throw LOG.err("hasOrig", "盘盈单[{0}]由盘点单产生，不可手工删除", gain.getCode());
			delLineTid(getB(), GsGainLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsGain> {

		@Override
		public void run() {
			super.run();
			GsGain gain = loadThisBeanAndLock();
			assertStatusIsInit(gain);
			gain.stStatus(STATUS.CHECKED);
			gain.setApprBy(getUser().getPkey());
			gain.setApprTime(Env.INST.getWorkDate());
			gain.upd();
			setB(gain);
			GsWarehouse gw = gain.gtWarehouse();
			List lines = getLinesTid(gain, GsGainLine.class);
			GsPub.insertIn(gain, gw, gain.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertStimate(gain, gw, lines, Gs.OEnrouteType.QTZT, null);
		//待处理
			FrmPendingDAO.ins(getB(), CstIn.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsGain> {

		@Override
		public void run() {
			super.run();
			GsGain gain = loadThisBeanAndLock();
			assertStatusIsCheck(gain);
			gain.stStatus(STATUS_INIT);
			gain.setApprBy(null);
			gain.setApprTime(null);
			gain.upd();
			setB(gain);
			GsWarehouse gw = gain.gtWarehouse();
			List lines = getLinesTid(gain, GsGainLine.class);
			GsPub.deleteIn(gain);
			GsPub.deleteStimate(gain, gw, lines, Gs.OEnrouteType.QTZT);
		//取消待处理
			FrmPendingDAO.del(getB(), CstIn.TB);
		}
	}

	@Override
	public void inOk(GsIn in, GsGain model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		model.stStatus(Sys.OBillStatus.DONE);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsGainLine.class), Gs.OEnrouteType.QTZT);
	}

	@Override
	public void inCancel(GsIn in, GsGain model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		model.stStatus(Sys.OBillStatus.CHECKED);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsGainLine.class), Gs.OEnrouteType.QTZT,
		    null);
	}
	
	@Override
	public List<IGoods> getInLines(GsGain model, int idx, int count) {
		return Idu.getLinesTid(model, GsGainLine.class, idx, count);
	}
	
	@Override
	public int getInLinesCount(GsGain model) {
		return Idu.getLinesTidCount(model, GsGainLine.class);
	}
}
