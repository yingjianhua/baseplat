/**
 * 
 */
package irille.core.sys;

import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * @author surface1
 * 
 */
public class SysAttemper extends BeanInt<SysAttemper> {
	private static final Log LOG = new Log(SysAttemper.class);

	public static final Tb TB = new Tb(SysAttemper.class, "周期调度").setAutoIncrement();

	public enum OPeriod implements IEnumOpt {//@formatter:off
		EVERYDAY_B(11,"每天日启"),
		EVERYDAY_E(12,"每天日终"),
		WEEK_B(21,"每周几日启"),
		WEEK_E(22,"每周几日终"),
		MONTH_B(51,"每月初日启"),
		MONTH_E(52,"每月末日终"),
		MONTH_DAY_B(61,"每月几日日启"),
		MONTH_DAY_E(62,"每月几日日终"),
		SEASON_B(71,"每季初日启"),
		SEASON_E(72,"每季末日终"),
		YEAR_B(91,"年初日启"),
		YEAR_E(92,"年末日终");
		public static final String NAME="周期";
		public static final OPeriod DEFAULT = EVERYDAY_B; // 定义缺省值
		private EnumLine _line;
		private OPeriod(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OType implements IEnumOptObj<Class>  {//@formatter:off
		ONCE(1,"一次调用",null),
		ORG(2,"分机构调整",SysOrg.class),
		CELL(3,"分核算单元调用",SysCell.class);
		public static final String NAME="调用方式";
		public static final OType DEFAULT = ONCE; // 定义缺省值
		private EnumLine _line;
		private Class _obj;
		private OType(int key, String name,Class obj) {
			_line=new EnumLine(this,key,name);_obj=obj;	}
		public EnumLine getLine(){return _line;	}
		public Class getObj(){return _obj;	}
	}		//@formatter:on
	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()), 
		CLAZZ(SYS.STR__40,"类"),//类必须实现IAttemper接口
		NAME(SYS.NAME__100),
		SORT(SYS.SORT__SHORT),
		ENABLED(SYS.ENABLED),
		TYPE(TB.crt(OType.DEFAULT)),
		PERIOD(TB.crt(OPeriod.DEFAULT)),
		VALUE(SYS.BYTE,"值"),
		SKIPABLE(SYS.YN,"失败是否可跳过"),
		PARA(SYS.STR__40,"参数"),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CLAZZ = TB.addIndex("clazz", true,T.CLAZZ);
		public static final Index IDX_SORT = TB.addIndex("sort", true,T.SORT);
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _clazz;	// 类  STR(40)
  private String _name;	// 名称  STR(100)
  private Short _sort;	// 排序号  SHORT
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _type;	// 调用方式 <OType>  BYTE
	// ONCE:1,一次调用
	// ORG:2,分机构调整
	// CELL:3,分核算单元调用
  private Byte _period;	// 周期 <OPeriod>  BYTE
	// EVERYDAY_B:11,每天日启
	// EVERYDAY_E:12,每天日终
	// WEEK_B:21,每周几日启
	// WEEK_E:22,每周几日终
	// MONTH_B:51,每月初日启
	// MONTH_E:52,每月末日终
	// MONTH_DAY_B:61,每月几日日启
	// MONTH_DAY_E:62,每月几日日终
	// SEASON_B:71,每季初日启
	// SEASON_E:72,每季末日终
	// YEAR_B:91,年初日启
	// YEAR_E:92,年末日终
  private Byte _value;	// 值  BYTE
  private Byte _skipable;	// 失败是否可跳过 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _para;	// 参数  STR(40)
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysAttemper init(){
		super.init();
    _clazz=null;	// 类  STR(40)
    _name=null;	// 名称  STR(100)
    _sort=0;	// 排序号  SHORT
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _type=OType.DEFAULT.getLine().getKey();	// 调用方式 <OType>  BYTE
    _period=OPeriod.DEFAULT.getLine().getKey();	// 周期 <OPeriod>  BYTE
    _value=0;	// 值  BYTE
    _skipable=OYn.DEFAULT.getLine().getKey();	// 失败是否可跳过 <OYn>  BYTE
    _para=null;	// 参数  STR(40)
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysAttemper loadUniqueClazz(boolean lockFlag,String clazz) {
    return (SysAttemper)loadUnique(T.IDX_CLAZZ,lockFlag,clazz);
  }
  public static SysAttemper chkUniqueClazz(boolean lockFlag,String clazz) {
    return (SysAttemper)chkUnique(T.IDX_CLAZZ,lockFlag,clazz);
  }
  public static SysAttemper loadUniqueSort(boolean lockFlag,Short sort) {
    return (SysAttemper)loadUnique(T.IDX_SORT,lockFlag,sort);
  }
  public static SysAttemper chkUniqueSort(boolean lockFlag,Short sort) {
    return (SysAttemper)chkUnique(T.IDX_SORT,lockFlag,sort);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getClazz(){
    return _clazz;
  }
  public void setClazz(String clazz){
    _clazz=clazz;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Short getSort(){
    return _sort;
  }
  public void setSort(Short sort){
    _sort=sort;
  }
  public Byte getEnabled(){
    return _enabled;
  }
  public void setEnabled(Byte enabled){
    _enabled=enabled;
  }
  public Boolean gtEnabled(){
    return byteToBoolean(_enabled);
  }
  public void stEnabled(Boolean enabled){
    _enabled=booleanToByte(enabled);
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
  public Byte getPeriod(){
    return _period;
  }
  public void setPeriod(Byte period){
    _period=period;
  }
  public OPeriod gtPeriod(){
    return (OPeriod)(OPeriod.EVERYDAY_B.getLine().get(_period));
  }
  public void stPeriod(OPeriod period){
    _period=period.getLine().getKey();
  }
  public Byte getValue(){
    return _value;
  }
  public void setValue(Byte value){
    _value=value;
  }
  public Byte getSkipable(){
    return _skipable;
  }
  public void setSkipable(Byte skipable){
    _skipable=skipable;
  }
  public Boolean gtSkipable(){
    return byteToBoolean(_skipable);
  }
  public void stSkipable(Boolean skipable){
    _skipable=booleanToByte(skipable);
  }
  public String getPara(){
    return _para;
  }
  public void setPara(String para){
    _para=para;
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
