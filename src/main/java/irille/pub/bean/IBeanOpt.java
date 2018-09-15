//Created on 2005-9-19
package irille.pub.bean;


/**
 * Title: 表格的选项接口，即表格可以类似选项选择<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public interface IBeanOpt  {

	/**
	 * 选项的值，一般为主键
	 * @return 结果
	 */
	public abstract Object iOptValue();
	
	/**
	 * 选项显示的内容，一般为：code+":"+name
	 * @return 结果
	 */
	public abstract Object iOptName();
	
}
