package irille.core.sys;

import irille.pub.bean.BeanInt;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

/**
 * 表示模板可以，给哪些单元使用
 * 如某核算单元，只能选择销售模板中的一种，不允许选择多个
 * @author whx
 * @version 创建时间：2014年8月25日 下午1:34:09
 */
public class SysTemplatCell extends BeanInt<SysTemplatCell> {
	public static final Tb TB = new Tb(SysTemplatCell.class, "模板可使用单元").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
			PKEY(TB.crtIntPkey()),
			TEMPLAT(SYS.TEMPLAT),
			CELL(SYS.CELL),
			ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
			// 索引
			public static final Index IDX_TEMPLAT_CELL = TB.addIndex("templatCell", true, T.TEMPLAT, T.CELL);
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
  private Integer _templat;	// 科目模板 <表主键:SysTemplat>  INT
  private Integer _cell;	// 核算单元 <表主键:SysCell>  INT
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysTemplatCell init(){
		super.init();
    _templat=null;	// 科目模板 <表主键:SysTemplat>  INT
    _cell=null;	// 核算单元 <表主键:SysCell>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysTemplatCell loadUniqueTemplatCell(boolean lockFlag,Integer templat,Integer cell) {
    return (SysTemplatCell)loadUnique(T.IDX_TEMPLAT_CELL,lockFlag,templat,cell);
  }
  public static SysTemplatCell chkUniqueTemplatCell(boolean lockFlag,Integer templat,Integer cell) {
    return (SysTemplatCell)chkUnique(T.IDX_TEMPLAT_CELL,lockFlag,templat,cell);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
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
