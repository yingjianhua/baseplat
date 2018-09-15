package irille.pub.bean;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.math.BigDecimal;

/**
 * 起止日期
 * 
 * @author whx
 */
public class CmbGoodsPrice extends BeanGoodsPrice<CmbGoodsPrice> {
	public static final TbCmb TB = new TbCmb(CmbGoodsPrice.class, "商品单位数量金额");

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		GOODS(GsGoods.fldOutKey()),
		UOM(GsUom.fldOutKey()),
		QTY(SYS.QTY),
		PRICE(SYS.PRICE),
		AMT(SYS.AMT),
		INF(TB.crtInf()),
		//>>>>>>>以下是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		;
		//<<<<<<<以上是自动产生的字段定义对象----请保留此行,用于自动产生代码识别用!
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}

	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}

	//@formatter:on

  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _price;	// 单价  DEC(14,4)
  private BigDecimal _amt;	// 金额  DEC(16,2)

	@Override
  public CmbGoodsPrice init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _price=ZERO;	// 单价  DEC(14,4)
    _amt=ZERO;	// 金额  DEC(16,2)
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

}
