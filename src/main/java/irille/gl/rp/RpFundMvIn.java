package irille.gl.rp;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.BeanBill;
import irille.pub.bean.CmbBill;
import irille.pub.gl.TallyLines;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class RpFundMvIn extends BeanBill<RpFundMvIn> {
	private static final Log LOG = new Log(RpFundMvIn.class);

	public static final Tb TB = new Tb(RpFundMvIn.class, "资金调入单").setAutoIncrement().addActIUDL().addActApprove()
	    .addActOpt("doIn", "调入").addActOpt("unIn", "调入取消").addActNote().addActTally();

	public enum T implements IEnumFld {//@formatter:off
		CMB_BILL(CmbBill.fldFlds()),
		AMT(SYS.AMT),
		JOURNAL(RpJournal.fldOutKey().setNull()),
		SUMMARY(SYS.SUMMARY__200_NULL, false),
		NAME(SYS.NAME__100_NULL, "存款人"),
		BANK_NAME(SYS.NAME__100_NULL, "银行账号"),
		DOC_NUM(SYS.DOC_NUM__NULL),
		DOC_DATE(SYS.DOC_DATE__NULL),
		MVOUT(RpFundMvOut.fldOutKey().setNull()),
		CASHIER(SYS.CASHIER),
		IN_TIME(SYS.TIME__NULL, "调入时间"),
		IS_APPR(SYS.NY, "调出审核标识"),
		IS_CRT(SYS.NY, "是否发起方"),
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
		T.CMB_BILL.getFld().getTb().lockAllFlds();
	}
	//@formatter:on

	public void getAccObjs(String name, irille.pub.gl.AccObjs objs) {
	};

	@Override
	public void initTallyLines(TallyLines ls) {
	}

	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = Tb.crtOutKey(TB, code, name);
		fld.setType(null);
		return fld;
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
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private Long _journal;	// 出纳日记账 <表主键:RpJournal>  LONG<null>
  private String _summary;	// 摘要  STR(200)
  private String _name;	// 存款人  STR(100)<null>
  private String _bankName;	// 银行账号  STR(100)<null>
  private String _docNum;	// 票据号  STR(40)<null>
  private Date _docDate;	// 票据日期  DATE<null>
  private Long _mvout;	// 资金调出单 <表主键:RpFundMvOut>  LONG<null>
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT
  private Date _inTime;	// 调入时间  TIME<null>
  private Byte _isAppr;	// 调出审核标识 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _isCrt;	// 是否发起方 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpFundMvIn init(){
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
    _amt=ZERO;	// 金额  DEC(16,2)
    _journal=null;	// 出纳日记账 <表主键:RpJournal>  LONG
    _summary=null;	// 摘要  STR(200)
    _name=null;	// 存款人  STR(100)
    _bankName=null;	// 银行账号  STR(100)
    _docNum=null;	// 票据号  STR(40)
    _docDate=null;	// 票据日期  DATE
    _mvout=null;	// 资金调出单 <表主键:RpFundMvOut>  LONG
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _inTime=null;	// 调入时间  TIME
    _isAppr=OYn.DEFAULT.getLine().getKey();	// 调出审核标识 <OYn>  BYTE
    _isCrt=OYn.DEFAULT.getLine().getKey();	// 是否发起方 <OYn>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static RpFundMvIn loadUniqueCode(boolean lockFlag,String code) {
    return (RpFundMvIn)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static RpFundMvIn chkUniqueCode(boolean lockFlag,String code) {
    return (RpFundMvIn)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public Long getJournal(){
    return _journal;
  }
  public void setJournal(Long journal){
    _journal=journal;
  }
  public RpJournal gtJournal(){
    if(getJournal()==null)
      return null;
    return (RpJournal)get(RpJournal.class,getJournal());
  }
  public void stJournal(RpJournal journal){
    if(journal==null)
      setJournal(null);
    else
      setJournal(journal.getPkey());
  }
  public String getSummary(){
    return _summary;
  }
  public void setSummary(String summary){
    _summary=summary;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getBankName(){
    return _bankName;
  }
  public void setBankName(String bankName){
    _bankName=bankName;
  }
  public String getDocNum(){
    return _docNum;
  }
  public void setDocNum(String docNum){
    _docNum=docNum;
  }
  public Date getDocDate(){
    return _docDate;
  }
  public void setDocDate(Date docDate){
    _docDate=docDate;
  }
  public Long getMvout(){
    return _mvout;
  }
  public void setMvout(Long mvout){
    _mvout=mvout;
  }
  public RpFundMvOut gtMvout(){
    if(getMvout()==null)
      return null;
    return (RpFundMvOut)get(RpFundMvOut.class,getMvout());
  }
  public void stMvout(RpFundMvOut mvout){
    if(mvout==null)
      setMvout(null);
    else
      setMvout(mvout.getPkey());
  }
  public Integer getCashier(){
    return _cashier;
  }
  public void setCashier(Integer cashier){
    _cashier=cashier;
  }
  public SysUser gtCashier(){
    if(getCashier()==null)
      return null;
    return (SysUser)get(SysUser.class,getCashier());
  }
  public void stCashier(SysUser cashier){
    if(cashier==null)
      setCashier(null);
    else
      setCashier(cashier.getPkey());
  }
  public Date getInTime(){
    return _inTime;
  }
  public void setInTime(Date inTime){
    _inTime=inTime;
  }
  public Byte getIsAppr(){
    return _isAppr;
  }
  public void setIsAppr(Byte isAppr){
    _isAppr=isAppr;
  }
  public Boolean gtIsAppr(){
    return byteToBoolean(_isAppr);
  }
  public void stIsAppr(Boolean isAppr){
    _isAppr=booleanToByte(isAppr);
  }
  public Byte getIsCrt(){
    return _isCrt;
  }
  public void setIsCrt(Byte isCrt){
    _isCrt=isCrt;
  }
  public Boolean gtIsCrt(){
    return byteToBoolean(_isCrt);
  }
  public void stIsCrt(Boolean isCrt){
    _isCrt=booleanToByte(isCrt);
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
