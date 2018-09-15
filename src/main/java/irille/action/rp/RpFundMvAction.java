package irille.action.rp;

import irille.action.ActionForm;
import irille.gl.rp.RpFundMv;
import irille.gl.rp.RpFundMvDAO;

public class RpFundMvAction extends ActionForm<RpFundMv,RpFundMv> {

	@Override
	public Class beanClazz() {
		return RpFundMv.class;
	}

	public RpFundMv getBean() {
		return _bean;
	}

	public void setBean(RpFundMv bean) {
		this._bean = bean;
	}
	
	public void doRec() throws Exception {
		RpFundMvDAO.DoRec rec = new RpFundMvDAO.DoRec();
		rec.setBKey(getPkey());
		rec.commit();
		writeSuccess(rec.getB());
	}
	public void unRec() throws Exception {
		RpFundMvDAO.UnRec rec = new RpFundMvDAO.UnRec();
		rec.setBKey(getPkey());
		rec.commit();
		writeSuccess(rec.getB());
	}
}
