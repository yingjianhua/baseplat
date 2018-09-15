package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsGoods;
import irille.gl.gs.GsGoodsDAO;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class GsGoodsAction extends ActionBase<GsGoods> {
	private String params; //接受
	private String _outSale;
	private String _outMv;
	private String _outPro;
	private String _inPur;
	private String _inMv;
	private String _inPro;

	public GsGoods getBean() {
		return _bean;
	}

	public void setBean(GsGoods bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsGoods.class;
	}

	public String getOutSale() {
		return _outSale;
	}

	public void setOutSale(String outSale) {
		_outSale = outSale;
	}

	public String getOutMv() {
		return _outMv;
	}

	public void setOutMv(String outMv) {
		_outMv = outMv;
	}

	public String getOutPro() {
		return _outPro;
	}

	public void setOutPro(String outPro) {
		_outPro = outPro;
	}

	public String getInPur() {
		return _inPur;
	}

	public void setInPur(String inPur) {
		_inPur = inPur;
	}

	public String getInMv() {
		return _inMv;
	}

	public void setInMv(String inMv) {
		_inMv = inMv;
	}

	public String getInPro() {
		return _inPro;
	}

	public void setInPro(String inPro) {
		_inPro = inPro;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	//	@Override
	//	public GsGoods insRun() throws Exception {
	//		String inFlag = getInPur() + getInMv() + getInPro();
	//		String outFlag = getOutSale() + getOutMv() + getOutPro();
	//		getBean().setInFlag(inFlag.replaceAll("null", "0"));
	//		getBean().setOutFlag(outFlag.replaceAll("null", "0"));
	//		GsGoodsDAO.Ins ins = new GsGoodsDAO.Ins();
	//		ins.setB(_bean);
	//		ins.commit();
	//		return ins.getB();
	//	}
	//
	//	@Override
	//	public GsGoods updRun() throws Exception {
	//		String inFlag = getInPur() + getInMv() + getInPro();
	//		String outFlag = getOutSale() + getOutMv() + getOutPro();
	//		getBean().setInFlag(inFlag.replaceAll("null", "0"));
	//		getBean().setOutFlag(outFlag.replaceAll("null", "0"));
	//		GsGoodsDAO.Upd upd = new GsGoodsDAO.Upd();
	//		upd.setB(_bean);
	//		upd.commit();
	//		return upd.getB();
	//	}

	//	@Override
	//	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
	//		if (Str.isEmpty(pref) == false)
	//			return json;
	//		//仅在加载FORM时转换出入库属性
	//		GsGoods goods = (GsGoods) bean;
	//		if (goods.getOutFlag() != null && goods.getOutFlag().length() == 3) {
	//			json.put("outSale", goods.getOutFlag().charAt(0) == '1');
	//			json.put("outMv", goods.getOutFlag().charAt(1) == '1');
	//			json.put("outPro", goods.getOutFlag().charAt(2) == '1');
	//		}
	//		if (goods.getInFlag() != null && goods.getInFlag().length() == 3) {
	//			json.put("inPur", goods.getInFlag().charAt(0) == '1');
	//			json.put("inMv", goods.getInFlag().charAt(1) == '1');
	//			json.put("inPro", goods.getInFlag().charAt(2) == '1');
	//		}
	//		return json;
	//	}

	@Override
	public String orderBy() {
		return " ORDER BY CODE,PKEY DESC";
	}

	//内容下拉框远程控件
	public void autoComplete() throws Exception {
		String code = null;
		String diy = null;
		if (getParams() != null) {
			JSONArray ja = new JSONArray(getParams());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
				if (json.has("DIY"))
					diy = json.optString("DIY");
				if (json.has("code"))
					code = json.optString("code");
			}
		}
		JSONArray arr = GsGoodsDAO.autoComplete(getStart(), getLimit(), code, diy);
		JSONObject obj = new JSONObject();
		obj.put("success", "true");
		obj.put("items", arr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(obj.toString());
	}
}
