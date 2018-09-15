package irille.gl.gs;

import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;
import java.util.Date;

public class GsReportSalOut extends BeanLong<GsReportSalOut> {
	public static final Tb TB = new TbView(GsReportSalOut.class, "销售出库报表	").setAutoLocal().addActList();
	
	public enum T implements IEnumFld {//@formatter:off
		CODE(SYS.CODE__40,"出库单号"),	//出库单号
		OUT_TIME(SYS.DATE_TIME,"出库时间"),	//出库时间
		GOODS_CODE(SYS.CODE__20, "货物代码"),
		NAME(SYS.NAME__100, "货物名称"),
		SPEC(SYS.STR__200_NULL,"规格"),
		UOM(GsUom.fldOutKey()),	//计量单位
		QTY(SYS.QTY),	//数量
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
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
		T.CODE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//@formatter:on
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private String _code;	// 出库单号  STR(40)
  private Date _outTime;	// 出库时间  TIME
  private String _goodsCode;	// 货物代码  STR(20)
  private String _name;	// 货物名称  STR(100)
  private String _spec;	// 规格  STR(200)<null>
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)

	@Override
  public GsReportSalOut init(){
		super.init();
    _code=null;	// 出库单号  STR(40)
    _outTime=Env.getTranBeginTime();	// 出库时间  TIME
    _goodsCode=null;	// 货物代码  STR(20)
    _name=null;	// 货物名称  STR(100)
    _spec=null;	// 规格  STR(200)
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    return this;
  }

  //方法----------------------------------------------
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Date getOutTime(){
    return _outTime;
  }
  public void setOutTime(Date outTime){
    _outTime=outTime;
  }
  public String getGoodsCode(){
    return _goodsCode;
  }
  public void setGoodsCode(String goodsCode){
    _goodsCode=goodsCode;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getSpec(){
    return _spec;
  }
  public void setSpec(String spec){
    _spec=spec;
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

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
