/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysEm;
import irille.core.sys.SysOrg;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.gl.gs.Gs.OOutOrder;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * @author surface1
 *
 */
public class GsWarehouse extends BeanInt implements IExtName {
	private static final Log LOG = new Log(GsWarehouse.class);

	public static final Tb TB = new Tb(GsWarehouse.class, "仓库").setAutoLocal() //仓库是特殊的部门，必须先在部门中定义好
	    .addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		DEPT(SysDept.fldOneToOne()),
		ORG(SysOrg.fldOutKey()),
		CELL(SYS.CELL),
		ENABLED(SYS.ENABLED),
		LOCATION_FLAG(SYS.NY,"货位管理标志"),
		OUT_ORDER(TB.crt(Gs.OOutOrder.DEFAULT)),
		CONSIGNEES(SYS.EM,"收货人",true),
		INVENTED(SYS.NY,"是否为虚拟仓库"), //虚拟仓库用于直飞单的处理
		//TODO CELL,核算单位更改，自动更改；本仓库管理不可更改
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CELL = TB.addIndex("cell", false,CELL);
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on
	
	@Override
	public String getExtName() {
		return gtDept().getName();
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _locationFlag;	// 货位管理标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _outOrder;	// 存货出库顺序 <OOutOrder>  BYTE
	// FREE:1,随意
	// FIFO:2,先入先出
  private Integer _consignees;	// 收货人 <表主键:SysEm>  INT<null>
  private Byte _invented;	// 是否为虚拟仓库 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsWarehouse init(){
		super.init();
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _locationFlag=OYn.DEFAULT.getLine().getKey();	// 货位管理标志 <OYn>  BYTE
    _outOrder=OOutOrder.DEFAULT.getLine().getKey();	// 存货出库顺序 <OOutOrder>  BYTE
    _consignees=null;	// 收货人 <表主键:SysEm>  INT
    _invented=OYn.DEFAULT.getLine().getKey();	// 是否为虚拟仓库 <OYn>  BYTE
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  //取一对一表对象: SysDept
  public SysDept gtDept(){
    return get(SysDept.class,getPkey());
  }
  public void stDept(SysDept dept){
      setPkey(dept.getPkey());
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
  public Byte getLocationFlag(){
    return _locationFlag;
  }
  public void setLocationFlag(Byte locationFlag){
    _locationFlag=locationFlag;
  }
  public Boolean gtLocationFlag(){
    return byteToBoolean(_locationFlag);
  }
  public void stLocationFlag(Boolean locationFlag){
    _locationFlag=booleanToByte(locationFlag);
  }
  public Byte getOutOrder(){
    return _outOrder;
  }
  public void setOutOrder(Byte outOrder){
    _outOrder=outOrder;
  }
  public OOutOrder gtOutOrder(){
    return (OOutOrder)(OOutOrder.FREE.getLine().get(_outOrder));
  }
  public void stOutOrder(OOutOrder outOrder){
    _outOrder=outOrder.getLine().getKey();
  }
  public Integer getConsignees(){
    return _consignees;
  }
  public void setConsignees(Integer consignees){
    _consignees=consignees;
  }
  public SysEm gtConsignees(){
    if(getConsignees()==null)
      return null;
    return (SysEm)get(SysEm.class,getConsignees());
  }
  public void stConsignees(SysEm consignees){
    if(consignees==null)
      setConsignees(null);
    else
      setConsignees(consignees.getPkey());
  }
  public Byte getInvented(){
    return _invented;
  }
  public void setInvented(Byte invented){
    _invented=invented;
  }
  public Boolean gtInvented(){
    return byteToBoolean(_invented);
  }
  public void stInvented(Boolean invented){
    _invented=booleanToByte(invented);
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
