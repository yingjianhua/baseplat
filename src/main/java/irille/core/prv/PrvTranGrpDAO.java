package irille.core.prv;

import irille.core.prv.Prv.OPrvType;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.svr.ProvDataCtrl;

import java.util.List;

public class PrvTranGrpDAO {
	
	public static class Ins extends IduIns<Ins, PrvTranGrp> {
		@Override
		public void after() {
		  super.after();
		  List<PrvRole> roles = PrvRole.list(PrvRole.class, "", false);
			for(PrvRole role: roles) {
				PrvRoleTran roleTran = new PrvRoleTran();
				roleTran.stRole(role);
				roleTran.stGrp(getB());
				roleTran.stType(OPrvType.DEFAULT);
				roleTran.setDay(0);
				roleTran.ins();
			}
		}
	}
	public static class Del extends IduDel<Del, PrvTranGrp> {
		
		@Override
		public void before() {
		  super.before();
				String where = Idu.sqlString("{0}=?", PrvRoleTran.T.GRP);
				List<PrvRoleTran> trans = PrvRoleTran.list(PrvRoleTran.class, where, true, getB().getPkey());
				for(PrvRoleTran line: trans) {
					line.del();
				}
				List<PrvTranData> datas = PrvTranData.list(PrvTranData.class, where, true, getB().getPkey());
				for(PrvTranData line: datas) {
					line.setGrp(null);
					line.upd();
				}
				ProvDataCtrl.INST.clearAll();
		}
	}
}
