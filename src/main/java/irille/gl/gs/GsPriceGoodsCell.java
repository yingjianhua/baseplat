package irille.gl.gs;

import irille.core.sys.CmbFlag;
import irille.core.sys.SysCell;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * 核算单元价格信息
 * 与存货账GsJlStock一一对应
 * 
 * list--列头就显示默认的价格1-12，但主表信息中显示对应的价格名称
 * form--根据选择的基本价格信息，动态变更价格1-12的LABEL名称
 * --货物需要根据分类过滤
 * --选择好分类、货物、定价基数后，自动计算12种价格
 *
 * @author whx
 * @version 创建时间：2014年11月28日 上午11:40:51
 */
public class GsPriceGoodsCell extends BeanInt<GsPriceGoodsCell> {
	public static final Log LOG = new Log(GsPriceGoodsCell.class);
	public static final Tb TB = new Tb(GsPriceGoodsCell.class, "核算单元价格信息").setAutoIncrement().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CELL(SYS.CELL),
		PRICE_GOODS(GsPriceGoods.fldOutKey()),
		PRICE_NAME(GsPrice.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		PRICE_COST(SYS.PRICE_NULL, "定价基数"), //定价基数：如果基础表中定义为可维护，则可以维护
		PRICE(TB.crtDime(SYS.PRICE_NULL, new int[] { 1,2,3,4,5,6,7,8,9,10,11,12}, "价格1", "价格2", "价格3", "价格4", "价格5"
				, "价格6", "价格7", "价格8", "价格9", "价格10", "价格11", "价格12")), 
		CMB_FLAG(CmbFlag.fldFlds()), //各类标志：可修改
		SYNC_FLAG(SYS.YN, "同步价格"),
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
		FLAG_SAL(TB.get("flagSal")),	//零售标志
		FLAG_PF(TB.get("flagPf")),	//批发标志
		FLAG_MVOUT(TB.get("flagMvout")),	//调出标志
		FLAG_MVIN(TB.get("flagMvin")),	//调入标志
		FLAG_PUR(TB.get("flagPur")),	//采购标志
		FLAG_FINI(TB.get("flagFini")),	//产成品标志
		FLAG_HALF(TB.get("flagHalf")),	//半成品标志
		FLAG_PRIV(TB.get("flagPriv")),	//自用品标志
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CELL_GOODS = TB.addIndex("cellGoods", true, CELL, GOODS, PRICE_NAME);
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
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _priceGoods;	// 基础价格信息 <表主键:GsPriceGoods>  INT
  private Integer _priceName;	// 定价名称 <表主键:GsPrice>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private BigDecimal _priceCost;	// 定价基数  DEC(14,4)<null>
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
  private Byte _flagSal;	// 零售标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPf;	// 批发标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagMvout;	// 调出标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagMvin;	// 调入标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPur;	// 采购标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagFini;	// 产成品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagHalf;	// 半成品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _flagPriv;	// 自用品标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _syncFlag;	// 同步价格 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPriceGoodsCell init(){
		super.init();
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _priceGoods=null;	// 基础价格信息 <表主键:GsPriceGoods>  INT
    _priceName=null;	// 定价名称 <表主键:GsPrice>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _priceCost=null;	// 定价基数  DEC(14,4)
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
    _flagSal=OYn.DEFAULT.getLine().getKey();	// 零售标志 <OYn>  BYTE
    _flagPf=OYn.DEFAULT.getLine().getKey();	// 批发标志 <OYn>  BYTE
    _flagMvout=OYn.DEFAULT.getLine().getKey();	// 调出标志 <OYn>  BYTE
    _flagMvin=OYn.DEFAULT.getLine().getKey();	// 调入标志 <OYn>  BYTE
    _flagPur=OYn.DEFAULT.getLine().getKey();	// 采购标志 <OYn>  BYTE
    _flagFini=OYn.DEFAULT.getLine().getKey();	// 产成品标志 <OYn>  BYTE
    _flagHalf=OYn.DEFAULT.getLine().getKey();	// 半成品标志 <OYn>  BYTE
    _flagPriv=OYn.DEFAULT.getLine().getKey();	// 自用品标志 <OYn>  BYTE
    _syncFlag=OYn.DEFAULT.getLine().getKey();	// 同步价格 <OYn>  BYTE
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsPriceGoodsCell loadUniqueCellGoods(boolean lockFlag,Integer cell,Integer goods,Integer priceName) {
    return (GsPriceGoodsCell)loadUnique(T.IDX_CELL_GOODS,lockFlag,cell,goods,priceName);
  }
  public static GsPriceGoodsCell chkUniqueCellGoods(boolean lockFlag,Integer cell,Integer goods,Integer priceName) {
    return (GsPriceGoodsCell)chkUnique(T.IDX_CELL_GOODS,lockFlag,cell,goods,priceName);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
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
  public Integer getPriceGoods(){
    return _priceGoods;
  }
  public void setPriceGoods(Integer priceGoods){
    _priceGoods=priceGoods;
  }
  public GsPriceGoods gtPriceGoods(){
    if(getPriceGoods()==null)
      return null;
    return (GsPriceGoods)get(GsPriceGoods.class,getPriceGoods());
  }
  public void stPriceGoods(GsPriceGoods priceGoods){
    if(priceGoods==null)
      setPriceGoods(null);
    else
      setPriceGoods(priceGoods.getPkey());
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
  public Byte getFlagSal(){
    return _flagSal;
  }
  public void setFlagSal(Byte flagSal){
    _flagSal=flagSal;
  }
  public Boolean gtFlagSal(){
    return byteToBoolean(_flagSal);
  }
  public void stFlagSal(Boolean flagSal){
    _flagSal=booleanToByte(flagSal);
  }
  public Byte getFlagPf(){
    return _flagPf;
  }
  public void setFlagPf(Byte flagPf){
    _flagPf=flagPf;
  }
  public Boolean gtFlagPf(){
    return byteToBoolean(_flagPf);
  }
  public void stFlagPf(Boolean flagPf){
    _flagPf=booleanToByte(flagPf);
  }
  public Byte getFlagMvout(){
    return _flagMvout;
  }
  public void setFlagMvout(Byte flagMvout){
    _flagMvout=flagMvout;
  }
  public Boolean gtFlagMvout(){
    return byteToBoolean(_flagMvout);
  }
  public void stFlagMvout(Boolean flagMvout){
    _flagMvout=booleanToByte(flagMvout);
  }
  public Byte getFlagMvin(){
    return _flagMvin;
  }
  public void setFlagMvin(Byte flagMvin){
    _flagMvin=flagMvin;
  }
  public Boolean gtFlagMvin(){
    return byteToBoolean(_flagMvin);
  }
  public void stFlagMvin(Boolean flagMvin){
    _flagMvin=booleanToByte(flagMvin);
  }
  public Byte getFlagPur(){
    return _flagPur;
  }
  public void setFlagPur(Byte flagPur){
    _flagPur=flagPur;
  }
  public Boolean gtFlagPur(){
    return byteToBoolean(_flagPur);
  }
  public void stFlagPur(Boolean flagPur){
    _flagPur=booleanToByte(flagPur);
  }
  public Byte getFlagFini(){
    return _flagFini;
  }
  public void setFlagFini(Byte flagFini){
    _flagFini=flagFini;
  }
  public Boolean gtFlagFini(){
    return byteToBoolean(_flagFini);
  }
  public void stFlagFini(Boolean flagFini){
    _flagFini=booleanToByte(flagFini);
  }
  public Byte getFlagHalf(){
    return _flagHalf;
  }
  public void setFlagHalf(Byte flagHalf){
    _flagHalf=flagHalf;
  }
  public Boolean gtFlagHalf(){
    return byteToBoolean(_flagHalf);
  }
  public void stFlagHalf(Boolean flagHalf){
    _flagHalf=booleanToByte(flagHalf);
  }
  public Byte getFlagPriv(){
    return _flagPriv;
  }
  public void setFlagPriv(Byte flagPriv){
    _flagPriv=flagPriv;
  }
  public Boolean gtFlagPriv(){
    return byteToBoolean(_flagPriv);
  }
  public void stFlagPriv(Boolean flagPriv){
    _flagPriv=booleanToByte(flagPriv);
  }
  public Byte getSyncFlag(){
    return _syncFlag;
  }
  public void setSyncFlag(Byte syncFlag){
    _syncFlag=syncFlag;
  }
  public Boolean gtSyncFlag(){
    return byteToBoolean(_syncFlag);
  }
  public void stSyncFlag(Boolean syncFlag){
    _syncFlag=booleanToByte(syncFlag);
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
