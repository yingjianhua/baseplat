package irille.action.sys;

import irille.action.ActionSync;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysTemplatCell;

import java.util.List;

public class SysTemplatCellAction extends ActionSync<SysTemplatCell> {
	@Override
	public Class beanClazz() {
		return SysTemplatCell.class;
	}

	public SysTemplatCell getBean() {
		return _bean;
	}

	public void setBean(SysTemplatCell bean) {
		this._bean = bean;
	}

	public List<SysTemplatCell> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysTemplatCell> insLines) {
		_insLines = insLines;
	}

	public List<SysTemplatCell> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysTemplatCell> updLines) {
		_updLines = updLines;
	}

	public List<SysTemplatCell> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysTemplatCell> delLines) {
		_delLines = delLines;
	}
	
	@Override
	public void syncBefore() {
		super.syncBefore();
		Integer templat = Integer.parseInt(getMainPkey());
		if (getInsLines() != null)
			for (SysTemplatCell line : getInsLines()) {
				line.setTemplat(templat);
			}
	}

}
