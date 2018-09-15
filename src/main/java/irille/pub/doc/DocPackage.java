/**
 * 
 */
package irille.pub.doc;

import irille.pub.ClassTools;
import irille.pub.doc.DocBase;
import irille.pub.html.Node;

/**
 * @author whx
 *
 */
public class DocPackage<T extends DocPackage> extends DocBase<T>{
	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#init()
	 */
	@Override
	public T init() {
		setHtml("package", (String)ClassTools.getStaticProerty(getClass(), "NAME"));
		super.init();
		initFoot();
		return (T)this;
	}
	/* (non-Javadoc)
	 * @see irille.pub.dep.DocBase#initHead(irille.pub.html.Node)
	 */
	@Override
	public void initHead(Node n) {
		n.H2().add(getAnchorName());
		super.initHead(n);
	}
}
