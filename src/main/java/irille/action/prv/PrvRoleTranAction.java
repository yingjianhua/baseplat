package irille.action.prv;

import irille.action.ActionBase;
import irille.core.prv.PrvRoleTran;
import irille.core.prv.PrvRoleTranDAO;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

public class PrvRoleTranAction extends ActionBase<PrvRoleTran> {
	public List<PrvRoleTran> _listLine;

	@Override
	public Class beanClazz() {
		return PrvRoleTran.class;
	}

	public PrvRoleTran getBean() {
		return _bean;
	}

	public void setBean(PrvRoleTran bean) {
		this._bean = bean;
	}
  
	public List<PrvRoleTran> getListLine() {
		return _listLine;
	}

	public void setListLine(List<PrvRoleTran> listLine) {
		_listLine = listLine;
	}

	public void updCtrl() throws Exception{
		PrvRoleTranDAO.UpdCtrl act = new PrvRoleTranDAO.UpdCtrl();
		act.setListLine(getListLine());
		act.commit();
		writeSuccess();
	}
	
	public void loadByRole() throws Exception{
		JSONObject json = new JSONObject();
		JSONArray ja = new JSONArray();
		List<PrvRoleTran> list = PrvRoleTranDAO.loadByRole(_bean);
		JSONObject lineJson = null;
		for (PrvRoleTran line : list) {
			lineJson = crtJsonByBean(line);
			ja.put(lineJson);
		}
		json.put(STORE_ROOT, ja);
		json.put(STORE_TOTAL, list.size());
		writerOrExport(json);
	}
}
