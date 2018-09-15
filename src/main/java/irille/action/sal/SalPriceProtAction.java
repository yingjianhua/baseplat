package irille.action.sal;

import irille.action.ActionBase;
import irille.core.sys.SysCustom;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsUom;
import irille.pss.sal.SalPriceProt;
import irille.pss.sal.SalPriceProtDAO;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class SalPriceProtAction extends ActionBase<SalPriceProt> {
	private Integer _tmpId;
	private String _goods;
	private BigDecimal _cost;
	private Integer _clsPrice;
	private Integer _cust;
	private Integer _uom;
	
	public SalPriceProt getBean() {
		return _bean;
	}

	public void setBean(SalPriceProt bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return SalPriceProt.class;
	}

	public Integer getTmpId() {
		return _tmpId;
	}

	public void setTmpId(Integer tmpId) {
		_tmpId = tmpId;
	}

	public String getGoods() {
		return _goods;
	}

	public void setGoods(String goods) {
		_goods = goods;
	}

	public BigDecimal getCost() {
		return _cost;
	}

	public void setCost(BigDecimal cost) {
		_cost = cost;
	}

	public Integer getClsPrice() {
		return _clsPrice;
	}

	public void setClsPrice(Integer clsPrice) {
		_clsPrice = clsPrice;
	}

	public Integer getCust() {
		return _cust;
	}

	public void setCust(Integer cust) {
		_cust = cust;
	}

	public Integer getUom() {
		return _uom;
	}

	public void setUom(Integer uom) {
		_uom = uom;
	}
	
//根据客户、货物读取信息及默认价格
	public void loadInfoPrice() throws IOException, JSONException {
		GsGoods goods = GsGoods.chkUniqueCode(false, getGoods());
		SysCustom cust = BeanBase.load(SysCustom.class, getCust());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (goods != null) {
			if (getUom() == null) {
				json.put("goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
				json.put("goodsName", goods.getName());
				json.put("goodsSpec", goods.getSpec());
				json.put("uom", goods.getUom() + BEAN_SPLIT + goods.gtUom().getName());
				json.put("price", SalPriceProtDAO.getPrice(cust, goods));
			} else {
				GsUom uom = BeanBase.load(GsUom.class, getUom());
				json.put("goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
				json.put("goodsName", goods.getName());
				json.put("goodsSpec", goods.getSpec());
				json.put("uom",  + uom.getPkey() + BEAN_SPLIT + uom.getName());
				json.put("price", goods.gtUom().tranPrice(uom, SalPriceProtDAO.getPrice(cust, goods)));
			}
		}
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	private Object nv(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return toDateJson((Date) obj);
		if (obj instanceof BeanMain) {
			BeanMain b = (BeanMain) obj;
			return b.getPkey() + BEAN_SPLIT + ((IExtName) b).getExtName();
		}
		return obj;
	}
}
