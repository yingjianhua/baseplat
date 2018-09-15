package irille.action.pur;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsGoods;
import irille.pss.pur.PurProtGoodsApplyLine;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class PurProtGoodsApplyLineAction extends ActionLineGoods< PurProtGoodsApplyLine> {

	public  PurProtGoodsApplyLine getBean() {
		return _bean;
	}

	public void setBean( PurProtGoodsApplyLine bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return  PurProtGoodsApplyLine.class;
	}

	
	public void checkForm() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		GsGoods goods = getBean().gtGoods();
		JSONObject json = crtJsonByBean(getBean(), "bean.");
		json.remove("bean.goods");
		json.put("bean.goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
		json.put("link.goodsName", goods.getName());
		json.put("link.goodsSpec", goods.getSpec());
		json.put("bean.uom", goods.getUom()+BEAN_SPLIT+goods.gtUom().getName());
		json.put("success", true);
		response.getWriter().print(json.toString());
	}
}
