/**
 * 
 */
package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * @author surface1
 */
public class GsUom extends BeanInt<GsUom> implements IExtName {
	private static final Log LOG = new Log(GsUom.class);

	public static final Tb TB = new Tb(GsUom.class, "计量单位").setAutoIncrement();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		UOM_TYPE(GsUomType.fldOutKey()),
		NAME(SYS.NAME__100),
		SHORTKEY(SYS.SHORTKEY__NULL),
		RATE(SYS.QTY,"转换率"),
		ENABLED(SYS.ENABLED),
		REM(SYS.REM__200_NULL),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_TYPE_NAME = TB.addIndex("typeName",
				true,UOM_TYPE,NAME);
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
	  return getName();
	}
	public static Fld fldOutKey() {
		Fld fld = fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
		fld.setType(null);
		return fld;
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	//@formatter:on

	/**
	 * 转化计量单位的数量
	 * @param toUom 目标计量单位
	 * @param qty 数据
	 * @return
	 */
	public BigDecimal tranQty(GsUom toUom, BigDecimal qty) {
		if (this.equals(toUom))
			return qty;
		if (!getUomType().equals(toUom.getUomType())) //必须为同一类型
			throw LOG.err("uomtype", "{0}与{1}的类似不一致，不可进行数量转换", this.gtUomType().getName(), toUom.gtUomType().getName());
		return qty.multiply(getRate()).divide(toUom.getRate(), Sys.T.QTY.getFld().getScale(), BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 转换计量单位的价格
	 * @param toUom 目标计量单位
	 * @param price 价格
	 * @return
	 */
	public BigDecimal tranPrice(GsUom toUom, BigDecimal price) {
		if (this.equals(toUom))
			return price;
		if (!getUomType().equals(toUom.getUomType())) //必须为同一类型
			throw LOG.err("uomtype", "{0}与{1}的类似不一致，不可进行数量转换", this.gtUomType().getName(), toUom.gtUomType().getName());
		return price.multiply(toUom.getRate()).divide(getRate(), Sys.T.QTY.getFld().getScale(), BigDecimal.ROUND_HALF_UP);
	}

	/**
	 * 转换计量单位的金额
	 * @param toUom 目标计量单位
	 * @param amt 金额
	 * @return
	 */
	public BigDecimal tranAmt(GsUom toUom, BigDecimal amt) {
		if (this.equals(toUom))
			return amt;
		if (!getUomType().equals(toUom.getUomType())) //必须为同一类型
			throw LOG.err("uomtype", "{0}与{1}的类似不一致，不可进行数量转换", this.gtUomType().getName(), toUom.gtUomType().getName());
		return amt.multiply(toUom.getRate()).divide(getRate(), Sys.T.QTY.getFld().getScale(), BigDecimal.ROUND_HALF_UP);
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private Integer _uomType;	// 计量单位类型 <表主键:GsUomType>  INT
  private String _name;	// 名称  STR(100)
  private String _shortkey;	// 快捷键  STR(40)<null>
  private BigDecimal _rate;	// 转换率  DEC(14,4)
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private String _rem;	// 备注  STR(200)<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsUom init(){
		super.init();
    _uomType=null;	// 计量单位类型 <表主键:GsUomType>  INT
    _name=null;	// 名称  STR(100)
    _shortkey=null;	// 快捷键  STR(40)
    _rate=ZERO;	// 转换率  DEC(14,4)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _rem=null;	// 备注  STR(200)
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsUom loadUniqueTypeName(boolean lockFlag,Integer uomType,String name) {
    return (GsUom)loadUnique(T.IDX_TYPE_NAME,lockFlag,uomType,name);
  }
  public static GsUom chkUniqueTypeName(boolean lockFlag,Integer uomType,String name) {
    return (GsUom)chkUnique(T.IDX_TYPE_NAME,lockFlag,uomType,name);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public Integer getUomType(){
    return _uomType;
  }
  public void setUomType(Integer uomType){
    _uomType=uomType;
  }
  public GsUomType gtUomType(){
    if(getUomType()==null)
      return null;
    return (GsUomType)get(GsUomType.class,getUomType());
  }
  public void stUomType(GsUomType uomType){
    if(uomType==null)
      setUomType(null);
    else
      setUomType(uomType.getPkey());
  }
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getShortkey(){
    return _shortkey;
  }
  public void setShortkey(String shortkey){
    _shortkey=shortkey;
  }
  public BigDecimal getRate(){
    return _rate;
  }
  public void setRate(BigDecimal rate){
    _rate=rate;
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
