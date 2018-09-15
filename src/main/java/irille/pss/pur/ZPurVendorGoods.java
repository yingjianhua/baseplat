package irille.pss.pur;

import irille.core.sys.SysOrg;
import irille.core.sys.SysSupplier;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 协议单 - 供应商协议信息
 * 商品定价协议单，明细 -- 供应商品定价表
 * 供应商协议信息表
 * @author whx
 * @version 创建时间：2014年8月20日 下午3:43:27
 */
public class ZPurVendorGoods extends BeanLong<ZPurVendorGoods> {
	public static final Tb TB = new Tb(ZPurVendorGoods.class, "供应商品对照表").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		SUPPLIER(SYS.SUPPLIER),
		NAME(SYS.NAME__100, "供应商名称"),
		GOODS(GsGoods.fldOutKey()),
		UOM(GsUom.fldOutKey()),
		VENDOR_GOODS_NAME(SYS.NAME__100_NULL, "他方品名"),
		VENDOR_NUM(SYS.CODE__40, "他方代号", true),
		VENDOR_SPEC(SYS.CODE__100, "他方规格", true),
		LATEST_PRICE(SYS.PRICE, "上次成交价", true),
		LATEST_DATE(SYS.DATE__NULL, "上次成交日期", true),
		LATEST_SPE_PRICE(SYS.PRICE, "上次成交特价", true),
		LATEST_SPE_DATE(SYS.DATE__NULL, "上次特价成交日期", true),
		ORG(SYS.ORG),
		UPDATED_TIME(SYS.UPDATED_DATE_TIME),
		REM(SYS.REM__200_NULL),
		//		(FLDS.),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		// 索引
		public static final Index IDX_SUPPLIER_GOODS = TB.addIndex("supplierGoods", true, SUPPLIER, GOODS);
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

  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _supplier;	// 供应商 <表主键:SysSupplier>  INT
  private String _name;	// 供应商名称  STR(100)
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private String _vendorGoodsName;	// 他方品名  STR(100)<null>
  private String _vendorNum;	// 他方代号  STR(40)<null>
  private String _vendorSpec;	// 他方规格  STR(100)<null>
  private BigDecimal _latestPrice;	// 上次成交价  DEC(14,4)<null>
  private Date _latestDate;	// 上次成交日期  DATE<null>
  private BigDecimal _latestSpePrice;	// 上次成交特价  DEC(14,4)<null>
  private Date _latestSpeDate;	// 上次特价成交日期  DATE<null>
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private String _rem;	// 备注  STR(200)<null>

	@Override
  public ZPurVendorGoods init(){
		super.init();
    _supplier=null;	// 供应商 <表主键:SysSupplier>  INT
    _name=null;	// 供应商名称  STR(100)
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _vendorGoodsName=null;	// 他方品名  STR(100)
    _vendorNum=null;	// 他方代号  STR(40)
    _vendorSpec=null;	// 他方规格  STR(100)
    _latestPrice=null;	// 上次成交价  DEC(14,4)
    _latestDate=null;	// 上次成交日期  DATE
    _latestSpePrice=null;	// 上次成交特价  DEC(14,4)
    _latestSpeDate=null;	// 上次特价成交日期  DATE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rem=null;	// 备注  STR(200)
    return this;
  }

  //方法----------------------------------------------
  public static ZPurVendorGoods loadUniqueSupplierGoods(boolean lockFlag,Integer supplier,Integer goods) {
    return (ZPurVendorGoods)loadUnique(T.IDX_SUPPLIER_GOODS,lockFlag,supplier,goods);
  }
  public static ZPurVendorGoods chkUniqueSupplierGoods(boolean lockFlag,Integer supplier,Integer goods) {
    return (ZPurVendorGoods)chkUnique(T.IDX_SUPPLIER_GOODS,lockFlag,supplier,goods);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
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
  public Integer getUom(){
    return _uom;
  }
  public void setUom(Integer uom){
    _uom=uom;
  }
  public GsUom gtUom(){
    if(getUom()==null)
      return null;
    return (GsUom)get(GsUom.class,getUom());
  }
  public void stUom(GsUom uom){
    if(uom==null)
      setUom(null);
    else
      setUom(uom.getPkey());
  }
  public String getVendorGoodsName(){
    return _vendorGoodsName;
  }
  public void setVendorGoodsName(String vendorGoodsName){
    _vendorGoodsName=vendorGoodsName;
  }
  public String getVendorNum(){
    return _vendorNum;
  }
  public void setVendorNum(String vendorNum){
    _vendorNum=vendorNum;
  }
  public String getVendorSpec(){
    return _vendorSpec;
  }
  public void setVendorSpec(String vendorSpec){
    _vendorSpec=vendorSpec;
  }
  public BigDecimal getLatestPrice(){
    return _latestPrice;
  }
  public void setLatestPrice(BigDecimal latestPrice){
    _latestPrice=latestPrice;
  }
  public Date getLatestDate(){
    return _latestDate;
  }
  public void setLatestDate(Date latestDate){
    _latestDate=latestDate;
  }
  public BigDecimal getLatestSpePrice(){
    return _latestSpePrice;
  }
  public void setLatestSpePrice(BigDecimal latestSpePrice){
    _latestSpePrice=latestSpePrice;
  }
  public Date getLatestSpeDate(){
    return _latestSpeDate;
  }
  public void setLatestSpeDate(Date latestSpeDate){
    _latestSpeDate=latestSpeDate;
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

}
