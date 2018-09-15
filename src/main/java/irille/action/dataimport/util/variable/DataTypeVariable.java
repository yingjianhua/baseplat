package irille.action.dataimport.util.variable;

import java.util.ArrayList;
import java.util.List;
/**
 * 
* @ClassName: DataTypeVariable 
* @Description: 数据类型，静态字典功能
* @author zhangwanxi
* @date 2013-7-22 上午11:08:55 
*
 */
public class DataTypeVariable {
	public static final String STRING="string";
	public static final String NUMBER="number";
	public static final String DATE="date";
	
	public static List getDataTypes(){
		List list=new ArrayList();
		VariableItem item=new VariableItem();
		item.setName(getName(STRING));
		item.setValue(STRING);
		list.add(item);
		item=new VariableItem();
		item.setName(getName(NUMBER));
		item.setValue(NUMBER);
		list.add(item);
		item=new VariableItem();
		item.setName(getName(DATE));
		item.setValue(DATE);
		list.add(item);
		return list;
	}
	
	public static String getName(String value){
		if(STRING.equals(value)){
			return "字符";
		}else if(NUMBER.equals(value)){
			return "数字";
		}else if(DATE.equals(value)){
			return "日期时间";
		}
		return "";
	}
	
	public static String getValue(String name){
		if(getName(STRING).equals(name)){
			return STRING;
		}else if(getName(NUMBER).equals(name)){
			return NUMBER;
		}else if(getName(DATE).equals(name)){
			return DATE;
		}
		return "";
	}
}
