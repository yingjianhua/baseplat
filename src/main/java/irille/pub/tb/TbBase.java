//Created on 2005-9-17
package irille.pub.tb;

import irille.pub.Cn;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.PackageBase.IOptCustEnum;
import irille.pub.view.VFlds;

import java.util.Hashtable;
import java.util.Vector;

/**
 * Title: 表<br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class TbBase<THIS extends TbBase> implements IPubVars{
	private static final Log LOG = new Log(TbBase.class);
	public static final String PKEY = Fld.PKEY;

	private Class _clazz; // bean类或View的类
	private String _code; // bean类名的最后一段
	private String _name; // 名称
	private String _shortName; // 简称

	protected Vector<Fld> _fields = new Vector<Fld>();
	protected Fld[] _flds = null;
	private transient Hashtable<String, Fld> _fieldHash = new Hashtable<String, Fld>();
	
	
	public enum OBeanType implements IEnumOpt {//@formatter:off
		TB(1,"数据库表"),VIEW(2,"数据库表"),CMB(3,"字段集");
		public static final String NAME="表类型";
		public static final OBeanType DEFAULT = TB; // 定义缺省值
		private EnumLine _line;
		private OBeanType(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	public TbBase(Class clazz, String code, String name, String shortName) {
		_clazz = clazz;
		_name = name;
		_code = code;
		_shortName = shortName;
	}

	public TbBase(Class clazz, String name) {
		this(clazz, Str.getClazzLastCode(clazz), name, name);
	}

	public final String getCode() {
		return _code;
	}

	public final Class getClazz() {
		return _clazz;
	}

	public final void lockAllFlds() {
		for (Fld fld : _fields)
			fld.lock();
	}

	/**
	 * @return the shortName
	 */
	public String getShortName() {
		return _shortName;
	}

	/**
	 * 取类实例变量名称，如: userName
	 * 
	 * @return 结果
	 */
	public final String[] getFldCodes() {
		String[] fieldCodes = new String[_fields.size()];
		for (int i = 0; i < _fields.size(); i++) {
			fieldCodes[i] = get(i).getCode();
		}
		return fieldCodes;
	}

	/**
	 * 取line, 从0开始
	 * 
	 * @return line
	 */
	public final Fld get(int id) {
		if (id >= _fields.size() || id < 0)
			throw LOG.err("idNotValid", "表[{0}]中字段序号范围为[0-{1}], 引用值[{2}]超范围!",
					getCode(), new Integer(_fields.size() - 1), new Integer(id));
		return _fields.elementAt(id);
	}

	/**
	 * 取字段
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final Fld get(String code) {
		Fld fld = _fieldHash.get(code);
		if (fld == null)
			throw LOG.err("fieldNotInTable", "表[{0}]中无字段[{1}]!", getCode(), code);
		return fld;
	}

	public final boolean chk(String code) {
		Fld fld = _fieldHash.get(code);
		if (fld == null)
			return false;
		return true;
	}

	/**
	 * 取字段Id号，从0开始
	 * 
	 * @param code
	 *          代码
	 * @return 结果
	 */
	public final int getId(String code) {
		for (int i = 0; i < _fields.size(); i++)
			if (_fields.get(i).getCode().equals(code))
				return i;
		throw LOG.err("fieldNotInTable", "表[{0}]中无字段[{1}]!", getCode(), code);
	}

	/**
	 * 取明细数量
	 * 
	 * @return 结果
	 */
	public final int size() {
		return _fields.size();
	}
	
	public final <FLD extends Fld> FLD add(FLD fld) {
		if (fld.isAutoCreate()) // 如是自动产生的代码,则不添加到表中!
			return fld;
		if (fld.getTb() != this) { // 如果不是本表的字段，则自动复制
			// TODO 后加的前台显示类型，暂以这种方式设置
			if (fld.getTb() != null && fld.isLocked())
				fld = (FLD) fld.copyNew(fld.getCode(), fld.getName());
		}
		if (_fieldHash.get(fld.getCode()) != null && isDatabaseTb()) // 是Bean对象允许重复
			throw LOG.err("fldError", "表[{0}]字段[{1}]重复!", getCode(), fld.getCode());
		_fieldHash.put(fld.getCode(), fld);
		if(fld.getCode().equals("pkey")) {
			_fields.add(0, fld);
		}
		else {
			_fields.addElement(fld);
		}
		fld.setTb(this);
		return fld;
	}
	
	public final <FLD extends Fld> FLD add(FLD fld, Enum enum1) {
		return add(fld, enum1, fld.getName());
	}
	
	public final <FLD extends Fld> FLD add(FLD fld, Enum enum1, String name, boolean... null1) {
		if (name==null)
			name=fld.getName();
		fld.setName(name);
		fld.setCode(getEnumFieldCode(enum1.name()));
		if (null1.length > 0)
			fld.setNull(null1[0]);
		return add(fld);
	}
	
	public final static <FLD extends Fld> FLD crt(IEnumFld outKeyFld) {
		return (FLD) outKeyFld.getFld().copyNew();
	}

	/**
	 * 创建数组
	 * 
	 * @param <FLD>
	 * @param baseFld
	 *          数组的无素数据类型
	 * @param numbs
	 *          下标数组
	 * @param names
	 *          名称数组
	 * @return
	 */
	public final static FldVDime crtDime(IEnumFld fld, int numbs[],
			String... names) {
		return crtDime(fld.getFld().getCode(), fld.getFld().getName(), fld, numbs,
				names);
	}
	public final static FldVDime crtDime(Fld fld, int numbs[],
			String... names) {
		return crtDime(fld.getCode(), fld.getName(), fld, numbs,
				names);
	}
	/**
	 * 计算字段
	 * @param baseFld 数据类型的参照字段
	 * @param exp 表达式，用于返回计算的值
	 * @param name 名称
	 * @return
	 */
	public final static FldVCal crtCal(IEnumFld baseFld, String exp,String name) {
		return new FldVCal(baseFld.getFld(),exp,baseFld.getFld().getCode(),name);
	}
	/**
	 * 计算字段
	 * @param baseFld 数据类型的参照字段
	 * @param exp 表达式，用于返回计算的值
	 * @return
	 */
	public final static FldVCal crtCal(IEnumFld baseFld, String exp) {
		return new FldVCal(baseFld.getFld(),exp,baseFld.getFld().getCode(),baseFld.getFld().getName());
	}

	@SuppressWarnings("unchecked")
	public final static FldVDime crtDime(String code, String name,
			IEnumFld baseFld, int numbs[], String... names) {
		return new FldVDime(code, name, baseFld.getFld(), numbs, names);
	}
	@SuppressWarnings("unchecked")
	public final static FldVDime crtDime(String code, String name,
			Fld fld, int numbs[], String... names) {
		return  new FldVDime(code, name, fld, numbs, names);
	}

	public final static FldVCmb crtCmb(String code, String name, TbBase tb) {
		return new FldVCmb(code, name, tb);
	}

	public final static FldVCmbFlds crtCmbFlds( Tb tb) {
		return  new FldVCmbFlds(Str.getClazzLastCode(tb.getClazz()), tb);
	}
	public final static FldVFlds crtFlds(String code, IEnumFld... flds) {
		return  new FldVFlds(code,flds);
	}
	public final static FldVFlds crtFlds(String code, Tb tb ,IEnumFld... withoutFlds) {
		return  new FldVFlds(code).add(tb, withoutFlds);
	}

	public final static FldVInf crtInf() {
		return new FldVInf();
	}

	//
	// public final <FLD extends Fld> FLD add(FLD fld, String fldName) {
	// return add(fld,fld.getCode(),fldName);
	// }
	public final static <FLD extends Fld> FLD crt(IEnumFld enumFld, String fldcode,
			String fldName) {
		return (FLD) crt(enumFld.getFld(), fldcode, fldName);
	}

	/**
	 * 
	 * @param <FLD>
	 * @param fld
	 * @param enum1
	 * @param name
	 *          为null表示取fld.getFld().getName()
	 * @param null1
	 * @return
	 */
	public final <FLD extends Fld> FLD add(IEnumFld fld, Enum enum1, String name,
			boolean[] null1) {
		if (name == null)
			name = fld.getFld().getName();
		return (FLD) add(crt(fld, getEnumFieldCode(enum1.name()), name).setNull(
				null1.length == 0 ? fld.getFld().isNull() : null1[0]));
	}

	
	public final <FLD extends Fld> FLD add(IEnumFld fld, Enum enum1, String name,
			int strlen) {
		if (!(fld.getFld() instanceof FldStr))
			throw LOG.err("NotInstanceofFldStr", "此方法的Fld对象类型[{0}]不是[{1}]类型", fld
					.getFld().getClass(), FldStr.class.getClass());
		Fld f = crt(fld, getEnumFieldCode(enum1.name()), name);
		((FldStr) f).setLen(strlen);
		return (FLD) add(f);
	}

	public final static <FLD extends Fld> FLD crt(FLD fld, String fldcode, String fldName) {
		return (FLD) fld.copyNew(fldcode, fldName);
	}

	public final <FLD extends Fld> FLD replace(FLD fld) {
		if (fld.getTb() != this)
			fld = (FLD) fld.copyNew(fld.getCode(), fld.getName());
		Fld oldfld = _fieldHash.get(fld.getCode());
		if (oldfld == null)
			throw LOG.err("replaceErr", "表[{0}]字段[{1}]不存在", getCode(), fld.getCode());
		int index = _fields.indexOf(oldfld);
		_fieldHash.put(fld.getCode(), fld);
		_fields.set(index, fld);
		return fld;
	}

	public final static FldByte crtByte(String code, String name) {
		return new FldByte(code, name);
	}

	public final static FldDate crtDate(String code, String name, boolean null0) {
		return new FldDate(code, name, null0);
	}

	public final static FldDate crtDate(String code, String name) {
		return crtDate(code, name, false);
	}

	public final static FldDec crtDec(String code, String name, int len, int scale) {
		return new FldDec(code, name, len, scale);
	}

	public final static FldInt crtInt(String code, String name) {
		return new FldInt(code, name);
	}

	public final static FldInt crtIntPkey() {
		return new FldInt(PKEY, "编号");
	}

	public final static FldLongTbObj crtLongTbObj(String code, String name) {
		return new FldLongTbObj(code, name);
	}
	public final static FldLongTbObj crtLongTbObj(String code, String name,Class returnType) {
		return new FldLongTbObj(code, name,returnType);
	}
	public final static FldFormAndCode crtFormAndCode(String code, String name) {
		return new FldFormAndCode(code, name);
	}
	
	// oracle的raw字段
	public final static FldChar crtRawPkey() {
		return (FldChar) new FldChar(PKEY, "代码", 32, false).setRaw();
	}

	public final static FldEnumByte crt(IEnumOpt enumObj, String code, String name) {
		return (FldEnumByte) new FldEnumByte(enumObj, code, name);
	}

	public final static FldEnumByte crt(IEnumOpt en) {
		return crt(en, en.getLine().getCodeEnum(), en.getLine().getNameEnum());
	}

	public final static FldOutKey crtOutKey(Class outTbClass, String code, String name) {
		return new FldOutKey(outTbClass, code, name);
	}
	public final static FldOutKey crtOutKey(Tb tb, String code, String name) {
		return new FldOutKey(tb.getClazz(), code, name);
	}


	public final static FldVOneToOne crtOneToOne(Class otherBeanClass, String code,
			String name) {
		return new FldVOneToOne(otherBeanClass,code, name);
	}
	public final static FldVOneToMany crtOneToMany(Class beanBaseClass, String code,
			String name) {
		return new FldVOneToMany(beanBaseClass,code, name);
	}
	public final static FldVOneToOne crtOneToOne(Tb tb, String code,
			String name) {
		return new FldVOneToOne(tb.getClazz(),code, name);
	}

	public final FldOutKey addOutKey(Class outTbClass, Enum enum1, String name,
			boolean[] null1) {
		return (FldOutKey) add(new FldOutKey(outTbClass,
				getEnumFieldCode(enum1.name()), name).setNull(null1.length == 0 ? false
				: null1[0]));
	}

	/**
	 * 取Enum对象名称的Field(首字大字)表示,如将SHORT_NAME__PERSON_20_NULL转换为ShortName
	 * 
	 * @param enum1
	 * @return
	 */
	public static final String getEnumFieldCode(String enumName) {
		int idx = enumName.indexOf("__");
		if (idx < 0)
			return Str.tranLineUpperToField(enumName);
		;
		return Str.tranLineUpperToField(enumName.substring(0, idx));
	}

	public final static FldVTbAndKey crtTbAndKey(String code, String name) {
		return new FldVTbAndKey(code, name);
	}

	public final static FldVOptAndKey crtOptAndKey(String code, String name,
			IEnumOptObj<Class> opt) {
		return new FldVOptAndKey(code, name, opt);
	}

	//
	// public final ZFldVFormOLD addForm(String code, String name, Fld tbCodeFld,
	// Fld tbPkeyFld) {
	// return (ZFldVFormOLD) add(new ZFldVFormOLD(this, code, name, tbCodeFld,
	// tbPkeyFld));
	// }
	//
	// public final ZFldVFormOLD addBill(String code, String name, Fld tbCodeFld,
	// Fld tbPkeyFld) {
	// return (ZFldVFormOLD) add(new ZFldVBillOLD(this, code, name, tbCodeFld,
	// tbPkeyFld));
	// }
	/**
	 * 此方法自动新建Pkey的Fld
	 */
	public final static FldLong crtLong(String code, String name) {
		return new FldLong(code, name);
	}

	public final static FldLong crtLongPkey() {
		return new FldLong("pkey", "编号");
	}

//	public final static FldOptByte crtOptByte(Opt opt, String code, String name) {
//		return new FldOptByte(opt, code, name);
//	}
//
//	public final static FldOptByte crtOptByte(Opt opt) {
//		return crtOptByte(opt, opt.getCode(), opt.getName());
//	}
//
//	public final static FldOptStr crtOptStr(Opt opt, String code, String name, int len) {
//		return new FldOptStr(opt, code, name, len);
//	}
//
//	public final static FldOptStr crtOptStr(Opt opt, int len) {
//		return crtOptStr(opt, opt.getCode(), opt.getName(), len);
//	}

	public final static FldOptCust crtOptCust(OptCust opt, int len) {
		return new FldOptCust(opt.getCode(), opt.getName(),opt.getCode(), len);
	}
	public final static FldOptCust crtOptCust(IOptCustEnum optEnum, int len) {
		return crtOptCust(optEnum.getOpt(),len);
	}

	public final static FldShort crtShort(String code, String name) {
		return new FldShort(code, name);
	}

	public final static FldChar crtChar(String code, String name, int len, boolean null1) {
		return new FldChar(code, name, len, null1);
	}

	public final static FldChar crtChar(String code, String name, int len) {
		return crtChar(code, name, len, false);
	}

	public final static FldStr crtStr(String code, String name, int len, boolean null1) {
		return new FldStr(code, name, len, null1);
	}

	public final static FldStr crtStr(String code, String name, int len) {
		return crtStr(code, name, len, false);
	}

	public final static FldStr crtStrPkey() {
		return new FldStr("pkey", "编号", 20, false);
	}
	public final static FldStr crtStrPkey(int len) {
		return new FldStr("pkey", "编号", len, false);
	}

	public final static FldText crtText(String code, String name, int len, boolean null1) {
		return new FldText(code, name, len, null1);
	}

	public final static FldText crtText(String code, String name, int len) {
		return crtText(code, name, len, false);
	}

	public final static FldTime crtTime(String code, String name, boolean null1) {
		return new FldTime(code, name, null1);
	}

	public final static FldTime crtTime(String code, String name) {
		return crtTime(code, name, false);
	}

	/**
	 * 增加明细表
	 * 
	 * @param lineTbFld
	 *          明细表的本表主键字段
	 * @param code
	 *          明细代码
	 * @param name
	 *          明细名称
	 * @param OrderByFlds
	 *          排序表达式的字段，升序可以直接用Fld对象,降序可以用Fld对象的方法indexDesc()
	 * @return
	 */
//	public static FldLines crtLines(Fld lineTbFld, String code, String name,
//			IIndexFld... OrderByFlds) {
//		return new FldLines(lineTbFld, code, name, OrderByFlds);
//	}
//	public static FldLines crtLines(IEnumFld lineTbFld, String code, String name,
//			IIndexFld... OrderByFlds) {
//		return new FldLines(lineTbFld.getFld(), code, name, OrderByFlds);
//	}
	public static FldLines crtLines(IEnumFld lineTbFld, String code, String name,
			IEnumFld OrderByFld) {
	return new FldLines(lineTbFld, code, name, OrderByFld);
	}
	public static FldLines crtLines(IEnumFld lineTbFld, Cn cn,
			IEnumFld OrderByFld) {
	return new FldLines(lineTbFld, cn.getCode(),cn.getName(), OrderByFld);
	}

	public int compareTo(Tb o) {
		return getCode().compareTo(o.getCode());
	}

	/**
	 * 对象相等与否比较
	 * 
	 * @param obj
	 *          对象
	 * @return 结果
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	// @Override
	public final boolean equals(Object obj) {
		if (!(obj instanceof Tb))
			return false;
		return ((Tb) obj).getCode().equals(getCode());
	}

	/**
	 * 取fields
	 * 
	 * @return fields
	 */
	public final Fld[] getFlds() {
		if (_flds == null || _flds.length != _fields.size()) {
			Fld[] flds = new Fld[_fields.size()];
			_fields.toArray(flds);
			_flds = flds;
		}
		return _flds;
	}

	public final String getName() {
		return _name;
	}
	
	public String getCodeNoPackage(){
		String code=getCode();
		for(int i=1;i<code.length();i++) {
			if(Character.isUpperCase(code.charAt(i))) {
				return Character.toLowerCase(code.charAt(i))+code.substring(i+1);
			}
		}
		return "";
	}
	public final void resetAllVFld() {
		for(Fld fld:_fields)
			fld.setVFld(fld.crtVFld());
	}
	
	public VFlds crtVFlds() {
		return new VFlds("bean").addAll(this);
	}
	
	
	public OBeanType getBeanType() {
		return OBeanType.CMB;
	}


	/**
	 * @return the databaseTb
	 */
	public boolean isDatabaseTb() {
		return getBeanType()==OBeanType.TB;
	}
}
