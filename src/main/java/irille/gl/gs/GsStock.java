/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.core.sys.Sys.OEnabled;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * TODO 新加一个单独的修改货位功能
 * @author whx
 * @version 创建时间：2014年8月16日 上午8:58:49
 */
public class GsStock extends BeanInt implements IExtName{
	public static final Tb TB = new Tb(GsStock.class, "存货").setAutoIncrement().addActList().addActIns().addActUpd().addActPrint(); //存货的核算级别是仓库

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		WAREHOUSE(GsWarehouse.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		QTY(SYS.QTY,"库存数量"),
		LOCATION(GsLocation.fldOutKey().setNull()), //货位固定，一种商品在一个仓库不再设多个货位，简化管理
		ENROUTE_QTY(SYS.QTY,"在途数量"),//已采购未到货的数量
		LOCKED_QTY(SYS.QTY,"存货锁定数量"), //已销售未出库的数量
		LOWEST_QTY(SYS.QTY,"最低库存"),
		SAFETY_QTY(SYS.QTY,"安全库存"),
		LIMIT_QTY(SYS.QTY,"上限库存"),
		PUR_LEAD_DAYS(SYS.SHORT,"采购提前天数"),
		ENABLED(SYS.ENABLED),
		CELL(SYS.CELL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		LINES(Tb.crtLines(T.WAREHOUSE, CN_LINES,	T.PKEY))
		;
		// 索引
		public static final Index IDX_WG = TB.addIndex("wg", true,WAREHOUSE,GOODS);
		private Fld _fld;
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}

	//@formatter:on

	@Override
	public String getExtName() {
		return gtWarehouse().getExtName();
	}
	
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private BigDecimal _qty;	// 库存数量  DEC(14,4)
  private Integer _location;	// 货位 <表主键:GsLocation>  INT<null>
  private BigDecimal _enrouteQty;	// 在途数量  DEC(14,4)
  private BigDecimal _lockedQty;	// 存货锁定数量  DEC(14,4)
  private BigDecimal _lowestQty;	// 最低库存  DEC(14,4)
  private BigDecimal _safetyQty;	// 安全库存  DEC(14,4)
  private BigDecimal _limitQty;	// 上限库存  DEC(14,4)
  private Short _purLeadDays;	// 采购提前天数  SHORT
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsStock init(){
		super.init();
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _qty=ZERO;	// 库存数量  DEC(14,4)
    _location=null;	// 货位 <表主键:GsLocation>  INT
    _enrouteQty=ZERO;	// 在途数量  DEC(14,4)
    _lockedQty=ZERO;	// 存货锁定数量  DEC(14,4)
    _lowestQty=ZERO;	// 最低库存  DEC(14,4)
    _safetyQty=ZERO;	// 安全库存  DEC(14,4)
    _limitQty=ZERO;	// 上限库存  DEC(14,4)
    _purLeadDays=0;	// 采购提前天数  SHORT
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsStock loadUniqueWg(boolean lockFlag,Integer warehouse,Integer goods) {
    return (GsStock)loadUnique(T.IDX_WG,lockFlag,warehouse,goods);
  }
  public static GsStock chkUniqueWg(boolean lockFlag,Integer warehouse,Integer goods) {
    return (GsStock)chkUnique(T.IDX_WG,lockFlag,warehouse,goods);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
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
  public BigDecimal getQty(){
    return _qty;
  }
  public void setQty(BigDecimal qty){
    _qty=qty;
  }
  public Integer getLocation(){
    return _location;
  }
  public void setLocation(Integer location){
    _location=location;
  }
  public GsLocation gtLocation(){
    if(getLocation()==null)
      return null;
    return (GsLocation)get(GsLocation.class,getLocation());
  }
  public void stLocation(GsLocation location){
    if(location==null)
      setLocation(null);
    else
      setLocation(location.getPkey());
  }
  public BigDecimal getEnrouteQty(){
    return _enrouteQty;
  }
  public void setEnrouteQty(BigDecimal enrouteQty){
    _enrouteQty=enrouteQty;
  }
  public BigDecimal getLockedQty(){
    return _lockedQty;
  }
  public void setLockedQty(BigDecimal lockedQty){
    _lockedQty=lockedQty;
  }
  public BigDecimal getLowestQty(){
    return _lowestQty;
  }
  public void setLowestQty(BigDecimal lowestQty){
    _lowestQty=lowestQty;
  }
  public BigDecimal getSafetyQty(){
    return _safetyQty;
  }
  public void setSafetyQty(BigDecimal safetyQty){
    _safetyQty=safetyQty;
  }
  public BigDecimal getLimitQty(){
    return _limitQty;
  }
  public void setLimitQty(BigDecimal limitQty){
    _limitQty=limitQty;
  }
  public Short getPurLeadDays(){
    return _purLeadDays;
  }
  public void setPurLeadDays(Short purLeadDays){
    _purLeadDays=purLeadDays;
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
  public static java.util.List<GsStock> getLines(irille.gl.gs.GsWarehouse mainBean){
    return list(irille.gl.gs.GsStock.class,
        " warehouse=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<GsStock> getLines(irille.gl.gs.GsWarehouse mainBean, int idx,int count){
    return list(irille.gl.gs.GsStock.class,false," warehouse=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.gl.gs.GsWarehouse mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM gs_stock WHERE warehouse=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
