package irille.action.frm;

import irille.action.ActionBase;
import irille.core.sys.SysTable;
import irille.gl.frm.FrmPending;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.svr.Env;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class FrmPendingAction extends ActionBase<FrmPending> {
	public FrmPending getBean() {
		return _bean;
	}

	public void setBean(FrmPending bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return FrmPending.class;
	}

	public void listOut() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = "1=1";Env.INST.getTran().getUser();
		if (Str.isEmpty(getFilter()) == false) {
			JSONArray whereJa = new JSONArray(getFilter());
			String descType = whereJa.getJSONObject(0).getString(QUERY_VALUE);
			where = FrmPending.T.ORG.getFld().getCodeSqlField() + "=" +getLoginSys().getOrg() + " AND "
			    + FrmPending.T.DESC_TYPE.getFld().getCodeSqlField() + "=" + descType;
			if (whereJa.length() > 1)
				where += " AND " + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "%" + SysTable.NUM_BASE + "="
				    + whereJa.getJSONObject(1).getString(QUERY_VALUE);
		} else {
			throw LOG.err("getPendErr", "取待处理数据出错!");
		}
		List<FrmPending> list = BeanBase.list(FrmPending.class, false, where, 0, 0);
		JSONObject lineJson = null;
		for (FrmPending line : list) {
			lineJson = crtJsonByBean(line);
			Bean orig = line.gtOrigForm();
			if (orig.gtTB().chk("amt"))
				lineJson.put("amt", orig.propertyValue(orig.gtTB().get("amt")));
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	}

	public void listHand() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		String numb = null; //默认写死前台只可能传一个单据号条件过来
		if (Str.isEmpty(getQuery()) == false) {
			JSONArray ja2 = new JSONArray(getQuery());
			for (int i = 0; i < ja2.length(); i++) {
				String pro = ja2.getJSONObject(i).getString(QUERY_PROPERTY);
				if (pro.equals("param"))
					numb = ja2.getJSONObject(i).getString(QUERY_VALUE);
			}
		}
		String where = "1=1";
		if (Str.isEmpty(numb) == false)
			where = FrmPending.T.ORIG_FORM_NUM.getFld().getCodeSqlField()+" like '%"+numb+"%'";
		where += " AND "
		    + Idu.sqlString("{0} is null and {1}=" + Idu.getUser().getPkey() + " group by {2}", FrmPending.T.FORM_HANDOVER,
		        FrmPending.T.USER_SYS, FrmPending.T.ORIG_FORM);

		List<FrmPending> list = BeanBase.list(FrmPending.class, false, where, 0, 0);
		JSONObject lineJson = null;
		for (FrmPending line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	}

}
