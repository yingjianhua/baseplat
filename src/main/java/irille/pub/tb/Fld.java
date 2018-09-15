//Created on 2005-9-17
package irille.pub.tb;

import irille.pub.ClassTools;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.Infs.IFld;
import irille.pub.tb.Tb.IIndexFld;
import irille.pub.tb.Tb.IndexDescending;
import irille.pub.valid.VBase;
import irille.pub.view.FmtBase;
import irille.pub.view.VFld;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Title: 数据类型<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class Fld<T extends Fld> implements IIndexFld, IFld,IPubVars {
	private static final Log LOG = new Log(Fld.class);

	public static final String PKEY="pkey";
	private boolean _locked = false;
	
	public abstract Object getResult(ResultSet rs, String columnName) throws SQLException;
	
	public enum OOutType implements IEnumOpt {//@formatter:off
		COMBO(1,"选项","COMBO"),
		MILLION(11,"万元显示","0.0001"), 
		MILLION_H(12,"亿元显示","0.00000001"),
		PERCENT(13,"百分比显示","100"), 
		PERCENT_H(14,"千分比显示","1000"),
		JESTD(15,"金额千分位显示","JESTD"),
		JE(16,"金额","JE"),
		;
		public static final String NAME="字段输出类型";
		private EnumLine _line;
		private VFld _vfld;
		private String _text;
		private OOutType(int key, String name,String text) {
			_line=new EnumLine(this,key,name); _text=text;}
		public EnumLine getLine(){return _line;	}
		@Override
		public String toString() {	return _text;}
	}		//@formatter:on

	public enum KIND { // 字段数据种
		SIMPLE, // 简单
		OPT, // 选项
		LINES, // 明细
		UNION, // 虚拟组合字段
		DIME, // 虚拟数组字段，会自动增加真实的每个字段
		ENUM,
		CAL  //计算字段
		// 枚举
	}

	private String _code; // 字段代码
	private String _codeSqlField;
	private Method _getMethod = null; // 用于快速取get方法,提高性能
	private Method _getMethodOBJ = null; // 用于快速取get方法,提高性能
	private Method _setMethod = null; // 用于快速取set方法,提高性能
	private String _name; // 字段名
	private boolean _null = false; // 空值标志
	private TbBase _tb; //
	private String _help = null;
	private VBase[] _valids = null; // 校验器
	private FmtBase _fmt = null;
	private boolean _raw = false; // oracle-raw-long类型
	private OOutType _type = null; // 用于产生不同类型的视图对象
	private Object _defaultValue = null; // 字段的初始值代码表示，用于产生源代码
	private boolean _autoCreate=false;   //是否为自动建立的字段, 如数组的各字段
	private VFld _vFld=null;

	public Fld(String code, String name) {
		super();
		_code = code;
		_name = name;
		_codeSqlField = Str.tranFieldToLineLower(code); // 数据库中的字段，即将大写转为“_”
	}

	//@formatter:off
  //   JavaType					SqlType					TypeName
	//-----------------------------------------------------
	//	Byte.class				Types.TINYINT		BYTE
	//	Byte.class				Types.TINYINT		BOOLEAN
	//	Short.class				Types.SMALLINT	SHORT
	//	Integer.class			Types.INTEGER		INT
	//	Long.class				Types.BIGINT		LONG
	//	BigDecimal.class	Types.DECIMAL		DEC
	//	String.class			Types.VARCHAR		STR
	//	Date.class				Types.DATE			DATE
	//	Date.class				Types.TIME			TIME	
	// @formatter:on

	/**
	 * java的数据库
	 */
	public abstract Class getJavaType();

	/**
	 * SQL的类型，如Types.VARCHAR
	 * 
	 * @return
	 */
	public abstract int getSqlType();

	/**
	 * 类型名，如：LONG，INT
	 * 
	 * @return
	 */
	public abstract String getTypeName();

	/**
	 * 显示宽度，一般为getLen,另考虑小数点及选项等情况
	 * 
	 * @return 英文（半角）字符数
	 */
	public abstract short getWidth();

	/**
	 * 根据当前对象，建立默认的VFld对象
	 * 
	 * @return 返回null表示没有对应的默认VFld对象，或不可用
	 */
	public abstract VFld crtVFld();

	/**
	 * 转换成相应的数据类型
	 * 
	 * @param value
	 * @return
	 */
	public abstract Object tran(String value);

	public final boolean isNull() {
		return _null;
	}

	public final T setNull() {
		return setNull(true);
	}

	public final T setNull(boolean null1) {
		assertUnlocked();
		_null = null1;
		return (T) this;
	}

	public final boolean isRaw() {
		return _raw;
	}

	public final T setRaw() {
		assertUnlocked();
		_raw = true;
		return (T) this;
	}

	public final T setRaw(boolean raw) {
		assertUnlocked();
		_raw = raw;
		return (T) this;
	}

	public KIND getKind() {
		return KIND.SIMPLE;
	}

	@Override
	public boolean isIndexAscending() {
		return true;
	}

	/**
	 * 是否为选项
	 * 
	 * @return
	 */
	public boolean isOpt() {
		return getKind() == KIND.OPT;
	}

	public boolean isSimple() {
		return getKind() == KIND.SIMPLE;

	}

	public boolean isLines() {
		return getKind() == KIND.LINES;
	}

	/**
	 * 是否为数据库的真实字段
	 * 
	 * @return
	 */
	public final boolean isDatabaseField() {
		return getKind() == KIND.SIMPLE || getKind() == KIND.OPT;
	}

	/**
	 * 外部程序要复制新对象要调用copyNew方法
	 * 
	 * @param tb
	 * @param code
	 * @param name
	 * @return
	 */
	public abstract T copyNew(String code, String name);

	/**
	 * 外部程序要复制新对象要调用copyNew方法
	 * 
	 * @param tb
	 * @return
	 */
	public T copyNew() {
		return copyNew(getCode(), getName());
	}

	/**
	 * 被了类的New方法调用
	 * 
	 * @param newObj
	 * @return
	 */
	protected T copy(Fld newObj) {
		newObj._valids = _valids;
		newObj._null = _null;
		newObj._fmt = _fmt;
		newObj._help = _help;
		newObj._type = _type;
		newObj._defaultValue = _defaultValue;
		return (T) newObj;
	}

	/**
	 * 检查值是否有效
	 * 
	 * @param value
	 *          要测的值
	 * @return
	 */
	public boolean isOk(Object value) {
		// XXX
		return true;
	}

	/**
	 * 校验字段的有效性
	 * 
	 * @param value
	 *          值
	 */
	public void assertOk(Object value) {
		if (isOk(value))
			return;
		// XXX 出错！！！

	}

	/**
	 * 返回数据类型，用于打印实体变量定义的说明用 格式如：DECIMAL(12,2)
	 * 
	 * @return 数据类型与长度的文本
	 */
	public String outTypeAndLen() {
		String s = " " + getTypeName();
		if (getLen() > 0) {
			s = s + "(" + getLen();
			if (getScale() > 0)
				s = s + "," + getScale();
			s = s + ")";
		}
		return s;
	}

	public TbBase getTb() {
		return _tb;
	}

	public void assertOpt() {
		if (isOpt())
			return;
		throw LOG.err("notIsOpt", "类[{0}]不是选项，不能进行此操作!", getClass().getName());
	}

	/**
	 * 是否为其它表的主键
	 * 
	 * @return
	 */
	public boolean isOutKey() {
		return false;
	}

	public String getCode() {
		return _code;
	}

	public String getName() {
		return _name;
	}

	public int getLen() {
		return 0;
	}

	public int getScale() {
		return 0;
	}

	public T setValids(VBase... valids) {
		assertUnlocked();
		_valids = valids;
		return (T) this;
	}

	public VBase[] getValids() {
		return _valids;
	}

	/**
	 * 取SQL字段
	 * 
	 * @return
	 */
	public String getCodeSqlField() {
		return _codeSqlField;
	}

	/**
	 * 取选项
	 * 
	 * @return
	 */
	public OptBase getOpt() {
		return null;
	}

	/**
	 * 确认值在列表中
	 * 
	 * @param value
	 * @param lineOrValues
	 */
	public void assertIn(Object value, Object... lineOrValues) {
		assertOpt();
		getOpt().assertIn(this.getName(), value, lineOrValues);
	}

	/**
	 * 判断值在列表中
	 * 
	 * @param value
	 * @param lineOrValues
	 */
	public boolean isIn(Object value, Object... lineOrValues) {
		assertOpt();
		return getOpt().isIn(value, lineOrValues);
	}

	/**
	 * 确认值不在列表中
	 * 
	 * @param value
	 * @param lineOrValues
	 */
	public boolean isNotIn(Object value, Object... lineOrValues) {
		return !isIn(value, lineOrValues);
	}

	/**
	 * 判断值不在列表中
	 * 
	 * @param value
	 * @param lineOrValues
	 */
	public void assertNotIn(Object value, Object... lineOrValues) {
		assertOpt();
		getOpt().assertNotIn(this.getName(), value, lineOrValues);
	}

	/**
	 * 检查字段的空值标志
	 * 
	 * @param value
	 */
	public final void validNull(Object value) {
		if (isNull() || value != null)
			return;
		throw LOG.err("valIsNull", "字段[{0}]不可以为空!", getName());
	}

	/**
	 * 输出字段在Bean中变量定义的源代码
	 * 
	 * @return
	 */
	public String outSrcVarDefine() {
		String s = "  private " + Str.getClazzLastCode(getJavaType()) + " _"
				+ getCode()+ ";	// " + getName()
				+ " " + outTypeAndLen();
		if (isNull())
			return s + "<null>" + LN;
		return s + LN;
	}

	/**
	 * 输出字段在Bean中变量初始化的源代码
	 * 
	 * @return
	 */
	public String outSrcInit() {
		if(getCode().equals("pkey"))
			return "";
		String s = "    _"
				+ getCode() + "=" + getDefaultValueSourceCode() + ";	// " + getName()
				+ " " + outTypeAndLen();
		return s+LN;
	}

	
	/**
	 * 返回返回值的数据类型
	 * 
	 * @return
	 */
	public String getReturnTypeShort() {
		return Str.getClazzLastCode(getJavaType());
	}

	/**
	 * 返回首字母大写的code值，用于get与set方法
	 * 
	 * @return
	 */
	public String getCodeFirstUpper() {
		return Str.tranFirstUpper(getCode());
	}

	/**
	 * 输出get与set方法的源代码
	 * 
	 * @return
	 */
	public String outSrcMethod() {
		StringBuilder buf = new StringBuilder();
		String returnType = getReturnTypeShort();
		String firstUpper = getCodeFirstUpper();
		// 输出get方法
		buf.append("  public " + returnType + " get" + firstUpper + "(){" + LN);
		buf.append("    return _" + getCode() + ";" + LN);
		buf.append("  }" + LN);
		// 输出set方法
		buf.append("  public void set" + firstUpper + "(" + returnType + " "
				+ getCode() + "){" + LN);
		buf.append("    _" + getCode() + "=" + getCode() + ";" + LN);
		buf.append("  }" + LN);
		return buf.toString();
	}

	public Method getGetMethod() {
		if (_getMethod == null)
			_getMethod = ClassTools.getMethod(getTb().getClazz(),
					"get" + Str.tranFirstUpper(_code));
		return _getMethod;
	}

	public Method getGetMethodOBJ() {
		if (_getMethodOBJ == null)
			_getMethodOBJ = ClassTools.getMethod(getTb().getClazz(),
					"gt" + Str.tranFirstUpper(_code));
		return _getMethodOBJ;
	}

	public Method getSetMethod() {
		if (_setMethod == null)
			_setMethod = ClassTools.getMethod(getTb().getClazz(),
					"set" + Str.tranFirstUpper(_code), getJavaType());
		return _setMethod;
	}

	/**
	 * 返回索引降序的对象
	 * 
	 * @return
	 */
	public IIndexFld indexDesc() {
		return new IndexDescending(this);
	}

	public Fld setHelp(String help) {
		assertUnlocked();
		_help = help;
		return this;
	}

	public String getHelp() {
		return _help;
	}

	/**
	 * 同表同字段才相等
	 */
	@Override
	public boolean equals(Object arg0) {
		Fld obj = (Fld) arg0;
		return obj._code.equals(_code) && _tb == obj._tb;
	}

	/**
	 * 检查数据的空值标志是否有效; 检查数据范围; 检查所有VBase的校验器是否有效 不同的子类需要重构此方法并添加一些规则
	 * 
	 * @param obj
	 */
	public void valid(Object obj) {
		if ((!_null) && obj == null)
			throw LOG.err("Cannot null", "{0}不可以为空", getName());
		if (obj == null || !isOpt())
			return;
		getOpt().assertValid(this, null); // 检查选项的有效性
		if (_valids == null)
			return;
		for (VBase v : _valids) {
			v.valid(this, obj);
		}
	}

	public FmtBase getFmt() {
		return _fmt;
	}

	public T setFmt(FmtBase fmt) {
		assertUnlocked();
		_fmt = fmt;
		return (T) this;
	}

	public OOutType getType() {
		return _type;
	}

	public Fld setType(OOutType type) {
		assertUnlocked();
		_type = type;
		return this;
	}

	public Fld setViewOutCombo() {
		assertUnlocked();
		setType(OOutType.COMBO);
		return this;
	}

	public Fld setViewDecJe() {
		assertUnlocked();
		setType(OOutType.JE);
		return this;
	}

	public Fld setViewDecJeStd() {
		assertUnlocked();
		setType(OOutType.JESTD);
		return this;
	}

	public Fld setViewDecPercent() {
		assertUnlocked();
		setType(OOutType.PERCENT);
		return this;
	}

	public Fld setViewDecPercentH() {
		assertUnlocked();
		setType(OOutType.PERCENT_H);
		return this;
	}

	public Fld setViewDecMillion() {
		assertUnlocked();
		setType(OOutType.MILLION);
		return this;
	}

	public Fld setViewDecMillionH() {
		assertUnlocked();
		setType(OOutType.MILLION_H);
		return this;
	}

	/**
	 * 输出对象的toString()方法内容
	 */
	public String getDefaultValueSourceCode() {
		if (_defaultValue == null || isNull())
			return "null";
		if (_defaultValue instanceof BigDecimal)
			return "new BigDecimal("+_defaultValue.toString()+")";
		return _defaultValue.toString();
	}
	
	public Object getDefaultValue(){
		return _defaultValue;
	}

	/**
	 * 置字段初始化时的值，用于产生到原代码的变量定义代码中，直接输出对象的toString()方法内容
	 */
	public Fld setDefaultValue(Object defaultValue) {
		assertUnlocked();
		_defaultValue = defaultValue;
		return this;
	}


	/**
	 * 确认未锁
	 * 
	 */
	public final void assertUnlocked() {
		if (_locked)
			throw LOG.err("assertUnlocked", "Fld对象[{0}]已加锁，不能变更其内容!", getTb().getCode()+"."+getCode()+":"+getName());
	}

	// 对字段属性加锁
	public Fld lock() {
		_locked = true;
		return this;
	}
	
	public boolean isLocked() {
		return _locked;
	}
	
	public Fld setTb(TbBase tb) {
		assertUnlocked();
		_tb=tb;
		return this;
	}

	/**
	 * @return the autoCreate
	 */
	public boolean isAutoCreate() {
		return _autoCreate;
	}

	/**
	 * @param autoCreate the autoCreate to set
	 */
	public Fld setAutoCreate() {
		assertUnlocked();
		_autoCreate = true;
		return this;
	}
	/**
	 * @param name the name to set
	 */
	public Fld setName(String name) {
		assertUnlocked();
		_name = name;
		return this;
	}
	
	public Fld setCode(String code) {
		assertUnlocked();
		_code = code;
		_codeSqlField = Str.tranFieldToLineLower(code); 
		return this;
	}

	/**
	 * @return the vFld
	 */
	public VFld getVFld() {
		if(_vFld==null)
			_vFld=crtVFld();
		return _vFld;
	}

	/**
	 * @param vFld the vFld to set
	 */
	public Fld setVFld(VFld vFld) {
//		assertUnlocked();
		_vFld = vFld;
		return this;
	}
	
	public static Fld[] fromIEnumFlds(IEnumFld... iflds) {
		Fld[] flds=new Fld[iflds.length];
		int i=0;
		for(IEnumFld ifld: iflds)
			flds[i++]=ifld.getFld();
		return flds;
	}
}
