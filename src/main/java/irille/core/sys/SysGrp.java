//Created on 2005-10-20
package irille.core.sys;

import irille.core.sys.Sys.ODispScope4;
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

/**
 * 用户组：组可以包含组及个人
 * 分：集团、机构、部门、用户级别，各级权限：
 * 集团级别：只有系统管理员可以建
 * 机构级别：可由系统管理员或机构系统管理员可以建
 * 部门机构：系统管理员，机构系统管理员，部门主管可以建
 * 个人：系统管理员，机构管理员或个人可以建
 * @author surface1
 *
 */
public class SysGrp extends BeanInt<SysGrp> {
	private static final Log LOG = new Log(SysGrp.class);
	public static final Tb TB = new Tb(SysGrp.class, "用户组管理").setAutoIncrement()
			.addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20), 
		NAME(SYS.NAME__100),
		SCOPE(TB.crtOptAndKey("scopeObj", "对象",Sys.ODispScope4.GRP)),
		UPDATED_BY(SYS.UPDATED_BY),
		UPDATED_DATE_TIME(SYS.UPDATED_DATE_TIME),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		DISP_SCOPE4(TB.get("dispScope4")),	//可视范围
		SCOPE_OBJ_PKEY(TB.get("scopeObjPkey")),	//对象主键值
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", false,T.CODE);
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
  private Byte _dispScope4;	// 可视范围 <ODispScope4>  BYTE
	// GRP:1,集团级
	// ORG:2,机构
	// DEPT:3,部门级
	// USER:4,用户
  private Integer _scopeObjPkey;	// 对象主键值  INT<null>
  private Integer _updatedBy;	// 更新员 <表主键:SysUser>  INT
  private Date _updatedDateTime;	// 更新时间  TIME
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysGrp init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _dispScope4=ODispScope4.DEFAULT.getLine().getKey();	// 可视范围 <ODispScope4>  BYTE
    _scopeObjPkey=null;	// 对象主键值  INT
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
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtScopeObj(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtDispScope4();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_scopeObjPkey);
  }
  public Byte getDispScope4(){
    return _dispScope4;
  }
  public void setDispScope4(Byte dispScope4){
    _dispScope4=dispScope4;
  }
  public ODispScope4 gtDispScope4(){
    return (ODispScope4)(ODispScope4.GRP.getLine().get(_dispScope4));
  }
  public void stDispScope4(ODispScope4 dispScope4){
    _dispScope4=dispScope4.getLine().getKey();
  }
  public Integer getScopeObjPkey(){
    return _scopeObjPkey;
  }
  public void setScopeObjPkey(Integer scopeObjPkey){
    _scopeObjPkey=scopeObjPkey;
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
