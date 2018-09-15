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

public class GsSplitDAO implements IIn<GsSplit>, IOut<GsSplit> {
	public static final Log LOG = new Log(GsSplitDAO.class);

	public static class Ins extends IduInsLines<Ins, GsSplit, GsSplitLine> {
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
			getB().setAmt(BigDecimal.ZERO);
			getB().setAmtCost(BigDecimal.ZERO);
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Upd extends IduUpdLines<Upd, GsSplit, GsSplitLine> {

		@Override
		public void before() {
			super.before();
			if (getB().getQty().compareTo(BigDecimal.ZERO) <= 0)
				throw LOG.err("notPlus", "数量必须为正数！");
			Idu.checkFormGoods(getLines());
			GsSplit mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			GsWarehouse warehouse = mode.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			PropertyUtils.copyProperties(mode, getB(), GsSplit.T.REM, GsSplit.T.WAREHOUSE, GsSplit.T.GOODS, GsSplit.T.QTY, GsSplit.T.UOM);
			setB(mode);
			updLineTid(getB(), getLines(), GsSplitLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsSplit> {

		@Override
		public void before() {
			super.before();
			GsSplit split = loadThisBeanAndLock();
			assertStatusIsInit(split);
			delLineTid(getB(), GsSplitLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsSplit> {

		@Override
		public void run() {
			super.run();
			GsSplit split = loadThisBeanAndLock();
			assertStatusIsInit(split);
			split.stStatus(STATUS.CHECKED);
			split.setApprBy(getUser().getPkey());
			split.setApprTime(Env.INST.getWorkDate());
			split.upd();
			setB(split);
			GsWarehouse gw = split.gtWarehouse();
			List<GsSplitLine> mlines = new ArrayList();
			GsSplitLine mline = new GsSplitLine();
			mline.setGoods(split.getGoods());
			mline.setUom(split.getUom());
			mline.setQty(split.getQty());
			mlines.add(mline);
			List<GsSplitLine> lines = getLinesTid(split, GsSplitLine.class);
			GsPub.insertIn(split, gw, split.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertOut(split, gw, split.gtOrg().getName(), Sys.OShipingMode.NO.getLine().getKey(), null);
			GsPub.insertStimate(split, gw, lines, Gs.OEnrouteType.QTZT, null);
			GsPub.insertStimate(split, gw, mlines, Gs.OEnrouteType.QTSD, null);
			//待处理
			FrmPendingDAO.ins(getB(), CstOut.TB, CstIn.TB);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsSplit> {

		@Override
		public void run() {
			super.run();
			GsSplit split = loadThisBeanAndLock();
			assertStatusIsCheck(split);
			split.stStatus(STATUS_INIT);
			split.setApprBy(null);
			split.setApprTime(null);
			split.upd();
			setB(split);
			GsWarehouse gw = split.gtWarehouse();
			List<GsSplitLine> mlines = new ArrayList();
			GsSplitLine mline = new GsSplitLine();
			mline.setGoods(split.getGoods());
			mline.setUom(split.getUom());
			mline.setQty(split.getQty());
			mlines.add(mline);
			List<GsSplitLine> lines = getLinesTid(split, GsSplitLine.class);
			GsPub.deleteIn(split);
			GsPub.deleteOut(split);
			GsPub.deleteStimate(split, gw, lines, Gs.OEnrouteType.QTZT);
			GsPub.deleteStimate(split, gw, mlines, Gs.OEnrouteType.QTSD);
			//取消待处理
			FrmPendingDAO.del(getB(), CstIn.TB, CstOut.TB);
		}
	}

	@Override
	public void inOk(GsIn in, GsSplit model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的出库单尚未审核，不可操作！");
		model.stStatus(Sys.OBillStatus.DONE);
		model.upd();
		GsPub.deleteStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsSplitLine.class), Gs.OEnrouteType.QTZT);
	}

	@Override
	public void inCancel(GsIn in, GsSplit model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.DONE);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		GsPub.insertStimate(model, model.gtWarehouse(), Idu.getLinesTid(model, GsSplitLine.class), Gs.OEnrouteType.QTZT, null);
	}

	@Override
	public void outOk(GsOut out, GsSplit model) {
		Idu.assertStatusOther(model, Sys.OBillStatus.CHECKED);
		model.stStatus(Sys.OBillStatus.INOUT);
		model.upd();
		List<GsSplitLine> mlines = new ArrayList();
		GsSplitLine mline = new GsSplitLine();
		mline.setGoods(model.getGoods());
		mline.setUom(model.getUom());
		mline.setQty(model.getQty());
		mlines.add(mline);
		GsPub.deleteStimate(model, model.gtWarehouse(), mlines, Gs.OEnrouteType.QTSD);
	}

	@Override
	public void outCancel(GsOut out, GsSplit model) {
//		Idu.assertStatusOther(model, Sys.OBillStatus.INOUT);
		if (model.getStatus() != Sys.OBillStatus.INOUT.getLine().getKey())
			throw LOG.err("assertStatus", model.gtTB().getName() + "[" + model.getCode() + "]对应的入库单已审核，不可操作！");
		model.stStatus(Sys.OBillStatus.CHECKED);
		model.upd();
		List<GsSplitLine> mlines = new ArrayList();
		GsSplitLine mline = new GsSplitLine();
		mline.setGoods(model.getGoods());
		mline.setUom(model.getUom());
		mline.setQty(model.getQty());
		mlines.add(mline);
		GsPub.insertStimate(model, model.gtWarehouse(), mlines, Gs.OEnrouteType.QTSD, null);
	}
	
	@Override
	public List<IGoods> getInLines(GsSplit model, int idx, int count) {
		return Idu.getLinesTid(model, GsSplitLine.class, idx, count);
	}
	
	@Override
	public int getInLinesCount(GsSplit model) {
		return Idu.getLinesTidCount(model, GsSplitLine.class);
	}
	
	@Override
	public List<IGoods> getOutLines(GsSplit model, int idx, int count) {
		List<IGoods> list = new ArrayList<IGoods>();
		GsSplitLine line = new GsSplitLine();
		line.setGoods(model.getGoods());
		line.setQty(model.getQty());
		line.setUom(model.getUom());
		list.add(line);
		return list;
	}
	
	@Override
	public int getOutLinesCount(GsSplit model) {
		return 1;
	}
}
