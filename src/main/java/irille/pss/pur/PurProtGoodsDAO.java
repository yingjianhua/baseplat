package irille.pss.pur;

import irille.pub.Log;

public class PurProtGoodsDAO {
	public static final Log LOG = new Log(PurProtGoodsDAO.class);
	
	public static void checkProtGoods(Integer templat, Integer supplier, Integer goods) {
		PurProtGoods protGoods = PurProtGoods.chkUniqueTempCustObj(false, templat, supplier, goods);
		if (protGoods == null)
			throw LOG.err("ins", "当前模板的供应商货物协议未签订！");
	}
}
