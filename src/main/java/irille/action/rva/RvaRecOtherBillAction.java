package irille.action.rva;

import irille.action.ActionForm;
import irille.gl.gl.GlSubjectMapDAO;
import irille.gl.pya.Pya;
import irille.gl.rva.Rva;
import irille.gl.rva.RvaRecOtherBill;
import irille.gl.rva.RvaRecOtherBillDAO;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class RvaRecOtherBillAction extends ActionForm<RvaRecOtherBill, RvaRecOtherBill> {

	@Override
	public Class beanClazz() {
		return RvaRecOtherBill.class;
	}

	public RvaRecOtherBill getBean() {
		return _bean;
	}

	public void setBean(RvaRecOtherBill bean) {
		this._bean = bean;
	}
	public static void getSubjects() throws Exception{
		StringBuilder subjects = new StringBuilder();
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_USER.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_INNER_CELL.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_DEPT.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_ACCOUNT.getCode()).getSubject());
		subjects.append(","+GlSubjectMapDAO.getByAlias(Rva.SubjectAlias.RO_OTHER.getCode()).getSubject());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print("subject in ("+subjects.toString().substring(1)+")");
	}
}
