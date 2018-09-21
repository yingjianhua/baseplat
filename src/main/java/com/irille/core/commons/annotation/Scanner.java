package com.irille.core.commons.annotation;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.irille.core.commons.file.FileConsumer;
import com.irille.core.commons.file.Finder;

import irille.pub.bean.BeanBase;
import irille.pub.bean.BeanMain;

public class Scanner {
	
	private static final Logger logger = LoggerFactory.getLogger(Scanner.class);
	
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
					logger.info("类初始加载： {}", BeanBase.tb(clazz).getCode());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		});
	}
	
	public static <T extends Annotation> List<Method> findMethodByAnnotation(Class<T> annotationClass, Class<?> clazz) {
		logger.debug("scanning annotation {} in {}", annotationClass.getName(), clazz.getName());
		return Stream.of(clazz.getMethods())
				.filter(method->{
					if(method.getAnnotation(annotationClass)!=null) {
						logger.debug("finded {}", method);
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
		logger.debug("scanning annotation {} in {}", annotationClass.getName(), rootPackage);
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
					logger.debug("finded {}", clazz.getName());
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
	
}
