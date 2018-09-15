package irille.pss.sal;

import irille.core.sys.SysCustom;
import irille.core.sys.SysOrg;
import irille.pub.bean.IGoodsPrice;
import irille.pub.svr.Env;

import java.util.List;

public class SalCustGoodsDAO {

	/**
	 * 更新客户的价格信息
	 * @param org 机构
	 * @param cust 客户
	 * @param lines 货物价格对象集合
	 */
	public static void updCustGoods(SysOrg org, SysCustom cust, List<IGoodsPrice> lines) {
		SalCustGoods cg;
		for (IGoodsPrice line : lines) {
			if ((cg = SalCustGoods.chkUniqueCustGoods(true, org.getPkey(), cust.getPkey(),
			    line.getGoods())) == null) {
				cg = new SalCustGoods();
				cg.stOrg(org);
				cg.stCust(cust);
				cg.stGoods(line.gtGoods());
				cg.setLatestPrice(line.getPrice());
				cg.ins();
			} else {
				cg.stGoods(line.gtGoods());
				cg.setLatestPrice(line.getPrice());
				cg.setLatestDate(Env.INST.getWorkDate());
				cg.upd();
			}
		}
	}
}
