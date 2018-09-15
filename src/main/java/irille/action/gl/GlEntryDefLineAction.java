package irille.action.gl;

import irille.action.ActionSync;
import irille.core.sys.SysTable;
import irille.gl.gl.GlEntryDef;
import irille.gl.gl.GlEntryDefLine;
import irille.pub.Str;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlEntryDefLineAction extends ActionSync<GlEntryDefLine> {
	public List<GlEntryDefLine> _listLine;

	@Override
	public Class beanClazz() {
		return GlEntryDefLine.class;
	}

	public GlEntryDefLine getBean() {
		return _bean;
	}

	public void setBean(GlEntryDefLine bean) {
		this._bean = bean;
	}

	public List<GlEntryDefLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GlEntryDefLine> listLine) {
		_listLine = listLine;
	}

	public void sync() throws Exception {
		GlEntryDef main = BeanBase.load(GlEntryDef.class, Long.parseLong(getMainPkey()));
		Idu.updLineTid(main, getListLine(), GlEntryDefLine.class);
		writeSuccess();
	}

	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		// [{"property":"code","value":"aaa"},{"property":"name","value":"bbb"}]
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			if (fld.getCode().equals("pkey")) { //主键判断作特殊处理
				long key1 = Long.parseLong(param) * SysTable.NUM_BASE;
				long key2 = key1 + SysTable.NUM_BASE;
				sql += " AND pkey > " + key1 + " AND pkey < " + key2;
				continue;
			}
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		return crtFilterAll() + sql + orderBy();
	}

}
