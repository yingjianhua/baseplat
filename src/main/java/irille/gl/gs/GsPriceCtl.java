package irille.gl.gs;

import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * 价格级别对应所选[表-定价名称]中的1-12个价格
 * 
 * 开发注意、前台JS要求
 * model--4个级别定义为参考外键配置
 * list--渲染器同外键
 * form--自定义开发、4个级别更改为普通选项，选项STORE使用同一个，通过AJAX获取
 * --定价名称外键字段更改后，动态重新获取STORE并清空4个级别字段内容
 * action--返回JSON时，4个级别输出内容重构
 * @author whx
 * @version 创建时间：2014年11月28日 上午10:31:50
 */
public class GsPriceCtl extends BeanInt<GsPriceCtl> {
	public static final Log LOG = new Log(GsPriceCtl.class);
	public static final Tb TB = new Tb(GsPriceCtl.class, "定价控制").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(Tb.crtIntPkey()),
		TB_OBJ(Tb.crtLongTbObj("tbObj", "对象").setNull()), //可以存机构或核算单元
		PRICE(GsPrice.fldOutKey()),
		RETAIL_LEVEL(SYS.BYTE, "默认零售价格级别"),
		LOWEST_LEVEL(SYS.BYTE, "默认最低零售价格级别"),
		TRADE_LEVEL(SYS.BYTE, "默认批发价格级别"),
		MV_LEVEL(SYS.BYTE, "默认调拨价格级别"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_OBJ = TB.addIndex("obj", true, TB_OBJ);
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
  private Long _tbObj;	// 对象  LONG<null>
  private Integer _price;	// 定价名称 <表主键:GsPrice>  INT
  private Byte _retailLevel;	// 默认零售价格级别  BYTE
  private Byte _lowestLevel;	// 默认最低零售价格级别  BYTE
  private Byte _tradeLevel;	// 默认批发价格级别  BYTE
  private Byte _mvLevel;	// 默认调拨价格级别  BYTE
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPriceCtl init(){
		super.init();
    _tbObj=null;	// 对象  LONG
    _price=null;	// 定价名称 <表主键:GsPrice>  INT
    _retailLevel=0;	// 默认零售价格级别  BYTE
    _lowestLevel=0;	// 默认最低零售价格级别  BYTE
    _tradeLevel=0;	// 默认批发价格级别  BYTE
    _mvLevel=0;	// 默认调拨价格级别  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsPriceCtl loadUniqueObj(boolean lockFlag,Long tbObj) {
    return (GsPriceCtl)loadUnique(T.IDX_OBJ,lockFlag,tbObj);
  }
  public static GsPriceCtl chkUniqueObj(boolean lockFlag,Long tbObj) {
    return (GsPriceCtl)chkUnique(T.IDX_OBJ,lockFlag,tbObj);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Long getTbObj(){
    return _tbObj;
  }
  public void setTbObj(Long tbObj){
    _tbObj=tbObj;
  }
  //外部主键对象: BeanLong
  public Bean gtTbObj(){
    return (Bean)gtLongTbObj(getTbObj());
  }
  public void stTbObj(Bean tbObj){
      setTbObj(tbObj.gtLongPkey());
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
  public Byte getRetailLevel(){
    return _retailLevel;
  }
  public void setRetailLevel(Byte retailLevel){
    _retailLevel=retailLevel;
  }
  public Byte getLowestLevel(){
    return _lowestLevel;
  }
  public void setLowestLevel(Byte lowestLevel){
    _lowestLevel=lowestLevel;
  }
  public Byte getTradeLevel(){
    return _tradeLevel;
  }
  public void setTradeLevel(Byte tradeLevel){
    _tradeLevel=tradeLevel;
  }
  public Byte getMvLevel(){
    return _mvLevel;
  }
  public void setMvLevel(Byte mvLevel){
    _mvLevel=mvLevel;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
