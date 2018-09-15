package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysCellDAO;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

import java.util.List;

public class GsDemandDAO {
	public static final Log LOG = new Log(GsDemandDAO.class);

	/**
	 * 产生需求清单
	 * 
	 * @param form
	 *            来源请购单
	 * @param gw
	 *            仓库对象
	 * @param name
	 *            名称
	 * @param lines
	 *            请购单明细集合
	 */
	public static void insertByRequest(GsRequest form, List<GsRequestLine> lines) {
		if (lines == null || lines.size() == 0)
			throw LOG.err("noLines", "货物信息不存在，不可产生出库单!");
		Ins act = new Ins();
		for (GsRequestLine line : lines) {
			GsDemand demand = new GsDemand();
			demand.setWarehouse(form.getWarehouse());
			demand.setGoods(line.getGoods());
			demand.setUom(line.getUom());
			demand.setQty(line.getQty());
			demand.setRequestTime(Env.INST.getWorkDate());
			demand.setOrigForm(form.gtLongPkey());
			demand.setOrigFormNum(form.getCode());
			demand.setStatus(Sys.OBillStatus.INIT.getLine().getKey());
			demand.setPoForm(null);
			demand.setPoFormNum(null);
			demand.stCell(SysCellDAO.getCellByOrg(form.getOrg()));
			demand.setOrg(form.getOrg());
			demand.setCreatedBy(form.getCreatedBy());
			demand.setCreatedTime(Env.INST.getWorkDate());
			act.setB(demand);
			act.commit();
		}
	}

	// 根据源单据删除需求清单
	public static void deleteByRequest(GsRequest form) {
		List<GsDemand> lines = BeanBase.list(GsDemand.class,
				GsDemand.T.ORIG_FORM.getFld().getCodeSqlField() + "=?", false,
				form.gtLongPkey());
		if (lines == null || lines.size() == 0)
			throw LOG.err("notHasOut", "找不到产生的需求清单!");
		for (GsDemand line : lines)
			if (line.getPoForm() != null)
				throw LOG.err("hasPurOrder", "采购订单已产生，不可弃审！");
		Del act = new Del();
		for (GsDemand line : lines) {
			act.setB(line);
			act.commit();
		}
	}

	//注册供应单
	public static void createdByPo(long demandId, Bean bean, String poFormNum) {
		GsDemand demand = BeanBase.loadAndLock(GsDemand.class, demandId);
		Upd act = new Upd();
		demand.setPoForm(bean.gtLongPkey());
		demand.setPoFormNum(poFormNum);
		demand.stStatus(Sys.OBillStatus.CHECKED);
		act.setB(demand);
		act.commit();
	}

	//注销供应单
	public static void fireByPo(Bean bean, String poFormNum) {
		// GsDemand demand = BeanBase.loadAndLock(GsDemand.class,demandId);
		List<GsDemand> list = BeanBase.list(GsDemand.class,
				"po_form = ? and po_form_num = ?", true, bean.gtLongPkey(), poFormNum);
		Upd act = new Upd();
		act.setChkVersion(false);
		for(GsDemand demand:list){
			demand.setPoForm(null);
			demand.setPoFormNum(null);
			demand.stStatus(Sys.OBillStatus.INIT);
			act.setB(demand);
			act.commit();
		}
	}

	public static class Ins extends IduIns<Ins, GsDemand> {

	}
	
	public static class Upd extends IduUpd<Upd, GsDemand> {
		
	}

	public static class Del extends IduDel<Del, GsDemand> {

	}

}
