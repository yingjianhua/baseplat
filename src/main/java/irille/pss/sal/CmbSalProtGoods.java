package irille.pss.sal;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmb;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

import java.math.BigDecimal;
import java.util.Date;

public class CmbSalProtGoods extends BeanInt<CmbSalProtGoods> {
	public static final Tb TB = new TbCmb(CmbSalProtGoods.class, "客户货物信息");

	public enum T implements IEnumFld {//@formatter:off
		VENDOR_GOODS_NAME(SYS.NAME__100_NULL, "他方品名"),
		VENDOR_NUM(SYS.CODE__40, "他方代码", true),
		VENDOR_SPEC(SYS.CODE__100, "他方规格", true),
		PRICE(SYS.PRICE, "协议价"),
		DATE_START(SYS.DATE, "启用日期"),
		DATE_END(SYS.DATE, "到期日期", true),
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
		T.VENDOR_GOODS_NAME.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	public static Fld fldCmb(String code,String name, boolean isnull) {
		FldVCmb fld = TB.crtCmb(code, name, TB);
		return fld.setNullCmb(isnull);
	}
	//@formatter:on

	//>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //>>>>>>>以下是自动产生的源代码行----请保留此行,用于自动产生代码识别用!
  //实例变量定义-----------------------------------------
  private String _vendorGoodsName;	// 他方品名  STR(100)<null>
  private String _vendorNum;	// 他方代码  STR(40)<null>
  private String _vendorSpec;	// 他方规格  STR(100)<null>
  private BigDecimal _price;	// 协议价  DEC(14,4)
  private Date _dateStart;	// 启用日期  DATE
  private Date _dateEnd;	// 到期日期  DATE<null>

	@Override
  public CmbSalProtGoods init(){
		super.init();
    _vendorGoodsName=null;	// 他方品名  STR(100)
    _vendorNum=null;	// 他方代码  STR(40)
    _vendorSpec=null;	// 他方规格  STR(100)
    _price=ZERO;	// 协议价  DEC(14,4)
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    return this;
  }

  //方法----------------------------------------------
  public String getVendorGoodsName(){
    return _vendorGoodsName;
  }
  public void setVendorGoodsName(String vendorGoodsName){
    _vendorGoodsName=vendorGoodsName;
  }
  public String getVendorNum(){
    return _vendorNum;
  }
  public void setVendorNum(String vendorNum){
    _vendorNum=vendorNum;
  }
  public String getVendorSpec(){
    return _vendorSpec;
  }
  public void setVendorSpec(String vendorSpec){
    _vendorSpec=vendorSpec;
  }
  public BigDecimal getPrice(){
    return _price;
  }
  public void setPrice(BigDecimal price){
    _price=price;
  }
  public Date getDateStart(){
    return _dateStart;
  }
  public void setDateStart(Date dateStart){
    _dateStart=dateStart;
  }
  public Date getDateEnd(){
    return _dateEnd;
  }
  public void setDateEnd(Date dateEnd){
    _dateEnd=dateEnd;
  }

}
