package irille.action.sys;

import irille.action.ActionBase;
import irille.core.lg.Lg;
import irille.core.lg.LgLogin;
import irille.core.lg.LgLoginDAO;
import irille.core.prv.PrvRole;
import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserDAO;
import irille.core.sys.SysUserRoleDAO;
import irille.pub.Exp;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.Env;
import irille.pub.svr.LoginUserMsg;
import irille.pub.verify.RandomImageServlet;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class SysUserAction extends ActionBase<SysUser> {
	private static final Log LOG = new Log(SysUserAction.class);
	private String _mmOld; // 原密码
	private String _mmNew; // 新密码
	private String _mmCheck; // 新密码确认
	private String _checkCode; // 验证码

	@Override
	public Class beanClazz() {
		return SysUser.class;
	}

	public SysUser getBean() {
		return _bean;
	}

	public void setBean(SysUser bean) {
		this._bean = bean;
	}

	public String getMmOld() {
		return _mmOld;
	}

	public void setMmOld(String mmOld) {
		_mmOld = mmOld;
	}

	public String getMmNew() {
		return _mmNew;
	}

	public void setMmNew(String mmNew) {
		_mmNew = mmNew;
	}

	public String getMmCheck() {
		return _mmCheck;
	}

	public void setMmCheck(String mmCheck) {
		_mmCheck = mmCheck;
	}

	public String getCheckCode() {
		return _checkCode;
	}

	public void setCheckCode(String checkCode) {
		_checkCode = checkCode;
	}

	public static String getOs(String agent) {
		if (agent.indexOf("win") > -1) {
			if (agent.indexOf("windows 95") > -1 || agent.indexOf("win95") > -1) {
				return "Windows 95";
			}
			if (agent.indexOf("windows 98") > -1 || agent.indexOf("win98") > -1) {
				return "Windows 98";
			}
			if (agent.indexOf("windows nt") > -1 || agent.indexOf("winnt") > -1) {
				return "Windows NT";
			}
		}
		return "";
	}

	public static String getBrowser(String agent) {
		if (agent.indexOf("firefox") > -1)
			return "Firefox";
		if (agent.indexOf("msie") > -1)
			return agent.substring(agent.indexOf("msie")).split("\\;")[0];
		if (agent.indexOf("chrome") > -1)
			return "Chrome";
		return "";
	}

	public String login() {
		try {
			String verifyCode = verifyCode();
			if (Str.isEmpty(verifyCode) || Str.isEmpty(getCheckCode()) || verifyCode.equals(getCheckCode()) == false)
				throw LOG.err("errcode", "验证码错误");
			SysUser user = SysUserDAO.loginCheck(getBean().getLoginName(), getMmCheck());
			user.stLoginState(Sys.OLoginState.PC); // TODO 最近登录的记录
			user.upd();
			String agent = ServletActionContext.getRequest().getHeader("User-Agent").toLowerCase();
			LgLogin lg = LgLoginDAO.initLg(user, Lg.OClient.WINDOWS.getLine().getKey(), ServletActionContext.getRequest()
			    .getRemoteAddr(), getOs(agent), getBrowser(agent));
			LoginUserMsg umsg = new LoginUserMsg(lg);
			this.session.put(LOGIN, umsg);
			Env.INST.initTran(umsg, "sys_SysUser_login");
		} catch (Exp e) {
			setSarg1(e.getLastMessage());
			return LOGIN;
		}
		setResult("/mvc/index.jsp");
		return RTRENDS;
	}
	
	public String loginTest() {
		try {
//			String verifyCode = verifyCode();
//			if (Str.isEmpty(verifyCode) || Str.isEmpty(getCheckCode()) || verifyCode.equals(getCheckCode()) == false)
//				throw LOG.err("errcode", "验证码错误");
			SysUser user = SysUserDAO.loginCheckTmp(getBean().getLoginName(), getMmCheck());
			user.stLoginState(Sys.OLoginState.PC); // TODO 最近登录的记录
			user.upd();
			String agent = ServletActionContext.getRequest().getHeader("User-Agent").toLowerCase();
			LgLogin lg = LgLoginDAO.initLg(user, Lg.OClient.WINDOWS.getLine().getKey(), ServletActionContext.getRequest()
			    .getRemoteAddr(), getOs(agent), getBrowser(agent));
			LoginUserMsg umsg = new LoginUserMsg(lg);
			this.session.put(LOGIN, umsg);
			Env.INST.initTran(umsg, "sys_SysUser_login");
		} catch (Exp e) {
			setSarg1(e.getLastMessage());
			return LOGIN;
		}
		setResult("/mvc/index.jsp");
		return RTRENDS;
	}

	// public String loginOA() {
	// try {
	// SysUser user = SysUserDAO.loginCheck(getBean().getLoginName(),
	// getMmCheck());
	// user.stLoginState(Sys.OLoginState.PC); //TODO 最近登录的记录
	// user.upd();
	// String agent =
	// ServletActionContext.getRequest().getHeader("User-Agent").toLowerCase();
	// LgLogin lg = LgLoginDAO.initLg(user,
	// Lg.OClient.WINDOWS.getLine().getKey(), ServletActionContext.getRequest()
	// .getRemoteAddr(), getOs(agent), getBrowser(agent));
	// LoginUserMsg umsg = new LoginUserMsg(lg);
	// this.session.put(LOGIN, umsg);
	// Env.INST.initTran(umsg, "sys_SysUser_login");
	// } catch (Exp e) {
	// setSarg1(e.getLastMessage());
	// return LOGIN;
	// }
	// setResult("/mvc/index.jsp");
	// return RTRENDS;
	// }

	public void loginout() {
		this.session.remove(LOGIN);
	}

	public void getLoginMsg() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		SysUser user = getLoginSys();
		SysCell cell = SysCellDAO.getCellByUser(user);
		JSONObject json = new JSONObject();
		json.put("userPkey", user.getPkey());
		json.put("userName", user.getName());
		json.put("deptPkey", user.getDept());
		json.put("deptName", user.gtDept().getName());
		json.put("cellPkey", cell.getPkey());
		json.put("cellName", cell.getName());
		json.put("orgPkey", user.getOrg());
		json.put("orgName", user.gtOrg().getName());
		//安装模块信息
		JSONObject menusJs = new JSONObject();
		String menus = "";
		for (String str : SysMenuAction.MENUMAP.keySet()) {
			menus += "," + str;
			menusJs = menusJs.put(str, SysMenuAction.MENUMAP.get(str));
		}
		json.put("menus", menus.substring(1));
		json.put("menusObj", menusJs);
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	// 取Session中的验证码
	private String verifyCode() {
		HttpServletRequest request = ServletActionContext.getRequest();
		HttpSession ssn = request.getSession(false);
		if (ssn == null)
			return null;
		return (String) ssn.getAttribute(RandomImageServlet.RANDOM_LOGIN_KEY);
	}

	@Override
	public SysUser insRun() throws Exception {
		SysUserDAO.Ins ins = new SysUserDAO.Ins();
		ins.setB(_bean);
		ins._mm = getMmNew();
		ins._mmcheck = getMmCheck();
		ins.commit();
		return ins.getB();
	}

	public void updPwd() {
		SysUserDAO.UpdPwd act = new SysUserDAO.UpdPwd();
		act.setB(_bean);
		act._mm = getMmNew();
		act._mmcheck = getMmCheck();
		act.commit();
	}

	public void updPwdMy() {
		Integer key = getLoginSys().getPkey();
		if (!SysUserDAO.chkPwd(key, getMmOld()))
			throw LOG.err("passwordERR", "原密码错误");
		SysUserDAO.UpdPwd act = new SysUserDAO.UpdPwd();
		act.setBKey(key);
		act._mm = getMmNew();
		act._mmcheck = getMmCheck();
		act.commit();
	}

	@Override
	public String orderBy() {
		return " ORDER BY PKEY";
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		Vector<PrvRole> vec = SysUserRoleDAO.listRoleByUser((SysUser) bean);
		String roles = "";
		for (PrvRole line : vec)
			roles += "," + line.getName();
		if (Str.isEmpty(roles) == false)
			json.put(pref + "roles", roles.substring(1));
		return json;
	}
}
