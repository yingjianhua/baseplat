package irille.gl.rp;

import irille.core.sys.SysUser;
import irille.core.sys.SysUserRole;
import irille.core.sys.SysUserRoleDAO;
import irille.pub.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;

/**
 * 审核后，状态更改为‘已审核’
 * 调出后，状态更改为‘可记账’(检查审核标识为是)
 * 作调出时，界面上检查审核标识，输入出纳账，并后台检查审核标识、检查是当前用户是否为出纳员
 * 
 * 手动新增时：
 * 初始审核标识-否、是发起方；审核后，自动产生调入单；
 * 
 * 由调入单审核产生时：
 * 初始审核标识-是、否发起方； 审核后，更改来源调入单的审核标识
 * 
 * @author whx
 * @version 创建时间：2015年8月27日 下午3:10:48
 */
public class RpFundMvOutDAO {
	public static final Log LOG = new Log(RpFundMvOutDAO.class);

	public static void main(String[] args) throws Exception {
//		SysUserRole.TB.getCode();
//		SysUser us = new SysUser();
//		us.setPkey(1);
//		SysUserRoleDAO.listAllRoleByUser(us);

		Class.forName("com.ibm.db2.jcc.DB2Driver");
		Connection conn = DriverManager.getConnection(
		    "jdbc:db2://192.168.128.81:50000/IRILLE:retrieveMessagesFromServerOnGetMessage=true;currentSchema=IRILLEMS;",
		    "db2admin", "123456");
		//		PreparedStatement stmt = conn
		//		    .prepareStatement("INSERT INTO whx3(PKEY,T_BYTE,T_INT,T_DATE,T_TEXT) VALUES(?,?,?,?,?)");
		//		stmt.setObject(1, new Integer("16"));
		//		stmt.setObject(2, new Byte("3"));
		//		stmt.setObject(3, new Integer("3"));
		//		stmt.setObject(4, new Date());
		//		stmt.setObject(5, "sdfsdfsdf");
		//		stmt.executeUpdate();

		String sql = "SELECT * FROM sys_user_role WHERE sys_user=1";
		String sql2 = "SELECT * FROM(SELECT B.*, ROWNUMBER() OVER() AS RN FROM  (SELECT * FROM whx3 ) AS B ) WHERE RN > 2 and RN <= 4";
		PreparedStatement pstmt = conn.prepareStatement(sql);
//		pstmt.setObject(1, new Integer(1));
		ResultSet rs = pstmt.executeQuery();
		while (rs.next()) {
			System.err.println(rs.getObject(1)+":"+rs.getObject(2));
		}
		conn.close();
	}
}
