package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysCom;
import irille.core.sys.SysPersonLink;
import irille.core.sys.SysPersonLinkDAO;
import irille.core.sys.SysSupplier;
import irille.core.sys.SysSupplierDAO;
import irille.core.sys.Sys.OLinkType;
import irille.gl.gl.GlJournalDAO;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SysSupplierAction extends ActionBase<SysSupplier> {
	public SysCom _one;
	private String params;

	@Override
	public Class beanClazz() {
		return SysSupplier.class;
	}

	public SysSupplier getBean() {
		return _bean;
	}

	public void setBean(SysSupplier bean) {
		this._bean = bean;
	}

	public SysCom getOne() {
		return _one;
	}

	public void setOne(SysCom one) {
		_one = one;
	}

	public String getParams() {
		return params;
	}

	public void setParams(String params) {
		this.params = params;
	}

	/**
	 * 初始倾听界面
	 */
	public void init() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		json.put("sysUser", getLoginSys().getPkey() + "##" + getLoginSys().getName());
		json.put("sysDept", getLoginSys().getDept() + "##" + getLoginSys().gtDept().getName());
		json.put("sysOrg", getLoginSys().getOrg() + "##" + getLoginSys().gtOrg().getName());
		response.getWriter().print(json.toString());
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
		JSONArray arr = SysSupplierDAO.autoComplete(getStart(), getLimit(), code, diy);
		JSONObject obj = new JSONObject();
		obj.put("success", "true");
		obj.put("items", arr);
		HttpServletResponse response = ServletActionContext.getResponse();
		response.getWriter().print(obj.toString());
	}

	/**
	 * 读取供应商名称
	 */
	public void loadName() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		json.put("name", BeanBase.load(SysSupplier.class, getPkey()).getName());
		response.getWriter().print(json.toString());
	}

	public void loadInfoDetail() throws IOException, JSONException {
		SysSupplier sup = SysSupplier.chkUniqueCode(false, getSarg1());
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		if (sup != null) {
			json.put("supplier", sup.getPkey() + BEAN_SPLIT + sup.getCode());
			json.put("supname", sup.getName());
			SysPersonLink link = SysPersonLinkDAO.getDefault(sup, OLinkType.PUR);
			if (link != null) {
				json.put("goodsbyName", link.getName());
				json.put("goodsbyAddr", link.getOfAddr());
				json.put("goodsbyMobile", link.getPeMobile());
				json.put("goodsbyTel", link.getOfTel());
			}
		}
		response.getWriter().print(json.toString());
	}

	public void loadPram() throws IOException, JSONException {
		HttpServletResponse response = ServletActionContext.getResponse();
		JSONObject json = new JSONObject();
		List<SysPersonLink> warelinks = Idu.getLines(SysPersonLink.T.TB_OBJ_LONG,
		    BeanBase.load(SysSupplier.class, getPkey()).gtLongPkey());
		if (warelinks.size() > 0) {
			SysPersonLink link = warelinks.get(0);
			json.put("shipName", link.getName());
			json.put("shipAddr", link.getOfAddr());
			json.put("shipMobile", link.getPeMobile());
			json.put("shipTel", link.getOfTel());
		}
		json.put("name", BeanBase.load(SysSupplier.class, getPkey()).getName());
		response.getWriter().print(json.toString());
	}

	@Override
	public SysSupplier insRun() throws Exception {
		SysSupplierDAO.Ins ins = new SysSupplierDAO.Ins();
		ins.setB(_bean);
		ins._com = getOne();
		ins.commit();
		return ins.getB();
	}

	@Override
	public SysSupplier updRun() throws Exception {
		SysSupplierDAO.Upd upd = new SysSupplierDAO.Upd();
		upd.setB(_bean);
		upd._com = getOne();
		upd.commit();
		return upd.getB();
	}

	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception {
		SysCom com = ((SysSupplier) bean).gtCom();
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
		
		json.put(pref+ "pyaAmt", GlJournalDAO.getSupplierPya((SysSupplier)bean));
		return json;
	}

	/*
	 * @Override
	 * public void insAfter() {
	 * // TODO Auto-generated method stub
	 * super.insAfter();
	 * SysComDAO.Ins comIns = new SysComDAO.Ins();
	 * getCom().setPkey(getBean().gtLongPkey());
	 * getCom().setName(getBean().getName());
	 * getCom().setShortName(getBean().getShortName());
	 * getCom().setRem(getBean().getRem());
	 * comIns.setB(getCom());
	 * comIns.commit();
	 * }
	 * @Override
	 * public void updAfter() {
	 * // TODO Auto-generated method stub
	 * super.updAfter();
	 * SysComDAO.Upd comUpd = new SysComDAO.Upd();
	 * getCom().setPkey(getBean().gtLongPkey());
	 * getCom().setName(getBean().getName());
	 * getCom().setShortName(getBean().getShortName());
	 * getCom().setRem(getBean().getRem());
	 * comUpd.setB(getCom());
	 * comUpd.commit();
	 * }
	 */

	/*
	 * @Override
	 * public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref)
	 * throws Exception {
	 * SysCom comm = ((SysSupplier) bean).gtCom();
	 * if (Str.isEmpty(pref) == false)
	 * pref = "com.";
	 * for (Fld fld : comm.gtTB().getFlds()) {
	 * String fldcode = fld.getCode();
	 * if (fld instanceof FldLines || fld instanceof FldV)
	 * continue;
	 * if (fldcode.equals("pkey") ||
	 * fldcode.equals(SysPerson.T.NAME.getFld().getCode()))
	 * continue;
	 * Object obj = comm.propertyValue(fld);
	 * if (obj == null)
	 * continue;
	 * if (fld instanceof FldOutKey) {
	 * if (obj instanceof String && Str.isEmpty(obj.toString()))
	 * continue;
	 * BeanMain outkey = (BeanMain) comm.propertyValueOBJ(fld);
	 * String show = outkey.getPkey().toString();
	 * if (outkey instanceof IExtName)
	 * show = ((IExtName) outkey).getExtName();
	 * json.put(pref + fldcode, outkey.getPkey() + BEAN_SPLIT + show);
	 * continue;
	 * }
	 * if (fld instanceof FldOptCust) {
	 * OptBase opt = ((FldOptCust) fld).getOpt(true);
	 * String optname = "错误的值";
	 * IOptLine lineI = opt.chk(obj.toString());
	 * if (lineI != null)
	 * optname = lineI.getName();
	 * json.put(pref + fldcode, obj + BEAN_SPLIT + optname);
	 * continue;
	 * }
	 * if (fld.getSqlType() == Types.DATE) {
	 * json.put(pref + fldcode, toDateJson((Date) obj));
	 * continue;
	 * }
	 * if (fld.getSqlType() == Types.TIME) {
	 * json.put(pref + fldcode, toTimeJson((Date) obj));
	 * continue;
	 * }
	 * json.put(pref + fldcode, obj);
	 * }
	 * return json;
	 * }
	 */
}
