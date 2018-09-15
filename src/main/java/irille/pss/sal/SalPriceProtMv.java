package irille.pss.sal;

import irille.core.sys.SysCell;
import irille.pss.sal.Sal.ODiscountLevel;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

/**
 * 调拨价格协议
 * 默认为从定价控制GsPriceCtl中取默认的调拨价格级别，如存在协议则取协议级别
 * 前台界面上，这里的级别显示仅显示（1-12级），级别名称显示不了，
 * @author whx
 * @version 创建时间：2014年11月28日 上午10:31:50
 */
public class SalPriceProtMv extends BeanInt<SalPriceProtMv> {
	public static final Log LOG = new Log(SalPriceProtMv.class);
	public static final Tb TB = new Tb(SalPriceProtMv.class, "调拨价格协议").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		 PKEY(Tb.crtIntPkey()),
		CELL_SAL(SYS.CELL, "销售方核算单元"),
		CELL_PUR(SYS.CELL, "采购方核算单元"),
		PRICE_LEVEL(TB.crt(Sal.ODiscountLevel.DEFAULT).setName("价格级别")), //（1-12级选一个）
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
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
  private Integer _cellSal;	// 销售方核算单元 <表主键:SysCell>  INT
  private Integer _cellPur;	// 采购方核算单元 <表主键:SysCell>  INT
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
  public SalPriceProtMv init(){
		super.init();
    _cellSal=null;	// 销售方核算单元 <表主键:SysCell>  INT
    _cellPur=null;	// 采购方核算单元 <表主键:SysCell>  INT
    _priceLevel=ODiscountLevel.DEFAULT.getLine().getKey();	// 价格级别 <ODiscountLevel>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getCellSal(){
    return _cellSal;
  }
  public void setCellSal(Integer cellSal){
    _cellSal=cellSal;
  }
  public SysCell gtCellSal(){
    if(getCellSal()==null)
      return null;
    return (SysCell)get(SysCell.class,getCellSal());
  }
  public void stCellSal(SysCell cellSal){
    if(cellSal==null)
      setCellSal(null);
    else
      setCellSal(cellSal.getPkey());
  }
  public Integer getCellPur(){
    return _cellPur;
  }
  public void setCellPur(Integer cellPur){
    _cellPur=cellPur;
  }
  public SysCell gtCellPur(){
    if(getCellPur()==null)
      return null;
    return (SysCell)get(SysCell.class,getCellPur());
  }
  public void stCellPur(SysCell cellPur){
    if(cellPur==null)
      setCellPur(null);
    else
      setCellPur(cellPur.getPkey());
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
