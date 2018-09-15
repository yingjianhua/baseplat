//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.OPasswordType;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.svr.Env.SysConf;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

public class SysUserLogin extends BeanInt<SysUserLogin> {

	private static final Log LOG = new Log(SysUserLogin.class);
	public static final byte FETCH_SIZE = Env.getConfParaInt(
			SysConf.MAX_PASSWORD_LAW_LESS_COUNT).byteValue();// 密码每天最多尝试次数

	public static final Tb TB = new Tb(SysUserLogin.class, "系统账号管理", "账号").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		USER(SysUser.fldOneToOne()), //自动新建 PKEY字段
		PC_LAST_LOGIN_TIME(SYS.DATE_TIME__NULL,"PC最近一次登录时间"),
		PC_LAST_LOGIN_IP(SYS.IP__NULL,"PC最后一次登录IP"),  //以前的登录信息以后再用专门的表时间记录
		MO_LAST_LOGIN_TIME(SYS.DATE_TIME__NULL,"移动端最近一次登录时间"),
		MO_LAST_LOGIN_IP(SYS.IP__NULL,"移动端最后一次登录IP"),  //以前的登录信息以后再用专门的表时间记录
		PASSWORD_TYPE(TB.crt(Sys.OPasswordType.DEFAULT)),
		PASSWORD(SYS.PASSWORD__NULL),
		LOGIN_CODE(SYS.STR__100_NULL,"登录码-作验证用"),
		LAW_LESS_COUNT(SYS.BYTE,"非法登录次数"),
		LAW_LESS_DATE_TIME(SYS.DATE_TIME__NULL,"上次非法登录时间"),
		COOKIES_KEEP_DAYS(SYS.SHORT,"COOKIES保存天数"),
		CREATE_BY(SYS.CREATED_BY),
		CREATE_DATE(SYS.CREATED_DATE),	
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull)
			{_fld=TB.addOutKey(clazz,this,name,isnull);}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.USER.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Date _pcLastLoginTime;	// PC最近一次登录时间  TIME<null>
  private String _pcLastLoginIp;	// PC最后一次登录IP  STR(30)<null>
  private Date _moLastLoginTime;	// 移动端最近一次登录时间  TIME<null>
  private String _moLastLoginIp;	// 移动端最后一次登录IP  STR(30)<null>
  private Byte _passwordType;	// 密码类型 <OPasswordType>  BYTE
	// PASSWORD:1,密码
	// QQ:2,QQ验证
	// SINA:3,新浪验证
  private String _password;	// 密码  STR(40)<null>
  private String _loginCode;	// 登录码-作验证用  STR(100)<null>
  private Byte _lawLessCount;	// 非法登录次数  BYTE
  private Date _lawLessDateTime;	// 上次非法登录时间  TIME<null>
  private Short _cookiesKeepDays;	// COOKIES保存天数  SHORT
  private Integer _createBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createDate;	// 建档日期  DATE
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysUserLogin init(){
		super.init();
    _pcLastLoginTime=null;	// PC最近一次登录时间  TIME
    _pcLastLoginIp=null;	// PC最后一次登录IP  STR(30)
    _moLastLoginTime=null;	// 移动端最近一次登录时间  TIME
    _moLastLoginIp=null;	// 移动端最后一次登录IP  STR(30)
    _passwordType=OPasswordType.DEFAULT.getLine().getKey();	// 密码类型 <OPasswordType>  BYTE
    _password=null;	// 密码  STR(40)
    _loginCode=null;	// 登录码-作验证用  STR(100)
    _lawLessCount=0;	// 非法登录次数  BYTE
    _lawLessDateTime=null;	// 上次非法登录时间  TIME
    _cookiesKeepDays=0;	// COOKIES保存天数  SHORT
    _createBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createDate=Env.getWorkDate();	// 建档日期  DATE
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
  //取一对一表对象: SysUser
  public SysUser gtUser(){
    return get(SysUser.class,getPkey());
  }
  public void stUser(SysUser user){
      setPkey(user.getPkey());
  }
  public Date getPcLastLoginTime(){
    return _pcLastLoginTime;
  }
  public void setPcLastLoginTime(Date pcLastLoginTime){
    _pcLastLoginTime=pcLastLoginTime;
  }
  public String getPcLastLoginIp(){
    return _pcLastLoginIp;
  }
  public void setPcLastLoginIp(String pcLastLoginIp){
    _pcLastLoginIp=pcLastLoginIp;
  }
  public Date getMoLastLoginTime(){
    return _moLastLoginTime;
  }
  public void setMoLastLoginTime(Date moLastLoginTime){
    _moLastLoginTime=moLastLoginTime;
  }
  public String getMoLastLoginIp(){
    return _moLastLoginIp;
  }
  public void setMoLastLoginIp(String moLastLoginIp){
    _moLastLoginIp=moLastLoginIp;
  }
  public Byte getPasswordType(){
    return _passwordType;
  }
  public void setPasswordType(Byte passwordType){
    _passwordType=passwordType;
  }
  public OPasswordType gtPasswordType(){
    return (OPasswordType)(OPasswordType.PASSWORD.getLine().get(_passwordType));
  }
  public void stPasswordType(OPasswordType passwordType){
    _passwordType=passwordType.getLine().getKey();
  }
  public String getPassword(){
    return _password;
  }
  public void setPassword(String password){
    _password=password;
  }
  public String getLoginCode(){
    return _loginCode;
  }
  public void setLoginCode(String loginCode){
    _loginCode=loginCode;
  }
  public Byte getLawLessCount(){
    return _lawLessCount;
  }
  public void setLawLessCount(Byte lawLessCount){
    _lawLessCount=lawLessCount;
  }
  public Date getLawLessDateTime(){
    return _lawLessDateTime;
  }
  public void setLawLessDateTime(Date lawLessDateTime){
    _lawLessDateTime=lawLessDateTime;
  }
  public Short getCookiesKeepDays(){
    return _cookiesKeepDays;
  }
  public void setCookiesKeepDays(Short cookiesKeepDays){
    _cookiesKeepDays=cookiesKeepDays;
  }
  public Integer getCreateBy(){
    return _createBy;
  }
  public void setCreateBy(Integer createBy){
    _createBy=createBy;
  }
  public SysUser gtCreateBy(){
    if(getCreateBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreateBy());
  }
  public void stCreateBy(SysUser createBy){
    if(createBy==null)
      setCreateBy(null);
    else
      setCreateBy(createBy.getPkey());
  }
  public Date getCreateDate(){
    return _createDate;
  }
  public void setCreateDate(Date createDate){
    _createDate=createDate;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
