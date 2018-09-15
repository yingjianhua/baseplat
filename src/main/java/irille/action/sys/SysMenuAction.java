package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysMenu;
import irille.core.sys.SysModule;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.svr.ProvCtrl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SysMenuAction extends ActionBase<SysMenu> {
	public static final Log LOG = new Log(SysMenuAction.class);
	private String _type;
	private String _jyType;
	private String _jumpUrl;

	public static LinkedHashMap<String, String> MENUMAP = new LinkedHashMap<String, String>();

	//	static {
	//		MENUMAP.put("sal", "销售管理");
	//		MENUMAP.put("pur", "采购管理");
	//		MENUMAP.put("goods", "货物管理");
	//		MENUMAP.put("gs", "存货管理");
	//		MENUMAP.put("cst", "成本核算");
	//		MENUMAP.put("rp", "出纳管理");
	//		MENUMAP.put("pya", "应收应付");
	//		MENUMAP.put("gl", "财务管理");
	//		MENUMAP.put("tp", "目标管理");
	//		MENUMAP.put("cms", "网站管理");
	//		MENUMAP.put("ykt", "一卡通管理");
	//		MENUMAP.put("sys", "系统管理");
	//	}

	//['sal','pur','goods','gs','cst','rp','pya','gl','tp','cms','ykt','sys'];

	//由系统运行时调用，初始化所有安装模块下的菜单
	public static void initMenus() {
		if (MENUMAP.size() > 0)
			return;
		List<SysModule> list = BeanBase.list(SysModule.class, "1=1", false);
		ArrayList<String> ary = new ArrayList<String>();
		for (SysModule line : list) {
			if (Str.isEmpty(line.getMenus()))
				continue;
			String[] ss = line.getMenus().split(",");
			for (String s : ss)
				ary.add(s);
		}
		//Collections.sort(ary, (ol, o2)->o1.split("-")[2].compareTo(o2.split("-")[2]));
		
		Collections.sort(ary, new Comparator<String>() {
			public int compare(String o1, String o2) {
				return o1.split("-")[2].compareTo(o2.split("-")[2]);
			}
		});
		for (String line : ary) {
			String[] ss = line.split("\\-");
			MENUMAP.put(ss[0], ss[1]);
			System.out.println(ss[0]+"||||||"+ss[1]);
		}
	}

	@Override
	public Class beanClazz() {
		return SysMenu.class;
	}

	public SysMenu getBean() {
		return _bean;
	}

	public void setBean(SysMenu bean) {
		this._bean = bean;
	}

	public String getType() {
		return _type;
	}

	public void setType(String type) {
		_type = type;
	}

	public void setId(String pkey) {
		// TODO 与EXT有关
		// 加载MENU时，前台自动带ID=ROOT参数，与底层load方法时用户的setId冲突
	}

	public void setJyType(String _jyType) {
		this._jyType = _jyType;
	}

	public String getJyType() {
		return _jyType;
	}

	public String getJumpUrl() {
		return _jumpUrl;
	}

	public void setJumpUrl(String jumpUrl) {
		_jumpUrl = jumpUrl;
	}

	/**
	 * 用户功能缓存初始化 前台EXT加载时AJAX同步初台化后，再开始加载菜单
	 * @throws Exception
	 */
	public void initAct() throws Exception {
		String types = ProvCtrl.getInstance().initActSet();
		if (Str.isEmpty(types))
			return;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(types);
	}

	public void loadMenu() throws Exception {
		if (Str.isEmpty(getType()))
			return;
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(ProvCtrl.getInstance().getMenuByType(getType()));
	}

	public void loadAuthorityByRole() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(ProvCtrl.getInstance().crtRes((Integer) getPkey(), getJyType()).toString());
	}

	//界面进行跳转时调用
	public void loadJump() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		String allmenus = ProvCtrl.getInstance().getMenuByType(getType());
		JSONArray ja = new JSONArray(allmenus);
		JSONObject submenu = getSubmenu(ja);
		if (submenu != null) {
			json.put("menu", submenu);
		}
		if (json.has("menu") == false)
			throw LOG.err("noprv", "您没有查看的权限!");
		String types = ProvCtrl.getInstance().initActSet(); //有权限的模块
		json.put("success", true);
		json.put("types", types);
		response.getWriter().print(json.toString());
	}

	private JSONObject getSubmenu(JSONArray ja) throws JSONException {
		for (int i = 0; i < ja.length(); i++) {
			JSONObject submenu = ja.getJSONObject(i);
			if (submenu.has("children") == true) {
				JSONObject n = getSubmenu(submenu.getJSONArray("children"));
				if (n != null)
					return n;
			}
			if (submenu.has("url") == false)
				continue;
			if (submenu.getString("url").equals(getJumpUrl()) == false)
				continue;
			return submenu;
		}
		return null;
	}

	//功能权限页面，获取模块下拉列表数据
	public void getMenuName() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONArray ja = new JSONArray();
		for (String line : MENUMAP.keySet()) {
			JSONObject json = new JSONObject();
			json.put("value", line);
			json.put("text", MENUMAP.get(line));
			ja.put(json);
		}
		JSONObject js = new JSONObject();
		js.put("items", ja);
		response.getWriter().print(js.put("success", true).toString());
	}
}

