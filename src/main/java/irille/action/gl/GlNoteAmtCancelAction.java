package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlNoteAmtCancel;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduUnapprove;

public class GlNoteAmtCancelAction extends ActionBase<GlNoteAmtCancel> {
	public GlNoteAmtCancel getBean() {
		return _bean;
	}

	public void setBean(GlNoteAmtCancel bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlNoteAmtCancel.class;
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
