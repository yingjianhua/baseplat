package irille.action;

import irille.gl.gs.GsGoods;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class ActionLineGoods<MAIN extends BeanMain> extends ActionLineTid<MAIN> {

	@Override
	public Class beanClazz() {
		throw LOG.err("notDefine", "ACTION未重写[beanClazz]方法");
	}

	@Override
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		GsGoods goods = (GsGoods) bean.propertyValueOBJ(bean.gtTB().get("goods"));
		if (Str.isEmpty(pref) == false)
			return json;
		//加载列表时取关联字段值
		json.remove("goods");
		json.put("goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
		json.put("goodsName", goods.getName());
		json.put("goodsSpec", goods.getSpec());
		return json;
	}

	//单据明细界面，货物变更后，自动读取货物代码、名称与规格
	public void loadInfo() throws IOException, JSONException {
		GsGoods goods = GsGoods.chkUniqueCode(false, getSarg1());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (goods != null) {
			json.put("goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
			json.put("goodsName", goods.getName());
			json.put("goodsSpec", goods.getSpec());
			json.put("uom", goods.getUom() + BEAN_SPLIT + goods.gtUom().getName());
		}
		response.getWriter().print(json.toString());
	}

}
