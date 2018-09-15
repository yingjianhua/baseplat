package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.Sys.OCurrency;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;


public class GlDailyLedger extends BeanLong<GlDailyLedger> {
	public static final Tb TB = new Tb(GlDailyLedger.class, "日总账").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		CELL(SYS.CELL),
		SUBJECT(GlSubject.fldOutKey()),
		WORK_DATE(SYS.WORK_DATE),
		CURRENCY(SYS.CURRENCY),
		DR_QTY(SYS.DR_QTY),
		DR_AMT(SYS.DR_AMT),
		CR_QTY(SYS.CR_QTY),
		CR_AMT(SYS.CR_AMT),
		DR_BALANCE(SYS.DR_BALANCE),
		CR_BALANCE(SYS.CR_BALANCE),
		NUM(SYS.CNT,"有效账户数"),
		ORG(SYS.ORG),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CELL_SUBJECT_WORK_DATE = TB.addIndex("cellSubjectWorkDate", 
				true,CELL,SUBJECT,WORK_DATE);
		public static final Index IDX_WORK_DATE_CELL_SUBJECT = TB.addIndex("workDateCellSubject", 
				true,WORK_DATE,CELL,SUBJECT);
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _subject;	// 科目字典 <表主键:GlSubject>  INT
  private Date _workDate;	// 工作日期  DATE
  private Byte _currency;	// 币种 <OCurrency>  BYTE
	// RMB:1,人民币
	// MY:2,美元
	// OY:3,欧元
	// GB:4,港币
	// RY:5,日元
	// HB:6,韩币
  private Integer _drQty;	// 借方发生笔数  INT
  private BigDecimal _drAmt;	// 借方发生额  DEC(16,2)
  private Integer _crQty;	// 贷方发生笔数  INT
  private BigDecimal _crAmt;	// 贷方发生额  DEC(16,2)
  private BigDecimal _drBalance;	// 借方余额  DEC(16,2)
  private BigDecimal _crBalance;	// 贷方余额  DEC(16,2)
  private Integer _num;	// 有效账户数  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlDailyLedger init(){
		super.init();
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _subject=null;	// 科目字典 <表主键:GlSubject>  INT
    _workDate=Env.getWorkDate();	// 工作日期  DATE
    _currency=OCurrency.DEFAULT.getLine().getKey();	// 币种 <OCurrency>  BYTE
    _drQty=0;	// 借方发生笔数  INT
    _drAmt=ZERO;	// 借方发生额  DEC(16,2)
    _crQty=0;	// 贷方发生笔数  INT
    _crAmt=ZERO;	// 贷方发生额  DEC(16,2)
    _drBalance=ZERO;	// 借方余额  DEC(16,2)
    _crBalance=ZERO;	// 贷方余额  DEC(16,2)
    _num=0;	// 有效账户数  INT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlDailyLedger loadUniqueCellSubjectWorkDate(boolean lockFlag,Integer cell,Integer subject,Date workDate) {
    return (GlDailyLedger)loadUnique(T.IDX_CELL_SUBJECT_WORK_DATE,lockFlag,cell,subject,workDate);
  }
  public static GlDailyLedger chkUniqueCellSubjectWorkDate(boolean lockFlag,Integer cell,Integer subject,Date workDate) {
    return (GlDailyLedger)chkUnique(T.IDX_CELL_SUBJECT_WORK_DATE,lockFlag,cell,subject,workDate);
  }
  public static GlDailyLedger loadUniqueWorkDateCellSubject(boolean lockFlag,Date workDate,Integer cell,Integer subject) {
    return (GlDailyLedger)loadUnique(T.IDX_WORK_DATE_CELL_SUBJECT,lockFlag,workDate,cell,subject);
  }
  public static GlDailyLedger chkUniqueWorkDateCellSubject(boolean lockFlag,Date workDate,Integer cell,Integer subject) {
    return (GlDailyLedger)chkUnique(T.IDX_WORK_DATE_CELL_SUBJECT,lockFlag,workDate,cell,subject);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
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
  public Integer getSubject(){
    return _subject;
  }
  public void setSubject(Integer subject){
    _subject=subject;
  }
  public GlSubject gtSubject(){
    if(getSubject()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubject());
  }
  public void stSubject(GlSubject subject){
    if(subject==null)
      setSubject(null);
    else
      setSubject(subject.getPkey());
  }
  public Date getWorkDate(){
    return _workDate;
  }
  public void setWorkDate(Date workDate){
    _workDate=workDate;
  }
  public Byte getCurrency(){
    return _currency;
  }
  public void setCurrency(Byte currency){
    _currency=currency;
  }
  public OCurrency gtCurrency(){
    return (OCurrency)(OCurrency.RMB.getLine().get(_currency));
  }
  public void stCurrency(OCurrency currency){
    _currency=currency.getLine().getKey();
  }
  public Integer getDrQty(){
    return _drQty;
  }
  public void setDrQty(Integer drQty){
    _drQty=drQty;
  }
  public BigDecimal getDrAmt(){
    return _drAmt;
  }
  public void setDrAmt(BigDecimal drAmt){
    _drAmt=drAmt;
  }
  public Integer getCrQty(){
    return _crQty;
  }
  public void setCrQty(Integer crQty){
    _crQty=crQty;
  }
  public BigDecimal getCrAmt(){
    return _crAmt;
  }
  public void setCrAmt(BigDecimal crAmt){
    _crAmt=crAmt;
  }
  public BigDecimal getDrBalance(){
    return _drBalance;
  }
  public void setDrBalance(BigDecimal drBalance){
    _drBalance=drBalance;
  }
  public BigDecimal getCrBalance(){
    return _crBalance;
  }
  public void setCrBalance(BigDecimal crBalance){
    _crBalance=crBalance;
  }
  public Integer getNum(){
    return _num;
  }
  public void setNum(Integer num){
    _num=num;
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
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
