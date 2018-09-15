package irille.core.sys;

import irille.core.sys.Sys.OCardPersonType;
import irille.core.sys.Sys.OLinkType;
import irille.core.sys.Sys.OMerry;
import irille.core.sys.Sys.OSex;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

public class SysPersonLink extends BeanInt<SysPersonLink> implements IExtName{
public static final Tb TB = new Tb(SysPersonLink.class, "联系人").setAutoIncrement();

//加类型 收货/发贷/财务/销售
public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		NAME(SYS.NAME__100),
		TB_OBJ_LONG(Tb.crtLongTbObj("tbObjLong", "所属对象")),
		TYPE(Tb.crt(Sys.OLinkType.SAL).setNull()),
		PE(CmbMsgPerson.fldCmb("pe", "个人")),
		OF(CmbMsgOffice.fldCmb("of", "公司")),
		REM(SYS.REM__200_NULL),
		UPDATED_BY(SYS.UPDATED_BY),
		UPDATED_DATE_TIME(SYS.UPDATED_DATE_TIME),
		CREATED_BY(SYS.CREATED_BY),
		CREATED_DATE_TIME(SYS.CREATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PE_CARD_TYPE(TB.get("peCardType")),	//个人证件类型
		PE_CARD_NUMB(TB.get("peCardNumb")),	//个人证件号码
		PE_MOBILE(TB.get("peMobile")),	//个人常用手机
		PE_MOBILE_OTHER(TB.get("peMobileOther")),	//个人其它手机
		PE_EMAIL(TB.get("peEmail")),	//个人邮箱
		PE_WX(TB.get("peWx")),	//个人微信
		PE_QQ(TB.get("peQq")),	//个人QQ
		PE_WB(TB.get("peWb")),	//个人微博
		PE_MSN(TB.get("peMsn")),	//个人MSN
		PE_SEX(TB.get("peSex")),	//个人性别
		PE_BIRTHDAY(TB.get("peBirthday")),	//个人出生日期
		PE_MERRY(TB.get("peMerry")),	//个人婚姻状况
		PE_EDU(TB.get("peEdu")),	//个人学历
		PE_DEGREE(TB.get("peDegree")),	//个人学位
		PE_POSITIONAL_TITLE(TB.get("pePositionalTitle")),	//个人职称
		PE_DRIVING_LICENSE(TB.get("peDrivingLicense")),	//个人驾照类别
		PE_PARTY(TB.get("peParty")),	//个人党派
		PE_BELIEF(TB.get("peBelief")),	//个人信仰
		OF_COMPANY_NAME(TB.get("ofCompanyName")),	//公司名称
		OF_DEPT_NAME(TB.get("ofDeptName")),	//公司所在部门
		OF_POST(TB.get("ofPost")),	//公司职务
		OF_TEL(TB.get("ofTel")),	//公司电话
		OF_FAX(TB.get("ofFax")),	//公司传真
		OF_WEBSITE(TB.get("ofWebsite")),	//公司网址
		OF_ADDR(TB.get("ofAddr")),	//公司地址
		OF_ZIP_CODE(TB.get("ofZipCode")),	//公司邮编
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
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
	
	@Override
	  public String getExtName() {
		  return getName();
	  }
	
//	@Override
//	public String getShortName() { //个人的简称也用Name
//		return getName();
//	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _name;	// 名称  STR(100)
  private Long _tbObjLong;	// 所属对象  LONG
  private Byte _type;	// 类型 <OLinkType>  BYTE<null>
	// SAL:1,收货
	// PUR:2,发货
	// GL:3,财务
  private Byte _peCardType;	// 个人证件类型 <OCardPersonType>  BYTE<null>
	// NONE:0,无
	// ID_CARD:1,身份证
	// PASSPORT:2,护照
	// MILITARY:3,军人证
	// DRIVER:4,驾驶证
  private String _peCardNumb;	// 个人证件号码  STR(20)<null>
  private String _peMobile;	// 个人常用手机  STR(20)<null>
  private String _peMobileOther;	// 个人其它手机  STR(20)<null>
  private String _peEmail;	// 个人邮箱  STR(100)<null>
  private String _peWx;	// 个人微信  STR(100)<null>
  private String _peQq;	// 个人QQ  STR(100)<null>
  private String _peWb;	// 个人微博  STR(100)<null>
  private String _peMsn;	// 个人MSN  STR(100)<null>
  private Byte _peSex;	// 个人性别 <OSex>  BYTE<null>
	// UNKNOW:0,未知
	// MALE:1,男
	// FEMALE:2,女
  private Date _peBirthday;	// 个人出生日期  DATE<null>
  private Byte _peMerry;	// 个人婚姻状况 <OMerry>  BYTE<null>
	// UNKNOW:0,未知
	// SINGLE:1,未婚
	// MARRIED:2,已婚
	// DIVORCED:3,离异
	// WIDOWED:4,丧偶
  private String _peEdu;	// 个人学历  STR(20)<null>
  private String _peDegree;	// 个人学位  STR(20)<null>
  private String _pePositionalTitle;	// 个人职称  STR(20)<null>
  private String _peDrivingLicense;	// 个人驾照类别  STR(20)<null>
  private String _peParty;	// 个人党派  STR(20)<null>
  private String _peBelief;	// 个人信仰  STR(20)<null>
  private String _ofCompanyName;	// 公司名称  STR(100)<null>
  private String _ofDeptName;	// 公司所在部门  STR(100)<null>
  private String _ofPost;	// 公司职务  STR(100)<null>
  private String _ofTel;	// 公司电话  STR(20)<null>
  private String _ofFax;	// 公司传真  STR(20)<null>
  private String _ofWebsite;	// 公司网址  STR(200)<null>
  private String _ofAddr;	// 公司地址  STR(200)<null>
  private String _ofZipCode;	// 公司邮编  STR(6)<null>
  private String _rem;	// 备注  STR(200)<null>
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedDateTime;	// 更新时间  TIME
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdDateTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysPersonLink init(){
		super.init();
    _name=null;	// 名称  STR(100)
    _tbObjLong=null;	// 所属对象  LONG
    _type=OLinkType.DEFAULT.getLine().getKey();	// 类型 <OLinkType>  BYTE
    _peCardType=OCardPersonType.DEFAULT.getLine().getKey();	// 个人证件类型 <OCardPersonType>  BYTE
    _peCardNumb=null;	// 个人证件号码  STR(20)
    _peMobile=null;	// 个人常用手机  STR(20)
    _peMobileOther=null;	// 个人其它手机  STR(20)
    _peEmail=null;	// 个人邮箱  STR(100)
    _peWx=null;	// 个人微信  STR(100)
    _peQq=null;	// 个人QQ  STR(100)
    _peWb=null;	// 个人微博  STR(100)
    _peMsn=null;	// 个人MSN  STR(100)
    _peSex=OSex.DEFAULT.getLine().getKey();	// 个人性别 <OSex>  BYTE
    _peBirthday=null;	// 个人出生日期  DATE
    _peMerry=OMerry.DEFAULT.getLine().getKey();	// 个人婚姻状况 <OMerry>  BYTE
    _peEdu=null;	// 个人学历  STR(20)
    _peDegree=null;	// 个人学位  STR(20)
    _pePositionalTitle=null;	// 个人职称  STR(20)
    _peDrivingLicense=null;	// 个人驾照类别  STR(20)
    _peParty=null;	// 个人党派  STR(20)
    _peBelief=null;	// 个人信仰  STR(20)
    _ofCompanyName=null;	// 公司名称  STR(100)
    _ofDeptName=null;	// 公司所在部门  STR(100)
    _ofPost=null;	// 公司职务  STR(100)
    _ofTel=null;	// 公司电话  STR(20)
    _ofFax=null;	// 公司传真  STR(20)
    _ofWebsite=null;	// 公司网址  STR(200)
    _ofAddr=null;	// 公司地址  STR(200)
    _ofZipCode=null;	// 公司邮编  STR(6)
    _rem=null;	// 备注  STR(200)
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedDateTime=Env.getTranBeginTime();	// 更新时间  TIME
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdDateTime=Env.getTranBeginTime();	// 建档时间  TIME
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Long getTbObjLong(){
    return _tbObjLong;
  }
  public void setTbObjLong(Long tbObjLong){
    _tbObjLong=tbObjLong;
  }
  //外部主键对象: BeanLong
  public Bean gtTbObjLong(){
    return (Bean)gtLongTbObj(getTbObjLong());
  }
  public void stTbObjLong(Bean tbObjLong){
      setTbObjLong(tbObjLong.gtLongPkey());
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OLinkType gtType(){
    return (OLinkType)(OLinkType.SAL.getLine().get(_type));
  }
  public void stType(OLinkType type){
    _type=type.getLine().getKey();
  }
  //组合对象的操作
  public CmbMsgPerson gtPe(){
    CmbMsgPerson b=new CmbMsgPerson();
    	b.setCardType(_peCardType);
    	b.setCardNumb(_peCardNumb);
    	b.setMobile(_peMobile);
    	b.setMobileOther(_peMobileOther);
    	b.setEmail(_peEmail);
    	b.setWx(_peWx);
    	b.setQq(_peQq);
    	b.setWb(_peWb);
    	b.setMsn(_peMsn);
    	b.setSex(_peSex);
    	b.setBirthday(_peBirthday);
    	b.setMerry(_peMerry);
    	b.setEdu(_peEdu);
    	b.setDegree(_peDegree);
    	b.setPositionalTitle(_pePositionalTitle);
    	b.setDrivingLicense(_peDrivingLicense);
    	b.setParty(_peParty);
    	b.setBelief(_peBelief);
    return b;
  }
  public void stPe(CmbMsgPerson pe){
    _peCardType=pe.getCardType();
    _peCardNumb=pe.getCardNumb();
    _peMobile=pe.getMobile();
    _peMobileOther=pe.getMobileOther();
    _peEmail=pe.getEmail();
    _peWx=pe.getWx();
    _peQq=pe.getQq();
    _peWb=pe.getWb();
    _peMsn=pe.getMsn();
    _peSex=pe.getSex();
    _peBirthday=pe.getBirthday();
    _peMerry=pe.getMerry();
    _peEdu=pe.getEdu();
    _peDegree=pe.getDegree();
    _pePositionalTitle=pe.getPositionalTitle();
    _peDrivingLicense=pe.getDrivingLicense();
    _peParty=pe.getParty();
    _peBelief=pe.getBelief();
  }
  public Byte getPeCardType(){
    return _peCardType;
  }
  public void setPeCardType(Byte peCardType){
    _peCardType=peCardType;
  }
  public OCardPersonType gtPeCardType(){
    return (OCardPersonType)(OCardPersonType.NONE.getLine().get(_peCardType));
  }
  public void stPeCardType(OCardPersonType peCardType){
    _peCardType=peCardType.getLine().getKey();
  }
  public String getPeCardNumb(){
    return _peCardNumb;
  }
  public void setPeCardNumb(String peCardNumb){
    _peCardNumb=peCardNumb;
  }
  public String getPeMobile(){
    return _peMobile;
  }
  public void setPeMobile(String peMobile){
    _peMobile=peMobile;
  }
  public String getPeMobileOther(){
    return _peMobileOther;
  }
  public void setPeMobileOther(String peMobileOther){
    _peMobileOther=peMobileOther;
  }
  public String getPeEmail(){
    return _peEmail;
  }
  public void setPeEmail(String peEmail){
    _peEmail=peEmail;
  }
  public String getPeWx(){
    return _peWx;
  }
  public void setPeWx(String peWx){
    _peWx=peWx;
  }
  public String getPeQq(){
    return _peQq;
  }
  public void setPeQq(String peQq){
    _peQq=peQq;
  }
  public String getPeWb(){
    return _peWb;
  }
  public void setPeWb(String peWb){
    _peWb=peWb;
  }
  public String getPeMsn(){
    return _peMsn;
  }
  public void setPeMsn(String peMsn){
    _peMsn=peMsn;
  }
  public Byte getPeSex(){
    return _peSex;
  }
  public void setPeSex(Byte peSex){
    _peSex=peSex;
  }
  public OSex gtPeSex(){
    return (OSex)(OSex.UNKNOW.getLine().get(_peSex));
  }
  public void stPeSex(OSex peSex){
    _peSex=peSex.getLine().getKey();
  }
  public Date getPeBirthday(){
    return _peBirthday;
  }
  public void setPeBirthday(Date peBirthday){
    _peBirthday=peBirthday;
  }
  public Byte getPeMerry(){
    return _peMerry;
  }
  public void setPeMerry(Byte peMerry){
    _peMerry=peMerry;
  }
  public OMerry gtPeMerry(){
    return (OMerry)(OMerry.UNKNOW.getLine().get(_peMerry));
  }
  public void stPeMerry(OMerry peMerry){
    _peMerry=peMerry.getLine().getKey();
  }
  public String getPeEdu(){
    return _peEdu;
  }
  public void setPeEdu(String peEdu){
    _peEdu=peEdu;
  }
  public String getPeDegree(){
    return _peDegree;
  }
  public void setPeDegree(String peDegree){
    _peDegree=peDegree;
  }
  public String getPePositionalTitle(){
    return _pePositionalTitle;
  }
  public void setPePositionalTitle(String pePositionalTitle){
    _pePositionalTitle=pePositionalTitle;
  }
  public String getPeDrivingLicense(){
    return _peDrivingLicense;
  }
  public void setPeDrivingLicense(String peDrivingLicense){
    _peDrivingLicense=peDrivingLicense;
  }
  public String getPeParty(){
    return _peParty;
  }
  public void setPeParty(String peParty){
    _peParty=peParty;
  }
  public String getPeBelief(){
    return _peBelief;
  }
  public void setPeBelief(String peBelief){
    _peBelief=peBelief;
  }
  //组合对象的操作
  public CmbMsgOffice gtOf(){
    CmbMsgOffice b=new CmbMsgOffice();
    	b.setCompanyName(_ofCompanyName);
    	b.setDeptName(_ofDeptName);
    	b.setPost(_ofPost);
    	b.setTel(_ofTel);
    	b.setFax(_ofFax);
    	b.setWebsite(_ofWebsite);
    	b.setAddr(_ofAddr);
    	b.setZipCode(_ofZipCode);
    return b;
  }
  public void stOf(CmbMsgOffice of){
    _ofCompanyName=of.getCompanyName();
    _ofDeptName=of.getDeptName();
    _ofPost=of.getPost();
    _ofTel=of.getTel();
    _ofFax=of.getFax();
    _ofWebsite=of.getWebsite();
    _ofAddr=of.getAddr();
    _ofZipCode=of.getZipCode();
  }
  public String getOfCompanyName(){
    return _ofCompanyName;
  }
  public void setOfCompanyName(String ofCompanyName){
    _ofCompanyName=ofCompanyName;
  }
  public String getOfDeptName(){
    return _ofDeptName;
  }
  public void setOfDeptName(String ofDeptName){
    _ofDeptName=ofDeptName;
  }
  public String getOfPost(){
    return _ofPost;
  }
  public void setOfPost(String ofPost){
    _ofPost=ofPost;
  }
  public String getOfTel(){
    return _ofTel;
  }
  public void setOfTel(String ofTel){
    _ofTel=ofTel;
  }
  public String getOfFax(){
    return _ofFax;
  }
  public void setOfFax(String ofFax){
    _ofFax=ofFax;
  }
  public String getOfWebsite(){
    return _ofWebsite;
  }
  public void setOfWebsite(String ofWebsite){
    _ofWebsite=ofWebsite;
  }
  public String getOfAddr(){
    return _ofAddr;
  }
  public void setOfAddr(String ofAddr){
    _ofAddr=ofAddr;
  }
  public String getOfZipCode(){
    return _ofZipCode;
  }
  public void setOfZipCode(String ofZipCode){
    _ofZipCode=ofZipCode;
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
