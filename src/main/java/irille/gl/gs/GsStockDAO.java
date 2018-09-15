package irille.gl.gs;

import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.pur.PurRevLine;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.List;

public class GsStockDAO {
	public static final Log LOG = new Log(GsStockDAO.class);

	//取仓库存货档案
	public static GsStock getStockOrCrt(GsWarehouse ware, GsGoods goods, boolean lock) {
		GsStock mode = GsStock.chkUniqueWg(lock, ware.getPkey(), goods.getPkey());
		if (mode != null)
			return mode;
		return createAuto(ware, goods);
	}

	//自动新建存货档案
	private static GsStock createAuto(GsWarehouse ware, GsGoods goods) {
		GsStock mode = new GsStock().init();
		mode.stGoods(goods);
		mode.stWarehouse(ware);
		mode.stCell(ware.gtCell());
		mode.ins();
		return mode;
	}

	/**
	 * 出库检查是否有足够的货物数量
	 * @param ware 仓库
	 * @param goods 货物
	 * @param qty 默认数量
	 */
	public static void checkQtyFact(GsWarehouse ware, GsGoods goods, BigDecimal qty) {
		//		if (goods.forceWork()) TODO
		//			return;
		if (goods.isWork())
			return;
		GsStock storage = getStockOrCrt(ware, goods, false);
		if (storage.getQty().compareTo(qty) == -1)
			throw LOG.err("notEnouth", "货物[{0}-{1}]数量不足，无法满足出库!", goods.getCode(), goods.getName());
	}

	/**
	 * 更新存货档案中的锁定数量或在途数量
	 * @param ware 仓库
	 * @param lines 货物明细
	 * @param etype 出入类型
	 * @param addFlag 加减标识
	 */
	public static void updateStQty(GsWarehouse ware, List<IGoods> lines, OEnrouteType etype, boolean addFlag) {
		GsStock mode;
		for (IGoods line : lines) {
			//			if (line.gtGoods().forceWork()) TODO 劳务与现在费用分摊的问题，这里暂时不考虑
			//				continue;
			if (line.gtGoods().isWork())
				continue;
			mode = getStockOrCrt(ware, line.gtGoods(), true);
			mode.assertEnabled();
			if (etype.getLine().getKey() >= 50) { //销售等待出库情况
				if (addFlag)
					mode.setLockedQty(mode.getLockedQty().add(line.getDefaultUomQty().abs()));
				else
					mode.setLockedQty(mode.getLockedQty().subtract(line.getDefaultUomQty().abs()));
				if (mode.getLockedQty().signum() < 0)
					throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]的锁定数量不可为负数", mode.gtWarehouse().gtDept().getName(), mode.gtGoods()
					    .getErrMsg());
				if (!mode.gtGoods().gtZeroOutFlag()) {
					if (mode.getQty().add(mode.getEnrouteQty()).subtract(mode.getLockedQty()).signum() == -1)//检查是否有足够的库存
						throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]不允许零出库，可用数量不足[{2}]", mode.gtWarehouse().gtDept().getName(), mode
						    .gtGoods().getErrMsg(), line.getDefaultUomQty().abs());
				}
			} else { //采购等待入库情况
				if (addFlag)
					mode.setEnrouteQty(mode.getEnrouteQty().add(line.getDefaultUomQty().abs()));
				else
					mode.setEnrouteQty(mode.getEnrouteQty().subtract(line.getDefaultUomQty().abs()));
				if (mode.getEnrouteQty().signum() < 0)
					throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]的在途数量不可为负数", mode.gtWarehouse().gtDept().getName(), mode.gtGoods()
					    .getErrMsg());
			}
			mode.upd();
		}
	}
	/**
	 * 更新存货档案中的锁定数量或在途数量
	 * @param ware 仓库
	 * @param lines 货物明细
	 * @param etype 出入类型
	 * @param addFlag 加减标识
	 */
	public static void updateStQtyByPurRev(GsWarehouse ware, List<PurRevLine> lines, OEnrouteType etype, boolean addFlag) {
		GsStock mode;
		for (PurRevLine line : lines) {
			//			if (line.gtGoods().forceWork()) TODO 劳务与现在费用分摊的问题，这里暂时不考虑
			//				continue;
			if (line.gtGoods().isWork())
				continue;
			mode = getStockOrCrt(ware, line.gtGoods(), true);
			mode.assertEnabled();
			if (etype.getLine().getKey() >= 50) { //销售等待出库情况
				if (addFlag)
					mode.setLockedQty(mode.getLockedQty().add(line.gtUom().tranQty(line.gtGoods().gtUom(), line.getQty()).abs()));
				else
					mode.setLockedQty(mode.getLockedQty().subtract(line.gtUom().tranQty(line.gtGoods().gtUom(), line.getQty()).abs()));
				if (mode.getLockedQty().signum() < 0)
					throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]的锁定数量不可为负数", mode.gtWarehouse().gtDept().getName(), mode.gtGoods()
							.getErrMsg());
				if (!mode.gtGoods().gtZeroOutFlag()) {
					if (mode.getQty().add(mode.getEnrouteQty()).subtract(mode.getLockedQty()).signum() == -1)//检查是否有足够的库存
						throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]不允许零出库，可用数量不足[{2}]", mode.gtWarehouse().gtDept().getName(), mode
								.gtGoods().getErrMsg(), line.getDefaultUomQty().abs());
				}
			} else { //采购等待入库情况
				if (addFlag)
					mode.setEnrouteQty(mode.getEnrouteQty().add(line.gtUom().tranQty(line.gtGoods().gtUom(), line.getQty()).abs()));
				else
					mode.setEnrouteQty(mode.getEnrouteQty().subtract(line.gtUom().tranQty(line.gtGoods().gtUom(), line.getQty()).abs()));
				if (mode.getEnrouteQty().signum() < 0)
					throw LOG.err("notEnough", "仓库[{0}]中货物[{1}]的在途数量不可为负数", mode.gtWarehouse().gtDept().getName(), mode.gtGoods()
							.getErrMsg());
			}
			mode.upd();
		}
	}

	public static class Ins extends IduIns<Ins, GsStock> {
		public void before() {
			super.before();
			if(GsStock.chkUniqueWg(false, getB().getWarehouse(), getB().getGoods())!=null)
				throw LOG.err("err","仓库中已存在此货物!");
			if(getB().getLowestQty().intValueExact()==-1)
				throw LOG.err("err","最低库存不能为负数!");
			if(getB().getSafetyQty().intValueExact()==-1)
				throw LOG.err("err","安全库存不能为负数!");
			if(getB().getLimitQty().intValueExact()==-1)
				throw LOG.err("err","上限库存不能为负数!");
			if(getB().getPurLeadDays()<0)
				throw LOG.err("err","采购提前天数不能为负数!");
			getB().stEnabled(true);
			getB().setCell(getB().gtWarehouse().getCell());
			getB().setQty(BigDecimal.ZERO);
			getB().setEnrouteQty(BigDecimal.ZERO);
			getB().setLockedQty(BigDecimal.ZERO);
		}
	}

	public static class Upd extends IduUpd<Upd, GsStock> {

		public void before() {
			super.before();
			if(getB().getLowestQty().intValueExact()==-1)
				throw LOG.err("err","最低库存不能为负数!");
			if(getB().getSafetyQty().intValueExact()==-1)
				throw LOG.err("err","安全库存不能为负数!");
			if(getB().getLimitQty().intValueExact()==-1)
				throw LOG.err("err","上限库存不能为负数!");
			if(getB().getPurLeadDays()<0)
				throw LOG.err("err","采购提前天数不能为负数!");
			GsStock stock = loadThisBeanAndLock();
			PropertyUtils.copyPropertiesWithout(stock, getB(), GsStock.T.ENABLED, GsStock.T.WAREHOUSE, GsStock.T.GOODS,
			    GsStock.T.QTY, GsStock.T.CELL,GsStock.T.ENROUTE_QTY, GsStock.T.LOCKED_QTY);
			setB(stock);
		}
	}

	public static class Del extends IduDel<Del, GsStock> {

		@Override
		public void before() {
			super.before();
			GsStockLine.TB.getName(); //静态块循环异常
			if (Idu.getLinesCount(GsStockLine.T.STOCK.getFld(), getB().getPkey()) > 0)
				throw LOG.err("haveLines", "存货[{0}]已存在明细，不能删除", getB().gtGoods().getErrMsg());
			if (Idu.getLinesCount(GsStockBatch.T.STOCK.getFld(), getB().getPkey()) > 0)
				throw LOG.err("haveBatch", "存货[{0}]已存在存货批次，不能删除", getB().gtGoods().getErrMsg());
		}

	}

}
