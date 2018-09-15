package irille.pub.tools;

import irille.pub.Str;

import java.math.BigDecimal;
import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class DecConverter extends StrutsTypeConverter {
	
	public Object convertFromString(Map context, String[] values, Class toClass) {
		BigDecimal bd = null;
		if (BigDecimal.class == toClass && values != null && values.length > 0) {
			String bdStr = values[0];
			if (Str.isNum(bdStr) == false)
				return null;
			bd = new BigDecimal(bdStr);
			return bd;
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if(o instanceof String[]) {
			if (((String[])o)[0].equals("")) return null;
			return ((String[])o)[0];
		} else if(o instanceof String){
			if(((String)o).equals("")) return null;
			return (String)o;
		}
		return (String)o;
	}

}
