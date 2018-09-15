package irille.pss.pur;

import irille.pss.lgt.LgtShipMode;
import irille.pss.sal.Sal;
import irille.pss.sal.Sal.OCreditLevel;
import irille.pss.sal.Sal.OShipType;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmb;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.math.BigDecimal;
import java.util.Date;

public class CmbPurProt extends BeanInt<CmbPurProt> {
	public static final Tb TB = new TbCmb(CmbPurProt.class, "采购协议信息");

	public enum T implements IEnumFld {//@formatter:off
		SETTLE(SYS.STR__100_NULL, "结算方式"),
		SETTLE_MONTH(SYS.YN, "是否月结"),
		SETTLE_NEXT_DAY(SYS.INT, "次月天数"),
		CREDIT_LEVEL(TB.crt(Sal.OCreditLevel.LO)),
		CREDIT_LIMIT(SYS.AMT, "信用额度"),
		CREDIT_OTHER(SYS.AMT, "押抵金额"), //表示允许他方欠款的金额
		TAX_RATE(SYS.RATE, "税点(%)"),
		DESC_KIND(SYS.STR__100_NULL, "产品质量"),
		DESC_SAL(SYS.STR__100_NULL, "年承诺销售数量"),
		PACK_DEMAND(SYS.PACK_DEMAND, true), //包装要求
		SHIP_MODE(LgtShipMode.fldOutKey().setNull()), //运输方式
		SHIP_TYPE(TB.crt(Sal.OShipType.WAIT)), //运费支付方式
		DATE_PROT(SYS.DATE, "协议签订日期"),
		DATE_START(SYS.DATE, "启用日期"),
		DATE_END(SYS.DATE, "到期日期", true),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		T.SETTLE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	public static Fld fldCmb(String code,String name, boolean isnull) {
		FldVCmb fld = TB.crtCmb(code, name, TB);
		return fld.setNullCmb(isnull);
	}
	//@formatter:on

	//>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
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

	@Override
  public CmbPurProt init(){
		super.init();
    _settle=null;	// 结算方式  STR(100)
    _settleMonth=1;	// 是否月结 <OYn>  BYTE
    _settleNextDay=0;	// 次月天数  INT
    _creditLevel=1;	// 信用等级 <OCreditLevel>  BYTE
    _creditLimit=ZERO;	// 信用额度  DEC(16,2)
    _creditOther=ZERO;	// 押抵金额  DEC(16,2)
    _taxRate=ZERO;	// 税点(%)  DEC(8,4)
    _descKind=null;	// 产品质量  STR(100)
    _descSal=null;	// 年承诺销售数量  STR(100)
    _packDemand=null;	// 包装要求  STR(200)
    _shipMode=null;	// 运输方式 <表主键:LgtShipMode>  INT
    _shipType=3;	// 费用承担方式 <OShipType>  BYTE
    _dateProt=null;	// 协议签订日期  DATE
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    return this;
  }

  //方法----------------------------------------------
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

}
