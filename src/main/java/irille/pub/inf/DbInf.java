package irille.pub.inf;

import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

/**
 * 数据信息接口 XXX 方法还需完善
 * @author Maxwell Wu
 * 
 */
public interface DbInf {

	/**
	 * 建SQL查询条件
	 * @param fld
	 * @param param 前EXT返回的查询值
	 * @return
	 */
	public String crtWhereSearch(Fld fld, String param);
	
	/**
	 * 取新增时，PreparedStatement需要设置的第1个下标
	 * 主要注意ORCAL时，自增型也是需要从主键开始设置
	 * @param tb
	 * @return
	 */
	public int getInsFirstFldId(Tb tb);
	
	/**
	 * 向预处理对象中设置值时，不同数据库对类型要求不一致
	 * @param fld
	 * @param obj
	 * @return
	 */
	public Object toSqlObject(Fld fld, Object obj);
	
	public boolean isOracle();
	
	/**
	 * 取数据库配置文件
	 * @return
	 */
	public String getConfigFile();
	
	/**
	 * 取名称
	 * @return
	 */
	public String getName();

	/**
	 * 取字符串左边若干字符
	 * 
	 * @param fld 字段
	 * @param num 字符数
	 * @return
	 */
	public String left(String fld, int num);

	/**
	 * 取字符串右边若干字符
	 * 
	 * @param fld
	 *          字段
	 * @param num
	 *          字符数
	 * @return
	 */
	public String right(String fld, int num);

	/**
	 * 取字符串的子串
	 * 
	 * @param fld
	 *          字段
	 * @param post
	 *          位置
	 * @param num
	 *          字符数
	 * @return
	 */
	public String substr(String fld, int post, int num);

	/**
	 * 取主键
	 * 及其他特定的方法
	 */
}
