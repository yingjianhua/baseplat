package irille.pss.sal;

import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsKind;
import irille.gl.gs.GsWarehouse;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.gl.AccObjs;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class SalRptLine extends BeanLong<SalRptLine> {
	private static final Log LOG = new Log(SalRptLine.class);

	public static final Tb TB = new Tb(SalRptLine.class, "销售报表流水", "销售流水")
			.setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		CUST(SysCustom.fldOutKey()),
		GOODS_KIND(GsGoodsKind.fldOutKey()),
		GOODS(GsGoods.fldOutKey()),
		QTY(SYS.QTY),
		AMT(SYS.AMT),
		DATE(SYS.DATE),
		OPERATOR(SYS.USER_SYS, "操作员"),
		DEPT(SysDept.fldOutKey()),
		ORG(SysOrg.fldOutKey()),
		WAREHOUSE(GsWarehouse.fldOutKey().setNull()),
		FORM(SYS.ORIG_FORM__CODE),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		FORM_NUM(TB.get("formNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_DATE_CUST_GOODS= TB.addIndex("dateCustGoods",false,DATE, CUST, GOODS);
		public static final Index IDX_CUST= TB.addIndex("cust",false,CUST);
		public static final Index IDX_GOODS_KIND= TB.addIndex("goodsKind",false,GOODS_KIND);
		public static final Index IDX_FORM= TB.addIndex("form",false,ORG,FORM);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return 	_fld;}
	}

	static { //在此可以加一些对FLD进行特殊设定的代码
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
		//@formatter:on

	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}

	public void getAccObjs(String name, AccObjs objs) {
	}

	// >>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _cust;	// 客户 <表主键:SysCustom>  INT
  private Integer _goodsKind;	// 货物类别 <表主键:GsGoodsKind>  INT
  private Integer _goods;	// 货物 <表主键:GsGoods>  INT
  private BigDecimal _qty;	// 数量  DEC(14,4)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private Date _date;	// 日期  DATE
  private Integer _operator;	// 操作员 <表主键:SysUser>  INT
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _warehouse;	// 仓库 <表主键:GsWarehouse>  INT<null>
  private Long _form;	// 源单据  LONG
  private String _formNum;	// 源单据号  STR(40)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SalRptLine init(){
		super.init();
    _cust=null;	// 客户 <表主键:SysCustom>  INT
    _goodsKind=null;	// 货物类别 <表主键:GsGoodsKind>  INT
    _goods=null;	// 货物 <表主键:GsGoods>  INT
    _qty=ZERO;	// 数量  DEC(14,4)
    _amt=ZERO;	// 金额  DEC(16,2)
    _date=null;	// 日期  DATE
    _operator=null;	// 操作员 <表主键:SysUser>  INT
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _warehouse=null;	// 仓库 <表主键:GsWarehouse>  INT
    _form=null;	// 源单据  LONG
    _formNum=null;	// 源单据号  STR(40)
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
  public Integer getGoodsKind(){
    return _goodsKind;
  }
  public void setGoodsKind(Integer goodsKind){
    _goodsKind=goodsKind;
  }
  public GsGoodsKind gtGoodsKind(){
    if(getGoodsKind()==null)
      return null;
    return (GsGoodsKind)get(GsGoodsKind.class,getGoodsKind());
  }
  public void stGoodsKind(GsGoodsKind goodsKind){
    if(goodsKind==null)
      setGoodsKind(null);
    else
      setGoodsKind(goodsKind.getPkey());
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
  public BigDecimal getQty(){
    return _qty;
  }
  public void setQty(BigDecimal qty){
    _qty=qty;
  }
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public Date getDate(){
    return _date;
  }
  public void setDate(Date date){
    _date=date;
  }
  public Integer getOperator(){
    return _operator;
  }
  public void setOperator(Integer operator){
    _operator=operator;
  }
  public SysUser gtOperator(){
    if(getOperator()==null)
      return null;
    return (SysUser)get(SysUser.class,getOperator());
  }
  public void stOperator(SysUser operator){
    if(operator==null)
      setOperator(null);
    else
      setOperator(operator.getPkey());
  }
  public Integer getDept(){
    return _dept;
  }
  public void setDept(Integer dept){
    _dept=dept;
  }
  public SysDept gtDept(){
    if(getDept()==null)
      return null;
    return (SysDept)get(SysDept.class,getDept());
  }
  public void stDept(SysDept dept){
    if(dept==null)
      setDept(null);
    else
      setDept(dept.getPkey());
  }
  public Integer getOrg(){
    return _org;
  }
  public void setOrg(Integer org){
    _org=org;
  }
  public SysOrg gtOrg(){
    if(getOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrg());
  }
  public void stOrg(SysOrg org){
    if(org==null)
      setOrg(null);
    else
      setOrg(org.getPkey());
  }
  public Integer getWarehouse(){
    return _warehouse;
  }
  public void setWarehouse(Integer warehouse){
    _warehouse=warehouse;
  }
  public GsWarehouse gtWarehouse(){
    if(getWarehouse()==null)
      return null;
    return (GsWarehouse)get(GsWarehouse.class,getWarehouse());
  }
  public void stWarehouse(GsWarehouse warehouse){
    if(warehouse==null)
      setWarehouse(null);
    else
      setWarehouse(warehouse.getPkey());
  }
  public Long getForm(){
    return _form;
  }
  public void setForm(Long form){
    _form=form;
  }
  //外部主键对象: IForm
  public Bean gtForm(){
    return (Bean)gtLongTbObj(getForm());
  }
  public void stForm(Bean form){
      setForm(form.gtLongPkey());
  }
  public String getFormNum(){
    return _formNum;
  }
  public void setFormNum(String formNum){
    _formNum=formNum;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	// <<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
