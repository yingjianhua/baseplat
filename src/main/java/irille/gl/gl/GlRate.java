package irille.gl.gl;

import irille.core.sys.SysUser;
import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 
 * @author whx
 * 
 */

public class GlRate extends BeanInt<GlRate>{
	private static final Log LOG = new Log(GlRate.class);
	public static final Tb TB = new Tb(GlRate.class, "利率表").addActIUDL().setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		RATE_TYPE(GlRateType.fldOutKey()),
		BDATE(SYS.DATE,"启用日期"),
		BASE_RATE(SYS.RATE,"基准利率"),
		FLOAT_UP(SYS.RATE,"最大上浮幅度"),
		FLOAT_DOWN(SYS.RATE,"最大下浮幅度"),
		ENABLED(SYS.ENABLED),
		INTEREST_RATE(SYS.RATE,"罚息利率"),
		IR_FLOAT_UP(SYS.RATE,"罚息最大上浮幅度"),
		IR_FLOAT_DOWN(SYS.RATE,"罚息最大下浮幅度"),
		REM(SYS.REM__200_NULL),
		CREATED_BY(SYS.CREATED_BY),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_RATE_TYPE_BDATE = TB.addIndex("rateTypeBdate",
				true,RATE_TYPE,BDATE);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _rateType;	// 利率类型 <表主键:GlRateType>  INT
  private Date _bdate;	// 启用日期  DATE
  private BigDecimal _baseRate;	// 基准利率  DEC(8,4)
  private BigDecimal _floatUp;	// 最大上浮幅度  DEC(8,4)
  private BigDecimal _floatDown;	// 最大下浮幅度  DEC(8,4)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private BigDecimal _interestRate;	// 罚息利率  DEC(8,4)
  private BigDecimal _irFloatUp;	// 罚息最大上浮幅度  DEC(8,4)
  private BigDecimal _irFloatDown;	// 罚息最大下浮幅度  DEC(8,4)
  private String _rem;	// 备注  STR(200)<null>
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlRate init(){
		super.init();
    _rateType=null;	// 利率类型 <表主键:GlRateType>  INT
    _bdate=null;	// 启用日期  DATE
    _baseRate=ZERO;	// 基准利率  DEC(8,4)
    _floatUp=ZERO;	// 最大上浮幅度  DEC(8,4)
    _floatDown=ZERO;	// 最大下浮幅度  DEC(8,4)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _interestRate=ZERO;	// 罚息利率  DEC(8,4)
    _irFloatUp=ZERO;	// 罚息最大上浮幅度  DEC(8,4)
    _irFloatDown=ZERO;	// 罚息最大下浮幅度  DEC(8,4)
    _rem=null;	// 备注  STR(200)
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlRate loadUniqueRateTypeBdate(boolean lockFlag,Integer rateType,Date bdate) {
    return (GlRate)loadUnique(T.IDX_RATE_TYPE_BDATE,lockFlag,rateType,bdate);
  }
  public static GlRate chkUniqueRateTypeBdate(boolean lockFlag,Integer rateType,Date bdate) {
    return (GlRate)chkUnique(T.IDX_RATE_TYPE_BDATE,lockFlag,rateType,bdate);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getRateType(){
    return _rateType;
  }
  public void setRateType(Integer rateType){
    _rateType=rateType;
  }
  public GlRateType gtRateType(){
    if(getRateType()==null)
      return null;
    return (GlRateType)get(GlRateType.class,getRateType());
  }
  public void stRateType(GlRateType rateType){
    if(rateType==null)
      setRateType(null);
    else
      setRateType(rateType.getPkey());
  }
  public Date getBdate(){
    return _bdate;
  }
  public void setBdate(Date bdate){
    _bdate=bdate;
  }
  public BigDecimal getBaseRate(){
    return _baseRate;
  }
  public void setBaseRate(BigDecimal baseRate){
    _baseRate=baseRate;
  }
  public BigDecimal getFloatUp(){
    return _floatUp;
  }
  public void setFloatUp(BigDecimal floatUp){
    _floatUp=floatUp;
  }
  public BigDecimal getFloatDown(){
    return _floatDown;
  }
  public void setFloatDown(BigDecimal floatDown){
    _floatDown=floatDown;
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
  public BigDecimal getInterestRate(){
    return _interestRate;
  }
  public void setInterestRate(BigDecimal interestRate){
    _interestRate=interestRate;
  }
  public BigDecimal getIrFloatUp(){
    return _irFloatUp;
  }
  public void setIrFloatUp(BigDecimal irFloatUp){
    _irFloatUp=irFloatUp;
  }
  public BigDecimal getIrFloatDown(){
    return _irFloatDown;
  }
  public void setIrFloatDown(BigDecimal irFloatDown){
    _irFloatDown=irFloatDown;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Integer getCreatedBy(){
    return _createdBy;
  }
  public void setCreatedBy(Integer createdBy){
    _createdBy=createdBy;
  }
  public SysUser gtCreatedBy(){
    if(getCreatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreatedBy());
  }
  public void stCreatedBy(SysUser createdBy){
    if(createdBy==null)
      setCreatedBy(null);
    else
      setCreatedBy(createdBy.getPkey());
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
