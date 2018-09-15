package irille.pub.tools;

import irille.pub.Str;

import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

public class StringConverter extends StrutsTypeConverter {

	public Object convertFromString(Map context, String[] values, Class toClass) {
		int a =0;
		a++;
		if (String.class == toClass && values != null && values.length > 0) {
			String str = values[0];
			if (Str.isEmpty(str))
				return null;
			return str;
		}
		return null;
	}

	@Override
	public String convertToString(Map context, Object o) {
		if (o == null)
			return null;
		if (o instanceof String[]) {
				String[] s = (String[]) o;
				return s[0];
		}
		return o.toString();
	}

}
