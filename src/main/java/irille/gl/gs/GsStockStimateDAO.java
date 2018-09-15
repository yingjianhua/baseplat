package irille.gl.gs;

import irille.gl.gs.Gs.OEnrouteType;
import irille.pss.pur.PurOrder;
import irille.pss.pur.PurOrderLine;
import irille.pss.pur.PurRevLine;
import irille.pub.Log;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanForm;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GsStockStimateDAO {
	public static final Log LOG = new Log(GsStockStimateDAO.class);

	/**
	 * 出入库预计登记
	 * @param form 表单
	 * @param gw 仓库
	 * @param lines 货物数据
	 * @param etype 登记类型
	 * @param date 计划日期
	 */
	public static void insertByForm(BeanForm form, GsWarehouse gw, List lines, OEnrouteType etype, Date date) {
		if (lines == null || lines.size() == 0 || lines.get(0) instanceof IGoods == false)
			throw LOG.err("noLines", "货物信息不存在!");
		for (Object line : lines) {
			IGoods inf = (IGoods) line;
			if (inf.gtGoods().isWork())
				continue;
			GsStockStimate st = new GsStockStimate().init();
			st.stWarehouse(gw);
			st.setGoods(inf.getGoods());
			st.stEnrouteType(etype);
			st.setUom(inf.getUom());
			st.setQty(inf.getQty());
			st.setPlanDate(date);
			st.setOrigForm(form.gtLongPkey());
			st.setOrigFormNum(form.getCode());
			st.ins();
		}
	}

	/**
	 * 取消登记
	 * 因请购采购的问题，被迫更改为一条条取消
	 * 注意这里不对登记的状态作判断，仅判断出入库的状态为初始即可
	 * @param form 源单据
	 */
	public static void deleteByForm(BeanForm form, OEnrouteType etype, List lines) {
		String sql = Idu.sqlString("{0}=? and {1}=? and {2}=? and {3}=?", GsStockStimate.T.ORIG_FORM,
		    GsStockStimate.T.ENROUTE_TYPE, GsStockStimate.T.GOODS, GsStockStimate.T.QTY);
		for (Object line : lines) {
			IGoods inf = (IGoods) line;
			if (inf.gtGoods().isWork())
				continue;
			List<GsStockStimate> st = BeanBase.list(GsStockStimate.class, sql, true, form.gtLongPkey(), etype.getLine()
			    .getKey(), inf.getGoods(), inf.getQty());
			if (st.size() == 0)
				throw LOG.err("noData", "[{0}]对应的出入库单已审核，不可弃审!", form.getCode());
			st.get(0).del();
		}
		//		String sql = Idu.sqlString("delete from {0} where {1}=? and {2}=?", GsStockStimate.class,
		//		    GsStockStimate.T.ORIG_FORM, GsStockStimate.T.ENROUTE_TYPE);
		//		BeanBase.executeUpdate(sql, form.gtLongPkey(), etype.getLine().getKey());
	}
	/**
	 * 根据采购订单删除预出入库记录
	 * @param order 采购订单对象
	 */
	public static void deleteByPurOrder(PurOrder order) {
		String sql = Idu.sqlString("DELETE FROM {0} WHERE {1}=?", GsStockStimate.class, GsStockStimate.T.ORIG_FORM.getFld());
		BeanBase.executeUpdate(sql, order.gtLongPkey());
	}
	
	/**
	 * 根据采购订单更改预出入库记录
	 * @param order 采购订单对象
	 * @param ordlines 采购订单明细对象
	 */
	public static void updateByPurOrder(PurOrder order, List<PurOrderLine> ordlines) {
		String where = GsStockStimate.T.ORIG_FORM.getFld().getCodeSqlField() + "=?";
		List<GsStockStimate> list = BeanBase.list(GsStockStimate.class, where, true, order.gtLongPkey());
		List<PurOrderLine> delList = new ArrayList<PurOrderLine>();
		int ordlinesSize = ordlines.size();
		for (GsStockStimate stline : list) {
			for (PurOrderLine ordline : ordlines) {
				if (stline.getGoods().equals(ordline.getGoods()))
					if (ordline.getQty().compareTo(ordline.getQtyOpen()) > 0) {
						stline.setQty(ordline.getQty().subtract(ordline.getQtyOpen()));
						stline.upd();
					} else {
						stline.del();
					}
				delList.add(ordline);
			}
		}
		ordlines.removeAll(delList);
		if (ordlinesSize > list.size()) {
			for (PurOrderLine ordline : ordlines) {
				if (ordline.getQty().compareTo(ordline.getQtyOpen()) > 0) {
					GsStockStimate st = new GsStockStimate().init();
					st.setWarehouse(order.getWarehouse());
					st.setGoods(ordline.getGoods());
					st.setEnrouteType(OEnrouteType.CGZT.getLine().getKey());
					st.setUom(ordline.getUom());
					st.setQty(ordline.getQty().subtract(ordline.getQtyOpen()));
//				st.setPlanDate(null);
					st.setOrigForm(order.gtLongPkey());
					st.setOrigFormNum(order.getCode());
					st.ins();
				}
			}
		}
	}
	/**
	 * 根据采购订单和收货单明细更改预出入库记录
	 * @param order 采购订单对象
	 * @param ordlines 采购订单明细对象
	 */
	public static void cancelByPurOrderAndPurRevLine(PurOrder order, List<PurRevLine> revlines) {
		String where = GsStockStimate.T.ORIG_FORM.getFld().getCodeSqlField() + "=?";
		List<GsStockStimate> list = BeanBase.list(GsStockStimate.class, where, true, order.gtLongPkey());
		for (PurRevLine revline : revlines) {
			if (revline.gtGoods().isWork())
				continue;
			boolean exist = false;//用于判断收货单明细里有，但是预出入记录里没有的明细
			for (GsStockStimate stline : list) {
				if (revline.getGoods().equals(stline.getGoods()) && revline.getUom().equals(stline.getUom())) {
					stline.setQty(stline.getQty().add(revline.getQty()));
					stline.upd();
					exist = true;
				}
			}
			if (!exist) {
				GsStockStimate st = new GsStockStimate().init();
				st.setWarehouse(order.getWarehouse());
				st.setGoods(revline.getGoods());
				st.setEnrouteType(OEnrouteType.CGZT.getLine().getKey());
				st.setUom(revline.getUom());
				st.setQty(revline.getQty());
//				st.setPlanDate(null);
				st.setOrigForm(order.gtLongPkey());
				st.setOrigFormNum(order.getCode());
				st.ins();
			}
		}
	}
}
