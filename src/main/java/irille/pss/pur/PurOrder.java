package irille.pss.pur;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysShiping;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OShipingMode;
import irille.gl.gs.GsWarehouse;
import irille.pss.pur.Pur.OOrderStatus;
import irille.pub.bean.BeanBill;
import irille.pub.bean.CmbBill;
import irille.pub.gl.AccObjs;
import irille.pub.gl.TallyLines;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class PurOrder extends BeanBill<PurOrder> implements IExtName{
	public static final Tb TB = new Tb(PurOrder.class, "采购订单", "采购订单").setAutoIncrement().addActIUDL()
	    .addActOpt("checkPrice", "核价").addActApprove().addActOpt("doClose", "关闭").addActNote().addActTally().addActPrint();//.addActOpt("doOpen", "打开")

	public enum T implements IEnumFld {//@formatter:off
		CMB_BILL(CmbBill.fldFlds()),
		SUPPLIER(SYS.SUPPLIER), //供应商
		SUPNAME(SYS.NAME__100, "供应商名称"),
		WAREHOUSE(GsWarehouse.fldOutKey()), //仓库
		AMT(SYS.AMT), //金额
		AMT_COST(SYS.AMT_COST),
		EARNEST(SYS.AMT, "订金"),
		BUYER(SYS.USER_SYS, "采购员"), //采购员
		REV_ADDR(SYS.ADDR__200_NULL, "收货地址"),
		ORD_STATUS(TB.crt(Pur.OOrderStatus.INIT)),
		BILL_FLAG(TB.crt(Sys.OBillFlag.YES)),
		SHIPING_MODE(TB.crt(Sys.OShipingMode.DEFAULT)),
		SHIPING(SysShiping.fldOutKey().setNull()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		CODE(TB.get("code")),	//单据号
		STATUS(TB.get("status")),	//状态
		ORG(TB.get("org")),	//机构
		CELL(TB.get("cell")),	//核算单元
		DEPT(TB.get("dept")),	//部门
		CREATED_BY(TB.get("createdBy")),	//建档员
		CREATED_TIME(TB.get("createdTime")),	//建档时间
		APPR_BY(TB.get("apprBy")),	//审核员
		APPR_TIME(TB.get("apprTime")),	//审核时间
		TALLY_BY(TB.get("tallyBy")),	//记账员
		TALLY_TIME(TB.get("tallyTime")),	//记账日期
		REM(TB.get("rem")),	//备注
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		public static final Index IDX_CODE= TB.addIndex("code",true,CODE);
		public static final Index IDX_ORG_TIME= TB.addIndex("orgTime",false,ORG,CREATED_TIME);
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
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
		T.CMB_BILL.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}

	@Override
	public void getAccObjs(String name, AccObjs objs) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initTallyLines(TallyLines ls) {
		// TODO Auto-generated method stub
	}
	
	@Override
	public String getExtName() {
		return getCode();
	}

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
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
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private Integer _tallyBy;	// 记账员 <表主键:SysUser>  INT<null>
  private Date _tallyTime;	// 记账日期  DATE<null>
  private String _rem;	// 备注  STR(200)<null>
  private Integer _supplier;	// 供应商 <表主键:SysSupplier>  INT
  private String _supname;	// 供应商名称  STR(100)
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _amtCost;	// 费用合计  DEC(16,2)
  private BigDecimal _earnest;	// 订金  DEC(16,2)
  private Integer _buyer;	// 采购员 <表主键:SysUser>  INT
  private String _revAddr;	// 收货地址  STR(200)<null>
  private Byte _ordStatus;	// 开单状态 <OOrderStatus>  BYTE
	// INIT:1,未完成
	// CLOSE:2,已关闭
  private Byte _billFlag;	// 开票标准 <OBillFlag>  BYTE
	// YES:1,开票
	// NO:0,不开票
	// WAIT:3,待定
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
  public PurOrder init(){
		super.init();
    _code=null;	// 单据号  STR(40)
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _tallyBy=null;	// 记账员 <表主键:SysUser>  INT
    _tallyTime=null;	// 记账日期  DATE
    _rem=null;	// 备注  STR(200)
    _supplier=null;	// 供应商 <表主键:SysSupplier>  INT
    _supname=null;	// 供应商名称  STR(100)
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _amt=ZERO;	// 金额  DEC(16,2)
    _amtCost=ZERO;	// 费用合计  DEC(16,2)
    _earnest=ZERO;	// 订金  DEC(16,2)
    _buyer=null;	// 采购员 <表主键:SysUser>  INT
    _revAddr=null;	// 收货地址  STR(200)
    _ordStatus=OOrderStatus.DEFAULT.getLine().getKey();	// 开单状态 <OOrderStatus>  BYTE
    _billFlag=OBillFlag.DEFAULT.getLine().getKey();	// 开票标准 <OBillFlag>  BYTE
    _shipingMode=OShipingMode.DEFAULT.getLine().getKey();	// 运输方式 <OShipingMode>  BYTE
    _shiping=null;	// 发货信息 <表主键:SysShiping>  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static PurOrder loadUniqueCode(boolean lockFlag,String code) {
    return (PurOrder)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static PurOrder chkUniqueCode(boolean lockFlag,String code) {
    return (PurOrder)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Integer getCell(){
    return _cell;
  }
  public void setCell(Integer cell){
    _cell=cell;
  }
  public SysCell gtCell(){
    if(getCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getCell());
  }
  public void stCell(SysCell cell){
    if(cell==null)
      setCell(null);
    else
      setCell(cell.getPkey());
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
  public Integer getTallyBy(){
    return _tallyBy;
  }
  public void setTallyBy(Integer tallyBy){
    _tallyBy=tallyBy;
  }
  public SysUser gtTallyBy(){
    if(getTallyBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getTallyBy());
  }
  public void stTallyBy(SysUser tallyBy){
    if(tallyBy==null)
      setTallyBy(null);
    else
      setTallyBy(tallyBy.getPkey());
  }
  public Date getTallyTime(){
    return _tallyTime;
  }
  public void setTallyTime(Date tallyTime){
    _tallyTime=tallyTime;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Integer getSupplier(){
    return _supplier;
  }
  public void setSupplier(Integer supplier){
    _supplier=supplier;
  }
  public SysSupplier gtSupplier(){
    if(getSupplier()==null)
      return null;
    return (SysSupplier)get(SysSupplier.class,getSupplier());
  }
  public void stSupplier(SysSupplier supplier){
    if(supplier==null)
      setSupplier(null);
    else
      setSupplier(supplier.getPkey());
  }
  public String getSupname(){
    return _supname;
  }
  public void setSupname(String supname){
    _supname=supname;
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
  public BigDecimal getEarnest(){
    return _earnest;
  }
  public void setEarnest(BigDecimal earnest){
    _earnest=earnest;
  }
  public Integer getBuyer(){
    return _buyer;
  }
  public void setBuyer(Integer buyer){
    _buyer=buyer;
  }
  public SysUser gtBuyer(){
    if(getBuyer()==null)
      return null;
    return (SysUser)get(SysUser.class,getBuyer());
  }
  public void stBuyer(SysUser buyer){
    if(buyer==null)
      setBuyer(null);
    else
      setBuyer(buyer.getPkey());
  }
  public String getRevAddr(){
    return _revAddr;
  }
  public void setRevAddr(String revAddr){
    _revAddr=revAddr;
  }
  public Byte getOrdStatus(){
    return _ordStatus;
  }
  public void setOrdStatus(Byte ordStatus){
    _ordStatus=ordStatus;
  }
  public OOrderStatus gtOrdStatus(){
    return (OOrderStatus)(OOrderStatus.INIT.getLine().get(_ordStatus));
  }
  public void stOrdStatus(OOrderStatus ordStatus){
    _ordStatus=ordStatus.getLine().getKey();
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

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
