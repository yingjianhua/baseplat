package irille.action.gl;

import irille.action.ActionBase;
import irille.core.sys.Sys;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.rp.RpJournalLine;
import irille.gl.rp.RpJournalLineDAO;
import irille.gl.rp.RpNoteCashPay;
import irille.gl.rp.RpNoteCashRpt;
import irille.gl.rp.RpNotePay;
import irille.gl.rp.RpNotePayBill;
import irille.gl.rp.RpNoteRpt;
import irille.gl.rp.RpNoteRptBill;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanBill;
import irille.pub.bean.IBill;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduPage;
import irille.pub.idu.IduUnapprove;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class GlNoteAction extends ActionBase<GlNote> {
	private RpNotePayBill rpNotePayBill;
	private RpNoteRptBill rpNoteRptBill;
	private String tableCode;
	private String slip;
	private Date dateStart;
	private Date dateDue;
	private int slipPkey;

	public RpNotePayBill getRpNotePayBill() {
		return rpNotePayBill;
	}

	public void setRpNotePayBill(RpNotePayBill rpNotePayBill) {
		this.rpNotePayBill = rpNotePayBill;
	}

	public RpNoteRptBill getRpNoteRptBill() {
		return rpNoteRptBill;
	}

	public void setRpNoteRptBill(RpNoteRptBill rpNoteRptBill) {
		this.rpNoteRptBill = rpNoteRptBill;
	}

	public int getSlipPkey() {
		return slipPkey;
	}

	public void setSlipPkey(int slipPkey) {
		this.slipPkey = slipPkey;
	}

	public String getSlip() {
		return slip;
	}

	public void setSlip(String slip) {
		this.slip = slip;
	}

	public Date getDateStart() {
		return dateStart;
	}

	public void setDateStart(Date dateStart) {
		this.dateStart = dateStart;
	}

	public Date getDateDue() {
		return dateDue;
	}

	public void setDateDue(Date dateDue) {
		this.dateDue = dateDue;
	}

	public String getTableCode() {
		return tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public GlNote getBean() {
		return _bean;
	}

	public void setBean(GlNote bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlNote.class;
	}

	public void approve() throws Exception {
		GlNoteDAO.Approve act = new GlNoteDAO.Approve();
		act.setPkeys(getPkeys());
		act.commit();
		if (act.getB() != null) { //前台只选择一个
			HttpServletResponse response = ServletActionContext.getResponse();
			JSONObject json = crtJsonByBean(act.getB(), "bean.").put("success", true).put("bean.code",
			    ((IBill) ((GlNote) act.getB()).gtBill()).getCode());
			response.getWriter().print(json.toString());
		} else {
			writeSuccess();
		}
	}

	public void unapprove() throws Exception {
		GlNoteDAO.Unapprove act = new GlNoteDAO.Unapprove();
		act.setPkeys(getPkeys());
		act.commit();
		if (act.getB() != null) { //前台只选择一个
			HttpServletResponse response = ServletActionContext.getResponse();
			JSONObject json = crtJsonByBean(act.getB(), "bean.").put("success", true).put("bean.code",
			    ((IBill) ((GlNote) act.getB()).gtBill()).getCode());
			response.getWriter().print(json.toString());
		} else {
			writeSuccess();
		}

	}

	public void insByTb() throws Exception {
		Object obj = null;
		if (Str.isEmpty(slip) == false) {
			if (slip.equals(RpNotePayBill.class.getName())) {
				obj = getRpNotePayBill();
			} else if (slip.equals(RpNoteRptBill.class.getName())) {
				obj = getRpNoteRptBill();
			}
		}
		GlNoteDAO.insByTb(_bean, tableCode, getSlip(), getSlipPkey(), getDateStart(), getDateDue(), obj);
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = crtJsonByBean(_bean, "bean.");
		if (_bean.getExtTable() != null) {
			JSONObject j = GlNoteDAO.loadExt(_bean);
			j.remove("success");
			json.put("extContent", j.toString().replaceAll("[\"{}]", "") + "");
		}
		json.put("success", true);
		response.getWriter().print(json.toString());
	}

	@Override
	public void delMulti() throws Exception {
		String[] ary = getPkeys().split(",");
		Long[] svs = new Long[ary.length];
		for (int i = 0; i < ary.length; i++) {
			svs[i] = Long.parseLong(ary[i]);
		}

		for (Long line : svs) {
			IduDel act = newDel();
			act.setBKey(line);
			act.commit();
			GlNote b = (GlNote) act.getB();
			if (b.gtIsAuto() == false) {
				if (b.gtExtTable() == null)
					continue;
				String code = b.gtExtTable().getCode();
				if (code.equals(RpNoteCashPay.class.getName()) || code.equals(RpNotePay.class.getName())
				    || code.equals(RpNoteCashRpt.class.getName()) || code.equals(RpNoteRpt.class.getName())) {
					List<RpJournalLine> list = BeanBase.list(RpJournalLine.class, "note=?", false, b.getPkey());
					RpJournalLineDAO.Del delDao = new RpJournalLineDAO.Del();
					for (RpJournalLine bean : list) {
						delDao.setB(bean);
						delDao.commit();
					}
				}
			}
		}
		writeSuccess();
	}

	public void loadExt() throws Exception {
		GlNote note = (GlNote) BeanBase.load(beanClazz(), _bean.getPkey());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject lineJson = GlNoteDAO.loadExt(note);
		response.getWriter().print(lineJson.toString());
	}

	//由便签界面调用 | 由内转单子表明细GRID调用
	public void listByTb() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		List<GlNote> list = new ArrayList<GlNote>();
		String diySql = null;
		if (!Str.isEmpty(getQuery())) {
			JSONArray jq = new JSONArray(getQuery());
			for (int i = 0; i < jq.length(); i++) {
				if (jq.getJSONObject(i).getString(QUERY_PROPERTY).equals("diy")) {
					diySql = jq.getJSONObject(i).getString(QUERY_VALUE);
					break;
				}
			}
		}
		String where = GlNote.T.IS_AUTO.getFld().getCodeSqlField() + "=" + Sys.OYn.NO.getLine().getKey();
		if (Str.isEmpty(diySql) == false) {
			where += " AND " + diySql;
			list = BeanBase.list(GlNote.class, where, false);
		} else if (tableCode != null && !"".equals(tableCode)) {
			where += " AND bill = ?";
			list = BeanBase.list(GlNote.class, where, false,
			    ((BeanBill) Class.forName(tableCode).newInstance()).load(_bean.getPkey()).gtLongPkey());
		}
		JSONObject lineJson = null;
		for (GlNote line : list) {
			lineJson = crtJsonByBean(line);
			if (line.getExtTable() != null) {
				JSONObject j = GlNoteDAO.loadExt(line);
				j.remove("success");
				lineJson.put("extContent", j.toString().replaceAll("[\"{}]", "") + "");
			} else
				lineJson.put("extContent", "扩展属性表:无");
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	}

	@Override
	public void list() throws Exception {
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = Str.isEmpty(getQuery()) ? crtFilter() : crtQuery();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(getLimit());
		page.setWhere(where);
		page.commit();
		List<GlNote> list = page.getList();
		JSONObject lineJson = null;
		for (GlNote line : list) {
			lineJson = crtJsonByBean(line);
			lineJson.put("code", ((IBill) line.gtBill()).getCode());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, page.getCount());
		writerOrExport(json);
	}

	public void list4Writeoff() throws Exception {//销账管理 
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = Str.isEmpty(getQuery()) ? crtFilter() : crtQuery();
		where = "ext_table in (4401,4403,4405,4601,4603,4605) and " + where;
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(getLimit());
		page.setWhere(where);
		page.commit();
		List<GlNote> list = page.getList();
		JSONObject lineJson = null;
		for (GlNote line : list) {
			lineJson = crtJsonByBean(line);
			lineJson.put("code", ((IBill) line.gtBill()).getCode());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, page.getCount());
		writerOrExport(json);
	}

	public void list4WriteoffLine() throws Exception {//销账管理 返回销账明细的记录
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		GlNote note = GlNote.load(GlNote.class, getPkey());
		List<GlNote> lines = new ArrayList<GlNote>();
		Class zz = Class.forName(note.gtExtTable().getCode() + "Line");
		List<Object> objs = BeanBase.list(zz, "main_note=?", false, getPkey());
		JSONObject lineJson = null;
		for (Object obj : objs) {
			Date tallyDate = (Date) zz.getMethod("getTallyDate").invoke(obj);
			if (tallyDate == null)
				continue;
			GlNote line = GlNote.load(GlNote.class, ((Bean) obj).getPkey());
			lineJson = crtJsonByBean(line, "bean.");
			lineJson.put("bean.code", ((IBill) line.gtBill()).getCode());
			lineJson.put("bean.tallyDate", toDateJson(tallyDate));
			ja.put(lineJson);

		}
		json.put(STORE_ROOT, ja);
		json.put("success", true);
		writerOrExport(json);
	}
}
