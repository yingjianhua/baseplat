package irille.pss.sal;

import irille.core.sys.Sys.OYn;
import irille.pss.lgt.LgtShipMode;
import irille.pss.sal.Sal.OCreditLevel;
import irille.pss.sal.Sal.ODiscountLevel;
import irille.pss.sal.Sal.OProtocoType;
import irille.pss.sal.Sal.OShipType;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmb;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.math.BigDecimal;
import java.util.Date;

public class CmbSalProt extends BeanInt<CmbSalProt> {
	public static final Tb TB = new TbCmb(CmbSalProt.class, "客户信息");

	public enum T implements IEnumFld {//@formatter:off
		SETTLE(SYS.STR__100_NULL, "结算方式"),
		SETTLE_MONTH(SYS.YN, "是否月结"),
		SETTLE_NEXT_DAY(SYS.INT, "次月天数"),
		CREDIT_LEVEL(TB.crt(Sal.OCreditLevel.LO)),
		CREDIT_LIMIT(SYS.AMT, "信用额度"),
		CREDIT_OTHER(SYS.AMT, "押抵金额"), //表示允许他方欠款的金额
		PROTOCOL_TYPE(TB.crt(Sal.OProtocoType.RATE)),
		DISCOUNT_LEVEL(TB.crt(Sal.ODiscountLevel.ON)),
		TAX_RATE(SYS.RATE, "税点(%)"),
		PACK_DEMAND(SYS.PACK_DEMAND, true), //包装要求
		SHIP_MODE(LgtShipMode.fldOutKey().setNull()), //运输方式
		SHIP_TYPE(TB.crt(Sal.OShipType.WAIT)), //运费支付方式
		DATE_PROT(SYS.DATE, "协议签订日期"),
		DATE_START(SYS.DATE, "启用日期"),
		DATE_END(SYS.DATE, "到期日期", true),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
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
		T.TAX_RATE.getFld().setDefaultValue(BigDecimal.ZERO);
		T.CREDIT_LIMIT.getFld().setDefaultValue(BigDecimal.ZERO);
		T.CREDIT_OTHER.getFld().setDefaultValue(BigDecimal.ZERO);
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
	//实例变量定义-----------------------------------------
	private String _settle; // 结算方式  STR(100)<null>
	private Byte _settleMonth; // 是否月结 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
	private Integer _settleNextDay; // 次月天数  INT
	private Byte _creditLevel; // 信用等级 <OCreditLevel>  BYTE
	// LO:1,低
	// MI:2,中
	// HI:3,高
	private BigDecimal _creditLimit; // 信用额度  DEC(16,2)
	private BigDecimal _creditOther; // 押抵金额  DEC(16,2)
	private Byte _protocolType; // 协议类型 <OProtocoType>  BYTE
	// RATE:1,固定折扣
	// PROT:2,按协议价
	// SAL:3,按普通另售价
	private Byte _discountLevel; // 折扣级别 <ODiscountLevel>  BYTE
	// ON:1,级别一
	// TW:2,级别二
	// TH:3,级别三
	// FO:4,级别四
	// FI:5,级别五
	// SI:6,级别六
	// SE:7,级别七
	// EI:8,级别八
	// NI:9,级别九
	// TE:10,级别十
	// EL:11,级别十一
	// TWE:12,级别十二
	private BigDecimal _taxRate; // 税点(%)  DEC(8,4)
	private String _packDemand; // 包装要求  STR(200)<null>
	private Integer _shipMode; // 运输方式 <表主键:LgtShipMode>  INT<null>
	private Byte _shipType; // 费用承担方式 <OShipType>  BYTE
	// MY:1,我方支付
	// OTHER:2,他方支付
	// WAIT:3,待定
	private Date _dateProt; // 协议签订日期  DATE
	private Date _dateStart; // 启用日期  DATE
	private Date _dateEnd; // 到期日期  DATE<null>

	@Override
	public CmbSalProt init() {
		super.init();
		_settle = null; // 结算方式  STR(100)
		_settleMonth = OYn.DEFAULT.getLine().getKey(); // 是否月结 <OYn>  BYTE
		_settleNextDay = 0; // 次月天数  INT
		_creditLevel = OCreditLevel.DEFAULT.getLine().getKey(); // 信用等级 <OCreditLevel>  BYTE
		_creditLimit = new BigDecimal(0); // 信用额度  DEC(16,2)
		_creditOther = new BigDecimal(0); // 押抵金额  DEC(16,2)
		_protocolType = OProtocoType.DEFAULT.getLine().getKey(); // 协议类型 <OProtocoType>  BYTE
		_discountLevel = ODiscountLevel.DEFAULT.getLine().getKey(); // 折扣级别 <ODiscountLevel>  BYTE
		_taxRate = new BigDecimal(0); // 税点(%)  DEC(8,4)
		_packDemand = null; // 包装要求  STR(200)
		_shipMode = null; // 运输方式 <表主键:LgtShipMode>  INT
		_shipType = OShipType.DEFAULT.getLine().getKey(); // 费用承担方式 <OShipType>  BYTE
		_dateProt = null; // 协议签订日期  DATE
		_dateStart = null; // 启用日期  DATE
		_dateEnd = null; // 到期日期  DATE
		return this;
	}

	//方法----------------------------------------------
	public String getSettle() {
		return _settle;
	}

	public void setSettle(String settle) {
		_settle = settle;
	}

	public Byte getSettleMonth() {
		return _settleMonth;
	}

	public void setSettleMonth(Byte settleMonth) {
		_settleMonth = settleMonth;
	}

	public Boolean gtSettleMonth() {
		return byteToBoolean(_settleMonth);
	}

	public void stSettleMonth(Boolean settleMonth) {
		_settleMonth = booleanToByte(settleMonth);
	}

	public Integer getSettleNextDay() {
		return _settleNextDay;
	}

	public void setSettleNextDay(Integer settleNextDay) {
		_settleNextDay = settleNextDay;
	}

	public Byte getCreditLevel() {
		return _creditLevel;
	}

	public void setCreditLevel(Byte creditLevel) {
		_creditLevel = creditLevel;
	}

	public OCreditLevel gtCreditLevel() {
		return (OCreditLevel) (OCreditLevel.LO.getLine().get(_creditLevel));
	}

	public void stCreditLevel(OCreditLevel creditLevel) {
		_creditLevel = creditLevel.getLine().getKey();
	}

	public BigDecimal getCreditLimit() {
		return _creditLimit;
	}

	public void setCreditLimit(BigDecimal creditLimit) {
		_creditLimit = creditLimit;
	}

	public BigDecimal getCreditOther() {
		return _creditOther;
	}

	public void setCreditOther(BigDecimal creditOther) {
		_creditOther = creditOther;
	}

	public Byte getProtocolType() {
		return _protocolType;
	}

	public void setProtocolType(Byte protocolType) {
		_protocolType = protocolType;
	}

	public OProtocoType gtProtocolType() {
		return (OProtocoType) (OProtocoType.RATE.getLine().get(_protocolType));
	}

	public void stProtocolType(OProtocoType protocolType) {
		_protocolType = protocolType.getLine().getKey();
	}

	public Byte getDiscountLevel() {
		return _discountLevel;
	}

	public void setDiscountLevel(Byte discountLevel) {
		_discountLevel = discountLevel;
	}

	public ODiscountLevel gtDiscountLevel() {
		return (ODiscountLevel) (ODiscountLevel.ON.getLine().get(_discountLevel));
	}

	public void stDiscountLevel(ODiscountLevel discountLevel) {
		_discountLevel = discountLevel.getLine().getKey();
	}

	public BigDecimal getTaxRate() {
		return _taxRate;
	}

	public void setTaxRate(BigDecimal taxRate) {
		_taxRate = taxRate;
	}

	public String getPackDemand() {
		return _packDemand;
	}

	public void setPackDemand(String packDemand) {
		_packDemand = packDemand;
	}

	public Integer getShipMode() {
		return _shipMode;
	}

	public void setShipMode(Integer shipMode) {
		_shipMode = shipMode;
	}

	public LgtShipMode gtShipMode() {
		if (getShipMode() == null)
			return null;
		return (LgtShipMode) get(LgtShipMode.class, getShipMode());
	}

	public void stShipMode(LgtShipMode shipMode) {
		if (shipMode == null)
			setShipMode(null);
		else
			setShipMode(shipMode.getPkey());
	}

	public Byte getShipType() {
		return _shipType;
	}

	public void setShipType(Byte shipType) {
		_shipType = shipType;
	}

	public OShipType gtShipType() {
		return (OShipType) (OShipType.WAIT.getLine().get(_shipType));
	}

	public void stShipType(OShipType shipType) {
		_shipType = shipType.getLine().getKey();
	}

	public Date getDateProt() {
		return _dateProt;
	}

	public void setDateProt(Date dateProt) {
		_dateProt = dateProt;
	}

	public Date getDateStart() {
		return _dateStart;
	}

	public void setDateStart(Date dateStart) {
		_dateStart = dateStart;
	}

	public Date getDateEnd() {
		return _dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		_dateEnd = dateEnd;
	}

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
