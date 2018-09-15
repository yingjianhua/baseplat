/**
 * 
 */
package irille.doc;

import irille.pub.doc.DocBase;

/**
 * 智慧经营系统的帮助主文档
 * 每个包目录中有以【"D"+包名】为文件名的包说明文件，可以对包进行说明，当在帮助主界面点击包名时，会在右侧内容区显示该文件的内容。
 * 每个包目录中还有以【"D"+包名+"List"】的文件，需要将该包所有的类帮助文件附件中对象中，以便引用，以应用方式运行该文件当重建该包下所有的说明文档。
 * 对包的引用要在【DPackageList】中进行定义。
 * @author whx
 *
 */
public class Index extends DocBase{
	public static String FRAME_PACKAGE_LIST="packageListFrame";
	public static String FRAME_CLASS_LIST="classListFrame";
	public static String FRAME_CLASS="classFrame";
	
	public static DocBase DOC=new Index().init();//产生帮助文件的对象实例，在测试代码中会引用
	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#init()
	 */
	@Override
	public DocBase init() {
		setHtml("index", OverviewSummaryDoc.NAME);
		super.init();
		body().setXmlLabel("FRAMESET").set("rows","20%,80%") 
		.Frame().setSrc("PackageListDoc.htm").setName(FRAME_PACKAGE_LIST);
		body().Frameset().set("cols","15%,85%")
		.Frame().setSrc("ListDoc.htm").setName(FRAME_CLASS_LIST)
		.Frame().setSrc("OverviewSummaryDoc.htm").setName(FRAME_CLASS);
		body().Noframes().H2("")._R().p().br();
		return this;
	}
}
