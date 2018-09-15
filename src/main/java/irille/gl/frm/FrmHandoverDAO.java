package irille.gl.frm;

import irille.core.sys.SysSeqDAO;
import irille.pub.Log;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.util.List;

public class FrmHandoverDAO {
	public static final Log LOG = new Log(FrmHandoverDAO.class);
	
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		notFound("{0}[{1}]的对应的[{2}]已处理，不能进行当前操作");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on
	
	
	public static class Ins extends IduInsLines<Ins, FrmHandover, FrmHandoverLine> {
		@Override
		public void before() {
			// TODO Auto-generated method stub
			super.before();
			getB().setCode(SysSeqDAO.getSeqnumForm(FrmHandover.TB));
			getB().stStatus(STATUS_INIT);
			getB().setOrg(getUser().getOrg());
			getB().setDept(getUser().getDept());
			getB().setCreatedBy(getUser().getPkey());
			getB().setCreatedTime(Env.INST.getWorkDate());
		}

		@Override
		public void after() {
			super.after();
			insLineTid(getB(), getLines());
		}

	}
	
	public static class Upd extends IduUpdLines<Upd, FrmHandover, FrmHandoverLine> {

		@Override
		public void before() {
			super.before();
			FrmHandover handover = loadThisBeanAndLock();
			assertStatusIsInit(handover);
			handover.setApprBy(getB().getApprBy());
			handover.setRem(getB().getRem());
			setB(handover);
			updLineTid(getB(), getLines(), FrmHandoverLine.class);
		}
	}

	public static class Del extends IduDel<Del, FrmHandover> {

		@Override
		public void before() {
			super.before();
			FrmHandover handover = loadThisBeanAndLock();
			assertStatusIsInit(handover);
			delLineTid(getB(), FrmHandoverLine.class);
		}

	}

	public static class Approve extends IduApprove<Approve, FrmHandover> {

		@Override
		public void run() {
			super.run();
			FrmHandover handover = loadThisBeanAndLock();
			assertStatusIsInit(handover);
			handover.stStatus(STATUS.CHECKED);
			handover.setCreatedTime(Env.INST.getWorkDate());
			handover.upd();
			setB(handover);
			if (getB().gtApprBy().getOrg().equals(getB().gtCreatedBy().getOrg()) == false)
				throw LOG.err("errOrg", "交出人与接收人不在同一机构，不可操作!");
			//插入交接单号到待处理单据
			String sql = "UPDATE " + FrmPending.TB.getCodeSqlTb() + " SET " 
					+ FrmPending.T.FORM_HANDOVER.getFld().getCodeSqlField() 
					+ "=? WHERE " + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "=?";
			List<FrmHandoverLine> list = Idu.getLinesTid(handover, FrmHandoverLine.class);
			for (FrmHandoverLine line : list) {
				BeanBase.executeUpdate(sql, handover.getPkey(), line.getForm());
			}
		}
	}

	public static class Unapprove extends IduUnapprove<Unapprove, FrmHandover> {

		@Override
		public void run() {
			super.run();
			FrmHandover handover = loadThisBeanAndLock();
			assertStatusIsCheck(handover);
			handover.stStatus(STATUS_INIT);
			handover.upd();
			setB(handover);
			//删除待处理单据的交接单号
			String sql = "UPDATE " + FrmPending.TB.getCodeSqlTb() + " SET " 
					+ FrmPending.T.FORM_HANDOVER.getFld().getCodeSqlField() 
					+ "=? WHERE " + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "=?";
			List<FrmHandoverLine> list = Idu.getLinesTid(handover, FrmHandoverLine.class);
			for (FrmHandoverLine line : list) {
				BeanBase.executeUpdate(sql, null, line.getForm());
			}
		}
	}
	
	public static class Take extends IduOther<Take, FrmHandover> {
		
		@Override
		public void run() {
			super.run();
			FrmHandover handover = loadThisBeanAndLock();
			assertStatusIsCheck(handover);
			if (handover.getApprBy().equals(getUser().getPkey()) == false)
				throw LOG.err("errUser", "当前用户不是接收人[{0}]，不可接收!", handover.gtApprBy().getName());
			handover.stStatus(STATUS.DONE);
			handover.setApprBy(getUser().getPkey());
			handover.setApprTime(Env.INST.getWorkDate());
			handover.upd();
			setB(handover);
			//删除待处理单据的交接单号,并将当前用户设置为系统用户
			String sql = "UPDATE " + FrmPending.TB.getCodeSqlTb() + " SET " 
					+ FrmPending.T.FORM_HANDOVER.getFld().getCodeSqlField() 
					+ "=? , " + FrmPending.T.USER_SYS.getFld().getCodeSqlField() 
					+ "=? WHERE " + FrmPending.T.ORIG_FORM.getFld().getCodeSqlField() + "=?";
			List<FrmHandoverLine> list = Idu.getLinesTid(handover, FrmHandoverLine.class);
			for (FrmHandoverLine line : list) {
				BeanBase.executeUpdate(sql, null, getUser().getPkey(), line.getForm());
			}
		}
	}
}
