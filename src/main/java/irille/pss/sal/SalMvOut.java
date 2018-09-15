package irille.pss.sal;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysShiping;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillFlag;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OShipingMode;
import irille.gl.gl.Gl;
import irille.gl.gs.GsWarehouse;
import irille.pss.cst.CstPub;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBill;
import irille.pub.bean.CmbBill;
import irille.pub.bean.IGoods;
import irille.pub.bean.IGoodsPrice;
import irille.pub.gl.AccObjs;
import irille.pub.gl.TallyLines;
import irille.pub.idu.Idu;
import irille.pub.inf.ICstInout;
import irille.pub.inf.ICstInvoice;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 调出单 调出单只能由调入单产生，建立后由调出方人员确定从仓库发货还是产生直销需求，以其它方式再供货
 * ================================
 * 调出单
 * 新增方式
 * 新增
 * 从需求新增
 * 
 * 删除
 * 从需求新增来的单据删除后反操作，恢复需求
 * 
 * 确认
 * 状态标记为确认，确认之后才能审核
 * 
 * 审核
 * 调出单审核后生成调入单，若没有选择仓库则产生直销需求
 * 生成的调入单审核后产生出入库单
 * 
 * 
 * @author
 * @version 创建时间：2014年8月21日 上午9:49:20
 */
public class SalMvOut extends BeanBill<SalMvOut> implements ICstInvoice, ICstInout {
	public static final Tb TB = new Tb(SalMvOut.class, "调出单").setAutoIncrement().addActIns().addActOpt("insFd", "从需求新增")
	    .addActUpd().addActDel().addActList().addActOpt("chk", "确认").addActApprove().addActNote().addActTally();

	public enum OMvType implements IEnumOpt {//@formatter:off
		CELL(1,"机构内核算单元之间"),ORG(2,"集团内机构之间"),AGREEMENT(11,"协议调出");
		public static final String NAME="调拔类型";
		public static final OMvType DEFAULT = AGREEMENT ; // 定义缺省值
		private EnumLine _line;
		private OMvType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;}
	}		//@formatter:on

	public enum OGoodsFrom implements IEnumOpt {//@formatter:off
		WAREHOURSE(1,"来源于仓库"),DIRECT_DEMAND(2,"产生直销需求");
		public static final String NAME="货物来源";
		public static final OGoodsFrom DEFAULT = WAREHOURSE; // 定义缺省值
		private EnumLine _line;
		private OGoodsFrom(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum OFromType implements IEnumOpt {//@formatter:off
		DEMAND(1,"需求"),MVIN(2,"调入"),NULL(3,"手动");
		public static final String NAME="单据来源";
		public static final OFromType DEFAULT = NULL; // 定义缺省值
		private EnumLine _line;
		private OFromType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public enum T implements IEnumFld {//@formatter:off
		CMB_BILL(CmbBill.fldFlds()),
		MV_TYPE(Tb.crt(OMvType.DEFAULT)),
		GOODS_FROM(Tb.crt(OGoodsFrom.DEFAULT)),		
		WAREHOUSE(GsWarehouse.fldOutKey().setNull()),
		IN_FORM(SYS.FORM,"调入单",true),
		ORG_OTHER(SYS.ORG,"调入机构"), 
		CELL_OTHER(SYS.CELL,"调入核算单元"), 
		WAREHOUSE_OTHER(GsWarehouse.fldOutKey("warehouseOther","调入仓库").setNull()),
		FROM_TYPE(Tb.crt(OFromType.DEFAULT)),		
		AMT(SYS.AMT),
		AMT_COST(SYS.AMT_COST),
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
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
	public List<IGoodsPrice> getInvoiceLines() {
		return Idu.getLinesTid(this, SalMvOutLine.class);
	}

	@Override
	public List<IGoods> getCstInoutLines(int type) {
		return CstPub.getCmbGoods(Idu.getLinesTid(this, SalMvOutLine.class)); //调出核算按成本价计算
	}

	@Override
	public void getAccObjs(String name, AccObjs objs) {
	}

	@Override
	public void initTallyLines(TallyLines ls) {
		ls.addByAlias(gtCell(), this, Sal.SubjectAlias.SAL_MV.getCode()).set(_amt, Gl.ODirect.DR, null,
		    "调出单(" + getCode() + ")");
		ls.addByAlias(gtCell(), this, Sal.SubjectAlias.SAL_PENDING.getCode()).set(_amt, Gl.ODirect.CR, null,
		    "调出单(" + getCode() + ")");
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
  private Byte _mvType;	// 调拔类型 <OMvType>  BYTE
	// CELL:1,机构内核算单元之间
	// ORG:2,集团内机构之间
	// AGREEMENT:11,协议调出
  private Byte _goodsFrom;	// 货物来源 <OGoodsFrom>  BYTE
	// WAREHOURSE:1,来源于仓库
	// DIRECT_DEMAND:2,产生直销需求
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT<null>
  private Long _inForm;	// 调入单  LONG<null>
  private Integer _orgOther;	// 调入机构 <表主键:SysOrg>  INT
  private Integer _cellOther;	// 调入核算单元 <表主键:SysCell>  INT
  private Integer _warehouseOther;	// 调入仓库 <表主键:GsWarehouse>  INT<null>
  private Byte _fromType;	// 单据来源 <OFromType>  BYTE
	// DEMAND:1,需求
	// MVIN:2,调入
	// NULL:3,手动
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _amtCost;	// 费用合计  DEC(16,2)
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
  public SalMvOut init(){
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
    _mvType=OMvType.DEFAULT.getLine().getKey();	// 调拔类型 <OMvType>  BYTE
    _goodsFrom=OGoodsFrom.DEFAULT.getLine().getKey();	// 货物来源 <OGoodsFrom>  BYTE
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _inForm=null;	// 调入单  LONG
    _orgOther=null;	// 调入机构 <表主键:SysOrg>  INT
    _cellOther=null;	// 调入核算单元 <表主键:SysCell>  INT
    _warehouseOther=null;	// 调入仓库 <表主键:GsWarehouse>  INT
    _fromType=OFromType.DEFAULT.getLine().getKey();	// 单据来源 <OFromType>  BYTE
    _amt=ZERO;	// 金额  DEC(16,2)
    _amtCost=ZERO;	// 费用合计  DEC(16,2)
    _billFlag=OBillFlag.DEFAULT.getLine().getKey();	// 开票标准 <OBillFlag>  BYTE
    _shipingMode=OShipingMode.DEFAULT.getLine().getKey();	// 运输方式 <OShipingMode>  BYTE
    _shiping=null;	// 发货信息 <表主键:SysShiping>  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SalMvOut loadUniqueCode(boolean lockFlag,String code) {
    return (SalMvOut)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SalMvOut chkUniqueCode(boolean lockFlag,String code) {
    return (SalMvOut)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Byte getMvType(){
    return _mvType;
  }
  public void setMvType(Byte mvType){
    _mvType=mvType;
  }
  public OMvType gtMvType(){
    return (OMvType)(OMvType.AGREEMENT.getLine().get(_mvType));
  }
  public void stMvType(OMvType mvType){
    _mvType=mvType.getLine().getKey();
  }
  public Byte getGoodsFrom(){
    return _goodsFrom;
  }
  public void setGoodsFrom(Byte goodsFrom){
    _goodsFrom=goodsFrom;
  }
  public OGoodsFrom gtGoodsFrom(){
    return (OGoodsFrom)(OGoodsFrom.WAREHOURSE.getLine().get(_goodsFrom));
  }
  public void stGoodsFrom(OGoodsFrom goodsFrom){
    _goodsFrom=goodsFrom.getLine().getKey();
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
  public Long getInForm(){
    return _inForm;
  }
  public void setInForm(Long inForm){
    _inForm=inForm;
  }
  //外部主键对象: IForm
  public Bean gtInForm(){
    return (Bean)gtLongTbObj(getInForm());
  }
  public void stInForm(Bean inForm){
      setInForm(inForm.gtLongPkey());
  }
  public Integer getOrgOther(){
    return _orgOther;
  }
  public void setOrgOther(Integer orgOther){
    _orgOther=orgOther;
  }
  public SysOrg gtOrgOther(){
    if(getOrgOther()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrgOther());
  }
  public void stOrgOther(SysOrg orgOther){
    if(orgOther==null)
      setOrgOther(null);
    else
      setOrgOther(orgOther.getPkey());
  }
  public Integer getCellOther(){
    return _cellOther;
  }
  public void setCellOther(Integer cellOther){
    _cellOther=cellOther;
  }
  public SysCell gtCellOther(){
    if(getCellOther()==null)
      return null;
    return (SysCell)get(SysCell.class,getCellOther());
  }
  public void stCellOther(SysCell cellOther){
    if(cellOther==null)
      setCellOther(null);
    else
      setCellOther(cellOther.getPkey());
  }
  public Integer getWarehouseOther(){
    return _warehouseOther;
  }
  public void setWarehouseOther(Integer warehouseOther){
    _warehouseOther=warehouseOther;
  }
  public GsWarehouse gtWarehouseOther(){
    if(getWarehouseOther()==null)
      return null;
    return (GsWarehouse)get(GsWarehouse.class,getWarehouseOther());
  }
  public void stWarehouseOther(GsWarehouse warehouseOther){
    if(warehouseOther==null)
      setWarehouseOther(null);
    else
      setWarehouseOther(warehouseOther.getPkey());
  }
  public Byte getFromType(){
    return _fromType;
  }
  public void setFromType(Byte fromType){
    _fromType=fromType;
  }
  public OFromType gtFromType(){
    return (OFromType)(OFromType.NULL.getLine().get(_fromType));
  }
  public void stFromType(OFromType fromType){
    _fromType=fromType.getLine().getKey();
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
