//Created on 2005-10-20
package irille.core.sys;

import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class SysTableAct extends BeanInt<SysTableAct> {
	private static final Log LOG = new Log(SysTableAct.class);

	public static final Tb TB = new Tb(SysTableAct.class, "菜单功能", "功能")
			.setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		SYS_TABLE(SYS.TABLE_ID),
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
		public static final Index IDX_TABLE_CODE = TB.addIndex("tableCode", true,T.SYS_TABLE,T.CODE);
		public static final Index IDX_TABLE_SORT = TB.addIndex("tableSort", 
				false,T.SYS_TABLE,T.SORT);
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
  private Integer _sysTable;	// 表 <表主键:SysTable>  INT
  private String _code;	// 代码  STR(20)
  private String _name;	// 名称  STR(100)
  private String _css;	// 页面样式  STR(100)<null>
  private Short _sort;	// 排序号  SHORT
  private String _ico;	// 图标  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysTableAct init(){
		super.init();
    _sysTable=null;	// 表 <表主键:SysTable>  INT
    _code=null;	// 代码  STR(20)
    _name=null;	// 名称  STR(100)
    _css=null;	// 页面样式  STR(100)
    _sort=0;	// 排序号  SHORT
    _ico=null;	// 图标  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysTableAct loadUniqueTableCode(boolean lockFlag,Integer sysTable,String code) {
    return (SysTableAct)loadUnique(T.IDX_TABLE_CODE,lockFlag,sysTable,code);
  }
  public static SysTableAct chkUniqueTableCode(boolean lockFlag,Integer sysTable,String code) {
    return (SysTableAct)chkUnique(T.IDX_TABLE_CODE,lockFlag,sysTable,code);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getSysTable(){
    return _sysTable;
  }
  public void setSysTable(Integer sysTable){
    _sysTable=sysTable;
  }
  public SysTable gtSysTable(){
    if(getSysTable()==null)
      return null;
    return (SysTable)get(SysTable.class,getSysTable());
  }
  public void stSysTable(SysTable sysTable){
    if(sysTable==null)
      setSysTable(null);
    else
      setSysTable(sysTable.getPkey());
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
