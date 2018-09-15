package irille.gl.gl;

import irille.core.sys.Sys.OEnabled;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

public class GlGoods extends BeanInt<GlGoods> {
	private static final Log LOG = new Log(GlGoods.class);
	public static final Tb TB = new Tb(GlGoods.class, "存货账").addActIUDL().setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		JOURNAL(GlJournal.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		BATCH(SYS.STR__40_NULL, "批次代码"), // 预留
		UOM(GsUom.fldOutKey()),
		QTY(SYS.QTY),
		PRICE(SYS.PRICE),
		BALANCE(SYS.BALANCE),
		ENABLED(SYS.ENABLED),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_JOURNAL_GOODS = TB.addIndex("journalGoods",
				true,JOURNAL,GOODS);
		private Fld _fld;
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Long _journal;	// 分户账 <表主键:GlJournal>  LONG
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private String _batch;	// 批次代码  STR(40)<null>
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _price;	// 单价  DEC(14,4)
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlGoods init(){
		super.init();
    _journal=null;	// 分户账 <表主键:GlJournal>  LONG
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _batch=null;	// 批次代码  STR(40)
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _price=ZERO;	// 单价  DEC(14,4)
    _balance=ZERO;	// 余额  DEC(16,2)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlGoods loadUniqueJournalGoods(boolean lockFlag,Long journal,Integer goods) {
    return (GlGoods)loadUnique(T.IDX_JOURNAL_GOODS,lockFlag,journal,goods);
  }
  public static GlGoods chkUniqueJournalGoods(boolean lockFlag,Long journal,Integer goods) {
    return (GlGoods)chkUnique(T.IDX_JOURNAL_GOODS,lockFlag,journal,goods);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Long getJournal(){
    return _journal;
  }
  public void setJournal(Long journal){
    _journal=journal;
  }
  public GlJournal gtJournal(){
    if(getJournal()==null)
      return null;
    return (GlJournal)get(GlJournal.class,getJournal());
  }
  public void stJournal(GlJournal journal){
    if(journal==null)
      setJournal(null);
    else
      setJournal(journal.getPkey());
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
  public String getBatch(){
    return _batch;
  }
  public void setBatch(String batch){
    _batch=batch;
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
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public Byte getEnabled(){
    return _enabled;
  }
  public void setEnabled(Byte enabled){
    _enabled=enabled;
  }
  public Boolean gtEnabled(){
    return byteToBoolean(_enabled);
  }
  public void stEnabled(Boolean enabled){
    _enabled=booleanToByte(enabled);
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
