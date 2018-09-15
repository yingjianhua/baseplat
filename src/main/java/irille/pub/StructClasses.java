//Created on 2005-9-17
package irille.pub;

import java.io.Serializable;

/**
 * Title: 代码与名称的结构类<br>
 * Description: Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public class StructClasses implements Serializable {
	private static final long serialVersionUID = 1L;

	public static class XY {
		private int _x;
		private int _y;

		public XY(int x, int y) {
			super();
			_x = x;
			_y = y;
		}

		public int getX() {
			return _x;
		}

		public int getY() {
			return _y;
		}
	}

	public static class Size {
		private int _width;
		private int _height;

		public Size(int width, int height) {
			super();
			_width = width;
			_height = height;
		}

		public final int getWidth() {
			return _width;
		}

		public final int getHeight() {
			return _height;
		}
	}
}
