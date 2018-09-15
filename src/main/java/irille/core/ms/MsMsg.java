/**
 * 
 */
package irille.core.ms;

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
public class MsMsg extends BeanLong<MsMsg>{
	public static final Tb TB = new Tb(MsMsg.class, "消息表").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		SENDER(SYS.USER_SYS, "发件人"),
		TITLE(SYS.STR__100, "标题"),
		CONTENT(SYS.STR__1000, "内容"),
		CREATE_TIME(SYS.CREATED_DATE_TIME),
		DEL(SYS.YN, "删除标识"),
		MODEL_TYPE(SYS.STR__10_NULL, "类型"),
		SRC_CODE(SYS.STR__100_NULL, "来源代码"),
		SRC_NAME(SYS.STR__100_NULL, "来源名称"),
		SRC_PKEY(SYS.LONG, "来源主键" ,true),
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
  private Integer _sender;	// 发件人 <表主键:SysUser>  INT
  private String _title;	// 标题  STR(100)
  private String _content;	// 内容  STR(1000)
  private Date _createTime;	// 建档时间  TIME
  private Byte _del;	// 删除标识 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _modelType;	// 类型  STR(10)<null>
  private String _srcCode;	// 来源代码  STR(100)<null>
  private String _srcName;	// 来源名称  STR(100)<null>
  private Long _srcPkey;	// 来源主键  LONG<null>

	@Override
  public MsMsg init(){
		super.init();
    _sender=null;	// 发件人 <表主键:SysUser>  INT
    _title=null;	// 标题  STR(100)
    _content=null;	// 内容  STR(1000)
    _createTime=Env.getTranBeginTime();	// 建档时间  TIME
    _del=OYn.DEFAULT.getLine().getKey();	// 删除标识 <OYn>  BYTE
    _modelType=null;	// 类型  STR(10)
    _srcCode=null;	// 来源代码  STR(100)
    _srcName=null;	// 来源名称  STR(100)
    _srcPkey=(long)0;	// 来源主键  LONG
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
  public String getContent(){
    return _content;
  }
  public void setContent(String content){
    _content=content;
  }
  public Date getCreateTime(){
    return _createTime;
  }
  public void setCreateTime(Date createTime){
    _createTime=createTime;
  }
  public Byte getDel(){
    return _del;
  }
  public void setDel(Byte del){
    _del=del;
  }
  public Boolean gtDel(){
    return byteToBoolean(_del);
  }
  public void stDel(Boolean del){
    _del=booleanToByte(del);
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

}
