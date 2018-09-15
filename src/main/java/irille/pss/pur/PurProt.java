package irille.pss.pur;

import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OYn;
import irille.pss.lgt.LgtShipMode;
import irille.pss.sal.Sal.OCreditLevel;
import irille.pss.sal.Sal.OShipType;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 信用额度（海川）：约定允许欠款的额度，账期到后，需要全额付款
 * 信用额度（海川）：约定产品质量、年承诺销售数量
 * 押抵金额（科盛）：约定允许欠款的额度，账期到后，这部分可以不付款，仅付超出额度的金额
 * 税点（科盛）：采购中与供应商约定，收货单中价格是原单价，另做运费发票表示税金，分摊到本次采购的成本中
 * TODO 结算方式 上面是一部分，电汇、汇票、货到付款。。。理解的不是很好
 * TODO 采购中的折扣暂未考虑
 * @author whx
 * @version 创建时间：2014年8月29日 下午1:45:26
 */
public class PurProt extends BeanInt<PurProt> {
	public static final Tb TB = new Tb(PurProt.class, "供应商协议").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		TEMPLAT(SYS.TEMPLAT),
		SUPPLIER(SYS.SUPPLIER), //供应商
		NAME(SYS.NAME__100, "供应商名称"),
		CMB_PUR_PROT(CmbPurProt.fldFlds()),
		UPDATED_BY(SYS.UPDATED_BY), //更新员
		UPDATED_TIME(SYS.UPDATED_DATE_TIME), //更新日期
		REM(SYS.REM__200_NULL), //备注
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
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
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_TEMP_CUST = TB.addIndex("tempCust", true,TEMPLAT, SUPPLIER);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _templat;	// 科目模板 <表主键:SysTemplat>  INT
  private Integer _supplier;	// 供应商 <表主键:SysSupplier>  INT
  private String _name;	// 供应商名称  STR(100)
  private String _settle;	// 结算方式  STR(100)<null>
  private Byte _settleMonth;	// 是否月结 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Integer _settleNextDay;	// 次月天数  INT
  private Byte _creditLevel;	// 信用等级 <OCreditLevel>  BYTE
	// LO:1,低
	// MI:2,中
	// HI:3,高
  private BigDecimal _creditLimit;	// 信用额度  DEC(16,2)
  private BigDecimal _creditOther;	// 押抵金额  DEC(16,2)
  private BigDecimal _taxRate;	// 税点(%)  DEC(8,4)
  private String _descKind;	// 产品质量  STR(100)<null>
  private String _descSal;	// 年承诺销售数量  STR(100)<null>
  private String _packDemand;	// 包装要求  STR(200)<null>
  private Integer _shipMode;	// 运输方式 <表主键:LgtShipMode>  INT<null>
  private Byte _shipType;	// 费用承担方式 <OShipType>  BYTE
	// MY:1,我方支付
	// OTHER:2,他方支付
	// WAIT:3,待定
  private Date _dateProt;	// 协议签订日期  DATE
  private Date _dateStart;	// 启用日期  DATE
  private Date _dateEnd;	// 到期日期  DATE<null>
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PurProt init(){
		super.init();
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _supplier=null;	// 供应商 <表主键:SysSupplier>  INT
    _name=null;	// 供应商名称  STR(100)
    _settle=null;	// 结算方式  STR(100)
    _settleMonth=OYn.DEFAULT.getLine().getKey();	// 是否月结 <OYn>  BYTE
    _settleNextDay=0;	// 次月天数  INT
    _creditLevel=OCreditLevel.DEFAULT.getLine().getKey();	// 信用等级 <OCreditLevel>  BYTE
    _creditLimit=ZERO;	// 信用额度  DEC(16,2)
    _creditOther=ZERO;	// 押抵金额  DEC(16,2)
    _taxRate=ZERO;	// 税点(%)  DEC(8,4)
    _descKind=null;	// 产品质量  STR(100)
    _descSal=null;	// 年承诺销售数量  STR(100)
    _packDemand=null;	// 包装要求  STR(200)
    _shipMode=null;	// 运输方式 <表主键:LgtShipMode>  INT
    _shipType=OShipType.DEFAULT.getLine().getKey();	// 费用承担方式 <OShipType>  BYTE
    _dateProt=null;	// 协议签订日期  DATE
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PurProt loadUniqueTempCust(boolean lockFlag,Integer templat,Integer supplier) {
    return (PurProt)loadUnique(T.IDX_TEMP_CUST,lockFlag,templat,supplier);
  }
  public static PurProt chkUniqueTempCust(boolean lockFlag,Integer templat,Integer supplier) {
    return (PurProt)chkUnique(T.IDX_TEMP_CUST,lockFlag,templat,supplier);
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
  public Integer getUpdatedBy(){
    return _updatedBy;
  }
  public void setUpdatedBy(Integer updatedBy){
    _updatedBy=updatedBy;
  }
  public SysUser gtUpdatedBy(){
    if(getUpdatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getUpdatedBy());
  }
  public void stUpdatedBy(SysUser updatedBy){
    if(updatedBy==null)
      setUpdatedBy(null);
    else
      setUpdatedBy(updatedBy.getPkey());
  }
  public Date getUpdatedTime(){
    return _updatedTime;
  }
  public void setUpdatedTime(Date updatedTime){
    _updatedTime=updatedTime;
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
