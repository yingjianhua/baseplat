package irille.pub;


/**
 * 代码与名称对象
 * @author whx
 *
 */
public class PubInfs {
	public static interface IMsg {
		public String getMsg();
		public String name();
	}
	
	public static interface IRegMsg extends IMsg {
		public String getReg();
	}
	
}
