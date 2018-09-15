package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.core.sys.Sys.OCurrency;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OTallyState;
import irille.pub.IPubVars;
import irille.pub.bean.BeanLong;
import irille.pub.gl.IJournalLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;

/**
 * 财务.流水账
 * @author whx
 */
public class GlDaybookLine extends BeanLong<GlDaybookLine> implements IPubVars{
	public static final Tb TB = new Tb(GlDaybookLine.class, "流水明细").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //根据主表的主键前半段+序号 产生本表的主键！！
		JOURNAL(SYS.JOURNAL),
		TALLY_STATE(TB.crt(Gl.OTallyState.DEFAULT)),
		DIRECT(GL.DIRECT),
		AMT(SYS.AMT),
		CURRENCY(SYS.CURRENCY),
		SUMMARY(SYS.SUMMARY__200_NULL),
		IN_FLAG(GL.IN_FLAG),
		DOC_NUM(SYS.DOC_NUM__NULL),
		CELL(SYS.CELL), //此值为JOURNAL对象的所属Cell
		AGENT_CELL(SYS.CELL,"代理核算单元"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
//		public static final Index IDX_DATE_SORT = TB.addIndex("dateSort", true,WORK_DATE,SORT);
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	public void setToJouranlLine(GlDaybook daybook, GlJournal journal, IJournalLine jl) {
		jl.init();
		jl.setBalance(journal.getBalance());
		jl.setMainPkey(journal.getPkey());
		jl.setTallyDate(daybook.getWorkDate());
	}
	
//	public GlDaybookLine copyFromTallyNote(TallyNote note) {
//		IJl jl = note.gtJournal();
//		init();
//		stJournal(jl);
//		setDirect(note.getDirect());
//		setAmt(note.getAmt());
//		setSummary(note.getSummary());
//		stInFlag(jl.gtInFlag());
//		setDocNum(note.getDocNum());
//		setCell(jl.getCell());
//		setAgentCell(note.getTally().getBill().getCell());
//
//		switch(jl.gtTallyFlag()) {
//		case ONE:
//		case AUTO:
//			stTallyState(OTallyState.DO);
//			break;
//		case FORM:
//			stTallyState(OTallyState.WAIT);
//			break;
//		}
//		return this;
//	}

	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _journal;	// 分户账 <表主键:GlJournal>  LONG
  private Byte _tallyState;	// 入账标志 <OTallyState>  BYTE
	// INIT:0,初始
	// WAIT_TOTAL:12,待被汇总入账
	// DONE_ONE:21,已入账-单笔
	// DONE_TOTAL_BY:22,已被汇总入账
	// DONE_TOTAL:23,汇总入账
  private Byte _direct;	// 借贷标志 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private Byte _currency;	// 币种 <OCurrency>  BYTE
	// RMB:1,人民币
	// MY:2,美元
	// OY:3,欧元
	// GB:4,港币
	// RY:5,日元
	// HB:6,韩币
  private String _summary;	// 摘要  STR(200)<null>
  private Byte _inFlag;	// 表内标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private String _docNum;	// 票据号  STR(40)<null>
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _agentCell;	// 代理核算单元 <表主键:SysCell>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlDaybookLine init(){
		super.init();
    _journal=null;	// 分户账 <表主键:GlJournal>  LONG
    _tallyState=OTallyState.DEFAULT.getLine().getKey();	// 入账标志 <OTallyState>  BYTE
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _amt=ZERO;	// 金额  DEC(16,2)
    _currency=OCurrency.DEFAULT.getLine().getKey();	// 币种 <OCurrency>  BYTE
    _summary=null;	// 摘要  STR(200)
    _inFlag=OYn.DEFAULT.getLine().getKey();	// 表内标志 <OYn>  BYTE
    _docNum=null;	// 票据号  STR(40)
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _agentCell=null;	// 代理核算单元 <表主键:SysCell>  INT
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
  public Byte getTallyState(){
    return _tallyState;
  }
  public void setTallyState(Byte tallyState){
    _tallyState=tallyState;
  }
  public OTallyState gtTallyState(){
    return (OTallyState)(OTallyState.INIT.getLine().get(_tallyState));
  }
  public void stTallyState(OTallyState tallyState){
    _tallyState=tallyState.getLine().getKey();
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
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
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
  public String getSummary(){
    return _summary;
  }
  public void setSummary(String summary){
    _summary=summary;
  }
  public Byte getInFlag(){
    return _inFlag;
  }
  public void setInFlag(Byte inFlag){
    _inFlag=inFlag;
  }
  public Boolean gtInFlag(){
    return byteToBoolean(_inFlag);
  }
  public void stInFlag(Boolean inFlag){
    _inFlag=booleanToByte(inFlag);
  }
  public String getDocNum(){
    return _docNum;
  }
  public void setDocNum(String docNum){
    _docNum=docNum;
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
  public Integer getAgentCell(){
    return _agentCell;
  }
  public void setAgentCell(Integer agentCell){
    _agentCell=agentCell;
  }
  public SysCell gtAgentCell(){
    if(getAgentCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getAgentCell());
  }
  public void stAgentCell(SysCell agentCell){
    if(agentCell==null)
      setAgentCell(null);
    else
      setAgentCell(agentCell.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
