/**
 * 
 */
package irille.pub.tb;

/**
 * @author surface1
 *
 */
public interface IEnumOpt {
	public EnumLine getLine();
	
	public String name();
	public static interface IEnumBoolean {  //布尔形的接口，1为yes，0为false,对Bean产生方法时有用
	}

}
