/**
 * 
 */
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
 * @author surface1
 *
 */
public class GsLocation extends BeanInt implements IExtName {
	private static final Log LOG = new Log(GsLocation.class);

	public static final Tb TB = new Tb(GsLocation.class, "货位"	) //仓库是特殊的部门，必须先在部门中定义好
	.setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()), 
		WAREHOUSE(GsWarehouse.fldOutKey()),
		NAME(SYS.NAME__100),
		ENABLED(SYS.ENABLED),
		WEIGHT(SYS.QTY,"总可用重量"),
		WEIGHT_USED(SYS.QTY,"已用重量"),
		WEIGHT_AVAIL(SYS.QTY,"可用重量"),
		VALUME(SYS.QTY,"总可用体积"),
		VALUME_USED(SYS.QTY,"已用体积"),
		VALUME_AVAIL(SYS.QTY,"可用体积"),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_HOUSE_NAME = TB.addIndex("houseName", true,WAREHOUSE,NAME);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = TB.crtOutKey(TB,code,name);
		fld.setType(null);
		return fld;
	}
	
	public static GsLocation chkUniqueWarehouseName(boolean lockFlag,Integer warehouse, String name) {
	    return (GsLocation)chkUnique(T.IDX_HOUSE_NAME, lockFlag, warehouse, name);
	  }
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private String _name;	// 名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private BigDecimal _weight;	// 总可用重量  DEC(14,4)
  private BigDecimal _weightUsed;	// 已用重量  DEC(14,4)
  private BigDecimal _weightAvail;	// 可用重量  DEC(14,4)
  private BigDecimal _valume;	// 总可用体积  DEC(14,4)
  private BigDecimal _valumeUsed;	// 已用体积  DEC(14,4)
  private BigDecimal _valumeAvail;	// 可用体积  DEC(14,4)
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsLocation init(){
		super.init();
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _name=null;	// 名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _weight=ZERO;	// 总可用重量  DEC(14,4)
    _weightUsed=ZERO;	// 已用重量  DEC(14,4)
    _weightAvail=ZERO;	// 可用重量  DEC(14,4)
    _valume=ZERO;	// 总可用体积  DEC(14,4)
    _valumeUsed=ZERO;	// 已用体积  DEC(14,4)
    _valumeAvail=ZERO;	// 可用体积  DEC(14,4)
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsLocation loadUniqueHouseName(boolean lockFlag,Integer warehouse,String name) {
    return (GsLocation)loadUnique(T.IDX_HOUSE_NAME,lockFlag,warehouse,name);
  }
  public static GsLocation chkUniqueHouseName(boolean lockFlag,Integer warehouse,String name) {
    return (GsLocation)chkUnique(T.IDX_HOUSE_NAME,lockFlag,warehouse,name);
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
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
  public BigDecimal getWeight(){
    return _weight;
  }
  public void setWeight(BigDecimal weight){
    _weight=weight;
  }
  public BigDecimal getWeightUsed(){
    return _weightUsed;
  }
  public void setWeightUsed(BigDecimal weightUsed){
    _weightUsed=weightUsed;
  }
  public BigDecimal getWeightAvail(){
    return _weightAvail;
  }
  public void setWeightAvail(BigDecimal weightAvail){
    _weightAvail=weightAvail;
  }
  public BigDecimal getValume(){
    return _valume;
  }
  public void setValume(BigDecimal valume){
    _valume=valume;
  }
  public BigDecimal getValumeUsed(){
    return _valumeUsed;
  }
  public void setValumeUsed(BigDecimal valumeUsed){
    _valumeUsed=valumeUsed;
  }
  public BigDecimal getValumeAvail(){
    return _valumeAvail;
  }
  public void setValumeAvail(BigDecimal valumeAvail){
    _valumeAvail=valumeAvail;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
