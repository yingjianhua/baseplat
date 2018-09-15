/**
 * 
 */
package irille.pub;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

/**
 * 此接口用于引用本类中的常用变量！
 * 
 * @author surface1
 * 
 */
public interface IPubVars {
	public static final String STR0 = "";
	public static final String LN = "\r\n";
	public static final String TAB = "	";
	public static final String LINE = "========================================================="
			+ LN;
	public static final String ONE="one";
	public static final String TWO="tow"; 
	public static final BigDecimal ZERO = BigDecimal.ZERO;
	public static final BigDecimal HUNDRED = new BigDecimal(100);
	public static final BigDecimal MAX_DECIMAL = new BigDecimal(
			9999999999999999.0);
	public static final Boolean TRUE = Boolean.TRUE;
	public static final Boolean FALSE = Boolean.FALSE;
	public static final Long LONG0 = new Long(0);
	public static final Integer INT0 = new Integer(0);
	public static final long TIMES_OF_DAY = 1000 * 60 * 60 * 24;
	public static final int ROUND = BigDecimal.ROUND_HALF_UP;
	public static final String FILE_SEPARATOR = System
			.getProperty("file.separator");// 文件目录之间的分隔符

	public static final SimpleDateFormat FORMAT_TIME = new SimpleDateFormat(
			"yyyyMMddHHmmss");
	public static final SimpleDateFormat FORMAT_DATE = new SimpleDateFormat(
			"yyyyMMdd");
}
