package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.SysUser;
import irille.core.sys.Sys.OBillStatus;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl.ODirect;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.gl.AccObjs;
import irille.pub.gl.ITallyBean;
import irille.pub.gl.NoteProperty;
import irille.pub.gl.SplitType;
import irille.pub.gl.TallyLines;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class GlNote extends BeanLong<GlNote> implements ITallyBean {
	private static final Log LOG = new Log(GlNote.class);

	public static final NoteProperty NOTE_PROPERTY = NoteProperty.J; // 属性
	public static final SplitType SPLIT_TYPE = SplitType.WEIGHT; // 分摊方式

	public static final Tb TB = new Tb(GlNote.class, "记账条").setAutoIncrement().addActApprove().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		BILL(SYS.BILL),
		EXT_TABLE(SYS.TABLE_ID,"扩展属性表", true),  //如为简单金额型的NOTE，则没有扩展属性，此字段为本表的TABLE_ID
		JOURNAL(SYS.JOURNAL),
		ORG(SYS.ORG),
		DEPT(SYS.DEPT),
		CELL(SYS.CELL),
		DIRECT(GL.DIRECT),
		STATUS(SYS.STATUS),
		AMT(SYS.AMT,"金额"),
		DOC_NUM(SYS.DOC_NUM__NULL),
		SUMMARY(SYS.SUMMARY__100_NULL),
		CREATED_BY(SYS.CREATED_BY),	
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		APPR_BY(SYS.APPR_BY_NULL), //审核员
		APPR_TIME(SYS.APPR_DATE_TIME__NULL), //审核日期
		REM(SYS.REM__200_NULL),
		IS_AUTO(SYS.YN, "是否自动产生"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_BILL_PKEY= TB.addIndex("billPkey",false,
				BILL,PKEY);
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
		T.STATUS.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	public static Fld fldOutKey() {
		return fldOutKey(MAIN_PKEY, TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	
	public static Fld fldViewFlds() {
		return Tb.crtFlds("note",TB,T.EXT_TABLE);
	}
	
	@Override
	public void getAccObjs(String name, AccObjs objs) {
	}

	@Override
	public void initTallyLines(TallyLines ls) {
		ls.addByJournal(gtJournal()).set(_amt, _direct, _docNum, _summary);
	}

	public GlNote(BigDecimal amt,byte direct,String docNum,String summary) {
		_amt=amt;
		_docNum=docNum;
		_summary=summary;
		_direct=direct;
	}
	
  public GlNote(){}

	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _bill;	// 凭证  LONG
  private Integer _extTable;	// 扩展属性表 <表主键:SysTable>  INT<null>
  private Long _journal;	// 分户账 <表主键:GlJournal>  LONG
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
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
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Integer _apprBy;	// 审核员 <表主键:SysUser>  INT<null>
  private Date _apprTime;	// 审核时间  TIME<null>
  private String _rem;	// 备注  STR(200)<null>
  private Byte _isAuto;	// 是否自动产生 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlNote init(){
		super.init();
    _bill=null;	// 凭证  LONG
    _extTable=null;	// 扩展属性表 <表主键:SysTable>  INT
    _journal=null;	// 分户账 <表主键:GlJournal>  LONG
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _status=OBillStatus.DEFAULT.getLine().getKey();	// 状态 <OBillStatus>  BYTE
    _amt=ZERO;	// 金额  DEC(16,2)
    _docNum=null;	// 票据号  STR(40)
    _summary=null;	// 摘要  STR(100)
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _apprBy=null;	// 审核员 <表主键:SysUser>  INT
    _apprTime=null;	// 审核时间  TIME
    _rem=null;	// 备注  STR(200)
    _isAuto=OYn.DEFAULT.getLine().getKey();	// 是否自动产生 <OYn>  BYTE
    _rowVersion=0;	// 版本  SHORT
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
  public Integer getExtTable(){
    return _extTable;
  }
  public void setExtTable(Integer extTable){
    _extTable=extTable;
  }
  public SysTable gtExtTable(){
    if(getExtTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getExtTable());
  }
  public void stExtTable(SysTable extTable){
    if(extTable==null)
      setExtTable(null);
    else
      setExtTable(extTable.getPkey());
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
  public Byte getIsAuto(){
    return _isAuto;
  }
  public void setIsAuto(Byte isAuto){
    _isAuto=isAuto;
  }
  public Boolean gtIsAuto(){
    return byteToBoolean(_isAuto);
  }
  public void stIsAuto(Boolean isAuto){
    _isAuto=booleanToByte(isAuto);
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
