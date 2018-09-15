package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoodsKind;
import irille.pub.Str;
import irille.pub.bean.BeanBase;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class GsGoodsKindAction extends ActionBase<GsGoodsKind> {
	public GsGoodsKind getBean() {
		return _bean;
	}

	public void setBean(GsGoodsKind bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsGoodsKind.class;
	}
	
	@Override
	public String orderBy() {
	  return " ORDER BY " + GsGoodsKind.T.CODE.getFld().getCodeSqlField();
	}

	public void loadCust() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (Str.isEmpty(getSarg1()) == false) {
			GsGoodsKind kind = BeanBase.load(GsGoodsKind.class, Integer.parseInt(getSarg1().split(BEAN_SPLIT)[0]));
			if (kind != null) {
				json.put("cust1", kind.getCust1());
				json.put("cust2", kind.getCust2());
				json.put("cust3", kind.getCust3());
				json.put("cust4", kind.getCust4());
				json.put("cust5", kind.getCust5());
			}
		}
		json.put("success", true);
		response.getWriter().print(json.toString());
	}
}
