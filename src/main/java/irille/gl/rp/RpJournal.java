package irille.gl.rp;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OEnabled;
import irille.gl.gl.GlJournal;
import irille.gl.rp.Rp.ORpJournalType;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

public class RpJournal extends BeanLong<RpJournal> implements IExtName {
	private static final Log LOG = new Log(RpJournal.class);

	public static final Tb TB = new Tb(RpJournal.class, "出纳日记账").setAutoLocal().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		JL_AMT(GlJournal.fldOneToOne()),
		CODE(SYS.CODE__40,"代码"),
		NAME(SYS.NAME__100,"名称"),
		ENABLED(TB.crt(SYS.ENABLED)),
		TYPE(TB.crt(ORpJournalType.CASHIER)),
		YESTODAY_BALANCE(SYS.BALANCE,"昨天余额"),
		DR_QTY(SYS.DR_QTY,"今日收入笔数"),
		DR_AMT(SYS.DR_AMT,"今日收入金额"),
		CR_QTY(SYS.CR_QTY,"今日支出笔数"),
		CR_AMT(SYS.CR_AMT,"今日支出金额"),
		BALANCE(SYS.BALANCE,"今日余额"),
		BANK_NAME(SYS.STR__40_NULL,"银行名称"), //如为现金，则银行信息为空
		BANK_ACC_CODE(SYS.STR__40_NULL,"银行账号"), 		
		BANK_ACC_NAME(SYS.NAME__100_NULL,"账户名称"),
		WORK_BOX(RpWorkBox.fldOutKey()), //所属工作箱
		CASHIER(SYS.CASHIER), //此字段为所属工作箱的保管人，不可在些表中修改
		ORG(SYS.ORG), //自动从分户账中取
		CELL(SYS.CELL),//自动从分户账中取
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ORG = TB.addIndex("org", false, ORG);
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CODE.getFld().getTb();//加锁所有字段,不可以修改
	}
	//@formatter:on
	public String getErrMsg() {
		return getCode()+" : "+getName();
	}
	
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 代码  STR(40)
  private String _name;	// 名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _rpJournalType;	// 账户类型 <ORpJournalType>  BYTE
	// CASHIER:1,现金
	// COM:2,对公账户
	// CARD:3,对私卡账户
	// OTHER:9,第三方支付
  private BigDecimal _yestodayBalance;	// 昨天余额  DEC(16,2)
  private Integer _drQty;	// 今日收入笔数  INT
  private BigDecimal _drAmt;	// 今日收入金额  DEC(16,2)
  private Integer _crQty;	// 今日支出笔数  INT
  private BigDecimal _crAmt;	// 今日支出金额  DEC(16,2)
  private BigDecimal _balance;	// 今日余额  DEC(16,2)
  private String _bankName;	// 银行名称  STR(40)<null>
  private String _bankAccCode;	// 银行账号  STR(40)<null>
  private String _bankAccName;	// 账户名称  STR(100)<null>
  private Long _workBox;	// 所属工作箱 <表主键:RpWorkBox>  LONG
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpJournal init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rpJournalType=ORpJournalType.DEFAULT.getLine().getKey();	// 账户类型 <ORpJournalType>  BYTE
    _yestodayBalance=ZERO;	// 昨天余额  DEC(16,2)
    _drQty=0;	// 今日收入笔数  INT
    _drAmt=ZERO;	// 今日收入金额  DEC(16,2)
    _crQty=0;	// 今日支出笔数  INT
    _crAmt=ZERO;	// 今日支出金额  DEC(16,2)
    _balance=ZERO;	// 今日余额  DEC(16,2)
    _bankName=null;	// 银行名称  STR(40)
    _bankAccCode=null;	// 银行账号  STR(40)
    _bankAccName=null;	// 账户名称  STR(100)
    _workBox=null;	// 所属工作箱 <表主键:RpWorkBox>  LONG
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对一表对象: GlJournal
  public GlJournal gtJournal(){
    return get(GlJournal.class,getPkey());
  }
  public void stJournal(GlJournal journal){
      setPkey(journal.getPkey());
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
  public Byte getRpJournalType(){
    return _rpJournalType;
  }
  public void setRpJournalType(Byte rpJournalType){
    _rpJournalType=rpJournalType;
  }
  public ORpJournalType gtRpJournalType(){
    return (ORpJournalType)(ORpJournalType.CASHIER.getLine().get(_rpJournalType));
  }
  public void stRpJournalType(ORpJournalType rpJournalType){
    _rpJournalType=rpJournalType.getLine().getKey();
  }
  public BigDecimal getYestodayBalance(){
    return _yestodayBalance;
  }
  public void setYestodayBalance(BigDecimal yestodayBalance){
    _yestodayBalance=yestodayBalance;
  }
  public Integer getDrQty(){
    return _drQty;
  }
  public void setDrQty(Integer drQty){
    _drQty=drQty;
  }
  public BigDecimal getDrAmt(){
    return _drAmt;
  }
  public void setDrAmt(BigDecimal drAmt){
    _drAmt=drAmt;
  }
  public Integer getCrQty(){
    return _crQty;
  }
  public void setCrQty(Integer crQty){
    _crQty=crQty;
  }
  public BigDecimal getCrAmt(){
    return _crAmt;
  }
  public void setCrAmt(BigDecimal crAmt){
    _crAmt=crAmt;
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public String getBankName(){
    return _bankName;
  }
  public void setBankName(String bankName){
    _bankName=bankName;
  }
  public String getBankAccCode(){
    return _bankAccCode;
  }
  public void setBankAccCode(String bankAccCode){
    _bankAccCode=bankAccCode;
  }
  public String getBankAccName(){
    return _bankAccName;
  }
  public void setBankAccName(String bankAccName){
    _bankAccName=bankAccName;
  }
  public Long getWorkBox(){
    return _workBox;
  }
  public void setWorkBox(Long workBox){
    _workBox=workBox;
  }
  public RpWorkBox gtWorkBox(){
    if(getWorkBox()==null)
      return null;
    return (RpWorkBox)get(RpWorkBox.class,getWorkBox());
  }
  public void stWorkBox(RpWorkBox workBox){
    if(workBox==null)
      setWorkBox(null);
    else
      setWorkBox(workBox.getPkey());
  }
  public Integer getCashier(){
    return _cashier;
  }
  public void setCashier(Integer cashier){
    _cashier=cashier;
  }
  public SysUser gtCashier(){
    if(getCashier()==null)
      return null;
    return (SysUser)get(SysUser.class,getCashier());
  }
  public void stCashier(SysUser cashier){
    if(cashier==null)
      setCashier(null);
    else
      setCashier(cashier.getPkey());
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
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
