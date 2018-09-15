package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysDept;
import irille.pub.Str;
import irille.pub.idu.Idu;
import irille.pub.idu.IduPage;

import java.util.List;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

public class SysDeptAction extends ActionBase<SysDept> {
	@Override
	public Class beanClazz() {
		return SysDept.class;
	}

	public SysDept getBean() {
		return _bean;
	}

	public void setBean(SysDept bean) {
		this._bean = bean;
	}

	@Override
	public String orderBy() {
		return Idu.sqlString(" ORDER BY {0},{1}", SysDept.T.ORG, SysDept.T.CODE);
	}

	public void getComboTrigger() throws Exception {
		String where = crtQueryAll();
		where += " AND enabled = 1";
		if (!Str.isEmpty(getSarg1()))
			where += " AND (" + getSarg1() + ")";
		where += orderBy();
		IduPage page = newPage();
		page.setStart(getStart());
		page.setLimit(0); // 取所有数据，下拉框不分页
		page.setWhere(where);
		page.commit();
		List<SysDept> list = page.getList();
		JSONArray ja = new JSONArray();
		JSONObject lineJson = null;
		for (SysDept line : list) {
			lineJson = new JSONObject();
			// 注意不论主键是否为STRING,都转成字符串
			// 前EXT组件初始化时,数字也是以字符形式判断其值
			lineJson.put("value", line.getPkey().toString());
			if (Str.isEmpty(line.gtOrg().getShortName()) == false)
				lineJson.put("text", line.gtOrg().getShortName() + "-" + line.getName());
			else
				lineJson.put("text", line.gtOrg().getName() + "-" + line.getName());
			ja.put(lineJson);
		}
		JSONObject json = new JSONObject();
		json.put(STORE_ROOT, ja);
		ServletActionContext.getResponse().getWriter().print(json.toString());
	}
}
