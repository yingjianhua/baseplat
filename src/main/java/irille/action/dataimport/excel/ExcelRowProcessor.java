package irille.action.dataimport.excel;

import java.io.IOException;

/**
 * 
* @ClassName: ExcelRowProcessor 
* @Description: 接口，Excel行级处理器
* @author zhangwanxi
* @date 2013-7-22 上午11:05:52 
*
 */
public interface ExcelRowProcessor{
	/**
	 * 
	* @Title: processByRow 
	* @Description: 处理所有的sheet
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @author zhangwanxi 
	* @throws
	 */
	public void processByRow() throws Exception;
	/**
	 * 
	* @Title: processByRow 
	* @Description: 根据sheetIndex处理sheet
	* @param @param sheetIndex
	* @param @throws Exception    设定文件 
	* @return void    返回类型 
	* @author zhangwanxi 
	* @throws
	 */
	public void processByRow(int sheetIndex) throws Exception;
	/**
	 * 
	* @Title: processRow 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @param row    设定文件 
	* @return void    返回类型 
	* @author zhangwanxi 
	* @throws
	 */
	public void processRow(XRow row);
	/**
	 * 
	* @Title: stop 
	* @Description: TODO(这里用一句话描述这个方法的作用) 
	* @param @throws IOException    设定文件 
	* @return void    返回类型 
	* @author zhangwanxi 
	* @throws
	 */
	public void stop() throws IOException;
}
