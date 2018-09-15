//Created on 2005-9-27
package irille.core.sys;

import irille.pub.Log;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduUpd;

public class SysCtypeCodeDAO {
	public static final Log LOG = new Log(SysCtypeCodeDAO.class);

	public static class Upd extends IduUpd<Upd, SysCtypeCode> {
		@Override
		public void log() {
		}
	}

	public static class Ins extends IduIns<Ins, SysCtypeCode> {
		@Override
		public void log() {
		}
	}
	public static class Del extends IduDel<Del, SysCtypeCode> {
		@Override
		public void log() {
		}
	}
}