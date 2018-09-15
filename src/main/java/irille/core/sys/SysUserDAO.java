package irille.core.sys;

import irille.pub.Cn;
import irille.pub.DateTools;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

//TODO 默认密码，用户第一次登录时提示修改
public class SysUserDAO {
	private static final Log LOG = new Log(SysUserDAO.class);

	public static void main(String[] args) {
		System.err.println(inCode("104admin8888"));
	}

	public static String inCode(String str) {
		return DateTools.getDigest(str.toLowerCase());
	}

	/**
	 * 用户登录验证
	 * @param code 登录用户名
	 * @param password 登录密码
	 * @param type 用户类型
	 * @return
	 */
	public static SysUser loginCheck(String code, String password) {
		if (Str.isEmpty(code))
			throw LOG.err("loginCheck", "请输入用户名");
		if (password == null || Str.isEmpty(password))
			throw LOG.err("loginCheck", "请输入密码");
		SysUser user = SysUser.chkUniqueCode(false, code);
		if (user == null)
			throw LOG.err("loginCheck", "您输入的账户名和密码不匹配，请重新输入");
		if (user.gtLoginState() == Sys.OLoginState.DISABLE)
			throw LOG.err("loginCheck", "您输入的账户已被停用");
		if (user.gtLoginState() == Sys.OLoginState.NOT_ACTIVE)
			throw LOG.err("loginCheck", "您输入的账户未激活");
		if (chkPwd(user.getPkey(), password) == false)
			throw LOG.err("loginCheck", "您输入的账户名和密码不匹配，请重新输入");
		return user;
	}
	
	public static SysUser loginCheckTmp(String code, String password) {
		if (Str.isEmpty(code))
			throw LOG.err("loginCheck", "请输入用户名");
		SysUser user = SysUser.chkUniqueCode(false, code);
		if (user == null)
			throw LOG.err("loginCheck", "您输入的账户名不匹配，请重新输入");
		return user;
	}

	public static boolean chkPwd(Integer key, String mm) {
		SysUserLogin login = new SysUserLogin().load(key);
		String pwd = key + mm;
		return login.getPassword().compareTo(inCode(pwd)) == 0;
	}

	/**
	 * 职员新增自动建账号
	 * 登录账号、默认密码
	 * @param em
	 */
	public static SysUser insByEM(SysEm em, String loginName) {
		if (SysUser.chkUniqueCode(false, loginName) != null)
			throw LOG.err("notFound", "登录账号[{0}]已存在,不可重复新增!", loginName);
		SysUser user = new SysUser();
		user.setLoginName(loginName);
		user.setName(em.getName());
		user.setNickname(em.getNickname());
		user.stLoginState(Sys.OLoginState.NOT_ACTIVE);
		user.setOrg(em.getOrg());
		user.setDept(em.getDept());
		user.setTbObj(em.gtLongPkey());
		user.ins();
		//新增密码信息
		SysUserLogin login = new SysUserLogin().init();
		login.setPkey(user.getPkey());
		login.stPasswordType(Sys.OPasswordType.PASSWORD);
		login.setPassword(inCode(user.getPkey() + "123456"));
		login.stCreateBy(Idu.getUser());
		login.setCreateDate(Env.INST.getWorkDate());
		login.ins();
		return user;
	}

	private static Cn getB() {
		// TODO Auto-generated method stub
		return null;
	}

	private static void validate(SysUser user) {
		if (Str.isEmpty(user.getLoginName()))
			throw LOG.err("inputAcc","请输入用户账号");
		//		if (user.getLoginName().length() < 6)
		//			throw LOG.err("帐号不能小于6位");
		if (user.gtDept().getOrg().equals(user.getOrg()) == false)
			throw LOG.err("deptNotInOrg","部门[{0}]不属于机构[{1}]", user.gtDept().getName(), user.gtOrg().getName());
		//user.stLoginState(Sys.OLoginState.LOGOUT);//TODO 不知道这句话的作用
	}

	public static void validatePw(String mm, String mmcheck) {
		//TODO 密码暂时不做复杂的限制
		//String ptn = "^(?=.*\\d)(?=.*[a-zA-Z]).{6,}$";
		if (mm == null || mm.length() < 6)
			throw LOG.err("err", "密码不能小于6位!");
		//		if (mm.matches(ptn) == false)
		//			throw LOG.err("err", "密码必须含有一个字母和一个数字!");
		if (mmcheck == null || mmcheck.equals(mm) == false)
			throw LOG.err("err", "密码确认不一致");
	}

	public static class Ins extends IduIns<Ins, SysUser> {
		public String _mm;
		public String _mmcheck;

		@Override
		public void before() {
			super.before();
			validate(getB());
			validatePw(_mm, _mmcheck);
			if (SysUser.chkUniqueCode(false, getB().getLoginName()) != null)
				throw LOG.err("hasCode", "用户账号已存在");
		}

		@Override
		public void after() {
			super.after();
			//新增密码信息
			SysUserLogin login = new SysUserLogin().init();
			login.setPkey(getB().getPkey());
			login.stPasswordType(Sys.OPasswordType.PASSWORD);
			login.setPassword(inCode(getB().getPkey() + _mm));
			login.stCreateBy(getUser());
			login.ins();
		}

	}

	public static class Upd extends IduUpd<Upd, SysUser> {

		public void before() {
			super.before();
			if (getB().getPkey() == 1)
				throw LOG.err("err", "系统管理员帐号不可修改");
			validate(getB());
			SysUser dbBean = load(getB().getPkey());
			if (dbBean.getLoginName().equals(getB().getLoginName()) == false
			    && SysUser.chkUniqueCode(false, getB().getLoginName()) != null)
				throw LOG.err("hasCode", "用户账号已存在");
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysUser.T.PKEY, SysUser.T.TB_OBJ,
					SysUser.T.NAME, SysUser.T.NICKNAME, SysUser.T.DEPT, SysUser.T.ORG);
			setB(dbBean);
			BeanBuf.clear(SysUser.class, getB().getPkey());
		}
		
	}

	public static class UpdPwd extends IduOther<UpdPwd, SysUser> {
		public static Cn CN = new Cn("updPwd", "修改密码");
		public String _mm;
		public String _mmcheck;
		
		public UpdPwd() {
			setChkVersion(false);
		}

		@Override
		public void run() {
			validatePw(_mm, _mmcheck);
			SysUserLogin login = BeanBase.load(SysUserLogin.class, getB().getPkey());
			login.setPassword(inCode(getB().getPkey() + _mm));
			login.upd();
		}

	}

	public static class Del extends IduDel<Del, SysUser> {
		
//		@Override
//		public void valid() {
//			super.valid();
//			haveBeUsed(SysUserRole.class, SysUserRole.T.USER, getB().getPkey());
//			haveBeUsed(SysDept.class, SysDept.T.MANAGER, getB().getPkey());
//			haveBeUsed(LgLogin.class, LgLogin.T.USER, getB().getPkey());
//		}
		
		@Override
		public void run() {
			if (getB().getPkey() == 1)
				throw LOG.err("err", "系统管理员帐号不可删除");
			getB().stLoginState(Sys.OLoginState.DISABLE);
			getB().upd();
			BeanBuf.clear(SysUser.class, getB().getPkey());
		}
	}

}
