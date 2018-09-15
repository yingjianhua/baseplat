package irille.pub.tools;

import java.io.IOException;
import java.util.Properties;

public class ReadPropertiesTools {

	public static Properties getReadPro(String proPerPath) throws IOException{
		java.io.InputStream inputStream = ReadPropertiesTools.class.getClassLoader().getResourceAsStream(proPerPath);  
		Properties p = new Properties();
		p.load(inputStream);
		return p;
	}
}
