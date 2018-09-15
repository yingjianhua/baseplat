package irille.gl.gl;

import irille.core.sys.SysCell;
import irille.core.sys.SysDept;
import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.pub.IPubVars;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 财务.流水账
 * @author whx
 */
public class GlDaybook extends BeanLong<GlDaybook> implements IPubVars{
	public static final Tb TB = new Tb(GlDaybook.class, "流水账").setAutoIncrement()
			.addActList().addActTally();
	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //此表主键类似Form的主键进行编号
		BILL(SYS.BILL),
		CODE(SYS.CODE__20),
		WORK_DATE(SYS.WORK_DATE),
		DEPT(SYS.DEPT),
		CELL(SYS.CELL),
		ORG(SYS.ORG),
		CREATE_TIME(SYS.CREATED_DATE_TIME),
		TALLY_BY(SYS.TALLY_BY),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_DATE_PKEY = TB.addIndex("datePkey", true,WORK_DATE,PKEY);
		public static final Index IDX_BILL = TB.addIndex("bill", true,BILL);
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
		return fldOutKey(MAIN_PKEY, TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
//	@Override
//	public void initSeq(SysSeq s) {
//		s.setPkey(gtTable().getPkey());
//		s.stOrgFlag(false);
//		s.stType(Sys.OType.DAY);
//	}

	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _bill;	// 凭证  LONG
  private String _code;	// 代码  STR(20)
  private Date _workDate;	// 工作日期  DATE
  private Integer _dept;	// 部门 <表主键:SysDept>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Date _createTime;	// 建档时间  TIME
  private Integer _tallyBy;	// 记账员 <表主键:SysUser>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlDaybook init(){
		super.init();
    _bill=null;	// 凭证  LONG
    _code=null;	// 代码  STR(20)
    _workDate=Env.getWorkDate();	// 工作日期  DATE
    _dept=null;	// 部门 <表主键:SysDept>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _createTime=Env.getTranBeginTime();	// 建档时间  TIME
    _tallyBy=null;	// 记账员 <表主键:SysUser>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlDaybook loadUniqueDatePkey(boolean lockFlag,Date workDate,Long pkey) {
    return (GlDaybook)loadUnique(T.IDX_DATE_PKEY,lockFlag,workDate,pkey);
  }
  public static GlDaybook chkUniqueDatePkey(boolean lockFlag,Date workDate,Long pkey) {
    return (GlDaybook)chkUnique(T.IDX_DATE_PKEY,lockFlag,workDate,pkey);
  }
  public static GlDaybook loadUniqueBill(boolean lockFlag,Long bill) {
    return (GlDaybook)loadUnique(T.IDX_BILL,lockFlag,bill);
  }
  public static GlDaybook chkUniqueBill(boolean lockFlag,Long bill) {
    return (GlDaybook)chkUnique(T.IDX_BILL,lockFlag,bill);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Long getBill(){
    return _bill;
  }
  public void setBill(Long bill){
    _bill=bill;
  }
  //外部主键对象: IBill
  public Bean gtBill(){
    return (Bean)gtLongTbObj(getBill());
  }
  public void stBill(Bean bill){
      setBill(bill.gtLongPkey());
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Date getWorkDate(){
    return _workDate;
  }
  public void setWorkDate(Date workDate){
    _workDate=workDate;
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
  public Date getCreateTime(){
    return _createTime;
  }
  public void setCreateTime(Date createTime){
    _createTime=createTime;
  }
  public Integer getTallyBy(){
    return _tallyBy;
  }
  public void setTallyBy(Integer tallyBy){
    _tallyBy=tallyBy;
  }
  public SysUser gtTallyBy(){
    if(getTallyBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getTallyBy());
  }
  public void stTallyBy(SysUser tallyBy){
    if(tallyBy==null)
      setTallyBy(null);
    else
      setTallyBy(tallyBy.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
