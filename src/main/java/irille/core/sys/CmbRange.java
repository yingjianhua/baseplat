package irille.core.sys;

import irille.core.sys.Sys.ORangeType;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOptObj;
import irille.pub.tb.Tb;
import irille.pub.tb.TbCmb;

/**
 * 组合字段 - 可视范围
 * @author whx
 * @version 创建时间：2014年11月27日 下午3:34:37
 */
public class CmbRange extends BeanInt<CmbRange> {
	public static final Tb TB = new TbCmb(CmbRange.class, "可用范围");

	public enum T implements IEnumFld {//@formatter:off
		RANGE(TB.crtOptAndKey("rangeObj", "可用对象",Sys.ORangeType.GRP)),
		CELL(SYS.CELL, "管理核算单元"),
		
//		可视范围：集团、上下级机构、及下级机构、本机构、及上级机构、上下级核算单元、及下级核算单元、及上级核算单元、核算单元
//		可视对象：为空表示集团，内容为机构或核算单元
//		管理核算单元
		
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		RANGE_TYPE(TB.get("rangeType")),	//可用范围
		RANGE_PKEY(TB.get("rangePkey")),	//可用对象主键值
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
		private T(Fld fld) {_fld=TB.add(fld,this); }
		public Fld getFld(){return _fld;}
	}		
	static { //在此可以加一些对FLD进行特殊设定的代码
		T.RANGE.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	public static Fld fldFlds() {
		return Tb.crtCmbFlds(TB);
	}
	public static Fld fldCmb(String code,String name) {
		return TB.crtCmb(code, name, TB);
	}
	//@formatter:on


	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
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
  private Long _rangePkey;	// 可视对象主键值  LONG<null>
  private Integer _cell;	// 管理核算单元 <表主键:SysCell>  INT

	@Override
  public CmbRange init(){
		super.init();
    _rangeType=ORangeType.DEFAULT.getLine().getKey();	// 可视范围 <ORangeType>  BYTE
    _rangePkey=(long)0;	// 可视对象主键值  LONG
    _cell=null;	// 管理核算单元 <表主键:SysCell>  INT
    return this;
  }

  //方法----------------------------------------------
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
  public Long getRangePkey(){
    return _rangePkey;
  }
  public void setRangePkey(Long rangePkey){
    _rangePkey=rangePkey;
  }
  //外部主键对象: BeanLong
  public Bean gtRangePkey(){
    return (Bean)gtLongTbObj(getRangePkey());
  }
  public void stRangePkey(Bean rangePkey){
      setRangePkey(rangePkey.gtLongPkey());
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

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
