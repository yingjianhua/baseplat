/**
 * 
 */
package irille.core.prv;

import irille.core.sys.CmbRange;
import irille.core.sys.SysCell;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.ORangeType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class PrvRole extends BeanInt<PrvRole> implements IExtName {
	private static final Log LOG = new Log(PrvRole.class);
	public static final Tb TB = new Tb(PrvRole.class, "角色管理").setAutoIncrement().addActIUDL().addActOpt("updCtrl", "权限控制");

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20),
		NAME(SYS.NAME__100),
		ENABLED(SYS.ENABLED),
		CMB_SYS_RANGE(CmbRange.fldFlds()),
		UPDATED_BY(SYS.UPDATED_BY),
		UPDATED_TIME(SYS.UPDATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		RANGE(TB.get("range")),	//可用对象
		RANGE_TYPE(TB.get("rangeType")),	//可视范围
		RANGE_PKEY(TB.get("rangePkey")),	//可用对象主键值
		CELL(TB.get("cell")),	//管理核算单元
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
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
	@Override
	public String getExtName() {
	  return getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _rangeType;	// 可视范围 <ORangeType>  BYTE
	// GRP:1,集团级
	// ORG_ALL:11,上下级机构
	// ORG_DOWN:12,及下级机构
	// ORG_UP:13,及上级机构
	// ORG:14,本机构
	// CELL_ALL:21,上下级核算单元
	// CELL_DOWN:22,及下级核算单元
	// CELL_UP:23,及上级核算单元
	// CELL:24,本核算单元
  private Integer _rangePkey;	// 可用对象主键值  INT<null>
  private Integer _cell;	// 管理核算单元 <表主键:SysCell>  INT
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PrvRole init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rangeType=ORangeType.DEFAULT.getLine().getKey();	// 可视范围 <ORangeType>  BYTE
    _rangePkey=null;	// 可用对象主键值  INT
    _cell=null;	// 管理核算单元 <表主键:SysCell>  INT
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PrvRole loadUniqueCode(boolean lockFlag,String code) {
    return (PrvRole)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static PrvRole chkUniqueCode(boolean lockFlag,String code) {
    return (PrvRole)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
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
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtRange(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtRangeType();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_rangePkey);
  }
  public Byte getRangeType(){
    return _rangeType;
  }
  public void setRangeType(Byte rangeType){
    _rangeType=rangeType;
  }
  public ORangeType gtRangeType(){
    return (ORangeType)(ORangeType.GRP.getLine().get(_rangeType));
  }
  public void stRangeType(ORangeType rangeType){
    _rangeType=rangeType.getLine().getKey();
  }
  public Integer getRangePkey(){
    return _rangePkey;
  }
  public void setRangePkey(Integer rangePkey){
    _rangePkey=rangePkey;
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

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
