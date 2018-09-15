package irille.pss.sal;

import irille.core.sys.SysCell;
import irille.core.sys.SysCustom;
import irille.pss.sal.Sal.ODiscountLevel;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * 销售价格协议
 * 默认为从定价控制GsPriceCtl中取默认的销售价格级别，如存在协议则取协议级别
 * 
 * 前台界面上，这里的级别显示仅显示（1-12级），级别名称显示不了，
 * 因为对应不同的商品级别的名称不同 -- 会不会有问题？
 * @author whx
 * @version 创建时间：2014年11月28日 上午10:31:50
 */
public class SalPriceProt extends BeanInt<SalPriceProt> {
	public static final Log LOG = new Log(SalPriceProt.class);
	public static final Tb TB = new Tb(SalPriceProt.class, "销售价格协议").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		 PKEY(Tb.crtIntPkey()),
		CELL(SYS.CELL),
		CUST(SYS.CUST),
		NAME(SYS.NAME__40, "客户名称"),
		PRICE_LEVEL(TB.crt(Sal.ODiscountLevel.DEFAULT).setName("价格级别")), //（1-12级选一个）
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_CELL_CUST= TB.addIndex("cellCust",true,CELL,CUST);
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
		T.CELL.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}
	public static Fld fldOutKey(String code, String name) {
		return TB.crtOutKey(TB,code,name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _cust;	// 客户 <表主键:SysCustom>  INT
  private String _name;	// 客户名称  STR(40)
  private Byte _priceLevel;	// 价格级别 <ODiscountLevel>  BYTE
	// ON:1,级别一
	// TW:2,级别二
	// TH:3,级别三
	// FO:4,级别四
	// FI:5,级别五
	// SI:6,级别六
	// SE:7,级别七
	// EI:8,级别八
	// NI:9,级别九
	// TE:10,级别十
	// EL:11,级别十一
	// TWE:12,级别十二
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SalPriceProt init(){
		super.init();
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _cust=null;	// 客户 <表主键:SysCustom>  INT
    _name=null;	// 客户名称  STR(40)
    _priceLevel=ODiscountLevel.DEFAULT.getLine().getKey();	// 价格级别 <ODiscountLevel>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SalPriceProt loadUniqueCellCust(boolean lockFlag,Integer cell,Integer cust) {
    return (SalPriceProt)loadUnique(T.IDX_CELL_CUST,lockFlag,cell,cust);
  }
  public static SalPriceProt chkUniqueCellCust(boolean lockFlag,Integer cell,Integer cust) {
    return (SalPriceProt)chkUnique(T.IDX_CELL_CUST,lockFlag,cell,cust);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getCell(){
    return _cell;
  }
  public void setCell(Integer cell){
    _cell=cell;
  }
  public SysCell gtCell(){
    if(getCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getCell());
  }
  public void stCell(SysCell cell){
    if(cell==null)
      setCell(null);
    else
      setCell(cell.getPkey());
  }
  public Integer getCust(){
    return _cust;
  }
  public void setCust(Integer cust){
    _cust=cust;
  }
  public SysCustom gtCust(){
    if(getCust()==null)
      return null;
    return (SysCustom)get(SysCustom.class,getCust());
  }
  public void stCust(SysCustom cust){
    if(cust==null)
      setCust(null);
    else
      setCust(cust.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Byte getPriceLevel(){
    return _priceLevel;
  }
  public void setPriceLevel(Byte priceLevel){
    _priceLevel=priceLevel;
  }
  public ODiscountLevel gtPriceLevel(){
    return (ODiscountLevel)(ODiscountLevel.ON.getLine().get(_priceLevel));
  }
  public void stPriceLevel(ODiscountLevel priceLevel){
    _priceLevel=priceLevel.getLine().getKey();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
