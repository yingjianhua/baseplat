package irille.pss.pur;

import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pss.sal.CmbSalProtGoods;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.FldVCmbFlds;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;
import java.util.Date;

public class PurProtGoodsApplyLine extends BeanLong<PurProtGoodsApplyLine> {
	public static final Tb TB = new Tb(PurProtGoodsApplyLine.class,
			"供应商货物协议申请单明细").setAutoLocal();

	public enum T implements IEnumFld {// @formatter:off
		PKEY(TB.crtLongPkey()), 
		GOODS(GsGoods.fldOutKey()), 
		UOM(GsUom.fldOutKey()), 
		CMB_SAL_PROT(((FldVCmbFlds)CmbSalProtGoods.fldFlds()).setNullCmb(true)), 
		AFT(CmbSalProtGoods.fldCmb("after", "变更后")),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		VENDOR_GOODS_NAME(TB.get("vendorGoodsName")),	//他方品名
		VENDOR_NUM(TB.get("vendorNum")),	//他方代码
		VENDOR_SPEC(TB.get("vendorSpec")),	//他方规格
		PRICE(TB.get("price")),	//协议价
		DATE_START(TB.get("dateStart")),	//启用日期
		DATE_END(TB.get("dateEnd")),	//到期日期
		AFT_VENDOR_GOODS_NAME(TB.get("aftVendorGoodsName")),	//变更后他方品名
		AFT_VENDOR_NUM(TB.get("aftVendorNum")),	//变更后他方代码
		AFT_VENDOR_SPEC(TB.get("aftVendorSpec")),	//变更后他方规格
		AFT_PRICE(TB.get("aftPrice")),	//变更后协议价
		AFT_DATE_START(TB.get("aftDateStart")),	//变更后启用日期
		AFT_DATE_END(TB.get("aftDateEnd")),	//变更后到期日期
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		private Fld _fld;

		private T(Class clazz, String name, boolean... isnull) {
			_fld = TB.addOutKey(clazz, this, name, isnull);
		}

		private T(IEnumFld fld, boolean... isnull) {
			this(fld, null, isnull);
		}

		private T(IEnumFld fld, String name, boolean... null1) {
			_fld = TB.add(fld, this, name, null1);
		}

		private T(IEnumFld fld, String name, int strLen) {
			_fld = TB.add(fld, this, name, strLen);
		}

		private T(Fld fld) {
			_fld = TB.add(fld, this);
		}

		public Fld getFld() {
			return _fld;
		}
	}

	static { // 在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();// 加锁所有字段,不可以修改
	}
	// @formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private String _vendorGoodsName;	// 他方品名  STR(100)<null>
  private String _vendorNum;	// 他方代码  STR(40)<null>
  private String _vendorSpec;	// 他方规格  STR(100)<null>
  private BigDecimal _price;	// 协议价  DEC(14,4)<null>
  private Date _dateStart;	// 启用日期  DATE<null>
  private Date _dateEnd;	// 到期日期  DATE<null>
  private String _aftVendorGoodsName;	// 变更后他方品名  STR(100)<null>
  private String _aftVendorNum;	// 变更后他方代码  STR(40)<null>
  private String _aftVendorSpec;	// 变更后他方规格  STR(100)<null>
  private BigDecimal _aftPrice;	// 变更后协议价  DEC(14,4)
  private Date _aftDateStart;	// 变更后启用日期  DATE
  private Date _aftDateEnd;	// 变更后到期日期  DATE<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public PurProtGoodsApplyLine init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _vendorGoodsName=null;	// 他方品名  STR(100)
    _vendorNum=null;	// 他方代码  STR(40)
    _vendorSpec=null;	// 他方规格  STR(100)
    _price=null;	// 协议价  DEC(14,4)
    _dateStart=null;	// 启用日期  DATE
    _dateEnd=null;	// 到期日期  DATE
    _aftVendorGoodsName=null;	// 变更后他方品名  STR(100)
    _aftVendorNum=null;	// 变更后他方代码  STR(40)
    _aftVendorSpec=null;	// 变更后他方规格  STR(100)
    _aftPrice=ZERO;	// 变更后协议价  DEC(14,4)
    _aftDateStart=null;	// 变更后启用日期  DATE
    _aftDateEnd=null;	// 变更后到期日期  DATE
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
  //组合对象的操作
  public CmbSalProtGoods gtAft(){
    CmbSalProtGoods b=new CmbSalProtGoods();
    	b.setVendorGoodsName(_aftVendorGoodsName);
    	b.setVendorNum(_aftVendorNum);
    	b.setVendorSpec(_aftVendorSpec);
    	b.setPrice(_aftPrice);
    	b.setDateStart(_aftDateStart);
    	b.setDateEnd(_aftDateEnd);
    return b;
  }
  public void stAft(CmbSalProtGoods aft){
    _aftVendorGoodsName=aft.getVendorGoodsName();
    _aftVendorNum=aft.getVendorNum();
    _aftVendorSpec=aft.getVendorSpec();
    _aftPrice=aft.getPrice();
    _aftDateStart=aft.getDateStart();
    _aftDateEnd=aft.getDateEnd();
  }
  public String getAftVendorGoodsName(){
    return _aftVendorGoodsName;
  }
  public void setAftVendorGoodsName(String aftVendorGoodsName){
    _aftVendorGoodsName=aftVendorGoodsName;
  }
  public String getAftVendorNum(){
    return _aftVendorNum;
  }
  public void setAftVendorNum(String aftVendorNum){
    _aftVendorNum=aftVendorNum;
  }
  public String getAftVendorSpec(){
    return _aftVendorSpec;
  }
  public void setAftVendorSpec(String aftVendorSpec){
    _aftVendorSpec=aftVendorSpec;
  }
  public BigDecimal getAftPrice(){
    return _aftPrice;
  }
  public void setAftPrice(BigDecimal aftPrice){
    _aftPrice=aftPrice;
  }
  public Date getAftDateStart(){
    return _aftDateStart;
  }
  public void setAftDateStart(Date aftDateStart){
    _aftDateStart=aftDateStart;
  }
  public Date getAftDateEnd(){
    return _aftDateEnd;
  }
  public void setAftDateEnd(Date aftDateEnd){
    _aftDateEnd=aftDateEnd;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
