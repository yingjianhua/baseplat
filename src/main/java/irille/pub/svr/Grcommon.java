package irille.pub.svr;

public class Grcommon {
//判断是否包含特殊字符
	public static boolean HasSpecialChar(String text)
	{
	    if (text == null) 
	        return false;
	    
	    boolean ret = false;     
	    int len = text.length();
	    for (int i = 0; i < len; ++i)
	    {
	        char c = text.charAt(i);
	        if (c == '&' ||  c == '<' || c == '>' || c == '"')
	        {
	            ret = true;
	            break;
	        }
	    }
	    
	    return ret;
	}

	//对数据中的特殊字符进行编码
	public static String HTMLEncode(String text)
	{
	    int len = text.length();
	    StringBuffer results = new StringBuffer(len + 20);
	    char[] orig = text.toCharArray();
	    
	    int beg = 0;
	    for (int i = 0; i < len; ++i)
	    {
	        char c = text.charAt(i);
	        switch (c) 
	        {
	            case '&':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&amp;");
	                break;
	            case '<':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&lt;");
	                break;
	            case '>':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&gt;");
	                break;
	            case '"':
	                if (i > beg) 
	                    results.append(orig, beg, i - beg);
	                beg = i + 1;
	                results.append("&quot;");
	                break;
	        }
	    }
	    
	    results.append(orig, beg, len - beg);
	    
	    return results.toString();
	}
}
