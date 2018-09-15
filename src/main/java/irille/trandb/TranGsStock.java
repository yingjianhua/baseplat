package irille.trandb;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsLocation;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockBatch;
import irille.gl.gs.GsWarehouse;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-8 11:40:14
 * 循环数据创建RpWorkBox和RpJournal
 * 迁移数据: `inv_storage`、`inv_location`
 * 迁移条件: SELECT * FROM `inv_storage`a,`inv_location`b WHERE a.`lastest_loc` = b.`id` AND a.`qty` != '0' AND a.`warehouse` NOT REGEXP '^(11001|11009|11010|11011|11012)';
 */
public class TranGsStock {
	public static void run() throws Exception {
		runDelAndAddData();
		runGsStock();
	}
	public static void runGsStock() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `inv_storage`a,`inv_location`b WHERE a.`lastest_loc` = b.`id` AND a.`qty` != '0' AND a.`warehouse` NOT REGEXP '^(11001|11009|11010|11011|11012)';";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据创建开始==================");
		System.out.println("==================创建【存货】==================");
		while (rs.next()) {
			String warehouse = rs.getString("a.warehouse");
			String goods = rs.getString("a.goods");
			goods = goods.substring(2);
			BigDecimal qty = rs.getBigDecimal("a.qty");
			int lastestLoc = rs.getInt("a.lastest_loc");
			String locCode = rs.getString("b.code");
			SysCell cell = SysCell.chkUniqueCode(false, warehouse.substring(0,5));
			GsGoods gsGoods = GsGoods.chkUniqueCode(false, goods);
			SysDept dept = SysDept.chkUniqueCode(false, warehouse.replaceAll("--", "").replaceAll("-", ""));
			if(dept!=null){
				GsWarehouse gsWarehouse = GsWarehouse.load(GsWarehouse.class, dept.getPkey());
				if(locCode.equals("0")){
					locCode = "默认货位";
				}
				GsLocation location = GsLocation.chkUniqueHouseName(false, gsWarehouse.getPkey(), locCode);
				GsStock gsStock = GsStock.chkUniqueWg(false, gsWarehouse.getPkey(), gsGoods.getPkey());
				if(gsStock == null){
					gsStock = new GsStock();
					gsStock.setWarehouse(gsWarehouse.getPkey());
					gsStock.setGoods(gsGoods.getPkey());
					gsStock.setQty(qty);
					if(location!=null){
						gsStock.setLocation(location.getPkey());
					}else{
						System.out.println("不存在的货位: "+locCode);						
					}
					gsStock.setEnrouteQty(BigDecimal.ZERO);
					gsStock.setLockedQty(BigDecimal.ZERO);
					gsStock.setLowestQty(BigDecimal.ZERO);
					gsStock.setSafetyQty(BigDecimal.ZERO);
					gsStock.setLimitQty(BigDecimal.ZERO);
					gsStock.setPurLeadDays((short) 0);
					gsStock.stEnabled(true);
					gsStock.setCell(cell.getPkey());
					gsStock.ins();
					GsStockBatch batch = new GsStockBatch();
					batch.setStock(gsStock.getPkey());
					batch.stCleared(false);
					batch.setQty(gsStock.getQty());
					batch.ins();
				}else{
					System.out.println("已存在的数据: " + gsStock.getPkey());
				}
			}else{
				System.out.println("不存在的仓库"+warehouse);				
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}
	public static void runDelAndAddData() throws Exception{
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GsStock.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsStock.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsStockBatch.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsStockBatch.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GsStock.class);
		new DbMysql().db(GsStockBatch.class);
	}
}
