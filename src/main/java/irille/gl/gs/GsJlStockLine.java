/**
 * 
 */
package irille.gl.gs;

import irille.gl.gl.GlDaybookLine;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author surface1
 *
 */
public class GsJlStockLine extends BeanLong {
	public static final Tb TB = new Tb(GsJlStockLine.class, "存货账明细行").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		JL_STOCK(GsJlStock.fldOutKey()),
		QTY(SYS.QTY),
		PRICE(SYS.PRICE,"单价"),
		AMT(SYS.AMT,"金额"),
		TALLY_DATE(SYS.TALLY_DATE__NULL),
		DAYBOOK_LINE(GlDaybookLine.fldOutKey()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_STOCK_PKEY = TB.addIndex("stockPkey", true,JL_STOCK,PKEY);
		public static final Index IDX_DAYBOOK = TB.addIndex("daybook", false,DAYBOOK_LINE);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _jlStock;	// 存货账 <表主键:GsJlStock>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _price;	// 单价  DEC(14,4)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private Date _tallyDate;	// 记账日期  DATE<null>
  private Long _daybookLine;	// 流水明细 <表主键:GlDaybookLine>  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsJlStockLine init(){
		super.init();
    _jlStock=null;	// 存货账 <表主键:GsJlStock>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _price=ZERO;	// 单价  DEC(14,4)
    _amt=ZERO;	// 金额  DEC(16,2)
    _tallyDate=null;	// 记账日期  DATE
    _daybookLine=null;	// 流水明细 <表主键:GlDaybookLine>  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsJlStockLine loadUniqueStockPkey(boolean lockFlag,Integer jlStock,Long pkey) {
    return (GsJlStockLine)loadUnique(T.IDX_STOCK_PKEY,lockFlag,jlStock,pkey);
  }
  public static GsJlStockLine chkUniqueStockPkey(boolean lockFlag,Integer jlStock,Long pkey) {
    return (GsJlStockLine)chkUnique(T.IDX_STOCK_PKEY,lockFlag,jlStock,pkey);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getJlStock(){
    return _jlStock;
  }
  public void setJlStock(Integer jlStock){
    _jlStock=jlStock;
  }
  public GsJlStock gtJlStock(){
    if(getJlStock()==null)
      return null;
    return (GsJlStock)get(GsJlStock.class,getJlStock());
  }
  public void stJlStock(GsJlStock jlStock){
    if(jlStock==null)
      setJlStock(null);
    else
      setJlStock(jlStock.getPkey());
  }
  public BigDecimal getQty(){
    return _qty;
  }
  public void setQty(BigDecimal qty){
    _qty=qty;
  }
  public BigDecimal getPrice(){
    return _price;
  }
  public void setPrice(BigDecimal price){
    _price=price;
  }
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public Date getTallyDate(){
    return _tallyDate;
  }
  public void setTallyDate(Date tallyDate){
    _tallyDate=tallyDate;
  }
  public Long getDaybookLine(){
    return _daybookLine;
  }
  public void setDaybookLine(Long daybookLine){
    _daybookLine=daybookLine;
  }
  public GlDaybookLine gtDaybookLine(){
    if(getDaybookLine()==null)
      return null;
    return (GlDaybookLine)get(GlDaybookLine.class,getDaybookLine());
  }
  public void stDaybookLine(GlDaybookLine daybookLine){
    if(daybookLine==null)
      setDaybookLine(null);
    else
      setDaybookLine(daybookLine.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
