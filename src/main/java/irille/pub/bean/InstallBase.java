package irille.pub.bean;

import irille.core.sys.SysMenuDAO;
import irille.core.sys.SysTableDAO;
import irille.pub.ClassTools;
import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.PackageBase.TbMsg;
import irille.pub.bean.PackageBase.TranDataMsg;
import irille.pub.svr.Env;
import irille.pub.svr.Svr;
import irille.pub.svr.Svr.IRunSvr;

import java.util.Hashtable;
import java.util.Vector;

import junit.framework.TestResult;

///*
// * 系统参数初始化 ,可用方法: mkDirsInWorkDir 建立工作目录; mkDirsInProgramDir 程序目录下建立子目录;
// */
//@Override
//public void initParas() {
//}
///* 
// * 菜单的初始化操作
// * @see irille.pub.bean.InstallBase#initMenu(irille.core.sys.SysMenuDAO)
// */
//@Override
//public void initMenu(SysMenuDAO m) {
//}
///* 

/**
 * 每个Bean包都包含一个以包名为类名的类，继承自此类，在初始化时要构造一次实例进行一次初始化，以对模块进行必要的初始化
 * 
 * @author whx
 * 
 */
public abstract class InstallBase implements IRunSvr {
	private static final Log LOG = new Log(InstallBase.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		installInit("安装模块【{0}:{1}】数据初始化..."),
		setup("    正在安装设置模块【{0}:{1}】..."),
		setupDone("    模块【{0}】设置完成..."),
		tableId("表【{0}】序号【{1}】重复定义，与表【{2}】相同！"),
		table("表【{0}】重复定义！"),
		;
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	private Vector<String> _mkDirsInWorkDir = new Vector(); // 需要建立的工作子目录
	private Vector<String> _mkDirsInProgramDir = new Vector(); // 需要建立的程序子目录
	private PackageBase _package = null;

	public InstallBase() {
		// LOG.info("安装模块【{0}:{1}】数据初始化...", getClass(), "模块名");
		LOG.info(Msgs.installInit, getClass(), getName());
	}

	public abstract void initMenu(SysMenuDAO m);

	/**
	 * 安装的前处理 系统参数初始化 ,可用方法: mkDirsInWorkDir 建立工作目录; mkDirsInProgramDir
	 * 程序目录下建立子目录;
	 */
	public void installBefore() {
	}

	/**
	 * 安装的后处理
	 */
	public void installAfter() {
	}

	private static SysMenuDAO _menu = new SysMenuDAO();

	/**
	 * 安装系统
	 */
	public final void install() {
		LOG.info(Msgs.setup, getClass(), getName());
		mkDirs(Env.INST.getWorkDir(), getMkDirsInWorkDir()); //建立所需的目录
		mkDirs(Env.INST.getProgramDir(), getMkDirsInProgramDir());
		installBefore();
		updateTableAndCheckId();
		initMenu(_menu); // 装入菜单
		installAfter();
		LOG.info(Msgs.setupDone, getName());
	}

	/**
	 * 更新表信息并检查包的ID号是否重复
	 * 更新表权限信息
	 * 更新表模块信息
	 */
	private void updateTableAndCheckId() {
		Hashtable<Integer, Class> _tbsId = new Hashtable();
		Hashtable<Class, Integer> _tbs = new Hashtable();
		int i;
		//表信息
		for (TbMsg tb : (Vector<TbMsg>) getPackage().getTbMsgs()) {
			Class tbClazz = tb.getClazz();
			i = tb.getId();
			System.err.println(i + ":" + tbClazz.getName()); // XXX
			if (_tbsId.get(i) != null) {
				throw LOG.err(Msgs.tableId, tbClazz.getName(), i, _tbsId.get(i).getName());
			}
			_tbsId.put(i, tbClazz);

			if (_tbs.get(tbClazz) != null) {
				throw LOG.err(Msgs.table, tbClazz.getName());
			}
			_tbs.put(tbClazz, i);
			SysTableDAO.initByTb(i, tb.getTb(), tb.getTableType());
		}
		//权限信息
		for (TranDataMsg td : (Vector<TranDataMsg>) getPackage().getTranDatas()) 
			td.iu();
		//模块信息
		getPackage().initModule();
	}

	/**
	 * 建立目录
	 * 
	 * @param baseDir
	 * @param dirs
	 */
	private void mkDirs(String baseDir, Vector<String> dirs) {
		for (String dir : dirs) {
			FileTools.mkDir(baseDir + dir);
		}
	}

	/**
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	// @Override
	public int countTestCases() {
		Svr.init();
		SysMenuDAO.setMenuList();
		SysTableDAO.setTableList();
		Svr.run(this);
		return 0;
	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.svr.Svr.IRunSvr#runSvr()
	 */
	@Override
	public void runSvr() {
		install();
		SysTableDAO.procClearBean(Str.getClazzPackage(getClass()));
		_menu.procClearBean(Str.getClazzPackage(getClass()));
		_menu.procAddAll();
	}

	/**
	 * 取包的ID号
	 * 
	 * @return
	 */
	public int getPackageId() {
		return getPackage().getPackageId();
	}

	/**
	 * 在工作目录下建立子目录
	 * 
	 * @param dirs
	 */
	public void addMkDirsInWorkDir(String... dirs) {
		for (String dir : dirs)
			_mkDirsInWorkDir.add(dir);
	}

	/**
	 * 在程序目录下建立子目录
	 * 
	 * @param dirs
	 */
	public void addMkDirsInProgramDir(String... dirs) {
		for (String dir : dirs)
			_mkDirsInProgramDir.add(dir);
	}

	/**
	 * 取包名称
	 * 
	 * @return
	 */
	public String getName() {
		return getPackage().getName();
	}

	public PackageBase getPackage() {
		if (_package == null) {
			String pack = getClass().getName();
			_package = ((PackageBase) ClassTools.getStaticProerty(pack.substring(0, pack.length() - "_Install".length()),
			    "INST"));
		}
		return _package;
	}

	/**
	 * @return the mkDirsInWorkDir
	 */
	public Vector<String> getMkDirsInWorkDir() {
		return _mkDirsInWorkDir;
	}

	/**
	 * @return the mkDirsInProgramDir
	 */
	public Vector<String> getMkDirsInProgramDir() {
		return _mkDirsInProgramDir;
	}

	// @Override
	public void run(TestResult arg0) {
	}

	public final void testCase() {
	}
}
