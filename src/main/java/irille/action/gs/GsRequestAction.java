package irille.action.gs;

import irille.action.ActionForm;
import irille.gl.gs.GsRequest;
import irille.gl.gs.GsRequestLine;

import java.util.List;

public class GsRequestAction extends ActionForm<GsRequest, GsRequestLine> {

	@Override
	public Class beanClazz() {
		return GsRequest.class;
	}

	public GsRequest getBean() {
		return _bean;
	}

	public void setBean(GsRequest bean) {
		this._bean = bean;
	}

	public List<GsRequestLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GsRequestLine> listLine) {
		_listLine = listLine;
	}

}
