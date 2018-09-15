//Created on 2005-10-22
package irille.pub.valid;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.tb.Fld;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


/**
 * Title: String校验器<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class ValidDate extends Valid {
	private static final Log LOG = new Log(ValidDate.class);
	public static final ValidDate DEFAULT = new ValidDate(null);
	public static final long TIMES_OF_DAY = 1000 * 60 * 60 * 24;
	public static final int DAY = Calendar.DAY_OF_MONTH;
	public static final int MONTH = Calendar.MONTH;
	public static final int YEAR = Calendar.YEAR;
	private SimpleDateFormat _format;

	/**
	 * 构造方法
	 * @param format 格式
	 */
	public ValidDate(SimpleDateFormat format) {
		_format = format;
	}

	/**
	 * 数据转换
	 * @param clazz 目标类
	 * @param value 值
	 * @return 转换后的值
	 */
	public Object tran(Class clazz, Object value) {
		if (value == null)
			return null;
		Date date;
		if (value instanceof Date)
			date = (Date) value;
		else {
			String val = value.toString().trim();
			if (val.equals(""))
				return null;
			try {
				date = _format.parse(val.toString().trim());
			} catch (Exception e) {
				throw LOG.err("dateInvalid", "值[{0}]不是有效的日期格式，正确的格式为[{1}]", val.toString().trim(),
				    _format.toPattern());
			}
		}
		if (clazz.equals(Timestamp.class))
			return new Timestamp(date.getTime());
		if (clazz.equals(java.sql.Date.class))
			return new java.sql.Date(date.getTime());
		if (clazz.equals(Date.class))
			return new Date(date.getTime());
		throw LOG.err("tranDate", "日期转换出错");
	}

	/**
	 * 数据有效性检查
	 * @param field 字段
	 * @param value 值
	 */
	public void check(Fld field, Object value) {
		checkNullFlag(field, value);
	}

	/**
	 * 日期是否相等
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 结果
	 */
	public static boolean equals(Date date1, Date date2) {
		return DEFAULT.equals((Object) date1, (Object) date2);
	}

	/**
	 * 日期是否相等
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return 结果
	 */
	public boolean equals(Object date1, Object date2) {
		if (date1 == date2)
			return true;
		if (date1 == null || date2 == null)
			return false;
		return compare(date1, date2) == 0;
	}

	/**
	 * 比较日期大小, null值最小
	 * @param date1 日期1
	 * @param date2 日期2
	 * @return date1>date2时返回1，date1==date2时返回0，date1<date2时返回-1
	 */
	public int compare(Object date1, Object date2) {
		if (date1 == null) {
			if (date2 == null)
				return 0;
			return 1;
		}
		if (date2 == null)
			return -1;
		if (date2 instanceof String)
			date2 = Str.toDate(date2.toString());
		Calendar calendar1 = Calendar.getInstance();
		Calendar calendar2 = Calendar.getInstance();
		calendar1.setTime((Date) date1);
		calendar2.setTime((Date) date2);
		int i1 = calendar1.get(Calendar.YEAR) * 10000 + calendar1.get(Calendar.MONTH) * 100
		    + calendar1.get(Calendar.DAY_OF_MONTH);
		int i2 = calendar2.get(Calendar.YEAR) * 10000 + calendar2.get(Calendar.MONTH) * 100
		    + calendar2.get(Calendar.DAY_OF_MONTH);
		if (i1 > i2)
			return 1;
		return i1 == i2 ? 0 : -1;
	}

	public static Date add(Date secDate, int type, int amount) {
		Calendar c = getCalendar(secDate);
		c.add(type, amount);
		return c.getTime();
	}

	public static long subDate(Date date, Date dateSec, int type) {
		if (type == DAY)
			return (date.getTime() - dateSec.getTime()) / TIMES_OF_DAY;
		if (type == MONTH)
			return (date.getYear() - dateSec.getYear()) * 12 + date.getMonth() - dateSec.getMonth();
		if (type == YEAR)
			return date.getYear() - dateSec.getYear();
		throw LOG.err("noType", "类型错误");
	}

	public static long subDate(Date date) {
		return subDate(date, new Date(), DAY);
	}

	public static long subDate(Date date, int type) {
		return subDate(date, new Date(), type);
	}

	public static Calendar getCalendar(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c;
	}

	/**
	 * 将时间整理为年月日，清除时分秒为0
	 * @param date 日期
	 * @return 结果
	 */
	public static Date trimTo000000(Date date) {
		date.setHours(0);
		date.setMinutes(0);
		date.setSeconds(0);
		return date;
	}

	/**
	 * 将时间整理为年月日，时间为23:59:59
	 * @param date 日期
	 * @return 结果
	 */
	public static Date trimTo235959(Date date) {
		date.setHours(23);
		date.setMinutes(59);
		date.setSeconds(59);
		return date;
	}
	// public static final Cn ERR_COMP_TIMESTAMP_IS_NULL = new Cn(TIMESTAMP.class,
	// "err.compTimestampIsNull|比较的时间不可以为[null]"); //Cn
	// //允许比较允许偏差5秒
	// public static final int TIME_DIFF = 5 * 1000;
	//
	// /**
	// * 时间是否相等
	// * @param timpstamp1 时间1
	// * @param timestamp2 时间2
	// * @return 结果
	// */
	// public static boolean equals(Timestamp timpstamp1, Timestamp timestamp2) {
	// if (timpstamp1 == timestamp2)
	// return true;
	// if (timpstamp1 == null || timestamp2 == null)
	// return false;
	// return compare(timpstamp1, timestamp2) == 0;
	// }
	//
	// /**
	// * 比较日期大小
	// * @param timpstamp1 时间1
	// * @param timestamp2 时间2
	// * @return
	// timpstamp1>timestamp2时返回1，timpstamp1==timestamp2时返回0，timpstamp1<timestamp2时返回-1
	// */
	// public static int compare(Timestamp timpstamp1, Timestamp timestamp2) {
	// if (timpstamp1 == null || timestamp2 == null)
	// throw new Exp(ERR_COMP_TIMESTAMP_IS_NULL);
	// long timeDiff = timpstamp1.getTime() - timestamp2.getTime();
	// if (timeDiff <= TIME_DIFF && TIME_DIFF >= -TIME_DIFF)
	// return 0;
	// return timeDiff >= 0 ? 1 : -1;
	// }
}
