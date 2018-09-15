package irille.action;

import irille.core.sys.SysTable;
import irille.pub.Str;
import irille.pub.bean.BeanMain;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class ActionLineTid<MAIN extends BeanMain> extends ActionBase<MAIN> {

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
