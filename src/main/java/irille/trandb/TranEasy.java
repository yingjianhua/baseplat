package irille.trandb;

import irille.gl.gs.GsUom;
import irille.gl.gs.GsUomType;
import irille.gl.gs.GsUomTypeDAO;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 迁移数据: `erp_uom_type`、`erp_uom`
 */
public class TranEasy {
	public static void run() throws Exception {
		DelAndAdd();
		runGsUomType();
	}

	public static void runGsUomType() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `erp_uom_type`";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("=============迁移【计量单位类型迁移】============");
		while (rs.next()) {
			String name = rs.getString("name");
			String rem = rs.getString("remark");
			String code = rs.getString("code");
			byte enabled = rs.getByte("enabled");
			GsUomType uomtype = GsUomType.chkUniqueName(false, name);
			if (uomtype == null) {
				uomtype = new GsUomType().init();
				uomtype.setName(name);
				uomtype.setEnabled(enabled);
				uomtype.setRem(rem);
				GsUomTypeDAO.Ins uomins = new GsUomTypeDAO.Ins();
				uomins.setB(uomtype);
				uomins.commit();
				runGsUom(code, uomtype);
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runGsUom(String code, GsUomType uomtype) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `erp_uom` WHERE uom_type = '" + code + "'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		while (rs.next()) {
			String name = rs.getString("name");
			BigDecimal rate = rs.getBigDecimal("rate");
			byte enabled = rs.getByte("enabled");
			GsUom uom = new GsUom();
			uom.setUomType(uomtype.getPkey());
			uom.setName(name);
			uom.setEnabled(enabled);
			uom.setRate(rate);
			uom.ins();
		}
		TranDb.INST.close(stat, rs);
	}
	public static void DelAndAdd() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(GsUom.class).getCodeSqlTb());
		stmt.execute("DROP TABLE " + Bean.tb(GsUomType.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GsUom.class).getCodeSqlTb() + "]-->成功!");
		System.out.println("删除表[" + Bean.tb(GsUomType.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(GsUom.class);
		new DbMysql().db(GsUomType.class);
	}
}
