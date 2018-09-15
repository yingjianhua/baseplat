package irille.action.dataimport.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * 
* @ClassName: DateUtil 
* @Description: 日期工具类
* @author zhangwanxi
* @date 2013-7-22 上午11:09:32 
*
 */
public class DateUtil {
	public static Date parse(String date,String format){
		DateFormat df=new SimpleDateFormat(format);
		Date d=null;
		try {
			d= df.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return d;
	}
	
	public static Date parse(String date){
		return parse(date,"yy-M-d H:m:s");
	}
	
	public static String format(Date date,String format){
		DateFormat df=new SimpleDateFormat(format);
		return df.format(date);
	}
	
	public static String format(Date date){
		return format(date, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static String format(Timestamp stamp,String format){
		DateFormat df=new SimpleDateFormat(format);
		return df.format(stamp);
	}
	
	public static String format(Timestamp stamp){
		return format(stamp, "yyyy-MM-dd HH:mm:ss");
	}
	
	public static Date stampToDate(Timestamp stamp){
		return parse(format(stamp));
	}
	
	public static String getCurrentDate(){
		return format(new Date(),"yyyy-MM-dd");
	}
	
	public static String getCurrentDateTime(){
		return format(new Date(),"yyyy-MM-dd HH:mm:ss");
	}
	
	public static void main(String[] args) {
		String dd="08/3/4 12:3:5";
		Date d=parse(dd,"yy/M/d H:m:s");
		System.out.println(format(d));
	}
}
