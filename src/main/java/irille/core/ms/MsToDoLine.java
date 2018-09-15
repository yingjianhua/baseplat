/**
 * 
 */
package irille.core.ms;

import irille.core.sys.SysUser;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class MsToDoLine extends BeanLong<MsToDoLine>{
	public static final Tb TB = new Tb(MsToDoLine.class, "消息表").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		DO_LIST(MsToDo.fldOutKey()),
		RECEIVER(SYS.USER_SYS, "接收人"),
		DO_TIME(SYS.DATE_TIME__NULL, "处理时间"),
		SUGGESTION(SYS.STR__100_NULL, "处理意见"),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		LINES(Tb.crtLines(T.DO_LIST, CN_LINES,	T.PKEY))
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
	
	//@formatter:on


  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _toDo;	// 待办事项 <表主键:MsToDo>  LONG
  private Integer _receiver;	// 接收人 <表主键:SysUser>  INT
  private Date _doTime;	// 处理时间  TIME<null>
  private String _suggestion;	// 处理意见  STR(100)<null>

	@Override
  public MsToDoLine init(){
		super.init();
    _toDo=null;	// 待办事项 <表主键:MsToDo>  LONG
    _receiver=null;	// 接收人 <表主键:SysUser>  INT
    _doTime=null;	// 处理时间  TIME
    _suggestion=null;	// 处理意见  STR(100)
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Long getToDo(){
    return _toDo;
  }
  public void setToDo(Long toDo){
    _toDo=toDo;
  }
  public MsToDo gtToDo(){
    if(getToDo()==null)
      return null;
    return (MsToDo)get(MsToDo.class,getToDo());
  }
  public void stToDo(MsToDo toDo){
    if(toDo==null)
      setToDo(null);
    else
      setToDo(toDo.getPkey());
  }
  public Integer getReceiver(){
    return _receiver;
  }
  public void setReceiver(Integer receiver){
    _receiver=receiver;
  }
  public SysUser gtReceiver(){
    if(getReceiver()==null)
      return null;
    return (SysUser)get(SysUser.class,getReceiver());
  }
  public void stReceiver(SysUser receiver){
    if(receiver==null)
      setReceiver(null);
    else
      setReceiver(receiver.getPkey());
  }
  public Date getDoTime(){
    return _doTime;
  }
  public void setDoTime(Date doTime){
    _doTime=doTime;
  }
  public String getSuggestion(){
    return _suggestion;
  }
  public void setSuggestion(String suggestion){
    _suggestion=suggestion;
  }
  public static java.util.List<MsToDoLine> getLines(irille.core.ms.MsToDo mainBean){
    return list(irille.core.ms.MsToDoLine.class,
        " to_do=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<MsToDoLine> getLines(irille.core.ms.MsToDo mainBean, int idx,int count){
    return list(irille.core.ms.MsToDoLine.class,false," to_do=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.core.ms.MsToDo mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM ms_to_do_line WHERE to_do=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }

}
