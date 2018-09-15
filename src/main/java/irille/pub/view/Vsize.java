package irille.pub.view;




/**
 * 视图字段的显示大小，listWidth因变动较大，直接在Fld定义
 * 
 * @author whx
 * 
 */
public class Vsize {
	public static final Vsize DEFAULT=new Vsize();
	
	private short _dispHigh; // 显示高度，1-1000表示以行为单位的行数，负数表示以点阵为单位
	private short _dispWidth;// 显示宽度，1-1000表示以英文字符数，负数表示以点阵为单位， XXX 如何考虑
	private short _editHigh; // 显示高度，1-1000表示以行为单位的行数，负数表示以点阵为单位
	private short _editWidth;// 显示宽度，1-1000表示以英文字符数，负数表示以点阵为单位， XXX 如何考虑
	private short _listHigh; // 显示高度，1-1000表示以行为单位的行数，负数表示以点阵为单位

	/**
	 * 构造方法
	 * 
	 * @param view
	 *          视图
	 */
	public Vsize() {
		_dispWidth =_editWidth= (short) getDefaultDispWidth();
		setDefaultHigh();
	}
	
	public Vsize(int dispWidth,int editWidth) {
		_dispWidth=(short)dispWidth;
		_editWidth=(short)editWidth;
		setDefaultHigh();
	}

	public void setDefaultHigh() {
		_dispHigh =_editHigh=_listHigh =  (short) getDefaultDispHigh();
	}
	/**
	 * 取缺省的显示高度
	 * 
	 * @return
	 */
	public int getDefaultDispHigh() {
		return 1; // 默认为1 行高度
	}

	/**
	 * 取缺省的显示宽度
	 * 
	 * @return
	 */
	public int getDefaultDispWidth() {
		return 45; // 默认为30个字符宽度
	}

	
/**
 * 复制新对象，此方法被子类调整
 * @param newObj 新的对象
 * @return 结果
 */
  protected Vsize copy( Vsize newObj) {
  	newObj.setDispSize(_dispWidth, _dispHigh);
  	newObj.setEditSize(_editWidth, _editHigh);
  	newObj.setListHigh(_listHigh);
  	return newObj;
  }
  
	
	/**
	 * 取缺省的编辑高度
	 * 
	 * @return
	 */
	public int getDefaultEditHigh() {
		return getDefaultDispHigh(); // 默认与显示的高度一样
	}

	/**
	 * 取缺省的编辑宽度
	 * 
	 * @return
	 */
	public int getDefaultEditWidth() {
		return getDefaultDispWidth();// 默认与显示的宽度一样
	}

	public Vsize setDispHigh(int dispHigh) {
		_dispHigh = (short) dispHigh;
		return this;
	}

	public Vsize setDispSize(int width, int high) {
		setDispHigh(high);
		setDispWidth(width);
		return this;
	}

	public Vsize setDispAndEditSize(int width, int high) {
		setDispHigh(high);
		setDispWidth(width);
		setEditHigh(high);
		setEditWidth(width);
		return this;
	}

	public Vsize setDispWidth(int width) {
		_dispWidth = (short) width;
		return this;
	}


	public Vsize setEditHigh(int high) {
		_editHigh = (short) high;
		return  this;
	}

	public int getDistHigh() {
		return _dispHigh;
	}

	public int getDispWidth() {
		return _dispWidth;
	}
	public int getEditHigh() {
		return _editHigh;
	}

	public int getEditWidth() {
		return _editWidth;
	}
	public int getListHigh() {
		return _listHigh;
	}

	public Vsize setListHigh(int listHigh) {
		_listHigh = (short) listHigh;
		return this;
	}

	public Vsize setEditSize(int width, int high) {
		setEditHigh(high);
		setEditWidth(width);
		return this;
	}
	public Vsize setEditWidth(int width) {
		_editWidth = (short) width;
		return this;
	}
}
