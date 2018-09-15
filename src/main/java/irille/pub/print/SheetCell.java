/**
 * 
 */
package irille.pub.print;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.print.Sheets.RowColInf;
import irille.pub.print.Sheets.SheetGap;

/**
 * 表格单元
 * 
 * @author whx
 * 
 */
public class SheetCell<OBJ extends Object> implements RowColInf {
	private static final Log LOG = new Log(SheetCell.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		msg("");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static final float NOSET = Sheet.NOSET; // 未设置

	private Sheet _sheet;
	private int _row, _col, _rows, _cols;
	private SheetGap _gap = null;
	private OBJ _obj=null;

	public SheetCell(Sheet sheet, int row, int col, int rows, int cols) {
		_sheet = sheet;
		set(row,col,rows,cols);
	}

	public SheetCell(Sheet sheet, int row, int col) {
		this(sheet, row, col, 1, 1);
	}

	
	
	void set(int row, int col, int rows, int cols) {
		_row = row;
		_col = col;
		_rows = rows;
		_cols = cols;		
	}
	
	@Override
	public int row() {
		return _row;
	}

	@Override
	public int col() {
		return _col;
	}

	@Override
	public int rows() {
		return _rows;
	}

	@Override
	public int cols() {
		return _cols;
	}

	@Override
	public RowColInf copyNew() {
		throw LOG.err();
	}

	/**
	 * @return the top
	 */
	public float getGapTop() {
		if (_gap == null || _gap.top() != NOSET)
			return _gap.top();
		return _sheet.row(_row).getGapTop();
	}

	public SheetCell setGap(double top, double bottom, double left, double right) {
		_gap = new SheetGap(top, bottom, left, right);
		return this;
	}

	public SheetGap getGap(){
		return _gap;
	}
	
	public SheetCell setGapTop(double top) {
		if (_gap == null)
			_gap = new SheetGap(top, NOSET, NOSET, NOSET);
		else
			_gap.setTop(top);
		return this;
	}

	public float getGapBottom() {
		if (_gap == null || _gap.bottom() != NOSET)
			return _gap.bottom();
		return _sheet.row(_row + _rows - 1).getGapBottom();
	}

	public SheetCell setGapBottom(double bottom) {
		if (_gap == null)
			_gap = new SheetGap(NOSET,bottom, NOSET, NOSET);
		else
		_gap.setBottom(bottom);
		return this;
	}

	public float getGapLeft() {
		if (_gap == null || _gap.left() != NOSET)
			return _gap.left();
		return _sheet.col(_col).getGapLeft();
	}

	public SheetCell setGapLeft(double left) {
		if (_gap == null)
			_gap = new SheetGap(NOSET, NOSET,left, NOSET);
		else
		_gap.setLeft(left);
		return this;
	}

	public float getGapRight() {
		if (_gap == null || _gap.right() != NOSET)
			return _gap.right();
		return _sheet.col(_col + _cols - 1).getGapRight();
	}

	public SheetCell setGapRight(double right) {
		if (_gap == null)
			_gap = new SheetGap(NOSET, NOSET, NOSET,right);
		else
		_gap.setRight(right);
		return this;
	}

	/**
	 * @return the obj
	 */
	public OBJ getObj() {
		return _obj;
	}

	/**
	 * @param obj the obj to set
	 */
	public void setValue(OBJ obj) {
		_obj = obj;
	}

}
