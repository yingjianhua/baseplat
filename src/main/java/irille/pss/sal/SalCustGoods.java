package irille.pss.sal;

import irille.core.sys.SysCustom;
import irille.core.sys.SysOrg;
import irille.gl.gs.GsGoods;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class SalCustGoods extends BeanInt<SalCustGoods> {
	public static final Tb TB = new Tb(SalCustGoods.class, "客户常用商品").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		ORG(SYS.ORG),
		CUST(SYS.CUST),
		GOODS(GsGoods.fldOutKey()),
		LATEST_PRICE(SYS.PRICE, "上次成交价",true),
		LATEST_DATE(SYS.DATE, "上次成交日期",true),
		LATEST_SPE_PRICE(SYS.PRICE, "上次成交特价",true),
		LATEST_SPE_DATE(SYS.DATE, "上次特价成交日期",true),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CUST_GOODS = TB.addIndex("custGoods", true,ORG,CUST,GOODS);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _cust;	// 客户 <表主键:SysCustom>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private BigDecimal _latestPrice;	// 上次成交价  DEC(14,4)<null>
  private Date _latestDate;	// 上次成交日期  DATE<null>
  private BigDecimal _latestSpePrice;	// 上次成交特价  DEC(14,4)<null>
  private Date _latestSpeDate;	// 上次特价成交日期  DATE<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SalCustGoods init(){
		super.init();
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cust=null;	// 客户 <表主键:SysCustom>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _latestPrice=null;	// 上次成交价  DEC(14,4)
    _latestDate=null;	// 上次成交日期  DATE
    _latestSpePrice=null;	// 上次成交特价  DEC(14,4)
    _latestSpeDate=null;	// 上次特价成交日期  DATE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SalCustGoods loadUniqueCustGoods(boolean lockFlag,Integer org,Integer cust,Integer goods) {
    return (SalCustGoods)loadUnique(T.IDX_CUST_GOODS,lockFlag,org,cust,goods);
  }
  public static SalCustGoods chkUniqueCustGoods(boolean lockFlag,Integer org,Integer cust,Integer goods) {
    return (SalCustGoods)chkUnique(T.IDX_CUST_GOODS,lockFlag,org,cust,goods);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
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
  public Integer getCust(){
    return _cust;
  }
  public void setCust(Integer cust){
    _cust=cust;
  }
  public SysCustom gtCust(){
    if(getCust()==null)
      return null;
    return (SysCustom)get(SysCustom.class,getCust());
  }
  public void stCust(SysCustom cust){
    if(cust==null)
      setCust(null);
    else
      setCust(cust.getPkey());
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
