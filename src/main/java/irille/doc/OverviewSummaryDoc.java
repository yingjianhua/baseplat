/**
 * 
 */
package irille.doc;

import irille.pub.doc.DocPackage;

/**
 * 智慧经营系统的概述文档。
 * @author whx
 *
 */
public class OverviewSummaryDoc extends DocPackage{
	public static String NAME = "智慧经营系统V1.0";   //必须定义，在基类中要取用
	public static OverviewSummaryDoc DOC=(OverviewSummaryDoc)new OverviewSummaryDoc().init();//产生帮助文件的对象实例，在测试代码中会引用
	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#initMsg()
	 */
	@Override
	public void initMsg() {
		super.initMsg();
		DOC.p2("系统包括：总账子系统、目标计划子系统、进销存子系统等。");	
	}
}
