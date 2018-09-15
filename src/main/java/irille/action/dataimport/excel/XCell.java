package irille.action.dataimport.excel;
/**
 * 
* @ClassName: XCell 
* @Description: Excel单元格对象封装
* @author zhangwanxi
* @date 2013-7-22 上午11:06:10 
*
 */
public class XCell {
	private int rowIndex;
	private int columnIndex;
	private String value;
	
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	public int getColumnIndex() {
		return columnIndex;
	}
	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
