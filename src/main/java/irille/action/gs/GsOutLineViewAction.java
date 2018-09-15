package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.core.sys.SysTable;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsOut;
import irille.gl.gs.GsOutLineView;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockDAO;
import irille.gl.gs.GsWarehouse;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.inf.IIn;
import irille.pub.inf.IOut;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GsOutLineViewAction extends ActionLineGoods<GsOutLineView> {
	public GsOutLineView getBean() {
		return _bean;
	}

	public void setBean(GsOutLineView bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsOutLineView.class;
	}
	
	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		GsOut out = getOut();
		Class dc = Class.forName(out.gtOrigForm().getClass().getName() + "DAO");
		IOut outLine = (IOut) dc.newInstance();
		List<IGoods> list = outLine.getOutLines(out.gtOrigForm(), getStart(), getLimit());
		JSONObject lineJson = null;
		for (IGoods line : list) {
			if (line.gtGoods().isWork())
				continue;
			lineJson = crtJsonByBean((Bean) line);
			//重写的部分
			GsStock gs = GsStockDAO.getStockOrCrt(out.gtWarehouse(), line.gtGoods(), false);
			lineJson.put("location", gs.getLocation() == null ? null : gs.gtLocation()
			    .getPkey() + BEAN_SPLIT + gs.gtLocation().getName());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, outLine.getOutLinesCount(out.gtOrigForm()));
		writerOrExport(json);
	}
	
	public void listAppr() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		GsOut out = getOut();
		Class dc = Class.forName(out.gtOrigForm().getClass().getName() + "DAO");
		IOut outLine = (IOut) dc.newInstance();
		List<IGoods> list = outLine.getOutLines(out.gtOrigForm(), getStart(), getLimit());
		JSONObject lineJson = null;
		for (IGoods line : list) {
			if (line.gtGoods().isWork())
				continue;
			lineJson = crtJsonByBean((Bean) line);
			//重写的部分
			GsStock gs = GsStockDAO.getStockOrCrt(out.gtWarehouse(), line.gtGoods(), false);
			lineJson.put("location", gs.getLocation() == null ? null : gs.gtLocation()
			    .getPkey() + BEAN_SPLIT + gs.gtLocation().getName());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, outLine.getOutLinesCount(out.gtOrigForm()));
		writerOrExport(json);
	}
	
	public void getGoodsKeys() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String keys = "";
		int i = 1;
		GsOut out = BeanBase.load(GsOut.class, getPkey());
		String oirgName = out.gtOrigForm().getClass().getName();
		Class dc = Class.forName(oirgName + "DAO");
		IOut outLine = (IOut) dc.newInstance();
		List<IGoods> list = outLine.getOutLines(out.gtOrigForm(), getStart(), getLimit());
		for (IGoods line : list) {
			keys += line.getGoods();
			if (i < list.size())
				keys += ",";
			i++;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(keys);
	}
	
	private GsOut getOut() throws JSONException {
		GsOut out = null;
		JSONArray ja = new JSONArray(getFilter());
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			if (fld.getCode().equals("pkey")) { //主键判断作特殊处理
				long key1 = Long.parseLong(param) * SysTable.NUM_BASE;
				out = GsOut.load(GsOut.class, Long.parseLong(param));
				break;
			}
		}
		return out;
	}
	public static GsOutLineView transIGoods2GsOutLineView(GsOut out, IGoods line) {
		GsStock gs = GsStockDAO.getStockOrCrt(out.gtWarehouse(), line.gtGoods(), false);
		GsOutLineView view = new GsOutLineView();
		view.stGoods(line.gtGoods());
		view.setQty(line.getQty());
		view.stUom(line.gtUom());
		view.stLocation(gs.gtLocation());
		return view;
	}
	public static List<GsOutLineView> transIGoods2GsOutLineView(GsOut out, List<IGoods> goods) {
		List<GsOutLineView> list = new ArrayList<GsOutLineView>();
		GsWarehouse warehouse = out.gtWarehouse();
		for(IGoods line:goods) {
			GsStock gs = GsStockDAO.getStockOrCrt(warehouse, line.gtGoods(), false);
			GsOutLineView view = new GsOutLineView();
			view.stGoods(line.gtGoods());
			view.setQty(line.getQty());
			view.stUom(line.gtUom());
			view.stLocation(gs.gtLocation());
			list.add(view);
		}
		return list;
	}
}
