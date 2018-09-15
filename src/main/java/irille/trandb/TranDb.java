package irille.trandb;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import snaq.db.ConnectionPoolManager;

public class TranDb {
	private static final Log LOG = new Log(TranDb.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		initEnd("========原ERP----连接池初始化成功========");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	private static ConnectionPoolManager _poolManager;
	private static final String POOLNAME = "irille";
	private static TranDb _instance = null;
	private ThreadLocal<Connection> _conns = new ThreadLocal();
	public static TranDb INST = new TranDb();

	// 根据配置初始化连接池
	private TranDb() {
		try {
			_poolManager = ConnectionPoolManager
			    .getInstance(new File(Tran.class.getResource("tranOld.properties").getPath()));
		} catch (IOException e) {
			throw LOG.err(e, "dbpoolInit", "TranDb初始化失败");
		}
		LOG.info(Msgs.initEnd);
	}

	// 从指定连接池内取CONN对象
	private Connection getConnection() {
		Connection connection = getConnection(POOLNAME);
		return connection;
	}

	private Connection getConnection(String poolName) {
		Connection connection;
		try {
			connection = _poolManager.getConnection(poolName);
		} catch (SQLException e) {
			throw LOG.err(e, "getConn", "连接池取对象出错");
		}
		return connection;
	}

	// 清空连接池
	public void release(String poolName) {
		if (_poolManager.getPool(poolName) != null)
			_poolManager.getPool(poolName).release();
	}

	public void releaseAll() {
		_poolManager.release();
	}

	public Connection getConn() {
		Connection conn = _conns.get();
		if (conn == null) {
			conn = getConnection();
			try {
				conn.setAutoCommit(false);
			} catch (SQLException e) {
				LOG.err("setCommit", "关闭自动提交出错", e);
			}
			_conns.set(conn);
			return conn;
		}
		return conn;
	}

	// 每次通信最后必须调用关闭连接
	public void removeConn() {
		Connection conn = _conns.get();
		if (conn != null) {
			_conns.remove();
			try {
				if (conn.isClosed() == false)
					conn.close();
			} catch (SQLException e) {
				throw LOG.err(e, "closeConn", "关闭连接对象出错");
			}
		}
	}

	public static final void close(PreparedStatement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
	}

	public static final void close(Statement stmt, ResultSet rs) {
		close(rs);
		close(stmt);
	}

	public static final void close(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			throw LOG.err(e, "closeStmt", "关闭对象【Statement】出错");
		}
	}

	public static final void close(PreparedStatement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception e) {
			throw LOG.err(e, "closePreparedStatement", "关闭对象【PreparedStatement】出错");
		}
	}

	public static final void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception e) {
			throw LOG.err(e, "closeResultSet", "关闭对象【ResultSet】出错");
		}
	}
}
