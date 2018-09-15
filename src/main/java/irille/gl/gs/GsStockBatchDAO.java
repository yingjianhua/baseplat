package irille.gl.gs;

import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.List;

public class GsStockBatchDAO {
	public static final Log LOG = new Log(GsStockBatchDAO.class);

	// 根据存货信息取存货批次档案
	public static List<GsStockBatch> getStockBatchByStock(GsStock stock, boolean lock) {
		List<GsStockBatch> list = BeanBase.list(GsStockBatch.class, GsStockBatch.T.STOCK.getFld().getCodeSqlField() + "=?",
		    lock, stock.getPkey());
		return list;
	}

	public static void updQty(GsStockBatch sb, BigDecimal qty) {
		sb.setQty(sb.getQty().add(qty));
		if (sb.getQty().signum() < 0) {
			String pc = sb.getName();
			if (Str.isEmpty(pc))
				pc = "默认";
			throw LOG.err("errQty", "货物[{0}] - 批次[{1}]数量不足，请检查[存货批次信息]中的剩余数量!", sb.gtStock().gtGoods().getErrMsg(), pc);
		}
		if (sb.getQty().signum() == 0) {
			sb.stCleared(true);
			sb.del();
		}else {
			sb.stCleared(false);
			sb.upd();
		}
	}

	// 取存货批次档案
	public static GsStockBatch getStockBatchOrCrt(GsStock stock, String batchName, boolean lock) {
		GsStockBatch mode = null;
		if (Str.isEmpty(batchName)) {
			List<GsStockBatch> list = BeanBase.list(GsStockBatch.class, GsStockBatch.T.STOCK.getFld().getCodeSqlField()
			    + "=? AND " + GsStockBatch.T.NAME.getFld().getCodeSqlField() + " IS NULL", lock, stock.getPkey());
			if (list != null && list.size() > 0)
				mode = list.get(0);
		} else {
			mode = GsStockBatch.chkUniqueSn(lock, stock.getPkey(), batchName);
		}
		if (mode != null)
			return mode;
		return createAuto(stock, batchName);
	}

	// 自动新建存货批次档案
	public static GsStockBatch createAuto(GsStock stock, String batchName) {
		GsStockBatch mode = new GsStockBatch().init();
		mode.stStock(stock);
		mode.setName(batchName);
		mode.ins();
		return mode;
	}

	public static class Ins extends IduIns<Ins, GsStockBatch> {
		public void before() {
			super.before();
			getB().setQty(BigDecimal.ZERO);
		}
	}

	public static class Upd extends IduUpd<Upd, GsStockBatch> {

		public void before() {
			super.before();
			GsStockBatch batch = BeanBase.get(GsStockBatch.class, getB().getPkey());
			PropertyUtils.copyProperties(batch, getB(), GsStockBatch.T.CLEARED, GsStockBatch.T.NAME,
			    GsStockBatch.T.ENTRY_TIME);
			setB(batch);
		}
	}

}
