package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsPhyinv;
import irille.gl.gs.GsPhyinvBatchLine;
import irille.gl.gs.GsPhyinvGoodsLine;
import irille.gl.gs.GsPhyinvDAO.Inv;
import irille.gl.gs.GsPhyinvDAO.Produce;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class GsPhyinvAction extends ActionForm<GsPhyinv, GsPhyinvGoodsLine> {
	public List<GsPhyinvBatchLine> _batchListLine;

	@Override
	public Class beanClazz() {
		return GsPhyinv.class;
	}

	public GsPhyinv getBean() {
		return _bean;
	}

	public void setBean(GsPhyinv bean) {
		this._bean = bean;
	}

	public List<GsPhyinvGoodsLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsPhyinvGoodsLine> listLine) {
		_listLine = listLine;
	}
	
	public List<GsPhyinvBatchLine> getBatchListLine() {
		return _batchListLine;
	}

	public void setBatchListLine(List<GsPhyinvBatchLine> batchListLine) {
		_batchListLine = batchListLine;
	}
	
	public void produce() throws Exception{
		Produce act = new Produce();
		act.setBKey(getPkey());
		act.commit();
		writeSuccess(act.getB());
	}
	
	public void inv() throws Exception {
		Inv inv = new Inv();
		inv.setB(_bean);
		inv.setBatchLines(getBatchListLine());
		inv.commit();
		writeSuccess(inv.getB());
	}
}
