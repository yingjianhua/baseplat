package irille.gl.gs;

import irille.pub.bean.BeanGoods;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.math.BigDecimal;

/**
 * 提供XLS导入的功能
 * @author whx
 * @version 创建时间：2014年8月25日 上午10:33:39
 */
public class GsPhyinvBatchLine extends BeanGoods<GsPhyinvBatchLine> {
	public static final Tb TB = new Tb(GsPhyinvBatchLine.class, "存货盘点任务明细").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		GOODS(GsGoods.fldOutKey()),
		LOCATION(GsLocation.fldOutKey().setNull()),
		BATCH(GsStockBatch.fldOutKey()),
		UOM(GsUom.fldOutKey()),
		QTY(SYS.QTY, "账上数量"),
		COUNT_QTY(SYS.QTY, "实际数量",true),
		DIFF_QTY(SYS.QTY, "数量差额",true),
		DIFF_AMT(SYS.AMT, "金额差额",true),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		//public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Integer _location;	// 货位 <表主键:GsLocation>  INT<null>
  private Long _batch;	// 批次 <表主键:GsStockBatch>  LONG
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 账上数量  DEC(14,4)
  private BigDecimal _countQty;	// 实际数量  DEC(14,4)<null>
  private BigDecimal _diffQty;	// 数量差额  DEC(14,4)<null>
  private BigDecimal _diffAmt;	// 金额差额  DEC(16,2)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPhyinvBatchLine init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _location=null;	// 货位 <表主键:GsLocation>  INT
    _batch=null;	// 批次 <表主键:GsStockBatch>  LONG
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 账上数量  DEC(14,4)
    _countQty=null;	// 实际数量  DEC(14,4)
    _diffQty=null;	// 数量差额  DEC(14,4)
    _diffAmt=null;	// 金额差额  DEC(16,2)
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
  public Integer getLocation(){
    return _location;
  }
  public void setLocation(Integer location){
    _location=location;
  }
  public GsLocation gtLocation(){
    if(getLocation()==null)
      return null;
    return (GsLocation)get(GsLocation.class,getLocation());
  }
  public void stLocation(GsLocation location){
    if(location==null)
      setLocation(null);
    else
      setLocation(location.getPkey());
  }
  public Long getBatch(){
    return _batch;
  }
  public void setBatch(Long batch){
    _batch=batch;
  }
  public GsStockBatch gtBatch(){
    if(getBatch()==null)
      return null;
    return (GsStockBatch)get(GsStockBatch.class,getBatch());
  }
  public void stBatch(GsStockBatch batch){
    if(batch==null)
      setBatch(null);
    else
      setBatch(batch.getPkey());
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
  public BigDecimal getCountQty(){
    return _countQty;
  }
  public void setCountQty(BigDecimal countQty){
    _countQty=countQty;
  }
  public BigDecimal getDiffQty(){
    return _diffQty;
  }
  public void setDiffQty(BigDecimal diffQty){
    _diffQty=diffQty;
  }
  public BigDecimal getDiffAmt(){
    return _diffAmt;
  }
  public void setDiffAmt(BigDecimal diffAmt){
    _diffAmt=diffAmt;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
