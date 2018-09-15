package irille.gl.gs;

import irille.core.sys.SysUser;
import irille.gl.gs.Gs.OType;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 属于子货物类别时，如果没有设置存货科目别名，则默认设置为上级类别的别名 规则为"@上级别名"
 * TODO 作价格上下限比例控制
 * 
 * //表结构作如下修改
 * 类别：标准产品、自有产品、服务、人工
 * 组合字段：零售标志
 * 组合字段：可视范围
 * 启用标志
 * @author whx
 */

public class GsGoodsKind extends BeanInt<GsGoodsKind> implements IExtName {
	private static final Log LOG = new Log(GsGoodsKind.class);
	public static final Tb TB = new Tb(GsGoodsKind.class, "货物类别").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__40), //可以多级定义，用“."分隔
		PARENT(GsGoodsKind.fldOutKey().setName("上级类别").setNull()),
		TYPE(TB.crt(Gs.OType.COMMODITY)),
		NAME(SYS.NAME__100),
		SHORTKEY(SYS.SHORTKEY__NULL),
		CUST(TB.crtDime(SYS.NAME__40_NULL, new int[] { 1,2,3,4,5}, "属性名称1", "属性名称2", "属性名称3", "属性名称4", "属性名称5")), 
		SUBJECT_ALIAS(SYS.STR__20_NULL,"存货科目别名"), //其他科目会自动取关联的科目，为空表示取上级分类的“存货科目别名”
		UPDATEBY(SYS.UPDATED_BY),
		UPDATED_TIME(SYS.UPDATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		CUST1(TB.get("cust1")),	//属性名称1
		CUST2(TB.get("cust2")),	//属性名称2
		CUST3(TB.get("cust3")),	//属性名称3
		CUST4(TB.get("cust4")),	//属性名称4
		CUST5(TB.get("cust5")),	//属性名称5
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_CODE = TB.addIndex("code", true, CODE);
//		public static final Index IDX_SHORTKEY = TB.addIndex("shortkey", false,SHORTKEY);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.SUBJECT_ALIAS._fld.setHelp("为空表示取上级分类的\"存货科目别名\"");
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return getName();
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private Integer _parent;	// 上级类别 <表主键:GsGoodsKind>  INT<null>
  private Byte _type;	// 类型 <OType>  BYTE
	// COMMODITY:1,标准产品
	// RAW_MATERIAL:11,自有产品
	// SERVICE:12,服务
	// WORK:99,人工
  private String _name;	// 名称  STR(100)
  private String _shortkey;	// 快捷键  STR(40)<null>
  private String _cust1;	// 属性名称1  STR(40)<null>
  private String _cust2;	// 属性名称2  STR(40)<null>
  private String _cust3;	// 属性名称3  STR(40)<null>
  private String _cust4;	// 属性名称4  STR(40)<null>
  private String _cust5;	// 属性名称5  STR(40)<null>
  private String _subjectAlias;	// 存货科目别名  STR(20)<null>
  private Integer _updateby;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsGoodsKind init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _parent=null;	// 上级类别 <表主键:GsGoodsKind>  INT
    _type=OType.DEFAULT.getLine().getKey();	// 类型 <OType>  BYTE
    _name=null;	// 名称  STR(100)
    _shortkey=null;	// 快捷键  STR(40)
    _cust1=null;	// 属性名称1  STR(40)
    _cust2=null;	// 属性名称2  STR(40)
    _cust3=null;	// 属性名称3  STR(40)
    _cust4=null;	// 属性名称4  STR(40)
    _cust5=null;	// 属性名称5  STR(40)
    _subjectAlias=null;	// 存货科目别名  STR(20)
    _updateby=null;	// 更新员 <表主键:SysUser>  INT
    _updatedTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsGoodsKind loadUniqueCode(boolean lockFlag,String code) {
    return (GsGoodsKind)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GsGoodsKind chkUniqueCode(boolean lockFlag,String code) {
    return (GsGoodsKind)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Integer getParent(){
    return _parent;
  }
  public void setParent(Integer parent){
    _parent=parent;
  }
  public GsGoodsKind gtParent(){
    if(getParent()==null)
      return null;
    return (GsGoodsKind)get(GsGoodsKind.class,getParent());
  }
  public void stParent(GsGoodsKind parent){
    if(parent==null)
      setParent(null);
    else
      setParent(parent.getPkey());
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OType gtType(){
    return (OType)(OType.COMMODITY.getLine().get(_type));
  }
  public void stType(OType type){
    _type=type.getLine().getKey();
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getShortkey(){
    return _shortkey;
  }
  public void setShortkey(String shortkey){
    _shortkey=shortkey;
  }
  //数组对象: String
  public String gtCust(int i){
    switch(i) {
    case 1:
    	return getCust1();
    case 2:
    	return getCust2();
    case 3:
    	return getCust3();
    case 4:
    	return getCust4();
    case 5:
    	return getCust5();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stCust( int i, String cust){
    switch(i) {
    case 1:
    	setCust1(cust);
    	return;
    case 2:
    	setCust2(cust);
    	return;
    case 3:
    	setCust3(cust);
    	return;
    case 4:
    	setCust4(cust);
    	return;
    case 5:
    	setCust5(cust);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public String getCust1(){
    return _cust1;
  }
  public void setCust1(String cust1){
    _cust1=cust1;
  }
  public String getCust2(){
    return _cust2;
  }
  public void setCust2(String cust2){
    _cust2=cust2;
  }
  public String getCust3(){
    return _cust3;
  }
  public void setCust3(String cust3){
    _cust3=cust3;
  }
  public String getCust4(){
    return _cust4;
  }
  public void setCust4(String cust4){
    _cust4=cust4;
  }
  public String getCust5(){
    return _cust5;
  }
  public void setCust5(String cust5){
    _cust5=cust5;
  }
  public String getSubjectAlias(){
    return _subjectAlias;
  }
  public void setSubjectAlias(String subjectAlias){
    _subjectAlias=subjectAlias;
  }
  public Integer getUpdateby(){
    return _updateby;
  }
  public void setUpdateby(Integer updateby){
    _updateby=updateby;
  }
  public SysUser gtUpdateby(){
    if(getUpdateby()==null)
      return null;
    return (SysUser)get(SysUser.class,getUpdateby());
  }
  public void stUpdateby(SysUser updateby){
    if(updateby==null)
      setUpdateby(null);
    else
      setUpdateby(updateby.getPkey());
  }
  public Date getUpdatedTime(){
    return _updatedTime;
  }
  public void setUpdatedTime(Date updatedTime){
    _updatedTime=updatedTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
