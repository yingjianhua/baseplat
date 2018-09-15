package irille.trandb;

import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.gl.gs.GsLocation;
import irille.gl.gs.GsLocationDAO;
import irille.gl.gs.GsWarehouse;
import irille.gl.gs.GsWarehouseDAO;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-15 13:52:04
 * 迁移数据: `inv_warehouse`、`inv_location`
 * 迁移条件: `code` NOT REGEXP '^(11001|11009|11010|11011|11012)' AND enabled <> 2
 */
public class TranGsWarehouse {
	public static void run() throws Exception {
		DelAndAdd();
		runWarehouse();
	}

	public static void runWarehouse() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `inv_warehouse` WHERE `code` NOT REGEXP '^(11001|11009|11010|11011|11012)' AND enabled <> 2";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("===================数据迁移开始==================");
		System.out.println("==================迁移【仓库数据】===============");
		while (rs.next()) {
			String code = rs.getString("code");
			String org = rs.getString("org");
			String rem = rs.getString("remark");
			String dept;
			SysOrg orgpkey = SysOrg.chkUniqueCode(false, org.substring(0, 2) + "." + org.substring(2));
			dept = code.replaceAll("--", "-").replaceAll("-", "");
			if (dept.length() == 10) {
				dept = dept.substring(0, 8) + dept.substring(8);
			}
			SysDept deptpkey = SysDept.chkUniqueCode(false, dept);
			if (deptpkey != null) {
				GsWarehouse warehouse = new GsWarehouse().init();
				warehouse.stEnabled(true);
				warehouse.stLocationFlag(true);
				warehouse.stInvented(false);
				warehouse.setRem(rem);
				warehouse.stDept(deptpkey);
				GsWarehouseDAO.Ins warehouseins = new GsWarehouseDAO.Ins();
				warehouseins.setB(warehouse);
				warehouseins.commit();
				runLocation(code, warehouse);
			} else {
				System.err.println("部门[" + dept + "]不存在");
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runLocation(String code, GsWarehouse warehouse) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `inv_location` where warehouse = '" + code + "'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		while (rs.next()) {
			String code1 = rs.getString("code");
			if (code1.equals("0"))
				code1 = "默认货位";
			byte enabled = rs.getByte("enabled");
			GsLocation location = GsLocation.chkUniqueHouseName(false, warehouse.getPkey(), code1);
			if (location == null) {
				location = new GsLocation().init();
				location.setEnabled(enabled);
				location.setName(code1);
				location.setWarehouse(warehouse.getPkey());
				GsLocationDAO.Ins locationins = new GsLocationDAO.Ins();
				locationins.setB(location);
				locationins.commit();
			}else{
				System.err.println("货位["+location.getName()+"]已存在");
			}

		}
		TranDb.INST.close(stat, rs);
	}

	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GsWarehouse.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsWarehouse.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(GsLocation.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsLocation.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GsWarehouse.class);
		new DbMysql().db(GsLocation.class);
	}
}
