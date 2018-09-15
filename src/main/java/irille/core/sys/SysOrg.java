package irille.core.sys;

import irille.core.sys.Sys.OCurrency;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OOrgState;
import irille.core.sys.Sys.OValuationMethods;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVOneToOne;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 与SYSCOM不是标准的一对一；COM里是带了表ID的主键
 * @author whx
 * @version 创建时间：2015年3月13日 上午11:35:59
 */
public class SysOrg extends BeanInt<SysOrg> implements IExtName {

	private static final Log LOG = new Log(SysOrg.class);
	public static final Tb TB = new Tb(SysOrg.class, "机构信息", "机构").setAutoIncrement().addActIUDL().
			addActEnabled().addActOpt("ded", "日终处理");
	static {
		Gl.class.getName();
	}

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__40),
		NAME(SYS.NAME__100,"机构名称"),
		SHORT_NAME(SYS.SHORT_NAME__20_NULL,"机构简称"),
		ENABLED(TB.crt(SYS.ENABLED)),
		ORG_UP(SYS.ORG,"上级机构",true),
		WORK_DATE(SYS.WORK_DATE),
		STATE(TB.crt(Sys.OOrgState.DAY_END)),
		TEMPLAT(SYS.TEMPLAT,"科目模板"),
		VALUATION_METHODS(TB.crt(Sys.OValuationMethods.DEFAULT)),
		INTERNATION_TRADE(SYS.YN,"是否有国际贸易"),
		CURRENCY(SYS.CURRENCY),
		ROW_VERSION(SYS.ROW_VERSION),
		COM(new FldVOneToOne(TB.getClazz(), "com", "单位信息", true)),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,T.CODE);
		public static final Index IDX_ORG_UP = TB.addIndex("orgUp", false,T.ORG_UP);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return Str.isEmpty(getShortName()) ? getName() : getShortName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(),TB.getShortName());
	}
	public static Fld fldOutKey(String code,String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	//取一对一表对象: SysCom
  public SysCom gtCom(){
    return get(SysCom.class,gtLongPkey());
  }
  public void stCom(SysCom com){
      setPkey((int)(com.getPkey()/SysTable.NUM_BASE));
  }
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private String _name;	// 机构名称  STR(100)
  private String _shortName;	// 机构简称  STR(20)<null>
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Integer _orgUp;	// 上级机构 <表主键:SysOrg>  INT<null>
  private Date _workDate;	// 工作日期  DATE
  private Byte _state;	// 机构状态 <OOrgState>  BYTE
	// DAY_END:1,日终处理结束
	// OPENING:2,营业中
	// DAY_END_DOING:3,日终处理中...
  private Integer _templat;	// 科目模板 <表主键:SysTemplat>  INT
  private Byte _valuationMethods;	// 存货计价方式 <OValuationMethods>  BYTE
	// AVERAGE:1,核算单元库存的平均价
  private Byte _internationTrade;	// 是否有国际贸易 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _currency;	// 币种 <OCurrency>  BYTE
	// RMB:1,人民币
	// MY:2,美元
	// OY:3,欧元
	// GB:4,港币
	// RY:5,日元
	// HB:6,韩币
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysOrg init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 机构名称  STR(100)
    _shortName=null;	// 机构简称  STR(20)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _orgUp=null;	// 上级机构 <表主键:SysOrg>  INT
    _workDate=Env.getWorkDate();	// 工作日期  DATE
    _state=OOrgState.DEFAULT.getLine().getKey();	// 机构状态 <OOrgState>  BYTE
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _valuationMethods=OValuationMethods.DEFAULT.getLine().getKey();	// 存货计价方式 <OValuationMethods>  BYTE
    _internationTrade=OYn.DEFAULT.getLine().getKey();	// 是否有国际贸易 <OYn>  BYTE
    _currency=OCurrency.DEFAULT.getLine().getKey();	// 币种 <OCurrency>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysOrg loadUniqueCode(boolean lockFlag,String code) {
    return (SysOrg)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysOrg chkUniqueCode(boolean lockFlag,String code) {
    return (SysOrg)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Integer getOrgUp(){
    return _orgUp;
  }
  public void setOrgUp(Integer orgUp){
    _orgUp=orgUp;
  }
  public SysOrg gtOrgUp(){
    if(getOrgUp()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrgUp());
  }
  public void stOrgUp(SysOrg orgUp){
    if(orgUp==null)
      setOrgUp(null);
    else
      setOrgUp(orgUp.getPkey());
  }
  public Date getWorkDate(){
    return _workDate;
  }
  public void setWorkDate(Date workDate){
    _workDate=workDate;
  }
  public Byte getState(){
    return _state;
  }
  public void setState(Byte state){
    _state=state;
  }
  public OOrgState gtState(){
    return (OOrgState)(OOrgState.DAY_END.getLine().get(_state));
  }
  public void stState(OOrgState state){
    _state=state.getLine().getKey();
  }
  public Integer getTemplat(){
    return _templat;
  }
  public void setTemplat(Integer templat){
    _templat=templat;
  }
  public SysTemplat gtTemplat(){
    if(getTemplat()==null)
      return null;
    return (SysTemplat)get(SysTemplat.class,getTemplat());
  }
  public void stTemplat(SysTemplat templat){
    if(templat==null)
      setTemplat(null);
    else
      setTemplat(templat.getPkey());
  }
  public Byte getValuationMethods(){
    return _valuationMethods;
  }
  public void setValuationMethods(Byte valuationMethods){
    _valuationMethods=valuationMethods;
  }
  public OValuationMethods gtValuationMethods(){
    return (OValuationMethods)(OValuationMethods.AVERAGE.getLine().get(_valuationMethods));
  }
  public void stValuationMethods(OValuationMethods valuationMethods){
    _valuationMethods=valuationMethods.getLine().getKey();
  }
  public Byte getInternationTrade(){
    return _internationTrade;
  }
  public void setInternationTrade(Byte internationTrade){
    _internationTrade=internationTrade;
  }
  public Boolean gtInternationTrade(){
    return byteToBoolean(_internationTrade);
  }
  public void stInternationTrade(Boolean internationTrade){
    _internationTrade=booleanToByte(internationTrade);
  }
  public Byte getCurrency(){
    return _currency;
  }
  public void setCurrency(Byte currency){
    _currency=currency;
  }
  public OCurrency gtCurrency(){
    return (OCurrency)(OCurrency.RMB.getLine().get(_currency));
  }
  public void stCurrency(OCurrency currency){
    _currency=currency.getLine().getKey();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
