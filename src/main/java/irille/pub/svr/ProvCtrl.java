package irille.pub.svr;

import irille.action.sys.SysMenuAction;
import irille.core.prv.PrvRoleActDAO;
import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuAct;
import irille.core.sys.SysMenuDAO;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProvCtrl {
	private static final Log LOG = new Log(ProvCtrl.class);
	public static final String ID_MENU = "Menu_";
	// <用户主键##菜单模块, json值>
	private static HashMap<String, String> _userMenuMap = new HashMap<String, String>();
	// <用户主键##菜单主键, acts值>---------
	private static HashMap<String, String> _umActsMap = new HashMap<String, String>();
	private static HashSet<Integer> _isActSet = new HashSet<Integer>();
	private static HashMap<Integer, String> _uTypeMap = new HashMap<Integer, String>(); // 用户主键,模块集合
	// 功能-代码缓存
	private static HashMap<Integer, SysMenuAct> _actMap = new HashMap<Integer, SysMenuAct>();
	private static HashMap<Integer, SysMenu> _menuMap = new HashMap<Integer, SysMenu>();
	private static ProvCtrl INSTANCE = new ProvCtrl();

	private ProvCtrl() {
		/**
		 * 初始化数据>>菜单与菜单功能
		 * 避免重复读取数据库
		 */
		List<SysMenuAct> list = BeanBase.list(SysMenuAct.class, "", false);
		for (SysMenuAct line : list)
			_actMap.put(line.getPkey(), line);
		List<SysMenu> list2 = BeanBase.list(SysMenu.class, "", false);
		for (SysMenu line : list2)
			_menuMap.put(line.getPkey(), line);
	}

	public static ProvCtrl getInstance() {
		return INSTANCE;
	}

	public void clearAll() {
		_userMenuMap.clear();
		_umActsMap.clear();
		_isActSet.clear();
		_uTypeMap.clear();
	}

	public void clear(Integer user) {
		Iterator it = _userMenuMap.keySet().iterator();
		String str;
		while (it.hasNext()) {
			str = (String) it.next();
			if (str.contains(user + "##"))
				it.remove();
		}
		it = _umActsMap.keySet().iterator();
		while (it.hasNext()) {
			str = (String) it.next();
			if (str.contains(user + "##"))
				it.remove();
		}
		_isActSet.remove(user);
		_uTypeMap.remove(user);
	}

	public String initActSet() {
		int user = Env.INST.getTran().getUser().getPkey();
		// 初始化权限相关数据
		if (_isActSet.contains(new Integer(user)) == false) {
			HashSet<Integer> actSet = PrvRoleActDAO.listByUser(Env.INST.getTran().getUser());
			HashSet<String> typeSet = new HashSet<String>(); // 模块SET
			for (Integer actPkey : actSet) {
				SysMenuAct ma = _actMap.get(actPkey);
				typeSet.add(_menuMap.get(ma.getMenu()).getType());
				String key = user + "##" + ma.getMenu();
				String acts = _umActsMap.get(key);
				if (acts == null)
					acts = "";
				acts += ma.getCode() + ",";
				_umActsMap.put(key, acts);
			}
			String types = "";
			for (String std : typeSet)
				types += "," + std;
			
//			for (String std : SysMenuAction.MENUMAP.keySet())
//				if (typeSet.contains(std))
//					types += "," + std;
			
			_uTypeMap.put(new Integer(user), Str.isEmpty(types) ? "" : types.substring(1));
			_isActSet.add(new Integer(user));
		}
		return _uTypeMap.get(new Integer(user));
	}

	// 根据类型与当前用户，取MENU的JSON
	public String getMenuByType(String type) throws JSONException {
		//当功能权限修改后，会清空大部分的缓存数据
		//由于目前菜单是异步加载，所以每次请求都检查用户的功能权限缓存中是否已经初始化了
		initActSet();
		int user = Env.INST.getTran().getUser().getPkey();
		String json = _userMenuMap.get(user + "##" + type);
		if (json != null)
			return json;
		// 取某模块下的所有MENU对象
		List<SysMenu> list = SysMenuDAO.listByType(type);
		ArrayList<ProvLine> plineAry = new ArrayList<ProvCtrl.ProvLine>();
		ProvLine pline = new ProvLine();
		plineAry.add(pline); // 第一个
		for (SysMenu menu : list) {
			ProvLine fline = pline.addMenu(menu);
			if (pline != fline) {
				plineAry.add(fline);
				pline = fline;
			}
			String acts = _umActsMap.get(user + "##" + menu.getPkey());
			// 原来给分类统计的菜单做，已废除
			// if (acts == null && menu.getMenuUrl() != null && menu.getMenuUp() !=
			// null)
			// acts = _umActsMap.get(user + "##" + menu.getMenuUp());
			fline.setActs(menu, acts);
		}
		JSONArray ja = new JSONArray();
		for (ProvLine line : plineAry) {
			JSONObject lj = line.toJson(type);
			if (lj != null)
				ja.put(lj);
		}
		String rtn = ja.length() > 0 ? ja.toString() : "";
		_userMenuMap.put(user + "##" + type, rtn);
		return rtn;
	}

	//更改角色功能权限界面中调用
	public JSONArray crtRes(Integer pkey, String type) throws JSONException {
		JSONArray ja = new JSONArray();
		HashSet<Integer> setAct = PrvRoleActDAO.listByRole(pkey, PrvRoleActDAO.TYPE_MY);
		HashSet<Integer> setActSub = PrvRoleActDAO.listByRole(pkey, PrvRoleActDAO.TYPE_SUB);
		List<SysMenu> listBean = SysMenuDAO.listByType(type);
		for (SysMenu bean : listBean) {
			JSONObject json = new JSONObject();
			List<SysMenuAct> listAct = Idu.getLines(SysMenuAct.T.MENU, bean.getPkey());
			if (listAct.size() == 0) // 某些菜单没有功能的，不在界面上显示
				continue;
			JSONArray jaact = new JSONArray();
			JSONArray jaactsub = new JSONArray();
			for (SysMenuAct act : listAct) {
				JSONObject j = new JSONObject();
				j.put("mc", act.getName());
				j.put("pkey", act.getPkey());
				j.put("checked", setAct.contains(act.getPkey()));
				jaact.put(j);
				JSONObject jsub = new JSONObject();
				jsub.put("mc", act.getName());
				jsub.put("pkey", act.getPkey());
				jsub.put("checked", setActSub.contains(act.getPkey()));
				jaactsub.put(jsub);
			}
			json.put("acts", jaact);
			json.put("actssub", jaactsub);
			String upName = getUpMenuName(bean);
			json.put("title", upName + bean.getName());
			ja.put(json);
		}
		return ja;
	}

	public String getUpMenuName(SysMenu bean) {
		Integer up = bean.getMenuUp();
		if (up != null) {
			SysMenu upBean = _menuMap.get(up);
			up = upBean.getMenuUp();
			if (up != null)
				upBean = _menuMap.get(up);
			return upBean.getName() + "-";
		}
		return "";
	}

	/**
	 * 菜单对象
	 * 目前只支持到三级
	 * @author whx
	 */
	public class ProvLine {
		public SysMenu _menuOne;
		public ArrayList<SysMenu> _menuTwoList;
		public HashMap<Integer, ArrayList<SysMenu>> _menuThrMap;
		public HashMap<Integer, String> _actsMap;

		public ProvLine() {
		}

		public ProvLine addMenu(SysMenu menu) {
			// 初始化一级菜单
			if (_menuOne == null) {
				_menuOne = menu;
				return this;
			}
			// 一级菜单情况
			if (menu.getMenuUp() == null) {
				ProvLine line = new ProvLine();
				return line.addMenu(menu);
			}
			int level = validate(menu);
			// 二级菜单
			if (level == 2) {
				if (_menuTwoList == null)
					_menuTwoList = new ArrayList<SysMenu>();
				_menuTwoList.add(menu);
				return this;
			}
			// 三级菜单
			if (level == 3) {
				if (_menuThrMap == null)
					_menuThrMap = new HashMap<Integer, ArrayList<SysMenu>>();
				ArrayList<SysMenu> ary = _menuThrMap.get(menu.getMenuUp());
				if (ary == null) {
					ary = new ArrayList<SysMenu>();
					_menuThrMap.put(menu.getMenuUp(), ary);
				}
				ary.add(menu);
				return this;
			}
			throw LOG.err("errOrder", "不可超过3级菜单");
		}

		// 设置对应菜单的权限
		private void setActs(SysMenu menu, String acts) {
			if (acts == null)
				return;
			if (_actsMap == null)
				_actsMap = new HashMap<Integer, String>();
			_actsMap.put(menu.getPkey(), acts);
		}

		// 上下级菜单排序错误检查
		private int validate(SysMenu menu) {
			if (menu.getMenuUp().intValue() == _menuOne.getPkey().intValue())
				return 2;
			if (_menuTwoList != null) {
				for (SysMenu line : _menuTwoList)
					if (menu.getMenuUp().intValue() == line.getPkey().intValue())
						return 3;
			}
			throw LOG.err("errOrder", "上下级菜单排序错误[" + menu.getName() + "]");
		}

		public JSONObject toJson(String type) throws JSONException {
			if (_actsMap == null)
				return null; // 没有任务权限，无此交易
			boolean isleaf = true;
			if (_menuTwoList != null)
				isleaf = false;
			String act1 = _actsMap.get(_menuOne.getPkey());
			JSONObject json = new JSONObject();
			json.put("id", getMenuId(_menuOne)).put("text", _menuOne.getName()).put("leaf", isleaf + "")
			    .put("cls", isleaf ? "file" : "floder").put("url", _menuOne.getUrl()).put("beanType", type)
			    .put("roles", act1);
			// 下面是二级菜单的处理
			if (isleaf == false) {
				JSONArray ja2 = new JSONArray();
				for (SysMenu line2 : _menuTwoList) {
					boolean isleaf2 = true;
					if (_menuThrMap != null && _menuThrMap.containsKey(line2.getPkey()))
						isleaf2 = false;
					String acts2 = _actsMap.get(line2.getPkey());
					JSONObject json2 = new JSONObject();
					json2.put("id", getMenuId(line2)).put("text", line2.getName()).put("leaf", isleaf2 + "")
					    .put("cls", isleaf2 ? "file" : "floder").put("url", line2.getUrl()).put("beanType", type)
					    .put("roles", acts2);
					// 下面是三级菜单处理--目前系统肯定是叶节点
					if (isleaf2 == false) {
						ArrayList<SysMenu> list3 = _menuThrMap.get(line2.getPkey());
						JSONArray ja3 = new JSONArray();
						for (SysMenu line3 : list3) {
							String acts3 = _actsMap.get(line3.getPkey());
							if (acts3 == null)
								continue;
							JSONObject json3 = new JSONObject();
							json3.put("id", getMenuId(line3)).put("text", line3.getName()).put("leaf", "true").put("cls", "file")
							    .put("url", line3.getUrl()).put("beanType", type).put("roles", acts3);
							ja3.put(json3);
						}
						if (ja3.length() > 0) // 有权限的情况
							json2.put("children", ja3);
					}
					// 二级节点无权限并无下级菜单时跳过
					if (acts2 == null && json2.has("children") == false)
						continue;
					ja2.put(json2);
				}
				if (ja2.length() > 0)
					json.put("children", ja2);
			}
			// 一级节点无权限并无下级菜单时跳过
			if (act1 == null && json.has("children") == false)
				return null;
			return json;
		}

		private String getMenuId(SysMenu menu) {
			return ID_MENU + menu.getPkey();
		}
	}

}
