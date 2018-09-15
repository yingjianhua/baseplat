package irille.pub.tools;

/**
 *重写了一下 jdk1.5提供的UUID工具类，自动转为32位的uuid
 * 
 * @author  qinyaoyun
 * @version v1.0
 */
public class UUIDGeneratorTools {
	public static String randomUUID(){
		return java.util.UUID.randomUUID().toString().replace("-", "");
	}
}
