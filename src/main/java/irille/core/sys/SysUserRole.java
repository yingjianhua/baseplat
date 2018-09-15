package irille.core.sys;

import irille.core.prv.PrvRole;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysUserRole extends BeanInt<SysUserRole> {
	public static final Tb TB = new Tb(SysUserRole.class, "用户角色").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		USER_SYS(SysUser.fldOutKey()),
		ROLE(PrvRole.fldOutKey()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_USER_ROLE = TB.addIndex("userRole", true,USER_SYS, ROLE);
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

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _userSys;	// 用户 <表主键:SysUser>  INT
  private Integer _role;	// 角色管理 <表主键:PrvRole>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysUserRole init(){
		super.init();
    _userSys=null;	// 用户 <表主键:SysUser>  INT
    _role=null;	// 角色管理 <表主键:PrvRole>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysUserRole loadUniqueUserRole(boolean lockFlag,Integer userSys,Integer role) {
    return (SysUserRole)loadUnique(T.IDX_USER_ROLE,lockFlag,userSys,role);
  }
  public static SysUserRole chkUniqueUserRole(boolean lockFlag,Integer userSys,Integer role) {
    return (SysUserRole)chkUnique(T.IDX_USER_ROLE,lockFlag,userSys,role);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getUserSys(){
    return _userSys;
  }
  public void setUserSys(Integer userSys){
    _userSys=userSys;
  }
  public SysUser gtUserSys(){
    if(getUserSys()==null)
      return null;
    return (SysUser)get(SysUser.class,getUserSys());
  }
  public void stUserSys(SysUser userSys){
    if(userSys==null)
      setUserSys(null);
    else
      setUserSys(userSys.getPkey());
  }
  public Integer getRole(){
    return _role;
  }
  public void setRole(Integer role){
    _role=role;
  }
  public PrvRole gtRole(){
    if(getRole()==null)
      return null;
    return (PrvRole)get(PrvRole.class,getRole());
  }
  public void stRole(PrvRole role){
    if(role==null)
      setRole(null);
    else
      setRole(role.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
