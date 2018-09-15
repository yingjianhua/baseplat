package irille.gl.gl;

import irille.core.sys.SysOrg;
import irille.core.sys.SysUser;
import irille.pub.bean.BeanLong;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

import java.util.Date;

public class GlReportProfit extends BeanLong<GlReportProfit> {
	public static final Tb TB = new Tb(GlReportProfit.class, "利润表").setAutoIncrement().addActIns().addActDel()
	    .addActList().addActPrint();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //此表主键类似Form的主键进行编号
		ORG(SYS.ORG),
		BEGIN_DATE(SYS.DATE,"起始日期"),
		END_DATE(SYS.DATE,"结束日期"),
		REM(SYS.REM__200_NULL),
		CREATE_BY(SYS.CREATED_BY),
		CREATE_TIME(SYS.CREATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
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
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Date _beginDate;	// 起始日期  DATE
  private Date _endDate;	// 结束日期  DATE
  private String _rem;	// 备注  STR(200)<null>
  private Integer _createBy;	// 建档员 <表主键:SysUser>  INT
  private Date _createTime;	// 建档时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlReportProfit init(){
		super.init();
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _beginDate=null;	// 起始日期  DATE
    _endDate=null;	// 结束日期  DATE
    _rem=null;	// 备注  STR(200)
    _createBy=Idu.getUser().getPkey();	// 建档员 <表主键:SysUser>  INT
    _createTime=Env.getTranBeginTime();	// 建档时间  TIME
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
  public Date getBeginDate(){
    return _beginDate;
  }
  public void setBeginDate(Date beginDate){
    _beginDate=beginDate;
  }
  public Date getEndDate(){
    return _endDate;
  }
  public void setEndDate(Date endDate){
    _endDate=endDate;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Integer getCreateBy(){
    return _createBy;
  }
  public void setCreateBy(Integer createBy){
    _createBy=createBy;
  }
  public SysUser gtCreateBy(){
    if(getCreateBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getCreateBy());
  }
  public void stCreateBy(SysUser createBy){
    if(createBy==null)
      setCreateBy(null);
    else
      setCreateBy(createBy.getPkey());
  }
  public Date getCreateTime(){
    return _createTime;
  }
  public void setCreateTime(Date createTime){
    _createTime=createTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
