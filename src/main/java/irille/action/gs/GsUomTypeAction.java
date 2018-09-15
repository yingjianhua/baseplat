package irille.action.gs;

import irille.action.ActionBase;
import irille.gl.gs.GsUom;
import irille.gl.gs.GsUomType;
import irille.gl.gs.GsUomTypeDAO;

import java.util.List;

public class GsUomTypeAction extends ActionBase<GsUomType> {
	public List<GsUom> _listLine;
	
	public GsUomType getBean() {
		return _bean;
	}

	public void setBean(GsUomType bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsUomType.class;
	}
	

	public List<GsUom> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsUom> listLine) {
		_listLine = listLine;
	}

	@Override
	public GsUomType updRun() throws Exception {
		GsUomTypeDAO.Upd upd = new GsUomTypeDAO.Upd();
		upd.setB(_bean);
		//upd.setLines(getListLine());
		upd.commit();
		return upd.getB();
	}

	@Override
	public GsUomType insRun() throws Exception {
		GsUomTypeDAO.Ins ins = new GsUomTypeDAO.Ins();
		ins.setB(_bean);
		//ins.setLines(getListLine());
		ins.commit();
		return ins.getB();
	}

}
