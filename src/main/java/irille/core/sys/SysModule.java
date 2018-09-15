package irille.core.sys;

import irille.pub.bean.BeanStr;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class SysModule extends BeanStr<SysModule> {
	public static final Tb TB = new Tb(SysModule.class, "系统模块").addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtStrPkey()),
		NAME(SYS.NAME__100),
		LICENSES(SYS.INT_PLUS_OR_ZERO,"许可数"),
		MENUS(SYS.STR__100_NULL, "菜单模块"),
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
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private String _pkey;	// 编号  STR(20)
  private String _name;	// 名称  STR(100)
  private Integer _licenses;	// 许可数  INT
  private String _menus;	// 菜单模块  STR(100)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysModule init(){
		super.init();
    _name=null;	// 名称  STR(100)
    _licenses=0;	// 许可数  INT
    _menus=null;	// 菜单模块  STR(100)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public String getPkey(){
    return _pkey;
  }
  public void setPkey(String pkey){
    _pkey=pkey;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getLicenses(){
    return _licenses;
  }
  public void setLicenses(Integer licenses){
    _licenses=licenses;
  }
  public String getMenus(){
    return _menus;
  }
  public void setMenus(String menus){
    _menus=menus;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
