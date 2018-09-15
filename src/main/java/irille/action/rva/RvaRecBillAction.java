package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.rva.Rva;
import irille.gl.rva.RvaRecBill;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class RvaRecBillAction extends ActionForm<RvaRecBill, RvaRecBill> {

	@Override
	public Class beanClazz() {
		return RvaRecBill.class;
	}

	public RvaRecBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RA_CUST.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RA_OTHER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RA_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RA_CELL.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
