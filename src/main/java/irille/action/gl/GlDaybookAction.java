package irille.action.gl;

import irille.action.ActionBase;
import irille.core.sys.SysTable;
import irille.core.sys.SysTableDAO;
import irille.gl.frm.FrmPending;
import irille.gl.gl.GlDaybook;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.idu.IduBase;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlDaybookAction extends ActionBase<GlDaybook> {
	private String module;
	private String bill;
	private String _formIds;
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getBill() {
		return bill;
	}
	public void setBill(String bill) {
		this.bill = bill;
	}
	public String getFormIds() {
		return _formIds;
	}
	public void setFormIds(String formIds) {
		_formIds = formIds;
	}
	public GlDaybook getBean() {
		return _bean;
	}

	public void setBean(GlDaybook bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlDaybook.class;
	}
	public void tally() throws Exception{
		String []pkeys = getFormIds().split(",");
		for(String pkey: pkeys) {
			Bean bean = Bean.gtLongTbObj(Long.parseLong(pkey));
			if(bean instanceof BeanBill) {
				IduBase.DoTally tally = new IduBase.DoTally();
				tally.setClazz(bean.getClass());
				tally.setB((BeanBill)bean);
				tally.commit();
			}else {
				LOG.err("notBill","{0}不是单据，不能进行记账！", bean.gtTB().getName());
			}
		}
		writeSuccess();
	}
	public void untally() throws Exception{
		  List<GlDaybook> daybooks = BeanBase.list(GlDaybook.class, "pkey in ("+getPkeys()+")", false);
		  for(GlDaybook daybook : daybooks) {
		  	BeanBill bill = (BeanBill)daybook.gtBill();
		  	IduBase.UnTally tally = new IduBase.UnTally();
		  	tally.setClazz(bill.getClass());
		  	tally.setB(bill);
		  	tally.commit();
		  }
		  writeSuccess();
	}
	
	public void listPending() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = "1=1";
		if (Str.isEmpty(getFilter()) == false) {
			JSONArray whereJa = new JSONArray(getFilter());
			String property = whereJa.getJSONObject(0).getString(QUERY_PROPERTY);
			String orginForm = whereJa.getJSONObject(0).getString(QUERY_VALUE);
			where = FrmPending.T.ORG.getFld().getCodeSqlField() + "=" + getLoginSys().getOrg() + " AND "
			    + FrmPending.T.DESC_TYPE.getFld().getCodeSqlField() + "=" + SysTable.loadUniqueCode(false, GlDaybook.class.getName()).getPkey();
			if(property.equals("bill")) {
					where += " AND " + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "%" + SysTable.NUM_BASE + "="
					    + orginForm;
			} else {
				where += " AND floor(" + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "%" + SysTable.NUM_BASE + "/100)="
						+ orginForm;
			}
			
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
}
