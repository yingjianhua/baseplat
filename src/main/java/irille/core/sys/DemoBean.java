package irille.core.sys;

import irille.core.sys.CmbBeginEndDate.ICmbBeginEndDate;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OUserType;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 账套信息
 * 
 * @author whx
 */
public class DemoBean extends BeanLong<DemoBean> implements ICmbBeginEndDate{
	

	private static final Log LOG = new Log(DemoBean.class);

	public static final Tb TB = new Tb(DemoBean.class, "测试").setAutoIncrement()
			.addActIUDL();

	public enum T implements IEnumFld {
		//@formatter:off
		OUTKEY(SysCom.fldOneToOne()),
		NAME(SYS.NAME__100),
		CODE(SYS.CODE__20),
		SHORT_NAME(SYS.SHORT_NAME__20_NULL),
		ENABLED(SYS.ENABLED), 
		TYPE(TB.crt(Gl.ODirect.CR, "type", "类型")), 
		TB_OBJ(Tb.crtLongTbObj("tbObj", "对象")),
		PRN(SYS.YN,"是否打印"), 
		CARD(TB.crtDime(SYS.CARD_NUMB__NULL, new int[] { 1,3, 5 }, "字段1", "字段B", "字段Z")), 
		DEPT(SysDept.fldOutKey()),
		TBANDKEY(TB.crtTbAndKey("demo","例子")),	
		LINK(TB.crtOptAndKey("link", "关联",OUserType.EM)), //关联的表, 如职员表,个人客户表等
		PLAN(CmbBeginEndDate.fldCmb("plan", "计划")),
		REALITY(CmbBeginEndDate.fldCmb("reality", "实际")),
		BEDATE(CmbBeginEndDate.fldFlds()),
		INF(TB.crtInf()),  //自动建接口文件

		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		PKEY(TB.get("pkey")),	//编号
		CARD_NUMB1(TB.get("cardNumb1")),	//字段1
		CARD_NUMB3(TB.get("cardNumb3")),	//字段B
		CARD_NUMB5(TB.get("cardNumb5")),	//字段Z
		DEMO_TABLE(TB.get("demoTable")),	//例子
		DEMO_PKEY(TB.get("demoPkey")),	//例子主键值
		USER_TYPE(TB.get("userType")),	//用户类型
		LINK_PKEY(TB.get("linkPkey")),	//关联主键值
		PLAN_B_DATE(TB.get("planBDate")),	//计划起始日期
		PLAN_E_DATE(TB.get("planEDate")),	//计划结束日期
		REALITY_B_DATE(TB.get("realityBDate")),	//实际起始日期
		REALITY_E_DATE(TB.get("realityEDate")),	//实际结束日期
		B_DATE(TB.get("bDate")),	//起始日期
		E_DATE(TB.get("eDate")),	//结束日期
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
    ;
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,
				T.CODE);
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
		T.DEPT._fld.setNull();
		T.CARD.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
		
	}
	//@formatter:on

	



  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _name;	// 名称  STR(100)
  private String _code;	// 代码  STR(20)
  private String _shortName;	// 简称  STR(20)<null>
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _type;	// 类型 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
  private Long _tbObj;	// 对象  LONG
  private Byte _prn;	// 是否打印 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _cardNumb1;	// 字段1  STR(20)
  private String _cardNumb3;	// 字段B  STR(20)
  private String _cardNumb5;	// 字段Z  STR(20)
  private Integer _dept;	// 部门 <表主键:SysDept>  INT<null>
  private Integer _demoTable;	// 例子 <表主键:SysTable>  INT
  private Integer _demoPkey;	// 例子主键值  INT
  private Byte _userType;	// 用户类型 <OUserType>  BYTE
	// ADMIN:1,系统用户
	// EM:2,职员
	// OUTER_PERSON:5,个人客户
	// OUTER_COMPANY:6,企业客户
  private Integer _linkPkey;	// 关联主键值  INT
  private Date _planBDate;	// 计划起始日期  DATE<null>
  private Date _planEDate;	// 计划结束日期  DATE<null>
  private Date _realityBDate;	// 实际起始日期  DATE<null>
  private Date _realityEDate;	// 实际结束日期  DATE<null>
  private Date _bDate;	// 起始日期  DATE<null>
  private Date _eDate;	// 结束日期  DATE<null>

	@Override
  public DemoBean init(){
		super.init();
    _name=null;	// 名称  STR(100)
    _code=null;	// 代码  STR(20)
    _shortName=null;	// 简称  STR(20)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _type=ODirect.DEFAULT.getLine().getKey();	// 类型 <ODirect>  BYTE
    _tbObj=(long)0;	// 对象  LONG
    _prn=OYn.DEFAULT.getLine().getKey();	// 是否打印 <OYn>  BYTE
    _cardNumb1=null;	// 字段1  STR(20)
    _cardNumb3=null;	// 字段B  STR(20)
    _cardNumb5=null;	// 字段Z  STR(20)
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _demoTable=null;	// 例子 <表主键:SysTable>  INT
    _demoPkey=0;	// 例子主键值  INT
    _userType=OUserType.DEFAULT.getLine().getKey();	// 用户类型 <OUserType>  BYTE
    _linkPkey=0;	// 关联主键值  INT
    _planBDate=null;	// 计划起始日期  DATE
    _planEDate=null;	// 计划结束日期  DATE
    _realityBDate=null;	// 实际起始日期  DATE
    _realityEDate=null;	// 实际结束日期  DATE
    _bDate=null;	// 起始日期  DATE
    _eDate=null;	// 结束日期  DATE
    return this;
  }

  //方法----------------------------------------------
  public static DemoBean loadUniqueCode(boolean lockFlag,String code) {
    return (DemoBean)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static DemoBean chkUniqueCode(boolean lockFlag,String code) {
    return (DemoBean)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对一表对象: SysCom
  public SysCom gtCom(){
    return get(SysCom.class,getPkey());
  }
  public void stCom(SysCom com){
      setPkey(com.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public String getShortName(){
    return _shortName;
  }
  public void setShortName(String shortName){
    _shortName=shortName;
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
  public ODirect gtType(){
    return (ODirect)(ODirect.CR.getLine().get(_type));
  }
  public void stType(ODirect type){
    _type=type.getLine().getKey();
  }
  public Long getTbObj(){
    return _tbObj;
  }
  public void setTbObj(Long tbObj){
    _tbObj=tbObj;
  }
  //外部主键对象: BeanLong
  public Bean gtTbObj(){
    return (Bean)gtLongTbObj(getTbObj());
  }
  public void stTbObj(Bean tbObj){
      setTbObj(tbObj.gtLongPkey());
  }
  public Byte getPrn(){
    return _prn;
  }
  public void setPrn(Byte prn){
    _prn=prn;
  }
  public Boolean gtPrn(){
    return byteToBoolean(_prn);
  }
  public void stPrn(Boolean prn){
    _prn=booleanToByte(prn);
  }
  //数组对象: String
  public String gtCardNumb(int i){
    switch(i) {
    case 1:
    	return _cardNumb1;
    case 3:
    	return _cardNumb3;
    case 5:
    	return _cardNumb5;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stCardNumb( int i, String cardNumb){
    switch(i) {
    case 1:
    	_cardNumb1=cardNumb;
    	return;
    case 3:
    	_cardNumb3=cardNumb;
    	return;
    case 5:
    	_cardNumb5=cardNumb;
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public String getCardNumb1(){
    return _cardNumb1;
  }
  public void setCardNumb1(String cardNumb1){
    _cardNumb1=cardNumb1;
  }
  public String getCardNumb3(){
    return _cardNumb3;
  }
  public void setCardNumb3(String cardNumb3){
    _cardNumb3=cardNumb3;
  }
  public String getCardNumb5(){
    return _cardNumb5;
  }
  public void setCardNumb5(String cardNumb5){
    _cardNumb5=cardNumb5;
  }
  public Integer getDept(){
    return _dept;
  }
  public void setDept(Integer dept){
    _dept=dept;
  }
  public SysDept gtDept(){
    if(getDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getDept());
  }
  public void stDept(SysDept dept){
    if(dept==null)
      setDept(null);
    else
      setDept(dept.getPkey());
  }
  //根据表字段及主键字段的值取对象
  public Bean gtDemo(){
    if(getDemoPkey()==null)
    	return null;
    return gtTbAndPkeyObj(getDemoTable(),getDemoPkey());
  }
  public void stDemo(Bean demo){
    setDemoPkey((Integer)demo.pkeyValues()[0]);
    setDemoTable(demo.gtTbId());
  }
  public Integer getDemoTable(){
    return _demoTable;
  }
  public void setDemoTable(Integer demoTable){
    _demoTable=demoTable;
  }
  public SysTable gtDemoTable(){
    if(getDemoTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getDemoTable());
  }
  public void stDemoTable(SysTable demoTable){
    if(demoTable==null)
      setDemoTable(null);
    else
      setDemoTable(demoTable.getPkey());
  }
  public Integer getDemoPkey(){
    return _demoPkey;
  }
  public void setDemoPkey(Integer demoPkey){
    _demoPkey=demoPkey;
  }
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtLink(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtUserType();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_linkPkey);
  }
  public Byte getUserType(){
    return _userType;
  }
  public void setUserType(Byte userType){
    _userType=userType;
  }
  public OUserType gtUserType(){
    return (OUserType)(OUserType.EM.getLine().get(_userType));
  }
  public void stUserType(OUserType userType){
    _userType=userType.getLine().getKey();
  }
  public Integer getLinkPkey(){
    return _linkPkey;
  }
  public void setLinkPkey(Integer linkPkey){
    _linkPkey=linkPkey;
  }
  //组合对象的操作
  public CmbBeginEndDate gtPlan(){
    CmbBeginEndDate b=new CmbBeginEndDate();
    	b.setBDate(_planBDate);
    	b.setEDate(_planEDate);
    return b;
  }
  public void stPlan(CmbBeginEndDate plan){
    _planBDate=plan.getBDate();
    _planEDate=plan.getEDate();
  }
  public Date getPlanBDate(){
    return _planBDate;
  }
  public void setPlanBDate(Date planBDate){
    _planBDate=planBDate;
  }
  public Date getPlanEDate(){
    return _planEDate;
  }
  public void setPlanEDate(Date planEDate){
    _planEDate=planEDate;
  }
  //组合对象的操作
  public CmbBeginEndDate gtReality(){
    CmbBeginEndDate b=new CmbBeginEndDate();
    	b.setBDate(_realityBDate);
    	b.setEDate(_realityEDate);
    return b;
  }
  public void stReality(CmbBeginEndDate reality){
    _realityBDate=reality.getBDate();
    _realityEDate=reality.getEDate();
  }
  public Date getRealityBDate(){
    return _realityBDate;
  }
  public void setRealityBDate(Date realityBDate){
    _realityBDate=realityBDate;
  }
  public Date getRealityEDate(){
    return _realityEDate;
  }
  public void setRealityEDate(Date realityEDate){
    _realityEDate=realityEDate;
  }
  public Date getBDate(){
    return _bDate;
  }
  public void setBDate(Date bDate){
    _bDate=bDate;
  }
  public Date getEDate(){
    return _eDate;
  }
  public void setEDate(Date eDate){
    _eDate=eDate;
  }

}
