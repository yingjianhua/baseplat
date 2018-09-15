package irille.action.dataimport.exception;
/**
 * 
* @ClassName: DataImportException 
* @Description: 数据导入异常类定义，各种细节异常继承此类
* @author zhangwanxi
* @date 2013-7-22 上午11:08:09 
*
 */
public class DataImportException extends Exception{
	private static final long serialVersionUID = 1L;
	public DataImportException(String message){
		super(message);
	}
}
