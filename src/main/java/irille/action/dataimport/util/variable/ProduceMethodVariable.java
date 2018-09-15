package irille.action.dataimport.util.variable;

import java.util.ArrayList;
import java.util.List;
/**
 * 列规则中，生成方法字段，静态字典的作用
* @ClassName: ProduceMethodVariable 
* @Description: 列规则中，生成方法字段，静态字典的作用
* @author zhangwanxi
* @date 2013-7-22 上午11:09:12 
*
 */
public class ProduceMethodVariable {
	public static final String IMPORT_DATA="import_data";
	public static final String CUSTOM_METHOD="custom_method";
	public static final String DEFAULT_VALUE="default_value";
	
	public static List getProduceMethods(){
		List list=new ArrayList();
		VariableItem item=new VariableItem();
		item.setName(getName(IMPORT_DATA));
		item.setValue(IMPORT_DATA);
		list.add(item);
		item=new VariableItem();
		item.setName(getName(CUSTOM_METHOD));
		item.setValue(CUSTOM_METHOD);
		list.add(item);
		item=new VariableItem();
		item.setName(getName(DEFAULT_VALUE));
		item.setValue(DEFAULT_VALUE);
		list.add(item);
		return list;
	}
	
	public static String getName(String value){
		if(IMPORT_DATA.equals(value)){
			return "导入数据";
		}else if(CUSTOM_METHOD.equals(value)){
			return "自定义策略";
		}else if(DEFAULT_VALUE.equals(value)){
			return "默认值";
		}
		return "";
	}
	
	public static String getValue(String name){
		if(getName(IMPORT_DATA).equals(name)){
			return IMPORT_DATA;
		}else if(getName(CUSTOM_METHOD).equals(name)){
			return CUSTOM_METHOD;
		}else if(getName(DEFAULT_VALUE).equals(name)){
			return DEFAULT_VALUE;
		}
		return "";
	}
}
