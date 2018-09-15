package irille.pss.sal;

import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPriceCtl;
import irille.gl.gs.GsPriceCtlDAO;
import irille.gl.gs.GsPriceGoodsCell;
import irille.gl.gs.GsPriceGoodsCellDAO;
import irille.gl.gs.Gs.OType;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoodsPrice;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.List;

public class SalPriceProtDAO {
	public static final Log LOG = new Log(SalPriceProtDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFound("[{0}]对应的[{1}]不存在！"),
		lowPrice("货物[{0}]价格低于[{1} : {2}]！");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static BigDecimal getPrice(SysCustom custom, GsGoods goods) {
		return getPrice(Idu.getCell(), custom, goods);
	}

	/**
	 * 存在销售价格协议则取协议的级别，不存在则取货物-定价控制中所属核算单元的默认销售级别
	 * 取核算单元价格信息记录，返回对应级别的价格
	 * 注意，无定制控制或无核算单元价格信息都抛出异常
	 * @param cell 核算单元
	 * @param custom 客户对象
	 * @param goods 货物对象
	 * @return 价格
	 */
	public static BigDecimal getPrice(SysCell cell, SysCustom custom, GsGoods goods) {
		Byte level = 0;
		if (goods.isWork())
			return BigDecimal.ZERO;
		GsPriceCtl priceCtl = GsPriceCtlDAO.getPriceCtrl(cell);//定价控制中所属核算单元的默认销售级别
		level = priceCtl.getRetailLevel();
		SalPriceProt priceProt = SalPriceProt.chkUniqueCellCust(false, cell.getPkey(), custom.getPkey());
		if (priceProt != null)
			level = priceProt.getPriceLevel();//存在销售价格协议则取协议的级别
		GsPriceGoodsCell priceGoodsCell = GsPriceGoodsCellDAO.getAutoCreate(cell, goods, priceCtl.gtPrice());
		return priceGoodsCell.gtPrice(level);
	}

	/**
	 * 销售单据审核时，检查其价格的合理性
	 * 1.存在客户协议，并不小于客户折扣价则正常
	 * 2.如果低于最低零售价，则抛出异常
	 * 3.如果低于业务员折扣价，则抛出异常，根据SalDiscountPriv中的操作员折扣级别取价格
	 * @param custom
	 * @param list
	 */
	public static void checkPrice(SysCell cell, SysCustom custom, List<IGoodsPrice> list) {
		//如果低于最低零售价，则抛出异常
		GsPriceCtl priceCtl = GsPriceCtlDAO.getPriceCtrl(cell);
		for (IGoodsPrice line : list) {
			if (line.gtGoods().isWork())
				continue;
			GsPriceGoodsCell priceGoodsCell = GsPriceGoodsCellDAO.getAutoCreate(cell, line.gtGoods(), priceCtl.gtPrice());
			//存在客户协议，并不小于客户折扣价则正常
			SalPriceProt prot = SalPriceProt.chkUniqueCellCust(false, cell.getPkey(), custom.getPkey());
			if (prot != null) {
				if (line.getPrice().compareTo(priceGoodsCell.gtPrice(prot.getPriceLevel())) >= 0)
					continue;
			}

			BigDecimal price = priceGoodsCell.gtPrice(priceCtl.getLowestLevel());
			if (line.getPrice().compareTo(price) == -1)
				throw LOG.err(Msgs.lowPrice, line.gtGoods().getName(), "最低零售价", price);
			//如果低于业务员折扣价，则抛出异常，根据SalDiscountPriv中的操作员折扣级别取价格
			SalDiscountPriv priv = BeanBase.chk(SalDiscountPriv.class, Idu.getUser().getPkey());
			if (priv == null)
				throw LOG.err(Msgs.notFound, Idu.getUser().getName(), SalDiscountPriv.TB.getName());
			price = priceGoodsCell.gtPrice(priv.getDiscountLevel());
			if (line.getPrice().compareTo(price) == -1)
				throw LOG.err(Msgs.lowPrice, line.gtGoods().getCode()+":"+line.gtGoods().getName(), "业务员折扣价", price);
		}
	}

	public static class Ins extends IduIns<Ins, SalPriceProt> {
	}

	public static class Upd extends IduUpd<Upd, SalPriceProt> {
	}

	public static class Del extends IduDel<Del, SalPriceProt> {
	}

}
