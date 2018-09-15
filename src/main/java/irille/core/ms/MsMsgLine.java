/**
 * 
 */
package irille.core.ms;

import irille.core.sys.SysUser;
import irille.core.sys.Sys.OYn;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * @author surface1
 * 
 */
public class MsMsgLine extends BeanLong<MsMsgLine>{
	public static final Tb TB = new Tb(MsMsgLine.class, "消息明细").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()), 
		MSG(MsMsg.fldOutKey()),
		READ_TIME(SYS.DATE_TIME__NULL, "阅读时间"),
		DEL(SYS.YN, "删除标识"),
		RECEIVER(SYS.USER_SYS, "收件人"),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		LINES(Tb.crtLines(T.MSG, CN_LINES,T.PKEY)),
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
  private Long _msg;	// 消息表 <表主键:MsMsg>  LONG
  private Date _readTime;	// 阅读时间  TIME<null>
  private Byte _del;	// 删除标识 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Integer _receiver;	// 收件人 <表主键:SysUser>  INT

	@Override
  public MsMsgLine init(){
		super.init();
    _msg=null;	// 消息表 <表主键:MsMsg>  LONG
    _readTime=null;	// 阅读时间  TIME
    _del=OYn.DEFAULT.getLine().getKey();	// 删除标识 <OYn>  BYTE
    _receiver=null;	// 收件人 <表主键:SysUser>  INT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Long getMsg(){
    return _msg;
  }
  public void setMsg(Long msg){
    _msg=msg;
  }
  public MsMsg gtMsg(){
    if(getMsg()==null)
      return null;
    return (MsMsg)get(MsMsg.class,getMsg());
  }
  public void stMsg(MsMsg msg){
    if(msg==null)
      setMsg(null);
    else
      setMsg(msg.getPkey());
  }
  public Date getReadTime(){
    return _readTime;
  }
  public void setReadTime(Date readTime){
    _readTime=readTime;
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
  public static java.util.List<MsMsgLine> getLines(irille.core.ms.MsMsg mainBean){
    return list(irille.core.ms.MsMsgLine.class,
        " msg=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<MsMsgLine> getLines(irille.core.ms.MsMsg mainBean, int idx,int count){
    return list(irille.core.ms.MsMsgLine.class,false," msg=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.core.ms.MsMsg mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM ms_msg_line WHERE msg=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }

}
