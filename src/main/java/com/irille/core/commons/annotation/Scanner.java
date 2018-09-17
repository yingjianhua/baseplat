package com.irille.core.commons.annotation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import com.irille.core.commons.file.FileConsumer;
import com.irille.core.commons.file.Finder;

import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;

public class Scanner {
	private static final Logger log = Logger.getLogger(Scanner.class);
	private static Map<Class<?>, List<Class<?>>> TYPE_ANNOTATION_MAPS = new HashMap<>();
	
	public static void find() {
		Scanner.class.getResource("/").getPath();
		new Finder().find("", ".class").deal(dd->{});
	}
	public static void initAllBean() {
		String file = Scanner.class.getResource("/").getFile();
		String filepath = new File(file).getAbsolutePath();
		
		new Finder()
		.find(filepath, "\\.class$")
		.stream()
		.map(fileName->FileConsumer.toClassName(filepath+"\\", fileName))
		.forEach(className->{
			Class<?> clazz;
			try {
				clazz = Class.forName(className);
				if (BeanMain.class.isAssignableFrom(clazz))
					System.out.println("类初始加载：" + BeanBase.tb(clazz).getCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static <T extends Annotation> List<Method> findMethodByAnnotation(Class<T> annotationClass, Class<?> clazz) {
		log.debug("scanning annotation "+annotationClass.getName()+" in "+clazz.getName());
		return Stream.of(clazz.getMethods())
				.filter(method->{
					if(method.getAnnotation(annotationClass)!=null) {
						log.debug("finded "+method);
						return true;
					} else {
						return false;
					}
				})
				.sorted((m1,m2)->{
					Order order1 = m1.getAnnotation(Order.class);
					Order order2 = m2.getAnnotation(Order.class);
					return Integer.compare(order1==null?Integer.MAX_VALUE:order1.value(), order2==null?Integer.MAX_VALUE:order2.value());
				})
				.collect(Collectors.toList());
	}
	
	/**
	 * 从rootPackage包进行搜索带有annotationClass注解的类
	 * 
	 * <p>eg. <code>findClassByAnnotation(Controller.class, "com.irille.omt.action")</code>
	 * 
	 * @param annotationClass 注解类
	 * @param rootPackage 起始包位置
	 * @return
	 */
	public static <T extends Annotation> List<Class<?>> findClassByAnnotation(Class<T> annotationClass, String rootPackage) {
		return findClassByAnnotation(annotationClass, rootPackage, true);
	}
	/**
	 * 从从项目根目录开始搜索带有annotationClass注解的类
	 * 
	 * <p>eg. <code>findClassByAnnotation(Controller.class)</code>
	 * 
	 * @param annotationClass 注解类
	 * @return
	 */
	public static <T extends Annotation> List<Class<?>> findClassByAnnotation(Class<T> annotationClass) {
		return findClassByAnnotation(annotationClass, null, true);
	}
	public static <T extends Annotation> List<Class<?>> findClassByAnnotation(Class<T> annotationClass, String rootPackage, boolean noCache) {
		log.debug("scanning annotation "+annotationClass.getName()+" in "+rootPackage);
		if(noCache||TYPE_ANNOTATION_MAPS.containsKey(annotationClass)) {
			String classPath = new File(Scanner.class.getResource("/").getFile()).getAbsolutePath();
			String rootPath = classPath+File.separator+(rootPackage==null?"":rootPackage.replaceAll("\\.", "\\\\"));
			
			List<Class<?>> classes = new Finder()
			.find(rootPath, "\\.class$")
			.stream()
			.map(fileName->FileConsumer.toClassName(classPath+File.separator, fileName))
			.map(className -> {
				try {
					return Class.forName(className);
				} catch (ClassNotFoundException e) {
					return null;
				}
			})
			.filter(clazz->{
				if(clazz!=null&&clazz.getAnnotation(annotationClass)!=null) {
					log.debug("finded "+clazz.getName());
					return true;
				} else {
					return false;
				}
			})
			.sorted((c1,c2)->{
				Order order1 = c1.getAnnotation(Order.class);
				Order order2 = c2.getAnnotation(Order.class);
				return Integer.compare(order1==null?Integer.MAX_VALUE:order1.value(), order2==null?Integer.MAX_VALUE:order2.value());
			})
			.collect(Collectors.toList());
			TYPE_ANNOTATION_MAPS.put(annotationClass, classes);
		}
		return TYPE_ANNOTATION_MAPS.get(annotationClass);
		
	}
//	public static class InsInit extends IduOther<InsInit, UsrAccess> {
//		
//		private static Set<Class> Controllers;
//
//		@Override
//		public void before() {
//			super.before();
//			System.out.println("加载供应商功能权限-----------------");
//			setControllers(loadController(new HashSet<Class>()));
//		}
//
//		@Override
//		public void run() {
//			super.run();
//			String sql = "";
//			for(Class clazz: getControllers()) {
//				String className = clazz.getName();
//				String act = "";
//				//UsrSupplier
//				String controller = className.substring(clazz.getName().lastIndexOf(".")+2,clazz.getName().length()-"Action".length());
//				//供应商
//				String controllerName = ((Controller)clazz.getAnnotation(Controller.class)).name();
//				//usr
//				String module = className.split("\\.")[2];
//				//用户管理
//				String moduleName = ((Controller)clazz.getAnnotation(Controller.class)).module();
//				
//				System.out.println(controller);
//				String method;
//				String alias;
//				int sort;
//				for(Method m:clazz.getMethods()) {
//					RequestMapping rm = m.getAnnotation(RequestMapping.class);
//					if(rm != null) {
//						alias = rm.alias();
//						method = m.getName();
//						sort = rm.sort();
//						act = module+"_"+controller+"_"+method;
//						System.out.println(act+" "+moduleName+"_"+ controllerName+"_"+alias);
//						
//						UsrAccess access = UsrAccess.chk(UsrAccess.class, act);
//						sql += ",'"+act+"'";
//						if(access == null) {
//							access = new UsrAccess().init();
//							access.setModule(moduleName);
//							access.setController(controllerName);
//							access.setName(alias);
//							access.setSort(sort);
//							access.setPkey(act);
//							access.ins();
//						} else {
//							access.setModule(moduleName);
//							access.setController(controllerName);
//							access.setName(alias);
//							access.setSort(sort);
//							access.upd();
//						}
//					}
//				}
//				if(sql.length()>0) {
//					Bean.executeUpdate("delete from "+UsrAccess.TB.getCodeSqlTb()+" where "+UsrAccess.T.PKEY.getFld().getCodeSqlField()+" not in ("+sql.substring(1)+")");
//				}
//				
//			}
//		}
//		
// 		private static Set<Class> loadController(Set<Class> controllers) {
//			File rootFile = new File(Scanner.class.getResource("/").getFile());
//			loadController(rootFile, rootFile.getPath() + File.separator, controllers);
//			return controllers;
//		}
//
//		private static <T> void loadController(File rootFile, String parentDirectory, Set<Class> controllers) {
//			if (rootFile.isDirectory()) {
//				File[] files = rootFile.listFiles();
//				for (File file : files) {
//					loadController(file, parentDirectory, controllers);
//				}
//			} else {
//				String className = null;
//				try {
//					if (rootFile.getPath().indexOf(".class") != -1) {
//						//irille.sellerAction.usr.inf.IUsrSupplierAction
//						className = rootFile.getPath().replace(parentDirectory, "").replace(".class", "").replace(File.separator, ".");
//						if(className.contains("irille.sellerAction")) {
//							Class clazz = Class.forName(className);
//							if(ISellerAction.class.isAssignableFrom(clazz) && clazz.getAnnotation(Controller.class) != null) {
//								controllers.add(clazz);
//							}
//						}
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//			}
//		}
//
//		public static Set<Class> getControllers() {
//			return Controllers;
//		}
//
//		public static void setControllers(Set<Class> controllers) {
//			Controllers = controllers;
//		}
//		
//	}
}
