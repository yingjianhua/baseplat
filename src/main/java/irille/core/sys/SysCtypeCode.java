package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class SysCtypeCode extends BeanInt<SysCtypeCode> {
	public static final Tb TB = new Tb(SysCtypeCode.class, "系统参数明细").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CTYPE_TYPE(SysCtype.fldOutKey()),
		CODE_VALUE(SYS.CODE__40, "参数代码"),
		CODE_NAME(SYS.NAME__100, "参数名称"),
		CODE_DES(SYS.STR__100_NULL, "描述"),
		LINES(Tb.crtLines(T.CTYPE_TYPE, CN_LINES,	T.PKEY)),
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
  private Integer _pkey;	// 编号  INT
  private String _ctypeType;	// 系统参数 <表主键:SysCtype>  STR(20)
  private String _codeValue;	// 参数代码  STR(40)
  private String _codeName;	// 参数名称  STR(100)
  private String _codeDes;	// 描述  STR(100)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysCtypeCode init(){
		super.init();
    _ctypeType=null;	// 系统参数 <表主键:SysCtype>  STR(20)
    _codeValue=null;	// 参数代码  STR(40)
    _codeName=null;	// 参数名称  STR(100)
    _codeDes=null;	// 描述  STR(100)
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
  public String getCtypeType(){
    return _ctypeType;
  }
  public void setCtypeType(String ctypeType){
    _ctypeType=ctypeType;
  }
  public SysCtype gtCtypeType(){
    if(getCtypeType()==null)
      return null;
    return (SysCtype)get(SysCtype.class,getCtypeType());
  }
  public void stCtypeType(SysCtype ctypeType){
    if(ctypeType==null)
      setCtypeType(null);
    else
      setCtypeType(ctypeType.getPkey());
  }
  public String getCodeValue(){
    return _codeValue;
  }
  public void setCodeValue(String codeValue){
    _codeValue=codeValue;
  }
  public String getCodeName(){
    return _codeName;
  }
  public void setCodeName(String codeName){
    _codeName=codeName;
  }
  public String getCodeDes(){
    return _codeDes;
  }
  public void setCodeDes(String codeDes){
    _codeDes=codeDes;
  }
  public static java.util.List<SysCtypeCode> getLines(irille.core.sys.SysCtype mainBean){
    return list(irille.core.sys.SysCtypeCode.class,
        " ctype_type=? ORDER BY pkey",false,
        mainBean.getPkey());
  }
  public static java.util.List<SysCtypeCode> getLines(irille.core.sys.SysCtype mainBean, int idx,int count){
    return list(irille.core.sys.SysCtypeCode.class,false," ctype_type=? ORDER BY pkey DESC",idx,count,mainBean.getPkey());
  }
  public static int getLinesCount(irille.core.sys.SysCtype mainBean){
    return ((Number) queryOneRow("SELECT count(*) FROM sys_ctype_code WHERE ctype_type=? ORDER BY pkey",mainBean.getPkey())[0]).intValue();
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
