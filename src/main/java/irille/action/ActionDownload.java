package irille.action;

import irille.pub.Log;
import irille.pub.Str;
import irille.pub.bean.BeanMain;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.ServletActionContext;

public class ActionDownload<MAIN extends BeanMain> extends ActionBase<MAIN> {
	public static final Log LOG = new Log(ActionDownload.class);

	private String _fileName;
	private String _fileUrl;
	private String _isReal;

	public Class beanClazz() {
		throw LOG.err("err","action中beanClazz未重写");
	}
	
	public String getFileName() {
		return _fileName;
	}

	public void setFileName(String fileName) throws UnsupportedEncodingException {
		_fileName = fileName;
	}

	public String getFileUrl() {
		return _fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		_fileUrl = fileUrl;
	}

	public String getIsReal() {
		return _isReal;
	}

	public void setIsReal(String isRealPath) {
		_isReal = isRealPath;
	}

	public InputStream getInputStream() throws Exception {
		String encoding = Str.getEncoding(getFileUrl());
		String furl = getFileUrl(); //weblogic默认编码中文可以传递
		if (encoding.toUpperCase().equals(Str.ENCODING_ISO)) //tomcat的情况转utf8
			furl = new String(furl.getBytes(encoding), Str.ENCODING_UTF8);
		// String sysen = System.getProperty("file.encoding");
		// LOG.info("系统编码："+sysen +"    参数编码:"+encoding);
		// LOG.info("whxwhx01:"+furl);
		if (getIsReal() == null)
			return ServletActionContext.getServletContext().getResourceAsStream(furl);
		else
			return new FileInputStream(getDownPath() + furl);
	}

	public String execute() throws Exception {
		return SUCCESS;
	}
	
	// 需要注意tomcat与weblogic的编码方式不同
	public void down() throws Exception {
		String encoding = Str.getEncoding(getFileName());
		HttpServletResponse response = ServletActionContext.getResponse();
		response.reset();
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition", "attachment; filename="
		    + new String(getFileName().getBytes(encoding), "ISO-8859-1")); // ISO-8859-1编码
		OutputStream os = response.getOutputStream();
		InputStream input = getInputStream();
		byte[] readBytes = new byte[1024];
		int count = input.read(readBytes);
		while (count != -1) {
			os.write(readBytes, 0, count);
			count = input.read(readBytes);
		}
		input.close();
		String fname = getFileName();
		if (encoding.toUpperCase().equals(Str.ENCODING_ISO)) //tomcat的情况转utf8
			fname = new String(fname.getBytes(encoding), Str.ENCODING_UTF8);
		//TODO 
		//SysLogopDAO.log("下载", Sys.LOG_OPTYPE_FUN, "下载-"+fname);
	}

}
