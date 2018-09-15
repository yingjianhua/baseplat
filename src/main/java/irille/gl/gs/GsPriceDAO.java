package irille.gl.gs;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class GsPriceDAO {
	public static final Log LOG = new Log(GsPriceDAO.class);

	public static class Ins extends IduIns<Ins, GsPrice> {

		@Override
		public void before() {
			super.before();
			if (Str.isEmpty(getB().gtNamePrice(1)))
				throw LOG.err("null","请至少填写一个价格名称！");
			if (isNotInProperOrder(getB()))
				throw LOG.err("notInProperOrder", "价格名称没有按连续顺序填写，无法新增！");
		}
	}
	
	public static class Upd extends IduUpd<Upd, GsPrice> {
		@Override
		public void before() {
			super.before();
			if (Str.isEmpty(getB().gtNamePrice(1)))
				throw LOG.err("null","请至少填写一个价格名称！");
			if (isNotInProperOrder(getB()))
				throw LOG.err("notInProperOrder", "价格名称没有按连续顺序填写，无法修改！");
		}
	}

	public static class Del extends IduDel<Del, GsPrice> {

	}
	
	/**
	 * 判断价格名称是否按顺序填写
	 * @param price
	 * @return true 按连续顺序, false 没有按连续顺序
	 */
	public static boolean isNotInProperOrder(GsPrice price) {
		String order = "";
		for (int i = 1; i < 13; i++) {
			if (Str.isEmpty(price.gtNamePrice(i)))
				order += "0";
			else
				order += "1";
		}
		return order.contains("01");
	}

}
