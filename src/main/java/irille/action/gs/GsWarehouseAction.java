package irille.action.gs;

import irille.action.ActionBase;
import irille.core.sys.SysDept;
import irille.core.sys.SysPersonLink;
import irille.gl.gs.GsWarehouse;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONException;
import org.json.JSONObject;

public class GsWarehouseAction extends ActionBase<GsWarehouse> {
	public GsWarehouse getBean() {
		return _bean;
	}

	public void setBean(GsWarehouse bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GsWarehouse.class;
	}

	@Override
	public void setPkey(String pkey) {
		super.setPkey(pkey.split("##")[0]);
	}

	@Override
	public void setPkeys(String pkeys) {
		String pks[] = pkeys.split(",");
		String pk = "";
		for (String p : pks) {
			pk += p.split("##")[0] + ",";
		}
		super.setPkeys(pk);
	}

	@Override
	public String orderBy() {
		return Idu.sqlString(" ORDER BY {0}", GsWarehouse.T.ORG);
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		GsWarehouse warehouse = (GsWarehouse) bean;
		json.remove(pref + GsWarehouse.T.PKEY.getFld().getCode());
		json.put(pref + GsWarehouse.T.PKEY.getFld().getCode(), warehouse.getPkey() + BEAN_SPLIT + warehouse.getExtName());
		return json;
	}

	/**
	 * 读取仓库联系人信息
	 */
	public void loadParm() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		SysPersonLink.TB.getCode();// TODO
		List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG, BeanBase.get(SysDept.class, getPkey())
		    .gtLongPkey());
		if (warelinks.size() > 0) {
			SysPersonLink link = warelinks.get(0);
			json.put("shipName", link.getName());
			json.put("shipAddr", link.getOfAddr());
			json.put("shipMobile", link.getPeMobile());
			json.put("shipTel", link.getOfTel());
		}
		response.getWriter().print(json.toString());
	}

}
