/**
 * 
 */
package irille.pub.print;


/**
 * 大小类
 * @author whx
 *
 */
public class Size<T extends Size> {
	private float _width,_height;
	
	public Size ( double height,double width) {
		_width=(float)width;
		_height=(float)height;
	}
	
	public T copyNew() {
		return copy((T) new Size(_width,_height));
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
	 * 宽度
	 * @return the width
	 */
	public float width() {
		return _width;
	}


	/**
	 * 高度
	 * @return the height
	 */
	public float height() {
		return _height;
	}
}
