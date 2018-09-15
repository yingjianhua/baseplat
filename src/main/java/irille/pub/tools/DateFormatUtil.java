package irille.pub.tools;

import java.text.SimpleDateFormat;
import java.util.Date;

public class  DateFormatUtil {
	public static Date formatDate(String value) throws Exception{
		Date date = null;
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		SimpleDateFormat dfDash = new SimpleDateFormat("yyyy-MM-dd");
		if(value.length()!=10){ 
			if(value.contains("/")){
				String[] str2 = value.split("/");
				value = str2[0]+"/"+(str2[1].length()==2?str2[1]:("0"+str2[1]))+"/"+(str2[2].length()==2?str2[2]:("0"+str2[2]));
				date = df.parse(value);
			}else if(value.contains("-")){
				String[] str2 = value.split("-");
				value = str2[0]+"-"+(str2[1].length()==2?str2[1]:("0"+str2[1]))+"-"+(str2[2].length()==2?str2[2]:("0"+str2[2]));
				date = dfDash.parse(value);
			}else {
				value = value.substring(0,4)+"-"+value.substring(4, 6)+"-"+value.substring(6, 8);
				date = dfDash.parse(value);
			}
			
		}else {
			if(value.contains("/")){
				date = df.parse(value);
			}else{
				date = dfDash.parse(value);
			}
		}
		return date;
	
	}
	public static void main(String[] args) {
		try {
			System.out.println(formatDate("444444444"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
