package irille.pub.bean;

import irille.core.prv.PrvTranData;
import irille.core.prv.PrvTranDataDAO;
import irille.core.sys.ConfPackage;
import irille.core.sys.SysMenuDAO;
import irille.core.sys.SysModule;
import irille.core.sys.SysTableDAO;
import irille.gl.gl.GlSubjectMapDAO;
import irille.pub.ClassTools;
import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.inf.ICn;
import irille.pub.svr.Env;
import irille.pub.svr.IEnumConf;
import irille.pub.svr.Svr;
import irille.pub.svr.Svr.IRunSvr;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.OptCust;
import irille.pub.tb.Tb;
import irille.pub.tb.TbBase;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Vector;

/**
 * 每个Bean包都包含一个以包名为类名的类，继承自此类，在初始化时要构造一次实例进行一次初始化，以对模块进行必要的初始化
 * 
 * @author whx
 * 
 */
public abstract class PackageBase {
	private static final Log LOG = new Log(PackageBase.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		init("初始化模块【{0}】信息...");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static final OTableType TABLE_TYPE = OTableType.SYS; // 表类型

	private String _name = null;
	private int _id = -1; // 包的ID号，-1表示还未设定
	private Vector<TbMsg> _tbMsgs = new Vector();

	public PackageBase() {
		initOnlyOne();
	}

	private boolean _initOnlyOneFlag = false;

	/**
	 * 初始化，在运行期间仅执行一次
	 */
	public void initOnlyOne() { // 初始化方法，在每次启动时执行一次
		if (_initOnlyOneFlag)
			throw LOG.err("err", "包【{0}】的初始化方法不能重复调用！", getClass());
		_initOnlyOneFlag = true;
		LOG.info(Msgs.init, getClass());
	}

	//初始化表信息
	public abstract void initTbMsg();

	//初始化表权限信息
	public abstract void initTranData();

	//模块安装信息
	public abstract SysModule initModule();

	public SysModule iuModule(TbBase tbbase, String menus) {
		SysModule m = BeanBase.chk(SysModule.class, tbbase.getCode());
		if (m == null) {
			m = new SysModule().init();
			m.setPkey(tbbase.getCode());
			m.setName(tbbase.getName());
			m.setMenus(menus);
			m.ins();
		} else {
			m.setName(tbbase.getName());
			m.setMenus(menus);
			m.upd();
		}
		System.out.println("同步模块信息：" + m.getName());
		return m;
	}

	/**
	 * 取临时目录
	 * @return
	 */
	public ITmpDir[] getTmpDirs() {
		return new ITmpDir[0];
	}

	/**
	 * @return 模块的变量
	 */
	public IEnumConf[] getVarsConf() {
		return new IEnumConf[0];
	}

	/**
	 * 取用户选项
	 * 
	 * @return the OptCust
	 */
	public IOptCustEnum[] getOptCusts() {
		return new IOptCustEnum[0];
	}

	/**
	 * 取所有的科目别名
	 * 
	 * @return
	 */
	public ISubjectAlias[] getSubjectAliases() {
		return new ISubjectAlias[0];
	}

	/**
	 * 取成本核算的别名
	 * 在科目别名刷新时调用，与商品类别的科目别名对应
	 * @return
	 */
	public ISubjectAlias[] getSubjectAliasesCst() {
		return new ISubjectAlias[0];
	}

	/**
	 * 指定新表
	 */
	public void addTb(int localId, Class beanClazz, OTableType tableType) {
		if (localId > 199)
			throw LOG.err("err", "表的ID号不能大于199！");
		_tbMsgs.add(new TbMsg(getPackageId() + localId, beanClazz, tableType));
	}

	/**
	 * 指定新表, 表类型为SYS
	 */
	public void addTb(int localId, Class beanClazz) {
		addTb(localId, beanClazz, TABLE_TYPE.SYS);
	}

	// 建立目录，可重复调用，但每次启动后，系统只会在第一次调用时新建目录！
	public static void mkTmpDirOnlyOne(ITmpDir dir) {
		MkTmpDirOnlyOne.mkDir(dir);
	}

	public final String getName() {
		if (_name == null)
			_name = ((TbBase) ClassTools.getStaticProerty(getClass(), "TB")).getName();
		return _name;
	}

	private boolean _tbMsgsFlag = false;

	/**
	 * @return the tbMsgs
	 */
	public Vector<TbMsg> getTbMsgs() {
		if (!_tbMsgsFlag) {
			_tbMsgsFlag = true;
			initTbMsg();
		}
		return _tbMsgs;
	}

	public int getPackageId() {
		return ConfPackage.INST.getPackageId(getClass());
	}

	/**
	 * 表信息
	 * 
	 * @author whx
	 * 
	 */
	public static class TbMsg {
		private int _id;
		private Class _clazz;
		private Tb _tb = null;
		private OTableType _tableType;

		private TbMsg(int id, Class beanClazz, OTableType tableType) {
			super();
			_id = id;
			_clazz = beanClazz;
			_tableType = tableType;
		}

		public Class getClazz() {
			return _clazz;
		}

		/**
		 * @return the id
		 */
		public int getId() {
			return _id;
		}

		/**
		 * @return the tb
		 */
		public Tb getTb() {
			if (_tb == null) {
				_tb = ((Tb) ClassTools.getStaticProerty(_clazz, "TB"));
			}
			return _tb;
		}

		/**
		 * @return the tableType
		 */
		public OTableType getTableType() {
			return _tableType;
		}
	}

	/**
	 * 包配置文件
	 * 
	 * @author whx
	 * 
	 */
	public static class ConfPackageBase implements IRunSvr {
		private static final Log LOG = new Log(ConfPackageBase.class);
		private LinkedHashMap<Class, Integer> _packs = new LinkedHashMap();

		public ConfPackageBase() {
			initPacks();
		}

		public void install() {
			Svr.init(); // 防止循环调用
			SysMenuDAO.setMenuList();
			SysTableDAO.setTableList();
			checkPackageId(); // 检查表ID是否重复
			Svr.run(this);
		}

		/*
		 * (non-Javadoc)
		 * @see irille.pub.svr.Svr.IRunSvr#runSvr()
		 */
		@Override
		public void runSvr() {
			for (Class clazz : _packs.keySet()) {
				// 检查表ID与表是否重复
				InstallBase ins = (InstallBase) ClassTools.getStaticProerty(clazz.getName() + "_Install", "INST");
				ins.install();
			}
			SysTableDAO.procClearBean(""); // 清理所有不存在的表
			PrvTranDataDAO.clearTranDataBean();
			SysMenuDAO.procClearBean();
			SysMenuDAO.procAddAll();
			procClearModule();
			//对别名的初始化
			GlSubjectMapDAO.refreshBase();
		}

		//清理模块安装信息
		private void procClearModule() {
			Vector<String> vec = new Vector();
			for (Class clazz : _packs.keySet())
				vec.add(clazz.getSimpleName());
			List<SysModule> list = BeanBase.list(SysModule.class, "1=1", false);
			for (SysModule line : list)
				if (vec.contains(line.getPkey()) == false)
					line.del();
		}

		private void checkPackageId() {
			Hashtable<Integer, Class> _packsId = new Hashtable();
			int i;
			// 检查包ID是否重复
			for (Class clazz : _packs.keySet()) {
				i = _packs.get(clazz);
				if (_packsId.get(i) != null) {
					throw LOG.err("err", "包【{0}】序号【{1}】重复定义，与包【{2}】相同！", clazz.getName(), i, _packsId.get(i).getName());
				}
				_packsId.put(i, clazz);
			}
		}

		/**
		 * 初始化包
		 */
		public void initPacks() {
		}

		public void add(Class clazz, int tbFirstId) {
			if (_packs.get(clazz) != null) {
				throw LOG.err("errPack", "包【{0}】重复定义！", clazz.getName());
			}
			_packs.put(clazz, tbFirstId);
		}

		public int getPackageId(Class packageClazz) {
			Integer id = _packs.get(packageClazz);
			if (id != null)
				return id;
			throw LOG.err("err", "包【{0}】的起始ID号未定义！", packageClazz.getName());
		}

		protected boolean _packsFlag = false;

		/**
		 * @return the tbMsgs
		 */
		public LinkedHashMap<Class, Integer> getPacks() {
			if (!_packsFlag) {
				_packsFlag = true;
				initPacks();
			}
			return _packs;
		}

	}

	/**
	 * 科目别名接口
	 * 
	 * @author whx
	 * 
	 */
	public static interface ISubjectAlias extends ICn {
		public String getSubName(int i);
	}

	/**
	 * 选项声明类的接口
	 * 
	 * @author whx
	 * 
	 */
	public static interface IOptCustEnum {
		public OptCust getOpt();
	}

	/**
	 * 科目别名接口
	 * 
	 * @author whx
	 * 
	 */
	public static interface ITmpDir {
		public String getName();

		public String getDir();

		// 建立目录，每次启动后，在需要时只建立一次！
		public void mkDir();
	}

	/**
	 * 建立目录，可重复调用，但每次启动后，系统只会在第一次调用时新建目录！
	 * 
	 * @author whx
	 * 
	 */
	private static class MkTmpDirOnlyOne {
		private static HashSet<String> _list = new HashSet();

		public static void mkDir(ITmpDir dir) {
			if (_list.contains(dir.getDir()))
				return;
			FileTools.mkDir(Env.INST.getTmpDir() + dir.getDir());
		}
	}

	public enum OTableType implements IEnumOpt {
		//@formatter:off  系统表、单据、凭证、NOTE
			SYS(1,"系统表"),FORM(2,"单据"),BILL(3,"凭证"),NOTE(4,"便签");
			public static String NAME="表类型";
			public static OTableType DEFAULT = SYS; // 定义缺省值
			private EnumLine _line;
			private OTableType(int key, String name) {_line=new EnumLine(this,key,name);	}
			public EnumLine getLine(){return _line;	}
			//@formatter:on
	}

	private Vector _tranDatas = new Vector();
	private Boolean _tranDatasFlag = false;

	public Vector<TranDataMsg> getTranDatas() {
		if (!_tranDatasFlag) {
			_tranDatasFlag = true;
			initTranData();
		}
		return _tranDatas;
	}

	public void addTD(Object o) {
		_tranDatas.add(o);
	}

	public class TranDataMsg {
		Tb _tb;
		String _name;
		IEnumFld[] _uflds;
		IEnumFld[] _dflds;
		IEnumFld[] _cflds;
		IEnumFld[] _oflds;

		public TranDataMsg(Tb tb) {
			_tb = tb;
			_name = "默认";
		}

		public TranDataMsg(String name, Tb tb) {
			_tb = tb;
			_name = name;
		}

		public TranDataMsg u(IEnumFld... flds) {
			_uflds = flds;
			return this;
		}

		public TranDataMsg d(IEnumFld... flds) {
			_dflds = flds;
			return this;
		}

		public TranDataMsg c(IEnumFld... flds) {
			_cflds = flds;
			return this;
		}

		public TranDataMsg o(IEnumFld... flds) {
			_oflds = flds;
			return this;
		}

		private String getCode(IEnumFld... flds) {
			if (flds == null)
				return null;
			String code = "";
			for (IEnumFld fld : flds)
				code += "." + fld.getFld().getCode();
			return code.substring(1);
		}

		private String getName(IEnumFld... flds) {
			if (flds == null)
				return null;
			String name = "";
			for (IEnumFld fld : flds)
				name += fld.getFld().getName();
			return name;
		}

		//getName...
		public void iu() {
			//注意修改的情况
			if (_uflds != null) {
				_name = getName(_uflds);
			} else if (_dflds != null) {
				_name = getName(_dflds);
			} else if (_cflds != null) {
				_name = getName(_cflds);
			} else if (_oflds != null) {
				_name = getName(_oflds);
			}
			//String where = "name=? and tran=?";
			PrvTranData bean = (PrvTranData) PrvTranData.chkUniqueTranName(false, _tb.getID(), _name);
			boolean isForm = false;
			if (BeanBill.class.isAssignableFrom(_tb.getClazz()) || BeanForm.class.isAssignableFrom(_tb.getClazz())) {
				isForm = true;
			}
			if (bean == null) {
				bean = new PrvTranData();
				bean.setTran(_tb.getID());
				bean.setTranCode(_tb.getClazz().getName());
				bean.setTranName(_tb.getName());
				bean.setIsForm(isForm ? (byte) 1 : 0);
				bean.setUserFld(getCode(_uflds));
				bean.setUserName(getName(_uflds));
				bean.setDeptFld(getCode(_dflds));
				bean.setDeptName(getName(_dflds));
				bean.setCellFld(getCode(_cflds));
				bean.setCellName(getName(_cflds));
				bean.setOrgFld(getCode(_oflds));
				bean.setOrgName(getName(_oflds));
				bean.setName(_name);
				bean.ins();
			} else {
				bean.setTranCode(_tb.getClazz().getName());
				bean.setTranName(_tb.getName());
				bean.setIsForm(isForm ? (byte) 1 : 0);
				bean.setUserFld(getCode(_uflds));
				bean.setUserName(getName(_uflds));
				bean.setDeptFld(getCode(_dflds));
				bean.setDeptName(getName(_dflds));
				bean.setCellFld(getCode(_cflds));
				bean.setCellName(getName(_cflds));
				bean.setOrgFld(getCode(_oflds));
				bean.setOrgName(getName(_oflds));
				bean.upd();
			}
			PrvTranDataDAO.add(bean);
		}
	}
}
