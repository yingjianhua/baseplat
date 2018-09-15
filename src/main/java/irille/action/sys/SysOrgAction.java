package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.SysCom;
import irille.core.sys.SysOrg;
import irille.core.sys.SysOrgDAO;
import irille.pub.Str;
import irille.pub.bean.Bean;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class SysOrgAction extends ActionBase<SysOrg> {
	public SysCom _one;

	@Override
	public Class beanClazz() {
		return SysOrg.class;
	}
	
	public SysOrg getBean() {
		return _bean;
	}

	public void setBean(SysOrg bean) {
		this._bean = bean;
	}
	
	public SysCom getOne() {
		return _one;
	}

	public void setOne(SysCom one) {
		_one = one;
	}

	@Override
	public String orderBy() {
		return " ORDER BY PKEY";
	}

	//加入一对一关联表字段的显示
	public JSONObject crtJsonExt(JSONObject json, Bean bean, String pref) throws Exception{
		SysCom com = ((SysOrg)bean).gtCom();
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
		json.put(one + SysCom.T.UPDATED_BY.getFld().getCode(), com.getUpdatedBy()+BEAN_SPLIT+com.gtUpdatedBy().getName());
		json.put(one + SysCom.T.UPDATED_DATE_TIME.getFld().getCode(), toTimeJson((Date) com.getUpdatedDateTime()));
		json.put(one + SysCom.T.CREATED_BY.getFld().getCode(), com.getCreatedBy()+BEAN_SPLIT+com.gtCreatedBy().getName());
		json.put(one + SysCom.T.CREATED_DATE_TIME.getFld().getCode(), toTimeJson((Date) com.getCreatedDateTime()));
	  return json;
	}
	
	@Override
	public SysOrg insRun() throws Exception {
		SysOrgDAO.Ins ins = new SysOrgDAO.Ins();
		ins.setB(_bean);
		ins._com = getOne();
		ins.commit();
		return ins.getB();
	}
	
	@Override
	public SysOrg updRun() throws Exception {
		SysOrgDAO.Upd upd = new SysOrgDAO.Upd();
		upd.setB(_bean);
		upd._com = getOne();
		upd.commit();
		return upd.getB();
	}
	
	public void ded() throws Exception {
		SysOrgDAO.DayEnd ded = new SysOrgDAO.DayEnd();
		ded.setBKey(getPkey());
		ded.commit();
		writeSuccess(ded.getB());
	}
	
	public void getFields() throws Exception{
		HttpServletResponse response = ServletActionContext.getResponse();
		String fields = "{name:'bean.pkey',mapping:'pkey',type:'int',useNull:true},{name:'bean.code',mapping:'code',type:'string'},{name:'bean.name',mapping:'name',type:'string'},{name:'bean.shortName',mapping:'shortName',type:'string'},{name:'bean.enabled',mapping:'enabled',type:'int',useNull:true},{name:'bean.orgUp',mapping:'orgUp',type:'string',outkey:true},{name:'bean.workDate',mapping:'workDate',type:'date'},{name:'bean.state',mapping:'state',type:'int',useNull:true},{name:'bean.templat',mapping:'templat',type:'string',outkey:true},{name:'bean.valuationMethods',mapping:'valuationMethods',type:'int',useNull:true},{name:'bean.internationTrade',mapping:'internationTrade',type:'int',useNull:true},{name:'bean.currency',mapping:'currency',type:'int',useNull:true},{name:'one.tel1',mapping:'tel1',type:'string'},{name:'one.tel2',mapping:'tel2',type:'string'},{name:'one.fax',mapping:'fax',type:'string'},{name:'one.website',mapping:'website',type:'string'},{name:'one.addr',mapping:'addr',type:'string'},{name:'one.zipCode',mapping:'zipCode',type:'string'},{name:'one.rem',mapping:'rem',type:'string'},{name:'one.updatedBy',mapping:'updatedBy',type:'string',outkey:true},{name:'one.updatedDateTime',mapping:'updatedDateTime',type:'date',dateFormat:'Y-m-dH:i:s'},{name:'one.createdBy',mapping:'createdBy',type:'string',outkey:true},{name:'one.createdDateTime',mapping:'createdDateTime',type:'date',dateFormat:'Y-m-dH:i:s'}";
		response.getWriter().print(fields);
	}
}
