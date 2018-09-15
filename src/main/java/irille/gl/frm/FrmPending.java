package irille.gl.frm;

import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.SysTable;
import irille.core.sys.SysUser;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanLong;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

/**
 * 需要被动产生一些目标单据的单据
 * @author whx
 *
 */
public class FrmPending extends BeanLong<FrmPending> {
	public static final Tb TB = new Tb(FrmPending.class, "待处理单据").setAutoIncrement().addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //单据号
		ORIG_FORM(SYS.ORIG_FORM__CODE),
		ORG(SYS.ORG),
		CELL(SYS.CELL),
		FORM_HANDOVER(FrmHandover.fldOutKey().setNull()), //非空表示在交接中
		USER_SYS(SYS.USER_SYS, "当前用户"),
		USER_CRT(SYS.USER_SYS, "创建用户"),
		DESC_TYPE(SYS.TABLE_ID, "目标单据类型"),
		CREATED_TIME(SYS.CREATED_DATE_TIME),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		ORIG_FORM_NUM(TB.get("origFormNum")),	//源单据号
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_ORG_DESC = TB.addIndex("orgDesc", false, ORG, DESC_TYPE);
		public static final Index IDX_FORM_DESC = TB.addIndex("formDesc", true, ORIG_FORM, DESC_TYPE);
		private Fld _fld;
		private T(Class clazz,String name,boolean... isnull) 
			{_fld=TB.addOutKey(clazz,this,name,isnull);	}
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
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private Long _origForm;	// 源单据  LONG
  private String _origFormNum;	// 源单据号  STR(40)
  private Integer _org;	// 机构 <表主键:SysOrg>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Long _formHandover;	// 单据交接单 <表主键:FrmHandover>  LONG<null>
  private Integer _userSys;	// 当前用户 <表主键:SysUser>  INT
  private Integer _userCrt;	// 创建用户 <表主键:SysUser>  INT
  private Integer _descType;	// 目标单据类型 <表主键:SysTable>  INT
  private Date _createdTime;	// 建档时间  TIME
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public FrmPending init(){
		super.init();
    _origForm=null;	// 源单据  LONG
    _origFormNum=null;	// 源单据号  STR(40)
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _formHandover=null;	// 单据交接单 <表主键:FrmHandover>  LONG
    _userSys=null;	// 当前用户 <表主键:SysUser>  INT
    _userCrt=null;	// 创建用户 <表主键:SysUser>  INT
    _descType=null;	// 目标单据类型 <表主键:SysTable>  INT
    _createdTime=Env.getTranBeginTime();	// 建档时间  TIME
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static FrmPending loadUniqueFormDesc(boolean lockFlag,Long origForm,Integer descType) {
    return (FrmPending)loadUnique(T.IDX_FORM_DESC,lockFlag,origForm,descType);
  }
  public static FrmPending chkUniqueFormDesc(boolean lockFlag,Long origForm,Integer descType) {
    return (FrmPending)chkUnique(T.IDX_FORM_DESC,lockFlag,origForm,descType);
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
  public Long getFormHandover(){
    return _formHandover;
  }
  public void setFormHandover(Long formHandover){
    _formHandover=formHandover;
  }
  public FrmHandover gtFormHandover(){
    if(getFormHandover()==null)
      return null;
    return (FrmHandover)get(FrmHandover.class,getFormHandover());
  }
  public void stFormHandover(FrmHandover formHandover){
    if(formHandover==null)
      setFormHandover(null);
    else
      setFormHandover(formHandover.getPkey());
  }
  public Integer getUserSys(){
    return _userSys;
  }
  public void setUserSys(Integer userSys){
    _userSys=userSys;
  }
  public SysUser gtUserSys(){
    if(getUserSys()==null)
      return null;
    return (SysUser)get(SysUser.class,getUserSys());
  }
  public void stUserSys(SysUser userSys){
    if(userSys==null)
      setUserSys(null);
    else
      setUserSys(userSys.getPkey());
  }
  public Integer getUserCrt(){
    return _userCrt;
  }
  public void setUserCrt(Integer userCrt){
    _userCrt=userCrt;
  }
  public SysUser gtUserCrt(){
    if(getUserCrt()==null)
      return null;
    return (SysUser)get(SysUser.class,getUserCrt());
  }
  public void stUserCrt(SysUser userCrt){
    if(userCrt==null)
      setUserCrt(null);
    else
      setUserCrt(userCrt.getPkey());
  }
  public Integer getDescType(){
    return _descType;
  }
  public void setDescType(Integer descType){
    _descType=descType;
  }
  public SysTable gtDescType(){
    if(getDescType()==null)
      return null;
    return (SysTable)get(SysTable.class,getDescType());
  }
  public void stDescType(SysTable descType){
    if(descType==null)
      setDescType(null);
    else
      setDescType(descType.getPkey());
  }
  public Date getCreatedTime(){
    return _createdTime;
  }
  public void setCreatedTime(Date createdTime){
    _createdTime=createdTime;
  }
  public String getRem(){
    return _rem;
  }
  public void setRem(String rem){
    _rem=rem;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
