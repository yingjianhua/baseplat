package irille.core.sys;

import irille.core.sys.Sys.OComPersonFlag;
import irille.core.sys.Sys.OEnabled;
import irille.pub.bean.BeanInt;
import irille.pub.bean.ISeq;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysSupplier extends BeanInt<SysSupplier> implements IExtName,ISeq{
	public static final Tb TB = new Tb(SysSupplier.class, "供应商信息", "供应商")
			.setAutoIncrement().addActIUDL()
		    .addActEdit("联系人").addActEnabled();

	public enum T implements IEnumFld {// @formatter:off
		PKEY(TB.crtIntPkey()), 
		CODE(SYS.CODE__40), 
		NAME(SYS.NAME__100), 
		SHORT_NAME(SYS.SHORT_NAME__20_NULL,"简称"), 
		COM_PERSON_FLAG(TB.crt(Sys.OComPersonFlag.COM)), 
		ENABLED(TB.crt(SYS.ENABLED)), 
		MNG_ORG(SYS.ORG, "管理机构"), 
		MNG_DEPT(SYS.DEPT, "管理部门", true), 
		BUSINESS_MEMBER(SYS.BUSINESS_MEMBER,true), 
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true, T.CODE);
		public static final Index IDX_SHORT_NAME = TB.addIndex("shortName",
				false, T.SHORT_NAME);
		private Fld _fld;

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld, this);
		}

		public Fld getFld() {
			return _fld;
		}
	}// @formatter:on

	static { // 在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();// 加锁所有字段,不可以修改
	}

	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	
	@Override
	public String getExtName() {
	  return getCode();
	}
	
	 //取一对一表对象: SysCom
  public SysCom gtCom(){
    return get(SysCom.class,gtLongPkey());
  }
  public void stCom(SysCom com){
      setPkey((int)(com.getPkey()/SysTable.NUM_BASE));
  }
  
  public void initSeq(SysSeq s) {
		s.setPkey(gtTable().getPkey());
		s.stOrgFlag(false);
		s.stType(Sys.OType.NONE);
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private String _name;	// 名称  STR(100)
  private String _shortName;	// 简称  STR(20)<null>
  private Byte _comPersonFlag;	// 性质 <OComPersonFlag>  BYTE
	// COM:1,单位
	// PERSON:2,个人
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Integer _mngOrg;	// 管理机构 <表主键:SysOrg>  INT
  private Integer _mngDept;	// 管理部门 <表主键:SysDept>  INT<null>
  private Integer _businessMember;	// 业务代表 <表主键:SysUser>  INT<null>
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysSupplier init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _shortName=null;	// 简称  STR(20)
    _comPersonFlag=OComPersonFlag.DEFAULT.getLine().getKey();	// 性质 <OComPersonFlag>  BYTE
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _mngOrg=null;	// 管理机构 <表主键:SysOrg>  INT
    _mngDept=null;	// 管理部门 <表主键:SysDept>  INT
    _businessMember=null;	// 业务代表 <表主键:SysUser>  INT
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysSupplier loadUniqueCode(boolean lockFlag,String code) {
    return (SysSupplier)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysSupplier chkUniqueCode(boolean lockFlag,String code) {
    return (SysSupplier)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public String getShortName(){
    return _shortName;
  }
  public void setShortName(String shortName){
    _shortName=shortName;
  }
  public Byte getComPersonFlag(){
    return _comPersonFlag;
  }
  public void setComPersonFlag(Byte comPersonFlag){
    _comPersonFlag=comPersonFlag;
  }
  public OComPersonFlag gtComPersonFlag(){
    return (OComPersonFlag)(OComPersonFlag.COM.getLine().get(_comPersonFlag));
  }
  public void stComPersonFlag(OComPersonFlag comPersonFlag){
    _comPersonFlag=comPersonFlag.getLine().getKey();
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
  public Integer getMngOrg(){
    return _mngOrg;
  }
  public void setMngOrg(Integer mngOrg){
    _mngOrg=mngOrg;
  }
  public SysOrg gtMngOrg(){
    if(getMngOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getMngOrg());
  }
  public void stMngOrg(SysOrg mngOrg){
    if(mngOrg==null)
      setMngOrg(null);
    else
      setMngOrg(mngOrg.getPkey());
  }
  public Integer getMngDept(){
    return _mngDept;
  }
  public void setMngDept(Integer mngDept){
    _mngDept=mngDept;
  }
  public SysDept gtMngDept(){
    if(getMngDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getMngDept());
  }
  public void stMngDept(SysDept mngDept){
    if(mngDept==null)
      setMngDept(null);
    else
      setMngDept(mngDept.getPkey());
  }
  public Integer getBusinessMember(){
    return _businessMember;
  }
  public void setBusinessMember(Integer businessMember){
    _businessMember=businessMember;
  }
  public SysUser gtBusinessMember(){
    if(getBusinessMember()==null)
      return null;
    return (SysUser)get(SysUser.class,getBusinessMember());
  }
  public void stBusinessMember(SysUser businessMember){
    if(businessMember==null)
      setBusinessMember(null);
    else
      setBusinessMember(businessMember.getPkey());
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
