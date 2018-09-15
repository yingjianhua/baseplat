package irille.action.sys;

import irille.action.ActionBase;
import irille.core.sys.Sys;
import irille.core.sys.SysEm;
import irille.core.sys.SysEmDAO;
import irille.core.sys.SysPerson;
import irille.core.sys.SysUser;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLines;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.FldV;
import irille.pub.tb.OptBase;
import irille.pub.tb.Infs.IOptLine;

import java.sql.Types;
import java.util.Date;

import org.json.JSONObject;

public class SysEmAction extends ActionBase<SysEm> {
	public SysPerson _one;
	public SysUser _user;

	@Override
	public Class beanClazz() {
		return SysEm.class;
	}
	
	public SysEm getBean() {
		return _bean;
	}

	public void setBean(SysEm bean) {
		this._bean = bean;
	}

	public SysPerson getOne() {
		return _one;
	}

	public void setOne(SysPerson one) {
		_one = one;
	}
	
	public SysUser getUser() {
		return _user;
	}

	public void setUser(SysUser user) {
		_user = user;
	}

	@Override
	public String orderBy() {
		return " ORDER BY PKEY";
	}

	//加入一对一关联表字段的显示
	public JSONObject crtJsonExt(JSONObject lineJson, Bean bean, String pref) throws Exception {
		SysUser user = ((SysEm)bean).gtUserSys();
		if (user != null){
			String upref = pref;
			if (Str.isEmpty(pref) == false)
				upref = "user.";
			lineJson.put(upref+"loginName", user.getLoginName());
		}
		SysPerson person = ((SysEm) bean).gtPerson();
		if (Str.isEmpty(pref) == false)
			pref = "one.";
		for (Fld fld : person.gtTB().getFlds()) {
			String fldcode = fld.getCode();
			if (fld instanceof FldLines || fld instanceof FldV)
				continue;
			//删除一对一的版本号
			if (fldcode.equals("pkey") || fldcode.equals(SysPerson.T.NAME.getFld().getCode())
					|| fldcode.equals(SysPerson.T.ROW_VERSION.getFld().getCode()))
				continue;
			Object obj = person.propertyValue(fld);
			if (obj == null)
				continue;
			if (fld instanceof FldOutKey) {
				if (obj instanceof String && Str.isEmpty(obj.toString()))
					continue;
				BeanMain outkey = (BeanMain) person.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName)
					show = ((IExtName) outkey).getExtName();
				lineJson.put(pref + fldcode, outkey.getPkey() + BEAN_SPLIT + show);
				continue;
			}
			if (fld instanceof FldOptCust) {
				OptBase opt = ((FldOptCust) fld).getOpt(true);
				String optname = "错误的值";
				IOptLine lineI = opt.chk(obj.toString());
				if (lineI != null)
					optname = lineI.getName();
				lineJson.put(pref + fldcode, obj + BEAN_SPLIT + optname);
				continue;
			}
			if (fld.getSqlType() == Types.DATE) {
				lineJson.put(pref + fldcode, toDateJson((Date) obj));
				continue;
			}
			if (fld.getSqlType() == Types.TIME) {
				lineJson.put(pref + fldcode, toTimeJson((Date) obj));
				continue;
			}
			lineJson.put(pref + fldcode, obj);
		}
		return lineJson;
	}

	@Override
	public SysEm insRun() throws Exception {
		SysEmDAO.Ins ins = new SysEmDAO.Ins();
		ins.setB(_bean);
		ins._person = getOne();
		ins._loginName = getUser().getLoginName();
		ins.commit();
		return ins.getB();
	}

	@Override
	public SysEm updRun() throws Exception {
		SysEmDAO.Upd upd = new SysEmDAO.Upd();
		upd.setB(_bean);
		upd._person = getOne();
		upd.commit();
		return upd.getB();
	}
	
}
