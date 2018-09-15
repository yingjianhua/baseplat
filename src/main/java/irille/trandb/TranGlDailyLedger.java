package irille.trandb;

import irille.gl.gl.Gl;
import irille.gl.gl.GlDailyLedger;
import irille.gl.gl.GlJournal;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Statement;
import java.util.List;

public class TranGlDailyLedger {
	public static void run() throws Exception {
		Statement stmt4 = DbPool.getInstance().getConn().createStatement();
		stmt4.execute("DROP TABLE " + Bean.tb(GlDailyLedger.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(GlDailyLedger.class).getCodeSqlTb() + "]-->成功!");
		stmt4.close();
		new DbMysql().db(GlDailyLedger.class);
		runGlDailyLedger();
	}

	public static void runGlDailyLedger() throws Exception {
		List<GlJournal> Journal = BeanBase.list(GlJournal.class, null, false);
		System.out.println("==================创建【日总账】====================");
		for (GlJournal glJournal : Journal) {
			GlDailyLedger dailyLedger = GlDailyLedger.chkUniqueCellSubjectWorkDate(false, glJournal.getCell(), glJournal.getSubject(), glJournal.gtOrg().getWorkDate());
			if (dailyLedger == null) {
				dailyLedger = new GlDailyLedger().init();
				dailyLedger.setCell(glJournal.getCell());
				dailyLedger.setSubject(glJournal.getSubject());
				dailyLedger.setWorkDate(glJournal.gtOrg().getWorkDate());
				dailyLedger.setCurrency(glJournal.getCurrency());
				dailyLedger.setOrg(glJournal.getOrg());
				dailyLedger.ins();
			}
			if (glJournal.gtDirect() == Gl.ODirect.DR) {
				dailyLedger.setDrBalance(dailyLedger.getDrBalance().add(glJournal.getBalance()));
			} else {
				dailyLedger.setCrBalance(dailyLedger.getCrBalance().add(glJournal.getBalance()));
			}
			dailyLedger.upd();
		}
		System.out.println("====================完成=创建======================");
	}

}
