package irille.gl.rp;

import irille.core.sys.Sys.OEnabled;
import irille.gl.gl.GlJournal;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;

public class RpJournalDAO {
	public static final Log LOG = new Log(RpJournalDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		cantupd("该出纳帐号已产生交易，不可修改！"),
		cellworng("工作箱核算单元与分户帐核算单元不一致!"),
		moreRecord("该出纳帐已存在，不可重复新增");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static class Ins extends IduIns<IduIns, RpJournal> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			if (BeanBase.chk(RpJournal.class, getB().getPkey()) != null)
				throw LOG.err(Msgs.moreRecord);
			GlJournal journal = BeanBase.load(GlJournal.class, getB().getPkey());
			getB().setCode(journal.getCode());
			getB().setName(journal.getName());
			getB().setOrg(journal.getOrg());
			getB().setCell(journal.getCell());
			getB().setCashier(getB().gtWorkBox().getUserSys());
			if (getB().gtWorkBox().getMngCell().intValue() != getB().getCell())
				throw LOG.err(Msgs.cellworng);
			getB().setEnabled(OEnabled.TRUE.getLine().getKey());
			getB().setYestodayBalance(BigDecimal.ZERO);
			getB().setBalance(BigDecimal.ZERO);
			getB().setDrAmt(BigDecimal.ZERO);
			getB().setDrQty(0);
			getB().setCrAmt(BigDecimal.ZERO);
			getB().setCrQty(0);
			super.before();
		}

		@Override
		public void after() {
			// TODO Auto-generated method stub
			super.after();
			List<RpWorkBoxGoods> list = Idu.getLinesTid(getB().gtWorkBox(), RpWorkBoxGoods.class);
			RpWorkBoxGoods bg = new RpWorkBoxGoods();
			bg.setBoxGoods(getB().gtLongPkey());
			bg.setName(getB().getName());
			bg.setUserSys(getB().getCashier());
			bg.setDate(Env.getWorkDate());
			bg.setRem(getB().getRem());
			list.add(bg);
			IduInsLines.updLineTid(getB().gtWorkBox(), list, RpWorkBoxGoods.class);
		}
	}

	public static class Upd extends IduUpd<IduUpd, RpJournal> {

		@Override
		public void before() {
			super.before();
			RpJournal dbBean = loadThisBeanAndLock();
			PropertyUtils.copyProperties(dbBean, getB(), RpJournal.T.NAME, RpJournal.T.TYPE, 
					RpJournal.T.BANK_NAME,RpJournal.T.BANK_ACC_CODE, RpJournal.T.BANK_ACC_NAME, 
					RpJournal.T.BANK_NAME, RpJournal.T.WORK_BOX, RpJournal.T.REM);
			setB(dbBean);
		}
	}

	public static class Del extends IduDel<Del, RpJournal> {

		@Override
		public void valid() {
			super.valid();
			if (getB().getBalance().signum() != 0)
				throw LOG.err("beUsed", "出纳账[{0}：{1}]已发生业务，不可删除!", getB().getCode(),getB().getName());
			haveBeUsed(RpJournalLine.class, RpJournalLine.T.JOURNAL, b.getPkey());
		}
	}
}
