package irille.action.sys;

import irille.action.ActionSync;
import irille.core.sys.SysCtype;
import irille.core.sys.SysCtypeCode;
import irille.pub.bean.BeanBase;
import irille.pub.svr.OptCustCtrl;

import java.util.List;

public class SysCtypeCodeAction extends ActionSync<SysCtypeCode> {

	@Override
	public Class beanClazz() {
		return SysCtypeCode.class;
	}

	public SysCtypeCode getBean() {
		return _bean;
	}

	public void setBean(SysCtypeCode bean) {
		this._bean = bean;
	}

	public List<SysCtypeCode> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysCtypeCode> insLines) {
		_insLines = insLines;
	}

	public List<SysCtypeCode> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysCtypeCode> updLines) {
		_updLines = updLines;
	}

	public List<SysCtypeCode> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysCtypeCode> delLines) {
		_delLines = delLines;
	}

	/**
	 * 设置机构号工与金融机构主键
	 */
	public void syncBefore() {
		super.syncBefore();
		SysCtype bean = BeanBase.load(SysCtype.class, getMainPkey());
		if (bean.getPkey().equals("foType"))
			return;
		if (getInsLines() != null)
			for (SysCtypeCode line : getInsLines()) {
				line.setCtypeType(getMainPkey());
				if (line.getCodeValue().length() != bean.getCtypeLen())
					throw LOG.err("codeLen", "参数配置的代码长度为{0}位", bean.getCtypeLen());
			}
		if (getUpdLines() != null)
			for (SysCtypeCode line : getUpdLines())
				if (line.getCodeValue().length() != bean.getCtypeLen())
					throw LOG.err("codeLen", "参数配置的代码长度为{0}位", bean.getCtypeLen());
		// 清空参数配置的缓存
		OptCustCtrl.getInstance().clear(bean.getPkey());
	}

	@Override
	public String orderBy() {
		return " ORDER BY " + SysCtypeCode.T.CODE_VALUE.getFld().getCodeSqlField();
	}

}
