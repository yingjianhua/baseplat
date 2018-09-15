/**
 * 
 */
package irille.pub.doc;

import irille.doc.Index;
import irille.pub.ClassTools;
import irille.pub.html.Node;

import java.util.Vector;

/**
 * @author whx
 * 
 */
public class DocClassList<T extends DocClassList> extends DocBase<T> {
	private Node _listNode;
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see irille.pub.dep.DocBase#init()
	 */
	@Override
	public T init() {
		Object pack=ClassTools.getStaticProerty(getClass(), "PACKAGE");
		setHtml("package", (String)ClassTools.getStaticProerty(pack.getClass(), "NAME"));
		super.init();
		_listNode = body();
		return (T) this;
	}

	public void setListNode(Node n) {
		_listNode = n;
	}

	public void addPackage(DocBase doc) {
		_listNode.add(
				getUrl(doc).setClazz(CSS.FRAME_TITLE_FONT).set("target",
						Index.FRAME_CLASS)).p();
		addToDocList(doc);
	}

	public void add(DocBase... docs) {
		for (DocBase doc : docs) {
			_listNode.add(
					getUrl(doc).setClazz(CSS.FRAME_ITEM_FONT).set("target",
							Index.FRAME_CLASS)).p();
			addToDocList(doc);
		}
	}
	/**
	 * @return the listNode
	 */
	public Node getListNode() {
		return _listNode;
	}
}
