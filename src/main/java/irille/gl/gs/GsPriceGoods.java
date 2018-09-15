package irille.gl.gs;

import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * 基础价格信息
 * 本表的管理权限由GsPriceGoodsKind基础价格分类决定
 * 
 * list--列头就显示默认的价格1-12，但主表信息中显示对应的价格名称
 * form--根据选择的基本价格分类，动态变更价格1-12的LABEL名称
 *         --货物需要根据分类过滤
 *         --选择好分类、货物、定价基数后，自动计算12种价格
 *
 * @author whx
 * @version 创建时间：2014年11月28日 上午11:40:51
 */
public class GsPriceGoods extends BeanInt<GsPriceGoods> implements IExtName {
	public static final Log LOG = new Log(GsPriceGoods.class);
	public static final Tb TB = new Tb(GsPriceGoods.class, "基础价格信息").setAutoIncrement().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		GOODS(GsGoods.fldOutKey()),
		PRICE_KIND(GsPriceGoodsKind.fldOutKey()),
		PRICE_NAME(GsPrice.fldOutKey()),
		PRICE_COST(SYS.PRICE, "定价基数"),
		PRICE(TB.crtDime(SYS.PRICE_NULL, new int[] { 1,2,3,4,5,6,7,8,9,10,11,12}, "价格1", "价格2", "价格3", "价格4", "价格5"
				, "价格6", "价格7", "价格8", "价格9", "价格10", "价格11", "价格12")), 
		ENABLED(SYS.ENABLED),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PRICE1(TB.get("price1")),	//价格1
		PRICE2(TB.get("price2")),	//价格2
		PRICE3(TB.get("price3")),	//价格3
		PRICE4(TB.get("price4")),	//价格4
		PRICE5(TB.get("price5")),	//价格5
		PRICE6(TB.get("price6")),	//价格6
		PRICE7(TB.get("price7")),	//价格7
		PRICE8(TB.get("price8")),	//价格8
		PRICE9(TB.get("price9")),	//价格9
		PRICE10(TB.get("price10")),	//价格10
		PRICE11(TB.get("price11")),	//价格11
		PRICE12(TB.get("price12")),	//价格12
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_GOODS_PRICE = TB.addIndex("goodsPrice", true, GOODS, PRICE_NAME);
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
	@Override
	public String getExtName() {
	  return gtPriceKind().getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}
	public static Fld fldOutKey(String code, String name) {
		Fld fld = TB.crtOutKey(TB,code,name);
		fld.setType(null);
		return fld;
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _priceKind;	// 基础价格分类 <表主键:GsPriceGoodsKind>  INT
  private Integer _priceName;	// 定价名称 <表主键:GsPrice>  INT
  private BigDecimal _priceCost;	// 定价基数  DEC(14,4)
  private BigDecimal _price1;	// 价格1  DEC(14,4)<null>
  private BigDecimal _price2;	// 价格2  DEC(14,4)<null>
  private BigDecimal _price3;	// 价格3  DEC(14,4)<null>
  private BigDecimal _price4;	// 价格4  DEC(14,4)<null>
  private BigDecimal _price5;	// 价格5  DEC(14,4)<null>
  private BigDecimal _price6;	// 价格6  DEC(14,4)<null>
  private BigDecimal _price7;	// 价格7  DEC(14,4)<null>
  private BigDecimal _price8;	// 价格8  DEC(14,4)<null>
  private BigDecimal _price9;	// 价格9  DEC(14,4)<null>
  private BigDecimal _price10;	// 价格10  DEC(14,4)<null>
  private BigDecimal _price11;	// 价格11  DEC(14,4)<null>
  private BigDecimal _price12;	// 价格12  DEC(14,4)<null>
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPriceGoods init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _priceKind=null;	// 基础价格分类 <表主键:GsPriceGoodsKind>  INT
    _priceName=null;	// 定价名称 <表主键:GsPrice>  INT
    _priceCost=ZERO;	// 定价基数  DEC(14,4)
    _price1=null;	// 价格1  DEC(14,4)
    _price2=null;	// 价格2  DEC(14,4)
    _price3=null;	// 价格3  DEC(14,4)
    _price4=null;	// 价格4  DEC(14,4)
    _price5=null;	// 价格5  DEC(14,4)
    _price6=null;	// 价格6  DEC(14,4)
    _price7=null;	// 价格7  DEC(14,4)
    _price8=null;	// 价格8  DEC(14,4)
    _price9=null;	// 价格9  DEC(14,4)
    _price10=null;	// 价格10  DEC(14,4)
    _price11=null;	// 价格11  DEC(14,4)
    _price12=null;	// 价格12  DEC(14,4)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsPriceGoods loadUniqueGoodsPrice(boolean lockFlag,Integer goods,Integer priceName) {
    return (GsPriceGoods)loadUnique(T.IDX_GOODS_PRICE,lockFlag,goods,priceName);
  }
  public static GsPriceGoods chkUniqueGoodsPrice(boolean lockFlag,Integer goods,Integer priceName) {
    return (GsPriceGoods)chkUnique(T.IDX_GOODS_PRICE,lockFlag,goods,priceName);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getGoods(){
    return _goods;
  }
  public void setGoods(Integer goods){
    _goods=goods;
  }
  public GsGoods gtGoods(){
    if(getGoods()==null)
      return null;
    return (GsGoods)get(GsGoods.class,getGoods());
  }
  public void stGoods(GsGoods goods){
    if(goods==null)
      setGoods(null);
    else
      setGoods(goods.getPkey());
  }
  public Integer getPriceKind(){
    return _priceKind;
  }
  public void setPriceKind(Integer priceKind){
    _priceKind=priceKind;
  }
  public GsPriceGoodsKind gtPriceKind(){
    if(getPriceKind()==null)
      return null;
    return (GsPriceGoodsKind)get(GsPriceGoodsKind.class,getPriceKind());
  }
  public void stPriceKind(GsPriceGoodsKind priceKind){
    if(priceKind==null)
      setPriceKind(null);
    else
      setPriceKind(priceKind.getPkey());
  }
  public Integer getPriceName(){
    return _priceName;
  }
  public void setPriceName(Integer priceName){
    _priceName=priceName;
  }
  public GsPrice gtPriceName(){
    if(getPriceName()==null)
      return null;
    return (GsPrice)get(GsPrice.class,getPriceName());
  }
  public void stPriceName(GsPrice priceName){
    if(priceName==null)
      setPriceName(null);
    else
      setPriceName(priceName.getPkey());
  }
  public BigDecimal getPriceCost(){
    return _priceCost;
  }
  public void setPriceCost(BigDecimal priceCost){
    _priceCost=priceCost;
  }
  //数组对象: BigDecimal
  public BigDecimal gtPrice(int i){
    switch(i) {
    case 1:
    	return getPrice1();
    case 2:
    	return getPrice2();
    case 3:
    	return getPrice3();
    case 4:
    	return getPrice4();
    case 5:
    	return getPrice5();
    case 6:
    	return getPrice6();
    case 7:
    	return getPrice7();
    case 8:
    	return getPrice8();
    case 9:
    	return getPrice9();
    case 10:
    	return getPrice10();
    case 11:
    	return getPrice11();
    case 12:
    	return getPrice12();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stPrice( int i, BigDecimal price){
    switch(i) {
    case 1:
    	setPrice1(price);
    	return;
    case 2:
    	setPrice2(price);
    	return;
    case 3:
    	setPrice3(price);
    	return;
    case 4:
    	setPrice4(price);
    	return;
    case 5:
    	setPrice5(price);
    	return;
    case 6:
    	setPrice6(price);
    	return;
    case 7:
    	setPrice7(price);
    	return;
    case 8:
    	setPrice8(price);
    	return;
    case 9:
    	setPrice9(price);
    	return;
    case 10:
    	setPrice10(price);
    	return;
    case 11:
    	setPrice11(price);
    	return;
    case 12:
    	setPrice12(price);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public BigDecimal getPrice1(){
    return _price1;
  }
  public void setPrice1(BigDecimal price1){
    _price1=price1;
  }
  public BigDecimal getPrice2(){
    return _price2;
  }
  public void setPrice2(BigDecimal price2){
    _price2=price2;
  }
  public BigDecimal getPrice3(){
    return _price3;
  }
  public void setPrice3(BigDecimal price3){
    _price3=price3;
  }
  public BigDecimal getPrice4(){
    return _price4;
  }
  public void setPrice4(BigDecimal price4){
    _price4=price4;
  }
  public BigDecimal getPrice5(){
    return _price5;
  }
  public void setPrice5(BigDecimal price5){
    _price5=price5;
  }
  public BigDecimal getPrice6(){
    return _price6;
  }
  public void setPrice6(BigDecimal price6){
    _price6=price6;
  }
  public BigDecimal getPrice7(){
    return _price7;
  }
  public void setPrice7(BigDecimal price7){
    _price7=price7;
  }
  public BigDecimal getPrice8(){
    return _price8;
  }
  public void setPrice8(BigDecimal price8){
    _price8=price8;
  }
  public BigDecimal getPrice9(){
    return _price9;
  }
  public void setPrice9(BigDecimal price9){
    _price9=price9;
  }
  public BigDecimal getPrice10(){
    return _price10;
  }
  public void setPrice10(BigDecimal price10){
    _price10=price10;
  }
  public BigDecimal getPrice11(){
    return _price11;
  }
  public void setPrice11(BigDecimal price11){
    _price11=price11;
  }
  public BigDecimal getPrice12(){
    return _price12;
  }
  public void setPrice12(BigDecimal price12){
    _price12=price12;
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
