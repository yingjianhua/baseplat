package irille.pss.sal;

import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPriceCtl;
import irille.gl.gs.GsPriceCtlDAO;
import irille.gl.gs.GsPriceGoodsCell;
import irille.gl.gs.GsPriceGoodsCellDAO;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.idu.Idu;

import java.math.BigDecimal;

public class SalPriceProtMvDAO {
	public static final Log LOG = new Log(SalPriceProtMvDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFound("[{0}]对应的[{1}]不存在！"),
		lowPrice("货物[{0}]价格低于[{1} : {2}]！");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	//调拨参数暂时未使用 TODO 
	public static BigDecimal getPrice(SysCell cell, GsGoods goods) {
		Byte level = 0;
		if (goods.isWork())
			return BigDecimal.ZERO;
		GsPriceCtl priceCtl = GsPriceCtlDAO.getPriceCtrl(cell);//定价控制中所属核算单元的默认销售级别
		level = priceCtl.getMvLevel();
		GsPriceGoodsCell priceGoodsCell = GsPriceGoodsCellDAO.getAutoCreate(cell, goods, priceCtl.gtPrice());
		return priceGoodsCell.gtPrice(level);
	}

}
