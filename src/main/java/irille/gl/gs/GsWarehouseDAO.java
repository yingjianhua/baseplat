package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysDept;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class GsWarehouseDAO {
	public static final Log LOG = new Log(GsWarehouseDAO.class);
	
	public static void updCell(SysDept dept, SysCell cell) {
		GsWarehouse gw = BeanBase.chk(GsWarehouse.class, dept.getPkey());
		if (gw == null)
			return ;
		if (cell == null)
			throw LOG.err("no", "仓库的核算单元不可为空");
		if (gw.getCell().intValue() == cell.getPkey().intValue())
			return;
		gw.stCell(cell);
		gw.upd();
		String sql = Idu.sqlString("update {0} set {1}=? where {2}=?", GsStock.class, GsStock.T.CELL, GsStock.T.WAREHOUSE);
		BeanBase.executeUpdate(sql, cell.getPkey(), gw.getPkey());
	}

	public static class Ins extends IduIns<Ins, GsWarehouse> {
		public void before() {
			super.before();
			GsWarehouse.TB.getName(); //TODO 静态块循环异常
			if (new GsWarehouse().chk(getB().getPkey()) != null)
				throw LOG.err("err", "仓库[{0}]已存在，不能新增", getB().gtDept().getName());
			getB().stDept(BeanBase.get(SysDept.class, getB().getPkey()));
			getB().setOrg(getB().gtDept().getOrg());
			getB().stCell(SysCellDAO.getCellByDept(getB().getPkey()));
		}
	}

	public static class Upd extends IduUpd<Upd, GsWarehouse> {
		public void before() {
			super.before();
			GsWarehouse warehouse = BeanBase.get(GsWarehouse.class, getB().getPkey());
			//warehouse.setCell(getB().getCell());
			warehouse.setEnabled(getB().getEnabled());
			warehouse.setLocationFlag(getB().getLocationFlag());
			warehouse.setOutOrder(getB().getOutOrder());
			warehouse.setConsignees(getB().getConsignees());
			warehouse.setInvented(getB().getInvented());
			warehouse.setRem(getB().getRem());
			setB(warehouse);
		}
	}

}
