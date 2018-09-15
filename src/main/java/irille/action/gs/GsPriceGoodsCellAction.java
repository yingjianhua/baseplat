package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsPriceGoods;
import irille.gl.gs.GsPriceGoodsCell;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsPriceGoodsCellAction extends ActionBase<GsPriceGoodsCell> {
	private Integer _cell;
	private Integer _priceKind;
	
	public GsPriceGoodsCell getBean() {
		return _bean;
	}

	public void setBean(GsPriceGoodsCell bean) {
		this._bean = bean;
	}

	public Integer getCell() {
		return _cell;
	}

	public void setCell(Integer cell) {
		this._cell = cell;
	}

	public Integer getPriceKind() {
		return _priceKind;
	}

	public void setPriceKind(Integer priceKind) {
		this._priceKind = priceKind;
	}

	@Override
	public Class beanClazz() {
		return GsPriceGoodsCell.class;
	}
	
//	public void listSelectedPriceGoods() throws Exception {
//		String result = "";
//		String where = GsPriceGoodsCell.T.CELL.getFld().getCodeSqlField() + " = ? AND " 
//				+ GsPriceGoodsCell.T.PRICE_GOODS.getFld().getCodeSqlField() 
//				+ " IN (SELECT " + GsPriceGoods.T.PKEY.getFld().getCodeSqlField() + " FROM " 
//				+ GsPriceGoods.TB.getCodeSqlTb() + " WHERE " 
//				+ GsPriceGoods.T.PRICE_KIND.getFld().getCodeSqlField() + " = ?)";
//		List<GsPriceGoodsCell> list = BeanBase.list(GsPriceGoodsCell.class, where, true, getCell(), getPriceKind());
//		JSONObject lineJson = null;
//		for (GsPriceGoodsCell line : list) {
//			result += line.getPriceGoods() + BEAN_SPLIT;
//		}
//		HttpServletResponse response = ServletActionContext.getResponse();
//		if(!"".equals(result))
//		response.getWriter().print(result.substring(0, result.length()-2));
//	}
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
			if ("price".equals(fldName)){
				sql += " AND " + "price_goods in (SELECT pkey FROM gs_price_goods WHERE price_kind IN (SELECT pkey FROM `gs_price_goods_kind` WHERE price = " + param + " ))";
				continue;
			}
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		return crtFilterAll() + sql + orderBy();
	}
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GsPriceGoodsCell) bean).gtGoods();
		String gcode = GsPriceGoodsCell.T.GOODS.getFld().getCode();
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
