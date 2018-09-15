package irille.pss.sal;

import irille.pub.bean.BeanStr;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;

public class SalCollect extends BeanStr<SalCollect> {
	public static final Tb TB = new TbView(SalCollect.class, "销售汇总	").setAutoLocal().addActList()
	.addActOpt("fdToday", "本日销售查询").addActOpt("fdMonth", "本月销售查询")
	.addActOpt("fdTotal", "总账销售查询").addActOpt("fdLimit", "制定日期查询");
	
	public enum T implements IEnumFld {//@formatter:off
		CODE(SYS.STR__20, "代码"),
		NAME(SYS.NAME__100,"名称"),
		AMT_SAL(SYS.AMT_COST,"销售总金额"),
		AMT_RTN(SYS.AMT_COST,"退货总金额"),
		AMT_CASH(SYS.AMT_COST,"全额现金"),
		AMT_REC(SYS.AMT_COST,"全额赊账"),
		AMT_RECBACK(SYS.AMT_COST,"回款总额"),
		AMT_ORDER(SYS.AMT_COST,"冲减订金金额"),
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
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private BigDecimal _amtSal;	// 销售总金额  DEC(16,2)
  private BigDecimal _amtRtn;	// 退货总金额  DEC(16,2)
  private BigDecimal _amtCash;	// 全额现金  DEC(16,2)
  private BigDecimal _amtRec;	// 全额赊账  DEC(16,2)
  private BigDecimal _amtRecback;	// 回款总额  DEC(16,2)
  private BigDecimal _amtOrder;	// 冲减订金金额  DEC(16,2)

	@Override
  public SalCollect init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _amtSal=ZERO;	// 销售总金额  DEC(16,2)
    _amtRtn=ZERO;	// 退货总金额  DEC(16,2)
    _amtCash=ZERO;	// 全额现金  DEC(16,2)
    _amtRec=ZERO;	// 全额赊账  DEC(16,2)
    _amtRecback=ZERO;	// 回款总额  DEC(16,2)
    _amtOrder=ZERO;	// 冲减订金金额  DEC(16,2)
    return this;
  }

  //方法----------------------------------------------
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public BigDecimal getAmtSal(){
    return _amtSal;
  }
  public void setAmtSal(BigDecimal amtSal){
    _amtSal=amtSal;
  }
  public BigDecimal getAmtRtn(){
    return _amtRtn;
  }
  public void setAmtRtn(BigDecimal amtRtn){
    _amtRtn=amtRtn;
  }
  public BigDecimal getAmtCash(){
    return _amtCash;
  }
  public void setAmtCash(BigDecimal amtCash){
    _amtCash=amtCash;
  }
  public BigDecimal getAmtRec(){
    return _amtRec;
  }
  public void setAmtRec(BigDecimal amtRec){
    _amtRec=amtRec;
  }
  public BigDecimal getAmtRecback(){
    return _amtRecback;
  }
  public void setAmtRecback(BigDecimal amtRecback){
    _amtRecback=amtRecback;
  }
  public BigDecimal getAmtOrder(){
    return _amtOrder;
  }
  public void setAmtOrder(BigDecimal amtOrder){
    _amtOrder=amtOrder;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
