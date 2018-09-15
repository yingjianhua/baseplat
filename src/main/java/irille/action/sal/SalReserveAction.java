package irille.action.sal;

import irille.action.ActionForm;
import irille.pss.sal.SalReserve;
import irille.pss.sal.SalReserveDAO;
import irille.pss.sal.SalReserveLine;
import irille.pss.sal.SalReserveDAO.DoClose;
import irille.pub.bean.Bean;
import irille.pub.bean.IForm;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduUnapprove;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class SalReserveAction extends ActionForm<SalReserve, SalReserveLine> {

	@Override
	public Class beanClazz() {
		return SalReserve.class;
	}
	
	public SalReserve getBean() {
		return _bean;
	}

	public void setBean(SalReserve bean) {
		this._bean = bean;
	}
	
	public List<SalReserveLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SalReserveLine> listLine) {
		_listLine = listLine;
	}

	public void doClose() throws Exception {
		DoClose act = new SalReserveDAO.DoClose();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}
}
