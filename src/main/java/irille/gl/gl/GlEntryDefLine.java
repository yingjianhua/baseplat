package irille.gl.gl;

import irille.core.sys.Sys.OYn;
import irille.gl.gl.Gl.OAccSource;
import irille.gl.gl.Gl.ODirect3;
import irille.pub.IPubVars;
import irille.pub.bean.BeanLong;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.valid.VNumber;

public class GlEntryDefLine extends BeanLong<GlEntryDefLine> implements IPubVars {
	public static final Tb TB = new Tb(GlEntryDefLine.class, "分录定义行").setAutoLocal();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),   //本表要注意顺序，按前台输入的顺序重新编号后存入
		LINE_ID(SYS.SHORT,"数据行下标"),//下标值从0开始，核算单元、票号、备注等取自此下标的内容
		ACC_SOURCE(TB.crt(Gl.OAccSource.DEFAULT)),
		//“@数字”取指定数据结构行中的科目，其它为具体的源科目代号或别名。
		SOURCE_ALIAS_OR_SUBJECT(SYS.STR__20_NULL,"源别名或科目号"), 
		//目标科目的别名。
		TARGET_ALIAS(SYS.STR__20_NULL,"目标别名"),	//当ACC_SOURCE为MAP时，此项不可以为空。
		DIRECT3(TB.crt(Gl.ODirect3.DR)),
		AMT_EXPR(SYS.STR__100,"金额表达式"),
		NEGATIVE_ABLE(SYS.NY,"金额可否为负数"),
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
		private T(Fld fld) {_fld=TB.add(fld, this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.LINE_ID.getFld().setHelp("下标值从0开始。").setValids(VNumber.PLUS_OR_ZERO);
		T.SOURCE_ALIAS_OR_SUBJECT._fld.setHelp("“@数字”取指定数据结构行中的科目，其它为具体的源科目代号或别名。");
		T.TARGET_ALIAS._fld.setHelp("目标科目的别名，当ACC_SOURCE为MAP时，此项不可以为空。。");
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
  private Short _lineId;	// 数据行下标  SHORT
  private Byte _accSource;	// 账号来源 <OAccSource>  BYTE
	// TARGET:2,指定目标科目取账户
	// MAP:3,根据源科目取关联科目
  private String _sourceAliasOrSubject;	// 源别名或科目号  STR(20)<null>
  private String _targetAlias;	// 目标别名  STR(20)<null>
  private Byte _direct3;	// 借贷标志 <ODirect3>  BYTE
	// DR:1,借方
	// CR:2,贷方
	// VALUE:3,取参数的借贷标志
  private String _amtExpr;	// 金额表达式  STR(100)
  private Byte _negativeAble;	// 金额可否为负数 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlEntryDefLine init(){
		super.init();
    _lineId=0;	// 数据行下标  SHORT
    _accSource=OAccSource.DEFAULT.getLine().getKey();	// 账号来源 <OAccSource>  BYTE
    _sourceAliasOrSubject=null;	// 源别名或科目号  STR(20)
    _targetAlias=null;	// 目标别名  STR(20)
    _direct3=ODirect3.DEFAULT.getLine().getKey();	// 借贷标志 <ODirect3>  BYTE
    _amtExpr=null;	// 金额表达式  STR(100)
    _negativeAble=OYn.DEFAULT.getLine().getKey();	// 金额可否为负数 <OYn>  BYTE
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public Long getPkey(){
    return _pkey;
  }
  public void setPkey(Long pkey){
    _pkey=pkey;
  }
  public Short getLineId(){
    return _lineId;
  }
  public void setLineId(Short lineId){
    _lineId=lineId;
  }
  public Byte getAccSource(){
    return _accSource;
  }
  public void setAccSource(Byte accSource){
    _accSource=accSource;
  }
  public OAccSource gtAccSource(){
    return (OAccSource)(OAccSource.TARGET.getLine().get(_accSource));
  }
  public void stAccSource(OAccSource accSource){
    _accSource=accSource.getLine().getKey();
  }
  public String getSourceAliasOrSubject(){
    return _sourceAliasOrSubject;
  }
  public void setSourceAliasOrSubject(String sourceAliasOrSubject){
    _sourceAliasOrSubject=sourceAliasOrSubject;
  }
  public String getTargetAlias(){
    return _targetAlias;
  }
  public void setTargetAlias(String targetAlias){
    _targetAlias=targetAlias;
  }
  public Byte getDirect3(){
    return _direct3;
  }
  public void setDirect3(Byte direct3){
    _direct3=direct3;
  }
  public ODirect3 gtDirect3(){
    return (ODirect3)(ODirect3.DR.getLine().get(_direct3));
  }
  public void stDirect3(ODirect3 direct3){
    _direct3=direct3.getLine().getKey();
  }
  public String getAmtExpr(){
    return _amtExpr;
  }
  public void setAmtExpr(String amtExpr){
    _amtExpr=amtExpr;
  }
  public Byte getNegativeAble(){
    return _negativeAble;
  }
  public void setNegativeAble(Byte negativeAble){
    _negativeAble=negativeAble;
  }
  public Boolean gtNegativeAble(){
    return byteToBoolean(_negativeAble);
  }
  public void stNegativeAble(Boolean negativeAble){
    _negativeAble=booleanToByte(negativeAble);
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
