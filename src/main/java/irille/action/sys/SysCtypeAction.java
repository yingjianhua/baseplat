package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.core.sys.SysCtypeDAO;
import irille.pub.svr.OptCustCtrl;
import irille.pub.tb.OptCust;
import irille.pub.tb.Infs.IOptLine;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class SysCtypeAction extends ActionBase<SysCtype> {
	public List<SysCtypeCode> _listLine;
	public String _ctype;

	@Override
	public Class beanClazz() {
		return SysCtype.class;
	}

	public SysCtype getBean() {
		return _bean;
	}

	public void setBean(SysCtype bean) {
		this._bean = bean;
	}

	public List<SysCtypeCode> getListLine() {
		return _listLine;
	}

	public void setListLine(List<SysCtypeCode> listLine) {
		_listLine = listLine;
	}

	public String getCtype() {
		return _ctype;
	}

	public void setCtype(String ctype) {
		_ctype = ctype;
	}

	@Override
	public SysCtype updRun() throws Exception {
		SysCtypeDAO.Upd act = new SysCtypeDAO.Upd();
		act.setB(getBean());
		act.setLines(getListLine());
		act.commit();
		return act.getB();
	}

	public void getCombo() throws Exception {
		OptCust cust = OptCustCtrl.getInstance().get(getCtype());
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		IOptLine[] lines = cust.getLines();
		JSONObject lineJson;
		for (IOptLine line : lines) {
			lineJson = new JSONObject();
			lineJson.put("value", line.getKey());
			lineJson.put("text", line.getName());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		ServletActionContext.getResponse().getWriter().print(json.toString());
	}

	//查询ctype所有数据及所有对应的字表数据,此方法页面初始化是数据加入到js缓存
	public void ctypeNode() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(new SysCtypeDAO().ctypeNode().toString());
	}
}
