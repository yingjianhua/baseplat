package irille.pss.sal;

import irille.gl.gs.GsPub;
import irille.gl.gs.Gs.OEnrouteType;
import irille.pub.Cn;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.Bean;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

public class SalReserveDAO {
	public static final Log LOG = new Log(SalReserveDAO.class);

	public static class Ins extends IduInsLines<Ins, SalReserve, SalReserveLine> {
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			initForm(getB());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}
		
	}

	public static class Upd extends IduUpdLines<Upd, SalReserve, SalReserveLine> {

		@Override
		public void before() {
			super.before();
			SalReserve mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			PropertyUtils.copyPropertiesWithout(mode, getB(), SalReserve.T.PKEY, SalReserve.T.CODE, SalReserve.T.STATUS,
			    SalReserve.T.ORG, SalReserve.T.DEPT, SalReserve.T.CREATED_BY, SalReserve.T.CREATED_TIME);
			setB(mode);
			Idu.checkFormGoods(getLines());
			updLineTid(getB(), getLines(), SalReserveLine.class);
		}
	}

	public static class Del extends IduDel<Del, SalReserve> {

		@Override
		public void valid() {
			super.valid();
			assertStatusIsInit(getB());
		}

		@Override
		public void before() {
			super.before();
			delLineTid(getB(), SalReserveLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, SalReserve> {

		@Override
		public void run() {
			super.run();
			SalReserve mode = loadThisBeanAndLock();
			assertStatusIsInit(mode);
			mode.stStatus(STATUS.CHECKED);
			mode.setApprBy(getUser().getPkey());
			mode.setApprTime(Env.INST.getWorkDate());
			mode.upd();
			setB(mode);
			GsPub.insertStimate(mode, mode.gtWarehouse(), getLinesTid(mode, SalReserveLine.class), OEnrouteType.QTSD,
			    mode.getExpireDate());
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, SalReserve> {

		@Override
		public void run() {
			super.run();
			SalReserve mode = loadThisBeanAndLock();
			assertStatusIsCheck(mode);
			mode.stStatus(STATUS_INIT);
			mode.setApprBy(null);
			mode.setApprTime(null);
			mode.upd();
			setB(mode);
			GsPub.deleteStimate(mode, mode.gtWarehouse(), getLinesTid(mode, SalReserveLine.class), OEnrouteType.QTSD);
		}
	}

	public static class DoClose extends IduOther<DoClose, SalReserve> {
		public static Cn CN = new Cn("doClose", "关闭");

		@Override
		public void run() {
			super.run();
			assertStatusIsCheck(getB());
			SalReserve mode = loadThisBeanAndLock();
			mode.stStatus(STATUS.DONE);
			mode.upd();
			setB(mode);
			GsPub.deleteStimate(mode, mode.gtWarehouse(), getLinesTid(mode, SalReserveLine.class), OEnrouteType.QTSD);
		}
	}

}
