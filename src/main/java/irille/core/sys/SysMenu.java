//Created on 2005-10-20
package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysMenu extends BeanInt<SysMenu> implements Comparable {
	private static final Log LOG = new Log(SysMenu.class);

	public static final Tb TB = new Tb(SysMenu.class, "系统菜单").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		MENU_UP(SYS.MENU, "上级菜单", true),
//		CODE(SYS.CODE__20), //可以用"."分隔进行多级设置
		NAME(SYS.NAME__100),
		URL(SYS.URL__NULL, "链接地址"),
		TYPE(SYS.STR__40_NULL, "分组类型"), 
		SORT(SYS.SORT__SHORT),
		ICO(SYS.ICO__NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_UP_SORT = TB.addIndex("upSort", 
				false,T.MENU_UP,T.SORT);
		public static final Index IDX_TYPE = TB.addIndex("type", 
				false,T.TYPE);
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

	@Override
	public final int compareTo(Object obj) {
		// 菜单排序,Collections.sort(ary);需实现此方法
		SysMenu cb = (SysMenu) obj;
		return getSort().compareTo(cb.getSort());
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _menuUp;	// 上级菜单 <表主键:SysMenu>  INT<null>
  private String _name;	// 名称  STR(100)
  private String _url;	// 链接地址  STR(200)<null>
  private String _type;	// 分组类型  STR(40)<null>
  private Short _sort;	// 排序号  SHORT
  private String _ico;	// 图标  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysMenu init(){
		super.init();
    _menuUp=null;	// 上级菜单 <表主键:SysMenu>  INT
    _name=null;	// 名称  STR(100)
    _url=null;	// 链接地址  STR(200)
    _type=null;	// 分组类型  STR(40)
    _sort=0;	// 排序号  SHORT
    _ico=null;	// 图标  STR(200)
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
  public Integer getMenuUp(){
    return _menuUp;
  }
  public void setMenuUp(Integer menuUp){
    _menuUp=menuUp;
  }
  public SysMenu gtMenuUp(){
    if(getMenuUp()==null)
      return null;
    return (SysMenu)get(SysMenu.class,getMenuUp());
  }
  public void stMenuUp(SysMenu menuUp){
    if(menuUp==null)
      setMenuUp(null);
    else
      setMenuUp(menuUp.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getUrl(){
    return _url;
  }
  public void setUrl(String url){
    _url=url;
  }
  public String getType(){
    return _type;
  }
  public void setType(String type){
    _type=type;
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
