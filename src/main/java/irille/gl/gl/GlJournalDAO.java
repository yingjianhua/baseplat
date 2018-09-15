package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTable;
import irille.gl.gl.Gl.OAccType;
import irille.pss.pur.Pur;
import irille.pss.sal.Sal;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.PackageBase.ISubjectAlias;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class GlJournalDAO {
	private static final Log LOG = new Log(GlJournalDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		aliasNotSet("别名[{0}]未制定科目！"),
		assertTotal("汇总科目[{0}]不能开户！"),
		openObj("非单明细科目{0}开户请指定序号或对象！"),
		autoOpen("科目[{0}]为非自动开户或账户类型为多明细时，不可自动开户！");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public final static String WHERE_JOURNAL = Idu
	    .sqlString("{0} = ? AND {1} = ?", GlJournal.T.CELL, GlJournal.T.SUBJECT);

	public static Set<GlJournal> getJournals(SysCell cell, GlSubject subject) {
		Set<GlSubject> set = new HashSet<GlSubject>();
		GlSubjectDAO.getAccount(set, subject);
		Set<GlJournal> rtn = new HashSet();
		for (GlSubject line : set) {
			List<GlJournal> list = BeanBase.list(GlJournal.class, WHERE_JOURNAL, true, cell.getPkey(), line.getPkey());
			rtn.addAll(list);
		}
		return rtn;
	}

	public static GlJournal getAutoCreate(SysCell cell, ISubjectAlias alias, AccObjs objs) {
		GlSubjectMap map = GlSubjectMapDAO.getByAlias(cell.gtOrg().gtTemplat(), alias.getCode());
		if (map.getSubject() == null)
			throw LOG.err(Msgs.aliasNotSet, alias.getName());
		return getAutoCreate(cell, map.gtSubject(), objs);
	}

	public static GlJournal getAutoCreate(SysCell cell, GlSubject subject, AccObjs objs) {
		if (subject.gtTotalFlag())
			throw LOG.err(Msgs.assertTotal, subject.getName());
		String code = toCode(cell, subject, objs);
		GlJournal journal = GlJournal.chkUniqueCode(false, code);
		if (journal != null)
			return journal;
		Gl.OAccType otype = subject.gtAccType();
		if (subject.gtAutoCrt() == false || otype == Gl.OAccType.MUCH)
			throw LOG.err(Msgs.autoOpen, subject.getName());
		journal = new GlJournal().init();
		journal.setCode(code);
		journal.setOrg(cell.getOrg());
		journal.stCell(cell);
		journal.stSubject(subject);
		if (otype == Gl.OAccType.ONE)
			journal.setName(subject.getName());
		else {
			journal.setName(subject.getName() + "-" + objs.getObjName(subject));
			journal.setObjPkey(objs.getObjLongPkey(subject));
		}
		journal.setBalance(BigDecimal.ZERO);
		journal.setBalanceUse(BigDecimal.ZERO);
		journal.setAccType(subject.getAccType());
		journal.setInFlag(subject.getInFlag());
		journal.setDirect(subject.getDirect());
		journal.setAccJournalType(subject.getAccJournalType());
		journal.setTallyFlag(subject.getTallyFlag());
		journal.ins();
		return journal;
	}

	public static String toCode(SysCell cell, GlSubject subject, AccObjs objs) {
		return toCode(cell, subject, objs.getObjPkey(subject));
	}

	/**
	 * 单账户科目的账号=“核算代码-科号”，其它的为“核算代码-科目-代码”
	 */
	public static String toCode(SysCell cell, GlSubject subject, Integer pkey) {
		if (subject.gtAccType() == Gl.OAccType.ONE)
			return cell.getCode() + "-" + subject.getCode();
		if (pkey == null)
			throw LOG.err(Msgs.openObj, subject.getName());
		return cell.getCode() + "-" + subject.getCode() + "-" + pkey;
	}

	public static String toCode(SysCell cell, GlSubject subject, Long pkey) {
		if (subject.gtAccType() == Gl.OAccType.ONE)
			return cell.getCode() + "-" + subject.getCode();
		if (pkey == null)
			throw LOG.err(Msgs.openObj, subject.getName());
		return cell.getCode() + "-" + subject.getCode() + "-" + pkey;
	}

	public static class Ins extends IduIns<Ins, GlJournal> {

		public void before() {
			super.before();
			GlSubject subject = getB().gtSubject();
			OAccType type = subject.gtAccType();
			Class obj = type.getObj();
			if (obj != null) 
				getB().setObjPkey(Bean.gtLongPkey(getB().getObjPkey(), SysTable.gtTable(obj).getPkey()));
			SysCell cell = getB().gtCell();
			getB().setCode(toCode(cell, subject, getB().getObjPkey()));
			getB().stOrg(cell.gtOrg());
			getB().stCurrency(Sys.OCurrency.RMB);
			getB().setBalance(BigDecimal.ZERO);
			getB().setBalanceUse(BigDecimal.ZERO);
			getB().stState(Gl.OJlState.NORMAL);
			getB().setAccType(subject.getAccType());
			getB().stFrostFlag(Gl.OFrostFlag.NORMAL);
			getB().stInterestAccrual(Gl.OInterestAccrual.NO);
			getB().stInFlag(subject.gtInFlag());
			getB().setDirect(subject.getDirect());
			getB().setAccJournalType(subject.getAccJournalType());
			getB().setTallyFlag(subject.getTallyFlag());
			// 汇总科目不能开户
			if (subject.gtTotalFlag())
				throw LOG.err("noOpen", "汇总科目[{0}]不能开户", subject.getName());
			if (GlJournal.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("same", "普通分户账[{0}]已存在，不可重复开账", getB().getCode());
		}

	}

	public static class Upd extends IduUpd<Upd, GlJournal> {

		public void before() {
			super.before();
			GlJournal dbBean = load(getB().getPkey());
			dbBean.setName(getB().getName());
			dbBean.setRem(getB().getRem());
			setB(dbBean);
		}

	}

	public static class Del extends IduDel<Del, GlJournal> {

		@Override
		public void valid() {
			super.valid();
			if (Idu.getLinesCount(GlJournalLine.T.MAIN_PKEY.getFld(), getB().getPkey()) > 0)
				throw LOG.err("errLines", "账户[{0}]已有明细发生，不能删除", getB().getName());
		}

	}

	public static BigDecimal getCustomRva(SysCustom custom) {
		String code = toCode(Idu.getCell(), GlSubjectMapDAO.getByAlias(Sal.SubjectAlias.SAL_INCOME.getCode()).gtSubject(),
		    new AccObjs().setCustom(custom));
		GlJournal journal = GlJournal.chkUniqueCode(false, code);
		if (journal == null)
			return BigDecimal.ZERO;
		return journal.getBalance();
	}

	public static BigDecimal getSupplierPya(SysSupplier supplier) {
		String code = toCode(Idu.getCell(), GlSubjectMapDAO.getByAlias(Pur.SubjectAlias.PUR_INCOME.getCode()).gtSubject(),
		    new AccObjs().setSupplier(supplier));
		GlJournal journal = GlJournal.chkUniqueCode(false, code);
		if (journal == null)
			return BigDecimal.ZERO;
		return journal.getBalance();
	}
}
