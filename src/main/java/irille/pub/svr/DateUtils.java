/*
 *  DateUtils.java
 *  
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Library General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *  
 *  Author: Winter Lau (javayou@gmail.com)
 *  http://dlog4j.sourceforge.net
 */
package irille.pub.svr;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期相关的工具类
 * 
 * @author Winter Lau
 */
public class DateUtils extends org.apache.commons.lang3.time.DateUtils {
	/**
	 * 合并日期和时间
	 * 
	 * @param date
	 * @param time
	 * @return
	 */
	public static Calendar mergeDateTime(Date date, Time time) {
		Calendar cal = Calendar.getInstance();
		if (date != null)
			cal.setTime(date);
		if (time != null) {
			Calendar temp = Calendar.getInstance();
			temp.setTime(time);
			cal.set(Calendar.HOUR_OF_DAY, temp.get(Calendar.HOUR_OF_DAY));
			cal.set(Calendar.MINUTE, temp.get(Calendar.MINUTE));
			cal.set(Calendar.SECOND, temp.get(Calendar.SECOND));
			cal.set(Calendar.MILLISECOND, temp.get(Calendar.MILLISECOND));
		}
		return cal;
	}

	/**
	 * 返回两个日期相差的天数
	 * 
	 * @param d1
	 * @param d2
	 * @return
	 */
	public static int diff_in_date(Date d1, Date d2) {
		return (int) Math.abs(((d1.getTime() - d2.getTime()) / 86400000));
	}

	public static int subMonth(Date d1, Date d2) {
		int suby = d2.getYear() - d1.getYear();
		int rst = (suby + 1) * 12;
		rst = rst - d1.getMonth();
		rst = rst - (11 - d2.getMonth());
		return rst;
	}

	/**
	 * 获取某天开始的那一刻
	 * 
	 * @param year
	 * @param month
	 * @param date
	 * @return
	 */
	public static Calendar getDateBegin(int year, int month, int date) {
		Calendar begin_time = Calendar.getInstance();
		begin_time.set(Calendar.YEAR, year);
		begin_time.set(Calendar.MONTH, month - 1);
		begin_time.set(Calendar.DATE, date);
		begin_time.set(Calendar.HOUR_OF_DAY, 0);
		begin_time.set(Calendar.MINUTE, 0);
		begin_time.set(Calendar.SECOND, 0);
		begin_time.set(Calendar.MILLISECOND, 0);
		return begin_time;
	}

	/**
	 * 清除日历的时间字段
	 * 
	 * @param cal
	 */
	public static void resetTime(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
	}

	/**
	 * "yyyy-mm-dd形式转化为date类型"
	 * 
	 * @param str
	 * @return
	 */
	public static Date stringToDate(String str) {
		String[] arr = str.split("-");
		Date date = new Date(Integer.parseInt(arr[0]) - 1900, Integer.parseInt(arr[1]) - 1,
		    Integer.parseInt(arr[2]));
		return date;
	}

	public static Date getDateSub(Date date, int sub) {
		return new Date(date.getYear(), date.getMonth(), date.getDate() - sub);
	}

	// 取当月第一天
	public static Date getMonthOne(Date date) {
		return new Date(date.getYear(), date.getMonth(), 1);
	}

	// 取前一个月
	public static Date getMonthBefore(Date date) {
		return new Date(date.getYear(), date.getMonth() - 1, date.getDate());
	}
	/**
	 * 将时间戳转化为Date类型
	 * @param timeMillis
	 * @return
	 * @throws ParseException
	 */
	public static Date timeMillisToDate(Long timeMillis) throws ParseException {
	    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss"); 
	    String sd = format.format(new Date(timeMillis)); 
	    Date date = format.parse(sd);
	    return date;
	}
	/**
	 * 将Date类型转化为时间戳
	 * @param date
	 * @return
	 */
	public static Long dateToTimeMillis(Date date) {
		return date.getTime();
	}

}
