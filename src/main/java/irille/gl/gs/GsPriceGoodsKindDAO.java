package irille.gl.gs;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;

public class GsPriceGoodsKindDAO {
	public static final Log LOG = new Log(GsPriceGoodsKindDAO.class);

	public static class Ins extends IduIns<Ins, GsPriceGoodsKind> {

		@Override
		public void before() {
			super.before();
			/*for (int i = 1; i <= 12; i++) {
				if (getB().gtRate(i) != null) {
					if (getB().gtRate(i).compareTo(BigDecimal.ZERO) == -1)
						throw LOG.err("error", "利润率的值不可为负数！！");
				}else{
					getB().stRate(i, new BigDecimal(0));					
				}
			}*/
			getB().stEnabled(true);
		}

	}

	public static class Upd extends IduUpd<Upd, GsPriceGoodsKind> {
		@Override
		public void before() {
			super.before();
			
			GsPriceGoodsKind model = loadThisBeanAndLock();
			PropertyUtils.copyPropertiesWithout(model, getB(), GsPriceGoodsKind.T.ENABLED);
			setB(model);
		}
	}

	public static class Del extends IduDel<Del, GsPriceGoodsKind> {

		public void valid() {
			super.valid();
			haveBeUsed(GsPriceGoods.class, GsPriceGoods.T.PRICE_KIND, b.getPkey());
		}
	}

}
