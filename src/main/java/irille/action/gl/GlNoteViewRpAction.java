package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteViewRp;
import irille.gl.rp.RpJournal;
import irille.gl.rp.RpNoteCashPay;
import irille.gl.rp.RpNoteCashRpt;
import irille.gl.rp.RpNotePay;
import irille.gl.rp.RpNoteRpt;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;
import irille.pub.tb.Fld;
import irille.pub.tb.Tb;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 待收付款登记处理的临时视图表
 * @author whx
 * @version 创建时间：2015年1月9日 下午3:19:03
 */
public class GlNoteViewRpAction extends ActionBase<GlNoteViewRp> {

	public GlNoteViewRp getBean() {
		return _bean;
	}

	public void setBean(GlNoteViewRp bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlNote.class; //设置为实际的表类型
	}
	
	public String crtFilter() throws JSONException {
		if (Str.isEmpty(getFilter()))
			return crtFilterAll() + orderBy();
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
			//TODO 前台源单据号是作外键显示其类型，这里过滤掉仅保留主键值
			if (fldName.equals(GlNote.T.BILL.getFld().getCode()))
				param = param.split(BEAN_SPLIT)[0];
			sql += " AND " + Env.INST.getDB().crtWhereSearch(fld, param);
		}
		return crtFilterAll() + sql + orderBy();
	}

	@Override
	public String crtAll() { //仅作为出纳模块中待收付款菜单的明细显示时调用
		String all = Idu.sqlString("{0} in (" + RpNoteCashPay.TB.getID() + "," + RpNoteCashRpt.TB.getID() + ","
		    + RpNotePay.TB.getID() + "," + RpNoteRpt.TB.getID() + ")", GlNote.T.EXT_TABLE);
		return all;
	}

	@Override
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		GlNoteViewRp.TB.getCode();
		GlNote note = (GlNote) bean;
		json.put(pref + GlNoteViewRp.T.JOURNAL.getFld().getCode(),
		    note.getJournal() + BEAN_SPLIT + BeanBase.load(RpJournal.class, note.getJournal()).getName());
		if (note.getExtTable() == RpNoteCashPay.TB.getID()) {
			RpNoteCashPay pay = BeanBase.load(RpNoteCashPay.class, note.getPkey());
			json.put(pref + GlNoteViewRp.T.TYPE.getFld().getCode(), 0);
			json.put(pref + GlNoteViewRp.T.TYPE_TIME.getFld().getCode(), toTimeJson(pay.getTranTime()));
			json.put(pref + GlNoteViewRp.T.CASHIER.getFld().getCode(), pay.getCashier() + BEAN_SPLIT
			    + pay.gtCashier().getName());
		} else if (note.getExtTable() == RpNoteCashRpt.TB.getID()) {
			RpNoteCashRpt rpt = BeanBase.load(RpNoteCashRpt.class, note.getPkey());
			json.put(pref + GlNoteViewRp.T.TYPE.getFld().getCode(), 0);
			json.put(pref + GlNoteViewRp.T.TYPE_TIME.getFld().getCode(), toTimeJson(rpt.getTranTime()));
			json.put(pref + GlNoteViewRp.T.CASHIER.getFld().getCode(), rpt.getCashier() + BEAN_SPLIT
			    + rpt.gtCashier().getName());
		} else if (note.getExtTable() == RpNotePay.TB.getID()) {
			RpNotePay pay = BeanBase.load(RpNotePay.class, note.getPkey());
			json.put(pref + GlNoteViewRp.T.TYPE.getFld().getCode(), pay.getType());
			json.put(pref + GlNoteViewRp.T.TYPE_TIME.getFld().getCode(), toTimeJson(pay.getPayDate()));
			json.put(pref + GlNoteViewRp.T.CASHIER.getFld().getCode(), pay.getCashier() + BEAN_SPLIT
			    + pay.gtCashier().getName());
			String desc = "";
			if (pay.getDate() != null)
				desc += ",票据日期：" + toDateJson(pay.getDate());
			if (Str.isEmpty(pay.getName()) == false)
				desc += ",收款方名称：" + pay.getName();
			if (Str.isEmpty(pay.getAccount()) == false)
				desc += ",收款方账号：" + pay.getAccount();
			if (Str.isEmpty(pay.getBank()) == false)
				desc += ",收款方银行：" + pay.getBank();
			if (Str.isEmpty(desc) == false)
				json.put(pref + GlNoteViewRp.T.TYPE_DES.getFld().getCode(), desc.substring(1));
		} else if (note.getExtTable() == RpNoteRpt.TB.getID()) {
			RpNoteRpt rpt = BeanBase.load(RpNoteRpt.class, note.getPkey());
			json.put(pref + GlNoteViewRp.T.TYPE.getFld().getCode(), rpt.getType());
			json.put(pref + GlNoteViewRp.T.TYPE_TIME.getFld().getCode(), toTimeJson(rpt.getTranTime()));
			json.put(pref + GlNoteViewRp.T.CASHIER.getFld().getCode(), rpt.getCashier() + BEAN_SPLIT
			    + rpt.gtCashier().getName());
			String desc = "";
			if (rpt.getReceiveDate() != null)
				desc += ",到账日期：" + toDateJson(rpt.getReceiveDate());
			if (Str.isEmpty(rpt.getName()) == false)
				desc += ",付款方名称：" + rpt.getName();
			if (Str.isEmpty(desc) == false)
				json.put(pref + GlNoteViewRp.T.TYPE_DES.getFld().getCode(), desc.substring(1));
		}
		return json;

	}
}
