package irille.action.rp;

import irille.action.ActionBase;
import irille.gl.rp.RpJournal;

public class RpJournalAction extends ActionBase<RpJournal> {

	public RpJournal getBean() {
		return _bean;
	}

	public void setBean(RpJournal bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		// TODO Auto-generated method stub
		return RpJournal.class;
	}

}
