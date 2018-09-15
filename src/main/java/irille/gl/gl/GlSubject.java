package irille.gl.gl;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysTemplat;
import irille.core.sys.Sys.OCurrency;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl.OAccJournalType;
import irille.gl.gl.Gl.OAccType;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OSubjectKind;
import irille.gl.gl.Gl.OTallyFlag;
import irille.gl.gl.Gl.OUseScope;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * 二级科目如按对象核算，第二级的代码为统一为“账户类型”的编码，即单账户为1，多账户为2，机构为61....
 * @author surface1
 *
 */
public class GlSubject extends BeanInt<GlSubject> implements IPubVars, IExtName {
	private static final Log LOG = new Log(GlSubject.class);
	public static final Tb TB = new Tb(GlSubject.class, "科目字典").addActIUDL().addActEnabled()
	    .setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		TEMPLAT(SysTemplat.fldOutKey()),
		SUBJECT_UP(GlSubject.fldOutKey().setName("上级科目").setNull()),
		CODE(SYS.CODE__20,"科目号"),
		NAME(SYS.NAME__100),
		SUBJECT_KIND(TB.crt(Gl.OSubjectKind.BW)),
		CURRENCY(SYS.CURRENCY),
		DIRECT(GL.DIRECT),
		TOTAL_FLAG(SYS.NY,"是否汇总科目"),
		ENABLED(SYS.ENABLED),
		ACC_JOURNAL_TYPE(TB.crt(Gl.OAccJournalType.AMT)),
		TALLY_FLAG(TB.crt(Gl.OTallyFlag.DEFAULT)),	
		USE_SCOPE(TB.crt(Gl.OUseScope.ORG)),	
		ACC_TYPE(TB.crt(Gl.OAccType.ONE)), //2级代码自动建立
		AUTO_CRT(SYS.NY,"可否自动建账户"), //非多账户类型的账户可以自动建立
		WRITEOFF_FLAG(SYS.NY,"是否设销账"),
		IN_FLAG(GL.IN_FLAG),
		REM(SYS.REM__200_NULL),
		LINES(Tb.crtLines(T.SUBJECT_UP, CN_LINES,	T.PKEY).setName("子科目明细")),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		;
		
		// 索引
		public static final Index IDX_TEMP_CODE = TB.addIndex("tempCode", true,TEMPLAT,CODE);
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
		T.IN_FLAG.getFld().setDefaultValue(Sys.OYn.YES);
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = Tb.crtOutKey(TB, code, name);
		fld.setType(null);
		return fld;
	}
	//@formatter:on

	public static GlSubject gt(SysCell cell, String subjectCode) {
		return chkUniqueTempCode(false, cell.getTemplat(), subjectCode);
	}

	/**
	 * 根据模板与别名取科目
	 */
	public static final GlSubject gtByTemplatAlias(SysTemplat templat, String alias) {
		GlSubjectMap map = GlSubjectMapDAO.getByAlias(templat, alias);
		if (map == null)
			throw LOG.err("noMap", "模板[{0}]，科目别名[{1}]未定义", templat.getName(), alias);
		if (map.getSubject() == null)
			throw LOG.err("noSubject", "模板[{0}]，科目别名[{1}]未制定科目字典", templat.getName(), alias);
		return map.gtSubject();
	}

	/**
	 * 根据模板与别名取科目
	 */
	public static final GlSubject gtByTemplatAlias(SysTemplat templat, String sourceAliasOrSubject, String aliasTarget) {
		GlSubjectMap map = GlSubjectMapDAO.getByAlias(templat, sourceAliasOrSubject, aliasTarget);
		if (map == null)
			throw LOG.err("noMap", "模板[{0}]，科目别名[{1}-{2}]未定义", templat.getName(), sourceAliasOrSubject, aliasTarget);
		if (map.getSubject() == null)
			throw LOG.err("noSubject", "模板[{0}]，科目别名[{1}-{2}]未制定科目字典", templat.getName(), sourceAliasOrSubject, aliasTarget);
		return map.gtSubject();
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _templat;	// 财务模板 <表主键:SysTemplat>  INT
  private Integer _subjectUp;	// 上级科目 <表主键:GlSubject>  INT<null>
  private String _code;	// 科目号  STR(20)
  private String _name;	// 名称  STR(100)
  private Byte _subjectKind;	// 分类 <OSubjectKind>  BYTE
	// XJ:11,现金
	// YHCK:12,银行存款
	// LDZC:13,流动资产
	// CH:14,存货
	// FLDZC:15,非流动资产
	// GDZC:16,固定资产
	// LJZJ:17,累计折旧
	// LDFZ:21,流动负债
	// ZB:31,资本
	// LJYYBNLR:32,累计盈余--本年利润
	// LJYY:33,累计盈余
	// SCCB:41,生产成本
	// SR:51,收入
	// QTSR:52,其他收入
	// XSCB:53,销售成本
	// QTFY:54,其他费用
	// FY:55,费用
	// BW:91,表外
  private Byte _currency;	// 币种 <OCurrency>  BYTE
	// RMB:1,人民币
	// MY:2,美元
	// OY:3,欧元
	// GB:4,港币
	// RY:5,日元
	// HB:6,韩币
  private Byte _direct;	// 借贷标志 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
  private Byte _totalFlag;	// 是否汇总科目 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _accJournalType;	// 明细账金额类型 <OAccJournalType>  BYTE
	// AMT:1,金额
	// QTY_AMT:2,数量金额
	// QTY_AMT_BATCH:3,分批次数量金额
  private Byte _tallyFlag;	// 记明细汇总标志 <OTallyFlag>  BYTE
	// TOTAL:1,每日汇总成一笔
	// BILL:2,按单据汇总
	// ONE:9,逐笔
  private Byte _useScope;	// 应用范围 <OUseScope>  BYTE
	// ORG:1,机构
	// CELL:2,核算单元
	// ALL:3,机构与核算单元都可用
	// CLEARING_CENTER:11,清算中心
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
  private Byte _autoCrt;	// 可否自动建账户 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _writeoffFlag;	// 是否设销账 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _inFlag;	// 表内标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlSubject init(){
		super.init();
    _templat=null;	// 财务模板 <表主键:SysTemplat>  INT
    _subjectUp=null;	// 上级科目 <表主键:GlSubject>  INT
    _code=null;	// 科目号  STR(20)
    _name=null;	// 名称  STR(100)
    _subjectKind=OSubjectKind.DEFAULT.getLine().getKey();	// 分类 <OSubjectKind>  BYTE
    _currency=OCurrency.DEFAULT.getLine().getKey();	// 币种 <OCurrency>  BYTE
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _totalFlag=OYn.DEFAULT.getLine().getKey();	// 是否汇总科目 <OYn>  BYTE
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _accJournalType=OAccJournalType.DEFAULT.getLine().getKey();	// 明细账金额类型 <OAccJournalType>  BYTE
    _tallyFlag=OTallyFlag.DEFAULT.getLine().getKey();	// 记明细汇总标志 <OTallyFlag>  BYTE
    _useScope=OUseScope.DEFAULT.getLine().getKey();	// 应用范围 <OUseScope>  BYTE
    _accType=OAccType.DEFAULT.getLine().getKey();	// 账户类型 <OAccType>  BYTE
    _autoCrt=OYn.DEFAULT.getLine().getKey();	// 可否自动建账户 <OYn>  BYTE
    _writeoffFlag=OYn.DEFAULT.getLine().getKey();	// 是否设销账 <OYn>  BYTE
    _inFlag=OYn.DEFAULT.getLine().getKey();	// 表内标志 <OYn>  BYTE
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlSubject loadUniqueTempCode(boolean lockFlag,Integer templat,String code) {
    return (GlSubject)loadUnique(T.IDX_TEMP_CODE,lockFlag,templat,code);
  }
  public static GlSubject chkUniqueTempCode(boolean lockFlag,Integer templat,String code) {
    return (GlSubject)chkUnique(T.IDX_TEMP_CODE,lockFlag,templat,code);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getTemplat(){
    return _templat;
  }
  public void setTemplat(Integer templat){
    _templat=templat;
  }
  public SysTemplat gtTemplat(){
    if(getTemplat()==null)
      return null;
    return (SysTemplat)get(SysTemplat.class,getTemplat());
  }
  public void stTemplat(SysTemplat templat){
    if(templat==null)
      setTemplat(null);
    else
      setTemplat(templat.getPkey());
  }
  public Integer getSubjectUp(){
    return _subjectUp;
  }
  public void setSubjectUp(Integer subjectUp){
    _subjectUp=subjectUp;
  }
  public GlSubject gtSubjectUp(){
    if(getSubjectUp()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubjectUp());
  }
  public void stSubjectUp(GlSubject subjectUp){
    if(subjectUp==null)
      setSubjectUp(null);
    else
      setSubjectUp(subjectUp.getPkey());
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
  public Byte getSubjectKind(){
    return _subjectKind;
  }
  public void setSubjectKind(Byte subjectKind){
    _subjectKind=subjectKind;
  }
  public OSubjectKind gtSubjectKind(){
    return (OSubjectKind)(OSubjectKind.BW.getLine().get(_subjectKind));
  }
  public void stSubjectKind(OSubjectKind subjectKind){
    _subjectKind=subjectKind.getLine().getKey();
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
  public Byte getTotalFlag(){
    return _totalFlag;
  }
  public void setTotalFlag(Byte totalFlag){
    _totalFlag=totalFlag;
  }
  public Boolean gtTotalFlag(){
    return byteToBoolean(_totalFlag);
  }
  public void stTotalFlag(Boolean totalFlag){
    _totalFlag=booleanToByte(totalFlag);
  }
  public Byte getEnabled(){
    return _enabled;
  }
  public void setEnabled(Byte enabled){
    _enabled=enabled;
  }
  public Boolean gtEnabled(){
    return byteToBoolean(_enabled);
  }
  public void stEnabled(Boolean enabled){
    _enabled=booleanToByte(enabled);
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
  public Byte getUseScope(){
    return _useScope;
  }
  public void setUseScope(Byte useScope){
    _useScope=useScope;
  }
  public OUseScope gtUseScope(){
    return (OUseScope)(OUseScope.ORG.getLine().get(_useScope));
  }
  public void stUseScope(OUseScope useScope){
    _useScope=useScope.getLine().getKey();
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
  public Byte getAutoCrt(){
    return _autoCrt;
  }
  public void setAutoCrt(Byte autoCrt){
    _autoCrt=autoCrt;
  }
  public Boolean gtAutoCrt(){
    return byteToBoolean(_autoCrt);
  }
  public void stAutoCrt(Boolean autoCrt){
    _autoCrt=booleanToByte(autoCrt);
  }
  public Byte getWriteoffFlag(){
    return _writeoffFlag;
  }
  public void setWriteoffFlag(Byte writeoffFlag){
    _writeoffFlag=writeoffFlag;
  }
  public Boolean gtWriteoffFlag(){
    return byteToBoolean(_writeoffFlag);
  }
  public void stWriteoffFlag(Boolean writeoffFlag){
    _writeoffFlag=booleanToByte(writeoffFlag);
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
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public static java.util.List<GlSubject> getLines(irille.gl.gl.GlSubject mainBean){
    return list(irille.gl.gl.GlSubject.class,
        " subject_up=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<GlSubject> getLines(irille.gl.gl.GlSubject mainBean, int idx,int count){
    return list(irille.gl.gl.GlSubject.class,false," subject_up=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.gl.gl.GlSubject mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM gl_subject WHERE subject_up=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
