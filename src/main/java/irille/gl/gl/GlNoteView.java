package irille.gl.gl;

import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OWriteoffFlag;
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
 * 入库时的操作明细表，如何使用待定
 * @author whx
 * @version 创建时间：2014年8月25日 上午10:22:10
 */
public class GlNoteView extends BeanLong<GlNoteView> {
	public static final Tb TB = new TbView(GlNoteView.class, "记账条视图").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
//		NOTE(GlNote.fldViewFlds()),
//		WRITEOFF(GlNoteWriteoff.fldViewFlds()),
//		
		PKEY(TB.crtLongPkey()),
		BILL(SYS.BILL),
		JOURNAL(SYS.JOURNAL),
		DIRECT(GL.DIRECT),
		STATUS(SYS.STATUS),
		AMT(SYS.AMT,"金额"),
		DOC_NUM(SYS.DOC_NUM__NULL),
		SUMMARY(SYS.SUMMARY__100_NULL),
		WRITEOFF_FLAG(TB.crt(OWriteoffFlag.DEFAULT)),
		WRITEOFF(Tb.crtOutKey(GlNoteWriteoff.class, "writeoff", "销账计划")),
		DATE_START(SYS.DATE__NULL,"起始日期"),
		DATE_DUE(SYS.DATE__NULL,"到期日期"),
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
  private Long _journal;	// 分户账 <表主键:GlJournal>  LONG
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
  private Byte _writeoffFlag;	// 销账标志 <OWriteoffFlag>  BYTE
	// WRITEOFF_NO:1,不销账
	// WRITEOFF_NEW:2,新建销账计划
	// WRITEOFF_WRITE:3,核销
  private Long _writeoff;	// 销账计划 <表主键:GlNoteWriteoff>  LONG
  private Date _dateStart;	// 起始日期  DATE<null>
  private Date _dateDue;	// 到期日期  DATE<null>
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>

	@Override
  public GlNoteView init(){
		super.init();
    _bill=null;	// 凭证  LONG
    _journal=null;	// 分户账 <表主键:GlJournal>  LONG
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _amt=ZERO;	// 金额  DEC(16,2)
    _docNum=null;	// 票据号  STR(40)
    _summary=null;	// 摘要  STR(100)
    _writeoffFlag=OWriteoffFlag.DEFAULT.getLine().getKey();	// 销账标志 <OWriteoffFlag>  BYTE
    _writeoff=null;	// 销账计划 <表主键:GlNoteWriteoff>  LONG
    _dateStart=null;	// 起始日期  DATE
    _dateDue=null;	// 到期日期  DATE
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
  public GlJournal gtJournal(){
    if(getJournal()==null)
      return null;
    return (GlJournal)get(GlJournal.class,getJournal());
  }
  public void stJournal(GlJournal journal){
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
  public Byte getWriteoffFlag(){
    return _writeoffFlag;
  }
  public void setWriteoffFlag(Byte writeoffFlag){
    _writeoffFlag=writeoffFlag;
  }
  public OWriteoffFlag gtWriteoffFlag(){
    return (OWriteoffFlag)(OWriteoffFlag.WRITEOFF_NO.getLine().get(_writeoffFlag));
  }
  public void stWriteoffFlag(OWriteoffFlag writeoffFlag){
    _writeoffFlag=writeoffFlag.getLine().getKey();
  }
  public Long getWriteoff(){
    return _writeoff;
  }
  public void setWriteoff(Long writeoff){
    _writeoff=writeoff;
  }
  public GlNoteWriteoff gtWriteoff(){
    if(getWriteoff()==null)
      return null;
    return (GlNoteWriteoff)get(GlNoteWriteoff.class,getWriteoff());
  }
  public void stWriteoff(GlNoteWriteoff writeoff){
    if(writeoff==null)
      setWriteoff(null);
    else
      setWriteoff(writeoff.getPkey());
  }
  public Date getDateStart(){
    return _dateStart;
  }
  public void setDateStart(Date dateStart){
    _dateStart=dateStart;
  }
  public Date getDateDue(){
    return _dateDue;
  }
  public void setDateDue(Date dateDue){
    _dateDue=dateDue;
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
