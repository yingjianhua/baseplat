package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;

import java.util.Date;

/**
 * 系统信息表，本表只有一条记录
 * 
 * @author surface1
 * 
 */
public class SysSystem extends BeanInt<SysSystem> {

	private static final Log LOG = new Log(SysSystem.class);
	public static final Tb TB = new Tb(SysSystem.class, "系统信息表", "系统");
	public enum OSysState implements IEnumOpt {//@formatter:off
		DAY_END(1,"日终处理结束"),
		OPENING(2,"营业中"),
		DAY_END_DOING(3,"日终处理中..."),
		;
		public static final String NAME="机构状态";
		public static final OSysState DEFAULT = DAY_END; // 定义缺省值
		private EnumLine _line;
		private OSysState(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	public enum OCtrl implements IEnumOpt {//@formatter:off
		NORMAL(1,"正常操作"),
		QUREY(2,"可查询"),
		DONOT(3,"普通用户不可以操作"),
		;
		public static final String NAME="操作控制";
		public static final OCtrl DEFAULT = NORMAL; // 定义缺省值
		private EnumLine _line;
		private OCtrl(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		PRODUCT_USER_NAME(SYS.NAME__100,"使用单位名称"),
		LICENCE_CODE(SYS.STR__40,"许可证代码"),
		WORK_DATE(SYS.WORK_DATE),
		STATE(TB.crt(OSysState.DAY_END)),
		CTRL(TB.crt(OCtrl.DEFAULT)),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on
	//
	// public String toStringExt() {
	// return getOrgName();
	// }
	//
	// public String toStringExtView() {
	// return "orgName";
	// }

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _productUserName;	// 使用单位名称  STR(100)
  private String _licenceCode;	// 许可证代码  STR(40)
  private Date _workDate;	// 工作日期  DATE
  private Byte _sysState;	// 机构状态 <OSysState>  BYTE
	// DAY_END:1,日终处理结束
	// OPENING:2,营业中
	// DAY_END_DOING:3,日终处理中...
  private Byte _ctrl;	// 操作控制 <OCtrl>  BYTE
	// NORMAL:1,正常操作
	// QUREY:2,可查询
	// DONOT:3,普通用户不可以操作
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysSystem init(){
		super.init();
    _productUserName=null;	// 使用单位名称  STR(100)
    _licenceCode=null;	// 许可证代码  STR(40)
    _workDate=Env.getWorkDate();	// 工作日期  DATE
    _sysState=OSysState.DEFAULT.getLine().getKey();	// 机构状态 <OSysState>  BYTE
    _ctrl=OCtrl.DEFAULT.getLine().getKey();	// 操作控制 <OCtrl>  BYTE
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
  public String getProductUserName(){
    return _productUserName;
  }
  public void setProductUserName(String productUserName){
    _productUserName=productUserName;
  }
  public String getLicenceCode(){
    return _licenceCode;
  }
  public void setLicenceCode(String licenceCode){
    _licenceCode=licenceCode;
  }
  public Date getWorkDate(){
    return _workDate;
  }
  public void setWorkDate(Date workDate){
    _workDate=workDate;
  }
  public Byte getSysState(){
    return _sysState;
  }
  public void setSysState(Byte sysState){
    _sysState=sysState;
  }
  public OSysState gtSysState(){
    return (OSysState)(OSysState.DAY_END.getLine().get(_sysState));
  }
  public void stSysState(OSysState sysState){
    _sysState=sysState.getLine().getKey();
  }
  public Byte getCtrl(){
    return _ctrl;
  }
  public void setCtrl(Byte ctrl){
    _ctrl=ctrl;
  }
  public OCtrl gtCtrl(){
    return (OCtrl)(OCtrl.NORMAL.getLine().get(_ctrl));
  }
  public void stCtrl(OCtrl ctrl){
    _ctrl=ctrl.getLine().getKey();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
