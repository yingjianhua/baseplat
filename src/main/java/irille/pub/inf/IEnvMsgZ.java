package irille.pub.inf;

import java.security.Timestamp;
import java.sql.Time;
import java.util.Date;
import java.util.Hashtable;

/**
 * 环境信息接口
 * @author Maxwell Wu
 *
 */
public interface IEnvMsgZ {
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	
	/**
	 * 取工作目录
	 * @return
	 */
	public String getWorkDir();
	/**
	 * 取临时目录
	 * @return
	 */
	public String getTmpDir();
	/**
	 * 系统 中间数据目录
	 * @return
	 */
	public String getMiddleFileRoot();
	/**
	 * 取CSS小图片文件合并的目录
	 * @return
	 */
	public String getCssSprateDir();
	/**
	 * 取系统用户
	 * @return
	 */
	public String getSystemUser();
	/**
	 * 取用户目录
	 * @return
	 */
	public String getUserHome();

	/**
	 * 取客户端IP
	 * 
	 * @return
	 */
	public String getIp();

	/**
	 * 取客户端操作系统
	 * 
	 * @return
	 */
	public String getSystem();

	/**
	 * 取数据库
	 * 
	 * @return
	 */
	public IDb getDB();

	/**
	 * 取客户端主机名称
	 * 
	 * @return
	 */
	public String getMachineName();
}
