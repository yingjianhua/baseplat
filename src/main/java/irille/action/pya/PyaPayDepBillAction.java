package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.pya.Pya;
import irille.gl.pya.PyaPayDepBill;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class PyaPayDepBillAction extends ActionForm<PyaPayDepBill, PyaPayDepBill> {

	@Override
	public Class beanClazz() {
		return PyaPayDepBill.class;
	}

	public PyaPayDepBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayDepBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PD_SUPPLIER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PD_OTHER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PD_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PD_CELL.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
