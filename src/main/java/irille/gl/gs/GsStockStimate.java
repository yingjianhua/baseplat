/**
 * 
 */
package irille.gl.gs;

import irille.gl.gs.Gs.OEnrouteType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class GsStockStimate extends BeanLong {
	public static final Tb TB = new Tb(GsStockStimate.class, "预计出入库登记").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		WAREHOUSE(GsWarehouse.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		ENROUTE_TYPE(TB.crt(Gs.OEnrouteType.DEFAULT)),
		UOM(GsUom.fldOutKey()),
		QTY(SYS.QTY),
		PLAN_DATE(SYS.DATE__NULL,"预计到货出货日期"),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		CREATED_DATE(SYS.CREATED_DATE),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		ORIG_FORM_NUM(TB.get("origFormNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_GOODS = TB.addIndex("goods", false,GOODS);
		public static final Index IDX_FORM = TB.addIndex("form", false,ORIG_FORM);
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
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Byte _enrouteType;	// 预计出入库货物类别 <OEnrouteType>  BYTE
	// YQG:11,已请购
	// DBZT:12,调拔在途
	// CGZT:13,采购在途
	// SCDD:14,生产订单
	// DHZJ:15,到货/在检
	// WWDD:16,委外订单
	// QTZT:49,其他在途
	// YDQ:51,已订购
	// DBDF:52,调拔待发
	// DFH:53,待发货
	// SCWL:54,生产未领
	// BLJH:55,备料计划
	// WWWL:56,委外未领
	// QTSD:99,其他锁定
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private Date _planDate;	// 预计到货出货日期  DATE<null>
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Date _createdDate;	// 建档日期  DATE
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsStockStimate init(){
		super.init();
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _enrouteType=OEnrouteType.DEFAULT.getLine().getKey();	// 预计出入库货物类别 <OEnrouteType>  BYTE
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _planDate=null;	// 预计到货出货日期  DATE
    _origForm=null;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _createdDate=Env.getWorkDate();	// 建档日期  DATE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getWarehouse(){
    return _warehouse;
  }
  public void setWarehouse(Integer warehouse){
    _warehouse=warehouse;
  }
  public GsWarehouse gtWarehouse(){
    if(getWarehouse()==null)
      return null;
    return (GsWarehouse)get(GsWarehouse.class,getWarehouse());
  }
  public void stWarehouse(GsWarehouse warehouse){
    if(warehouse==null)
      setWarehouse(null);
    else
      setWarehouse(warehouse.getPkey());
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
  public Byte getEnrouteType(){
    return _enrouteType;
  }
  public void setEnrouteType(Byte enrouteType){
    _enrouteType=enrouteType;
  }
  public OEnrouteType gtEnrouteType(){
    return (OEnrouteType)(OEnrouteType.CGZT.getLine().get(_enrouteType));
  }
  public void stEnrouteType(OEnrouteType enrouteType){
    _enrouteType=enrouteType.getLine().getKey();
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
  public BigDecimal getQty(){
    return _qty;
  }
  public void setQty(BigDecimal qty){
    _qty=qty;
  }
  public Date getPlanDate(){
    return _planDate;
  }
  public void setPlanDate(Date planDate){
    _planDate=planDate;
  }
  public Long getOrigForm(){
    return _origForm;
  }
  public void setOrigForm(Long origForm){
    _origForm=origForm;
  }
  //外部主键对象: IForm
  public Bean gtOrigForm(){
    return (Bean)gtLongTbObj(getOrigForm());
  }
  public void stOrigForm(Bean origForm){
      setOrigForm(origForm.gtLongPkey());
  }
  public String getOrigFormNum(){
    return _origFormNum;
  }
  public void setOrigFormNum(String origFormNum){
    _origFormNum=origFormNum;
  }
  public Date getCreatedDate(){
    return _createdDate;
  }
  public void setCreatedDate(Date createdDate){
    _createdDate=createdDate;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
