package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysCell extends BeanInt<SysCell> implements IExtName {

	public static final Tb TB = new Tb(SysCell.class, "核算单元").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__40),
		NAME(SYS.NAME__100),
		YEAR(SYS.YEAR),
		ORG(SYS.ORG),
		DEPT(SYS.DEPT, true),
		TEMPLAT(SYS.TEMPLAT, "财务模板"), //部门的财务模板不能改，从上级机构上拷贝下来\\\\
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
		public static final Index IDX_ORG_DEPT = TB.addIndex("orgDept", true,T.ORG, T.DEPT);
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
	
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	@Override
	public String getExtName() {
	  return getName();
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private String _name;	// 名称  STR(100)
  private Short _year;	// 年份  SHORT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT<null>
  private Integer _templat;	// 财务模板 <表主键:SysTemplat>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysCell init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _year=0;	// 年份  SHORT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _templat=null;	// 财务模板 <表主键:SysTemplat>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysCell loadUniqueCode(boolean lockFlag,String code) {
    return (SysCell)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysCell chkUniqueCode(boolean lockFlag,String code) {
    return (SysCell)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysCell loadUniqueOrgDept(boolean lockFlag,Integer org,Integer dept) {
    return (SysCell)loadUnique(T.IDX_ORG_DEPT,lockFlag,org,dept);
  }
  public static SysCell chkUniqueOrgDept(boolean lockFlag,Integer org,Integer dept) {
    return (SysCell)chkUnique(T.IDX_ORG_DEPT,lockFlag,org,dept);
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
  public Short getYear(){
    return _year;
  }
  public void setYear(Short year){
    _year=year;
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
  public Integer getDept(){
    return _dept;
  }
  public void setDept(Integer dept){
    _dept=dept;
  }
  public SysDept gtDept(){
    if(getDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getDept());
  }
  public void stDept(SysDept dept){
    if(dept==null)
      setDept(null);
    else
      setDept(dept.getPkey());
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
