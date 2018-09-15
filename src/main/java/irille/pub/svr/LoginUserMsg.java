package irille.pub.svr;

import irille.core.lg.LgLogin;
import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysUser;
import irille.pub.Log;

import java.util.Date;

/**
 * 客户端信息接口
 * 
 * @author Maxwell Wu
 * 
 */
public class LoginUserMsg {
	private static final Log LOG = new Log(LoginUserMsg.class);
	public static final Env ENV = Env.INST;
	// ------------------------------------------------------------------------

	private String _ip;
	private String _system;
	private String _browser;
	private String _machineName;
	private Date _loginTime;
	private SysUser _user;
	private byte _client;
	private int _procTimes = 0; //累计处理时间
	private int _successCount = 0; //成功处理交易数
	private int _failCount = 0; //失败交易数
	private LgLogin _lg;

	public LoginUserMsg(SysUser user, byte client, String ip, String system, String browser) {
		_user = user;
		_client = client;
		_ip = ip;
		_system = system;
		_browser = browser;
		_loginTime = ENV.getSystemTime();
		//
		//		LOG.info("用户[{0} {1}],IP[{2}],客户端类型[{3}],系统[{4}],浏览器[{5}]登录系统！", user
		//				.getCode(), user.getName(), ip, c.get(client).getName(),
		//				system, browser);
	}

	public LoginUserMsg(LgLogin lg) {
		_user = lg.gtUserSys();
		_client = lg.getClient();
		_ip = lg.getIp();
		_system = lg.getSystem();
		_browser = lg.getBrowser();
		_loginTime = lg.getLoginTime();
		_lg = lg;
	}

	/**
	 * @return the client
	 */
	public byte getClient() {
		return _client;
	}

	/**
	 * @return the user
	 */
	public SysUser getUser() {
		return _user;
	}
	
	public SysDept getDept() {
		return _user.gtDept();
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return _ip;
	}

	/**
	 * @return the system
	 */
	public String getSystem() {
		return _system;
	}

	/**
	 * @return the browser
	 */
	public String getBrowser() {
		return _browser;
	}

	/**
	 * @return the loginTime
	 */
	public Date getLoginTime() {
		return _loginTime;
	}

	/**
	 * @return the machineName
	 */
	public String getMachineName() {
		return _machineName;
	}

	/**
	 * @return the procTimes
	 */
	public int getProcTimes() {
		return _procTimes;
	}

	/**
	 * @param procTimes the procTimes to set
	 */
	public void setProcTimes(int procTimes) {
		_procTimes = procTimes;
	}

	/**
	 * @return the successCount
	 */
	public int getSuccessCount() {
		return _successCount;
	}

	/**
	 * @param successCount the successCount to set
	 */
	public void setSuccessCount(int successCount) {
		_successCount = successCount;
	}

	/**
	 * @return the failCount
	 */
	public int getFailCount() {
		return _failCount;
	}

	/**
	 * @param failCount the failCount to set
	 */
	public void setFailCount(int failCount) {
		_failCount = failCount;
	}

	public LgLogin getLg() {
		return _lg;
	}

	public void setLg(LgLogin lg) {
		_lg = lg;
	}

	public SysUser get_user() {
		return _user;
	}

	public void set_user(SysUser _user) {
		this._user = _user;
	}
	
	

}
