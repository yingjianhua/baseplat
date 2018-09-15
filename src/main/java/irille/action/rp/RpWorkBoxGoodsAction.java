package irille.action.rp;

import irille.action.ActionLineTid;
import irille.gl.rp.RpWorkBoxGoods;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanForm;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;
import irille.pub.tb.Fld;
import irille.pub.tb.FldLongTbObj;
import irille.pub.tb.FldOptCust;
import irille.pub.tb.FldOutKey;
import irille.pub.tb.OptBase;
import irille.pub.tb.Infs.IOptLine;

import java.sql.Types;
import java.util.Date;

import org.json.JSONObject;

public class RpWorkBoxGoodsAction extends ActionLineTid<RpWorkBoxGoods> {

	public RpWorkBoxGoods getBean() {
		return _bean;
	}

	public void setBean(RpWorkBoxGoods bean) {
		this._bean = bean;
	}

	@Override
	public Class beanClazz() {
		return RpWorkBoxGoods.class;
	}

}
