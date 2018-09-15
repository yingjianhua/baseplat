package irille.action.dataimport.util;


/**
 * 
* @ClassName: StringUtil 
* @Description: 字符串工具类
* @author zhangwanxi
* @date 2013-7-22 上午11:09:56 
*
 */
public class StringUtil {
	public static String getStringValue(Object obj){
		if(obj==null){
			return "";
		}
		return obj.toString();
	}
	
	public static int getStringLength(String str){
		if(str==null){
			return 0;
		}
		return str.getBytes().length;
	}
	
	public static boolean hasValue(Object o){
		if(o==null||o.toString().trim().equals("")){
			return false;
		}
		return true;
	}
	
	public static boolean isNumber(String str){
		String reg="^[-|+]?\\d*([.]\\d+)?$";
		return str.matches(reg);
	}
	
	public static void main(String[] args) {
		String str="0123";
		System.out.println(isNumber(str));
	}
}
