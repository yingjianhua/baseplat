package irille.gl.gl;

import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.gl.Gl.ODirect;
import irille.gl.rp.Rp;
import irille.gl.rp.RpJournal;
import irille.gl.rp.Rp.OPayType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 待收付款登记菜单中，作明细显示
 * @author whx
 * @version 创建时间：2015年2月15日 下午4:09:09
 */
public class GlNoteViewRp extends BeanLong<GlNoteViewRp> {
	public static final Tb TB = new TbView(GlNoteViewRp.class, "记账条视图").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		BILL(SYS.BILL),
		JOURNAL(RpJournal.fldOutKey()),
		DIRECT(GL.DIRECT),
		STATUS(SYS.STATUS),
		AMT(SYS.AMT,"金额"),
		DOC_NUM(SYS.DOC_NUM__NULL),
		SUMMARY(SYS.SUMMARY__100_NULL),
		TYPE(TB.crt(Rp.OPayType.DEFAULT).setNull()),
		TYPE_TIME(SYS.DATE_TIME__NULL, "付款时间"),
		TYPE_DES(SYS.STR__200_NULL, "付款说明"),
		CASHIER(SYS.USER_SYS, "出纳",true),
		CREATED_BY(SYS.CREATED_BY),	
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		APPR_BY(SYS.APPR_BY_NULL), //审核员
		APPR_TIME(SYS.APPR_DATE_TIME__NULL), //审核日期
		REM(SYS.REM__200_NULL),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
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
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _bill;	// 凭证  LONG
  private Long _journal;	// 出纳日记账 <表主键:RpJournal>  LONG
  private Byte _direct;	// 借贷标志 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
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
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private String _docNum;	// 票据号  STR(40)<null>
  private String _summary;	// 摘要  STR(100)<null>
  private Byte _type;	// 付款方式 <OPayType>  BYTE<null>
	// CASH:0,现金
	// BANK_NET:1,网银
	// BANK_TRAN:2,转账支票
	// BANK_T_TRAN:21,电汇
	// BANK_CASH:22,现金支票
	// BANK_CONSIGN:31,委托付款
	// BANK_AUTO:32,自动划转
	// OTHER:99,其他
  private Date _typeTime;	// 付款时间  TIME<null>
  private String _typeDes;	// 付款说明  STR(200)<null>
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT<null>
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>

	@Override
  public GlNoteViewRp init(){
		super.init();
    _bill=null;	// 凭证  LONG
    _journal=null;	// 出纳日记账 <表主键:RpJournal>  LONG
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _amt=ZERO;	// 金额  DEC(16,2)
    _docNum=null;	// 票据号  STR(40)
    _summary=null;	// 摘要  STR(100)
    _type=OPayType.DEFAULT.getLine().getKey();	// 付款方式 <OPayType>  BYTE
    _typeTime=null;	// 付款时间  TIME
    _typeDes=null;	// 付款说明  STR(200)
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _rem=null;	// 备注  STR(200)
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Long getBill(){
    return _bill;
  }
  public void setBill(Long bill){
    _bill=bill;
  }
  //外部主键对象: IBill
  public Bean gtBill(){
    return (Bean)gtLongTbObj(getBill());
  }
  public void stBill(Bean bill){
      setBill(bill.gtLongPkey());
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
  public Byte getDirect(){
    return _direct;
  }
  public void setDirect(Byte direct){
    _direct=direct;
  }
  public ODirect gtDirect(){
    return (ODirect)(ODirect.CR.getLine().get(_direct));
  }
  public void stDirect(ODirect direct){
    _direct=direct.getLine().getKey();
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
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public String getDocNum(){
    return _docNum;
  }
  public void setDocNum(String docNum){
    _docNum=docNum;
  }
  public String getSummary(){
    return _summary;
  }
  public void setSummary(String summary){
    _summary=summary;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OPayType gtType(){
    return (OPayType)(OPayType.BANK_NET.getLine().get(_type));
  }
  public void stType(OPayType type){
    _type=type.getLine().getKey();
  }
  public Date getTypeTime(){
    return _typeTime;
  }
  public void setTypeTime(Date typeTime){
    _typeTime=typeTime;
  }
  public String getTypeDes(){
    return _typeDes;
  }
  public void setTypeDes(String typeDes){
    _typeDes=typeDes;
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

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
