/**
 * 
 */
package irille.pub.gl;

import irille.core.sys.SysCell;
import irille.core.sys.Sys.OCurrency;
import irille.gl.gl.GlGoodsLine;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlSubject;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OTallyState;
import irille.pub.Log;
import irille.pub.svr.ISvrVars;

import java.math.BigDecimal;
import java.util.List;

/**
 * 各类分录行的类
 * 
 * @author whx
 * 
 */
public class TallyLineClasses {
	private static final Log LOG = new Log(TallyLineClasses.class);

	/**
	 * 记账分录行基类 默认发生额为借方
	 * 
	 * @author whx
	 * 
	 */
	public static abstract class TallyLine<T extends TallyLine> implements ISvrVars {
		public static final Byte CURRENCY_DEF = OCurrency.DEFAULT.getLine().getKey();
		private String _name; // 分录名称，用于分录定义中取数用
		private Byte _direct = JF; // 借贷标志 <ODirect> BYTE
		// DR:1,借方
		// CR:2,贷方
		private BigDecimal _amt = ZERO; // 金额 DEC(16,2)
		private Byte _currency = CURRENCY_DEF; // 币种 <OCurrency>  BYTE
		private String _docNum = null; // 票据号 STR(40)<null>
		private String _summary = null; // 摘要 STR(40)<null>
		private boolean _negativeAble = false; // 可负数标志
		private byte _tallyState = OTallyState.INIT.getLine().getKey(); //初始状态
		private List<GlGoodsLine> _goodsLine = null; //用于记存货账，仅用于保存货物、数量、金额

		public abstract GlJournal getJournal();

		public abstract SysCell getCell();

		public T set(BigDecimal amt, byte direct, String docNum, String summary) {
			_amt = amt;
			_docNum = docNum;
			_summary = summary;
			_direct = direct;
			return (T) this;
		}

		public T set(BigDecimal amt, ODirect od, String docNum, String summary) {
			return set(amt, od.getLine().getKey(), docNum, summary);
		}

		/**
		 * 置分录名称，用于分录定义中取数用
		 * 
		 * @param name
		 * @return
		 */
		public T setName(String name) {
			_name = name;
			return (T) this;
		}

		public String getName() {
			return _name;
		}
		
		public List<GlGoodsLine> getGoodsLine() {
			return _goodsLine;
		}

		public void setGoodsLine(List<GlGoodsLine> goodsLine) {
			_goodsLine = goodsLine;
		}

		public T assertAmtNegativeAble() {
			if (getAmt().signum() == -1 && !isNegativeAble())
				throw LOG.err("amtSignum", "此金额【{0}】不允许为负数。", _amt);
			return (T) this;
		}

		public GlJournal getJlAmtByCode(String journalCode) {
			return GlJournal.loadUniqueCode(true, journalCode);
		}

		public GlSubject getSubjectByCode(String subjectCode) {
			return GlSubject.loadUniqueTempCode(false, getJournal().gtCell().getTemplat(), subjectCode);
		}

		public GlSubject getSubject() {
			return getJournal().gtSubject();
		}

		public Byte getDirect() {
			return _direct;
		}

		public T setDirect(Byte direct) {
			_direct = direct;
			return (T) this;
		}

		public T setDirectToDf() {
			_direct = DF;
			return (T) this;
		}

		public ODirect gtDirect() {
			return (ODirect) (ODirect.CR.getLine().get(_direct));
		}

		public T stDirect(ODirect direct) {
			_direct = direct.getLine().getKey();
			return (T) this;
		}

		public BigDecimal getAmt() {
			return _amt;
		}

		public T setAmt(BigDecimal amt) {
			_amt = amt;
			return (T) this;
		}

		public String getDocNum() {
			return _docNum;
		}

		public T setDocNum(String docNum) {
			_docNum = docNum;
			return (T) this;
		}

		public String getSummary() {
			return _summary;
		}

		public T setSummary(String summary) {
			_summary = summary;
			return (T) this;
		}

		public Byte getCurrency() {
			return _currency;
		}

		public void setCurrency(Byte currency) {
			_currency = currency;
		}

		public OCurrency gtCurrency() {
			return (OCurrency) (OCurrency.RMB.getLine().get(_currency));
		}

		public void stCurrency(OCurrency currency) {
			_currency = currency.getLine().getKey();
		}

		/**
		 * @return the negativeAble
		 */
		public boolean isNegativeAble() {
			return _negativeAble;
		}

		/**
		 * @param negativeAble
		 *          the negativeAble to set
		 */
		public T setNegativeAble(boolean negativeAble) {
			_negativeAble = negativeAble;
			return (T) this;
		}

		/**
		 * @return the tallyState
		 */
		public byte getTallyState() {
			return _tallyState;
		}

		/**
		 * @param tallyState the tallyState to set
		 */
		public T setTallyState(byte tallyState) {
			_tallyState = tallyState;
			return (T) this;
		}

		public OTallyState gtTallyState() {
			return (OTallyState) (OTallyState.DEFAULT.getLine().get(_tallyState));
		}

		public T stTallyState(OTallyState tallyState) {
			_tallyState = tallyState.getLine().getKey();
			return (T) this;
		}
	}

	/**
	 * 直接给账号的标准分录行
	 * 
	 * @author whx
	 * 
	 */
	public static class TallyLineZh extends TallyLine<TallyLineZh> {
		private String _journalCode;

		public TallyLineZh(String journalCode) {
			_journalCode = journalCode;
		}

		@Override
		public SysCell getCell() {
			return getJournal().gtCell();
		}

		@Override
		public GlJournal getJournal() {
			return getJlAmtByCode(_journalCode);
		}
	}

	/**
	 * 直接给账号的标准分录行
	 * 
	 * @author whx
	 * 
	 */
	public static class TallyLineJournal extends TallyLine<TallyLineJournal> {
		private GlJournal _journal;

		public TallyLineJournal(GlJournal journal) {
			_journal = journal;
		}

		@Override
		public SysCell getCell() {
			return _journal.gtCell();
		}

		@Override
		public GlJournal getJournal() {
			return _journal;
		}
	}

	/**
	 * 根据科目或别名取账户的基类，需要指定账户的对象信息，如机构、部门、核算单元、用户等
	 * 
	 * @author whx
	 * 
	 */
	private static abstract class TallyLineSubjectBase<T extends TallyLineSubjectBase> extends TallyLine<T> {
		private ITallyBean _tallyBean;
		private SysCell _cell;

		public TallyLineSubjectBase(SysCell cell, ITallyBean tallyBean) {
			_tallyBean = tallyBean;
			_cell = cell;
		}

		/*
		 * (non-Javadoc)
		 * @see irille.pub.gl.TallyNote#getJournal()
		 */
		@Override
		public GlJournal getJournal() {
			// 根据科目与对象取账户
			// 如没找到，且为可以自动建立的账户，则自动创建。
			AccObjs acco = new AccObjs();
			getTallyBean().getAccObjs(getName(), acco);
			return GlJournalDAO.getAutoCreate(getCell(), getSubject(), acco);
		}

		public ITallyBean getTallyBean() {
			return _tallyBean;
		}

		@Override
		public SysCell getCell() {
			return _cell;
		}

		public T setJournalObjs(ITallyBean tallyBean) {
			_tallyBean = tallyBean;
			return (T) this;
		}
	}

	/**
	 * 根据科目与对象取账户的对象
	 * 
	 * @author whx
	 * 
	 */
	public static class TallyLineSubject extends TallyLineSubjectBase<TallyLineSubject> {
		GlSubject _subject;

		public TallyLineSubject(SysCell cell, ITallyBean tallyBean, GlSubject subject) {
			super(cell, tallyBean);
			_subject = subject;
		}

		/*
		 * (non-Javadoc)
		 * @see irille.pub.gl.TallyNote#getSubject()
		 */
		@Override
		public GlSubject getSubject() {
			return _subject;
		}
	}

	/**
	 * 根据科目号取科目对象
	 * 
	 * @author whx
	 * 
	 */
	public static class TallyLineAlias extends TallyLineSubjectBase<TallyLineAlias> {
		private String _alias;

		public TallyLineAlias(SysCell cell, ITallyBean tallyBean, String alias) {
			super(cell, tallyBean);
			_alias = alias;
		}

		/*
		 * (non-Javadoc)
		 * @see irille.pub.gl.TallyNote#getSubject()
		 */
		@Override
		public GlSubject getSubject() {
			return GlSubject.gtByTemplatAlias(getCell().gtTemplat(), _alias);
		}
	}

	/**
	 * 根据科目别名及关联取科目对象
	 * 
	 * @author whx
	 * 
	 */
	public static class TallyLineAlias2 extends TallyLineSubjectBase<TallyLineAlias2> {
		private String _aliasOrSubjectCode;
		private String _targetAilas;

		public TallyLineAlias2(SysCell cell, ITallyBean tallyBean, String aliasOrSubjectCode, String targetAilas) {
			super(cell, tallyBean);
			_aliasOrSubjectCode = aliasOrSubjectCode;
			_targetAilas = targetAilas;
		}

		/*
		 * (non-Javadoc)
		 * @see irille.pub.gl.TallyNote#getSubject()
		 */
		@Override
		public GlSubject getSubject() {
			return GlSubject.gtByTemplatAlias(getCell().gtTemplat(), _aliasOrSubjectCode, _targetAilas);
		}
	}
	//
	// /** 根据科目与对象产生账户的中间数据，单账户科目权用设定科目即可 */
	// public static class JournalFromSubjectAlias {
	// private Object _sourceSubjectCodeOrObjectOrAilas; // 可为科目的CODE或科目对象或科目别名
	// private String _alisTarget;
	// private Object _obj; // 对象，可以为机构、部门等核算的分类，如采用科目与对象取账号，必须与所记账的账户明细分类要一致
	//
	// public JournalFromSubjectAlias(Object sourceSubjectCodeOrObjectOrAilas,
	// String alisTarget, Object obj) {
	// _sourceSubjectCodeOrObjectOrAilas = sourceSubjectCodeOrObjectOrAilas;
	// _alisTarget = alisTarget;
	// _obj = obj;
	// }
	//
	// /**
	// * @return the sourceSubjectCodeOrObjectOrAilas
	// */
	// public Object getSourceSubjectCodeOrObjectOrAilas() {
	// return _sourceSubjectCodeOrObjectOrAilas;
	// }
	//
	// /**
	// * @return the alistarget
	// */
	// public String getAlisTarget() {
	// return _alisTarget;
	// }
	//
	// /**
	// * @return the obj
	// */
	// public Object getObj() {
	// return _obj;
	// }
	//
	// }

}
