package irille.action.pur;

import irille.action.ActionBase;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysTemplatCell;
import irille.core.sys.SysTemplatCellDAO;
import irille.gl.gs.GsDemand;
import irille.gl.gs.GsGoods;
import irille.pss.pur.PurProtGoods;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.ProvDataCtrl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class PurProtGoodsAction extends ActionBase<PurProtGoods> {

	private String[] goods;
	private Integer temId;
	private Integer supId;

	public String[] getGoods() {
		return goods;
	}

	public void setGoods(String[] goods) {
		this.goods = goods;
	}

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

	@Override
	public Class beanClazz() {
		return PurProtGoods.class;
	}

	public PurProtGoods getBean() {
		return _bean;
	}

	public void setBean(PurProtGoods bean) {
		this._bean = bean;
	}

	public void listForGoods() throws Exception {
		if (getGoods() == null || getGoods().length == 0)
			return;
		Set<String> set = new HashSet<String>();
		set.addAll(Arrays.asList(getGoods()));
		goods = set.toArray(new String[0]);

		String gs = "";
		for (String str : getGoods()) {
			gs += "," + str;
		}
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		List<PurProtGoods> list = BeanBase.list(
				PurProtGoods.class,
				PurProtGoods.T.GOODS.getFld().getCodeSqlField() + " IN ("
						+ gs.substring(1) + ") AND "
						+ PurProtGoods.T.TEMPLAT.getFld().getCodeSqlField()
						+ " = "+SysTemplatCellDAO.getPurTmpl().getPkey()+" AND "
				+ ProvDataCtrl.INST.getWhere(Idu.getUser(), SysSupplier.class) + " GROUP BY "
						+ PurProtGoods.T.TEMPLAT.getFld().getCodeSqlField()
						+ ","
						+ PurProtGoods.T.SUPPLIER.getFld().getCodeSqlField()
						+ " HAVING COUNT(0) = " + getGoods().length, false);
		JSONObject lineJson = null;
		for (PurProtGoods line : list) {
			lineJson = crtJsonByBean(line,"bean.");
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put("success", true);
		writerOrExport(json);
	}

	public void listForSupplier() throws Exception {

		String STR = Idu.sqlString("{0}=? and {1}=?", PurProtGoods.T.TEMPLAT,
				PurProtGoods.T.SUPPLIER);
		HttpServletResponse response = ServletActionContext.getResponse();
		List<PurProtGoods> list = BeanBase.list(PurProtGoods.class, STR, false,
				getTemId(), getSupId());
		ArrayList<String> sl = new ArrayList<String>();
		for (PurProtGoods line : list) {
			sl.add(line.getGoods().toString());
		}
		JSONObject json = new JSONObject();
		json.put("goods", sl.toArray(new String[0]));
		response.getWriter().print(json.toString());

	}
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((PurProtGoods) bean).gtGoods();
		String gcode = PurProtGoods.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		return json;
	}
}
