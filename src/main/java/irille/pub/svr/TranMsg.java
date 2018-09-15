package irille.pub.svr;

import irille.core.lg.LgLoginDAO;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysDept;
import irille.core.sys.SysMenuAct;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.inf.IDb;

import java.util.Date;

/**
 * 交易信息，在用户提交交易后会新建此实例
 * 
 * @author Maxwell Wu
 * 
 */
public class TranMsg {
	private static final Log LOG = new Log(TranMsg.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		tranBegin("用户[{0} {1}],交易[{3}], 功能[{4}] 开始"), 
		tranEnd("用户[{0} {1}],交易[{3}], 功能[{4}] 结束--{5}");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	private static final IDb DB = Env.INST.getDB();
	private LoginUserMsg _loginUser;
	private SysMenuAct _act;
	private Date _beginTime;
	private Integer _objTable; // 处理对象 <表主键:SysTable> INT<null>
	private Integer _objPkey; // 处理对象主键值 INT<null>


	public TranMsg(LoginUserMsg loginUser, SysMenuAct act) {
		_loginUser = loginUser;
		_act = act;
		_beginTime = getEnv().getSystemTime();
		// 数据加载日志不显示
		if (act != null && act.getPkey() != null)
			LOG.debug(Msgs.tranBegin, loginUser.getUser().getLoginName(), loginUser
					.getUser().getName(), act.getTableName(), act.getName());
	}

	public void end(boolean success) {
		String flag = success ? "成功" : "失败";
		if (_act != null && _act.getPkey() != null)
			LOG.debug(Msgs.tranBegin, _loginUser.getUser().getLoginName(), _loginUser
					.getUser().getName(), _act.getTableName(), _act.getName(), flag);
		LgLoginDAO.initTran(this, Env.getSystemTime(), success);
	}

	public void setObj(Integer objTable, Integer pkey) {
		_objPkey = pkey;
		_objTable = objTable;
	}

	/**
	 * 取环境信息
	 * 
	 * @return
	 */
	public Env getEnv() {
		return _loginUser.ENV;
	}

	/**
	 * 取数据库信息
	 * 
	 * @return
	 */
	public IDb getDB() {
		return DB;
	}

	/**
	 * 用取功能
	 * 
	 * @return
	 */
	public SysMenuAct getAct() {
		return _act;
	}

	/**
	 * 取交易
	 * 
	 * @return
	 */
	public SysTable getTable() {
		return _act.gtAct().gtSysTable();
	}

	/**
	 * 取用户
	 * 
	 * @return
	 */
	public SysUser getUser() {
		return _loginUser.getUser();
	}

	/**
	 * 取交易用户信息
	 * 
	 * @return
	 */
	public LoginUserMsg getLoginUser() {
		return _loginUser;
	}

	/**
	 * 取交易开始时间
	 * 
	 * @return
	 */
	public Date getBeginTime() {
		return _beginTime;
	}

	public SysOrg getOrg() {
		return _loginUser.getUser().gtOrg();
	}

	public SysDept getDept() {
		return _loginUser.getUser().gtDept();
	}

	public SysCell getCell() {
		return SysCellDAO.getCellByDept(getDept().getPkey());
		// return _loginUser.getUser().gtDept().getCell(); //XXX
	}

	public Integer getObjTable() {
		return _objTable;
	}

	public Integer getObjPkey() {
		return _objPkey;
	}

}
