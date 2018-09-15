package irille.core.sys;

import irille.core.sys.Sys.OBufType;
import irille.pub.ClassTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanInt;
import irille.pub.bean.PackageBase.OTableType;
import irille.pub.inf.IExtName;
import irille.pub.svr.ProvCtrl;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.tb.Infs.ITbOutKey;
import irille.pub.tb.Tb.Index;

import java.util.Hashtable;

/**
 * 类型（系统表、单据、凭证、NOTE），是否需要交接(系统表，不可交接)
 * @author whx
 * @version 创建时间：2014年8月27日 上午9:15:20
 */
public class SysTable extends BeanInt<SysTable> implements ITbOutKey, IExtName {
	private static final Log LOG = new Log(SysTable.class);
	public static final Tb TB = new Tb(SysTable.class, "数据表信息", "表").addActList();
	public static final int NUM_BASE = 100000; //附加表代码时，乘积基数10万

	public enum T implements IEnumFld {
		//@formatter:off
		PKEY(TB.crtIntPkey()),
		CODE(SYS.CODE__40),
		NAME(SYS.NAME__100),
		SHORT_NAME(SYS.NAME__40_NULL, "简称"),
		TYPE(TB.crt(Sys.OTableType.SYS)),
		BUF_TYPE(TB.crt(Sys.OBufType.NO)),
		SEQNUM(SYS.LONG, "当前最大序号"),
		ROW_VERSION(SYS.ROW_VERSION),
		//>>>以下是自动产生的源代码行--内嵌字段定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--内嵌字段定义--请保留此行用于识别<<<
		;
		//>>>以下是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别>>>
		//<<<以上是自动产生的源代码行--自动建立的索引定义--请保留此行用于识别<<<
		public static final Index IDX_CODE = TB.addIndexUnique("code", CODE);
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
		return fldOutKey(TB.getCodeNoPackage(), "表类型");
	}
	public static Fld fldOutKeyFrm() {
		return fldOutKey(TB.getCodeNoPackage(), "单据类型");
	}

	public static Fld fldOutKey(String code, String name) {
		return Tb.crtOutKey(TB, code, name);
	}
	private Class _clazz=null;
	private static Hashtable<Class, SysTable> _clazzHash = new Hashtable();
	private static Hashtable<Integer, SysTable> _idHash = new Hashtable();

	public Class gtClazz() {
		if(_clazz==null) 
			_clazz=ClassTools.getClass(getCode());
			return _clazz;
	}
	public static final SysTable gtTable(Class clazz) {
		SysTable tb=_clazzHash.get(clazz);
		if(tb==null)
			tb=addToHash(loadUniqueCode(false, clazz.getName()));
		return tb;
	}
	public static final SysTable gtTable(int id) {
		SysTable tb=_idHash.get(id);
		if(tb==null)
			tb=addToHash(get(SysTable.class, id));
		return tb;
	}
	public static final SysTable chkTable(Class clazz) {
		SysTable tb=_clazzHash.get(clazz);
		if(tb==null) {
			tb = chkUniqueCode(false, clazz.getName());
			if (tb != null)
				tb=addToHash(tb);
		}
		return tb;
	}
	public static final SysTable chkTable(int id) {
		SysTable tb=_idHash.get(id);
		if(tb==null) {
			tb = tb.chk(id);
			if (tb != null)
				tb=addToHash(tb);
		}
		return tb;
	}
	private static final SysTable addToHash(SysTable tb) {
		_clazzHash.put(tb.gtClazz(), tb);
		_idHash.put(tb.getPkey(), tb );
		return tb;
	}
	
	//@formatter:on

	//>>>以下是自动产生的源代码行--源代码--请保留此行用于识别>>>
  //实例变量定义-----------------------------------------
  private Integer _pkey;	// 编号  INT
  private String _code;	// 代码  STR(40)
  private String _name;	// 名称  STR(100)
  private String _shortName;	// 简称  STR(40)<null>
  private Byte _type;	// 表类型 <OTableType>  BYTE
	// SYS:1,系统表
	// FORM:2,单据
	// BILL:3,凭证
	// NOTE:4,便签
  private Byte _bufType;	// 缓冲类型 <OBufType>  BYTE
	// NO:0,不缓冲
	// RECORDS:1,指定记录缓冲
	// ALL:2,整表缓冲
  private Long _seqnum;	// 当前最大序号  LONG
  private Short _rowVersion;	// 版本  SHORT

	@Override
  public SysTable init(){
		super.init();
    _code=null;	// 代码  STR(40)
    _name=null;	// 名称  STR(100)
    _shortName=null;	// 简称  STR(40)
    _type=OTableType.DEFAULT.getLine().getKey();	// 表类型 <OTableType>  BYTE
    _bufType=OBufType.DEFAULT.getLine().getKey();	// 缓冲类型 <OBufType>  BYTE
    _seqnum=(long)0;	// 当前最大序号  LONG
    _rowVersion=0;	// 版本  SHORT
    return this;
  }

  //方法----------------------------------------------
  public static SysTable loadUniqueCode(boolean lockFlag,String code) {
    return (SysTable)loadUnique(T.IDX_CODE,lockFlag,code);
  }
  public static SysTable chkUniqueCode(boolean lockFlag,String code) {
    return (SysTable)chkUnique(T.IDX_CODE,lockFlag,code);
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
  public String getName(){
    return _name;
  }
  public void setName(String name){
    _name=name;
  }
  public String getShortName(){
    return _shortName;
  }
  public void setShortName(String shortName){
    _shortName=shortName;
  }
  public Byte getType(){
    return _type;
  }
  public void setType(Byte type){
    _type=type;
  }
  public OTableType gtType(){
    return (OTableType)(OTableType.SYS.getLine().get(_type));
  }
  public void stType(OTableType type){
    _type=type.getLine().getKey();
  }
  public Byte getBufType(){
    return _bufType;
  }
  public void setBufType(Byte bufType){
    _bufType=bufType;
  }
  public OBufType gtBufType(){
    return (OBufType)(OBufType.NO.getLine().get(_bufType));
  }
  public void stBufType(OBufType bufType){
    _bufType=bufType.getLine().getKey();
  }
  public Long getSeqnum(){
    return _seqnum;
  }
  public void setSeqnum(Long seqnum){
    _seqnum=seqnum;
  }
  public Short getRowVersion(){
    return _rowVersion;
  }
  public void setRowVersion(Short rowVersion){
    _rowVersion=rowVersion;
  }

	//<<<以上是自动产生的源代码行--源代码--请保留此行用于识别<<<
}
