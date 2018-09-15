/**
 * 
 */
package irille.pub.print;

import irille.action.gs.GsInLineViewAction;
import irille.action.gs.GsOutLineViewAction;
import irille.gl.gs.GsInit;
import irille.gl.gs.GsInitLine;
import irille.pub.FileTools;
import irille.pub.IPubVars;
import irille.pub.Log;
import irille.pub.Str;
import irille.pub.PubInfs.IMsg;
import irille.pub.bean.BeanBase;
import irille.pub.bean.IGoods;
import irille.pub.ext.Ext;
import irille.pub.idu.Idu;
import irille.pub.inf.IIn;
import irille.pub.inf.IOut;
import irille.pub.svr.DbPool;
import irille.pub.svr.Env;

import java.util.List;

/**
 * 产生报表打印的JSP文件
 * 
 * @author whx
 * 
 */
public class CrtJsp<T extends CrtJsp> implements IPubVars {
	private static final Log LOG = new Log(CrtJsp.class);

	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		CRT_FILE("正在建立文件：{0}......"), OK("ok!");
		private String _msg;

		private Msgs(String msg) {
			_msg = msg;
		}

		public String getMsg() {
			return _msg;
		}
	} // @formatter:on

	private Class _mainClazz;
	private Class[] _linesClazz;
	private Class[] _importClazz = null;
	private JspJavaCode _javaCodes = new JspJavaCode();
	private String _mainVar;
	private String _fileNameAdd = ""; // 文件名的附加字符
	private String[] _mainParams;
	private String[] _linesParams;

	public CrtJsp(Class mainClazz, Class... linesClazz) {
		_mainClazz = mainClazz;
		_linesClazz = linesClazz;
		_mainVar = lastCode(mainClazz);
	}

	public CrtJsp(String[] mainParams, String[] linesParams, Class mainClazz, Class... linesClazz) {
		_mainClazz = mainClazz;
		_linesClazz = linesClazz;
		_mainVar = lastCode(mainClazz);
		_mainParams = mainParams;
		_linesParams = linesParams;
	}

	public void run() {
		addFieldCodes();
		addMainCodes();
		if (_linesClazz.length > 0)
			addLineCodes();
		addGenXml();
		crtFile();
	}

	public void setImport(Class... importClazz) {
		_importClazz = importClazz;
	}

	public void head(StringBuilder buf) {
		buf.append("<%@ page language=\"java\" contentType=\"text/html; charset=UTF-8\" pageEncoding=\"UTF-8\"%>" + LN);
		outImport(buf, GenXmlData.class);
		outImport(buf, Idu.class);
		outImport(buf, BeanBase.class);
		outImport(buf, List.class);
		outImport(buf, DbPool.class);
		outImport(buf, _mainClazz);
		for (Class clazz : _linesClazz)
			outImport(buf, clazz);
		if (_importClazz != null)
			for (Class clazz : _importClazz)
				outImport(buf, clazz);
		if ("GsIn".equals(_mainClazz.getSimpleName())) {
			outImport(buf, IIn.class);
			outImport(buf, IGoods.class);
			outImport(buf, GsInLineViewAction.class);
		}
		if ("GsOut".equals(_mainClazz.getSimpleName())) {
			outImport(buf, IOut.class);
			outImport(buf, IGoods.class);
			outImport(buf, GsOutLineViewAction.class);
		}
	}

	public void outImport(StringBuilder buf, Class clazz) {
		new JspImport(clazz).out(buf);
	}

	private String lastCode(Class clazz) {
		return Str.getClazzLastCode(clazz);
	}

	public void addFieldCodes() {
		String mainVar = "";
		String linesVar = "";
		for (int i = 0; i < _mainParams.length; i++) {
			mainVar += "\"" + _mainParams[i] + "\"";
			if (i != _mainParams.length - 1)
				mainVar += ",";
		}
		for (int i = 0; i < _linesParams.length; i++) {
			linesVar += "\"" + _linesParams[i] + "\"";
			if (i != _linesParams.length - 1)
				linesVar += ",";
		}
		_javaCodes.addLn("try {");
		_javaCodes.addLn("String mainFlds[] = {" + mainVar + "};");
		_javaCodes.addLn("String linesFlds[] = {" + linesVar + "};");
	}

	public void addMainCodes() {
		_javaCodes.addLn(_mainVar + " main" + _mainVar + "=BeanBase.load(" + _mainVar + ".class,request.getParameter(\"pkey\"));");
	}

	public void addLineCodes() {
		String var;
		if ("GsIn".equals(_mainVar) || "GsOut".equals(_mainVar)) {
			for (Class clazz : _linesClazz) {
				var = lastCode(clazz);
				_javaCodes.addLn("String oirgName = main" + _mainVar + ".gtOrigForm().getClass().getName();");
				_javaCodes.addLn("Class dc = Class.forName(oirgName + \"DAO\");");
				_javaCodes.addLn("I" + _mainVar.substring(2) + " " + _mainVar.substring(2) + "Line = (I" + _mainVar.substring(2) + ") dc.newInstance();");
				_javaCodes.addLn("List<IGoods> view = " + _mainVar.substring(2) + "Line.get" + _mainVar.substring(2) + "Lines(main" + _mainVar + ".gtOrigForm(), 0, 0);");
				_javaCodes.addLn("List<" + var + ">  list" + var + " = " + var + "Action.transIGoods2" + var + "(main" + _mainVar + ", view);");
			}
		} else if ("GsStock".equals(_mainVar)) {
			for (Class clazz : _linesClazz) {
				var = lastCode(clazz);
				_javaCodes.addLn("String pkey = request.getParameter(\"pkey\");");
				_javaCodes.addLn("String startDate = request.getParameter(\"startDate\");");
				_javaCodes.addLn("String endDate = request.getParameter(\"endDate\");");
				_javaCodes.addLn("String where = \"stock = ?\";");
				_javaCodes.addLn("List<GsStockLine> listGsStockLine;");
				_javaCodes.addLn("if(!startDate.isEmpty()){");
				_javaCodes.addLn("	where += \" and DATE(gs_time) >= '\" + startDate + \"'\";");
				_javaCodes.addLn("}"+LN+"if(!endDate.isEmpty()){");
				_javaCodes.addLn("	where += \" and DATE(gs_time) <= '\" + endDate + \"'\";");
				_javaCodes.addLn("}");
				_javaCodes.addLn("listGsStockLine = BeanBase.list(GsStockLine.class, where,false,mainGsStock.getPkey());");
			}
		} else {
			for (Class clazz : _linesClazz) {
				var = lastCode(clazz);
				_javaCodes.addLn("List<" + var + "> list" + var + " = Idu.getLinesTid(main" + _mainVar + ", " + var + ".class);");
			}
		}
	}

	public void addGenXml() {
		String var;
		String list = "";
		for (Class clazz : _linesClazz) {
			var = lastCode(clazz);
			list += ",list" + var;
		}
		_javaCodes.addLn("GenXmlData.GenParameterXmlData(response,main" + _mainVar + list + ",mainFlds,linesFlds);");
		_javaCodes.addLn("} finally {");
		_javaCodes.addLn("DbPool.getInstance().removeConn();"+ LN + "	}");
	}

	public String getFileName() {
		return Env.getResourceDir() + "print/" + Ext.getPag(_mainClazz) + "/" + Ext.getClazz(_mainClazz) + getFileNameAdd() + ".jsp";
	}

	/**
	 * 建立文件
	 * 
	 * @return
	 */
	public T crtFile() {
		LOG.info(Msgs.CRT_FILE, getFileName());
		FileTools.writeStr(getFileName(), out());
		LOG.info(Msgs.OK);
		return (T) this;
	}

	public String out() {
		StringBuilder buf = new StringBuilder();
		head(buf);
		_javaCodes.out(buf);
		return buf.toString();
	}

	public static class JspJavaCode extends JspLine {
		StringBuilder _lines = new StringBuilder();

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.print.CrtJsp.JspLine#head()
		 */
		@Override
		public String head() {
			return super.head() + LN;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.print.PrnJsp.JspLine#cmd()
		 */
		@Override
		public String cmd() {
			return _lines.toString();
		}

		public JspJavaCode add(String line) {
			_lines.append(line);
			return this;
		}

		public JspJavaCode addLn(String line) {
			_lines.append(line + LN);
			return this;
		}
	}

	/**
	 * 导入类引用
	 */
	public static class JspImport extends JspLine {
		private Class _importClazz;

		/**
		 * @param cmd
		 */
		public JspImport(Class importClazz) {
			_importClazz = importClazz;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see irille.pub.print.PrnJsp.JspLine#cmd()
		 */
		@Override
		public String cmd() {
			return "@page import=\"" + _importClazz.getName() + "\"";
		}
	}

	/**
	 * Jsp行的基类
	 * 
	 * @author whx
	 * 
	 */
	public static class JspLine implements IPubVars {
		private boolean _outLn = true;

		public JspLine() {
		}

		public String head() {
			return "<%";
		}

		public String cmd() {
			return "";
		}

		public String foot() {
			return "%>";
		}

		public void setOutLn(boolean value) {
			_outLn = value;
		}

		public void out(StringBuilder buf) {
			buf.append(head() + cmd() + foot() + (_outLn ? LN : ""));
		}
	}

	/**
	 * @return the fileNameAdd
	 */
	public String getFileNameAdd() {
		return _fileNameAdd;
	}

	/**
	 * @param fileNameAdd
	 *          the fileNameAdd to set
	 */
	public T setFileNameAdd(String fileNameAdd) {
		_fileNameAdd = fileNameAdd;
		return (T) this;
	}
}
