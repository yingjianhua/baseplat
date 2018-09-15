package irille.gl.gs;

import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GsPhyinvDAO {
	public static final Log LOG = new Log(GsPhyinvDAO.class);
	
	public static class Ins extends IduInsLines<Ins, GsPhyinv, GsPhyinvGoodsLine> {
		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "单据货物信息不可为空");
			for (GsPhyinvGoodsLine line : getLines()) {
				int i = 0;
				if (line == null || line.getGoods() == null)
					throw LOG.err("errLine", "请完善单据货物明细信息!");
				for (GsPhyinvGoodsLine rline : getLines())
					if (line.getGoods().equals(rline.getGoods()))
						i++;
				if (i > 1)
					throw LOG.err("errRepeat", "货物[{0}]重复，请完善单据货物明细信息！", line.gtGoods().getCode());
			}
			getB().setCode(SysSeqDAO.getSeqnum(GsRequest.TB, getB().gtWarehouse().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setOrg(getB().gtWarehouse().getOrg());
			getB().setDept(getB().getWarehouse());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getSystemTime());
			getB().setUnmatchNum(0);
			getB().setUnmatchAmt(BigDecimal.ZERO);
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
			insLineTid(getB(), crtBatchLines(getB(), getLines()));
		}

	}

	public static class Upd extends IduUpdLines<Upd, GsPhyinv, GsPhyinvGoodsLine> {

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err("noLines", "单据货物信息不可为空");
			for (GsPhyinvGoodsLine line : getLines()) {
				int i = 0;
				if (line == null || line.getGoods() == null)
					throw LOG.err("errLine", "请完善单据货物明细信息!");
				for (GsPhyinvGoodsLine rline : getLines())
					if (line.getGoods().equals(rline.getGoods()))
						i++;
				if (i > 1)
					throw LOG.err("errRepeat", "货物[{0}]重复，请完善单据货物明细信息！", line.gtGoods().getCode());
			}
			GsPhyinv mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			GsWarehouse warehouse = mode.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			PropertyUtils.copyProperties(mode, getB(), GsPhyinv.T.WAREHOUSE, GsPhyinv.T.PLAN_FINI_DATE, GsPhyinv.T.COUNTED_BY, GsPhyinv.T.REM);
			setB(mode);
			updLineTid(getB(), getLines(), GsPhyinvGoodsLine.class);
			updLineTid(getB(), crtBatchLines(getB(), getLines()), GsPhyinvBatchLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsPhyinv> {

		@Override
		public void before() {
			super.before();
			GsPhyinv phyinv = loadThisBeanAndLock();
			assertStatusIsInit(phyinv);
			delLineTid(getB(), GsPhyinvGoodsLine.class);
			delLineTid(getB(), GsPhyinvBatchLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsPhyinv> {

		@Override
		public void run() {
			super.run();
			GsPhyinv phyinv = loadThisBeanAndLock();
			assertStatusIsInit(phyinv);
			phyinv.stStatus(STATUS.CHECKED);
			phyinv.setApprBy(getUser().getPkey());
			phyinv.setApprTime(Env.INST.getWorkDate());
			phyinv.upd();
			setB(phyinv);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsPhyinv> {

		@Override
		public void run() {
			super.run();
			GsPhyinv phyinv = loadThisBeanAndLock();
			assertStatusIsCheck(phyinv);
			phyinv.stStatus(STATUS_INIT);
			phyinv.setApprBy(null);
			phyinv.setApprTime(null);
			phyinv.upd();
			setB(phyinv);
		}
	}
	
	public static class Produce extends IduOther<Produce, GsPhyinv> {
		@Override
		public void run() {
			super.run();
			assertStatusIsCheck(getB());
			GsPhyinv phyinv = loadThisBeanAndLock();
			List<GsPhyinvBatchLine> list = Idu.getLinesTid(phyinv, GsPhyinvBatchLine.class);
			for (GsPhyinvBatchLine line : list) {
				if (line.getCountQty() == null)
					throw LOG.err("null", "货物{0}未盘点登记，不可产生盘盈盘亏单!", line.gtGoods().getName());
			}
			phyinv.stStatus(STATUS.DONE);
			phyinv.upd();
			setB(phyinv);
			List<GsGainLine> gainList = new ArrayList<GsGainLine>();
			List<GsLossLine> lossList = new ArrayList<GsLossLine>();
			for (GsPhyinvBatchLine line : list) {
				if (line.getCountQty().subtract(line.getQty()).compareTo(BigDecimal.ZERO) == 1) {
					//盘盈单明细
					GsGainLine gainLine = new GsGainLine();
					gainLine.setGoods(line.getGoods());
					gainLine.setUom(line.getUom());
					gainLine.setQty(line.getDiffQty());
					gainList.add(gainLine);
				} else if (line.getCountQty().subtract(line.getQty()).compareTo(BigDecimal.ZERO) == -1) {
					//盘亏单明细
					GsLossLine lossLine = new GsLossLine();
					lossLine.setGoods(line.getGoods());
					lossLine.setUom(line.getUom());
					lossLine.setQty(line.getDiffQty());
					lossList.add(lossLine);
				}
			}
			if (gainList.size() > 0) {
				//产生盘盈单
				GsGain gain = new GsGain().init();
				gain.setCode(SysSeqDAO.getSeqnumForm(GsGain.TB));
				gain.stStatus(STATUS_INIT);
				gain.setOrg(getUser().getOrg());
				gain.setDept(getUser().getDept());
				gain.setOrigForm(getB().gtLongPkey());
				gain.setOrigFormNum(getB().getCode());
				gain.setWarehouse(getB().getWarehouse());
				GsGainDAO.Ins act = new GsGainDAO.Ins();
				act.setB(gain).setLines(gainList);
				act.commit();
			}
			if (lossList.size() > 0) {
				//产生盘亏单
				GsLoss loss = new GsLoss().init();
				loss.setCode(SysSeqDAO.getSeqnumForm(GsLoss.TB));
				loss.stStatus(STATUS_INIT);
				loss.setOrg(getUser().getOrg());
				loss.setDept(getUser().getDept());
				loss.setOrigForm(getB().gtLongPkey());
				loss.setOrigFormNum(getB().getCode());
				loss.setWarehouse(getB().getWarehouse());
				GsLossDAO.Ins act = new GsLossDAO.Ins();
				act.setB(loss).setLines(lossList);
				act.commit();
			}
		}
	}
	
	public static class Inv extends IduOther<Inv, GsPhyinv> {
		
		private List<GsPhyinvBatchLine> _batchLines;

		public List<GsPhyinvBatchLine> getBatchLines() {
			return _batchLines;
		}

		public void setBatchLines(List<GsPhyinvBatchLine> batchLines) {
			_batchLines = batchLines;
		}
		@Override
		public void run() {
			super.run();
			GsPhyinv phyinv = loadThisBeanAndLock();
			assertStatusIsCheck(phyinv);
			int un = 0;
			for (GsPhyinvBatchLine line : getBatchLines()) {
				if (line.getCountQty() == null)
					continue;
				if (line.getCountQty().compareTo(BigDecimal.ZERO) == -1)
					throw LOG.err("negative", "货物{0}实际数量不可为负数!", line.gtGoods().getName());
				line.setDiffQty(line.getCountQty().subtract(line.getQty()).abs());
				line.setDiffAmt(BigDecimal.ZERO);//TODO 待成本模块
				if (line.getDiffQty().compareTo(BigDecimal.ZERO) == 1)
					un++;
			}
			phyinv.setUnmatchNum(un);
			phyinv.setUnmatchAmt(BigDecimal.ZERO);//TODO 待成本模块
			phyinv.upd();
			setB(phyinv);
			Idu.updLineTid(getB(), getBatchLines(), GsPhyinvBatchLine.class);
		}
	}
	
	private static List<GsPhyinvBatchLine> crtBatchLines(GsPhyinv model, List<GsPhyinvGoodsLine> goodsLines) {
		List<GsPhyinvBatchLine> pblist = new ArrayList<GsPhyinvBatchLine>();
		for (GsPhyinvGoodsLine line : goodsLines) {
			GsGoods goods = line.gtGoods();
			if (goods.isWork())
				continue;
			GsStock stock = GsStockDAO.getStockOrCrt(model.gtWarehouse(), goods, false);
			List<GsStockBatch> batchs = GsStockBatchDAO.getStockBatchByStock(stock, false);
			if (batchs == null || batchs.size() == 0) {
				GsStockBatch sb = GsStockBatchDAO.createAuto(stock, null);
				GsPhyinvBatchLine pb = new GsPhyinvBatchLine().init();
				pb.stGoods(goods);
				pb.stBatch(sb);
				pb.setUom(goods.getUom());
				pb.setQty(BigDecimal.ZERO);
				pblist.add(pb);
				continue;
			}
			for (GsStockBatch batch : batchs) {
				GsPhyinvBatchLine pb = new GsPhyinvBatchLine().init();
				pb.stGoods(goods);
				pb.setLocation(stock.getLocation());
				pb.stBatch(batch);
				pb.setUom(goods.getUom());
				pb.setQty(batch.getQty());
				pblist.add(pb);
			}
		}
		return pblist;
	}

}
