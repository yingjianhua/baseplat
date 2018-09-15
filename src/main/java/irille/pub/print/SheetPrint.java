/**
 * 
 */
package irille.pub.print;

import irille.pub.print.Sheets.LocationSize;
import irille.pub.print.Sheets.LocationSizeInf;
import irille.pub.print.Sheets.SheetGap;

/**
 * 打印页面的表格类
 * 
 * @author whx
 * 
 */
public class SheetPrint<T extends SheetPrint> {
	public static final Size A4_VERTICAL = new Size(29.7, 21.0);
	public static final Size A5_VERTICAL = new Size(21.3, 13.95);
	public static final Size A3_VERTICAL = new Size(42.0, 29.7);
	public static final Size B4_VERTICAL = new Size(36.4, 25.7);
	public static final Size K8_VERTICAL = new Size(36.8, 26.0);
	public static final Size K16_VERTICAL = new Size(26.0, 18.4);
	public static final Size A4_HORIZONTAL = new Size(21, 29.7);
	public static final Size A5_HORIZONTAL = new Size(13.95, 21.3);
	public static final Size A3_HORIZONTAL = new Size(29.7, 42.0);
	public static final Size B4_HORIZONTAL = new Size(25.7, 36.4);
	public static final Size K8_HORIZONTAL = new Size(26.0, 36.8);
	public static final Size K16_HORIZONTAL = new Size(18.4, 26.0);

	public static final SheetGap GAP254_191 = new SheetGap(2.54, 1.91);
	public static final SheetGap GAP_DEFAULT = GAP254_191;

	private boolean _oriention = true; //纸张是否横向
	private Size _pageSize = A4_VERTICAL; // 纸张大小
	private SheetGap _pageGap = GAP_DEFAULT; // 边距, 页眉页脚之间的关系参照WPS表格
	private Sheet _pageHeader = null;
	private Sheet _pageBody = null;
	private Sheet _pageFooter = null;
	private Sheet _reportBody = null;
	private Sheet _reportHeader = null;
	private Sheet _reportFooter = null;
	private Sheet _groupHeader = null;
	private Sheet _groupFooter = null;

	public T setPageHeader(double[] heights, double... widths) {
		_pageHeader = new Sheet(pageOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setPageBody(double[] heights, double... widths) {
		_pageBody = new Sheet(pageOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setPageFooter(double[] heights, double... widths) {
		_pageFooter = new Sheet(pageOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setReportBody(double[] heights, double... widths) {
		_reportBody = new Sheet(reportOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setReportHeader(double[] heights, double... widths) {
		_reportHeader = new Sheet(reportOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setReportFooter(double[] heights, double... widths) {
		_reportFooter = new Sheet(reportOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setGroupHeader(double[] heights, double... widths) {
		_groupHeader = new Sheet(reportOutLocationSize(), heights, widths);
		return (T) this;
	}

	public T setGroupFooter(double[] heights, double... widths) {
		_groupFooter = new Sheet(reportOutLocationSize(), heights, widths);
		return (T) this;
	}

	public LocationSizeInf pageOutLocationSize() {
		return new LocationSize(0, 0, printHeight(), printWidth());
	}

	public LocationSizeInf reportOutLocationSize() {
		return new LocationSize(0, 0, 9999, printWidth());
	}

	public float printWidth() {
		return _pageSize.width() - _pageGap.left() - _pageGap.right();
	}

	public float printHeight() {
		return _pageSize.height() - _pageGap.top() - _pageGap.bottom();
	}

	public boolean isOriention() {
		return _oriention;
	}

	public void setOriention(boolean oriention) {
		this._oriention = oriention;
	}

	/**
	 * @return the pageSize
	 */
	public Size getPageSize() {
		return _pageSize;
	}

	/**
	 * @param pageSize the pageSize to set
	 */
	public T setPageSize(Size pageSize) {
		_pageSize = pageSize;
		//TODO 以下方法暂时无效
		//		_pageHeader.setOutLocationSize(pageOutLocationSize());
		//		_pageBody.setOutLocationSize(pageOutLocationSize());
		//		_pageFooter.setOutLocationSize(pageOutLocationSize());
		//		_reportHeader.setOutLocationSize(reportOutLocationSize());
		//		_reportFooter.setOutLocationSize(reportOutLocationSize());
		//		_reportBody.setOutLocationSize(reportOutLocationSize());
		//		_groupHeader.setOutLocationSize(reportOutLocationSize());
		//		_groupFooter.setOutLocationSize(reportOutLocationSize());
		return (T) this;
	}

	/**
	 * @return the pageGap
	 */
	public SheetGap getPageGap() {
		return _pageGap;
	}

	public void setPageGap(SheetGap pageGap) {
		_pageGap = pageGap;
	}

	/**
	 * @return the pageHead
	 */
	public Sheet getPageHeader() {
		return _pageHeader;
	}

	/**
	 * @return the pageBody
	 */
	public Sheet getPageBody() {
		return _pageBody;
	}

	/**
	 * @return the pageFoot
	 */
	public Sheet getPageFooter() {
		return _pageFooter;
	}

	/**
	 * @return the reportBody
	 */
	public Sheet getReportBody() {
		return _reportBody;
	}

	/**
	 * @return the reportHead
	 */
	public Sheet getReportHeader() {
		return _reportHeader;
	}

	/**
	 * @return the reportFoot
	 */
	public Sheet getReportFooter() {
		return _reportFooter;
	}

	/**
	 * @return the groupHead
	 */
	public Sheet getGroupHeader() {
		return _groupHeader;
	}

	/**
	 * @return the groupFoot
	 */
	public Sheet getGroupFooter() {
		return _groupFooter;
	}
}
