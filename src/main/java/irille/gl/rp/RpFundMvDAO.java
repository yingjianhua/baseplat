package irille.gl.rp;

import irille.core.sys.Sys;
import irille.core.sys.SysSeqDAO;
import irille.gl.frm.FrmPendingDAO;
import irille.gl.gl.Gl;
import irille.gl.gl.GlDaybook;
import irille.gl.gl.GlJournalDAO;
import irille.gl.gl.GlNote;
import irille.gl.gl.GlNoteDAO;
import irille.gl.gl.Gl.ODirect;
import irille.gl.rp.RpJournalLine.ODC;
import irille.pss.sal.Sal;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.gl.AccObjs;
import irille.pub.idu.Idu;
import irille.pub.idu.IduApprove;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduInsLines;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUnapprove;
import irille.pub.idu.IduUpdLines;
import irille.pub.svr.Env;

import java.math.BigDecimal;
import java.util.List;
/**
 * 核算单元内部资金调拨
 * 
 * 新增，修改，删除，审核，弃审，接收，接收取消，便签，记账，记账取消；
 * 
 * */

public class RpFundMvDAO {
	public static final Log LOG = new Log(RpFundMvDAO.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		samejour("相同的帐户之间不可调拨!"),
		overcell("来源账户和接收账户不在同一核算单元内"),
		overorg("接收账户或来源账户属于其他机构"),
		noplus("金额必须为正数"),
		norole("你没有权限对该资金调拨单做{0}操作!");
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on

	/**
	 * 新增： form 填写来源账户、接收账户、金额、备注；
	 * 				valid 来源账户和接收账户属于同一核算单元，且属于当前操作的出纳员所属的机构，金额 不可为负，来源账户和接收账户不能相同
	 * 				before 序号表产生code，status 为初始， 建档员 当前操作人，建档时间 当前时间，机构 操作人员所属机构，部门 操作人员所属部门，核算单元 来源账户和接收账户所属核算单元
	 * */
	public static class Ins extends IduInsLines<Ins, RpFundMv, RpFundMv> {
		@Override
		public void valid() {
		  super.valid();
		  RpFundMv bean = getB();
		  RpJournal source = bean.gtSourceJournal();
		  RpJournal desc = bean.gtDescJournal();
		  if(source.equals(desc)) {
		  	throw LOG.err(Msgs.samejour);
		  }
		  if(source.getCell()!=desc.getCell()) {
		  	throw LOG.err(Msgs.overcell);
		  }
		  if(source.gtCell().getOrg()!=Idu.getOrg().getPkey()) {
		  	throw LOG.err(Msgs.overorg);
		  }
		  if(bean.getAmt().compareTo(BigDecimal.ZERO)<=0) {
		  	throw LOG.err(Msgs.noplus);
		  }
		}
		@Override
		public void before() {
			getB().setCode(SysSeqDAO.getSeqnumForm(RpFundMv.TB));
			getB().stStatus(STATUS_INIT);

			getB().stCreatedBy(Idu.getUser());
			getB().setCreatedTime(Env.getWorkDate());
			getB().stOrg(Idu.getOrg());
			getB().stDept(Idu.getDept());
			getB().stCell(getB().gtSourceJournal().gtCell());
		}
	}
	/**
	 * 修改： form 填写来源账户、接收账户、金额、备注；
	 * 				valid 来源账户和接收账户属于同一核算单元，且属于当前操作的出纳员所属的机构，金额 不可为负，状态 初始，来源账户和接收账户不能相同
	 * 				before 核算单元 来源账户和接收账户所属核算单元,
	 * */
	public static class Upd extends IduUpdLines<Upd, RpFundMv, RpFundMv> {

		@Override
		public void valid() {
			super.valid();
			RpFundMv bean = getB();
			RpFundMv dbBean = load(getB().getPkey());
			assertStatusIsInit(dbBean);
			RpJournal source = bean.gtSourceJournal();
		  RpJournal desc = bean.gtDescJournal();
		  if(source.equals(desc)) {
		  	throw LOG.err(Msgs.samejour);
		  }
		  if(source.getCell()!=desc.getCell()) {
		  	throw LOG.err(Msgs.overcell);
		  }
		  if(source.gtCell().getOrg()!=dbBean.getOrg()) {
		  	throw LOG.err(Msgs.overorg);
		  }
		  if(bean.getAmt().compareTo(BigDecimal.ZERO)<=0) {
		  	throw LOG.err(Msgs.noplus);
		  }
		}
		@Override
		public void before() {
			
			RpFundMv dbBean = loadThisBeanAndLock();
			PropertyUtils.copyProperties(dbBean, getB(), RpFundMvOld.T.SOURCE_JOURNAL, RpFundMvOld.T.DESC_JOURNAL, RpFundMvOld.T.AMT,
			    RpFundMvOld.T.REM);
			dbBean.stCell(dbBean.gtSourceJournal().gtCell());
			setB(dbBean);
		}
	}
	/**
	 * 删除： 
	 * 				valid 状态为初始，操作人员为建档员；
	 * */
	public static class Del extends IduDel<Del, RpFundMv> {

		@Override
		public void valid() {
			super.valid();
			RpFundMv dbBean = loadThisBeanAndLock();
			assertStatusIsInit(dbBean);
		}
	}
	/**
	 * 审核：来源账户的出纳员进行审核操作 
	 * 
	 * 				valid 状态为初始，操作人员为来源账户的出纳；
	 * 				run 审核员设置为当前操作人员，审核时间为当前时间，状态设置为已审核，交出人为当前操作人员呢，交出时间为当前时间
	 * 				after 新建凭条1，分户账为来源账户的分户账，借贷方向为贷，bill为该资金调拨单，没有扩展表
	 * 							新建凭条2，分户账为存财务中心款项,借贷方向为借，bill为该资金调拨单，没有扩展表
	 * 							新建出纳日记账明细，凭条为凭条1，备注为资金调拨，出纳为来源账户的出纳
	 * 							新建待处理登记，原单据为该资金调拨单，目标单据类型为流水账
	 * */
	public static class Approve extends IduApprove<Approve, RpFundMv> {

		@Override
		public void valid() {
		  super.valid();
			RpFundMv dbBean = loadAndLock(getB().getPkey());
			assertStatusIsInit(dbBean);
			if(!dbBean.gtSourceJournal().gtCashier().equals(Idu.getUser())) {
				throw LOG.err(Msgs.norole,"审核");
			}
			setB(dbBean);
		}
		@Override
		public void run() {
			RpFundMv bean = getB();
			bean.stStatus(Sys.OBillStatus.CHECKED);
			bean.stApprBy(getUser());
			bean.setApprTime(Env.getWorkDate());
			bean.stSourceBy(getUser());
			bean.setSourceTime(bean.getApprTime());
			bean.upd();
			setB(bean);
			super.run();
		}
		@Override
		public void after() {
		  super.after();
		  RpFundMv bean = getB();
		  GlNote note = GlNoteDAO.insAuto(bean, bean.getAmt(), bean.gtSourceJournal().gtJournal(), ODirect.CR, null);
		  RpJournalLineDAO.addByBill(note, ODC.CR, "资金调拨", bean.gtSourceBy());
		  GlNoteDAO.insAuto(bean, bean.getAmt(),GlJournalDAO.getAutoCreate(bean.gtCell(), Sal.SubjectAlias.SAL_MV, new AccObjs()), ODirect.DR, null);
		  
		}
	}
	/**
	 * 弃审：审核员进行的弃审操作 
	 * 
	 * 				valid 状态为已审核;
	 * 				run 审核员设置为null，审核时间设置为null，状态设置为初始，交出人为null，交出时间为null
	 * 				after 删除凭条1，分户账为来源账户的分户账，借贷方向为贷，bill为该资金调拨单
	 * 							删除凭条2，分户账为存财务中心款项，借贷方向为借，bill为该资金调拨单 
	 * 							删除出纳日记账明细，凭条为凭条1；
	 * 							删除待处理登记，原单据为该资金调拨单
	 * */
	public static class Unapprove extends IduUnapprove<Unapprove, RpFundMv> {
		@Override
		public void valid() {
		  super.valid();
		  RpFundMv dbBean = loadThisBeanAndLock();
		  assertStatusIsCheck(dbBean);
		  setB(dbBean);
		}
		@Override
		public void run() {
			super.run();
			RpFundMv bean = getB();
			bean.stStatus(Sys.OBillStatus.INIT);
			bean.setApprBy(null);
			bean.setApprTime(null);
			bean.stSourceBy(null);
			bean.setSourceTime(null);
			bean.upd();
			setB(bean);
		}
		@Override
		public void after() {
			RpFundMv bean = getB();
			GlNoteDAO.delByBill(bean.gtLongPkey());
			RpJournalLineDAO.delByMv(bean.gtLongPkey());
			// 待处理
		  super.after();
		}
		
	}
	/**
	 * 接收：接收账户的出纳员进行接收操作 
	 * 
	 * 				valid 状态为已审核，操作人员为接收账户的出纳；
	 * 				run 接收人为当前的操作人员，接收时间为当前时间，状态为可记账
	 * 				after 新建凭条1，分户账为接收账户的分户账，借贷方向为借，bill为该资金调拨单，没有扩展表
	 * 							新建凭条2，分户账为存财务中心款项,借贷方向为贷，bill为该资金调拨单，没有扩展表
	 * 							新建出纳日记账明细，凭条为凭条1，备注为资金调拨，出纳为接收账户的出纳，借贷方向为借
	 * */
	public static class DoRec extends IduOther<DoRec, RpFundMv> {
		
		@Override
		public void valid() {
		  super.valid();
		  RpFundMv dbBean = loadAndLock(getB().getPkey());
		  assertStatusIsCheck(dbBean);
		  if(!dbBean.gtDescBy().equals(Idu.getUser())) {
		  	throw LOG.err(Msgs.norole,"接收");
		  }
		}
		public void run() {
			super.run();
			RpFundMv bean = getB();
			bean.stStatus(Sys.OBillStatus.TALLY_ABLE);
			bean.stDescBy(Idu.getUser());
			bean.setDescTime(Env.getWorkDate());
			bean.upd();
			setB(bean);
		}
		@Override
		public void after() {
		  super.after();
		  RpFundMv bean = getB();
		  GlNote note = GlNoteDAO.insAuto(bean, bean.getAmt(), bean.gtDescJournal().gtJournal(), ODirect.DR, null);
		  RpJournalLineDAO.addByBill(note, ODC.DR, "资金调拨", bean.gtDescBy());
		  GlNoteDAO.insAuto(bean, bean.getAmt(),GlJournalDAO.getAutoCreate(bean.gtCell(), Sal.SubjectAlias.SAL_MV, new AccObjs()), ODirect.CR, null);
		// 待处理
			FrmPendingDAO.ins(getB(), GlDaybook.TB);
		}
	}
	/**
	 * 接收取消：接收账户的出纳员进行审核操作 
	 * 
	 * 				valid 状态为可记账；
	 * 				run 接收人设置为null，接收时间时间为null，状态为已审核
	 * 				after 删除凭条1，分户账为接收账户的分户账，借贷方向为借，bill为该资金调拨单
	 * 							删除凭条2，分户账为存财务中心款项，借贷方向为贷，bill为该资金调拨单 
	 * 							删除出纳日记账明细，凭条为凭条1；
	 * */
	public static class UnRec extends IduOther<UnRec, RpFundMv> {
		@Override
		public void valid() {
		  super.valid();
		  RpFundMv dbBean = loadAndLock(getB().getPkey());
		  assertStatusIsTally(dbBean);
		  setB(dbBean);
		}
		@Override
		public void run() {
		  super.run();
		  RpFundMv bean = getB();
		  bean.stDescBy(null);
		  bean.setDescTime(null);
		  bean.stStatus(Sys.OBillStatus.CHECKED);
		  bean.upd();
		  setB(bean);
		}
		@Override
		public void after() {
		  super.after();
		  RpFundMv bean = getB();
		  
		  String where = Idu.sqlString("{0}=? and {1}=? and {2}=?", GlNote.T.BILL, GlNote.T.JOURNAL, GlNote.T.DIRECT);
		  System.out.println(where);
		  System.out.println(bean.gtLongPkey()+"|"+bean.gtDescJournal().gtJournal().getPkey()+"|"+Gl.ODirect.DR.getLine().getKey());
			List<GlNote> listOld = BeanBase.list(GlNote.class, where, true, bean.gtLongPkey(),bean.gtDescJournal().gtJournal().getPkey(),Gl.ODirect.DR.getLine().getKey());
			GlNoteDAO.del(listOld.get(0));

			List<RpJournalLine> list = BeanBase.list(RpJournalLine.class, "note=?", false, listOld.get(0).getPkey());
			RpJournalLineDAO.Del delDao = new RpJournalLineDAO.Del();
			for (RpJournalLine line : list) {
				delDao.setB(line);
				delDao.commit();
			}
			
			where = Idu.sqlString("{0}=? and {1}=? and {2}=?", GlNote.T.BILL, GlNote.T.JOURNAL, GlNote.T.DIRECT);
			listOld = BeanBase.list(GlNote.class, where, true, bean.gtLongPkey(),GlJournalDAO.getAutoCreate(bean.gtCell(), Sal.SubjectAlias.SAL_MV, new AccObjs()).getPkey(),Gl.ODirect.CR.getLine().getKey());
			for (GlNote line : listOld)
				GlNoteDAO.del(line);			
			
			// 待处理登记 ，可以在流水账中统一做记账处理
			FrmPendingDAO.del(getB(), GlDaybook.TB);
		}
	}
}
