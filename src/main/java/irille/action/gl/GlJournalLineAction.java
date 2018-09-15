package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.GlDaybookLine;
import irille.gl.gl.GlJournalLine;
import irille.pub.Str;
import irille.pub.idu.IduPage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

public class GlJournalLineAction extends ActionBase<GlJournalLine> {

	public GlJournalLine getBean() {
		return _bean;
	}

	public void setBean(GlJournalLine bean) {
		this._bean = bean;
	}
	
	@Override
	public Class beanClazz() {
	  return GlJournalLine.class;
	}
	
	public void list() throws Exception {//通过一对一的关系将流水账中的内容同时返回给前台
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		// 目前过滤器的搜索，是肯定会带初始条件的
		String where = Str.isEmpty(getQuery()) ? crtFilter() : crtQuery();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(getLimit());
		page.setWhere(where);
		page.commit();
		List<GlJournalLine> list = page.getList();
		page.setWhere(where.replaceAll("main_pkey", "journal"));
		page.setClazz(GlDaybookLine.class);
		page.commit();
		List<GlDaybookLine> list2 = page.getList();
		Map<Long,GlDaybookLine> map = new HashMap<Long, GlDaybookLine>();
		for(GlDaybookLine line:list2) {
			map.put(line.getPkey(), line);
		}
		JSONObject lineJson = null;
		GlDaybookLine eline;
		for (GlJournalLine line : list) {
			lineJson = crtJsonByBean(line);
			eline = map.get(line.getPkey());
			lineJson.put("direct", eline.getDirect());
			lineJson.put("amt", eline.getAmt());
			lineJson.put("summary", eline.getSummary());
			lineJson.put("docNum", eline.getDocNum());
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, page.getCount());
		writerOrExport(json);
	}

}
