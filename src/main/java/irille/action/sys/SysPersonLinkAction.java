package irille.action.sys;

import irille.action.ActionSync;
import irille.core.sys.SysCustom;
import irille.core.sys.SysDept;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysSupplier;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SysPersonLinkAction extends ActionSync<SysPersonLink> {
	public String _itemId;

	@Override
	public Class beanClazz() {
		return SysPersonLink.class;
	}

	public SysPersonLink getBean() {
		return _bean;
	}

	public void setBean(SysPersonLink bean) {
		this._bean = bean;
	}

	public List<SysPersonLink> getInsLines() {
		return _insLines;
	}

	public void setInsLines(List<SysPersonLink> insLines) {
		_insLines = insLines;
	}

	public List<SysPersonLink> getUpdLines() {
		return _updLines;
	}

	public void setUpdLines(List<SysPersonLink> updLines) {
		_updLines = updLines;
	}

	public List<SysPersonLink> getDelLines() {
		return _delLines;
	}

	public void setDelLines(List<SysPersonLink> delLines) {
		_delLines = delLines;
	}

	public String getItemId() {
		return _itemId;
	}

	public void setItemId(String itemId) {
		_itemId = itemId;
	}

	@Override
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()) || getFilter().contains("itemId") == false)
			return super.crtFilter();
		JSONArray ja = new JSONArray(getFilter());
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if (fldName.equals("itemId"))
				setItemId(param);
			else if (fldName.equals("tbObjLong"))
				setMainPkey(param);
		}
		Integer pkey = Integer.parseInt(getMainPkey());
		int tid = 0;
		if (getItemId().contains("SysDept"))
			tid = SysDept.TB.getID();
		else if (getItemId().contains("SysCustom"))
			tid = SysCustom.TB.getID();
		else if (getItemId().contains("SysSupplier"))
			tid = SysSupplier.TB.getID();
		String sql = " AND " + SysPersonLink.T.TB_OBJ_LONG.getFld().getCodeSqlField() + "=" + Bean.gtLongPkey(pkey, tid);
		return crtFilterAll() + sql + orderBy();

	}

	@Override
	public void syncBefore() {
		super.syncBefore();
		Integer pkey = Integer.parseInt(getMainPkey());
		int tid = 0;
		if (getItemId().contains("SysDept"))
			tid = SysDept.TB.getID();
		else if (getItemId().contains("SysCustom"))
			tid = SysCustom.TB.getID();
		else if (getItemId().contains("SysSupplier"))
			tid = SysSupplier.TB.getID();
		if (getInsLines() != null)
			for (SysPersonLink line : getInsLines()) {
				line.setTbObjLong(Bean.gtLongPkey(pkey, tid));
			}
	}
}
