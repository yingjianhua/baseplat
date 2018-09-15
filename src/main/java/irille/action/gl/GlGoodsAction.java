package irille.action.gl;

import irille.action.ActionBase;
import irille.core.sys.SysCell;
import irille.gl.gl.GlGoods;
import irille.gl.gl.GlGoodsDAO;
import irille.gl.gs.GsGoods;
import irille.pub.Str;
import irille.pub.bean.Bean;

import org.json.JSONObject;

public class GlGoodsAction extends ActionBase<GlGoods> {
	public Integer _cell;

	public GlGoods getBean() {
		return _bean;
	}

	public void setBean(GlGoods bean) {
		this._bean = bean;
	}

	public Integer getCell() {
		return _cell;
	}

	public void setCell(Integer cell) {
		_cell = cell;
	}

	@Override
	public Class beanClazz() {
		return GlGoods.class;
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		if (Str.isEmpty(pref) == false)
			pref = "link.";
		GsGoods goods = ((GlGoods) bean).gtGoods();
		String gcode = GlGoods.T.GOODS.getFld().getCode();
		json.put(pref + gcode + "Name", goods.getName());
		json.put(pref + gcode + "Spec", goods.getSpec());
		SysCell cell = ((GlGoods)bean).gtJournal().gtCell();
		json.put("cell", cell.getPkey()+BEAN_SPLIT+cell.getExtName());
		return json;
	}

	@Override
	public GlGoods insRun() throws Exception {
		GlGoodsDAO.Ins ins = new GlGoodsDAO.Ins();
		ins.setB(_bean);
		ins._mycell = getCell();
		ins.commit();
		return ins.getB();
	}
}
