package irille.core.prv;

import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBase;
import irille.pub.idu.IduUpd;
import irille.pub.svr.ProvDataCtrl;

import java.util.ArrayList;
import java.util.List;

public class PrvTranDataDAO {

	private static ArrayList<PrvTranData> _tranDataList = new ArrayList();

	public static void add(PrvTranData bean) {
		_tranDataList.add(bean);
	}

	public static void setTranDataList() {
		_tranDataList = new ArrayList();
	}

	public static void clearTranDataBean() {
		String ids = ",0";
		for (PrvTranData line : _tranDataList) {
			ids += "," + line.getPkey();
		}
		List<PrvTranData> list = BeanBase.list(PrvTranData.class, "pkey not in (" + ids.substring(1) + ")", false);
		for (PrvTranData line : list) {
			line.del();
		}
	}

	public static class Upd extends IduUpd<Upd, PrvTranData> {

		public void before() {
			super.before();
			PrvTranData dbBean = loadThisBeanAndLock();
			PropertyUtils.copyProperties(dbBean, getB(), PrvTranData.T.GRP);
			setB(dbBean);
			ProvDataCtrl.INST.clear(getB().getTranCode());
		}

	}

}
