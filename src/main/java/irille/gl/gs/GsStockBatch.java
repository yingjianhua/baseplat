/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.Sys.OYn;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author whx
 * @version 创建时间：2014年8月27日 上午9:36:17
 */
public class GsStockBatch extends BeanLong implements IExtName{
	private static final Log LOG = new Log(GsStockBatch.class);

	public static final Tb TB = new Tb(GsStockBatch.class, "存货批次","批次"	)
			.setAutoIncrement().addActList().addActUpd();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		STOCK(GsStock.fldOutKey()),
		NAME(SYS.NAME__100_NULL, "批次"),
		CLEARED(SYS.NY,"结清标志"),
		EXP_DATE(SYS.DATE__NULL,"有效(保质)期", true),
		ENTRY_TIME(SYS.DATE_TIME__NULL,"入库日期"),
		QTY(SYS.QTY,"库存数量"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_SEEL = TB.addIndex("seel", true,STOCK,EXP_DATE,ENTRY_TIME);
		public static final Index IDX_SN = TB.addIndex("sn", true, STOCK,NAME);
		private Fld _fld;
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	@Override
	public String getExtName() {
		return getName();
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _stock;	// 存货 <表主键:GsStock>  INT
  private String _name;	// 批次  STR(100)<null>
  private Byte _cleared;	// 结清标志 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Date _expDate;	// 有效(保质)期  DATE<null>
  private Date _entryTime;	// 入库日期  TIME<null>
  private BigDecimal _qty;	// 库存数量  DEC(14,4)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsStockBatch init(){
		super.init();
    _stock=null;	// 存货 <表主键:GsStock>  INT
    _name=null;	// 批次  STR(100)
    _cleared=OYn.DEFAULT.getLine().getKey();	// 结清标志 <OYn>  BYTE
    _expDate=null;	// 有效(保质)期  DATE
    _entryTime=null;	// 入库日期  TIME
    _qty=ZERO;	// 库存数量  DEC(14,4)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsStockBatch loadUniqueSeel(boolean lockFlag,Integer stock,Date expDate,Date entryTime) {
    return (GsStockBatch)loadUnique(T.IDX_SEEL,lockFlag,stock,expDate,entryTime);
  }
  public static GsStockBatch chkUniqueSeel(boolean lockFlag,Integer stock,Date expDate,Date entryTime) {
    return (GsStockBatch)chkUnique(T.IDX_SEEL,lockFlag,stock,expDate,entryTime);
  }
  public static GsStockBatch loadUniqueSn(boolean lockFlag,Integer stock,String name) {
    return (GsStockBatch)loadUnique(T.IDX_SN,lockFlag,stock,name);
  }
  public static GsStockBatch chkUniqueSn(boolean lockFlag,Integer stock,String name) {
    return (GsStockBatch)chkUnique(T.IDX_SN,lockFlag,stock,name);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getStock(){
    return _stock;
  }
  public void setStock(Integer stock){
    _stock=stock;
  }
  public GsStock gtStock(){
    if(getStock()==null)
      return null;
    return (GsStock)get(GsStock.class,getStock());
  }
  public void stStock(GsStock stock){
    if(stock==null)
      setStock(null);
    else
      setStock(stock.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Byte getCleared(){
    return _cleared;
  }
  public void setCleared(Byte cleared){
    _cleared=cleared;
  }
  public Boolean gtCleared(){
    return byteToBoolean(_cleared);
  }
  public void stCleared(Boolean cleared){
    _cleared=booleanToByte(cleared);
  }
  public Date getExpDate(){
    return _expDate;
  }
  public void setExpDate(Date expDate){
    _expDate=expDate;
  }
  public Date getEntryTime(){
    return _entryTime;
  }
  public void setEntryTime(Date entryTime){
    _entryTime=entryTime;
  }
  public BigDecimal getQty(){
    return _qty;
  }
  public void setQty(BigDecimal qty){
    _qty=qty;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
