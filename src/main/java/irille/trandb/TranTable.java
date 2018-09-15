package irille.trandb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TranTable {
	public static void run() throws Exception {
		runTable("!=");
		runTable("=");
	}

	public static void runTable(String zfc) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `gl_report` WHERE table_type " + zfc
				+ " 4 ORDER BY order_id";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		int num = 0;
		System.out.println("==================报表打印开始==================");
		System.out.println("index\tname\tvalue_type\tsymbol_type");
		while (rs.next()) {
			num++;
			String name = rs.getString("name");
			String code = rs.getString("code");
			Byte value_type = rs.getByte("value_type");
			Byte symbol_type = rs.getByte("symbol_type");
			String v, s;
			switch (value_type) {
			case 1:
				v = "余额";
				break;
			case 2:
				v = "小计";
				break;
			case 3:
				v = "总计";
				break;
			default:
				v = "无";
				break;
			}
			switch (symbol_type) {
			case 1:
				s = "加";
				break;
			case 2:
				s = "减";
				break;
			default:
				s = "无";
				break;
			}
			System.out.println(num + "\t" + name + "\t" + v + "\t" + s);
			runLenk(code,num);
		}
		TranDb.INST.close(stat, rs);
		System.out.println("==================报表打印完成==================");
	}

	public static void runLenk(String report,int t) throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `gl_report_line`a,`gl_account`b WHERE a.`account` = b.`code` AND a.`report` = '"
				+ report + "'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		int n = 0;
		while (rs.next()) {
			n++;
			String name = rs.getString("name");
			String code = rs.getString("code");
			Byte symbol_type = rs.getByte("symbol_type");
			String t1;
			switch (symbol_type) {
			case 1:
				t1 = "加";
				break;
			case 2:
				t1 = "减";
				break;
			default:
				t1 = "无";
				break;
			}
			System.out.println("\t"+t+"." + n + "\t" + code + "\t" + name+"\t"+t1);

		}

		TranDb.INST.close(stat, rs);
	}

}
