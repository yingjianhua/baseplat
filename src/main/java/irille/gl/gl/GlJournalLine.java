package irille.gl.gl;

import irille.pub.bean.BeanLong;
import irille.pub.gl.IJournalLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class GlJournalLine extends BeanLong implements IJournalLine{
	public static final Tb TB = new Tb(GlJournalLine.class, "普通分户账明细").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		DAYBOOK_LINE(GlDaybookLine.fldOneToOne()),  //XXX
		MAIN_PKEY(Tb.crtOutKey(GlJournal.class, "mainKey", "分户账")),
		BALANCE(SYS.BALANCE),
		TALLY_DATE(SYS.TALLY_DATE__NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		LINES(Tb.crtLines(T.MAIN_PKEY, CN_LINES,	T.PKEY))
		;
		// 索引
	public static final Index IDX_MAIN_DATE_PKEY = TB.addIndex("mainDatePkey", true,
			MAIN_PKEY, TALLY_DATE,PKEY);

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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _mainPkey;	// 分户账 <表主键:GlJournal>  LONG
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private Date _tallyDate;	// 记账日期  DATE<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlJournalLine init(){
		super.init();
    _mainPkey=null;	// 分户账 <表主键:GlJournal>  LONG
    _balance=ZERO;	// 余额  DEC(16,2)
    _tallyDate=null;	// 记账日期  DATE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlJournalLine loadUniqueMainDatePkey(boolean lockFlag,Long mainPkey,Date tallyDate,Long pkey) {
    return (GlJournalLine)loadUnique(T.IDX_MAIN_DATE_PKEY,lockFlag,mainPkey,tallyDate,pkey);
  }
  public static GlJournalLine chkUniqueMainDatePkey(boolean lockFlag,Long mainPkey,Date tallyDate,Long pkey) {
    return (GlJournalLine)chkUnique(T.IDX_MAIN_DATE_PKEY,lockFlag,mainPkey,tallyDate,pkey);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  //取一对一表对象: GlDaybookLine
  public GlDaybookLine gtDaybookLine(){
    return get(GlDaybookLine.class,getPkey());
  }
  public void stDaybookLine(GlDaybookLine daybookLine){
      setPkey(daybookLine.getPkey());
  }
  public Long getMainPkey(){
    return _mainPkey;
  }
  public void setMainPkey(Long mainPkey){
    _mainPkey=mainPkey;
  }
  public GlJournal gtMainPkey(){
    if(getMainPkey()==null)
      return null;
    return (GlJournal)get(GlJournal.class,getMainPkey());
  }
  public void stMainPkey(GlJournal mainPkey){
    if(mainPkey==null)
      setMainPkey(null);
    else
      setMainPkey(mainPkey.getPkey());
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public Date getTallyDate(){
    return _tallyDate;
  }
  public void setTallyDate(Date tallyDate){
    _tallyDate=tallyDate;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }
  public static java.util.List<GlJournalLine> getLines(irille.gl.gl.GlJournal mainBean){
    return list(irille.gl.gl.GlJournalLine.class,
        " main_pkey=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<GlJournalLine> getLines(irille.gl.gl.GlJournal mainBean, int idx,int count){
    return list(irille.gl.gl.GlJournalLine.class,false," main_pkey=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.gl.gl.GlJournal mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM gl_journal_line WHERE main_pkey=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
