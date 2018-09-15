package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.core.sys.SysShiping;
import irille.core.sys.SysShipingDAO;
import irille.pub.Cn;
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
import irille.pub.inf.IIn;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class GsInDAO {
	public static final Log LOG = new Log(GsInDAO.class);

	/**
	 * 产生入库单
	 * 
	 * @param form
	 *          来源单据
	 * @param gw
	 *          仓库对象
	 * @param name
	 *          名称
	 * @param lines
	 *          明细集合
	 */
	public static void insertByForm(BeanForm form, GsWarehouse gw, String name, Byte shipingMode, Long shiping) {
		GsIn in = new GsIn();
		in.stWarehouse(gw);
		in.setOrg(gw.getOrg());
		in.setDept(gw.getPkey());
		in.setGsName(name);
		in.setOrigForm(form.gtLongPkey());
		in.setOrigFormNum(form.getCode());
		in.setRem(form.getRem());
		in.setShipingMode(shipingMode);
		in.setShiping(shiping);
		Ins act = new Ins();
		act.setB(in);
		act.commit();
	}

	// 根据源单据删除入库单
	public static void deleteByForm(BeanForm form) {
		GsIn in = GsIn.chkUniqueOrig(true, form.gtLongPkey());
		if (in == null)
			throw LOG.err("notHasIn", "找不到产生的入库单!");
		Del act = new Del();
		act.setB(in);
		act.commit();
	}

	public static class Ins extends IduIns<Ins, GsIn> {
		@Override
		public void before() {
			super.before();
			getB().setCode(SysSeqDAO.getSeqnum(GsIn.TB, getB().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
		}
	}

	public static class Del extends IduDel<Del, GsIn> {

		@Override
		public void before() {
			super.before();
			GsIn mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			//			delLineTid(getB(), GsInLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsIn> {
		public SysShiping _ship;

		public List<GsInLineView> _lines;

		public List<GsInLineView> getLines() {
			return _lines;
		}

		public void setLines(List<GsInLineView> lines) {
			_lines = lines;
		}

		@Override
		public void run() {
			super.run();
			Idu.checkFormGoods(getLines());
			for (GsInLineView line : getLines())
				if (line.gtGoods().isWork())
					throw LOG.err("isWork", "入库单明细不可包含劳务类型货物！");
			GsIn in = BeanBase.get(GsIn.class, getB().getPkey());
			// 校验单据状态是否为初始状态
			assertStatusIsInit(in);
			// 入库
			in.setStatus(Sys.OBillStatus.CHECKED.getLine().getKey());
			in.setOperator(getB().getOperator());
			in.setChecker(getB().getChecker());
			in.setInTime(getB().getInTime());
			in.setApprBy(getUser().getPkey());
			in.setApprTime(Env.INST.getWorkDate());
			in.stShipingMode(getB().gtShipingMode());
			SysShipingDAO.updByGs(in, in.gtOrigForm(), _ship, GsIn.T.SHIPING.getFld(), in.gtShipingMode(), in.getShiping());
			in.upd();
			setB(in);
			GsWarehouse gw = in.gtWarehouse();
			//判断明细行总数是否等于原明细行总数
			try {
				IIn origDao = (IIn) Class.forName(in.gtOrigForm().getClass().getName() + "DAO").newInstance();
				List<IGoods> origLines = origDao.getInLines(in.gtOrigForm(), 0, 0);
				GsPub.delWork(origLines);
				//对原始单据明细货物进行一次相同货物合并成一条记录
				List<IGoods> unitLines = GsPub.unitLines(origLines);
				//审核明细货物与合并后的原始明细货物数量比较
				for (IGoods unLine : unitLines) {
					BigDecimal tmp = BigDecimal.ZERO;
					for (GsInLineView line : getLines())
						if (line.getGoods().equals(unLine.getGoods()))
							tmp = tmp.add(line.getDefaultUomQty());
					if (unLine.getQty().compareTo(tmp) != 0)
						throw LOG.err("notCompare", "货物[{0} : {1}]数量与来源单据不符，数据相差{2}", unLine.gtGoods().getCode(), unLine.gtGoods()
						    .getName(), unLine.getQty().subtract(tmp));
				}
			} catch (Exp e) {
				throw e;
			} catch (Exception e) {
				throw LOG.err("noClassName", "入库单[{0}]取入库明细行出错，无法入库!", in.getCode());
			}
			for (GsInLineView line : getLines()) {
				// 修改存货数量，新增存货明细行，修改存货批次数量。如果没有填写批次代码，则加入到名称为空的批次。新增的存货的货位默认为NULL，只能去存货菜单进行手动修改。
				GsStock stock = GsStockDAO.getStockOrCrt(gw, line.gtGoods(), true);
				GsStockBatch batch = GsStockBatchDAO.getStockBatchOrCrt(stock, line.getBatchCode(), true);
				GsStockBatchDAO.updQty(batch, line.getDefaultUomQty());
				stock.setQty(stock.getQty().add(line.getDefaultUomQty()));
				stock.upd();
				GsStockLine sl = new GsStockLine();
				sl.stStock(stock);
				sl.setName(in.getGsName());
				sl.setGsForm(in.gtLongPkey());
				sl.setGsFormNum(in.getCode());
				sl.setOrigForm(in.getOrigForm());
				sl.setOrigFormNum(in.getOrigFormNum());
				sl.setGsTime(Env.INST.getSystemTime());
				sl.setGsBatchName(line.getBatchCode());
				sl.setGsQty(line.getDefaultUomQty());
				sl.setQty(stock.getQty());
				sl.ins();
			}
			GsPub.inOk(in);// 源单据状态标记为完成
		}

	}

	public static class Unapprove extends IduOther<Unapprove, GsIn> {

		@Override
		public void run() {
			super.run();
			GsIn in = BeanBase.get(GsIn.class, getB().getPkey());
			assertStatusIsCheck(in);
			try {
				in.stStatus(Sys.OBillStatus.INIT);
				in.setOperator(null);
				in.setChecker(null);
				in.setInTime(null);
				in.setApprBy(null);
				in.setApprTime(null);
				in.upd();
				setB(in);
				IIn inLine = (IIn) Class.forName(in.gtOrigForm().getClass().getName() + "DAO").newInstance();
				List<IGoods> origLines = inLine.getInLines(in.gtOrigForm(), 0, 0);
				GsPub.delWork(origLines);
				List<IGoods> unitLines = GsPub.unitLines(origLines); //处理后源单据的货物明细 - 默认数量
				List<GsStockLine> slines = GsStockLineDAO.getStockLines(getB(), true, 0, 0);
				for (GsStockLine line : slines) {
					GsStock stock = Bean.loadAndLock(GsStock.class, line.getStock());
					GsStockBatch batch = GsStockBatchDAO.getStockBatchOrCrt(stock, line.getGsBatchName(), true);
					GsStockBatchDAO.updQty(batch, line.getGsQty().negate());
					GsGoods goods = stock.gtGoods();
					for (IGoods origline : unitLines) { //以源单据为准标，减去已处理的数量
						if (origline.getGoods().equals(goods.getPkey())) {
							origline.setQty(origline.getQty().subtract(line.getGsQty()));
							break;
						}
					}
					stock.setQty(stock.getQty().subtract(line.getGsQty()));
					//在当前存货行之后的所有存货行剩余数量减去当前存货行数量
					List<GsStockLine> afterLines = BeanBase.list(GsStockLine.class, GsStockLine.T.STOCK.getFld()
					    .getCodeSqlField() + "=? AND " + GsStockLine.T.PKEY.getFld().getCodeSqlField() + ">?", true,
					    stock.getPkey(), line.getPkey());
					for (GsStockLine after : afterLines) {
						after.setQty(after.getQty().subtract(line.getGsQty()));
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
				throw LOG.err("noClassName", "取入库明细行出错，无法弃审!", in.getCode());
			}
			GsPub.inCancel(in);
		}
	}

}
