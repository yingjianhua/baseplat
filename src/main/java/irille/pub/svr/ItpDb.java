package irille.pub.svr;

import irille.pub.Exp;

import java.util.Date;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;
import org.json.JSONObject;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

public class ItpDb extends AbstractInterceptor {

	public String intercept(ActionInvocation actionInvocation) throws Exception {
		// String rtn = ActionBase.LOGIN;
		Date date1 = new Date();
		String rtn = null;
		boolean success = true;
		String path = actionInvocation.getProxy().getActionName();
		try {
			rtn = actionInvocation.invoke();
			String[] ps = path.split("\\_");
			if (ps[ps.length - 1].equals("list") == false) // 查询不处理事务提交
				DbPool.getInstance().getConn().commit();
		} catch (Exception e) {
			success = false;
			DbPool.getInstance().getConn().rollback();
			e.printStackTrace();
			String msg = e.getMessage();
			if (e instanceof Exp)
				msg = ((Exp) e).getLastMessage();
			procExp(path, msg, e);
			return rtn;
		} finally {
			Env.INST.endTran(success);
			DbPool.getInstance().getConn().commit(); //提交对日志的更新
			DbPool.getInstance().removeConn();
			Date date3 = new Date();
			System.err.println("后台处理【"+path+"】共消耗【"+(date3.getTime()-date1.getTime())+"】毫秒");
		}
		return rtn;
	}

	// 处理AJAX请求异常的处理
	public void procExp(String path, String msg, Exception e) throws Exception {
		String[] ps = path.split("\\_");
		String method = ps[ps.length - 1];
		HttpServletResponse response = ServletActionContext.getResponse();
		// 如何传递错误信息到前台--未解决部分
		// ext.store.load
		// ext.model.load
		if (method.equals("load"))
			throw e;
		// del  delMulti  iu  updPwd...
		JSONObject json = new JSONObject().put("success", false).put("msg", msg);
		response.getWriter().print(json.toString());
		return;
	}
}
