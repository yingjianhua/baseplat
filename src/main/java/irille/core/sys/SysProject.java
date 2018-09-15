//Created on 2005-10-20
package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 项目
 * @author whx 此表需要进一步完善，whx 20130402
 */
public class SysProject extends BeanInt<SysProject> {
	public static final Tb TB = new Tb(SysProject.class, "项目信息", "项目")
			.setAutoIncrement().addActIUDL();

	public enum OState implements IEnumOpt {//@formatter:off
		INIT(0,"未开始"),DOING(1,"执行中"),PAUSE(2,"暂停"),
		KILL(8,"中止"),	DONE(9,"已完成")	;
		public static final String NAME="部门状态";
		public static final OState DEFAULT = INIT; // 定义缺省值
		private EnumLine _line;
		private OState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20), 
		NAME(SYS.NAME__100,"项目名称"),
		STATE(TB.crt(OState.INIT)),
		ORG(SYS.ORG,"所属机构"), 
		DEPT(SYS.DEPT,"负责部门"),
		MANAGER(SYS.MANAGER_NULL,"项目负责人"),
		PLAN(TB.crtCmb("plan", "计划", CmbBeginEndDate.TB)),
		REALITY(TB.crtCmb("reality", "实际", CmbBeginEndDate.TB)),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PLAN_B_DATE(TB.get("planBDate")),	//计划起始日期
		PLAN_E_DATE(TB.get("planEDate")),	//计划结束日期
		REALITY_B_DATE(TB.get("realityBDate")),	//实际起始日期
		REALITY_E_DATE(TB.get("realityEDate")),	//实际结束日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
			public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
			public static final Index IDX_ORG_DEPT = TB.addIndex("orgDept", false,T.ORG,T.DEPT);
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
	//
	// public String toStringExt() {
	// return getName();
	// }
	//
	// public String toStringExtView() {
	// return "name";
	// }

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(20)
  private String _name;	// 项目名称  STR(100)
  private Byte _state;	// 部门状态 <OState>  BYTE
	// INIT:0,未开始
	// DOING:1,执行中
	// PAUSE:2,暂停
	// KILL:8,中止
	// DONE:9,已完成
  private Integer _org;	// 所属机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 负责部门 <表主键:SysDept>  INT
  private Integer _manager;	// 项目负责人 <表主键:SysUser>  INT<null>
  private Date _planBDate;	// 计划起始日期  DATE<null>
  private Date _planEDate;	// 计划结束日期  DATE<null>
  private Date _realityBDate;	// 实际起始日期  DATE<null>
  private Date _realityEDate;	// 实际结束日期  DATE<null>
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysProject init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 项目名称  STR(100)
    _state=OState.DEFAULT.getLine().getKey();	// 部门状态 <OState>  BYTE
    _org=null;	// 所属机构 <表主键:SysOrg>  INT
    _dept=null;	// 负责部门 <表主键:SysDept>  INT
    _manager=null;	// 项目负责人 <表主键:SysUser>  INT
    _planBDate=null;	// 计划起始日期  DATE
    _planEDate=null;	// 计划结束日期  DATE
    _realityBDate=null;	// 实际起始日期  DATE
    _realityEDate=null;	// 实际结束日期  DATE
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysProject loadUniqueCode(boolean lockFlag,String code) {
    return (SysProject)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysProject chkUniqueCode(boolean lockFlag,String code) {
    return (SysProject)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Byte getState(){
    return _state;
  }
  public void setState(Byte state){
    _state=state;
  }
  public OState gtState(){
    return (OState)(OState.INIT.getLine().get(_state));
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
  //组合对象的操作
  public CmbBeginEndDate gtPlan(){
    CmbBeginEndDate b=new CmbBeginEndDate();
    	b.setBDate(_planBDate);
    	b.setEDate(_planEDate);
    return b;
  }
  public void stPlan(CmbBeginEndDate plan){
    _planBDate=plan.getBDate();
    _planEDate=plan.getEDate();
  }
  public Date getPlanBDate(){
    return _planBDate;
  }
  public void setPlanBDate(Date planBDate){
    _planBDate=planBDate;
  }
  public Date getPlanEDate(){
    return _planEDate;
  }
  public void setPlanEDate(Date planEDate){
    _planEDate=planEDate;
  }
  //组合对象的操作
  public CmbBeginEndDate gtReality(){
    CmbBeginEndDate b=new CmbBeginEndDate();
    	b.setBDate(_realityBDate);
    	b.setEDate(_realityEDate);
    return b;
  }
  public void stReality(CmbBeginEndDate reality){
    _realityBDate=reality.getBDate();
    _realityEDate=reality.getEDate();
  }
  public Date getRealityBDate(){
    return _realityBDate;
  }
  public void setRealityBDate(Date realityBDate){
    _realityBDate=realityBDate;
  }
  public Date getRealityEDate(){
    return _realityEDate;
  }
  public void setRealityEDate(Date realityEDate){
    _realityEDate=realityEDate;
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
