/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.SysCell;
import irille.gl.gl.GlSubject;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * @author surface1
 *
 */
public class GsJlStock extends BeanInt {
	private static final Log LOG = new Log(GsJlStock.class);

	public static final Tb TB = new Tb(GsJlStock.class, "存货账").setAutoIncrement().addActIUDL(); //存货账的核算级别是Cell

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CELL(SYS.CELL),
		GOODS(GsGoods.fldOutKey()),
		NAME(SYS.NAME__100),
		SUBJECT(GlSubject.fldOutKey()),
		UOM(GsUom.fldOutKey()),
		PAPER_QTY(SYS.QTY,"账面数量"), //在财务部门入账时更新
		PAPER_PRICE(SYS.PRICE,"账面单价"),
		PAPER_BALANCE(SYS.BALANCE,"账面余额"),
		STOCK_QTY(SYS.QTY,"库存数量"), //库存与出入库实物对应
		STOCK_PRICE(SYS.PRICE,"库存单价"),
		STOCK_BALANCE(SYS.BALANCE,"库存余额"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CELL_GOODS = TB.addIndex("cellGoods", true,CELL,GOODS);
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private String _name;	// 名称  STR(100)
  private Integer _subject;	// 科目字典 <表主键:GlSubject>  INT
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _paperQty;	// 账面数量  DEC(14,4)
  private BigDecimal _paperPrice;	// 账面单价  DEC(14,4)
  private BigDecimal _paperBalance;	// 账面余额  DEC(16,2)
  private BigDecimal _stockQty;	// 库存数量  DEC(14,4)
  private BigDecimal _stockPrice;	// 库存单价  DEC(14,4)
  private BigDecimal _stockBalance;	// 库存余额  DEC(16,2)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsJlStock init(){
		super.init();
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _name=null;	// 名称  STR(100)
    _subject=null;	// 科目字典 <表主键:GlSubject>  INT
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _paperQty=ZERO;	// 账面数量  DEC(14,4)
    _paperPrice=ZERO;	// 账面单价  DEC(14,4)
    _paperBalance=ZERO;	// 账面余额  DEC(16,2)
    _stockQty=ZERO;	// 库存数量  DEC(14,4)
    _stockPrice=ZERO;	// 库存单价  DEC(14,4)
    _stockBalance=ZERO;	// 库存余额  DEC(16,2)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsJlStock loadUniqueCellGoods(boolean lockFlag,Integer cell,Integer goods) {
    return (GsJlStock)loadUnique(T.IDX_CELL_GOODS,lockFlag,cell,goods);
  }
  public static GsJlStock chkUniqueCellGoods(boolean lockFlag,Integer cell,Integer goods) {
    return (GsJlStock)chkUnique(T.IDX_CELL_GOODS,lockFlag,cell,goods);
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getSubject(){
    return _subject;
  }
  public void setSubject(Integer subject){
    _subject=subject;
  }
  public GlSubject gtSubject(){
    if(getSubject()==null)
      return null;
    return (GlSubject)get(GlSubject.class,getSubject());
  }
  public void stSubject(GlSubject subject){
    if(subject==null)
      setSubject(null);
    else
      setSubject(subject.getPkey());
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
  public BigDecimal getPaperQty(){
    return _paperQty;
  }
  public void setPaperQty(BigDecimal paperQty){
    _paperQty=paperQty;
  }
  public BigDecimal getPaperPrice(){
    return _paperPrice;
  }
  public void setPaperPrice(BigDecimal paperPrice){
    _paperPrice=paperPrice;
  }
  public BigDecimal getPaperBalance(){
    return _paperBalance;
  }
  public void setPaperBalance(BigDecimal paperBalance){
    _paperBalance=paperBalance;
  }
  public BigDecimal getStockQty(){
    return _stockQty;
  }
  public void setStockQty(BigDecimal stockQty){
    _stockQty=stockQty;
  }
  public BigDecimal getStockPrice(){
    return _stockPrice;
  }
  public void setStockPrice(BigDecimal stockPrice){
    _stockPrice=stockPrice;
  }
  public BigDecimal getStockBalance(){
    return _stockBalance;
  }
  public void setStockBalance(BigDecimal stockBalance){
    _stockBalance=stockBalance;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
