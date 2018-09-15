//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.tb.Infs.IFld;

import java.math.BigDecimal;
import java.text.MessageFormat;

/**
 * Title: 数据校验器基类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * 
 * @version 1.0
 */
public abstract class VBase<T> {
	private static final Log LOG = new Log(VBase.class);
	/**
	 * 检查数据的有效性
	 * @param fld
	 * @param value
	 */
	public abstract void valid(IFld fld,Object value);

	/**
	 * 外部程序要复制新对象要调用copyNew方法
	 * 
	 * @param tb
	 * @return
	 */
	public abstract T copyNew();

	/**
	 * 被了类的copyNew方法调用 
	 * @param newObj
	 * @return
	 */
	protected T copy(VBase newObj) {
		return (T) newObj;
	}

	/**
	 * 如错提示信息
	 * 
	 * @return
	 */
	public abstract String help();

	/**
	 * 返回前台JS代码
	 * 
	 * @return
	 */
	public abstract String getJS();
	
	/**
	 * 转化为BigDecimal
	 * @param value
	 * @return
	 */
	public static BigDecimal toDec(Object value) {
		return new BigDecimal(value.toString());
	}

	/**
	 * 出错
	 * @param fld 字段
	 * @param value 值
	 * @return
	 */
	public String errString(IFld fld,Object value) {
		return new MessageFormat("数据无效! 表[{0}]字段[{1}]的值[{2}], 合法条件：{3}。 ").format(
		    fld.getTb().getName(), fld.getName(), value.toString(), help())
		    .toString();
	}
}
