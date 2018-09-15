package irille.action.gs;

import irille.action.ActionSync;
import irille.core.sys.Sys;
import irille.gl.gs.GsUom;
import irille.pub.bean.BeanBuf;

import java.util.List;

public class GsUomAction extends ActionSync<GsUom> {
	
	public GsUom getBean() {
		return _bean;
	}

	public void setBean(GsUom bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
		return GsUom.class;
	}
	public List<GsUom> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<GsUom> insLines) {
		_insLines = insLines;
	}

	public List<GsUom> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<GsUom> updLines) {
		_updLines = updLines;
	}
	public List<GsUom> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<GsUom> delLines) {
		_delLines = delLines;
	}
	@Override
	public String crtQueryAll() {
	  return GsUom.T.ENABLED.getFld().getCodeSqlField() + "=" + Sys.OEnabled.TRUE.getLine().getKey();
	}
	public void syncBefore() {
		super.syncBefore();
		BeanBuf.clear(GsUom.class);
		if (getInsLines() != null)
			for (GsUom line : getInsLines()) {
				line.setUomType(Integer.parseInt(getMainPkey()));
			}
	}
}
