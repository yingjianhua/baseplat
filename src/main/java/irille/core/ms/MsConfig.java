/**
 * 
 */
package irille.core.ms;

import irille.core.sys.Sys.OYn;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * @author surface1
 * 
 */
public class MsConfig extends BeanInt<MsConfig>{
	public static final Tb TB = new Tb(MsConfig.class, "消息表").addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()), 
		REMIND_SIGN(SYS.YN, "提醒标识"),
		TODO_SIGN(SYS.YN, "待办标识"),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
  private Integer _pkey;	// 编号  INT
  private Byte _remindSign;	// 提醒标识 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _todoSign;	// 待办标识 <OYn>  BYTE
	// YES:1,是
	// NO:0,否

	@Override
  public MsConfig init(){
		super.init();
    _remindSign=OYn.DEFAULT.getLine().getKey();	// 提醒标识 <OYn>  BYTE
    _todoSign=OYn.DEFAULT.getLine().getKey();	// 待办标识 <OYn>  BYTE
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Byte getRemindSign(){
    return _remindSign;
  }
  public void setRemindSign(Byte remindSign){
    _remindSign=remindSign;
  }
  public Boolean gtRemindSign(){
    return byteToBoolean(_remindSign);
  }
  public void stRemindSign(Boolean remindSign){
    _remindSign=booleanToByte(remindSign);
  }
  public Byte getTodoSign(){
    return _todoSign;
  }
  public void setTodoSign(Byte todoSign){
    _todoSign=todoSign;
  }
  public Boolean gtTodoSign(){
    return byteToBoolean(_todoSign);
  }
  public void stTodoSign(Boolean todoSign){
    _todoSign=booleanToByte(todoSign);
  }

}
