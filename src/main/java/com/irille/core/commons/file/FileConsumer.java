package com.irille.core.commons.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 文件处理器
 * @author yingjianhua
 *
 */
public class FileConsumer {
	
	/**
	 * 对文件中的匹配到的字符串进行替换处理
	 * @param fileName 文件名
	 * @param regex 匹配正则表达式
	 * @param translator 对匹配到的字符串进行处理后,返回替换内容
	 */
	public static void replaceAll(String fileName, String regex, Function<String, String> translator) {
		BufferedReader in = null;
		BufferedWriter out = null;
		Pattern p = Pattern.compile(regex);
		try{
			in = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), "UTF-8"));
			StringBuilder sb = new StringBuilder();
			String buffer;
			while((buffer = in.readLine())!=null) {
				sb.append(buffer+"\r\n");
			};
			String filecontent = sb.toString();
			Matcher m = p.matcher(filecontent);
			while(m.find()) {
				String origin = m.group(1);
				String target = translator.apply(origin);
				filecontent = filecontent.replaceAll(origin, target);
				System.out.println(""+origin+"  ==>  "+target);
			}
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(fileName)), "UTF-8"));
			out.write(filecontent);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in!=null)
					in.close();
				if(out!=null)
					out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static String toClassName(String classPath, String fileName) {
		if(fileName.indexOf(classPath)!=-1&&fileName.indexOf(".class")!=-1) {
			return fileName.substring(classPath.length(),fileName.indexOf(".class")).replaceAll("\\\\", ".");
		} else {
			return null;
		}
		
	}
	public static void toClassName(String classPath, String fileName, java.util.function.Consumer<String> a) {
		if(fileName.indexOf(classPath)!=-1&&fileName.indexOf(".class")!=-1) {
			a.accept(fileName.substring(classPath.length(),fileName.indexOf(".class")).replaceAll(File.separator, "."));
		}
	}
}
