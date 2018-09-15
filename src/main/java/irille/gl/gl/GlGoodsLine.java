package irille.gl.gl;

import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

public class GlGoodsLine extends BeanLong{
	public static final Tb TB = new Tb(GlGoodsLine.class, "存货账明细").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		GOODS(GlGoods.fldOutKey()),
		QTY(SYS.QTY),
		PRICE(SYS.PRICE),
		AMT(SYS.AMT),
		BALANCE_QTY(SYS.QTY, "结余数量"),
		BALANCE(SYS.BALANCE),
		DAYBOOK_LINE(GlDaybookLine.fldOutKey()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		;
		// 索引
	public static final Index IDX_GOODS = TB.addIndex("goods", false,	GOODS);
	public static final Index IDX_DAYBOOK_LINE = TB.addIndex("daybookLine", false,	DAYBOOK_LINE);

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
  private Integer _goods;	// 存货账 <表主键:GlGoods>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _price;	// 单价  DEC(14,4)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _balanceQty;	// 结余数量  DEC(14,4)
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private Long _daybookLine;	// 流水明细 <表主键:GlDaybookLine>  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlGoodsLine init(){
		super.init();
    _goods=null;	// 存货账 <表主键:GlGoods>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _price=ZERO;	// 单价  DEC(14,4)
    _amt=ZERO;	// 金额  DEC(16,2)
    _balanceQty=ZERO;	// 结余数量  DEC(14,4)
    _balance=ZERO;	// 余额  DEC(16,2)
    _daybookLine=null;	// 流水明细 <表主键:GlDaybookLine>  LONG
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
  public Integer getGoods(){
    return _goods;
  }
  public void setGoods(Integer goods){
    _goods=goods;
  }
  public GlGoods gtGoods(){
    if(getGoods()==null)
      return null;
    return (GlGoods)get(GlGoods.class,getGoods());
  }
  public void stGoods(GlGoods goods){
    if(goods==null)
      setGoods(null);
    else
      setGoods(goods.getPkey());
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
  public BigDecimal getBalanceQty(){
    return _balanceQty;
  }
  public void setBalanceQty(BigDecimal balanceQty){
    _balanceQty=balanceQty;
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
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
