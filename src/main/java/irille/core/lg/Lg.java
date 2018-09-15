/**
 * 
 */
package irille.core.lg;

import irille.core.sys.Sys;
import irille.core.sys.SysModule;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.PackageBase;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

/**
 * @author whx
 * 
 */
public class Lg extends PackageBase {
	private static final Log LOG = new Log(Lg.class);
	public static final Lg INST = new Lg();
	public static TbBase TB = new TbBase<Tb>(Lg.class, "日志模块"); //定义公共的Fld对象用
	public static final Sys.T SYS = Sys.T.AMT;

	private Lg() {
	}

	@Override
	public void initTbMsg() { // 初始化表信息
		addTb(1, LgAttemper.class);
		addTb(2, LgLogin.class);
		addTb(3, LgTran.class);
	}

	public void initTranData() { //初始化PrvTranData表数据
		addTD(new TranDataMsg(LgLogin.TB).d(LgLogin.T.USER_SYS).d(LgLogin.T.USER_SYS, SysUser.T.DEPT)
		    .o(LgLogin.T.USER_SYS, SysUser.T.ORG));
		addTD(new TranDataMsg(LgTran.TB).u(LgTran.T.LOGIN, LgLogin.T.USER_SYS)
		    .d(LgTran.T.LOGIN, LgLogin.T.USER_SYS, SysUser.T.DEPT).o(LgTran.T.LOGIN, LgLogin.T.USER_SYS, SysUser.T.ORG));
	}

	@Override
	public SysModule initModule() {
		return iuModule(Lg.TB, null);
	}

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		super.initOnlyOne();
	}

	public enum T implements IEnumFld {
		LOGIN(TB.crtOutKey(LgLogin.class, "login", "登录日志")), ;
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		//		T.LOGIN.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}//@formatter:on

	public enum OClient implements IEnumOpt {
		//@formatter:off
		WINDOWS(0, "Windows"),
		IOS(1, "IOS"),
		ANDRIOD(2, "Andriod");
		public static String NAME = "客户端类型";
		public static final OClient DEFAULT = WINDOWS; // 定义缺省值
		private EnumLine _line;
		private OClient(int key, String name) {_line = new EnumLine(this, key, name);}
		public EnumLine getLine() {return _line;}
	//@formatter:on
	}

}
