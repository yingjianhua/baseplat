/**
 * 
 */
package irille.core.lg;

import irille.core.sys.SysTable;
import irille.core.sys.SysTableAct;
import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class LgTran extends BeanLong<LgTran> {
	private static final Log LOG = new Log(LgTran.class);

	public static final Tb TB = new Tb(LgTran.class, "交易日志").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		LOGIN(TB.crtOutKey(LgLogin.class, "login", "登录记录")),
		B_TIME(SYS.DATE_TIME,"开始时间"),
		ACT(SYS.ACT, true),
		PROC_TIME(SYS.INT,"处理用时"),
		SUCCESS_FLAG(SYS.YN,"成功标志"),
		OBJ(TB.crtTbAndKey("obj", "处理对象")),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		OBJ_TABLE(TB.get("objTable")),	//处理对象
		OBJ_PKEY(TB.get("objPkey")),	//处理对象主键值
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		static {TB.get("objTable").setNull();TB.get("objPkey").setNull();}
		// 索引
		public static final Index IDX_IDX1 = TB.addIndex("idx1",
				false,LOGIN,B_TIME);
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
		T.OBJ_TABLE._fld.setNull();
		T.OBJ_PKEY._fld.setNull();
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _login;	// 登录记录 <表主键:LgLogin>  LONG
  private Date _bTime;	// 开始时间  TIME
  private Integer _act;	// 功能 <表主键:SysTableAct>  INT<null>
  private Integer _procTime;	// 处理用时  INT
  private Byte _successFlag;	// 成功标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Integer _objTable;	// 处理对象 <表主键:SysTable>  INT<null>
  private Integer _objPkey;	// 处理对象主键值  INT<null>
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public LgTran init(){
		super.init();
    _login=null;	// 登录记录 <表主键:LgLogin>  LONG
    _bTime=Env.getTranBeginTime();	// 开始时间  TIME
    _act=null;	// 功能 <表主键:SysTableAct>  INT
    _procTime=0;	// 处理用时  INT
    _successFlag=OYn.DEFAULT.getLine().getKey();	// 成功标志 <OYn>  BYTE
    _objTable=null;	// 处理对象 <表主键:SysTable>  INT
    _objPkey=null;	// 处理对象主键值  INT
    _rem=null;	// 备注  STR(200)
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
  public Long getLogin(){
    return _login;
  }
  public void setLogin(Long login){
    _login=login;
  }
  public LgLogin gtLogin(){
    if(getLogin()==null)
      return null;
    return (LgLogin)get(LgLogin.class,getLogin());
  }
  public void stLogin(LgLogin login){
    if(login==null)
      setLogin(null);
    else
      setLogin(login.getPkey());
  }
  public Date getBTime(){
    return _bTime;
  }
  public void setBTime(Date bTime){
    _bTime=bTime;
  }
  public Integer getAct(){
    return _act;
  }
  public void setAct(Integer act){
    _act=act;
  }
  public SysTableAct gtAct(){
    if(getAct()==null)
      return null;
    return (SysTableAct)get(SysTableAct.class,getAct());
  }
  public void stAct(SysTableAct act){
    if(act==null)
      setAct(null);
    else
      setAct(act.getPkey());
  }
  public Integer getProcTime(){
    return _procTime;
  }
  public void setProcTime(Integer procTime){
    _procTime=procTime;
  }
  public Byte getSuccessFlag(){
    return _successFlag;
  }
  public void setSuccessFlag(Byte successFlag){
    _successFlag=successFlag;
  }
  public Boolean gtSuccessFlag(){
    return byteToBoolean(_successFlag);
  }
  public void stSuccessFlag(Boolean successFlag){
    _successFlag=booleanToByte(successFlag);
  }
  //根据表字段及主键字段的值取对象
  public Bean gtObj(){
    if(getObjPkey()==null)
    	return null;
    return gtTbAndPkeyObj(getObjTable(),getObjPkey());
  }
  public void stObj(Bean obj){
    setObjPkey((Integer)obj.pkeyValues()[0]);
    setObjTable(obj.gtTbId());
  }
  public Integer getObjTable(){
    return _objTable;
  }
  public void setObjTable(Integer objTable){
    _objTable=objTable;
  }
  public SysTable gtObjTable(){
    if(getObjTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getObjTable());
  }
  public void stObjTable(SysTable objTable){
    if(objTable==null)
      setObjTable(null);
    else
      setObjTable(objTable.getPkey());
  }
  public Integer getObjPkey(){
    return _objPkey;
  }
  public void setObjPkey(Integer objPkey){
    _objPkey=objPkey;
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
