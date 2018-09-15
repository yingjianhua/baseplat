/**
 * 
 */
package irille.core.ms;

import irille.core.ms.Ms.ODoType;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OYn;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class MsToDo extends BeanLong<MsToDo>{
	public static final Tb TB = new Tb(MsToDo.class, "待办事项").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		SENDER(SYS.USER_SYS, "发起人"),
		TITLE(SYS.STR__100, "标题"),
		CREATE_TIME(SYS.CREATED_DATE_TIME,"发起时间"),
		DO_TIME(SYS.DATE_TIME__NULL,"处理时间"),
		DEL_REC(SYS.YN,"删除标识(收)"),
		DEL_SEND(SYS.YN,"删除标识(发)"),
		MODEL_TYPE(SYS.STR__10_NULL, "类型"),
		SRC_CODE(SYS.STR__100_NULL, "来源代码"),
		SRC_NAME(SYS.STR__100_NULL, "来源名称"),
		SRC_PKEY(SYS.LONG, "来源主键" ,true),
		DO_TYPE(TB.crt(Ms.ODoType.DEFAULT)),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
    ;
		// 索引
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
	
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = TB.crtOutKey(TB,code,name);
		fld.setType(null);
		return fld;
	}
	//@formatter:on


  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _sender;	// 发起人 <表主键:SysUser>  INT
  private String _title;	// 标题  STR(100)
  private Date _createTime;	// 发起时间  TIME
  private Date _doTime;	// 处理时间  TIME<null>
  private Byte _delRec;	// 删除标识(收) <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _delSend;	// 删除标识(发) <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _modelType;	// 类型  STR(10)<null>
  private String _srcCode;	// 来源代码  STR(100)<null>
  private String _srcName;	// 来源名称  STR(100)<null>
  private Long _srcPkey;	// 来源主键  LONG<null>
  private Byte _doType;	// 处理类型 <ODoType>  BYTE
	// COO:1,协作执行
	// COM:2,竞争执行

	@Override
  public MsToDo init(){
		super.init();
    _sender=null;	// 发起人 <表主键:SysUser>  INT
    _title=null;	// 标题  STR(100)
    _createTime=Env.getTranBeginTime();	// 发起时间  TIME
    _doTime=null;	// 处理时间  TIME
    _delRec=OYn.DEFAULT.getLine().getKey();	// 删除标识(收) <OYn>  BYTE
    _delSend=OYn.DEFAULT.getLine().getKey();	// 删除标识(发) <OYn>  BYTE
    _modelType=null;	// 类型  STR(10)
    _srcCode=null;	// 来源代码  STR(100)
    _srcName=null;	// 来源名称  STR(100)
    _srcPkey=(long)0;	// 来源主键  LONG
    _doType=ODoType.DEFAULT.getLine().getKey();	// 处理类型 <ODoType>  BYTE
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getSender(){
    return _sender;
  }
  public void setSender(Integer sender){
    _sender=sender;
  }
  public SysUser gtSender(){
    if(getSender()==null)
      return null;
    return (SysUser)get(SysUser.class,getSender());
  }
  public void stSender(SysUser sender){
    if(sender==null)
      setSender(null);
    else
      setSender(sender.getPkey());
  }
  public String getTitle(){
    return _title;
  }
  public void setTitle(String title){
    _title=title;
  }
  public Date getCreateTime(){
    return _createTime;
  }
  public void setCreateTime(Date createTime){
    _createTime=createTime;
  }
  public Date getDoTime(){
    return _doTime;
  }
  public void setDoTime(Date doTime){
    _doTime=doTime;
  }
  public Byte getDelRec(){
    return _delRec;
  }
  public void setDelRec(Byte delRec){
    _delRec=delRec;
  }
  public Boolean gtDelRec(){
    return byteToBoolean(_delRec);
  }
  public void stDelRec(Boolean delRec){
    _delRec=booleanToByte(delRec);
  }
  public Byte getDelSend(){
    return _delSend;
  }
  public void setDelSend(Byte delSend){
    _delSend=delSend;
  }
  public Boolean gtDelSend(){
    return byteToBoolean(_delSend);
  }
  public void stDelSend(Boolean delSend){
    _delSend=booleanToByte(delSend);
  }
  public String getModelType(){
    return _modelType;
  }
  public void setModelType(String modelType){
    _modelType=modelType;
  }
  public String getSrcCode(){
    return _srcCode;
  }
  public void setSrcCode(String srcCode){
    _srcCode=srcCode;
  }
  public String getSrcName(){
    return _srcName;
  }
  public void setSrcName(String srcName){
    _srcName=srcName;
  }
  public Long getSrcPkey(){
    return _srcPkey;
  }
  public void setSrcPkey(Long srcPkey){
    _srcPkey=srcPkey;
  }
  public Byte getDoType(){
    return _doType;
  }
  public void setDoType(Byte doType){
    _doType=doType;
  }
  public ODoType gtDoType(){
    return (ODoType)(ODoType.COO.getLine().get(_doType));
  }
  public void stDoType(ODoType doType){
    _doType=doType.getLine().getKey();
  }

}
