package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSeqDAO;
import irille.pss.pur.PurProtGoods;
import irille.pub.JsonTools;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import org.json.JSONArray;

public class GsGoodsDAO {
	public static final Log LOG = new Log(GsGoodsDAO.class);

	public static JSONArray autoComplete(int idx, int count, String code, String diy) {
		StringBuffer sql = new StringBuffer(Idu.sqlString("select pkey,code,name,spec from {0} where {1}={2}",
		    GsGoods.class, GsGoods.T.ENABLED, Sys.OEnabled.TRUE));
		if (Str.isEmpty(code) == false) {
			sql.append(" and (code like '%" + code + "%' OR name like '%" + code + "%' OR spec like '%" + code + "%')");
		}
		if (Str.isEmpty(diy) == false)
			sql.append(" and " + diy);
		sql.append(" order by code");
		return JsonTools.mapToArray(BeanBase.executeQueryMap(BeanBase.getPageSql(sql.toString(), false, idx, count)));
	}

	private static void initSpec(GsGoods goods) {
		StringBuffer buf = new StringBuffer();
		if (Str.isEmpty(goods.gtKind().getCust1()))
			return;
		if (Str.isEmpty(goods.getCust1()) == false)
			buf.append(" | " + goods.getCust1());
		if (Str.isEmpty(goods.getCust2()) == false)
			buf.append(" | " + goods.getCust2());
		if (Str.isEmpty(goods.getCust3()) == false)
			buf.append(" | " + goods.getCust3());
		if (Str.isEmpty(goods.getCust4()) == false)
			buf.append(" | " + goods.getCust4());
		if (Str.isEmpty(goods.getCust5()) == false)
			buf.append(" | " + goods.getCust5());
		if (Str.isEmpty(buf.toString()) == false)
			goods.setSpec(buf.toString().substring(3));
	}

	//	public static long addCheckSumL(long key) {
	//		int[] iPrime = new int[] { 11, 13, 17, 19, 23, 29, 31, 37, 41 }; /* 质数 */
	//		char[] s = String.valueOf(key).toCharArray();
	//		char[] m = new char[1];
	//		int cs = 0;
	//		for (int i = 0; i < ((s.length + 1) / 2); i++) {
	//			m[0] = s[i];
	//			cs += Integer.parseInt(String.valueOf(i * 2 + 1 >= s.length ? s[i * 2]
	//					: s[i * 2] * 10 + s[i * 2 + 1]))
	//					* iPrime[i];
	//		}
	//		cs %= 100;
	//		return key * 100 + cs;
	//	}

	public static class Ins extends IduIns<Ins, GsGoods> {

		public void before() {
			super.before();
			if (getB().getPurLeadDays() < 0)
				throw LOG.err("err", "采购提前天数不能为负数");
			if (Str.isEmpty(getB().getCode()) == false) {
				if (GsGoods.chkUniqueCode(false, getB().getCode()) != null)
					throw LOG.err("hasGoods", "货物代码[{0}]已存在，请重新输入!", getB().getCode());
			} else {
				while (true) {
					String seqnum = SysSeqDAO.getSeqnumInt(GsGoods.TB) + "";
					if (GsGoods.chkUniqueCode(false, seqnum) != null)
						continue;
					getB().setCode(seqnum);
					break;
				}
			}
			getB().stEnabled(true);
			initSpec(getB());
		}

	}

	public static class Upd extends IduUpd<IduUpd, GsGoods> {

		public void before() {
			super.before();
			if (getB().getPurLeadDays() < 0)
				throw LOG.err("err", "采购提前天数不能为负数");
			GsGoods good = load(getB().getPkey());
			if (good.getCode().equals(getB().getCode()) == false)
				if (GsGoods.chkUniqueCode(false, getB().getCode()) != null)
					throw LOG.err("hasGoods", "货物代码[{0}]已存在，请重新输入!", getB().getCode());
			PropertyUtils.copyPropertiesWithout(good, getB(), GsGoods.T.PKEY, GsGoods.T.UOM, GsGoods.T.ENABLED);
			setB(good);
			initSpec(getB());
		}

	}

	public static class Del extends IduDel<Del, GsGoods> {

		public void valid() {
			super.valid();
			haveBeUsed(GsStock.class, GsStock.T.GOODS, b.getPkey());
			haveBeUsed(GsPriceGoods.class, GsPriceGoods.T.GOODS, b.getPkey());
			haveBeUsed(PurProtGoods.class, PurProtGoods.T.GOODS, b.getPkey());
		}

	}

}
