package irille.action.rp;

import irille.action.ActionForm;
import irille.gl.rp.RpNotePayBill;
import irille.pub.bean.IForm;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduUnapprove;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class RpNotePayBillAction extends ActionForm<RpNotePayBill,RpNotePayBill>{
	@Override
	public Class beanClazz() {
	  return RpNotePayBill.class;
	}
	
	public RpNotePayBill getBean() {
		return _bean;
	}

	public void setBean(RpNotePayBill bean) {
		this._bean = bean;
	}
	
/*	public void approve() throws Exception {
		IduApprove act = newApprove();
		act.setBKey(getPkey());
		act.commit();
		
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(act.getB(), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
		
	}
	public void unapprove() throws Exception {
		IduUnapprove act = newUnapprove();
		act.setBKey(getPkey());
		act.commit();
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(act.getB(), "bean.");
		json.put("success", true);
		response.getWriter().print(json.toString());
	}*/
}
