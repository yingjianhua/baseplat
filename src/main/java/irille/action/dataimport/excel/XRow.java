package irille.action.dataimport.excel;

import java.util.ArrayList;
import java.util.List;
/**
 * 
* @ClassName: XRow 
* @Description: Excel行对象封装
* @author zhangwanxi
* @date 2013-7-22 上午11:06:24 
*
 */
public class XRow {
	private int rowIndex;
	private List<XCell> cells=new ArrayList<XCell>();
	
	public int getRowIndex() {
		return rowIndex;
	}
	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}
	//获取列大小
	public int getCellsSize() {
		return cells.size();
	}
	public XRow addCell(XCell cell){
		this.cells.add(cell);
		return this;
	}
	public XCell getCell(int cellIndex){
		return cells.get(cellIndex);
	}
}
