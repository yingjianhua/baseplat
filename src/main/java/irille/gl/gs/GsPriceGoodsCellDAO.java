package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.List;

public class GsPriceGoodsCellDAO {
	public static final Log LOG = new Log(GsPriceGoodsCellDAO.class);

	//当核算价格信息不存时，系统自动从基础价格信息里拷贝新增
	public static GsPriceGoodsCell getAutoCreate(SysCell cell, GsGoods goods, GsPrice price) {
		GsPriceGoodsCell cc = GsPriceGoodsCell.chkUniqueCellGoods(false, cell.getPkey(), goods.getPkey(), price.getPkey());
		if (cc == null) {
			GsPriceGoods pg = GsPriceGoods.chkUniqueGoodsPrice(false, goods.getPkey(), price.getPkey());
			if (pg == null)
				throw LOG.err("noPg", "货物[{0}]的基础价格信息未定义", goods.getCode() + ":" + goods.getName());
			cc = new GsPriceGoodsCell().init();
			cc.setPriceGoods(pg.getPkey());
			cc.setPriceName(pg.getPriceName());
			cc.setGoods(pg.getGoods());
			cc.setCell(cell.getPkey());
			cc.setPriceCost(pg.getPriceCost());
			cc.setPrice1(pg.getPrice1());
			cc.setPrice2(pg.getPrice2());
			cc.setPrice3(pg.getPrice3());
			cc.setPrice4(pg.getPrice4());
			cc.setPrice5(pg.getPrice5());
			cc.setPrice6(pg.getPrice6());
			cc.setPrice7(pg.getPrice7());
			cc.setPrice8(pg.getPrice8());
			cc.setPrice9(pg.getPrice9());
			cc.setPrice10(pg.getPrice10());
			cc.setPrice11(pg.getPrice11());
			cc.setPrice12(pg.getPrice12());
			cc.ins();
		}
		return cc;
	}

	//由基础价格更改后调用
	public static void syncLines(GsPriceGoods pg) {
		List<GsPriceGoodsCell> list = Idu.getLines(GsPriceGoodsCell.T.PRICE_GOODS, pg.getPkey());
		for (GsPriceGoodsCell line : list) {
			if (line.gtSyncFlag() == false)
				continue;
			line.setPriceName(pg.getPriceName());
			line.setGoods(pg.getGoods());
			line.setPriceCost(pg.getPriceCost());
			line.setPrice1(pg.getPrice1());
			line.setPrice2(pg.getPrice2());
			line.setPrice3(pg.getPrice3());
			line.setPrice4(pg.getPrice4());
			line.setPrice5(pg.getPrice5());
			line.setPrice6(pg.getPrice6());
			line.setPrice7(pg.getPrice7());
			line.setPrice8(pg.getPrice8());
			line.setPrice9(pg.getPrice9());
			line.setPrice10(pg.getPrice10());
			line.setPrice11(pg.getPrice11());
			line.setPrice12(pg.getPrice12());
			line.upd();
		}
	}

	public static class Ins extends IduIns<Ins, GsPriceGoodsCell> {
		@Override
		public void before() {
			super.before();
			getB().stEnabled(true);
			getB().setPriceName(getB().gtPriceGoods().getPriceName());
			if (GsPriceGoodsCell.chkUniqueCellGoods(false, getB().getCell(), getB().getGoods(), getB().getPriceName()) != null)
				throw LOG.err("exist", "核算单元【{0}】，货物【{1}】的核算单元价格信息已存在，不可新增！", getB().gtCell().getName(), getB().gtGoods()
				    .getName());
		}
	}

	public static class Upd extends IduUpd<Upd, GsPriceGoodsCell> {
		@Override
		public void before() {
			super.before();
			GsPriceGoodsCell dbBean = loadThisBeanAndLock();
			if (getB().getPriceCost().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("error", "定价基数的值不可为负数！！");
			for (int i = 1; i <= 12; i++) {
				if (getB().gtPrice(i) != null) {
					if (getB().gtPrice(i).compareTo(BigDecimal.ZERO) == -1)
						throw LOG.err("error", "价格的值不可为负数！！");
				}
			}
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), GsPriceGoodsCell.T.ENABLED, GsPriceGoodsCell.T.PRICE_GOODS,
			    GsPriceGoodsCell.T.PRICE_NAME, GsPriceGoodsCell.T.GOODS, GsPriceGoodsCell.T.CELL);
			setB(dbBean);
		}
	}

	public static class Del extends IduDel<Del, GsPriceGoodsCell> {

	}

}
