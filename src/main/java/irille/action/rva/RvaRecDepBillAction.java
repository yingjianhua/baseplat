package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.rva.Rva;
import irille.gl.rva.RvaRecDepBill;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class RvaRecDepBillAction extends ActionForm<RvaRecDepBill, RvaRecDepBill> {

	@Override
	public Class beanClazz() {
		return RvaRecDepBill.class;
	}

	public RvaRecDepBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecDepBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_CUST.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_OTHER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RD_CELL.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
