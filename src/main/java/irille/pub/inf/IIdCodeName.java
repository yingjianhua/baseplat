package irille.pub.inf;

/**
 * 对象Bean的基类
 * @author Maxwell Wu
 *
 */
public interface IIdCodeName {
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");

	/**
	 * 取ID号
	 * @return
	 */
	public Long getId();
	
	/**
	 *取代码
	 * @return
	 */
	public String getCode();
	/**
	 * 取名称
	 * @return
	 */
	public String getName();

}
