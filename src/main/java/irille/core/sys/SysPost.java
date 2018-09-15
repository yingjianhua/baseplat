//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.ODispScope2;
import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.util.Date;

public class SysPost extends BeanInt<SysPost> {
	private static final Log LOG = new Log(SysPost.class);
	public static final Tb TB = new Tb(SysPost.class, "职位管理","职位").setAutoIncrement()
			.addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20), 
		NAME(SYS.NAME__100),
		ENABLED(SYS.ENABLED),
		SCOPE(TB.crtOptAndKey("scopeObj", "级别对象",Sys.ODispScope2.ORG)),
		POSTLEVEL(SYS.SHORT,"职位级别"),
		UPDATED_BY(SYS.UPDATED_BY),
		UPDATED_DATE_TIME(SYS.UPDATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		DISP_SCOPE2(TB.get("dispScope2")),	//可视范围
		SCOPE_OBJ_PKEY(TB.get("scopeObjPkey")),	//级别对象主键值
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
//		public static final FldLines FLD_USER_MX = TB.crtLines(SysUserRole.FLD_ROLE_PKEY, "userMx",
//		    "用户角色", SysUserRole.FLD_PKEY);
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", false,T.CODE);
		public static final Index IDX_SCOPE = TB.addIndex("scope",
				false,T.DISP_SCOPE2,T.SCOPE_OBJ_PKEY);
		private Fld _fld;
		private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
		private T(IEnumFld fld, String name,boolean... null1) {
			_fld=TB.add(fld,this,name,null1);}
		private T(IEnumFld fld, String name,int strLen) {
			_fld=TB.add(fld,this,name,strLen);}
		private T(Fld fld) {_fld=TB.add(fld); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.CODE._fld.setHelp("可以重复, 便于快速输入。");
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on
	//
	// public String toStringExt() {
	// return getRoleName();
	// }
	//
	// public String toStringExtView() {
	// return "roleName";
	// }

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Byte _dispScope2;	// 可视范围 <ODispScope2>  BYTE
	// GRP:1,集团级
	// ORG:2,机构
  private Integer _scopeObjPkey;	// 级别对象主键值  INT<null>
  private Short _postlevel;	// 职位级别  SHORT
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedDateTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysPost init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _dispScope2=ODispScope2.DEFAULT.getLine().getKey();	// 可视范围 <ODispScope2>  BYTE
    _scopeObjPkey=null;	// 级别对象主键值  INT
    _postlevel=0;	// 职位级别  SHORT
    _updatedBy=null;	// 更新员 <表主键:SysUser>  INT
    _updatedDateTime=Env.getTranBeginTime();	// 更新时间  TIME
    _rowVersion=0;	// 版本  SHORT
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
  public Byte getEnabled(){
    return _enabled;
  }
  public void setEnabled(Byte enabled){
    _enabled=enabled;
  }
  public Boolean gtEnabled(){
    return byteToBoolean(_enabled);
  }
  public void stEnabled(Boolean enabled){
    _enabled=booleanToByte(enabled);
  }
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtScopeObj(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtDispScope2();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_scopeObjPkey);
  }
  public Byte getDispScope2(){
    return _dispScope2;
  }
  public void setDispScope2(Byte dispScope2){
    _dispScope2=dispScope2;
  }
  public ODispScope2 gtDispScope2(){
    return (ODispScope2)(ODispScope2.ORG.getLine().get(_dispScope2));
  }
  public void stDispScope2(ODispScope2 dispScope2){
    _dispScope2=dispScope2.getLine().getKey();
  }
  public Integer getScopeObjPkey(){
    return _scopeObjPkey;
  }
  public void setScopeObjPkey(Integer scopeObjPkey){
    _scopeObjPkey=scopeObjPkey;
  }
  public Short getPostlevel(){
    return _postlevel;
  }
  public void setPostlevel(Short postlevel){
    _postlevel=postlevel;
  }
  public Integer getUpdatedBy(){
    return _updatedBy;
  }
  public void setUpdatedBy(Integer updatedBy){
    _updatedBy=updatedBy;
  }
  public SysUser gtUpdatedBy(){
    if(getUpdatedBy()==null)
      return null;
    return (SysUser)get(SysUser.class,getUpdatedBy());
  }
  public void stUpdatedBy(SysUser updatedBy){
    if(updatedBy==null)
      setUpdatedBy(null);
    else
      setUpdatedBy(updatedBy.getPkey());
  }
  public Date getUpdatedDateTime(){
    return _updatedDateTime;
  }
  public void setUpdatedDateTime(Date updatedDateTime){
    _updatedDateTime=updatedDateTime;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
