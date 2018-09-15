package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysShiping;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OShipingMode;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbForm;
import irille.pub.bean.IForm;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class GsIn extends BeanForm<GsIn> implements IForm {
	public static final Tb TB = new Tb(GsIn.class, "入库单", "入库单").setAutoIncrement().addActList().addActApprove().addActPrint();

	public enum T implements IEnumFld {//@formatter:off
		CMB_FORM(CmbForm.fldFlds()),
		WAREHOUSE(GsWarehouse.fldOutKey()),
		GS_NAME(SYS.NAME__100),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		OPERATOR(SYS.USER_SYS, "理货员", true),
		CHECKER(SYS.USER_SYS, "检验员", true),
		IN_TIME(SYS.DATE_TIME__NULL,"实际入库时间"),
		SHIPING_MODE(TB.crt(Sys.OShipingMode.DEFAULT)),
		SHIPING(SysShiping.fldOutKey().setNull()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		CODE(TB.get("code")),	//单据号
		STATUS(TB.get("status")),	//状态
		ORG(TB.get("org")),	//机构
		DEPT(TB.get("dept")),	//部门
		CREATED_BY(TB.get("createdBy")),	//建档员
		CREATED_TIME(TB.get("createdTime")),	//建档时间
		APPR_BY(TB.get("apprBy")),	//审核员
		APPR_TIME(TB.get("apprTime")),	//审核时间
		REM(TB.get("rem")),	//备注
		ORIG_FORM_NUM(TB.get("origFormNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		public static final Index IDX_CODE= TB.addIndex("code",true,CODE);
		public static final Index IDX_ORG_TIME= TB.addIndex("orgTime",false,ORG,CREATED_TIME);
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ORIG = TB.addIndex("orig", true, ORIG_FORM);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CMB_FORM.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 单据号  STR(40)
  private Byte _status;	// 状态 <OBillStatus>  BYTE
	// INIT:11,初始
	// OK:21,已输入确认
	// VERIFING:53,复核中
	// VERIFIED:58,已复核
	// CHECKING:63,审核中
	// CHECKED:68,已审核
	// VETTING:73,审批中
	// VETTED:78,已审批
	// INOUT:81,已出入库
	// TALLY_ABLE:83,可记账
	// DONE:98,完成
	// DELETED:99,作废
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private String _gsName;	// 名称  STR(100)
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Integer _operator;	// 理货员 <表主键:SysUser>  INT<null>
  private Integer _checker;	// 检验员 <表主键:SysUser>  INT<null>
  private Date _inTime;	// 实际入库时间  TIME<null>
  private Byte _shipingMode;	// 运输方式 <OShipingMode>  BYTE
	// NO:1,不需运输
	// SELF:2,自提
	// EXPRESS:10,快递
	// ROAD:30,陆运
	// SEA:50,海运
	// AIR:70,空运
	// OTHER:99,其它
  private Long _shiping;	// 发货信息 <表主键:SysShiping>  LONG<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsIn init(){
		super.init();
    _code=null;	// 单据号  STR(40)
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _rem=null;	// 备注  STR(200)
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _gsName=null;	// 名称  STR(100)
    _origForm=null;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _operator=null;	// 理货员 <表主键:SysUser>  INT
    _checker=null;	// 检验员 <表主键:SysUser>  INT
    _inTime=null;	// 实际入库时间  TIME
    _shipingMode=OShipingMode.DEFAULT.getLine().getKey();	// 运输方式 <OShipingMode>  BYTE
    _shiping=null;	// 发货信息 <表主键:SysShiping>  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsIn loadUniqueCode(boolean lockFlag,String code) {
    return (GsIn)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GsIn chkUniqueCode(boolean lockFlag,String code) {
    return (GsIn)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GsIn loadUniqueOrig(boolean lockFlag,Long origForm) {
    return (GsIn)loadUnique(T.IDX_ORIG,lockFlag,origForm);
  }
  public static GsIn chkUniqueOrig(boolean lockFlag,Long origForm) {
    return (GsIn)chkUnique(T.IDX_ORIG,lockFlag,origForm);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Byte getStatus(){
    return _status;
  }
  public void setStatus(Byte status){
    _status=status;
  }
  public OBillStatus gtStatus(){
    return (OBillStatus)(OBillStatus.INIT.getLine().get(_status));
  }
  public void stStatus(OBillStatus status){
    _status=status.getLine().getKey();
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
  public Integer getApprBy(){
    return _apprBy;
  }
  public void setApprBy(Integer apprBy){
    _apprBy=apprBy;
  }
  public SysUser gtApprBy(){
    if(getApprBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getApprBy());
  }
  public void stApprBy(SysUser apprBy){
    if(apprBy==null)
      setApprBy(null);
    else
      setApprBy(apprBy.getPkey());
  }
  public Date getApprTime(){
    return _apprTime;
  }
  public void setApprTime(Date apprTime){
    _apprTime=apprTime;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Integer getWarehouse(){
    return _warehouse;
  }
  public void setWarehouse(Integer warehouse){
    _warehouse=warehouse;
  }
  public GsWarehouse gtWarehouse(){
    if(getWarehouse()==null)
      return null;
    return (GsWarehouse)get(GsWarehouse.class,getWarehouse());
  }
  public void stWarehouse(GsWarehouse warehouse){
    if(warehouse==null)
      setWarehouse(null);
    else
      setWarehouse(warehouse.getPkey());
  }
  public String getGsName(){
    return _gsName;
  }
  public void setGsName(String gsName){
    _gsName=gsName;
  }
  public Long getOrigForm(){
    return _origForm;
  }
  public void setOrigForm(Long origForm){
    _origForm=origForm;
  }
  //外部主键对象: IForm
  public Bean gtOrigForm(){
    return (Bean)gtLongTbObj(getOrigForm());
  }
  public void stOrigForm(Bean origForm){
      setOrigForm(origForm.gtLongPkey());
  }
  public String getOrigFormNum(){
    return _origFormNum;
  }
  public void setOrigFormNum(String origFormNum){
    _origFormNum=origFormNum;
  }
  public Integer getOperator(){
    return _operator;
  }
  public void setOperator(Integer operator){
    _operator=operator;
  }
  public SysUser gtOperator(){
    if(getOperator()==null)
      return null;
    return (SysUser)get(SysUser.class,getOperator());
  }
  public void stOperator(SysUser operator){
    if(operator==null)
      setOperator(null);
    else
      setOperator(operator.getPkey());
  }
  public Integer getChecker(){
    return _checker;
  }
  public void setChecker(Integer checker){
    _checker=checker;
  }
  public SysUser gtChecker(){
    if(getChecker()==null)
      return null;
    return (SysUser)get(SysUser.class,getChecker());
  }
  public void stChecker(SysUser checker){
    if(checker==null)
      setChecker(null);
    else
      setChecker(checker.getPkey());
  }
  public Date getInTime(){
    return _inTime;
  }
  public void setInTime(Date inTime){
    _inTime=inTime;
  }
  public Byte getShipingMode(){
    return _shipingMode;
  }
  public void setShipingMode(Byte shipingMode){
    _shipingMode=shipingMode;
  }
  public OShipingMode gtShipingMode(){
    return (OShipingMode)(OShipingMode.NO.getLine().get(_shipingMode));
  }
  public void stShipingMode(OShipingMode shipingMode){
    _shipingMode=shipingMode.getLine().getKey();
  }
  public Long getShiping(){
    return _shiping;
  }
  public void setShiping(Long shiping){
    _shiping=shiping;
  }
  public SysShiping gtShiping(){
    if(getShiping()==null)
      return null;
    return (SysShiping)get(SysShiping.class,getShiping());
  }
  public void stShiping(SysShiping shiping){
    if(shiping==null)
      setShiping(null);
    else
      setShiping(shiping.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
