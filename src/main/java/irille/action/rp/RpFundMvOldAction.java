package irille.action.rp;

import irille.action.ActionForm;
import irille.gl.rp.RpFundMvOld;
import irille.gl.rp.RpFundMvOldDAO;
import irille.gl.rp.RpFundMvOldDAO.Rec;

public class RpFundMvOldAction extends ActionForm<RpFundMvOld,RpFundMvOld> {

	@Override
	public Class beanClazz() {
		return RpFundMvOld.class;
	}

	public RpFundMvOld getBean() {
		return _bean;
	}

	public void setBean(RpFundMvOld bean) {
		this._bean = bean;
	}
	
	public void rec() throws Exception {
		RpFundMvOldDAO.Rec rec = new Rec();
		rec.setBKey(getPkey());
		rec.commit();
		writeSuccess(rec.getB());
	}

}
