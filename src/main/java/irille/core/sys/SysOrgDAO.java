//Created on 2005-9-27
package irille.core.sys;

import irille.core.sys.Sys.OOrgState;
import irille.gl.gl.GlDailyLedgerDAO;
import irille.gl.gl.GlReportAssetDAO;
import irille.gl.gl.GlReportProfitDAO;
import irille.gl.gl.GlTransformDAO;
import irille.gl.gl.GlDailyLedger.T;
import irille.pub.Cn;
import irille.pub.DateTools;
import irille.pub.Log;
import irille.pub.PropertyUtils;
import irille.pub.bean.BeanBuf;
import irille.pub.idu.IduDel;
import irille.pub.idu.IduIns;
import irille.pub.idu.IduOther;
import irille.pub.idu.IduUpd;
import irille.pub.svr.Env;
import irille.pub.svr.ProvDataCtrl;

import java.util.Date;

public class SysOrgDAO {
	public static final Log LOG = new Log(SysOrgDAO.class);

	public static class Ins extends IduIns<Ins, SysOrg> {
		public SysCom _com = null;

		@Override
		public void before() {
			super.before();
			getB().stEnabled(true);
			getB().setWorkDate(Env.INST.getSystemTime());
			getB().stState(OOrgState.OPENING);
			if (getB().getOrgUp() != null)
				if(!getB().getCode().startsWith(getB().gtOrgUp().getCode()))
					throw LOG.err("codeError","机构代码[{0}]需要以上级机构代码[{1}]开头",getB().getCode(),getB().gtOrgUp().getCode());
			if (SysOrg.chkUniqueCode(false, getB().getCode()) != null)
				throw LOG.err("notFound", "机构代码[{0}]已存在,不可重复新增!", getB().getCode());
		}

		@Override
		public void after() {
			super.after();
			_com.setPkey(getB().gtLongPkey());
			_com.setName(getB().getName());
			_com.setShortName(getB().getShortName());
			SysComDAO.Ins cins = new SysComDAO.Ins();
			cins.setB(_com);
			cins.commit();
			ProvDataCtrl.initOrgMap();//将缓存中存储的机构上下级关系更新
		}

	}

	public static class Upd extends IduUpd<Upd, SysOrg> {
		public SysCom _com = null;

		public void before() {
			super.before();
			SysOrg dbBean = loadAndLock(getB().getPkey());
			if (dbBean.getTemplat().equals(getB().getTemplat()) == false)
				SysCellDAO.resetTemplate(getB().getPkey(), getB().getTemplat());
			_com.setPkey(dbBean.gtLongPkey());
			_com.setName(getB().getName());
			_com.setShortName(getB().getShortName());
			_com.setRowVersion(dbBean.getRowVersion());
			SysComDAO.Upd cins = new SysComDAO.Upd();
			cins.setB(_com);
			cins.commit();
			//
			PropertyUtils.copyPropertiesWithout(dbBean, getB(), SysOrg.T.PKEY, SysOrg.T.CODE, 
			    SysOrg.T.ORG_UP, SysOrg.T.ENABLED, T.WORK_DATE, SysOrg.T.STATE);
			setB(dbBean);
			BeanBuf.clear(SysOrg.class, getB().getPkey());
		}

	}

	public static class Del extends IduDel<Del, SysOrg> {

		public void valid() {
			super.valid();
				haveBeUsed(SysOrg.class, SysOrg.T.ORG_UP, b.getPkey());
				haveBeUsed(SysDept.class, SysDept.T.ORG, b.getPkey());
				haveBeUsed(SysCell.class, SysCell.T.ORG, b.getPkey());
		}
		
		@Override
		public void after() {
		  super.after();
		  SysComDAO.Del cact = new SysComDAO.Del();
			cact.setBKey(getB().gtLongPkey());
			cact.commit();
			BeanBuf.clear(SysOrg.class, getB().getPkey());
			ProvDataCtrl.initOrgMap();//将缓存中存储的机构上下级关系更新
		}

	}

	public static class DayEnd extends IduOther<DayEnd, SysOrg> {
		public static Cn CN = new Cn("dayend", "日终处理");

		public void run() {
			if (getOrg().equals(getB()) == false)
				throw LOG.err("errOrg", "日终只能在本机构处理!");
			if (getB().gtState() != Sys.OOrgState.OPENING)
				throw LOG.err("errOrg", "机构[{0}]状态非'营业中'，不可作日终处理!", getB().getName());
			getB().stState(Sys.OOrgState.DAY_END_DOING);
			getB().upd();
			BeanBuf.clear(SysOrg.class, getB().getPkey());
			Date workDate = getB().getWorkDate();
			GlTransformDAO.doTotal(getB()); //日终汇总处理
			if (DateTools.isLastDayOfMonth(workDate)) { // 月末处理
				GlReportProfitDAO.Insert(getB(), DateTools.getFirstDayOfMonth(workDate), workDate);
				GlTransformDAO.doCarryOver(getB()); //月末损益结转
				GlReportAssetDAO.Insert(getB(), DateTools.getFirstDayOfMonth(workDate), workDate);
			}
			//切换工作日
			Date nextDate = GlDailyLedgerDAO.nextDate(getB().getWorkDate(), getOrg());
			getB().setWorkDate(nextDate);
			getB().stState(Sys.OOrgState.OPENING);
			getB().upd();
			BeanBuf.clear(SysOrg.class, getB().getPkey());
		}
	}

}
