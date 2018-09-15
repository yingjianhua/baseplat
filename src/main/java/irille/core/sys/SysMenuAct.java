//Created on 2005-10-20
package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysMenuAct extends BeanInt<SysMenuAct> {
	private static final Log LOG = new Log(SysMenuAct.class);

	public static final Tb TB = new Tb(SysMenuAct.class, "菜单功能").setAutoIncrement();
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		MENU(SYS.MENU, "所属菜单",true),
		ACT(SYS.ACT),
		TABLE_CODE(SYS.CODE__40,"交易代码"),
		TABLE_NAME(SYS.NAME__100,"交易名称"),
		CODE(SYS.CODE__20), //可以用"."分隔进行多级设置
		NAME(SYS.NAME__100),
		CSS(SYS.STR__100_NULL,"页面样式"),
		SORT(SYS.SORT__SHORT),
		ICO(SYS.ICO__NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_MENU_ACT = TB.addIndex("menuAct", true,
				T.MENU,T.ACT);
		public static final Index IDX_MENU_SORT = TB.addIndex("menuSort", 
				false,T.MENU,T.SORT);
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

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _menu;	// 所属菜单 <表主键:SysMenu>  INT<null>
  private Integer _act;	// 功能 <表主键:SysTableAct>  INT
  private String _tableCode;	// 交易代码  STR(40)
  private String _tableName;	// 交易名称  STR(100)
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private String _css;	// 页面样式  STR(100)<null>
  private Short _sort;	// 排序号  SHORT
  private String _ico;	// 图标  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysMenuAct init(){
		super.init();
    _menu=null;	// 所属菜单 <表主键:SysMenu>  INT
    _act=null;	// 功能 <表主键:SysTableAct>  INT
    _tableCode=null;	// 交易代码  STR(40)
    _tableName=null;	// 交易名称  STR(100)
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _css=null;	// 页面样式  STR(100)
    _sort=0;	// 排序号  SHORT
    _ico=null;	// 图标  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysMenuAct loadUniqueMenuAct(boolean lockFlag,Integer menu,Integer act) {
    return (SysMenuAct)loadUnique(T.IDX_MENU_ACT,lockFlag,menu,act);
  }
  public static SysMenuAct chkUniqueMenuAct(boolean lockFlag,Integer menu,Integer act) {
    return (SysMenuAct)chkUnique(T.IDX_MENU_ACT,lockFlag,menu,act);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getMenu(){
    return _menu;
  }
  public void setMenu(Integer menu){
    _menu=menu;
  }
  public SysMenu gtMenu(){
    if(getMenu()==null)
      return null;
    return (SysMenu)get(SysMenu.class,getMenu());
  }
  public void stMenu(SysMenu menu){
    if(menu==null)
      setMenu(null);
    else
      setMenu(menu.getPkey());
  }
  public Integer getAct(){
    return _act;
  }
  public void setAct(Integer act){
    _act=act;
  }
  public SysTableAct gtAct(){
    if(getAct()==null)
      return null;
    return (SysTableAct)get(SysTableAct.class,getAct());
  }
  public void stAct(SysTableAct act){
    if(act==null)
      setAct(null);
    else
      setAct(act.getPkey());
  }
  public String getTableCode(){
    return _tableCode;
  }
  public void setTableCode(String tableCode){
    _tableCode=tableCode;
  }
  public String getTableName(){
    return _tableName;
  }
  public void setTableName(String tableName){
    _tableName=tableName;
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
  public String getCss(){
    return _css;
  }
  public void setCss(String css){
    _css=css;
  }
  public Short getSort(){
    return _sort;
  }
  public void setSort(Short sort){
    _sort=sort;
  }
  public String getIco(){
    return _ico;
  }
  public void setIco(String ico){
    _ico=ico;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
