package irille.action.gs;

import irille.action.ActionBase;
import irille.core.sys.SysCell;
import irille.core.sys.SysOrg;
import irille.gl.gs.GsPrice;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.inf.IExtName;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsPriceAction extends ActionBase<GsPrice> {
	public GsPrice getBean() {
		return _bean;
	}

	public void setBean(GsPrice bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsPrice.class;
	}

	public void getPriceNameCombo() throws JSONException, IOException {
		if (getPkey() == null)
			return;
		GsPrice price = BeanBase.load(GsPrice.class, getPkey());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONArray ja = new JSONArray();
		for (int i = 1; i < 13; i++) {
			if (!Str.isEmpty(price.gtNamePrice(i))) {
				JSONObject json = new JSONObject();
				json.put("value", String.valueOf(i));
				json.put("text", price.gtNamePrice(i));
				ja.put(json);
			}
		}
		response.getWriter().print(ja.toString());
	}

	public void getPriceNames() throws IOException {
		if (getPkey() == null)
			return;
		String names = "";
		GsPrice price = BeanBase.load(GsPrice.class, getPkey());
		String cell ="*" + price.getCell() + "##" + price.gtCell().getName();
		int rangeType = price.getRangeType();
		String range;
		if (rangeType > 10) {
			range = "*" + rangeType + "*" + price.getRangePkey() + "##" + ((IExtName) price.gtRange()).getExtName();
		} else {
			range = "*" + rangeType;
		}
		for (int i = 1; i < 13; i++) {
			if (!Str.isEmpty(price.gtNamePrice(i)))
				names += price.gtNamePrice(i) + "-";
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(names + cell + range);
	}

	@Override
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		// TODO Auto-generated method stub
		GsPrice price = (GsPrice) bean;
		if (price.getRangePkey() != null)
			json.put(pref + "rangePkey", price.getRangePkey() + BEAN_SPLIT +((IExtName)price.gtRange()).getExtName());
		return json;
	}
}
