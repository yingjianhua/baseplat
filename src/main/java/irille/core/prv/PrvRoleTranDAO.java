package irille.core.prv;

import irille.pub.Cn;
import irille.pub.idu.Idu;
import irille.pub.idu.IduOther;
import irille.pub.svr.ProvDataCtrl;

import java.util.List;

public class PrvRoleTranDAO{
	public static List<PrvRoleTran> loadByRole(PrvRoleTran bean) {
		String where = Idu.sqlString("{0}=?", PrvRoleTran.T.ROLE);
		List<PrvRoleTran> list = PrvRoleTran.list(PrvRoleTran.class, where, false, bean.getRole());
		return list;
	}
	
	public static class UpdCtrl extends IduOther<UpdCtrl, PrvRole> {
		public static Cn CN = new Cn("updCtrl", "权限控制");
		public List<PrvRoleTran> _listLine;

		@Override
		public void run() {
			for(PrvRoleTran line:getListLine()) {
				PrvRoleTran bean = PrvRoleTran.load(PrvRoleTran.class, line.getPkey());
				bean.setType(line.getType());
				bean.setDay(line.getDay());
				bean.upd();
			}
			ProvDataCtrl.INST.clearAll();
		}
		
		public List<PrvRoleTran> getListLine() {
			return _listLine;
		}

		public void setListLine(List<PrvRoleTran> listLine) {
			_listLine = listLine;
		}

	}
}
