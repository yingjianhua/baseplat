package irille.action.pya;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.pya.Pya;
import irille.gl.pya.PyaPayOtherBill;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class PyaPayOtherBillAction extends ActionForm<PyaPayOtherBill, PyaPayOtherBill> {

	@Override
	public Class beanClazz() {
		return PyaPayOtherBill.class;
	}

	public PyaPayOtherBill getBean() {
		return _bean;
	}

	public void setBean(PyaPayOtherBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_USER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_DEPT.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_ACCOUNT.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Pya.SubjectAlias.PO_OTHER.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
