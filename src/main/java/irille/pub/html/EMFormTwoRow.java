package irille.pub.html;

import irille.pub.tb.Tb;
import irille.pub.view.VFlds;


/**
 * 属性类的基类
 * 
 * @author whx
 * 
 */
public class EMFormTwoRow<T extends EMFormTwoRow> extends EMForm<T> {
	private int _width = 275;
	private int _column = 2;
	
	/**
	 * @param tb
	 */
	public EMFormTwoRow(Tb tb,VFlds... vflds) {
		super(tb,vflds);
	}

	/* (non-Javadoc)
	 * @see irille.pub.html.EMForm#setFieldDefaultsProperies(irille.pub.html.ExtList)
	 */
	@Override
	public void setFieldDefaultsProperies(ExtList v) {
		v.add(LABEL_WIDTH,getWidthLabel()).add(WIDTH,getWidth())
		.add(LABEL_STYLE,"font-weight : bold");
	}
	/* (non-Javadoc)
	 * @see irille.pub.html.EMForm#setLayoutProperies(irille.pub.html.ExtList)
	 */
	@Override
	public void setLayoutProperies(ExtList v) {
		v.add(TYPE, "table").add(COLUMNS, getColumn())
		.add(ITEM_CLS,"x-layout-table-items-form");
	}
	public int getWidth() {
		return _width;
	}
	public void setWidth(int width) {
		_width = width;
	}
	public int getColumn() {
		return _column;
	}
	public void setColumn(int column) {
		_column = column;
	}
	
	
}
