package irille.core.lg;

import irille.core.sys.SysUser;
import irille.pub.Log;
import irille.pub.svr.Env;
import irille.pub.svr.TranMsg;

import java.util.Date;

public class LgLoginDAO {
	public static final Log LOG = new Log(LgLoginDAO.class);

	public static LgLogin initLg(SysUser user, byte client, String ip, String system, String browser) {
		LgLogin lg = new LgLogin().init();
		lg.setLoginTime(Env.getSystemTime());
		lg.setUserSys(user.getPkey());
		lg.setClient(client);
		lg.setIp(ip);
		lg.setSystem(system);
		lg.setBrowser(browser);
		lg.ins();
		return lg;
	}

	public static LgTran initTran(TranMsg tranMsg, Date etime, boolean success) {
		LgLogin login = tranMsg.getLoginUser().getLg();
		LgTran tran = new LgTran();
		tran.stLogin(login);
		tran.setBTime(tranMsg.getBeginTime());
		tran.setProcTime((int) (etime.getTime() - tranMsg.getBeginTime().getTime()));
		tran.stSuccessFlag(success);
		tran.setObjTable(tranMsg.getObjTable());
		tran.setObjPkey(tranMsg.getObjPkey());
		if (tranMsg.getAct() != null && tranMsg.getAct().getPkey() != null) {
			tran.stAct(tranMsg.getAct().gtAct());
			tran.setRem(tranMsg.getAct().getTableName() + " - " + tranMsg.getAct().getName());
			if (tran.getObjPkey() != null)
				tran.setRem(tran.getRem() + " 编号:" + tran.getObjPkey());
		} else if (tranMsg.getAct() != null) //加载类型
			tran.setRem(tranMsg.getAct().getName() + " " + tranMsg.getAct().getCode());
		tran.ins();
		//同时更新用户登录日志
		login.setCountProcTime(login.getCountProcTime() + tran.getProcTime());
		if (tranMsg.getAct() != null && tranMsg.getAct().getPkey() != null) {
			if (success)
				login.setCountSuccess(login.getCountSuccess() + 1);
			else
				login.setCountFail(login.getCountFail() + 1);
		} else if (tranMsg.getAct() != null) {
			if (success)
				login.setLoadSuccess(login.getLoadSuccess() + 1);
			else
				login.setLoadFail(login.getLoadFail() + 1);
		}
		login.upd();
		return tran;
	}

}
