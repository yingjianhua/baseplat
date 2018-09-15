//Created on 2005-10-22
package irille.pub.view;

import irille.pub.Log;
import irille.pub.tb.Infs.IFld;

import java.io.Serializable;


/**
 * Title: 输出格式处理<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public abstract class FmtBase<T extends FmtBase> implements Serializable {
	private static final Log LOG = new Log(FmtBase.class);
	public static final byte ALIGN_LEFT=1;
	public static final byte ALIGN_CENTER=2;
	public static final byte ALIGN_RIGHT=3;
	private byte _align=ALIGN_LEFT;//左右对齐

	public static final char[] UPPER_NUMBERS = new char[] { '零', '壹', '贰', '叁',
			'肆', '伍', '陆', '柒', '捌', '玖' };
	public static final char[] LOWER_NUMBERS = new char[] { 'O', '一', '二', '三',
			'四', '五', '六', '七', '八', '九' };

/**
 * 输出信息
 * @param fld
 * @param value
 * @return
 */
	public String out(IFld fld,Object value) {
		return value.toString();
	}
	/**
	 * 输出HTML代码
	 * @param fld
	 * @param value
	 * @return
	 */
	public String getHtml(IFld fld,Object value) {
		return out(fld,value);
	}

	public T copyNew() {
		return (T)this;
	}
	
	public byte getAlign() {
  	return _align;
  }

	protected T setAlign(byte align) {
  	_align = align;
  	return (T)this;
  }
}
