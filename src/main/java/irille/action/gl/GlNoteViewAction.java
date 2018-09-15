package irille.action.gl;

import irille.action.ActionLineTid;
import irille.core.sys.Sys;
import irille.gl.gl.Gl;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteView;
import irille.gl.gl.GlNoteViewDAO;
import irille.gl.gl.GlNoteWriteoff;
import irille.gl.gl.GlNoteWriteoffLine;
import irille.gl.gl.GlTransform;
import irille.pub.ClassTools;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 便签的临时视图表
 * 内转单及各BILL表单的便签，查询时默认为非自动的类型
 * @author whx
 * @version 创建时间：2015年1月9日 下午3:19:03
 */
public class GlNoteViewAction extends ActionLineTid<GlNoteView> {
	public String _tableCode;
	public List<GlNoteView> _listLine;

	public GlNoteView getBean() {
		return _bean;
	}

	public void setBean(GlNoteView bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlNote.class; //设置为实际的表类型
	}

	public String getTableCode() {
		return _tableCode;
	}

	public void setTableCode(String tableCode) {
		_tableCode = tableCode;
	}

	public List<GlNoteView> getListLine() {
		return _listLine;
	}

	public void setListLine(List<GlNoteView> listLine) {
		_listLine = listLine;
	}

	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
		int tid = GlTransform.TB.getID(); //
		JSONArray ja = new JSONArray(getFilter());
		String sql = "";
		Tb tb = tb();
		String billCode = GlNote.T.BILL.getFld().getCode();
		for (int i = 0; i < ja.length(); i++) {
			JSONObject json = ja.getJSONObject(i);
			String fldName = json.getString(QUERY_PROPERTY);
			String param = json.getString(QUERY_VALUE);
			if (Str.isEmpty(param))
				continue;
			//用于Gl/GlNote/WinNote.js
			if (fldName.equals("tableCode")) {
				tid = Tb.getID(ClassTools.getClass(param));
				continue;
			}
			if (!tb.chk(fldName))
				continue;
			Fld fld = tb.get(fldName);
			if (fld == null)
				continue;
			if (fld.getCode().equals(billCode)) { //主键判断作特殊处理
				long key1 = Bean.gtLongPkey(Integer.parseInt(param), tid);
				sql += " AND " + billCode + " = " + key1;
				continue;
			}
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		return GlNote.T.IS_AUTO.getFld().getCodeSqlField() + "=" + Sys.OYn.NO.getLine().getKey() + sql + orderBy();
	}

	@Override
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		//TODO 
		GlNoteView.TB.getCode();
		GlNote note = (GlNote) bean;
		if (note.getExtTable() == null)
			json.put(pref + GlNoteView.T.WRITEOFF_FLAG.getFld().getCode(), Gl.OWriteoffFlag.WRITEOFF_NO.getLine().getKey());
		else if (note.getExtTable() == GlNoteWriteoff.TB.getID()) {
			GlNoteWriteoff nw = BeanBase.load(GlNoteWriteoff.class, note.getPkey());
			json.put(pref + GlNoteView.T.WRITEOFF_FLAG.getFld().getCode(), Gl.OWriteoffFlag.WRITEOFF_NEW.getLine().getKey());
			json.put(pref + GlNoteView.T.DATE_START.getFld().getCode(), nw.getDateStart());
			json.put(pref + GlNoteView.T.DATE_DUE.getFld().getCode(), nw.getDateDue());
		} else if (note.getExtTable() == GlNoteWriteoffLine.TB.getID()) {
			GlNoteWriteoffLine nl = BeanBase.load(GlNoteWriteoffLine.class, note.getPkey());
			json.put(pref + GlNoteView.T.WRITEOFF_FLAG.getFld().getCode(), Gl.OWriteoffFlag.WRITEOFF_WRITE.getLine().getKey());
			json.put(pref + GlNoteView.T.WRITEOFF.getFld().getCode(), nl.getMainNote());
		}
		return json;
	}

	public void iuByOther() throws Exception {
		GlNoteViewDAO.IuByOther act = new GlNoteViewDAO.IuByOther();
		act.setClassCode(getTableCode());
		act.setBillPkey(Long.parseLong(getPkey() + ""));
		act.setLines(getListLine());
		act.commit();
		writeSuccess();
	}

}
