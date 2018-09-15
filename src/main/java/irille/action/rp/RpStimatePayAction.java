package irille.action.rp;

import irille.action.ActionBase;
import irille.gl.gl.GlNoteViewRp;
import irille.gl.rp.RpNotePay;
import irille.gl.rp.RpStimatePay;
import irille.gl.rp.RpStimatePayDAO;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class RpStimatePayAction extends ActionBase<RpStimatePay> {
	private GlNoteViewRp _note;
	private RpNotePay _rp;

	@Override
	public Class beanClazz() {
		return RpStimatePay.class;
	}

	public RpStimatePay getBean() {
		return _bean;
	}

	public void setBean(RpStimatePay bean) {
		this._bean = bean;
	}

	public GlNoteViewRp getNote() {
		return _note;
	}

	public void setNote(GlNoteViewRp note) {
		_note = note;
	}

	public RpNotePay getRp() {
		return _rp;
	}

	public void setRp(RpNotePay rp) {
		_rp = rp;
	}
	
	/**
	 * 付款处理
	 * @throws Exception
	 */
	public void doProc() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(RpStimatePayDAO.doProc(getNote(), getRp()), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}
	
	/**
	 * 付款取消处理
	 * @throws Exception
	 */
	public void unProc() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(RpStimatePayDAO.unProc((Long)getPkey()), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

}
