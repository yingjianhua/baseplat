/**
 * 
 */
package irille.pub.print;


/**
 * 表格相关的的类
 * @author whx
 *
 */
public class Sheets implements SheetVarsInf{
	/**
	 * 大小为一行一列的RowCol对象
	 * @author whx
	 *
	 * @param <T>
	 */
	public static class RowCol11<T extends RowCol11> implements RowColInf<T> {
		private int _row, _col;

		public RowCol11(int row, int col) {
			_row = row;
			_col = col;
		}

		/**
		 * 外部程序要复制新对象要调用copyNew方法
		 * 
		 * @param tb
		 * @return
		 */
		public T copyNew() {
			return copy((T) new RowCol11(_row, _col));
		}

		/**
		 * 被子类的New方法调用
		 * 
		 * @param newObj
		 * @return
		 */
		protected T copy(T newObj) {
			return (T) this;
		}

		
		/**
		 * 行数
		 */
		@Override
		public int rows() {
			return 1;
		}

		/**
		 * 列数
		 */
		@Override
		public int cols() {
			return 1;
		}

		/**
		 * 行位置，从1开始
		 */
		@Override
		public int row() {
			return _row;
		}

		/**
		 * 列位置，从1开始
		 */
		@Override
		public int col() {
			return _col;
		}
	}

	/**
	 * 行列对象，包括占用的列数与行数
	 * @author whx
	 *
	 * @param <T>
	 */
	public static class RowCol<T extends RowCol> extends RowCol11<T> {
		private int _rows, _cols;

		public RowCol(int row, int col, int rows, int cols) {
			super(row, col);
			_rows = rows;
			_cols = cols;
		}

		/**
		 * 外部程序要复制新对象要调用copyNew方法
		 * 
		 * @param tb
		 * @return
		 */
		public T copyNew() {
			return copy((T) new RowCol(row(), col(), _rows, _cols));
		}

		@Override
		public int rows() {
			return _rows;
		}

		@Override
		public int cols() {
			return _cols;
		}
	}

	/**
	 * 行列接口 
	 * @author whx
	 *
	 * @param <T>
	 */
	public static interface RowColInf<T extends RowColInf> {
		public int row();

		public int col();

		public int rows();

		public int cols();

		public T copyNew();
	}

	/**
	 * 位置大小对象，源自表格中的单元格区域，从表格中的指定位置大小（行列）计算最终的位置大小
	 * @author whx
	 *
	 * @param <T>
	 */
	public static class LocationSizeSheet<T extends LocationSizeSheet> implements
			LocationSizeInf<T> {
		private Sheet _outSheet;
		private RowColInf _rowCol;

		public LocationSizeSheet(Sheet outSheet, RowColInf rowCol) {
			super();
			_outSheet = outSheet;
			_rowCol = rowCol;
		}

		/**
		 * 外部程序要复制新对象要调用copyNew方法
		 * 
		 * @param tb
		 * @return
		 */
		public T copyNew() {
			return copy((T) new LocationSizeSheet(_outSheet, _rowCol));
		}

		/**
		 * 被子类的New方法调用
		 * 
		 * @param newObj
		 * @return
		 */
		protected T copy(T newObj) {
			return (T) this;
		}

		public float left() {
			return _outSheet.cellLocationSizeAbs(_rowCol).left();
		}

		public float top() {
			return _outSheet.cellLocationSizeAbs(_rowCol).top();
		}

		public float width() {
			return _outSheet.cellLocationSizeAbs(_rowCol).width();
		}

		public float height() {
			return _outSheet.cellLocationSizeAbs(_rowCol).height();
		}

		/**
		 * @return the outSheet
		 */
		public Sheet getOutSheet() {
			return _outSheet;
		}

		/**
		 * @return the rowCol
		 */
		public RowColInf getRowCol() {
			return _rowCol;
		}
	}

	/**
	 * 位置大小
	 * @author whx
	 *
	 * @param <T>
	 */
	public static class LocationSize<T extends LocationSize> implements
			LocationSizeInf<T> {
		private float _left, _top, _width, _height;

		public LocationSize(double top, double left, double height, double width) {
			super();
			_left = (float) left;
			_top = (float) top;
			_width = (float) width;
			_height = (float) height;
		}

		/**
		 * 外部程序要复制新对象要调用copyNew方法
		 * 
		 * @param tb
		 * @return
		 */
		public T copyNew() {
			return copy((T) new LocationSize(_top, _left, _height, _width));
		}

		/**
		 * 被子类的New方法调用
		 * 
		 * @param newObj
		 * @return
		 */
		protected T copy(T newObj) {
			return (T) this;
		}

		public float left() {
			return _left;
		}

		public float top() {
			return _top;
		}

		public float width() {
			return _width;
		}

		public float height() {
			return _height;
		}
	}

	/**
	 * 位置大小接口
	 * @author whx
	 *
	 * @param <T>
	 */
	public static interface LocationSizeInf<T extends LocationSizeInf> {
		public float left();

		public float top();

		public float width();

		public float height();

		public T copyNew();
	}

	/**
	 * 边距间隔类
	 * @author whx
	 *
	 */
	public static class SheetGap {
		private float _top, _bottom, _left, _right;

		public SheetGap(double top, double bottom, double left, double right) {
			super();
			_top = (float) top;
			_bottom = (float) bottom;
			_left = (float) left;
			_right = (float) right;
		}

		public SheetGap(double topBottom, double leftRight) {
			this(topBottom,topBottom,leftRight,leftRight);
		}

		/**
		 * @return the top
		 */
		public float top() {
			return _top;
		}

		/**
		 * @param top
		 *          the top to set
		 */
		public void setTop(double top) {
			_top = (float)top;
		}

		/**
		 * @return the bottom
		 */
		public float bottom() {
			return _bottom;
		}

		/**
		 * @param bottom
		 *          the bottom to set
		 */
		public void setBottom(double bottom) {
			_bottom = (float)bottom;
		}

		/**
		 * @return the left
		 */
		public float left() {
			return _left;
		}

		/**
		 * @param left
		 *          the left to set
		 */
		public void setLeft(double left) {
			_left = (float)left;
		}

		/**
		 * @return the right
		 */
		public float right() {
			return _right;
		}

		/**
		 * @param right
		 *          the right to set
		 */
		public void setRight(double right) {
			_right = (float)right;
		}
		public boolean isLeftNoset() {
			return this._left == NOSET;
		}
		public boolean isRightNoset() {
			return this._right == NOSET;
		}
		public boolean isTopNoset() {
			return this._top == NOSET;
		}
		public boolean isBottomNoset() {
			return this._bottom == NOSET;
		}

	}
}
