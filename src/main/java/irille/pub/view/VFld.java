package irille.pub.view;

import irille.core.sys.SysUser;
import irille.gl.gs.GsUom;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanForm;
import irille.pub.ext.Ext.IExtOut;
import irille.pub.ext.Ext.IExtVars;
import irille.pub.html.ExtDime;
import irille.pub.html.ExtFunCall;
import irille.pub.html.ExtList;
import irille.pub.tb.EnumLine;
import irille.pub.tb.Fld;
import irille.pub.tb.FldByte;
import irille.pub.tb.FldDec;
import irille.pub.tb.FldInt;
import irille.pub.tb.FldLong;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldShort;
import irille.pub.tb.FldTime;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.tb.OptBase;
import irille.pub.tb.TbBase;
import irille.pub.tb.Fld.OOutType;
import irille.pub.tb.Infs.IFld;
import irille.pub.valid.VBase;

import java.util.Vector;

public abstract class VFld<T extends VFld> implements IFld, IPubVars, IExtVars {
	public static final Log LOG = new Log(VFld.class);

	public enum OAlign implements IEnumOpt {//@formatter:off
		TOP_LEFT(11,"上左对齐"),TOP_CENTER(12,"上中"),TOP_RIGHT(13,"上右对齐"),
		MIDDLE_LEFT(21,"中左对齐"),MIDDLE_CENTER(22,"中中"),MIDDLE_RIGHT(23,"中右对齐"),
		BOTTOM_LEFT(31,"下左对齐"),BOTTOM_CENTER(32,"下中"),BOTTOM_RIGHT(33,"下右对齐");
		public static final String NAME="对齐";
		public static final OAlign DEFAULT = MIDDLE_LEFT; // 定义缺省值
		private EnumLine _line;
		private OAlign(int key, String name) {_line=new EnumLine(this,key,name);	}
		public EnumLine getLine(){return _line;	}
	}		//@formatter:on

	private boolean _null;
	private Object _defaultValue = null;
	private Fld _fld;
	private VBase[] _valids = null; // 校验器
	private Object _help = null;
	private String _code;
	private String _name;
	private String _listName;
	private FmtBase _fmt;
	private String _red; // 低于RED则显示红色
	private OOutType _type; // 特殊类型标识
	private OAlign _align = OAlign.MIDDLE_LEFT;
	private OAlign _alignTitle = OAlign.MIDDLE_LEFT;
	private String _xtype;
	private String _modelType;
	private String _beanCode = "bean.";
	private boolean _useNull = false;
	private float _widthList, _widthFld;
	private String _readOnly = null;
	private String _hidden = null;
	private String _expandCol = null;
	private ExtList _attrs = null; // 附加属性，可以通过getOtherAttr()取得对象后，自定义加入

	public VFld(Fld fld, String xtype, String modelType) {
		_fld = fld;
		_code = fld.getCode();
		_null = fld.isNull();
		_valids = fld.getValids();
		_name = fld.getName();
		_help = fld.getHelp();
		_type = fld.getType();
		_defaultValue = fld.getDefaultValue();
		_xtype = xtype;
		_modelType = modelType;
		_widthList = _widthFld = getWidthDefault();
		_listName = _name;
	}

	public Fld getFld() {
		return _fld;
	}

	public final T copyNew() {
		return copyNew(_fld);
	}

	public abstract T copyNew(Fld fld);

	// MODEL
	public abstract String extModel();

	// FORM控件
	public abstract String extForm(String lnt, boolean isComp);

	// 编辑UI-主界面控件
	public abstract String extEdit(String lnt);

	// 复合UI-表格编辑控件
	public abstract String extEditList(String lnt);

	// 主表信息
	public String extTextField(String lnt) {
		return "" +
		/**/"{" + lnt +
		/**/"xtype : 'textfield'," + lnt +
		/**/"name : 'bean." + getCode() + "'," + lnt +
		/**/"fieldLabel : '" + getName() + "'" + lnt +
		/**/"}";
	}

	// 单元格编辑器
	public String extListEditor() {
		return "";
	}

	// 表格单元格
	public String extList() {
		return extList(false, "");
	}

	// 带编辑器的单元格
	public String extListEdit(String lnt) {
		return extList(true, lnt);
	}

	public String extList(boolean isEdit, String lnt) {
		String renderer = "";
		if (Str.isEmpty(extRenderer()) == false)
			renderer = ", " + extRenderer();
		String align = "";
		if (Str.isEmpty(extAlign()) == false)
			align = ", " + extAlign();
		String edit = "";
		if (isEdit)
			edit = ", " + extEditList(lnt);
		return "{text : '" + getName() + "', width : " + extWidth() + ", dataIndex : 'bean." + getCode()
		    + "', sortable : true" + renderer + align + edit + "}";
	}

	public String extRenderer() {
		if (Str.isEmpty(getRed()) == false)
			return "renderer: mvc.Tools.redRenderer(" + getRed() + ")";
		return "";
	}

	public String extAlign() {
		return "";
	}

	// 搜索栏
	public String extSearch(String lnt) {
		String tmp = "" +
		/**/"{" + lnt +
		/**/"	xtype : 'label'," + lnt +
		/**/"	text : '" + getName() + "：'" + lnt +
		/**/"},{" + lnt +
		/**/"	xtype : 'textfield'," + lnt +
		/**/"	name : '" + getCode() + "'" + lnt +
		/**/"}";
		return tmp;
	}

	protected String extFormNull(String lnt, boolean isComp) {
		if (isNull() || isComp)
			return "";
		String tmp = "" +
		/**/"	afterLabelTextTpl : required," + lnt +
		/**/"	allowBlank : false," + lnt;
		return tmp;
	}

	protected String extFormEmpty(String lnt, boolean isComp) {
		if (isComp || Str.isEmpty(getFld().getHelp()))
			return "";
		return "	emptyText : '" + getFld().getHelp() + "'," + lnt;
	}

	public int extWidth() {
		return getWidthDefault();
	}

	// ------

	public void useNull(ExtList v) {
		if (isUseNull())
			v.add(USE_NULL, true);
	}

	// MODEL
	public void model(ExtList v) {
		v.add(NAME, getBeanName());
		v.add(MAPPING, getCode());
		v.add(TYPE, getModelType());
		useNull(v);
	}

	public void renderer(ExtList v) {
		if (Str.isEmpty(getRed()) == false)
			v.addExp(RENDERER, "mvc.Tools.redRenderer(" + getRed() + ")");
	}

	// 搜索栏
	public void search(ExtDime v) {
		searchName(v.AddList(null));
		searchCode(v.AddList(null));
		v.add("");
	}

	public void searchName(ExtList v) {
		v.add(XTYPE, "label").add(TEXT, getName() + "：");
	}

	public void searchCode(ExtList v) {
		v.add(XTYPE, getXtype()).add(NAME, getCode());
	}

	// FORM控件
	public void form(ExtList v, boolean isComp) {
		v.setTabs(false);
		v.add(XTYPE, getXtype()).add(NAME, getBeanName());
		if (getDefaultValue() != null && isComp == false) { //TODO 跟BEAN测试输出代码ZERO冲突
			if (getFld() instanceof FldDec && getDefaultValue().toString().equals("ZERO"))
				v.add(VALUE, 0);
			else if (getFld() instanceof FldLong || getFld() instanceof FldInt || getFld() instanceof FldShort
					|| getFld() instanceof FldByte)
				v.addExp(VALUE, getDefaultValue().toString());
			else
				v.add(VALUE, getDefaultValue().toString());
		}if (this instanceof VFldTime) { // 为兼容老的代码，用于比较所有JS代码一致的特殊处理！ whx 20141013
			v.add(FIELD_LABEL, getName());
			formNull(v, isComp);
			formEmpty(v, isComp);
		} else {
			formNull(v, isComp);
			formEmpty(v, isComp);
			v.add(FIELD_LABEL, getName());
		}
		addReadOnly(v);
		addHidden(v);
	}

	public void addReadOnly(ExtList v) {
		if (_readOnly == null)
			return;
		v.addExp(READ_ONLY, _readOnly);
	}

	public void addHidden(ExtList v) {
		if (_hidden == null)
			return;
		v.addExp(HIDDEN, _hidden);
	}

	// 编辑UI-主界面控件
	public void edit(ExtList v) {
		v.setTabs(false);
		v.add(XTYPE, getXtype()).add(FIELD_LABEL, getName());
		v.addExp(VALUE, "this.record.get('" + getBeanName() + "')");
	}

	// 复合UI-表格编辑控件
	// 调用本方法前，在节点前要加入程序行“editor: ”， 这是与其它方法的不同之处。
	public void editList(ExtList v) {
		v.add(XTYPE, getXtype());
	}

	// 主表信息
	public void textField(ExtList v) {
		v.setTabs(false);
		v.add(XTYPE, getXtype());
		v.add(NAME, getBeanName());
		v.add(FIELD_LABEL, getName());
		// return "" +
		// /**/"{" + lnt +
		// /**/"xtype : 'textfield'," + lnt +
		// /**/"name : 'bean." + getCode() + "'," + lnt +
		// /**/"fieldLabel : '" + getName() + "'" + lnt +
		// /**/"}";
	}

	// // 单元格编辑器
	// public String listEditor() {
	// return "";
	// }

	// 表格单元格
	public ExtList list(ExtList v) {
		return list(false, v);
	}

	// 带编辑器的单元格
	public ExtList listEdit(ExtList v) {
		return list(true, v);
	}

	public ExtList list(boolean isEdit, ExtList v) {
		v.setTabs(false);
		v.add(TEXT, getName()).add(WIDTH, (int) getWidthList());
		v.add(DATA_INDEX, getBeanName());
		v.add(SORTABLE, true);
		renderer(v);
		align(v);
		if (Str.isEmpty(getExpandCol()) == false)
			v.add(EXPANDCOL, getExpandCol().equals("true") ? true : false);
		if (Str.isEmpty(getHidden()) == false)
			v.add(HIDDEN, getHidden().equals("true") ? true : false);
		if (isEdit) {
			editList(v.AddList(EDITOR).setTabs(false));
		}
		return v;
	}

	public void align(ExtList v) {
		switch (getAlign()) {
		case MIDDLE_LEFT:
			return;
		case MIDDLE_RIGHT:
			v.add(ALIGN, "right");
			return;
		case MIDDLE_CENTER:
			v.add(ALIGN, "center");
			return;
		}
	}

	protected void formNull(ExtList v, boolean isComp) {
		if (isNull() || isComp)
			return;
		v.addExp(AFTER_LABEL_TEXT_TPL, "required").add(ALLOW_BLANK, false);

		// if (isNull() || isComp)
		// return "";
		// String tmp = "" +
		// /**/"	afterLabelTextTpl : required," + lnt +
		// /**/"	allowBlank : false," + lnt;
		// return tmp;
	}

	protected void formEmpty(ExtList v, boolean isComp) {
		if (isComp || Str.isEmpty(getFld().getHelp()))
			return;
		v.add(EMPTY_TEXT, getFld().getHelp());
	}

	//高级搜索控件（三个组件一起）
	public void winSearch(ExtList v, boolean isComp) {
		ExtList label = new ExtList(v);//label部分
		ExtList optcht = new ExtList(v);//比较器部分
		ExtList l = new ExtList(optcht);
		ExtList form = new ExtList(v);//字段控件部分

		label.add(XTYPE, "label").add(TEXT, getName());
		optcht.setCloseStr(null); //不输出“{}”
		l.add(NAME, "optcht_" + getCode());
		winSearchOptcht(l);
		optcht.add(new ExtFunCall(optcht, "mvc.Tools.crtComboBase", (false), l));
		winSearchForm(form, isComp);

		v.setCloseStr(null);
		v.setTabs(false);
		v.add(label);
		v.add(optcht);
		v.add(form);
	}

	//条件选项内部组件属性设置
	protected void winSearchOptcht(ExtList v) {
		v.add(WIDTH, 80);
	}

	protected void winSearchForm(ExtList v, boolean isComp) {
		v.setTabs(false);
		v.add(XTYPE, getXtype()).add(NAME, getCode());
		if (getDefaultValue() != null && isComp == false)
			v.add(VALUE, getDefaultValue().toString());
		if (this instanceof VFldTime) { // 为兼容老的代码，用于比较所有JS代码一致的特殊处理！ whx 20141013
			formNull(v, isComp);
			formEmpty(v, isComp);
		} else {
			formNull(v, isComp);
			formEmpty(v, isComp);
		}
	}

	/**
	 * @return the xtype
	 */
	public String getXtype() {
		return _xtype;
	}

	/**
	 * @return the modelType
	 */
	public String getModelType() {
		return _modelType;
	}

	public void setModeType(String type) {
		_modelType = type;
	}

	/**
	 * @param beanCode
	 *          the beanName to set
	 */
	public VFld setBeanCode(String beanCode) {
		_beanCode = beanCode;
		return this;
	}

	/**
	 * @return the beanName
	 */
	public String getBeanCode() {
		return _beanCode;
	}
	
	public String getBeanName() {
		if (_beanCode.equals("NULL."))
			return getCode();
		return getBeanCode()+getCode();
	}

	// 取长度
	public int getWidthDefault() {
		if (getFld() instanceof FldTime)
			return 140;
		if (getFld() instanceof FldOutKey)
			if (((FldOutKey)getFld()).getOutTbClazz().equals(SysUser.class) || 
					((FldOutKey)getFld()).getOutTbClazz().equals(GsUom.class))
			return 75;
		if (isOpt()) {
			if (getName().length() <= 4 && !(this instanceof VFldEnumByte))
				return 70;
		}
		if (getCode().toLowerCase().contains("formnum") || getName().equals("单据号"))
			return 135;
		else if (getCode().equals("enabled") || getCode().equals("direct"))
			return 75;
		else if (getCode().equals("status")) 
			return 60;
		return 100;
	}

	protected final T copy(VFld newObj) {
		newObj._null = _null;
		newObj._defaultValue = _defaultValue;
		newObj._fld = _fld;
		newObj._valids = _valids;
		newObj._help = _help;
		newObj._name = _name;
		newObj._fmt = _fmt;
		newObj._red = _red;
		newObj._type = _type;
		newObj._xtype = _xtype;
		newObj._modelType = _modelType;
		newObj._beanCode = _beanCode;
		newObj._useNull = _useNull;
		newObj._widthFld = _widthFld;
		newObj._widthList = _widthList;
		newObj._readOnly = _readOnly;
		newObj._hidden = _hidden;
		newObj._expandCol = _expandCol;
		newObj._code = _code;
		newObj._listName = _listName;
		newObj._align = _align;
		newObj._alignTitle = _alignTitle;
		if (_attrs == null)
			newObj._attrs = null;
		else {
			newObj._attrs = new ExtList(_attrs.getParent());
			for (IExtOut ext : (Vector<IExtOut>) _attrs.getNodes())
				newObj._attrs.add(ext);
		}
		return (T) newObj;
	}

	public Object getDefaultValue() {
		return _defaultValue;
	}

	public T setDefaultValue(Object defaultValue) {
		_defaultValue = defaultValue;
		return (T) this;
	}

	public boolean isNull() {
		return _null;
	}

	public T setNullFalse() {
		_null = false;
		return (T) this;
	}

	public T setNullTrue() {
		_null = true;
		return (T) this;
	}

	public VBase[] getValids() {
		return _valids;
	}

	public T setValid(VBase... valids) {
		_valids = valids;
		return (T) this;
	}

	public String getName() {
		return _name;
	}

	public String getNameMH() {
		return _name + ":";
	}

	public T setName(String name) {
		_name = name;
		return (T) this;
	}

	public FmtBase getFmt() {
		return _fmt;
	}

	public String getCode() {
		return _code;
	}

	public T setCode(String code) {
		_code = code;
		return (T) this;
	}

	public TbBase getTb() {
		return _fld.getTb();
	}

	public boolean isOpt() {
		return _fld.isOpt();
	}

	public OptBase getOpt() {
		return _fld.getOpt();
	}

	public Object getHelp() {
		if (_help == null)
			return "";
		return _help;
	}

	public T setHelp(Object help) {
		_help = help;
		return (T) this;
	}

	public T setFmt(FmtBase fmt) {
		_fmt = fmt;
		return (T) this;
	}

	public String getRed() {
		return _red;
	}

	public T setRed(String red) {
		_red = red;
		return (T) this;
	}

	public OOutType getType() {
		return _type;
	}

	public void setType(OOutType type) {
		this._type = type;
	}

	/**
	 * @return the align
	 */
	public OAlign getAlign() {
		return _align;
	}

	/**
	 * @param align
	 *          the align to set
	 */
	public void setAlign(OAlign align) {
		_align = align;
	}

	public OAlign getAlignTitle() {
		return _alignTitle;
	}

	public void setAlignTitle(OAlign alignTitle) {
		_alignTitle = alignTitle;
	}

	/**
	 * @param xtype
	 *          the xtype to set
	 */
	public void setXtype(String xtype) {
		_xtype = xtype;
	}

	/**
	 * @return the useNull
	 */
	public boolean isUseNull() {
		return _useNull;
	}

	/**
	 * @param useNull
	 *          the useNull to set
	 */
	public void setUseNull(boolean useNull) {
		_useNull = useNull;
	}

	/**
	 * @return the widthList
	 */
	public float getWidthList() {
		return _widthList;
	}

	/**
	 * @param widthList
	 *          the widthList to set
	 */
	public void setWidthList(double widthList) {
		_widthList = (float) widthList;
	}

	/**
	 * @return the widthFld
	 */
	public float getWidthFld() {
		return _widthFld;
	}

	/**
	 * @param widthFld
	 *          the widthFld to set
	 */
	public void setWidthFld(double widthFld) {
		_widthFld = (float) widthFld;
	}

	/**
	 * @return the readOnly
	 */
	public String getReadOnly() {
		return _readOnly;
	}

	/**
	 * @param readOnly
	 *          the readOnly to set
	 */
	public T setReadOnly(String readOnly) {
		_readOnly = readOnly;
		return (T) this;
	}

	/**
	 * @return the readOnly
	 */
	public String getHidden() {
		return _hidden;
	}

	/**
	 * @param readOnly
	 *          the readOnly to set
	 */
	public T setHidden(String hidden) {
		_hidden = hidden;
		return (T) this;
	}

	public String getExpandCol() {
		return _expandCol;
	}

	public void setExpandCol(String expandCol) {
		_expandCol = expandCol;
	}

	/**
	 * 判断是否为同一个Fld
	 * 
	 * @param fld
	 * @return
	 */
	public boolean equals(IEnumFld fld) {
		return getFld() == fld.getFld();
	}

	/**
	 * @return the otherAttrs
	 */
	public ExtList attrs() {
		if (_attrs == null)
			_attrs = new ExtList();
		return _attrs;
	}

	/**
	 * @return the listName
	 */
	public String getListName() {
		return _listName;
	}

	/**
	 * @param listName the listName to set
	 */
	public T setListName(String listName) {
		_listName = listName;
		return (T) this;
	}
}
