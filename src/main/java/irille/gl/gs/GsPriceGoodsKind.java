package irille.gl.gs;

import irille.core.sys.CmbRange;
import irille.core.sys.SysCell;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.ORangeType;
import irille.gl.gs.Gs.OPriceOrig;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * 基础价格分类
 * 用于维护基础价格信息时，选择一个货物与成本价后，根据所属的价格分类的各利润率自动计算价格
 * 所选价格名称对象中定义了几种价格，则对应的利润率也必须填写、其它可以为空
 * 
 * 文档中的代码删除了
 * 加入了货物类别、价格名称两个外键
 * @author whx
 * @version 创建时间：2014年11月28日 上午11:40:51
 */
public class GsPriceGoodsKind extends BeanInt<GsPriceGoodsKind> implements IExtName {
	public static final Log LOG = new Log(GsPriceGoodsKind.class);
	public static final Tb TB = new Tb(GsPriceGoodsKind.class, "基础价格分类").setAutoIncrement().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__100),
		NAME(SYS.NAME__40), //默认为货物分类的名称或代码
		PRICE(GsPrice.fldOutKey()),
		PRICE_ORIG(TB.crt(Gs.OPriceOrig.COST_UPD)),
		ENABLED(SYS.ENABLED),
		RATE(TB.crtDime(SYS.RATE_NULL, new int[] { 1,2,3,4,5,6,7,8,9,10,11,12}, "利润率1(%)", "利润率2(%)", "利润率3(%)", "利润率4(%)", "利润率5(%)"
				, "利润率6(%)", "利润率7(%)", "利润率8(%)", "利润率9(%)", "利润率10(%)", "利润率11(%)", "利润率12(%)").setNull()), 
		CMB_SYS_RANGE(CmbRange.fldFlds()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		RATE1(TB.get("rate1")),	//利润率1(%)
		RATE2(TB.get("rate2")),	//利润率2(%)
		RATE3(TB.get("rate3")),	//利润率3(%)
		RATE4(TB.get("rate4")),	//利润率4(%)
		RATE5(TB.get("rate5")),	//利润率5(%)
		RATE6(TB.get("rate6")),	//利润率6(%)
		RATE7(TB.get("rate7")),	//利润率7(%)
		RATE8(TB.get("rate8")),	//利润率8(%)
		RATE9(TB.get("rate9")),	//利润率9(%)
		RATE10(TB.get("rate10")),	//利润率10(%)
		RATE11(TB.get("rate11")),	//利润率11(%)
		RATE12(TB.get("rate12")),	//利润率12(%)
		RANGE(TB.get("range")),	//可用对象
		RANGE_TYPE(TB.get("rangeType")),	//可视范围
		RANGE_PKEY(TB.get("rangePkey")),	//可用对象主键值
		CELL(TB.get("cell")),	//管理核算单元
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_CODE_PRICE = TB.addIndex("codePrice", true, CODE, PRICE);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}
	public static Fld fldOutKey(String code, String name) {
		return TB.crtOutKey(TB,code,name);
	}
	
	@Override
	public String getExtName() {
	  return getName();
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(100)
  private String _name;	// 名称  STR(40)
  private Integer _price;	// 定价名称 <表主键:GsPrice>  INT
  private Byte _priceOrig;	// 定价基数来源 <OPriceOrig>  BYTE
	// COST:1,成本价自动产生（不可维护）
	// COST_UPD:2,成本价自动产生（可维护）
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private BigDecimal _rate1;	// 利润率1(%)  DEC(8,4)<null>
  private BigDecimal _rate2;	// 利润率2(%)  DEC(8,4)<null>
  private BigDecimal _rate3;	// 利润率3(%)  DEC(8,4)<null>
  private BigDecimal _rate4;	// 利润率4(%)  DEC(8,4)<null>
  private BigDecimal _rate5;	// 利润率5(%)  DEC(8,4)<null>
  private BigDecimal _rate6;	// 利润率6(%)  DEC(8,4)<null>
  private BigDecimal _rate7;	// 利润率7(%)  DEC(8,4)<null>
  private BigDecimal _rate8;	// 利润率8(%)  DEC(8,4)<null>
  private BigDecimal _rate9;	// 利润率9(%)  DEC(8,4)<null>
  private BigDecimal _rate10;	// 利润率10(%)  DEC(8,4)<null>
  private BigDecimal _rate11;	// 利润率11(%)  DEC(8,4)<null>
  private BigDecimal _rate12;	// 利润率12(%)  DEC(8,4)<null>
  private Byte _rangeType;	// 可视范围 <ORangeType>  BYTE
	// GRP:1,集团级
	// ORG_ALL:11,上下级机构
	// ORG_DOWN:12,及下级机构
	// ORG_UP:13,及上级机构
	// ORG:14,本机构
	// CELL_ALL:21,上下级核算单元
	// CELL_DOWN:22,及下级核算单元
	// CELL_UP:23,及上级核算单元
	// CELL:24,本核算单元
  private Integer _rangePkey;	// 可用对象主键值  INT<null>
  private Integer _cell;	// 管理核算单元 <表主键:SysCell>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPriceGoodsKind init(){
		super.init();
    _code=null;	// 代码  STR(100)
    _name=null;	// 名称  STR(40)
    _price=null;	// 定价名称 <表主键:GsPrice>  INT
    _priceOrig=OPriceOrig.DEFAULT.getLine().getKey();	// 定价基数来源 <OPriceOrig>  BYTE
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rate1=null;	// 利润率1(%)  DEC(8,4)
    _rate2=null;	// 利润率2(%)  DEC(8,4)
    _rate3=null;	// 利润率3(%)  DEC(8,4)
    _rate4=null;	// 利润率4(%)  DEC(8,4)
    _rate5=null;	// 利润率5(%)  DEC(8,4)
    _rate6=null;	// 利润率6(%)  DEC(8,4)
    _rate7=null;	// 利润率7(%)  DEC(8,4)
    _rate8=null;	// 利润率8(%)  DEC(8,4)
    _rate9=null;	// 利润率9(%)  DEC(8,4)
    _rate10=null;	// 利润率10(%)  DEC(8,4)
    _rate11=null;	// 利润率11(%)  DEC(8,4)
    _rate12=null;	// 利润率12(%)  DEC(8,4)
    _rangeType=ORangeType.DEFAULT.getLine().getKey();	// 可视范围 <ORangeType>  BYTE
    _rangePkey=null;	// 可用对象主键值  INT
    _cell=null;	// 管理核算单元 <表主键:SysCell>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsPriceGoodsKind loadUniqueCodePrice(boolean lockFlag,String code,Integer price) {
    return (GsPriceGoodsKind)loadUnique(T.IDX_CODE_PRICE,lockFlag,code,price);
  }
  public static GsPriceGoodsKind chkUniqueCodePrice(boolean lockFlag,String code,Integer price) {
    return (GsPriceGoodsKind)chkUnique(T.IDX_CODE_PRICE,lockFlag,code,price);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
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
  public Integer getPrice(){
    return _price;
  }
  public void setPrice(Integer price){
    _price=price;
  }
  public GsPrice gtPrice(){
    if(getPrice()==null)
      return null;
    return (GsPrice)get(GsPrice.class,getPrice());
  }
  public void stPrice(GsPrice price){
    if(price==null)
      setPrice(null);
    else
      setPrice(price.getPkey());
  }
  public Byte getPriceOrig(){
    return _priceOrig;
  }
  public void setPriceOrig(Byte priceOrig){
    _priceOrig=priceOrig;
  }
  public OPriceOrig gtPriceOrig(){
    return (OPriceOrig)(OPriceOrig.COST_UPD.getLine().get(_priceOrig));
  }
  public void stPriceOrig(OPriceOrig priceOrig){
    _priceOrig=priceOrig.getLine().getKey();
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
  //数组对象: BigDecimal
  public BigDecimal gtRate(int i){
    switch(i) {
    case 1:
    	return getRate1();
    case 2:
    	return getRate2();
    case 3:
    	return getRate3();
    case 4:
    	return getRate4();
    case 5:
    	return getRate5();
    case 6:
    	return getRate6();
    case 7:
    	return getRate7();
    case 8:
    	return getRate8();
    case 9:
    	return getRate9();
    case 10:
    	return getRate10();
    case 11:
    	return getRate11();
    case 12:
    	return getRate12();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stRate( int i, BigDecimal rate){
    switch(i) {
    case 1:
    	setRate1(rate);
    	return;
    case 2:
    	setRate2(rate);
    	return;
    case 3:
    	setRate3(rate);
    	return;
    case 4:
    	setRate4(rate);
    	return;
    case 5:
    	setRate5(rate);
    	return;
    case 6:
    	setRate6(rate);
    	return;
    case 7:
    	setRate7(rate);
    	return;
    case 8:
    	setRate8(rate);
    	return;
    case 9:
    	setRate9(rate);
    	return;
    case 10:
    	setRate10(rate);
    	return;
    case 11:
    	setRate11(rate);
    	return;
    case 12:
    	setRate12(rate);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public BigDecimal getRate1(){
    return _rate1;
  }
  public void setRate1(BigDecimal rate1){
    _rate1=rate1;
  }
  public BigDecimal getRate2(){
    return _rate2;
  }
  public void setRate2(BigDecimal rate2){
    _rate2=rate2;
  }
  public BigDecimal getRate3(){
    return _rate3;
  }
  public void setRate3(BigDecimal rate3){
    _rate3=rate3;
  }
  public BigDecimal getRate4(){
    return _rate4;
  }
  public void setRate4(BigDecimal rate4){
    _rate4=rate4;
  }
  public BigDecimal getRate5(){
    return _rate5;
  }
  public void setRate5(BigDecimal rate5){
    _rate5=rate5;
  }
  public BigDecimal getRate6(){
    return _rate6;
  }
  public void setRate6(BigDecimal rate6){
    _rate6=rate6;
  }
  public BigDecimal getRate7(){
    return _rate7;
  }
  public void setRate7(BigDecimal rate7){
    _rate7=rate7;
  }
  public BigDecimal getRate8(){
    return _rate8;
  }
  public void setRate8(BigDecimal rate8){
    _rate8=rate8;
  }
  public BigDecimal getRate9(){
    return _rate9;
  }
  public void setRate9(BigDecimal rate9){
    _rate9=rate9;
  }
  public BigDecimal getRate10(){
    return _rate10;
  }
  public void setRate10(BigDecimal rate10){
    _rate10=rate10;
  }
  public BigDecimal getRate11(){
    return _rate11;
  }
  public void setRate11(BigDecimal rate11){
    _rate11=rate11;
  }
  public BigDecimal getRate12(){
    return _rate12;
  }
  public void setRate12(BigDecimal rate12){
    _rate12=rate12;
  }
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtRange(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtRangeType();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_rangePkey);
  }
  public Byte getRangeType(){
    return _rangeType;
  }
  public void setRangeType(Byte rangeType){
    _rangeType=rangeType;
  }
  public ORangeType gtRangeType(){
    return (ORangeType)(ORangeType.GRP.getLine().get(_rangeType));
  }
  public void stRangeType(ORangeType rangeType){
    _rangeType=rangeType.getLine().getKey();
  }
  public Integer getRangePkey(){
    return _rangePkey;
  }
  public void setRangePkey(Integer rangePkey){
    _rangePkey=rangePkey;
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
