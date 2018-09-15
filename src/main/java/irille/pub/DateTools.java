package irille.pub;

import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.ConversionException;

public class DateTools {
	private static final Log LOG = new Log(DateTools.class);
	public static final long FIRST_TIME = new Date(2011, 01, 01).getTime(); // 从2011年1月1日起计算
	protected static char[] hexChar = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String getDigest(String instr) {
		try {
			MessageDigest _md = MessageDigest.getInstance("MD5");
			_md.reset();
			byte[] in = instr.getBytes();
			_md.update(in);
			byte[] out = _md.digest();
			return toHexString(out);
		} catch (Exception e) {
			throw LOG.err(e, "incode", "MD5加密出错");
		}
	}

	public static String toHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(hexChar[(b[i] & 0xf0) >>> 4]);
			sb.append(hexChar[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String toMonth(int year, int month) {
		String year1 = "0000" + Integer.toString(year);
		year1 = year1.substring(year1.length() - 4, year1.length());
		String mon1 = "00" + Integer.toString(month);
		mon1 = mon1.substring(mon1.length() - 2, mon1.length());
		return year1 + mon1;
	}
	
	//判断是否为月末
	public static boolean isLastDayOfMonth(Date date) { 
    Calendar calendar = Calendar.getInstance(); 
    calendar.setTime(date); 
    calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1)); 
    if (calendar.get(Calendar.DAY_OF_MONTH) == 1) { 
        return true; 
    } 
    return false; 
	} 
	
	public static Date getFirstDayOfMonth(Date date) { 
    Calendar calendar = Calendar.getInstance(); 
    calendar.setTime(date); 
    calendar.set(Calendar.DATE, 1);
    return calendar.getTime();
	} 

	public static Date addDate(Date date, int delta) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, delta);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int delta) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, delta);
		return cal.getTime();
	}

	public static String toMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		return toMonth(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1);
	}

	public static String toMonthShort(Date date) {
		return toMonth(date).substring(2);
	}

	public static String monthAdd(String month, int delta) {
		try {
			int year = Integer.parseInt(month.substring(0, 4));
			int mon = Integer.parseInt(month.substring(4, 6));
			if (mon < 1 || mon > 12)
				throw LOG.err("errMonth", "月份不合法!");
			Calendar cal = Calendar.getInstance();
			cal.set(year, mon - 1, 1);
			cal.add(Calendar.MONTH, delta);
			year = cal.get(Calendar.YEAR);
			mon = cal.get(Calendar.MONTH) + 1;
			return toMonth(year, mon);
		} catch (Exception e) {
			throw LOG.err("errMonth", "年月加出错!");
		}
	}

	public static void main(String[] args) {
		System.err.println(getOrderValue());
	}

	/**
	 * 取按时间排序字段的Order值，返回的值大于0
	 * 
	 * @return
	 */
	public static final int getOrderValue() {
		return (int) (Integer.MAX_VALUE - (System.currentTimeMillis() - FIRST_TIME) / 2000);
	}

	/**
	 * 取按时间排序字段的Order值，返回的值大于0
	 * 
	 * @param date
	 *          一般为数据的建档时间或更新时间
	 * @return
	 */
	public static final int getOrderValue(Date date) {
		return (int) (Integer.MAX_VALUE - (date.getTime() - FIRST_TIME) / 2000);
	}

	/**
	 * 取按时间排序字段锁定时的Order值，返回的值小于0
	 * 
	 * @return
	 */
	public static final int getOrderLockValue() {
		return (int) -(System.currentTimeMillis() - FIRST_TIME) / 2000;
	}

	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";

	public static String today2String() {
		return date2String(new Date(), DEFAULT_DATE_PATTERN);
	}

	/**
	 * 日期转换为字符串。<br/>
	 * 
	 * @param date
	 *          日期
	 * @param pattern
	 *          转换格式
	 * 
	 * @throws String
	 */
	public static String date2String(Date date, String pattern) {
		return (new SimpleDateFormat(pattern)).format(date);
	}

	/**
	 * 字符串转换为日期。<br/>
	 * 
	 * @param source
	 *          日期字符串
	 * @param pattern
	 *          转换格式字符串
	 * 
	 * @return Date
	 * @throws ParseException
	 */
	public static Date string2Date(String source, String pattern) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
			dateFormat.setLenient(false);
			return dateFormat.parse(source);
		} catch (ParseException e) {
			throw new ConversionException("日期转换错误");
		}
	}

	/**
	 * 字符串转日期 YYYY/MM/DD或YYYY-MM-DD
	 * @param source
	 * @return
	 */
	public static Date string2Date(String source) throws Exception {
		SimpleDateFormat dateFormat;
		if (source.indexOf("/") > -1)
			dateFormat = new SimpleDateFormat("yyyy/MM/dd");
		else
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		dateFormat.setLenient(false);
		return dateFormat.parse(source);
	}

	//比较年月日是否一致
	public static boolean equalsYMD(Date date1, Date date2) {
		if (date1 == null || date2 == null)
			return false;
		if (date1.getYear() != date2.getYear())
			return false;
		if (date1.getMonth() != date2.getMonth())
			return false;
		if (date1.getDate() != date2.getDate())
			return false;
		return true;
	}
}
