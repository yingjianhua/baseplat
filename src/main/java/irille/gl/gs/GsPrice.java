package irille.gl.gs;

import irille.core.sys.CmbRange;
import irille.core.sys.SysCell;
import irille.core.sys.Sys.ORangeType;
import irille.pub.Log;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;

/**
 * 价格名称如：如零售价、银卡会员、金卡、钻石、金钻等，一级批发、二级批发，暂最多支持12种
 * @author whx
 * @version 创建时间：2014年11月28日 上午10:45:09
 */
public class GsPrice extends BeanInt<GsPrice> implements IExtName {
	public static final Log LOG = new Log(GsPrice.class);
	public static final Tb TB = new Tb(GsPrice.class, "定价名称").setAutoIncrement().addActIUDL();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		NAME(SYS.NAME__40, "名称"),
		NAME_PRICE(TB.crtDime(SYS.NAME__40_NULL, new int[] { 1,2,3,4,5,6,7,8,9,10,11,12}, "价格名称1", "价格名称2", "价格名称3", "价格名称4", "价格名称5"
				, "价格名称6", "价格名称7", "价格名称8", "价格名称9", "价格名称10", "价格名称11", "价格名称12")), 
		CMB_SYS_RANGE(CmbRange.fldFlds()),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		NAME_PRICE1(TB.get("namePrice1")),	//价格名称1
		NAME_PRICE2(TB.get("namePrice2")),	//价格名称2
		NAME_PRICE3(TB.get("namePrice3")),	//价格名称3
		NAME_PRICE4(TB.get("namePrice4")),	//价格名称4
		NAME_PRICE5(TB.get("namePrice5")),	//价格名称5
		NAME_PRICE6(TB.get("namePrice6")),	//价格名称6
		NAME_PRICE7(TB.get("namePrice7")),	//价格名称7
		NAME_PRICE8(TB.get("namePrice8")),	//价格名称8
		NAME_PRICE9(TB.get("namePrice9")),	//价格名称9
		NAME_PRICE10(TB.get("namePrice10")),	//价格名称10
		NAME_PRICE11(TB.get("namePrice11")),	//价格名称11
		NAME_PRICE12(TB.get("namePrice12")),	//价格名称12
		RANGE(TB.get("range")),	//可用对象
		RANGE_TYPE(TB.get("rangeType")),	//可视范围
		RANGE_PKEY(TB.get("rangePkey")),	//可用对象主键值
		CELL(TB.get("cell")),	//管理核算单元
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
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
	@Override
	public String getExtName() {
	  return getName();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}
	public static Fld fldOutKey(String code, String name) {
		return TB.crtOutKey(TB,code,name);
	}
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _name;	// 名称  STR(40)
  private String _namePrice1;	// 价格名称1  STR(40)<null>
  private String _namePrice2;	// 价格名称2  STR(40)<null>
  private String _namePrice3;	// 价格名称3  STR(40)<null>
  private String _namePrice4;	// 价格名称4  STR(40)<null>
  private String _namePrice5;	// 价格名称5  STR(40)<null>
  private String _namePrice6;	// 价格名称6  STR(40)<null>
  private String _namePrice7;	// 价格名称7  STR(40)<null>
  private String _namePrice8;	// 价格名称8  STR(40)<null>
  private String _namePrice9;	// 价格名称9  STR(40)<null>
  private String _namePrice10;	// 价格名称10  STR(40)<null>
  private String _namePrice11;	// 价格名称11  STR(40)<null>
  private String _namePrice12;	// 价格名称12  STR(40)<null>
  private Byte _rangeType;	// 可视范围 <ORangeType>  BYTE
	// GRP:1,集团级
	// ORG_ALL:11,上下级机构
	// ORG_DOWN:12,及下级机构
	// ORG_UP:13,及上级机构
	// ORG:14,本机构
	// CELL_ALL:21,上下级核算单元
	// CELL_DOWN:22,及下级核算单元
	// CELL_UP:23,及上级核算单元
	// CELL:24,本核算单元
  private Integer _rangePkey;	// 可用对象主键值  INT<null>
  private Integer _cell;	// 管理核算单元 <表主键:SysCell>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsPrice init(){
		super.init();
    _name=null;	// 名称  STR(40)
    _namePrice1=null;	// 价格名称1  STR(40)
    _namePrice2=null;	// 价格名称2  STR(40)
    _namePrice3=null;	// 价格名称3  STR(40)
    _namePrice4=null;	// 价格名称4  STR(40)
    _namePrice5=null;	// 价格名称5  STR(40)
    _namePrice6=null;	// 价格名称6  STR(40)
    _namePrice7=null;	// 价格名称7  STR(40)
    _namePrice8=null;	// 价格名称8  STR(40)
    _namePrice9=null;	// 价格名称9  STR(40)
    _namePrice10=null;	// 价格名称10  STR(40)
    _namePrice11=null;	// 价格名称11  STR(40)
    _namePrice12=null;	// 价格名称12  STR(40)
    _rangeType=ORangeType.DEFAULT.getLine().getKey();	// 可视范围 <ORangeType>  BYTE
    _rangePkey=null;	// 可用对象主键值  INT
    _cell=null;	// 管理核算单元 <表主键:SysCell>  INT
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  //数组对象: String
  public String gtNamePrice(int i){
    switch(i) {
    case 1:
    	return getNamePrice1();
    case 2:
    	return getNamePrice2();
    case 3:
    	return getNamePrice3();
    case 4:
    	return getNamePrice4();
    case 5:
    	return getNamePrice5();
    case 6:
    	return getNamePrice6();
    case 7:
    	return getNamePrice7();
    case 8:
    	return getNamePrice8();
    case 9:
    	return getNamePrice9();
    case 10:
    	return getNamePrice10();
    case 11:
    	return getNamePrice11();
    case 12:
    	return getNamePrice12();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stNamePrice( int i, String namePrice){
    switch(i) {
    case 1:
    	setNamePrice1(namePrice);
    	return;
    case 2:
    	setNamePrice2(namePrice);
    	return;
    case 3:
    	setNamePrice3(namePrice);
    	return;
    case 4:
    	setNamePrice4(namePrice);
    	return;
    case 5:
    	setNamePrice5(namePrice);
    	return;
    case 6:
    	setNamePrice6(namePrice);
    	return;
    case 7:
    	setNamePrice7(namePrice);
    	return;
    case 8:
    	setNamePrice8(namePrice);
    	return;
    case 9:
    	setNamePrice9(namePrice);
    	return;
    case 10:
    	setNamePrice10(namePrice);
    	return;
    case 11:
    	setNamePrice11(namePrice);
    	return;
    case 12:
    	setNamePrice12(namePrice);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public String getNamePrice1(){
    return _namePrice1;
  }
  public void setNamePrice1(String namePrice1){
    _namePrice1=namePrice1;
  }
  public String getNamePrice2(){
    return _namePrice2;
  }
  public void setNamePrice2(String namePrice2){
    _namePrice2=namePrice2;
  }
  public String getNamePrice3(){
    return _namePrice3;
  }
  public void setNamePrice3(String namePrice3){
    _namePrice3=namePrice3;
  }
  public String getNamePrice4(){
    return _namePrice4;
  }
  public void setNamePrice4(String namePrice4){
    _namePrice4=namePrice4;
  }
  public String getNamePrice5(){
    return _namePrice5;
  }
  public void setNamePrice5(String namePrice5){
    _namePrice5=namePrice5;
  }
  public String getNamePrice6(){
    return _namePrice6;
  }
  public void setNamePrice6(String namePrice6){
    _namePrice6=namePrice6;
  }
  public String getNamePrice7(){
    return _namePrice7;
  }
  public void setNamePrice7(String namePrice7){
    _namePrice7=namePrice7;
  }
  public String getNamePrice8(){
    return _namePrice8;
  }
  public void setNamePrice8(String namePrice8){
    _namePrice8=namePrice8;
  }
  public String getNamePrice9(){
    return _namePrice9;
  }
  public void setNamePrice9(String namePrice9){
    _namePrice9=namePrice9;
  }
  public String getNamePrice10(){
    return _namePrice10;
  }
  public void setNamePrice10(String namePrice10){
    _namePrice10=namePrice10;
  }
  public String getNamePrice11(){
    return _namePrice11;
  }
  public void setNamePrice11(String namePrice11){
    _namePrice11=namePrice11;
  }
  public String getNamePrice12(){
    return _namePrice12;
  }
  public void setNamePrice12(String namePrice12){
    _namePrice12=namePrice12;
  }
  //根据表类型选项字段及主键字段的值取对象
  public Bean gtRange(){
    IEnumOptObj<Class> opt=(IEnumOptObj)gtRangeType();
    if(opt.getObj()==null)
    	return null;
    return get(opt.getObj(),_rangePkey);
  }
  public Byte getRangeType(){
    return _rangeType;
  }
  public void setRangeType(Byte rangeType){
    _rangeType=rangeType;
  }
  public ORangeType gtRangeType(){
    return (ORangeType)(ORangeType.GRP.getLine().get(_rangeType));
  }
  public void stRangeType(ORangeType rangeType){
    _rangeType=rangeType.getLine().getKey();
  }
  public Integer getRangePkey(){
    return _rangePkey;
  }
  public void setRangePkey(Integer rangePkey){
    _rangePkey=rangePkey;
  }
  public Integer getCell(){
    return _cell;
  }
  public void setCell(Integer cell){
    _cell=cell;
  }
  public SysCell gtCell(){
    if(getCell()==null)
      return null;
    return (SysCell)get(SysCell.class,getCell());
  }
  public void stCell(SysCell cell){
    if(cell==null)
      setCell(null);
    else
      setCell(cell.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
