/**
 * 
 */
package irille.pub.print;

import irille.pub.print.GrfNode.GrfExp;
import irille.pub.print.Grfs.GrfColumnContent;
import irille.pub.print.Grfs.GrfColumnTitle;
import irille.pub.print.Grfs.GrfItemsColumn;
import irille.pub.print.Grfs.GrfItemsColumnContentCell;
import irille.pub.print.Grfs.GrfItemsColumnTitleCell;
import irille.pub.print.Grfs.GrfItemsField;
import irille.pub.print.Grfs.GrfItemsGroup;
import irille.pub.print.Grfs.GrfRecordset;
import irille.pub.tb.EnumLine;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.IEnumOpt;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

/**
 * Grf产生器的明细表基类对象
 * 
 * @author whx
 * 
 */
public class GrfDetailGrid<T extends GrfDetailGrid> extends GrfNode<T> {
	public static final String ACROSS_DOWN = "acrossDown";
	public static final String DOWN_ACROSS_EQUAL = "downAcrossEqual";
	protected GrfExp _borderStyles = new GrfExp("[]");
	protected GrfExp _showColLine = new GrfExp("F");
	protected GrfExp _showRowLine = new GrfExp("F");
	private GrfExp _colLineWidth = null;
	private GrfExp _rowLineWidth = null;
	private GrfExp _pageColumnDirection = null;
	private GrfExp _pageColumnCount = null;
	private GrfExp _showTitileCellUnderLine = new GrfExp("F");
	protected GrfRecordset _recordset = new GrfRecordset(this);
	protected GrfItemsColumn _itemsColumn = new GrfItemsColumn(this);
	protected GrfColumnContent _columnContent = new GrfColumnContent(this);
	protected GrfColumnTitle _columnTitle = new GrfColumnTitle(this);
	protected GrfItemsGroup _itemsGroup;

	private VFlds _vflds;
	private double[] _colWidths;

	/**
	 * 构造方法
	 * 
	 * @param parent
	 *          父节点
	 * @param vflds
	 *          字段集
	 * @param columnTitleHeight
	 *          表头高度
	 * @param columnContentHeight
	 *          表内容高度
	 * @param colWidths
	 *          列宽
	 */
	public GrfDetailGrid(GrfNode parent, VFlds vflds, double columnTitleHeight,
			double columnContentHeight, double colWidths[], VFlds... cvflds) {
		super(parent, "Object DetailGrid");
		if (cvflds.length == vflds.size()) {
			int i = 0;
			for (VFld vfld : vflds.getVFlds()) {
				if (cvflds[i] != null)
					vfld.setCode(toCode(cvflds[i]));
				i++;
			}
		}
		_vflds = vflds;
		_columnTitle.setHeight(columnTitleHeight);
		_columnContent.setHeight(columnContentHeight);
		_colWidths = colWidths;
		vflds.setWidths(colWidths);
	}

	public T init() {
		initBefore();
		initDetail(getRecordset().getItemsField(), getItemsColumn(),
				getColumnContent().getItemsColumnContentCell(), getColumnTitle()
						.getItemsColumnTitleCell());
		return (T) this;
	}

	public void initBefore() { // 初始化的前处理
	}

	public void initDetail(GrfItemsField field, GrfItemsColumn col,
			GrfItemsColumnContentCell colContentCell,
			GrfItemsColumnTitleCell colTitleCell) {
		for (VFld fld : _vflds.getVFlds()) {
			addItemsField(field, fld);
			addItemsColumn(col, fld);
			initItemsColumnContentCell(colContentCell, fld);
			initItemsColumnTitleCell(colTitleCell, fld);
		}
	}

	public void addItemsField(GrfItemsField n, VFld fld) {
		n.AddItem_Name(fld.getCode());
	}

	public void addItemsColumn(GrfItemsColumn n, VFld fld) {
		n.AddItem_Name_Width(fld.getCode(), fld.getWidthList());
	}

	public void initItemsColumnContentCell(GrfItemsColumnContentCell n, VFld fld) {
		n.AddItem_Column_DataField(fld.getCode(), fld.getCode()).setTextAlign(
				fld.getAlign());
	}

	public void initItemsColumnTitleCell(GrfItemsColumnTitleCell n, VFld fld) {
		n.AddItem_Column_Text_FreeCell(fld.getCode(), fld.getListName(), getShowTitileCellUnderLine(), fld.getAlignTitle());
	}
	
	private static String toCode(VFlds vflds) {
		String result = "";
		int i = 0;
		for (VFld fld : vflds.getVFlds()) {
			result += fld.getCode();
			if (i < vflds.size() - 1)
				result += ".";
			i++;
		}
		return result;
	}

	@Override
	public T copyNew(GrfNode parent) {
		return copy((T) new GrfDetailGrid(parent, _vflds, _columnTitle.height(),
				_columnContent.height(), _colWidths));
	}

	@Override
	protected T copy(T newObj) {
		super.copy(newObj);
		newObj._borderStyles = _borderStyles;
		newObj._showColLine = _showColLine;
		newObj._showRowLine = _showRowLine;
		newObj._recordset = _recordset.copyNew(newObj);
		newObj._itemsColumn = _itemsColumn.copyNew(newObj);
		newObj._columnContent = _columnContent.copyNew(newObj);
		newObj._columnTitle = _columnTitle.copyNew(newObj);
		newObj._itemsGroup = _itemsGroup.copyNew(newObj);
		return newObj;
	}

	@Override
	public void outBefore2() {
		super.outBefore2();
		addAttr("BorderStyles", _borderStyles);
		addAttr("ShowColLine", _showColLine);
		addAttr("ShowRowLine", _showRowLine);
		addAttr("ColLineWidth", _colLineWidth);
		addAttr("RowLineWidth", _rowLineWidth);
		addAttr("PageColumnDirection", _pageColumnDirection);
		addAttr("PageColumnCount", _pageColumnCount);
		Add(_recordset);
		Add(_itemsColumn);
		Add(_columnContent);
		Add(_columnTitle);
		Add(_itemsGroup);
	}

	/**
	 * @return the borderStyles
	 */
	public GrfExp getBorderStyles() {
		return _borderStyles;
	}

	/**
	 * @param borderStyles
	 *          the borderStyles to set
	 */
	public void setBorderStyles(GrfExp borderStyles) {
		_borderStyles = borderStyles;
	}

	/**
	 * @return the showColLine
	 */
	public GrfExp getShowColLine() {
		return _showColLine;
	}

	/**
	 * @param showColLine
	 *          the showColLine to set
	 */
	public void setShowColLine(GrfExp showColLine) {
		_showColLine = showColLine;
	}

	/**
	 * @return the showRowLine
	 */
	public GrfExp getShowRowLine() {
		return _showRowLine;
	}

	/**
	 * @param showRowLine
	 *          the showRowLine to set
	 */
	public void setShowRowLine(GrfExp showRowLine) {
		_showRowLine = showRowLine;
	}
	
	/**
	 * @return the showTitileCellUnderLine
	 */
	public GrfExp getShowTitileCellUnderLine() {
		return _showTitileCellUnderLine;
	}
	
	/**
	 * @param showTitileCellUnderLine
	 *          the showTitileCellUnderLine to set
	 */
	public void setShowTitileCellUnderLine(GrfExp showTitileCellUnderLine) {
		_showTitileCellUnderLine = showTitileCellUnderLine;
	}
	
	/**
	 * @param showTitileCellUnderLine
	 *          the showTitileCellUnderLine to set
	 */
	public void showTitileCellUnderLine(boolean show) {
		if (show)
			_showTitileCellUnderLine = new GrfExp("T");
		else
			_showTitileCellUnderLine = new GrfExp("F");
	}

	/**
	 * @return the recordset
	 */
	public GrfRecordset getRecordset() {
		return _recordset;
	}

	/**
	 * @return the itemsColumn
	 */
	public GrfItemsColumn getItemsColumn() {
		return _itemsColumn;
	}

	/**
	 * @return the columnContent
	 */
	public GrfColumnContent getColumnContent() {
		return _columnContent;
	}

	/**
	 * @return the columnTitle
	 */
	public GrfColumnTitle getColumnTitle() {
		return _columnTitle;
	}

	public GrfItemsGroup getItemsGroup() {
		return _itemsGroup;
	}

	public void setItemsGroup(GrfItemsGroup itemsGroup) {
		this._itemsGroup = itemsGroup;
	}

	/**
	 * @return the vflds
	 */
	public VFlds getVFlds() {
		return _vflds;
	}

	public VFld getVFld(IEnumFld fld) {
		return _vflds.get(fld);
	}
	
	public GrfExp getColLineWidth() {
		return _colLineWidth;
	}

	public void setColLineWidth(double colLineWidth) {
		_colLineWidth = new GrfExp(colLineWidth);
	}

	public GrfExp getRowLineWidth() {
		return _rowLineWidth;
	}

	public void setRowLineWidth(double rowLineWidth) {
		_rowLineWidth = new GrfExp(rowLineWidth);
	}

	public GrfExp getPageColumnDirection() {
		return _pageColumnDirection;
	}

	public void setPageColumnDirection(GrfExp pageColumnDirection) {
		_pageColumnDirection = pageColumnDirection;
	}

	public GrfExp getPageColumnCount() {
		return _pageColumnCount;
	}

	public void setPageColumnCount(int pageColumnCount) {
		_pageColumnCount = new GrfExp(pageColumnCount);
	}
}
