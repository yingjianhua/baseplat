package irille.trandb;

import irille.gl.gl.GlGoods;
import irille.gl.gl.GlJournal;
import irille.gl.gs.GsGoods;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-10 09:24:09
 * 迁移数据:gl_goods
 * 迁移条件:SELECT * FROM `gl_goods` WHERE journal NOT REGEXP '^(11001|11009|11010|11011|11012)' AND enabled <> '2'
 */
public class TranGlGoods {
	public static void run() throws Exception {
		runDelAndAddData();
		runGlGoods();
	}

	public static void runGlGoods() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `gl_goods` WHERE journal NOT REGEXP '^(11001|11009|11010|11011|11012)' AND enabled <> '2'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据创建开始==================");
		System.out.println("=================创建【存货账】=================");
		while (rs.next()) {
			String journal = rs.getString("journal");
			String goods = rs.getString("goods");
			goods = goods.substring(2);
			BigDecimal costQty = rs.getBigDecimal("cost_qty");
			BigDecimal costPrice = rs.getBigDecimal("cost_price");
			BigDecimal costBalance = rs.getBigDecimal("cost_balance");
			GsGoods gsGoods = GsGoods.chkUniqueCode(false, goods);
			if (gsGoods != null) {
				GlJournal glJournal = GlJournal.chkUniqueCode(false, journal);
				if (glJournal != null) {
					GlGoods glGoods = GlGoods.chkUniqueJournalGoods(false, glJournal.getPkey(), gsGoods.getPkey());
					if (glGoods == null) {
						glGoods = new GlGoods();
						glGoods.setGoods(gsGoods.getPkey());
						glGoods.setJournal(glJournal.getPkey());
						glGoods.setUom(gsGoods.getUom());
						glGoods.setQty(costQty);
						glGoods.setPrice(costPrice);
						glGoods.setBalance(costBalance);
						glGoods.stEnabled(true);
						glGoods.ins();
					} else {
						System.out.println("已存在的数据: " + glGoods.getPkey());
					}
				}else{
					System.err.println("普通分户账["+journal+"]不存在");
				}
			}else{
				System.err.println("货物["+goods+"]不存在");
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runDelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GlGoods.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GlGoods.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GlGoods.class);
	}
	
	/*public static void main(String[] args) {
	  List<GlJournal> list = Bean.list(GlJournal.class, "1=1", false);
	  for (GlJournal line : list) {
	  	if (line.getAccType().equals(line.gtSubject().getAccType()) == false)
	  		System.err.println(line.getCode()+"\t"+line.getName() + "\t" + line.gtSubject().gtAccType().getLine().getName()+"\t"+line.gtAccType().getLine().getName());
	  }
  }*/
}
