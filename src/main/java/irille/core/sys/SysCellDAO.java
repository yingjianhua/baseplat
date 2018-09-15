package irille.core.sys;

import irille.gl.gs.GsWarehouse;
import irille.gl.gs.GsWarehouseDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.ProvCtrl;
import irille.pub.svr.ProvDataCtrl;

import java.util.List;

public class SysCellDAO {
	public static final Log LOG = new Log(SysCellDAO.class);
	
	private static void autoSet(SysCell bean) {
		String code = bean.gtOrg().getCode();
		if (bean.getDept() != null)
			code = bean.gtDept().getCode();
		bean.setCode(code);
		bean.setTemplat(bean.gtOrg().getTemplat());
	}

	//机构更改模板后，刷新所有相关的核算单元数据
	public static void resetTemplate(Integer org, Integer tmp) {
		String where = Idu.sqlString("{0}=?", SysCell.T.ORG);
		List<SysCell> list = BeanBase.list(SysCell.class, where, true, org);
		for (SysCell line : list) {
			line.setTemplat(tmp);
			line.upd();
		}
		BeanBuf.clear(SysCell.class);
	}

	public static class Ins extends IduIns<Ins, SysCell> {
		@Override
		public void before() {
			super.before();
			autoSet(getB());
			chkUnique(getB());
		}

		@Override
		public void after() {
			super.after();
			if (getB().getDept() == null)
				return;
			SysDept dept = getB().gtDept();
			dept.setCell(getB().getPkey());
			dept.upd();
			ProvDataCtrl.initCellMap();//将缓存中存储的核算单元上下级更新
		}

	}

	public static class Upd extends IduUpd<Upd, SysCell> {

		public void before() {
			super.before();
			autoSet(getB());
			SysCell dbBean = load(getB().getPkey());
			if (dbBean.getCode().equals(getB().getCode()) == false)
				chkUnique(getB());
			
			SysDept oldDept = dbBean.gtDept();
			SysDept newDept = getB().gtDept();
			if ((oldDept+"").equals(newDept+"") == false) {
				if (newDept == null) {
					oldDept.setCell(null);
					oldDept.upd();
					GsWarehouseDAO.updCell(oldDept, null);
				} else {
					newDept.setCell(getB().getPkey());
					newDept.upd();
					GsWarehouseDAO.updCell(newDept, getB());
					if (oldDept != null) {
						oldDept.setCell(null);
						oldDept.upd();
						GsWarehouseDAO.updCell(oldDept, null);
					}
				}
				BeanBuf.clear(SysDept.class);
			}
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysCell.T.PKEY);
			setB(dbBean);
			BeanBuf.clear(SysCell.class, getB().getPkey());
		}
	}
	
	public static class Del extends IduDel<Del, SysCell> {
		@Override
		public void before() {
			super.before();
			BeanBuf.clear(SysCell.class, getB().getPkey());
			SysDept dept = getB().gtDept();
			if (dept == null)
				return;
			dept.setCell(null);
			dept.upd();
			BeanBuf.clear(SysDept.class, dept.getPkey());
			GsWarehouseDAO.updCell(dept, null);
		}
		
		public void after() {
			super.after();
			ProvDataCtrl.initCellMap();//将缓存中存储的核算单元上下级更新
		}
	}

	private static void chkUnique(SysCell model) {
		if(model.getDept()==null)
			if(model.chkUniqueCode(false, model.getCode()) != null) 
				throw LOG.err("isExist","核算单元[{0}]已有相同代码的信息存在!", model.getCode());
		if (model.chkUniqueOrgDept(false, model.getOrg(), model.getDept()) != null)
			throw LOG.err("isExist", "机构[{0}]且部门[{1}]的记录已存在！", model.gtOrg().getName(), model.gtDept().getName());
	}

	// 根据用户取核算单元，若没有返回null
	public static SysCell getCellByUser(SysUser user) {
		return getCellByDept(user.getDept());
	}

	// 根据部门取核算单元
	public static SysCell getCellByDept(Integer deptId) {
		SysDept dept = BeanBase.get(SysDept.class, deptId);
		SysCell cell = SysCell.chkUniqueOrgDept(false, dept.getOrg(), deptId);
		if (cell == null) {
			if (dept.getDeptUp() != null)
				return getCellByDept(dept.getDeptUp());
			return getCellByOrg(dept.getOrg());
		}
		return cell;
	}

	// 根据机构取核算单元
	public static SysCell getCellByOrg(Integer orgId) {
		List<SysCell> list = BeanBase.list(SysCell.class, SysCell.T.ORG.getFld().getCodeSqlField() + "=? and "
		    + SysCell.T.DEPT.getFld().getCodeSqlField() + " is null", false, orgId);
		if (list.size() > 0)
			return list.get(0);
		throw LOG.err("isExist", "机构[{0}]的核算单元未设置！", BeanBase.get(SysOrg.class, orgId).getName());
	}
}
