package irille.gl.gl;

import irille.pub.Log;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;

public class GlEntryDefDAO {
	public static final Log LOG = new Log(GlEntryDefDAO.class);

	public static class Ins extends IduIns<Ins, GlEntryDef> {
		
		@Override
		public void before() {
		  super.before();
		}

	}

	public static class Del extends IduDel<Del, GlEntryDef> {

		@Override
		public void before() {
			super.before();
			delLineTid(getB(), GlEntryDefLine.class);
		}

	}

}
