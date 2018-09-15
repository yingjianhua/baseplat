package irille.gl.gs;

import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class ZGsBatch extends BeanLong<ZGsBatch> {
	public static final Tb TB = new Tb(ZGsBatch.class, "存货批次", "存货批次").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		WAREHOUSE(GsWarehouse.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		LOCATION(GsLocation.fldOutKey()),
		BATCH_CODE(SYS.CODE__40, "批次代码", true),
		ENABLED(SYS.ENABLED),
		QTY(SYS.QTY),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		//		(FLDS.),
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		// 索引
		public static final Index IDX_WAREHOUSE_GOODS_LOCATION_BATCH_CODE = TB.addIndex("warehouseGoodsLocationBatchCode", true, WAREHOUSE, GOODS, LOCATION, BATCH_CODE);
		public static final Index IDX_LOCATION = TB.addIndex("location", false, LOCATION);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
	//实例变量定义-----------------------------------------
	private Long _pkey; // 编号  LONG
	private String _code; // 单据号  STR(40)
	private Byte _status; // 状态 <OBillStatus>  BYTE
	// INIT:11,初始
	// VERIFING:53,复核中
	// VERIFIED:58,已复核
	// CHECKING:63,审核中
	// CHECKED:68,已审核
	// VETTING:73,审批中
	// VETTED:78,已审批
	// TALLY_ABLE:83,可记账
	// DONE:98,完成
	// DELETED:99,作废
	private Integer _org; // 机构 <表主键:SysOrg>  INT
	private Integer _dept; // 部门 <表主键:SysDept>  INT
	private Integer _createdBy; // 建档员 <表主键:SysUser>  INT
	private Date _createdTime; // 建档时间  TIME
	private Integer _apprBy; // 审核员 <表主键:SysUser>  INT<null>
	private Date _apprTime; // 审核时间  TIME<null>
	private String _rem; // 备注  STR(200)<null>
	private Integer _warehouse; // 仓库 <表主键:GsWarehouse>  INT
	private Integer _goods; // 货物 <表主键:GsGoods>  INT
	private Integer _location; // 货位 <表主键:GsLocation>  INT
	private String _batchCode; // 批次代码  STR(40)<null>
	private Byte _enabled; // 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
	private BigDecimal _qty; // 数量  DEC(14,4)

	@Override
	public ZGsBatch init() {
		super.init();
		_code = null; // 单据号  STR(40)
		_status = 11; // 状态 <OBillStatus>  BYTE
		_org = null; // 机构 <表主键:SysOrg>  INT
		_dept = null; // 部门 <表主键:SysDept>  INT
		_createdBy = null; // 建档员 <表主键:SysUser>  INT
		_createdTime = Env.getTranBeginTime(); // 建档时间  TIME
		_apprBy = null; // 审核员 <表主键:SysUser>  INT
		_apprTime = null; // 审核时间  TIME
		_rem = null; // 备注  STR(200)
		_warehouse = null; // 仓库 <表主键:GsWarehouse>  INT
		_goods = null; // 货物 <表主键:GsGoods>  INT
		_location = null; // 货位 <表主键:GsLocation>  INT
		_batchCode = null; // 批次代码  STR(40)
		_enabled = 1; // 启用标志 <OEnabled>  BYTE
		_qty = ZERO; // 数量  DEC(14,4)
		return this;
	}

	//方法----------------------------------------------
	public static ZGsBatch loadUniqueWarehouseGoodsLocationBatchCode(boolean lockFlag, Integer warehouse, Integer goods,
	    Integer location, String batchCode) {
		return (ZGsBatch) loadUnique(T.IDX_WAREHOUSE_GOODS_LOCATION_BATCH_CODE, lockFlag, warehouse, goods, location,
		    batchCode);
	}

	public static ZGsBatch chkUniqueWarehouseGoodsLocationBatchCode(boolean lockFlag, Integer warehouse, Integer goods,
	    Integer location, String batchCode) {
		return (ZGsBatch) chkUnique(T.IDX_WAREHOUSE_GOODS_LOCATION_BATCH_CODE, lockFlag, warehouse, goods, location,
		    batchCode);
	}

	public static ZGsBatch loadUniqueLocation(boolean lockFlag, Integer location) {
		return (ZGsBatch) loadUnique(T.IDX_LOCATION, lockFlag, location);
	}

	public static ZGsBatch chkUniqueLocation(boolean lockFlag, Integer location) {
		return (ZGsBatch) chkUnique(T.IDX_LOCATION, lockFlag, location);
	}

	public Long getPkey() {
		return _pkey;
	}

	public void setPkey(Long pkey) {
		_pkey = pkey;
	}

	public String getCode() {
		return _code;
	}

	public void setCode(String code) {
		_code = code;
	}

	public Byte getStatus() {
		return _status;
	}

	public void setStatus(Byte status) {
		_status = status;
	}

	public OBillStatus gtStatus() {
		return (OBillStatus) (OBillStatus.INIT.getLine().get(_status));
	}

	public void stStatus(OBillStatus status) {
		_status = status.getLine().getKey();
	}

	public Integer getOrg() {
		return _org;
	}

	public void setOrg(Integer org) {
		_org = org;
	}

	public SysOrg gtOrg() {
		if (getOrg() == null)
			return null;
		return (SysOrg) get(SysOrg.class, getOrg());
	}

	public void stOrg(SysOrg org) {
		if (org == null)
			setOrg(null);
		else
			setOrg(org.getPkey());
	}

	public Integer getDept() {
		return _dept;
	}

	public void setDept(Integer dept) {
		_dept = dept;
	}

	public SysDept gtDept() {
		if (getDept() == null)
			return null;
		return (SysDept) get(SysDept.class, getDept());
	}

	public void stDept(SysDept dept) {
		if (dept == null)
			setDept(null);
		else
			setDept(dept.getPkey());
	}

	public Integer getCreatedBy() {
		return _createdBy;
	}

	public void setCreatedBy(Integer createdBy) {
		_createdBy = createdBy;
	}

	public SysUser gtCreatedBy() {
		if (getCreatedBy() == null)
			return null;
		return (SysUser) get(SysUser.class, getCreatedBy());
	}

	public void stCreatedBy(SysUser createdBy) {
		if (createdBy == null)
			setCreatedBy(null);
		else
			setCreatedBy(createdBy.getPkey());
	}

	public Date getCreatedTime() {
		return _createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		_createdTime = createdTime;
	}

	public Integer getApprBy() {
		return _apprBy;
	}

	public void setApprBy(Integer apprBy) {
		_apprBy = apprBy;
	}

	public SysUser gtApprBy() {
		if (getApprBy() == null)
			return null;
		return (SysUser) get(SysUser.class, getApprBy());
	}

	public void stApprBy(SysUser apprBy) {
		if (apprBy == null)
			setApprBy(null);
		else
			setApprBy(apprBy.getPkey());
	}

	public Date getApprTime() {
		return _apprTime;
	}

	public void setApprTime(Date apprTime) {
		_apprTime = apprTime;
	}

	public String getRem() {
		return _rem;
	}

	public void setRem(String rem) {
		_rem = rem;
	}

	public Integer getWarehouse() {
		return _warehouse;
	}

	public void setWarehouse(Integer warehouse) {
		_warehouse = warehouse;
	}

	public GsWarehouse gtWarehouse() {
		if (getWarehouse() == null)
			return null;
		return (GsWarehouse) get(GsWarehouse.class, getWarehouse());
	}

	public void stWarehouse(GsWarehouse warehouse) {
		if (warehouse == null)
			setWarehouse(null);
		else
			setWarehouse(warehouse.getPkey());
	}

	public Integer getGoods() {
		return _goods;
	}

	public void setGoods(Integer goods) {
		_goods = goods;
	}

	public GsGoods gtGoods() {
		if (getGoods() == null)
			return null;
		return (GsGoods) get(GsGoods.class, getGoods());
	}

	public void stGoods(GsGoods goods) {
		if (goods == null)
			setGoods(null);
		else
			setGoods(goods.getPkey());
	}

	public Integer getLocation() {
		return _location;
	}

	public void setLocation(Integer location) {
		_location = location;
	}

	public GsLocation gtLocation() {
		if (getLocation() == null)
			return null;
		return (GsLocation) get(GsLocation.class, getLocation());
	}

	public void stLocation(GsLocation location) {
		if (location == null)
			setLocation(null);
		else
			setLocation(location.getPkey());
	}

	public String getBatchCode() {
		return _batchCode;
	}

	public void setBatchCode(String batchCode) {
		_batchCode = batchCode;
	}

	public Byte getEnabled() {
		return _enabled;
	}

	public void setEnabled(Byte enabled) {
		_enabled = enabled;
	}

	public Boolean gtEnabled() {
		return byteToBoolean(_enabled);
	}

	public void stEnabled(Boolean enabled) {
		_enabled = booleanToByte(enabled);
	}

	public BigDecimal getQty() {
		return _qty;
	}

	public void setQty(BigDecimal qty) {
		_qty = qty;
	}

}
