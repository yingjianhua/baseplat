package irille.gl.rp;

import irille.core.sys.Sys;
import irille.core.sys.SysCellDAO;
import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.Bean;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.util.List;

public class RpHandoverDAO {
	public static final Log LOG = new Log(RpHandoverDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		noLines("明细信息不可为空!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	public static class Ins extends IduInsLines<Ins, RpHandover, RpHandoverLine> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err(Msgs.noLines);
			getB().setCode(SysSeqDAO.getSeqnumForm(RpHandover.TB));
			getB().stStatus(STATUS_INIT);
			getB().setOrg(getUser().getOrg());
			getB().setDept(getUser().getDept());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
			getB().stCell(SysCellDAO.getCellByUser(getUser()));
			getB().setDescBy(getB().gtWorkBox().getUserSys());
			getB().setWorkBoxName(getB().gtWorkBox().getName());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}
	}

	public static class Upd extends IduUpdLines<Upd, RpHandover, RpHandoverLine> {

		@Override
		public void before() {
			super.before();
			if (getLines() == null || getLines().size() == 0)
				throw LOG.err(Msgs.noLines);
			RpHandover rev = loadThisBeanAndLock();
			PropertyUtils.copyProperties(rev, getB(), RpHandover.T.SOURCE, RpHandover.T.WORK_BOX,
			    RpHandover.T.REM, RpHandover.T.GIVE_UP_TIME);
			setB(rev);
			getB().setDescBy(getB().gtWorkBox().getUserSys());
			getB().setWorkBoxName(getB().gtWorkBox().getName());
			updLineTid(getB(), getLines(), RpHandoverLine.class);
		}
	}

	public static class Del extends IduDel<Del, RpHandover> {

		@Override
		public void valid() {
			super.valid();
			RpHandover hand = loadThisBeanAndLock();
			assertStatusIsInit(hand);
		}

		@Override
		public void before() {
			super.before();
			delLineTid(getB(), RpHandoverLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, RpHandover> {

		@Override
		public void run() {
			super.run();
			RpHandover handover = loadThisBeanAndLock();
			assertStatusIsInit(handover);
			handover.stStatus(Sys.OBillStatus.CHECKED);
			handover.setApprBy(getUser().getPkey());
			handover.setApprTime(Env.getTranBeginTime());
			handover.upd();
			List<RpWorkBoxGoods> list = getLinesTid(handover.gtWorkBox(), RpWorkBoxGoods.class);
			long pkey = lineFirstPkey(handover.getWorkBox());
			if (list.size() == 0)
				pkey = pkey + 1;
			else
				pkey = list.get(0).getPkey() + 1;

			for (Object line : getLinesTid(getB(), RpHandoverLine.class)) {
				RpHandoverLine handLine = (RpHandoverLine) line;
				Bean bean = handLine.gtBoxGoods();
				if (bean instanceof RpSeal) {
					RpSeal seal = (RpSeal) bean;
					seal.setWorkBox(handover.getWorkBox());
					seal.setUserSys(handover.gtWorkBox().getUserSys());
					seal.setMngCell(handover.gtWorkBox().getMngCell());
					seal.upd();
				}else if(bean instanceof RpJournal){
					RpJournal journal = (RpJournal)bean;
					journal.setWorkBox(handover.getWorkBox());
					journal.setCashier(handover.gtWorkBox().getUserSys());
					journal.upd();
				}// TODO 别的类型
				String updSql = Idu.sqlString("update {0} set {1}=? where {2}=?", RpWorkBoxGoods.class,
				    RpWorkBoxGoods.T.PKEY, RpWorkBoxGoods.T.BOX_GOODS);
				BeanBase.executeUpdate(updSql, pkey++, handLine.gtBoxGoods().gtLongPkey());
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, RpHandover> {
		@Override
		public void run() {
			super.run();
			RpHandover rev = loadThisBeanAndLock();
			assertStatusIsCheck(rev);
			rev.stStatus(Sys.OBillStatus.INIT);
			rev.setApprBy(null);
			rev.setApprTime(null);
			rev.upd();
		}
	}
}
