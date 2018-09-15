package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.SysSupplier;
import irille.gl.gs.GsGoods;
import irille.pss.pur.PurProtApply;
import irille.pss.pur.PurProtGoods;
import irille.pss.pur.PurProtGoodsApply;
import irille.pss.pur.PurProtGoodsApplyLine;
import irille.pub.bean.BeanBase;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class PurProtGoodsApplyAction extends
		ActionForm<PurProtGoodsApply, PurProtGoodsApplyLine> {
	private Integer temId;
	private Integer supId;
	private Integer goodsId;

	public Integer getTemId() {
		return temId;
	}

	public void setTemId(Integer temId) {
		this.temId = temId;
	}

	public Integer getSupId() {
		return supId;
	}

	public void setSupId(Integer supId) {
		this.supId = supId;
	}

	public Integer getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(Integer goodsId) {
		this.goodsId = goodsId;
	}

	@Override
	public Class beanClazz() {
		return PurProtGoodsApply.class;
	}

	public PurProtGoodsApply getBean() {
		return _bean;
	}

	public void setBean(PurProtGoodsApply bean) {
		this._bean = bean;
	}

	public List<PurProtGoodsApplyLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurProtGoodsApplyLine> listLine) {
		_listLine = listLine;
	}
	
	
	public void init() throws Exception {
		JSONObject json = new JSONObject();
		PurProtGoods bean = PurProtGoods.chkUniqueTempCustObj(false, getTemId(), getSupId(), getGoodsId());
		GsGoods goods = BeanBase.load(GsGoods.class, getGoodsId());
		
		PurProtGoodsApplyLine pap = new PurProtGoodsApplyLine();
		if (bean != null) {
			pap.setGoods(bean.getGoods());
			pap.setUom(bean.getUom());
			pap.setVendorGoodsName(bean.getVendorGoodsName());
			pap.setVendorNum(bean.getVendorNum());
			pap.setVendorSpec(bean.getVendorSpec());
			pap.setPrice(bean.getPrice());
			pap.setDateStart(bean.getDateStart());
			pap.setDateEnd(bean.getDateEnd());
			
			pap.setAftVendorGoodsName(bean.getVendorGoodsName());
			pap.setAftVendorNum(bean.getVendorNum());
			pap.setAftVendorSpec(bean.getVendorSpec());
			pap.setAftPrice(bean.getPrice());
			pap.setAftDateStart(bean.getDateStart());
			pap.setAftDateEnd(bean.getDateEnd());
			json = crtJsonByBean(pap, "bean.");
			json.put("success", "1");
		} else {
			
			pap.setGoods(goods.getPkey());
			pap.setUom(goods.getUom());
			
			json = crtJsonByBean(pap, "bean.");
			json.put("success", "0");
		}
		writerOrExport(json);
	}
	

	// 单据明细界面，货物变更后，自动读取货物代码、名称与规格
	public void loadInfo() throws IOException, JSONException {
		PurProtGoods line = PurProtGoods.chkUniqueTempCustObj(false,
				getTemId(), getSupId(), getGoodsId());
		GsGoods goods = new GsGoods().chk(getGoodsId());
		HttpServletResponse response = ServletActionContext.getResponse();
		
		JSONObject json = new JSONObject();
		if (goods != null) {
			json.put("goods", goods.getPkey() + BEAN_SPLIT + goods.getCode());
			json.put("goodsName", goods.getName());
			json.put("goodsSpec", goods.getSpec());
			json.put("uom", goods.getUom() + BEAN_SPLIT
					+ goods.gtUom().getName());
		}
//		if(line != null){
//			json.put("", "");
//		}
		response.getWriter().print(json.toString());
	}
}
