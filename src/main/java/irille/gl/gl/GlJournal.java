package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.Sys.OCurrency;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl.OAccJournalType;
import irille.gl.gl.Gl.OAccType;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OFrostFlag;
import irille.gl.gl.Gl.OInterestAccrual;
import irille.gl.gl.Gl.OJlState;
import irille.gl.gl.Gl.OTallyFlag;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanLong;
import irille.pub.gl.IJournalExt;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.svr.Env.SysConf;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.List;

/**
 * 分户账 账号组成：1.单账户：机构-科目-1 2.多账户：机构-科目-账号序号 3.其他分类：机构-科目-分类1-分类2...
 * 
 * @author whx
 */

public class GlJournal extends BeanLong<GlJournal> implements IJournalExt, IExtName {
	private static final Log LOG = new Log(GlJournal.class);
	public static final Tb TB = new Tb(GlJournal.class, "分户账").setAutoIncrement().addActIUDL();
	public static final String CODE_SEPARATOR = Env.getConfParaStr(SysConf.CODE_SEPARATOR);

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		CODE(SYS.CODE__20),
		NAME(SYS.NAME__100),
		CELL(SYS.CELL),
		SUBJECT(GlSubject.fldOutKey()),
		CURRENCY(SYS.CURRENCY), //此属性从科目表复制，不可以修改
		BALANCE(SYS.BALANCE),
		BALANCE_USE(SYS.BALANCE,"可用余额"),
		STATE(TB.crt(OJlState.NORMAL)),
		ORG(SYS.ORG),
		REM(SYS.REM__200_NULL),
		INTEREST_ACCRUAL(TB.crt(Gl.OInterestAccrual.NO)),
		FROST_FLAG(TB.crt(Gl.OFrostFlag.NORMAL)), //冻结标志	
		//OBJ(TB.crtOptAndKey("obj", "核算对象",Gl.OAccType.DEFAULT)), 
		ACC_TYPE(TB.crt(Gl.OAccType.DEFAULT)),	//账户类型
		OBJ_PKEY(SYS.BEAN_LONG, true),	//核算对象主键值
		IN_FLAG(GL.IN_FLAG),
		DIRECT(GL.DIRECT),
		ACC_JOURNAL_TYPE(TB.crt(Gl.OAccJournalType.AMT)),
		TALLY_FLAG(TB.crt(Gl.OTallyFlag.DEFAULT)),	
		EXT_TABLE(SYS.TABLE_ID,"扩展属性表", true),  //其他类型的分户账，扩展信息存到扩展表中
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.DIRECT.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	} 
	
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	public static Fld fldOutKey(String code, String name) {
		Fld fld = Tb.crtOutKey(TB, code, name);
		fld.setType(null);
		return fld;
	}
  
  @Override
  public String getExtName() {
    return getName();
  }
  
//@formatter:on

	/**
	 * 取扩展表的对象
	 */
	public IJournalExt gtJournalExt() {
		return this;
	}

	public void tallyBefore() {
	}

	public void tallyAfter() {
	}

	/*
	 * 记账处理
	 * 本对象的处理已由被调的用记账程序完成，在此只要其他特定的操作即可
	 * @see irille.pub.gl.IJournalExt#tallyExt(irille.gl.gl.GlDaybook,
	 * irille.gl.gl.GlDaybookLine)
	 */
	@Override
	public void tallyExt(GlDaybook daybook, GlDaybookLine daybookLine) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * @see irille.pub.gl.IJournalExt#tallyExtCancel(irille.gl.gl.GlDaybook,
	 * irille.gl.gl.GlDaybookLine)
	 */
	@Override
	public void tallyExtCancel(GlDaybook daybook, GlDaybookLine daybookLine) {
		// TODO Auto-generated method stub

	}

	/*
	 * 记账时增加明细行，因为有些分录的明细是在日终时合并记账的，所以日常的分录只更新分户账，待日终时一次性处理，仅调用此方法即可
	 * @see irille.pub.gl.IJournalExt#tallyLine(irille.gl.gl.GlDaybook,
	 * irille.gl.gl.GlDaybookLine)
	 */
	@Override
	public void tallyLine(GlDaybook daybook, GlDaybookLine daybookLine) {
		GlJournalLine line = new GlJournalLine();
		line.setPkey(daybookLine.getPkey());
		line.setMainPkey(getPkey());
		line.setBalance(getBalance());
		line.setTallyDate(gtOrg().getWorkDate()); //取机构的工作日期
		line.ins();
	}

	@Override
	public void tallyLineCancel(GlDaybook daybook, GlDaybookLine daybookLine) {
		GlJournalLine line = BeanBase.chkAndLock(GlJournalLine.class, daybookLine.getPkey());
		if (line == null)
			throw LOG.err("lineNotFound", "分户账[{0}]中对应明细档案没找到，可能数据已清理!", daybookLine.gtJournal().getName());
		long lineId = line.getPkey();
		line.del();
		// 取后续的记录、并更新余额
		String SQL_AFTER_LINE = Idu.sqlString("{0}=? AND {1}>? ", GlJournalLine.T.MAIN_PKEY, GlJournalLine.T.PKEY);
		List<GlJournalLine> lines = BeanBase.list(GlJournalLine.class, SQL_AFTER_LINE, true, daybookLine.getJournal(), lineId);
		BigDecimal amt = daybookLine.getAmt();
		if (daybookLine.gtDirect() != gtDirect()) // 同方向
			amt = amt.negate();
		for (GlJournalLine l : lines) {
			l.setBalance(l.getBalance().subtract(amt));
			l.upd();
		}
	}

	/**
	 * 产生账号，与科目类型的账户类型有关： 单账户科目： cell.getCode-subject.getCode-subject.
	 * 
	 * @param cell
	 * @param subjectCode
	 * @param beanOrSerial
	 *          如为多账户科目，为Int型的序号，否则为Bean
	 * @return
	 */
	public static String crtCode(SysCell cell, GlSubject subject, Object beanOrSerial) {
		String code = cell.getCode() + CODE_SEPARATOR + subject.getCode();
		OAccType type = subject.gtAccType();
		switch (type) {
		case ONE:
			return code;
		case MUCH:
			return code + CODE_SEPARATOR + subject.getAccType() + beanOrSerial.toString();
		default:
			return code + CODE_SEPARATOR + subject.getAccType() + ((Bean) beanOrSerial).pkeyValues()[0].toString();
		}
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _subject;	// 科目字典 <表主键:GlSubject>  INT
  private Byte _currency;	// 币种 <OCurrency>  BYTE
	// RMB:1,人民币
	// MY:2,美元
	// OY:3,欧元
	// GB:4,港币
	// RY:5,日元
	// HB:6,韩币
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private BigDecimal _balanceUse;	// 可用余额  DEC(16,2)
  private Byte _state;	// 状态 <OJlState>  BYTE
	// NORMAL:1,正常
	// CTRL:2,余额控制
	// CLOSE:9,已销户
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private String _rem;	// 备注  STR(200)<null>
  private Byte _interestAccrual;	// 计息标志 <OInterestAccrual>  BYTE
	// NO:0,不计息
	// MONTH:1,按月计息
  private Byte _frostFlag;	// 冻结标志 <OFrostFlag>  BYTE
	// NORMAL:1,正常
	// NO_DR:2,禁止借(付)方发生
	// NO_CR:3,禁止贷(收)方发生
	// NO:4,禁止借贷双方发生
  private Byte _accType;	// 账户类型 <OAccType>  BYTE
	// ONE:1,单账户科目
	// MUCH:2,多账户科目
	// ORG:61,机构
	// CELL:62,核算单元
	// DEPT:63,部门
	// PROJECT:65,项目
	// EM:66,职员
	// USER:67,用户
	// CUSTOM:68,客户
	// SUPPLIER:69,供应商
  private Long _objPkey;	// 对象  LONG<null>
  private Byte _inFlag;	// 表内标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _direct;	// 借贷标志 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
  private Byte _accJournalType;	// 明细账金额类型 <OAccJournalType>  BYTE
	// AMT:1,金额
	// QTY_AMT:2,数量金额
	// QTY_AMT_BATCH:3,分批次数量金额
  private Byte _tallyFlag;	// 记明细汇总标志 <OTallyFlag>  BYTE
	// TOTAL:1,每日汇总成一笔
	// BILL:2,按单据汇总
	// ONE:9,逐笔
  private Integer _extTable;	// 扩展属性表 <表主键:SysTable>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlJournal init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _subject=null;	// 科目字典 <表主键:GlSubject>  INT
    _currency=OCurrency.DEFAULT.getLine().getKey();	// 币种 <OCurrency>  BYTE
    _balance=ZERO;	// 余额  DEC(16,2)
    _balanceUse=ZERO;	// 可用余额  DEC(16,2)
    _state=OJlState.DEFAULT.getLine().getKey();	// 状态 <OJlState>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _rem=null;	// 备注  STR(200)
    _interestAccrual=OInterestAccrual.DEFAULT.getLine().getKey();	// 计息标志 <OInterestAccrual>  BYTE
    _frostFlag=OFrostFlag.DEFAULT.getLine().getKey();	// 冻结标志 <OFrostFlag>  BYTE
    _accType=OAccType.DEFAULT.getLine().getKey();	// 账户类型 <OAccType>  BYTE
    _objPkey=null;	// 对象  LONG
    _inFlag=OYn.DEFAULT.getLine().getKey();	// 表内标志 <OYn>  BYTE
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _accJournalType=OAccJournalType.DEFAULT.getLine().getKey();	// 明细账金额类型 <OAccJournalType>  BYTE
    _tallyFlag=OTallyFlag.DEFAULT.getLine().getKey();	// 记明细汇总标志 <OTallyFlag>  BYTE
    _extTable=null;	// 扩展属性表 <表主键:SysTable>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlJournal loadUniqueCode(boolean lockFlag,String code) {
    return (GlJournal)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GlJournal chkUniqueCode(boolean lockFlag,String code) {
    return (GlJournal)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getCell(){
    return _cell;
  }
  public void setCell(Integer cell){
    _cell=cell;
  }
  public SysCell gtCell(){
    if(getCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getCell());
  }
  public void stCell(SysCell cell){
    if(cell==null)
      setCell(null);
    else
      setCell(cell.getPkey());
  }
  public Integer getSubject(){
    return _subject;
  }
  public void setSubject(Integer subject){
    _subject=subject;
  }
  public GlSubject gtSubject(){
    if(getSubject()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubject());
  }
  public void stSubject(GlSubject subject){
    if(subject==null)
      setSubject(null);
    else
      setSubject(subject.getPkey());
  }
  public Byte getCurrency(){
    return _currency;
  }
  public void setCurrency(Byte currency){
    _currency=currency;
  }
  public OCurrency gtCurrency(){
    return (OCurrency)(OCurrency.RMB.getLine().get(_currency));
  }
  public void stCurrency(OCurrency currency){
    _currency=currency.getLine().getKey();
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public BigDecimal getBalanceUse(){
    return _balanceUse;
  }
  public void setBalanceUse(BigDecimal balanceUse){
    _balanceUse=balanceUse;
  }
  public Byte getState(){
    return _state;
  }
  public void setState(Byte state){
    _state=state;
  }
  public OJlState gtState(){
    return (OJlState)(OJlState.NORMAL.getLine().get(_state));
  }
  public void stState(OJlState state){
    _state=state.getLine().getKey();
  }
  public Integer getOrg(){
    return _org;
  }
  public void setOrg(Integer org){
    _org=org;
  }
  public SysOrg gtOrg(){
    if(getOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrg());
  }
  public void stOrg(SysOrg org){
    if(org==null)
      setOrg(null);
    else
      setOrg(org.getPkey());
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Byte getInterestAccrual(){
    return _interestAccrual;
  }
  public void setInterestAccrual(Byte interestAccrual){
    _interestAccrual=interestAccrual;
  }
  public OInterestAccrual gtInterestAccrual(){
    return (OInterestAccrual)(OInterestAccrual.NO.getLine().get(_interestAccrual));
  }
  public void stInterestAccrual(OInterestAccrual interestAccrual){
    _interestAccrual=interestAccrual.getLine().getKey();
  }
  public Byte getFrostFlag(){
    return _frostFlag;
  }
  public void setFrostFlag(Byte frostFlag){
    _frostFlag=frostFlag;
  }
  public OFrostFlag gtFrostFlag(){
    return (OFrostFlag)(OFrostFlag.NORMAL.getLine().get(_frostFlag));
  }
  public void stFrostFlag(OFrostFlag frostFlag){
    _frostFlag=frostFlag.getLine().getKey();
  }
  public Byte getAccType(){
    return _accType;
  }
  public void setAccType(Byte accType){
    _accType=accType;
  }
  public OAccType gtAccType(){
    return (OAccType)(OAccType.ONE.getLine().get(_accType));
  }
  public void stAccType(OAccType accType){
    _accType=accType.getLine().getKey();
  }
  public Long getObjPkey(){
    return _objPkey;
  }
  public void setObjPkey(Long objPkey){
    _objPkey=objPkey;
  }
  //外部主键对象: IBeanLong
  public Bean gtObjPkey(){
    return (Bean)gtLongTbObj(getObjPkey());
  }
  public void stObjPkey(Bean objPkey){
      setObjPkey(objPkey.gtLongPkey());
  }
  public Byte getInFlag(){
    return _inFlag;
  }
  public void setInFlag(Byte inFlag){
    _inFlag=inFlag;
  }
  public Boolean gtInFlag(){
    return byteToBoolean(_inFlag);
  }
  public void stInFlag(Boolean inFlag){
    _inFlag=booleanToByte(inFlag);
  }
  public Byte getDirect(){
    return _direct;
  }
  public void setDirect(Byte direct){
    _direct=direct;
  }
  public ODirect gtDirect(){
    return (ODirect)(ODirect.CR.getLine().get(_direct));
  }
  public void stDirect(ODirect direct){
    _direct=direct.getLine().getKey();
  }
  public Byte getAccJournalType(){
    return _accJournalType;
  }
  public void setAccJournalType(Byte accJournalType){
    _accJournalType=accJournalType;
  }
  public OAccJournalType gtAccJournalType(){
    return (OAccJournalType)(OAccJournalType.AMT.getLine().get(_accJournalType));
  }
  public void stAccJournalType(OAccJournalType accJournalType){
    _accJournalType=accJournalType.getLine().getKey();
  }
  public Byte getTallyFlag(){
    return _tallyFlag;
  }
  public void setTallyFlag(Byte tallyFlag){
    _tallyFlag=tallyFlag;
  }
  public OTallyFlag gtTallyFlag(){
    return (OTallyFlag)(OTallyFlag.ONE.getLine().get(_tallyFlag));
  }
  public void stTallyFlag(OTallyFlag tallyFlag){
    _tallyFlag=tallyFlag.getLine().getKey();
  }
  public Integer getExtTable(){
    return _extTable;
  }
  public void setExtTable(Integer extTable){
    _extTable=extTable;
  }
  public SysTable gtExtTable(){
    if(getExtTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getExtTable());
  }
  public void stExtTable(SysTable extTable){
    if(extTable==null)
      setExtTable(null);
    else
      setExtTable(extTable.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
