/**
 * 
 */
package irille.gl.gs;

import irille.pub.bean.Bean;
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
public class GsStockLine extends BeanLong {
	public static final Tb TB = new Tb(GsStockLine.class, "存货行").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		STOCK(GsStock.fldOutKey()),
		NAME(SYS.NAME__40, true),
		GS_FORM(SYS.ORIG_FORM__CODE, "出入库单据"),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		GS_TIME(SYS.TIME,"出入库时间"),
		GS_BATCH_NAME(SYS.STR__40_NULL, "批次代码"),  //如果有货位、分批次或有到期日的，则有批次信息
		GS_QTY(SYS.QTY, "出入库数量"),
		QTY(SYS.QTY, "剩余数量"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		GS_FORM_NUM(TB.get("gsFormNum")),	//出入库单据号
		ORIG_FORM_NUM(TB.get("origFormNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_STOCK = TB.addIndex("stock", false,STOCK);
		public static final Index IDX_GS = TB.addIndex("gs", false,GS_FORM);
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
  private Integer _stock;	// 存货 <表主键:GsStock>  INT
  private String _name;	// 名称  STR(40)<null>
  private Long _gsForm;	// 出入库单据  LONG
  private String _gsFormNum;	// 出入库单据号  STR(40)
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Date _gsTime;	// 出入库时间  TIME
  private String _gsBatchName;	// 批次代码  STR(40)<null>
  private BigDecimal _gsQty;	// 出入库数量  DEC(14,4)
  private BigDecimal _qty;	// 剩余数量  DEC(14,4)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsStockLine init(){
		super.init();
    _stock=null;	// 存货 <表主键:GsStock>  INT
    _name=null;	// 名称  STR(40)
    _gsForm=null;	// 出入库单据  LONG
    _gsFormNum=null;	// 出入库单据号  STR(40)
    _origForm=null;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _gsTime=null;	// 出入库时间  TIME
    _gsBatchName=null;	// 批次代码  STR(40)
    _gsQty=ZERO;	// 出入库数量  DEC(14,4)
    _qty=ZERO;	// 剩余数量  DEC(14,4)
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
  public Long getGsForm(){
    return _gsForm;
  }
  public void setGsForm(Long gsForm){
    _gsForm=gsForm;
  }
  //外部主键对象: IForm
  public Bean gtGsForm(){
    return (Bean)gtLongTbObj(getGsForm());
  }
  public void stGsForm(Bean gsForm){
      setGsForm(gsForm.gtLongPkey());
  }
  public String getGsFormNum(){
    return _gsFormNum;
  }
  public void setGsFormNum(String gsFormNum){
    _gsFormNum=gsFormNum;
  }
  public Long getOrigForm(){
    return _origForm;
  }
  public void setOrigForm(Long origForm){
    _origForm=origForm;
  }
  //外部主键对象: IForm
  public Bean gtOrigForm(){
    return (Bean)gtLongTbObj(getOrigForm());
  }
  public void stOrigForm(Bean origForm){
      setOrigForm(origForm.gtLongPkey());
  }
  public String getOrigFormNum(){
    return _origFormNum;
  }
  public void setOrigFormNum(String origFormNum){
    _origFormNum=origFormNum;
  }
  public Date getGsTime(){
    return _gsTime;
  }
  public void setGsTime(Date gsTime){
    _gsTime=gsTime;
  }
  public String getGsBatchName(){
    return _gsBatchName;
  }
  public void setGsBatchName(String gsBatchName){
    _gsBatchName=gsBatchName;
  }
  public BigDecimal getGsQty(){
    return _gsQty;
  }
  public void setGsQty(BigDecimal gsQty){
    _gsQty=gsQty;
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
