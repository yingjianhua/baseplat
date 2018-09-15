/**
 * 
 */
package irille.pub.print;

import irille.print.gl.GlReportProfitGrf.GrfFreeGrid;
import irille.pub.print.Sheets.LocationSize;
import irille.pub.print.Sheets.LocationSizeInf;
import irille.pub.tb.Fld;
import irille.pub.tb.IEnumFld;
import irille.pub.tb.Tb;
import irille.pub.view.VFld;
import irille.pub.view.VFlds;
import irille.pub.view.VFld.OAlign;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * @author whx
 * 
 */
public class Grfs implements SheetVarsInf {

	public static class GrfFont<T extends GrfFont> extends GrfNode<T> {
	  protected Object _name = "宋体";
	  protected Object _size = new GrfExp("105000,0");
	  protected Object _weight = 400;
	  protected Object _charset = 134;

		public GrfFont(GrfNode parent) {
			super(parent, "Object Font");
		}

		public T set(String name, String size, Object weight, Object charset) {
			_name = name;
			_size = new GrfExp(size);
			_weight = weight;
			_charset = charset;
			return (T) this;
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfFont(parent));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj._name = _name;
			newObj._size = _size;
			newObj._weight = _weight;
			newObj._charset = _charset;
			return newObj;
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Name", _name);
			addAttr("Size", _size);
			addAttr("Weight", _weight);
			addAttr("Charset", _charset);
		}

		/**
		 * @return the name
		 */
		public Object getName() {
			return _name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(Object name) {
			_name = name;
		}

		/**
		 * @return the size
		 */
		public Object getSize() {
			return _size;
		}

		/**
		 * @param size
		 *            the size to set
		 */
		public void setSize(Object size) {
			_size = size;
		}

		/**
		 * @return the weight
		 */
		public Object getWeight() {
			return _weight;
		}

		/**
		 * @param weight
		 *            the weight to set
		 */
		public void setWeight(Object weight) {
			_weight = weight;
		}

		/**
		 * @return the charset
		 */
		public Object getCharset() {
			return _charset;
		}

		/**
		 * @param charset
		 *            the charset to set
		 */
		public void setCharset(Object charset) {
			_charset = charset;
		}
	}

	public static class GrfPrinter<T extends GrfPrinter> extends GrfNode<T> {
		// private GrfExp _oriention = new GrfExp("Landscape");

		public GrfPrinter(GrfNode parent) {
			super(parent, "Object Printer");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfPrinter(parent));
		}

		// @Override
		// protected T copy(T newObj) {
		// super.copy(newObj);
		// newObj._oriention = _oriention;
		// return newObj;
		// }

		// @Override
		// public void outBefore2() {
		// super.outBefore2();
		// addAttr("Oriention", _oriention);
		// }

		// /**
		// * @return the oriention
		// */
		// public GrfExp getOriention() {
		// return _oriention;
		// }

		// /**
		// * @param oriention
		// * the oriention to set
		// */
		// public void setOriention(GrfExp oriention) {
		// _oriention = oriention;
		// }
	}

	public static class GrfRecordset<T extends GrfRecordset> extends GrfNode<T> {
	  protected GrfItemsField _itemsField = new GrfItemsField(this);

		public GrfRecordset(GrfNode parent) {
			super(parent, "Object Recordset");
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			Add(_itemsField);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfRecordset(parent));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj._itemsField = _itemsField.copyNew(newObj);
			return newObj;
		}

		/**
		 * @return the itemsField
		 */
		public GrfItemsField getItemsField() {
			return _itemsField;
		}
	}

	public static class GrfItemsField<T extends GrfItemsField> extends
			GrfNode<T> {
		public GrfItemsField(GrfNode parent) {
			super(parent, "Items Field");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsField(parent));
		}

		/**
		 * 返回最后加入一节点
		 * 2015-6-30 14:41:50
		 * 修改内容：明细网格初始化时判断所添加字段是否为货位（location）是就添加脚本属性
		 * @param nameValues
		 * @return
		 */
		public GrfItem_Name AddItem_Name(Object... nameValues) {
			GrfItem_Name item = null;
			for (Object obj : nameValues) {
				item = new GrfItem_Name(this, obj);
				if(obj.equals("location")){
					Add(item).addAttr("GetDisplayTextScript", "if (Sender.IsNull) Sender.DisplayText = \"无\"");	
				}else if(obj.equals("gsQty")||obj.equals("qty")){
					Add(item).addExp("Type", "Float").addAttr("Format", "0.####");
				} else{
					Add(item);					
				}
			}
			return item;
		}
	}

	public static class GrfItemsGroup<T extends GrfItemsGroup> extends
			GrfNode<T> {

		private GrfGroupItem _grfGroupItem;
		private SheetPrint _sheetPrint;
		private VFlds _vflds;

		public GrfItemsGroup(GrfNode parent, SheetPrint sheetPrint, VFlds vflds) {
			super(parent, "Items Group");
			_sheetPrint = sheetPrint;
			_vflds = vflds;
			_grfGroupItem = new GrfGroupItem(parent, "Group1", sheetPrint, vflds);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsGroup(parent, _sheetPrint, _vflds));
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
//			addIfHeadNotNull(_grfGroupItem);
			Add(_grfGroupItem);
		}
		
		private void addIfHeadNotNull(GrfGroupItem item) {
			if (item.getHeight() == null
					|| item.getHeight().toString().equals("0"))
				return;
			Add(item);
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param name
		 * @return
		 */
		public GrfItem_Name AddItem_Name(Object name) {
			GrfItem_Name item = new GrfItem_Name(this, name);
			Add(item);
			return item;
		}
		
		public GrfGroupItem getGrfGroupItem() {
			return _grfGroupItem;
		}
		
		public void setGrfGroupItem(GrfGroupItem grfGroupItem) {
			this._grfGroupItem = grfGroupItem;
		}
	}

	public static class GrfGroupItem<T extends GrfGroupItem> extends GrfItem<T> {
		private GrfExp _pageGroup = new GrfExp("T");
		private SheetPrint _sheetPrint;
		private VFlds _vflds;
		protected GrfGroupHeader _groupHeader = null;
		protected GrfGroupFooter _groupFooter = null;

		public GrfGroupItem(GrfNode parent, String name, SheetPrint sheetPrint,
				VFlds vflds) {
			super(parent, name);
			_sheetPrint = sheetPrint;
			_vflds = vflds;
			if (_sheetPrint.getGroupHeader() != null)
				_groupHeader = new GrfGroupHeader(this, _sheetPrint.getGroupHeader(), vflds);
			if (_sheetPrint.getGroupFooter() != null)
				_groupFooter = new GrfGroupFooter(this, _sheetPrint.getGroupFooter(), vflds);
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("PageGroup", _pageGroup);
			Add(_groupHeader);
			Add(_groupFooter);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfGroupItem(parent, getName(), _sheetPrint, _vflds));
		}
		
		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			if (_groupHeader != null)
				newObj._groupHeader = _groupHeader.copyNew(newObj);
			if (_groupFooter != null)
				newObj._groupFooter = _groupFooter.copyNew(newObj);
			return newObj;
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param name
		 * @return
		 */
		public GrfItem_Name AddItem_Name(Object name) {
			GrfItem_Name item = new GrfItem_Name(this, name);
			Add(item);
			return item;
		}
		
		public GrfGroupHeader getGroupHeader() {
			return _groupHeader;
		}
		
		public void setGroupHeader(GrfGroupHeader groupHeader) {
			this._groupHeader = groupHeader;
		}
		
		public GrfGroupFooter getGroupFooter() {
			return _groupFooter;
		}
		
		public void setFooter(GrfGroupFooter groupFooter) {
			this._groupFooter = groupFooter;
		}
		
		/**
		 * @return the sheetPrint
		 */
		public SheetPrint getSheetPrint() {
			return _sheetPrint;
		}
	}

	public static class GrfGroupHeader<T extends GrfGroupHeader> extends
			GrfNode<T> {
		private GrfItemsControl _itemsControl = new GrfItemsControl(this);
		private Sheet _sheet = null;
		private VFlds _vflds;
		private GrfExp _visible = new GrfExp("F");
		private float _height = (float) 0.65;
		private Object _dataField = "amt";

		public GrfGroupHeader(GrfNode parent, Sheet sheet, VFlds vflds) {
			super(parent, "Object GroupHeader");
			_sheet = sheet;
			setHeight(_sheet.height()); // 高度以区域定义为准
			_vflds = vflds;
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfGroupHeader(parent, _sheet, _vflds));
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Visible", _visible);
			addAttr("Height", _height);
		}
		
		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize, OAlign textAlign) {
			GrfItem item = new GrfItem(this, type, name);
			item._text = text;
			item.setSize(locationSize);
			if (textAlign != null)
				item.setTextAlign(textAlign);
			return (GrfItem) _itemsControl.Add(item);
		}
		
		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols, OAlign textAlign) {
			return AddItem(type, name, text,
					_sheet.cellLocationSize(row, col, rows, cols), textAlign);
		}

		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize) {
			return AddItem(type, name, text, locationSize, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols) {
			return AddItem(type, name, text, row, col, rows, cols, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col) {
			return AddItem(type, name, text, row, col, 1, 1);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, OAlign textAlign) {
			return AddItem(type, name, text, row, col, 1, 1, textAlign);
		}

		public GrfItem AddName(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getName()
					+ root().getLabelExtStr(), row, col, rows, cols, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, LocationSize locationSize) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getName()
					+ root().getLabelExtStr(), locationSize, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, int row, int col) {
			return AddName(fld, row, col, 1, 1);
		}

		public GrfItem AddValue(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", row, col,
					rows, cols, _vflds.get(fld).getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, LocationSize locationSize) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", locationSize,
					_vflds.get(fld).getAlign());
		}
	
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField,
				LocationSize locationSize, OAlign textAlign) {
			GrfItem_DataField_Format item = new GrfItem_DataField_Format(this, type, name, dataField);
			item.setSize(locationSize);
			if (textAlign != null)
				item.setTextAlign(textAlign);
			return (GrfItem_DataField_Format) _itemsControl.Add(item);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, int rows, int cols, OAlign textAlign) {
			return AddItemDataField(type, name, dataField,
					_sheet.cellLocationSize(row, col, rows, cols), textAlign);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField,
				LocationSize locationSize) {
			return AddItemDataField(type, name, dataField, locationSize, null);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, int rows, int cols) {
			return AddItemDataField(type, name, dataField, row, col, rows, cols, null);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col) {
			return AddItemDataField(type, name, dataField, row, col, 1, 1);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, OAlign textAlign) {
			return AddItemDataField(type, name, dataField, row, col, 1, 1, textAlign);
		}
		
		public GrfItem_DataField_Format AddSummary(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItemDataField("SummaryBox", _vflds.get(fld).getFld().getCode()
					+ "Summary", _vflds.get(fld).getFld().getCode()
					+ root().getLabelExtStr(), row, col, rows, cols, root()
					.getAlignFleldLabel());
		}

		public GrfItem_DataField_Format AddSummary(IEnumFld fld, LocationSize locationSize) {
			return AddItemDataField("SummaryBox", _vflds.get(fld).getFld().getCode()
					+ "Summary", _vflds.get(fld).getFld().getCode()
					+ root().getLabelExtStr(), locationSize, root()
					.getAlignFleldLabel());
		}
		
		public GrfItem AddValue(IEnumFld fld, int row, int col) {
			return AddValue(fld, row, col, 1, 1);
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param name
		 * @return
		 */
		public GrfItem_Name AddItem_Name_Width(Object name, Object width) {
			GrfItem_Name_Width item = new GrfItem_Name_Width(this, name, width);
			Add(item);
			return item;
		}

		/**
		 * @return the height
		 */
		public float height() {
			return _height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height) {
			_height = (float) height;
		}

		public GrfExp getVisible() {
			return _visible;
		}

		public void setVisible(GrfExp visible) {
			this._visible = visible;
		}
		
		public Object getDataField() {
			return _dataField;
		}

		public void setDataField(Object dataField) {
			this._dataField = dataField;
		}

		public GrfItemsControl getItemsControl() {
			return _itemsControl;
		}

		public void setItemsControl(GrfItemsControl itemsControl) {
			this._itemsControl = itemsControl;
		}

	}

	public static class GrfGroupFooter<T extends GrfGroupFooter> extends
			GrfNode<T> {
		private GrfItemsControl _itemsControl = new GrfItemsControl(this);
		private Sheet _sheet = null;
		private VFlds _vflds;
		private GrfExp _visible = new GrfExp("F");
		private float _height = (float) 0.65;
		private Object _dataField = "amt";

		public GrfGroupFooter(GrfNode parent, Sheet sheet, VFlds vflds) {
			super(parent, "Object GroupFooter");
			_sheet = sheet;
			setHeight(_sheet.height()); // 高度以区域定义为准
			_vflds = vflds;
		}
		
		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfGroupFooter(parent, _sheet, _vflds));
		}


		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Visible", _visible);
			addAttr("Height", _height);
			Add(_itemsControl);
		}
		
		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize, OAlign textAlign) {
			GrfItem item = new GrfItem(this, type, name);
			item._text = text;
			item.setSize(locationSize);
			if (textAlign != null)
				item.setTextAlign(textAlign);
			return (GrfItem) _itemsControl.Add(item);
		}
		
		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols, OAlign textAlign) {
			return AddItem(type, name, text,
					_sheet.cellLocationSize(row, col, rows, cols), textAlign);
		}

		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize) {
			return AddItem(type, name, text, locationSize, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols) {
			return AddItem(type, name, text, row, col, rows, cols, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col) {
			return AddItem(type, name, text, row, col, 1, 1);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, OAlign textAlign) {
			return AddItem(type, name, text, row, col, 1, 1, textAlign);
		}

		public GrfItem AddName(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getFld().getName()
					+ root().getLabelExtStr(), row, col, rows, cols, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, LocationSize locationSize) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getFld().getName()
					+ root().getLabelExtStr(), locationSize, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, int row, int col) {
			return AddName(fld, row, col, 1, 1);
		}

		public GrfItem AddValue(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", row, col,
					rows, cols, _vflds.get(fld).getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, LocationSize locationSize) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", locationSize,
					_vflds.get(fld).getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, int row, int col) {
			return AddValue(fld, row, col, 1, 1);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField,
				LocationSize locationSize, OAlign textAlign) {
			GrfItem_DataField_Format item = new GrfItem_DataField_Format(this, type, name, dataField);
			item.setSize(locationSize);
			if (textAlign != null)
				item.setTextAlign(textAlign);
			return (GrfItem_DataField_Format) _itemsControl.Add(item);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, int rows, int cols, OAlign textAlign) {
			return AddItemDataField(type, name, dataField,
					_sheet.cellLocationSize(row, col, rows, cols), textAlign);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField,
				LocationSize locationSize) {
			return AddItemDataField(type, name, dataField, locationSize, null);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, int rows, int cols) {
			return AddItemDataField(type, name, dataField, row, col, rows, cols, null);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col) {
			return AddItemDataField(type, name, dataField, row, col, 1, 1);
		}
		
		public GrfItem_DataField_Format AddItemDataField(String type, String name, String dataField, int row,
				int col, OAlign textAlign) {
			return AddItemDataField(type, name, dataField, row, col, 1, 1, textAlign);
		}
		
		public GrfItem_DataField_Format AddSummary(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItemDataField("SummaryBox", _vflds.get(fld).getFld().getCode()
					+ "Summary", _vflds.get(fld).getFld().getCode(), row, col, rows, cols, root()
					.getAlignFleldLabel());
		}

		public GrfItem_DataField_Format AddSummary(IEnumFld fld, LocationSize locationSize) {
			return AddItemDataField("SummaryBox", _vflds.get(fld).getFld().getCode()
					+ "Summary", _vflds.get(fld).getFld().getCode(), locationSize, root()
					.getAlignFleldLabel());
		}
		
		public GrfItem_DataField_Format AddSummary(IEnumFld fld, int row, int col) {
			return AddSummary(fld, row, col, 1, 1);
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param name
		 * @return
		 */
		public GrfItem_Name AddItem_Name_Width(Object name, Object width) {
			GrfItem_Name_Width item = new GrfItem_Name_Width(this, name, width);
			Add(item);
			return item;
		}

		/**
		 * @return the height
		 */
		public float height() {
			return _height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height) {
			_height = (float) height;
		}

		public GrfExp getVisible() {
			return _visible;
		}

		public void setVisible(GrfExp visible) {
			this._visible = visible;
		}

		public Object getDataField() {
			return _dataField;
		}

		public void setDataField(Object dataField) {
			this._dataField = dataField;
		}

		public GrfItemsControl getItemsControl() {
			return _itemsControl;
		}

		public void setItemsControl(GrfItemsControl itemsControl) {
			this._itemsControl = itemsControl;
		}
	}

	public static class GrfItemsColumn<T extends GrfItemsColumn> extends
			GrfNode<T> {
		public GrfItemsColumn(GrfNode parent) {
			super(parent, "Items Column");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsColumn(parent));
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param name
		 * @return
		 */
		public GrfItem_Name AddItem_Name_Width(Object name, Object width) {
			GrfItem_Name_Width item = new GrfItem_Name_Width(this, name, width);
			Add(item);
			return item;
		}
	}

	public static class GrfColumnContent<T extends GrfColumnContent> extends
			GrfNode<T> {
		private GrfExp _GrowToNextRow = new GrfExp("T");
		private GrfItemsColumnContentCell _itemsColumnContentCell = new GrfItemsColumnContentCell(
				this);
		private float _height = (float) 0.65;

		public GrfColumnContent(GrfNode parent) {
			super(parent, "Object ColumnContent");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfColumnContent(parent));
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Height", _height);
			addAttr("GrowToNextRow", _GrowToNextRow);
			Add(_itemsColumnContentCell);
		}

		/**
		 * @return the itemsColumnContentCell
		 */
		public GrfItemsColumnContentCell getItemsColumnContentCell() {
			return _itemsColumnContentCell;
		}

		/**
		 * @return the height
		 */
		public float height() {
			return _height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height) {
			_height = (float) height;
		}
		
		
		public GrfColumnContent setGrowToNextRow(GrfExp growToNextRow) {
			_GrowToNextRow = growToNextRow;
			return this;
		}
		public GrfExp growToNextRow() {
			return _GrowToNextRow;
		}
	}

	public static class GrfItemsColumnContentCell<T extends GrfItemsColumnContentCell>
			extends GrfNode<T> {
		public GrfItemsColumnContentCell(GrfNode parent) {
			super(parent, "Items ColumnContentCell");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsColumnContentCell(parent));
		}

		public GrfItem_Column_DataField AddItem_Column_DataField(Object column,
				Object dataField) {
			GrfItem_Column_DataField item = new GrfItem_Column_DataField(this,
					column, dataField);
			List<String> Qty = new ArrayList<String>();
			if(column.equals("gsQty")){
				Qty.add("gsQty");
			}
			if(column.equals("qty")){
				Qty.add("qty");
			}
			if(column.equals("name")||column.equals("gsFormNum")||column.equals("origFormNum")){
				Add(item).addExp("FreeCell", "T").addExp("CanGrow", "T").AddNode("Items Control").AddNode("Item").addExp("Type", "MemoBox")
				.addAttr("Name", "MemoBox1").addExp("Dock", "Fill")
				.addExp("CanGrow", "T").addExp("WordWrap", "T").addExp("TextAlign", "topLeft").addAttr("Text", "[#"+column+"#]").addExp("LnSpacing", "1000");
			}else if(column.equals("gsQty")){
				Add(item).addExp("FreeCell", "T").addExp("CanGrow", "T").AddNode("Items Control").AddNode("Item").addExp("Type", "MemoBox")
				.addAttr("Name", "MemoBox1").addExp("Dock", "Fill")
				.addExp("CanGrow", "T").addExp("WordWrap", "T").addExp("TextAlign", "topRight").addAttr("Text", "[#"+column+"#]").addExp("LnSpacing", "1000");				
			}else if(Qty.size()==2){
				Add(item).addExp("FreeCell", "T").addExp("CanGrow", "T").AddNode("Items Control").AddNode("Item").addExp("Type", "MemoBox")
				.addAttr("Name", "MemoBox1").addExp("Dock", "Fill")
				.addExp("CanGrow", "T").addExp("WordWrap", "T").addExp("TextAlign", "topRight").addAttr("Text", "[#qty#]").addExp("LnSpacing", "1000");				
			}else{
				Add(item);
			}
			return item;
		}
	}

	public static class GrfColumnTitle<T extends GrfColumnTitle> extends
			GrfNode<T> {
		private float _height = (float) 0.65;
		private GrfItemsColumnTitleCell _itemsColumnTitleCell = new GrfItemsColumnTitleCell(
				this);

		public GrfColumnTitle(GrfNode parent) {
			super(parent, "Object ColumnTitle");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfColumnTitle(parent));
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Height", _height);
			Add(_itemsColumnTitleCell);
		}

		/**
		 * @return the itemsColumnTitleCell
		 */
		public GrfItemsColumnTitleCell getItemsColumnTitleCell() {
			return _itemsColumnTitleCell;
		}

		/**
		 * @return the height
		 */
		public float height() {
			return _height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public void setHeight(double height) {
			_height = (float) height;
		}
	}

	public static class GrfItemsColumnTitleCell<T extends GrfItemsColumnTitleCell>
			extends GrfNode<T> {
		
		public GrfItemsColumnTitleCell(GrfNode parent) {
			super(parent, "Items ColumnTitleCell");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsColumnTitleCell(parent));
		}

		public GrfItem_GroupTitle_Column_Text_FreeCell AddItem_Column_Text_FreeCell(
				Object column, Object text, Object freeCell, OAlign textAlign) {
			return AddItem_Column_Text_FreeCell("F", column, text, freeCell, textAlign);
		}

		public GrfItem_GroupTitle_Column_Text_FreeCell AddItem_Column_Text_FreeCell(
				String groupTitle, Object column, Object text, Object freeCell, OAlign textAlign) {
			GrfItem_GroupTitle_Column_Text_FreeCell item = new GrfItem_GroupTitle_Column_Text_FreeCell(
					this, column, text, freeCell);
			item.setGroupTitle(new GrfExp(groupTitle));
			if (freeCell.toString().equals("T"))
				item.add(new GrfItemsControl(this).add(new GrfItem_ColumnTitleCellUnderLine(this, column, text).setTextAlign(textAlign)));//TODO
			else //by whx 2015/6/17
				item.setTextAlign(textAlign);
			Add(item);
			return item;
		}
	}

	public static class GrfItemsParameter<T extends GrfItemsParameter> extends
			GrfNode<T> {
		public GrfItemsParameter(GrfNode parent) {
			super(parent, "Items Parameter");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsParameter(parent));
		}

		/**
		 * 返回最后加入一节点
		 * 
		 * @param nameValues
		 * @return
		 */
		public GrfItem_Name AddItem_Name(Object... nameValues) {
			GrfItem_Name item = null;
			for (Object obj : nameValues) {
				item = new GrfItem_Name(this, obj);
				Add(item);
			}
			return item;
		}
	}

	public static class GrfItemsSection<T extends GrfItemsSection> extends
			GrfNode<T> {
	  protected GrfSectionItem _pageHeader = null, _pageFooter = null,
				_reportHeader = null, _reportFooter = null;
		private SheetPrint _sheetPrint;
		private VFlds _vflds;

		public GrfItemsSection(GrfNode parent, SheetPrint sheetPrint,
				VFlds vflds) {
			super(parent, "Items Section");
			_sheetPrint = sheetPrint;
			_vflds = vflds;
			if (_sheetPrint.getPageHeader() != null)
				_pageHeader = new GrfSectionItem(this, "PageHeader", null,
						_sheetPrint.getPageHeader(), vflds);
			if (_sheetPrint.getPageFooter() != null)
				_pageFooter = new GrfSectionItem(this, "PageFooter", null,
						_sheetPrint.getPageFooter(), vflds);
			if (_sheetPrint.getReportHeader() != null)
				_reportHeader = new GrfSectionItem(this, "ReportHeader",
						"ReportHeader1", _sheetPrint.getReportHeader(), vflds);
			if (_sheetPrint.getReportFooter() != null)
				_reportFooter = new GrfSectionItem(this, "ReportFooter",
						"reportFooter1", _sheetPrint.getReportFooter(), vflds);
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addIfHeadNotNull(_pageHeader);
			addIfHeadNotNull(_pageFooter);
			addIfHeadNotNull(_reportHeader);
			addIfHeadNotNull(_reportFooter);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsSection(parent, _sheetPrint, _vflds));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			if (_pageHeader != null)
				newObj._pageHeader = _pageHeader.copyNew(newObj);
			if (_pageFooter != null)
				newObj._pageFooter = _pageFooter.copyNew(newObj);
			if (_reportHeader != null)
				newObj._reportHeader = _reportHeader.copyNew(newObj);
			if (_reportFooter != null)
				newObj._reportFooter = _reportFooter.copyNew(newObj);
			return newObj;
		}

		private void addIfHeadNotNull(GrfSectionItem item) {
			if (item.getHeight() == null
					|| item.getHeight().toString().equals("0"))
				return;
			Add(item);
		}

		/**
		 * @return the pageHeader
		 */
		public GrfSectionItem getPageHeader() {
			return _pageHeader;
		}

		/**
		 * @param pageHeader
		 *            the pageHeader to set
		 */
		public void setPageHeader(GrfSectionItem pageHeader) {
			_pageHeader = pageHeader;
		}

		/**
		 * @return the pageFooter
		 */
		public GrfSectionItem getPageFooter() {
			return _pageFooter;
		}

		/**
		 * @param pageFooter
		 *            the pageFooter to set
		 */
		public void setPageFooter(GrfSectionItem pageFooter) {
			_pageFooter = pageFooter;
		}

		/**
		 * @return the reportHeader
		 */
		public GrfSectionItem getReportHeader() {
			return _reportHeader;
		}

		/**
		 * @param reportHeader
		 *            the reportHeader to set
		 */
		public void setReportHeader(GrfSectionItem reportHeader) {
			_reportHeader = reportHeader;
		}

		/**
		 * @return the reportFooter
		 */
		public GrfSectionItem getReportFooter() {
			return _reportFooter;
		}

		/**
		 * @param reportFooter
		 *            the reportFooter to set
		 */
		public void setReportFooter(GrfSectionItem reportFooter) {
			_reportFooter = reportFooter;
		}

		/**
		 * @return the sheetPrint
		 */
		public SheetPrint getSheetPrint() {
			return _sheetPrint;
		}
	}

	public static class GrfSectionItem<T extends GrfSectionItem> extends
			GrfItem<T> {
	  protected GrfItemsControl _itemsControl = new GrfItemsControl(this);
		private Sheet _sheet = null;
		private VFlds _vflds;

		public GrfSectionItem(GrfNode parent, String type, String name,
				Sheet sheet, VFlds vflds) {
			super(parent, type, name);
			_sheet = sheet;
			setHeight(_sheet.height()); // 高度以区域定义为准
			_vflds = vflds;
		}

		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize, OAlign textAlign) {
			GrfItem item = new GrfItem(this, type, name);
			item._text = text;
			item.setSize(locationSize);
			if (textAlign != null)
				item.setTextAlign(textAlign);
			return (GrfItem) _itemsControl.Add(item);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols, OAlign textAlign) {
			return AddItem(type, name, text,
					_sheet.cellLocationSize(row, col, rows, cols), textAlign);
		}

		public GrfItem AddItem(String type, String name, String text,
				LocationSize locationSize) {
			return AddItem(type, name, text, locationSize, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, int rows, int cols) {
			return AddItem(type, name, text, row, col, rows, cols, null);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col) {
			return AddItem(type, name, text, row, col, 1, 1);
		}

		public GrfItem AddItem(String type, String name, String text, int row,
				int col, OAlign textAlign) {
			return AddItem(type, name, text, row, col, 1, 1, textAlign);
		}

		public GrfItem AddName(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getName()
					+ root().getLabelExtStr(), row, col, rows, cols, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, LocationSize locationSize) {
			return AddItem("StaticBox", _vflds.get(fld).getFld().getCode()
					+ "Name", _vflds.get(fld).getName()
					+ root().getLabelExtStr(), locationSize, root()
					.getAlignFleldLabel());
		}
		
		public GrfItem AddNameMulti(LocationSize locationSize, IEnumFld...flds) {
			String name = "";
			String code = "";
			for (int i = 0; i < flds.length; i++) {
				name += _vflds.get(flds[i]).getName();
				code += _vflds.get(flds[i]).getFld().getCode();
				if (i < flds.length - 1)
					name += "/";
			}
			return AddItem("StaticBox", code + "Name", name + root().getLabelExtStr(), locationSize, root()
					.getAlignFleldLabel());
		}

		public GrfItem AddName(IEnumFld fld, int row, int col) {
			return AddName(fld, row, col, 1, 1);
		}
		
		public GrfFreeGrid AddFreeGrid(String name, LocationSize locationSize, double[] heights, double...widths) {
				return new GrfFreeGrid(AddItem("FreeGrid", name, null, locationSize), name).init(heights, widths);
		}

		public GrfItem AddValue(IEnumFld fld, int row, int col, int rows,
				int cols) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", row, col,
					rows, cols, _vflds.get(fld).getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, int row, int col, int rows,
				int cols, VFlds vflds) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ toCode(vflds) + "}#]", row, col, rows, cols,
					_vflds.get(fld).getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, LocationSize locationSize) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ _vflds.get(fld).getFld().getCode() + "}#]", locationSize,
					_vflds.get(fld).getAlign());
		}
		
		public GrfItem AddValue(IEnumFld fld, LocationSize locationSize,
				VFlds vflds) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ toCode(vflds) + "}#]", locationSize, _vflds.get(fld)
					.getAlign());
		}
		/**
		 * 设置普通多值的单元格
		 * @param fld 用以设置组件名和对其方式，建议使用第一个字段
		 * @param locationSize
		 * @param flds
		 * @return
		 */
		public GrfItem AddValueMulti(IEnumFld fld, LocationSize locationSize, IEnumFld...flds) {
			String code = "";
			for (int i = 0; i < flds.length; i++) {
				code += "[#{" + flds[i] + "}#]";
				if (i < flds.length - 1)
					code += "/";
			}
			return AddItem("MemoBox", code.replace("/", ""), code, locationSize, _vflds.get(fld)
					.getAlign());
		}
		/**
		 * 设置外键多值的单元格
		 * @param fld 用以设置组件名和对其方式，建议使用第一个字段
		 * @param locationSize
		 * @param multiVflds
		 * @return
		 */
		public GrfItem AddValueMulti(IEnumFld fld, LocationSize locationSize, VFlds...multiVflds) {
			String code = "";
			for (int i = 0; i < multiVflds.length; i++) {
				code += "[#{" + toCode(multiVflds[i]) + "}#]";
				if (i < multiVflds.length - 1)
					code += "/";
			}
			return AddItem("MemoBox", code.replace("/", ""), code, locationSize, _vflds.get(fld)
					.getAlign());
		}
		/**
		 * 设置外键多值的单元格
		 * @param fld 用以设置组件名和对其方式，建议使用第一个字段
		 * @param locationSize
		 * @param multiVflds
		 * @return
		 */
		public GrfItem AddValueMulti(IEnumFld fld, LocationSize locationSize, String...multiToLinkCodes) {
			String code = "";
			for (int i = 0; i < multiToLinkCodes.length; i++) {
				code += "[#{" + multiToLinkCodes[i] + "}#]";
				if (i < multiToLinkCodes.length - 1)
					code += "/";
			}
			return AddItem("MemoBox", code.replace("/", ""), code, locationSize, _vflds.get(fld)
					.getAlign());
		}

		public GrfItem AddValue(IEnumFld fld, LocationSize locationSize,
				VFlds vflds, Tb[] tbs, VFlds[] multiVflds) {
			return AddItem("MemoBox", _vflds.get(fld).getFld().getCode(), "[#{"
					+ toLinkCode(vflds, tbs, multiVflds) + "}#]", locationSize, _vflds.get(fld)
					.getAlign());
		}
		
		public GrfItem AddValue(IEnumFld fld, int row, int col) {
			return AddValue(fld, row, col, 1, 1);
		}

		public T addLine(String name, Object top, Object width) {
			GrfItem item = new GrfItem(this, "Line", name);
			item._top = top;
			item._width = width;
			Add(item);
			return (T) this;
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
		
		/**
		 * 生成无外键的外表关联字段字符串，后两个数组参数长度必须相同
		 * @param vflds 当前相关外表的字段
		 * @param tbs 关联的外表
		 * @param multiVfld 对应tbs顺序的关联外表的字段
		 * @return 无外键的外表关联字段字符串
		 */
		private static String toLinkCode(VFlds vflds, Tb[] tbs, VFlds[] multiVflds) {
			String result = toCode(vflds);
			for (int i = 0; i < tbs.length; i++) {
				result += "_" + tbs[i].getCode() + "." + toCode(multiVflds[i]);
			}
			return result;
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			Add(_itemsControl);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfSectionItem(parent, getType().toString(),
					getName(), _sheet, _vflds));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj._itemsControl = _itemsControl.copyNew(newObj);
			return newObj;
		}

		/**
		 * @return the itemsControl
		 */
		public GrfItemsControl getItemsControl() {
			return _itemsControl;
		}

		/**
		 * @param itemsControl
		 *            the itemsControl to set
		 */
		public void setItemsControl(GrfItemsControl itemsControl) {
			_itemsControl = itemsControl;
		}

		/**
		 * @return the area
		 */
		public Sheet getSheet() {
			return _sheet;
		}
	}

	public static class GrfItemsControl<T extends GrfItemsControl> extends
			GrfNode<T> {
		public GrfItemsControl(GrfNode parent) {
			super(parent, "Items Control");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemsControl(parent));
		}

	}

	public static class GrfItem_DataField_Format<T extends GrfItem_DataField_Format>
			extends GrfItem<T> {
		private String _dataField;
		private String _format;
		
		public GrfItem_DataField_Format(GrfNode parent, String type, String name, String dataField) {
			super(parent, type.toString(), name.toString());
			_dataField = dataField;
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_DataField_Format(parent, getType().toString(), getName(), _dataField));
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
//			insertAttrToFirst("DataField", _dataField);
			addAttr("DataField", _dataField);
			addAttr("Format", _format);
		}

		/**
		 * @return the dataField
		 */
		public Object getDataField() {
			return _dataField;
		}

		/**
		 * @param dataField
		 *            the dataField to set
		 */
		public void setDataField(String dataField) {
			_dataField = dataField;
		}

		public Object getFormat() {
			return _format;
		}

		public void setFormat(String format) {
			this._format = format;
		}
	}

	public static class GrfItem_Column_DataField<T extends GrfItem_Column_DataField>
			extends GrfItemBase<T> {
		private Object _column;
		private Object _dataField;

		public GrfItem_Column_DataField(GrfNode parent, Object column,
				Object dataField) {
			super(parent);
			_dataField = dataField;
			_column = column;
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_Column_DataField(parent, _column,
					_dataField));
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
			insertAttrToFirst("DataField", _dataField);
			insertAttrToFirst("Column", _column);
		}

		/**
		 * @return the column
		 */
		public Object getColumn() {
			return _column;
		}

		/**
		 * @param column
		 *            the column to set
		 */
		public void setColumn(Object column) {
			_column = column;
		}

		/**
		 * @return the dataField
		 */
		public Object getDataField() {
			return _dataField;
		}

		/**
		 * @param dataField
		 *            the dataField to set
		 */
		public void setDataField(Object dataField) {
			_dataField = dataField;
		}
	}

	public static class GrfItem_GroupTitle_Column_Text_FreeCell<T extends GrfItem_GroupTitle_Column_Text_FreeCell>
			extends GrfItemBase<T> {
			  protected Object _groupTitle = null;
		private Object _column, _text, _freeCell;

		public GrfItem_GroupTitle_Column_Text_FreeCell(GrfNode parent,
				Object column, Object text, Object freeCell) {
			super(parent);
			_column = column;
			_text = text;
			_freeCell = freeCell;
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
			insertExpToFirst("FreeCell", _freeCell);
			insertAttrToFirst("Text", _text);
			insertAttrToFirst("Column", _column);
			insertExpToFirst("GroupTitle", _groupTitle);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_GroupTitle_Column_Text_FreeCell(parent,
					_column, _text, _freeCell));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj._groupTitle = _groupTitle;
			return newObj;
		}

		/**
		 * @return the groupTitle
		 */
		public Object getGroupTitle() {
			return _groupTitle;
		}

		/**
		 * @param groupTitle
		 *            the groupTitle to set
		 */
		public void setGroupTitle(Object groupTitle) {
			_groupTitle = groupTitle;
		}

		/**
		 * @return the column
		 */
		public Object getColumn() {
			return _column;
		}

		/**
		 * @param column
		 *            the column to set
		 */
		public void setColumn(Object column) {
			_column = column;
		}

		/**
		 * @return the text
		 */
		public Object getText() {
			return _text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(Object text) {
			_text = text;
		}

		/**
		 * @return the text
		 */
		public Object getFreeCell() {
			return _freeCell;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setFreeCell(Object freeCell) {
			_freeCell = freeCell;
		}
	}

	public static class GrfItem_ColumnTitleCellUnderLine<T extends GrfItem_ColumnTitleCellUnderLine>
			extends GrfItemBase<T> {
		private Object _type, _name, _borderStyles, _borderColor, _dock, _text;

		public GrfItem_ColumnTitleCellUnderLine(GrfNode parent, Object name, Object text) {
			super(parent);
			_name = name;
			_text = text;
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
			insertAttrToFirst("Text", _text);
			insertExpToFirst("Dock", "Fill");
			insertExpToFirst("BorderColor", "Black");
			insertExpToFirst("BorderStyles", "[DrawBottom]");
			insertAttrToFirst("Name", _name);
			insertExpToFirst("Type", "StaticBox");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_ColumnTitleCellUnderLine(parent, _name, _text));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			return newObj;
		}

		public Object getType() {
			return _type;
		}

		public void setType(Object type) {
			this._type = type;
		}

		public Object getName() {
			return _name;
		}

		public void setName(Object name) {
			this._name = name;
		}

		public Object getBorderStyles() {
			return _borderStyles;
		}

		public void setBorderStyles(Object borderStyles) {
			this._borderStyles = borderStyles;
		}

		public Object getBorderColor() {
			return _borderColor;
		}

		public void setBorderColor(Object borderColor) {
			this._borderColor = borderColor;
		}

		public Object getDock() {
			return _dock;
		}

		public void setDock(Object dock) {
			this._dock = dock;
		}

		/**
		 * @return the text
		 */
		public Object getText() {
			return _text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public void setText(Object text) {
			_text = text;
		}
	}

	public static class GrfItem_Name_Width<T extends GrfItem_Name_Width>
			extends GrfItem_Name<T> {
		private Object _width;

		public GrfItem_Name_Width(GrfNode parent, Object name, Object width) {
			super(parent, name);
			_width = width;
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Width", _width);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_Name_Width(parent, getName(), _width));
		}

		/**
		 * @return the width
		 */
		public Object getWidth() {
			return _width;
		}

		/**
		 * @param width
		 *            the width to set
		 */
		public void setWidth(Object width) {
			_width = width;
		}
	}

	public static class GrfItem_Name<T extends GrfItem_Name> extends
			GrfItemBase<T> {
		private Object _name;

		public GrfItem_Name(GrfNode parent, Object name) {
			super(parent);
			_name = name;
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
			addAttr("Name", _name);
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItem_Name(parent, _name));
		}

		/**
		 * @return the name
		 */
		public Object getName() {
			return _name;
		}
	}

	public static class GrfItem<T extends GrfItem> extends GrfItemBase<T> {
		private String _name;
		private Object _type;
		private Object _left = null;
		private Object _top = null;
		private Object _width = null;
		private Object _height = null;
		protected Object _text = null;

		public GrfItem(GrfNode parent, String name) {
			super(parent);
			_type = null;
			_name = name;
		}

		public GrfItem(GrfNode parent, String type, String name) {
			super(parent);
			_type = new GrfExp(type);
			_name = name;
		}

		public T setSize(Object left, Object top, Object width, Object height) {
			_left = left;
			_top = top;
			_width = width;
			_height = height;
			return (T) this;
		}

		public T setSize(LocationSizeInf ls) {
			setSize(ls.left(), ls.top(), ls.width(), ls.height());
			return (T) this;
		}

		@Override
		public T copyNew(GrfNode parent) {
			return _type == null ? copy((T) new GrfItem(parent, _name))
					: copy((T) new GrfItem(parent, _type.toString(), _name));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj.setSize(_left, _top, _width, _height);
			newObj._text = _text;
			return newObj;
		}

		public T setTextFldName(Fld fld) {
			_text = fld.getName();
			return (T) this;
		}

		public T setTextFldValue(Fld fld) {
			_text = "[#{" + fld.getCode() + "}#]";
			return (T) this;
		}

		public T addFont(String name, String size, Object weight, Object charset) {
			GrfFont font = new GrfFont(this);
			font.set(name, size, weight, charset);
			return (T) this;
		}

		public static String pageNumber() {
			return "[#SystemVar(PageNumber)#]";
		}

		public static String pageCount() {
			return "[#SystemVar(PageCount)#]";
		}

		@Override
		public void outBefore1() {
			super.outBefore1();
			insertAttrToFirst("Name", _name);
			if (_type != null)
				insertAttrToFirst("Type", _type);
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			addAttr("Left", _left);
			addAttr("Top", _top);
			addAttr("Width", _width);
			addAttr("Height", _height);
			addAttr("Text", _text);
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return _name;
		}

		/**
		 * @param name
		 *            the name to set
		 */
		public void setName(String name) {
			_name = name;
		}

		/**
		 * @return the left
		 */
		public Object getLeft() {
			return _left;
		}

		/**
		 * @param left
		 *            the left to set
		 */
		public void setLeft(Object left) {
			_left = left;
		}

		/**
		 * @return the top
		 */
		public Object getTop() {
			return _top;
		}

		/**
		 * @param top
		 *            the top to set
		 */
		public void setTop(Object top) {
			_top = top;
		}

		/**
		 * @return the width
		 */
		public Object getWidth() {
			return _width;
		}

		/**
		 * @param width
		 *            the width to set
		 */
		public void setWidth(Object width) {
			_width = width;
		}

		/**
		 * @return the height
		 */
		public Object getHeight() {
			return _height;
		}

		/**
		 * @param height
		 *            the height to set
		 */
		public T setHeight(Object height) {
			_height = height;
			return (T) this;
		}

		/**
		 * @return the text
		 */
		public Object getText() {
			return _text;
		}

		/**
		 * @param text
		 *            the text to set
		 */
		public T setText(Object text) {
			_text = text;
			return (T) this;
		}

		/**
		 * @return the type
		 */
		public Object getType() {
			return _type;
		}
	}

	public static class GrfItemBase<T extends GrfItemBase> extends GrfNode<T> {
	  protected Object _textAlign = null;

		public GrfItemBase(GrfNode parent) {
			super(parent, "Item");
		}

		@Override
		public T copyNew(GrfNode parent) {
			return copy((T) new GrfItemBase(parent));
		}

		@Override
		protected T copy(T newObj) {
			super.copy(newObj);
			newObj._textAlign = _textAlign;
			return newObj;
		}

		@Override
		public void outBefore2() {
			super.outBefore2();
			if (_textAlign != null && _textAlign != OAlign.TOP_LEFT)
				addAttr("TextAlign", _textAlign);
		}

		/**
		 * @return the textAlign
		 */
		public Object getTextAlign() {
			return _textAlign;
		}

		public T setTextAlign(OAlign align) {
			_textAlign = align.getLine().getCode();
			return (T) this;
		}

		public T setTextAlignToTopLeft() {
			return setTextAlign(ALIGN.TOP_LEFT);
		}

		public T setTextAlignToTopRight() {
			return setTextAlign(ALIGN.TOP_RIGHT);
		}

		public T setTextAlignToTopCenter() {
			return setTextAlign(ALIGN.TOP_CENTER);
		}

		public T setTextAlignToBottomLeft() {
			return setTextAlign(ALIGN.BOTTOM_LEFT);
		}

		public T setTextAlignToBottomRight() {
			return setTextAlign(ALIGN.BOTTOM_RIGHT);
		}

		public T setTextAlignToBottomCenter() {
			return setTextAlign(ALIGN.BOTTOM_CENTER);
		}

		public T setTextAlignToMiddleLeft() {
			return setTextAlign(ALIGN.MIDDLE_LEFT);
		}

		public T setTextAlignToMiddleRight() {
			return setTextAlign(ALIGN.MIDDLE_RIGHT);
		}

		public T setTextAlignToMiddleCenter() {
			return setTextAlign(ALIGN.MIDDLE_CENTER);
		}

	}
}
