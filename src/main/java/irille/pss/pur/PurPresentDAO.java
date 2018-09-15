package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlDaybook;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.cst.CstIn;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.inf.IIn;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class PurPresentDAO implements IIn<PurPresent>{
	public static final Log LOG = new Log(PurPresentDAO.class);

	public static class Ins extends IduInsLines<Ins, PurPresent, PurPresentLine> {
		
		public SysShiping _ship;
		
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			initBill(getB());
			getB().setAmt(BigDecimal.ZERO);
			getB().setAmtCost(BigDecimal.ZERO);
			getB().setBillFlag(Sys.OBillFlag.NO.getLine().getKey());
			for (PurPresentLine line : getLines()) {
				line.setPrice(BigDecimal.ZERO);
				line.setAmt(BigDecimal.ZERO);
				line.setCostPur(BigDecimal.ZERO);
			}
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			SysShipingDAO.ins(getB(), _ship, PurPresent.T.SHIPING.getFld(), getB().gtShipingMode());
			if (getB().getShiping() != null)
				getB().upd();
		}
	}

	public static class Upd extends IduUpdLines<Upd, PurPresent, PurPresentLine> {

		public SysShiping _ship;
		
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			PurPresent rev = loadThisBeanAndLock();
			PropertyUtils.copyProperties(rev, getB(), PurPresent.T.SUPPLIER, PurPresent.T.WAREHOUSE,
			    PurPresent.T.REM);
			setB(rev);
			for (PurPresentLine line : getLines()) {
				line.setPrice(BigDecimal.ZERO);
				line.setAmt(BigDecimal.ZERO);
				line.setCostPur(BigDecimal.ZERO);
			}
			updLineTid(getB(), getLines(), PurPresentLine.class);
			SysShipingDAO.upd(getB(), _ship, PurPresent.T.SHIPING.getFld(), getB().gtShipingMode());
		}
	}

	public static class Del extends IduDel<Del, PurPresent> {

		@Override
		public void valid() {
			super.valid();
			PurPresent order = loadThisBeanAndLock();
			assertStatusIsInit(order);
		}

		@Override
		public void before() {
			super.before();
			delLineTid(getB(), PurPresentLine.class);
			SysShipingDAO.del(getB(), getB().gtShipingMode());
		}

	}

	public static class Approve extends IduApprove<Approve, PurPresent> {

		@Override
		public void run() {
			super.run();
			PurPresent rev = loadThisBeanAndLock();
			assertStatusIsInit(rev);
			rev.stStatus(Sys.OBillStatus.TALLY_ABLE);
			rev.setApprBy(getUser().getPkey());
			rev.setApprTime(Env.getTranBeginTime());
			rev.upd();
			setB(rev);
			GsPub.insertStimate(rev, rev.gtWarehouse(), Idu.getLinesTid(rev, PurPresentLine.class),
			    OEnrouteType.QTZT, null);
			GsPub.insertIn(rev, rev.gtWarehouse(), rev.getSupname(), rev.getShipingMode(), rev.getShiping());
		//待处理
			FrmPendingDAO.ins(getB(), CstIn.TB, GlDaybook.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, PurPresent> {
		@Override
		public void run() {
			super.run();
			PurPresent rev = loadThisBeanAndLock();
			assertStatusIsTally(rev);
			rev.stStatus(Sys.OBillStatus.INIT);
			rev.setApprBy(null);
			rev.setApprTime(null);
			rev.upd();
			setB(rev);
			GsPub.deleteStimate(rev, rev.gtWarehouse(), Idu.getLinesTid(rev, PurPresentLine.class),
			    OEnrouteType.QTZT);
			GsPub.deleteIn(rev);
		//取消待处理
			FrmPendingDAO.del(getB(), CstIn.TB, GlDaybook.TB);
		}
	}
	
	@Override
	public void inOk(GsIn in, PurPresent model) {
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurPresentLine.class),
		    OEnrouteType.QTZT);
		model.setShiping(in.getShiping());
		model.setShipingMode(in.getShipingMode());
		model.upd();
	}

	@Override
	public void inCancel(GsIn in, PurPresent model) {
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, PurPresentLine.class),
		    OEnrouteType.QTZT, null);
	}

	@Override
	public List<IGoods> getInLines(PurPresent model, int idx, int count) {
		return Idu.getLinesTid(model, PurPresentLine.class, idx, count);
	}

	@Override
	public int getInLinesCount(PurPresent model) {
		return Idu.getLinesTidCount(model, PurPresentLine.class);
	}
}
