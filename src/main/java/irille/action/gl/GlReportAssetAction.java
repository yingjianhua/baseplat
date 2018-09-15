package irille.action.gl;

import irille.action.ActionForm;
import irille.gl.gl.GlReportAsset;
import irille.gl.gl.GlReportAssetLine;

import org.json.JSONException;

public class GlReportAssetAction extends ActionForm<GlReportAsset,GlReportAssetLine>{
	
	@Override
	public Class beanClazz() {
	  return GlReportAsset.class;
	}
	
	public GlReportAsset getBean() {
		return _bean;
	}

	public void setBean(GlReportAsset bean) {
		this._bean = bean;
	}

	@Override
	public String crtFilter() throws JSONException {
	  return super.crtFilter().replace("ORDER BY PKEY DESC", "ORDER BY PKEY ASC");
	}
}
