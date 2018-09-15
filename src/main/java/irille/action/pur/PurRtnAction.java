package irille.action.pur;

import irille.action.ActionForm;
import irille.core.sys.SysShiping;
import irille.pss.pur.PurRtn;
import irille.pss.pur.PurRtnDAO;
import irille.pss.pur.PurRtnLine;
import irille.pub.Str;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanMain;
import irille.pub.inf.IExtName;

import java.util.Date;
import java.util.List;

import org.json.JSONObject;

public class PurRtnAction extends ActionForm<PurRtn, PurRtnLine> {

	public SysShiping _ship;

	public SysShiping getShip() {
		return _ship;
	}

	public void setShip(SysShiping ship) {
		_ship = ship;
	}

	@Override
	public Class beanClazz() {
		return PurRtn.class;
	}

	public PurRtn getBean() {
		return _bean;
	}

	public void setBean(PurRtn bean) {
		this._bean = bean;
	}

	public List<PurRtnLine> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PurRtnLine> listLine) {
		_listLine = listLine;
	}

	@Override
	public PurRtn insRun() throws Exception {
		PurRtnDAO.Ins ins = new PurRtnDAO.Ins();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}

	@Override
	public PurRtn updRun() throws Exception {
		PurRtnDAO.Upd ins = new PurRtnDAO.Upd();
		ins.setB(_bean);
		ins._ship = getShip();
		ins.setLines(_listLine);
		ins.commit();
		return ins.getB();
	}
	
	private Object nv(Object obj) {
		if (obj == null)
			return null;
		if (obj instanceof Date)
			return toTimeJson((Date) obj); // 注意类型
		if (obj instanceof BeanMain) {
			BeanMain b = (BeanMain) obj;
			return b.getPkey() + BEAN_SPLIT + ((IExtName) b).getExtName();
		}
		return obj;
	}
}
