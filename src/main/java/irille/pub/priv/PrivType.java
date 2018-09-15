/**
 * 
 */
package irille.pub.priv;

import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.priv.Privs.PrivUserMsgInf;

/**
 * @author whx
 *
 */
public abstract class PrivType<OBJ extends Bean> {
	private static final Log LOG = new Log(PrivType.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		msg("");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on


	public abstract boolean isOk(OBJ obj); //有是否这权限
	
	public PrivUserMsgInf getPrivUserMsg() {
		return null;
	}
	
//	public void assertOk(OBJ obj) {
//		if(isOk(obj))
//			return;
//		throw assertErr();
//	}
//	
//	public abstract Exp assertErr();
}
