package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 
 * @author whx
 * 
 */

public class GlRateType extends BeanInt<GlRateType> implements IExtName {
	private static final Log LOG = new Log(GlRateType.class);
	public static final Tb TB = new Tb(GlRateType.class, "利率类型").setAutoIncrement().addActIUDL();

	public enum OTimeType implements IEnumOpt {//@formatter:off
		SHORT(1,"短期(1年及以内)"),MID(2,"中期(1年以上3年及以内)"),LONG(3,"长期(3年以上)");
		public static final String NAME="期限";
		public static final OTimeType DEFAULT = SHORT; // 定义缺省值
		private EnumLine _line;
		private OTimeType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OType implements IEnumOpt {//@formatter:off
		ZERO(1,"0利率"),GENERAL(2,"通用"),
		INNER_DEPOSIT(11,"内部上存款"),EXT_DEPOSIT(12,"外部存款"),
		INNER_LOAN(21,"内部借款"),EXT_LOAN(22,"外部借入款"),
		BANK_LOAN(23,"银行贷款"),BILL(24,"票据业务"),OTHER(91,"其它");
		public static final String NAME="期限";
		public static final OType DEFAULT = GENERAL; // 定义缺省值
		private EnumLine _line;
		private OType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20),
		NAME(SYS.NAME__100),
		TIME_TYPE(TB.crt(OTimeType.DEFAULT)),
		TYPE(TB.crt(OType.DEFAULT)),
		ORG(SYS.ORG,true), //为空表示集团范围可用
		CREATED_BY(SYS.CREATED_BY),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code",
				true,CODE);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	
	@Override
	public String getExtName() {
	  return getName();
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private Byte _timeType;	// 期限 <OTimeType>  BYTE
	// SHORT:1,短期(1年及以内)
	// MID:2,中期(1年以上3年及以内)
	// LONG:3,长期(3年以上)
  private Byte _type;	// 期限 <OType>  BYTE
	// ZERO:1,0利率
	// GENERAL:2,通用
	// INNER_DEPOSIT:11,内部上存款
	// EXT_DEPOSIT:12,外部存款
	// INNER_LOAN:21,内部借款
	// EXT_LOAN:22,外部借入款
	// BANK_LOAN:23,银行贷款
	// BILL:24,票据业务
	// OTHER:91,其它
  private Integer _org;	// 机构 <表主键:SysOrg>  INT<null>
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlRateType init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _timeType=OTimeType.DEFAULT.getLine().getKey();	// 期限 <OTimeType>  BYTE
    _type=OType.DEFAULT.getLine().getKey();	// 期限 <OType>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlRateType loadUniqueCode(boolean lockFlag,String code) {
    return (GlRateType)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GlRateType chkUniqueCode(boolean lockFlag,String code) {
    return (GlRateType)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Byte getTimeType(){
    return _timeType;
  }
  public void setTimeType(Byte timeType){
    _timeType=timeType;
  }
  public OTimeType gtTimeType(){
    return (OTimeType)(OTimeType.SHORT.getLine().get(_timeType));
  }
  public void stTimeType(OTimeType timeType){
    _timeType=timeType.getLine().getKey();
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OType gtType(){
    return (OType)(OType.GENERAL.getLine().get(_type));
  }
  public void stType(OType type){
    _type=type.getLine().getKey();
  }
  public Integer getOrg(){
    return _org;
  }
  public void setOrg(Integer org){
    _org=org;
  }
  public SysOrg gtOrg(){
    if(getOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrg());
  }
  public void stOrg(SysOrg org){
    if(org==null)
      setOrg(null);
    else
      setOrg(org.getPkey());
  }
  public Integer getCreatedBy(){
    return _createdBy;
  }
  public void setCreatedBy(Integer createdBy){
    _createdBy=createdBy;
  }
  public SysUser gtCreatedBy(){
    if(getCreatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreatedBy());
  }
  public void stCreatedBy(SysUser createdBy){
    if(createdBy==null)
      setCreatedBy(null);
    else
      setCreatedBy(createdBy.getPkey());
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
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
