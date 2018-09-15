package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.pss.cst.CstIn;
import irille.pss.cst.CstOut;
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
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GsUniteDAO implements IIn<GsUnite>, IOut<GsUnite> {
	public static final Log LOG = new Log(GsUniteDAO.class);

	public static class Ins extends IduInsLines<Ins, GsUnite, GsUniteLine> {
		@Override
		public void before() {
			super.before();
			if (getB().getQty().compareTo(BigDecimal.ZERO) <= 0)
				throw LOG.err("notPlus", "数量必须为正数！");
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

	public static class Upd extends IduUpdLines<Upd, GsUnite, GsUniteLine> {

		@Override
		public void before() {
			super.before();
			if (getB().getQty().compareTo(BigDecimal.ZERO) <= 0)
				throw LOG.err("notPlus", "数量必须为正数！");
			Idu.checkFormGoods(getLines());
			GsUnite mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			GsWarehouse warehouse = mode.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			PropertyUtils.copyProperties(mode, getB(), GsUnite.T.REM, GsUnite.T.WAREHOUSE, GsUnite.T.GOODS, GsUnite.T.QTY, GsUnite.T.UOM);
			setB(mode);
			updLineTid(getB(), getLines(), GsUniteLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsUnite> {

		@Override
		public void before() {
			super.before();
			GsUnite Unite = loadThisBeanAndLock();
			assertStatusIsInit(Unite);
			delLineTid(getB(), GsUniteLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsUnite> {

		@Override
		public void run() {
			super.run();
			GsUnite Unite = loadThisBeanAndLock();
			assertStatusIsInit(Unite);
			Unite.stStatus(STATUS.CHECKED);
			Unite.setApprBy(getUser().getPkey());
			Unite.setApprTime(Env.INST.getWorkDate());
			Unite.upd();
			setB(Unite);
			GsWarehouse gw = Unite.gtWarehouse();
			List<GsUniteLine> mlines = new ArrayList();
			GsUniteLine mline = new GsUniteLine();
			mline.setGoods(Unite.getGoods());
			mline.setUom(Unite.getUom());
			mline.setQty(Unite.getQty());
			mlines.add(mline);
			List<GsUniteLine> lines = getLinesTid(Unite, GsUniteLine.class);
			GsPub.insertIn(Unite, gw, Unite.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertOut(Unite, gw, Unite.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertStimate(Unite, gw, mlines, Gs.OEnrouteType.QTZT, null);
			GsPub.insertStimate(Unite, gw, lines, Gs.OEnrouteType.QTSD, null);
			//待处理
			FrmPendingDAO.ins(getB(), CstOut.TB, CstIn.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsUnite> {

		@Override
		public void run() {
			super.run();
			GsUnite Unite = loadThisBeanAndLock();
			assertStatusIsCheck(Unite);
			Unite.stStatus(STATUS_INIT);
			Unite.setApprBy(null);
			Unite.setApprTime(null);
			Unite.upd();
			setB(Unite);
			GsWarehouse gw = Unite.gtWarehouse();
			List<GsUniteLine> mlines = new ArrayList();
			GsUniteLine mline = new GsUniteLine();
			mline.setGoods(Unite.getGoods());
			mline.setUom(Unite.getUom());
			mline.setQty(Unite.getQty());
			mlines.add(mline);
			List<GsUniteLine> lines = getLinesTid(Unite, GsUniteLine.class);
			GsPub.deleteIn(Unite);
			GsPub.deleteOut(Unite);
			GsPub.deleteStimate(Unite, gw, mlines, Gs.OEnrouteType.QTZT);
			GsPub.deleteStimate(Unite, gw, lines, Gs.OEnrouteType.QTSD);
			//取消待处理
			FrmPendingDAO.del(getB(), CstIn.TB, CstOut.TB);
		}
	}
	
	@Override
	public void inOk(GsIn in, GsUnite model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的出库单尚未审核，不可操作！");
		model.stStatus(Sys.OBillStatus.DONE);
		model.upd();
		List<GsUniteLine> mlines = new ArrayList();
		GsUniteLine mline = new GsUniteLine();
		mline.setGoods(model.getGoods());
		mline.setUom(model.getUom());
		mline.setQty(model.getQty());
		mlines.add(mline);
		GsPub.deleteStimate(model, model.gtWarehouse(), mlines, Gs.OEnrouteType.QTZT);
	}

	@Override
	public void inCancel(GsIn in, GsUnite model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		List<GsUniteLine> mlines = new ArrayList();
		GsUniteLine mline = new GsUniteLine();
		mline.setGoods(model.getGoods());
		mline.setUom(model.getUom());
		mline.setQty(model.getQty());
		mlines.add(mline);
		GsPub.insertStimate(model, model.gtWarehouse(), mlines, Gs.OEnrouteType.QTZT, null);
	}

	@Override
	public void outOk(GsOut out, GsUnite model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsUniteLine.class), Gs.OEnrouteType.QTSD);
	}

	@Override
	public void outCancel(GsOut out, GsUnite model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的入库单已审核，不可操作！");
		model.stStatus(Sys.OBillStatus.CHECKED);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsUniteLine.class), Gs.OEnrouteType.QTSD, null);
	}
	
	@Override
	public List<IGoods> getInLines(GsUnite model, int idx, int count) {
		List<IGoods> list = new ArrayList<IGoods>();
		GsUniteLine line = new GsUniteLine();
		line.setGoods(model.getGoods());
		line.setQty(model.getQty());
		line.setUom(model.getUom());
		list.add(line);
		return list;
	}
	
	@Override
	public int getInLinesCount(GsUnite model) {
		return 1;
	}
	
	@Override
	public List<IGoods> getOutLines(GsUnite model, int idx, int count) {
		return Idu.getLinesTid(model, GsUniteLine.class, idx, count);
	}
	
	@Override
	public int getOutLinesCount(GsUnite model) {
		return Idu.getLinesTidCount(model, GsUniteLine.class);
	}

}
