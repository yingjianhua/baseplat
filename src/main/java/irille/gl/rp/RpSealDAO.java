package irille.gl.rp;

import irille.core.sys.Sys.OEnabled;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.idu.Idu;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;

import java.util.List;

public class RpSealDAO {
	public static final Log LOG = new Log(RpSealDAO.class);

	public static class Ins extends IduIns<IduIns, RpSeal> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			getB().setEnabled(OEnabled.TRUE.getLine().getKey());
			getB().setType(getB().gtWorkBox().getType());
			getB().setUserSys(getB().gtWorkBox().getUserSys());
			getB().setMngCell(getB().gtWorkBox().getMngCell());
			super.before();
		}

		@Override
		public void after() {
			// TODO Auto-generated method stub
			super.after();
			List<RpWorkBoxGoods> list = Idu.getLinesTid(getB().gtWorkBox(), RpWorkBoxGoods.class);
			RpWorkBoxGoods bg = new RpWorkBoxGoods();
			bg.setBoxGoods(getB().gtLongPkey());
			bg.setName(getB().getName());
			bg.setUserSys(getB().getUserSys());
			bg.setDate(Env.getWorkDate());
			bg.setRem(getB().getRem());
			list.add(bg);
			IduInsLines.updLineTid(getB().gtWorkBox(), list, RpWorkBoxGoods.class);
		}
	}

	public static class Upd extends IduUpd<IduUpd, RpSeal> {

		@Override
		public void before() {
			RpSeal dbBean = loadThisBeanAndLock();
			PropertyUtils.copyProperties(dbBean, getB(), RpSeal.T.NAME, RpSeal.T.REM);
			setB(dbBean);
		}
	}
	public static class Del extends IduDel<Del, RpSeal> {

		@Override
		public void valid() {
			super.valid();
			haveBeUsed(RpHandoverLine.class, RpHandoverLine.T.BOX_GOODS, b.gtLongPkey());
		}
	}

}
