package com.irille.core.commons.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * 文件搜索器
 * @author yingjianhua
 *
 */
public class Finder {
	
	private List<String> files = new ArrayList<>();
	private boolean debug = false;
	
	public Finder debug() {
		this.debug = true;
		return this;
	}

	public Finder debug(boolean debug) {
		this.debug = debug;
		return this;
	}

	/**
	 * 在rootPath根路径下查找所有名字为filename的文件
	 * @param rootPath 根路径
	 * @param filename 文件名
	 * @return this
	 */
	public Finder find(String rootPath, String regex) {
		Pattern pattern = Pattern.compile(regex);
		File root = new File(rootPath);
		if(root.exists()||root.isDirectory())
			loop(root, pattern);
		return this;
	}
	
	private void loop(File path, Pattern pattern) {
		for(File file:path.listFiles()) {
			if(file.isDirectory()) {
				loop(file, pattern);
			} else {
				if(pattern.matcher(file.getAbsolutePath()).find()) {
					files.add(file.getAbsolutePath());
				}
			}
		};
	}
	/**
	 * 对搜索到的文件进行消费
	 * @param consumer 消费者
	 * @return this,可进行连续操作
	 */
	public Finder deal(java.util.function.Consumer<String> consumer) {
		Long d0 = System.currentTimeMillis();
		for(String file:files) {
			if(debug)
				System.out.println("处理文件:"+file);
			consumer.accept(file);
		}
		if(debug)
			System.out.println("总用时: "+(System.currentTimeMillis()-d0)+" ms");
		return this;
	}
	/**
	 * 对搜索到的文件进行消费
	 * @param consumer 消费者
	 * @return this,可进行连续操作
	 */
	public Stream<String> stream() {
		return files.stream();
	}
}
