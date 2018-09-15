package irille.gl.gl;

import irille.gl.gl.Gl.ODirect;
import irille.gl.gl.Gl.OSymbolType;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class GlReportLine extends BeanLong<GlReportLine> {
	public static final Tb TB = new Tb(GlReportLine.class, "报表设置明细").setAutoLocal();
	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //根据主表的主健前半段+序号 产生本表的主键！！
		REPORT(TB.crtOutKey(GlReport.class, "report", "报表")),
		SUBJECT(SYS.SUBJECT),
		DIRECT(GL.DIRECT),
		SYMBOL_TYPE(Tb.crt(Gl.OSymbolType.DEFAULT)),
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _report;	// 报表 <表主键:GlReport>  LONG
  private Integer _subject;	// 科目 <表主键:GlSubject>  INT
  private Byte _direct;	// 借贷标志 <ODirect>  BYTE
	// DR:1,借方
	// CR:2,贷方
  private Byte _symbolType;	// 加减类型 <OSymbolType>  BYTE
	// ADD:1,加
	// SUB:2,减
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlReportLine init(){
		super.init();
    _report=null;	// 报表 <表主键:GlReport>  LONG
    _subject=null;	// 科目 <表主键:GlSubject>  INT
    _direct=ODirect.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect>  BYTE
    _symbolType=OSymbolType.DEFAULT.getLine().getKey();	// 加减类型 <OSymbolType>  BYTE
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
  public Long getReport(){
    return _report;
  }
  public void setReport(Long report){
    _report=report;
  }
  public GlReport gtReport(){
    if(getReport()==null)
      return null;
    return (GlReport)get(GlReport.class,getReport());
  }
  public void stReport(GlReport report){
    if(report==null)
      setReport(null);
    else
      setReport(report.getPkey());
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
  public Byte getSymbolType(){
    return _symbolType;
  }
  public void setSymbolType(Byte symbolType){
    _symbolType=symbolType;
  }
  public OSymbolType gtSymbolType(){
    return (OSymbolType)(OSymbolType.ADD.getLine().get(_symbolType));
  }
  public void stSymbolType(OSymbolType symbolType){
    _symbolType=symbolType.getLine().getKey();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
