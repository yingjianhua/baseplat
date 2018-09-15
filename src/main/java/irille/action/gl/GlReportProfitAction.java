package irille.action.gl;

import irille.action.ActionForm;
import irille.gl.gl.GlReportProfit;
import irille.gl.gl.GlReportProfitLine;

public class GlReportProfitAction extends ActionForm<GlReportProfit,GlReportProfitLine>{
	
	@Override
	public Class beanClazz() {
	  return GlReportProfit.class;
	}
	
	public GlReportProfit getBean() {
		return _bean;
	}

	public void setBean(GlReportProfit bean) {
		this._bean = bean;
	}
	
}
