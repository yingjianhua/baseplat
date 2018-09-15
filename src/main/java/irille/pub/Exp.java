//Created on 2005-9-17
package irille.pub;

import irille.pub.PubInfs.IMsg;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Vector;



/**
 * Title: 运行态异常类<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class Exp extends RuntimeException implements Serializable,IPubVars {
	private static final Log LOG = new Log(Exp.class);
	// 已执行过的程序抛出的非Exp类异常，如果是直接产生的其值为null
	private transient Throwable _exception = null;
	// 本类异常的抛出矢量
	private Vector _tracks; //出错信息
	private String _exceptionStackTrace = null;

	/**
	 * 构造方法
	 * @param code 出错码
	 * @param msg 出错信息
	 * @param paras
	 */
	private Exp(String code,String msg, Object... paras) {
		_tracks = new Vector();
		_tracks.addElement(new Track(code,msg, paras));
	}

	/**
	 * 构造Exp对象
	 * @param code
	 * @param msg
	 * @param paras
	 * @return
	 */
	public static final Exp newExp(IMsg msg, Object... paras) {
		return new Exp(msg.name(),msg.getMsg(),paras);
	}
	/**
	 * 新建ExpRuntime对象
	 * @param e 异常
	 * @param paras 参数
	 * @return 结果
	 */
	public static final Exp newExp(Throwable e, IMsg msg, Object... paras) {
		if (e instanceof Exp) {
			((Exp) e).addTrack(msg, paras);
			return (Exp) e;
		}
		//	XXX	if (e instanceof Exp) { 
		//			Exp exp = new Exp((Exp) e);
		//			exp.addTrack(message, paras);
		//			return exp;
		//		}
		Exp exp = new Exp(msg.name(),msg.getMsg(), paras);
		exp._exception = e;
		return exp;
	}

	/**
	 * 增加出错信息
	 * @param code 出错码
	 * @param msg 要添加的出错信息 
	 * @param paras 参数
	 * @return 当前异常
	 */
	protected final Exp addTrack(IMsg msg, Object[] paras) {
		_tracks.addElement(new Track(msg.name(),msg.getMsg(), paras));
		return this;
	}

	/**
	 * 取exception
	 * @return exception
	 */
	public Throwable getException() {
		return this._exception;
	}

	/**
	 * 取tracks
	 * @return tracks
	 */
	public Track[] getTracks() {
		Track[] tracks = new Track[_tracks.size()];
		for (int i = 0; i < tracks.length; i++) {
			tracks[i] = (Track) _tracks.elementAt(i);
		}
		return tracks;
	}

	/**
	 * 判断异常代码是否一致, 如不一致则重新抛出
	 * @param code String 异常代码
	 * @throws Exp 异常不一致，重新抛出
	 */
	public void assertCode(String code) throws Exp {
		if (code.equals(getLastCode())) {
			return;
		}
		throw this;
	}

	/**
	 * 判断异常代码是否一致
	 * @param code String 异常代码
	 * @return 结果
	 * @throws Exp 异常不一致，重新抛出
	 */
	public boolean isCode(String code) throws Exp {
		return code.equals(getLastCode());
	}

	/**
	 * 返回主要出错信息
	 * @return 结果
	 */
	public String getPrimaryMessage() {//XXX
		//		StringBuffer buf = new StringBuffer();
		//		for (int i = _tracks.size() - 1; i >= 0; i--) {
		//			buf.append(((Track) _tracks.elementAt(i)).getMessage() + Constants.LN);
		//		}
		//		//		return buf.toString();
		//		String result = buf.toString();
		//		return result;
		return ((Track) _tracks.elementAt(0)).getMessage();
	}

	/**
	 * 取最后的出错代码
	 * @return String 出错代码
	 */
	public String getLastCode() {
		if (_tracks.size() == 0) {
			return null;
		}
		return ((Track) _tracks.elementAt(_tracks.size() - 1)).getCode();
	}

	/**
	 * 取最后的出错信息
	 * @return String 出错信息
	 */
	public String getLastMessage() {
		if (_tracks.size() == 0) {
			return null;
		}
		return ((Track) _tracks.elementAt(_tracks.size() - 1)).getMessage();
	}

	/**
	 * 对象序列化处理
	 * @param out ObjectOutputStream 对象输出流
	 * @throws IOException 出错
	 * @throws ClassNotFoundException 出错
	 */
	private void writeObject(ObjectOutputStream out) throws IOException,
			ClassNotFoundException {
		getOtherExceptionStackTrace();
		out.defaultWriteObject();
	}

	//	/**
	//	 * @param s
	//	 * @see java.lang.Throwable#printStackTrace(java.io.PrintStream)
	//	 */
	//	@Override
	//	public void printStackTrace(PrintStream s) {
	//		s.print(toString());
	//		super.printStackTrace(s);
	//	}
	/**
	 * 取其它类的出错信息串
	 * @return 结果
	 */
	public String getOtherExceptionStackTrace() {
		if (_exceptionStackTrace != null)
			return _exceptionStackTrace;
		if (_exception == null)
			return "";
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		PrintStream os = new PrintStream(out);
		_exception.printStackTrace(os);
		_exceptionStackTrace = out.toString();
		return _exceptionStackTrace;
	}

	/**
	 * 转化为字符串
	 * @return String 结果
	 */
	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append(getOtherExceptionStackTrace());
		buf.append("irille.pub.Exp:");
		for (int i = 0; i < _tracks.size(); i++) {
			buf.append("        (" + ((Track) _tracks.elementAt(i)).getCode() + "):"
					+ LN);
			buf.append(((Track) _tracks.elementAt(i)).getMessage() + LN);
		}
		return buf.toString();
	}
	
	public String getExpMessage() {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < _tracks.size(); i++) {
			buf.append("        (" + ((Track) _tracks.elementAt(i)).getCode() + "):"
					+ LN);
			buf.append(((Track) _tracks.elementAt(i)).getMessage() + LN);
		}
		Throwable e = _exception;
		while (e != null) {
			buf.append(e.getMessage() + LN);
			e = e.getCause();
		}
		return buf.toString();
	}
	
	public void printLastMessage() {
		System.err.println(getLastMessage());
	}
	public static String getExpMessage(Throwable exp) {
		if (exp instanceof Exp)
			return ((Exp)exp).getExpMessage();
		Throwable e = exp;
		StringBuffer buf = new StringBuffer();
		while (e != null) {
			buf.append(e.getMessage() + LN);
			e = e.getCause();
		}
		return buf.toString();
	}

	/**
	 * Title: 异常信息类<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static class Track implements Serializable {
		private String _code; //
		private String _message; //出错信息, 已将参数值填入

		Track(String code,String name, Object[] para) {
			_code = code;
			_message = new MessageFormat(name).format(para);
		}

		/**
		 * 取code
		 * @return code
		 */
		public String getCode() {
			return _code;
		}

		/**
		 * 取message
		 * @return message
		 */
		public String getMessage() {
			return this._message;
		}
	}
	
	public Exp(String message, Throwable throwable) {
		super(message, throwable);
	}
	public Exp(String message) {
		super(message);
	}
}
