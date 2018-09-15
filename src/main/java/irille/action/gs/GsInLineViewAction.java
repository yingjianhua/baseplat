package irille.action.gs;

import irille.action.ActionLineGoods;
import irille.gl.gs.GsIn;
import irille.gl.gs.GsInLineView;
import irille.gl.gs.GsStock;
import irille.gl.gs.GsStockDAO;
import irille.gl.gs.GsWarehouse;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.inf.IIn;
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

public class GsInLineViewAction extends ActionLineGoods<GsInLineView> {
	public GsInLineView getBean() {
		return _bean;
	}

	public void setBean(GsInLineView bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsInLineView.class;
	}

	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		GsIn in = getIn();
		String oirgName = in.gtOrigForm().getClass().getName();
		Class dc = Class.forName(oirgName + "DAO");
		IIn inLine = (IIn) dc.newInstance();
		List<IGoods> list = inLine.getInLines(in.gtOrigForm(), getStart(), getLimit());
		JSONObject lineJson = null;
		for (IGoods line : list) {
			if (line.gtGoods().isWork())
				continue;
			lineJson = crtJsonByBean((Bean) line);
			//重写的部分
			GsStock gs = GsStockDAO.getStockOrCrt(in.gtWarehouse(), line.gtGoods(), false);
			lineJson.put("location", gs.getLocation() == null ? null : gs.gtLocation().getPkey() + BEAN_SPLIT
			    + gs.gtLocation().getName());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, inLine.getInLinesCount(in.gtOrigForm()));
		writerOrExport(json);
	}

	//加载默认的货位
	public void listAppr() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		GsIn in = getIn();
		String oirgName = in.gtOrigForm().getClass().getName();
		Class dc = Class.forName(oirgName + "DAO");
		IIn inLine = (IIn) dc.newInstance();
		List<IGoods> list = inLine.getInLines(in.gtOrigForm(), getStart(), getLimit());
		JSONObject lineJson = null;
		for (IGoods line : list) {
			if (line.gtGoods().isWork())
				continue;
			lineJson = crtJsonByBean((Bean) line);
			//重写的部分
			GsStock gs = GsStockDAO.getStockOrCrt(in.gtWarehouse(), line.gtGoods(), false);
			lineJson.put("location", gs.getLocation() == null ? null : gs.gtLocation().getPkey() + BEAN_SPLIT
			    + gs.gtLocation().getName());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, inLine.getInLinesCount(in.gtOrigForm()));
		writerOrExport(json);
	}

	public void getGoodsKeys() throws IOException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		String keys = "";
		int i = 1;
		GsIn in = BeanBase.load(GsIn.class, getPkey());
		String oirgName = in.gtOrigForm().getClass().getName();
		Class dc = Class.forName(oirgName + "DAO");
		IIn inLine = (IIn) dc.newInstance();
		List<IGoods> list = inLine.getInLines(in.gtOrigForm(), getStart(), getLimit());
		for (IGoods line : list) {
			keys += line.getGoods();
			if (i < list.size())
				keys += ",";
			i++;
		}
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(keys);
	}

	private GsIn getIn() throws JSONException {
		GsIn in = null;
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
				in = GsIn.load(GsIn.class, Long.parseLong(param));
				break;
			}
		}
		return in;
	}

	public static GsInLineView transIGoods2GsInLineView(GsIn in, IGoods line) {
		GsStock gs = GsStockDAO.getStockOrCrt(in.gtWarehouse(), line.gtGoods(), false);
		GsInLineView view = new GsInLineView();
		view.stGoods(line.gtGoods());
		view.setQty(line.getQty());
		view.stUom(line.gtUom());
		view.stLocation(gs.gtLocation());
		return view;
	}

	public static List<GsInLineView> transIGoods2GsInLineView(GsIn in, List<IGoods> goods) {
		List<GsInLineView> list = new ArrayList<GsInLineView>();
		GsWarehouse warehouse = in.gtWarehouse();
		for (IGoods line : goods) {
			GsStock gs = GsStockDAO.getStockOrCrt(warehouse, line.gtGoods(), false);
			GsInLineView view = new GsInLineView();
			view.stGoods(line.gtGoods());
			view.setQty(line.getQty());
			view.stUom(line.gtUom());
			view.stLocation(gs.gtLocation());
			list.add(view);
		}
		return list;
	}

	//	private JSONObject getViewLines(GsIn in) throws Exception {
	//		JSONObject json = new JSONObject();
	//		JSONArray ja = new JSONArray();
	//		if (in.gtStatus() == Sys.OBillStatus.INIT) { //未审核时取源单据的明细
	//			String oirgName = in.gtOrigForm().getClass().getName();
	//			Class dc = Class.forName(oirgName + "DAO");
	//			IIn inLine = (IIn) dc.newInstance();
	//			List<IGoods> list = inLine.getInLines(in.gtOrigForm(), getStart(), getLimit());
	//			for (IGoods line : list) {
	//				if (line.gtGoods().isWork())
	//					continue;
	//				GsInLineView view = new GsInLineView();
	//				view.setPkey(line.getPkey());
	//				view.setGoods(line.getGoods());
	//				view.setQty(line.getQty());
	//				view.setUom(line.getUom());
	//				ja.put(crtJsonByBean(view));
	//			}
	//			json.put(STORE_ROOT, ja);
	//			json.put(STORE_TOTAL, inLine.getInLinesCount(in.gtOrigForm()));
	//		} else { //审核后取存货明细信息
	//			List<GsStockLine> list = GsStockLineDAO.getStockLines(in, false, getStart(), getLimit());
	//			for (GsStockLine line : list) {
	//				GsStock st = line.gtStock();
	//				GsInLineView view = new GsInLineView();
	//				view.setPkey(line.getPkey());
	//				view.setGoods(st.getGoods());
	//				view.setQty(line.getGsQty());
	//				view.setUom(view.gtGoods().getUom());
	//				view.setLocation(st.getLocation());
	//				view.setBatchCode(line.getGsBatchName());
	//				ja.put(crtJsonByBean(view));
	//			}
	//			json.put(STORE_ROOT, ja);
	//			json.put(STORE_TOTAL, GsStockLineDAO.getStockLinesCount(in));
	//		}
	//		return json;
	//	}

}
