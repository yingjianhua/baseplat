package irille.pub.svr;

import irille.core.prv.Prv;
import irille.core.prv.PrvRoleTran;
import irille.core.prv.PrvTranData;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysCustom;
import irille.core.sys.SysCustomOrg;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysSupplierOrg;
import irille.core.sys.SysUser;
import irille.core.sys.SysUserRoleDAO;
import irille.pss.sal.SalSale;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.Tb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

public class ProvDataCtrl {
	private static final Log LOG = new Log(ProvDataCtrl.class);
	private static String R_CODE = "#替换字段#";
	private static String R_VALUE = "#替换值#";
	private static String DATE_CODE = "created_time";
	//<表代码，交易资源 数组>
	private static HashMap<String, Vector<PrvTranData>> _mapTranData = new HashMap<String, Vector<PrvTranData>>();
	//<用户##表代码##资源名，资源权限对象(仅取权限最大的)>
	private static HashMap<String, PrvRoleTran> _mapRoleTran = new HashMap<String, PrvRoleTran>();
	//key：机构的代码，value：机构
	private static HashMap<String, String> _mapOrgExt = new HashMap<String, String>();
	private static HashMap<String, String> _mapDeptExt = new HashMap<String, String>();
	private static HashMap<String, String> _mapCellExt = new HashMap<String, String>();
	public static ProvDataCtrl INST = new ProvDataCtrl();

	private ProvDataCtrl() {
		//缓存 -- 初始化交易资源的基本信息
		List<PrvTranData> list = BeanBase.list(PrvTranData.class, "1=1", false);
		for (PrvTranData line : list) {
			Vector<PrvTranData> vec = _mapTranData.get(line.getTranCode());
			if (vec == null) {
				vec = new Vector<PrvTranData>();
				_mapTranData.put(line.getTranCode(), vec);
			}
			vec.add(line);
		}
		initDeptMap();
		initOrgMap();
		initCellMap();
	}

	public void clearAll() {
		_mapRoleTran.clear();
	}

	public void clear(Integer user) {
		Iterator it = _mapRoleTran.keySet().iterator();
		String str;
		while (it.hasNext()) {
			str = (String) it.next();
			if (str.contains(user + "##"))
				it.remove();
		}
	}

	public void clear(String tablecode) {
		Iterator it = _mapRoleTran.keySet().iterator();
		String str;
		while (it.hasNext()) {
			str = (String) it.next();
			if (str.contains(tablecode + "##"))
				it.remove();
		}
	}

	//初始化用户表，对应的最高权限类型
	public static PrvRoleTran getRoleTran(SysUser user, Class clazz, PrvTranData data) {
		String userClazz = user.getPkey() + "##" + clazz.getName() + "##" + data.getName();
		PrvRoleTran roletran = _mapRoleTran.get(userClazz);
		if (roletran == null) {
			Vector<Integer> vec = SysUserRoleDAO.listAllRoleByUser(user);
			for (Integer role : vec) {
				PrvRoleTran lineTran = PrvRoleTran.chkUniqueRoleGrp(false, role, data.getGrp());
				if (roletran == null || roletran.getType().byteValue() < lineTran.getType().byteValue())
					roletran = lineTran;
			}
			_mapRoleTran.put(userClazz, roletran);
		}
		return roletran;
	}
	/**
	 * 判断目标资源是否需要进行资源权限控制
	 * 
	 * @param clazz 目标资源的class
	 * @return 若需要进行控制返回true，不需要则返回false;
	 * @author yingjianhua
	 */
	public static boolean needControl(Class clazz) {
		return _mapTranData.get(clazz.getName()) != null;
	}
	
	public static String getWhere(SysUser user, Class clazz) {
		if (clazz.equals(SysCustom.class) || clazz.equals(SysSupplier.class))
			return getWhereCrm(user, clazz);
		Vector<PrvTranData> vec = _mapTranData.get(clazz.getName());
		if (vec == null) //没有初始化资源数据，则不作控制
			return "1=1";
		String where = "";
		Tb tb = Tb.getTBByBean(clazz);
		Calendar cal = Calendar.getInstance();
		for (PrvTranData line : vec) {
			String partsql = "";
			PrvRoleTran rt = getRoleTran(user, clazz, line);

			if (line.gtIsForm() && rt != null && rt.getDay().intValue() > 0) { //表单的天数控制
				cal.setTime(Env.INST.getSystemTime());
				cal.add(cal.DATE, -rt.getDay());
				partsql += DATE_CODE + ">='" + cal.getTime().toLocaleString().split("\\ ")[0] + "' AND ";//TODO toLocaleString 方法已经不推荐使用了，可以考虑换成DateFormat
				//partsql += DATE_CODE + ">='" + DateFormat.getDateTimeInstance().format(cal.getTime()).split("\\ ")[0] + "' AND ";
			}
			Prv.OPrvType type = rt == null ? Prv.OPrvType.NO : rt.gtType(); //未分配组作无权限处理
			if (type == Prv.OPrvType.NO)
				partsql += "1=2";
			else if (type == Prv.OPrvType.USER) {
				if (line.getUserFld() == null)
					partsql += "1=1";
				else
					partsql += getSqlPart(tb, line.getUserFld()).replace(R_VALUE, "=" + user.getPkey());
			} else if (type == Prv.OPrvType.DEPT) {
				if (line.getDeptFld() == null)
					partsql += "1=1";
				else
					partsql += getSqlPart(tb, line.getDeptFld()).replace(R_VALUE, "=" + user.getDept());
			} else if (type == Prv.OPrvType.DEPT_EXT) {
				if (line.getDeptFld() == null)
					partsql += "1=1";
				else
					/*
					 * partsql += getSqlPart(tb, line.getDeptFld()).replace(
					 * R_VALUE,
					 * " in (select pkey from sys_dept where code='?' or code like '?%')".
					 * replaceAll("\\?", user.gtDept()
					 * .getCode()));
					 */
					partsql += getSqlPart(tb, line.getDeptFld()).replace(R_VALUE,
					    " in (" + _mapDeptExt.get(user.gtDept().getPkey().toString()) + ")");
			} else if (type == Prv.OPrvType.CELL) {
				if (line.getCellFld() == null)
					partsql += "1=1";
				else
					partsql += getSqlPart(tb, line.getCellFld()).replace(R_VALUE, "=" + Idu.getCell().getPkey());
			} else if (type == Prv.OPrvType.CELL_EXT) {
				if (line.getCellFld() == null)
					partsql += "1=1";
				else
					/*
					 * partsql += getSqlPart(tb, line.getCellFld()).replace(
					 * R_VALUE,
					 * " in (select pkey from sys_cell where code='?' or code like '?%')".
					 * replaceAll("\\?", Idu.getCell()
					 * .getCode()));
					 */
					partsql += getSqlPart(tb, line.getCellFld()).replace(R_VALUE,
					    " in (" + _mapCellExt.get(Idu.getCell().getPkey().toString()) + ")");
			} else if (type == Prv.OPrvType.ORG) {
				if (line.getOrgFld() == null)
					partsql += "1=1";
				else
					partsql += getSqlPart(tb, line.getOrgFld()).replace(R_VALUE, "=" + user.getOrg());
			} else if (type == Prv.OPrvType.ORG_EXT) {
				if (line.getOrgFld() == null)
					partsql += "1=1";
				else
					/*
					 * partsql += getSqlPart(tb, line.getOrgFld())
					 * .replace(
					 * R_VALUE,
					 * " in (select pkey from sys_org where code='?' or code like '?%')".
					 * replaceAll("\\?", user.gtOrg()
					 * .getCode()));
					 */
					partsql += getSqlPart(tb, line.getOrgFld()).replace(R_VALUE,
					    " in (" + _mapOrgExt.get(user.gtOrg().getPkey().toString()) + ")");
			} else if (type == Prv.OPrvType.ALL) {
				return "1=1";
			}
			where += " OR " + partsql;
		}
		return "(" + where.substring(4) + ")";
	}

	//org   warehouse.org
	private static String getSqlPart(Tb tb, String fldcode) {
		String[] fs = fldcode.split("\\.");
		String where = R_CODE;
		Tb fldTb = tb;
		for (int i = 0; i < fs.length; i++) {
			String line = fs[i];
			if (i == fs.length - 1) {
				where = where.replace(R_CODE, Str.tranFieldToLineLower(line) + R_VALUE);
				continue;
			}
			FldOutKey fld = (FldOutKey) fldTb.get(line);
			fldTb = Tb.getTBByBean(fld.getOutTbClazz());
			where = where.replace(R_CODE, Str.tranFieldToLineLower(line) + " in (select pkey from " + fldTb.getCodeSqlTb()
			    + " where " + R_CODE + ")");
		}
		return where;
	}

	//客户、供应商--资源权限的特殊处理
	private static String getWhereCrm(SysUser user, Class clazz) {
		Vector<PrvTranData> vec = _mapTranData.get(clazz.getName());
		if (vec == null) //没有初始化资源数据，则不作控制
			return "1=1";
		String mainCode = SysCustomOrg.T.CUSTOM.getFld().getCodeSqlField();
		String tableCode = SysCustomOrg.TB.getCodeSqlTb();
		if (clazz.equals(SysSupplier.class)) {
			mainCode = SysSupplierOrg.T.SUPPLIER.getFld().getCodeSqlField();
			tableCode = SysSupplierOrg.TB.getCodeSqlTb();
		}
		String where = "";
		Tb tb = Tb.getTBByBean(clazz);
		for (PrvTranData line : vec) {
			String partsql = "";
			PrvRoleTran rt = getRoleTran(user, clazz, line);
			Prv.OPrvType type = rt == null ? Prv.OPrvType.NO : rt.gtType(); //未分配组作无权限处理
			//如是按核算单元，根据单元的类型更改为按机构或部门
			if (type == Prv.OPrvType.CELL) {
				if (SysCellDAO.getCellByUser(user).getDept() == null)
					type = Prv.OPrvType.ORG;
				else
					type = Prv.OPrvType.DEPT;
			} else if (type == Prv.OPrvType.CELL_EXT) {
				if (SysCellDAO.getCellByUser(user).getDept() == null)
					type = Prv.OPrvType.ORG_EXT;
				else
					type = Prv.OPrvType.DEPT_EXT;
			}
			//---------------------------------
			if (type == Prv.OPrvType.NO)
				partsql += "1=2";
			else if (type == Prv.OPrvType.USER) {
				partsql += "business_member=" + user.getPkey();
			} else if (type == Prv.OPrvType.DEPT) {
				String linksql = Idu.sqlString("select {0} from {1} where dept={2} or (org={3} and dept is null)", mainCode,
				    tableCode, user.getDept(), user.getOrg());
				partsql += "pkey in (" + linksql + ")";
			} else if (type == Prv.OPrvType.DEPT_EXT) {
				String linksql = Idu.sqlString("select {0} from {1} where dept in ({2}) or (org={3} and dept is null)",
				    mainCode, tableCode, _mapDeptExt.get(user.gtDept().getPkey().toString()), user.getOrg());
				partsql += "pkey in (" + linksql + ")";
			} else if (type == Prv.OPrvType.ORG) {
				String linksql = Idu.sqlString("select {0} from {1} where org={2}", mainCode, tableCode, user.getOrg());
				partsql += "pkey in (" + linksql + ")";
			} else if (type == Prv.OPrvType.ORG_EXT) {
				String linksql = Idu.sqlString("select {0} from {1} where org in ({2})", mainCode, tableCode,
				    _mapOrgExt.get(user.gtOrg().getPkey().toString()));
				partsql += "pkey in (" + linksql + ")";
			} else if (type == Prv.OPrvType.ALL) {
				return "1=1";
			}
			where += " OR " + partsql;
		}
		return "(" + where.substring(4) + ")";
	}

	//初始化--部门、本级及下级部门
	public static void initDeptMap() {
		List<SysDept> list = BeanBase.list(SysDept.class, "1=1", false);
		for (SysDept line : list) {
			String pkeys = line.getPkey().toString();
			for (SysDept line2 : list) {
				if (line2.equals(line))
					continue;
				if (line2.getCode().startsWith(line.getCode()))
					pkeys += "," + line2.getPkey();
			}
			_mapDeptExt.put(line.getPkey().toString(), pkeys);
		}
	}

	//初始化--机构、本级及下级机构
	public static void initOrgMap() {
		List<SysOrg> orgs = SysOrg.list(SysOrg.class, "1=1", false);
		for (SysOrg org : orgs) {
			String code = org.getCode();
			String pkeys = "";
			for (SysOrg line : orgs) {
				if (line.getCode().startsWith(code)) {
					pkeys = pkeys + "," + line.getPkey();
				}
			}
			_mapOrgExt.put(org.getPkey() + "", pkeys.substring(1));
		}
	}

	//初始化--单元、本级及下级单元
	public static void initCellMap() {
		List<SysCell> cells = SysCell.list(SysCell.class, "1=1", false);
		for (SysCell cell : cells) {
			String code = cell.getCode();
			String pkeys = "";
			for (SysCell line : cells) {
				if (line.getCode().startsWith(code)) {
					pkeys = pkeys + "," + line.getPkey();
				}
			}
			_mapOrgExt.put(cell.getPkey() + "", pkeys.substring(1));
		}
	}

	public static void main(String[] args) {
		System.err.println(getSqlPart(SalSale.TB, "createdBy.dept"));
	}

}