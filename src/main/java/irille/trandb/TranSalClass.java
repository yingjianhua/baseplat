package irille.trandb;

import irille.core.sys.SysOrg;
import irille.core.sys.Sys.ORangeType;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPrice;
import irille.gl.gs.GsPriceCtl;
import irille.gl.gs.GsPriceCtlDAO;
import irille.gl.gs.GsPriceDAO;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsCell;
import irille.gl.gs.GsPriceGoodsDAO;
import irille.gl.gs.GsPriceGoodsKind;
import irille.gl.gs.GsPriceGoodsKindDAO;
import irille.gl.gs.Gs.OPriceOrig;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

/**
 * @author LYL
 * 2015-7-3 20:41:36 
 * 迁移内容：
 * 					一、定价名称：自己定义的
 * 					二、定价控制：循环机构表内的机构创建
 * 					三、基础价格分类：对照sal_price_cls
 * 						  1、code：除去'1_',
 * 							2、定价名称：手写的，
 * 							3、定价基础来源：【成本价自动产生（可维护）】
 * 							4、后面4个价格：对照sal_discount_cls 中的前四个折扣(100+零售价利润率)*折扣价(1-4)/100-100
 * 					四、基础价格信息：sal_price_ctl
 * 							1、基础价格分类：迁移后基础价格分类的code
 * 							2、折扣1-4：定价*(1+折扣(1-4)/100)
 * 					五、核算单元价格信息
 * 							1、和基础价格信息一样根据机构迁移
 */
public class TranSalClass {
	public static void run() throws Exception {
		DelAndAddData();
		// 定价名称
		System.out.println("====================【创建定价名称】================");
		GsPrice gsPrice = new GsPrice().init();
		gsPrice.setName("默认价格");
		gsPrice.stRangeType(ORangeType.GRP);
		gsPrice.setCell(5);
		String[] price = { "最低售价", "零售价", "批发价", "调拨价", "折扣价1", "折扣价2", "折扣价3", "折扣价4" };
		for (int i = 0; i < price.length; i++) {
			gsPrice.stNamePrice(i + 1, price[i]);
		}
		GsPriceDAO.Ins gsPricIns = new GsPriceDAO.Ins();
		gsPricIns.setB(gsPrice);
		gsPricIns.commit();
		System.out.println("======================【完成创建】==================");
		runGsPriceCtl();
		runGsPriceGoodsKind();
		runGsPriceGoods();
	}

	public static void runGsPriceCtl() throws Exception {
		List<SysOrg> list = BeanBase.list(SysOrg.class, null, false);
		System.out.println("====================【创建定价控制】================");
		for (SysOrg sysOrg : list) {
			GsPriceCtl gsPriceCtl = new GsPriceCtl().init();
			gsPriceCtl.setTbObj(sysOrg.gtLongPkey());
			gsPriceCtl.setPrice(1);
			gsPriceCtl.setRetailLevel((byte) 2);
			gsPriceCtl.setLowestLevel((byte) 1);
			gsPriceCtl.setTradeLevel((byte) 3);
			gsPriceCtl.setMvLevel((byte) 4);
			GsPriceCtlDAO.Ins gsPricCtlIns = new GsPriceCtlDAO.Ins();
			gsPricCtlIns.setB(gsPriceCtl);
			gsPricCtlIns.commit();
		}
		System.out.println("======================【完成创建】==================");
	}

	public static void runGsPriceGoodsKind() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `sal_price_cls` a,`sal_discount_cls` b WHERE a.`code` = b.`code`";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("==============迁移【基础价格分类】==============");
		while (rs.next()) {
			String code = rs.getString("a.code");
			code = code.replaceAll("1-", "");
			String name = rs.getString("a.name");
			BigDecimal retail = new BigDecimal(100);
			BigDecimal[] rate = new BigDecimal[8];
			rate[0] = rs.getBigDecimal("a.lowest_price_rate");
			rate[1] = rs.getBigDecimal("a.retail_price_rate");
			rate[2] = rs.getBigDecimal("a.trade_price_rate");
			rate[3] = rs.getBigDecimal("a.move_price_rate");
			rate[4] = rs.getBigDecimal("b.rate1");
			rate[5] = rs.getBigDecimal("b.rate2");
			rate[6] = rs.getBigDecimal("b.rate3");
			rate[7] = rs.getBigDecimal("b.rate4");
			GsPriceGoodsKind gsPriceGoodsKind = new GsPriceGoodsKind().init();
			gsPriceGoodsKind.setCode(code);
			gsPriceGoodsKind.setName(name);
			gsPriceGoodsKind.setPrice(1);
			gsPriceGoodsKind.stPriceOrig(OPriceOrig.COST_UPD);
			gsPriceGoodsKind.stEnabled(true);
			for (int i = 0; i < rate.length; i++) {
				if (i > 3) {
					rate[i] = ((rate[0].add(retail).multiply(rate[i].divide(retail, 4, BigDecimal.ROUND_HALF_DOWN))).subtract(retail)).setScale(4, BigDecimal.ROUND_HALF_DOWN);
					gsPriceGoodsKind.stRate(i + 1, rate[i]);
				} else {
					gsPriceGoodsKind.stRate(i + 1, rate[i]);
				}
			}
			gsPriceGoodsKind.stRangeType(ORangeType.GRP);
			gsPriceGoodsKind.setCell(5);
			GsPriceGoodsKindDAO.Ins gpgki = new GsPriceGoodsKindDAO.Ins();
			gpgki.setB(gsPriceGoodsKind);
			gpgki.commit();
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runGsPriceGoods() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `sal_price_ctl` WHERE org IS NULL";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("==============迁移【基础价格信息】==============");
		while (rs.next()) {
			String goods = rs.getString("goods");
			goods = goods.replaceAll("1-", "");
			String price = rs.getString("price_cls");
			price = price.replaceAll("1-", "");
			BigDecimal retail = new BigDecimal(100);
			BigDecimal[] rate = new BigDecimal[8];
			BigDecimal costPrice = rs.getBigDecimal("cost_price");
			rate[0] = rs.getBigDecimal("lowest_price");
			rate[1] = rs.getBigDecimal("retail_price");
			rate[2] = rs.getBigDecimal("trade_price");
			rate[3] = rs.getBigDecimal("move_price");
			GsPriceGoodsKind gsPriceGoodsKind = GsPriceGoodsKind.chkUniqueCodePrice(false, price, 1);
			if (gsPriceGoodsKind != null) {
				GsGoods gsGoods = GsGoods.chkUniqueCode(false, goods);
				if (gsGoods != null) {
					GsPriceGoods gsPriceGoods = GsPriceGoods.chkUniqueGoodsPrice(false, gsGoods.getPkey(), gsPriceGoodsKind.getPrice());
					if (gsPriceGoods == null) {
						gsPriceGoods = new GsPriceGoods().init();
						gsPriceGoods.setGoods(gsGoods.getPkey());
						gsPriceGoods.setPriceKind(gsPriceGoodsKind.getPkey());
						gsPriceGoods.setPriceName(gsPriceGoodsKind.getPrice());
						gsPriceGoods.setPriceCost(costPrice);
						for (int i = 0; i < rate.length; i++) {
							if (i > 3) {
								rate[i] = costPrice.multiply((gsPriceGoodsKind.gtRate(i)).divide(retail, 4, BigDecimal.ROUND_HALF_DOWN).add(new BigDecimal(1)));
								gsPriceGoods.stPrice(i + 1, rate[i]);
							} else {
								gsPriceGoods.stPrice(i + 1, rate[i]);
							}
						}
						gsPriceGoods.stEnabled(true);
						GsPriceGoodsDAO.Ins gpgd = new GsPriceGoodsDAO.Ins();
						gpgd.setB(gsPriceGoods);
						gpgd.commit();
					} else {
						System.err.println("基础价格信息[" + gsPriceGoods.getPkey() + "]已存在");
					}
				}else {
					System.err.println("货物[" + goods + "]不存在");
				}
			} else {
				System.err.println("基础价格分类[" + price + "]不存在");
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	// public static void runGsPriceGoodsCell() throws Exception {
	// Connection conn = TranDb.INST.getConn();
	// String sql = "SELECT * FROM `sal_price_ctl` WHERE org IS NOT NULL";
	// Statement stat = (Statement) conn.createStatement();
	// ResultSet rs = (ResultSet) stat.executeQuery(sql);
	// System.out.println("==================数据迁移开始==================");
	// System.out.println("============迁移【核算基础价格信息】=============");
	// while (rs.next()) {
	// String goods = rs.getString("goods");
	// goods = goods.replaceAll("1-", "");
	// String price = rs.getString("price_cls");
	// price = price.replaceAll("1-", "");
	// String cell = rs.getString("org");
	// SysCell sysCell = SysCell.chkUniqueCode(false, cell);
	// GsGoods gsGoods = GsGoods.chkUniqueCode(false, goods);
	// GsPriceGoodsKind gsPriceGoodsKind =
	// GsPriceGoodsKind.chkUniqueCodePrice(false, price, 1);
	// GsPriceGoods gsPriceGoods = GsPriceGoods.chkUniqueGoodsPrice(false,
	// gsGoods.getPkey(), gsPriceGoodsKind.getPrice());
	// GsPriceGoodsCell gsPriceGoodsCell = new GsPriceGoodsCell().init();
	// gsPriceGoodsCell.setCell(sysCell.getPkey());
	// gsPriceGoodsCell.setPriceGoods(gsPriceGoods.getPkey());
	// gsPriceGoodsCell.setGoods(gsPriceGoods.getGoods());
	// gsPriceGoodsCell.setPriceName(gsPriceGoods.getPriceName());
	// gsPriceGoodsCell.stEnabled(true);
	// for (int i = 1; i <= 8; i++) {
	// gsPriceGoodsCell.stPrice(i, gsPriceGoods.gtPrice(i));
	// }
	// GsPriceGoodsCellDAO.Ins gpgdc = new GsPriceGoodsCellDAO.Ins();
	// gpgdc.setB(gsPriceGoodsCell);
	// gpgdc.commit();
	// }
	// System.out.println("==================数据迁移完成==================");
	// TranDb.INST.close(stat, rs);
	// }

	public static void DelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GsPrice.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsPrice.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsPriceCtl.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsPriceCtl.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsPriceGoodsKind.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsPriceGoodsKind.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsPriceGoods.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsPriceGoods.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsPriceGoodsCell.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsPriceGoodsCell.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GsPrice.class);
		new DbMysql().db(GsPriceCtl.class);
		new DbMysql().db(GsPriceGoodsKind.class);
		new DbMysql().db(GsPriceGoods.class);
		new DbMysql().db(GsPriceGoodsCell.class);
	}
}
