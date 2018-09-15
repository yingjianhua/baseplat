/**
 * 
 */
package irille.doc;

import irille.pub.doc.DocBase;
import irille.pub.doc.DocClassList;
import irille.pub.doc.DocIrilleCss;
import irille.pub.doc.DocPackage;
import irille.pub.doc.DocTran;

import java.util.Vector;

/**
 * 本文件由系统根据【DPackageList】的内容自动产生，不需要维护。
 * 产生系统所有包及交易的引用目录。
 * @author whx
 * 
 */
public class ListDoc extends DocClassList {
	public static OverviewSummaryDoc PACKAGE = OverviewSummaryDoc.DOC; // 必须定义，在基类中要取用
	public static ListDoc DOC = (ListDoc) new ListDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用
	private static	PackageListDoc PACKAGE_LIST = PackageListDoc.DOC;

	/**
	 * 重新产生系统中所有的帮助文件！！！
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		//StartInitServlet.initBeanLoad();
		//===============
		DOC.saveAllToFile();
		new DocIrilleCss().countTestCases(); //产生CSS文件		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		addToDocList(PACKAGE);
		addToDocList(PACKAGE_LIST);
		addToDocList(Index.DOC);

		addPackage(PACKAGE);

		//建立所有包中类的清单
		for (DocBase pack : (Vector<DocBase>) PACKAGE_LIST.getDocList()) {
			if (pack.getClass().equals(getClass()))
				continue;
			pack.saveToFileBeforeDo();
			addToDocList(pack);
			for (DocBase doc : (Vector<DocBase>) pack.getDocList()) {
				if (doc instanceof DocPackage) {
					getListNode().br();
					addPackage(doc);
				} else if (doc instanceof DocTran) {
					add(doc);
				} else
					addToDocList(doc);
			}
		}
	}
}
