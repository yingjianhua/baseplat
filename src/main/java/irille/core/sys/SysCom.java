package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class SysCom extends BeanLong<SysCom> {
	private static final Log LOG = new Log(SysCom.class);
	public static final Tb TB = new Tb(SysCom.class, "单位信息", "单位").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		NAME(SYS.NAME__100,"机构名称"),//企业客户的名称必须唯一
		SHORT_NAME(SYS.SHORT_NAME__20_NULL,"机构简称"),
		TEL1(SYS.TEL__NULL,"电话1"),
		TEL2(SYS.TEL__NULL,"电话2"),
		FAX(SYS.FAX__NULL),
		WEBSITE(SYS.WEBSITE__NULL),
		ADDR(SYS.ADDR__200_NULL),
		ZIP_CODE(SYS.ZIP__COCE),
		REM(SYS.REM__200_NULL),
		UPDATED_BY(SYS.UPDATED_BY),
		UPDATED_DATE_TIME(SYS.UPDATED_DATE_TIME),
		CREATED_BY(SYS.CREATED_BY),
		CREATED_DATE_TIME(SYS.CREATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
//		(FLDS.),
//		(FLDS.),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_NAME = TB.addIndex("name", true,NAME);//企业客户的名称必须唯一
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
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(),TB.getShortName());
	}
	public static Fld fldOneToOne(String code,String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _name;	// 机构名称  STR(100)
  private String _shortName;	// 机构简称  STR(20)<null>
  private String _tel1;	// 电话1  STR(20)<null>
  private String _tel2;	// 电话2  STR(20)<null>
  private String _fax;	// 传真  STR(20)<null>
  private String _website;	// 网址  STR(200)<null>
  private String _addr;	// 地址  STR(200)<null>
  private String _zipCode;	// 邮编  STR(6)<null>
  private String _rem;	// 备注  STR(200)<null>
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedDateTime;	// 更新时间  TIME
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdDateTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysCom init(){
		super.init();
    _name=null;	// 机构名称  STR(100)
    _shortName=null;	// 机构简称  STR(20)
    _tel1=null;	// 电话1  STR(20)
    _tel2=null;	// 电话2  STR(20)
    _fax=null;	// 传真  STR(20)
    _website=null;	// 网址  STR(200)
    _addr=null;	// 地址  STR(200)
    _zipCode=null;	// 邮编  STR(6)
    _rem=null;	// 备注  STR(200)
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedDateTime=Env.getTranBeginTime();	// 更新时间  TIME
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdDateTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysCom loadUniqueName(boolean lockFlag,String name) {
    return (SysCom)loadUnique(T.IDX_NAME,lockFlag,name);
  }
  public static SysCom chkUniqueName(boolean lockFlag,String name) {
    return (SysCom)chkUnique(T.IDX_NAME,lockFlag,name);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
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
  public String getTel1(){
    return _tel1;
  }
  public void setTel1(String tel1){
    _tel1=tel1;
  }
  public String getTel2(){
    return _tel2;
  }
  public void setTel2(String tel2){
    _tel2=tel2;
  }
  public String getFax(){
    return _fax;
  }
  public void setFax(String fax){
    _fax=fax;
  }
  public String getWebsite(){
    return _website;
  }
  public void setWebsite(String website){
    _website=website;
  }
  public String getAddr(){
    return _addr;
  }
  public void setAddr(String addr){
    _addr=addr;
  }
  public String getZipCode(){
    return _zipCode;
  }
  public void setZipCode(String zipCode){
    _zipCode=zipCode;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
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
  public Date getUpdatedDateTime(){
    return _updatedDateTime;
  }
  public void setUpdatedDateTime(Date updatedDateTime){
    _updatedDateTime=updatedDateTime;
  }
  public Integer getCreatedBy(){
    return _createdBy;
  }
  public void setCreatedBy(Integer createdBy){
    _createdBy=createdBy;
  }
  public SysUser gtCreatedBy(){
    if(getCreatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreatedBy());
  }
  public void stCreatedBy(SysUser createdBy){
    if(createdBy==null)
      setCreatedBy(null);
    else
      setCreatedBy(createdBy.getPkey());
  }
  public Date getCreatedDateTime(){
    return _createdDateTime;
  }
  public void setCreatedDateTime(Date createdDateTime){
    _createdDateTime=createdDateTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
