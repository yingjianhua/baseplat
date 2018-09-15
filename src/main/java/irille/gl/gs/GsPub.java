package irille.gl.gs;

import irille.gl.gs.Gs.OEnrouteType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbGoods;
import irille.pub.bean.IGoods;
import irille.pub.inf.IIn;
import irille.pub.inf.IOut;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * 接口公共类
 * @author whx
 * @version 创建时间：2014年10月29日 上午11:05:14
 */
public class GsPub {
	public static final Log LOG = new Log(GsPub.class);

	//产生出库单
	public static void insertOut(BeanForm form, GsWarehouse gw, String name, Byte shipingMode, Long shiping) {
		GsOutDAO.insertByForm(form, gw, name, shipingMode, shiping);
	}

	//产生入库单
	public static void insertIn(BeanForm form, GsWarehouse gw, String name, Byte shipingMode, Long shiping) {
		GsInDAO.insertByForm(form, gw, name, shipingMode, shiping);
	}

	//产生直销需求
	public static void insertDemandDirect(BeanForm form) {
		GsDemandDirectDAO.insertByForm(form);
	}

	//根据源单据删除出库单
	public static void deleteOut(BeanForm form) {
		GsOutDAO.deleteByForm(form);
	}

	//根据源单据删除入库单
	public static void deleteIn(BeanForm form) {
		GsInDAO.deleteByForm(form);
	}

	//根据源单据删除直销需求
	public static void deleteDemandDirect(BeanForm form) {
		GsDemandDirectDAO.deleteByForm(form);

	}

	//出入库预处理登记
	public static void insertStimate(BeanForm form, GsWarehouse gw, List lines, OEnrouteType etype, Date date) {
		GsStockStimateDAO.insertByForm(form, gw, lines, etype, date);
		GsStockDAO.updateStQty(gw, lines, etype, true);
	}

	//出入库预处理--删除
	public static void deleteStimate(BeanForm form, GsWarehouse gw, List lines, OEnrouteType etype) {
		GsStockStimateDAO.deleteByForm(form, etype, lines);
		GsStockDAO.updateStQty(gw, lines, etype, false);
	}

	//检查仓库现有数量，QTY为默认数量
	public static void checkQtyFact(GsWarehouse ware, GsGoods goods, BigDecimal qty) {
		GsStockDAO.checkQtyFact(ware, goods, qty);
	}

	public static void checkQtyFact(GsWarehouse ware, List lines) {
		for (IGoods line : (List<IGoods>) lines)
			checkQtyFact(ware, line.gtGoods(), line.getDefaultUomQty());
	}

	public static void inOk(GsIn in) {
		try {
			Class dc = Class.forName(in.gtOrigForm().getClass().getName() + "DAO");
			IIn inf = (IIn) dc.newInstance();
			inf.inOk(in, (Bean) in.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "inOk", "入库单[{0}]审核出错", in.getCode());
		}
	}

	public static void inCancel(GsIn in) {
		try {
			Class dc = Class.forName(in.gtOrigForm().getClass().getName() + "DAO");
			IIn inf = (IIn) dc.newInstance();
			inf.inCancel(in, (Bean) in.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "inCancel", "入库单[{0}]弃审出错", in.getCode());
		}
	}

	public static void outOk(GsOut out) {
		try {
			Class dc = Class.forName(out.gtOrigForm().getClass().getName() + "DAO");
			IOut inf = (IOut) dc.newInstance();
			inf.outOk(out, (Bean) out.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "outOk", "出库单[{0}]审核出错", out.getCode());
		}
	}

	public static void outCancel(GsOut out) {
		try {
			Class dc = Class.forName(out.gtOrigForm().getClass().getName() + "DAO");
			IOut inf = (IOut) dc.newInstance();
			inf.outCancel(out, (Bean) out.gtOrigForm());
		} catch (Exception e) {
			throw LOG.err(e, "outCancel", "出库单[{0}]弃审出错", out.getCode());
		}
	}

	//合并相同货物的明细数量、并转为默认计量单位
	public static List<IGoods> unitLines(List<IGoods> lines) {
		HashMap<Integer, IGoods> map = new HashMap<Integer, IGoods>();
		for (IGoods line : lines) {
			IGoods ig = map.get(line.getGoods());
			if (ig == null) {
				line.setQty(line.getDefaultUomQty());
				line.setUom(line.gtGoods().getUom());
				map.put(line.getGoods(), line);
				continue;
			}
			ig.setQty(ig.getQty().add(line.getDefaultUomQty()));
		}
		List<IGoods> nl = new ArrayList<IGoods>();
		nl.addAll(map.values());
		return nl;
	}

	//过滤劳务类型的货物
	public static void delWork(List<IGoods> list) {
		for (int i = list.size() - 1; i >= 0; i--) {
			if (list.get(i).gtGoods().isWork())
				list.remove(i);
		}
	}
}
