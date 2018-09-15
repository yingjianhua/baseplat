package irille.pub.html;

import irille.pub.Log;
import irille.pub.html.Nodes.Span;

/**
 * 数据节点为虚拟节点，为了留数据空间从数据推里取数据调用后显示
 * @author whx
 *
 * @param <THIS> 当前类
 */
public class NodeVirtual<THIS extends NodeVirtual> 
    extends Span<THIS>{
	private static final Log LOG= new Log(NodeVirtual.class);

	@Override
	protected void createXmlBody(
			IXmlBuf buf, TabStruct tab) {
		if (sizeNode() > 0)
			throw LOG.err("createXmlBody","[DataNode]的数据节点不能有子节点");
	}

	//本方法将本DataNode对象记录到View中，在与数据结合时再输出Html代码
	@Override
	public void createXml(IXmlBuf buf, TabStruct tab) {
	  buf.appendVirtualNode(this);
	}
}
