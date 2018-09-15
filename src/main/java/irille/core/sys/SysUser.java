//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.OLoginState;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Fld.OOutType;
import irille.pub.tb.Tb.Index;

public class SysUser extends BeanInt<SysUser> implements IExtName {
	public static final Tb TB = new Tb(SysUser.class, "用户管理", "用户").setAutoIncrement().addActIUDL()
	    .addActOpt("pwd", "密码修改","upd-icon", true).addActOpt("role", "角色设置", "upd-icon", true);

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		LOGIN_NAME(SYS.CODE__20,"登录账号"),
		NAME(SYS.NAME__100_NULL),
		NICKNAME(SYS.NICKNAME__40_NULL),
		LOGIN_STATE(TB.crt(Sys.OLoginState.DEFAULT)),//FLD_USER_STA
		ORG(SYS.ORG), 
		DEPT(SYS.DEPT),
		PHOTO(SYS.PHOTO__NULL,"照片"),
		TB_OBJ(Tb.crtLongTbObj("tbObj", "对象").setNull()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,T.LOGIN_NAME);
		public static final Index IDX_ORG_DEPT = TB.addIndex("orgDept", false,T.ORG,T.DEPT);
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
	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldOutKey() {
		Fld fld = fldOutKey(TB.getCodeNoPackage(),TB.getShortName());
		fld.setType(null);
		return fld;
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
  private String _loginName;	// 登录账号  STR(20)
  private String _name;	// 名称  STR(100)<null>
  private String _nickname;	// 昵称  STR(40)<null>
  private Byte _loginState;	// 登录状态 <OLoginState>  BYTE
	// LOGOUT:0,未登录
	// PC:1,PC在线
	// WAP:2,WAP在线
	// LOCKED:3,非法密码锁定
	// DISABLE:4,停用
	// NOT_ACTIVE:5,未激活
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private String _photo;	// 照片  STR(200)<null>
  private Long _tbObj;	// 对象  LONG<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysUser init(){
		super.init();
    _loginName=null;	// 登录账号  STR(20)
    _name=null;	// 名称  STR(100)
    _nickname=null;	// 昵称  STR(40)
    _loginState=OLoginState.DEFAULT.getLine().getKey();	// 登录状态 <OLoginState>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _photo=null;	// 照片  STR(200)
    _tbObj=null;	// 对象  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysUser loadUniqueCode(boolean lockFlag,String loginName) {
    return (SysUser)loadUnique(T.IDX_CODE,lockFlag,loginName);
  }
  public static SysUser chkUniqueCode(boolean lockFlag,String loginName) {
    return (SysUser)chkUnique(T.IDX_CODE,lockFlag,loginName);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getLoginName(){
    return _loginName;
  }
  public void setLoginName(String loginName){
    _loginName=loginName;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getNickname(){
    return _nickname;
  }
  public void setNickname(String nickname){
    _nickname=nickname;
  }
  public Byte getLoginState(){
    return _loginState;
  }
  public void setLoginState(Byte loginState){
    _loginState=loginState;
  }
  public OLoginState gtLoginState(){
    return (OLoginState)(OLoginState.LOGOUT.getLine().get(_loginState));
  }
  public void stLoginState(OLoginState loginState){
    _loginState=loginState.getLine().getKey();
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
  public String getPhoto(){
    return _photo;
  }
  public void setPhoto(String photo){
    _photo=photo;
  }
  public Long getTbObj(){
    return _tbObj;
  }
  public void setTbObj(Long tbObj){
    _tbObj=tbObj;
  }
  //外部主键对象: BeanLong
  public Bean gtTbObj(){
    return (Bean)gtLongTbObj(getTbObj());
  }
  public void stTbObj(Bean tbObj){
      setTbObj(tbObj.gtLongPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
