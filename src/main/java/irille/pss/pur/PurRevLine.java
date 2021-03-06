package irille.pss.pur;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pub.bean.BeanPurLine;
import irille.pub.bean.CmbPurLine;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;

/**
 * 金额 = 实收数量/计划数量 * 单价
 * 如果数量有亏损，必须挂损失的NOTE
 * @author whx
 * @version 创建时间：2014年8月27日 下午3:19:49
 */
public class PurRevLine extends BeanPurLine<PurRevLine> {
	public static final Tb TB = new Tb(PurRevLine.class, "收货单明细").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		CMB_GOODS_COST(CmbPurLine.fldFlds()),
		RECEIVED_QTY(SYS.QTY, "实收数量"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		PKEY(TB.get("pkey")),	//编号
		GOODS(TB.get("goods")),	//货物
		UOM(TB.get("uom")),	//计量单位
		QTY(TB.get("qty")),	//数量
		PRICE(TB.get("price")),	//单价
		AMT(TB.get("amt")),	//金额
		COST_PUR(TB.get("costPur")),	//采购成本
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
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
		T.CMB_GOODS_COST.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _price;	// 单价  DEC(14,4)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _costPur;	// 采购成本  DEC(16,2)
  private BigDecimal _receivedQty;	// 实收数量  DEC(14,4)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PurRevLine init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _price=ZERO;	// 单价  DEC(14,4)
    _amt=ZERO;	// 金额  DEC(16,2)
    _costPur=ZERO;	// 采购成本  DEC(16,2)
    _receivedQty=ZERO;	// 实收数量  DEC(14,4)
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
  public GsGoods gtGoods(){
    if(getGoods()==null)
      return null;
    return (GsGoods)get(GsGoods.class,getGoods());
  }
  public void stGoods(GsGoods goods){
    if(goods==null)
      setGoods(null);
    else
      setGoods(goods.getPkey());
  }
  public Integer getUom(){
    return _uom;
  }
  public void setUom(Integer uom){
    _uom=uom;
  }
  public GsUom gtUom(){
    if(getUom()==null)
      return null;
    return (GsUom)get(GsUom.class,getUom());
  }
  public void stUom(GsUom uom){
    if(uom==null)
      setUom(null);
    else
      setUom(uom.getPkey());
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
  public BigDecimal getCostPur(){
    return _costPur;
  }
  public void setCostPur(BigDecimal costPur){
    _costPur=costPur;
  }
  public BigDecimal getReceivedQty(){
    return _receivedQty;
  }
  public void setReceivedQty(BigDecimal receivedQty){
    _receivedQty=receivedQty;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
