package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.pya.Pya;
import irille.gl.pya.PyaPayBill;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class PyaPayBillAction extends ActionForm<PyaPayBill, PyaPayBill> {

	@Override
	public Class beanClazz() {
		return PyaPayBill.class;
	}

	public PyaPayBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_SUPPLIER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_OTHER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PA_CELL.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
