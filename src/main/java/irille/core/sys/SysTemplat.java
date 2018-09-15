package irille.core.sys;

import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OTemplateType;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * TODO +表-核算单元对照表，表示哪些单元可以用
 * @author whx
 * @version 创建时间：2014年8月21日 上午9:32:12
 */
public class SysTemplat extends BeanInt<SysTemplat> implements IExtName {
	public static final Tb TB = new Tb(SysTemplat.class, "财务模板").setAutoIncrement().addActIUDL().addActEdit()
	    .addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
			PKEY(TB.crtIntPkey()),
			TYPE(TB.crt(Sys.OTemplateType.GL)),
			CODE(SYS.CODE__40,"代码"), //代码相同但年份不同，在年初结转时将自动切换为新科目模板！
			YEAR(SYS.YEAR,"启用年份"),
			NAME(SYS.NAME__100,"模板名称"),
			MNG_CELL(SYS.CELL, "管理单元", true),
			ENABLED(TB.crt(SYS.ENABLED)),
			REM(SYS.REM__200_NULL),
			ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
			// 索引
			public static final Index IDX_CODE_YEAR = TB.addIndex("codeYear", true,CODE,YEAR, TYPE);
			private Fld _fld;
			private T(IEnumFld fld,boolean... isnull) { this(fld,null,isnull); } 
			private T(IEnumFld fld, String name,boolean... null1) {
				_fld=TB.add(fld,this,name,null1);}
			private T(IEnumFld fld, String name,int strLen) {
				_fld=TB.add(fld,this,name,strLen);}
			private T(Fld fld) {_fld=TB.add(fld, this); }
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
		@Override
		public String getExtName() {
		  return getName();
		}
		//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Byte _type;	// 模板类型 <OTemplateType>  BYTE
	// GL:1,财务
	// PUR:3,采购
  private String _code;	// 代码  STR(40)
  private Short _year;	// 启用年份  SHORT
  private String _name;	// 模板名称  STR(100)
  private Integer _mngCell;	// 管理单元 <表主键:SysCell>  INT<null>
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysTemplat init(){
		super.init();
    _type=OTemplateType.DEFAULT.getLine().getKey();	// 模板类型 <OTemplateType>  BYTE
    _code=null;	// 代码  STR(40)
    _year=0;	// 启用年份  SHORT
    _name=null;	// 模板名称  STR(100)
    _mngCell=null;	// 管理单元 <表主键:SysCell>  INT
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysTemplat loadUniqueCodeYear(boolean lockFlag,String code,Short year,Byte type) {
    return (SysTemplat)loadUnique(T.IDX_CODE_YEAR,lockFlag,code,year,type);
  }
  public static SysTemplat chkUniqueCodeYear(boolean lockFlag,String code,Short year,Byte type) {
    return (SysTemplat)chkUnique(T.IDX_CODE_YEAR,lockFlag,code,year,type);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OTemplateType gtType(){
    return (OTemplateType)(OTemplateType.GL.getLine().get(_type));
  }
  public void stType(OTemplateType type){
    _type=type.getLine().getKey();
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Short getYear(){
    return _year;
  }
  public void setYear(Short year){
    _year=year;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Integer getMngCell(){
    return _mngCell;
  }
  public void setMngCell(Integer mngCell){
    _mngCell=mngCell;
  }
  public SysCell gtMngCell(){
    if(getMngCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getMngCell());
  }
  public void stMngCell(SysCell mngCell){
    if(mngCell==null)
      setMngCell(null);
    else
      setMngCell(mngCell.getPkey());
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
