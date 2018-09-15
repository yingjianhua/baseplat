package irille.pss.pur;

import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OYn;
import irille.pss.lgt.LgtShipMode;
import irille.pss.sal.Sal.OCreditLevel;
import irille.pss.sal.Sal.OShipType;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbForm;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmbFlds;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class PurProtApply extends BeanForm<PurProtApply> {
	public static final Tb TB = new Tb(PurProtApply.class, "供应商协议申请单")
			.setAutoIncrement().addActIUDL().addActApproveDo();

	public enum T implements IEnumFld {//@formatter:off
		CMB_FORM(CmbForm.fldFlds()),
		TEMPLAT(SYS.TEMPLAT),
		SUPPLIER(SYS.SUPPLIER), //供应商
		NAME(SYS.NAME__100, "供应商名称"),
		CMB_SAL_PROT(((FldVCmbFlds)CmbPurProt.fldFlds()).setNullCmb(true)),
		AFT(CmbPurProt.fldCmb("aft", "变更后")),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		CODE(TB.get("code")),	//单据号
		STATUS(TB.get("status")),	//状态
		ORG(TB.get("org")),	//机构
		DEPT(TB.get("dept")),	//部门
		CREATED_BY(TB.get("createdBy")),	//建档员
		CREATED_TIME(TB.get("createdTime")),	//建档时间
		APPR_BY(TB.get("apprBy")),	//审核员
		APPR_TIME(TB.get("apprTime")),	//审核时间
		REM(TB.get("rem")),	//备注
		SETTLE(TB.get("settle")),	//结算方式
		SETTLE_MONTH(TB.get("settleMonth")),	//是否月结
		SETTLE_NEXT_DAY(TB.get("settleNextDay")),	//次月天数
		CREDIT_LEVEL(TB.get("creditLevel")),	//信用等级
		CREDIT_LIMIT(TB.get("creditLimit")),	//信用额度
		CREDIT_OTHER(TB.get("creditOther")),	//押抵金额
		TAX_RATE(TB.get("taxRate")),	//税点(%)
		DESC_KIND(TB.get("descKind")),	//产品质量
		DESC_SAL(TB.get("descSal")),	//年承诺销售数量
		PACK_DEMAND(TB.get("packDemand")),	//包装要求
		SHIP_MODE(TB.get("shipMode")),	//运输方式
		SHIP_TYPE(TB.get("shipType")),	//费用承担方式
		DATE_PROT(TB.get("dateProt")),	//协议签订日期
		DATE_START(TB.get("dateStart")),	//启用日期
		DATE_END(TB.get("dateEnd")),	//到期日期
		AFT_SETTLE(TB.get("aftSettle")),	//变更后结算方式
		AFT_SETTLE_MONTH(TB.get("aftSettleMonth")),	//变更后是否月结
		AFT_SETTLE_NEXT_DAY(TB.get("aftSettleNextDay")),	//变更后次月天数
		AFT_CREDIT_LEVEL(TB.get("aftCreditLevel")),	//变更后信用等级
		AFT_CREDIT_LIMIT(TB.get("aftCreditLimit")),	//变更后信用额度
		AFT_CREDIT_OTHER(TB.get("aftCreditOther")),	//变更后押抵金额
		AFT_TAX_RATE(TB.get("aftTaxRate")),	//变更后税点(%)
		AFT_DESC_KIND(TB.get("aftDescKind")),	//变更后产品质量
		AFT_DESC_SAL(TB.get("aftDescSal")),	//变更后年承诺销售数量
		AFT_PACK_DEMAND(TB.get("aftPackDemand")),	//变更后包装要求
		AFT_SHIP_MODE(TB.get("aftShipMode")),	//变更后运输方式
		AFT_SHIP_TYPE(TB.get("aftShipType")),	//变更后费用承担方式
		AFT_DATE_PROT(TB.get("aftDateProt")),	//变更后协议签订日期
		AFT_DATE_START(TB.get("aftDateStart")),	//变更后启用日期
		AFT_DATE_END(TB.get("aftDateEnd")),	//变更后到期日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		public static final Index IDX_CODE= TB.addIndex("code",true,CODE);
		public static final Index IDX_ORG_TIME= TB.addIndex("orgTime",false,ORG,CREATED_TIME);
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CMB_FORM.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 单据号  STR(40)
  private Byte _status;	// 状态 <OBillStatus>  BYTE
	// INIT:11,初始
	// OK:21,已输入确认
	// VERIFING:53,复核中
	// VERIFIED:58,已复核
	// CHECKING:63,审核中
	// CHECKED:68,已审核
	// VETTING:73,审批中
	// VETTED:78,已审批
	// INOUT:81,已出入库
	// TALLY_ABLE:83,可记账
	// DONE:98,完成
	// DELETED:99,作废
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>
  private Integer _templat;	// 科目模板 <表主键:SysTemplat>  INT
  private Integer _supplier;	// 供应商 <表主键:SysSupplier>  INT
  private String _name;	// 供应商名称  STR(100)
  private String _settle;	// 结算方式  STR(100)<null>
  private Byte _settleMonth;	// 是否月结 <OYn>  BYTE<null>
	// YES:1,是
	// NO:0,否
  private Integer _settleNextDay;	// 次月天数  INT<null>
  private Byte _creditLevel;	// 信用等级 <OCreditLevel>  BYTE<null>
	// LO:1,低
	// MI:2,中
	// HI:3,高
  private BigDecimal _creditLimit;	// 信用额度  DEC(16,2)<null>
  private BigDecimal _creditOther;	// 押抵金额  DEC(16,2)<null>
  private BigDecimal _taxRate;	// 税点(%)  DEC(8,4)<null>
  private String _descKind;	// 产品质量  STR(100)<null>
  private String _descSal;	// 年承诺销售数量  STR(100)<null>
  private String _packDemand;	// 包装要求  STR(200)<null>
  private Integer _shipMode;	// 运输方式 <表主键:LgtShipMode>  INT<null>
  private Byte _shipType;	// 费用承担方式 <OShipType>  BYTE<null>
	// MY:1,我方支付
	// OTHER:2,他方支付
	// WAIT:3,待定
  private Date _dateProt;	// 协议签订日期  DATE<null>
  private Date _dateStart;	// 启用日期  DATE<null>
  private Date _dateEnd;	// 到期日期  DATE<null>
  private String _aftSettle;	// 变更后结算方式  STR(100)<null>
  private Byte _aftSettleMonth;	// 变更后是否月结 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Integer _aftSettleNextDay;	// 变更后次月天数  INT
  private Byte _aftCreditLevel;	// 变更后信用等级 <OCreditLevel>  BYTE
	// LO:1,低
	// MI:2,中
	// HI:3,高
  private BigDecimal _aftCreditLimit;	// 变更后信用额度  DEC(16,2)
  private BigDecimal _aftCreditOther;	// 变更后押抵金额  DEC(16,2)
  private BigDecimal _aftTaxRate;	// 变更后税点(%)  DEC(8,4)
  private String _aftDescKind;	// 变更后产品质量  STR(100)<null>
  private String _aftDescSal;	// 变更后年承诺销售数量  STR(100)<null>
  private String _aftPackDemand;	// 变更后包装要求  STR(200)<null>
  private Integer _aftShipMode;	// 变更后运输方式 <表主键:LgtShipMode>  INT<null>
  private Byte _aftShipType;	// 变更后费用承担方式 <OShipType>  BYTE
	// MY:1,我方支付
	// OTHER:2,他方支付
	// WAIT:3,待定
  private Date _aftDateProt;	// 变更后协议签订日期  DATE
  private Date _aftDateStart;	// 变更后启用日期  DATE
  private Date _aftDateEnd;	// 变更后到期日期  DATE<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PurProtApply init(){
		super.init();
    _code=null;	// 单据号  STR(40)
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _rem=null;	// 备注  STR(200)
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _supplier=null;	// 供应商 <表主键:SysSupplier>  INT
    _name=null;	// 供应商名称  STR(100)
    _settle=null;	// 结算方式  STR(100)
    _settleMonth=OYn.DEFAULT.getLine().getKey();	// 是否月结 <OYn>  BYTE
    _settleNextDay=null;	// 次月天数  INT
    _creditLevel=OCreditLevel.DEFAULT.getLine().getKey();	// 信用等级 <OCreditLevel>  BYTE
    _creditLimit=null;	// 信用额度  DEC(16,2)
    _creditOther=null;	// 押抵金额  DEC(16,2)
    _taxRate=null;	// 税点(%)  DEC(8,4)
    _descKind=null;	// 产品质量  STR(100)
    _descSal=null;	// 年承诺销售数量  STR(100)
    _packDemand=null;	// 包装要求  STR(200)
    _shipMode=null;	// 运输方式 <表主键:LgtShipMode>  INT
    _shipType=OShipType.DEFAULT.getLine().getKey();	// 费用承担方式 <OShipType>  BYTE
    _dateProt=null;	// 协议签订日期  DATE
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    _aftSettle=null;	// 变更后结算方式  STR(100)
    _aftSettleMonth=OYn.DEFAULT.getLine().getKey();	// 变更后是否月结 <OYn>  BYTE
    _aftSettleNextDay=0;	// 变更后次月天数  INT
    _aftCreditLevel=OCreditLevel.DEFAULT.getLine().getKey();	// 变更后信用等级 <OCreditLevel>  BYTE
    _aftCreditLimit=ZERO;	// 变更后信用额度  DEC(16,2)
    _aftCreditOther=ZERO;	// 变更后押抵金额  DEC(16,2)
    _aftTaxRate=ZERO;	// 变更后税点(%)  DEC(8,4)
    _aftDescKind=null;	// 变更后产品质量  STR(100)
    _aftDescSal=null;	// 变更后年承诺销售数量  STR(100)
    _aftPackDemand=null;	// 变更后包装要求  STR(200)
    _aftShipMode=null;	// 变更后运输方式 <表主键:LgtShipMode>  INT
    _aftShipType=OShipType.DEFAULT.getLine().getKey();	// 变更后费用承担方式 <OShipType>  BYTE
    _aftDateProt=null;	// 变更后协议签订日期  DATE
    _aftDateStart=null;	// 变更后启用日期  DATE
    _aftDateEnd=null;	// 变更后到期日期  DATE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PurProtApply loadUniqueCode(boolean lockFlag,String code) {
    return (PurProtApply)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static PurProtApply chkUniqueCode(boolean lockFlag,String code) {
    return (PurProtApply)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Byte getStatus(){
    return _status;
  }
  public void setStatus(Byte status){
    _status=status;
  }
  public OBillStatus gtStatus(){
    return (OBillStatus)(OBillStatus.INIT.getLine().get(_status));
  }
  public void stStatus(OBillStatus status){
    _status=status.getLine().getKey();
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
  public Integer getDept(){
    return _dept;
  }
  public void setDept(Integer dept){
    _dept=dept;
  }
  public SysDept gtDept(){
    if(getDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getDept());
  }
  public void stDept(SysDept dept){
    if(dept==null)
      setDept(null);
    else
      setDept(dept.getPkey());
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
  public Integer getApprBy(){
    return _apprBy;
  }
  public void setApprBy(Integer apprBy){
    _apprBy=apprBy;
  }
  public SysUser gtApprBy(){
    if(getApprBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getApprBy());
  }
  public void stApprBy(SysUser apprBy){
    if(apprBy==null)
      setApprBy(null);
    else
      setApprBy(apprBy.getPkey());
  }
  public Date getApprTime(){
    return _apprTime;
  }
  public void setApprTime(Date apprTime){
    _apprTime=apprTime;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
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
  public Integer getSupplier(){
    return _supplier;
  }
  public void setSupplier(Integer supplier){
    _supplier=supplier;
  }
  public SysSupplier gtSupplier(){
    if(getSupplier()==null)
      return null;
    return (SysSupplier)get(SysSupplier.class,getSupplier());
  }
  public void stSupplier(SysSupplier supplier){
    if(supplier==null)
      setSupplier(null);
    else
      setSupplier(supplier.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getSettle(){
    return _settle;
  }
  public void setSettle(String settle){
    _settle=settle;
  }
  public Byte getSettleMonth(){
    return _settleMonth;
  }
  public void setSettleMonth(Byte settleMonth){
    _settleMonth=settleMonth;
  }
  public Boolean gtSettleMonth(){
    return byteToBoolean(_settleMonth);
  }
  public void stSettleMonth(Boolean settleMonth){
    _settleMonth=booleanToByte(settleMonth);
  }
  public Integer getSettleNextDay(){
    return _settleNextDay;
  }
  public void setSettleNextDay(Integer settleNextDay){
    _settleNextDay=settleNextDay;
  }
  public Byte getCreditLevel(){
    return _creditLevel;
  }
  public void setCreditLevel(Byte creditLevel){
    _creditLevel=creditLevel;
  }
  public OCreditLevel gtCreditLevel(){
    return (OCreditLevel)(OCreditLevel.LO.getLine().get(_creditLevel));
  }
  public void stCreditLevel(OCreditLevel creditLevel){
    _creditLevel=creditLevel.getLine().getKey();
  }
  public BigDecimal getCreditLimit(){
    return _creditLimit;
  }
  public void setCreditLimit(BigDecimal creditLimit){
    _creditLimit=creditLimit;
  }
  public BigDecimal getCreditOther(){
    return _creditOther;
  }
  public void setCreditOther(BigDecimal creditOther){
    _creditOther=creditOther;
  }
  public BigDecimal getTaxRate(){
    return _taxRate;
  }
  public void setTaxRate(BigDecimal taxRate){
    _taxRate=taxRate;
  }
  public String getDescKind(){
    return _descKind;
  }
  public void setDescKind(String descKind){
    _descKind=descKind;
  }
  public String getDescSal(){
    return _descSal;
  }
  public void setDescSal(String descSal){
    _descSal=descSal;
  }
  public String getPackDemand(){
    return _packDemand;
  }
  public void setPackDemand(String packDemand){
    _packDemand=packDemand;
  }
  public Integer getShipMode(){
    return _shipMode;
  }
  public void setShipMode(Integer shipMode){
    _shipMode=shipMode;
  }
  public LgtShipMode gtShipMode(){
    if(getShipMode()==null)
      return null;
    return (LgtShipMode)get(LgtShipMode.class,getShipMode());
  }
  public void stShipMode(LgtShipMode shipMode){
    if(shipMode==null)
      setShipMode(null);
    else
      setShipMode(shipMode.getPkey());
  }
  public Byte getShipType(){
    return _shipType;
  }
  public void setShipType(Byte shipType){
    _shipType=shipType;
  }
  public OShipType gtShipType(){
    return (OShipType)(OShipType.WAIT.getLine().get(_shipType));
  }
  public void stShipType(OShipType shipType){
    _shipType=shipType.getLine().getKey();
  }
  public Date getDateProt(){
    return _dateProt;
  }
  public void setDateProt(Date dateProt){
    _dateProt=dateProt;
  }
  public Date getDateStart(){
    return _dateStart;
  }
  public void setDateStart(Date dateStart){
    _dateStart=dateStart;
  }
  public Date getDateEnd(){
    return _dateEnd;
  }
  public void setDateEnd(Date dateEnd){
    _dateEnd=dateEnd;
  }
  //组合对象的操作
  public CmbPurProt gtAft(){
    CmbPurProt b=new CmbPurProt();
    	b.setSettle(_aftSettle);
    	b.setSettleMonth(_aftSettleMonth);
    	b.setSettleNextDay(_aftSettleNextDay);
    	b.setCreditLevel(_aftCreditLevel);
    	b.setCreditLimit(_aftCreditLimit);
    	b.setCreditOther(_aftCreditOther);
    	b.setTaxRate(_aftTaxRate);
    	b.setDescKind(_aftDescKind);
    	b.setDescSal(_aftDescSal);
    	b.setPackDemand(_aftPackDemand);
    	b.setShipMode(_aftShipMode);
    	b.setShipType(_aftShipType);
    	b.setDateProt(_aftDateProt);
    	b.setDateStart(_aftDateStart);
    	b.setDateEnd(_aftDateEnd);
    return b;
  }
  public void stAft(CmbPurProt aft){
    _aftSettle=aft.getSettle();
    _aftSettleMonth=aft.getSettleMonth();
    _aftSettleNextDay=aft.getSettleNextDay();
    _aftCreditLevel=aft.getCreditLevel();
    _aftCreditLimit=aft.getCreditLimit();
    _aftCreditOther=aft.getCreditOther();
    _aftTaxRate=aft.getTaxRate();
    _aftDescKind=aft.getDescKind();
    _aftDescSal=aft.getDescSal();
    _aftPackDemand=aft.getPackDemand();
    _aftShipMode=aft.getShipMode();
    _aftShipType=aft.getShipType();
    _aftDateProt=aft.getDateProt();
    _aftDateStart=aft.getDateStart();
    _aftDateEnd=aft.getDateEnd();
  }
  public String getAftSettle(){
    return _aftSettle;
  }
  public void setAftSettle(String aftSettle){
    _aftSettle=aftSettle;
  }
  public Byte getAftSettleMonth(){
    return _aftSettleMonth;
  }
  public void setAftSettleMonth(Byte aftSettleMonth){
    _aftSettleMonth=aftSettleMonth;
  }
  public Boolean gtAftSettleMonth(){
    return byteToBoolean(_aftSettleMonth);
  }
  public void stAftSettleMonth(Boolean aftSettleMonth){
    _aftSettleMonth=booleanToByte(aftSettleMonth);
  }
  public Integer getAftSettleNextDay(){
    return _aftSettleNextDay;
  }
  public void setAftSettleNextDay(Integer aftSettleNextDay){
    _aftSettleNextDay=aftSettleNextDay;
  }
  public Byte getAftCreditLevel(){
    return _aftCreditLevel;
  }
  public void setAftCreditLevel(Byte aftCreditLevel){
    _aftCreditLevel=aftCreditLevel;
  }
  public OCreditLevel gtAftCreditLevel(){
    return (OCreditLevel)(OCreditLevel.LO.getLine().get(_aftCreditLevel));
  }
  public void stAftCreditLevel(OCreditLevel aftCreditLevel){
    _aftCreditLevel=aftCreditLevel.getLine().getKey();
  }
  public BigDecimal getAftCreditLimit(){
    return _aftCreditLimit;
  }
  public void setAftCreditLimit(BigDecimal aftCreditLimit){
    _aftCreditLimit=aftCreditLimit;
  }
  public BigDecimal getAftCreditOther(){
    return _aftCreditOther;
  }
  public void setAftCreditOther(BigDecimal aftCreditOther){
    _aftCreditOther=aftCreditOther;
  }
  public BigDecimal getAftTaxRate(){
    return _aftTaxRate;
  }
  public void setAftTaxRate(BigDecimal aftTaxRate){
    _aftTaxRate=aftTaxRate;
  }
  public String getAftDescKind(){
    return _aftDescKind;
  }
  public void setAftDescKind(String aftDescKind){
    _aftDescKind=aftDescKind;
  }
  public String getAftDescSal(){
    return _aftDescSal;
  }
  public void setAftDescSal(String aftDescSal){
    _aftDescSal=aftDescSal;
  }
  public String getAftPackDemand(){
    return _aftPackDemand;
  }
  public void setAftPackDemand(String aftPackDemand){
    _aftPackDemand=aftPackDemand;
  }
  public Integer getAftShipMode(){
    return _aftShipMode;
  }
  public void setAftShipMode(Integer aftShipMode){
    _aftShipMode=aftShipMode;
  }
  public LgtShipMode gtAftShipMode(){
    if(getAftShipMode()==null)
      return null;
    return (LgtShipMode)get(LgtShipMode.class,getAftShipMode());
  }
  public void stAftShipMode(LgtShipMode aftShipMode){
    if(aftShipMode==null)
      setAftShipMode(null);
    else
      setAftShipMode(aftShipMode.getPkey());
  }
  public Byte getAftShipType(){
    return _aftShipType;
  }
  public void setAftShipType(Byte aftShipType){
    _aftShipType=aftShipType;
  }
  public OShipType gtAftShipType(){
    return (OShipType)(OShipType.WAIT.getLine().get(_aftShipType));
  }
  public void stAftShipType(OShipType aftShipType){
    _aftShipType=aftShipType.getLine().getKey();
  }
  public Date getAftDateProt(){
    return _aftDateProt;
  }
  public void setAftDateProt(Date aftDateProt){
    _aftDateProt=aftDateProt;
  }
  public Date getAftDateStart(){
    return _aftDateStart;
  }
  public void setAftDateStart(Date aftDateStart){
    _aftDateStart=aftDateStart;
  }
  public Date getAftDateEnd(){
    return _aftDateEnd;
  }
  public void setAftDateEnd(Date aftDateEnd){
    _aftDateEnd=aftDateEnd;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
