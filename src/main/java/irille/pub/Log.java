package irille.pub;

import irille.pub.PubInfs.IMsg;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

/**
 * 日志程序，内部结合 Log4j，根据Log4j的配置对信息进行分等级输出
 * 
 * @author whx
 * 
 */
public class Log {
	private static final Log LOG = new Log(Log.class);
	public enum Msgs implements IMsg {// 信息定义的类名必须为Msgs, 以便系统能检索 @formatter:off
		error("执行到非法的代码出错!"),
		serial("序列化[{0}]类型的对象出错!"),
		compareTo("比较对象出错，类型[{0}]与[{1}]的对象不能比较!"),
		classNotFound("类[{0}]没找到!"),
		clone("克隆[{0}]类型的对象出错!"),
		newInstance("构造类[{0}]的新实例出错!"),
		;
		private String _msg;
		private Msgs(String msg) { _msg=msg; }
		public String getMsg() {return _msg; }
	} //@formatter:on


	private Logger _log;

	public Log(Class clazz) {
		_log = Logger.getLogger(clazz);
	}

	/**
	 * 取Log4j对象
	 * @return
	 */
	public Logger getLogger() {
		return _log;
	}

	public String getClazzName() {
		return _log.getName();
	}

	/**
	 * 调试
	 * 
	 * @param msg
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应
	 */
	public void debug(IMsg msg, Object... paras) {
		_log.debug(messageToString(msg.getMsg(), paras));
	}

	/**
	 * 出错
	 * 
	 * @param code
	 *          出错码
	 * @param message
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应 return 异常
	 */
	public Exp err(String code, String message, Object... paras) {
		_log.error(messageToString(code + ":" + message, paras));
		return Exp.newExp(new Cn(code, message), paras);
	}
	
	/**
	 * 出错
	 * @param msg
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应 return 异常
	 */
	public Exp err(IMsg msg, Object... paras) {
		_log.error(messageToString(msg.name() + ":" + msg.getMsg(), paras));
		return Exp.newExp(msg, paras);
	}
	
	
//**此方法不能定义， 与上一方法会有二异性！！！  whx 20140724
//	public Exp err(String message, Object... paras) {
//		_log.error(messageToString("default:" + message, paras));
//		return Exp.newExp("default", message, paras);
//	}
	
	/**
	 * 出错，返回异常，内容为"执行到非法的代码出错"
	 * 
	 * @return 异常
	 */
	public Exp err() {
		return err(Msgs.error);
	}

	/**
	 * 出错，返回异常，内容为"执行到非法的代码出错"
	 * 
	 * @param t
	 * @return 异常
	 */
	public Exp err(Throwable t) {
		return err(t, Msgs.error);
	}

	/**
	 * 
	 * @param t
	 *          异常
	 * @param code
	 *          出错码
	 * @param message
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应
	 * @return 异常
	 */
	public Exp err(Throwable t, String code, String message, Object... paras) {
		if (t instanceof Exp)
			return (Exp) t;
		_log.error(messageToString(message, paras), t);
		return Exp.newExp(t,new Cn(code,message), paras);
	}
	
	public Exp err(Throwable t, IMsg msg, Object... paras) {
		if (t instanceof Exp)
			return (Exp) t;
		_log.error(messageToString(msg.getMsg(), paras), t);
		return Exp.newExp(t, msg, paras);
	}

	/**
	 * 建立序列化出错的异常
	 * 
	 * @param e
	 *          出错
	 * @param clazz
	 *          类
	 * @return 结果
	 */
	public Exp errSerial(Exception e, Class clazz) {
		return err(e,Msgs.serial, clazz.getName());
	}
	/**
	 * 建立克隆出错的异常
	 * 
	 * @param clazz
	 *          类
	 * @return 结果
	 */
	public Exp errCompareTo(Class clazz, Class objClazz) {
		return err(Msgs.compareTo, clazz.getName(), objClazz.getName());
	}

	/**
	 * 类没找到出错
	 * 
	 * @param e
	 *          出错
	 * @param clazz
	 *          类串
	 * @return 异常
	 */
	public Exp errClassNotFound(Exception e, String clazz) {
		return err(e, Msgs.classNotFound, clazz);
	}

	public Exp errClone(Exception e, Class clazz) {
		return err(e,Msgs.clone, clazz.getName());
	}

	/**
	 * 构造实例出错
	 * 
	 * @param e
	 *          出错
	 * @param clazz
	 *          类
	 * @return 异常
	 */
	public Exp errNewInstance(Exception e, Class clazz) {
		return err(e, Msgs.newInstance,clazz.getName());
	}

	/**
	 * 输出信息
	 * 
	 * @param msg
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应
	 */
	public void info(IMsg msg, Object... paras) {
		_log.info(messageToString(msg.getMsg(), paras));
	}

//	/**
//	 * 跟踪信息
//	 * 
//	 * @param message
//	 *          信息，当中可以用{0},{1}等指定参数
//	 * @param paras
//	 *          输出的参数，与{0},{1}等位置对应
//	 */
//	public void trace(String message, Object... paras) {
//		// 本方法原来在irille项目中有的，现没有了，要解决whx 20121007
//		// _log.trace(messageToString(message, paras));
//		err();
//	}

	/**
	 * 警告
	 * 
	 * @param msg
	 *          信息，当中可以用{0},{1}等指定参数
	 * @param paras
	 *          输出的参数，与{0},{1}等位置对应
	 */
	public void warn(IMsg msg, Object... paras) {
		_log.warn("警告!!!: "+messageToString(msg.getMsg(), paras));
	}

	public static String messageToString(String message, Object... paras) {
		return new MessageFormat(message).format(paras).toString();
	}
}
