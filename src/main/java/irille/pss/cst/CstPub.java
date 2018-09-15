package irille.pss.cst;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCellDAO;
import irille.gl.frm.FrmLinkDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.GlGoods;
import irille.gl.gl.GlGoodsLine;
import irille.gl.gl.GlJournal;
import irille.gl.gs.GsGain;
import irille.gl.gs.GsLoss;
import irille.gl.gs.GsSplit;
import irille.gl.gs.GsUnite;
import irille.pss.pur.PurMvIn;
import irille.pss.pur.PurOrderDirect;
import irille.pss.pur.PurPresent;
import irille.pss.pur.PurRev;
import irille.pss.pur.PurRtn;
import irille.pss.sal.SalMvOut;
import irille.pss.sal.SalPresent;
import irille.pss.sal.SalRtn;
import irille.pss.sal.SalSale;
import irille.pss.sal.SalSaleDirect;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBill;
import irille.pub.bean.BeanGoodsPrice;
import irille.pub.bean.CmbGoods;
import irille.pub.bean.IBill;
import irille.pub.bean.IForm;
import irille.pub.bean.IGoods;
import irille.pub.bean.IGoodsPrice;
import irille.pub.idu.Idu;
import irille.pub.inf.ICstInout;
import irille.pub.inf.ICstInvoice;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class CstPub {
	public static final Log LOG = new Log(CstPub.class);

	/**
	 * 新增核算单的统一接口
	 * @param bill 各类核算单据对象
	 * @param lineClazz 对应核算单据的明细表CLASS
	 * @param formIds 产生核算单的各类单据ID(一般都是产生出入库单的单据)
	 * @param inoutType 出入库标志，见接口类ICstInout
	 * @return
	 */
	public static <BEAN extends BeanBill, LINE extends BeanGoodsPrice> List doInsert(BEAN bill, Class<LINE> lineClazz,
	    String formIds, int inoutType) {
		String[] aryIds = formIds.split("\\,");
		List<LINE> lines = new Vector();
		List<IForm> linesForm = new Vector();
		//以第一条为标准，所有的汇总单据核算单元必须一致
		Bean expBean = Bean.gtLongTbObj(Long.parseLong(aryIds[0]));
		if (expBean instanceof IBill) {
			bill.setOrg(((IBill) expBean).getOrg());
			bill.setCell(((IBill) expBean).getCell());
		} else {
			bill.setOrg(((IForm) expBean).getOrg());
			bill.stCell(SysCellDAO.getCellByDept(((IForm) expBean).getDept()));
		}
		for (String lineId : aryIds) { //遍历所有待处理表单
			ICstInout inf = (ICstInout) Bean.gtLongTbObj(Long.parseLong(lineId));
			linesForm.add((IForm) inf);
			if (inf instanceof IBill) {
				if (bill.getCell().equals(((IBill)inf).getCell()) == false)
					throw LOG.err("sameCell", "不同核算单元的单据不可汇总!");
			}else {
				if (bill.gtCell().equals(SysCellDAO.getCellByDept(((IForm) inf).getDept())) == false)
					throw LOG.err("sameCell", "不同核算单元的单据不可汇总!");
			}
			for (IGoods lineInf : inf.getCstInoutLines(inoutType)) { //遍历单据的货物明细
				//劳务类型货物-过滤
				if (lineInf.gtGoods().isWork())
					continue;
				BigDecimal qty = lineInf.getDefaultUomQty();
				BigDecimal amt = null;
				if (lineInf instanceof IGoodsPrice) {//【收货单】，【采购直销】两个单据直接取价格作为成本价，在记账的时候会统计到存货账中；
					amt = ((IGoodsPrice) lineInf).getAmt();
				} else {//盘盈单，盘亏单，合并单，拆分单，【销售单】，【赠送单】虽然明细原本是有价格的类型，但是已经在bean里面转成没有价格的类型了。
					//这样就会到存货账中取该货物的成本价，作为统计；
					BigDecimal price = lineInf.gtGoods().getPriceOnDefaultUom(bill.gtCell());
					amt = price.multiply(qty).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP);
				}
				if (bill instanceof CstOutRed || bill instanceof CstInRed) {//TODO 是否为红字的还要继续判断，if条件还不完整 TODO 下面的insInvoice的相同位置是否也有相同问题
					//红字发票取负数
					qty = qty.negate();
					amt = amt.negate();
				}
				LINE salLine = Idu.checkGoodsIn(lines, lineInf.gtGoods());
				if (salLine == null) {
					try {
						salLine = lineClazz.newInstance();
						salLine.init();//TODO 由于销售费用（expensesSale）和销售合计（amtCost）需要初始化；
					} catch (Exception e) {
						throw LOG.err("newInstErr", "发票明细对象初始化错误!");
					}
					salLine.setGoods(lineInf.getGoods());
					salLine.setUom(salLine.gtGoods().getUom());
					salLine.setQty(BigDecimal.ZERO);
					salLine.setPrice(BigDecimal.ZERO);
					salLine.setAmt(BigDecimal.ZERO);
					lines.add(salLine);
				}
				salLine.setQty(salLine.getQty().add(qty));
				salLine.setAmt(salLine.getAmt().add(amt));
			}
		}
		//重新计划价格
		for (LINE line : lines)
			line.recountPrice();
		//合计金额 - 新增
		bill.propertySet(bill.gtTB().get("amt"), Idu.sumAmt(lines));
		bill.ins();
		//登记单据关联
		FrmLinkDAO.ins(bill, linesForm);
		//删除待处理记录
		FrmPendingDAO.del(linesForm, bill.gtTB());
		return lines;
	}

	/**
	 * 新增发票的统一接口
	 * 
	 * 核算单元、部门、机构需要根据汇总的单据重新设置
	 * @param bill 各类发票单据
	 * @param lineClazz 对应发票的明细表CLASS
	 * @param formIds 产生的发票的单据ID
	 * @return
	 */
	public static <BEAN extends BeanBill, LINE extends BeanGoodsPrice> List insInvoice(BEAN bill, Class<LINE> lineClazz,
	    String formIds) {
		String[] aryIds = formIds.split("\\,");
		List<LINE> lines = new Vector();
		List<IForm> linesForm = new Vector();
		//以第一条为标准，所有的汇总单据核算单元必须一致
		IBill expBill = (IBill) Bean.gtLongTbObj(Long.parseLong(aryIds[0]));
		bill.setOrg(expBill.getOrg());
		bill.setCell(expBill.getCell());
		for (String lineId : aryIds) { //遍历所有待处理表单
			ICstInvoice inf = (ICstInvoice) Bean.gtLongTbObj(Long.parseLong(lineId));
			IBill lineBill = (IBill) inf;
			if (bill.getCell().equals(lineBill.getCell()) == false)
				throw LOG.err("sameCell", "不同核算单元的单据不可汇总!");
			linesForm.add(lineBill);
			for (IGoodsPrice lineInf : inf.getInvoiceLines()) { //遍历单据的货物明细
				//劳务计销售收入，在发票中不过滤
				BigDecimal qty = lineInf.getDefaultUomQty();
				BigDecimal amt = lineInf.getAmt();
				if (bill instanceof CstSalInvoiceRed || bill instanceof CstPurInvoiceRed) {
					//红字发票取负数
					qty = qty.negate();
					amt = amt.negate();
				}
				LINE salLine = Idu.checkGoodsIn(lines, lineInf.gtGoods());
				if (salLine == null) {
					try {
						salLine = lineClazz.newInstance();
					} catch (Exception e) {
						throw LOG.err("newInstErr", "发票明细对象初始化错误!");
					}
					salLine.setGoods(lineInf.getGoods());
					salLine.setUom(salLine.gtGoods().getUom());
					salLine.setQty(BigDecimal.ZERO);
					salLine.setPrice(BigDecimal.ZERO);
					salLine.setAmt(BigDecimal.ZERO);
					lines.add(salLine);
				}
				salLine.setQty(salLine.getQty().add(qty));
				salLine.setAmt(salLine.getAmt().add(amt));
			}
		}
		//重新计划价格
		for (LINE line : lines)
			line.recountPrice();
		//合计金额 - 新增
		bill.propertySet(bill.gtTB().get("amt"), Idu.sumAmt(lines));
		bill.ins();
		//登记单据关联
		FrmLinkDAO.ins(bill, linesForm);
		//删除待处理记录
		FrmPendingDAO.del(linesForm, bill.gtTB());
		return lines;
	}

	/**
	 * 删除单据关联的所有数据
	 * 把来源单据的待处理记录重新加回
	 * @param bill
	 */
	public static <BEAN extends BeanBill> void delInvoice(BEAN bill) {
		List<IForm> linkList = FrmLinkDAO.queryLinkForm(bill, false);
		for (IForm line : linkList)
			FrmPendingDAO.insByCst(line, bill.gtTB(), ((IForm) bill).getCreatedBy());
		FrmLinkDAO.del(bill);
	}

	public static String getAlia(ICstInout orgin) {
		if (orgin instanceof SalRtn) {
			return "cstSalRtn";
		} else if (orgin instanceof SalPresent) {
			return "cstSalPresent";
		} else if (orgin instanceof SalMvOut) {
			return "cstSalOut";
		} else if (orgin instanceof SalSaleDirect) {
			return "cstSalDirect";
		} else if (orgin instanceof SalSale) {
			return "cstSal";
		} else if (orgin instanceof PurRtn) {
			return "cstPurRtn";
		} else if (orgin instanceof PurPresent) {
			return "cstPurPresent";
		} else if (orgin instanceof PurMvIn) {
			return "cstPurIn";
		} else if (orgin instanceof PurOrderDirect) {
			return "cstPurDirect";
		} else if (orgin instanceof PurRev) {
			return "cstPur";
		} else if (orgin instanceof GsUnite) {
			return "cstGsUnit";
		} else if (orgin instanceof GsSplit) {
			return "cstGsSplit";
		} else if (orgin instanceof GsLoss) {
			return "cstGsLoss";
		} else if (orgin instanceof GsGain) {
			return "cstGsGain";
		} else {
			throw LOG.err("noAlia", "根据原单据取科目别名出错!");
		}
	}

	public static Sum countByJournal(List<ICstInout> orgins, BeanBill cst) {
		Sum sum = new Sum();
		for (ICstInout orgin : orgins) {
			String target = CstPub.getAlia(orgin);
			for (IGoods lineInf : orgin.getCstInoutLines(0)) { //遍历单据的货物明细
				BigDecimal qty = lineInf.getDefaultUomQty();
				BigDecimal amt = null;
//				SysCell cell = SysCellDAO.getCellByDept(((IForm) orgin).getDept());
				GlJournal gsJournal = lineInf.gtGoods().getJournal(cst.gtCell(), null);
				GlJournal cstJournal = lineInf.gtGoods().getJournal(cst.gtCell(), target);
				if (lineInf instanceof IGoodsPrice) {
					amt = ((IGoodsPrice) lineInf).getAmt();
				} else {//盘盈单，盘亏单，合并单，拆分单
					BigDecimal price = lineInf.gtGoods().getPriceOnDefaultUom(gsJournal);
					amt = price.multiply(qty).setScale(Sys.T.AMT.getFld().getScale(), BigDecimal.ROUND_HALF_UP);
				}
				if (cst instanceof CstOutRed || cst instanceof CstInRed) {
					qty = qty.negate();
					amt = amt.negate();
				}
				GlGoods goods = GlGoods.chkUniqueJournalGoods(true, gsJournal.getPkey(), lineInf.getGoods());
				if (goods == null) {
					goods = new GlGoods();
					goods.stJournal(gsJournal);
					goods.stGoods(lineInf.gtGoods());
					goods.stUom(lineInf.gtGoods().gtUom());
					goods.setEnabled((byte) 1);
					goods.setQty(BigDecimal.ZERO);
					goods.setPrice(BigDecimal.ZERO);
					goods.setBalance(BigDecimal.ZERO);
					goods.ins();
				}
				sum.add(gsJournal, cstJournal, goods, qty, amt);
			}
		}
		return sum;
	}

	public static List<IGoods> getCmbGoods(List<IGoodsPrice> oldList) {
		List<IGoods> newList = new ArrayList<IGoods>();
		CmbGoods goods;
		for (IGoodsPrice line : oldList) {
			goods = new CmbGoods();
			goods.stGoods(line.gtGoods());
			goods.stUom(line.gtUom());
			goods.setQty(line.getQty());
			goods.setPkey(line.getPkey());
			newList.add(goods);
		}
		return newList;
	}

	static class Sum {
		private Map<GlJournal, List<GlGoodsLine>> gsMap;
		private Map<GlJournal, BigDecimal> cstMap;

		public Sum() {
			gsMap = new HashMap<GlJournal, List<GlGoodsLine>>();
			cstMap = new HashMap<GlJournal, BigDecimal>();
		}

		public void add(GlJournal gsJournal, GlJournal cstJournal, GlGoods goods, BigDecimal qty, BigDecimal amt) {
			GlGoodsLine line = new GlGoodsLine();
			line.stGoods(goods);
			line.setQty(qty);
			line.setAmt(amt);
			setGsMap(gsJournal, line);
			setCstMap(cstJournal, amt);
		}

		private void setGsMap(GlJournal gsJournal, GlGoodsLine line) {
			if (gsMap.get(gsJournal) == null) {
				gsMap.put(gsJournal, new ArrayList<GlGoodsLine>());
			}
			gsMap.get(gsJournal).add(line);
		}

		private void setCstMap(GlJournal cstJournal, BigDecimal amt) {
			if (cstMap.get(cstJournal) == null) {
				cstMap.put(cstJournal, BigDecimal.ZERO);
			}
			cstMap.put(cstJournal, cstMap.get(cstJournal).add(amt));
		}

		public Map<GlJournal, List<GlGoodsLine>> getGsMap() {
			return gsMap;
		}

		public Map<GlJournal, BigDecimal> getCstMap() {
			return cstMap;
		}

		public BigDecimal sumAmt(GlJournal journal) {
			List<GlGoodsLine> goodsLine = gsMap.get(journal);

			BigDecimal amt = BigDecimal.ZERO;
			for (int i = 0; i < goodsLine.size(); i++) {
				amt = amt.add(goodsLine.get(i).getAmt());
			}
			return amt;
		}

		public void showMap() {
			for (GlJournal journal : gsMap.keySet()) {
				System.out.println("journal:" + journal + "|" + journal.getName());
				for (GlGoodsLine line : gsMap.get(journal)) {
					System.out.println("amt:" + line.getAmt());
					System.out.println("qty:" + line.getQty());
					System.out.println("goods:" + line.getGoods());
				}
			}
		}
	}
}
