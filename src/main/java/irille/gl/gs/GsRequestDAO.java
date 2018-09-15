package irille.gl.gs;

import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;
import irille.pub.tb.Tb;

import java.util.List;

public class GsRequestDAO {

	public static final Log LOG = new Log(GsRequestDAO.class);

	public static class Ins extends IduInsLines<Ins, GsRequest, GsRequestLine> {
		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			getB().setCode(SysSeqDAO.getSeqnum(GsRequest.TB, getB().gtWarehouse().gtOrg(), true));
			getB().stStatus(STATUS_INIT);
			getB().setOrg(getB().gtWarehouse().getOrg());
			getB().setDept(getB().getWarehouse());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getSystemTime());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}

	public static class Upd extends IduUpdLines<Upd, GsRequest, GsRequestLine> {

		@Override
		public void before() {
			super.before();
			Idu.checkFormGoods(getLines());
			GsRequest request = loadThisBeanAndLock();
			assertStatusIsInit(request);
			GsWarehouse warehouse = request.gtWarehouse();
			if (warehouse.getOrg() != getB().gtWarehouse().getOrg())
				throw LOG.err("errOrg", "仓库不属于机构[{0}]，请重新选择！", warehouse.gtOrg().getName());
			request.setWarehouse(getB().getWarehouse());
			request.setRem(getB().getRem());
			setB(request);
			updLineTid(getB(), getLines(), GsRequestLine.class);
		}
	}

	public static class Del extends IduDel<Del, GsRequest> {

		@Override
		public void before() {
			super.before();
			GsRequest request = loadThisBeanAndLock();
			assertStatusIsInit(request);
			delLineTid(getB(), GsRequestLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, GsRequest> {

		@Override
		public void run() {
			super.run();
			GsRequest req = loadThisBeanAndLock();
			assertStatusIsInit(req);
			req.stStatus(STATUS.CHECKED);
			req.setApprBy(getUser().getPkey());
			req.setApprTime(Env.INST.getSystemTime());
			req.upd();
			setB(req);
			GsWarehouse gw = req.gtWarehouse();
			List<GsRequestLine> lines = getLinesTid(req, GsRequestLine.class);
			GsDemandDAO.insertByRequest(req, lines);
			GsPub.insertStimate(req, gw, lines, Gs.OEnrouteType.YQG, null);
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, GsRequest> {

		@Override
		public void run() {
			super.run();
			GsRequest req = loadThisBeanAndLock();
			assertStatusIsCheck(req);
			req.stStatus(STATUS_INIT);
			req.setApprBy(null);
			req.setApprTime(null);
			req.upd();
			setB(req);
			GsWarehouse gw = req.gtWarehouse();
			List<GsRequestLine> lines = getLinesTid(req, GsRequestLine.class);
			GsDemandDAO.deleteByRequest(req);
			GsPub.deleteStimate(req, gw, lines, Gs.OEnrouteType.YQG);
		}
	}
}
