package irille.gl.rp;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.gl.gl.GlNote;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class RpJournalLine extends BeanLong<RpJournalLine> {
	private static final Log LOG = new Log(RpJournalLine.class);

	public static final Tb TB = new Tb(RpJournalLine.class, "出纳行明细行", "出纳行明细行").setAutoIncrement();

	public enum ODC implements IEnumOpt {//@formatter:off
		DR(1,"收"),CR(2,"付");
		public static final String NAME="收付标志";
		public static final ODC DEFAULT = DR ; // 定义缺省值
		private EnumLine _line;
		private ODC(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on
	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		ORG(SYS.ORG),
		CELL(SYS.CELL),
		JOURNAL(RpJournal.fldOutKey()),
		BILL(SYS.BILL),
		TYPE(SYS.STR__10,"交易类型"),//现金转账等
		AMT(SYS.AMT,"金额"),
		DC(Tb.crt(ODC.DEFAULT)),
		BALANCE(SYS.BALANCE),
		DOC(SYS.DOC_NUM__NULL),
		SUMMARY(SYS.SUMMARY__100_NULL),
		CASHIER(SYS.USER_SYS, "出纳"),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		NOTE(SYS.NOTE),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_JOURNAL_CREATED_TIME_PKEY = TB.addIndex("journalCreateTimePkey",
				false, JOURNAL,CREATED_TIME,PKEY);
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb();//加锁所有字段,不可以修改
	}
	//@formatter:on
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Long _journal;	// 出纳日记账 <表主键:RpJournal>  LONG
  private Long _bill;	// 凭证  LONG
  private String _type;	// 交易类型  STR(10)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private Byte _dC;	// 收付标志 <ODC>  BYTE
	// DR:1,收
	// CR:2,付
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private String _doc;	// 票据号  STR(40)<null>
  private String _summary;	// 摘要  STR(100)<null>
  private Integer _cashier;	// 出纳 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Long _note;	// 凭条 <表主键:GlNote>  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpJournalLine init(){
		super.init();
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _journal=null;	// 出纳日记账 <表主键:RpJournal>  LONG
    _bill=null;	// 凭证  LONG
    _type=null;	// 交易类型  STR(10)
    _amt=ZERO;	// 金额  DEC(16,2)
    _dC=ODC.DEFAULT.getLine().getKey();	// 收付标志 <ODC>  BYTE
    _balance=ZERO;	// 余额  DEC(16,2)
    _doc=null;	// 票据号  STR(40)
    _summary=null;	// 摘要  STR(100)
    _cashier=null;	// 出纳 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _note=null;	// 凭条 <表主键:GlNote>  LONG
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
  public String getType(){
    return _type;
  }
  public void setType(String type){
    _type=type;
  }
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public Byte getDC(){
    return _dC;
  }
  public void setDC(Byte dC){
    _dC=dC;
  }
  public ODC gtDC(){
    return (ODC)(ODC.DR.getLine().get(_dC));
  }
  public void stDC(ODC dC){
    _dC=dC.getLine().getKey();
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public String getDoc(){
    return _doc;
  }
  public void setDoc(String doc){
    _doc=doc;
  }
  public String getSummary(){
    return _summary;
  }
  public void setSummary(String summary){
    _summary=summary;
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
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public Long getNote(){
    return _note;
  }
  public void setNote(Long note){
    _note=note;
  }
  public GlNote gtNote(){
    if(getNote()==null)
      return null;
    return (GlNote)get(GlNote.class,getNote());
  }
  public void stNote(GlNote note){
    if(note==null)
      setNote(null);
    else
      setNote(note.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
