//Created on 2005-11-12
package irille.pub.svr;

import irille.core.lg.LgLogin;
import irille.core.lg.LgTran;
import irille.core.sys.SysMenu;
import irille.core.sys.SysMenuAct;
import irille.core.sys.SysTable;
import irille.core.sys.SysTableAct;
import irille.core.sys.SysTemplat;
import irille.pub.Log;

import java.sql.Connection;


/**
 * Title: 服务端通讯类, 包含一个连接的相关信息<br>
 * Description: <br>
 * Copyright: Copyright (c) 2005<br>
 * Company: IRILLE<br>
 * @version 1.0
 */
public class Svr {
	private static final Log LOG= new Log(Svr.class);
	public static Svr INST = new Svr();
	/**
	 * 主要用于测试时对MI的虚拟对象构造、权限检查拦截等
	 */
	public static ITest TEST = null;
	/**
	 * 运行独立事务
	 */
	public static void run(IRunSvr runSvr) {
		Svr.init(); // 防止循环调用
		Svr.tranBegin();
		try {
			runSvr.runSvr();
			Svr.commit();
		} finally {
			Svr.rollback();
		}
		Svr.releaseAll();
	}
	
	public static Svr getInstance() {
		return INST;
	}
	
	public static void tranBegin() {  //交易开始
		//XXX
	}
	
	public static void commit() {
		try {
			DbPool.getInstance().getConn().commit();			
		} catch (Exception e) {
			throw LOG.err(e);
		}
	}

	public static void rollback() {
		try {
			DbPool.getInstance().getConn().rollback();			
		} catch (Exception e) {
			throw LOG.err(e);
		}		
	}
	
	public static DbPool getDbPool() {
		return DbPool.getInstance();
	}
	
	public static Connection getConn() {
		return DbPool.getInstance().getConn();
	}
	
	public static void releaseAll() {
		DbPool.getInstance().releaseAll();
	}
	
	public static void commitAndReleaseAll() {
		commit();
		releaseAll();
	}
	//Bean的初始化操作，因有很多循环引用，故先处理此代码
	public static void init() {
		SysTemplat.TB.getCode();
		LgLogin.TB.getCode();
		LgTran.TB.getCode();
		SysTable.TB.getCode();
		SysTableAct.TB.getCode();
		SysMenu.TB.getCode();
		SysMenuAct.TB.getCode();
	}

	
	
	/**
	 * 运行事务接口，该接口会进行事务的前处理及事务提交或回滚
	 * @author whx
	 *
	 */
	public interface IRunSvr {
		public void runSvr();
	}

//	public static final Cn ERR_CTX_NOT_SETED = CNF.newErr("ctxNotSeted", "应用上下文未设置");

	// /**
	// * 初始化，仅装入指定model关联的映射文件
	// */
	// public final void init(Class... modelClasses) {
	// HashSet<Class> set = new HashSet<Class>();
	// Class[] clazzes = BeanTools.getPackageClazzModels(BaseUser.class);
	// for (Class clazz : clazzes)
	// set.add(clazz);
	// for (Class clazz : modelClasses)
	// BeanReferences.getToSet(clazz, set);
	// init(set);
	// }
	//
	// /**
	// * 缺省实始化，装入所有的model，所要的包在常量ALL_MODEL_REFERENCES_DEFINE_CLASSES中定义
	// */
	// public void initDefault() {
	// HashSet<Class> set = new HashSet<Class>();
	// // Class[] clazzes = BeanTools.getPackageClazzes(BaseUser.class);
	// // for (Class clazz : clazzes)
	// // set.add(clazz);
	// BeanReferences.getAllToSet(set);
	// HashSet<Class> refSet = new HashSet<Class>();
	// for(Class clazz:set)
	// BeanReferences.getToSet(clazz, refSet);
	// init(refSet);
	// }
	//
	// /**
	// * 初始化事务管理类及dao
	// * @param set 所有用到的model类集合
	// */
	// public void init(HashSet<Class> set) {
	// if (_transactionManager != null) {
	// _transactionManager.getSessionFactory().close();
	// }
	// Configuration conf = new Configuration();
	// File prop = new File(".", "etc/hibernate.properties");
	// conf.addProperties(FileTools.loadProperties(prop));
	// conf.addXML(getSetToHbm(set));
	// _transactionManager = new HibernateTransactionManager(conf
	// .buildSessionFactory());
	// }
	// // /**
	// * 取_transactionManager
	// * @return _transactionManager
	// */
	// public synchronized HibernateTransactionManager getTransactionManager() {
	// if (_transactionManager == null) {
	// if (TEST != null)
	// throw new Exp(ERR_CTX_NOT_SETED);
	// initDefault();
	// }
	// return _transactionManager;
	// }

	/**
	 * Title: 测试用补充类<br>
	 * Description: <br>
	 * Copyright: Copyright (c) 2005<br>
	 * Company: IRILLE<br>
	 * @version 1.0
	 */
	public static interface ITest {
		/**
		 * 构造新的MI实例
		 * @param <T> 结果
		 * @param MIClazz
		 * @return 结果
		 */
		public <T> T newInstance(Class<T> MIClazz);

		/**
		 * 增加日志
		 * @param clazz 类
		 * @param method 方法
		 * @param msg 信息
		 */
		public void addLog(Class clazz, String method, String msg);
	}
	// public final class IrilleApplicationContext extends
	// ClassPathXmlApplicationContext {
	// /**
	// * Create a new ClassPathXmlApplicationContext, loading the definitions
	// * from the given XML files and automatically refreshing the context.
	// * @param configLocations array of file paths
	// */
	// public IrilleApplicationContext(String[] configLocations)
	// throws BeansException {
	// super(configLocations, true);
	// }
	//
	// /**
	// * Loads the bean definitions via an XmlBeanDefinitionReader.
	// * @see org.springframework.beans.factory.xml.XmlBeanDefinitionReader
	// * @see #initBeanDefinitionReader
	// * @see #loadBeanDefinitions
	// */
	// protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory)
	// throws IOException {
	// // Create a new XmlBeanDefinitionReader for the given BeanFactory.
	// IrilleXmlBeanDefinitionReader beanDefinitionReader = new
	// IrilleXmlBeanDefinitionReader(
	// beanFactory);
	// // Configure the bean definition reader with this context's
	// // resource loading environment.
	// beanDefinitionReader.setResourceLoader(this);
	// if (getClassLoader() != null) {
	// beanDefinitionReader.setBeanClassLoader(getClassLoader());
	// }
	// beanDefinitionReader.setEntityResolver(new ResourceEntityResolver(this));
	// // Allow a subclass to provide custom initialization of the reader,
	// // then proceed with actually loading the bean definitions.
	// initBeanDefinitionReader(beanDefinitionReader);
	// loadBeanDefinitions(beanDefinitionReader);
	// //从hbmString读入bean
	// if (_hbmString != null)
	// beanDefinitionReader.loadBeanDefinitions(_msg, _hbmString);
	// }
	// }
	//
	// public static final class IrilleXmlBeanDefinitionReader extends
	// XmlBeanDefinitionReader {
	// /**
	// * Create new XmlBeanDefinitionReader for the given bean factory.
	// */
	// public IrilleXmlBeanDefinitionReader(BeanDefinitionRegistry beanFactory) {
	// super(beanFactory);
	// }
	//
	// /**
	// * Load bean definitions from the specified XML file.
	// * @param resource the resource descriptor for the XML file
	// * @return the number of bean definitions found
	// * @throws BeanDefinitionStoreException in case of loading or parsing errors
	// */
	// public int loadBeanDefinitions(String msg, String hbmString)
	// throws BeansException {
	// InputStream is = null;
	// try {
	// DocumentBuilderFactory factory = createDocumentBuilderFactory();
	// if (logger.isDebugEnabled()) {
	// logger.debug("Using JAXP implementation [" + factory + "]");
	// }
	// DocumentBuilder builder = createDocumentBuilder(factory);
	// is = new StringBufferInputStream(hbmString);
	// // is = resource.getInputStream();
	// Document doc = builder.parse(is);
	// return registerBeanDefinitions(doc, null);
	// } catch (ParserConfigurationException ex) {
	// throw new BeanDefinitionStoreException(
	// "Parser configuration exception parsing XML from " + msg, ex);
	// } catch (SAXParseException ex) {
	// throw new BeanDefinitionStoreException("Line " + ex.getLineNumber()
	// + " in XML document from " + msg + " is invalid", ex);
	// } catch (SAXException ex) {
	// throw new BeanDefinitionStoreException("XML document from " + msg
	// + " is invalid", ex);
	// } catch (IOException ex) {
	// throw new BeanDefinitionStoreException(
	// "IOException parsing XML document from " + msg, ex);
	// } finally {
	// if (is != null) {
	// try {
	// is.close();
	// } catch (IOException ex) {
	// logger.warn("Could not close InputStream", ex);
	// }
	// }
	// }
	// }
	// }
}
