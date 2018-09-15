/**
 * 
 */
package irille.doc;

import irille.doc.gs.GsListDoc;
import irille.doc.sys.SysListDoc;
import irille.pub.doc.DocBase;

/**
 * 包清单
 * 智慧经营系统的各包要在此类中指定，以便帮助文档产生总体引用架构。
 * @author whx
 * 
 */
public class PackageListDoc extends DocBase {
	public static PackageListDoc DOC = (PackageListDoc) new PackageListDoc().init();// 产生帮助文件的对象实例，在测试代码中会引用

	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#init()
	 */
	@Override
	public DocBase init() {
		setHtml("packageList", "包清单");
		super.init();

		addPackage(ListDoc.DOC);
		//@formatter:off	
		add(
				SysListDoc.DOC,
				GsListDoc.DOC
				);
		//@formatter:off	
		return this;
	}

	public void add(DocBase... docs) {
		for (DocBase doc : docs) {
			/*body().add(
					getUrl(doc).setClazz(CSS.FRAME_ITEM_FONT).set("target",
							Index.FRAME_CLASS_LIST)).p();*/
		  body().add(
					getUrl(doc).setClazz(CSS.FRAME_ITEM_FONT).set("target",
							Index.FRAME_CLASS_LIST));
			addToDocList(doc);
		}
	}

	public void addPackage(DocBase doc) {
		body().add(
				getUrl(doc).setClazz(CSS.FRAME_TITLE_FONT).set("target",
						Index.FRAME_CLASS_LIST)).p();
		addToDocList(doc);
	}

}
