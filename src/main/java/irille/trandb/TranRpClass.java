package irille.trandb;

import irille.core.sys.SysCell;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OEnabled;
import irille.gl.gl.GlJournal;
import irille.gl.rp.RpJournal;
import irille.gl.rp.RpWorkBox;
import irille.gl.rp.RpWorkBoxDAO;
import irille.gl.rp.RpWorkBoxGoods;
import irille.gl.rp.Rp.ORpJournalType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL 2015-7-8 11:40:14 
 * 循环数据创建RpWorkBox和RpJournal 
 * 迁移数据: `rp_journal`、
 * 迁移条件: `org` NOT IN ('11001','11009','11010','11011','11012') GROUP BY `org`
 */
public class TranRpClass {
	public static void run() throws Exception {
		runDelAndAddData();
		runRpWorkBox();
		runRpJournal();
	}

	public static void runRpWorkBox() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `rp_journal` WHERE `org` NOT IN ('11001','11009','11010','11011','11012')  GROUP BY `org`";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		RpWorkBox workBox = new RpWorkBox().init();
		System.out.println("==================数据创建开始==================");
		System.out.println("==================创建【工具箱】================");
		while (rs.next()) {
			String org = rs.getString("org");
			String user = rs.getString("cashier");
			SysUser sysUser = SysUser.chkUniqueCode(false, user);
			SysCell cell = SysCell.chkUniqueCode(false, org);
			workBox.setMngCell(cell.getPkey());
			workBox.setUserSys(sysUser.getPkey());
			workBox.setName("默认工作箱");
			RpWorkBoxDAO.Ins ins = new RpWorkBoxDAO.Ins();
			ins.setB(workBox);
			ins.commit();
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runRpJournal() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `rp_journal` WHERE `org` NOT IN ('11001','11009','11010','11011','11012') AND enabled != '2' AND balance != '0'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据创建开始==================");
		System.out.println("================创建【出纳日记账】===============");
		while (rs.next()) {
			String code = rs.getString("code");
			String name = rs.getString("name");
			String org = rs.getString("org");
			BigDecimal balance = rs.getBigDecimal("balance");
			String journal = rs.getString("journal");
			journal = journal.replaceAll("-2-", "-");
			String bankAccount = rs.getString("bank_account");
			String bankName = rs.getString("bank_name");
			String rem = rs.getString("remark");
			String cashier = rs.getString("cashier");
			SysUser user = SysUser.chkUniqueCode(false, cashier);
			SysCell cell = SysCell.chkUniqueCode(false, org);
			RpWorkBox rpWorkBox = (RpWorkBox) RpWorkBox.loadUnique(RpWorkBox.T.IDX_MNG_CELL, false, cell.getPkey());
			GlJournal glJournal = GlJournal.chkUniqueCode(false, journal);
			if (glJournal == null) {
				String where = "rem = '因数据错误原数据:(" + journal + ")'";
				glJournal = BeanBase.list(GlJournal.class, where, false).get(0);
			}

			RpJournal rpJournal = new RpJournal();
			rpJournal.setPkey(glJournal.getPkey());
			rpJournal.setCode(glJournal.getCode());
			rpJournal.setName(name);
			if (journal.lastIndexOf("-101-") != -1) {
				rpJournal.stRpJournalType(ORpJournalType.CASHIER);
			} else {
				rpJournal.stRpJournalType(ORpJournalType.COM);
			}
			rpJournal.setBalance(balance);
			rpJournal.setBankName(bankName);
			rpJournal.setBankAccCode(bankAccount);
			rpJournal.setRem(rem);
			if (user != null) {
				rpJournal.setCashier(user.getPkey());
			} else {
				System.out.println(cashier);
			}
			rpJournal.setCell(cell.getPkey());
			rpJournal.setOrg(cell.getOrg());
			rpJournal.setWorkBox(rpWorkBox.getPkey());
			rpJournal.setEnabled(OEnabled.TRUE.getLine().getKey());
			rpJournal.setYestodayBalance(BigDecimal.ZERO);
			rpJournal.setDrAmt(BigDecimal.ZERO);
			rpJournal.setDrQty(0);
			rpJournal.setCrAmt(BigDecimal.ZERO);
			rpJournal.setCrQty(0);
			rpJournal.ins();
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void runDelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(RpWorkBox.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(RpWorkBox.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(RpJournal.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(RpJournal.class).getCodeSqlTb() + "]-->成功!");
		stmt.execute("DROP TABLE " + Bean.tb(RpWorkBoxGoods.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(RpWorkBoxGoods.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(RpWorkBox.class);
		new DbMysql().db(RpJournal.class);
		new DbMysql().db(RpWorkBoxGoods.class);
	}
}
