package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlSubjectMap;
import irille.gl.gl.GlSubjectMapDAO;

import java.io.IOException;

import org.json.JSONException;

public class GlSubjectMapAction extends ActionBase<GlSubjectMap> {

	@Override
	public Class beanClazz() {
		return GlSubjectMap.class;
	}

	public GlSubjectMap getBean() {
		return _bean;
	}

	public void setBean(GlSubjectMap bean) {
		this._bean = bean;
	}

	public void refresh() throws IOException, JSONException {
		GlSubjectMapDAO.refreshBase();
		writeSuccess();
	}

}
