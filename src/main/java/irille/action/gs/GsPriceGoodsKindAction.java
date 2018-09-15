package irille.action.gs;

import irille.action.ActionBase;
import irille.core.sys.Sys;
import irille.gl.gs.GsPrice;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsKind;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.inf.IExtName;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsPriceGoodsKindAction extends ActionBase<GsPriceGoodsKind> {
	public GsPriceGoodsKind getBean() {
		return _bean;
	}

	public void setBean(GsPriceGoodsKind bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsPriceGoodsKind.class;
	}

	public void getPriceNamesAndRates() throws IOException {
		if (getPkey() == null)
			return;
		String names = "";
		String rates = "";
		BigDecimal hundred = new BigDecimal(100);
		GsPriceGoodsKind kind = BeanBase.load(GsPriceGoodsKind.class, getPkey());
		GsPrice price = kind.gtPrice();
		for (int i = 1; i < 13; i++) {
			if (!Str.isEmpty(price.gtNamePrice(i))) {
				names += price.gtNamePrice(i) + "##";
				rates += kind.gtRate(i) == null ? BigDecimal.ZERO : kind.gtRate(i).add(hundred)
				    .divide(hundred, GsPriceGoodsKind.T.RATE1.getFld().getScale(), BigDecimal.ROUND_HALF_UP)
				    + "##";
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(names.substring(0, names.length() - 2) + "||" + rates.substring(0, rates.length() - 2));
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		GsPriceGoodsKind price = (GsPriceGoodsKind) bean;
		/*if (Str.isEmpty(pref) == false)
			return json;*/
		if (price.getRangePkey() != null)
			json.put(pref+"rangePkey", price.getRangePkey() + BEAN_SPLIT + ((IExtName) price.gtRange()).getExtName());
		return json;
	}
}
