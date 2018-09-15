package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.Sys;
import irille.core.sys.SysCom;
import irille.core.sys.SysCustom;
import irille.core.sys.SysCustomDAO;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysPersonLinkDAO;
import irille.core.sys.Sys.OLinkType;
import irille.gl.gl.GlJournalDAO;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.idu.IduPage;
import irille.pub.inf.IExtName;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SysCustomAction extends ActionBase<SysCustom> {
	private String params; //接受
	public SysCom _one;

	@Override
	public Class beanClazz() {
		return SysCustom.class;
	}

	public SysCustom getBean() {
		return _bean;
	}

	public void setBean(SysCustom bean) {
		this._bean = bean;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	public SysCom getOne() {
		return _one;
	}

	public void setOne(SysCom one) {
		_one = one;
	}

	//内容下拉框远程控件
	public void autoComplete() throws Exception {
		String code = null;
		String diy = null;
		if (getParams() != null) {
			JSONArray ja = new JSONArray(getParams());
			for (int i = 0; i < ja.length(); i++) {
				JSONObject json = ja.getJSONObject(i);
				if (json.has("DIY"))
					diy = json.optString("DIY");
				if (json.has("code"))
					code = json.optString("code");
			}
		}
		JSONArray arr = SysCustomDAO.autoComplete(getStart(), getLimit(), code, diy);
		JSONObject obj = new JSONObject();
		obj.put("success", "true");
		obj.put("items", arr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(obj.toString());
	}

	//读取客户名称、业务代表
	public void loadInfo() throws IOException, JSONException {
		SysCustom cust = SysCustom.chkUniqueCode(false, getSarg1());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (cust != null) {
			json.put("cust", cust.getPkey() + BEAN_SPLIT + cust.getCode());
			json.put("custName", cust.getName());
			if (cust.getBusinessMember() != null)
				json.put("business", cust.gtBusinessMember().getPkey() + BEAN_SPLIT + cust.gtBusinessMember().getExtName());
		}
		response.getWriter().print(json.toString());
	}

	//读取客户名称、业务代表、收货人信息
	public void loadInfoDetail() throws IOException, JSONException {
		SysCustom cust = SysCustom.chkUniqueCode(false, getSarg1());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (cust != null) {
			json.put("cust", cust.getPkey() + BEAN_SPLIT + cust.getCode());
			json.put("custName", cust.getName());
			if (cust.getBusinessMember() != null)
				json.put("business", cust.gtBusinessMember().getPkey() + BEAN_SPLIT + cust.gtBusinessMember().getExtName());
			SysPersonLink link = SysPersonLinkDAO.getDefault(cust, OLinkType.SAL);
			if (link != null) {
				json.put("goodsbyName", link.getName());
				json.put("goodsbyAddr", link.getOfAddr());
				json.put("goodsbyMobile", link.getPeMobile());
				json.put("goodsbyTel", link.getOfTel());
			}
		}
		response.getWriter().print(json.toString());
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysCom com = ((SysCustom) bean).gtCom();
		String one = "";
		if (Str.isEmpty(pref) == false)
			one = "one.";
		json.put(one + SysCom.T.TEL1.getFld().getCode(), com.getTel1());
		json.put(one + SysCom.T.TEL2.getFld().getCode(), com.getTel2());
		json.put(one + SysCom.T.FAX.getFld().getCode(), com.getFax());
		json.put(one + SysCom.T.WEBSITE.getFld().getCode(), com.getWebsite());
		json.put(one + SysCom.T.ADDR.getFld().getCode(), com.getAddr());
		json.put(one + SysCom.T.ZIP_CODE.getFld().getCode(), com.getZipCode());
		json.put(one + SysCom.T.REM.getFld().getCode(), com.getRem());
		json.put(one + SysCom.T.UPDATED_BY.getFld().getCode(), com.getUpdatedBy() + BEAN_SPLIT
		    + com.gtUpdatedBy().getName());
		json.put(one + SysCom.T.UPDATED_DATE_TIME.getFld().getCode(), toTimeJson((Date) com.getUpdatedDateTime()));
		json.put(one + SysCom.T.CREATED_BY.getFld().getCode(), com.getCreatedBy() + BEAN_SPLIT
		    + com.gtCreatedBy().getName());
		json.put(one + SysCom.T.CREATED_DATE_TIME.getFld().getCode(), toTimeJson((Date) com.getCreatedDateTime()));
		
		json.put(pref+ "rvaAmt", GlJournalDAO.getCustomRva((SysCustom)bean));
		return json;
	}

	@Override
	public SysCustom insRun() throws Exception {
		SysCustomDAO.Ins ins = new SysCustomDAO.Ins();
		ins.setB(_bean);
		ins._com = getOne();
		ins.commit();
		return ins.getB();
	}

	@Override
	public SysCustom updRun() throws Exception {
		SysCustomDAO.Upd upd = new SysCustomDAO.Upd();
		upd.setB(_bean);
		upd._com = getOne();
		upd.commit();
		return upd.getB();
	}
	
}
