  package irille.gl.gs;

import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.pub.bean.BeanForm;
import irille.pub.bean.CmbForm;
import irille.pub.bean.IForm;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class ZGsDemandDirect extends BeanForm<ZGsDemandDirect> {
	public static final Tb TB = new Tb(ZGsDemandDirect.class, "直销需求清单").setAutoLocal().addActList();

	public enum T implements IEnumFld {//@formatter:off
		CMB_FORM(CmbForm.fldFlds()),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		ORIG_DEPT(SYS.DEPT, "源单据部门"),
		ORIG_ORG(SYS.ORG, "源单据机构"),
		ORIG_CELL(SYS.CELL,"源单据核算单元"),
		PO_FORM(SYS.ORIG_FORM__CODE, "供应单", true),
	  //>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		PKEY(TB.get("pkey")),
		CODE(TB.get("code")),
		STATUS(TB.get("status")),
		ORG(TB.get("org")),
		DEPT(TB.get("dept")),
		CREATED_BY(TB.get("createdBy")),
		CREATED_TIME(TB.get("createdTime")),
		APPR_BY(TB.get("apprBy")),
		APPR_TIME(TB.get("apprTime")),
		REM(TB.get("rem")),
		ORIG_FORM_NUM(TB.get("origFormNum")),
		PO_FORM_NUM(TB.get("poFormNum")),
		;
		public static final Index IDX_CODE= TB.addIndex("code",true,CODE);
		public static final Index IDX_ORG_TIME= TB.addIndex("orgTime",false,ORG,CREATED_TIME);
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
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
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CMB_FORM.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 单据号  STR(40)
  private Byte _status;	// 状态 <OBillStatus>  BYTE
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
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Integer _origDept;	// 源单据部门 <表主键:SysDept>  INT
  private Integer _origOrg;	// 源单据机构 <表主键:SysOrg>  INT
  private Long _poForm;	// 供应单  LONG<null>
  private String _poFormNum;	// 供应单号  STR(40)<null>

	@Override
  public ZGsDemandDirect init(){
		super.init();
    _code=null;	// 单据号  STR(40)
    _status=11;	// 状态 <OBillStatus>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _createdBy=null;	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _rem=null;	// 备注  STR(200)
    _origForm=(long)0;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _origDept=null;	// 源单据部门 <表主键:SysDept>  INT
    _origOrg=null;	// 源单据机构 <表主键:SysOrg>  INT
    _poForm=(long)0;	// 供应单  LONG
    _poFormNum=null;	// 供应单号  STR(40)
    return this;
  }

  //方法----------------------------------------------
  public static ZGsDemandDirect loadUniqueCode(boolean lockFlag,String code) {
    return (ZGsDemandDirect)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static ZGsDemandDirect chkUniqueCode(boolean lockFlag,String code) {
    return (ZGsDemandDirect)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public Long getOrigForm(){
    return _origForm;
  }
  public void setOrigForm(Long origForm){
    _origForm=origForm;
  }
  //外部主键对象: IForm
  public IForm gtOrigForm(){
    return (IForm)gtLongTbObj(getOrigForm());
  }
  public void stOrigForm(IForm origForm){
      setOrigForm(origForm.getPkey());
  }
  public String getOrigFormNum(){
    return _origFormNum;
  }
  public void setOrigFormNum(String origFormNum){
    _origFormNum=origFormNum;
  }
  public Integer getOrigDept(){
    return _origDept;
  }
  public void setOrigDept(Integer origDept){
    _origDept=origDept;
  }
  public SysDept gtOrigDept(){
    if(getOrigDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getOrigDept());
  }
  public void stOrigDept(SysDept origDept){
    if(origDept==null)
      setOrigDept(null);
    else
      setOrigDept(origDept.getPkey());
  }
  public Integer getOrigOrg(){
    return _origOrg;
  }
  public void setOrigOrg(Integer origOrg){
    _origOrg=origOrg;
  }
  public SysOrg gtOrigOrg(){
    if(getOrigOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrigOrg());
  }
  public void stOrigOrg(SysOrg origOrg){
    if(origOrg==null)
      setOrigOrg(null);
    else
      setOrigOrg(origOrg.getPkey());
  }
  public Long getPoForm(){
    return _poForm;
  }
  public void setPoForm(Long poForm){
    _poForm=poForm;
  }
  //外部主键对象: IForm
  public IForm gtPoForm(){
    return (IForm)gtLongTbObj(getPoForm());
  }
  public void stPoForm(IForm poForm){
      setPoForm(poForm.getPkey());
  }
  public String getPoFormNum(){
    return _poFormNum;
  }
  public void setPoFormNum(String poFormNum){
    _poFormNum=poFormNum;
  }

}
