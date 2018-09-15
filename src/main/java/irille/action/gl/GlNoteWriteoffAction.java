package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlNoteWriteoff;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduUnapprove;

public class GlNoteWriteoffAction extends ActionBase<GlNoteWriteoff> {
	public GlNoteWriteoff getBean() {
		return _bean;
	}

	public void setBean(GlNoteWriteoff bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlNoteWriteoff.class;
	}

	public void approve() throws Exception {
		IduApprove act = newApprove();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess();
	}

	public void unapprove() throws Exception {
		IduUnapprove act = newUnapprove();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess();
	}
}
