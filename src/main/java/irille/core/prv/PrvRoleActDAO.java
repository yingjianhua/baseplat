package irille.core.prv;

import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuAct;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserRoleDAO;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class PrvRoleActDAO {
	public static final Log LOG = new Log(PrvRoleActDAO.class);

	/**
	 * 更新角色的功能权限
	 * @param role 角色对象
	 * @param type 模块类型
	 * @param lines 功能组 eg: 45,44,##46,47,##...
	 */
	public static void iud(PrvRole role, String type, String lines) {
		String ids = ""; //用于记录所有功能权限的主键集合，便于数据库多余的权限
		if (Str.isEmpty(lines) == false)
			for (String line : lines.split("\\##")) {
				if (Str.isEmpty(line))
					continue;
				//对一个菜单内的功能开始处理
				PrvRoleAct roleact = null;
				int index = 1;
				boolean isins = false; //新增或修改的标识
				for (String actline : line.split("\\,")) { //对功能开始循环处理
					if (Str.isEmpty(actline))
						continue;
					Integer actpkey = Integer.parseInt(actline);
					Integer menupkey = BeanBase.load(SysMenuAct.class, actpkey).getMenu();
					if (roleact == null) {
						roleact = PrvRoleAct.chkUniqueRoleMenu(false, role.getPkey(), menupkey);
						if (roleact == null) {
							roleact = new PrvRoleAct();
							roleact.stRole(role);
							roleact.setMenu(menupkey);
							isins = true;
						} else {
							roleact.clearAct();
						}
					}
					roleact.stAct(index, actpkey);
					index++;
				}
				if (isins)
					roleact.ins();
				else
					roleact.upd();
				ids += "," + roleact.getPkey();
			}
		//清除
		String sql = Idu.sqlString("delete from {0} where {1}=? and {2} in (select pkey from {3} where {4}=?)",
		    PrvRoleAct.class, PrvRoleAct.T.ROLE, PrvRoleAct.T.MENU, SysMenu.class, SysMenu.T.TYPE);
		if (Str.isEmpty(ids) == false)
			sql += " AND pkey not in (" + ids.substring(1) + ")";
		Bean.executeUpdate(sql, role.getPkey(), type);
	}

	public static int TYPE_MY = 1;
	public static int TYPE_SUB = 2;
	public static int TYPE_ALL = 3;

	/**
	 * 取角色已有的功能集合
	 * @param pkey 角色主键值
	 * @param sub 表示为取所有子角色的已有功能
	 * @return hashset避免重复值
	 */
	public static HashSet<Integer> listByRole(Integer pkey, int type) {
		HashSet<Integer> setAct = new HashSet();
		String where = Idu.sqlString("{0}=" + pkey, PrvRoleAct.T.ROLE);
		if (type != TYPE_MY) {
			Vector<Integer> vec = new Vector();
			PrvRoleDAO.getAllRoles(pkey, vec);
			if (type == TYPE_SUB)
				vec.remove(pkey); //过滤当前角色的功能
			if (vec.size() == 0)
				return setAct;
			String ids = "";
			for (Integer id : vec)
				ids += "," + id;
			where = PrvRoleAct.T.ROLE.getFld().getCode() + " in (" + ids.substring(1) + ")";
		}
		List<PrvRoleAct> list = BeanBase.list(PrvRoleAct.class, false, where, 0, 0);
		Integer actpkey = null;
		for (PrvRoleAct line : list) {
			for (int i = 1; i <= 12; i++) {
				actpkey = line.gtAct(i);
				if (actpkey != null)
					setAct.add(actpkey);
			}
		}
		return setAct;
	}

	/**
	 * 根据用户取出所有功能
	 * @param user
	 * @return
	 */
	public static HashSet<Integer> listByUser(SysUser user) {
		Vector<Integer> vec = SysUserRoleDAO.listAllRoleByUser(user); //所有角色集合
		String ids = "";
		Integer act = null;
		HashSet<Integer> setAct = new HashSet();
		if (vec.size() == 0)
			return setAct;
		for (Integer v : vec)
			ids += "," + v.intValue();
		String where = Idu.sqlString("{0} in (" + ids.substring(1) + ")", PrvRoleAct.T.ROLE);
		List<PrvRoleAct> actlist = BeanBase.list(PrvRoleAct.class, where, false);
		for (PrvRoleAct roleact : actlist) {
			for (int i=1; i<=12; i++) {
				act = roleact.gtAct(i);
				if (act != null)
					setAct.add(act);
			}
		}
		return setAct;
	}
	
	public static void delByMenuAct(SysMenuAct act) {
		String where = Idu.sqlString("{0}=?", PrvRoleAct.T.MENU);
		List<PrvRoleAct> list = BeanBase.list(PrvRoleAct.class, where, false, act.getMenu());
		for (PrvRoleAct line : list) {
			Vector<Integer> ary = new Vector();
			for (int i=1; i<=12; i++) {
				if (act.getPkey().equals(line.gtAct(i)) == false)
					ary.add(line.gtAct(i));
			}
			line.clearAct();
			for (int i=1; i<=ary.size(); i++)
				line.stAct(i, ary.get(i-1));
			if (ary.size()>0)
				line.upd();
			else
				line.del();
		}
	}

}
