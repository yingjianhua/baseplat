package irille.trandb;

import irille.core.sys.SysUser;
import irille.pss.sal.SalDiscountPriv;
import irille.pss.sal.SalDiscountPrivDAO;
import irille.pub.bean.Bean;
import irille.pub.svr.DbMysql;
import irille.pub.svr.DbPool;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author LYL
 * 2015-7-13 08:43:22 
 * sal_discount_priv				
 */
public class TranSalDiscount {
	public static void run() throws Exception {
		DelAndAddData();
		runSalDiscountPriv();
	}

	public static void runSalDiscountPriv() throws Exception {
		Connection conn = TranDb.INST.getConn();
		String sql = "SELECT * FROM `sal_discount_priv`a,`base_user`b WHERE a.`id` = b.`id` AND b.`enabled` != '2'";
		Statement stat = (Statement) conn.createStatement();
		ResultSet rs = (ResultSet) stat.executeQuery(sql);
		System.out.println("==================数据迁移开始==================");
		System.out.println("================迁移【折扣权限】================");
		//给管理员默认加一条记录
		SalDiscountPriv adminPriv = new SalDiscountPriv().init();
		adminPriv.setPkey(1);
		adminPriv.setUpdatedBy(1);
		adminPriv.setDiscountLevel((byte)5);
		adminPriv.ins();
		while (rs.next()) {
			int id = rs.getInt("a.id");
			byte discountLevel = rs.getByte("a.discount_level");
			discountLevel = (byte) (discountLevel + 4);
			if (discountLevel>8)
				discountLevel = 8;
			SysUser sysUser = SysUser.chkUniqueCode(false, id + "");
			SalDiscountPriv discountPriv = new SalDiscountPriv().init();
			if (sysUser != null) {
				discountPriv.setPkey(sysUser.getPkey());
				discountPriv.setDiscountLevel(discountLevel);
				SalDiscountPrivDAO.Ins ins = new SalDiscountPrivDAO.Ins();
				ins.setB(discountPriv);
				ins.commit();
			} else {
				System.out.println("不存在的用户: " + id);
			}
		}
		System.out.println("==================数据迁移完成==================");
		TranDb.INST.close(stat, rs);
	}

	public static void DelAndAddData() throws Exception {
		Statement stmt = DbPool.getInstance().getConn().createStatement();
		stmt.execute("DROP TABLE " + Bean.tb(SalDiscountPriv.class).getCodeSqlTb());
		System.out.println("删除表[" + Bean.tb(SalDiscountPriv.class).getCodeSqlTb() + "]-->成功!");
		stmt.close();
		new DbMysql().db(SalDiscountPriv.class);
	}
}
