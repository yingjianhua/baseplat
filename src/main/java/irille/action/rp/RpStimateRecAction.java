package irille.action.rp;

import irille.action.ActionBase;
import irille.gl.gl.GlNoteViewRp;
import irille.gl.rp.RpNoteRpt;
import irille.gl.rp.RpStimateRec;
import irille.gl.rp.RpStimateRecDAO;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class RpStimateRecAction extends ActionBase<RpStimateRec> {
	private GlNoteViewRp _note;
	private RpNoteRpt _rp;

	@Override
	public Class beanClazz() {
		return RpStimateRec.class;
	}

	public RpStimateRec getBean() {
		return _bean;
	}

	public void setBean(RpStimateRec bean) {
		this._bean = bean;
	}

	public GlNoteViewRp getNote() {
		return _note;
	}

	public void setNote(GlNoteViewRp note) {
		_note = note;
	}

	public RpNoteRpt getRp() {
		return _rp;
	}

	public void setRp(RpNoteRpt rp) {
		_rp = rp;
	}

	/**
	 * 付款处理
	 * @throws Exception
	 */
	public void doProc() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(RpStimateRecDAO.doProc(getNote(), getRp()), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	/**
	 * 付款取消处理
	 * @throws Exception
	 */
	public void unProc() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(RpStimateRecDAO.unProc((Long) getPkey()), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

}
