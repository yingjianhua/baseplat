package irille.pub.svr;


import irille.pub.Exp;
import irille.pub.Log;

import org.apache.struts2.interceptor.FileUploadInterceptor;

import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.config.ConfigurationException;

public class ItpFileUpload extends FileUploadInterceptor {
	private static final Log LOG = new Log(ItpFileUpload.class);

	public String intercept(ActionInvocation invocation) throws Exception {

		String result = null;
		try {
			result = super.intercept(invocation);
		} catch (Exception e) {
			if (e instanceof Exp)
				throw e;
			e.printStackTrace();
			if (e instanceof ConfigurationException)
				throw LOG.err("upload", "上传出错，请检查文件类型与大小");
			throw LOG.err("unknow", e.getMessage());
		}
		return result;
	}
	
}

