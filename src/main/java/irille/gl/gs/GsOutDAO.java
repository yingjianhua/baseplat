package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.pub.Exp;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanForm;
import irille.pub.bean.IGoods;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduOther;
import irille.pub.inf.IOut;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class GsOutDAO {
	public static final Log LOG = new Log(GsOutDAO.class);

	/**
	 * 产生出库单
	 * @param form 来源单据
	 * @param gw 仓库对象
	 * @param name 名称
	 * @param lines 明细集合
	 */
	public static void insertByForm(BeanForm form, GsWarehouse gw, String gsName, Byte shipingMode, Long shiping) {
		GsOut out = new GsOut();
		out.stWarehouse(gw);
		out.setOrg(gw.getOrg());
		out.setDept(gw.getPkey());
		out.setGsName(gsName);
		out.setOrigForm(form.gtLongPkey());
		out.setOrigFormNum(form.getCode());
		out.setRem(form.getRem());
		out.setShipingMode(shipingMode);
		out.setShiping(shiping);
		Ins act = new Ins();
		act.setB(out);
		act.commit();
	}

	//根据源单据删除出库单
	public static void deleteByForm(BeanForm form) {
		GsOut out = GsOut.chkUniqueOrig(true, form.gtLongPkey());
		if (out == null)
			throw LOG.err("notHasOut", "找不到产生的出库单!");
		Del act = new Del();
		act.setB(out);
		act.commit();
	}

	public static class Ins extends IduIns<Ins, GsOut> {
		@Override
		public void before() {
			super.before();
			getB().setCode(SysSeqDAO.getSeqnum(GsOut.TB, getB().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
		}

	}

	public static class Del extends IduDel<Del, GsOut> {

		@Override
		public void before() {
			super.before();
			GsOut gain = loadThisBeanAndLock();
			assertStatusIsInit(gain);
		}

	}

	public static class Approve extends IduApprove<Approve, GsOut> {
		public SysShiping _ship;

		public List<GsOutLineView> _lines;

		public List<GsOutLineView> getLines() {
			return _lines;
		}

		public void setLines(List<GsOutLineView> lines) {
			_lines = lines;
		}

		@Override
		public void run() {
			super.run();
			Idu.checkFormGoods(getLines());
			for (GsOutLineView line : getLines())
				if (line.gtGoods().isWork())
					throw LOG.err("isWork", "出库单明细不可包含劳务类型货物！");
			GsOut out = BeanBase.get(GsOut.class, getB().getPkey());
			assertStatusIsInit(out);
			//出库
			out.setStatus(Sys.OBillStatus.CHECKED.getLine().getKey());
			out.setOperator(getB().getOperator());
			out.setChecker(getB().getChecker());
			out.setOutTime(getB().getOutTime());
			out.setApprBy(getUser().getPkey());
			out.setApprTime(Env.INST.getWorkDate());
			out.setShipingMode(getB().getShipingMode());
			SysShipingDAO.updByGs(out, out.gtOrigForm(), _ship, GsOut.T.SHIPING.getFld(), out.gtShipingMode(),
			    out.getShiping());
			out.upd();
			setB(out);
			//判断明细行总数是否等于原明细行总数
			try {
				IOut origDao = (IOut) Class.forName(out.gtOrigForm().getClass().getName() + "DAO").newInstance();
				List<IGoods> origLines = origDao.getOutLines(out.gtOrigForm(), 0, 0);
				GsPub.delWork(origLines);
				List<IGoods> unitLines = GsPub.unitLines(origLines);
				//审核明细货物与合并后的原始明细货物数量比较
				for (IGoods unLine : unitLines) {
					BigDecimal tmp = BigDecimal.ZERO;
					for (GsOutLineView line : getLines())
						if (line.getGoods().equals(unLine.getGoods()))
							tmp = tmp.add(line.getDefaultUomQty());
					if (unLine.getQty().compareTo(tmp) != 0)
						throw LOG.err("notCompare", "货物[{0} : {1}]数量与来源单据不符，数量相差{2}", unLine.gtGoods().getCode(), unLine.gtGoods()
						    .getName(), unLine.getQty().subtract(tmp));
				}
			} catch (Exp e) {
				throw e;
			} catch (Exception e) {
				throw LOG.err("noClassName", "取出库明细行出错，无法出库!", out.getCode());
			}
			GsWarehouse gw = out.gtWarehouse();
			for (GsOutLineView line : getLines()) {
				GsGoods goods = line.gtGoods();
				GsStock stock = GsStockDAO.getStockOrCrt(gw, goods, true);
				GsStockBatch batch = GsStockBatchDAO.getStockBatchOrCrt(stock, line.getBatchCode(), true);
				GsStockBatchDAO.updQty(batch, line.getDefaultUomQty().negate());
				stock.setQty(stock.getQty().subtract(line.getDefaultUomQty()));
				stock.upd();
				GsStockLine sl = new GsStockLine();
				sl.stStock(stock);
				sl.setName(out.getGsName());
				sl.setGsForm(out.gtLongPkey());
				sl.setGsFormNum(out.getCode());
				sl.setOrigForm(out.getOrigForm());
				sl.setOrigFormNum(out.getOrigFormNum());
				sl.setGsTime(Env.INST.getSystemTime());
				sl.setGsBatchName(line.getBatchCode());
				sl.setGsQty(line.getDefaultUomQty());
				sl.setQty(stock.getQty());
				sl.ins();
			}
			GsPub.outOk(out);
		}

	}

	public static class Unapprove extends IduOther<Unapprove, GsOut> {

		@Override
		public void run() {
			super.run();
			GsOut out = BeanBase.get(GsOut.class, getB().getPkey());
			assertStatusIsCheck(out);
			try {
				out.setStatus(Sys.OBillStatus.INIT.getLine().getKey());
				out.setOperator(null);
				out.setChecker(null);
				out.setOutTime(null);
				out.setApprBy(null);
				out.setApprTime(null);
				out.upd();
				setB(out);
				IOut outLine = (IOut) Class.forName(out.gtOrigForm().getClass().getName() + "DAO").newInstance();
				List<IGoods> origLines = outLine.getOutLines(out.gtOrigForm(), 0, 0);
				GsPub.delWork(origLines);
				List<IGoods> unitLines = GsPub.unitLines(origLines); //处理后源单据的货物明细 - 默认数量
				List<GsStockLine> slines = GsStockLineDAO.getStockLines(getB(), true, 0, 0);
				for (GsStockLine line : slines) {
					GsStock stock = Bean.loadAndLock(GsStock.class, line.getStock());
					GsStockBatch batch = GsStockBatchDAO.getStockBatchOrCrt(stock, line.getGsBatchName(), true);
					GsStockBatchDAO.updQty(batch, line.getGsQty());
					GsGoods goods = stock.gtGoods();
					for (IGoods origline : unitLines) { //以源单据为准标，减去已处理的数量
						if (origline.getGoods().equals(goods.getPkey())) {
							origline.setQty(origline.getQty().subtract(line.getGsQty()));
							break;
						}
					}
					stock.setQty(stock.getQty().add(line.getGsQty()));
					//调整当前存货行之后的所有存货行剩余数量
					List<GsStockLine> afterLines = BeanBase.list(GsStockLine.class, GsStockLine.T.STOCK.getFld()
					    .getCodeSqlField() + "=? AND " + GsStockLine.T.PKEY.getFld().getCodeSqlField() + ">?", true,
					    stock.getPkey(), line.getPkey());
					for (GsStockLine after : afterLines) {
						after.setQty(after.getQty().add(line.getGsQty()));
						after.upd();
					}
					stock.upd();
					line.del();
				}
				for (IGoods origline : unitLines) { //以源单据为准标，校验是已完全撤销
					if (origline.getQty().signum() != 0)
						throw LOG.err("notEnouth", "货物[{0}-{1}]，在存库存中的数量已经变更，无法弃审!", origline.gtGoods().getCode(), origline
						    .gtGoods().getName());
				}
			} catch (Exp e) {
				throw e;
			} catch (Exception e) {
				throw LOG.err("noClassName", "取出库明细行出错，无法弃审!", out.getCode());
			}
			GsPub.outCancel(out);
		}
	}

}
