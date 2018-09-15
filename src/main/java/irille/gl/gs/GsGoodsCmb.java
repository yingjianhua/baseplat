package irille.gl.gs;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

public class GsGoodsCmb extends BeanInt<GsGoodsCmb> {
	public static final Tb TB = new Tb(GsGoodsCmb.class, "组合套件定义").setAutoIncrement().addActIUDL();
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()), 
		GOODS(GsGoods.fldOutKey()),
		SORT(SYS.SORT__SHORT),
		INNER_GOODS(GOODS,"内部货物"),
		INNER_COUNT(SYS.QTY,"内部货物数量"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_GOODS_SORT = TB.addIndex("goodsSort",
				false,GOODS,SORT);
		public static final Index IDX_GOODS_IN = TB.addIndex("goodsIn",
				true,GOODS,INNER_GOODS);
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
  private Integer _pkey;	// 编号  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private Short _sort;	// 排序号  SHORT
  private Integer _innerGoods;	// 内部货物 <表主键:GsGoods>  INT
  private BigDecimal _innerCount;	// 内部货物数量  DEC(14,4)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsGoodsCmb init(){
		super.init();
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _sort=0;	// 排序号  SHORT
    _innerGoods=null;	// 内部货物 <表主键:GsGoods>  INT
    _innerCount=ZERO;	// 内部货物数量  DEC(14,4)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsGoodsCmb loadUniqueGoodsIn(boolean lockFlag,Integer goods,Integer innerGoods) {
    return (GsGoodsCmb)loadUnique(T.IDX_GOODS_IN,lockFlag,goods,innerGoods);
  }
  public static GsGoodsCmb chkUniqueGoodsIn(boolean lockFlag,Integer goods,Integer innerGoods) {
    return (GsGoodsCmb)chkUnique(T.IDX_GOODS_IN,lockFlag,goods,innerGoods);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
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
  public Short getSort(){
    return _sort;
  }
  public void setSort(Short sort){
    _sort=sort;
  }
  public Integer getInnerGoods(){
    return _innerGoods;
  }
  public void setInnerGoods(Integer innerGoods){
    _innerGoods=innerGoods;
  }
  public GsGoods gtInnerGoods(){
    if(getInnerGoods()==null)
      return null;
    return (GsGoods)get(GsGoods.class,getInnerGoods());
  }
  public void stInnerGoods(GsGoods innerGoods){
    if(innerGoods==null)
      setInnerGoods(null);
    else
      setInnerGoods(innerGoods.getPkey());
  }
  public BigDecimal getInnerCount(){
    return _innerCount;
  }
  public void setInnerCount(BigDecimal innerCount){
    _innerCount=innerCount;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
