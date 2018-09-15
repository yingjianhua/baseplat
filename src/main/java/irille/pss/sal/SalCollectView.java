package irille.pss.sal;

import irille.core.sys.SysUser;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.TbView;

import java.math.BigDecimal;
import java.util.Date;

public class SalCollectView extends BeanInt<SalCollectView> {
	public static final Tb TB = new TbView(SalCollectView.class, "销售汇总", "销售汇总").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__40),
		NAME(SYS.NAME__100),
		SAL_AMT(SYS.AMT, "销售总金额"),
		RTN_AMT(SYS.AMT, "退货总金额"),
		CASH_AMT(SYS, "全额现金"),
		RECEIPT_AMT(SYS, "全额赊账"),
		ORDER_AMT(SYS, "冲减订金金额"),
		DATE(SYS.DATE),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
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
  private String _code;	// 代码  STR(40)
  private String _name;	// 名称  STR(100)
  private BigDecimal _salAmt;	// 销售总金额  DEC(16,2)
  private BigDecimal _rtnAmt;	// 退货总金额  DEC(16,2)
  private Integer _cashAmt;	// 全额现金 <表主键:SysUser>  INT
  private Integer _receiptAmt;	// 全额赊账 <表主键:SysUser>  INT
  private Integer _orderAmt;	// 冲减订金金额 <表主键:SysUser>  INT
  private Date _date;	// 日期  DATE

	@Override
  public SalCollectView init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _salAmt=ZERO;	// 销售总金额  DEC(16,2)
    _rtnAmt=ZERO;	// 退货总金额  DEC(16,2)
    _cashAmt=null;	// 全额现金 <表主键:SysUser>  INT
    _receiptAmt=null;	// 全额赊账 <表主键:SysUser>  INT
    _orderAmt=null;	// 冲减订金金额 <表主键:SysUser>  INT
    _date=null;	// 日期  DATE
    return this;
  }

  //方法----------------------------------------------
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
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
  public BigDecimal getSalAmt(){
    return _salAmt;
  }
  public void setSalAmt(BigDecimal salAmt){
    _salAmt=salAmt;
  }
  public BigDecimal getRtnAmt(){
    return _rtnAmt;
  }
  public void setRtnAmt(BigDecimal rtnAmt){
    _rtnAmt=rtnAmt;
  }
  public Integer getCashAmt(){
    return _cashAmt;
  }
  public void setCashAmt(Integer cashAmt){
    _cashAmt=cashAmt;
  }
  public SysUser gtCashAmt(){
    if(getCashAmt()==null)
      return null;
    return (SysUser)get(SysUser.class,getCashAmt());
  }
  public void stCashAmt(SysUser cashAmt){
    if(cashAmt==null)
      setCashAmt(null);
    else
      setCashAmt(cashAmt.getPkey());
  }
  public Integer getReceiptAmt(){
    return _receiptAmt;
  }
  public void setReceiptAmt(Integer receiptAmt){
    _receiptAmt=receiptAmt;
  }
  public SysUser gtReceiptAmt(){
    if(getReceiptAmt()==null)
      return null;
    return (SysUser)get(SysUser.class,getReceiptAmt());
  }
  public void stReceiptAmt(SysUser receiptAmt){
    if(receiptAmt==null)
      setReceiptAmt(null);
    else
      setReceiptAmt(receiptAmt.getPkey());
  }
  public Integer getOrderAmt(){
    return _orderAmt;
  }
  public void setOrderAmt(Integer orderAmt){
    _orderAmt=orderAmt;
  }
  public SysUser gtOrderAmt(){
    if(getOrderAmt()==null)
      return null;
    return (SysUser)get(SysUser.class,getOrderAmt());
  }
  public void stOrderAmt(SysUser orderAmt){
    if(orderAmt==null)
      setOrderAmt(null);
    else
      setOrderAmt(orderAmt.getPkey());
  }
  public Date getDate(){
    return _date;
  }
  public void setDate(Date date){
    _date=date;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
