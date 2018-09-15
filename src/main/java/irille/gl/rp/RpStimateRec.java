/**
 * 
 */
package irille.gl.rp;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;
import java.util.Date;

public class RpStimateRec extends BeanLong {
	public static final Tb TB = new Tb(RpStimateRec.class, "待收款登记").setAutoIncrement().addActList()
	    .addActOpt("doProc", "收款").addActOpt("unProc", "收款取消");

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		OBJ(SYS.BEAN_LONG),
		NAME(SYS.NAME__40),
		AMT(SYS.AMT),
		BALANCE(SYS.BALANCE),
		CLEAR_TIME(SYS.TIME, "结清时间", true),
		DEPT(SYS.DEPT),
		CELL(SYS.CELL),
		ORG(SYS.ORG),
		CREATED_BY(SYS.CREATED_BY),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		ORIG_FORM_NUM(TB.get("origFormNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_FORM = TB.addIndex("form", true,ORIG_FORM);
		public static final Index IDX_FORM_NUM = TB.addIndex("formNum", false,ORIG_FORM_NUM);
		public static final Index IDX_NAME = TB.addIndex("name", false,NAME);
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
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Long _obj;	// 对象  LONG
  private String _name;	// 名称  STR(40)
  private BigDecimal _amt;	// 金额  DEC(16,2)
  private BigDecimal _balance;	// 余额  DEC(16,2)
  private Date _clearTime;	// 结清时间  TIME<null>
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _createdBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createdTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public RpStimateRec init(){
		super.init();
    _origForm=null;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _obj=null;	// 对象  LONG
    _name=null;	// 名称  STR(40)
    _amt=ZERO;	// 金额  DEC(16,2)
    _balance=ZERO;	// 余额  DEC(16,2)
    _clearTime=null;	// 结清时间  TIME
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _createdBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static RpStimateRec loadUniqueForm(boolean lockFlag,Long origForm) {
    return (RpStimateRec)loadUnique(T.IDX_FORM,lockFlag,origForm);
  }
  public static RpStimateRec chkUniqueForm(boolean lockFlag,Long origForm) {
    return (RpStimateRec)chkUnique(T.IDX_FORM,lockFlag,origForm);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Long getOrigForm(){
    return _origForm;
  }
  public void setOrigForm(Long origForm){
    _origForm=origForm;
  }
  //外部主键对象: IForm
  public Bean gtOrigForm(){
    return (Bean)gtLongTbObj(getOrigForm());
  }
  public void stOrigForm(Bean origForm){
      setOrigForm(origForm.gtLongPkey());
  }
  public String getOrigFormNum(){
    return _origFormNum;
  }
  public void setOrigFormNum(String origFormNum){
    _origFormNum=origFormNum;
  }
  public Long getObj(){
    return _obj;
  }
  public void setObj(Long obj){
    _obj=obj;
  }
  //外部主键对象: IBeanLong
  public Bean gtObj(){
    return (Bean)gtLongTbObj(getObj());
  }
  public void stObj(Bean obj){
      setObj(obj.gtLongPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public BigDecimal getAmt(){
    return _amt;
  }
  public void setAmt(BigDecimal amt){
    _amt=amt;
  }
  public BigDecimal getBalance(){
    return _balance;
  }
  public void setBalance(BigDecimal balance){
    _balance=balance;
  }
  public Date getClearTime(){
    return _clearTime;
  }
  public void setClearTime(Date clearTime){
    _clearTime=clearTime;
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
  public Integer getCreatedBy(){
    return _createdBy;
  }
  public void setCreatedBy(Integer createdBy){
    _createdBy=createdBy;
  }
  public SysUser gtCreatedBy(){
    if(getCreatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreatedBy());
  }
  public void stCreatedBy(SysUser createdBy){
    if(createdBy==null)
      setCreatedBy(null);
    else
      setCreatedBy(createdBy.getPkey());
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
