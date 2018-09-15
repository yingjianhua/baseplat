package irille.gl.gl;

import irille.gl.gl.Gl.OSymbolType;
import irille.gl.gl.Gl.OTableType;
import irille.gl.gl.Gl.OValueType;
import irille.pub.bean.BeanLong;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;

public class GlReport extends BeanLong<GlReport> implements IExtName{
	public static final Tb TB = new Tb(GlReport.class, "报表设置").setAutoIncrement().addActList()
			.addActOpt("listByTableType", "列表");
	
	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtLongPkey()),  //此表主键类似Form的主键进行编号
		CODE(SYS.CODE__20),
		KEY_VALUE(SYS.INT,"键",true),
		NAME(SYS.NAME__100,"名称"),
		PARENT(TB.crtOutKey(GlReport.class, "report", "上级").setNull()),
		TABLE_TYPE(TB.crt(OTableType.DEFAULT)),
		VALUE_TYPE(TB.crt(OValueType.DEFAULT)),
		SYMBOL_TYPE(TB.crt(OSymbolType.DEFAULT)),
		ORDER_ID(SYS.INT,"顺序号"),
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
	@Override
	public String getExtName() {
	  return _name;
	}
	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Long _pkey;	// 编号  LONG
  private String _code;	// 代码  STR(20)
  private Integer _keyValue;	// 键  INT<null>
  private String _name;	// 名称  STR(100)
  private Long _parent;	// 上级 <表主键:GlReport>  LONG<null>
  private Byte _tableType;	// 表类型 <OTableType>  BYTE
	// ZC:1,资产
	// FZ:2,负债
	// QY:3,所有者权益
	// lR:4,利润表
  private Byte _valueType;	// 值类型 <OValueType>  BYTE
	// YE:1,余额
	// XJ:2,小计
	// ZJ:3,总计
	// W:4,无
  private Byte _symbolType;	// 加减类型 <OSymbolType>  BYTE
	// ADD:1,加
	// SUB:2,减
  private Integer _orderId;	// 顺序号  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GlReport init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _keyValue=null;	// 键  INT
    _name=null;	// 名称  STR(100)
    _parent=null;	// 上级 <表主键:GlReport>  LONG
    _tableType=OTableType.DEFAULT.getLine().getKey();	// 表类型 <OTableType>  BYTE
    _valueType=OValueType.DEFAULT.getLine().getKey();	// 值类型 <OValueType>  BYTE
    _symbolType=OSymbolType.DEFAULT.getLine().getKey();	// 加减类型 <OSymbolType>  BYTE
    _orderId=0;	// 顺序号  INT
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
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Integer getKeyValue(){
    return _keyValue;
  }
  public void setKeyValue(Integer keyValue){
    _keyValue=keyValue;
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public Long getParent(){
    return _parent;
  }
  public void setParent(Long parent){
    _parent=parent;
  }
  public GlReport gtParent(){
    if(getParent()==null)
      return null;
    return (GlReport)get(GlReport.class,getParent());
  }
  public void stParent(GlReport parent){
    if(parent==null)
      setParent(null);
    else
      setParent(parent.getPkey());
  }
  public Byte getTableType(){
    return _tableType;
  }
  public void setTableType(Byte tableType){
    _tableType=tableType;
  }
  public OTableType gtTableType(){
    return (OTableType)(OTableType.ZC.getLine().get(_tableType));
  }
  public void stTableType(OTableType tableType){
    _tableType=tableType.getLine().getKey();
  }
  public Byte getValueType(){
    return _valueType;
  }
  public void setValueType(Byte valueType){
    _valueType=valueType;
  }
  public OValueType gtValueType(){
    return (OValueType)(OValueType.YE.getLine().get(_valueType));
  }
  public void stValueType(OValueType valueType){
    _valueType=valueType.getLine().getKey();
  }
  public Byte getSymbolType(){
    return _symbolType;
  }
  public void setSymbolType(Byte symbolType){
    _symbolType=symbolType;
  }
  public OSymbolType gtSymbolType(){
    return (OSymbolType)(OSymbolType.ADD.getLine().get(_symbolType));
  }
  public void stSymbolType(OSymbolType symbolType){
    _symbolType=symbolType.getLine().getKey();
  }
  public Integer getOrderId(){
    return _orderId;
  }
  public void setOrderId(Integer orderId){
    _orderId=orderId;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
