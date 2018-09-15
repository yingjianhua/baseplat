package irille.core.sys;

import irille.core.sys.Sys.OEnabled;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVOneToOne;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Fld.OOutType;
import irille.pub.tb.Tb.Index;

/**
 * Title: 部门表<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class SysDept extends BeanInt<SysDept> implements IExtName {
	public static final Tb TB = new Tb(SysDept.class, "部门信息", "部门").setAutoIncrement().addActIUDL().addActEdit("联系人")
	    .addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
			PKEY(TB.crtIntPkey()),
			CODE(SYS.CODE__40), 
			NAME(SYS.NAME__100,"部门名称"),
			ENABLED(TB.crt(SYS.ENABLED)),
			ORG(SYS.ORG,"所属机构"), 
			MANAGER(SYS.MANAGER_NULL,"部门负责人"),
			DEPT_UP(SYS.DEPT, "上级部门",true),
			CELL(SYS.CELL,true),
			REM(SYS.REM__200_NULL),
			ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
			// 索引
			public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
			public static final Index IDX_ORG_DEPT = TB.addIndex("orgCode", false,T.ORG,T.CODE);
			public static final Index IDX_DEPTUP_DEPT = 
				TB.addIndex("DeptUpDept", false,T.DEPT_UP,T.CODE);
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
		Fld fld = fldOutKey(TB.getCodeNoPackage(),TB.getShortName());
		fld.setType(null);
		return fld;
	}
	public static Fld fldOutKey(String code,String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private String _name;	// 部门名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Integer _org;	// 所属机构 <表主键:SysOrg>  INT
  private Integer _manager;	// 部门负责人 <表主键:SysUser>  INT<null>
  private Integer _deptUp;	// 上级部门 <表主键:SysDept>  INT<null>
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT<null>
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysDept init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 部门名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _org=null;	// 所属机构 <表主键:SysOrg>  INT
    _manager=null;	// 部门负责人 <表主键:SysUser>  INT
    _deptUp=null;	// 上级部门 <表主键:SysDept>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysDept loadUniqueCode(boolean lockFlag,String code) {
    return (SysDept)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysDept chkUniqueCode(boolean lockFlag,String code) {
    return (SysDept)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Integer getManager(){
    return _manager;
  }
  public void setManager(Integer manager){
    _manager=manager;
  }
  public SysUser gtManager(){
    if(getManager()==null)
      return null;
    return (SysUser)get(SysUser.class,getManager());
  }
  public void stManager(SysUser manager){
    if(manager==null)
      setManager(null);
    else
      setManager(manager.getPkey());
  }
  public Integer getDeptUp(){
    return _deptUp;
  }
  public void setDeptUp(Integer deptUp){
    _deptUp=deptUp;
  }
  public SysDept gtDeptUp(){
    if(getDeptUp()==null)
      return null;
    return (SysDept)get(SysDept.class,getDeptUp());
  }
  public void stDeptUp(SysDept deptUp){
    if(deptUp==null)
      setDeptUp(null);
    else
      setDeptUp(deptUp.getPkey());
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
