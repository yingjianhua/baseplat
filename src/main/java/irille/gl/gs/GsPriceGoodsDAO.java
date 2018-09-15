package irille.gl.gs;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;

public class GsPriceGoodsDAO {
	public static final Log LOG = new Log(GsPriceGoodsDAO.class);

	public static class Ins extends IduIns<Ins, GsPriceGoods> {

		@Override
		public void before() {
			super.before();
			getB().stEnabled(true);
			if (getB().getPriceCost().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("error", "定价基数的值不可为负数！！");
			for (int i = 1; i <= 12; i++) {
				if (getB().gtPrice(i) != null) {
					if (getB().gtPrice(i).compareTo(BigDecimal.ZERO) == -1)
						throw LOG.err("error", "价格的值不可为负数！！");
				}
			}
			getB().setPriceName(getB().gtPriceKind().getPrice());
			if (GsPriceGoods.chkUniqueGoodsPrice(false, getB().getGoods(), getB().getPriceName()) != null)
				throw LOG.err("exist", "货物【{0}】，定价名称【{1}】的基础价格信息已存在，不可新增！", getB().gtGoods().getName(), getB().gtPriceName()
				    .getName());
		}

	}

	public static class Upd extends IduUpd<Upd, GsPriceGoods> {
		@Override
		public void before() {
			super.before();
			GsPriceGoods dbBean = loadThisBeanAndLock();
			if (getB().getPriceCost().compareTo(BigDecimal.ZERO) == -1)
				throw LOG.err("error", "定价基数的值不可为负数！！");
			for (int i = 1; i <= 12; i++) {
				if (getB().gtPrice(i) != null) {
					if (getB().gtPrice(i).compareTo(BigDecimal.ZERO) == -1)
						throw LOG.err("error", "价格的值不可为负数！！");
				}
			}
			getB().setPriceName(getB().gtPriceKind().getPrice());
			if (getB().getPriceName().equals(dbBean.getPriceName()) == false)
				if (GsPriceGoods.chkUniqueGoodsPrice(false, getB().getGoods(), getB().getPriceName()) != null)
					throw LOG.err("exist", "货物【{0}】，定价名称【{1}】的基础价格信息已存在，不可新增！", getB().gtGoods().getName(), getB().gtPriceName()
					    .getName());
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), GsPriceGoods.T.ENABLED);
			setB(dbBean);
		}

		@Override
		public void after() {
			super.after();
			GsPriceGoodsCellDAO.syncLines(getB());
		}
	}

	public static class Del extends IduDel<Del, GsPriceGoods> {

		public void valid() {
			super.valid();
			haveBeUsed(GsPriceGoodsCell.class, GsPriceGoodsCell.T.PRICE_GOODS, getB().getPkey());
		}
	}

}
