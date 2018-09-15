package irille.action.rp;

import irille.action.ActionLineTid;
import irille.gl.rp.RpJournalLine;

public class RpJournalLineAction extends ActionLineTid<RpJournalLine> {

	public RpJournalLine getBean() {
		return _bean;
	}

	public void setBean(RpJournalLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return RpJournalLine.class;
	}
	
}
