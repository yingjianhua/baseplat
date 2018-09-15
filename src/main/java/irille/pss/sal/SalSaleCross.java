package irille.pss.sal;

import irille.core.sys.CmbMsgGoodsBy;
import irille.core.sys.CmbMsgShip;
import irille.core.sys.Sys;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.gs.GsWarehouse;
import irille.pss.lgt.LgtShipMode;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbForm;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 跨仓库
 * @author whx
 * @version 创建时间：2014年8月27日 下午3:04:46
 */
public class SalSaleCross extends BeanForm<SalSaleCross> {
	public static final Tb TB = new Tb(SalSaleCross.class, "跨公司销售单").setAutoIncrement().addActIUDL().addActApprove();

	public enum T implements IEnumFld {//@formatter:off
		CMB_FORM(CmbForm.fldFlds()),
		CUST(SYS.CUST), //客户
		NAME(SYS.NAME__100, "客户名称"), //客户名称
		ORD(SalOrder.fldOutKey().setNull()),
		WAREHOUSE(GsWarehouse.fldOutKey()),
		AMT(SYS.AMT), //金额
		AMT_COST(SYS.AMT_COST),
		OPERATOR(SYS.USER_SYS, "业务员"), //业务员
		SHIP_BY(SYS.USER_SYS, "发货人", true), //发货人
		CMB_SHIP(CmbMsgShip.fldFlds()),
		GOODSBY(CmbMsgGoodsBy.fldCmb("goodsby", "收货人")),
		BILL_FLAG(TB.crt(Sys.OBillFlag.YES)),
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
		PACK_DEMAND(TB.get("packDemand")),	//包装要求
		SHIP_MODE(TB.get("shipMode")),	//运输方式
		TIME_SHIP_PLAN(TB.get("timeShipPlan")),	//计划发货日期
		TIME_SHIP(TB.get("timeShip")),	//实际发货日期
		TIME_ARR_PLAN(TB.get("timeArrPlan")),	//预计到货时间
		TIME_ARR(TB.get("timeArr")),	//实际到货时间
		GOODSBY_NAME(TB.get("goodsbyName")),	//收货人名称
		GOODSBY_ADDR(TB.get("goodsbyAddr")),	//收货人地址
		GOODSBY_MOBILE(TB.get("goodsbyMobile")),	//收货人手机号码
		GOODSBY_TEL(TB.get("goodsbyTel")),	//收货人电话
		GOODSBY_ZIP(TB.get("goodsbyZip")),	//收货人邮编
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		public static final Index IDX_CODE= TB.addIndex("code",true,CODE);
		public static final Index IDX_ORG_TIME= TB.addIndex("orgTime",false,ORG,CREATED_TIME);
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return 	_fld;}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CMB_FORM.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}

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
  private Integer _cust;	// 客户 <表主键:SysCustom>  INT
  private String _name;	// 客户名称  STR(100)
  private Long _ord;	// 销售订单 <表主键:SalOrder>  LONG<null>
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _amtCost;	// 费用合计  DEC(16,2)
  private Integer _operator;	// 业务员 <表主键:SysUser>  INT
  private Integer _shipBy;	// 发货人 <表主键:SysUser>  INT<null>
  private String _packDemand;	// 包装要求  STR(200)<null>
  private Integer _shipMode;	// 运输方式 <表主键:LgtShipMode>  INT<null>
  private Date _timeShipPlan;	// 计划发货日期  TIME<null>
  private Date _timeShip;	// 实际发货日期  TIME<null>
  private Date _timeArrPlan;	// 预计到货时间  TIME<null>
  private Date _timeArr;	// 实际到货时间  TIME<null>
  private String _goodsbyName;	// 收货人名称  STR(100)<null>
  private String _goodsbyAddr;	// 收货人地址  STR(200)<null>
  private String _goodsbyMobile;	// 收货人手机号码  STR(20)<null>
  private String _goodsbyTel;	// 收货人电话  STR(20)<null>
  private String _goodsbyZip;	// 收货人邮编  STR(6)<null>
  private Byte _billFlag;	// 开票标准 <OBillFlag>  BYTE
	// YES:1,开票
	// NO:0,不开票
	// WAIT:3,待定
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SalSaleCross init(){
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
    _cust=null;	// 客户 <表主键:SysCustom>  INT
    _name=null;	// 客户名称  STR(100)
    _ord=null;	// 销售订单 <表主键:SalOrder>  LONG
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _amt=ZERO;	// 金额  DEC(16,2)
    _amtCost=ZERO;	// 费用合计  DEC(16,2)
    _operator=null;	// 业务员 <表主键:SysUser>  INT
    _shipBy=null;	// 发货人 <表主键:SysUser>  INT
    _packDemand=null;	// 包装要求  STR(200)
    _shipMode=null;	// 运输方式 <表主键:LgtShipMode>  INT
    _timeShipPlan=null;	// 计划发货日期  TIME
    _timeShip=null;	// 实际发货日期  TIME
    _timeArrPlan=null;	// 预计到货时间  TIME
    _timeArr=null;	// 实际到货时间  TIME
    _goodsbyName=null;	// 收货人名称  STR(100)
    _goodsbyAddr=null;	// 收货人地址  STR(200)
    _goodsbyMobile=null;	// 收货人手机号码  STR(20)
    _goodsbyTel=null;	// 收货人电话  STR(20)
    _goodsbyZip=null;	// 收货人邮编  STR(6)
    _billFlag=OBillFlag.DEFAULT.getLine().getKey();	// 开票标准 <OBillFlag>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SalSaleCross loadUniqueCode(boolean lockFlag,String code) {
    return (SalSaleCross)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SalSaleCross chkUniqueCode(boolean lockFlag,String code) {
    return (SalSaleCross)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Integer getCust(){
    return _cust;
  }
  public void setCust(Integer cust){
    _cust=cust;
  }
  public SysCustom gtCust(){
    if(getCust()==null)
      return null;
    return (SysCustom)get(SysCustom.class,getCust());
  }
  public void stCust(SysCustom cust){
    if(cust==null)
      setCust(null);
    else
      setCust(cust.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Long getOrd(){
    return _ord;
  }
  public void setOrd(Long ord){
    _ord=ord;
  }
  public SalOrder gtOrd(){
    if(getOrd()==null)
      return null;
    return (SalOrder)get(SalOrder.class,getOrd());
  }
  public void stOrd(SalOrder ord){
    if(ord==null)
      setOrd(null);
    else
      setOrd(ord.getPkey());
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
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public BigDecimal getAmtCost(){
    return _amtCost;
  }
  public void setAmtCost(BigDecimal amtCost){
    _amtCost=amtCost;
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
  public Integer getShipBy(){
    return _shipBy;
  }
  public void setShipBy(Integer shipBy){
    _shipBy=shipBy;
  }
  public SysUser gtShipBy(){
    if(getShipBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getShipBy());
  }
  public void stShipBy(SysUser shipBy){
    if(shipBy==null)
      setShipBy(null);
    else
      setShipBy(shipBy.getPkey());
  }
  public String getPackDemand(){
    return _packDemand;
  }
  public void setPackDemand(String packDemand){
    _packDemand=packDemand;
  }
  public Integer getShipMode(){
    return _shipMode;
  }
  public void setShipMode(Integer shipMode){
    _shipMode=shipMode;
  }
  public LgtShipMode gtShipMode(){
    if(getShipMode()==null)
      return null;
    return (LgtShipMode)get(LgtShipMode.class,getShipMode());
  }
  public void stShipMode(LgtShipMode shipMode){
    if(shipMode==null)
      setShipMode(null);
    else
      setShipMode(shipMode.getPkey());
  }
  public Date getTimeShipPlan(){
    return _timeShipPlan;
  }
  public void setTimeShipPlan(Date timeShipPlan){
    _timeShipPlan=timeShipPlan;
  }
  public Date getTimeShip(){
    return _timeShip;
  }
  public void setTimeShip(Date timeShip){
    _timeShip=timeShip;
  }
  public Date getTimeArrPlan(){
    return _timeArrPlan;
  }
  public void setTimeArrPlan(Date timeArrPlan){
    _timeArrPlan=timeArrPlan;
  }
  public Date getTimeArr(){
    return _timeArr;
  }
  public void setTimeArr(Date timeArr){
    _timeArr=timeArr;
  }
  //组合对象的操作
  public CmbMsgGoodsBy gtGoodsby(){
    CmbMsgGoodsBy b=new CmbMsgGoodsBy();
    	b.setName(_goodsbyName);
    	b.setAddr(_goodsbyAddr);
    	b.setMobile(_goodsbyMobile);
    	b.setTel(_goodsbyTel);
    	b.setZip(_goodsbyZip);
    return b;
  }
  public void stGoodsby(CmbMsgGoodsBy goodsby){
    _goodsbyName=goodsby.getName();
    _goodsbyAddr=goodsby.getAddr();
    _goodsbyMobile=goodsby.getMobile();
    _goodsbyTel=goodsby.getTel();
    _goodsbyZip=goodsby.getZip();
  }
  public String getGoodsbyName(){
    return _goodsbyName;
  }
  public void setGoodsbyName(String goodsbyName){
    _goodsbyName=goodsbyName;
  }
  public String getGoodsbyAddr(){
    return _goodsbyAddr;
  }
  public void setGoodsbyAddr(String goodsbyAddr){
    _goodsbyAddr=goodsbyAddr;
  }
  public String getGoodsbyMobile(){
    return _goodsbyMobile;
  }
  public void setGoodsbyMobile(String goodsbyMobile){
    _goodsbyMobile=goodsbyMobile;
  }
  public String getGoodsbyTel(){
    return _goodsbyTel;
  }
  public void setGoodsbyTel(String goodsbyTel){
    _goodsbyTel=goodsbyTel;
  }
  public String getGoodsbyZip(){
    return _goodsbyZip;
  }
  public void setGoodsbyZip(String goodsbyZip){
    _goodsbyZip=goodsbyZip;
  }
  public Byte getBillFlag(){
    return _billFlag;
  }
  public void setBillFlag(Byte billFlag){
    _billFlag=billFlag;
  }
  public Boolean gtBillFlag(){
    return byteToBoolean(_billFlag);
  }
  public void stBillFlag(Boolean billFlag){
    _billFlag=booleanToByte(billFlag);
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
