package irille.pss.pur;

import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplat;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pss.sal.CmbSalProtGoods;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author whx
 * @version 创建时间：2014年8月26日 下午3:02:34
 */
public class PurProtGoods extends BeanInt<PurProtGoods> {
	public static final Tb TB = new Tb(PurProtGoods.class, "供应商货物协议").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		TEMPLAT(SYS.TEMPLAT),
		SUPPLIER(SYS.SUPPLIER), //供应商
		NAME(SYS.NAME__100, "供应商名称"),
		GOODS(GsGoods.fldOutKey()),
		UOM(GsUom.fldOutKey()),
		CMB_SAL_PROT_GOODS(CmbSalProtGoods.fldFlds()),
		PRICE_LAST(SYS.PRICE, "上次成交价",true),
		DATE_LAST(SYS.DATE,"上次成交时间",true), 
		UPDATED_BY(SYS.UPDATED_BY), //更新员
		UPDATED_TIME(SYS.UPDATED_DATE_TIME), //更新日期
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		VENDOR_GOODS_NAME(TB.get("vendorGoodsName")),	//他方品名
		VENDOR_NUM(TB.get("vendorNum")),	//他方代码
		VENDOR_SPEC(TB.get("vendorSpec")),	//他方规格
		PRICE(TB.get("price")),	//协议价
		DATE_START(TB.get("dateStart")),	//启用日期
		DATE_END(TB.get("dateEnd")),	//到期日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_TEMP_CUST_OBJ = TB.addIndex("tempCustObj", true,TEMPLAT,SUPPLIER,GOODS);
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
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private String _vendorGoodsName;	// 他方品名  STR(100)<null>
  private String _vendorNum;	// 他方代码  STR(40)<null>
  private String _vendorSpec;	// 他方规格  STR(100)<null>
  private BigDecimal _price;	// 协议价  DEC(14,4)
  private Date _dateStart;	// 启用日期  DATE
  private Date _dateEnd;	// 到期日期  DATE<null>
  private BigDecimal _priceLast;	// 上次成交价  DEC(14,4)<null>
  private Date _dateLast;	// 上次成交时间  DATE<null>
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PurProtGoods init(){
		super.init();
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _supplier=null;	// 供应商 <表主键:SysSupplier>  INT
    _name=null;	// 供应商名称  STR(100)
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _vendorGoodsName=null;	// 他方品名  STR(100)
    _vendorNum=null;	// 他方代码  STR(40)
    _vendorSpec=null;	// 他方规格  STR(100)
    _price=ZERO;	// 协议价  DEC(14,4)
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    _priceLast=null;	// 上次成交价  DEC(14,4)
    _dateLast=null;	// 上次成交时间  DATE
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PurProtGoods loadUniqueTempCustObj(boolean lockFlag,Integer templat,Integer supplier,Integer goods) {
    return (PurProtGoods)loadUnique(T.IDX_TEMP_CUST_OBJ,lockFlag,templat,supplier,goods);
  }
  public static PurProtGoods chkUniqueTempCustObj(boolean lockFlag,Integer templat,Integer supplier,Integer goods) {
    return (PurProtGoods)chkUnique(T.IDX_TEMP_CUST_OBJ,lockFlag,templat,supplier,goods);
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
  public BigDecimal getPrice(){
    return _price;
  }
  public void setPrice(BigDecimal price){
    _price=price;
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
  public BigDecimal getPriceLast(){
    return _priceLast;
  }
  public void setPriceLast(BigDecimal priceLast){
    _priceLast=priceLast;
  }
  public Date getDateLast(){
    return _dateLast;
  }
  public void setDateLast(Date dateLast){
    _dateLast=dateLast;
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
