/**
 * 
 */
package irille.core.lg;

import irille.core.lg.Lg.OClient;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class LgLogin extends BeanLong<LgLogin> implements IExtName{
	private static final Log LOG = new Log(LgLogin.class);

	public static final Tb TB = new Tb(LgLogin.class, "登录日志").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		LOGIN_TIME(SYS.DATE_TIME__NULL,"登录时间"),
		USER_SYS(SYS.USER_SYS),
		CLIENT(TB.crt(OClient.IOS)),
		IP(SYS.IP__NULL),
		SYSTEM(SYS.STR__40_NULL, "系统",true),
		BROWSER(SYS.STR__40_NULL, "浏览器",true),
		LOGOUT_TIME(SYS.DATE_TIME__NULL,"退出时间", true),
		COUNT_PROC_TIME(SYS.INT,"交易累计处理时间"),
		COUNT_SUCCESS(SYS.INT,"成功交易数"),
		COUNT_FAIL(SYS.INT,"失败交易数"),
		LOAD_SUCCESS(SYS.INT,"成功加载数"),
		LOAD_FAIL(SYS.INT,"失败加载数"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_USER = TB.addIndex("user",
				false,USER_SYS);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
	
	@Override
	public String getExtName() {
		SysUser user = gtUserSys();
	  return user.getLoginName()+" - "+user.getName();
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Date _loginTime;	// 登录时间  TIME<null>
  private Integer _userSys;	// 用户 <表主键:SysUser>  INT
  private Byte _client;	// 客户端类型 <OClient>  BYTE
	// WINDOWS:0,Windows
	// IOS:1,IOS
	// ANDRIOD:2,Andriod
  private String _ip;	// IP地址  STR(30)<null>
  private String _system;	// 系统  STR(40)<null>
  private String _browser;	// 浏览器  STR(40)<null>
  private Date _logoutTime;	// 退出时间  TIME<null>
  private Integer _countProcTime;	// 交易累计处理时间  INT
  private Integer _countSuccess;	// 成功交易数  INT
  private Integer _countFail;	// 失败交易数  INT
  private Integer _loadSuccess;	// 成功加载数  INT
  private Integer _loadFail;	// 失败加载数  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public LgLogin init(){
		super.init();
    _loginTime=null;	// 登录时间  TIME
    _userSys=null;	// 用户 <表主键:SysUser>  INT
    _client=OClient.DEFAULT.getLine().getKey();	// 客户端类型 <OClient>  BYTE
    _ip=null;	// IP地址  STR(30)
    _system=null;	// 系统  STR(40)
    _browser=null;	// 浏览器  STR(40)
    _logoutTime=null;	// 退出时间  TIME
    _countProcTime=0;	// 交易累计处理时间  INT
    _countSuccess=0;	// 成功交易数  INT
    _countFail=0;	// 失败交易数  INT
    _loadSuccess=0;	// 成功加载数  INT
    _loadFail=0;	// 失败加载数  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Date getLoginTime(){
    return _loginTime;
  }
  public void setLoginTime(Date loginTime){
    _loginTime=loginTime;
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
  public Byte getClient(){
    return _client;
  }
  public void setClient(Byte client){
    _client=client;
  }
  public OClient gtClient(){
    return (OClient)(OClient.IOS.getLine().get(_client));
  }
  public void stClient(OClient client){
    _client=client.getLine().getKey();
  }
  public String getIp(){
    return _ip;
  }
  public void setIp(String ip){
    _ip=ip;
  }
  public String getSystem(){
    return _system;
  }
  public void setSystem(String system){
    _system=system;
  }
  public String getBrowser(){
    return _browser;
  }
  public void setBrowser(String browser){
    _browser=browser;
  }
  public Date getLogoutTime(){
    return _logoutTime;
  }
  public void setLogoutTime(Date logoutTime){
    _logoutTime=logoutTime;
  }
  public Integer getCountProcTime(){
    return _countProcTime;
  }
  public void setCountProcTime(Integer countProcTime){
    _countProcTime=countProcTime;
  }
  public Integer getCountSuccess(){
    return _countSuccess;
  }
  public void setCountSuccess(Integer countSuccess){
    _countSuccess=countSuccess;
  }
  public Integer getCountFail(){
    return _countFail;
  }
  public void setCountFail(Integer countFail){
    _countFail=countFail;
  }
  public Integer getLoadSuccess(){
    return _loadSuccess;
  }
  public void setLoadSuccess(Integer loadSuccess){
    _loadSuccess=loadSuccess;
  }
  public Integer getLoadFail(){
    return _loadFail;
  }
  public void setLoadFail(Integer loadFail){
    _loadFail=loadFail;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
