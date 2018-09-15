//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.OState;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVOneToOne;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysEm extends BeanInt<SysEm> implements IExtName {
	private static final Log LOG = new Log(SysEm.class);
	public static final Tb TB = new Tb(SysEm.class, "职员信息", "职员").setAutoIncrement().addActList().addActIns().addActUpd();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.STR__8,"工号"), 
		NAME(SYS.NAME__100),   //此字段与SysPerson,SysUser表中的Name保持同步!!
		NICKNAME(SYS.NICKNAME__40_NULL),//此字段与SysUser表中的保持同步!!
		ENAME(SYS.NAME__100,"英文名", true),
		STATE(TB.crt(Sys.OState.DEFAULT)),
		ORG(SYS.ORG), //此字段与SysUser表中的保持同步!!
		DEPT(SYS.DEPT),//此字段与SysUser表中的保持同步!!
		USER_SYS(SYS.USER_SYS,true), //对应的系统用户
		CELL(SYS.CELL,true),
		PERSON(new FldVOneToOne(TB.getClazz(), "person", "个人信息", true)),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
		public static final Index IDX_ORG_DEPT = TB.addIndex("orgDept", false,T.ORG,T.DEPT);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.ORG.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(),TB.getShortName());
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
	
	 //取一对一表对象: SysPerson
  public SysPerson gtPerson(){
    return get(SysPerson.class,gtLongPkey());
  }
  public void stPerson(SysPerson person){
      setPkey((int)(person.getPkey()/SysTable.NUM_BASE));
  }
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 工号  STR(8)
  private String _name;	// 名称  STR(100)
  private String _nickname;	// 昵称  STR(40)<null>
  private String _ename;	// 英文名  STR(100)<null>
  private Byte _state;	// 职员状态 <OState>  BYTE
	// PROBATION_PERIOD:0,试用期
	// NORMAL:1,在职
	// STOP_PAY:11,停薪留职
	// RETIRE:81,退休
	// RESIGN:91,辞职
	// PROBATION_PERIOD_LEAVE:92,试用期离职
	// FIRE:93,开除
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _userSys;	// 用户 <表主键:SysUser>  INT<null>
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysEm init(){
		super.init();
    _code=null;	// 工号  STR(8)
    _name=null;	// 名称  STR(100)
    _nickname=null;	// 昵称  STR(40)
    _ename=null;	// 英文名  STR(100)
    _state=OState.DEFAULT.getLine().getKey();	// 职员状态 <OState>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _userSys=null;	// 用户 <表主键:SysUser>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysEm loadUniqueCode(boolean lockFlag,String code) {
    return (SysEm)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysEm chkUniqueCode(boolean lockFlag,String code) {
    return (SysEm)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public String getNickname(){
    return _nickname;
  }
  public void setNickname(String nickname){
    _nickname=nickname;
  }
  public String getEname(){
    return _ename;
  }
  public void setEname(String ename){
    _ename=ename;
  }
  public Byte getState(){
    return _state;
  }
  public void setState(Byte state){
    _state=state;
  }
  public OState gtState(){
    return (OState)(OState.NORMAL.getLine().get(_state));
  }
  public void stState(OState state){
    _state=state.getLine().getKey();
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

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
