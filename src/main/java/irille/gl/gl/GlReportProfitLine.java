package irille.gl.gl;

import irille.gl.gl.Gl.OTableType;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;

public class GlReportProfitLine extends BeanLong<GlReportProfitLine>{
	public static final Tb TB = new Tb(GlReportProfitLine.class, "利润表明细").setAutoLocal()
	    .addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //此表主键类似Form的主键进行编号
		PROFIT_REPORT(TB.crtOutKey(GlReportProfit.class, "profitReport", "利润表")),
		KEY_VALUE(SYS.INT_PLUS_OR_ZERO,"键",true),
		KEY_NAME(SYS.STR__100,"名称"),
		TABLE_TYPE(TB.crt(OTableType.DEFAULT)),
		AMT_BEGIN(SYS.AMT,"年初数"),
		AMT_END(SYS.AMT,"期末数"),
		ORDER_ID(SYS.INT_PLUS_OR_ZERO,"顺序号"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _profitReport;	// 利润表 <表主键:GlReportProfit>  LONG
  private Integer _keyValue;	// 键  INT<null>
  private String _keyName;	// 名称  STR(100)
  private Byte _tableType;	// 表类型 <OTableType>  BYTE
	// ZC:1,资产
	// FZ:2,负债
	// QY:3,所有者权益
	// lR:4,利润表
  private BigDecimal _amtBegin;	// 年初数  DEC(16,2)
  private BigDecimal _amtEnd;	// 期末数  DEC(16,2)
  private Integer _orderId;	// 顺序号  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlReportProfitLine init(){
		super.init();
    _profitReport=null;	// 利润表 <表主键:GlReportProfit>  LONG
    _keyValue=null;	// 键  INT
    _keyName=null;	// 名称  STR(100)
    _tableType=OTableType.DEFAULT.getLine().getKey();	// 表类型 <OTableType>  BYTE
    _amtBegin=ZERO;	// 年初数  DEC(16,2)
    _amtEnd=ZERO;	// 期末数  DEC(16,2)
    _orderId=0;	// 顺序号  INT
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
  public Long getProfitReport(){
    return _profitReport;
  }
  public void setProfitReport(Long profitReport){
    _profitReport=profitReport;
  }
  public GlReportProfit gtProfitReport(){
    if(getProfitReport()==null)
      return null;
    return (GlReportProfit)get(GlReportProfit.class,getProfitReport());
  }
  public void stProfitReport(GlReportProfit profitReport){
    if(profitReport==null)
      setProfitReport(null);
    else
      setProfitReport(profitReport.getPkey());
  }
  public Integer getKeyValue(){
    return _keyValue;
  }
  public void setKeyValue(Integer keyValue){
    _keyValue=keyValue;
  }
  public String getKeyName(){
    return _keyName;
  }
  public void setKeyName(String keyName){
    _keyName=keyName;
  }
  public Byte getTableType(){
    return _tableType;
  }
  public void setTableType(Byte tableType){
    _tableType=tableType;
  }
  public OTableType gtTableType(){
    return (OTableType)(OTableType.ZC.getLine().get(_tableType));
  }
  public void stTableType(OTableType tableType){
    _tableType=tableType.getLine().getKey();
  }
  public BigDecimal getAmtBegin(){
    return _amtBegin;
  }
  public void setAmtBegin(BigDecimal amtBegin){
    _amtBegin=amtBegin;
  }
  public BigDecimal getAmtEnd(){
    return _amtEnd;
  }
  public void setAmtEnd(BigDecimal amtEnd){
    _amtEnd=amtEnd;
  }
  public Integer getOrderId(){
    return _orderId;
  }
  public void setOrderId(Integer orderId){
    _orderId=orderId;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
