package irille.gl.gs;

import irille.core.sys.Sys;
import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.core.sys.SysSeq;
import irille.core.sys.Sys.OEnabled;
import irille.core.sys.Sys.OYn;
import irille.gl.gl.GlGoods;
import irille.gl.gl.GlJournal;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlSubject;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.gs.Gs.OBatchType;
import irille.pub.Log;
import irille.pub.bean.BeanInt;
import irille.pub.bean.ISeq;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Tb.Index;

import java.math.BigDecimal;

/**
 * 界面上拷贝计量单位的重量体积、用户可修改
 * 出入库标识在界面上以多选择框的方式显示
 * 出库标识
 * 入库标识
 * @author whx
 */

public class GsGoods extends BeanInt<GsGoods> implements IExtName, ISeq {
	private static final Log LOG = new Log(GsGoods.class);
	public static final Tb TB = new Tb(GsGoods.class, "货物").setAutoIncrement().addActIUDL().addActEnabled();

	public enum T implements IEnumFld {//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__20),
		KIND(GsGoodsKind.fldOutKey()),
		CODE_OLD(SYS.CODE__40, "原代码", true),
		NAME(SYS.NAME__100),
		SHORTKEY(SYS.SHORTKEY__NULL),
		UOM(GsUom.fldOutKey()),
		SPEC(SYS.STR__200_NULL,"规格"),
		CUST(TB.crtDime(SYS.NAME__40_NULL, new int[] { 1,2,3,4,5}, "属性名称1", "属性名称2", "属性名称3", "属性名称4", "属性名称5")), 
		WEIGHT_RATE(SYS.RATE, "单位重量", true),
		VALUME_RATE(SYS.RATE, "单位体积", true),
		IN_FLAG(SYS.STR__10_NULL, "入库标识"),
		OUT_FLAG(SYS.STR__10_NULL, "出库标识"),
		DESCRIP(SYS.DESCRIP__200_NULL,"描述"),
		BAR_CODE(SYS.STR__20_NULL,"条型码"),
		ZERO_OUT_FLAG(SYS.NY,"可否零库存出库"),
		BATCH_TYPE(TB.crt(Gs.OBatchType.NO)),
		ECONOMIC_QTY(SYS.QTY,"经济批量"),
		PUR_LEAD_DAYS(SYS.SHORT,"采购提前天数"),
		PHOTO(SYS.PHOTO__NULL),
		ENABLED(SYS.ENABLED),
		ORG(SYS.ORG,true), //为null表示所有机构可用
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		CUST1(TB.get("cust1")),	//属性名称1
		CUST2(TB.get("cust2")),	//属性名称2
		CUST3(TB.get("cust3")),	//属性名称3
		CUST4(TB.get("cust4")),	//属性名称4
		CUST5(TB.get("cust5")),	//属性名称5
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		// 索引
		public static final Index IDX_CODE = TB.addIndex("code", true,CODE);
		public static final Index IDX_BAR_CODE = TB.addIndex("barCode", false,BAR_CODE); //非空的话不能重复！
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
		T.CODE._fld.setHelp("为空表示由系统自动产生");
		T.PKEY.getFld().getTb().lockAllFlds();//加锁所有字段,不可以修改
	}
	@Override
	public String getExtName() {
	  return getCode();
	}
	public static Fld fldOutKey() {
		return fldOutKey(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOutKey(String code, String name) {
		Fld fld = TB.crtOutKey(TB,code,name);
		fld.setType(null);
		return fld;
	}
	public static Fld fldOneToOne() {
		return fldOneToOne(TB.getCodeNoPackage(), TB.getShortName());
	}

	public static Fld fldOneToOne(String code, String name) {
		return Tb.crtOneToOne(TB, code, name);
	}
	
	//是否是劳务类型
	public boolean isWork() {
		return gtKind().gtType() == Gs.OType.WORK;
	}
	
	public void initSeq(SysSeq s) {
		s.setPkey(gtTable().getPkey());
		s.stOrgFlag(false);
		s.stType(Sys.OType.NONE);
	}
	
	@Override
	public String toString() {
	  return super.toString();
	}
	
	public String getErrMsg() {
		return getCode()+" : " + getName();
	}
	
	//@formatter:on

	/**
	 * 转化为缺省的计量单位的数量
	 * @param uom 计量单位
	 * @param qty 数量
	 * @return
	 */
	public BigDecimal toDefaultQty(GsUom uom, BigDecimal qty) {
		return uom.tranQty(gtUom(), qty);
	}

	/**
	 * 转化为缺省的计量单位的价格
	 * @param uom 计量单位
	 * @param price 价格
	 * @return
	 */
	public BigDecimal toDefaultPrice(GsUom uom, BigDecimal price) {
		return uom.tranPrice(gtUom(), price);
	}

	/**
	 * 根据核算单元获得当前货物的单价/默认计量单位
	 * @param cell 核算单元
	 * @return
	 */
	public BigDecimal getPriceOnDefaultUom(SysCell cell) {
		GlJournal journal = getJournal(cell, null);
		return getPriceOnDefaultUom(journal);
	}

	public BigDecimal getPriceOnDefaultUom(GlJournal journal) {
		GlGoods goods = getGlGoods(journal);
		return toDefaultPrice(goods.gtUom(), goods.getPrice());
	}

	public GlGoods getGlGoods(GlJournal journal) {
		GlGoods goods = GlGoods.chkUniqueJournalGoods(false, journal.getPkey(), getPkey());
		if (goods == null)
			throw LOG.err("noGoods", "存货[{0}]对应的存货帐信息不存在，请先手动新增并维护其价格!", getErrMsg());
		return goods;
	}

	public GlJournal getJournal(SysCell cell, String target) {
		GlSubject subject = null;
		if (target == null) {
			subject = GlSubjectMapDAO.getByAlias(gtKind().getSubjectAlias()).gtSubject();
		} else {
			//			System.out.println("target:"+target);
			//			System.out.println("templat:"+Idu.getOrg().gtTemplat());
			//			System.out.println("alia:"+gtKind().getSubjectAlias());
			subject = GlSubjectMapDAO.getByAlias(Idu.getOrg().gtTemplat(), gtKind().getSubjectAlias(), target).gtSubject();
		}
		if (subject == null)
			throw LOG.err("noSubject", "对应的科目字典不存在!");
		return GlJournalDAO.getAutoCreate(cell, subject, new AccObjs());
	}

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(20)
  private Integer _kind;	// 货物类别 <表主键:GsGoodsKind>  INT
  private String _codeOld;	// 原代码  STR(40)<null>
  private String _name;	// 名称  STR(100)
  private String _shortkey;	// 快捷键  STR(40)<null>
  private Integer _uom;	// 计量单位 <表主键:GsUom>  INT
  private String _spec;	// 规格  STR(200)<null>
  private String _cust1;	// 属性名称1  STR(40)<null>
  private String _cust2;	// 属性名称2  STR(40)<null>
  private String _cust3;	// 属性名称3  STR(40)<null>
  private String _cust4;	// 属性名称4  STR(40)<null>
  private String _cust5;	// 属性名称5  STR(40)<null>
  private BigDecimal _weightRate;	// 单位重量  DEC(8,4)<null>
  private BigDecimal _valumeRate;	// 单位体积  DEC(8,4)<null>
  private String _inFlag;	// 入库标识  STR(10)<null>
  private String _outFlag;	// 出库标识  STR(10)<null>
  private String _descrip;	// 描述  STR(200)<null>
  private String _barCode;	// 条型码  STR(20)<null>
  private Byte _zeroOutFlag;	// 可否零库存出库 <OYn>  BYTE
	// YES:1,是
	// NO:0,否
  private Byte _batchType;	// 批次管理类型 <OBatchType>  BYTE
	// NO:0,不用分批次管理
	// BATCH:1,分批次管理
	// EXP_DATE:2,有效(保质)期管理
	// SERIAL:3,一物一序列号管理
  private BigDecimal _economicQty;	// 经济批量  DEC(14,4)
  private Short _purLeadDays;	// 采购提前天数  SHORT
  private String _photo;	// 图片  STR(200)<null>
  private Byte _enabled;	// 启用标志 <OEnabled>  BYTE
	// TRUE:1,启用
	// FALSE:0,停用
  private Integer _org;	// 机构 <表主键:SysOrg>  INT<null>
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public GsGoods init(){
		super.init();
    _code=null;	// 代码  STR(20)
    _kind=null;	// 货物类别 <表主键:GsGoodsKind>  INT
    _codeOld=null;	// 原代码  STR(40)
    _name=null;	// 名称  STR(100)
    _shortkey=null;	// 快捷键  STR(40)
    _uom=null;	// 计量单位 <表主键:GsUom>  INT
    _spec=null;	// 规格  STR(200)
    _cust1=null;	// 属性名称1  STR(40)
    _cust2=null;	// 属性名称2  STR(40)
    _cust3=null;	// 属性名称3  STR(40)
    _cust4=null;	// 属性名称4  STR(40)
    _cust5=null;	// 属性名称5  STR(40)
    _weightRate=null;	// 单位重量  DEC(8,4)
    _valumeRate=null;	// 单位体积  DEC(8,4)
    _inFlag=null;	// 入库标识  STR(10)
    _outFlag=null;	// 出库标识  STR(10)
    _descrip=null;	// 描述  STR(200)
    _barCode=null;	// 条型码  STR(20)
    _zeroOutFlag=OYn.DEFAULT.getLine().getKey();	// 可否零库存出库 <OYn>  BYTE
    _batchType=OBatchType.DEFAULT.getLine().getKey();	// 批次管理类型 <OBatchType>  BYTE
    _economicQty=ZERO;	// 经济批量  DEC(14,4)
    _purLeadDays=0;	// 采购提前天数  SHORT
    _photo=null;	// 图片  STR(200)
    _enabled=OEnabled.DEFAULT.getLine().getKey();	// 启用标志 <OEnabled>  BYTE
    _org=null;	// 机构 <表主键:SysOrg>  INT
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static GsGoods loadUniqueCode(boolean lockFlag,String code) {
    return (GsGoods)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static GsGoods chkUniqueCode(boolean lockFlag,String code) {
    return (GsGoods)chkUnique(T.IDX_CODE,lockFlag,code);
  }
  public Integer getPkey(){
    return _pkey;
  }
  public void setPkey(Integer pkey){
    _pkey=pkey;
  }
  public String getCode(){
    return _code;
  }
  public void setCode(String code){
    _code=code;
  }
  public Integer getKind(){
    return _kind;
  }
  public void setKind(Integer kind){
    _kind=kind;
  }
  public GsGoodsKind gtKind(){
    if(getKind()==null)
      return null;
    return (GsGoodsKind)get(GsGoodsKind.class,getKind());
  }
  public void stKind(GsGoodsKind kind){
    if(kind==null)
      setKind(null);
    else
      setKind(kind.getPkey());
  }
  public String getCodeOld(){
    return _codeOld;
  }
  public void setCodeOld(String codeOld){
    _codeOld=codeOld;
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
  public Integer getUom(){
    return _uom;
  }
  public void setUom(Integer uom){
    _uom=uom;
  }
  public GsUom gtUom(){
    if(getUom()==null)
      return null;
    return (GsUom)get(GsUom.class,getUom());
  }
  public void stUom(GsUom uom){
    if(uom==null)
      setUom(null);
    else
      setUom(uom.getPkey());
  }
  public String getSpec(){
    return _spec;
  }
  public void setSpec(String spec){
    _spec=spec;
  }
  //数组对象: String
  public String gtCust(int i){
    switch(i) {
    case 1:
    	return getCust1();
    case 2:
    	return getCust2();
    case 3:
    	return getCust3();
    case 4:
    	return getCust4();
    case 5:
    	return getCust5();
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public void stCust( int i, String cust){
    switch(i) {
    case 1:
    	setCust1(cust);
    	return;
    case 2:
    	setCust2(cust);
    	return;
    case 3:
    	setCust3(cust);
    	return;
    case 4:
    	setCust4(cust);
    	return;
    case 5:
    	setCust5(cust);
    	return;
  	default: throw LOG.err("dimeErr","Dime numb[{0}] invalid.",i);
		}
  }
  public String getCust1(){
    return _cust1;
  }
  public void setCust1(String cust1){
    _cust1=cust1;
  }
  public String getCust2(){
    return _cust2;
  }
  public void setCust2(String cust2){
    _cust2=cust2;
  }
  public String getCust3(){
    return _cust3;
  }
  public void setCust3(String cust3){
    _cust3=cust3;
  }
  public String getCust4(){
    return _cust4;
  }
  public void setCust4(String cust4){
    _cust4=cust4;
  }
  public String getCust5(){
    return _cust5;
  }
  public void setCust5(String cust5){
    _cust5=cust5;
  }
  public BigDecimal getWeightRate(){
    return _weightRate;
  }
  public void setWeightRate(BigDecimal weightRate){
    _weightRate=weightRate;
  }
  public BigDecimal getValumeRate(){
    return _valumeRate;
  }
  public void setValumeRate(BigDecimal valumeRate){
    _valumeRate=valumeRate;
  }
  public String getInFlag(){
    return _inFlag;
  }
  public void setInFlag(String inFlag){
    _inFlag=inFlag;
  }
  public String getOutFlag(){
    return _outFlag;
  }
  public void setOutFlag(String outFlag){
    _outFlag=outFlag;
  }
  public String getDescrip(){
    return _descrip;
  }
  public void setDescrip(String descrip){
    _descrip=descrip;
  }
  public String getBarCode(){
    return _barCode;
  }
  public void setBarCode(String barCode){
    _barCode=barCode;
  }
  public Byte getZeroOutFlag(){
    return _zeroOutFlag;
  }
  public void setZeroOutFlag(Byte zeroOutFlag){
    _zeroOutFlag=zeroOutFlag;
  }
  public Boolean gtZeroOutFlag(){
    return byteToBoolean(_zeroOutFlag);
  }
  public void stZeroOutFlag(Boolean zeroOutFlag){
    _zeroOutFlag=booleanToByte(zeroOutFlag);
  }
  public Byte getBatchType(){
    return _batchType;
  }
  public void setBatchType(Byte batchType){
    _batchType=batchType;
  }
  public OBatchType gtBatchType(){
    return (OBatchType)(OBatchType.NO.getLine().get(_batchType));
  }
  public void stBatchType(OBatchType batchType){
    _batchType=batchType.getLine().getKey();
  }
  public BigDecimal getEconomicQty(){
    return _economicQty;
  }
  public void setEconomicQty(BigDecimal economicQty){
    _economicQty=economicQty;
  }
  public Short getPurLeadDays(){
    return _purLeadDays;
  }
  public void setPurLeadDays(Short purLeadDays){
    _purLeadDays=purLeadDays;
  }
  public String getPhoto(){
    return _photo;
  }
  public void setPhoto(String photo){
    _photo=photo;
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
  public Integer getOrg(){
    return _org;
  }
  public void setOrg(Integer org){
    _org=org;
  }
  public SysOrg gtOrg(){
    if(getOrg()==null)
      return null;
    return (SysOrg)get(SysOrg.class,getOrg());
  }
  public void stOrg(SysOrg org){
    if(org==null)
      setOrg(null);
    else
      setOrg(org.getPkey());
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
