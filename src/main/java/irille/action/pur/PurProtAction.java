package irille.action.pur;

import irille.action.ActionBase;
import irille.pss.pur.PurProt;
import irille.pss.pur.PurProtGoods;
import irille.pub.bean.BeanBase;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

public class PurProtAction extends ActionBase<PurProt> {


	@Override
	public Class beanClazz() {
		return PurProt.class;
	}

	public PurProt getBean() {
		return _bean;
	}

	public void setBean(PurProt bean) {
		this._bean = bean;
	}

}
