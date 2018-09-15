package irille.trandb;

import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;
import irille.pub.svr.LoginUserMsg;
import irille.pub.svr.StartInitServlet;
import irille.pub.svr.Svr;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;

public class Tran {
	private static final Log LOG = new Log(Tran.class);

	public Tran() throws SQLException {
		// 初始化各种连接对象
		Env.INST.getDB();
		DbPool.setTranconfig(new File(Tran.class.getResource("tranMysql.properties").getPath()));
		DbPool.getInstance();
		TranDb.INST.getClass();
		StartInitServlet.initBeanLoad();
		// 初始化当前登录用户的信息，以避免某些内容类中取当前用户信息时出错
		SysUser lu = new SysUser();
		lu.setPkey(1);
		LoginUserMsg msg = new LoginUserMsg(lu, (byte) 0, null, null, null);
		Env.INST.initTran(msg, null);
	}

	public static void main(String[] args) throws Exception {
		Tran tran = new Tran();
		tran.run();
	}

	public void run() throws Exception {
		try {
			runAll();
			Svr.commit();
		} finally {
			Svr.rollback();
			Svr.releaseAll(); // 释放默认的连接池
			TranDb.INST.releaseAll(); // 释放原ERP的连接池
		}
	}

	public void runAll() throws Exception {
		try {
			Date d1 = new Date();
//			System.err.println(d1);
//			TranSysOrgDept.run();
//			TranSysUserCorrelation.run();
//			TranPrvRole.run();
//			TranSysCustom.run();
//			TranSysSupplier.run();
//			TranEasy.run();
//			TranGsWarehouse.run();
//			TranGsGoods.run();
//			TranSalClass.run();
//			TranGlJournal.run();
//			TranGlDailyLedger.run();
//			TranRpClass.run();
//			TranGsStock.run();
//			TranGlGoods.run();
//			TranSalDiscount.run();
//			TranPurClass.run();
			Date d2 = new Date();
			System.err.println(d2);
			long diff = d2.getTime() - d1.getTime();
			long hs = diff / 1000 ;
			System.err.println("迁移所用【" + hs + "】毫秒");
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("异常");
		}

		// TranTable.run();
	}

}