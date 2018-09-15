package irille.pub.html;

import irille.pub.FileTools;
import junit.framework.Test;
import junit.framework.TestResult;

/**
 * 
 * 
 * @param <T>
 */
public class Xml<T extends Xml> implements Test {
	private XmlNode _root;

	public static void main(String[] args) {
		Long time=System.currentTimeMillis();
		System.out.println(time);
		System.err.println(Integer.MIN_VALUE);
//		Xhtml html = new Xhtml("AA");
//		System.err.println(html.html().getCode());
//		System.out.println(html.toString(2));
	}

	public Xml(XmlNode root) {
		_root=root;
	}

	/**
	 * @param arg0
	 * @see junit.framework.Test#run(junit.framework.TestResult)
	 */
	public void run(TestResult arg0) {
		_root.toString(2);
	}

	public final void testCase() {
	}
	public T add(Node... nodes) {
		_root.add(nodes);
		return (T)this;
	}
	/**
	 * 取产生文件的根目录，没有重构的话默认为""
	 * @return
	 */
	public String rootDir() {
		return "";
	}
	
	/**
	 * 输出文件
	 * 
	 * @param file
	 *          文件名
	 * @param step
	 *          缩进空格数
	 */
	public final void saveToFile(String file, int step) {
		System.out.println("产生文件:" +file);
		FileTools.writeStr(file, toString(step));
	}

	public String toString(int step) {
		return _root.toString(step);
	}

	@Override
	public String toString() {
		return _root.toString();
	}

	/**
	 * @return
	 * @see junit.framework.Test#countTestCases()
	 */
	public int countTestCases() {
		return 1;
	}

	public XmlNode getRoot() {
		return _root;
	}
}
