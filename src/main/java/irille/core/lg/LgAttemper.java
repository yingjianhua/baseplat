/**
 * 
 */
package irille.core.lg;

import irille.core.sys.SysAttemper;
import irille.core.sys.SysAttemper.OType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class LgAttemper extends BeanInt<LgAttemper> {
	private static final Log LOG = new Log(LgAttemper.class);

	public static final Tb TB = new Tb(LgAttemper.class, "周期调度日志");
	public enum OResult implements IEnumOpt {//@formatter:off
		SUCCESS(1,"一次成功"),
		FAIL(2,"失败"),
		SUCCESS2(3,"若干次成功");
		public static final String NAME="处理结果";
		public static final OResult DEFAULT = SUCCESS; // 定义缺省值
		private EnumLine _line;
		private OResult(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()), 
		DATE(SYS.UPDATED_DATE,"交易日期"),
		ATTEMPER(TB.crtOutKey(SysAttemper.class, "attemper", "调度功能")),
		OBJ(TB.crtOptAndKey("obj", "对象",SysAttemper.OType.DEFAULT)),
		RESULT(TB.crt(OResult.DEFAULT)),
		DATE_TIME(SYS.DATE_TIME,"开始时间"),
		TIMES(SYS.INT,"用时(秒)"),
		REM(SYS.REM__200_NULL),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		TYPE(TB.get("type")),	//调用方式
		OBJ_PKEY(TB.get("objPkey")),	//对象主键值
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_IDX1 = TB.addIndex("idx1",
				true,DATE,ATTEMPER,TYPE,OBJ_PKEY);
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
		T.OBJ_PKEY._fld.setNull();
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Date _date;	// 交易日期  DATE
  private Integer _attemper;	// 调度功能 <表主键:SysAttemper>  INT
  private Byte _type;	// 调用方式 <OType>  BYTE
	// ONCE:1,一次调用
	// ORG:2,分机构调整
	// CELL:3,分核算单元调用
  private Integer _objPkey;	// 对象主键值  INT<null>
  private Byte _result;	// 处理结果 <OResult>  BYTE
	// SUCCESS:1,一次成功
	// FAIL:2,失败
	// SUCCESS2:3,若干次成功
  private Date _dateTime;	// 开始时间  TIME
  private Integer _times;	// 用时(秒)  INT
  private String _rem;	// 备注  STR(200)<null>

	@Override
  public LgAttemper init(){
		super.init();
    _date=Env.getWorkDate();	// 交易日期  DATE
    _attemper=null;	// 调度功能 <表主键:SysAttemper>  INT
    _type=OType.DEFAULT.getLine().getKey();	// 调用方式 <OType>  BYTE
    _objPkey=null;	// 对象主键值  INT
    _result=OResult.DEFAULT.getLine().getKey();	// 处理结果 <OResult>  BYTE
    _dateTime=Env.getTranBeginTime();	// 开始时间  TIME
    _times=0;	// 用时(秒)  INT
    _rem=null;	// 备注  STR(200)
    return this;
  }

  //方法----------------------------------------------
  public static LgAttemper loadUniqueIdx1(boolean lockFlag,Date date,Integer attemper,Byte type,Integer objPkey) {
    return (LgAttemper)loadUnique(T.IDX_IDX1,lockFlag,date,attemper,type,objPkey);
  }
  public static LgAttemper chkUniqueIdx1(boolean lockFlag,Date date,Integer attemper,Byte type,Integer objPkey) {
    return (LgAttemper)chkUnique(T.IDX_IDX1,lockFlag,date,attemper,type,objPkey);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Date getDate(){
    return _date;
  }
  public void setDate(Date date){
    _date=date;
  }
  public Integer getAttemper(){
    return _attemper;
  }
  public void setAttemper(Integer attemper){
    _attemper=attemper;
  }
  public SysAttemper gtAttemper(){
    if(getAttemper()==null)
      return null;
    return (SysAttemper)get(SysAttemper.class,getAttemper());
  }
  public void stAttemper(SysAttemper attemper){
    if(attemper==null)
      setAttemper(null);
    else
      setAttemper(attemper.getPkey());
  }
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtObj(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtType();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_objPkey);
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OType gtType(){
    return (OType)(OType.ONCE.getLine().get(_type));
  }
  public void stType(OType type){
    _type=type.getLine().getKey();
  }
  public Integer getObjPkey(){
    return _objPkey;
  }
  public void setObjPkey(Integer objPkey){
    _objPkey=objPkey;
  }
  public Byte getResult(){
    return _result;
  }
  public void setResult(Byte result){
    _result=result;
  }
  public OResult gtResult(){
    return (OResult)(OResult.SUCCESS.getLine().get(_result));
  }
  public void stResult(OResult result){
    _result=result.getLine().getKey();
  }
  public Date getDateTime(){
    return _dateTime;
  }
  public void setDateTime(Date dateTime){
    _dateTime=dateTime;
  }
  public Integer getTimes(){
    return _times;
  }
  public void setTimes(Integer times){
    _times=times;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
