package irille.core.sys;

import irille.core.sys.Sys.OCardPersonType;
import irille.core.sys.Sys.OMerry;
import irille.core.sys.Sys.OSex;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.util.Date;

/**
 * 个人信息
 * 
 * @author whx
 */
public class CmbMsgPerson extends BeanInt<CmbMsgPerson> {
	public static final Tb TB = new TbCmb(CmbMsgPerson.class, "个人常用信息");

	public enum T implements IEnumFld {//@formatter:off
		CARD_TYPE(SYS.CARD_TYPE__PERSON, true),
		CARD_NUMB(SYS.CARD_NUMB__NULL),
		MOBILE(SYS.MOBILE__NULL,"常用手机"),
		MOBILE_OTHER(SYS.MOBILE__NULL,"其它手机"),
		EMAIL(SYS.EMAIL__NULL),
		WX(SYS.WX),
		QQ(SYS.QQ),
		WB(SYS.WB),
		MSN(SYS.MSN),
		SEX(SYS.SEX, true),
		BIRTHDAY(SYS.BIRTHDAY_NULL),
		MERRY(SYS.MERRY, true),
		EDU(SYS.EDU__NULL),
		DEGREE(SYS.DEGREE__NULL),
		POSITIONAL_TITLE(SYS.POSITIONAL_TITLE__NULL),
		DRIVING_LICENSE(SYS.DRIVING_LICENSE__NULL),
		PARTY(SYS.PARTY__NULL),
		BELIEF(SYS.BELIEF__NULL),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		T.EMAIL.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	//@formatter:on
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Byte _cardType;	// 证件类型 <OCardPersonType>  BYTE<null>
	// NONE:0,无
	// ID_CARD:1,身份证
	// PASSPORT:2,护照
	// MILITARY:3,军人证
	// DRIVER:4,驾驶证
  private String _cardNumb;	// 证件号码  STR(20)<null>
  private String _mobile;	// 常用手机  STR(20)<null>
  private String _mobileOther;	// 其它手机  STR(20)<null>
  private String _email;	// 邮箱  STR(100)<null>
  private String _wx;	// 微信  STR(100)<null>
  private String _qq;	// QQ  STR(100)<null>
  private String _wb;	// 微博  STR(100)<null>
  private String _msn;	// MSN  STR(100)<null>
  private Byte _sex;	// 性别 <OSex>  BYTE<null>
	// UNKNOW:0,未知
	// MALE:1,男
	// FEMALE:2,女
  private Date _birthday;	// 出生日期  DATE<null>
  private Byte _merry;	// 婚姻状况 <OMerry>  BYTE<null>
	// UNKNOW:0,未知
	// SINGLE:1,未婚
	// MARRIED:2,已婚
	// DIVORCED:3,离异
	// WIDOWED:4,丧偶
  private String _edu;	// 学历  STR(20)<null>
  private String _degree;	// 学位  STR(20)<null>
  private String _positionalTitle;	// 职称  STR(20)<null>
  private String _drivingLicense;	// 驾照类别  STR(20)<null>
  private String _party;	// 党派  STR(20)<null>
  private String _belief;	// 信仰  STR(20)<null>

	@Override
  public CmbMsgPerson init(){
		super.init();
    _cardType=OCardPersonType.DEFAULT.getLine().getKey();	// 证件类型 <OCardPersonType>  BYTE
    _cardNumb=null;	// 证件号码  STR(20)
    _mobile=null;	// 常用手机  STR(20)
    _mobileOther=null;	// 其它手机  STR(20)
    _email=null;	// 邮箱  STR(100)
    _wx=null;	// 微信  STR(100)
    _qq=null;	// QQ  STR(100)
    _wb=null;	// 微博  STR(100)
    _msn=null;	// MSN  STR(100)
    _sex=OSex.DEFAULT.getLine().getKey();	// 性别 <OSex>  BYTE
    _birthday=null;	// 出生日期  DATE
    _merry=OMerry.DEFAULT.getLine().getKey();	// 婚姻状况 <OMerry>  BYTE
    _edu=null;	// 学历  STR(20)
    _degree=null;	// 学位  STR(20)
    _positionalTitle=null;	// 职称  STR(20)
    _drivingLicense=null;	// 驾照类别  STR(20)
    _party=null;	// 党派  STR(20)
    _belief=null;	// 信仰  STR(20)
    return this;
  }

  //方法----------------------------------------------
  public Byte getCardType(){
    return _cardType;
  }
  public void setCardType(Byte cardType){
    _cardType=cardType;
  }
  public OCardPersonType gtCardType(){
    return (OCardPersonType)(OCardPersonType.NONE.getLine().get(_cardType));
  }
  public void stCardType(OCardPersonType cardType){
    _cardType=cardType.getLine().getKey();
  }
  public String getCardNumb(){
    return _cardNumb;
  }
  public void setCardNumb(String cardNumb){
    _cardNumb=cardNumb;
  }
  public String getMobile(){
    return _mobile;
  }
  public void setMobile(String mobile){
    _mobile=mobile;
  }
  public String getMobileOther(){
    return _mobileOther;
  }
  public void setMobileOther(String mobileOther){
    _mobileOther=mobileOther;
  }
  public String getEmail(){
    return _email;
  }
  public void setEmail(String email){
    _email=email;
  }
  public String getWx(){
    return _wx;
  }
  public void setWx(String wx){
    _wx=wx;
  }
  public String getQq(){
    return _qq;
  }
  public void setQq(String qq){
    _qq=qq;
  }
  public String getWb(){
    return _wb;
  }
  public void setWb(String wb){
    _wb=wb;
  }
  public String getMsn(){
    return _msn;
  }
  public void setMsn(String msn){
    _msn=msn;
  }
  public Byte getSex(){
    return _sex;
  }
  public void setSex(Byte sex){
    _sex=sex;
  }
  public OSex gtSex(){
    return (OSex)(OSex.UNKNOW.getLine().get(_sex));
  }
  public void stSex(OSex sex){
    _sex=sex.getLine().getKey();
  }
  public Date getBirthday(){
    return _birthday;
  }
  public void setBirthday(Date birthday){
    _birthday=birthday;
  }
  public Byte getMerry(){
    return _merry;
  }
  public void setMerry(Byte merry){
    _merry=merry;
  }
  public OMerry gtMerry(){
    return (OMerry)(OMerry.UNKNOW.getLine().get(_merry));
  }
  public void stMerry(OMerry merry){
    _merry=merry.getLine().getKey();
  }
  public String getEdu(){
    return _edu;
  }
  public void setEdu(String edu){
    _edu=edu;
  }
  public String getDegree(){
    return _degree;
  }
  public void setDegree(String degree){
    _degree=degree;
  }
  public String getPositionalTitle(){
    return _positionalTitle;
  }
  public void setPositionalTitle(String positionalTitle){
    _positionalTitle=positionalTitle;
  }
  public String getDrivingLicense(){
    return _drivingLicense;
  }
  public void setDrivingLicense(String drivingLicense){
    _drivingLicense=drivingLicense;
  }
  public String getParty(){
    return _party;
  }
  public void setParty(String party){
    _party=party;
  }
  public String getBelief(){
    return _belief;
  }
  public void setBelief(String belief){
    _belief=belief;
  }

}
