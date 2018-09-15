/**
 * 
 */
package irille.pub.doc;

import irille.pub.html.Node;
import irille.pub.html.Nodes.Span;

/**
 * 帮助文档节点的接口
 * @author whx
 *
 * @param <NODE>
 */
public interface DocInf<NODE extends Node> {
	/**
	 * 取XHtml的节点，因为对象
	 * 
	 * @return
	 */
	public NODE getDocNode();

	public void outSourceCode(StringBuilder buf, String tab);
}
