package irille.gl.gl;

import irille.core.sys.SysTable;
import irille.core.sys.SysTemplat;
import irille.pub.Log;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

public class GlEntryDef extends BeanLong<GlEntryDef> {
	private static final Log LOG = new Log(GlEntryDef.class);
	public static final Tb TB = new Tb(GlEntryDef.class, "分录定义").setAutoIncrement().addActIUDL().addActEdit();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),
		TEMPLAT(SYS.TEMPLAT,"科目模板"), //为空表示所有模板都可通用
		TABLE_ID(SYS.TABLE_ID),
		CODE(SYS.CODE__40), //一个交易可以有多个不同的分录组合，但每次只能用一组
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_TEMPLAT_TABLE_CODE 
		   = TB.addIndex("templatTableCode", true,TEMPLAT,TABLE_ID,CODE);
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
  private Long _pkey;	// 编号  LONG
  private Integer _templat;	// 科目模板 <表主键:SysTemplat>  INT
  private Integer _tableId;	// 表 <表主键:SysTable>  INT
  private String _code;	// 代码  STR(40)
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlEntryDef init(){
		super.init();
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _tableId=null;	// 表 <表主键:SysTable>  INT
    _code=null;	// 代码  STR(40)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GlEntryDef loadUniqueTemplatTableCode(boolean lockFlag,Integer templat,Integer tableId,String code) {
    return (GlEntryDef)loadUnique(T.IDX_TEMPLAT_TABLE_CODE,lockFlag,templat,tableId,code);
  }
  public static GlEntryDef chkUniqueTemplatTableCode(boolean lockFlag,Integer templat,Integer tableId,String code) {
    return (GlEntryDef)chkUnique(T.IDX_TEMPLAT_TABLE_CODE,lockFlag,templat,tableId,code);
  }
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Integer getTemplat(){
    return _templat;
  }
  public void setTemplat(Integer templat){
    _templat=templat;
  }
  public SysTemplat gtTemplat(){
    if(getTemplat()==null)
      return null;
    return (SysTemplat)get(SysTemplat.class,getTemplat());
  }
  public void stTemplat(SysTemplat templat){
    if(templat==null)
      setTemplat(null);
    else
      setTemplat(templat.getPkey());
  }
  public Integer getTableId(){
    return _tableId;
  }
  public void setTableId(Integer tableId){
    _tableId=tableId;
  }
  public SysTable gtTableId(){
    if(getTableId()==null)
      return null;
    return (SysTable)get(SysTable.class,getTableId());
  }
  public void stTableId(SysTable tableId){
    if(tableId==null)
      setTableId(null);
    else
      setTableId(tableId.getPkey());
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
