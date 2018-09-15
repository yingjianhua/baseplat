package irille.core.sys;

import irille.pub.bean.BeanStr;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class SysCtype extends BeanStr<SysCtype> {
	public static final Tb TB = new Tb(SysCtype.class, "系统参数").addActEdit()
			.addActList();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtStrPkey(20)),
		CTYPE_NAME(SYS.NAME__100),
		CTYPE_DES(SYS.STR__100_NULL, "描述"),
		CTYPE_LEN(SYS.BYTE, "代码长度"),
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
  private String _ctypeName;	// 名称  STR(100)
  private String _ctypeDes;	// 描述  STR(100)<null>
  private Byte _ctypeLen;	// 代码长度  BYTE
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysCtype init(){
		super.init();
    _ctypeName=null;	// 名称  STR(100)
    _ctypeDes=null;	// 描述  STR(100)
    _ctypeLen=0;	// 代码长度  BYTE
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
  public String getCtypeName(){
    return _ctypeName;
  }
  public void setCtypeName(String ctypeName){
    _ctypeName=ctypeName;
  }
  public String getCtypeDes(){
    return _ctypeDes;
  }
  public void setCtypeDes(String ctypeDes){
    _ctypeDes=ctypeDes;
  }
  public Byte getCtypeLen(){
    return _ctypeLen;
  }
  public void setCtypeLen(Byte ctypeLen){
    _ctypeLen=ctypeLen;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
