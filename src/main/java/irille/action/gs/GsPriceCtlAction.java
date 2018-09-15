package irille.action.gs;

import irille.action.ActionBase;
import irille.core.sys.SysOrg;
import irille.gl.gs.GsPrice;
import irille.gl.gs.GsPriceCtl;
import irille.gl.gs.GsPriceCtlDAO;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.inf.IExtName;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class GsPriceCtlAction extends ActionBase<GsPriceCtl> {
	private Integer _tbObjCell;
	private Integer _tbObjOrg;

	public GsPriceCtl getBean() {
		return _bean;
	}

	public void setBean(GsPriceCtl bean) {
		this._bean = bean;
	}

	public Integer getTbObjCell() {
		return _tbObjCell;
	}

	public void setTbObjCell(Integer tbObjCell) {
		_tbObjCell = tbObjCell;
	}

	public Integer getTbObjOrg() {
		return _tbObjOrg;
	}

	public void setTbObjOrg(Integer tbObjOrg) {
		_tbObjOrg = tbObjOrg;
	}

	@Override
	public Class beanClazz() {
		return GsPriceCtl.class;
	}

	@Override
	public GsPriceCtl insRun() throws Exception {
		GsPriceCtlDAO.Ins ins = new GsPriceCtlDAO.Ins();
		ins.setB(_bean);
		ins._org = getTbObjOrg();
		ins._cell = getTbObjCell();
		ins.commit();
		insAfter();
		return ins.getB();
	}

	@Override
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		GsPriceCtl ctl = (GsPriceCtl) bean;
		GsPrice price = BeanBase.load(GsPrice.class, ctl.getPrice());
		json.remove(pref + GsPriceCtl.T.TB_OBJ.getFld().getCode());
		json.remove(pref + GsPriceCtl.T.RETAIL_LEVEL.getFld().getCode());
		json.remove(pref + GsPriceCtl.T.LOWEST_LEVEL.getFld().getCode());
		json.remove(pref + GsPriceCtl.T.TRADE_LEVEL.getFld().getCode());
		json.remove(pref + GsPriceCtl.T.MV_LEVEL.getFld().getCode());
		json.put(pref + GsPriceCtl.T.RETAIL_LEVEL.getFld().getCode(),
		    ctl.getRetailLevel() + BEAN_SPLIT + price.gtNamePrice(ctl.getRetailLevel()));
		json.put(pref + GsPriceCtl.T.LOWEST_LEVEL.getFld().getCode(),
		    ctl.getLowestLevel() + BEAN_SPLIT + price.gtNamePrice(ctl.getLowestLevel()));
		json.put(pref + GsPriceCtl.T.TRADE_LEVEL.getFld().getCode(),
		    ctl.getTradeLevel() + BEAN_SPLIT + price.gtNamePrice(ctl.getTradeLevel()));
		json.put(pref + GsPriceCtl.T.MV_LEVEL.getFld().getCode(),
		    ctl.getMvLevel() + BEAN_SPLIT + price.gtNamePrice(ctl.getMvLevel()));
		json.put(pref + GsPriceCtl.T.TB_OBJ.getFld().getCode(),
		    ctl.getTbObj() + BEAN_SPLIT + ((IExtName) ctl.gtTbObj()).getExtName());
		Bean obj = ctl.gtTbObj();
		if (obj instanceof SysOrg)
			json.put("tbObjOrg", obj.getPkey() + BEAN_SPLIT + ((IExtName) obj).getExtName());
		else
			json.put("tbObjCell", obj.getPkey() + BEAN_SPLIT + ((IExtName) obj).getExtName());
		json.put("tbObjType", obj instanceof SysOrg ? 1 : 2);
		return json;
	}

	@Override
	public void load() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		Bean bean = newBean().load(getPkey());
		JSONObject lineJson = crtJsonByBean(bean, "upd");
		response.getWriter().print(lineJson.toString());
	}

}
