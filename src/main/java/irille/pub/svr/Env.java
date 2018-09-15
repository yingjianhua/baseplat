package irille.pub.svr;

import irille.core.sys.SysMenuDAO;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.inf.IDb;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Properties;

public class Env implements IPubVars {
	private static final Log LOG = new Log(Env.class);
	private static Hashtable<IEnumConf, Object> _conf = new Hashtable();
	//	private static Vector<IEnumConf> _allSysConf=new Vector();
	public static Env INST = new Env(); // 实例，在系统启动时在StartInit类中初始化！
	private static ThreadLocal<TranMsg> _tranMsgs = new ThreadLocal();

	// 系统参数定义
	public static enum SysConf implements IEnumConf {//@formatter:off
		AMT_LEN("金额位数",16),
		AMT_DIGITS("金额小数位数",2),
		QTY_LEN("数量位数",14),
		QTY_DIGITS("数量小数位数",4),
		PRICE_LEN("单价位数",14),
		PRICE_DIGITS("单价小数位数",4),
		FETCH_SIZE("检索的最大记录数",500),
		MAX_PASSWORD_LAW_LESS_COUNT("密码每天最多尝试次数",5),
		COOKIES_KEEP_DAYS("COOKIES保存天数",30),
		CODE_SEPARATOR("单据类型与号码之间的分隔符","-"),
		PAGE_SIZE("每页行数",20);
		private String _name;
		private Object _obj;
		SysConf(String name,Object obj) { _name=name; _obj=obj; }
		public String getName() { return _name; }
		@Override
		public Object getDefaultValue() {return _obj;	}
	}//@formatter:on

	public SysConf[] getVarsConf() { //系统参数
		return SysConf.values();
	}

	// 不用，直接用WorkDir
	private String _workDir; // 工作目录
	private String _cssSpriteDir; // Css小图片文件合并的目录
	private String _tmpDir; // 空目录
	private String _programDir; // 安装目录
	private static String _workSpaceDir = null;

	private IDb _db;

	public Env() {
		_workDir = "c:\\irille\\wisdom\\";// 工作目录
		_programDir = _workDir;
		_cssSpriteDir = _workDir + "cssSprite" + FILE_SEPARATOR;
		_tmpDir = _workDir + "tmp" + FILE_SEPARATOR;
		String db = getDbConf();
		if (db.equals("DB2"))
			_db = new DbDb2(); // 当前数据库指定
		else if (db.equals("ORACLE"))
			_db = new DbOracle();
		else
			_db = new DbMysql();
		setConfDefault(SysConf.values());
	}

	private String getDbConf() {
		Properties pps = new Properties();
		try {
			InputStream in = getClass().getResourceAsStream("/db.properties");
			pps.load(in);
		} catch (IOException e) {
			throw LOG.err("loadDbConf", "加载数据库配置信息失败");
		}
		return pps.getProperty("db").toUpperCase();
	}

	public static TranMsg getTran() {
		TranMsg tranmsg = _tranMsgs.get();
		if (tranmsg == null)
			throw LOG.err("err", "交易信息初始化失败"); // 应在用户登录后、或登录校验中初始化
		return tranmsg;
	}

	public void initTran(LoginUserMsg umsg, String path) {
		TranMsg msg;
		if (path != null)
			msg = new TranMsg(umsg, SysMenuDAO.getByReq(path));
		else
			msg = new TranMsg(umsg, null);
		_tranMsgs.set(msg);
	}

	public void endTran(boolean success) {
		TranMsg msg = _tranMsgs.get(); // 在事务过滤器中调用
		if (msg != null) {
			msg.end(success);
			_tranMsgs.remove();
		}
	}
	public void removeTran() {
		TranMsg msg = _tranMsgs.get();
		if (msg != null) {
			_tranMsgs.remove();
		}
	}

	public static Date getTranBeginTime() {
		return getTran().getBeginTime();
	}

	public static final String getConfParaStr(IEnumConf name) {
		return _conf.get(name).toString();
	}

	public static final Integer getConfParaInt(IEnumConf name) {
		return Integer.parseInt(_conf.get(name).toString());
	}

	public static final Boolean getConfParaBoolean(IEnumConf name) {
		return Boolean.parseBoolean(_conf.get(name).toString());
	}

	/**
	 * 设置系统参数的初始值
	 * 
	 * @param conf
	 */
	private void setConfDefault(IEnumConf confs[]) {
		Enum e;
		for (IEnumConf conf : confs)
			_conf.put(conf, conf.getDefaultValue());
	}

	/**
	 * 打印系统的所有属性
	 */
	public static void printSystemPropertys() {
		Enumeration<Object> aa = System.getProperties().keys();
		for (int i = 0; aa.hasMoreElements(); i++) {
			Object s = aa.nextElement();
			System.err.println(i + ":" + s + "|" + System.getProperty(s.toString()));
		}
	}

	/**
	 * 取数据库操作类
	 */
	public IDb getDB() {
		return _db;
	}

	/**
	 * 取工作目录
	 * 
	 * @return
	 */
	public String getWorkDir() {
		return _workDir;
	}

	/**
	 * 取临时目录
	 * 
	 * @return
	 */
	public String getTmpDir() {
		return _tmpDir;
	}

	/**
	 * Css小图片文件合并的目录
	 * 
	 * @return
	 */
	public String getCssSprateDir() {
		return _cssSpriteDir;
	}

	/**
	 * @return the programDir
	 */
	public String getProgramDir() {
		return _programDir;
	}

	/**
	 * 取系统日期
	 * 
	 * @return
	 */
	public static Date getSystemTime() {
		return new Date();
	}

	public static Date getWorkDate() {
		return new Date(); // XXX
	}

	public static String getSystemUser() {
		return System.getProperty("user.name");
	}

	public static String getUserHome() {
		return System.getProperty("user.home");
	}

	// 在开发时,返回为包的目录,如:C:\workspace\irilleJbk
	public static String getUserDir() {
		return System.getProperty("user.dir") + FILE_SEPARATOR;
	}

	public static String getWorkSpaceDir() {
		if (_workSpaceDir == null)
			_workSpaceDir = Env.class.getResource("/").getFile().replace("/WebContent/WEB-INF/classes", "");
		;
		return _workSpaceDir;
	}

	public static String getResourceDir() {
		return getWorkSpaceDir() + "WebContent/";
	}

	//
	// @Override
	// public String getIp() {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public String getSystem() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	// @Override
	// public String getMachineName() {
	// // TODO Auto-generated method stub
	// return null;
	// }

	public static void outPrint(Object obj) {
		System.out.print(obj);
		System.out.flush();
	}

	public static void outPrintln(Object obj) {
		System.out.println(obj);
		System.out.flush();
	}

	public static void errPrint(Object obj) {
		System.err.print(obj);
		System.err.flush();
	}

	public static void errPrintln(Object obj) {
		System.err.println(obj);
		System.err.flush();
	}

	public static void flushErrOut() {
		System.err.flush();
		System.out.flush();
	}

}
