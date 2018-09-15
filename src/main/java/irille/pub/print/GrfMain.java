/**
 * 
 */
package irille.pub.print;

import irille.pub.FileTools;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.PubInfs.IMsg;
import irille.pub.ext.Ext;
import irille.pub.print.GrfNode.GrfExp;
import irille.pub.print.Grfs.GrfFont;
import irille.pub.print.Grfs.GrfGroupFooter;
import irille.pub.print.Grfs.GrfGroupHeader;
import irille.pub.print.Grfs.GrfItemsGroup;
import irille.pub.print.Grfs.GrfItemsParameter;
import irille.pub.print.Grfs.GrfItemsSection;
import irille.pub.print.Grfs.GrfPrinter;
import irille.pub.print.Grfs.GrfSectionItem;
import irille.pub.svr.Env;
import irille.pub.svr.Svr;
import irille.pub.tb.IEnumFld;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 报表打印产生器的主表基类
 * 
 * @author whx
 * 
 */
public class GrfMain<T extends GrfMain> extends GrfNode<T> implements Test,
		SheetVarsInf {
	private static final Log LOG = new Log(GrfMain.class);
	public static final GrfFont FONT_REPORT_HEAD = (GrfFont) (new GrfFont(null)
			.set("宋体", "200000,0", 900, 134));
	public static final GrfFont FONT_14_BOLD = (GrfFont) (new GrfFont(null)
	.set("宋体", "140000,0", 900, 134));
	public static final GrfFont FONT = (GrfFont) (new GrfFont(null).set("宋体",
			"120000,0", 400, 134));
	public static final GrfFont FONT_XIAOWU = (GrfFont) (new GrfFont(null).set("宋体",
			"100000,0", 400, 134));

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		CRT_FILE("正在建立文件：{0}......"),OK("ok!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	protected String _fileNameAdd = ""; // 文件名的附加字符
	private OAlign _alignFleldLabel = ALIGN.MIDDLE_LEFT; // 字段名的对齐方式
	private OAlign _alignListTitle = ALIGN.MIDDLE_CENTER; // 字段名在列表中的对齐方式

	private Class _mainClazz;
	private VFlds _vflds;

	protected String _labelExtStr = ":"; // 字段名称的后缀
	protected String _version = "5.8.0.6";
	protected String _title = "你的报表";
	private GrfFont _font = FONT_XIAOWU.copyNew(this);
	protected GrfPrinter _printer = new GrfPrinter(this);
	protected GrfDetailGrid _detailGrid = null;
	protected GrfItemsParameter _itemsParameter = new GrfItemsParameter(this);
	private SheetPrint _sheetPrint = new SheetPrint();
	protected GrfItemsSection _itemsSection = null;
	private GrfItemsGroup _itemsGroup = null;

	public GrfMain(Class mainClazz, VFlds vflds) {
		super(null, "Object Report");
		_mainClazz = mainClazz;
		_vflds = vflds;
	}

	/**
	 * 数据初始化
	 * 
	 * @return
	 */
	public T init() {
		initBefore(); // 初始化的前处理

		initItemsParameter(_itemsParameter, _vflds);

		initSheetPrint(_sheetPrint);
		initGrfPrinter(_printer);
		_itemsGroup = new GrfItemsGroup(this, _sheetPrint, _vflds);
		initItemsGroup(_itemsGroup, _itemsGroup.getGrfGroupItem().getGroupHeader(), _itemsGroup.getGrfGroupItem().getGroupFooter());
		_detailGrid.setItemsGroup(_itemsGroup);
		_itemsSection = new GrfItemsSection(this, _sheetPrint, _vflds);
		initItemsSection(_itemsSection, _itemsSection.getPageHeader(),
				_itemsSection.getPageFooter(), _itemsSection.getReportHeader(),
				_itemsSection.getReportFooter());
		return (T) this;
	}

	/**
	 * 初始化的前处理
	 */
	public void initBefore() { //
	}

	/**
	 * 初始化打印表格对象，用于定义各部门的高度及内部放置对象的单元格数量
	 * 
	 * @param sp
	 */
	public void initSheetPrint(SheetPrint sp) {
	}
	
	public void initGrfPrinter(GrfPrinter gp) {
		gp.addAttr("Width", _sheetPrint.getPageSize().width());
		gp.addAttr("Height", _sheetPrint.getPageSize().height());
		if (_sheetPrint.isOriention())
			gp.addAttr("Oriention", new GrfExp("Landscape"));
		gp.addAttr("LeftMargin", _sheetPrint.getPageGap().left());
		gp.addAttr("TopMargin", _sheetPrint.getPageGap().top());
		gp.addAttr("RightMargin", _sheetPrint.getPageGap().right());
		gp.addAttr("BottomMargin", _sheetPrint.getPageGap().bottom());
	}

	public void initItemsParameter(GrfItemsParameter n, VFlds vflds) {
		for (VFld fld : vflds.getVFlds())
			n.AddItem_Name(fld.getCode());
	}

	public void initItemsSection(GrfItemsSection is, GrfSectionItem ph,
			GrfSectionItem pf, GrfSectionItem rh, GrfSectionItem rf) {
	}
	
	public void initItemsGroup(GrfItemsGroup ig, GrfGroupHeader gh, GrfGroupFooter gf) {
	}

	@Override
	public T copyNew(GrfNode parent) {
		return copy((T) new GrfMain(_mainClazz, _vflds));
	}

	public final String getFileName() {
		return Env.getResourceDir() + "print/" + Ext.getPag(_mainClazz) + "/"
				+ Ext.getClazz(_mainClazz) + getFileNameAdd() + ".grf";
	}

	@SuppressWarnings("unchecked")
  @Override
	protected T copy(T newObj) {
		super.copy(newObj);
		newObj._fileNameAdd = _fileNameAdd;

		newObj._version = _version;
		newObj._title = _title;
		newObj._printer = _printer.copyNew(newObj);
		if (_detailGrid != null)
			newObj._detailGrid = _detailGrid.copyNew(newObj);
		newObj._itemsParameter = _itemsParameter.copyNew(newObj);
		newObj._itemsSection = _itemsSection.copyNew(newObj);
		newObj._labelExtStr = _labelExtStr;
		return newObj;
	}

	/**
	 * 取字段名称的后缀，一般为“:”
	 * @return the labelExtStr
	 */
	public String getLabelExtStr() {
		return _labelExtStr;
	}

	/**
	 * @param labelExtStr
	 *          the labelExtStr to set
	 */
	public T setLabelExtStr(String labelExtStr) {
		_labelExtStr = labelExtStr;
		return (T) this;
	}

	private final String lastCode(Class clazz) {
		return Str.getClazzLastCode(clazz);
	}

	/**
	 * 建立文件
	 * 
	 * @return
	 */
	public final T crtFile() {
		LOG.info(Msgs.CRT_FILE, getFileName());
		FileTools.writeStr(getFileName(), out());
		LOG.info(Msgs.OK);
		return (T) this;
	}

	public final String out() {
		StringBuilder buf = new StringBuilder();
		out(buf, 0);
		return buf.toString();
	}

	@Override
	public void outBefore2() {
		super.outBefore2();
		addAttr("Version", _version);
		addAttr("Title", _title);
		Add(_font);
		Add(_printer);
		Add(_detailGrid);
		Add(_itemsParameter);
		Add(_itemsSection);
	}

	/**
	 * 取文件名的后部添加字符串
	 * @return the fileNameAdd
	 */
	public final String getFileNameAdd() {
		return _fileNameAdd;
	}

	/**
	 * @param fileNameAdd
	 *          the fileNameAdd to set
	 */
	public final T setFileNameAdd(String fileNameAdd) {
		_fileNameAdd = fileNameAdd;
		return (T) this;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
		return _version;
	}

	/**
	 * @param version
	 *          the version to set
	 */
	public void setVersion(String version) {
		_version = version;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return _title;
	}

	public VFlds getVFlds() {
		return _vflds;
	}

	/**
	 * @param title
	 *          the title to set
	 */
	public void setTitle(String title) {
		_title = title;
	}

	/**
	 * @return the font
	 */
	public GrfFont getFont() {
		return _font;
	}

	/**
	 * @param font
	 *          the font to set
	 */
	public void setFont(GrfFont font) {
		_font = font;
	}

	/**
	 * @return the printer
	 */
	public GrfPrinter getPrinter() {
		return _printer;
	}

	/**
	 * @param printer
	 *          the printer to set
	 */
	public void setPrinter(GrfPrinter printer) {
		_printer = printer;
	}

	/**
	 * @return the detailGrid
	 */
	public GrfDetailGrid getDetailGrid() {
		return _detailGrid;
	}

	/**
	 * @param detailGrid
	 *          the detailGrid to set
	 */
	public T setDetailGrid(GrfDetailGrid detailGrid) {
		_detailGrid = detailGrid;
		return (T) this;
	}

	/**
	 * @return the itemsParameter
	 */
	public GrfItemsParameter getItemsParameter() {
		return _itemsParameter;
	}

	/**
	 * @param itemsParameter
	 *          the itemsParameter to set
	 */
	public void setItemsParameter(GrfItemsParameter itemsParameter) {
		_itemsParameter = itemsParameter;
	}

	/**
	 * @return the itemsSection
	 */
	public GrfItemsSection getItemsSection() {
		return _itemsSection;
	}

	/**
	 * @param itemsSection
	 *          the itemsSection to set
	 */
	public void setItemsSection(GrfItemsSection itemsSection) {
		_itemsSection = itemsSection;
	}

	public GrfItemsGroup getItemsGroup() {
		return _itemsGroup;
	}

	public void setItemsGroup(GrfItemsGroup itemsGroup) {
		this._itemsGroup = itemsGroup;
	}

	public VFld getVFld(IEnumFld fld) {
		return _vflds.get(fld);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.Test#run(junit.framework.TestResult)
	 */
	@Override
	public void run(TestResult arg0) {
	}

	public final void testCase() {
	}

	/**
	 * 此代码用于产生帮助文档及帮助文档相关的自动代码
	 * 
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	@Override
	public int countTestCases() {
		Svr.init();
		// System.err.println("begin");
		init();
		crtFile();
		// System.err.println("end");
		return 0;
	}

	/**
	 * @return the alignFleldLabel
	 */
	public OAlign getAlignFleldLabel() {
		return _alignFleldLabel;
	}

	/**
	 * @param alignFleldLabel
	 *          the alignFleldLabel to set
	 */
	public T setAlignFleldLabel(OAlign alignFleldLabel) {
		_alignFleldLabel = alignFleldLabel;
		return (T) this;
	}

	/**
	 * @return the alignListTitle
	 */
	public OAlign getAlignListTitle() {
		return _alignListTitle;
	}

	/**
	 * @param alignListTitle
	 *          the alignListTitle to set
	 */
	public T setAlignListTitle(OAlign alignListTitle) {
		_alignListTitle = alignListTitle;
		return (T) this;
	}

}
