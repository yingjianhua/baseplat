/**
 * 
 */
package irille.pub.print;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.print.Sheets.LocationSize;
import irille.pub.print.Sheets.LocationSizeInf;
import irille.pub.print.Sheets.LocationSizeSheet;
import irille.pub.print.Sheets.RowCol;
import irille.pub.print.Sheets.RowColInf;
import irille.pub.print.Sheets.SheetGap;

import java.util.ArrayList;
import java.util.Vector;

/**
 * 表格
 * 
 * @author whx
 * 
 */
public class Sheet<T extends Sheet, OBJ extends Object> implements SheetVarsInf {
	private static final Log LOG = new Log(Sheet.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		GET_ID("取ID号出错！"),
		ROW_OVERFLOW("行号[{0}]超范围[1-{1}]！"),
		COL_OVERFLOW("列号[{0}]超范围[1-{1}]！"),
		CELL_IS_MERGE("有合并的单元格，不能进行此操作！");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static final LocationSizeInf LOCATION_SIZE_DEFAULT = new LocationSize(
			0, 0, 9999, 9999);

	private Vector<SheetRow> _rows = new Vector();  //行对象数组
	private Vector<Float> _heights = new Vector(); // 各行的高度，-1的值表示取剩余空间的平均值
	private Vector<SheetCol> _cols = new Vector(); // 列对象数组
	private Vector<Float> _widths = new Vector();// 各列的宽度，-1的值表示取剩余空间的平均值
	private SheetGap _cellDefaultGap = new SheetGap(DEFAILT_GAP_UP_DOWN,
			DEFAILT_GAP_UP_DOWN, DEFAILT_GAP_LEFT_RIGHT, DEFAILT_GAP_LEFT_RIGHT);

	// 本表在外部对象中的位置与大小，用于计算绝对位置
	private LocationSizeInf _outLocationSize = LOCATION_SIZE_DEFAULT;

	/**
	 * 构建表
	 * @param heights 行高数组
	 * @param widths  列宽数组
	 */
	public Sheet(double[] heights, double[] widths) {
		this(LOCATION_SIZE_DEFAULT, heights, widths);
	}

	/**
	 * 构建单行表
	 * @param height 行高
	 * @param widths 列宽数组
	 */
	public Sheet(double height, double[] widths) {
		this(new double[] { height }, widths);
	}

	/**
	 * 新建内部表
	 * @param outLocationSize 相对于外部表的位置与大小
	 * @param heights 行高数组
	 * @param widths 列宽数组
	 */
	public Sheet(LocationSizeInf outLocationSize, double[] heights,
			double... widths) {
		_outLocationSize = outLocationSize;
		for (int i = 0; i < heights.length; i++) {
			_heights.add((float) heights[i]);
			_rows.add(new SheetRow(heights[i]));
		}
		for (int i = 0; i < widths.length; i++) {
			_widths.add((float) widths[i]);
			_cols.add(new SheetCol(widths[i]));
		}
		compute();
	}

	/**
	 * 取单元格对象
	 * @param row 行
	 * @param col 列
	 * @return
	 */
	public SheetCell cell(int row, int col) { // XXX
		if (col > _cols.size() || col < 1)
			throw LOG.err(Msgs.COL_OVERFLOW, col, _cols.size());
		SheetRow r = row(row);
		if (col > r._cells.size())
			return null;
		return r._cells.get(col);
	}

	public void setValue(int row, int col, OBJ obj) {
		SheetCell cell = cell(row, col);
		if (cell == null) {
			cell = new SheetCell(this, row, col);
			setCell(cell);
		}
		cell.setValue(obj);
	}

	public void setCell(SheetCell cell) {
		// XXX
	}

	public boolean cellIsNull(int row, int col) {
		return cell(row, col) == null;
	}

	/**
	 * 合并的单元格，即合并的多个单元格左上角位置的单元格则返回true
	 */
	public boolean cellIsMerge(int row, int col) {
		SheetCell c = cell(row, col);
		if (c == null || c.rows() == 1 && c.cols() == 1)
			return false;
		return c.col() == col && c.row() == row;
	}

	/**
	 * 被合并的单元格，即合并的多个单元格非左上角位置的单元格则返回true
	 */
	public boolean cellIsMerged(int row, int col) {
		SheetCell c = cell(row, col);
		if (c == null || c.rows() == 1 && c.cols() == 1)
			return false;
		return c.col() != col || c.row() != row;
	}

	/**
	 * 单元格大小为1
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public boolean cellSizeIs11(int row, int col) {
		SheetCell cell = cell(row, col);
		return cell == null || cell.rows() == 1 && cell.cols() == 1;
	}

	/**
	 * 确定单元格的大小为1
	 * 
	 * @param row
	 * @param col
	 */
	public void assertCellSizeIs11(int row, int col) {
		if (cellSizeIs11(row, col))
			return;
		throw LOG.err(Msgs.CELL_IS_MERGE);
	}

	/**
	 * 合并单元格
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 */
	public void merge(int row, int col, int rows, int cols) {
		for (int r = 1; r <= row; r++)
			// 检查单元格没有被合并
			for (int c = 1; c <= col; c++) {
				assertCellSizeIs11(r, c);
			}
		SheetCell cell = cell(row, col);
		cell.set(row, col, rows, cols);
		SheetRow sr;
		for (int r = 1; r <= row; r++) {
			sr = row(r);
			for (int c = 1; c <= col; c++) {
				sr.setCell(c, cell);
			}
		}
	}

	/**
	 * 取所有的单元格
	 * 
	 * @return
	 */
	public ArrayList<SheetCell> getAllCells() {
		ArrayList<SheetCell> list = new ArrayList();
		SheetCell cell;
		for (int row = 0; row < _rows.size(); row++)
			for (int col = 0; col < _cols.size(); col++) {
				cell = cell(row, col);
				if (cell != null && cell.row() == row && cell.col() == col)
					list.add(cell);
			}
		return list;
	}

	/**
	 * 合并取消
	 * 
	 * @param row
	 * @param col
	 */
	public void unMerge(int row, int col) {
		SheetCell cell = cell(row, col);
		cell.set(row, col, 1, 1);
		SheetRow sr;
		for (int r = 1; r <= row; r++) {
			sr = row(r);
			for (int c = 1; c <= col; c++) {
				sr.setCell(c, null);
			}
		}
		row(row).setCell(col, cell);
	}

	/**
	 * 新建内部表格，参数为所占的单元格位置
	 * 
	 * @param row
	 * @param col
	 * @param heights
	 * @param widths
	 * @return
	 */
	public Sheet newInnerSheet(int row, int col, double[] heights,
			double... widths) {
		return newInnerSheet(row, col, 1, 1, heights, widths);
	}

	/**
	 * 新建内部表格，参数为所占的单元格位置
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @param heights
	 * @param widths
	 * @return
	 */
	public Sheet newInnerSheet(int row, int col, int rows, int cols,
			double[] heights, double... widths) {
		return new Sheet(new LocationSizeSheet(this, new RowCol(row, col, rows,
				cols)), heights, widths);
	}

	/**
	 * 重新计划各行的位置与高度（可能有剩余空间平均分配的要求）
	 */
	public void computeRow() {
		Vector<Float> heights = doAvg(_outLocationSize.height(), _heights);// 对-1的值设置为剩余空间的平均值
		float count = 0;
		for (int i = 0; i < heights.size(); i++) {
			_rows.get(i)._height = heights.get(i);
			_rows.get(i)._top = count;
			count += heights.get(i);
		}
	}

	/**
	 * 重新计算各列的位置与高度（可能有剩余空间平均分配的要求）
	 */
	public void computeCol() {
		Vector<Float> widths = doAvg(_outLocationSize.width(), _widths); // 对-1的值设置为剩余空间的平均值
		// System.err.println("width="+_outLocationSize.width()+",avg="+widths.get(1)+"#####\n");
		float count = 0;
		for (int i = 0; i < widths.size(); i++) {
			_cols.get(i)._width = widths.get(i);
			_cols.get(i)._left = count;
			count += widths.get(i);
		}
	}

	/**
	 * 重新计算各行各列的位置与高度（可能有剩余空间平均分配的要求）
	 */
	public void compute() {
		computeCol();
		computeRow();
	}

	/**
	 * 处理平均分配宽度或高度的字段，当值为-1时表时平均分配宽度或高度
	 * 
	 * @param widths
	 * @return
	 */
	private Vector<Float> doAvg(float size, Vector<Float> values) {
		Vector<Float> vs = new Vector();
		int count = 0;
		float all = 0;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == AVG)
				count++;
			else
				all += values.get(i);
		}
		float avg = (size - all) / count;
		for (int i = 0; i < values.size(); i++) {
			if (values.get(i) == AVG)
				vs.add(avg);
			else
				vs.add(values.get(i));
		}
		return vs;
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对当前表左上解的位置)
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public LocationSize locationSize(int row, int col) {
		return locationSize(row, col, 1, 1);
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对当前表左上解的位置)
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @return
	 */
	public LocationSize locationSize(int row, int col, int rows, int cols) {
		return new LocationSize(top(row), left(col), bottom(row + rows - 1)
				- top(row), right(col + cols - 1) - left(col));
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对当前表左上解的位置)
	 * 
	 * @param cellRowCol
	 * @return
	 */
	public LocationSize locationSize(RowColInf cellRowCol) {
		return locationSize(cellRowCol.row(), cellRowCol.col(), cellRowCol.rows(),
				cellRowCol.cols());
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public LocationSize locationSizeAbs(int row, int col) {
		return locationSizeAbs(row, col, 1, 1);
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @return
	 */
	public LocationSize locationSizeAbs(int row, int col, int rows, int cols) {
		return new LocationSize(_outLocationSize.top() + top(row),
				_outLocationSize.left() + left(col), bottom(row + rows - 1) - top(row),
				right(col + cols - 1) - left(col));
	}

	/**
	 * 单元格除四周边框大小外的内容区域的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param cellRowCol
	 * @return
	 */
	public LocationSize locationSizeAbs(RowColInf cellRowCol) {
		return locationSizeAbs(cellRowCol.row(), cellRowCol.col(),
				cellRowCol.rows(), cellRowCol.cols());
	}

	/**
	 * 取单一单元格的位置大小
	 */
	public LocationSize cellLocationSize(int row, int col) {
		return cellLocationSize(row, col, 1, 1);
	}

	/**
	 * 取单元格的位置大小
	 * 
	 * @param row
	 *          行
	 * @param col
	 *          列
	 * @param rows
	 *          行数
	 * @param cols
	 *          列数
	 * @return
	 */
	public LocationSize cellLocationSize(int row, int col, int rows, int cols) {
		return new LocationSize(cellTop(row), cellLeft(col), cellBottom(row + rows
				- 1)
				- cellTop(row), cellRight(col + cols - 1) - cellLeft(col));
	}

	/**
	 * 取单元格的位置大小
	 * 
	 * @param cellRowCol
	 * @return
	 */
	public LocationSize cellLocationSize(RowColInf cellRowCol) {
		return cellLocationSize(cellRowCol.row(), cellRowCol.col(),
				cellRowCol.rows(), cellRowCol.cols());
	}

	/**
	 * 单元格的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param row
	 * @param col
	 * @return
	 */
	public LocationSize cellLocationSizeAbs(int row, int col) {
		return cellLocationSizeAbs(row, col, 1, 1);
	}

	/**
	 * 单元格的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param row
	 * @param col
	 * @param rows
	 * @param cols
	 * @return
	 */
	public LocationSize cellLocationSizeAbs(int row, int col, int rows, int cols) {
		return new LocationSize(_outLocationSize.top() + cellTop(row),
				_outLocationSize.left() + cellLeft(col), cellBottom(row + rows - 1)
						- cellTop(row), cellRight(col + cols - 1) - cellLeft(col));
	}

	/**
	 * 单元格的位置大小(相对最外层表格的绝对位置)
	 * 
	 * @param cellRowCol
	 * @return
	 */
	public LocationSize cellLocationSizeAbs(RowColInf cellRowCol) {
		return cellLocationSizeAbs(cellRowCol.row(), cellRowCol.col(),
				cellRowCol.rows(), cellRowCol.cols());
	}

	/**
	 * 内容区域的宽度
	 * 
	 * @param col
	 * @return
	 */
	public float width(int col) {
		SheetCol c = col(col);
		return c._width - c.getGapLeft() - c.getGapRight();
	}

	/**
	 * 内容区域的高度
	 * 
	 * @param row
	 * @return
	 */
	public float height(int row) {
		SheetRow r = row(row);
		return r._height - r.getGapBottom() - r.getGapTop();
	}

	/**
	 * 内容区域左边距表格最左边的位置
	 * 
	 * @param col
	 * @return
	 */
	public float left(int col) {
		SheetCol c = col(col);
		return c._left + c.getGapLeft();
	}

	/**
	 * 内容区域上部距表格最上边的位置
	 * 
	 * @param row
	 * @return
	 */
	public float top(int row) {
		SheetRow r = row(row);
		return r._top + r.getGapTop();
	}

	/**
	 * 内容区域右边距表格最左边的位置
	 * 
	 * @param col
	 * @return
	 */
	public float right(int col) { // 左下角相对左边的位置
		return left(col) + width(col);
	}

	/**
	 * 内容区域下边距表格最上边的位置
	 * 
	 * @param row
	 * @return
	 */
	public float bottom(int row) {// 左下角相对上边的位置
		return top(row) + height(row);
	}

	/**
	 * 表格总宽度
	 * 
	 * @return
	 */
	public float width() { //
		return cellRight(_cols.size());
	}

	/**
	 * 表格总高度
	 * 
	 * @return
	 */
	public float height() { //
		return cellBottom(_rows.size());
	}

	/**
	 * 设置指定列的宽度
	 * 
	 * @param col
	 * @param width
	 */
	public void setWidth(int col, double width) {
		col(col).setWidth(width);
	}

	/**
	 * 设置指定行的高度
	 * 
	 * @param row
	 * @param height
	 */
	public void setHeight(int row, double height) {
		row(row).setHeight(height);
	}

	/**
	 * 取行对象
	 * 
	 * @param row
	 * @return
	 */
	public SheetRow row(int row) {
		if (row > _rows.size() || row < 1)
			throw LOG.err(Msgs.COL_OVERFLOW, row, _rows.size());
		return _rows.get(row - 1);
	}

	/**
	 * 取列对象
	 * 
	 * @param col
	 * @return
	 */
	public SheetCol col(int col) {
		if (col > _cols.size() || col < 1)
			throw LOG.err(Msgs.COL_OVERFLOW, col, _cols.size());
		return _cols.get(col - 1);
	}

	/**
	 * 取列的宽度
	 * 
	 * @param col
	 * @return
	 */
	public float cellWidth(int col) {
		return col(col)._width;
	}

	/**
	 * 取行的高度
	 * 
	 * @param row
	 * @return
	 */
	public float cellHeight(int row) {
		return row(row)._height;
	}

	/**
	 * 单元格左边距表格最左边的位置
	 * 
	 * @param col
	 * @return
	 */
	public float cellLeft(int col) {
		return col(col)._left;
	}

	/**
	 * 单元格上边距表格最上边的位置
	 * 
	 * @param row
	 * @return
	 */
	public float cellTop(int row) {
		return row(row)._top;
	}

	/**
	 * 单元格右边距表格最左边的位置
	 * 
	 * @param col
	 * @return
	 */
	public float cellRight(int col) { // 左下角相对左边的位置
		return cellLeft(col) + cellWidth(col);
	}

	/**
	 * 单元格下边距表格最上边的位置
	 * 
	 * @param row
	 * @return
	 */
	public float cellBottom(int row) {// 左下角相对上边的位置
		return cellTop(row) + cellHeight(row);
	}

	/**
	 * 取总行数
	 * 
	 * @return
	 */
	public int rowCount() {
		return _rows.size();
	}

	/**
	 * 取总列数
	 * 
	 * @return
	 */
	public int colCount() {
		return _cols.size();
	}

	/**
	 * @return the outLocationSize
	 */
	public LocationSizeInf getOutLocationSize() {
		return _outLocationSize;
	}

	/**
	 * 重置当前表格在外部的位置
	 * 
	 * @param outLocationSize
	 *          the outLocationSize to set
	 */
	public T setOutLocationSize(LocationSizeInf outLocationSize) {
		_outLocationSize = outLocationSize;
		compute();
		return (T) this;
	}

	/**
	 * 重置当前表格在外部的位置
	 * 
	 * @param top
	 * @param left
	 * @param height
	 * @param width
	 * @return
	 */
	public T setOutLocationSize(double top, double left, double height,
			double width) {
		return setOutLocationSize(new LocationSize(top, left, height, width));
	}

	/**
	 * 表格行对象
	 * 
	 * @author whx
	 * 
	 */
	public class SheetRow {
		private float _height; // 行高
		private float _gapTop = NOSET; // 上边间隔
		private float _gapBottom = NOSET; // 下边间隔
		private float _top; // 上部离表上边的距离
		private Vector<SheetCell> _cells = new Vector(); // 单元格

		public SheetRow(double height) {
			_height = (float) height;
		}

		/**
		 * 取行的ID号，从1开始
		 * 
		 * @return
		 */
		public int id() {
			for (int i = 0; i < _rows.size(); i++)
				if (_rows.get(i) == this)
					return i + 1;
			throw LOG.err(Msgs.GET_ID);
		}

		/**
		 * 取指定列的单元格
		 * 
		 * @param col
		 * @return
		 */
		public SheetCell cell(int col) {
			if (col > _cols.size() || col < 1)
				throw LOG.err(Msgs.COL_OVERFLOW, col, _cols.size());
			if (col > _cells.size())
				return null;
			return _cells.get(col);
		}

		/**
		 * 置指定列的单元格
		 * 
		 * @param col
		 * @param cell
		 */
		void setCell(int col, SheetCell cell) {
			cell(col); // 测试col是否有效
			if (col > _cells.size()) {
				for (int i = 0; col == _cells.size() + 1; i++)
					_cells.add(null);
				_cells.add(cell);
				return;
			}
			_cells.set(col - 1, cell);
		}

		/**
		 * 取行的高度
		 * 
		 * @return
		 */
		public float height() {
			return _height;
		}

		/**
		 * 设置行的高度
		 * 
		 * @param height
		 */
		public void setHeight(double height) {
			Sheet.this._heights.set(id() - 1, (float) height);
			Sheet.this.computeRow(); // 重新计算宽度
		}

		/**
		 * 取行上部的空白大小，如未设置，则取表格的默认的参数
		 * 
		 * @return
		 */
		public float getGapTop() {
			if (this._gapTop == NOSET)
				return Sheet.this._cellDefaultGap.top();
			return this._gapTop;
		}

		/**
		 * 判断上部的空白是否未设置
		 * 
		 * @return
		 */
		public boolean isGapTopNoset() {
			return this._gapTop == NOSET;
		}

		/**
		 * 设置行的上部空白
		 * 
		 * @param gapTop
		 */
		public void setGapTop(double gapTop) {
			this._gapTop = (float) gapTop;
		}

		/**
		 * 取下部空白的大小，如没有设置，则取表格对象的缺省值
		 * 
		 * @return
		 */
		public float getGapBottom() {
			if (this._gapBottom == NOSET)
				return Sheet.this._cellDefaultGap.bottom();
			return this._gapBottom;
		}

		/**
		 * 判断下部的空白是否未设置
		 * 
		 * @return
		 */
		public boolean isGapBottomNoset() {
			return this._gapBottom == NOSET;
		}

		/**
		 * 设置行的上部空白
		 * 
		 * @param gapBottom
		 */
		public void setGapBottom(double gapBottom) {
			this._gapBottom = (float) gapBottom;
		}

		/**
		 * 取行上端距表格上部的距离
		 * 
		 * @return
		 */
		public float top() {
			return _top;
		}

		public String toString() {
			return "Height=" + _height + ",Top=" + _top + ",GapTop=" + this._gapTop
					+ ",GapBottom=" + this._gapBottom;
		}
	}

	/**
	 * 列对象
	 * 
	 * @author whx
	 * 
	 */
	public class SheetCol {
		private float _width; // 行高
		private float _gapLeft = NOSET; // 左边间隔
		private float _gapRight = NOSET; // 右边间隔
		private float _left; // 上部离表上边的距离

		public SheetCol(double width) {
			_width = (float) width;
		}

		/**
		 * 取列宽度
		 * 
		 * @return
		 */
		public float width() {
			return _width;
		}

		/**
		 * 取列的ID号
		 * 
		 * @return
		 */
		public int id() {
			for (int i = 0; i < _cols.size(); i++)
				if (_cols.get(i) == this)
					return i + 1;
			throw LOG.err(Msgs.GET_ID);
		}

		/**
		 * 设置宽度，-1表示自动取剩余宽度
		 * 
		 * @param width
		 */
		public void setWidth(double width) {
			Sheet.this._widths.set(id() - 1, (float) width);
			Sheet.this.computeCol(); // 重新计算宽度
		}

		/**
		 * 取左边的空白大小
		 * 
		 * @return
		 */
		public float getGapLeft() {
			if (this._gapLeft == NOSET)
				return Sheet.this._cellDefaultGap.left();
			return this._gapLeft;
		}

		/**
		 * 判断左边的空白是否未设定
		 * 
		 * @return
		 */
		public boolean isGapLeftNoset() {
			return this._gapLeft == NOSET;
		}

		/**
		 * 设置左边空白
		 * 
		 * @param gapLeft
		 */
		public void setGapLeft(double gapLeft) {
			this._gapLeft = (float) gapLeft;
		}

		/**
		 * 取右边的空白大小
		 * 
		 * @return
		 */
		public float getGapRight() {
			if (this._gapRight == NOSET)
				return Sheet.this._cellDefaultGap.right();
			return this._gapRight;
		}

		/**
		 * 判断右边的空白是否未设定
		 * 
		 * @return
		 */
		public boolean isGapRightNoset() {
			return this._gapRight == NOSET;
		}

		/**
		 * 设置右边空白
		 * 
		 * @param gapRight
		 */
		public void setGapRight(double gapRight) {
			this._gapRight = (float) gapRight;
		}

		/**
		 * 取左边距表格左边的距离
		 * 
		 * @return
		 */
		public float left() {
			return _left;
		}

		public String toString() {
			return "Width=" + _width + ",Left=" + _left + ",GapLeft=" + this._gapLeft
					+ ",GapRight=" + this._gapRight;
		}
	}
	public float getCellDefaultGapTop() {
		return _cellDefaultGap.top();
	}

	public void setCellDefaultGapTop(double gapTop) {
		_cellDefaultGap.setTop(gapTop);
		computeRow();
	}

	public float getCellDefaultGapBottom() {
		return _cellDefaultGap.bottom();
	}

	public void setCellDefaultGapBottom(double gapBottom) {
		_cellDefaultGap.setBottom(gapBottom);
		computeRow();
	}

	public float getCellDefaultGapLeft() {
		return _cellDefaultGap.left();
	}

	public void setCellDefaultGapLeft(double gapLeft) {
		_cellDefaultGap.setLeft(gapLeft);
		computeCol();
	}

	public float getCellDefaultGapRight() {
		return _cellDefaultGap.right();
	}

	public void setCellDefaultGapRight(double gapRight) {
		_cellDefaultGap.setRight(gapRight);
		computeCol();
	}
}
