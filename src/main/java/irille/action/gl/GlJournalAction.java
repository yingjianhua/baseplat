package irille.action.gl;

import irille.action.ActionBase;
import irille.gl.gl.Gl;
import irille.gl.gl.GlJournal;
import irille.gl.gl.Gl.OAccType;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanInt;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLongTbObj;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.OptBase;
import irille.pub.tb.Tb;
import irille.pub.tb.Infs.IOptLine;

import java.sql.Types;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

public class GlJournalAction extends ActionBase<GlJournal> {
	public GlJournal getBean() {
		return _bean;
	}

	public void setBean(GlJournal bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return GlJournal.class;
	}

	@Override
	public void load() throws Exception {
		HttpServletResponse response = ServletActionContext.getResponse();
		GlJournal bean = newBean().load(getPkey());
		JSONObject lineJson = crtJsonByBean(bean);
		OAccType type = bean.gtAccType();
		String value = null;
		if (type == OAccType.ONE) {
			response.getWriter().print(lineJson.toString());
			return;
		} else if (type == OAccType.MUCH) {
			value = bean.getObjPkey() + "";
		} else {
			BeanInt custom = (BeanInt) BeanBase.load(type.getObj(), bean.getObjPkey() / 100000);
			String name = (String) type.getObj().getMethod("getName").invoke(custom);
			value = custom.getPkey() + "##" + name;
		}
		lineJson.remove("objPkey");
		lineJson.put("objPkey", value);
		response.getWriter().print(lineJson.toString());
	}

	/**
	 * 主要是解决对象字段问题，当科目为多明细时，对象里填的是序号
	 */
	public JSONObject crtJsonByBean(Bean bean, String pref) throws Exception {
		JSONObject lineJson = new JSONObject();
		for (Fld fld : bean.gtTB().getFlds()) {
			String fldcode = fld.getCode();
			if (fld.isDatabaseField() == false)
				continue;
			Object obj = bean.propertyValue(fld);
			if (obj == null)
				continue;
			if (fld instanceof FldOutKey) {
				if (obj instanceof String && Str.isEmpty(obj.toString()))
					continue;
				BeanMain outkey = (BeanMain) bean.propertyValueOBJ(fld);
				String show = outkey.getPkey().toString();
				if (outkey instanceof IExtName)
					show = ((IExtName) outkey).getExtName();
				lineJson.put(pref + fldcode, outkey.getPkey() + BEAN_SPLIT + show);
				continue;
			}
			if (fld instanceof FldLongTbObj) {
				if (obj instanceof String && Str.isEmpty(obj.toString()))
					continue;
				//TODO 重写的部分
				if (((GlJournal)bean).gtSubject().gtAccType() == Gl.OAccType.MUCH) {
					lineJson.put(pref + fldcode, obj.toString() + BEAN_SPLIT + obj.toString());
					continue;
				}
				Tb tb = Tb.getTBByBean(Bean.gtLongClass(Long.parseLong(obj.toString())));
				lineJson.put(pref + fldcode, obj.toString() + BEAN_SPLIT + tb.getShortName());
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
		//外部扩展重载用
		crtJsonExt(lineJson, bean, pref);
		return lineJson;
	}

}
