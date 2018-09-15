//Created on 2005-9-27
package irille.core.sys;

import irille.core.prv.PrvRoleActDAO;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.Act;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Vector;

public class SysTableDAO {
	public static final Log LOG = new Log(SysTableDAO.class);

	//停用用
//	public static final void initInt(BeanInt bean) {
//		SysTable table = SysTable.chkUniqueCode(true, bean.getClass().getName());
//		table.setSeqnum(table.getSeqnum() + 1);
//		table.upd();
//		bean.setPkey(table.getSeqnum().intValue() * 100 + table.getPkey());
//	}
//
//	public static final void initLong(BeanLong bean) {
//		SysTable table = SysTable.chkUniqueCode(true, bean.getClass().getName());
//		table.setSeqnum(table.getSeqnum() + 1);
//		table.upd();
//		bean.setPkey(table.getSeqnum() * 100000 + table.getPkey());
//	}

	// 以下全部为初始化相关逻辑代码=============================

	private static void initByTb(int pkey, Tb tb) throws Exception {
		initByTb(pkey, tb, Sys.OTableType.SYS);
	}

	/**
	 * 初始化系统表
	 * 
	 * @param tb
	 * @param IEnumOpt
	 *          表类型
	 */
	public static void initByTb(int pkey, Tb tb, IEnumOpt type) {
		// 系统表
		SysTable t = SysTable.chkUniqueCode(false, tb.getClazz().getName());
		if (t == null) {
			t = new SysTable().init();
			t.setPkey(pkey);
			t.setCode(tb.getClazz().getName());
			t.setName(tb.getName());
			t.setShortName(tb.getShortName());
			t.setType(type.getLine().getKey());
			t.ins();
		} else {
			t.setPkey(pkey);
			t.setName(tb.getName());
			t.setShortName(tb.getShortName());
			t.setType(type.getLine().getKey());
			t.upd();
		}
		_tableList.add(t);
		// 系统表功能
		initActs(tb, t);

		SysSeqDAO.init(tb); // 如果tb的Bean实现ISeq接口，则初始化其序号信息
	}

	private static void initActs(Tb tb, SysTable table) {
		Vector<Act> acts = tb.getActs();
		Vector<String> vcode = new Vector<String>();
		String code;
		for (Act act : acts) {
			code = act.getCode();
			vcode.add(code);
			SysTableAct tableact = SysTableAct.chkUniqueTableCode(false,
					table.getPkey(), code);
			if (tableact != null) {
				tableact.setName(act.getName());
				tableact.upd();
				continue;
			}
			tableact = new SysTableAct().init();
			tableact.stSysTable(table);
			tableact.setName(act.getName());
			tableact.setCode(act.getCode());
			tableact.ins();
		}
		// 删除不用的功能
		List<SysTableAct> list = Idu.getLines(SysTableAct.T.SYS_TABLE.getFld(),
				table.getPkey());
		for (SysTableAct line : list) {
			if (vcode.contains(line.getCode()) == false) {
				List<SysMenuAct> maList = Idu.getLines(SysMenuAct.T.ACT.getFld(),
						line.getPkey());
				for (SysMenuAct mline : maList) {
					PrvRoleActDAO.delByMenuAct(mline);
					mline.del();
				}
				line.del();
			}
		}
	}

	private static ArrayList<SysTable> _tableList;
	
	public static void setTableList(){
		_tableList=new ArrayList();
	}

	// 删除table清理相关数据库关联
	public static void procClearBean() {
		String ids = "";
		for (SysTable line : _tableList) {
			ids += "," + line.getPkey();
		}
		HashSet<SysMenu> set = new HashSet<SysMenu>(); // 缓存准备删除的菜单对象
		List<SysTable> list = BeanBase.list(SysTable.class,
				"pkey not in (" + ids.substring(1) + ")", false);
		for (SysTable line : list) {
			List<SysTableAct> actList = Idu.getLines(
					SysTableAct.T.SYS_TABLE.getFld(), line.getPkey());
			for (SysTableAct actLine : actList) {
				List<SysMenuAct> maList = Idu.getLines(SysMenuAct.T.ACT.getFld(),
						actLine.getPkey());
				for (SysMenuAct mline : maList) {
					set.add(mline.gtMenu());
					PrvRoleActDAO.delByMenuAct(mline);
					mline.del();
				}
				actLine.del();
			}
			line.del();
		}
		for (SysMenu line : set)
			line.del();
	}

	// 删除table清理相关数据库关联
	public static void procClearBean(String packageCode) {
		String ids = "";
		for (SysTable line : _tableList) {
			ids += "," + line.getPkey();
		}
		HashSet<SysMenu> set = new HashSet<SysMenu>(); // 缓存准备删除的菜单对象
		List<SysTable> list = BeanBase.list(SysTable.class,
				"pkey not in (" + ids.substring(1) + ")", false); // 删除包的多余表相关信息
		for (SysTable line : list) {
			if (!line.getCode().substring(0, packageCode.length())
					.equals(packageCode)) {
				continue;
			}
			System.err.println("删除表档案：" + line.getCode());
			List<SysTableAct> actList = Idu.getLines(
					SysTableAct.T.SYS_TABLE.getFld(), line.getPkey());
			for (SysTableAct actLine : actList) {
				List<SysMenuAct> maList = Idu.getLines(SysMenuAct.T.ACT.getFld(),
						actLine.getPkey());
				for (SysMenuAct mline : maList) {
					set.add(mline.gtMenu());
					PrvRoleActDAO.delByMenuAct(mline);
					mline.del();
				}
				actLine.del();
			}
			line.del();
		}
		for (SysMenu line : set)
			line.del();
	}

}