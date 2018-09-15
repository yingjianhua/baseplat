package irille.core.sys;

import irille.action.ActionBase;
import irille.core.prv.PrvRole;
import irille.core.prv.PrvRoleAct;
import irille.core.prv.PrvRoleActDAO;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.Act;
import irille.pub.tb.Tb;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class SysMenuDAO {
	public static final Log LOG = new Log(SysMenuDAO.class);

	// 请求代码-菜单功能缓存
	private static HashMap<String, SysMenuAct> _requestMap = new HashMap<String, SysMenuAct>();

	// Env环境信息调用 sys_SysUser_list
	public static SysMenuAct getByReq(String path) {
		if (_requestMap.containsKey(path))
			return _requestMap.get(path);
		String[] ps = path.split("\\_");
		SysMenuAct menuact = null;
		if (ps.length < 3) {
			menuact = new SysMenuAct().init();
			menuact.setCode(path);
			menuact.setName(path);
			if (path.equals("download"))
				menuact.setName("下载");
		} else {
			String actinname = "irille.action." + ps[0] + "." + ps[1] + "Action";
			ActionBase action = (ActionBase)ClassTools.newInstance(ClassTools.getClass(actinname));
			Class clazz = action.beanClazz();
			List<SysMenuAct> mlist = BeanBase.list(SysMenuAct.class,
			    Idu.sqlString("{0}=? and {1}=?", SysMenuAct.T.TABLE_CODE, SysMenuAct.T.CODE), false, clazz.getName(), ps[2]);
			if (mlist.size() > 0) // 标准功能情况
				menuact = mlist.get(0);
			else { // 其他类型请求
				Tb tb = Tb.getTBByBean(clazz);
				menuact = new SysMenuAct().init();
				menuact.setTableCode(ps[1]);
				menuact.setTableName(tb.getName());
				menuact.setCode(path);
				if (ps[2].equals("initAct"))
					menuact.setName("加载功能");
				else if (ps[2].equals("loadMenu"))
					menuact.setName("加载菜单");
				else if (ps[2].equals("load"))
					menuact.setName("加载数据");
				else if (ps[2].equals("getComboTrigger"))
					menuact.setName("加载关联数据");
				else
					menuact.setName("其他加载");
			}
		}
		_requestMap.put(path, menuact);
		return menuact;
	}

	public static List<SysMenu> listByType(String type) {
		String where = Idu.sqlString("{0}=? order by {1}", SysMenu.T.TYPE, SysMenu.T.SORT);
		return BeanBase.list(SysMenu.class, false, where, 0, 0, type);
	}

	public static SysMenu chkByUrl(String url) {
		String sql = Idu.sqlString("select * from {0} where {1}=?", SysMenu.class, SysMenu.T.URL);
		return (SysMenu) BeanBase.chkUnique(SysMenu.class, sql, false, url);
	}

	public static SysMenu chkByName(String name, String type) {
		String where = Idu.sqlString("{0}=? and {1}=?", SysMenu.T.NAME, SysMenu.T.TYPE);
		List<SysMenu> list = BeanBase.list(SysMenu.class, false, where, 0, 0, name, type);
		if (list.size() != 1)
			return null;
		return list.get(0);
	}

	/**
	 * 普通菜单
	 * 
	 * @param name
	 * @param type
	 * @param url
	 * @param order
	 * @return
	 */
	private SysMenu procMenu(String name, String type, String url, int order, SysMenu up) {
		System.out.println("--------------" + name);
		SysMenu menu = null;
		if (url == null) // 非叶节点情况
			menu = chkByName(name, type);
		else
			menu = chkByUrl(url);
		if (menu != null) {
			menu.setName(name);
			menu.setSort((short) order);
			menu.stMenuUp(up);
			menu.setType(type);
			menu.upd();
			return menu;
		}
		menu = new SysMenu();
		menu.setUrl(url);
		menu.setName(name);
		menu.setSort((short) order);
		menu.setType(type);
		menu.stMenuUp(up);
		menu.ins();
		return menu;
	}

	/**
	 * 更改交易功能信息
	 * 
	 * @param tb实体对象
	 * @param bean所属菜单
	 * @param needRoles需要的功能
	 *          | null表示全部
	 */
	private void procMenuAct(Tb tb, SysMenu bean, String needRoles) {
		Vector<Act> acts = tb.getActs();
		Vector<String> vcode = new Vector<String>();
		SysTable table = SysTable.chkUniqueCode(false, tb.getClazz().getName());
		if (table == null)
			throw LOG.err("err", "请先初始化表：" + tb.getCode());
		for (Act act : acts) {
			if (needRoles != null && needRoles.contains(act.getCode()) == false)
				continue; // 过滤功能
			vcode.add(act.getCode());
			SysTableAct tableAct = SysTableAct.chkUniqueTableCode(false, table.getPkey(), act.getCode());
			SysMenuAct beanAct = SysMenuAct.chkUniqueMenuAct(false, bean.getPkey(), tableAct.getPkey());
			if (beanAct == null) {
				beanAct = new SysMenuAct().init();
				beanAct.stMenu(bean);
				beanAct.stAct(tableAct);
				beanAct.setTableCode(table.getCode());
				beanAct.setTableName(table.getName());
				beanAct.setCode(tableAct.getCode());
				beanAct.setName(tableAct.getName());
				beanAct.setCss(tableAct.getCss());
				beanAct.setIco(tableAct.getIco());
				beanAct.ins();
			} else {
				beanAct.setTableCode(table.getCode());
				beanAct.setTableName(table.getName());
				beanAct.setCode(tableAct.getCode());
				beanAct.setName(tableAct.getName());
				beanAct.setCss(tableAct.getCss());
				beanAct.setIco(tableAct.getIco());
				beanAct.upd();
			}
		}
		// 删除不用的功能
		List<SysMenuAct> list = Idu.getLines(SysMenuAct.T.MENU, bean.getPkey());
		for (SysMenuAct line : list) {
			if (vcode.contains(line.gtAct().getCode()) == false) {
				PrvRoleActDAO.delByMenuAct(line);
				line.del();
			}
		}
	}

	// 增加父节点菜单
	public SysMenu procParent(String name, String type, int order, SysMenu up) {
		if (_menuList == null)
			_menuList = new ArrayList<SysMenu>();
		SysMenu bean = procMenu(name, type, null, order, up);
		_menuList.add(bean);
		// 无TB的情况--无功能
		return bean;
	}

	public static void setMenuList() {
		_menuList = new ArrayList();
	}

	// 处理普通菜单与功能
	public SysMenu proc(Tb tb, int order, SysMenu up) {
		if (_menuList == null) //TODO 重新加回
			_menuList = new ArrayList<SysMenu>();
		String type = tb.getClazz().getName().split("\\.")[2];
		String url = "view." + type + "." + tb.getClazz().getSimpleName() + ".List";
		SysMenu bean = procMenu(tb.getName(), type, url, order, up);
		_menuList.add(bean);
		procMenuAct(tb, bean, null);
		return bean;
	}

	public SysMenu proc(Tb tb, int order, SysMenu up, String type) {
		return proc(tb, tb.getName(), type, order, up);
	}

	/**
	 * 处理普通菜单与功能 type可以sysUser放到其他模块的菜单下 url还是在原来的sys下面
	 */
	public SysMenu proc(Tb tb, String name, String type, int order, SysMenu up) {
		if (_menuList == null)
			_menuList = new ArrayList<SysMenu>();
		String type2 = tb.getClazz().getName().split("\\.")[2];
		String url = "view." + type2 + "." + tb.getClazz().getSimpleName() + ".List";
		SysMenu bean = procMenu(name, type, url, order, up);
		_menuList.add(bean);
		procMenuAct(tb, bean, null);
		return bean;
	}

	/**
	 * 处理普通菜单与功能
	 * 
	 * @param tb对应功能的取值来源
	 * @param name菜单名
	 * @param type
	 *          菜单类型
	 * @param url菜单URL
	 *          | 不包含MVC.
	 * @param order排序
	 * @param up上级菜单
	 * @param needRoles仅加入需要的功能
	 * @return
	 */
	public SysMenu proc(Tb tb, String name, String type, String url, int order, SysMenu up, String needRoles) {
		if (_menuList == null)
			_menuList = new ArrayList<SysMenu>();
		SysMenu bean = procMenu(name, type, url, order, up);
		_menuList.add(bean);
		procMenuAct(tb, bean, needRoles);
		return bean;
	}

	public SysMenu proc(Tb tb, String name, String type, String url, int order, SysMenu up) {
		return proc(tb, name, type, url, order, up, null);
	}

	private static ArrayList<SysMenu> _menuList;

	public static void procClearBean() {
		String ids = "";
		for (SysMenu line : _menuList) {
			ids += "," + line.getPkey();
		}
		List<SysMenu> list = BeanBase.list(SysMenu.class, "pkey not in (" + ids.substring(1) + ")", false);
		for (SysMenu line : list) {
			List<SysMenuAct> actList = Idu.getLines(SysMenuAct.T.MENU, line.getPkey());
			for (SysMenuAct actLine : actList) {
				BeanBase.executeUpdate(Idu.sqlString("delete from {0} where {1} =" + actLine.getMenu(), PrvRoleAct.class,
				    PrvRoleAct.T.MENU));
				actLine.del();
			}
			line.del();
		}
		// 下面处理因，同个TB重复操作，直接替换的情况
		List<SysMenuAct> list2 = BeanBase.list(SysMenuAct.class,
		    Idu.sqlString("{0} not in (select pkey from {1})", SysMenuAct.T.MENU, SysMenu.class), false);
		for (SysMenuAct actLine : list2) {
			PrvRoleActDAO.delByMenuAct(actLine);
			actLine.del();
		}
	}

	public static void procClearBean(String packageCode) {
		String ids = "";
		for (SysMenu line : _menuList) {
			ids += "," + line.getPkey();
		}
		List<SysMenu> list = BeanBase.list(SysMenu.class, "pkey not in (" + ids.substring(1) + ")", false);
		for (SysMenu line : list) {
			//XXX
			if (!line.gtTable().getCode().substring(0, packageCode.length()).equals(packageCode)) {
				System.out.println("忽略表" + line.gtTable().getCode());
				continue;
			}
			if (true) {
				System.out.println("操作表" + line.gtTable().getCode());
				continue;
			}

			List<SysMenuAct> actList = Idu.getLines(SysMenuAct.T.MENU, line.getPkey());
			for (SysMenuAct actLine : actList) {
				PrvRoleActDAO.delByMenuAct(actLine);
				actLine.del();
			}
			line.del();
		}
		// 下面处理因，同个TB重复操作，直接替换的情况
		List<SysMenuAct> list2 = BeanBase.list(SysMenuAct.class,
		    Idu.sqlString("{0} not in (select pkey from {1})", SysMenuAct.T.MENU, SysMenu.class), false);
		for (SysMenuAct actLine : list2) {
			PrvRoleActDAO.delByMenuAct(actLine);
			actLine.del();
		}
	}

	// 给1:管理员所有权限
	public static void procAddAll() {
		PrvRole admin = BeanBase.load(PrvRole.class, 1);
		List<SysMenu> list = BeanBase.list(SysMenu.class, "", false);
		for (SysMenu line : list) {
			int index = 1;
			boolean isins = false;
			PrvRoleAct roleact = PrvRoleAct.chkUniqueRoleMenu(false, admin.getPkey(), line.getPkey());
			if (roleact == null) {
				roleact = new PrvRoleAct();
				roleact.setRole(admin.getPkey());
				roleact.setMenu(line.getPkey());
				isins = true;
			} else {
				roleact.clearAct();
			}
			String where = Idu.sqlString("{0}=?", SysMenuAct.T.MENU);
			List<SysMenuAct> actlist = BeanBase.list(SysMenuAct.class, where, false, line.getPkey());
			for (SysMenuAct act : actlist) {
				roleact.stAct(index, act.getPkey());
				index++;
			}
			if (isins)
				roleact.ins();
			else
				roleact.upd();
		}
	}

}
