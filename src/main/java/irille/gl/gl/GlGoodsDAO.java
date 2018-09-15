package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.gl.AccObjs;
import irille.pub.gl.TallyLineClasses.TallyLine;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.List;

public class GlGoodsDAO {
	private static final Log LOG = new Log(GlGoodsDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		qtyNotZero("存货账[{0}]有余额，不能操作!"),
		hasLines("存货账[{0}]已有相关业务操作不能删除!"),
		subjectNotSet("[{0}]的库存科目别名未设置!"),
		UniqueRecordExists("当前核算单元下，已存在该货物的存货账，不能重复新增!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	/**
	 * 价格由系统自动根据 金额/数量来计算得出
	 * 不存在存货账记录的，则自动新增一条
	 * 更新存货账的数量、金额、价格(自动计算)，注意bookLine中的借贷标志
	 * 新增存货账明细行记录
	 * @param bookLine
	 * @param tallyLine
	 */
	public static void tally(GlDaybookLine bookLine, TallyLine tallyLine) {
		List<GlGoodsLine> goodsLines = tallyLine.getGoodsLine();
		if (goodsLines == null)
			return;
		for (GlGoodsLine goodsLine : goodsLines) {
			GlGoods goods = goodsLine.gtGoods();
			if (goods.gtJournal().getDirect() != bookLine.getDirect()) {
				goodsLine.setQty(goodsLine.getQty().negate());
				goodsLine.setAmt(goodsLine.getAmt().negate());
			}
			updateBalance(goods, bookLine.getDirect(), goodsLine.getAmt(), goodsLine.getQty(), true);
			goodsLine.setBalanceQty(goods.getQty());
			goodsLine.setBalance(goods.getBalance());
			if (goodsLine.getQty() == BigDecimal.ZERO) {
				goodsLine.setPrice(BigDecimal.ZERO);
			} else {
				goodsLine.setPrice(goodsLine.getAmt().divide(goodsLine.getQty(), GlGoodsLine.T.PRICE.getFld().getScale(),
				    BigDecimal.ROUND_HALF_UP));
			}
			goodsLine.stDaybookLine(bookLine);
			goodsLine.ins();
		}
	}

	public static void updateBalance(GlGoods goods, byte direct, BigDecimal amt, BigDecimal qty, boolean isTally) {
		//其中【direct】入库和红字入库时 为借DR，出库和红字出库时 为贷CR //暂时无用
		//其中【amt】和【qty】出库和入库时 为正，红字出库和红字入库时 为负
		if (isTally) {
			goods.setBalance(goods.getBalance().add(amt));
			goods.setQty(goods.getQty().add(qty));
		} else {
			goods.setBalance(goods.getBalance().subtract(amt));
			goods.setQty(goods.getQty().subtract(qty));
		}

		if (goods.getQty().signum() == -1) {
			throw LOG.err("cstNegative", "存货账[{0}]数量不能为负数!", goods.gtGoods().getName());//TODO 存货账 数量小于0 抛出异常
		} else if (goods.getQty().signum() != 0) {
			goods.setPrice(goods.getBalance().divide(goods.getQty(), GlGoodsLine.T.PRICE.getFld().getScale(),
			    BigDecimal.ROUND_HALF_UP));
		}
		if (goods.getBalance().signum() == 0 && goods.getQty().signum() == 0) {
			goods.stEnabled(false);//余额和数量都是零，说明已经结清
		} else {
			goods.stEnabled(true);
		}
		goods.upd();
	}

	/**
	 * 根据daybookline对象查询所有符合的存货账明细记录集合，循环作以下处理
	 * 没有存货账记录-抛出异常
	 * 更新存货账数量、金额、价格
	 * 删除存货账明细行记录，并更新之后的余额与余数
	 * @param bookLine
	 */
	public static void tallyCancel(GlDaybookLine bookLine) {
		String where = Idu.sqlString("{0}=?", GlGoodsLine.T.DAYBOOK_LINE);
		List<GlGoodsLine> lines = GlGoodsLine.list(GlGoodsLine.class, where, true, bookLine.getPkey());
		for (GlGoodsLine line : lines) {
			updateBalance(line.gtGoods(), bookLine.getDirect(), line.getAmt(), line.getQty(), false);
			where = Idu.sqlString("{0}>? and {1}=?", GlGoodsLine.T.PKEY, GlGoodsLine.T.GOODS);
			List<GlGoodsLine> ulines = GlGoodsLine.list(GlGoodsLine.class, where, true, line.getPkey(), line.getGoods());
			for (GlGoodsLine uline : ulines) {
				uline.setBalance(uline.getBalance().subtract(line.getAmt()));
				uline.setBalanceQty(uline.getBalanceQty().subtract(line.getQty()));
				uline.upd();
			}
			line.del();
		}
	}

	public static class Ins extends IduIns<Ins, GlGoods> {
		public Integer _mycell;

		public void before() {
			super.before();
			getB().setQty(BigDecimal.ZERO);
			getB().setBalance(BigDecimal.ZERO);
			getB().setUom(getB().gtGoods().getUom());
			getB().stEnabled(true);
			//根据商品的财务模板，取别名中配置的对应科目
			String subjectCode = getB().gtGoods().gtKind().getSubjectAlias();
			if (subjectCode.startsWith("@"))
				subjectCode = subjectCode.substring(1);
			GlSubjectMap map = GlSubjectMapDAO.getByAlias(Idu.getOrg().gtTemplat(), subjectCode);
			if (map.getSubject() == null)
				throw LOG.err(Msgs.subjectNotSet, subjectCode);
			getB().setJournal(
			    GlJournalDAO.getAutoCreate(Bean.get(SysCell.class, _mycell), map.gtSubject(), new AccObjs()).getPkey());
			if (GlGoods.chkUniqueJournalGoods(false, getB().getJournal(), getB().getGoods()) != null) {
				throw LOG.err(Msgs.UniqueRecordExists);
			}
		}

	}

	public static class Upd extends IduUpd<Upd, GlGoods> {

		public void before() {
			super.before();
			GlGoods dbBean = loadAndLock(getB().getPkey());
			if (dbBean.getQty().signum() != 0)
				throw LOG.err(Msgs.qtyNotZero, getB().gtGoods().getErrMsg());
			dbBean.setPrice(getB().getPrice());
			dbBean.setEnabled(getB().getEnabled());
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, GlGoods> {

		@Override
		public void valid() {
			super.valid();
			if (getB().getQty().signum() != 0)
				throw LOG.err(Msgs.qtyNotZero, getB().gtGoods().getErrMsg());
			if (Idu.getLinesCount(GlGoodsLine.T.GOODS.getFld(), getB().getPkey()) > 0)
				throw LOG.err(Msgs.hasLines, getB().gtGoods().getErrMsg());
		}

	}
}
