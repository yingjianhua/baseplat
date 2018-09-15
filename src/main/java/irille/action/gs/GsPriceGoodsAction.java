package irille.action.gs;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPrice;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsKind;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

public class GsPriceGoodsAction extends ActionBase<GsPriceGoods> {
	public GsPriceGoods getBean() {
		return _bean;
	}

	public void setBean(GsPriceGoods bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsPriceGoods.class;
	}

	public void getGCPNR() throws IOException {
		if (getPkey() == null)
			return;
		String prices = "";
		String names = "";
		String rates = "";
		BigDecimal hundred = new BigDecimal(100);
		GsPriceGoods pg = BeanBase.load(GsPriceGoods.class, getPkey());
		GsPriceGoodsKind kind = pg.gtPriceKind();
		GsPrice price = kind.gtPrice();
		for (int i = 1; i < 13; i++) {
			if (!Str.isEmpty(price.gtNamePrice(i))) {
				prices += pg.gtPrice(i) + "##";
				names += price.gtNamePrice(i) + "##";
				rates += kind.gtRate(i) == null ? BigDecimal.ZERO : kind.gtRate(i).add(hundred).divide(hundred, 2, RoundingMode.HALF_UP) + "##";
			}
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(pg.getGoods() + "##" + pg.gtGoods().getCode() + "||" + pg.getPriceCost() + "||" + prices.substring(0, prices.length() - 2) + "||" + names.substring(0, names.length() - 2) + "||" + rates.substring(0, rates.length() - 2));
	}

	@Override
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if ("price".equals(fldName)) {
				sql += " AND " + "price_kind in (select pkey from `gs_price_goods_kind` where price = " + param + ")";
				continue;
			}
			if ("diy".equals(fldName)) {
				sql += " AND " + param;
				continue;
			}
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		//sql += crtOutWhereSearch(ja, tb, GsStock.T.GOODS.getFld(), GsGoods.TB, "goods", GsGoods.T.NAME.getFld(), GsGoods.T.SPEC.getFld());
		return crtFilterAll() + sql + orderBy();
	}
	
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsPriceGoods) bean).gtGoods();
		String gcode = GsPriceGoods.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		if (pref.isEmpty()) {
			json.put("uom", goods.getUom() + BEAN_SPLIT + goods.gtUom().getName());
		} else {
			json.put("bean.uom", goods.getUom() + BEAN_SPLIT + goods.gtUom().getName());
		}
		return json;
	}
}
